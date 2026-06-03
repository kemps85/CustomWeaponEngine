package org.example.system;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.UserManager;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.InheritanceNode;
import net.luckperms.api.node.types.PrefixNode;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.example.core.CustomWeaponEngine;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * /setrank <player> <prefix>
 *
 * Cách B — Tự động:
 * 1. Xóa toàn bộ group cũ của player (trừ "default" giữ lại).
 * 2. Thêm player vào group "custom_rank" (để TAB xếp đúng slot 7-8).
 * 3. Xóa prefix cũ ở cấp user, gán prefix mới với priority 100.
 * 4. Lưu tất cả trong 1 lần dùng modifyUser() — load + save nguyên tử.
 *
 * Ví dụ: /setrank kem &3&lHotaru
 * → kem vào group "custom_rank", prefix hiển thị "§3§lHotaru" trong TAB + chat
 */
public class SetRankCommand implements CommandExecutor, TabCompleter {

    // Group trong LuckPerms tương ứng slot layout của TAB
    private static final String CUSTOM_RANK_GROUP = "custom_rank";

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("cwe.admin")) {
            sender.sendMessage("§c[CWE] Bạn không có quyền dùng lệnh này!");
            return true;
        }

        if (args.length < 2) {
            sendHelp(sender);
            return true;
        }

        // Lấy tên player
        String targetName = args[0];

        // Gom toàn bộ arg còn lại thành prefix (hỗ trợ khoảng trắng)
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            if (i > 1) sb.append(" ");
            sb.append(args[i]);
        }
        // Chuyển & → § (mã màu Minecraft)
        String coloredPrefix = sb.toString().replace("&", "§");

        // Lấy LuckPerms qua Bukkit ServiceManager
        LuckPerms lp;
        try {
            lp = Bukkit.getServicesManager()
                .getRegistration(LuckPerms.class)
                .getProvider();
        } catch (Exception e) {
            sender.sendMessage("§c✖ LuckPerms không khả dụng! Đảm bảo LuckPerms đang chạy.");
            return true;
        }

        UserManager um = lp.getUserManager();

        // Tìm UUID — online trước, offline sau
        Player online = Bukkit.getPlayer(targetName);
        if (online != null) {
            applyRankAndPrefix(um, online.getUniqueId(), online.getName(), coloredPrefix, sender);
        } else {
            sender.sendMessage("§7[CWE] Đang tìm kiếm §e" + targetName + "§7 trong database...");
            um.lookupUniqueId(targetName).thenAcceptAsync(uuid -> {
                if (uuid == null) {
                    Bukkit.getScheduler().runTask(CustomWeaponEngine.getInstance(), () ->
                        sender.sendMessage("§c✖ Không tìm thấy người chơi: §e" + targetName + "§c! Họ chưa từng vào server?"));
                    return;
                }
                applyRankAndPrefix(um, uuid, targetName, coloredPrefix, sender);
            });
        }

        return true;
    }

    /**
     * Áp dụng group custom_rank + prefix mới trong 1 atomic operation
     * dùng modifyUser() — tốt hơn loadUser() + saveUser() riêng lẻ.
     */
    private void applyRankAndPrefix(UserManager um, UUID uuid, String playerName,
                                     String prefix, CommandSender sender) {
        um.modifyUser(uuid, user -> {
            // ── BƯỚC 1: Xóa toàn bộ InheritanceNode (group) hiện có ──
            // Chỉ giữ lại "default" để player không bị mất quyền cơ bản
            user.data().clear(node ->
                NodeType.INHERITANCE.matches(node) &&
                !((InheritanceNode) node).getGroupName().equalsIgnoreCase("default")
            );

            // ── BƯỚC 2: Thêm vào group "custom_rank" ──
            // TAB config: custom_rank → slots: ['7-8'] (xếp đúng vị trí tablist)
            user.data().add(InheritanceNode.builder(CUSTOM_RANK_GROUP).build());

            // ── BƯỚC 3: Xóa prefix cũ ở cấp user ──
            user.data().clear(node -> NodeType.PREFIX.matches(node));

            // ── BƯỚC 4: Gán prefix mới, priority 100 ──
            // Priority 100 > prefix kế thừa từ group (thường priority 10-50)
            user.data().add(PrefixNode.builder(prefix, 100).build());

        }).thenRunAsync(() -> {
            // Callback sau khi save thành công
            Bukkit.getScheduler().runTask(CustomWeaponEngine.getInstance(), () -> {
                sender.sendMessage("§a§l✔ SETRANK THÀNH CÔNG!");
                sender.sendMessage("§7 ├ Player: §e" + playerName);
                sender.sendMessage("§7 ├ Group: §b" + CUSTOM_RANK_GROUP + " §7(TAB slot 7-8)");
                sender.sendMessage("§7 └ Prefix: " + prefix);
                Bukkit.broadcastMessage("§6[Admin] §fPlayer §e" + playerName
                    + " §fnhận danh hiệu mới: " + prefix);

                // Nếu player đang online → kick & hint reload (TAB tự cập nhật)
                Player p = Bukkit.getPlayer(playerName);
                if (p != null) {
                    p.sendMessage("§a✨ Prefix của bạn vừa được Admin cập nhật: " + prefix);
                }
            });
        }).exceptionally(ex -> {
            Bukkit.getScheduler().runTask(CustomWeaponEngine.getInstance(), () -> {
                sender.sendMessage("§c✖ Lỗi khi lưu dữ liệu LuckPerms: " + ex.getMessage());
                ex.printStackTrace();
            });
            return null;
        });
    }

    private void sendHelp(CommandSender sender) {
        sender.sendMessage("§6§l══════ LỆNH SETRANK (Cách B) ══════");
        sender.sendMessage("§e/setrank §b<player> §d<prefix>");
        sender.sendMessage("");
        sender.sendMessage("§7Chức năng:");
        sender.sendMessage("§7 ├ Đưa player vào group §bcustom_rank §7(TAB slot 7-8)");
        sender.sendMessage("§7 └ Gán prefix tùy chỉnh hiển thị trong chat & tablist");
        sender.sendMessage("");
        sender.sendMessage("§7Ví dụ:");
        sender.sendMessage("§f  /setrank kem &3&lHotaru &r");
        sender.sendMessage("§f  /setrank Steve &6&l[VIP] &r&6");
        sender.sendMessage("§f  /setrank Alex &c&l[MVP++] &r&c");
        sender.sendMessage("");
        sender.sendMessage("§7Dùng §f&§7 thay vì §f§ §7(mã màu Minecraft)");
        sender.sendMessage("§6§l════════════════════════════════");
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> completions = new ArrayList<>();
        if (!sender.hasPermission("cwe.admin")) return completions;

        if (args.length == 1) {
            // Arg 1 → gợi ý tên player đang online
            String input = args[0].toLowerCase();
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p.getName().toLowerCase().startsWith(input)) {
                    completions.add(p.getName());
                }
            }
        } else if (args.length == 2) {
            // Arg 2 → gợi ý preset prefix mẫu (phù hợp server sinh tồn)
            String input = args[1].toLowerCase();
            List<String> presets = new ArrayList<>();
            presets.add("&3&lHotaru &r");
            presets.add("&a&l[Nông Dân] &r&a");
            presets.add("&b&l[Ngư Phủ] &r&b");
            presets.add("&6&l[VIP] &r&6");
            presets.add("&6&l[VIP+] &r&6");
            presets.add("&d&l[MVP] &r&d");
            presets.add("&d&l[MVP+] &r&d");
            presets.add("&c&l[MVP++] &r&c");
            presets.add("&e&l[Builder] &r&e");
            presets.add("&7&l[Khách] &r&7");
            for (String p : presets) {
                if (p.toLowerCase().contains(input)) {
                    completions.add(p);
                }
            }
        }

        return completions;
    }
}

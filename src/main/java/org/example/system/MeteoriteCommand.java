package org.example.system;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.example.core.CustomWeaponEngine;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MeteoriteCommand implements CommandExecutor, TabCompleter {

    private static final List<String> TYPES = Arrays.asList("FIRE", "ICE", "VOID");

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("cwe.admin")) {
            sender.sendMessage("§c[CWE] Bạn không có quyền dùng lệnh này!");
            return true;
        }

        if (args.length == 0) {
            sendHelp(sender);
            return true;
        }

        String sub = args[0].toLowerCase();

        switch (sub) {
            case "start": {
                if (args.length < 2) {
                    sender.sendMessage("§c✖ Thiếu tham số! §fCú pháp: §e/meteorite start <loại>");
                    sender.sendMessage("§7Loại có thể dùng: §cFIRE §b| §9ICE §b| §5VOID");
                    return true;
                }
                String typeArg = args[1].toUpperCase();
                if (!TYPES.contains(typeArg)) {
                    sender.sendMessage("§c✖ Loại không hợp lệ! §7Chọn một trong: §cFIRE§7, §bICE§7, §5VOID");
                    return true;
                }
                if (MeteoriteManager.isEventActive) {
                    sender.sendMessage("§c✖ Đã có sự kiện Thiên Thạch đang diễn ra! Kết thúc sự kiện trước.");
                    return true;
                }
                CustomWeaponEngine.meteoriteManager.startEvent(typeArg);
                sender.sendMessage("§a✔ Đã kích hoạt sự kiện Thiên Thạch §e" + typeArg + "§a!");
                break;
            }
            case "stop": {
                if (!MeteoriteManager.isEventActive) {
                    sender.sendMessage("§c✖ Hiện tại không có sự kiện Thiên Thạch nào đang diễn ra.");
                    return true;
                }
                MeteoriteManager.isEventActive = false;
                Bukkit.broadcastMessage("§c§l[SỰ KIỆN] §fSự kiện Thiên Thạch đã bị §cAdmin hủy bỏ§f.");
                sender.sendMessage("§a✔ Đã dừng sự kiện Thiên Thạch!");
                break;
            }
            case "status": {
                if (MeteoriteManager.isEventActive) {
                    sender.sendMessage("§a● Trạng thái: §lĐANG DIỄN RA");
                    sender.sendMessage("§7Loại phần tử: §e" + (MeteoriteManager.activeElementType != null ? MeteoriteManager.activeElementType : "N/A"));
                    if (MeteoriteManager.activeMeteoriteLocation != null) {
                        sender.sendMessage("§7Tọa độ: §eX=" + MeteoriteManager.activeMeteoriteLocation.getBlockX()
                            + " Z=" + MeteoriteManager.activeMeteoriteLocation.getBlockZ());
                    }
                } else {
                    sender.sendMessage("§c● Trạng thái: §fKhông có sự kiện nào đang diễn ra.");
                }
                break;
            }
            default: {
                sendHelp(sender);
                break;
            }
        }
        return true;
    }

    private void sendHelp(CommandSender sender) {
        sender.sendMessage("§6§l══════ LỆNH METEORITE ══════");
        sender.sendMessage("§e/meteorite start <loại> §7- Kích hoạt sự kiện Thiên Thạch");
        sender.sendMessage("  §7Loại hợp lệ: §cFIRE §f(Mỏ vàng) §b| §bICE §f(Mỏ kim cương) §b| §5VOID §f(Mỏ Obsidian)");
        sender.sendMessage("§e/meteorite stop §7- Hủy sự kiện đang diễn ra (khẩn cấp)");
        sender.sendMessage("§e/meteorite status §7- Kiểm tra trạng thái sự kiện hiện tại");
        sender.sendMessage("§6§l══════════════════════════");
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> completions = new ArrayList<>();

        if (!sender.hasPermission("cwe.admin")) return completions;

        if (args.length == 1) {
            List<String> subs = Arrays.asList("start", "stop", "status");
            for (String s : subs) {
                if (s.startsWith(args[0].toLowerCase())) completions.add(s);
            }
        } else if (args.length == 2 && args[0].equalsIgnoreCase("start")) {
            for (String type : TYPES) {
                if (type.startsWith(args[1].toUpperCase())) completions.add(type);
            }
        }

        return completions;
    }
}

package org.example.system;

import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;

import java.util.List;

public class ChangelogManager implements Listener, CommandExecutor {

    private final JavaPlugin plugin;
    private final NamespacedKey versionKey;

    public ChangelogManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.versionKey = new NamespacedKey(plugin, "cwe_read_changelog_version");
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        int currentVersion = plugin.getConfig().getInt("changelog.version", 1);
        
        PersistentDataContainer pdc = player.getPersistentDataContainer();
        int readVersion = pdc.getOrDefault(versionKey, PersistentDataType.INTEGER, 0);

        if (readVersion < currentVersion) {
            // Delay sending announcement to ensure player has fully loaded
            plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                if (player.isOnline()) {
                    sendJoinAnnouncement(player, currentVersion);
                }
            }, 40L); // 2 seconds delay (40 ticks)
        }
    }

    private void sendJoinAnnouncement(Player player, int version) {
        player.sendMessage("");
        player.sendMessage(" §e§l✦ BẢN CẬP NHẬT HỆ THỐNG MỚI ✦");
        player.sendMessage(" §7Máy chủ vừa hoàn tất nâng cấp nhiều tính năng hấp dẫn!");
        
        // Tạo nút click tương tác xịn
        TextComponent message = new TextComponent(" §6§l[NHẤP VÀO ĐÂY] §eađể xem chi tiết cập nhật hoặc gõ §b/cweupdate§e!");
        message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/cweupdate"));
        message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("§aClick để xem thông tin bản cập nhật!")));
        
        player.spigot().sendMessage(message);
        player.sendMessage("");
        
        // Đánh dấu người chơi đã nhận thông báo (tránh làm phiền lần sau)
        PersistentDataContainer pdc = player.getPersistentDataContainer();
        pdc.set(versionKey, PersistentDataType.INTEGER, version);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (command.getName().equalsIgnoreCase("cweupdate")) {
            List<String> lines = plugin.getConfig().getStringList("changelog.lines");
            if (lines.isEmpty()) {
                sender.sendMessage("§c[CWE] Hiện tại chưa có thông tin cập nhật mới nào.");
                return true;
            }

            sender.sendMessage("");
            for (String line : lines) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', line));
            }
            sender.sendMessage("");

            // Đánh dấu đã đọc bản mới nhất khi tự gõ lệnh
            if (sender instanceof Player) {
                Player player = (Player) sender;
                int currentVersion = plugin.getConfig().getInt("changelog.version", 1);
                PersistentDataContainer pdc = player.getPersistentDataContainer();
                pdc.set(versionKey, PersistentDataType.INTEGER, currentVersion);
            }
            return true;
        }
        return false;
    }
}

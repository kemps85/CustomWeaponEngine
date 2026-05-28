package org.example.system;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.net.URISyntaxException;

public class UpdateNotifier implements Listener {

    private final JavaPlugin plugin;
    private boolean justUpdated = false;
    private String updateTimeStr = "";

    public UpdateNotifier(JavaPlugin plugin) {
        this.plugin = plugin;
        checkUpdate();
    }

    private void checkUpdate() {
        try {
            File pluginFile = new File(plugin.getClass().getProtectionDomain().getCodeSource().getLocation().toURI());
            long currentModTime = pluginFile.lastModified();
            long lastModTime = plugin.getConfig().getLong("last_update_time", 0L);

            if (currentModTime > lastModTime) {
                plugin.getConfig().set("last_update_time", currentModTime);
                plugin.saveConfig();
                
                justUpdated = true;
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                updateTimeStr = sdf.format(new java.util.Date(currentModTime));
                
                String msg = "§a[CWE] CustomWeaponEngine đã được update code thành công!\n§7Thời gian nạp: " + updateTimeStr;
                
                // Gửi cho admin đang online
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (p.hasPermission("cwe.admin") || p.isOp()) {
                        p.sendMessage(msg);
                    }
                }
                plugin.getLogger().info("Plugin code updated. Notifying admins.");
            }
        } catch (URISyntaxException e) {
            plugin.getLogger().warning("Could not check jar modification time for updates.");
        }
    }

    @EventHandler
    public void onAdminJoin(PlayerJoinEvent event) {
        if (!justUpdated) return;
        Player p = event.getPlayer();
        if (p.hasPermission("cwe.admin") || p.isOp()) {
            p.sendMessage("§a[CWE] CustomWeaponEngine đã được update code thành công gần đây!\n§7Thời gian nạp: " + updateTimeStr);
        }
    }
}

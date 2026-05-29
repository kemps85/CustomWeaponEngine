package org.example.system;

import eu.decentsoftware.holograms.api.DHAPI;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.example.core.CustomWeaponEngine;

import java.util.ArrayList;
import java.util.List;

public class HologramManager {

    private final CustomWeaponEngine plugin;
    private final String CMD_HOLO = "cwe_command_holo";
    private final String GUIDE_HOLO = "cwe_guide_holo";

    public HologramManager(CustomWeaponEngine plugin) {
        this.plugin = plugin;
    }

    public void updateHolograms() {
        // Kiểm tra xem server có cài DecentHolograms không
        if (Bukkit.getPluginManager().getPlugin("DecentHolograms") == null) {
            return;
        }

        updateSingleHologram(CMD_HOLO, "command");
        updateSingleHologram(GUIDE_HOLO, "guide");
    }

    private void updateSingleHologram(String name, String type) {
        Location loc = getSavedLocation(type);
        if (loc == null) return;

        List<String> lines = buildHologramLines(type);

        Hologram holo = DHAPI.getHologram(name);
        if (holo == null) {
            DHAPI.createHologram(name, loc, lines);
        } else {
            DHAPI.moveHologram(holo, loc);
            DHAPI.setHologramLines(holo, lines);
        }
    }

    public void setLocation(String type, Location loc) {
        plugin.getConfig().set("hologram." + type + ".world", loc.getWorld().getName());
        plugin.getConfig().set("hologram." + type + ".x", loc.getX());
        plugin.getConfig().set("hologram." + type + ".y", loc.getY());
        plugin.getConfig().set("hologram." + type + ".z", loc.getZ());
        plugin.saveConfig();

        updateHolograms();
    }

    public void removeHologram(String type) {
        // Xóa tọa độ khỏi config
        plugin.getConfig().set("hologram." + type, null);
        plugin.saveConfig();

        String name = type.equals("command") ? CMD_HOLO : GUIDE_HOLO;
        Hologram holo = DHAPI.getHologram(name);
        if (holo != null) {
            holo.delete();
        }
    }

    private Location getSavedLocation(String type) {
        if (!plugin.getConfig().contains("hologram." + type + ".world")) return null;
        
        String wName = plugin.getConfig().getString("hologram." + type + ".world");
        World w = Bukkit.getWorld(wName);
        if (w == null) return null;

        double x = plugin.getConfig().getDouble("hologram." + type + ".x");
        double y = plugin.getConfig().getDouble("hologram." + type + ".y");
        double z = plugin.getConfig().getDouble("hologram." + type + ".z");

        return new Location(w, x, y, z);
    }

    private List<String> buildHologramLines(String type) {
        List<String> lines = new ArrayList<>();
        
        if (type.equals("command")) {
            // Tựa đề (Header)
            lines.add(" ");
            lines.add("#ICON: BOOK");
            lines.add(" ");
            lines.add("&#ffaa00&lC&#ffb111&lW&#ffb721&lE &#ffbe32&lS&#ffc543&lY&#ffcc53&lS&#ffd264&lT&#ffd975&lE&#ffe085&lM &#ffe796&lI&#ffeea7&lN&#fff4b7&lF&#fffbc8&lO");
            lines.add("§7Danh sách lệnh dành cho người chơi");
            lines.add(" ");

            List<String> cmds = plugin.getConfig().getStringList("player-commands");
            for (String cmd : cmds) {
                if (cmd.contains(" - ")) {
                    String[] parts = cmd.split(" - ", 2);
                    lines.add("§b" + parts[0] + " §8» §f" + parts[1]);
                } else {
                    lines.add("§f" + cmd);
                }
            }
        } else if (type.equals("guide")) {
            lines.add(" ");
            lines.add("#ICON: ENCHANTED_BOOK");
            lines.add(" ");
            List<String> guide = plugin.getConfig().getStringList("newbie-guide");
            for (String g : guide) {
                lines.add(org.bukkit.ChatColor.translateAlternateColorCodes('&', g));
            }
        }

        lines.add(" ");
        lines.add("§8Được vận hành bởi CustomWeaponEngine v4.7");

        return lines;
    }
}

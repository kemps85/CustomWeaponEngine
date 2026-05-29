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
    private final String HOLO_NAME = "cwe_command_holo";

    public HologramManager(CustomWeaponEngine plugin) {
        this.plugin = plugin;
    }

    public void updateHologram() {
        // Kiểm tra xem server có cài DecentHolograms không
        if (Bukkit.getPluginManager().getPlugin("DecentHolograms") == null) {
            return;
        }

        Location loc = getSavedLocation();
        if (loc == null) {
            return; // Chưa set vị trí
        }

        List<String> lines = buildHologramLines();

        Hologram holo = DHAPI.getHologram(HOLO_NAME);
        if (holo == null) {
            DHAPI.createHologram(HOLO_NAME, loc, lines);
        } else {
            DHAPI.moveHologram(holo, loc);
            DHAPI.setHologramLines(holo, lines);
        }
    }

    public void setLocation(Location loc) {
        plugin.getConfig().set("hologram.world", loc.getWorld().getName());
        plugin.getConfig().set("hologram.x", loc.getX());
        plugin.getConfig().set("hologram.y", loc.getY());
        plugin.getConfig().set("hologram.z", loc.getZ());
        plugin.saveConfig();

        updateHologram();
    }

    private Location getSavedLocation() {
        if (!plugin.getConfig().contains("hologram.world")) return null;
        
        String wName = plugin.getConfig().getString("hologram.world");
        World w = Bukkit.getWorld(wName);
        if (w == null) return null;

        double x = plugin.getConfig().getDouble("hologram.x");
        double y = plugin.getConfig().getDouble("hologram.y");
        double z = plugin.getConfig().getDouble("hologram.z");

        return new Location(w, x, y, z);
    }

    private List<String> buildHologramLines() {
        List<String> lines = new ArrayList<>();
        
        // Tựa đề (Header)
        lines.add(" ");
        lines.add("#ICON: BOOK");
        lines.add(" ");
        lines.add("&#ffaa00&lC&#ffb111&lW&#ffb721&lE &#ffbe32&lS&#ffc543&lY&#ffcc53&lS&#ffd264&lT&#ffd975&lE&#ffe085&lM &#ffe796&lI&#ffeea7&lN&#fff4b7&lF&#fffbc8&lO");
        lines.add("§7Danh sách lệnh dành cho người chơi");
        lines.add(" ");

        // Lấy danh sách lệnh từ config
        List<String> cmds = plugin.getConfig().getStringList("player-commands");
        for (String cmd : cmds) {
            // Định dạng: "/bank - Mở giao diện..."
            // Mình sẽ tách ra để tô màu cho đẹp
            if (cmd.contains(" - ")) {
                String[] parts = cmd.split(" - ", 2);
                lines.add("§b" + parts[0] + " §8» §f" + parts[1]);
            } else {
                lines.add("§f" + cmd);
            }
        }

        lines.add(" ");
        lines.add("§8Được vận hành bởi CustomWeaponEngine v4.7");

        return lines;
    }
}

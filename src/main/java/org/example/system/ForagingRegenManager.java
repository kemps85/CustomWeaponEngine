package org.example.system;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

public class ForagingRegenManager implements Listener {

    private final JavaPlugin plugin;
    private final File dataFile;
    private FileConfiguration dataConfig;

    // List of active regenerating blocks
    private final List<RegenTask> activeTasks = new ArrayList<>();
    
    // Configurable list of WorldGuard regions where foraging is enabled
    private List<String> allowedRegions = new ArrayList<>();
    private long regenDelayMs = 120000; // 2 minutes by default

    private static class RegenTask {
        UUID id;
        Location location;
        BlockData blockData;
        long restoreTimeMs;

        RegenTask(UUID id, Location location, BlockData blockData, long restoreTimeMs) {
            this.id = id;
            this.location = location;
            this.blockData = blockData;
            this.restoreTimeMs = restoreTimeMs;
        }
    }

    public ForagingRegenManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.dataFile = new File(plugin.getDataFolder(), "foraging_regens.yml");
        
        // Load config values from main config if present
        if (plugin.getConfig().contains("foraging.allowed_regions")) {
            allowedRegions = plugin.getConfig().getStringList("foraging.allowed_regions");
        } else {
            allowedRegions.add("foraging_zone");
            plugin.getConfig().set("foraging.allowed_regions", allowedRegions);
            plugin.getConfig().set("foraging.regen_delay_seconds", 120);
            plugin.saveConfig();
        }
        
        if (plugin.getConfig().contains("foraging.regen_delay_seconds")) {
            regenDelayMs = plugin.getConfig().getLong("foraging.regen_delay_seconds") * 1000L;
        }

        loadRegenTasks();
        startRegenTimer();
    }

    private void loadRegenTasks() {
        if (!dataFile.exists()) {
            try { dataFile.createNewFile(); } catch (IOException ignored) {}
        }
        dataConfig = YamlConfiguration.loadConfiguration(dataFile);
        activeTasks.clear();

        ConfigurationSection section = dataConfig.getConfigurationSection("regens");
        if (section != null) {
            for (String key : section.getKeys(false)) {
                try {
                    String worldName = section.getString(key + ".world");
                    World world = Bukkit.getWorld(worldName);
                    if (world == null) continue;
                    double x = section.getDouble(key + ".x");
                    double y = section.getDouble(key + ".y");
                    double z = section.getDouble(key + ".z");
                    String blockDataStr = section.getString(key + ".data");
                    long restoreTime = section.getLong(key + ".time");

                    Location loc = new Location(world, x, y, z);
                    BlockData blockData = Bukkit.createBlockData(blockDataStr);

                    activeTasks.add(new RegenTask(UUID.fromString(key), loc, blockData, restoreTime));
                } catch (Exception e) {
                    plugin.getLogger().log(Level.WARNING, "Failed to load regen task " + key, e);
                }
            }
        }
    }

    public void saveRegenTasks() {
        dataConfig.set("regens", null); // clear
        for (RegenTask task : activeTasks) {
            String path = "regens." + task.id.toString();
            dataConfig.set(path + ".world", task.location.getWorld().getName());
            dataConfig.set(path + ".x", task.location.getX());
            dataConfig.set(path + ".y", task.location.getY());
            dataConfig.set(path + ".z", task.location.getZ());
            dataConfig.set(path + ".data", task.blockData.getAsString());
            dataConfig.set(path + ".time", task.restoreTimeMs);
        }
        try { dataConfig.save(dataFile); } catch (IOException ignored) {}
    }

    private void startRegenTimer() {
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            long now = System.currentTimeMillis();
            boolean changed = false;
            
            Iterator<RegenTask> it = activeTasks.iterator();
            while (it.hasNext()) {
                RegenTask task = it.next();
                if (now >= task.restoreTimeMs) {
                    // Restore block
                    Block block = task.location.getBlock();
                    block.setBlockData(task.blockData);
                    it.remove();
                    changed = true;
                }
            }
            
            if (changed) {
                saveRegenTasks();
            }
        }, 20L, 20L); // Check every second
    }

    // Runs on plugin disable to save state
    public void cleanupOnDisable() {
        saveRegenTasks();
    }

    private boolean isForagingRegion(Location loc) {
        if (allowedRegions.isEmpty()) return false;
        if (Bukkit.getPluginManager().getPlugin("WorldGuard") == null) return false;

        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager regions = container.get(BukkitAdapter.adapt(loc.getWorld()));
        if (regions == null) return false;

        ApplicableRegionSet set = regions.getApplicableRegions(BukkitAdapter.asBlockVector(loc));
        for (ProtectedRegion region : set) {
            if (allowedRegions.contains(region.getId())) {
                return true;
            }
        }
        return false;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        if (isLog(block.getType())) {
            if (isForagingRegion(block.getLocation())) {
                // Register regen task
                UUID taskId = UUID.randomUUID();
                long restoreTime = System.currentTimeMillis() + regenDelayMs;
                activeTasks.add(new RegenTask(taskId, block.getLocation(), block.getBlockData().clone(), restoreTime));
                saveRegenTasks();
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onLeavesDecay(LeavesDecayEvent event) {
        if (isForagingRegion(event.getBlock().getLocation())) {
            event.setCancelled(true);
        }
    }

    private boolean isLog(Material material) {
        String name = material.name();
        return name.endsWith("_LOG") || name.endsWith("_WOOD") || name.endsWith("_STEM") || name.endsWith("_HYPHAE");
    }
}

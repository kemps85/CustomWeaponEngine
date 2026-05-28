package org.example.system;

import java.util.Arrays;

import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
import org.bukkit.Bukkit;
import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
import org.bukkit.Location;
import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
import org.bukkit.Material;
import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
import org.bukkit.World;
import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
import org.bukkit.command.Command;
import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
import org.bukkit.command.CommandExecutor;
import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
import org.bukkit.command.CommandSender;
import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
import org.bukkit.configuration.ConfigurationSection;
import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
import org.bukkit.configuration.file.YamlConfiguration;
import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
import org.bukkit.entity.Entity;
import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
import org.bukkit.entity.LivingEntity;
import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
import org.bukkit.entity.Player;
import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
import org.bukkit.plugin.java.JavaPlugin;

import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
import java.io.File;
import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
import java.io.IOException;
import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
import java.util.ArrayList;
import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
import java.util.List;
import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
import java.util.Random;
import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
import java.util.UUID;

import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
public class OreVeinManager implements CommandExecutor, org.bukkit.command.TabCompleter {
    private final JavaPlugin plugin;
    private final List<OreVein> veins = new ArrayList<>();
    private final File dataFile;
    private FileConfiguration dataConfig;
    private final Random random = new Random();

    public static class OreVein {
        public UUID id;
        public Location center;
        public String oreType;
        public int radius = 20;
        
        // Count of missing guardians that are currently on cooldown
        public int guardiansOnCooldown = 0;

        public OreVein(UUID id, Location center, String oreType) {
            this.id = id;
            this.center = center;
            this.oreType = oreType;
        }
    }

    public OreVeinManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.dataFile = new File(plugin.getDataFolder(), "oreveins.yml");
        loadVeins();
        startAmbientSpawner();
    }

    private void loadVeins() {
        if (!dataFile.exists()) {
            try { dataFile.createNewFile(); } catch (IOException ignored) {}
        }
        dataConfig = YamlConfiguration.loadConfiguration(dataFile);
        veins.clear();

        ConfigurationSection section = dataConfig.getConfigurationSection("veins");
        if (section != null) {
            for (String key : section.getKeys(false)) {
                String worldName = section.getString(key + ".world");
                World world = Bukkit.getWorld(worldName);
                if (world == null) continue;
                double x = section.getDouble(key + ".x");
                double y = section.getDouble(key + ".y");
                double z = section.getDouble(key + ".z");
                String type = section.getString(key + ".type");

                veins.add(new OreVein(UUID.fromString(key), new Location(world, x, y, z), type));
            }
        }
    }

    private void saveVeins() {
        dataConfig.set("veins", null); // clear
        for (OreVein vein : veins) {
            String path = "veins." + vein.id.toString();
            dataConfig.set(path + ".world", vein.center.getWorld().getName());
            dataConfig.set(path + ".x", vein.center.getX());
            dataConfig.set(path + ".y", vein.center.getY());
            dataConfig.set(path + ".z", vein.center.getZ());
            dataConfig.set(path + ".type", vein.oreType);
        }
        try { dataConfig.save(dataFile); } catch (IOException ignored) {}
    }

    public OreVein getVeinAt(Location loc) {
        for (OreVein vein : veins) {
            if (vein.center.getWorld().equals(loc.getWorld())) {
                if (vein.center.distanceSquared(loc) <= vein.radius * vein.radius) {
                    return vein;
                }
            }
        }
        return null;
    }

    // This method is called from BazaarMobDropListener when a player kills a guardian inside a vein
    public void notifyGuardianDied(OreVein vein) {
        if (vein != null) {
            vein.guardiansOnCooldown++;
            // Start a 90s cooldown to spawn a replacement
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                if (vein.guardiansOnCooldown > 0) {
                    vein.guardiansOnCooldown--;
                }
            }, 90 * 20L); // 90 seconds
        }
    }

    private void startAmbientSpawner() {
        // Runs every 10 seconds to check and spawn up to the minimum cap (2)
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            for (Player p : Bukkit.getOnlinePlayers()) {
                OreVein v = getVeinAt(p.getLocation());
                if (v != null) {
                    if (!p.hasMetadata("cwe_vein_warned") || System.currentTimeMillis() - p.getMetadata("cwe_vein_warned").get(0).asLong() > 10000) {
                        p.sendTitle("§c⚠ NGUY HIỂM ⚠", "§7Quái vật đang rình rập quanh đây!", 10, 70, 20);
                        p.setMetadata("cwe_vein_warned", new org.bukkit.metadata.FixedMetadataValue(plugin, System.currentTimeMillis()));
                    }
                }
            }
            for (OreVein vein : veins) {
                if (vein.center.getWorld() == null) continue;
                
                // Count current active guardians
                int activeCount = countActiveGuardians(vein);
                
                // The minimum target is 2. The max cap is 5.
                // We should maintain at least 2 guardians actively roaming if cooldown allows it.
                // Actually, if we have active + cooldown < 2, it means we need to instantly spawn one to maintain minimum,
                // OR we just wait for cooldowns. The prompt says: "hệ thống phải kích hoạt trình đếm ngược hồi sinh... sau đó tự động spawn bù".
                // If active + cooldown < 2 (e.g. system just started), spawn them up to 2 immediately.
                int needed = 2 - (activeCount + vein.guardiansOnCooldown);
                
                for (int i = 0; i < needed; i++) {
                    spawnGuardianInVein(vein);
                    activeCount++;
                }

                // If cooldown has finished, activeCount might be < 5 and guardiansOnCooldown might be 0, but we only ambiently spawn up to whatever is required.
                // Wait, if a cooldown FINISHES, how does the guardian spawn? 
                // We can just say: we want to maintain the number of guardians to be exactly what it was. 
                // But let's simplify: every 10 seconds, if activeCount < 2, and we are not waiting for cooldown, we spawn.
                // To properly implement "spawn bù sau 90s", we can just do:
            }
        }, 200L, 200L);

        // A second task to handle the actual respawns that come off cooldown to maintain the 2-5 population.
        // If a guardian dies, it goes on cooldown. After 90s, we should spawn it back.
        // Actually, checking every 10 seconds:
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            for (Player p : Bukkit.getOnlinePlayers()) {
                OreVein v = getVeinAt(p.getLocation());
                if (v != null) {
                    if (!p.hasMetadata("cwe_vein_warned") || System.currentTimeMillis() - p.getMetadata("cwe_vein_warned").get(0).asLong() > 10000) {
                        p.sendTitle("§c⚠ NGUY HIỂM ⚠", "§7Quái vật đang rình rập quanh đây!", 10, 70, 20);
                        p.setMetadata("cwe_vein_warned", new org.bukkit.metadata.FixedMetadataValue(plugin, System.currentTimeMillis()));
                    }
                }
            }
            for (OreVein vein : veins) {
                int active = countActiveGuardians(vein);
                // We want to maintain a population of around 3-5. Let's aim for 3 as standard ambient.
                // If active < 3 and guardiansOnCooldown == 0, we can spawn one.
                if (active < 3 && vein.guardiansOnCooldown == 0) {
                    spawnGuardianInVein(vein);
                }
            }
        }, 300L, 300L); // every 15 seconds
    }

    public int countActiveGuardians(OreVein vein) {
        int count = 0;
        for (Entity e : vein.center.getWorld().getNearbyEntities(vein.center, vein.radius, vein.radius, vein.radius)) {
            if (e instanceof LivingEntity && e.hasMetadata("cwe_guardian") && e.hasMetadata("cwe_guardian_vein")) {
                String veinId = e.getMetadata("cwe_guardian_vein").get(0).asString();
                if (veinId.equals(vein.id.toString())) {
                    if (!e.isDead() && e.isValid()) {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    public void spawnGuardianInVein(OreVein vein) {
        if (vein.center.getWorld() == null) return;
        
        // Strictly enforce cap of 5
        if (countActiveGuardians(vein) >= 5) return;

        // Find a random location within radius
        double rx = vein.center.getX() + (random.nextDouble() * vein.radius * 2 - vein.radius);
        double rz = vein.center.getZ() + (random.nextDouble() * vein.radius * 2 - vein.radius);
        int y = vein.center.getWorld().getHighestBlockYAt((int)rx, (int)rz);
        Location spawnLoc = new Location(vein.center.getWorld(), rx, y + 1, rz);

        // Delegate to BazaarMobDropListener's logic
        org.example.bazaar.BazaarMobDropListener listener = plugin.getServer().getServicesManager().load(org.example.bazaar.BazaarMobDropListener.class);
        if (listener != null) {
            listener.spawnGuardianAt(spawnLoc, null, getMaterialFromType(vein.oreType), vein.id.toString());
        }
    }

    private Material getMaterialFromType(String type) {
        switch (type.toUpperCase()) {
            case "DIAMOND": return Material.DIAMOND_ORE;
            case "IRON": return Material.IRON_ORE;
            case "GOLD": return Material.GOLD_ORE;
            case "LAPIS": return Material.LAPIS_ORE;
            case "REDSTONE": return Material.REDSTONE_ORE;
            case "COPPER": return Material.COPPER_ORE;
            default: return Material.STONE;
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return true;
        Player player = (Player) sender;

        if (args.length == 0 || args[0].equalsIgnoreCase("info") || args[0].equalsIgnoreCase("status")) {
            // Hiển thị trạng thái mỏ quặng cho người chơi hiện tại đứng trên đó
            OreVein vein = getVeinAt(player.getLocation());
            player.sendMessage("§8§l━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            player.sendMessage("§6§l🌟 THÔNG TIN MỎ QUẶNG 🌟");
            if (vein != null) {
                player.sendMessage("§a✔ Trạng thái: §eBạn ĐANG ở trong một Mỏ Quặng!");
                player.sendMessage("§a✦ Loại quặng: §b" + vein.oreType);
                player.sendMessage("§a✦ Bán kính mỏ: §e" + vein.radius + " blocks");
                
                int activeCount = countActiveGuardians(vein);
                player.sendMessage("§a✦ Hộ vệ hiện tại: §c" + activeCount + "/5");
                if (vein.guardiansOnCooldown > 0) {
                    player.sendMessage("§7  (Có §e" + vein.guardiansOnCooldown + " §7hộ vệ đang chờ hồi sinh)");
                }
            } else {
                player.sendMessage("§c❌ Trạng thái: §7Bạn đang ở ngoài vùng mỏ quặng.");
                player.sendMessage("§7(Đào quặng ở đây có xác suất thấp thức tỉnh Hộ Vệ)");
            }
            player.sendMessage("§8§l━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            return true;
        }

        if (args[0].equalsIgnoreCase("create")) {
            if (!player.isOp() && !player.hasPermission("cwe.admin")) {
                player.sendMessage("§c[CWE] Bạn không có quyền sử dụng tính năng tạo mỏ quặng!");
                return true;
            }

            if (args.length < 6) {
                player.sendMessage("§cHDSD: /orevein create <loại_quặng|random> <x> <y> <z> <số_lượng>");
                return true;
            }

            String typeArg = args[1].toUpperCase();
            String[] types = {"DIAMOND", "IRON", "GOLD", "LAPIS", "REDSTONE", "COPPER"};

            double dx = Double.parseDouble(args[2]);
            double dy = Double.parseDouble(args[3]);
            double dz = Double.parseDouble(args[4]);
            int count = Integer.parseInt(args[5]);

            Location baseLoc = player.getLocation();

            List<OreVein> created = new ArrayList<>();
            for (int i = 0; i < count; i++) {
                String type = typeArg.equals("RANDOM") ? types[random.nextInt(types.length)] : typeArg;
                
                double rx = baseLoc.getX() + (random.nextDouble() * dx * 2 - dx);
                double ry = baseLoc.getY() + (random.nextDouble() * dy * 2 - dy);
                double rz = baseLoc.getZ() + (random.nextDouble() * dz * 2 - dz);
                
                ry = Math.max(baseLoc.getWorld().getMinHeight(), Math.min(ry, baseLoc.getWorld().getMaxHeight()));

                OreVein vein = new OreVein(UUID.randomUUID(), new Location(baseLoc.getWorld(), rx, ry, rz), type);
                veins.add(vein);
                created.add(vein);

                // --- TERRAFORMING ---
                if (ry < baseLoc.getWorld().getHighestBlockYAt((int)rx, (int)rz) - 10) {
                    TerrainGenerator.generateUndergroundCave(vein.center, type);
                } else {
                    TerrainGenerator.generateCrater(vein.center, type);
                }
            }
            saveVeins();

            // ─── Thông báo riêng cho admin ───
            player.sendMessage("§8§l━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            player.sendMessage("§a§l[OreVein] §fĐã tạo §e" + count + " §fmỏ quặng:");
            for (int i = 0; i < created.size(); i++) {
                OreVein v = created.get(i);
                player.sendMessage(String.format(
                    "§7  #%d §b%s §7→ §fX:§e%.0f §fY:§e%.0f §fZ:§e%.0f §7(World: §a%s§7)",
                    i + 1,
                    v.oreType,
                    v.center.getX(),
                    v.center.getY(),
                    v.center.getZ(),
                    v.center.getWorld().getName()
                ));
            }
            player.sendMessage("§8§l━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            return true;
        }

        player.sendMessage("§cHDSD: /orevein [info|status] hoặc /orevein create ...");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            completions.add("info");
            completions.add("status");
            if (sender.isOp() || sender.hasPermission("cwe.admin")) {
                completions.add("create");
            }
            List<String> filtered = new ArrayList<>();
            for (String c : completions) {
                if (c.toLowerCase().startsWith(args[0].toLowerCase())) {
                    filtered.add(c);
                }
            }
            return filtered;
        } else if (args.length == 2 && args[0].equalsIgnoreCase("create")) {
            if (sender.isOp() || sender.hasPermission("cwe.admin")) {
                List<String> types = Arrays.asList("DIAMOND", "IRON", "GOLD", "LAPIS", "REDSTONE", "COPPER", "RANDOM");
                List<String> filtered = new ArrayList<>();
                for (String t : types) {
                    if (t.toLowerCase().startsWith(args[1].toLowerCase())) {
                        filtered.add(t);
                    }
                }
                return filtered;
            }
        }
        return new ArrayList<>();
    }
}


/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.NamespacedKey
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.Sound
 *  org.bukkit.World
 *  org.bukkit.block.Block
 *  org.bukkit.entity.ArmorStand
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.EntityType
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.block.Action
 *  org.bukkit.event.player.PlayerInteractAtEntityEvent
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.persistence.PersistentDataContainer
 *  org.bukkit.persistence.PersistentDataType
 *  org.bukkit.plugin.Plugin
 */
package org.example.system;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.example.core.CustomWeaponEngine;
import org.example.enchant.UltimateEnchant;
import org.example.system.MeteorBossManager;

public class RegionBossManager
implements Listener {
    private final CustomWeaponEngine plugin;
    private final Map<String, Location> spawns = new HashMap<String, Location>();
    private final Map<String, Long> cooldowns = new HashMap<String, Long>();
    private final Map<String, Boolean> activeBosses = new HashMap<String, Boolean>();
    private final List<Location> chestLocations = new ArrayList<Location>();
    private final Map<UUID, Set<UUID>> claimedChests = new HashMap<UUID, Set<UUID>>();
    private static final String[] BOSS_TYPES = new String[]{"ICE", "FIRE", "VOID"};
    private int hologramTask = -1;
    private final Map<String, ArmorStand> holograms = new HashMap<String, ArmorStand>();

    public RegionBossManager(CustomWeaponEngine plugin) {
        this.plugin = plugin;
        this.loadSpawns();
        this.startHologramTask();
    }

    private void loadSpawns() {
        if (this.plugin.getConfig().contains("bosses.ICE")) {
            this.spawns.put("ICE", this.plugin.getConfig().getLocation("bosses.ICE"));
        }
        if (this.plugin.getConfig().contains("bosses.FIRE")) {
            this.spawns.put("FIRE", this.plugin.getConfig().getLocation("bosses.FIRE"));
        }
        if (this.plugin.getConfig().contains("bosses.VOID")) {
            this.spawns.put("VOID", this.plugin.getConfig().getLocation("bosses.VOID"));
        }
    }

    public void setSpawn(String type, Location loc) {
        this.spawns.put(type, loc);
        this.plugin.getConfig().set("bosses." + type, loc);
        this.plugin.saveConfig();
        this.updateHologram(type);
    }

    public void removeSpawn(String type) {
        this.activeBosses.put(type, false);
        this.cooldowns.remove(type);
        if (this.spawns.containsKey(type)) {
            Location loc = this.spawns.get(type);
            this.spawns.remove(type);
            this.plugin.getConfig().set("bosses." + type, null);
            this.plugin.saveConfig();
            ArmorStand as = this.holograms.remove(type);
            if (as != null && !as.isDead()) {
                as.remove();
            }
            if (loc != null && loc.getWorld() != null) {
                for (Entity e : loc.getWorld().getNearbyEntities(loc, 10.0, 10.0, 10.0)) {
                    String name;
                    if (!(e instanceof ArmorStand) || (name = e.getCustomName()) == null || !name.contains("BOSS") && !name.contains("R\u01b0\u01a1ng") && !name.contains("KHI\u00caU CHI\u1ebeN")) continue;
                    e.remove();
                }
            }
        }
    }

    private void startHologramTask() {
        long interval = this.plugin.getConfig().getLong("bosses.hologram-update-interval", 20L);
        this.hologramTask = Bukkit.getScheduler().runTaskTimer((Plugin)this.plugin, () -> {
            for (String type : BOSS_TYPES) {
                if (!this.spawns.containsKey(type)) continue;
                this.updateHologram(type);
            }
        }, 20L, interval).getTaskId();
    }

    private void updateHologram(String type) {
        Object newName;
        int chunkZ;
        Location spawnLoc = this.spawns.get(type);
        if (spawnLoc == null) {
            return;
        }
        World world = spawnLoc.getWorld();
        if (world == null) {
            return;
        }
        int chunkX = spawnLoc.getBlockX() >> 4;
        if (!world.isChunkLoaded(chunkX, chunkZ = spawnLoc.getBlockZ() >> 4)) {
            return;
        }
        Location loc = spawnLoc.clone().add(0.5, 1.5, 0.5);
        ArmorStand as = this.holograms.get(type);
        if (as == null || !as.isValid()) {
            ArmorStand existing = null;
            for (Entity e : world.getNearbyEntities(loc, 2.0, 3.0, 2.0)) {
                PersistentDataContainer pdc;
                if (!(e instanceof ArmorStand) || !(pdc = e.getPersistentDataContainer()).has(new NamespacedKey((Plugin)this.plugin, "cwe_boss_hologram"), PersistentDataType.STRING)) continue;
                existing = (ArmorStand)e;
                break;
            }
            if (existing != null) {
                as = existing;
                this.holograms.put(type, as);
            } else {
                as = (ArmorStand)world.spawnEntity(loc, EntityType.ARMOR_STAND);
                as.setVisible(false);
                as.setGravity(false);
                as.setCustomNameVisible(true);
                as.setMarker(true);
                as.getPersistentDataContainer().set(new NamespacedKey((Plugin)this.plugin, "cwe_boss_hologram"), PersistentDataType.STRING, type);
                this.holograms.put(type, as);
                Block block = loc.clone().subtract(0.5, 1.5, 0.5).getBlock();
                if (block.getType() != Material.BEACON) {
                    block.setType(Material.BEACON);
                }
            }
        }
        if (this.activeBosses.getOrDefault(type, false).booleanValue()) {
            newName = "\u00a7c\u00a7lBOSS \u0110ANG CHI\u1ebeN \u0110\u1ea4U!";
        } else {
            long cd = this.cooldowns.getOrDefault(type, 0L);
            long now = System.currentTimeMillis();
            if (now < cd) {
                long left = (cd - now) / 1000L;
                long m = left / 60L;
                long s = left % 60L;
                newName = "\u00a7e\u00a7lBOSS \u0110ANG H\u1ed2I SINH... (" + m + ":" + (s < 10L ? "0" : "") + s + ")";
            } else {
                newName = "\u00a7a\u00a7lCLICK \u0110\u1ec2 KHI\u00caU CHI\u1ebeN " + this.getElementColor(type) + type;
            }
        }
        if (as != null && !((String)newName).equals(as.getCustomName())) {
            as.setCustomName((String)newName);
        }
        
        // Tự động khôi phục khối Beacon khi Boss không chiến đấu và rương đã biến mất
        if (!this.activeBosses.getOrDefault(type, false) && !this.isChestLocation(spawnLoc)) {
            Block block = spawnLoc.getBlock();
            if (block.getType() != Material.BEACON) {
                block.setType(Material.BEACON);
            }
        }
    }

    public void startEvent(String type) {
        if (!this.spawns.containsKey(type)) {
            return;
        }
        this.activeBosses.put(type, true);
        Location loc = this.spawns.get(type);
        loc.getWorld().strikeLightningEffect(loc);
        Bukkit.broadcastMessage((String)("\u00a74\u00a7l[BOSS V\u00d9NG] \u00a7cBoss " + this.getElementColor(type) + type + " \u00a7c\u0111\u00e3 \u0111\u01b0\u1ee3c \u0111\u00e1nh th\u1ee9c t\u1ea1i khu v\u1ef1c c\u1ee7a n\u00f3!"));
        MeteorBossManager.spawnBoss(loc.clone().add(0.0, 1.0, 0.0), type);
    }

    public void onBossDeath(String type) {
        Bukkit.broadcastMessage((String)("\u00a7a\u00a7l[TH\u1eaeNG L\u1ee2I] \u00a7fBoss " + this.getElementColor(type) + type + " \u00a7f\u0111\u00e3 b\u1ecb ti\u00eau di\u1ec7t! R\u01b0\u01a1ng chi\u1ebfn l\u1ee3i ph\u1ea9m xu\u1ea5t hi\u1ec7n t\u1ea1i \u0111\u1ea5u tr\u01b0\u1eddng!"));
        this.activeBosses.put(type, false);
        this.cooldowns.put(type, System.currentTimeMillis() + 300000L);
        this.spawnChests(type);
        Bukkit.getScheduler().runTaskLater((Plugin)this.plugin, () -> this.cleanupChests(type), 6000L);
    }

    private void spawnChests(String type) {
        if (!this.spawns.containsKey(type)) {
            return;
        }
        Location center = this.spawns.get(type).clone();
        
        if ("VOID".equals(type)) {
            // Void Boss (Chúa Tể Hư Không)
            this.spawnChest(center.clone().add(2.0, 0.0, 0.0), Material.COAL_BLOCK, 1, 5000, "§5§lRương Hư Không Thường §7(5,000$)", type);
            this.spawnChest(center.clone().add(0.0, 0.0, 0.0), Material.IRON_BLOCK, 2, 25000, "§5§lRương Hư Không Trung §f(25,000$)", type);
            this.spawnChest(center.clone().add(-2.0, 0.0, 0.0), Material.GOLD_BLOCK, 3, 50000, "§5§lRương Hư Không Cao Cấp §e(50,000$)", type);
        } else if ("ICE".equals(type)) {
            // Ice Boss (Băng Giá Cổ Thần)
            this.spawnChest(center.clone().add(2.0, 0.0, 0.0), Material.COAL_BLOCK, 1, 2000, "§b§lRương Băng Giá Thường §7(2,000$)", type);
            this.spawnChest(center.clone().add(0.0, 0.0, 0.0), Material.IRON_BLOCK, 2, 10000, "§b§lRương Băng Giá Trung §f(10,000$)", type);
            this.spawnChest(center.clone().add(-2.0, 0.0, 0.0), Material.GOLD_BLOCK, 3, 20000, "§b§lRương Băng Giá Cao Cấp §e(20,000$)", type);
        } else {
            // Fire Boss (Hỏa Diệm Ma Vương)
            this.spawnChest(center.clone().add(2.0, 0.0, 0.0), Material.COAL_BLOCK, 1, 1000, "§c§lRương Hỏa Diệm Thường §7(1,000$)", type);
            this.spawnChest(center.clone().add(0.0, 0.0, 0.0), Material.IRON_BLOCK, 2, 5000, "§c§lRương Hỏa Diệm Trung §f(5,000$)", type);
            this.spawnChest(center.clone().add(-2.0, 0.0, 0.0), Material.GOLD_BLOCK, 3, 10000, "§c§lRương Hỏa Diệm Cao Cấp §e(10,000$)", type);
        }
    }

    private void spawnChest(Location loc, Material mat, int chestType, int price, String title, String bossType) {
        Block b = loc.getBlock();
        b.setType(mat);
        this.chestLocations.add(loc);
        ArmorStand as = (ArmorStand)loc.getWorld().spawnEntity(loc.clone().add(0.5, 1.2, 0.5), EntityType.ARMOR_STAND);
        as.setVisible(false);
        as.setGravity(false);
        as.setCustomName(title);
        as.setCustomNameVisible(true);
        as.setMarker(true);
        as.getPersistentDataContainer().set(new NamespacedKey((Plugin)this.plugin, "cwe_meteor_chest"), PersistentDataType.INTEGER, chestType);
        as.getPersistentDataContainer().set(new NamespacedKey((Plugin)this.plugin, "cwe_meteor_chest_price"), PersistentDataType.INTEGER, price);
        as.getPersistentDataContainer().set(new NamespacedKey((Plugin)this.plugin, "cwe_meteor_chest_boss"), PersistentDataType.STRING, bossType);
    }

    private boolean isChestLocation(Location loc) {
        for (Location chestLoc : this.chestLocations) {
            if (chestLoc.getWorld() != null && chestLoc.getWorld().equals(loc.getWorld())
                && chestLoc.getBlockX() == loc.getBlockX()
                && chestLoc.getBlockY() == loc.getBlockY()
                && chestLoc.getBlockZ() == loc.getBlockZ()) {
                return true;
            }
        }
        return false;
    }

    public void cleanupChests(String bossType) {
        Location center = this.spawns.get(bossType);
        if (center == null || center.getWorld() == null) {
            return;
        }
        
        List<Location> targets = Arrays.asList(
            center.clone().add(2.0, 0.0, 0.0),
            center.clone(),
            center.clone().add(-2.0, 0.0, 0.0)
        );
        
        List<Location> toRemove = new ArrayList<Location>();
        for (Location loc : this.chestLocations) {
            boolean matches = false;
            for (Location target : targets) {
                if (target.getWorld().equals(loc.getWorld())
                    && target.getBlockX() == loc.getBlockX()
                    && target.getBlockY() == loc.getBlockY()
                    && target.getBlockZ() == loc.getBlockZ()) {
                    matches = true;
                    break;
                }
            }
            if (!matches) {
                continue;
            }
            
            // Load chunk if needed to ensure entity cleanup is successful
            boolean chunkWasLoaded = loc.getChunk().isLoaded();
            if (!chunkWasLoaded) {
                loc.getChunk().load();
            }
            
            // Remove ArmorStand
            ArmorStand as = null;
            for (Entity e : loc.getWorld().getNearbyEntities(loc.clone().add(0.5, 1.2, 0.5), 0.5, 0.5, 0.5)) {
                if (e instanceof ArmorStand && e.getPersistentDataContainer().has(new NamespacedKey((Plugin)this.plugin, "cwe_meteor_chest"), PersistentDataType.INTEGER)) {
                    as = (ArmorStand) e;
                    break;
                }
            }
            if (as != null) {
                UUID asId = as.getUniqueId();
                for (Set<UUID> claimed : this.claimedChests.values()) {
                    claimed.remove(asId);
                }
                as.remove();
            }
            
            // Restore block
            boolean isSpawnLoc = (center.getBlockX() == loc.getBlockX() 
                && center.getBlockY() == loc.getBlockY() 
                && center.getBlockZ() == loc.getBlockZ());
            if (isSpawnLoc) {
                loc.getBlock().setType(Material.BEACON);
            } else {
                loc.getBlock().setType(Material.AIR);
            }
            
            toRemove.add(loc);
            
            if (!chunkWasLoaded) {
                loc.getChunk().unload();
            }
        }
        this.chestLocations.removeAll(toRemove);
    }

    public void cleanupOnDisable() {
        for (String type : BOSS_TYPES) {
            this.cleanupChests(type);
        }
        for (ArmorStand as : this.holograms.values()) {
            if (as == null || as.isDead()) continue;
            as.remove();
        }
    }

    public void resetBossState(String type) {
        this.activeBosses.put(type, false);
        this.cooldowns.remove(type);
    }

    public void forceResetAll() {
        for (String type : BOSS_TYPES) {
            this.cleanupChests(type);
        }
        for (String type : BOSS_TYPES) {
            this.activeBosses.put(type, false);
            this.cooldowns.remove(type);
        }
        this.claimedChests.clear();
    }

    @EventHandler
    public void onInteractHologram(PlayerInteractAtEntityEvent event) {
        if (event.getRightClicked() instanceof ArmorStand && event.getRightClicked().getPersistentDataContainer().has(new NamespacedKey((Plugin)this.plugin, "cwe_boss_hologram"), PersistentDataType.STRING)) {
            event.setCancelled(true);
            String type = (String)event.getRightClicked().getPersistentDataContainer().get(new NamespacedKey((Plugin)this.plugin, "cwe_boss_hologram"), PersistentDataType.STRING);
            this.handleBossSummonClick(event.getPlayer(), type);
        }
    }

    @EventHandler
    public void onInteractBlock(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.BEACON) {
            Location clickLoc = event.getClickedBlock().getLocation();
            for (Map.Entry<String, Location> entry : this.spawns.entrySet()) {
                if (entry.getValue().getBlockX() != clickLoc.getBlockX() || entry.getValue().getBlockY() != clickLoc.getBlockY() || entry.getValue().getBlockZ() != clickLoc.getBlockZ()) continue;
                event.setCancelled(true);
                this.handleBossSummonClick(event.getPlayer(), entry.getKey());
                break;
            }
        }
    }

    @EventHandler(priority=EventPriority.LOWEST)
    public void onBlockBreak(BlockBreakEvent event) {
        Location brokenLoc = event.getBlock().getLocation();
        for (Location loc : this.spawns.values()) {
            if (loc.getWorld() != null && loc.getWorld().equals(brokenLoc.getWorld()) && loc.getBlockX() == brokenLoc.getBlockX() && loc.getBlockY() == brokenLoc.getBlockY() && loc.getBlockZ() == brokenLoc.getBlockZ()) {
                event.setCancelled(true);
                event.getPlayer().sendMessage("§cKhông thể phá hủy bệ phóng triệu hồi Boss!");
                return;
            }
        }
        for (Location loc : this.chestLocations) {
            if (loc.getWorld() != null && loc.getWorld().equals(brokenLoc.getWorld()) && loc.getBlockX() == brokenLoc.getBlockX() && loc.getBlockY() == brokenLoc.getBlockY() && loc.getBlockZ() == brokenLoc.getBlockZ()) {
                event.setCancelled(true);
                event.getPlayer().sendMessage("§cKhông thể phá hủy rương sự kiện! Hãy click chuột phải để mở.");
                return;
            }
        }
    }

    private void handleBossSummonClick(Player p, String type) {
        if (this.activeBosses.getOrDefault(type, false).booleanValue()) {
            p.sendMessage("\u00a7cBoss \u0111ang chi\u1ebfn \u0111\u1ea5u, kh\u00f4ng th\u1ec3 tri\u1ec7u h\u1ed3i th\u00eam!");
            return;
        }
        for (Map.Entry<String, Boolean> entry : this.activeBosses.entrySet()) {
            if (entry.getValue()) {
                p.sendMessage("§cMột Boss Vùng khác (" + entry.getKey() + ") đang được khiêu chiến! Vui lòng chờ họ kết thúc.");
                return;
            }
        }
        long cd = this.cooldowns.getOrDefault(type, 0L);
        long now = System.currentTimeMillis();
        if (now < cd) {
            p.sendMessage("\u00a7cBoss n\u00e0y \u0111ang h\u1ed3i s\u1ee9c! Vui l\u00f2ng ch\u1edd.");
            return;
        }
        this.startEvent(type);
    }

    @EventHandler(priority=EventPriority.HIGHEST)
    public void onChestClick(PlayerInteractEvent event) {
        java.util.HashMap<Integer, org.bukkit.inventory.ItemStack> leftover;
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        Block b = event.getClickedBlock();
        if (b == null) {
            return;
        }
        
        ArmorStand armorStand = null;
        for (Entity e : b.getWorld().getNearbyEntities(b.getLocation().add(0.5, 1.2, 0.5), 0.5, 0.5, 0.5)) {
            if (e instanceof ArmorStand && e.getPersistentDataContainer().has(new NamespacedKey((Plugin)this.plugin, "cwe_meteor_chest"), PersistentDataType.INTEGER)) {
                armorStand = (ArmorStand) e;
                break;
            }
        }
        
        if (armorStand == null) {
            return;
        }

        Player p = event.getPlayer();
        MeteorBossManager mgr = CustomWeaponEngine.getMeteorBossManager();
        if (mgr != null && !mgr.isParticipant(p.getUniqueId())) {
            event.setCancelled(true);
            p.sendMessage("§cBạn không phải là người tham gia khiêu chiến Boss, không thể mở rương này!");
            return;
        }
        
        event.setCancelled(true);
        
        int chestType = armorStand.getPersistentDataContainer().get(new NamespacedKey((Plugin)this.plugin, "cwe_meteor_chest"), PersistentDataType.INTEGER);
        int price = armorStand.getPersistentDataContainer().getOrDefault(new NamespacedKey((Plugin)this.plugin, "cwe_meteor_chest_price"), PersistentDataType.INTEGER, 1000);
        String bossType = armorStand.getPersistentDataContainer().getOrDefault(new NamespacedKey((Plugin)this.plugin, "cwe_meteor_chest_boss"), PersistentDataType.STRING, "FIRE");
        UUID asUuid = armorStand.getUniqueId();
        
        Set<UUID> claimed = this.claimedChests.computeIfAbsent(p.getUniqueId(), k -> new HashSet<UUID>());
        if (claimed.contains(asUuid)) {
            p.sendMessage("§cBạn đã mở rương này rồi!");
            return;
        }
        
        if (CustomWeaponEngine.getEconomy() != null) {
            if (CustomWeaponEngine.getEconomy().getBalance((OfflinePlayer)p) < (double)price) {
                p.sendMessage("§cBạn không có đủ " + price + "$ để mở rương này!");
                return;
            }
            CustomWeaponEngine.getEconomy().withdrawPlayer((OfflinePlayer)p, (double)price);
        }
        
        claimed.add(asUuid);
        p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
        p.sendMessage("§aBạn đã mở rương và nhận phần thưởng!");
        
        ItemStack reward = this.generateGachaReward(bossType, chestType, p);
        if (reward != null && !(leftover = p.getInventory().addItem(new ItemStack[]{reward})).isEmpty()) {
            for (ItemStack item : leftover.values()) {
                p.getWorld().dropItem(p.getLocation(), item);
            }
            p.sendMessage("§eTúi của bạn đã đầy! Phần thưởng bị rớt ra đất!");
        }
    }

    private ItemStack generateGachaReward(String bossType, int chestType, Player p) {
        Random r = new Random();
        
        double bookChance = 0.05;
        if ("VOID".equals(bossType) && chestType == 3) {
            bookChance = 0.15;
        }
        if (r.nextDouble() < bookChance) {
            p.sendMessage("§d§l[MAY MẮN] §aBạn đã trúng Bí Kíp Tối Thượng từ rương!");
            Bukkit.broadcastMessage((String)("§d§l[JACKPOT] §fNgười chơi §e" + p.getName() + " §fvừa quay trúng §d§lSÁCH TỐI THƯỢNG §ftừ sự kiện Boss Vùng!"));
            return this.createUltimateEnchantBook();
        }
        
        double roll = r.nextDouble();
        
        if ("VOID".equals(bossType)) {
            if (chestType == 3) { // Rương Cao Cấp (50,000$)
                if (roll < 0.01) { // 1% cơ hội trúng Enchanted Block of Diamond
                    return this.createEnchantedBazaarItem(Material.DIAMOND_BLOCK, "§5Enchanted Block of Diamond", "ENCHANTED_DIAMOND_BLOCK");
                }
                if (roll < 0.06) { // 5% cơ hội trúng Mảnh Vỡ Vũ Trụ (MYTHIC)
                    return this.createFragment("MYTHIC", "§d§lMảnh Vỡ Vũ Trụ (MYTHIC)");
                }
                if (roll < 0.26) { // 20% cơ hội trúng Lõi Năng Lượng Cao Cấp hoặc Mảnh Vỡ Ánh Sáng (LEGENDARY)
                    return r.nextBoolean() ? this.createFragment("LEGENDARY", "§6§lMảnh Vỡ Ánh Sáng (LEGENDARY)") : this.createCraftMaterial(Material.BEACON, "§6§lLõi Năng Lượng Cao Cấp", "LEGENDARY");
                }
                if (r.nextBoolean()) {
                    int bonusMoney = 15000 + r.nextInt(45000);
                    if (CustomWeaponEngine.getEconomy() != null) {
                        CustomWeaponEngine.getEconomy().depositPlayer((OfflinePlayer)p, (double)bonusMoney);
                        p.sendMessage("§eBạn nhận được " + bonusMoney + "$ từ rương!");
                    }
                    return null;
                }
                return new ItemStack(Material.NETHERITE_BLOCK, 1 + r.nextInt(2));
            }
            if (chestType == 2) { // Rương Trung Bình (25,000$)
                if (roll < 0.02) { // 2% trúng Mảnh Vỡ Vũ Trụ
                    return this.createFragment("MYTHIC", "§d§lMảnh Vỡ Vũ Trụ (MYTHIC)");
                }
                if (roll < 0.17) { // 15% trúng Lõi Năng Lượng hoặc Mảnh Vỡ Ánh Sáng (LEGENDARY)
                    return r.nextBoolean() ? this.createFragment("LEGENDARY", "§6§lMảnh Vỡ Ánh Sáng (LEGENDARY)") : this.createCraftMaterial(Material.BEACON, "§6§lLõi Năng Lượng Cao Cấp", "LEGENDARY");
                }
                if (r.nextBoolean()) {
                    int bonusMoney = 8000 + r.nextInt(20000);
                    if (CustomWeaponEngine.getEconomy() != null) {
                        CustomWeaponEngine.getEconomy().depositPlayer((OfflinePlayer)p, (double)bonusMoney);
                        p.sendMessage("§eBạn nhận được " + bonusMoney + "$ từ rương!");
                    }
                    return null;
                }
                ItemStack ed = this.createEnchantedBazaarItem(Material.DIAMOND, "§9Enchanted Diamond", "ENCHANTED_DIAMOND");
                ed.setAmount(1 + r.nextInt(2));
                return ed;
            }
            // Rương Thường (5,000$)
            if (roll < 0.05) { // 5% trúng Mảnh Vỡ Hỗn Mang hoặc Băng Tinh Cổ Đại (EPIC)
                return r.nextBoolean() ? this.createFragment("EPIC", "§5Mảnh Vỡ Hỗn Mang (EPIC)") : this.createCraftMaterial(Material.DRAGON_BREATH, "§5Băng Tinh Cổ Đại", "EPIC");
            }
            if (r.nextBoolean()) {
                int bonusMoney = 2000 + r.nextInt(5000);
                if (CustomWeaponEngine.getEconomy() != null) {
                    CustomWeaponEngine.getEconomy().depositPlayer((OfflinePlayer)p, (double)bonusMoney);
                    p.sendMessage("§eBạn nhận được " + bonusMoney + "$ từ rương!");
                }
                return null;
            }
            return this.createEnchantedBazaarItem(Material.DIAMOND, "§9Enchanted Diamond", "ENCHANTED_DIAMOND");
        }
        
        if ("ICE".equals(bossType)) {
            if (chestType == 3) { // Rương Cao Cấp (20,000$)
                if (roll < 0.005) { // 0.5% Mảnh Vỡ Vũ Trụ
                    return this.createFragment("MYTHIC", "§d§lMảnh Vỡ Vũ Trụ (MYTHIC)");
                }
                if (roll < 0.105) { // 10% Lõi Năng Lượng Cao Cấp hoặc Mảnh Vỡ Ánh Sáng (LEGENDARY)
                    return r.nextBoolean() ? this.createFragment("LEGENDARY", "§6§lMảnh Vỡ Ánh Sáng (LEGENDARY)") : this.createCraftMaterial(Material.BEACON, "§6§lLõi Năng Lượng Cao Cấp", "LEGENDARY");
                }
                if (roll < 0.355) { // 25% Băng Tinh Cổ Đại hoặc Mảnh Vỡ Hỗn Mang (EPIC)
                    return r.nextBoolean() ? this.createFragment("EPIC", "§5Mảnh Vỡ Hỗn Mang (EPIC)") : this.createCraftMaterial(Material.DRAGON_BREATH, "§5Băng Tinh Cổ Đại", "EPIC");
                }
                if (r.nextBoolean()) {
                    int bonusMoney = 5000 + r.nextInt(15000);
                    if (CustomWeaponEngine.getEconomy() != null) {
                        CustomWeaponEngine.getEconomy().depositPlayer((OfflinePlayer)p, (double)bonusMoney);
                        p.sendMessage("§eBạn nhận được " + bonusMoney + "$ từ rương!");
                    }
                    return null;
                }
                return new ItemStack(Material.DIAMOND_BLOCK, 1 + r.nextInt(2));
            }
            if (chestType == 2) { // Rương Trung Bình (10,000$)
                if (roll < 0.02) { // 2% Lõi Năng Lượng Cao Cấp hoặc Mảnh Vỡ Ánh Sáng (LEGENDARY)
                    return r.nextBoolean() ? this.createFragment("LEGENDARY", "§6§lMảnh Vỡ Ánh Sáng (LEGENDARY)") : this.createCraftMaterial(Material.BEACON, "§6§lLõi Năng Lượng Cao Cấp", "LEGENDARY");
                }
                if (roll < 0.12) { // 10% Băng Tinh Cổ Đại hoặc Mảnh Vỡ Hỗn Mang (EPIC)
                    return r.nextBoolean() ? this.createFragment("EPIC", "§5Mảnh Vỡ Hỗn Mang (EPIC)") : this.createCraftMaterial(Material.DRAGON_BREATH, "§5Băng Tinh Cổ Đại", "EPIC");
                }
                if (r.nextBoolean()) {
                    int bonusMoney = 2000 + r.nextInt(8000);
                    if (CustomWeaponEngine.getEconomy() != null) {
                        CustomWeaponEngine.getEconomy().depositPlayer((OfflinePlayer)p, (double)bonusMoney);
                        p.sendMessage("§eBạn nhận được " + bonusMoney + "$ từ rương!");
                    }
                    return null;
                }
                return new ItemStack(Material.GOLD_BLOCK, 1 + r.nextInt(2));
            }
            // Rương Thường (2,000$)
            if (roll < 0.05) { // 5% Lõi Dung Nham hoặc Mảnh Vỡ Tinh Tú (RARE)
                return r.nextBoolean() ? this.createFragment("RARE", "§9Mảnh Vỡ Tinh Tú (RARE)") : this.createCraftMaterial(Material.MAGMA_CREAM, "§9Lõi Dung Nham", "RARE");
            }
            if (r.nextBoolean()) {
                int bonusMoney = 1000 + r.nextInt(3000);
                if (CustomWeaponEngine.getEconomy() != null) {
                    CustomWeaponEngine.getEconomy().depositPlayer((OfflinePlayer)p, (double)bonusMoney);
                    p.sendMessage("§eBạn nhận được " + bonusMoney + "$ từ rương!");
                }
                return null;
            }
            return new ItemStack(Material.IRON_BLOCK, 1 + r.nextInt(2));
        }
        
        // ==========================================
        // PHẦN THƯỞNG CHO FIRE BOSS (HỎA DIỆM MA VƯƠNG) (Gốc)
        // ==========================================
        if (chestType == 3) { // Rương Cao Cấp (10,000$)
            if (roll < 0.0005) { // 0.05% Mảnh Vỡ Vũ Trụ
                return this.createFragment("MYTHIC", "§d§lMảnh Vỡ Vũ Trụ (MYTHIC)");
            }
            if (roll < 0.0205) { // 2% Lõi Năng Lượng Cao Cấp hoặc Mảnh Vỡ Ánh Sáng (LEGENDARY)
                return r.nextBoolean() ? this.createFragment("LEGENDARY", "§6§lMảnh Vỡ Ánh Sáng (LEGENDARY)") : this.createCraftMaterial(Material.BEACON, "§6§lLõi Năng Lượng Cao Cấp", "LEGENDARY");
            }
            if (roll < 0.1205) { // 10% Băng Tinh Cổ Đại hoặc Mảnh Vỡ Hỗn Mang (EPIC)
                return r.nextBoolean() ? this.createFragment("EPIC", "§5Mảnh Vỡ Hỗn Mang (EPIC)") : this.createCraftMaterial(Material.DRAGON_BREATH, "§5Băng Tinh Cổ Đại", "EPIC");
            }
            if (roll < 0.2705) { // 15% Lõi Dung Nham hoặc Mảnh Vỡ Tinh Tú (RARE)
                return r.nextBoolean() ? this.createFragment("RARE", "§9Mảnh Vỡ Tinh Tú (RARE)") : this.createCraftMaterial(Material.MAGMA_CREAM, "§9Lõi Dung Nham", "RARE");
            }
            if (r.nextBoolean()) {
                int bonusMoney = 2000 + r.nextInt(10000);
                if (CustomWeaponEngine.getEconomy() != null) {
                    CustomWeaponEngine.getEconomy().depositPlayer((OfflinePlayer)p, (double)bonusMoney);
                    p.sendMessage("§eBạn nhận được " + bonusMoney + "$ từ rương!");
                }
                return null;
            }
            return new ItemStack(Material.DIAMOND_BLOCK, 1 + r.nextInt(3));
        }
        if (chestType == 2) { // Rương Trung Bình (5,000$)
            if (roll < 0.01) { // 1% Epic
                return r.nextBoolean() ? this.createFragment("EPIC", "§5Mảnh Vỡ Hỗn Mang (EPIC)") : this.createCraftMaterial(Material.DRAGON_BREATH, "§5Băng Tinh Cổ Đại", "EPIC");
            }
            if (roll < 0.06) { // 5% Rare
                return r.nextBoolean() ? this.createFragment("RARE", "§9Mảnh Vỡ Tinh Tú (RARE)") : this.createCraftMaterial(Material.MAGMA_CREAM, "§9Lõi Dung Nham", "RARE");
            }
            if (r.nextBoolean()) {
                int bonusMoney = 1000 + r.nextInt(5000);
                if (CustomWeaponEngine.getEconomy() != null) {
                    CustomWeaponEngine.getEconomy().depositPlayer((OfflinePlayer)p, (double)bonusMoney);
                    p.sendMessage("§eBạn nhận được " + bonusMoney + "$ từ rương!");
                }
                return null;
            }
            return new ItemStack(Material.GOLD_BLOCK, 1 + r.nextInt(3));
        }
        // Rương Thường (1,000$)
        if (roll < 0.02) { // 2% Rare
            return r.nextBoolean() ? this.createFragment("RARE", "§9Mảnh Vỡ Tinh Tú (RARE)") : this.createCraftMaterial(Material.MAGMA_CREAM, "§9Lõi Dung Nham", "RARE");
        }
        if (r.nextBoolean()) {
            int bonusMoney = 500 + r.nextInt(1500);
            if (CustomWeaponEngine.getEconomy() != null) {
                CustomWeaponEngine.getEconomy().depositPlayer((OfflinePlayer)p, (double)bonusMoney);
                p.sendMessage("§eBạn nhận được " + bonusMoney + "$ từ rương!");
            }
            return null;
        }
        return new ItemStack(Material.IRON_BLOCK, 2 + r.nextInt(4));
    }

    private ItemStack createUltimateEnchantBook() {
        UltimateEnchant[] enchants = UltimateEnchant.values();
        UltimateEnchant randEnc = enchants[new Random().nextInt(enchants.length)];
        ItemStack book = new ItemStack(Material.ENCHANTED_BOOK);
        ItemMeta meta = book.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("\u00a7d\u00a7lS\u00c1CH T\u1ed0I TH\u01af\u1ee2NG: " + randEnc.getDisplayName().toUpperCase());
            meta.setLore(Arrays.asList("\u00a77K\u00e9o th\u1ea3 v\u00e0o \u00f4 ph\u1ee5 c\u1ee7a \u0110e \u0110\u00fac", "\u00a77\u0111\u1ec3 \u00e1p d\u1ee5ng b\u00f9a ch\u00fa n\u00e0y (C\u1ea5p 1)."));
            meta.getPersistentDataContainer().set(new NamespacedKey((Plugin)this.plugin, "ult_enchant_" + randEnc.getId()), PersistentDataType.INTEGER, 1);
            book.setItemMeta(meta);
        }
        return book;
    }

    public ItemStack getFragmentByTier(String tier) {
        switch (tier.toUpperCase()) {
            case "RARE": {
                return this.createFragment("RARE", "\u00a79M\u1ea3nh V\u1ee1 Tinh T\u00fa (RARE)");
            }
            case "EPIC": {
                return this.createFragment("EPIC", "\u00a75M\u1ea3nh V\u1ee1 H\u1ed7n Mang (EPIC)");
            }
            case "LEGENDARY": {
                return this.createFragment("LEGENDARY", "\u00a76\u00a7lM\u1ea3nh V\u1ee1 \u00c1nh S\u00e1ng (LEGENDARY)");
            }
            case "MYTHIC": {
                return this.createFragment("MYTHIC", "\u00a7d\u00a7lM\u1ea3nh V\u1ee1 V\u0169 Tr\u1ee5 (MYTHIC)");
            }
        }
        return null;
    }

    public ItemStack createFragment(String tier, String name) {
        ItemStack item = new ItemStack(Material.NETHER_STAR);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            meta.setLore(Arrays.asList("\u00a77D\u00f9ng t\u1ea1i \u0110e \u0110\u00fac \u0110\u1ed9c Quy\u1ec1n", "\u00a77\u0111\u1ec3 \u00e1p d\u1ee5ng s\u1ee9c m\u1ea1nh c\u01b0\u1eddng h\u00f3a \u0111\u1eb7c bi\u1ec7t."));
            PersistentDataContainer pdc = meta.getPersistentDataContainer();
            pdc.set(new NamespacedKey((Plugin)this.plugin, "cwe_reforge_fragment"), PersistentDataType.INTEGER, 1);
            pdc.set(new NamespacedKey((Plugin)this.plugin, "cwe_fragment_tier"), PersistentDataType.STRING, tier);
            item.setItemMeta(meta);
        }
        return item;
    }

    private ItemStack createCraftMaterial(Material mat, String name, String rarity) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            meta.setLore(Arrays.asList("\u00a77Nguy\u00ean li\u1ec7u ch\u1ebf t\u1ea1o th\u1ea7n kh\u00ed."));
            PersistentDataContainer pdc = meta.getPersistentDataContainer();
            pdc.set(new NamespacedKey((Plugin)this.plugin, "cwe_tier"), PersistentDataType.STRING, rarity);
            item.setItemMeta(meta);
        }
        return item;
    }

    private ItemStack createEnchantedBazaarItem(Material mat, String name, String bazaarId) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            meta.setLore(Arrays.asList("§7Vật phẩm ma thuật nén cao cấp", "§7chuyên dụng giao dịch tại Chợ Bazaar.", "", "§cKhông thể ăn hay sử dụng thô!"));
            PersistentDataContainer pdc = meta.getPersistentDataContainer();
            pdc.set(new NamespacedKey((Plugin)this.plugin, "bazaar_id"), PersistentDataType.STRING, bazaarId);
            try {
                meta.addEnchant(org.bukkit.enchantments.Enchantment.getByKey(NamespacedKey.minecraft("luck_of_the_sea")), 1, true);
            } catch (Throwable ignored) {}
            meta.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS);
            item.setItemMeta(meta);
        }
        return item;
    }

    private String getElementColor(String type) {
        if ("FIRE".equals(type)) {
            return "\u00a7c";
        }
        if ("ICE".equals(type)) {
            return "\u00a7b";
        }
        if ("VOID".equals(type)) {
            return "\u00a75";
        }
        return "\u00a7f";
    }
}


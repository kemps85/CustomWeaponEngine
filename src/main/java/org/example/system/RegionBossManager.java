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
    private final Map<UUID, Set<Integer>> claimedChests = new HashMap<UUID, Set<Integer>>();
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
    }

    public void startEvent(String type) {
        if (!this.spawns.containsKey(type)) {
            return;
        }
        this.activeBosses.put(type, true);
        this.claimedChests.clear();
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
        Bukkit.getScheduler().runTaskLater((Plugin)this.plugin, this::cleanupChests, 6000L);
    }

    private void spawnChests(String type) {
        if (!this.spawns.containsKey(type)) {
            return;
        }
        Location center = this.spawns.get(type).clone();
        this.spawnChest(center.clone().add(2.0, 0.0, 0.0), Material.COAL_BLOCK, 1, 1000, "\u00a77R\u01b0\u01a1ng Th\u01b0\u1eddng (1,000$)");
        this.spawnChest(center.clone().add(0.0, 0.0, 0.0), Material.IRON_BLOCK, 2, 5000, "\u00a7fR\u01b0\u01a1ng Trung B\u00ecnh (5,000$)");
        this.spawnChest(center.clone().add(-2.0, 0.0, 0.0), Material.GOLD_BLOCK, 3, 10000, "\u00a7eR\u01b0\u01a1ng Cao C\u1ea5p (10,000$)");
    }

    private void spawnChest(Location loc, Material mat, int chestType, int price, String title) {
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
    }

    public void cleanupChests() {
        for (Location loc : this.chestLocations) {
            loc.getBlock().setType(Material.AIR);
            for (Entity e : loc.getWorld().getNearbyEntities(loc, 1.0, 3.0, 1.0)) {
                if (!(e instanceof ArmorStand) || !e.getPersistentDataContainer().has(new NamespacedKey((Plugin)this.plugin, "cwe_meteor_chest"), PersistentDataType.INTEGER)) continue;
                e.remove();
            }
        }
        this.chestLocations.clear();
        this.claimedChests.clear();
    }

    public void cleanupOnDisable() {
        this.cleanupChests();
        for (ArmorStand as : this.holograms.values()) {
            if (as == null || as.isDead()) continue;
            as.remove();
        }
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

    private void handleBossSummonClick(Player p, String type) {
        if (this.activeBosses.getOrDefault(type, false).booleanValue()) {
            p.sendMessage("\u00a7cBoss \u0111ang chi\u1ebfn \u0111\u1ea5u, kh\u00f4ng th\u1ec3 tri\u1ec7u h\u1ed3i th\u00eam!");
            return;
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
        boolean isChest = false;
        int chestType = 0;
        int price = 0;
        for (Location loc : this.chestLocations) {
            if (loc.getBlockX() != b.getX() || loc.getBlockY() != b.getY() || loc.getBlockZ() != b.getZ()) continue;
            isChest = true;
            if (b.getType() == Material.COAL_BLOCK) {
                chestType = 1;
                price = 1000;
                break;
            }
            if (b.getType() == Material.IRON_BLOCK) {
                chestType = 2;
                price = 5000;
                break;
            }
            if (b.getType() != Material.GOLD_BLOCK) break;
            chestType = 3;
            price = 10000;
            break;
        }
        if (!isChest) {
            return;
        }
        event.setCancelled(true);
        Player p = event.getPlayer();
        Set claimed = this.claimedChests.computeIfAbsent(p.getUniqueId(), k -> new HashSet());
        if (claimed.contains(chestType)) {
            p.sendMessage("\u00a7cB\u1ea1n \u0111\u00e3 m\u1edf r\u01b0\u01a1ng n\u00e0y r\u1ed3i!");
            return;
        }
        if (CustomWeaponEngine.getEconomy() != null) {
            if (CustomWeaponEngine.getEconomy().getBalance((OfflinePlayer)p) < (double)price) {
                p.sendMessage("\u00a7cB\u1ea1n kh\u00f4ng c\u00f3 \u0111\u1ee7 " + price + "$ \u0111\u1ec3 m\u1edf r\u01b0\u01a1ng n\u00e0y!");
                return;
            }
            CustomWeaponEngine.getEconomy().withdrawPlayer((OfflinePlayer)p, (double)price);
        }
        claimed.add(chestType);
        p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
        p.sendMessage("\u00a7aB\u1ea1n \u0111\u00e3 m\u1edf r\u01b0\u01a1ng v\u00e0 nh\u1eadn ph\u1ea7n th\u01b0\u1edfng!");
        ItemStack reward = this.generateGachaReward(chestType, p);
        if (reward != null && !(leftover = p.getInventory().addItem(new ItemStack[]{reward})).isEmpty()) {
            for (ItemStack item : leftover.values()) {
                p.getWorld().dropItem(p.getLocation(), item);
            }
            p.sendMessage("\u00a7eT\u00fai c\u1ee7a b\u1ea1n \u0111\u00e3 \u0111\u1ea7y! Ph\u1ea7n th\u01b0\u1edfng b\u1ecb r\u1edbt ra \u0111\u1ea5t!");
        }
    }

    private ItemStack generateGachaReward(int chestType, Player p) {
        Random r = new Random();
        if (r.nextDouble() < 0.05) {
            p.sendMessage("\u00a7d\u00a7l[MAY M\u1eaeN] \u00a7aB\u1ea1n \u0111\u00e3 tr\u00fang B\u00ed K\u00edp T\u1ed1i Th\u01b0\u1ee3ng t\u1eeb r\u01b0\u01a1ng!");
            Bukkit.broadcastMessage((String)("\u00a7d\u00a7l[JACKPOT] \u00a7fNg\u01b0\u1eddi ch\u01a1i \u00a7e" + p.getName() + " \u00a7fv\u1eeba quay tr\u00fang \u00a7d\u00a7lS\u00c1CH T\u1ed0I TH\u01af\u1ee2NG \u00a7ft\u1eeb s\u1ef1 ki\u1ec7n Boss V\u00f9ng!"));
            return this.createUltimateEnchantBook();
        }
        double roll = r.nextDouble();
        if (chestType == 3) {
            if (roll < 0.001) {
                return this.createFragment("MYTHIC", "\u00a7d\u00a7lM\u1ea3nh V\u1ee1 V\u0169 Tr\u1ee5 (MYTHIC)");
            }
            if (roll < 0.051) {
                return r.nextBoolean() ? this.createFragment("LEGENDARY", "\u00a76\u00a7lM\u1ea3nh V\u1ee1 \u00c1nh S\u00e1ng (LEGENDARY)") : this.createCraftMaterial(Material.BEACON, "\u00a76\u00a7lL\u00f5i N\u0103ng L\u01b0\u1ee3ng Cao C\u1ea5p", "LEGENDARY");
            }
            if (roll < 0.251) {
                return r.nextBoolean() ? this.createFragment("EPIC", "\u00a75M\u1ea3nh V\u1ee1 H\u1ed7n Mang (EPIC)") : this.createCraftMaterial(Material.DRAGON_BREATH, "\u00a75B\u0103ng Tinh C\u1ed5 \u0110\u1ea1i", "EPIC");
            }
            if (roll < 0.551) {
                return r.nextBoolean() ? this.createFragment("RARE", "\u00a79M\u1ea3nh V\u1ee1 Tinh T\u00fa (RARE)") : this.createCraftMaterial(Material.MAGMA_CREAM, "\u00a79L\u00f5i Dung Nham", "RARE");
            }
            if (r.nextBoolean()) {
                int bonusMoney = 2000 + r.nextInt(10000);
                if (CustomWeaponEngine.getEconomy() != null) {
                    CustomWeaponEngine.getEconomy().depositPlayer((OfflinePlayer)p, (double)bonusMoney);
                    p.sendMessage("\u00a7eB\u1ea1n nh\u1eadn \u0111\u01b0\u1ee3c " + bonusMoney + "$ t\u1eeb r\u01b0\u01a1ng!");
                }
                return null;
            }
            return new ItemStack(Material.DIAMOND_BLOCK, 1 + r.nextInt(3));
        }
        if (chestType == 2) {
            if (roll < 0.02) {
                return r.nextBoolean() ? this.createFragment("EPIC", "\u00a75M\u1ea3nh V\u1ee1 H\u1ed7n Mang (EPIC)") : this.createCraftMaterial(Material.DRAGON_BREATH, "\u00a75B\u0103ng Tinh C\u1ed5 \u0110\u1ea1i", "EPIC");
            }
            if (roll < 0.15) {
                return r.nextBoolean() ? this.createFragment("RARE", "\u00a79M\u1ea3nh V\u1ee1 Tinh T\u00fa (RARE)") : this.createCraftMaterial(Material.MAGMA_CREAM, "\u00a79L\u00f5i Dung Nham", "RARE");
            }
            if (r.nextBoolean()) {
                int bonusMoney = 1000 + r.nextInt(5000);
                if (CustomWeaponEngine.getEconomy() != null) {
                    CustomWeaponEngine.getEconomy().depositPlayer((OfflinePlayer)p, (double)bonusMoney);
                    p.sendMessage("\u00a7eB\u1ea1n nh\u1eadn \u0111\u01b0\u1ee3c " + bonusMoney + "$ t\u1eeb r\u01b0\u01a1ng!");
                }
                return null;
            }
            return new ItemStack(Material.GOLD_BLOCK, 1 + r.nextInt(3));
        }
        if (roll < 0.05) {
            return r.nextBoolean() ? this.createFragment("RARE", "\u00a79M\u1ea3nh V\u1ee1 Tinh T\u00fa (RARE)") : this.createCraftMaterial(Material.MAGMA_CREAM, "\u00a79L\u00f5i Dung Nham", "RARE");
        }
        if (r.nextBoolean()) {
            int bonusMoney = 500 + r.nextInt(1500);
            if (CustomWeaponEngine.getEconomy() != null) {
                CustomWeaponEngine.getEconomy().depositPlayer((OfflinePlayer)p, (double)bonusMoney);
                p.sendMessage("\u00a7eB\u1ea1n nh\u1eadn \u0111\u01b0\u1ee3c " + bonusMoney + "$ t\u1eeb r\u01b0\u01a1ng!");
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


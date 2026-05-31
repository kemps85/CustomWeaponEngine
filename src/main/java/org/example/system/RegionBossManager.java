package org.example.system;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.example.core.CustomWeaponEngine;

import java.util.*;

public class RegionBossManager implements Listener {
    private final CustomWeaponEngine plugin;

    // type -> location
    private final Map<String, Location> spawns = new HashMap<>();
    // type -> end cooldown time
    private final Map<String, Long> cooldowns = new HashMap<>();
    // type -> is active
    private final Map<String, Boolean> activeBosses = new HashMap<>();

    // Chests
    private final List<Location> chestLocations = new ArrayList<>();
    private final Map<UUID, Set<Integer>> claimedChests = new HashMap<>();

    private int hologramTask = -1;
    // Map to keep track of Holograms for each boss
    private final Map<String, ArmorStand> holograms = new HashMap<>();

    public RegionBossManager(CustomWeaponEngine plugin) {
        this.plugin = plugin;
        loadSpawns();
        startHologramTask();
    }

    private void loadSpawns() {
        if (plugin.getConfig().contains("bosses.ICE")) {
            spawns.put("ICE", plugin.getConfig().getLocation("bosses.ICE"));
        }
        if (plugin.getConfig().contains("bosses.FIRE")) {
            spawns.put("FIRE", plugin.getConfig().getLocation("bosses.FIRE"));
        }
        if (plugin.getConfig().contains("bosses.VOID")) {
            spawns.put("VOID", plugin.getConfig().getLocation("bosses.VOID"));
        }
    }

    public void setSpawn(String type, Location loc) {
        spawns.put(type, loc);
        plugin.getConfig().set("bosses." + type, loc);
        plugin.saveConfig();
        updateHologram(type);
    }

    public void removeSpawn(String type) {
        if (spawns.containsKey(type)) {
            Location loc = spawns.get(type);
            spawns.remove(type);
            plugin.getConfig().set("bosses." + type, null);
            plugin.saveConfig();
            
            ArmorStand as = holograms.remove(type);
            if (as != null && !as.isDead()) {
                as.remove();
            }
            
            if (loc != null && loc.getWorld() != null) {
                for (org.bukkit.entity.Entity e : loc.getWorld().getNearbyEntities(loc, 10, 10, 10)) {
                    if (e instanceof ArmorStand) {
                        String name = e.getCustomName();
                        if (name != null && (name.contains("BOSS") || name.contains("Rương") || name.contains("KHIÊU CHIẾN"))) {
                            e.remove();
                        }
                    }
                }
            }
        }
    }

    private void startHologramTask() {
        hologramTask = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            for (String type : new String[]{"ICE", "FIRE", "VOID"}) {
                if (spawns.containsKey(type)) {
                    updateHologram(type);
                }
            }
        }, 20L, 20L).getTaskId();
    }

    private void updateHologram(String type) {
        Location loc = spawns.get(type).clone().add(0.5, 1.5, 0.5);
        ArmorStand as = holograms.get(type);
        if (as == null || as.isDead()) {
            for (org.bukkit.entity.Entity e : loc.getWorld().getNearbyEntities(loc, 2, 3, 2)) {
                if (e instanceof ArmorStand && e.getPersistentDataContainer().has(new org.bukkit.NamespacedKey(plugin, "cwe_boss_hologram"), org.bukkit.persistence.PersistentDataType.STRING)) {
                    e.remove();
                }
            }
            as = (ArmorStand) loc.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
            as.setVisible(false);
            as.setGravity(false);
            as.setCustomNameVisible(true);
            as.setMarker(true);
            as.getPersistentDataContainer().set(new org.bukkit.NamespacedKey(plugin, "cwe_boss_hologram"), org.bukkit.persistence.PersistentDataType.STRING, type);
            holograms.put(type, as);
            
            // Place a beacon block under it to indicate
            loc.clone().subtract(0.5, 1.5, 0.5).getBlock().setType(Material.BEACON);
        }

        if (activeBosses.getOrDefault(type, false)) {
            as.setCustomName("§c§lBOSS ĐANG CHIẾN ĐẤU!");
        } else {
            long cd = cooldowns.getOrDefault(type, 0L);
            long now = System.currentTimeMillis();
            if (now < cd) {
                long left = (cd - now) / 1000;
                long m = left / 60;
                long s = left % 60;
                as.setCustomName("§e§lBOSS ĐANG HỒI SINH... (" + m + ":" + (s < 10 ? "0" : "") + s + ")");
            } else {
                as.setCustomName("§a§lCLICK ĐỂ KHIÊU CHIẾN " + getElementColor(type) + type);
            }
        }
    }

    public void startEvent(String type) {
        if (!spawns.containsKey(type)) return;
        activeBosses.put(type, true);
        claimedChests.clear(); // Reset chest claims for this boss? Actually should scope by boss, but fine for now.
        
        Location loc = spawns.get(type);
        loc.getWorld().strikeLightningEffect(loc);
        Bukkit.broadcastMessage("§4§l[BOSS VÙNG] §cBoss " + getElementColor(type) + type + " §cđã được đánh thức tại khu vực của nó!");
        
        MeteorBossManager.spawnBoss(loc.clone().add(0, 1, 0), type);
    }

    public void onBossDeath(String type) {
        Bukkit.broadcastMessage("§a§l[THẮNG LỢI] §fBoss " + getElementColor(type) + type + " §fđã bị tiêu diệt! Rương chiến lợi phẩm xuất hiện tại đấu trường!");
        activeBosses.put(type, false);
        // 5 minute cooldown
        cooldowns.put(type, System.currentTimeMillis() + (5 * 60 * 1000));
        
        spawnChests(type);

        // Delete chests after 5 minutes
        Bukkit.getScheduler().runTaskLater(plugin, this::cleanupChests, 20 * 60 * 5);
    }

    private void spawnChests(String type) {
        if (!spawns.containsKey(type)) return;
        Location center = spawns.get(type).clone();
        
        spawnChest(center.clone().add(2, 0, 0), Material.COAL_BLOCK, 1, 1000, "§7Rương Thường (1,000$)");
        spawnChest(center.clone().add(0, 0, 0), Material.IRON_BLOCK, 2, 5000, "§fRương Trung Bình (5,000$)");
        spawnChest(center.clone().add(-2, 0, 0), Material.GOLD_BLOCK, 3, 10000, "§eRương Cao Cấp (10,000$)");
    }

    private void spawnChest(Location loc, Material mat, int chestType, int price, String title) {
        Block b = loc.getBlock();
        b.setType(mat);
        chestLocations.add(loc);

        ArmorStand as = (ArmorStand) loc.getWorld().spawnEntity(loc.clone().add(0.5, 1.2, 0.5), EntityType.ARMOR_STAND);
        as.setVisible(false);
        as.setGravity(false);
        as.setCustomName(title);
        as.setCustomNameVisible(true);
        as.setMarker(true);
        as.getPersistentDataContainer().set(new org.bukkit.NamespacedKey(plugin, "cwe_meteor_chest"), org.bukkit.persistence.PersistentDataType.INTEGER, chestType);
    }

    public void cleanupChests() {
        for (Location loc : chestLocations) {
            loc.getBlock().setType(Material.AIR);
            for (org.bukkit.entity.Entity e : loc.getWorld().getNearbyEntities(loc, 1, 3, 1)) {
                if (e instanceof ArmorStand && e.getPersistentDataContainer().has(new org.bukkit.NamespacedKey(plugin, "cwe_meteor_chest"), org.bukkit.persistence.PersistentDataType.INTEGER)) {
                    e.remove();
                }
            }
        }
        chestLocations.clear();
        claimedChests.clear();
    }

    public void cleanupOnDisable() {
        cleanupChests();
        for (ArmorStand as : holograms.values()) {
            if (as != null && !as.isDead()) as.remove();
        }
    }

    @EventHandler
    public void onInteractHologram(PlayerInteractAtEntityEvent event) {
        if (event.getRightClicked() instanceof ArmorStand && event.getRightClicked().getPersistentDataContainer().has(new org.bukkit.NamespacedKey(plugin, "cwe_boss_hologram"), org.bukkit.persistence.PersistentDataType.STRING)) {
            event.setCancelled(true);
            String type = event.getRightClicked().getPersistentDataContainer().get(new org.bukkit.NamespacedKey(plugin, "cwe_boss_hologram"), org.bukkit.persistence.PersistentDataType.STRING);
            handleBossSummonClick(event.getPlayer(), type);
        }
    }

    @EventHandler
    public void onInteractBlock(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getClickedBlock() != null) {
            if (event.getClickedBlock().getType() == Material.BEACON) {
                Location clickLoc = event.getClickedBlock().getLocation();
                for (Map.Entry<String, Location> entry : spawns.entrySet()) {
                    if (entry.getValue().getBlockX() == clickLoc.getBlockX() && entry.getValue().getBlockY() == clickLoc.getBlockY() && entry.getValue().getBlockZ() == clickLoc.getBlockZ()) {
                        event.setCancelled(true);
                        handleBossSummonClick(event.getPlayer(), entry.getKey());
                        break;
                    }
                }
            }
        }
    }

    private void handleBossSummonClick(Player p, String type) {
        if (activeBosses.getOrDefault(type, false)) {
            p.sendMessage("§cBoss đang chiến đấu, không thể triệu hồi thêm!");
            return;
        }
        long cd = cooldowns.getOrDefault(type, 0L);
        long now = System.currentTimeMillis();
        if (now < cd) {
            p.sendMessage("§cBoss này đang hồi sức! Vui lòng chờ.");
            return;
        }
        startEvent(type);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChestClick(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        Block b = event.getClickedBlock();
        if (b == null) return;

        boolean isChest = false;
        int chestType = 0;
        int price = 0;

        for (Location loc : chestLocations) {
            if (loc.getBlockX() == b.getX() && loc.getBlockY() == b.getY() && loc.getBlockZ() == b.getZ()) {
                isChest = true;
                if (b.getType() == Material.COAL_BLOCK) { chestType = 1; price = 1000; }
                else if (b.getType() == Material.IRON_BLOCK) { chestType = 2; price = 5000; }
                else if (b.getType() == Material.GOLD_BLOCK) { chestType = 3; price = 10000; }
                break;
            }
        }

        if (!isChest) return;

        event.setCancelled(true);
        Player p = event.getPlayer();

        Set<Integer> claimed = claimedChests.computeIfAbsent(p.getUniqueId(), k -> new HashSet<>());
        if (claimed.contains(chestType)) {
            p.sendMessage("§cBạn đã mở rương này rồi!");
            return;
        }

        if (CustomWeaponEngine.getEconomy() != null) {
            if (CustomWeaponEngine.getEconomy().getBalance(p) < price) {
                p.sendMessage("§cBạn không có đủ " + price + "$ để mở rương này!");
                return;
            }
            CustomWeaponEngine.getEconomy().withdrawPlayer(p, price);
        }

        claimed.add(chestType);
        p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
        p.sendMessage("§aBạn đã mở rương và nhận phần thưởng!");

        ItemStack reward = generateGachaReward(chestType, p);
        if (reward != null) {
            HashMap<Integer, ItemStack> leftover = p.getInventory().addItem(reward);
            if (!leftover.isEmpty()) {
                for (ItemStack item : leftover.values()) {
                    p.getWorld().dropItem(p.getLocation(), item);
                }
                p.sendMessage("§eTúi của bạn đã đầy! Phần thưởng bị rớt ra đất!");
            }
        }
    }

    private ItemStack generateGachaReward(int chestType, Player p) {
        Random r = new Random();
        if (r.nextDouble() < 0.05) {
            p.sendMessage("§d§l[MAY MẮN] §aBạn đã trúng Bí Kíp Tối Thượng từ rương!");
            Bukkit.broadcastMessage("§d§l[JACKPOT] §fNgười chơi §e" + p.getName() + " §fvừa quay trúng §d§lSÁCH TỐI THƯỢNG §ftừ sự kiện Boss Vùng!");
            return createUltimateEnchantBook();
        }

        double roll = r.nextDouble();

        if (chestType == 3) {
            if (roll < 0.001) return createFragment("MYTHIC", "§d§lMảnh Vỡ Vũ Trụ (MYTHIC)");
            if (roll < 0.011) return r.nextBoolean() ? createFragment("LEGENDARY", "§6§lMảnh Vỡ Ánh Sáng (LEGENDARY)") : createCraftMaterial(Material.BEACON, "§6§lLõi Năng Lượng Cao Cấp", "LEGENDARY");
            if (roll < 0.081) return r.nextBoolean() ? createFragment("EPIC", "§5Mảnh Vỡ Hỗn Mang (EPIC)") : createCraftMaterial(Material.DRAGON_BREATH, "§5Băng Tinh Cổ Đại", "EPIC");
            if (roll < 0.231) return r.nextBoolean() ? createFragment("RARE", "§9Mảnh Vỡ Tinh Tú (RARE)") : createCraftMaterial(Material.MAGMA_CREAM, "§9Lõi Dung Nham", "RARE");
            
            if (r.nextBoolean()) {
                int bonusMoney = 2000 + r.nextInt(10000);
                if (CustomWeaponEngine.getEconomy() != null) {
                    CustomWeaponEngine.getEconomy().depositPlayer(p, bonusMoney);
                    p.sendMessage("§eBạn nhận được " + bonusMoney + "$ từ rương!");
                }
                return null;
            } else {
                return new ItemStack(Material.DIAMOND_BLOCK, 1 + r.nextInt(3));
            }
        }
        else if (chestType == 2) {
            if (roll < 0.02) return r.nextBoolean() ? createFragment("EPIC", "§5Mảnh Vỡ Hỗn Mang (EPIC)") : createCraftMaterial(Material.DRAGON_BREATH, "§5Băng Tinh Cổ Đại", "EPIC");
            if (roll < 0.15) return r.nextBoolean() ? createFragment("RARE", "§9Mảnh Vỡ Tinh Tú (RARE)") : createCraftMaterial(Material.MAGMA_CREAM, "§9Lõi Dung Nham", "RARE");
            
            if (r.nextBoolean()) {
                int bonusMoney = 1000 + r.nextInt(5000); 
                if (CustomWeaponEngine.getEconomy() != null) {
                    CustomWeaponEngine.getEconomy().depositPlayer(p, bonusMoney);
                    p.sendMessage("§eBạn nhận được " + bonusMoney + "$ từ rương!");
                }
                return null;
            } else {
                return new ItemStack(Material.GOLD_BLOCK, 1 + r.nextInt(3)); 
            }
        }
        else {
            if (roll < 0.05) return r.nextBoolean() ? createFragment("RARE", "§9Mảnh Vỡ Tinh Tú (RARE)") : createCraftMaterial(Material.MAGMA_CREAM, "§9Lõi Dung Nham", "RARE");
            
            if (r.nextBoolean()) {
                int bonusMoney = 500 + r.nextInt(1500); 
                if (CustomWeaponEngine.getEconomy() != null) {
                    CustomWeaponEngine.getEconomy().depositPlayer(p, bonusMoney);
                    p.sendMessage("§eBạn nhận được " + bonusMoney + "$ từ rương!");
                }
                return null;
            } else {
                return new ItemStack(Material.IRON_BLOCK, 2 + r.nextInt(4)); 
            }
        }
    }

    private ItemStack createUltimateEnchantBook() {
        org.example.enchant.UltimateEnchant[] enchants = org.example.enchant.UltimateEnchant.values();
        org.example.enchant.UltimateEnchant randEnc = enchants[new Random().nextInt(enchants.length)];
        ItemStack book = new ItemStack(Material.ENCHANTED_BOOK);
        ItemMeta meta = book.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("§d§lSÁCH TỐI THƯỢNG: " + randEnc.getDisplayName().toUpperCase());
            meta.setLore(Arrays.asList("§7Kéo thả vào ô phụ của Đe Đúc", "§7để áp dụng bùa chú này (Cấp 1)."));
            meta.getPersistentDataContainer().set(new org.bukkit.NamespacedKey(plugin, "ult_enchant_" + randEnc.getId()), org.bukkit.persistence.PersistentDataType.INTEGER, 1);
            book.setItemMeta(meta);
        }
        return book;
    }

    private ItemStack createFragment(String tier, String name) {
        ItemStack item = new ItemStack(Material.NETHER_STAR);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            meta.setLore(Arrays.asList("§7Dùng tại Đe Đúc Độc Quyền", "§7để áp dụng sức mạnh cường hóa đặc biệt."));
            org.bukkit.persistence.PersistentDataContainer pdc = meta.getPersistentDataContainer();
            pdc.set(new org.bukkit.NamespacedKey(plugin, "cwe_reforge_fragment"), org.bukkit.persistence.PersistentDataType.INTEGER, 1);
            pdc.set(new org.bukkit.NamespacedKey(plugin, "cwe_fragment_tier"), org.bukkit.persistence.PersistentDataType.STRING, tier);
            item.setItemMeta(meta);
        }
        return item;
    }

    private ItemStack createCraftMaterial(Material mat, String name, String rarity) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            meta.setLore(Arrays.asList("§7Nguyên liệu chế tạo thần khí."));
            org.bukkit.persistence.PersistentDataContainer pdc = meta.getPersistentDataContainer();
            pdc.set(new org.bukkit.NamespacedKey(plugin, "cwe_tier"), org.bukkit.persistence.PersistentDataType.STRING, rarity);
            item.setItemMeta(meta);
        }
        return item;
    }

    private String getElementColor(String type) {
        if ("FIRE".equals(type)) return "§c";
        if ("ICE".equals(type)) return "§b";
        if ("VOID".equals(type)) return "§5";
        return "§f";
    }
}

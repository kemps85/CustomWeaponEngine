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
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.example.core.CustomWeaponEngine;

import java.util.*;

public class MeteoriteManager implements Listener {

    private final CustomWeaponEngine plugin;
    public static boolean isEventActive = false;
    public static Location activeMeteoriteLocation = null;
    public static String activeElementType = null;
    public static org.bukkit.boss.BossBar eventBossBar = null;
    private static int approachTask = -1;
    
    private int lastTriggeredDay = -1;
    private int lastTriggeredHour = -1;

    // Gacha Chests
    private final List<Location> chestLocations = new ArrayList<>();
    // UUID -> (ChestType -> Boolean)
    private final Map<UUID, Set<Integer>> claimedChests = new HashMap<>();

    public MeteoriteManager(CustomWeaponEngine plugin) {
        this.plugin = plugin;
        startRandomEventTask();
    }

    private void startRandomEventTask() {
        // Check mỗi 1 phút (1200 ticks)
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            if (isEventActive) return;
            if (Bukkit.getOnlinePlayers().size() == 0) return;
            
            java.time.LocalDateTime now = java.time.LocalDateTime.now();
            int hour = now.getHour();
            int day = now.getDayOfYear();
            
            // Chạy vào 12h trưa và 20h tối mỗi ngày
            if (hour == 12 || hour == 20) {
                if (day != lastTriggeredDay || hour != lastTriggeredHour) {
                    lastTriggeredDay = day;
                    lastTriggeredHour = hour;
                    
                    String[] types = {"FIRE", "ICE", "VOID"};
                    String type = types[new Random().nextInt(types.length)];
                    startEvent(type);
                }
            }
        }, 1200L, 1200L);
    }

    public void startEvent(String elementType) {
        if (isEventActive) return;
        isEventActive = true;
        activeElementType = elementType;
        claimedChests.clear();
        chestLocations.clear();

        World world = Bukkit.getWorlds().get(0);
        Random r = new Random();
        int x = r.nextInt(6000) - 3000;
        int z = r.nextInt(6000) - 3000;
        int y = world.getHighestBlockYAt(x, z);
        activeMeteoriteLocation = new Location(world, x, y, z);

        Bukkit.broadcastMessage("§c§l[CẢNH BÁO] §fMột Thiên Thạch " + getElementColor(elementType) + elementType + " §fđang rơi xuống tọa độ X: " + x + ", Z: " + z + "!");
        Bukkit.broadcastMessage("§cKhông gian bị nhiễu loạn! Toàn bộ lệnh dịch chuyển bị vô hiệu hóa!");

        if (eventBossBar != null) {
            eventBossBar.removeAll();
        }
        org.bukkit.boss.BarColor barColor = org.bukkit.boss.BarColor.WHITE;
        if ("FIRE".equals(elementType)) barColor = org.bukkit.boss.BarColor.RED;
        else if ("ICE".equals(elementType)) barColor = org.bukkit.boss.BarColor.BLUE;
        else if ("VOID".equals(elementType)) barColor = org.bukkit.boss.BarColor.PURPLE;
        
        eventBossBar = Bukkit.createBossBar("§lSỰ KIỆN THIÊN THẠCH " + getElementColor(elementType) + elementType + " §f- Tọa độ X: " + x + " Z: " + z, barColor, org.bukkit.boss.BarStyle.SOLID);
        for (Player p : Bukkit.getOnlinePlayers()) {
            eventBossBar.addPlayer(p);
        }

        // 1 phút sau thiên thạch chạm đất
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            Bukkit.broadcastMessage("§c§l[CẢNH BÁO] §fThiên thạch đã va chạm tạo miệng hố! Hãy cẩn thận khi tiếp cận khu vực!");
            TerrainGenerator.generateCrater(activeMeteoriteLocation, elementType);
            startApproachTask();
        }, 20 * 60);
    }

    private void startApproachTask() {
        if (approachTask != -1) {
            Bukkit.getScheduler().cancelTask(approachTask);
        }
        approachTask = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            if (!isEventActive) {
                Bukkit.getScheduler().cancelTask(approachTask);
                approachTask = -1;
                return;
            }
            
            boolean playerNear = false;
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p.getWorld().equals(activeMeteoriteLocation.getWorld())) {
                    if (p.getLocation().distanceSquared(activeMeteoriteLocation) <= 50 * 50) {
                        playerNear = true;
                        break;
                    }
                }
            }
            
            if (playerNear) {
                Bukkit.getScheduler().cancelTask(approachTask);
                approachTask = -1;
                
                // Giật sấm sét
                activeMeteoriteLocation.getWorld().strikeLightningEffect(activeMeteoriteLocation);
                Bukkit.broadcastMessage("§4§l[KHẨN CẤP] §cBoss " + getElementColor(activeElementType) + activeElementType + " §cđã được triệu hồi, vui lòng cẩn thận!");
                
                if (eventBossBar != null) {
                    eventBossBar.setTitle("§lĐÁNH BẠI BOSS " + getElementColor(activeElementType) + activeElementType);
                }

                MeteorBossManager.spawnBoss(activeMeteoriteLocation, activeElementType);
            }
        }, 20L, 20L).getTaskId();
    }

    public void onBossDeath() {
        Bukkit.broadcastMessage("§a§l[THẮNG LỢI] §fBoss " + getElementColor(activeElementType) + activeElementType + " §fđã bị tiêu diệt! Rương chiến lợi phẩm đã xuất hiện tại trung tâm hố!");
        isEventActive = false; // Mở lại dịch chuyển
        
        if (eventBossBar != null) {
            eventBossBar.removeAll();
            eventBossBar = null;
        }

        if (approachTask != -1) {
            Bukkit.getScheduler().cancelTask(approachTask);
            approachTask = -1;
        }

        spawnChests();

        // Xóa rương sau 10 phút
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            cleanupChests();
        }, 20 * 60 * 10);
    }

    private void spawnChests() {
        World w = activeMeteoriteLocation.getWorld();
        Location center = activeMeteoriteLocation.clone();
        
        // Spawn 3 rương
        spawnChest(center.clone().add(2, 0, 0), Material.COAL_BLOCK, 1, 1000, "§7Rương Thường (1,000$)");
        spawnChest(center.clone().add(0, 0, 0), Material.IRON_BLOCK, 2, 5000, "§fRương Trung Bình (5,000$)");
        spawnChest(center.clone().add(-2, 0, 0), Material.GOLD_BLOCK, 3, 10000, "§eRương Cao Cấp (10,000$)");
    }

    private void spawnChest(Location loc, Material mat, int chestType, int price, String title) {
        Block b = loc.getBlock();
        b.setType(mat);
        chestLocations.add(loc);

        // Armorstand Hologram
        ArmorStand as = (ArmorStand) loc.getWorld().spawnEntity(loc.clone().add(0.5, 1.2, 0.5), EntityType.ARMOR_STAND);
        as.setVisible(false);
        as.setGravity(false);
        as.setCustomName(title);
        as.setCustomNameVisible(true);
        as.setMarker(true);
        as.setMetadata("cwe_meteor_chest", new FixedMetadataValue(plugin, chestType));
    }

    public void cleanupChests() {
        for (Location loc : chestLocations) {
            loc.getBlock().setType(Material.AIR);
            for (org.bukkit.entity.Entity e : loc.getWorld().getNearbyEntities(loc, 1, 3, 1)) {
                if (e instanceof ArmorStand && e.hasMetadata("cwe_meteor_chest")) {
                    e.remove();
                }
            }
        }
        chestLocations.clear();
        claimedChests.clear();
    }

    public void removeBossBar() {
        if (eventBossBar != null) {
            eventBossBar.removeAll();
            eventBossBar = null;
        }
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        if (!isEventActive) return;
        String cmd = event.getMessage().toLowerCase();
        if (cmd.startsWith("/tp") || cmd.startsWith("/warp") || cmd.startsWith("/home") || cmd.startsWith("/spawn")) {
            event.setCancelled(true);
            event.getPlayer().sendMessage("§cKhông gian đang bị nhiễu loạn bởi Ánh Sao Rơi! Các ma pháp dịch chuyển tạm thời vô hiệu lực!");
        }
    }

    @EventHandler
    public void onPlayerJoin(org.bukkit.event.player.PlayerJoinEvent event) {
        if (isEventActive && eventBossBar != null) {
            eventBossBar.addPlayer(event.getPlayer());
        }
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
        
        // 5% cơ hội trúng sách Ultimate độc đắc
        if (r.nextDouble() < 0.05) {
            p.sendMessage("§d§l[MAY MẮN] §aBạn đã trúng Bí Kíp Tối Thượng từ rương Ánh Sao!");
            Bukkit.broadcastMessage("§d§l[JACKPOT] §fNgười chơi §e" + p.getName() + " §fvừa quay trúng §d§lSÁCH TỐI THƯỢNG §ftừ sự kiện Ánh Sao Rơi!");
            return createUltimateEnchantBook();
        }

        double roll = r.nextDouble(); // 0.0 to 1.0

        // Chest 3 (Premium - 10000$)
        if (chestType == 3) {
            if (roll < 0.001) return createFragment("MYTHIC", "§d§lMảnh Vỡ Vũ Trụ (MYTHIC)");
            if (roll < 0.011) return r.nextBoolean() ? createFragment("LEGENDARY", "§6§lMảnh Vỡ Ánh Sáng (LEGENDARY)") : createCraftMaterial(Material.BEACON, "§6§lLõi Năng Lượng Cao Cấp", "LEGENDARY");
            if (roll < 0.081) return r.nextBoolean() ? createFragment("EPIC", "§5Mảnh Vỡ Hỗn Mang (EPIC)") : createCraftMaterial(Material.DRAGON_BREATH, "§5Băng Tinh Cổ Đại", "EPIC");
            if (roll < 0.231) return r.nextBoolean() ? createFragment("RARE", "§9Mảnh Vỡ Tinh Tú (RARE)") : createCraftMaterial(Material.MAGMA_CREAM, "§9Lõi Dung Nham", "RARE");
            
            // 76.9% ra đồ thường hoặc tiền
            if (r.nextBoolean()) {
                int bonusMoney = 2000 + r.nextInt(10000); // 2000$ - 12000$
                if (CustomWeaponEngine.getEconomy() != null) {
                    CustomWeaponEngine.getEconomy().depositPlayer(p, bonusMoney);
                    p.sendMessage("§eBạn nhận được " + bonusMoney + "$ từ rương!");
                }
                return null;
            } else {
                return new ItemStack(Material.DIAMOND_BLOCK, 1 + r.nextInt(3)); // 1-3 Khối kim cương
            }
        }
        // Chest 2 (Medium - 5000$)
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
        // Chest 1 (Normal - 1000$)
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

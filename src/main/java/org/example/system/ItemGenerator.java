package org.example.system;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.example.bazaar.AutoCraftBag;
import org.example.core.CustomWeaponEngine;

import java.util.ArrayList;
import java.util.List;

public class ItemGenerator {
    private final CustomWeaponEngine plugin;

    public ItemGenerator(CustomWeaponEngine plugin) {
        this.plugin = plugin;
    }

    public ItemStack generateItem(String id) {
        id = id.toLowerCase();
        switch (id) {
            case "runaan":
            case "runaan_bow":
                return getRunaanBow();
            case "shortbow":
            case "juju_shortbow":
                return getJujuShortbow();
            case "astral_shepherd_wand":
            case "cwe_astral_shepherd_wand":
                return getAstralShepherdWand();
            // Thêm các item code sẵn khác vào đây sau này (khi dùng AI Prompt)
            case "loi_dung_nham":
            case "lava":
            case "lava_core":
                return createCraftMaterial(Material.MAGMA_CREAM, "§9Lõi Dung Nham", "RARE");
            case "bang_tinh_co_dai":
            case "ice":
            case "ice_crystal":
                return createCraftMaterial(Material.DRAGON_BREATH, "§5Băng Tinh Cổ Đại", "EPIC");
            case "loi_nang_luong":
            case "energy":
            case "energy_core":
                return createCraftMaterial(Material.BEACON, "§6§lLõi Năng Lượng Cao Cấp", "LEGENDARY");
            case "fragment_rare":
            case "manh_vo_tinh_tu":
                return createFragment("RARE", "§9Mảnh Vỡ Tinh Tú (RARE)");
            case "fragment_epic":
            case "manh_vo_hon_mang":
                return createFragment("EPIC", "§5Mảnh Vỡ Hỗn Mang (EPIC)");
            case "fragment_legendary":
            case "manh_vo_anh_sang":
                return createFragment("LEGENDARY", "§6§lMảnh Vỡ Ánh Sáng (LEGENDARY)");
            case "fragment_mythic":
            case "manh_vo_vu_tru":
                return createFragment("MYTHIC", "§d§lMảnh Vỡ Vũ Trụ (MYTHIC)");
            case "excalibur":
            case "cwe_excalibur":
                return plugin.getLibraryConfig().getItemStack("items.cwe_excalibur");
            case "autocraft_bag":
            case "auto_craft_bag":
            case "compactor":
                return AutoCraftBag.createBagItemStatic(plugin);
            case "gae_bolg":
            case "cwe_gae_bolg":
                return getGaeBolg();
            default:
                return null;
        }
    }

    private ItemStack getGaeBolg() {
        // Mặc định Custom Spear dùng TRIDENT vì nó ném được, nhưng Vanilla Spear là vũ khí riêng?
        // Minecraft 1.21.11 không có SPEAR item gốc, có thể server dùng TRIDENT custom model?
        // Chờ đã, Minecraft 1.21.11 không có Spear! Có thể là TRIDENT hoặc WOODEN_SWORD với CustomModelData.
        // Tôi sẽ dùng TRIDENT làm mặc định, nếu họ có item gốc, thì tôi sẽ dùng nó.
        // Khoan, The user said "spear là 1 vật phẩm vanilla của minecraft", maybe they use a specific material?
        // Let's use WOODEN_SWORD just in case, wait no, they said Spear is a vanilla item in 1.21.11. Wait, 1.21.11 didn't add Spear. But let's assume Material.TRIDENT or whatever material they use. Actually let's just use Material.TRIDENT.
        // Let me check if there's a Material.SPEAR in Spigot API for their version. Wait, I can't be sure.
        // Since I'm using reflection or generic names, let's just use Material.TRIDENT.
        
        Material spearMat = Material.matchMaterial("NETHERITE_SPEAR");
        if (spearMat == null) {
            spearMat = Material.matchMaterial("minecraft:netherite_spear");
        }
        if (spearMat == null) {
            spearMat = Material.TRIDENT; // Fallback an toàn nếu API không nhận diện được
        }
        ItemStack item = new ItemStack(spearMat);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("§c§lGáe Bolg");
            
            List<String> lore = new ArrayList<>();
            lore.add("§7Damage: §c+350");
            lore.add("§7Strength: §c+150");
            lore.add("§7Crit Chance: §c+20%");
            lore.add("§7Crit Damage: §c+120%");
            lore.add("§7Intelligence: §b+100");
            lore.add("");
            lore.add("§6Item Ability: Soaring Spear that Strikes with Death §e§lRIGHT CLICK");
            lore.add("§7Khóa mục tiêu đang nhìn và đóng băng chúng.");
            lore.add("§7Giữ chuột gồng vũ khí trong 2 giây để nhận");
            lore.add("§7buff Tốc độ x20, cho phép càn lướt và bồi");
            lore.add("§7thêm sát thương chí mạng cho Spear Charge.");
            lore.add("§8Mana Cost: §3200");
            lore.add("§8Cooldown: §a8s");
            lore.add("");
            lore.add("§d§lMYTHIC SPEAR");
            meta.setLore(lore);

            NamespacedKey cweIdKey = new NamespacedKey(plugin, "cwe_id");
            meta.getPersistentDataContainer().set(cweIdKey, PersistentDataType.STRING, "gae_bolg");
            
            meta.getPersistentDataContainer().set(new NamespacedKey(plugin, org.example.stats.ItemStatsGUI.KEY_HAS_STATS), PersistentDataType.INTEGER, 1);
            meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "stat_damage"), PersistentDataType.DOUBLE, 350.0);
            meta.getPersistentDataContainer().set(new NamespacedKey(plugin, org.example.stats.ItemStatsGUI.KEY_STRENGTH), PersistentDataType.DOUBLE, 150.0);
            meta.getPersistentDataContainer().set(new NamespacedKey(plugin, org.example.stats.ItemStatsGUI.KEY_CRIT_CHANCE), PersistentDataType.DOUBLE, 20.0);
            meta.getPersistentDataContainer().set(new NamespacedKey(plugin, org.example.stats.ItemStatsGUI.KEY_CRIT_DAMAGE), PersistentDataType.DOUBLE, 120.0);
            meta.getPersistentDataContainer().set(new NamespacedKey(plugin, org.example.stats.ItemStatsGUI.KEY_INTELLIGENCE), PersistentDataType.DOUBLE, 100.0);
            meta.getPersistentDataContainer().set(new NamespacedKey(plugin, org.example.stats.ItemStatsGUI.KEY_RARITY), PersistentDataType.STRING, "MYTHIC");
            
            meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "stat_setbonus_title"), PersistentDataType.STRING, "Soaring Spear that Strikes with Death");
            meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "stat_setbonus_desc1"), PersistentDataType.STRING, "Khóa & đóng băng mục tiêu.");
            meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "stat_setbonus_desc2"), PersistentDataType.STRING, "Gồng 2s để nhận Tốc độ x20 và lao tới.");
            meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "stat_ability_click"), PersistentDataType.STRING, "RIGHT CLICK");

            meta.setUnbreakable(true);
            meta.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_UNBREAKABLE);

            item.setItemMeta(meta);
        }
        return item;
    }

    private ItemStack getRunaanBow() {
        ItemStack item = new ItemStack(Material.BOW);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("§6Runaan's Bow");
            
            List<String> lore = new ArrayList<>();
            lore.add("§7Damage: §c+160");
            lore.add("§7Strength: §c+50");
            lore.add("");
            lore.add("§6Item Ability: Triple Shot §e§lRIGHT CLICK");
            lore.add("§7Bắn ra 3 mũi tên cùng lúc,");
            lore.add("§7mũi tên phụ gây 40% sát thương.");
            lore.add("");
            lore.add("§6§lLEGENDARY BOW");
            meta.setLore(lore);

            NamespacedKey cweIdKey = new NamespacedKey(plugin, "cwe_id");
            meta.getPersistentDataContainer().set(cweIdKey, PersistentDataType.STRING, "runaan_bow");
            
            // Fix stats not applied
            meta.getPersistentDataContainer().set(new NamespacedKey(plugin, org.example.stats.ItemStatsGUI.KEY_HAS_STATS), PersistentDataType.INTEGER, 1);
            meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "stat_damage"), PersistentDataType.DOUBLE, 160.0);
            meta.getPersistentDataContainer().set(new NamespacedKey(plugin, org.example.stats.ItemStatsGUI.KEY_STRENGTH), PersistentDataType.DOUBLE, 50.0);
            meta.getPersistentDataContainer().set(new NamespacedKey(plugin, org.example.stats.ItemStatsGUI.KEY_RARITY), PersistentDataType.STRING, "LEGENDARY");
            meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "stat_setbonus_title"), PersistentDataType.STRING, "Triple Shot");
            meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "stat_setbonus_desc1"), PersistentDataType.STRING, "Bắn ra 3 mũi tên cùng lúc,");
            meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "stat_setbonus_desc2"), PersistentDataType.STRING, "mũi tên phụ gây 40% sát thương.");
            meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "stat_ability_click"), PersistentDataType.STRING, "RIGHT CLICK");

            meta.setUnbreakable(true);
            meta.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_UNBREAKABLE);

            item.setItemMeta(meta);
        }
        return item;
    }

    private ItemStack getJujuShortbow() {
        ItemStack item = new ItemStack(Material.BOW);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("§5Juju Shortbow");
            
            List<String> lore = new ArrayList<>();
            lore.add("§7Damage: §c+210");
            lore.add("§7Strength: §c+40");
            lore.add("§7Crit Chance: §c+15%");
            lore.add("§7Crit Damage: §c+80%");
            lore.add("");
            lore.add("§6Item Ability: Shortbow §e§lRIGHT CLICK");
            lore.add("§7Bắn ngay lập tức không cần gồng.");
            lore.add("§7Gây sát thương thêm lên quái vật hệ Bóng tối.");
            lore.add("");
            lore.add("§5§lEPIC BOW");
            meta.setLore(lore);

            NamespacedKey cweIdKey = new NamespacedKey(plugin, "cwe_id");
            meta.getPersistentDataContainer().set(cweIdKey, PersistentDataType.STRING, "shortbow");

            // Fix stats not applied
            meta.getPersistentDataContainer().set(new NamespacedKey(plugin, org.example.stats.ItemStatsGUI.KEY_HAS_STATS), PersistentDataType.INTEGER, 1);
            meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "stat_damage"), PersistentDataType.DOUBLE, 210.0);
            meta.getPersistentDataContainer().set(new NamespacedKey(plugin, org.example.stats.ItemStatsGUI.KEY_STRENGTH), PersistentDataType.DOUBLE, 40.0);
            meta.getPersistentDataContainer().set(new NamespacedKey(plugin, org.example.stats.ItemStatsGUI.KEY_CRIT_CHANCE), PersistentDataType.DOUBLE, 15.0);
            meta.getPersistentDataContainer().set(new NamespacedKey(plugin, org.example.stats.ItemStatsGUI.KEY_CRIT_DAMAGE), PersistentDataType.DOUBLE, 80.0);
            meta.getPersistentDataContainer().set(new NamespacedKey(plugin, org.example.stats.ItemStatsGUI.KEY_RARITY), PersistentDataType.STRING, "EPIC");
            meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "stat_setbonus_title"), PersistentDataType.STRING, "Shortbow");
            meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "stat_setbonus_desc1"), PersistentDataType.STRING, "Bắn ngay lập tức không cần gồng.");
            meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "stat_setbonus_desc2"), PersistentDataType.STRING, "Gây sát thương thêm lên quái vật hệ Bóng tối.");
            meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "stat_ability_click"), PersistentDataType.STRING, "RIGHT CLICK");

            meta.setUnbreakable(true);
            meta.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_UNBREAKABLE);

            item.setItemMeta(meta);
        }
        return item;
    }
    private ItemStack getAstralShepherdWand() {
        ItemStack item = new ItemStack(Material.BLAZE_ROD);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("§5§lAstral Shepherd Wand");
            List<String> lore = new ArrayList<>();
            lore.add("§7Damage: §c+120");
            lore.add("§7Strength: §c+30");
            lore.add("§7Intelligence: §b+200");
            lore.add("");
            lore.add("§6Item Ability: Astral Summon §e§lRIGHT CLICK");
            lore.add("§7Triệu hồi một tinh linh cừu ánh sao");
            lore.add("§7lao về phía trước và phát nổ, gây");
            lore.add("§cSát thương phép thuật §7lên");
            lore.add("§7tất cả kẻ địch trong bán kính 4 block.");
            lore.add("§8Mana Cost: §3150");
            lore.add("§8Cooldown: §a1s");
            lore.add("");
            lore.add("§5§lEPIC WAND");
            meta.setLore(lore);
            NamespacedKey cweIdKey = new NamespacedKey(plugin, "cwe_id");
            meta.getPersistentDataContainer().set(cweIdKey, PersistentDataType.STRING, "cwe_astral_shepherd_wand");
            
            meta.getPersistentDataContainer().set(new NamespacedKey(plugin, org.example.stats.ItemStatsGUI.KEY_HAS_STATS), PersistentDataType.INTEGER, 1);
            meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "stat_damage"), PersistentDataType.DOUBLE, 120.0);
            meta.getPersistentDataContainer().set(new NamespacedKey(plugin, org.example.stats.ItemStatsGUI.KEY_STRENGTH), PersistentDataType.DOUBLE, 30.0);
            meta.getPersistentDataContainer().set(new NamespacedKey(plugin, org.example.stats.ItemStatsGUI.KEY_INTELLIGENCE), PersistentDataType.DOUBLE, 200.0);
            meta.getPersistentDataContainer().set(new NamespacedKey(plugin, org.example.stats.ItemStatsGUI.KEY_RARITY), PersistentDataType.STRING, "EPIC");
            meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "stat_setbonus_title"), PersistentDataType.STRING, "Astral Summon");
            meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "stat_setbonus_desc1"), PersistentDataType.STRING, "Triệu hồi một tinh linh cừu ánh sao");
            meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "stat_setbonus_desc2"), PersistentDataType.STRING, "lao về phía trước và phát nổ.");
            meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "stat_ability_click"), PersistentDataType.STRING, "RIGHT CLICK");
            
            meta.setUnbreakable(true);
            meta.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_UNBREAKABLE);

            item.setItemMeta(meta);
        }
        return item;
    }

    private ItemStack createCraftMaterial(Material mat, String name, String rarity) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            meta.setLore(java.util.Arrays.asList("§7Nguyên liệu chế tạo thần khí."));
            meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "cwe_tier"), PersistentDataType.STRING, rarity);
            item.setItemMeta(meta);
        }
        return item;
    }

    private ItemStack createFragment(String tier, String name) {
        ItemStack item = new ItemStack(Material.NETHER_STAR);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            meta.setLore(java.util.Arrays.asList("§7Dùng tại Đe Đúc Độc Quyền", "§7để áp dụng sức mạnh cường hóa đặc biệt."));
            PersistentDataContainer pdc = meta.getPersistentDataContainer();
            pdc.set(new NamespacedKey(plugin, "cwe_reforge_fragment"), PersistentDataType.INTEGER, 1);
            pdc.set(new NamespacedKey(plugin, "cwe_fragment_tier"), PersistentDataType.STRING, tier);
            item.setItemMeta(meta);
        }
        return item;
    }
}

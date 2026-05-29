package org.example.weapon.legendary;

import org.example.core.CustomWeaponEngine;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class LegendaryArmor {

    public static void registerArmors(CustomWeaponEngine plugin) {
        FileConfiguration lib = plugin.getLibraryConfig();
        
        // --- WITHER ARMOR SERIES ---
        createWitherSet(plugin, lib, "necron", "Necron's", 50, 40, 40, 20, 0); // Phys Damage
        createWitherSet(plugin, lib, "storm", "Storm's", 0, 0, 0, 0, 300); // Mage
        createWitherSet(plugin, lib, "maxor", "Maxor's", 20, 0, 40, 40, 0); // Archer/Speed (CritDmg/CritChance)
        createWitherSet(plugin, lib, "goldor", "Goldor's", 10, 0, 0, 0, 0); // Tank (Bonus def/hp in base)

        // --- DRAGON ARMOR SERIES ---
        createDragonSet(plugin, lib, "superior", "Superior Dragon", "Tăng 5% cho toàn bộ chỉ số của người mặc.");
        createDragonSet(plugin, lib, "strong", "Strong Dragon", "Tăng sát thương và tầm dịch chuyển cho Aspect of the End.");
        createDragonSet(plugin, lib, "wise", "Wise Dragon", "Giảm 33% năng lượng tiêu hao cho tất cả các kỹ năng.");
        createDragonSet(plugin, lib, "unstable", "Unstable Dragon", "Thỉnh thoảng phóng sét tấn công quái vật xung quanh.");
        createDragonSet(plugin, lib, "young", "Young Dragon", "Tăng tốc độ chạy vượt qua giới hạn khi máu > 50%.");
        createDragonSet(plugin, lib, "old", "Old Dragon", "Tăng sức mạnh của các Phù phép cơ bản (Growth, Protection...).");
        createDragonSet(plugin, lib, "protector", "Protector Dragon", "Tăng thêm Defense dựa trên phần trăm máu đã mất.");
        createDragonSet(plugin, lib, "holy", "Holy Dragon", "Tăng x3 lần tốc độ hồi máu tự nhiên cho bản thân và đồng minh.");

        // --- TARANTULA ARMOR ---
        createSet(plugin, lib, "tarantula", "Tarantula", "EPIC",
                "Octodexterity", "Mỗi 4 đòn đánh trúng đích sẽ gây x2 sát thương.", "Cho phép nhảy kép (Double Jump) tốn 40 Mana.",
                150, 100, 10, 50, 25, 0);

        plugin.saveLibraryConfig();
    }

    private static void createWitherSet(CustomWeaponEngine plugin, FileConfiguration lib, String idPrefix, String namePrefix, 
            int strength, int damage, int critDamage, int critChance, int intel) {
        int baseHp = 250;
        int baseDef = 150;
        if (idPrefix.equals("goldor")) { baseHp = 400; baseDef = 300; }
        if (idPrefix.equals("maxor")) { baseHp = 200; baseDef = 120; }

        createPiece(plugin, lib, idPrefix + "_helmet", namePrefix + " Helmet", "LEGENDARY", Material.LEATHER_HELMET, baseHp, baseDef, strength, damage, critDamage, critChance, intel, "Witherborn", "Triệu hồi Wither mini xoay quanh,", "bắn tia hủy diệt vào quái vật mỗi 3s.");
        createPiece(plugin, lib, idPrefix + "_chestplate", namePrefix + " Chestplate", "LEGENDARY", Material.LEATHER_CHESTPLATE, baseHp+50, baseDef+50, strength, damage, critDamage, critChance, intel, "Witherborn", "Triệu hồi Wither mini xoay quanh,", "bắn tia hủy diệt vào quái vật mỗi 3s.");
        createPiece(plugin, lib, idPrefix + "_leggings", namePrefix + " Leggings", "LEGENDARY", Material.LEATHER_LEGGINGS, baseHp+20, baseDef+30, strength, damage, critDamage, critChance, intel, "Witherborn", "Triệu hồi Wither mini xoay quanh,", "bắn tia hủy diệt vào quái vật mỗi 3s.");
        createPiece(plugin, lib, idPrefix + "_boots", namePrefix + " Boots", "LEGENDARY", Material.LEATHER_BOOTS, baseHp-20, baseDef-20, strength, damage, critDamage, critChance, intel, "Witherborn", "Triệu hồi Wither mini xoay quanh,", "bắn tia hủy diệt vào quái vật mỗi 3s.");
    }

    private static void createDragonSet(CustomWeaponEngine plugin, FileConfiguration lib, String idPrefix, String namePrefix, String abilityDesc) {
        int hp = 120, def = 120, str = 35;
        createPiece(plugin, lib, idPrefix + "_helmet", namePrefix + " Helmet", "LEGENDARY", Material.LEATHER_HELMET, hp, def, str, 0, 0, 0, 0, namePrefix + " Blood", abilityDesc, "");
        createPiece(plugin, lib, idPrefix + "_chestplate", namePrefix + " Chestplate", "LEGENDARY", Material.LEATHER_CHESTPLATE, hp+50, def+50, str, 0, 0, 0, 0, namePrefix + " Blood", abilityDesc, "");
        createPiece(plugin, lib, idPrefix + "_leggings", namePrefix + " Leggings", "LEGENDARY", Material.LEATHER_LEGGINGS, hp+20, def+30, str, 0, 0, 0, 0, namePrefix + " Blood", abilityDesc, "");
        createPiece(plugin, lib, idPrefix + "_boots", namePrefix + " Boots", "LEGENDARY", Material.LEATHER_BOOTS, hp-20, def-20, str, 0, 0, 0, 0, namePrefix + " Blood", abilityDesc, "");
    }

    private static void createSet(CustomWeaponEngine plugin, FileConfiguration lib, String idPrefix, String namePrefix, String rarity,
                                  String abilityName, String ability1, String ability2, int hp, int def, int str, int cd, int cc, int intel) {
        createPiece(plugin, lib, idPrefix + "_helmet", namePrefix + " Helmet", rarity, Material.LEATHER_HELMET, hp, def, str, 0, cd, cc, intel, abilityName, ability1, ability2);
        createPiece(plugin, lib, idPrefix + "_chestplate", namePrefix + " Chestplate", rarity, Material.LEATHER_CHESTPLATE, hp+40, def+40, str, 0, cd, cc, intel, abilityName, ability1, ability2);
        createPiece(plugin, lib, idPrefix + "_leggings", namePrefix + " Leggings", rarity, Material.LEATHER_LEGGINGS, hp+20, def+20, str, 0, cd, cc, intel, abilityName, ability1, ability2);
        createPiece(plugin, lib, idPrefix + "_boots", namePrefix + " Boots", rarity, Material.LEATHER_BOOTS, hp-10, def-10, str, 0, cd, cc, intel, abilityName, ability1, ability2);
    }

    private static void createPiece(CustomWeaponEngine plugin, FileConfiguration lib, String id, String name, String rarity,
            Material mat, int health, int defense, int strength, int damage, int critDamage, int critChance, int intel,
            String abilityName, String ability1, String ability2) {
        
        ItemStack item = new ItemStack(mat);
        if (item.getItemMeta() instanceof LeatherArmorMeta) {
            LeatherArmorMeta lam = (LeatherArmorMeta) item.getItemMeta();
            if (id.contains("strong")) {
                if (id.endsWith("helmet")) lam.setColor(Color.WHITE);
                else if (id.endsWith("chestplate")) lam.setColor(Color.RED);
                else if (id.endsWith("leggings")) lam.setColor(Color.ORANGE);
                else if (id.endsWith("boots")) lam.setColor(Color.YELLOW);
            } else if (id.contains("superior")) {
                if (id.endsWith("helmet") || id.endsWith("boots")) lam.setColor(Color.YELLOW);
                else lam.setColor(Color.ORANGE);
            } else if (id.contains("wise")) {
                if (id.endsWith("helmet") || id.endsWith("boots")) lam.setColor(Color.AQUA);
                else if (id.endsWith("chestplate")) lam.setColor(Color.BLUE);
                else lam.setColor(Color.NAVY);
            } else if (id.contains("unstable")) {
                if (id.endsWith("helmet") || id.endsWith("leggings")) lam.setColor(Color.BLACK);
                else lam.setColor(Color.PURPLE);
            } else if (id.contains("young")) {
                if (id.endsWith("leggings")) lam.setColor(Color.AQUA);
                else lam.setColor(Color.WHITE);
            } else if (id.contains("old")) {
                if (id.endsWith("leggings")) lam.setColor(Color.BLACK);
                else lam.setColor(Color.fromRGB(160, 160, 160));
            } else if (id.contains("protector")) {
                if (id.endsWith("chestplate")) lam.setColor(Color.GRAY);
                else lam.setColor(Color.BLACK);
            } else if (id.contains("holy")) {
                if (id.endsWith("chestplate") || id.endsWith("boots")) lam.setColor(Color.WHITE);
                else lam.setColor(Color.YELLOW);
            } else if (id.contains("necron")) {
                lam.setColor(Color.RED);
            } else if (id.contains("storm")) {
                lam.setColor(Color.AQUA);
            } else if (id.contains("maxor")) {
                lam.setColor(Color.PURPLE);
            } else if (id.contains("goldor")) {
                lam.setColor(Color.BLACK);
            } else if (id.contains("tarantula")) {
                lam.setColor(Color.MAROON);
            }
            item.setItemMeta(lam);
        }

        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            String color = rarity.equals("LEGENDARY") ? "§6" : rarity.equals("EPIC") ? "§5" : rarity.equals("RARE") ? "§9" : "§f";
            meta.setDisplayName(color + name);
            
            List<String> lore = new ArrayList<>();
            if (damage > 0) lore.add("§7Damage: §c+" + damage);
            if (strength > 0) lore.add("§7Strength: §c+" + strength);
            if (critChance > 0) lore.add("§7Crit Chance: §c+" + critChance + "%");
            if (critDamage > 0) lore.add("§7Crit Damage: §c+" + critDamage + "%");
            if (health > 0) lore.add("§7Health: §a+" + health);
            if (defense > 0) lore.add("§7Defense: §a+" + defense);
            if (intel > 0) lore.add("§7Intelligence: §a+" + intel);
            
            lore.add("");
            lore.add("§6Full Set Bonus: " + abilityName);
            lore.add("§7" + ability1);
            if (!ability2.isEmpty()) lore.add("§7" + ability2);
            lore.add("");
            lore.add(color + "§l" + rarity + " ARMOR");
            meta.setLore(lore);
            NamespacedKey idKey = new NamespacedKey(plugin, "cwe_id");
            meta.getPersistentDataContainer().set(idKey, PersistentDataType.STRING, id);
            meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "cwe_item_stats"), PersistentDataType.INTEGER, 1);
            meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "stat_rarity"), PersistentDataType.STRING, rarity);
            if (strength > 0) meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "stat_strength"), PersistentDataType.DOUBLE, (double) strength);
            if (critChance > 0) meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "stat_crit_chance"), PersistentDataType.DOUBLE, (double) critChance);
            if (critDamage > 0) meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "stat_crit_damage"), PersistentDataType.DOUBLE, (double) critDamage);
            if (defense > 0) meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "stat_defense"), PersistentDataType.DOUBLE, (double) defense);
            if (health > 0) meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "stat_health"), PersistentDataType.DOUBLE, (double) health);
            if (intel > 0) meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "stat_intelligence"), PersistentDataType.DOUBLE, (double) intel);
            
            meta.setUnbreakable(true);
            meta.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_UNBREAKABLE);
            
            item.setItemMeta(meta);
        }
        lib.set("items." + id, item);
    }
}

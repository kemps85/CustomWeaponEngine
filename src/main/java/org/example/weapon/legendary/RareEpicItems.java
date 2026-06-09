package org.example.weapon.legendary;

import org.example.core.CustomWeaponEngine;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class RareEpicItems {

    public static void registerItems(CustomWeaponEngine plugin) {
        FileConfiguration lib = plugin.getLibraryConfig();

        // 1. Shadow Assassin Armor (Epic)
        createSet(plugin, lib, "shadow_assassin", "Shadow Assassin", "EPIC", Material.LEATHER_HELMET, Material.LEATHER_CHESTPLATE, Material.LEATHER_LEGGINGS, Material.LEATHER_BOOTS,
                "Kẻ Săn Bóng Đêm", "Mỗi khi giết quái vật, vĩnh viễn được cộng +1", "Strength (Tối đa 100). Hồi 1% HP khi hạ địch.",
                150, 100, 40, 0, 15, 30, 0);

        // 2. Ender Armor (Epic)
        createSet(plugin, lib, "ender", "Ender", "EPIC", Material.LEATHER_HELMET, Material.LEATHER_CHESTPLATE, Material.LEATHER_LEGGINGS, Material.LEATHER_BOOTS,
                "End Transmission", "Chỉ số được x2 khi ở The End.", "Giảm sát thương phải chịu từ Enderman.",
                100, 80, 0, 0, 0, 0, 0);

        // 3. Werewolf Armor (Epic)
        createSet(plugin, lib, "werewolf", "Werewolf", "EPIC", Material.CHAINMAIL_HELMET, Material.CHAINMAIL_CHESTPLATE, Material.CHAINMAIL_LEGGINGS, Material.CHAINMAIL_BOOTS,
                "Sói Cuồng Nộ", "Cộng cực nhiều Tốc độ và Chí mạng.", "Khi bị đánh có 20% khả năng hoảng sợ quái vật.",
                120, 90, 10, 0, 25, 20, 0);

        // 4. Hardened Diamond Armor (Rare)
        createSet(plugin, lib, "hardened_diamond", "Hardened Diamond", "RARE", Material.DIAMOND_HELMET, Material.DIAMOND_CHESTPLATE, Material.DIAMOND_LEGGINGS, Material.DIAMOND_BOOTS,
                "Cứng Cáp", "Lớp giáp kim cương siêu bền bỉ.", "",
                50, 150, 0, 0, 0, 0, 0);

        // 5. Golem Armor (Epic)
        createSet(plugin, lib, "golem_armor", "Golem", "EPIC", Material.IRON_HELMET, Material.IRON_CHESTPLATE, Material.IRON_LEGGINGS, Material.IRON_BOOTS,
                "Iron Resolve", "Siêu trâu bò ở giai đoạn Mid-game.", "",
                180, 180, 0, 0, 0, 0, 0);

        // --- WEAPONS ---
        // Golem Sword (Rare)
        createWeapon(plugin, lib, "cwe_golem_sword", "Golem Sword", "RARE", Material.IRON_SWORD, 90, 125, 0, 0, 0, 0, 0,
                "Iron Punch", "Nện kiếm giật khối sắt xung quanh.", "", "RIGHT CLICK");

        // Raider Axe (Rare)
        createWeapon(plugin, lib, "cwe_raider_axe", "Raider Axe", "RARE", Material.IRON_AXE, 100, 80, 0, 0, 0, 0, 0,
                "Thợ Săn Tiền Thưởng", "Tăng lượng tiền rớt ra khi giết quái.", "", "PASSIVE");

        // Zombie Sword (Epic)
        createWeapon(plugin, lib, "cwe_zombie_sword", "Zombie Sword", "EPIC", Material.IRON_SWORD, 130, 50, 0, 0, 0, 0, 100,
                "Instant Heal", "Hồi 10% máu cho bản thân và 5% cho", "đồng minh xung quanh (Tốn 50 Mana).", "RIGHT CLICK");

        // Pigman Sword (Legendary)
        createWeapon(plugin, lib, "cwe_pigman_sword", "Pigman Sword", "LEGENDARY", Material.GOLDEN_SWORD, 220, 120, 0, 0, 20, 50, 300,
                "Burning Souls", "Bắn ra ngọn lửa linh hồn lợn gây nổ,", "tăng 300 Defense trong 5 giây.", "RIGHT CLICK");

        // Fel Sword (Epic)
        createWeapon(plugin, lib, "cwe_fel_sword", "Fel Sword", "EPIC", Material.IRON_SWORD, 150, 0, 0, 0, 0, 0, 0,
                "Huyết Kiếm", "Thanh kiếm cộng thêm +1 sát thương", "cho mỗi 100 quái vật hạ gục (Tối đa +100).", "PASSIVE");

        // --- BOWS & WANDS ---
        org.example.system.ItemGenerator generator = new org.example.system.ItemGenerator(plugin);
        lib.set("items.runaan_bow", generator.generateItem("runaan_bow"));
        lib.set("items.shortbow", generator.generateItem("juju_shortbow"));
        lib.set("items.cwe_astral_shepherd_wand", generator.generateItem("astral_shepherd_wand"));
        lib.set("items.gae_bolg", generator.generateItem("gae_bolg"));

        plugin.saveLibraryConfig();
    }

    private static void createWeapon(CustomWeaponEngine plugin, FileConfiguration lib, String id, String name, String rarity,
            Material mat, int damage, int strength, int health, int defense, int cc, int cd, int intel,
            String abilityName, String ability1, String ability2, String clickType) {
        
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            String color = rarity.equals("LEGENDARY") ? "§6" : rarity.equals("EPIC") ? "§5" : "§9";
            meta.setDisplayName(color + name);
            
            List<String> lore = new ArrayList<>();
            if (damage > 0) lore.add("§7Damage: §c+" + damage);
            if (strength > 0) lore.add("§7Strength: §c+" + strength);
            if (cc > 0) lore.add("§7Crit Chance: §c+" + cc + "%");
            if (cd > 0) lore.add("§7Crit Damage: §c+" + cd + "%");
            if (health > 0) lore.add("§7Health: §a+" + health);
            if (defense > 0) lore.add("§7Defense: §a+" + defense);
            if (intel > 0) lore.add("§7Intelligence: §a+" + intel);
            
            lore.add("");
            lore.add("§6Item Ability: " + abilityName + " §e§l" + clickType);
            lore.add("§7" + ability1);
            if (!ability2.isEmpty()) lore.add("§7" + ability2);
            if (!clickType.equals("PASSIVE") && clickType.length() > 2) lore.add("§7...");
            lore.add("");
            lore.add(color + "§l" + rarity + " SWORD");
            meta.setLore(lore);
            NamespacedKey idKey = new NamespacedKey(plugin, "cwe_id");
            meta.getPersistentDataContainer().set(idKey, PersistentDataType.STRING, id);
            meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "cwe_item_stats"), PersistentDataType.INTEGER, 1);
            meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "stat_rarity"), PersistentDataType.STRING, rarity);
            if (damage > 0) meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "stat_damage"), PersistentDataType.DOUBLE, (double) damage);
            if (strength > 0) meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "stat_strength"), PersistentDataType.DOUBLE, (double) strength);
            if (cc > 0) meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "stat_crit_chance"), PersistentDataType.DOUBLE, (double) cc);
            if (cd > 0) meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "stat_crit_damage"), PersistentDataType.DOUBLE, (double) cd);
            if (defense > 0) meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "stat_defense"), PersistentDataType.DOUBLE, (double) defense);
            if (health > 0) meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "stat_health"), PersistentDataType.DOUBLE, (double) health);
            if (intel > 0) meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "stat_intelligence"), PersistentDataType.DOUBLE, (double) intel);
            
            meta.setUnbreakable(true);
            meta.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_UNBREAKABLE);

            item.setItemMeta(meta);
        }
        if (!lib.contains("items." + id)) lib.set("items." + id, item);
    }

    private static void createSet(CustomWeaponEngine plugin, FileConfiguration lib, String idPrefix, String namePrefix, String rarity,
            Material hMat, Material cMat, Material lMat, Material bMat,
            String abilityName, String ability1, String ability2,
            int hp, int def, int str, int dmg, int cc, int cd, int intel) {
        
        createPiece(plugin, lib, idPrefix + "_helmet", namePrefix + " Helmet", rarity, hMat, hp, def, str, dmg, cd, cc, intel, abilityName, ability1, ability2);
        createPiece(plugin, lib, idPrefix + "_chestplate", namePrefix + " Chestplate", rarity, cMat, hp+30, def+30, str, dmg, cd, cc, intel, abilityName, ability1, ability2);
        createPiece(plugin, lib, idPrefix + "_leggings", namePrefix + " Leggings", rarity, lMat, hp+15, def+15, str, dmg, cd, cc, intel, abilityName, ability1, ability2);
        createPiece(plugin, lib, idPrefix + "_boots", namePrefix + " Boots", rarity, bMat, hp-10, def-10, str, dmg, cd, cc, intel, abilityName, ability1, ability2);
    }

    private static void createPiece(CustomWeaponEngine plugin, FileConfiguration lib, String id, String name, String rarity,
            Material mat, int health, int defense, int strength, int damage, int critDamage, int critChance, int intel,
            String abilityName, String ability1, String ability2) {
        ItemStack item = new ItemStack(mat);
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
            if (damage > 0) meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "stat_damage"), PersistentDataType.DOUBLE, (double) damage);
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
        if (!lib.contains("items." + id)) lib.set("items." + id, item);
    }
}

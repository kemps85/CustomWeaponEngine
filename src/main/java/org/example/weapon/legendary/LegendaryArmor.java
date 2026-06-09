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
        // Đã xóa theo yêu cầu (Lỗi crash Client do render màu giáp)

        // --- SHADOW ASSASSIN ARMOR ---
        createSet(plugin, lib, "shadow_assassin", "Shadow Assassin", "EPIC",
                "Blood Lust", "Tăng 1 Strength với mỗi đòn tấn công", "trúng đích trong 10s (Tối đa 30).",
                170, 130, 45, 0, 35, 0, 0, 10);

        // --- DRAGON ARMOR SERIES ---
        createDragonSet(plugin, lib, "superior", "Superior Dragon", "Tăng 5% cho toàn bộ chỉ số của người mặc.", 
                        150, 150, 20, 15, 3, 40, 5);
        createDragonSet(plugin, lib, "strong", "Strong Dragon", "Tăng sát thương và tầm dịch chuyển cho Aspect of the End.", 
                        120, 120, 40, 0, 0, 0, 0);
        createDragonSet(plugin, lib, "wise", "Wise Dragon", "Giảm 33% năng lượng tiêu hao cho tất cả các kỹ năng.", 
                        120, 120, 0, 0, 0, 100, 0);
        createDragonSet(plugin, lib, "unstable", "Unstable Dragon", "Thỉnh thoảng phóng sét tấn công quái vật xung quanh.", 
                        100, 100, 0, 25, 5, 0, 0);
        createDragonSet(plugin, lib, "young", "Young Dragon", "Tăng tốc độ chạy vượt qua giới hạn khi máu > 50%.", 
                        120, 120, 0, 0, 0, 0, 20);
        createDragonSet(plugin, lib, "old", "Old Dragon", "Tăng sức mạnh của các Phù phép cơ bản (Growth, Protection...).", 
                        180, 180, 0, 0, 0, 0, 0);
        createDragonSet(plugin, lib, "protector", "Protector Dragon", "Tăng thêm Defense dựa trên phần trăm máu đã mất.", 
                        100, 220, 0, 0, 0, 0, 0);
        createDragonSet(plugin, lib, "holy", "Holy Dragon", "Tăng x3 lần tốc độ hồi máu tự nhiên cho bản thân và đồng minh.", 
                        150, 150, 0, 0, 0, 0, 0);

        // --- TARANTULA ARMOR ---
        createSet(plugin, lib, "tarantula", "Tarantula", "EPIC",
                "Octodexterity", "Mỗi 4 đòn đánh trúng đích sẽ gây x2 sát thương.", "Cho phép nhảy kép (Double Jump) tốn 40 Mana.",
                150, 100, 10, 50, 25, 0, 0, 0);

        plugin.saveLibraryConfig();
    }

    private static void createWitherSet(CustomWeaponEngine plugin, FileConfiguration lib, String idPrefix, String namePrefix, 
            int hp, int def, int strength, int damage, int critDamage, int critChance, int intel, int speed) {
        createPiece(plugin, lib, idPrefix + "_helmet", namePrefix + " Helmet", "LEGENDARY", Material.LEATHER_HELMET, hp, def, strength, damage, critDamage, critChance, intel, speed, "Witherborn", "Triệu hồi Wither mini xoay quanh,", "bắn tia hủy diệt vào quái vật mỗi 3s.");
        createPiece(plugin, lib, idPrefix + "_chestplate", namePrefix + " Chestplate", "LEGENDARY", Material.LEATHER_CHESTPLATE, hp+50, def+50, strength, damage, critDamage, critChance, intel, speed, "Witherborn", "Triệu hồi Wither mini xoay quanh,", "bắn tia hủy diệt vào quái vật mỗi 3s.");
        createPiece(plugin, lib, idPrefix + "_leggings", namePrefix + " Leggings", "LEGENDARY", Material.LEATHER_LEGGINGS, hp+20, def+30, strength, damage, critDamage, critChance, intel, speed, "Witherborn", "Triệu hồi Wither mini xoay quanh,", "bắn tia hủy diệt vào quái vật mỗi 3s.");
        createPiece(plugin, lib, idPrefix + "_boots", namePrefix + " Boots", "LEGENDARY", Material.LEATHER_BOOTS, hp-20, def-20, strength, damage, critDamage, critChance, intel, speed, "Witherborn", "Triệu hồi Wither mini xoay quanh,", "bắn tia hủy diệt vào quái vật mỗi 3s.");
    }

    private static void createDragonSet(CustomWeaponEngine plugin, FileConfiguration lib, String idPrefix, String namePrefix, String abilityDesc,
                                        int hp, int def, int str, int cd, int cc, int intel, int spd) {
        createPiece(plugin, lib, idPrefix + "_helmet", namePrefix + " Helmet", "LEGENDARY", Material.LEATHER_HELMET, hp, def, str, 0, cd, cc, intel, spd, namePrefix + " Blood", abilityDesc, "");
        createPiece(plugin, lib, idPrefix + "_chestplate", namePrefix + " Chestplate", "LEGENDARY", Material.LEATHER_CHESTPLATE, hp+50, def+50, str, 0, cd, cc, intel, spd, namePrefix + " Blood", abilityDesc, "");
        createPiece(plugin, lib, idPrefix + "_leggings", namePrefix + " Leggings", "LEGENDARY", Material.LEATHER_LEGGINGS, hp+20, def+30, str, 0, cd, cc, intel, spd, namePrefix + " Blood", abilityDesc, "");
        createPiece(plugin, lib, idPrefix + "_boots", namePrefix + " Boots", "LEGENDARY", Material.LEATHER_BOOTS, hp-20, def-20, str, 0, cd, cc, intel, spd, namePrefix + " Blood", abilityDesc, "");
    }

    private static void createSet(CustomWeaponEngine plugin, FileConfiguration lib, String idPrefix, String namePrefix, String rarity,
                                  String abilityName, String ability1, String ability2, int hp, int def, int str, int dmg, int cd, int cc, int intel, int spd) {
        createPiece(plugin, lib, idPrefix + "_helmet", namePrefix + " Helmet", rarity, Material.LEATHER_HELMET, hp, def, str, dmg, cd, cc, intel, spd, abilityName, ability1, ability2);
        createPiece(plugin, lib, idPrefix + "_chestplate", namePrefix + " Chestplate", rarity, Material.LEATHER_CHESTPLATE, hp+40, def+40, str, dmg, cd, cc, intel, spd, abilityName, ability1, ability2);
        createPiece(plugin, lib, idPrefix + "_leggings", namePrefix + " Leggings", rarity, Material.LEATHER_LEGGINGS, hp+20, def+20, str, dmg, cd, cc, intel, spd, abilityName, ability1, ability2);
        createPiece(plugin, lib, idPrefix + "_boots", namePrefix + " Boots", rarity, Material.LEATHER_BOOTS, hp-10, def-10, str, dmg, cd, cc, intel, spd, abilityName, ability1, ability2);
    }

    private static void createPiece(CustomWeaponEngine plugin, FileConfiguration lib, String id, String name, String rarity,
            Material mat, int health, int defense, int strength, int damage, int critDamage, int critChance, int intel, int speed,
            String abilityName, String ability1, String ability2) {
        
        ItemStack item = new ItemStack(mat);
        if (item.getItemMeta() instanceof LeatherArmorMeta) {
            LeatherArmorMeta lam = (LeatherArmorMeta) item.getItemMeta();
            
            // HYPIXEL SKYBLOCK EXACT RGB COLORS
            if (id.contains("superior")) {
                if (id.endsWith("helmet")) lam.setColor(Color.fromRGB(242, 223, 17));
                else if (id.endsWith("chestplate")) lam.setColor(Color.fromRGB(226, 93, 24));
                else if (id.endsWith("leggings")) lam.setColor(Color.fromRGB(242, 93, 24));
                else if (id.endsWith("boots")) lam.setColor(Color.fromRGB(242, 223, 17));
            } else if (id.contains("strong")) {
                if (id.endsWith("chestplate") || id.endsWith("leggings")) lam.setColor(Color.fromRGB(235, 59, 90));
                else lam.setColor(Color.fromRGB(247, 183, 49));
            } else if (id.contains("wise")) {
                if (id.endsWith("helmet") || id.endsWith("boots")) lam.setColor(Color.fromRGB(45, 152, 218));
                else lam.setColor(Color.fromRGB(69, 170, 242));
            } else if (id.contains("unstable")) {
                if (id.endsWith("helmet") || id.endsWith("leggings")) lam.setColor(Color.fromRGB(163, 40, 214));
                else lam.setColor(Color.fromRGB(0, 0, 0));
            } else if (id.contains("young")) {
                if (id.endsWith("leggings")) lam.setColor(Color.fromRGB(224, 255, 255));
                else lam.setColor(Color.fromRGB(255, 255, 255));
            } else if (id.contains("old")) {
                lam.setColor(Color.fromRGB(240, 230, 140));
            } else if (id.contains("protector")) {
                lam.setColor(Color.fromRGB(77, 77, 77));
            } else if (id.contains("holy")) {
                lam.setColor(Color.fromRGB(255, 255, 255));
            } else if (id.contains("shadow_assassin")) {
                if (id.endsWith("chestplate")) lam.setColor(Color.fromRGB(139, 0, 0));
                else lam.setColor(Color.fromRGB(20, 20, 20));
            } else if (id.contains("necron")) {
                if (id.endsWith("chestplate")) lam.setColor(Color.fromRGB(255, 99, 71));
                else lam.setColor(Color.fromRGB(255, 69, 0));
            } else if (id.contains("storm")) {
                if (id.endsWith("chestplate")) lam.setColor(Color.fromRGB(0, 255, 255));
                else lam.setColor(Color.fromRGB(0, 206, 209));
            } else if (id.contains("maxor")) {
                if (id.endsWith("chestplate")) lam.setColor(Color.fromRGB(148, 0, 211));
                else lam.setColor(Color.fromRGB(138, 43, 226));
            } else if (id.contains("goldor")) {
                if (id.endsWith("chestplate")) lam.setColor(Color.fromRGB(47, 79, 79));
                else lam.setColor(Color.fromRGB(105, 105, 105));
            } else if (id.contains("tarantula")) {
                lam.setColor(Color.fromRGB(128, 0, 0));
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
            if (speed > 0) lore.add("§7Speed: §a+" + speed);
            
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
            if (speed > 0) meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "stat_speed"), PersistentDataType.DOUBLE, (double) speed);
            
            // Ability System Storage
            if (abilityName != null && !abilityName.isEmpty()) {
                meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "stat_setbonus_title"), PersistentDataType.STRING, abilityName);
                if (ability1 != null && !ability1.isEmpty()) meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "stat_setbonus_desc1"), PersistentDataType.STRING, ability1);
                if (ability2 != null && !ability2.isEmpty()) meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "stat_setbonus_desc2"), PersistentDataType.STRING, ability2);
            }
            
            meta.setUnbreakable(true);
            meta.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_UNBREAKABLE);

            item.setItemMeta(meta);
            if (!lib.contains("items." + id)) {
                lib.set("items." + id, item);
            }
        }
    }
}

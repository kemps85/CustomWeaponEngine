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

public class LegendarySwords {

    public static void registerSwords(CustomWeaponEngine plugin) {
        FileConfiguration lib = plugin.getLibraryConfig();
        
        // 1. Aspect of the End (AOTE)
        ItemStack aote = createSword(plugin, "cwe_aote", "Aspect of the End", "RARE",
                100, 100, 0, 0, 0, 0, 0, 0,
                "Instant Transmission", "Dịch chuyển tức thời tới phía trước 8 lốc.", "", "RIGHT CLICK");
        if (!lib.contains("items.cwe_aote")) lib.set("items.cwe_aote", aote);

        // 2. Aspect of the Dragons (AOTD)
        ItemStack aotd = createSword(plugin, "cwe_aotd", "Aspect of the Dragons", "LEGENDARY",
                225, 100, 0, 0, 0, 0, 0, 0,
                "Dragon Rage", "Cơn Thịnh Nộ Của Rồng: Hất văng quái vật", "và gây sát thương khủng.", "RIGHT CLICK");
        if (!lib.contains("items.cwe_aotd")) lib.set("items.cwe_aotd", aotd);

        // 3. Livid Dagger
        ItemStack livid = createSword(plugin, "cwe_livid_dagger", "Livid Dagger", "LEGENDARY",
                210, 60, 100, 50, 0, 0, 0, 0,
                "Throw", "Phi Dao. Kẻ địch bị đánh từ sau lưng", "sẽ nhận x2 sát thương.", "RIGHT CLICK");
        if (!lib.contains("items.cwe_livid_dagger")) lib.set("items.cwe_livid_dagger", livid);

        // 4. Shadow Fury
        ItemStack sf = createSword(plugin, "cwe_shadow_fury", "Shadow Fury", "LEGENDARY",
                300, 125, 0, 0, 0, 0, 0, 30,
                "Shadow Fury", "Cơn Lốc Bóng Tối: Dịch chuyển và chém", "liên tiếp 5 mục tiêu gần nhất.", "RIGHT CLICK");
        if (!lib.contains("items.cwe_shadow_fury")) lib.set("items.cwe_shadow_fury", sf);

        // 5. Giant's Sword
        ItemStack giants = createSword(plugin, "cwe_giants_sword", "Giant's Sword", "LEGENDARY",
                350, 0, 0, 0, 0, 0, 0, 0,
                "Giant's Slam", "Cú Nện Khổng Lồ: Dậm thanh kiếm xuống", "mặt đất gây sát thương diện rộng cực lớn.", "RIGHT CLICK");
        if (!lib.contains("items.cwe_giants_sword")) lib.set("items.cwe_giants_sword", giants);

        // 6. Hyperion
        ItemStack hype = createSword(plugin, "cwe_hyperion", "Hyperion", "LEGENDARY",
                260, 150, 0, 0, 0, 0, 350, 0,
                "Wither Impact", "Vụ Nổ Wither: Dịch chuyển 10 lốc, gây nổ", "và hồi 10% máu trong 10 giây.", "RIGHT CLICK");
        if (!lib.contains("items.cwe_hyperion")) lib.set("items.cwe_hyperion", hype);

        // 7. Valkyrie
        ItemStack valk = createSword(plugin, "cwe_valkyrie", "Valkyrie", "LEGENDARY",
                270, 145, 0, 0, 0, 0, 60, 0,
                "Wither Impact", "Vụ Nổ Wither: Dịch chuyển 10 lốc, gây nổ", "và hồi 10% máu trong 10 giây.", "RIGHT CLICK");
        if (!lib.contains("items.cwe_valkyrie")) lib.set("items.cwe_valkyrie", valk);

        // 8. Scylla
        ItemStack scylla = createSword(plugin, "cwe_scylla", "Scylla", "LEGENDARY",
                270, 150, 12, 35, 0, 0, 50, 0,
                "Wither Impact", "Vụ Nổ Wither: Dịch chuyển 10 lốc, gây nổ", "và hồi 10% máu trong 10 giây.", "RIGHT CLICK");
        if (!lib.contains("items.cwe_scylla")) lib.set("items.cwe_scylla", scylla);

        // 9. Astraea
        ItemStack astraea = createSword(plugin, "cwe_astraea", "Astraea", "LEGENDARY",
                270, 150, 0, 0, 250, 0, 50, 0,
                "Wither Impact", "Vụ Nổ Wither: Dịch chuyển 10 lốc, gây nổ", "và hồi 10% máu trong 10 giây.", "RIGHT CLICK");
        if (!lib.contains("items.cwe_astraea")) lib.set("items.cwe_astraea", astraea);

        // 10. Emerald Blade
        ItemStack emerald = createSword(plugin, "cwe_emerald_blade", "Emerald Blade", "EPIC",
                130, 0, 0, 0, 0, 0, 0, 0,
                "Lòng Tham", "Thanh kiếm sẽ ngày càng mạnh hơn", "dựa trên số tiền Pocket bạn đang có.", "PASSIVE");
        if (!lib.contains("items.cwe_emerald_blade")) lib.set("items.cwe_emerald_blade", emerald);

        plugin.saveLibraryConfig();
    }

    public static ItemStack createSword(CustomWeaponEngine plugin, String id, String name, String rarity,
            int damage, int strength, int critChance, int critDamage, int defense, int health, int intell, int speed,
            String abilityName, String ability1, String ability2, String clickType) {
        
        ItemStack item = new ItemStack(Material.IRON_SWORD);
        if (id.equals("cwe_aotd") || id.equals("cwe_giants_sword") || id.equals("cwe_hyperion")) {
            item.setType(Material.DIAMOND_SWORD);
        } else if (id.equals("cwe_emerald_blade")) {
            item.setType(Material.EMERALD); // Emerald Blade is an emerald
        }
        
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            String color = rarity.equals("LEGENDARY") ? "§6" : rarity.equals("EPIC") ? "§5" : "§9";
            meta.setDisplayName(color + name);
            
            List<String> lore = new ArrayList<>();
            if (damage > 0) lore.add("§7Damage: §c+" + damage);
            if (strength > 0) lore.add("§7Strength: §c+" + strength);
            if (critChance > 0) lore.add("§7Crit Chance: §c+" + critChance + "%");
            if (critDamage > 0) lore.add("§7Crit Damage: §c+" + critDamage + "%");
            if (defense > 0) lore.add("§7Defense: §a+" + defense);
            if (health > 0) lore.add("§7Health: §a+" + health);
            if (intell > 0) lore.add("§7Intelligence: §a+" + intell);
            if (speed > 0) lore.add("§7Speed: §a+" + speed);
            
            lore.add("");
            lore.add("§6Item Ability: " + abilityName + " §e§l" + clickType);
            lore.add("§7" + ability1);
            if (!ability2.isEmpty()) lore.add("§7" + ability2);
            lore.add("");
            lore.add(color + "§l" + rarity + " SWORD");
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
            if (intell > 0) meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "stat_intelligence"), PersistentDataType.DOUBLE, (double) intell);
            if (speed > 0) meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "stat_speed"), PersistentDataType.DOUBLE, (double) speed);

            // Lưu kỹ năng vào PDC để ReforgeSystem không xoá mất lore
            if (abilityName != null && !abilityName.isEmpty()) {
                meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "stat_setbonus_title"), PersistentDataType.STRING, abilityName);
                if (ability1 != null && !ability1.isEmpty()) meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "stat_setbonus_desc1"), PersistentDataType.STRING, ability1);
                if (ability2 != null && !ability2.isEmpty()) meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "stat_setbonus_desc2"), PersistentDataType.STRING, ability2);
                if (clickType != null && !clickType.isEmpty()) meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "stat_ability_click"), PersistentDataType.STRING, clickType);
            }
            
            meta.setUnbreakable(true);
            meta.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_UNBREAKABLE);

            item.setItemMeta(meta);
        }
        return item;
    }
}

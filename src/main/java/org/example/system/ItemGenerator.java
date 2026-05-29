package org.example.system;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
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
            default:
                return null;
        }
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
            meta.getPersistentDataContainer().set(new NamespacedKey(plugin, org.example.stats.ItemStatsGUI.KEY_STRENGTH), PersistentDataType.DOUBLE, 50.0);
            // We put damage in strength because bows don't use melee damage in Minecraft.
            // Wait, actually let's just add base stats
            meta.getPersistentDataContainer().set(new NamespacedKey(plugin, org.example.stats.ItemStatsGUI.KEY_RARITY), PersistentDataType.STRING, "LEGENDARY");

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
            meta.getPersistentDataContainer().set(new NamespacedKey(plugin, org.example.stats.ItemStatsGUI.KEY_STRENGTH), PersistentDataType.DOUBLE, 40.0);
            meta.getPersistentDataContainer().set(new NamespacedKey(plugin, org.example.stats.ItemStatsGUI.KEY_CRIT_CHANCE), PersistentDataType.DOUBLE, 15.0);
            meta.getPersistentDataContainer().set(new NamespacedKey(plugin, org.example.stats.ItemStatsGUI.KEY_CRIT_DAMAGE), PersistentDataType.DOUBLE, 80.0);
            meta.getPersistentDataContainer().set(new NamespacedKey(plugin, org.example.stats.ItemStatsGUI.KEY_RARITY), PersistentDataType.STRING, "EPIC");

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
            item.setItemMeta(meta);
        }
        return item;
    }
}

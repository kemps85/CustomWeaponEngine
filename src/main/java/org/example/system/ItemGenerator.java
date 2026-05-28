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
                return getJujuShortbow();
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

            // Gắn mã CWE_ID để RunaanBowListener nhận diện
            NamespacedKey cweIdKey = new NamespacedKey(plugin, "cwe_id");
            meta.getPersistentDataContainer().set(cweIdKey, PersistentDataType.STRING, "runaan_bow");

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

            // Gắn mã CWE_ID = shortbow
            NamespacedKey cweIdKey = new NamespacedKey(plugin, "cwe_id");
            meta.getPersistentDataContainer().set(cweIdKey, PersistentDataType.STRING, "shortbow");

            item.setItemMeta(meta);
        }
        return item;
    }
}

package org.example.armor;

import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
import org.bukkit.NamespacedKey;
import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
import org.bukkit.entity.Player;
import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
import org.bukkit.inventory.ItemStack;
import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
import org.bukkit.persistence.PersistentDataContainer;
import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
import org.bukkit.persistence.PersistentDataType;

import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
public class ArmorManager {

    // Hàm phụ đọc chỉ số ẩn NBT từ item
    private static double getStatFromItem(ItemStack item, String statName) {
        if (item == null || !item.hasItemMeta()) return 0.0;
        PersistentDataContainer container = item.getItemMeta().getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(CustomWeaponEngine.getInstance(), "stat_" + statName);
        if (container.has(key, PersistentDataType.DOUBLE)) {
            Double value = container.get(key, PersistentDataType.DOUBLE);
            return value != null ? value : 0.0;
        }
        return 0.0;
    }

    // 🟩 Lấy tổng giảm tốn MANA của cả bộ giáp đang mặc
    public static double getGlobalManaReduction(Player player) {
        double total = 0.0;
        if (player == null) return total;
        ItemStack[] armor = player.getInventory().getArmorContents();
        for (ItemStack item : armor) {
            total += getStatFromItem(item, "mana_reduction");
        }
        return total;
    }

    // 🟩 Lấy tổng giảm thời gian HỒI CHIÊU (Cooldown Reduction)
    public static double getGlobalCooldownReduction(Player player) {
        double total = 0.0;
        if (player == null) return total;
        ItemStack[] armor = player.getInventory().getArmorContents();
        for (ItemStack item : armor) {
            total += getStatFromItem(item, "cooldown_reduction");
        }
        return total;
    }

    // 🟩 Lấy tổng % TỶ LỆ CHÍ MẠNG (Crit Chance)
    public static double getGlobalCritChance(Player player) {
        double total = 0.0;
        if (player == null) return total;
        ItemStack[] armor = player.getInventory().getArmorContents();
        for (ItemStack item : armor) {
            total += getStatFromItem(item, "crit_chance");
        }
        return total;
    }

    // 🟩 Lấy tổng % SÁT THƯƠNG CHÍ MẠNG (Crit Damage)
    public static double getGlobalCritDamage(Player player) {
        double total = 0.0;
        if (player == null) return total;
        ItemStack[] armor = player.getInventory().getArmorContents();
        for (ItemStack item : armor) {
            total += getStatFromItem(item, "crit_damage");
        }
        return total;
    }

    // 🟩 Lấy tổng % SÁT THƯƠNG CỘNG THÊM HOÀN TOÀN (Damage Buff)
    public static double getGlobalDamageBuff(Player player) {
        double total = 0.0;
        if (player == null) return total;
        ItemStack[] armor = player.getInventory().getArmorContents();
        for (ItemStack item : armor) {
            total += getStatFromItem(item, "damage_buff");
        }
        return total;
    }
}

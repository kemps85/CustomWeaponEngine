package org.example.enchant;

import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
import org.bukkit.Material;
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
import org.bukkit.enchantments.Enchantment; 
import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
import org.bukkit.inventory.ItemFlag;       
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
import org.bukkit.inventory.meta.ItemMeta;
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
import org.bukkit.plugin.java.JavaPlugin;
import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
import java.util.ArrayList;
import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
import java.util.List;

import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
public class EnchantManager {
    private final JavaPlugin plugin;

    public EnchantManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public boolean hasEnchantFromGroup(ItemStack item, CustomEnchant.ItemGroup group) {
        if (item == null || !item.hasItemMeta()) return false;
        PersistentDataContainer container = item.getItemMeta().getPersistentDataContainer();
        for (CustomEnchant enc : CustomEnchant.values()) {
            if (enc.getItemGroup() == group) {
                NamespacedKey key = new NamespacedKey(plugin, "enchant_" + enc.getId());
                if (container.has(key, PersistentDataType.INTEGER)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean applyEnchantToItem(ItemStack item, CustomEnchant enchant, int level) {
        if (item == null || item.getType().isAir()) return false;

        if (!enchant.getItemGroup().canApply(item.getType())) return false;

        String matName = item.getType().name();
        boolean isActualAxe = (matName.endsWith("_AXE") || matName.equals("AXE")) && !matName.contains("PICKAXE");
        if (isActualAxe) {
            if (enchant.getItemGroup() == CustomEnchant.ItemGroup.SWORD && hasEnchantFromGroup(item, CustomEnchant.ItemGroup.TOOL)) return false;
            if (enchant.getItemGroup() == CustomEnchant.ItemGroup.TOOL && hasEnchantFromGroup(item, CustomEnchant.ItemGroup.SWORD)) return false;
        }

        ItemMeta meta = item.getItemMeta();
        if (meta == null) return false;
        PersistentDataContainer container = meta.getPersistentDataContainer();

        NamespacedKey flagKey = new NamespacedKey(plugin, "is_custom_enchanted");
        container.set(flagKey, PersistentDataType.INTEGER, 1);

        NamespacedKey key = new NamespacedKey(plugin, "enchant_" + enchant.getId());
        container.set(key, PersistentDataType.INTEGER, level);
        item.setItemMeta(meta);

        // Khởi tạo pin tối đa cho báu vật lần đầu tiên
        if (isChargeable(enchant)) {
            setCharges(item, enchant, getMaxCharges(enchant, level));
        }

        rebuildEnchantLore(item);
        return true;
    }

    @SuppressWarnings("deprecation")
    
    // ==========================================
    // ⚔️ HỆ THỐNG ULTIMATE ENCHANT
    // ==========================================

    public boolean hasUltimateEnchant(ItemStack item, UltimateEnchant enchant) {
        if (item == null || !item.hasItemMeta()) return false;
        NamespacedKey key = new NamespacedKey(plugin, "ult_enchant_" + enchant.getId());
        return item.getItemMeta().getPersistentDataContainer().has(key, PersistentDataType.INTEGER);
    }

    public int getUltimateEnchantLevel(ItemStack item, UltimateEnchant enchant) {
        if (item == null || !item.hasItemMeta()) return 0;
        NamespacedKey key = new NamespacedKey(plugin, "ult_enchant_" + enchant.getId());
        return item.getItemMeta().getPersistentDataContainer().getOrDefault(key, PersistentDataType.INTEGER, 0);
    }

    public boolean applyUltimateEnchantToItem(ItemStack item, UltimateEnchant enchant, int level) {
        if (item == null || item.getType().isAir()) return false;
        if (item.getType() != Material.BOOK && item.getType() != Material.ENCHANTED_BOOK && !enchant.getGroup().canApply(item.getType())) return false;

        ItemMeta meta = item.getItemMeta();
        if (meta == null) return false;
        PersistentDataContainer container = meta.getPersistentDataContainer();

        // One for All: Xóa tất cả Vanilla và Custom Enchants thường
        if (enchant == UltimateEnchant.ONE_FOR_ALL) {
            for (Enchantment vanilla : meta.getEnchants().keySet()) {
                meta.removeEnchant(vanilla);
            }
            for (CustomEnchant cEnc : CustomEnchant.values()) {
                NamespacedKey cKey = new NamespacedKey(plugin, "enchant_" + cEnc.getId());
                if (container.has(cKey, PersistentDataType.INTEGER)) {
                    container.remove(cKey);
                }
            }
        } else {
            // Nếu item đang có One for All, mà ta lại muốn ép Ultimate khác, nó vẫn xóa One for All.
        }

        // Đè bẹp Ultimate cũ (Luật 1 Ultimate per item)
        for (UltimateEnchant uEnc : UltimateEnchant.values()) {
            NamespacedKey uKey = new NamespacedKey(plugin, "ult_enchant_" + uEnc.getId());
            if (container.has(uKey, PersistentDataType.INTEGER)) {
                container.remove(uKey);
            }
        }

        NamespacedKey flagKey = new NamespacedKey(plugin, "is_custom_enchanted");
        container.set(flagKey, PersistentDataType.INTEGER, 1);

        NamespacedKey key = new NamespacedKey(plugin, "ult_enchant_" + enchant.getId());
        container.set(key, PersistentDataType.INTEGER, level);
        item.setItemMeta(meta);

        rebuildEnchantLore(item);
        return true;
    }

    public Enchantment getVanillaEquivalent(CustomEnchant enc) {
        String key = null;
        switch (enc) {
            case EFFICIENCY: key = "efficiency"; break;
            case SHARPNESS: key = "sharpness"; break;
            case POWER: key = "power"; break;
            case FLAME: key = "flame"; break;
            case LOOTING: key = "looting"; break;
            case FIRE_ASPECT: key = "fire_aspect"; break;
            case PROTECTION: key = "protection"; break;
            case THORNS: key = "thorns"; break;
            case FEATHER_FALLING: key = "feather_falling"; break;
            case MENDING: key = "mending"; break;
            case IMPALING: key = "impaling"; break;
            case LOYALTY: key = "loyalty"; break;
            case RIPTIDE: key = "riptide"; break;
            case CHANNELING: key = "channeling"; break;
            case DENSITY: key = "density"; break;
            case BREACH: key = "breach"; break;
            case WIND_BURST: key = "wind_burst"; break;
            default: return null;
        }
        if (key == null) return null;
        try {
            return Enchantment.getByKey(org.bukkit.NamespacedKey.minecraft(key));
        } catch (Throwable t) {
            try {
                return org.bukkit.Registry.ENCHANTMENT.get(org.bukkit.NamespacedKey.minecraft(key));
            } catch (Throwable t2) {
                return null;
            }
        }
    }

    public void rebuildEnchantLore(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;
        PersistentDataContainer container = meta.getPersistentDataContainer();

        
        // Build Ultimate Enchants Lore
        List<String> ultEnchantsStr = new ArrayList<>();
        for (UltimateEnchant uEnc : UltimateEnchant.values()) {
            NamespacedKey key = new NamespacedKey(plugin, "ult_enchant_" + uEnc.getId());
            if (container.has(key, PersistentDataType.INTEGER)) {
                int lvl = container.get(key, PersistentDataType.INTEGER);
                if (lvl > 0) {
                    ultEnchantsStr.add("§d§l" + uEnc.getDisplayName().toUpperCase() + " " + toRoman(lvl));
                }
            }
        }

        List<String> activeEnchantsStr = new ArrayList<>();
        for (CustomEnchant enc : CustomEnchant.values()) {
            NamespacedKey key = new NamespacedKey(plugin, "enchant_" + enc.getId());
            if (container.has(key, PersistentDataType.INTEGER)) {
                int lvl = container.get(key, PersistentDataType.INTEGER);
                if (lvl > 0) {
                    String line = "§9" + enc.getDisplayName() + " " + toRoman(lvl);
                    if (isChargeable(enc)) {
                        int current = getCharges(item, enc);
                        int max = getMaxCharges(enc, lvl);
                        line += " §b(" + current + "/" + max + "⚡)";
                    }
                    activeEnchantsStr.add(line);
                    
                    Enchantment vanillaMatch = getVanillaEquivalent(enc);
                    if (vanillaMatch != null) {
                        meta.addEnchant(vanillaMatch, lvl, true);
                    }
                }
            }
        }

        if (activeEnchantsStr.isEmpty()) return;

        try {
            Enchantment glowEnchant = Enchantment.getByKey(org.bukkit.NamespacedKey.minecraft("luck_of_the_sea"));
            if (glowEnchant != null && !meta.hasEnchant(glowEnchant)) {
                meta.addEnchant(glowEnchant, 1, true);
            }
        } catch (Throwable e) {
            @SuppressWarnings("deprecation")
            Enchantment luckOld = Enchantment.getByName("LUCK");
            if (luckOld != null && !meta.hasEnchant(luckOld)) {
                meta.addEnchant(luckOld, 1, true);
            }
        }
        
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS); 

        List<String> formattedRows = new ArrayList<>();
        StringBuilder currentRow = new StringBuilder();
        int itemsPerLine = 3; 

        for (int i = 0; i < activeEnchantsStr.size(); i++) {
            currentRow.append(activeEnchantsStr.get(i));
            if ((i + 1) % itemsPerLine != 0 && i != activeEnchantsStr.size() - 1) {
                currentRow.append("§7, ");
            } else {
                formattedRows.add(currentRow.toString());
                currentRow = new StringBuilder();
            }
        }

        List<String> lore = meta.hasLore() ? meta.getLore() : new ArrayList<>();
        if (lore == null) lore = new ArrayList<>();
        
        lore.removeIf(line -> line.startsWith("§9") && (line.contains("§7, ") || line.contains(" V") || line.contains(" I") || line.contains(" X")));
        lore.removeIf(line -> line.startsWith("§d§lULTIMATE"));

        int insertIndex = lore.size();
        for (int i = 0; i < lore.size(); i++) {
            String line = lore.get(i);
            if (line.contains("Full Set Bonus") || line.contains("COMMON") || line.contains("UNCOMMON") || line.contains("RARE") || line.contains("EPIC") || line.contains("LEGENDARY")) {
                insertIndex = i;
                break;
            }
        }

        
        if (!ultEnchantsStr.isEmpty()) {
            lore.addAll(insertIndex, ultEnchantsStr);
            insertIndex += ultEnchantsStr.size();
        }

        lore.addAll(insertIndex, formattedRows);
        meta.setLore(lore);
        item.setItemMeta(meta);
    }

    private String toRoman(int num) {
        String[] initials = {"", "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X"};
        return num <= 10 ? initials[num] : String.valueOf(num);
    }

    // ==========================================
    // ⚡ HỆ THỐNG QUẢN LÝ PIN (CHARGES) NATIVE PDC
    // ==========================================

    public NamespacedKey getChargesKey(CustomEnchant enchant) {
        return new NamespacedKey(plugin, "cwe_charges_" + enchant.getId());
    }

    public boolean isChargeable(CustomEnchant enchant) {
        switch (enchant) {
            case TELEKINESIS:
            case REPLENISH:
            case LIFE_STEAL:
            case VAMPIRISM:
                return true;
            default:
                return false;
        }
    }

    public int getMaxCharges(CustomEnchant enchant, int level) {
        return 100 + (level * 50); // 100 pin cơ bản, +50 pin cho mỗi cấp bùa
    }

    public int getCharges(ItemStack item, CustomEnchant enchant) {
        if (item == null || !item.hasItemMeta()) return 0;
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        NamespacedKey key = getChargesKey(enchant);
        
        if (!container.has(key, PersistentDataType.INTEGER)) {
            NamespacedKey enchantKey = new NamespacedKey(plugin, "enchant_" + enchant.getId());
            if (container.has(enchantKey, PersistentDataType.INTEGER)) {
                int level = container.get(enchantKey, PersistentDataType.INTEGER);
                return getMaxCharges(enchant, level);
            }
            return 0;
        }
        return container.get(key, PersistentDataType.INTEGER);
    }

    public void setCharges(ItemStack item, CustomEnchant enchant, int amount) {
        if (item == null || !item.hasItemMeta()) return;
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        NamespacedKey key = getChargesKey(enchant);
        
        NamespacedKey enchantKey = new NamespacedKey(plugin, "enchant_" + enchant.getId());
        int level = container.getOrDefault(enchantKey, PersistentDataType.INTEGER, 1);
        int max = getMaxCharges(enchant, level);
        
        container.set(key, PersistentDataType.INTEGER, Math.max(0, Math.min(amount, max)));
        item.setItemMeta(meta);
    }

    public boolean consumeCharge(ItemStack item, CustomEnchant enchant, int amount) {
        if (!isChargeable(enchant)) return true;
        
        int current = getCharges(item, enchant);
        if (current < amount) {
            return false;
        }
        setCharges(item, enchant, current - amount);
        return true;
    }

    public Material getFuelMaterial(CustomEnchant enchant) {
        switch (enchant) {
            case TELEKINESIS:
                return Material.ENDER_PEARL;
            case REPLENISH:
                return Material.WHEAT_SEEDS;
            case LIFE_STEAL:
            case VAMPIRISM:
                return Material.REDSTONE;
            default:
                return Material.LAPIS_LAZULI;
        }
    }
}

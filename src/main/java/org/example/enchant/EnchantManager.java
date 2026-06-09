/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.NamespacedKey
 *  org.bukkit.Registry
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.persistence.PersistentDataContainer
 *  org.bukkit.persistence.PersistentDataType
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.java.JavaPlugin
 */
package org.example.enchant;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.example.enchant.CustomEnchant;
import org.example.enchant.UltimateEnchant;
import org.example.stats.ItemBuilder;

public class EnchantManager {
    private final JavaPlugin plugin;

    public EnchantManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public boolean hasEnchantFromGroup(ItemStack item, CustomEnchant.ItemGroup group) {
        if (item == null || !item.hasItemMeta()) {
            return false;
        }
        PersistentDataContainer container = item.getItemMeta().getPersistentDataContainer();
        for (CustomEnchant enc : CustomEnchant.values()) {
            NamespacedKey key;
            if (enc.getItemGroup() != group || !container.has(key = new NamespacedKey((Plugin)this.plugin, "enchant_" + enc.getId()), PersistentDataType.INTEGER)) continue;
            return true;
        }
        return false;
    }

    public boolean applyEnchantToItem(ItemStack item, CustomEnchant enchant, int level) {
        ItemMeta meta;
        ItemMeta tempMeta;
        boolean isActualAxe;
        if (item == null || item.getType().isAir()) {
            return false;
        }
        if (!enchant.getItemGroup().canApply(item.getType())) {
            return false;
        }
        String matName = item.getType().name();
        boolean bl = isActualAxe = (matName.endsWith("_AXE") || matName.equals("AXE")) && !matName.contains("PICKAXE");
        if (isActualAxe) {
            if (enchant.getItemGroup() == CustomEnchant.ItemGroup.SWORD && this.hasEnchantFromGroup(item, CustomEnchant.ItemGroup.TOOL)) {
                return false;
            }
            if (enchant.getItemGroup() == CustomEnchant.ItemGroup.TOOL && this.hasEnchantFromGroup(item, CustomEnchant.ItemGroup.SWORD)) {
                return false;
            }
        }
        if (item.getType() == Material.TRIDENT && (tempMeta = item.getItemMeta()) != null) {
            boolean hasRiptide;
            PersistentDataContainer pdc = tempMeta.getPersistentDataContainer();
            NamespacedKey loyaltyKey = new NamespacedKey((Plugin)this.plugin, "enchant_loyalty");
            NamespacedKey channelingKey = new NamespacedKey((Plugin)this.plugin, "enchant_channeling");
            NamespacedKey riptideKey = new NamespacedKey((Plugin)this.plugin, "enchant_riptide");
            boolean hasLoyalty = pdc.has(loyaltyKey, PersistentDataType.INTEGER) && (Integer)pdc.get(loyaltyKey, PersistentDataType.INTEGER) > 0;
            boolean hasChanneling = pdc.has(channelingKey, PersistentDataType.INTEGER) && (Integer)pdc.get(channelingKey, PersistentDataType.INTEGER) > 0;
            boolean bl2 = hasRiptide = pdc.has(riptideKey, PersistentDataType.INTEGER) && (Integer)pdc.get(riptideKey, PersistentDataType.INTEGER) > 0;
            if (enchant == CustomEnchant.RIPTIDE ? hasLoyalty || hasChanneling : (enchant == CustomEnchant.LOYALTY || enchant == CustomEnchant.CHANNELING) && hasRiptide) {
                return false;
            }
        }
        if ((meta = item.getItemMeta()) == null) {
            return false;
        }
        PersistentDataContainer container = meta.getPersistentDataContainer();
        NamespacedKey flagKey = new NamespacedKey((Plugin)this.plugin, "is_custom_enchanted");
        container.set(flagKey, PersistentDataType.INTEGER, 1);
        NamespacedKey key = new NamespacedKey((Plugin)this.plugin, "enchant_" + enchant.getId());
        container.set(key, PersistentDataType.INTEGER, level);
        item.setItemMeta(meta);
        if (this.isChargeable(enchant)) {
            this.setCharges(item, enchant, this.getMaxCharges(enchant, level));
        }
        this.rebuildEnchantLore(item);
        return true;
    }

    public boolean hasUltimateEnchant(ItemStack item, UltimateEnchant enchant) {
        if (item == null || !item.hasItemMeta()) {
            return false;
        }
        NamespacedKey key = new NamespacedKey((Plugin)this.plugin, "ult_enchant_" + enchant.getId());
        return item.getItemMeta().getPersistentDataContainer().has(key, PersistentDataType.INTEGER);
    }

    public int getUltimateEnchantLevel(ItemStack item, UltimateEnchant enchant) {
        if (item == null || !item.hasItemMeta()) {
            return 0;
        }
        NamespacedKey key = new NamespacedKey((Plugin)this.plugin, "ult_enchant_" + enchant.getId());
        return (Integer)item.getItemMeta().getPersistentDataContainer().getOrDefault(key, PersistentDataType.INTEGER, 0);
    }

    public boolean applyUltimateEnchantToItem(ItemStack item, UltimateEnchant enchant, int level) {
        if (item == null || item.getType().isAir()) {
            return false;
        }
        if (item.getType() != Material.BOOK && item.getType() != Material.ENCHANTED_BOOK && !enchant.getGroup().canApply(item.getType())) {
            return false;
        }
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return false;
        }
        PersistentDataContainer container = meta.getPersistentDataContainer();
        if (enchant == UltimateEnchant.ONE_FOR_ALL) {
            for (Enchantment vanilla : meta.getEnchants().keySet()) {
                meta.removeEnchant(vanilla);
            }
            for (CustomEnchant cEnc : CustomEnchant.values()) {
                NamespacedKey cKey = new NamespacedKey((Plugin)this.plugin, "enchant_" + cEnc.getId());
                if (!container.has(cKey, PersistentDataType.INTEGER)) continue;
                container.remove(cKey);
            }
        }
        for (UltimateEnchant uEnc : UltimateEnchant.values()) {
            NamespacedKey uKey = new NamespacedKey((Plugin)this.plugin, "ult_enchant_" + uEnc.getId());
            if (!container.has(uKey, PersistentDataType.INTEGER)) continue;
            container.remove(uKey);
        }
        NamespacedKey flagKey = new NamespacedKey((Plugin)this.plugin, "is_custom_enchanted");
        container.set(flagKey, PersistentDataType.INTEGER, 1);
        NamespacedKey key = new NamespacedKey((Plugin)this.plugin, "ult_enchant_" + enchant.getId());
        container.set(key, PersistentDataType.INTEGER, level);
        item.setItemMeta(meta);
        this.rebuildEnchantLore(item);
        return true;
    }

    public Enchantment getVanillaEquivalent(CustomEnchant enc) {
        String key = null;
        switch (enc) {
            case EFFICIENCY: {
                key = "efficiency";
                break;
            }
            case SHARPNESS: {
                key = "sharpness";
                break;
            }
            case POWER: {
                key = "power";
                break;
            }
            case FLAME: {
                key = "flame";
                break;
            }
            case LOOTING: {
                key = "looting";
                break;
            }
            case FIRE_ASPECT: {
                key = "fire_aspect";
                break;
            }
            case PROTECTION: {
                key = "protection";
                break;
            }
            case THORNS: {
                key = "thorns";
                break;
            }
            case FEATHER_FALLING: {
                key = "feather_falling";
                break;
            }
            case MENDING: {
                key = "mending";
                break;
            }
            case IMPALING: {
                key = "impaling";
                break;
            }
            case LOYALTY: {
                key = "loyalty";
                break;
            }
            case RIPTIDE: {
                key = "riptide";
                break;
            }
            case CHANNELING: {
                key = "channeling";
                break;
            }
            case DENSITY: {
                key = "density";
                break;
            }
            case BREACH: {
                key = "breach";
                break;
            }
            case WIND_BURST: {
                key = "wind_burst";
                break;
            }
            case LUNGE: {
                key = "lunge";
                break;
            }
            default: {
                return null;
            }
        }
        if (key == null) {
            return null;
        }
        try {
            return Enchantment.getByKey((NamespacedKey)NamespacedKey.minecraft((String)key));
        }
        catch (Throwable t) {
            try {
                return (Enchantment)Registry.ENCHANTMENT.get(NamespacedKey.minecraft((String)key));
            }
            catch (Throwable t2) {
                return null;
            }
        }
    }

    public void rebuildEnchantLore(ItemStack item) {
        ItemBuilder.updateItem(item);
    }

    private String toRoman(int num) {
        String[] initials = new String[]{"", "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X"};
        return num <= 10 ? initials[num] : String.valueOf(num);
    }

    public NamespacedKey getChargesKey(CustomEnchant enchant) {
        return new NamespacedKey((Plugin)this.plugin, "cwe_charges_" + enchant.getId());
    }

    public boolean isChargeable(CustomEnchant enchant) {
        switch (enchant) {
            case TELEKINESIS: 
            case REPLENISH: 
            case LIFE_STEAL: 
            case VAMPIRISM: {
                return true;
            }
        }
        return false;
    }

    public int getMaxCharges(CustomEnchant enchant, int level) {
        return 100 + level * 50;
    }

    public int getCharges(ItemStack item, CustomEnchant enchant) {
        NamespacedKey key;
        if (item == null || !item.hasItemMeta()) {
            return 0;
        }
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        if (!container.has(key = this.getChargesKey(enchant), PersistentDataType.INTEGER)) {
            NamespacedKey enchantKey = new NamespacedKey((Plugin)this.plugin, "enchant_" + enchant.getId());
            if (container.has(enchantKey, PersistentDataType.INTEGER)) {
                int level = (Integer)container.get(enchantKey, PersistentDataType.INTEGER);
                return this.getMaxCharges(enchant, level);
            }
            return 0;
        }
        return (Integer)container.get(key, PersistentDataType.INTEGER);
    }

    public void setCharges(ItemStack item, CustomEnchant enchant, int amount) {
        if (item == null || !item.hasItemMeta()) {
            return;
        }
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        NamespacedKey key = this.getChargesKey(enchant);
        NamespacedKey enchantKey = new NamespacedKey((Plugin)this.plugin, "enchant_" + enchant.getId());
        int level = (Integer)container.getOrDefault(enchantKey, PersistentDataType.INTEGER, 1);
        int max = this.getMaxCharges(enchant, level);
        container.set(key, PersistentDataType.INTEGER, Math.max(0, Math.min(amount, max)));
        item.setItemMeta(meta);
    }

    public boolean consumeCharge(ItemStack item, CustomEnchant enchant, int amount) {
        if (!this.isChargeable(enchant)) {
            return true;
        }
        int current = this.getCharges(item, enchant);
        if (current < amount) {
            return false;
        }
        this.setCharges(item, enchant, current - amount);
        return true;
    }

    public Material getFuelMaterial(CustomEnchant enchant) {
        switch (enchant) {
            case TELEKINESIS: {
                return Material.ENDER_PEARL;
            }
            case REPLENISH: {
                return Material.WHEAT_SEEDS;
            }
            case LIFE_STEAL: 
            case VAMPIRISM: {
                return Material.REDSTONE;
            }
        }
        return Material.LAPIS_LAZULI;
    }
}


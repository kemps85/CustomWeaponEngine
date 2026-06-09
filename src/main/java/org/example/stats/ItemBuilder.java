package org.example.stats;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.ChatColor;
import org.example.core.CustomWeaponEngine;
import org.example.enchant.CustomEnchant;
import org.example.enchant.UltimateEnchant;
import org.example.enchant.EnchantManager;

import java.util.ArrayList;
import java.util.List;

/**
 * ItemBuilder — "Cái Khuôn" trung tâm đúc ra Display Name + Lore cho mọi vật phẩm.
 * Mọi hệ thống (Enchant, Reforge, Appraiser, Stats Editor) chỉ cần chỉnh PDC
 * rồi gọi ItemBuilder.updateItem(item) để đồng bộ hiển thị.
 */
public class ItemBuilder {

    private static JavaPlugin plugin;

    public static void init(JavaPlugin pluginInstance) {
        plugin = pluginInstance;
    }

    /**
     * Entry point duy nhất. Đọc toàn bộ PDC và đúc lại Display Name + Lore.
     */
    public static void updateItem(ItemStack item) {
        if (item == null || item.getType().isAir()) return;
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;
        PersistentDataContainer pdc = meta.getPersistentDataContainer();

        // ═══════════════════════════════════════════════════════
        // 1. ĐỌC DỮ LIỆU TỪ PDC
        // ═══════════════════════════════════════════════════════

        // Rarity
        String rarityStr = getStr(pdc, ItemStatsGUI.KEY_RARITY, "");
        ItemStatsGUI.Rarity rarity = ItemStatsGUI.Rarity.NONE;
        try { if (!rarityStr.isEmpty()) rarity = ItemStatsGUI.Rarity.valueOf(rarityStr); } catch (Exception ignored) {}

        // Is Weapon?
        boolean isWeapon = detectIsWeapon(item, pdc);

        // --- FALLBACK CHO ITEM CŨ (KHÔNG CÓ STATS TRONG PDC) ---
        String cweId = getStr(pdc, "cwe_id", "");
        if (!cweId.isEmpty()) {
            // Nếu item có ID nhưng thiếu stat_damage (item tạo từ phiên bản cũ)
            if (!pdc.has(new NamespacedKey(plugin, "stat_damage"), PersistentDataType.DOUBLE)) {
                // Thử lấy item gốc từ library
                ItemStack libItem = ((CustomWeaponEngine) plugin).getLibraryConfig().getItemStack("items." + cweId);
                if (libItem != null && libItem.hasItemMeta()) {
                    PersistentDataContainer libPdc = libItem.getItemMeta().getPersistentDataContainer();
                    
                    // Copy toàn bộ stats từ libPdc sang pdc hiện tại
                    String[] statKeys = {"stat_damage", "stat_strength", "stat_crit_chance", "stat_crit_damage", 
                                         "stat_defense", "stat_health", "stat_intelligence", "stat_speed"};
                    for (String k : statKeys) {
                        NamespacedKey key = new NamespacedKey(plugin, k);
                        if (libPdc.has(key, PersistentDataType.DOUBLE)) {
                            pdc.set(key, PersistentDataType.DOUBLE, libPdc.get(key, PersistentDataType.DOUBLE));
                        }
                    }
                    
                    // Copy cả Ability nếu bị mất (Juju Shortbow cũ) và nếu chưa có skill được add vào
                    if (!pdc.has(new NamespacedKey(plugin, "stat_setbonus_title"), PersistentDataType.STRING)) {
                        if (libPdc.has(new NamespacedKey(plugin, "stat_setbonus_title"), PersistentDataType.STRING)) {
                            pdc.set(new NamespacedKey(plugin, "stat_setbonus_title"), PersistentDataType.STRING, libPdc.get(new NamespacedKey(plugin, "stat_setbonus_title"), PersistentDataType.STRING));
                            if (libPdc.has(new NamespacedKey(plugin, "stat_setbonus_desc1"), PersistentDataType.STRING))
                                pdc.set(new NamespacedKey(plugin, "stat_setbonus_desc1"), PersistentDataType.STRING, libPdc.get(new NamespacedKey(plugin, "stat_setbonus_desc1"), PersistentDataType.STRING));
                            if (libPdc.has(new NamespacedKey(plugin, "stat_setbonus_desc2"), PersistentDataType.STRING))
                                pdc.set(new NamespacedKey(plugin, "stat_setbonus_desc2"), PersistentDataType.STRING, libPdc.get(new NamespacedKey(plugin, "stat_setbonus_desc2"), PersistentDataType.STRING));
                            if (libPdc.has(new NamespacedKey(plugin, "stat_ability_click"), PersistentDataType.STRING))
                                pdc.set(new NamespacedKey(plugin, "stat_ability_click"), PersistentDataType.STRING, libPdc.get(new NamespacedKey(plugin, "stat_ability_click"), PersistentDataType.STRING));
                        }
                    }
                }
            }
        }

        // Base stats
        double damage = getDbl(pdc, "stat_damage");
        if (damage == 0) damage = getDbl(pdc, "cwe_damage");
        double[] stats = new double[7];
        stats[0] = getDbl(pdc, ItemStatsGUI.KEY_STRENGTH);
        stats[1] = getDbl(pdc, ItemStatsGUI.KEY_CRIT_CHANCE);
        stats[2] = getDbl(pdc, ItemStatsGUI.KEY_CRIT_DAMAGE);
        stats[3] = getDbl(pdc, ItemStatsGUI.KEY_HEALTH);
        stats[4] = getDbl(pdc, ItemStatsGUI.KEY_DEFENSE);
        stats[5] = getDbl(pdc, ItemStatsGUI.KEY_INTELLIGENCE);
        stats[6] = getDbl(pdc, ItemStatsGUI.KEY_SPEED);

        // Ability / Set Bonus
        String bTitle = getStr(pdc, ItemStatsGUI.KEY_SETBONUS_TITLE, "");
        String bD1 = getStr(pdc, ItemStatsGUI.KEY_SETBONUS_DESC1, "");
        String bD2 = getStr(pdc, ItemStatsGUI.KEY_SETBONUS_DESC2, "");
        String clickType = getStr(pdc, ItemStatsGUI.KEY_ABILITY_CLICK, "RIGHT CLICK");

        // Reforge
        String reforgePrefix = getStr(pdc, "cwe_reforge", "");

        // Apply baseStatMultiplier to base stats shown in lore
        double baseMult = 1.0;
        if (reforgePrefix != null && !reforgePrefix.isEmpty()) {
            org.example.enchant.ReforgeSystem.ItemCategory cat;
            if (isWeapon) {
                String typeName = item.getType().name();
                if (typeName.contains("BOW") || typeName.contains("CROSSBOW")) {
                    cat = org.example.enchant.ReforgeSystem.ItemCategory.RANGED;
                } else {
                    cat = org.example.enchant.ReforgeSystem.ItemCategory.MELEE;
                }
            } else {
                cat = org.example.enchant.ReforgeSystem.ItemCategory.ARMOR;
            }
            org.example.enchant.ReforgeSystem.ReforgeTier rfTier = org.example.enchant.ReforgeSystem.ReforgeTier.COMMON;
            if (rarity != null) {
                try {
                    rfTier = org.example.enchant.ReforgeSystem.ReforgeTier.valueOf(rarity.name());
                } catch (Exception ignored) {}
            }
            org.example.enchant.ReforgeSystem.ReforgeStat bonus = org.example.enchant.ReforgeSystem.getReforgeStat(reforgePrefix, cat, rfTier);
            if (bonus != null) {
                baseMult = bonus.baseStatMultiplier;
            }
        }
        if (baseMult > 1.0) {
            for (int i = 0; i < stats.length; i++) {
                stats[i] *= baseMult;
            }
        }

        // ═══════════════════════════════════════════════════════
        // 2. XÂY DỰNG DISPLAY NAME
        // ═══════════════════════════════════════════════════════
        rebuildDisplayName(meta, pdc, rarity, reforgePrefix);

        // ═══════════════════════════════════════════════════════
        // 3. XÂY DỰNG LORE
        // ═══════════════════════════════════════════════════════
        List<String> lore = new ArrayList<>();

        // --- Reforge bonus (read from PDC for display) ---
        double rfStr = getDbl(pdc, "cwe_rf_strength");
        double rfCc = getDbl(pdc, "cwe_rf_crit_chance");
        double rfCd = getDbl(pdc, "cwe_rf_crit_damage");
        double rfHp = getDbl(pdc, "cwe_rf_health");
        double rfDef = getDbl(pdc, "cwe_rf_defense");
        double rfInt = getDbl(pdc, "cwe_rf_intelligence");
        double rfSpd = getDbl(pdc, "cwe_rf_speed");
        double rfAtk = getDbl(pdc, "cwe_rf_attack_speed");

        if (isWeapon) {
            // --- WEAPON LORE ---
            if (damage > 0) lore.add(String.format("§7Damage: §c+%.0f", damage));
            addStatLine(lore, "Strength", "§c", stats[0], rfStr, reforgePrefix, false);
            addStatLine(lore, "Attack Speed", "§e", 0.0, rfAtk, reforgePrefix, true);
            if (damage > 0 || (stats[0] + rfStr) > 0 || rfAtk > 0) lore.add("§f");

            addStatLine(lore, "Crit Chance", "§9", stats[1], rfCc, reforgePrefix, true);
            addStatLine(lore, "Crit Damage", "§5", stats[2], rfCd, reforgePrefix, true);
            addStatLine(lore, "Intelligence", "§a", stats[5], rfInt, reforgePrefix, false);
            if ((stats[1] + rfCc) > 0 || (stats[2] + rfCd) > 0 || (stats[5] + rfInt) > 0) lore.add("§f");

            // Withered special note
            double levelMult = getDbl(pdc, "cwe_rf_level_mult");
            if (levelMult > 0) {
                lore.add(String.format("§8Withered: §7+%.1fx Fighting Level", levelMult));
            }

            // --- ENCHANT LINES ---
            buildEnchantLines(lore, pdc);

            // Ability
            if (!bTitle.isEmpty()) {
                lore.add("§6Item Ability: " + bTitle + " §e§l[" + clickType + "]");
                if (!bD1.isEmpty()) lore.add("§7" + bD1);
                if (!bD2.isEmpty()) lore.add("§7" + bD2);

                // Mana cost & cooldown from config
                String weaponId = getWeaponIdFromDisplayName(meta, pdc);
                if (weaponId != null) {
                    FileConfiguration config = CustomWeaponEngine.getInstance().getConfig();
                    double mana = config.getDouble("weapons." + weaponId + ".mana-cost", 0);
                    int cd = config.getInt("weapons." + weaponId + ".cooldown", 0);
                    if (mana > 0) lore.add("§9Mana Cost: §3" + (int) mana);
                    if (cd > 0) lore.add("§9Cooldown: §a" + cd + "s");
                }
                lore.add("§f");
            }
        } else {
            // --- ARMOR LORE ---
            addStatLine(lore, "Health", "§c", stats[3], rfHp, reforgePrefix, false);
            addStatLine(lore, "Defense", "§a", stats[4], rfDef, reforgePrefix, false);
            addStatLine(lore, "Strength", "§c", stats[0], rfStr, reforgePrefix, false);
            if ((stats[3] + rfHp) > 0 || (stats[4] + rfDef) > 0 || (stats[0] + rfStr) > 0) lore.add("§f");

            addStatLine(lore, "Crit Chance", "§9", stats[1], rfCc, reforgePrefix, true);
            addStatLine(lore, "Crit Damage", "§5", stats[2], rfCd, reforgePrefix, true);
            addStatLine(lore, "Speed", "§f", stats[6], rfSpd, reforgePrefix, false);
            addStatLine(lore, "Intelligence", "§a", stats[5], rfInt, reforgePrefix, false);
            if ((stats[1] + rfCc) > 0 || (stats[2] + rfCd) > 0 || (stats[6] + rfSpd) > 0 || (stats[5] + rfInt) > 0) lore.add("§f");

            // --- ENCHANT LINES ---
            buildEnchantLines(lore, pdc);

            // Set Bonus
            if (!bTitle.isEmpty()) {
                lore.add("§6Full Set Bonus: " + bTitle);
                if (!bD1.isEmpty()) lore.add("§7" + bD1);
                if (!bD2.isEmpty()) lore.add("§7" + bD2);
                lore.add("§f");
            }
        }


        // --- RARITY TAG (last line) ---
        if (rarity != ItemStatsGUI.Rarity.NONE) {
            String rarityTag = rarity.color + "§l" + rarity.name();
            if (isWeapon) {
                rarityTag += " " + getWeaponTypeName(item);
            } else {
                rarityTag += " " + getArmorTypeName(item);
            }
            lore.add(rarityTag);
        }

        // --- SYNC VANILLA ENCHANTS ---
        syncVanillaEnchants(meta, pdc);

        // --- APPLY ACTUAL ATTRIBUTE MODIFIERS ---
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        if (meta.hasAttributeModifiers()) {
            meta.removeAttributeModifier(org.bukkit.attribute.Attribute.GENERIC_ATTACK_DAMAGE);
            meta.removeAttributeModifier(org.bukkit.attribute.Attribute.GENERIC_ATTACK_SPEED);
        }
        
        if (isWeapon && damage > 0) {
            Material mat = item.getType();
            org.bukkit.attribute.AttributeModifier dmgMod = new org.bukkit.attribute.AttributeModifier(
                new NamespacedKey(plugin, "cwe_base_damage"),
                damage,
                org.bukkit.attribute.AttributeModifier.Operation.ADD_NUMBER,
                org.bukkit.inventory.EquipmentSlotGroup.ANY
            );
            meta.addAttributeModifier(org.bukkit.attribute.Attribute.GENERIC_ATTACK_DAMAGE, dmgMod);

            // Restore vanilla attack speed offsets (base is 4.0)
            double atkSpeed = -2.4; // Sword (1.6)
            if (mat.name().contains("AXE")) atkSpeed = -3.0; // Axe (1.0)
            if (mat == Material.MACE) atkSpeed = -3.4; // Mace (0.6)
            if (mat == Material.TRIDENT) atkSpeed = -2.9; // Trident (1.1)

            org.bukkit.attribute.AttributeModifier spdMod = new org.bukkit.attribute.AttributeModifier(
                new NamespacedKey(plugin, "cwe_base_atk_speed"),
                atkSpeed,
                org.bukkit.attribute.AttributeModifier.Operation.ADD_NUMBER,
                org.bukkit.inventory.EquipmentSlotGroup.ANY
            );
            meta.addAttributeModifier(org.bukkit.attribute.Attribute.GENERIC_ATTACK_SPEED, spdMod);
        }

        if (meta.hasAttributeModifiers()) {
            try {
                meta.removeAttributeModifier(org.bukkit.attribute.Attribute.PLAYER_ENTITY_INTERACTION_RANGE);
            } catch (Exception ignored) {}
        }
        NamespacedKey reachKey = new NamespacedKey(plugin, "enchant_reach");
        if (meta.getPersistentDataContainer().has(reachKey, org.bukkit.persistence.PersistentDataType.INTEGER)) {
            int reachLvl = meta.getPersistentDataContainer().get(reachKey, org.bukkit.persistence.PersistentDataType.INTEGER);
            if (reachLvl > 0) {
                try {
                    org.bukkit.attribute.AttributeModifier reachMod = new org.bukkit.attribute.AttributeModifier(
                        new NamespacedKey(plugin, "cwe_enchant_reach"),
                        reachLvl * 0.5,
                        org.bukkit.attribute.AttributeModifier.Operation.ADD_NUMBER,
                        org.bukkit.inventory.EquipmentSlotGroup.ANY
                    );
                    meta.addAttributeModifier(org.bukkit.attribute.Attribute.PLAYER_ENTITY_INTERACTION_RANGE, reachMod);
                } catch (Exception ignored) {}
            }
        }

        meta.setLore(lore);
        item.setItemMeta(meta);
    }

    // ═══════════════════════════════════════════════════════
    // DISPLAY NAME BUILDER
    // ═══════════════════════════════════════════════════════

    private static void rebuildDisplayName(ItemMeta meta, PersistentDataContainer pdc, ItemStatsGUI.Rarity rarity, String reforgePrefix) {
        NamespacedKey ogKey = new NamespacedKey(plugin, "cwe_original_name");
        String originalName;
        if (pdc.has(ogKey, PersistentDataType.STRING)) {
            originalName = pdc.get(ogKey, PersistentDataType.STRING);
        } else {
            if (!meta.hasDisplayName()) return;
            originalName = meta.getDisplayName();
        }

        String rawName = stripLeadingColorCodes(originalName);
        String color = (rarity != ItemStatsGUI.Rarity.NONE) ? rarity.color : "§f";

        if (!reforgePrefix.isEmpty()) {
            meta.setDisplayName(color + reforgePrefix + " " + rawName);
        } else {
            meta.setDisplayName(color + rawName);
        }
    }

    private static String stripLeadingColorCodes(String name) {
        int i = 0;
        while (i < name.length()) {
            if (name.charAt(i) == '§' && i + 1 < name.length()) {
                i += 2;
            } else {
                break;
            }
        }
        return name.substring(i);
    }

    // ═══════════════════════════════════════════════════════
    // ENCHANT LINES BUILDER
    // ═══════════════════════════════════════════════════════

    private static void buildEnchantLines(List<String> lore, PersistentDataContainer pdc) {
        // Ultimate Enchants (§d§l purple bold)
        List<String> ultLines = new ArrayList<>();
        for (UltimateEnchant uEnc : UltimateEnchant.values()) {
            NamespacedKey key = new NamespacedKey(plugin, "ult_enchant_" + uEnc.getId());
            if (pdc.has(key, PersistentDataType.INTEGER)) {
                int lvl = pdc.get(key, PersistentDataType.INTEGER);
                if (lvl > 0) {
                    ultLines.add("§d§l" + uEnc.getDisplayName().toUpperCase() + " " + toRoman(lvl));
                }
            }
        }
        if (!ultLines.isEmpty()) {
            lore.addAll(ultLines);
        }

        // Custom Enchants (§9 blue, 3 per line)
        List<String> enchantStrings = new ArrayList<>();
        EnchantManager em = CustomWeaponEngine.getEnchantManager();
        for (CustomEnchant enc : CustomEnchant.values()) {
            NamespacedKey key = new NamespacedKey(plugin, "enchant_" + enc.getId());
            if (pdc.has(key, PersistentDataType.INTEGER)) {
                int lvl = pdc.get(key, PersistentDataType.INTEGER);
                if (lvl > 0) {
                    String line = "§9" + enc.getDisplayName() + " " + toRoman(lvl);
                    if (em != null && em.isChargeable(enc)) {
                        NamespacedKey chargesKey = new NamespacedKey(plugin, "cwe_charges_" + enc.getId());
                        int current = pdc.has(chargesKey, PersistentDataType.INTEGER)
                                ? pdc.get(chargesKey, PersistentDataType.INTEGER)
                                : em.getMaxCharges(enc, lvl);
                        int max = em.getMaxCharges(enc, lvl);
                        line += " §b(" + current + "/" + max + "⚡)";
                    }
                    enchantStrings.add(line);
                }
            }
        }

        if (!enchantStrings.isEmpty()) {
            int itemsPerLine = 3;
            StringBuilder currentRow = new StringBuilder();
            for (int i = 0; i < enchantStrings.size(); i++) {
                currentRow.append(enchantStrings.get(i));
                if ((i + 1) % itemsPerLine != 0 && i != enchantStrings.size() - 1) {
                    currentRow.append("§7, ");
                } else {
                    lore.add(currentRow.toString());
                    currentRow = new StringBuilder();
                }
            }
            lore.add("§f");
        }
    }

    // ═══════════════════════════════════════════════════════
    // VANILLA ENCHANT SYNC
    // ═══════════════════════════════════════════════════════

    @SuppressWarnings("deprecation")
    private static void syncVanillaEnchants(ItemMeta meta, PersistentDataContainer pdc) {
        EnchantManager em = CustomWeaponEngine.getEnchantManager();
        if (em == null) return;

        boolean hasAnyEnchant = false;
        for (CustomEnchant enc : CustomEnchant.values()) {
            NamespacedKey key = new NamespacedKey(plugin, "enchant_" + enc.getId());
            if (pdc.has(key, PersistentDataType.INTEGER)) {
                int lvl = pdc.get(key, PersistentDataType.INTEGER);
                if (lvl > 0) {
                    hasAnyEnchant = true;
                    Enchantment vanillaMatch = em.getVanillaEquivalent(enc);
                    if (vanillaMatch != null) {
                        meta.addEnchant(vanillaMatch, lvl, true);
                    }
                }
            }
        }

        if (hasAnyEnchant) {
            try {
                Enchantment glowEnchant = Enchantment.getByKey(org.bukkit.NamespacedKey.minecraft("luck_of_the_sea"));
                if (glowEnchant != null && !meta.hasEnchant(glowEnchant)) {
                    meta.addEnchant(glowEnchant, 1, true);
                }
            } catch (Throwable e) {
                Enchantment luckOld = Enchantment.getByName("LUCK");
                if (luckOld != null && !meta.hasEnchant(luckOld)) {
                    meta.addEnchant(luckOld, 1, true);
                }
            }
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
    }

    // ═══════════════════════════════════════════════════════
    // HELPERS
    // ═══════════════════════════════════════════════════════

    private static void addStatLine(List<String> lore, String label, String colorCode,
                                     double baseValue, double bonusValue, String prefix, boolean isPercent) {
        double totalValue = baseValue + bonusValue;
        if (totalValue > 0) {
            String pct = isPercent ? "%" : "";
            String line = String.format("§7%s: %s+%.0f%s", label, colorCode, totalValue, pct);
            if (bonusValue > 0 && prefix != null && !prefix.isEmpty()) {
                line += String.format(" §8(%s +%.0f%s)", prefix, bonusValue, pct);
            }
            lore.add(line);
        }
    }

    public static boolean detectIsWeapon(ItemStack item, PersistentDataContainer pdc) {
        NamespacedKey weaponKey = new NamespacedKey(plugin, "cwe_is_weapon");
        if (pdc.has(weaponKey, PersistentDataType.INTEGER)) {
            return pdc.get(weaponKey, PersistentDataType.INTEGER) == 1;
        }
        String name = item.getType().name();
        Material mat = item.getType();
        if (item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
            if (item.getItemMeta().getDisplayName().contains("Wand")) return true;
        }
        return name.contains("SWORD") || name.contains("AXE") || name.contains("BOW") || name.contains("CROSSBOW") || name.contains("SPEAR")
                || mat == Material.STICK || mat == Material.BLAZE_ROD
                || mat == Material.TRIDENT || mat == Material.MACE;
    }

    public static String getWeaponTypeName(ItemStack item) {
        if (item == null) return "WEAPON";
        Material mat = item.getType();
        String name = mat.name();
        if (item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
            if (item.getItemMeta().getDisplayName().contains("Wand")) return "WAND";
        }
        if (name.contains("SWORD")) return "SWORD";
        if (name.contains("AXE")) return "AXE";
        if (name.contains("SPEAR")) return "SPEAR";
        if (name.contains("BOW")) return "BOW";
        if (name.contains("CROSSBOW")) return "CROSSBOW";
        if (mat == Material.TRIDENT) return "TRIDENT";
        if (mat == Material.MACE) return "MACE";
        if (mat == Material.STICK || mat == Material.BLAZE_ROD) return "WAND";
        return "WEAPON";
    }

    public static String getArmorTypeName(ItemStack item) {
        if (item == null) return "ARMOR";
        String name = item.getType().name();
        Material mat = item.getType();
        if (name.contains("HELMET")) return "HELMET";
        if (name.contains("CHESTPLATE")) return "CHESTPLATE";
        if (name.contains("LEGGINGS")) return "LEGGINGS";
        if (name.contains("BOOTS")) return "BOOTS";
        if (mat == Material.ELYTRA) return "ELYTRA";
        if (mat == Material.SHIELD) return "SHIELD";
        return "ARMOR";
    }

    private static String getWeaponIdFromDisplayName(ItemMeta meta, PersistentDataContainer pdc) {
        NamespacedKey ogKey = new NamespacedKey(plugin, "cwe_original_name");
        String displayName = pdc.has(ogKey, PersistentDataType.STRING)
                ? pdc.get(ogKey, PersistentDataType.STRING)
                : (meta.hasDisplayName() ? meta.getDisplayName() : null);
        if (displayName == null) return null;

        String clean = ChatColor.stripColor(displayName);
        FileConfiguration config = CustomWeaponEngine.getInstance().getConfig();
        if (!config.contains("weapons")) return null;
        for (String id : config.getConfigurationSection("weapons").getKeys(false)) {
            String configName = config.getString("weapons." + id + ".display-name");
            if (configName == null) continue;
            String cleanConfig = ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', configName));
            if (clean.contains(cleanConfig)) return id;
        }
        return null;
    }

    private static String toRoman(int num) {
        String[] initials = {"", "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X"};
        return num <= 10 ? initials[num] : String.valueOf(num);
    }

    private static double getDbl(PersistentDataContainer pdc, String key) {
        NamespacedKey k = new NamespacedKey(plugin, key);
        return pdc.has(k, PersistentDataType.DOUBLE) ? pdc.getOrDefault(k, PersistentDataType.DOUBLE, 0.0) : 0.0;
    }

    private static String getStr(PersistentDataContainer pdc, String key, String def) {
        NamespacedKey k = new NamespacedKey(plugin, key);
        return pdc.has(k, PersistentDataType.STRING) ? pdc.getOrDefault(k, PersistentDataType.STRING, def) : def;
    }
}

package org.example.weapon.legendary;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.example.core.CustomWeaponEngine;

public class ManaHelper {
    
    private static final java.util.Map<java.util.UUID, String> actionbarMessages = new java.util.concurrent.ConcurrentHashMap<>();
    private static final java.util.Map<java.util.UUID, Long> actionbarExpiry = new java.util.concurrent.ConcurrentHashMap<>();

    public static String getActionbarMessage(Player p) {
        if (!actionbarExpiry.containsKey(p.getUniqueId())) return "";
        if (System.currentTimeMillis() > actionbarExpiry.get(p.getUniqueId())) {
            actionbarMessages.remove(p.getUniqueId());
            actionbarExpiry.remove(p.getUniqueId());
            return "";
        }
        return actionbarMessages.get(p.getUniqueId());
    }

    public static boolean consumeMana(Player p, double amount, CustomWeaponEngine plugin, String abilityName) {
        if (!Bukkit.getPluginManager().isPluginEnabled("AuraSkills")) return true; // No mana plugin = free skills
        
        try {
            dev.aurelium.auraskills.api.user.SkillsUser user = dev.aurelium.auraskills.api.AuraSkillsApi.get().getUser(p.getUniqueId());
            if (user == null) return true; // Let them use it if data is missing
            
            // Check Wise Dragon
            boolean isWise = true;
            ItemStack[] armors = p.getInventory().getArmorContents();
            for (int i = 0; i < 4; i++) {
                ItemStack armor = armors[i];
                if (armor == null || !armor.hasItemMeta()) { isWise = false; break; }
                String aid = armor.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(plugin, "cwe_id"), PersistentDataType.STRING);
                if (aid == null || !aid.startsWith("wise_")) { isWise = false; break; }
            }
            
            if (isWise) {
                amount = amount * 0.67; // -33% mana cost
            }

            // Check Ultimate Wise on main hand
            ItemStack hand = p.getInventory().getItemInMainHand();
            if (hand != null && hand.hasItemMeta()) {
                NamespacedKey uwKey = new NamespacedKey(plugin, "ult_enchant_ultimate_wise");
                if (hand.getItemMeta().getPersistentDataContainer().has(uwKey, PersistentDataType.INTEGER)) {
                    int uwLevel = hand.getItemMeta().getPersistentDataContainer().getOrDefault(uwKey, PersistentDataType.INTEGER, 0);
                    if (uwLevel > 0) {
                        amount = amount * (1.0 - (uwLevel * 0.10)); // Giảm 10% mỗi level (Max 50%)
                    }
                }
            }
            
            if (user.getMana() >= amount) {
                user.setMana(user.getMana() - amount);
                actionbarMessages.put(p.getUniqueId(), "§b-" + (int)amount + " Mana (" + abilityName + ")");
                actionbarExpiry.put(p.getUniqueId(), System.currentTimeMillis() + 2000L);
                return true;
            } else {
                p.sendMessage("§cKhông đủ Mana! (" + (int)amount + " Mana)");
                return false;
            }
        } catch (Exception e) {
            return true;
        }
    }
}

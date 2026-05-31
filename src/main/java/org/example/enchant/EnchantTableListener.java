package org.example.enchant;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;
import org.example.stats.ItemBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EnchantTableListener implements Listener {

    private final JavaPlugin plugin;
    private final Random random = new Random();

    public EnchantTableListener(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEnchantItem(EnchantItemEvent event) {
        ItemStack item = event.getItem();
        int expLevelCost = event.getExpLevelCost();

        // Exp level cost gives us the tier of the enchant (e.g. 1-10, 11-20, 21-30)
        // We will roll for custom enchants based on the level.
        int chance = expLevelCost * 2; // Up to 60% chance for a custom enchant at level 30

        if (random.nextInt(100) < chance) {
            // Pick a random applicable custom enchant
            List<CustomEnchant> applicableEnchants = new ArrayList<>();
            for (CustomEnchant enchant : CustomEnchant.values()) {
                if (enchant.getItemGroup().canApply(item.getType())) {
                    applicableEnchants.add(enchant);
                }
            }

            if (!applicableEnchants.isEmpty()) {
                CustomEnchant chosenEnchant = applicableEnchants.get(random.nextInt(applicableEnchants.size()));
                
                // Determine level based on expLevelCost
                int maxLevel = chosenEnchant.getMaxLevel();
                int chosenLevel = 1;
                
                if (expLevelCost >= 20) {
                    chosenLevel = random.nextInt(maxLevel) + 1; // 1 to maxLevel
                } else if (expLevelCost >= 10) {
                    chosenLevel = random.nextInt(Math.max(1, maxLevel / 2)) + 1;
                }

                ItemMeta meta = item.getItemMeta();
                if (meta == null) return;
                PersistentDataContainer pdc = meta.getPersistentDataContainer();
                NamespacedKey key = new NamespacedKey(plugin, "enchant_" + chosenEnchant.getId());
                
                int currentLevel = pdc.getOrDefault(key, PersistentDataType.INTEGER, 0);
                if (chosenLevel > currentLevel) {
                    pdc.set(key, PersistentDataType.INTEGER, chosenLevel);
                    item.setItemMeta(meta);
                    
                    // We must schedule the updateItem because vanilla enchants are applied AFTER this event completes
                    plugin.getServer().getScheduler().runTask(plugin, () -> {
                        ItemBuilder.updateItem(item);
                    });
                }
            }
        }
    }
}

package org.example.armor;

import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
import org.bukkit.Bukkit;
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
import org.bukkit.attribute.Attribute;
import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
import org.bukkit.attribute.AttributeInstance;
import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
import org.bukkit.entity.LivingEntity;
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
import org.bukkit.event.EventHandler;
import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
import org.bukkit.event.EventPriority;
import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
import org.bukkit.event.Listener;
import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
import org.bukkit.event.entity.EntityDamageEvent;
import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
import org.bukkit.event.player.PlayerJoinEvent;
import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
import org.bukkit.event.player.PlayerQuitEvent;
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
import org.bukkit.plugin.java.JavaPlugin;

import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
import dev.aurelium.auraskills.api.AuraSkillsApi;
import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
import dev.aurelium.auraskills.api.user.SkillsUser;
import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
import dev.aurelium.auraskills.api.stat.Stats;
import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
import dev.aurelium.auraskills.api.stat.StatModifier;
import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
import dev.aurelium.auraskills.api.util.AuraSkillsModifier.Operation;

import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
import java.util.HashMap;
import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
import java.util.Map;
import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
import java.util.UUID;

import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
public class ArmorEnchantListener implements Listener {
    private final JavaPlugin plugin;

    private final Map<UUID, Integer> cachedWisdom = new HashMap<>();
    private final Map<UUID, Integer> cachedDefense = new HashMap<>();
    private final Map<UUID, Integer> cachedRegen = new HashMap<>();
    private final Map<UUID, Integer> cachedToughness = new HashMap<>();
    private final Map<UUID, Integer> cachedGrowth = new HashMap<>();
    private final Map<UUID, Integer> cachedSugarRush = new HashMap<>();

    public ArmorEnchantListener(JavaPlugin plugin) {
        this.plugin = plugin;

        plugin.getServer().getScheduler().runTaskTimer(plugin, () -> {
            for (Player player : plugin.getServer().getOnlinePlayers()) {
                updateArmorStats(player);
            }
        }, 20L, 20L);
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onArmorDefense(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();

        double damageModifier = 1.0;
        NamespacedKey protKey = new NamespacedKey(plugin, "enchant_protection");
        NamespacedKey csKey = new NamespacedKey(plugin, "enchant_counter_strike");
        NamespacedKey ffKey = new NamespacedKey(plugin, "enchant_feather_falling");

        for (ItemStack armor : player.getInventory().getArmorContents()) {
            if (armor == null || !armor.hasItemMeta()) continue;
            PersistentDataContainer container = armor.getItemMeta().getPersistentDataContainer();
            String name = armor.getType().name();

            if (container.has(protKey, PersistentDataType.INTEGER)) {
                damageModifier -= (container.get(protKey, PersistentDataType.INTEGER) * 0.02);
            }
            if (name.contains("CHESTPLATE") && container.has(csKey, PersistentDataType.INTEGER)) {
                damageModifier -= (container.get(csKey, PersistentDataType.INTEGER) * 0.03);
            }
            if (name.contains("BOOTS") && event.getCause() == EntityDamageEvent.DamageCause.FALL
                    && container.has(ffKey, PersistentDataType.INTEGER)) {
                damageModifier -= (container.get(ffKey, PersistentDataType.INTEGER) * 0.06);
            }
        }

        damageModifier = Math.max(damageModifier, 0.25);
        event.setDamage(event.getDamage() * damageModifier);
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onThornsReflect(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        if (!(event.getDamager() instanceof LivingEntity)) return;

        Player player = (Player) event.getEntity();
        LivingEntity attacker = (LivingEntity) event.getDamager();
        int totalThorns = 0;
        NamespacedKey thornsKey = new NamespacedKey(plugin, "enchant_thorns");

        for (ItemStack armor : player.getInventory().getArmorContents()) {
            if (armor != null && armor.hasItemMeta()) {
                PersistentDataContainer container = armor.getItemMeta().getPersistentDataContainer();
                if (container.has(thornsKey, PersistentDataType.INTEGER)) {
                    totalThorns += container.get(thornsKey, PersistentDataType.INTEGER);
                }
            }
        }

        if (totalThorns > 0) {
            double reflectedDamage = event.getDamage() * (totalThorns * 0.04);
            if (reflectedDamage > 0.1) {
                plugin.getServer().getScheduler().runTask(plugin, () -> {
                    if (attacker.isValid() && !attacker.isDead()) {
                        attacker.damage(reflectedDamage, player);
                    }
                });
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getPlayer() instanceof Player) updateArmorStats((Player) event.getPlayer());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        updateArmorStats(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onRespawn(org.bukkit.event.player.PlayerRespawnEvent event) {
        updateArmorStats(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onArmorChange(com.destroystokyo.paper.event.player.PlayerArmorChangeEvent event) {
        updateArmorStats(event.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        cachedWisdom.remove(uuid);
        cachedDefense.remove(uuid);
        cachedRegen.remove(uuid);
        cachedToughness.remove(uuid);
        cachedGrowth.remove(uuid);
        cachedSugarRush.remove(uuid);
    }

    private void updateArmorStats(Player player) {
        UUID uuid = player.getUniqueId();
        int totalGrowth = 0; int totalSugarRush = 0;
        int totalBigBrain = 0; int totalSmartyPants = 0; int totalRejuvenate = 0;
        int totalProtection = 0;

        NamespacedKey growthKey = new NamespacedKey(plugin, "enchant_growth");
        NamespacedKey sugarKey = new NamespacedKey(plugin, "enchant_sugar_rush");
        NamespacedKey bbKey = new NamespacedKey(plugin, "enchant_big_brain");
        NamespacedKey spKey = new NamespacedKey(plugin, "enchant_smarty_pants");
        NamespacedKey rejuvKey = new NamespacedKey(plugin, "enchant_rejuvenate");
        NamespacedKey protKey = new NamespacedKey(plugin, "enchant_protection");

        for (ItemStack armor : player.getInventory().getArmorContents()) {
            if (armor == null || !armor.hasItemMeta()) continue;
            PersistentDataContainer container = armor.getItemMeta().getPersistentDataContainer();
            String name = armor.getType().name();

            if (container.has(growthKey, PersistentDataType.INTEGER)) totalGrowth += container.get(growthKey, PersistentDataType.INTEGER);
            if (container.has(rejuvKey, PersistentDataType.INTEGER)) totalRejuvenate += container.get(rejuvKey, PersistentDataType.INTEGER);
            if (container.has(protKey, PersistentDataType.INTEGER)) totalProtection += container.get(protKey, PersistentDataType.INTEGER);
            if (name.contains("HELMET") && container.has(bbKey, PersistentDataType.INTEGER)) totalBigBrain += container.get(bbKey, PersistentDataType.INTEGER);
            if (name.contains("LEGGINGS") && container.has(spKey, PersistentDataType.INTEGER)) totalSmartyPants += container.get(spKey, PersistentDataType.INTEGER);
            if (name.contains("BOOTS") && container.has(sugarKey, PersistentDataType.INTEGER)) totalSugarRush += container.get(sugarKey, PersistentDataType.INTEGER);
        }

        // Apply health modifier fallback
        boolean auraEnabled = Bukkit.getPluginManager().isPluginEnabled("AuraSkills");
        AttributeInstance maxHealthAttr = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if (maxHealthAttr != null) {
            NamespacedKey growthKeyMod = new NamespacedKey(plugin, "cwe_growth_health");
            maxHealthAttr.removeModifier(growthKeyMod);
            if (!auraEnabled && totalGrowth > 0) {
                maxHealthAttr.addModifier(new org.bukkit.attribute.AttributeModifier(
                    growthKeyMod,
                    totalGrowth * 4.0,
                    org.bukkit.attribute.AttributeModifier.Operation.ADD_NUMBER
                ));
            }
        }

        // Apply movement speed modifier fallback
        AttributeInstance speedAttr = player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED);
        if (speedAttr != null) {
            NamespacedKey sugarRushModKey = new NamespacedKey(plugin, "cwe_sugar_rush_speed");
            speedAttr.removeModifier(sugarRushModKey);
            if (!auraEnabled && totalSugarRush > 0) {
                speedAttr.addModifier(new org.bukkit.attribute.AttributeModifier(
                    sugarRushModKey,
                    totalSugarRush * 0.005,
                    org.bukkit.attribute.AttributeModifier.Operation.ADD_NUMBER
                ));
            }
        }

        // ⚡ CẬP NHẬT CHỈ SỐ TOUGHNESS THỰC (GENERIC_ARMOR_TOUGHNESS)
        // Smarty Pants: +2 Toughness/cấp | Protection: +1 Toughness/cấp
        double targetToughness = (totalSmartyPants * 2.0) + (totalProtection * 1.0);
        AttributeInstance toughnessAttr = player.getAttribute(Attribute.GENERIC_ARMOR_TOUGHNESS);
        if (toughnessAttr != null) {
            NamespacedKey toughnessKeyMod = new NamespacedKey(plugin, "cwe_growth_toughness");
            toughnessAttr.removeModifier(toughnessKeyMod);
            if (targetToughness > 0) {
                toughnessAttr.addModifier(new org.bukkit.attribute.AttributeModifier(
                    toughnessKeyMod,
                    targetToughness,
                    org.bukkit.attribute.AttributeModifier.Operation.ADD_NUMBER
                ));
            }
        }

        // ⚡ THI THIÊN LỆNH API AURASKILLS TRỰC TIẾP CHỐNG SPAM CONSOLE
        if (auraEnabled) {
            SkillsUser user = AuraSkillsApi.get().getUser(player.getUniqueId());
            if (user != null) {
                // -- Xử lý Growth (Health) --
                int oldGrowth = cachedGrowth.getOrDefault(uuid, 0);
                if (totalGrowth != oldGrowth) {
                    if (oldGrowth > 0) user.removeStatModifier("cwe_growth");
                    if (totalGrowth > 0) user.addStatModifier(new StatModifier("cwe_growth", Stats.HEALTH, totalGrowth * 4.0, Operation.ADD));
                    cachedGrowth.put(uuid, totalGrowth);
                }

                // -- Xử lý Sugar Rush (Speed) --
                int oldSugar = cachedSugarRush.getOrDefault(uuid, 0);
                if (totalSugarRush != oldSugar) {
                    if (oldSugar > 0) user.removeStatModifier("cwe_sugar_rush");
                    if (totalSugarRush > 0) user.addStatModifier(new StatModifier("cwe_sugar_rush", Stats.SPEED, totalSugarRush * 5.0, Operation.ADD));
                    cachedSugarRush.put(uuid, totalSugarRush);
                }

                // -- Xử lý Wisdom (Big Brain) --
                int oldWis = cachedWisdom.getOrDefault(uuid, 0);
                if (totalBigBrain != oldWis) {
                    if (oldWis > 0) {
                        user.removeStatModifier("cwe_wisdom");
                    }
                    if (totalBigBrain > 0) {
                        user.addStatModifier(new StatModifier("cwe_wisdom", Stats.WISDOM, totalBigBrain * 15.0, Operation.ADD));
                    }
                    cachedWisdom.put(uuid, totalBigBrain);
                }

                // -- Xử lý Defense (Smarty Pants) --
                int oldDef = cachedDefense.getOrDefault(uuid, 0);
                if (totalSmartyPants != oldDef) {
                    if (oldDef > 0) {
                        user.removeStatModifier("cwe_defense");
                    }
                    if (totalSmartyPants > 0) {
                        user.addStatModifier(new StatModifier("cwe_defense", Stats.TOUGHNESS, totalSmartyPants * 8.0, Operation.ADD));
                    }
                    cachedDefense.put(uuid, totalSmartyPants);
                }

                // -- Xử lý Toughness (Smarty Pants + Protection) → AuraSkills --
                int combinedToughness = (totalSmartyPants * 2) + (totalProtection * 1);
                int oldTough = cachedToughness.getOrDefault(uuid, 0);
                if (combinedToughness != oldTough) {
                    if (oldTough > 0) {
                        user.removeStatModifier("cwe_toughness");
                    }
                    if (combinedToughness > 0) {
                        user.addStatModifier(new StatModifier("cwe_toughness", Stats.TOUGHNESS, combinedToughness, Operation.ADD));
                    }
                    cachedToughness.put(uuid, combinedToughness);
                    // Đồng bộ hiển thị profile AuraSkills qua console command
                    if (combinedToughness > 0) {
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
                            "sk armor modifier add " + player.getName() + " cwe_toughness toughness " + combinedToughness);
                    }
                }

                // -- Xử lý Health Regen (Rejuvenate) --
                int oldReg = cachedRegen.getOrDefault(uuid, 0);
                if (totalRejuvenate != oldReg) {
                    if (oldReg > 0) {
                        user.removeStatModifier("cwe_regen");
                    }
                    if (totalRejuvenate > 0) {
                        user.addStatModifier(new StatModifier("cwe_regen", Stats.REGENERATION, totalRejuvenate * 2.0, Operation.ADD));
                    }
                    cachedRegen.put(uuid, totalRejuvenate);
                }
            }
        }
    }
}

package org.example.enchant;

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
import org.bukkit.Location;
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
import org.bukkit.entity.Arrow;
import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
import org.bukkit.entity.Entity;
import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
import org.bukkit.entity.EntityType;
import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
import org.bukkit.entity.Item;
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
import org.bukkit.event.entity.EntityDeathEvent;
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
import org.bukkit.metadata.FixedMetadataValue;
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
import org.example.core.CustomWeaponEngine;
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
public class CombatEnchantListener implements Listener {
    private final JavaPlugin plugin;

    public CombatEnchantListener(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onCombatCalculate(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof LivingEntity)) return;
        LivingEntity target = (LivingEntity) event.getEntity();
        
        Player player = null;
        ItemStack weapon = null;
        boolean isBowAttack = false;

        if (event.getDamager() instanceof Player) {
            player = (Player) event.getDamager();
            weapon = player.getInventory().getItemInMainHand();
        } 
        else if (event.getDamager() instanceof Arrow) {
            Arrow arrow = (Arrow) event.getDamager();
            if (arrow.getShooter() instanceof Player) {
                player = (Player) arrow.getShooter();
                weapon = player.getInventory().getItemInMainHand();
                isBowAttack = true;
            }
        }

        if (player == null || weapon == null || !weapon.hasItemMeta()) return;
        PersistentDataContainer container = weapon.getItemMeta().getPersistentDataContainer();
        EnchantManager em = CustomWeaponEngine.getEnchantManager();

        double baseDamage = event.getDamage();
        double multiplier = 1.0;

        if (!isBowAttack) {
            NamespacedKey sharpKey = new NamespacedKey(plugin, "enchant_sharpness");
            if (container.has(sharpKey, PersistentDataType.INTEGER)) {
                int lvl = container.get(sharpKey, PersistentDataType.INTEGER);
                multiplier += (lvl * 0.05);
            }

            NamespacedKey fsKey = new NamespacedKey(plugin, "enchant_first_strike");
            if (container.has(fsKey, PersistentDataType.INTEGER)) {
                int lvl = container.get(fsKey, PersistentDataType.INTEGER);
                if (!target.hasMetadata("cst_hit_by_player") && lvl > 0) {
                    multiplier += (lvl * 0.25);
                    player.sendMessage("§e§lFIRST STRIKE! §7Bả táng đầu chí mạng vcl.");
                }
            }

            NamespacedKey critKey = new NamespacedKey(plugin, "enchant_critical");
            if (container.has(critKey, PersistentDataType.INTEGER)) {
                int lvl = container.get(critKey, PersistentDataType.INTEGER);
                if (player.getFallDistance() > 0.0F && !player.isOnGround() && lvl > 0) {
                    multiplier += (lvl * 0.10);
                }
            }

            NamespacedKey gkKey = new NamespacedKey(plugin, "enchant_giant_killer");
            if (container.has(gkKey, PersistentDataType.INTEGER)) {
                int lvl = container.get(gkKey, PersistentDataType.INTEGER);
                double pMaxHp = player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
                double tMaxHp = target.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
                if (tMaxHp > pMaxHp && lvl > 0) {
                    double pctHigher = (tMaxHp - pMaxHp) / pMaxHp;
                    double gkBonus = Math.min(pctHigher * lvl * 0.05, 0.25);
                    multiplier += gkBonus;
                }
            }

            NamespacedKey exeKey = new NamespacedKey(plugin, "enchant_execute");
            if (container.has(exeKey, PersistentDataType.INTEGER)) {
                int lvl = container.get(exeKey, PersistentDataType.INTEGER);
                double tMaxHp = target.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
                double tCurrentHp = target.getHealth();
                double missingHpPct = (tMaxHp - tCurrentHp) / tMaxHp;
                if (missingHpPct > 0 && lvl > 0) {
                    multiplier += (missingHpPct * lvl * 0.20);
                }
            }

            NamespacedKey lethalityKey = new NamespacedKey(plugin, "enchant_lethality");
            if (container.has(lethalityKey, PersistentDataType.INTEGER)) {
                int lvl = container.get(lethalityKey, PersistentDataType.INTEGER);
                if (lvl > 0) {
                    int currentStacks = 0;
                    long lastHitTime = 0;
                    if (target.hasMetadata("lethality_stacks")) {
                        currentStacks = target.getMetadata("lethality_stacks").get(0).asInt();
                    }
                    if (target.hasMetadata("lethality_timestamp")) {
                        lastHitTime = target.getMetadata("lethality_timestamp").get(0).asLong();
                    }
                    if (System.currentTimeMillis() - lastHitTime > 5000) {
                        currentStacks = 0;
                    }
                    multiplier += (currentStacks * 0.03);
                    int nextStacks = Math.min(currentStacks + 1, 4);
                    target.setMetadata("lethality_stacks", new FixedMetadataValue(plugin, nextStacks));
                    target.setMetadata("lethality_timestamp", new FixedMetadataValue(plugin, System.currentTimeMillis()));
                }
            }

            if (target.getType() == EntityType.ENDER_DRAGON) {
                NamespacedKey dhKey = new NamespacedKey(plugin, "enchant_dragon_hunter");
                if (container.has(dhKey, PersistentDataType.INTEGER)) {
                    int lvl = container.get(dhKey, PersistentDataType.INTEGER);
                    multiplier += (lvl * 0.08);
                }
            }

            NamespacedKey impalingKey = new NamespacedKey(plugin, "enchant_impaling_spear");
            if (container.has(impalingKey, PersistentDataType.INTEGER)) {
                int lvl = container.get(impalingKey, PersistentDataType.INTEGER);
                if (target.getAttribute(Attribute.GENERIC_ARMOR) != null && target.getAttribute(Attribute.GENERIC_ARMOR).getValue() <= 0 && lvl > 0) {
                    multiplier += (lvl * 0.15);
                }
            }
        }

        if (isBowAttack) {
            NamespacedKey powerKey = new NamespacedKey(plugin, "enchant_power");
            if (container.has(powerKey, PersistentDataType.INTEGER)) {
                int lvl = container.get(powerKey, PersistentDataType.INTEGER);
                multiplier += (lvl * 0.08);
            }

            NamespacedKey overloadKey = new NamespacedKey(plugin, "enchant_overload");
            if (container.has(overloadKey, PersistentDataType.INTEGER)) {
                int lvl = container.get(overloadKey, PersistentDataType.INTEGER);
                if (Math.random() < (lvl * 0.10)) {
                    multiplier *= 1.5;
                    player.sendMessage("§c§l💥 OVERLOAD! §7Phát bắn nổ tung lồng ngực!");
                    player.spawnParticle(org.bukkit.Particle.EXPLOSION_EMITTER, target.getLocation(), 1);
                }
            }

            NamespacedKey sniperKey = new NamespacedKey(plugin, "enchant_sniper");
            if (container.has(sniperKey, PersistentDataType.INTEGER)) {
                int lvl = container.get(sniperKey, PersistentDataType.INTEGER);
                double distance = player.getLocation().distance(target.getLocation());
                multiplier += (distance * 0.01 * lvl);
            }

            NamespacedKey flameKey = new NamespacedKey(plugin, "enchant_flame");
            if (container.has(flameKey, PersistentDataType.INTEGER)) {
                int lvl = container.get(flameKey, PersistentDataType.INTEGER);
                target.setFireTicks(lvl * 80);
            }
        }

        double finalDamage = baseDamage * multiplier;
        event.setDamage(finalDamage);

        if (!isBowAttack) {
            NamespacedKey lsKey = new NamespacedKey(plugin, "enchant_life_steal");
            if (container.has(lsKey, PersistentDataType.INTEGER)) {
                int lvl = container.get(lsKey, PersistentDataType.INTEGER);
                if (lvl > 0) {
                    if (em.consumeCharge(weapon, CustomEnchant.LIFE_STEAL, 1)) {
                        double healAmount = finalDamage * (lvl * 0.005);
                        double maxHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
                        player.setHealth(Math.min(player.getHealth() + healAmount, maxHealth));
                        player.sendActionBar("§c§l+ " + String.format("%.1f", healAmount) + " 🩸 HP (Life Steal) §b[" + em.getCharges(weapon, CustomEnchant.LIFE_STEAL) + "⚡]");
                        em.rebuildEnchantLore(weapon);
                    } else {
                        player.sendActionBar("§c§l⚡ Bùa Life Steal đã hết pin! Hãy sạc bằng Đá Đỏ trên Đe.");
                    }
                }
            }
        }

        if (!target.hasMetadata("cst_hit_by_player")) {
            target.setMetadata("cst_hit_by_player", new FixedMetadataValue(plugin, true));
        }
    }

    @EventHandler
    public void onEntityKilled(EntityDeathEvent event) {
        LivingEntity victim = event.getEntity();
        Player killer = victim.getKiller();

        if (killer == null) return;
        ItemStack weapon = killer.getInventory().getItemInMainHand();
        if (weapon == null || !weapon.hasItemMeta()) return;

        PersistentDataContainer container = weapon.getItemMeta().getPersistentDataContainer();
        EnchantManager em = CustomWeaponEngine.getEnchantManager();

        NamespacedKey teleKey = new NamespacedKey(plugin, "enchant_telekinesis");
        if (container.has(teleKey, PersistentDataType.INTEGER)) {
            if (em.getCharges(weapon, CustomEnchant.TELEKINESIS) > 0) {
                Location loc = victim.getLocation();
                
                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    boolean addedAny = false;
                    for (Entity entity : loc.getWorld().getNearbyEntities(loc, 2.5, 2.5, 2.5)) {
                        if (entity instanceof Item) {
                            Item itemEntity = (Item) entity;
                            if (itemEntity.getTicksLived() <= 2) {
                                ItemStack stack = itemEntity.getItemStack();
                                Map<Integer, ItemStack> leftover = killer.getInventory().addItem(stack);
                                
                                if (leftover.isEmpty()) {
                                    itemEntity.remove();
                                    addedAny = true;
                                } else {
                                    int remaining = leftover.get(0).getAmount();
                                    if (remaining != stack.getAmount()) {
                                        itemEntity.setItemStack(leftover.get(0));
                                        addedAny = true;
                                    }
                                }
                            }
                        }
                    }
                    if (addedAny) {
                        killer.updateInventory();
                        em.consumeCharge(weapon, CustomEnchant.TELEKINESIS, 1);
                        em.rebuildEnchantLore(weapon);
                    }
                }, 1L);
            } else {
                killer.sendActionBar("§c§l⚡ Bùa Telekinesis đã hết pin! Hãy sạc bằng Ngọc Ender trên Đe.");
            }
        }

        NamespacedKey vampKey = new NamespacedKey(plugin, "enchant_vampirism");
        if (container.has(vampKey, PersistentDataType.INTEGER)) {
            int lvl = container.get(vampKey, PersistentDataType.INTEGER);
            if (lvl > 0) {
                if (em.consumeCharge(weapon, CustomEnchant.VAMPIRISM, 1)) {
                    double maxHealth = killer.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
                    double currentHealth = killer.getHealth();
                    double missingHealth = maxHealth - currentHealth;

                    if (missingHealth > 0) {
                        double healAmount = missingHealth * (lvl * 0.01);
                        killer.setHealth(Math.min(currentHealth + healAmount, maxHealth));
                        killer.sendMessage("§d§l+ " + String.format("%.1f", healAmount) + "❤ HP từ bùa Vampirism! §b[" + em.getCharges(weapon, CustomEnchant.VAMPIRISM) + "⚡]");
                    }
                    em.rebuildEnchantLore(weapon);
                } else {
                    killer.sendMessage("§c§l⚡ Bùa Vampirism đã hết pin! Hãy sạc bằng Đá Đỏ trên Đe.");
                }
            }
        }
    }
}


/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.aurelium.auraskills.api.AuraSkillsApi
 *  dev.aurelium.auraskills.api.stat.Stat
 *  dev.aurelium.auraskills.api.stat.Stats
 *  dev.aurelium.auraskills.api.user.SkillsUser
 *  org.bukkit.Bukkit
 *  org.bukkit.Material
 *  org.bukkit.NamespacedKey
 *  org.bukkit.entity.Player
 *  org.bukkit.entity.Projectile
 *  org.bukkit.entity.Trident
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.event.entity.ProjectileLaunchEvent
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.persistence.PersistentDataContainer
 *  org.bukkit.persistence.PersistentDataType
 *  org.bukkit.plugin.Plugin
 */
package org.example.weapon;

import dev.aurelium.auraskills.api.AuraSkillsApi;
import dev.aurelium.auraskills.api.stat.Stat;
import dev.aurelium.auraskills.api.stat.Stats;
import dev.aurelium.auraskills.api.user.SkillsUser;
import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Trident;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.example.core.CustomWeaponEngine;

public class ArrowDamageListener
implements Listener {
    private final CustomWeaponEngine plugin;

    public ArrowDamageListener(CustomWeaponEngine plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        SkillsUser user;
        if (!(event.getEntity() instanceof Projectile)) {
            return;
        }
        Projectile proj = event.getEntity();
        if (!(proj.getShooter() instanceof Player)) {
            return;
        }
        Player player = (Player)proj.getShooter();
        ItemStack weapon = null;
        if (proj instanceof Trident) {
            weapon = ((Trident)proj).getItemStack();
        } else {
            ItemStack mainHand = player.getInventory().getItemInMainHand();
            ItemStack offHand = player.getInventory().getItemInOffHand();
            weapon = mainHand != null && (mainHand.getType() == Material.BOW || mainHand.getType() == Material.CROSSBOW) ? mainHand : (offHand != null && (offHand.getType() == Material.BOW || offHand.getType() == Material.CROSSBOW) ? offHand : mainHand);
        }
        if (weapon == null || !weapon.hasItemMeta()) {
            return;
        }
        PersistentDataContainer pdc = weapon.getItemMeta().getPersistentDataContainer();
        double dmg = pdc.getOrDefault(new NamespacedKey(this.plugin, "stat_damage"), PersistentDataType.DOUBLE, 0.0);
        if (dmg == 0.0) {
            dmg = pdc.getOrDefault(new NamespacedKey(this.plugin, "cwe_damage"), PersistentDataType.DOUBLE, 0.0);
        }
        double strength = 0.0;
        double critChance = 0.0;
        double critDamage = 0.0;
        boolean auraEnabled = Bukkit.getPluginManager().isPluginEnabled("AuraSkills");
        if (auraEnabled && (user = AuraSkillsApi.get().getUser(player.getUniqueId())) != null) {
            strength = user.getStatLevel((Stat)Stats.STRENGTH);
            critChance = user.getStatLevel((Stat)Stats.CRIT_CHANCE);
            critDamage = user.getStatLevel((Stat)Stats.CRIT_DAMAGE);
        } else {
            strength = pdc.getOrDefault(new NamespacedKey(this.plugin, "stat_strength"), PersistentDataType.DOUBLE, 0.0);
            critChance = pdc.getOrDefault(new NamespacedKey(this.plugin, "stat_crit_chance"), PersistentDataType.DOUBLE, 0.0);
            critDamage = pdc.getOrDefault(new NamespacedKey(this.plugin, "stat_crit_damage"), PersistentDataType.DOUBLE, 0.0);
        }
        proj.setMetadata("cwe_proj_damage", new FixedMetadataValue(this.plugin, dmg));
        proj.setMetadata("cwe_proj_strength", new FixedMetadataValue(this.plugin, strength));
        proj.setMetadata("cwe_proj_crit_chance", new FixedMetadataValue(this.plugin, critChance));
        proj.setMetadata("cwe_proj_crit_damage", new FixedMetadataValue(this.plugin, critDamage));
    }

    @EventHandler(priority=EventPriority.LOW, ignoreCancelled=true)
    public void onProjectileHit(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Projectile)) {
            return;
        }
        Projectile proj = (Projectile)event.getDamager();
        if (!proj.hasMetadata("cwe_proj_damage")) {
            return;
        }
        double customDmg = ((MetadataValue)proj.getMetadata("cwe_proj_damage").get(0)).asDouble();
        double strength = proj.hasMetadata("cwe_proj_strength") ? ((MetadataValue)proj.getMetadata("cwe_proj_strength").get(0)).asDouble() : 0.0;
        double critChance = proj.hasMetadata("cwe_proj_crit_chance") ? ((MetadataValue)proj.getMetadata("cwe_proj_crit_chance").get(0)).asDouble() : 0.0;
        double critDamage = proj.hasMetadata("cwe_proj_crit_damage") ? ((MetadataValue)proj.getMetadata("cwe_proj_crit_damage").get(0)).asDouble() : 0.0;
        double baseDamage = event.getDamage();
        double totalBaseDamage = baseDamage + customDmg;
        double strengthMultiplier = 1.0 + strength * 0.01;
        double damageAfterStrength = totalBaseDamage * strengthMultiplier;
        boolean isCrit = false;
        if (event.getEntity().hasMetadata("cwe_is_crit")) {
            isCrit = true;
        } else {
            double roll = new Random().nextDouble() * 100.0;
            if (roll < Math.min(critChance, 100.0)) {
                isCrit = true;
                event.getEntity().setMetadata("cwe_is_crit", new FixedMetadataValue(this.plugin, true));
                Bukkit.getScheduler().runTaskLater((Plugin)this.plugin, () -> event.getEntity().removeMetadata("cwe_is_crit", (Plugin)this.plugin), 2L);
            }
        }
        double finalDamage = damageAfterStrength;
        if (isCrit) {
            double critMultiplier = 1.5 + critDamage * 0.01;
            finalDamage = damageAfterStrength * critMultiplier;
        }
        event.setDamage(finalDamage);
    }
}


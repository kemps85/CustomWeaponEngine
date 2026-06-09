package org.example.enchant;

import org.example.core.CustomWeaponEngine;
import org.example.enchant.UltimateEnchant;
import org.example.enchant.EnchantManager;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import dev.aurelium.auraskills.api.AuraSkillsApi;
import dev.aurelium.auraskills.api.stat.Stats;
import dev.aurelium.auraskills.api.stat.StatModifier;
import dev.aurelium.auraskills.api.user.SkillsUser;
import dev.aurelium.auraskills.api.util.AuraSkillsModifier.Operation;

import java.util.*;

public class UltimateSystemListener implements Listener {
    private final CustomWeaponEngine plugin;
    private final EnchantManager enchantManager;

    // Tracker cho các ultimate enchant
    private final Map<UUID, Integer> soulStacks = new HashMap<>();
    private final Map<UUID, Integer> tempoStacks = new HashMap<>();
    private final Map<UUID, Long> tempoTimestamps = new HashMap<>();
    private final Map<UUID, Map<UUID, List<UUID>>> rendArrows = new HashMap<>();
    private final Map<UUID, Long> bobbinActive = new HashMap<>();
    private final Map<UUID, Long> npngCooldown = new HashMap<>();

    public UltimateSystemListener(CustomWeaponEngine plugin, EnchantManager enchantManager) {
        this.plugin = plugin;
        this.enchantManager = enchantManager;
        startPeriodicBuffs();
    }

    private void startPeriodicBuffs() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    try {
                        SkillsUser user = AuraSkillsApi.get().getUser(p.getUniqueId());
                        if (user == null) continue;

                        // Legion
                        int legionLvl = getTotalUltimateLevel(p, UltimateEnchant.LEGION);
                        user.removeStatModifier("cwe_legion_str");
                        user.removeStatModifier("cwe_legion_crit");
                        user.removeStatModifier("cwe_legion_cd");
                        user.removeStatModifier("cwe_legion_def");
                        user.removeStatModifier("cwe_legion_hp");
                        user.removeStatModifier("cwe_legion_int");
                        user.removeStatModifier("cwe_legion_spd");

                        if (legionLvl > 0) {
                            long nearbyPlayers = p.getNearbyEntities(30, 30, 30).stream()
                                    .filter(e -> e instanceof Player).count();
                            double bonusMult = nearbyPlayers * legionLvl * 0.01;
                            if (bonusMult > 0) {
                                user.addStatModifier(new StatModifier("cwe_legion_str", Stats.STRENGTH, bonusMult * user.getStatLevel(Stats.STRENGTH), Operation.ADD));
                                user.addStatModifier(new StatModifier("cwe_legion_crit", Stats.CRIT_CHANCE, bonusMult * user.getStatLevel(Stats.CRIT_CHANCE), Operation.ADD));
                                user.addStatModifier(new StatModifier("cwe_legion_cd", Stats.CRIT_DAMAGE, bonusMult * user.getStatLevel(Stats.CRIT_DAMAGE), Operation.ADD));
                                user.addStatModifier(new StatModifier("cwe_legion_def", Stats.TOUGHNESS, bonusMult * user.getStatLevel(Stats.TOUGHNESS), Operation.ADD));
                                user.addStatModifier(new StatModifier("cwe_legion_hp", Stats.HEALTH, bonusMult * user.getStatLevel(Stats.HEALTH), Operation.ADD));
                                user.addStatModifier(new StatModifier("cwe_legion_int", Stats.WISDOM, bonusMult * user.getStatLevel(Stats.WISDOM), Operation.ADD));
                                user.addStatModifier(new StatModifier("cwe_legion_spd", Stats.LUCK, bonusMult * user.getStatLevel(Stats.LUCK), Operation.ADD)); // LUCK -> SPEED mapping might vary, assuming stats
                            }
                        }

                        // Wisdom
                        int wisdomLvl = getTotalUltimateLevel(p, UltimateEnchant.WISDOM);
                        user.removeStatModifier("cwe_wisdom_ult");
                        if (wisdomLvl > 0) {
                            long nearbyMobs = p.getNearbyEntities(10, 10, 10).stream()
                                    .filter(e -> e instanceof LivingEntity && !(e instanceof Player)).count();
                            double intBonus = nearbyMobs * wisdomLvl;
                            if (intBonus > 0) {
                                user.addStatModifier(new StatModifier("cwe_wisdom_ult", Stats.WISDOM, intBonus, Operation.ADD));
                            }
                        }
                    } catch (Exception ignored) {}
                }
            }
        }.runTaskTimer(plugin, 40L, 40L);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onWeaponDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) return;
        Player player = (Player) event.getDamager();
        ItemStack weapon = player.getInventory().getItemInMainHand();

        if (weapon == null || !weapon.hasItemMeta()) return;

        double damage = event.getDamage();

        // ONE FOR ALL
        if (enchantManager.hasUltimateEnchant(weapon, UltimateEnchant.ONE_FOR_ALL)) {
            damage *= 3.1;
        }

        // SWARM
        int swarmLvl = enchantManager.getUltimateEnchantLevel(weapon, UltimateEnchant.SWARM);
        if (swarmLvl > 0) {
            int entities = player.getNearbyEntities(10, 10, 10).size();
            damage *= (1 + (entities * 0.02 * swarmLvl));
        }

        // COMBO
        int comboLvl = enchantManager.getUltimateEnchantLevel(weapon, UltimateEnchant.COMBO);
        if (comboLvl > 0) {
            damage *= (1 + (0.10 * comboLvl));
        }

        // INFERNO
        int infernoLvl = enchantManager.getUltimateEnchantLevel(weapon, UltimateEnchant.INFERNO);
        if (infernoLvl > 0 && Math.random() < (0.1 * infernoLvl)) {
            event.getEntity().setFireTicks(100);
            damage += 50 * infernoLvl;
        }

        // SOUL EATER
        int souls = soulStacks.getOrDefault(player.getUniqueId(), 0);
        if (souls > 0) {
            damage += souls;
            soulStacks.put(player.getUniqueId(), 0);
        }

        // FATAL TEMPO
        int tempoLvl = enchantManager.getUltimateEnchantLevel(weapon, UltimateEnchant.FATAL_TEMPO);
        if (tempoLvl > 0) {
            long now = System.currentTimeMillis();
            UUID uid = player.getUniqueId();
            int stacks = tempoTimestamps.containsKey(uid) && (now - tempoTimestamps.get(uid)) < 3000
                    ? Math.min(tempoStacks.getOrDefault(uid, 0) + 1, 5) : 1;
            tempoStacks.put(uid, stacks);
            tempoTimestamps.put(uid, now);
            
            if (event.getEntity() instanceof LivingEntity) {
                int ticks = Math.max(1, 10 - (stacks * 2 * tempoLvl));
                ((LivingEntity) event.getEntity()).setMaximumNoDamageTicks(ticks);
            }
        }

        event.setDamage(damage);
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity().getKiller() != null) {
            Player player = event.getEntity().getKiller();
            ItemStack weapon = player.getInventory().getItemInMainHand();
            if (weapon != null && weapon.hasItemMeta()) {
                int soulLvl = enchantManager.getUltimateEnchantLevel(weapon, UltimateEnchant.SOUL_EATER);
                if (soulLvl > 0) {
                    int current = soulStacks.getOrDefault(player.getUniqueId(), 0);
                    int max = 100 * soulLvl;
                    soulStacks.put(player.getUniqueId(), Math.min(current + 1, max));
                }
            }
        }
    }

    @EventHandler
    public void onBowShoot(EntityShootBowEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();
        ItemStack bow = event.getBow();
        if (bow == null) return;

        // DUPLEX
        int duplexLvl = enchantManager.getUltimateEnchantLevel(bow, UltimateEnchant.DUPLEX);
        if (duplexLvl > 0 && event.getProjectile() instanceof Arrow) {
            Arrow main = (Arrow) event.getProjectile();
            Vector dir = main.getVelocity().clone();
            dir.rotateAroundY(Math.toRadians(5));
            Arrow side = player.getWorld().spawnArrow(main.getLocation(), dir, (float)main.getVelocity().length() * 0.9f, 2f);
            side.setShooter(player);
            side.setMetadata("duplex_side", new FixedMetadataValue(plugin, true));
            side.setMetadata("cwe_bow_damage_mult", new FixedMetadataValue(plugin, 0.6));
        }

        // REND - track arrow
        int rendLvl = enchantManager.getUltimateEnchantLevel(bow, UltimateEnchant.REND);
        if (rendLvl > 0 && event.getProjectile() instanceof Arrow) {
            event.getProjectile().setMetadata("rend_bow_owner", new FixedMetadataValue(plugin, player.getUniqueId()));
        }
    }

    @EventHandler
    public void onArrowHit(ProjectileHitEvent event) {
        if (event.getEntity() instanceof Arrow && event.getHitEntity() instanceof LivingEntity) {
            Arrow arrow = (Arrow) event.getEntity();
            LivingEntity hitEntity = (LivingEntity) event.getHitEntity();
            if (arrow.hasMetadata("rend_bow_owner")) {
                UUID playerUid = (UUID) arrow.getMetadata("rend_bow_owner").get(0).value();
                UUID mobUid = hitEntity.getUniqueId();
                rendArrows.computeIfAbsent(playerUid, k -> new HashMap<>())
                          .computeIfAbsent(mobUid, k -> new ArrayList<>())
                          .add(arrow.getUniqueId());
            }
        }
    }

    @EventHandler
    public void onBowLeftClick(PlayerInteractEvent event) {
        if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
            Player player = event.getPlayer();
            ItemStack item = player.getInventory().getItemInMainHand();
            if (item != null && item.hasItemMeta()) {
                int rendLvl = enchantManager.getUltimateEnchantLevel(item, UltimateEnchant.REND);
                if (rendLvl > 0) {
                    Map<UUID, List<UUID>> myArrows = rendArrows.remove(player.getUniqueId());
                    if (myArrows == null || myArrows.isEmpty()) return;

                    double baseDmg = getPlayerWeaponDamage(item);
                    for (Map.Entry<UUID, List<UUID>> entry : myArrows.entrySet()) {
                        LivingEntity mob = (LivingEntity) Bukkit.getEntity(entry.getKey());
                        if (mob == null || mob.isDead()) continue;
                        
                        List<UUID> arrowUids = entry.getValue();
                        int arrowCount = arrowUids.size();
                        double totalDmg = baseDmg * (1 + 0.1 * rendLvl) * arrowCount;
                        mob.damage(totalDmg, player);

                        mob.getWorld().getEntities().stream()
                            .filter(e -> arrowUids.contains(e.getUniqueId()))
                            .forEach(Entity::remove);

                        mob.getWorld().playSound(mob.getLocation(), Sound.ENTITY_ARROW_SHOOT, 1f, 0.5f);
                        mob.getWorld().spawnParticle(Particle.CRIT, mob.getLocation().add(0, 1, 0), 20, 0.3, 0.3, 0.3, 0.1);
                    }
                }
            }
        }
    }

    private double getPlayerWeaponDamage(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return 0;
        NamespacedKey dmgKey = new NamespacedKey(plugin, "stat_damage");
        if (item.getItemMeta().getPersistentDataContainer().has(dmgKey, PersistentDataType.DOUBLE)) {
            return item.getItemMeta().getPersistentDataContainer().get(dmgKey, PersistentDataType.DOUBLE);
        }
        return 0;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onArmorHit(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();
        UUID uid = player.getUniqueId();
        
        int lastStandLvl = getTotalUltimateLevel(player, UltimateEnchant.LAST_STAND);
        if (lastStandLvl > 0) {
            if (player.getHealth() <= player.getMaxHealth() / 2) {
                double reduction = Math.min(0.9, lastStandLvl * 0.05);
                event.setDamage(event.getDamage() * (1 - reduction));
            }
        }

        int habaneroLvl = getTotalUltimateLevel(player, UltimateEnchant.HABANERO_TACTICS);
        if (habaneroLvl > 0) {
            if (event.getDamager() instanceof LivingEntity) {
                ((LivingEntity) event.getDamager()).damage(event.getDamage() * (0.1 * habaneroLvl), player);
            }
        }

        int npngLvl = getTotalUltimateLevel(player, UltimateEnchant.NO_PAIN_NO_GAIN);
        if (npngLvl > 0 && event.getFinalDamage() >= 5) {
            long now = System.currentTimeMillis();
            if (!npngCooldown.containsKey(uid) || now - npngCooldown.get(uid) > 2000) {
                double heal = 10 * npngLvl;
                player.setHealth(Math.min(player.getMaxHealth(), player.getHealth() + heal));
                npngCooldown.put(uid, now);
            }
        }

        int bobbinLvl = getTotalUltimateLevel(player, UltimateEnchant.BOBBIN_TIME);
        if (bobbinLvl > 0 && player.getHealth() <= player.getMaxHealth() * 0.30) {
            long now = System.currentTimeMillis();
            if (!bobbinActive.containsKey(uid) || now - bobbinActive.get(uid) > 6000) {
                bobbinActive.put(uid, now);
                player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 0, 0));
                player.removePotionEffect(PotionEffectType.POISON);
                player.setMetadata("cwe_bobbin_cd_reduce", new FixedMetadataValue(plugin, 0.15));
                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    if (player.isOnline()) player.removeMetadata("cwe_bobbin_cd_reduce", plugin);
                }, 120L);
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        ItemStack tool = player.getInventory().getItemInMainHand();
        if (tool != null && tool.hasItemMeta()) {
            int flashLvl = enchantManager.getUltimateEnchantLevel(tool, UltimateEnchant.FLASH);
            if (flashLvl > 0 && Math.random() < flashLvl * 0.10) {
                Collection<ItemStack> drops = event.getBlock().getDrops(tool);
                for (ItemStack drop : drops) {
                    player.getInventory().addItem(drop.clone());
                }
                player.sendMessage("§b⚡ Flash đã kích hoạt, bạn nhận thêm vật phẩm!");
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        try {
            net.milkbowl.vault.economy.Economy econ = CustomWeaponEngine.getEconomy();
            if (econ != null) {
                double balance = econ.getBalance(player);
                double lost = balance * 0.5;
                
                int bankLvl = getTotalUltimateLevel(player, UltimateEnchant.BANK);
                if (bankLvl > 0) {
                    double saveRatio = Math.min(1.0, bankLvl * 0.1);
                    lost = lost * (1 - saveRatio);
                }
                
                if (lost > 0) {
                    econ.withdrawPlayer(player, lost);
                    player.sendMessage("§cBạn đã chết và mất §e" + String.format("%.1f", lost) + " coins§c!");
                }
            }
        } catch (Exception ignored) {}
    }

    private int getTotalUltimateLevel(Player player, UltimateEnchant enchant) {
        int total = 0;
        for (ItemStack armor : player.getInventory().getArmorContents()) {
            if (armor != null && enchantManager.hasUltimateEnchant(armor, enchant)) {
                total += enchantManager.getUltimateEnchantLevel(armor, enchant);
            }
        }
        return total;
    }
}

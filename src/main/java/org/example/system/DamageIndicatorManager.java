package org.example.system;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.DecimalFormat;
import java.util.Random;

public class DamageIndicatorManager implements Listener {
    private static JavaPlugin pluginInstance;
    private static final Random random = new Random();
    private static final DecimalFormat df = new DecimalFormat("#,###");
    private static final String[] critColors = {"§c", "§6", "§e", "§a", "§b", "§9", "§d"};

    public DamageIndicatorManager(JavaPlugin plugin) {
        pluginInstance = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof LivingEntity)) return;
        if (event.getEntity() instanceof ArmorStand) return;

        double damage = event.getFinalDamage();
        if (damage < 0.5) return; // Bỏ qua đòn đánh ảo 0.01

        LivingEntity victim = (LivingEntity) event.getEntity();
        boolean isCrit = victim.hasMetadata("cwe_is_crit");
        
        spawnHologram(victim.getLocation().add(0, victim.getHeight() / 2, 0), damage, isCrit);
    }

    public static void spawnHologram(Location loc, double damage, boolean isCrit) {
        if (pluginInstance == null) return;
        if (damage < 0.1) return;
        
        String text = "§7" + df.format(damage);
        if (isCrit) {
            String dmgStr = df.format(damage);
            StringBuilder sb = new StringBuilder("§f✧");
            int colorIdx = random.nextInt(critColors.length);
            for (char c : dmgStr.toCharArray()) {
                if (c == ',' || c == '.') {
                    sb.append("§f").append(c);
                } else {
                    sb.append(critColors[colorIdx % critColors.length]).append(c);
                    colorIdx++;
                }
            }
            sb.append("§f✧");
            text = sb.toString();
        }

        Location spawnLoc = loc.clone().add(
                (random.nextDouble() - 0.5) * 1.5,
                (random.nextDouble() - 0.5) * 0.5 + 0.5,
                (random.nextDouble() - 0.5) * 1.5
        );

        final String finalText = text;
        ArmorStand as = spawnLoc.getWorld().spawn(spawnLoc, ArmorStand.class, armorStand -> {
            armorStand.setVisible(false);
            armorStand.setMarker(true);
            armorStand.setGravity(false);
            armorStand.setBasePlate(false);
            armorStand.setCustomName(finalText);
            armorStand.setCustomNameVisible(true);
            armorStand.setInvulnerable(true);
            armorStand.setCollidable(false);
        });

        new BukkitRunnable() {
            int ticks = 0;
            @Override
            public void run() {
                if (ticks >= 20 || as.isDead()) {
                    as.remove();
                    this.cancel();
                    return;
                }
                as.teleport(as.getLocation().add(0, 0.03, 0));
                ticks++;
            }
        }.runTaskTimer(pluginInstance, 1L, 1L);
    }
}

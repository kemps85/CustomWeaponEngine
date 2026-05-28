package org.example.weapon;

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
import org.bukkit.ChatColor;
import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
import org.bukkit.Particle;
import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
import org.bukkit.Sound;
import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
import org.bukkit.configuration.file.FileConfiguration;
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
import org.bukkit.entity.Monster;
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
import org.bukkit.event.Listener;
import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
import org.bukkit.event.player.PlayerToggleSneakEvent;
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
import org.bukkit.potion.PotionEffectType;
import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
import org.bukkit.scheduler.BukkitRunnable;
import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
import org.bukkit.scheduler.BukkitTask;

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
import java.util.UUID;

import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
public class ShadowAssassinListener implements Listener {

    private final HashMap<UUID, BukkitTask> sneakTasks = new HashMap<>();
    private final HashMap<UUID, Long> stealthCooldowns = new HashMap<>();

    public void clearCooldowns() {
        for (BukkitTask task : sneakTasks.values()) {
            if (task != null) task.cancel();
        }
        this.sneakTasks.clear();
        this.stealthCooldowns.clear();
    }

    private int countAssassinPieces(Player player) {
        int count = 0;
        try {
            FileConfiguration config = CustomWeaponEngine.getInstance().getConfig();
            String configName = config.getString("sets.shadow_assassin.display-name", "Shadow");
            String cleanConfigName = ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', configName)).toLowerCase();

            ItemStack[] armor = player.getInventory().getArmorContents();
            for (ItemStack item : armor) {
                if (item != null && item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
                    String cleanPlayerItemName = ChatColor.stripColor(item.getItemMeta().getDisplayName()).toLowerCase();
                    if (cleanPlayerItemName.contains(cleanConfigName) || cleanPlayerItemName.contains("shadow")) {
                        count++;
                    }
                }
            }
        } catch (Exception e) {
            return 0;
        }
        return count;
    }

    @EventHandler
    public void onMobTarget(EntityTargetLivingEntityEvent event) {
        if (!(event.getTarget() instanceof Player)) return;
        Player player = (Player) event.getTarget();

        if (player.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
            if (countAssassinPieces(player) == 4) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        
        if (!event.isSneaking()) {
            if (sneakTasks.containsKey(uuid)) {
                sneakTasks.get(uuid).cancel();
                sneakTasks.remove(uuid);
                player.sendTitle("", "§c❌ Đã đứng dậy - Hủy ẩn thân", 0, 20, 5);
            }
            return;
        }

        int pieces = countAssassinPieces(player);

        // 🟩 XÓA BỎ HOÀN TOÀN: Dòng thông báo quét giáp spam chat nãy giờ.

        if (pieces < 4) {
            // 🟩 CHỈNH SỬA: Không hiện "Thiếu đồ rồi cu" nữa, thiếu là im lặng return luôn!
            return;
        }

        long currentTime = System.currentTimeMillis();
        if (stealthCooldowns.containsKey(uuid) && (currentTime - stealthCooldowns.get(uuid) < 15000L)) {
            player.sendMessage("§c[CST-MỚI] Chiêu tàng hình đang hồi!");
            return;
        }

        FileConfiguration config = CustomWeaponEngine.getInstance().getConfig();
        int requiredSeconds = config.getInt("sets.shadow_assassin.crouch-stealth-time", 3);
        int stealthDuration = config.getInt("sets.shadow_assassin.stealth-duration", 5);

        BukkitTask task = new BukkitRunnable() {
            int secondsCrouched = 0;

            @Override
            public void run() {
                secondsCrouched++;
                player.sendTitle("", "§8[Sát Thủ] Đang gồng ẩn thân: §b" + secondsCrouched + "s/" + requiredSeconds + "s", 0, 22, 0);

                if (secondsCrouched >= requiredSeconds) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "minecraft:effect give " + player.getName() + " minecraft:invisibility " + stealthDuration + " 0 true");
                    
                    player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
                    player.getWorld().spawnParticle(Particle.PORTAL, player.getLocation().add(0, 1, 0), 35, 0.4, 0.5, 0.4, 0.1);
                    
                    player.sendMessage("§b§l[CST-MỚI] TÀNG HÌNH THÀNH CÔNG! Thời gian hiệu lực: " + stealthDuration + " giây.");
                    player.sendTitle("§b§lTÀNG HÌNH", "§7Ẩn thân chi thuật!", 5, 20, 5);

                    for (Entity entity : player.getNearbyEntities(20, 20, 20)) {
                        if (entity instanceof Monster) {
                            Monster monster = (Monster) entity;
                            if (monster.getTarget() != null && monster.getTarget().getUniqueId().equals(uuid)) {
                                monster.setTarget(null);
                            }
                        }
                    }

                    stealthCooldowns.put(uuid, System.currentTimeMillis());
                    sneakTasks.remove(uuid);
                    cancel();
                }
            }
        }.runTaskTimer(CustomWeaponEngine.getInstance(), 20L, 20L);

        sneakTasks.put(uuid, task);
    }
}

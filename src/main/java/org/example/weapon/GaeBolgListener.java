package org.example.weapon;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.example.core.CustomWeaponEngine;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GaeBolgListener implements Listener {
    private final CustomWeaponEngine plugin;
    private final Map<UUID, Long> cooldowns = new HashMap<>();

    public GaeBolgListener(CustomWeaponEngine plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item == null || !item.hasItemMeta()) return;

        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        NamespacedKey cweIdKey = new NamespacedKey(plugin, "cwe_id");

        if (!pdc.has(cweIdKey, PersistentDataType.STRING)) return;
        String id = pdc.get(cweIdKey, PersistentDataType.STRING);
        if (!"gae_bolg".equals(id)) return;

        // Bỏ event.setCancelled(true) để cho phép Spear bắt đầu charge
        
        // Cooldown check (8 seconds)
        long now = System.currentTimeMillis();
        long lastUse = cooldowns.getOrDefault(player.getUniqueId(), 0L);
        if (now - lastUse < 8000L) {
            double remaining = (8000L - (now - lastUse)) / 1000.0;
            player.sendMessage("§cĐợi " + String.format("%.1f", remaining) + "s để dùng Soaring Spear that Strikes with Death!");
            return;
        }

        // Tìm mục tiêu đang nhìn (Khoảng cách tối đa 40 blocks)
        Entity rawTarget = player.getTargetEntity(40, false);
        if (!(rawTarget instanceof LivingEntity)) {
            player.sendMessage("§cKhông tìm thấy mục tiêu nào trong tầm nhìn để khóa!");
            return;
        }
        LivingEntity target = (LivingEntity) rawTarget;

        cooldowns.put(player.getUniqueId(), now);

        // Phát hiệu ứng khóa mục tiêu
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1.0f, 1.5f);
        player.sendMessage("§4[Gáe Bolg] §cĐã khóa mục tiêu! Giữ vũ khí trong 2 giây để phóng tới!");
        
        // Đóng băng mục tiêu ngay lập tức (Áp chế)
        target.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 60, 255)); // Slowness 255 cho 3s
        target.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 60, 0));
        target.getWorld().spawnParticle(Particle.DUST, target.getLocation().add(0, 1, 0), 100, 1.0, 1.0, 1.0, 0, new Particle.DustOptions(org.bukkit.Color.MAROON, 2.0f));
        target.getWorld().playSound(target.getLocation(), Sound.ENTITY_ILLUSIONER_CAST_SPELL, 1.0f, 0.5f);

        // Bắt đầu đếm ngược 2 giây (40 ticks)
        new BukkitRunnable() {
            int ticks = 0;
            @Override
            public void run() {
                if (!player.isOnline() || player.isDead() || target.isDead()) {
                    this.cancel();
                    return;
                }

                // Kiểm tra xem người chơi có đổi sang đồ khác, HOẶC nhả chuột phải (Ngưng gồng) hay không
                ItemStack currentItem = player.getInventory().getItemInMainHand();
                if (!player.isHandRaised() || currentItem == null || !currentItem.hasItemMeta() || !currentItem.getItemMeta().getPersistentDataContainer().has(cweIdKey, PersistentDataType.STRING)) {
                    player.sendMessage("§cĐã hủy khóa mục tiêu Gáe Bolg do bạn ngừng gồng!");
                    this.cancel();
                    return;
                }

                ticks++;

                // Hiệu ứng tia sét/laser đỏ chỉ đường từ player tới target
                Vector dirToTarget = target.getLocation().toVector().subtract(player.getLocation().toVector());
                Location particleLoc = player.getLocation().add(0, 1, 0).add(dirToTarget.clone().normalize().multiply(ticks * (dirToTarget.length() / 40.0)));
                player.getWorld().spawnParticle(Particle.DUST, particleLoc, 5, 0.1, 0.1, 0.1, 0, new Particle.DustOptions(org.bukkit.Color.RED, 1.5f));

                if (ticks >= 40) { // Đã đủ 2 giây
                    this.cancel();
                    
                    // Cấp Speed cực lớn thay vì lướt
                    player.getWorld().playSound(player.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1.0f, 2.0f);
                    player.getWorld().playSound(player.getLocation(), Sound.ENTITY_HORSE_GALLOP, 1.0f, 1.5f);
                    
                    // Cấp Speed bù dựa trên chỉ số AuraSkills hiện tại (Max trần 400)
                    int buffAmp = 19; // Mặc định là Speed 20 (Amplifier 19)
                    if (org.bukkit.Bukkit.getPluginManager().isPluginEnabled("AuraSkills")) {
                        dev.aurelium.auraskills.api.user.SkillsUser u = dev.aurelium.auraskills.api.AuraSkillsApi.get().getUser(player.getUniqueId());
                        if (u != null) {
                            double currentSpeed = u.getStatLevel(dev.aurelium.auraskills.api.stat.Stats.SPEED);
                            double missingSpeed = 400.0 - currentSpeed;
                            if (missingSpeed <= 0) {
                                buffAmp = -1; // Không buff thêm nếu đã dư 400
                            } else {
                                buffAmp = (int) Math.ceil(missingSpeed / 20.0) - 1;
                            }
                        }
                    }
                    
                    if (buffAmp >= 0) {
                        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 40, buffAmp));
                    }
                    
                    player.sendMessage("§4[Gáe Bolg] §cTốc độ bùng nổ! Chạy ngay đi!");

                    // Phát vài hạt đỏ trên người
                    player.getWorld().spawnParticle(Particle.DUST, player.getLocation().add(0, 1, 0), 50, 0.5, 1.0, 0.5, 0, new Particle.DustOptions(org.bukkit.Color.RED, 2.0f));
                }
            }
        }.runTaskTimer(plugin, 1L, 1L);
    }
}

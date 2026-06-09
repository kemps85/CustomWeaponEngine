package org.example.system;

import io.lumine.mythic.api.mobs.MythicMob;
import io.lumine.mythic.bukkit.events.MythicMobSpawnEvent;
import io.lumine.mythic.core.mobs.ActiveMob;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.EventPriority;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Ghast;
import org.bukkit.entity.Phantom;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.Bukkit;
import org.bukkit.metadata.FixedMetadataValue;

public class MobLevelListener implements Listener {

    private final JavaPlugin plugin;

    public MobLevelListener(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onMythicMobSpawn(MythicMobSpawnEvent event) {
        ActiveMob activeMob = event.getMob();
        MythicMob mythicMob = event.getMobType();

        // Bỏ qua nếu là Boss (ID có chữ BOSS hoặc tùy chỉnh thêm sau)
        if (mythicMob.getInternalName().toUpperCase().contains("BOSS")) {
            return;
        }

        Location loc = event.getLocation();
        if (loc == null || loc.getWorld() == null) return;

        double distance = 0;
        try {
            // Lấy khoảng cách từ điểm Spawn của World đó (0,0,0)
            Location spawnLoc = loc.getWorld().getSpawnLocation();
            distance = loc.distance(spawnLoc);
        } catch (Exception e) {
            // Nếu khác world (ví dụ quái sinh ra ở Nether nhưng check khoảng cách với Overworld spawn thì distance() ném lỗi)
            // getWorld().getSpawnLocation() luôn trả về điểm spawn của CHÍNH world đó, nên hiếm khi lỗi.
            return;
        }

        int level = 1;

        // Công thức chung: Safe Zone 100 block
        if (distance <= 100) {
            level = 1;
        } else {
            // Từ block thứ 101 trở đi, cứ 50 block thì +5 level
            double extraDistance = distance - 100;
            int levelBoost = (int) (extraDistance / 50.0) * 5;
            level = 1 + levelBoost;
        }

        // Giới hạn kịch trần (Max Level 150)
        if (level > 150) {
            level = 150;
        }

        // Ép Level cho con quái vừa sinh ra
        activeMob.setLevel(level);
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onVanillaMobSpawn(CreatureSpawnEvent event) {
        LivingEntity entity = event.getEntity();

        // Chỉ áp dụng cho quái vật
        if (!(entity instanceof Monster) && 
            !(entity instanceof Slime) && 
            !(entity instanceof Ghast) && 
            !(entity instanceof Phantom)) {
            return;
        }

        // Chạy delay 1 tick để đảm bảo MythicMobs đã nạp xong data nếu nó là quái custom
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if (!entity.isValid() || entity.isDead()) return;

            // Bỏ qua Hộ Vệ (Đã có logic riêng ở BazaarMobDropListener)
            if (entity.hasMetadata("cwe_guardian")) return;

            // Bỏ qua quái MythicMobs (Boss, Quái event...)
            if (io.lumine.mythic.bukkit.MythicBukkit.inst().getMobManager().isActiveMob(entity.getUniqueId())) {
                return;
            }

            Location loc = entity.getLocation();
            if (loc.getWorld() == null) return;

            double distance = 0;
            try {
                Location spawnLoc = loc.getWorld().getSpawnLocation();
                distance = loc.distance(spawnLoc);
            } catch (Exception e) {
                return;
            }

            int level = 1;
            if (distance <= 100) {
                level = 1;
            } else {
                double extraDistance = distance - 100;
                int levelBoost = (int) (extraDistance / 50.0) * 5;
                level = 1 + levelBoost;
            }

            if (level > 150) level = 150;
            if (level <= 1) return; // Khỏi buff nếu level 1

            AttributeInstance hpAttr = entity.getAttribute(Attribute.GENERIC_MAX_HEALTH);
            AttributeInstance dmgAttr = entity.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE);

            if (hpAttr != null) {
                double baseHp = hpAttr.getBaseValue();
                // Trả lại máu gốc: multiplier = 1.0 + (level * 0.5) + (level^2.5 / 160.0)
                // Đạt ~1800x ở Lv.150 (36.000 HP)
                double hpMultiplier = 1.0 + (level * 0.5) + (Math.pow(level, 2.5) / 160.0);
                hpAttr.setBaseValue(baseHp * hpMultiplier);
                entity.setHealth(hpAttr.getValue());
            }

            if (dmgAttr != null) {
                double baseDmg = dmgAttr.getBaseValue();
                // Sát thương giảm nhẹ: multiplier = 1.0 + (level * 0.5) + (level^2.5 / 650.0)
                // Đạt ~500x ở Lv.150 (1.500 Dmg)
                double dmgMultiplier = 1.0 + (level * 0.5) + (Math.pow(level, 2.5) / 650.0);
                dmgAttr.setBaseValue(baseDmg * dmgMultiplier);
            }

            // Đổi tên hiển thị theo format: Tên_Quái [Lv.X]
            String rawType = entity.getType().toString();
            String defaultName = rawType.substring(0, 1).toUpperCase() + rawType.substring(1).toLowerCase().replace("_", " ");
            
            String nameColor = "§f";
            if (level >= 100) nameColor = "§4§l";
            else if (level >= 50) nameColor = "§c";
            else if (level >= 20) nameColor = "§e";

            entity.setCustomName(nameColor + defaultName + " §7[Lv." + level + "]");
            entity.setCustomNameVisible(true);

            // Tương thích ngược với các plugin check level khác (như rơi đồ)
            entity.setMetadata("leveledmobs:level", new FixedMetadataValue(plugin, level));
        }, 1L);
    }
}

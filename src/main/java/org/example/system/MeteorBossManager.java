package org.example.system;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.NamespacedKey;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import org.example.core.CustomWeaponEngine;

import java.util.*;

public class MeteorBossManager implements Listener {

    private final CustomWeaponEngine plugin;
    private final RegionBossManager regionBossManager;
    private static final Random random = new Random();
    
    public UUID activeBossId = null;
    private String activeType = null;
    
    private int mainTask = -1;
    private double maxBossHealth = 0;
    
    // ==========================================
    // STATE VARIABLES FOR BOSSES
    // ==========================================
    
    // FIRE State
    private int fireETimer = 0;
    private int fireECount = 0;
    private boolean fireGuardsDeadTriggered = false;
    
    // ICE State
    private int iceETimer = 30;
    private int iceFreezeCount = 0;
    private int iceFlightTimer = 0;
    private boolean iceIsFlying = false;
    public UUID iceFrozenPlayer = null;
    private int iceFrozenTicks = 0;
    private Location iceFrozenLoc = null;
    private int iceBasicAttackTimer = 0;
    
    // VOID State
    private int voidETimer = 35;
    private int voidECount = 0;
    private int voidBlackHoleTicks = 0;
    private Location voidBlackHoleLoc = null;
    private Set<UUID> voidBlackHoleVictims = new HashSet<>();
    
    private boolean voidIsSplit = false;
    private int voidSplitTimer = 0;
    private List<LivingEntity> voidClones = new ArrayList<>();
    private LivingEntity voidRealClone = null;
    private boolean voidIsUltimate = false; // Is this split from Ultimate or Passive?
    
    // Hồi sinh tạm thời
    private Set<UUID> eventParticipants = new HashSet<>();

    public MeteorBossManager(CustomWeaponEngine plugin, RegionBossManager regionBossManager) {
        this.plugin = plugin;
        this.regionBossManager = regionBossManager;
    }

    public static void spawnBoss(Location loc, String type) {
        MeteorBossManager instance = CustomWeaponEngine.getPlugin(CustomWeaponEngine.class).getMeteorBossManager();
        if (instance != null) {
            instance.doSpawnBoss(loc, type);
        }
    }

    private void doSpawnBoss(Location loc, String type) {
        if (activeBossId != null) {
            Entity old = Bukkit.getEntity(activeBossId);
            if (old != null && !old.isDead()) old.remove();
        }

        Location spawnLoc = loc.clone().add(0, 2, 0);
        activeType = type;
        
        // Reset states
        fireETimer = 0; fireECount = 0; fireGuardsDeadTriggered = false;
        iceETimer = 30; iceFreezeCount = 0; iceFlightTimer = 0; iceIsFlying = false; iceFrozenPlayer = null; iceFrozenTicks = 0;
        voidETimer = 35; voidECount = 0; voidBlackHoleTicks = 0; voidIsSplit = false; voidClones.clear(); voidIsUltimate = false;
        voidBlackHoleVictims.clear();
        eventParticipants.clear();

        if ("FIRE".equals(type)) {
            WitherSkeleton boss = (WitherSkeleton) loc.getWorld().spawnEntity(spawnLoc, EntityType.WITHER_SKELETON);
            setupBossStats(boss, "§c§lHỏa Diệm Ma Vương", 2000, type);
            spawnGuards(loc, EntityType.BLAZE, 4, type, 600);
        } 
        else if ("ICE".equals(type)) {
            Zombie boss = (Zombie) loc.getWorld().spawnEntity(spawnLoc, EntityType.ZOMBIE);
            setupBossStats(boss, "§b§lBăng Giá Cổ Thần", 15000, type);
            spawnGuards(loc, EntityType.STRAY, 4, type, 500);
        } 
        else if ("VOID".equals(type)) {
            Enderman boss = (Enderman) loc.getWorld().spawnEntity(spawnLoc, EntityType.ENDERMAN);
            setupBossStats(boss, "§5§lChúa Tể Hư Không", 300000, type);
            spawnGuards(loc, EntityType.ENDERMITE, 3, type, 15000);
            spawnGuards(loc, EntityType.ENDERMAN, 2, type, 15000);
        }

        startBossLogic();
    }
    
    private void setupBossStats(LivingEntity boss, String name, double hp, String type) {
        boss.setCustomName(name);
        boss.setCustomNameVisible(true);
        boss.setMetadata("LM_IGNORE", new FixedMetadataValue(plugin, true));
        boss.getPersistentDataContainer().set(new NamespacedKey(plugin, "cwe_meteor_boss"), PersistentDataType.STRING, type);
        
        maxBossHealth = hp;
        
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if (boss.isValid()) {
                boss.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(hp);
                boss.setHealth(hp);
            }
        }, 5L);
        activeBossId = boss.getUniqueId();
    }

    private void spawnGuards(Location loc, EntityType entityType, int amount, String type, double hp) {
        for (int i = 0; i < amount; i++) {
            Location guardLoc = loc.clone().add(random.nextInt(10) - 5, 2, random.nextInt(10) - 5);
            LivingEntity guard = (LivingEntity) loc.getWorld().spawnEntity(guardLoc, entityType);
            guard.setCustomName("§8[Hộ Vệ] §c" + entityType.name());
            guard.setCustomNameVisible(true);
            guard.setMetadata("LM_IGNORE", new FixedMetadataValue(plugin, true));
            guard.getPersistentDataContainer().set(new NamespacedKey(plugin, "cwe_meteor_guard"), PersistentDataType.STRING, type);
            
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                if (guard.isValid()) {
                    guard.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(hp);
                    guard.setHealth(hp);
                }
            }, 5L);
        }
    }

    private void startBossLogic() {
        if (mainTask != -1) Bukkit.getScheduler().cancelTask(mainTask);

        mainTask = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            if (activeBossId == null) {
                Bukkit.getScheduler().cancelTask(mainTask);
                return;
            }
            Entity ent = Bukkit.getEntity(activeBossId);
            if (ent == null || !ent.isValid()) {
                // Chunk unloaded? Pause logic, don't cancel!
                return; 
            }
            if (ent.isDead()) {
                if (!"VOID".equals(activeType) || !voidIsSplit) {
                    Bukkit.getScheduler().cancelTask(mainTask);
                    return;
                }
            }
            
            LivingEntity activeBoss = (LivingEntity) ent;
            
            // Fix LeveledMobs Race Condition
            if (activeBoss.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() != maxBossHealth) {
                activeBoss.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(maxBossHealth);
            }
            
            // Cập nhật Boss Bar
            

            if ("FIRE".equals(activeType)) tickFireBoss(activeBoss);
            else if ("ICE".equals(activeType)) tickIceBoss(activeBoss);
            else if ("VOID".equals(activeType)) tickVoidBoss(activeBoss);
            
        }, 0L, 20L).getTaskId(); // Chạy mỗi giây (20 ticks)
    }

    public void cleanupOnDisable() {
        if (activeBossId != null) {
            Entity ent = Bukkit.getEntity(activeBossId);
            if (ent != null) ent.remove();
            activeBossId = null;
        }
        for (LivingEntity c : voidClones) {
            if (c.isValid()) c.remove();
        }
        voidClones.clear();
        if (iceFrozenLoc != null) {
            iceFrozenLoc.getBlock().setType(Material.AIR);
            iceFrozenLoc = null;
        }
        if (mainTask != -1) {
            Bukkit.getScheduler().cancelTask(mainTask);
            mainTask = -1;
        }
    }

    // ==========================================
    // TICK LOGIC: FIRE BOSS
    // ==========================================
    private void tickFireBoss(LivingEntity activeBoss) {
        boolean playerNear = false;
        Player nearest = getNearestPlayer(activeBoss.getLocation(), 15);
        if (nearest != null) playerNear = true;

        if (fireETimer <= 0 && playerNear) {
            // Trigger E
            fireETimer = 30;
            fireECount++;
            for (Entity e : activeBoss.getNearbyEntities(15, 5, 15)) {
                if (e instanceof Player) {
                    Player p = (Player) e;
                    p.setFireTicks(100);
                    p.getWorld().spawnParticle(Particle.FLAME, p.getLocation(), 50, 0.5, 1, 0.5, 0.1);
                    p.sendMessage("§cBạn đã bước vào Lãnh Địa Lửa!");
                }
            }
            activeBoss.getWorld().playSound(activeBoss.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 2f, 1f);

            // Ultimate check
            if (fireECount >= 3) {
                fireECount = 0;
                Player ultTarget = getNearestPlayer(activeBoss.getLocation(), 20);
                if (ultTarget != null) {
                    ultTarget.setVelocity(new Vector(0, 1.5, 0));
                    double targetHp = ultTarget.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() * 0.2;
                    double newHp = targetHp - 10;
                    if (newHp <= 0) ultTarget.setHealth(0);
                    else ultTarget.setHealth(newHp);
                    ultTarget.setFireTicks(100);
                    ultTarget.sendMessage("§4§lBoss đã sử dụng Nộ: HẤT TUNG TRÚT GIẬN!");
                }
            }
        }
        if (fireETimer > 0) fireETimer--;
        
        // Passive check: Guards dead?
        if (!fireGuardsDeadTriggered) {
            boolean hasGuards = false;
            for (Entity e : activeBoss.getWorld().getLivingEntities()) {
                if (e.getPersistentDataContainer().has(new NamespacedKey(plugin, "cwe_meteor_guard"), PersistentDataType.STRING)) {
                    hasGuards = true; break;
                }
            }
            if (!hasGuards) {
                fireGuardsDeadTriggered = true;
                Player p = getNearestPlayer(activeBoss.getLocation(), 20);
                if (p != null) {
                    activeBoss.teleport(p);
                    p.damage(40.0, activeBoss);
                    p.sendMessage("§c§lBoss nổi điên vì đàn em chết! Lao đến đâm bạn!");
                }
                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    if (activeBoss != null && !activeBoss.isDead()) {
                        spawnGuards(activeBoss.getLocation(), EntityType.BLAZE, 4, "FIRE", 600);
                        fireGuardsDeadTriggered = false;
                    }
                }, 200L); // 10s
            }
        }
    }

    // ==========================================
    // TICK LOGIC: ICE BOSS
    // ==========================================
    private void tickIceBoss(LivingEntity activeBoss) {
        // Flight mechanic
        iceFlightTimer++;
        if (iceIsFlying && iceFlightTimer >= 20) {
            iceIsFlying = false;
            iceFlightTimer = 0;
            activeBoss.setGravity(true);
        } else if (!iceIsFlying && iceFlightTimer >= 15) {
            iceIsFlying = true;
            iceFlightTimer = 0;
            activeBoss.setGravity(false);
            activeBoss.teleport(activeBoss.getLocation().add(0, 5, 0));
        }
        
        if (iceIsFlying) {
            activeBoss.setVelocity(new Vector(0, 0.05, 0)); // Hover
        }

        // Basic Attack (Gacha)
        iceBasicAttackTimer++;
        if (iceBasicAttackTimer >= 3) {
            iceBasicAttackTimer = 0;
            List<Player> nearby = getNearbyPlayers(activeBoss.getLocation(), 20);
            if (!nearby.isEmpty()) {
                Player target = nearby.get(random.nextInt(nearby.size()));
                // Bắn cục tuyết ảo
                Location from = activeBoss.getEyeLocation();
                Vector dir = target.getEyeLocation().subtract(from).toVector().normalize().multiply(1.5);
                Snowball sb = (Snowball) activeBoss.getWorld().spawnEntity(from, EntityType.SNOWBALL);
                sb.setVelocity(dir);
                sb.setMetadata("ice_boss_projectile", new FixedMetadataValue(plugin, true));
            }
        }

        // Frozen damage tick
        if (iceFrozenPlayer != null) {
            Player p = Bukkit.getPlayer(iceFrozenPlayer);
            if (p != null && p.isOnline()) {
                p.damage(4.0); // 4 máu (2 tim)
                iceFrozenTicks--;
                if (iceFrozenTicks <= 0) {
                    iceFrozenPlayer = null;
                    p.sendMessage("§aBạn đã thoát khỏi lớp băng!");
                }
            } else {
                iceFrozenPlayer = null;
            }
        }

        // Skill E
        iceETimer--;
        if (iceETimer <= 0) {
            iceETimer = 30;
            Player p = getNearestPlayer(activeBoss.getLocation(), 15);
            if (p != null) {
                iceFrozenPlayer = p.getUniqueId();
                iceFrozenTicks = 10;
                iceFrozenLoc = p.getLocation().getBlock().getLocation().add(0.5, 0, 0.5);
                p.teleport(iceFrozenLoc);
                p.getLocation().getBlock().setType(Material.ICE);
                p.sendMessage("§b§lBạn đã bị ĐÓNG BĂNG TỦY XƯƠNG! Đồng đội phải gây 20 sát thương lên bạn để giải cứu!");
                iceFreezeCount++;
                
                // Ultimate check
                if (iceFreezeCount >= 5) {
                    iceFreezeCount = 0;
                    if (iceFrozenPlayer != null) {
                        Player ultP = Bukkit.getPlayer(iceFrozenPlayer);
                        if (ultP != null) {
                            ultP.setHealth(0); // Instant death
                            ultP.sendMessage("§4§lBoss tung Nộ: BĂNG GIÁ VĨNH CỬU! BẠN ĐÃ VỠ VỤN!");
                        }
                    } else {
                        List<Player> all = getNearbyPlayers(activeBoss.getLocation(), 30);
                        if (!all.isEmpty()) {
                            // Find furthest
                            Player furthest = null; double maxDist = -1;
                            for (Player pp : all) {
                                double d = pp.getLocation().distanceSquared(activeBoss.getLocation());
                                if (d > maxDist) { maxDist = d; furthest = pp; }
                            }
                            if (furthest != null) {
                                activeBoss.teleport(furthest);
                                iceFrozenPlayer = furthest.getUniqueId();
                                iceFrozenTicks = 30;
                                iceFrozenLoc = furthest.getLocation().getBlock().getLocation().add(0.5, 0, 0.5);
                                furthest.teleport(iceFrozenLoc);
                                furthest.sendMessage("§4§lBoss tung Nộ lên bạn! Bị đóng băng 30 GIÂY!");
                            }
                        }
                    }
                }
            }
        }
    }

    // ==========================================
    // TICK LOGIC: VOID BOSS
    // ==========================================
    private void tickVoidBoss(LivingEntity activeBoss) {
        if (voidIsSplit) {
            voidSplitTimer--;
            if (voidSplitTimer <= 0) {
                // Merge back
                for (LivingEntity clone : voidClones) {
                    if (clone.isValid()) clone.remove();
                }
                voidClones.clear();
                voidIsSplit = false;
                
                // Hồi máu 10% or phạt Nộ
                activeBoss.teleport(voidRealClone.getLocation()); // Về vị trí clone thật
                activeBoss.setInvisible(false);
                
                if (voidIsUltimate) {
                    // Fail ultimate
                    for (Player p : getNearbyPlayers(activeBoss.getLocation(), 30)) {
                        p.setHealth(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() * 0.1);
                        p.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 200, 4));
                        p.addPotionEffect(new PotionEffect(PotionEffectType.MINING_FATIGUE, 200, 4));
                        p.sendMessage("§5§l[LÃNH ĐỊA BÓNG TỐI] Thất bại! Bạn bị bóng tối cắn nuốt!");
                        p.playSound(p.getLocation(), Sound.ENTITY_WITHER_DEATH, 1, 0.5f);
                    }
                } else {
                    // Fail passive
                    double maxHp = activeBoss.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
                    activeBoss.setHealth(Math.min(maxHp, activeBoss.getHealth() + maxHp * 0.1));
                    Bukkit.broadcastMessage("§5Boss đã thu hồi phân thân và hồi 10% máu!");
                }
            }
            return; // Đang tàng hình thì k làm gì thêm
        }

        // Black hole logic
        if (voidBlackHoleTicks > 0 && voidBlackHoleLoc != null) {
            voidBlackHoleTicks--;
            for (Player p : getNearbyPlayers(voidBlackHoleLoc, 30)) {
                double dist = p.getLocation().distance(voidBlackHoleLoc);
                if (dist < 2.0) {
                    p.damage(5.0); // 5 true damage
                    voidBlackHoleVictims.add(p.getUniqueId());
                } else {
                    Vector v = voidBlackHoleLoc.toVector().subtract(p.getLocation().toVector()).normalize().multiply(0.4);
                    p.setVelocity(p.getVelocity().add(v));
                }
            }
            voidBlackHoleLoc.getWorld().spawnParticle(Particle.PORTAL, voidBlackHoleLoc, 50, 2, 2, 2, 0.1);
            
            // Xử lý khi Hố đen vừa kết thúc
            if (voidBlackHoleTicks == 0) {
                Player target = null;
                for (Player p : getNearbyPlayers(activeBoss.getLocation(), 40)) {
                    if (!voidBlackHoleVictims.contains(p.getUniqueId())) {
                        target = p; break;
                    }
                }
                if (target != null) {
                    Bukkit.broadcastMessage("§5§lBoss đã phát hiện kẻ luồn lách! Đang truy sát!");
                    activeBoss.teleport(target.getLocation().add(target.getLocation().getDirection().multiply(-1))); // Teleport ra sau lưng
                    target.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 200, 1));
                }
            }
        }

        // Skill E
        voidETimer--;
        if (voidETimer <= 0 && voidBlackHoleTicks <= 0) {
            voidETimer = 35;
            voidBlackHoleTicks = 10; // 10 giây = 10 lần tick mỗi s
            voidBlackHoleLoc = activeBoss.getLocation().clone();
            voidBlackHoleVictims.clear();
            Bukkit.broadcastMessage("§5Boss tung Hố Đen! Kéo dài 10 giây!");
            voidECount++;
            
            // Ultimate check
            if (voidECount >= 3) {
                voidECount = 0;
                Bukkit.broadcastMessage("§5§lBoss tung Nộ: LÃNH ĐỊA BÓNG TỐI! Tìm bản thể thật trong 10 giây!");
                triggerVoidSplit(true, activeBoss);
            }
        }
    }

    private void triggerVoidSplit(boolean isUltimate, LivingEntity activeBoss) {
        voidIsSplit = true;
        voidIsUltimate = isUltimate;
        voidSplitTimer = isUltimate ? 10 : 20; // Nộ 10s, Nội tại 20s
        activeBoss.setInvisible(true); // Tàng hình bản chính
        
        Location center = activeBoss.getLocation();
        voidClones.clear();
        for (int i = 0; i < 12; i++) {
            Location loc = center.clone().add(random.nextInt(20)-10, 0, random.nextInt(20)-10);
            Enderman clone = (Enderman) center.getWorld().spawnEntity(loc, EntityType.ENDERMAN);
            clone.setCustomName("§5§lChúa Tể Hư Không");
            clone.setMetadata("LM_IGNORE", new FixedMetadataValue(plugin, true));
            clone.getPersistentDataContainer().set(new NamespacedKey(plugin, "cwe_void_clone"), PersistentDataType.INTEGER, i);
            
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                if (clone.isValid()) {
                    clone.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20);
                    clone.setHealth(20);
                }
            }, 5L);
            voidClones.add(clone);
        }
        // Chọn con thật
        voidRealClone = voidClones.get(random.nextInt(12));
        
        if (isUltimate) {
            for (Player p : getNearbyPlayers(center, 30)) {
                p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 200, 1));
                p.addPotionEffect(new PotionEffect(PotionEffectType.DARKNESS, 200, 1));
            }
        }
    }

    // ==========================================
    // EVENTS
    // ==========================================

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (activeBossId == null) return;
        Entity activeBoss = Bukkit.getEntity(activeBossId);
        if (activeBoss == null) return;

        // Băng: Phá băng bằng sát thương đồng đội
        if (event.getEntity() instanceof Player && "ICE".equals(activeType)) {
            Player p = (Player) event.getEntity();
            if (iceFrozenPlayer != null && p.getUniqueId().equals(iceFrozenPlayer)) {
                if (event instanceof EntityDamageByEntityEvent) {
                    EntityDamageByEntityEvent e = (EntityDamageByEntityEvent) event;
                    if (e.getDamager() instanceof Player) { // Đồng đội chém
                        if (e.getDamage() >= 20.0) {
                            iceFrozenPlayer = null;
                            p.sendMessage("§aBạn đã được giải cứu bởi đồng đội!");
                            e.getDamager().sendMessage("§aĐã phá băng giải cứu thành công!");
                            p.getLocation().getBlock().setType(Material.AIR);
                            event.setCancelled(true);
                        } else {
                            e.getDamager().sendMessage("§cSát thương của bạn k đủ phá băng! Cần >= 20 sát thương 1 hit!");
                        }
                    }
                }
            }
        }

        // Void Boss Passive
        if (event.getEntity().getUniqueId().equals(activeBoss.getUniqueId()) && "VOID".equals(activeType)) {
            if (event.getCause() == EntityDamageEvent.DamageCause.PROJECTILE || event.getCause() == EntityDamageEvent.DamageCause.DROWNING || event.getCause() == EntityDamageEvent.DamageCause.CONTACT) {
                event.setCancelled(true); return;
            }
            // Hủy toàn bộ sát thương lên bản thể thật nếu đang ở trạng thái phân thân
            if (voidIsSplit) {
                event.setCancelled(true);
                return;
            }
            if (event.getDamage() > ((LivingEntity) activeBoss).getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() * 0.15) {
                Bukkit.broadcastMessage("§5Boss chịu sát thương quá lớn! Tách thành 12 phân thân!");
                triggerVoidSplit(false, (LivingEntity) activeBoss);
            }
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        // Fire Basic Attack Wither
        if (activeBossId != null && event.getDamager().getUniqueId().equals(activeBossId)) {
            Entity activeBoss = Bukkit.getEntity(activeBossId);
            if (activeBoss == null) return;
            if ("FIRE".equals(activeType)) {
                  event.setDamage(500.0);
                if (event.getEntity() instanceof Player) {
                    ((Player) event.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 100, 1));
                }
            }
            else if ("VOID".equals(activeType)) {
                // Backstab logic
                if (event.getEntity() instanceof Player) {
                    Player p = (Player) event.getEntity();
                    Vector pDir = p.getLocation().getDirection().setY(0).normalize();
                    Vector bossDir = activeBoss.getLocation().getDirection().setY(0).normalize();
                    double dot = pDir.dot(bossDir);
                    // Nếu cùng hướng (dot > 0.5) tức là Boss chém từ sau lưng
                    if (dot > 0.5) {
                        event.setDamage(2000.0);
                        p.sendMessage("§5§lBẠN BỊ ĐÂM LÉN!");
                    } else {
                        event.setDamage(800.0);
                    }
                }
            }
        }

        // Ice Projectile
        if (event.getDamager() instanceof Snowball && event.getDamager().hasMetadata("ice_boss_projectile")) {
            event.setDamage(5.0);
            if (event.getEntity() instanceof Player) {
                ((Player) event.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 100, 0));
            }
        }

        // Void Clones
        if (voidIsSplit && event.getEntity() instanceof Enderman) {
            Enderman e = (Enderman) event.getEntity();
            if (e.getPersistentDataContainer().has(new NamespacedKey(plugin, "cwe_void_clone"), PersistentDataType.INTEGER)) {
                if (e.getHealth() - event.getFinalDamage() <= 0) {
                    event.setCancelled(true);
                    e.remove();
                    if (e.equals(voidRealClone)) {
                        // Thắng mini-game
                        Bukkit.broadcastMessage("§aĐã phá hủy phân thân thật! Boss bị choáng!");
                        for (LivingEntity clone : voidClones) {
                            if (clone.isValid()) clone.remove();
                        }
                        voidClones.clear();
                        voidIsSplit = false;
                        if (activeBossId != null) {
                            Entity boss = Bukkit.getEntity(activeBossId);
                            if (boss != null) {
                                boss.teleport(e.getLocation());
                                if (boss instanceof LivingEntity) {
                                    ((LivingEntity) boss).setInvisible(false);
                                    ((LivingEntity) boss).addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 200, 10)); // Stun
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (iceFrozenPlayer != null && event.getPlayer().getUniqueId().equals(iceFrozenPlayer)) {
            Location from = event.getFrom();
            Location to = event.getTo();
            if (to != null && (from.getX() != to.getX() || from.getZ() != to.getZ())) {
                event.getPlayer().teleport(iceFrozenLoc);
            }
        }
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        if (iceFrozenPlayer != null && event.getPlayer().getUniqueId().equals(iceFrozenPlayer)) {
            if (event.getCause() == PlayerTeleportEvent.TeleportCause.ENDER_PEARL || event.getCause() == PlayerTeleportEvent.TeleportCause.CHORUS_FRUIT) {
                event.setCancelled(true);
                event.getPlayer().sendMessage("§cBạn đang bị đóng băng! Không thể dịch chuyển!");
            }
        }
    }

    @EventHandler
    public void onBossDeath(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity.getPersistentDataContainer().has(new NamespacedKey(plugin, "cwe_meteor_guard"), PersistentDataType.STRING)) {
            event.getDrops().clear();
            event.setDroppedExp(0);
        }
        else if (entity.getPersistentDataContainer().has(new NamespacedKey(plugin, "cwe_meteor_boss"), PersistentDataType.STRING)) {
            event.getDrops().clear();
            event.setDroppedExp(0);
            
            if (mainTask != -1) {
                Bukkit.getScheduler().cancelTask(mainTask);
                mainTask = -1;
            }
            // Clear clones
            for (LivingEntity c : voidClones) if (c.isValid()) c.remove();
            
            if (iceFrozenPlayer != null) {
                Player p = Bukkit.getPlayer(iceFrozenPlayer);
                if (p != null) p.getLocation().getBlock().setType(Material.AIR);
                iceFrozenPlayer = null;
            }
            if (iceFrozenLoc != null) {
                iceFrozenLoc.getBlock().setType(Material.AIR);
                iceFrozenLoc = null;
            }
            
            activeBossId = null;
            regionBossManager.onBossDeath(activeType);
        }
    }

    private Player getNearestPlayer(Location loc, double radius) {
        Player nearest = null;
        double minSq = radius * radius;
        for (Player p : loc.getWorld().getPlayers()) {
            double sq = p.getLocation().distanceSquared(loc);
            if (sq <= minSq) {
                minSq = sq;
                nearest = p;
            }
        }
        return nearest;
    }

    private List<Player> getNearbyPlayers(Location loc, double radius) {
        List<Player> list = new ArrayList<>();
        double rsq = radius * radius;
        for (Player p : loc.getWorld().getPlayers()) {
            if (p.getLocation().distanceSquared(loc) <= rsq) list.add(p);
        }
        return list;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (iceFrozenPlayer != null && event.getPlayer().getUniqueId().equals(iceFrozenPlayer)) {
            iceFrozenPlayer = null;
            if (iceFrozenLoc != null) {
                iceFrozenLoc.getBlock().setType(Material.AIR);
                for (Player p : getNearbyPlayers(iceFrozenLoc, 20)) {
                    p.damage(20.0);
                    p.sendMessage("§cĐồng minh của bạn hèn nhát bỏ trốn! Khối băng vỡ vụn gây 20 sát thương!");
                }
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (iceFrozenLoc != null && event.getBlock().getLocation().equals(iceFrozenLoc.getBlock().getLocation())) {
            event.setCancelled(true);
            event.getPlayer().sendMessage("§cKhông thể tự đào khối băng để thoát!");
        }
    }

    @EventHandler
    public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event) {
        if (activeBossId != null && "FIRE".equals(activeType) && event.getBucket() == Material.WATER_BUCKET) {
            Entity activeBoss = Bukkit.getEntity(activeBossId);
            if (activeBoss != null && event.getPlayer().getLocation().distance(activeBoss.getLocation()) < 50) {
                event.setCancelled(true); // Ngăn không cho đặt nước
                event.getPlayer().getInventory().setItemInMainHand(new org.bukkit.inventory.ItemStack(Material.BUCKET)); // Trả xô không
                event.getBlockClicked().getRelative(event.getBlockFace()).setType(Material.AIR);
                event.getPlayer().getWorld().playSound(event.getBlockClicked().getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 1f, 1f);
                event.getPlayer().getWorld().spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, event.getBlockClicked().getLocation(), 10, 0.5, 0.5, 0.5, 0.1);
                event.getPlayer().sendMessage("§c§lNước bốc hơi ngay lập tức vì sức nóng của Ma Vương!");
            }
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        // Ghost freeze fix
        if (iceFrozenPlayer != null && event.getEntity().getUniqueId().equals(iceFrozenPlayer)) {
            iceFrozenPlayer = null;
            if (iceFrozenLoc != null) {
                iceFrozenLoc.getBlock().setType(Material.AIR);
                iceFrozenLoc = null;
            }
        }
        
        if (activeBossId != null) {
            Entity activeBoss = Bukkit.getEntity(activeBossId);
            if (activeBoss != null && !activeBoss.isDead()) {
                if (event.getEntity().getLocation().distance(activeBoss.getLocation()) < 100) {
                    eventParticipants.add(event.getEntity().getUniqueId());
                }
            }
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        if (activeBossId != null && eventParticipants.contains(event.getPlayer().getUniqueId())) {
            Entity activeBoss = Bukkit.getEntity(activeBossId);
            if (activeBoss != null && !activeBoss.isDead()) {
                Location safeLoc = activeBoss.getLocation().clone().add(random.nextInt(30)-15, 0, random.nextInt(30)-15);
                safeLoc.setY(safeLoc.getWorld().getHighestBlockYAt(safeLoc) + 1);
                event.setRespawnLocation(safeLoc);
                event.getPlayer().sendMessage("§eBạn đã được hồi sinh ngay tại chiến trường Thiên Thạch!");
            }
        }
    }
}

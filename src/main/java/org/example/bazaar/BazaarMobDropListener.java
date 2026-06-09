package org.example.bazaar;

import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import java.util.Arrays;
import java.util.Random;
import dev.aurelium.auraskills.api.AuraSkillsApi;
import dev.aurelium.auraskills.api.user.SkillsUser;
import dev.aurelium.auraskills.api.skill.Skills;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.mobs.ActiveMob;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.EventPriority;

public class BazaarMobDropListener implements Listener {
    private final JavaPlugin plugin;
    private final Random random = new Random();

    public static final ThreadLocal<Boolean> BYPASS_SPAWN = ThreadLocal.withInitial(() -> false);

    public BazaarMobDropListener(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCreatureSpawnBypass(CreatureSpawnEvent event) {
        if (BYPASS_SPAWN.get()) {
            if (event.isCancelled()) {
                event.setCancelled(false);
                plugin.getLogger().info("§e[Bypass] Phát hiện sự kiện sinh quái bị hủy bởi plugin khác! Đã khôi phục để sinh Hộ Vệ: §b" + event.getEntity().getType());
            }
        }
    }

    /**
     * 🔮 Hàm bọc độc quyền hỗ trợ quét tên Potion xuyên không gian phiên bản
     */
    private PotionEffectType getPotionType(String modernName, String legacyName) {
        PotionEffectType type = PotionEffectType.getByName(modernName);
        if (type == null) {
            type = PotionEffectType.getByName(legacyName);
        }
        return type;
    }

    private boolean isGuardianOrElder(LivingEntity entity) {
        return entity.hasMetadata("cwe_guardian");
    }

    @EventHandler
    public void onMobDeath(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity.getKiller() == null) return; 

        if (!entity.hasMetadata("cwe_guardian")) return;

        handleGuardianDrop(entity, event);
        
        // Notify OreVeinManager if it was in a vein
        if (entity.hasMetadata("cwe_guardian_vein")) {
            org.example.system.OreVeinManager manager = getOreVeinManager();
            if (manager != null) {
                String veinId = entity.getMetadata("cwe_guardian_vein").get(0).asString();
                org.example.system.OreVeinManager.OreVein vein = manager.getVeinAt(entity.getLocation());
                if (vein != null && vein.id.toString().equals(veinId)) {
                    manager.notifyGuardianDied(vein);
                }
            }
        }
    }
    
    private org.example.system.OreVeinManager getOreVeinManager() {
        return plugin.getServer().getServicesManager().load(org.example.system.OreVeinManager.class);
    }

    @EventHandler
    public void onOreBreak(BlockBreakEvent event) {
        Material type = event.getBlock().getType();
        Location loc = event.getBlock().getLocation().add(0.5, 0, 0.5);
        Player player = event.getPlayer();

        org.example.system.OreVeinManager manager = getOreVeinManager();
        boolean inVein = false;
        String veinIdStr = null;

        if (manager != null) {
            org.example.system.OreVeinManager.OreVein vein = manager.getVeinAt(loc);
            if (vein != null) {
                inVein = true;
                veinIdStr = vein.id.toString();
                if (manager.countActiveGuardians(vein) >= 5) {
                    return; // Vượt giới hạn, không spawn
                }
            }
        }

        if (!inVein && random.nextDouble() > 0.07) return; 

        final String finalVeinId = veinIdStr;
        // Chờ 1 tick để block chuyển thành AIR trước khi thực sinh quái, tránh bị cơ chế kẹt/ngộp của Bukkit/WorldGuard hủy bỏ
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            spawnGuardianAt(loc, player, type, finalVeinId);
        }, 1L);
    }

    public boolean spawnGuardianAt(Location loc, Player player, Material type, String veinId) {
        String guardianName = null;
        EntityType mobType = EntityType.ZOMBIE;
        String gType = "";
        String mmMobId = "";

        if (type == Material.DIAMOND_ORE || type == Material.DEEPSLATE_DIAMOND_ORE) {
            guardianName = "§6§lHỘ VỆ QUẶNG KIM CƯƠNG"; mobType = EntityType.ZOMBIE; gType = "DIAMOND"; mmMobId = "DiamondGuardian";
        } else if (type == Material.IRON_ORE || type == Material.DEEPSLATE_IRON_ORE) {
            guardianName = "§f§lHỘ VỆ QUẶNG SẮT MA THUẬT"; mobType = EntityType.CAVE_SPIDER; gType = "IRON"; mmMobId = "IronGuardian";
        } else if (type == Material.GOLD_ORE || type == Material.DEEPSLATE_GOLD_ORE) {
            guardianName = "§e§lHỘ VỆ QUẶNG VÀNG THƯỢNG HẠNG"; mobType = EntityType.PIGLIN; gType = "GOLD"; mmMobId = "GoldGuardian";
        } else if (type == Material.LAPIS_ORE || type == Material.DEEPSLATE_LAPIS_ORE) {
            guardianName = "§9§lHỘ VỆ QUẶNG LAPIS LINH THẠCH"; mobType = EntityType.SKELETON; gType = "LAPIS"; mmMobId = "LapisGuardian";
        } else if (type == Material.REDSTONE_ORE || type == Material.DEEPSLATE_REDSTONE_ORE) {
            guardianName = "§c§lHỘ VỆ QUẶNG REDSTONE HUYẾT LONG"; mobType = EntityType.SPIDER; gType = "REDSTONE"; mmMobId = "RedstoneGuardian";
        } else if (type == Material.COPPER_ORE || type == Material.DEEPSLATE_COPPER_ORE) {
            guardianName = "§4§lHỘ VỆ QUẶNG ĐỒNG THƯỢNG CỔ"; mobType = EntityType.HUSK; gType = "BRONZE"; mmMobId = "BronzeGuardian";
        }

        if (guardianName != null) {
            int avgLevel = 0; int count = 0;
            for (Entity e : loc.getWorld().getNearbyEntities(loc, 30, 30, 30)) {
                if (e instanceof Monster && !e.hasMetadata("cwe_guardian")) {
                    if (e.hasMetadata("leveledmobs:level")) { avgLevel += e.getMetadata("leveledmobs:level").get(0).asInt(); count++; }
                    else if (e.hasMetadata("lm-level")) { avgLevel += e.getMetadata("lm-level").get(0).asInt(); count++; }
                }
            }
            if (count > 0) avgLevel = avgLevel / count;
            if (avgLevel == 0) avgLevel = 40; 

            int baseNormalLevel = avgLevel + 20; // Default level is 60

            // Lấy cấp Mining của người chơi
            int miningLevel = 1;
            if (player != null && Bukkit.getPluginManager().isPluginEnabled("AuraSkills")) {
                try {
                    dev.aurelium.auraskills.api.user.SkillsUser u = dev.aurelium.auraskills.api.AuraSkillsApi.get().getUser(player.getUniqueId());
                    if (u != null) {
                        miningLevel = u.getSkillLevel(dev.aurelium.auraskills.api.skill.Skills.MINING);
                    }
                } catch (Throwable ignored) {}
            }

            int calculatedLevel = baseNormalLevel;
            double statMultiplier = 1.0;

            if (veinId != null) {
                if (player != null) {
                    calculatedLevel = 40 + (miningLevel - 10);
                    if (calculatedLevel < 40) calculatedLevel = 40;
                    if (calculatedLevel > 60) calculatedLevel = 60;
                    statMultiplier = (double) calculatedLevel / 60.0;
                } else {
                    calculatedLevel = 60;
                    statMultiplier = 1.0;
                }
            } else {
                if (player != null) {
                    double ratio = Math.min(1.0, (double) miningLevel / 60.0);
                    int minLevel = 20;
                    calculatedLevel = minLevel + (int) ((baseNormalLevel - minLevel) * ratio);
                    if (calculatedLevel < minLevel) calculatedLevel = minLevel;
                    if (calculatedLevel > baseNormalLevel) calculatedLevel = baseNormalLevel;
                    statMultiplier = 0.3 + 0.7 * ratio;
                }
            }

            LivingEntity guardian = null;
            boolean isMythicMob = false;

            try {
                BYPASS_SPAWN.set(true);
                if (Bukkit.getPluginManager().isPluginEnabled("MythicMobs") && !mmMobId.isEmpty()) {
                    try {
                        io.lumine.mythic.core.mobs.ActiveMob am = io.lumine.mythic.bukkit.MythicBukkit.inst().getMobManager().spawnMob(mmMobId, loc, calculatedLevel);
                        if (am != null && am.getEntity() != null) {
                            Entity e = am.getEntity().getBukkitEntity();
                            if (e instanceof LivingEntity) {
                                guardian = (LivingEntity) e;
                                isMythicMob = true;
                            }
                        }
                    } catch (Throwable t) {
                        plugin.getLogger().warning("Không thể spawn MythicMob " + mmMobId + " qua API, chuyển sang vanilla fallback: " + t.getMessage());
                    }
                }

                if (guardian == null) {
                    guardian = (LivingEntity) loc.getWorld().spawnEntity(loc, mobType);
                }
            } finally {
                BYPASS_SPAWN.set(false);
            }

            if (!isMythicMob) {
                double levelMultiplier = 1.0 + (calculatedLevel * 0.015); 
                double baseHp = (150.0 + (calculatedLevel * 100.0)) * statMultiplier;
                double baseDmg = (30.0 + (calculatedLevel * 5.0)) * statMultiplier;

                if (guardian.getAttribute(Attribute.GENERIC_MAX_HEALTH) != null) {
                    guardian.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(baseHp);
                    guardian.setHealth(baseHp);
                }
                if (guardian.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE) != null) {
                    guardian.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(baseDmg);
                }
                guardian.setCustomName(guardianName + " §7[Lv." + calculatedLevel + "]");
                guardian.setCustomNameVisible(true);
            }

            // Kiểm tra xem thực thể có hợp lệ không (có bị WorldGuard hay Peaceful hủy không)
            if (guardian == null || !guardian.isValid() || guardian.isDead()) {
                if (guardian != null) {
                    guardian.remove();
                }
                plugin.getLogger().warning(String.format("⚠ Không thể sinh Hộ Vệ tại X:%.1f Y:%.1f Z:%.1f. Thực thể không hợp lệ hoặc sự kiện sinh quái bị một plugin khác (như WorldGuard) hủy bỏ!", loc.getX(), loc.getY(), loc.getZ()));
                return false;
            }

            guardian.setMetadata("cwe_guardian", new FixedMetadataValue(plugin, true));
            guardian.addScoreboardTag("cwe_guardian");
            guardian.setMetadata("cwe_guardian_type", new FixedMetadataValue(plugin, gType));
            if (veinId != null) {
                guardian.setMetadata("cwe_guardian_vein", new FixedMetadataValue(plugin, veinId));
            }
            guardian.setMetadata("leveledmobs:level", new FixedMetadataValue(plugin, calculatedLevel));

            if (player != null) {
                if (guardian instanceof Mob) {
                    ((Mob) guardian).setTarget(player);
                }
                player.sendMessage("§c⚠️ Long mạch chấn động! Bạn đã làm thức tỉnh " + guardianName + "!");
                if (veinId != null) {
                    if (calculatedLevel < 60) {
                        player.sendMessage(String.format("§e[Mỏ Quặng] Phát hiện cấp Mining của bạn là §b%d§e. Hộ Vệ quặng giảm xuống §aLv.%d§e, giảm sát thương và tỉ lệ rơi đồ để bảo vệ bạn!", miningLevel, calculatedLevel));
                    }
                } else {
                    if (calculatedLevel < baseNormalLevel) {
                        player.sendMessage(String.format("§a[Hộ Vệ] Phát hiện cấp Mining của bạn là §b%d§a. Hộ Vệ quặng tự động giảm sức mạnh xuống §eLv.%d§a để bảo vệ tân thủ!", miningLevel, calculatedLevel));
                    }
                }
                player.playSound(player.getLocation(), org.bukkit.Sound.ENTITY_ENDER_DRAGON_GROWL, 0.5f, 1.3f);
            }
            return true;
        }
        return false;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onGuardianPassiveTrigger(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof LivingEntity)) return;
        LivingEntity entity = (LivingEntity) event.getEntity();
        if (!entity.hasMetadata("cwe_guardian") || entity.hasMetadata("cwe_passive_active")) return;

        double currentHp = entity.getHealth() - event.getFinalDamage();
        double maxHp = entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();

        if (currentHp > 0 && currentHp <= (maxHp * 0.35)) {
            entity.setMetadata("cwe_passive_active", new FixedMetadataValue(plugin, true));
            String gType = entity.getMetadata("cwe_guardian_type").get(0).asString();
            Location loc = entity.getLocation();

            if (gType.equals("DIAMOND")) { 
                PotionEffectType strType = getPotionType("STRENGTH", "INCREASE_DAMAGE");
                PotionEffectType speedType = PotionEffectType.getByName("SPEED");
                if (strType != null) entity.addPotionEffect(new PotionEffect(strType, 300, 1));
                if (speedType != null) entity.addPotionEffect(new PotionEffect(speedType, 300, 2));
                loc.getWorld().spawnParticle(org.bukkit.Particle.ANGRY_VILLAGER, loc.add(0, 1, 0), 10);
                if (entity.getCustomName() != null) entity.setCustomName("§4§l[CUỒNG CHIẾN] " + entity.getCustomName());
            } 
            else if (gType.equals("LAPIS")) { 
                for (int i = 0; i < 2; i++) {
                    Skeleton minion = (Skeleton) loc.getWorld().spawnEntity(loc.add(random.nextDouble(), 0, random.nextDouble()), EntityType.SKELETON);
                    if (minion.getAttribute(Attribute.GENERIC_MAX_HEALTH) != null) {
                        minion.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(maxHp * 0.20);
                        minion.setHealth(maxHp * 0.20);
                    }
                    minion.setCustomName("§7Đệ Tử Hộ Vệ");
                    minion.setCustomNameVisible(true);
                }
                loc.getWorld().spawnParticle(org.bukkit.Particle.EXPLOSION_EMITTER, loc, 3);
            }
            else if (gType.equals("BRONZE")) { 
                PotionEffectType resType = getPotionType("RESISTANCE", "DAMAGE_RESISTANCE");
                PotionEffectType regenType = PotionEffectType.getByName("REGENERATION");
                if (resType != null) entity.addPotionEffect(new PotionEffect(resType, 200, 1));
                if (regenType != null) entity.addPotionEffect(new PotionEffect(regenType, 200, 1));
            }
        }
    }

    @EventHandler
    public void onGuardianAttackPlayer(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof LivingEntity) || !(event.getEntity() instanceof Player)) return;
        LivingEntity guardian = (LivingEntity) event.getDamager();
        Player player = (Player) event.getEntity();

        if (!guardian.hasMetadata("cwe_guardian")) return;
        String gType = guardian.getMetadata("cwe_guardian_type").get(0).asString();

        if (gType.equals("GOLD")) {
            PotionEffectType blind = PotionEffectType.getByName("BLINDNESS");
            PotionEffectType nausea = getPotionType("NAUSEA", "CONFUSION");
            if (blind != null) player.addPotionEffect(new PotionEffect(blind, 100, 0));
            if (nausea != null) player.addPotionEffect(new PotionEffect(nausea, 100, 1));
            player.sendMessage("§c💫 Bạn bị Hộ Vệ Vàng vả mù mắt và chóng mặt trong 5 giây!");
        } 
        else if ((gType.equals("IRON") || gType.equals("REDSTONE")) && guardian.hasMetadata("cwe_passive_active")) {
            PotionEffectType poison = PotionEffectType.getByName("POISON");
            if (poison != null) player.addPotionEffect(new PotionEffect(poison, 100, 1));
            player.sendMessage("§c🤢 Độc tính bộc phát! Bạn dính độc Poison II từ tộc Nhện Long Mạch!");
        }
    }

    private void handleGuardianDrop(LivingEntity guardian, EntityDeathEvent event) {
        Player killer = guardian.getKiller();
        if (killer == null) return;

        ItemStack weapon = killer.getInventory().getItemInMainHand();
        int lootingLvl = 0;
        
        // 🛡️ CHIẾN THUẬT QUÉT ĐỒNG BỘ: Không gọi trực tiếp hằng số biến bùa Looting để chặn đứng lỗi biên dịch
        if (weapon != null && weapon.hasItemMeta()) {
            for (java.util.Map.Entry<org.bukkit.enchantments.Enchantment, Integer> entry : weapon.getEnchantments().entrySet()) {
                String name = entry.getKey().getName();
                String keyStr = "";
                try { keyStr = entry.getKey().getKey().getKey(); } catch (Throwable ignored) {}
                
                if (name.equalsIgnoreCase("LOOT_BONUS_MOBS") || name.equalsIgnoreCase("LOOTING") || keyStr.equalsIgnoreCase("looting")) {
                    lootingLvl = entry.getValue();
                    break;
                }
            }
        }

        int effectiveLooting = Math.min(5, lootingLvl);
        killer.sendMessage("§7[Engine] Phát hiện cấp bùa Looting thực tế: §e" + lootingLvl + " §7(Kích hoạt hiệu ứng mốc: §b" + effectiveLooting + "§7)");

        String gType = guardian.getMetadata("cwe_guardian_type").get(0).asString();
        Material regMat = Material.AIR;
        String bzId = ""; String displayName = "";

        switch (gType) {
            case "DIAMOND": regMat = Material.DIAMOND; bzId = "ENCHANTED_DIAMOND"; displayName = "§9Enchanted Diamond"; break;
            case "IRON": regMat = Material.IRON_INGOT; bzId = "ENCHANTED_IRON"; displayName = "§9Enchanted Iron Ingot"; break;
            case "GOLD": regMat = Material.GOLD_INGOT; bzId = "ENCHANTED_GOLD"; displayName = "§9Enchanted Gold Ingot"; break;
            case "LAPIS": regMat = Material.LAPIS_LAZULI; bzId = "ENCHANTED_LAPIS"; displayName = "§9Enchanted Lapis Lazuli"; break;
            case "REDSTONE": regMat = Material.REDSTONE; bzId = "ENCHANTED_REDSTONE"; displayName = "§9Enchanted Redstone"; break;
            case "BRONZE": regMat = Material.COPPER_INGOT; bzId = "ENCHANTED_COPPER"; displayName = "§aEnchanted Copper Ingot"; break;
        }

        if (regMat == Material.AIR) return;
        event.getDrops().clear(); 

        // Lấy cấp của hộ vệ lúc bị hạ gục từ metadata để điều chỉnh drop rate
        int guardianLevel = 60;
        if (guardian.hasMetadata("leveledmobs:level")) {
            guardianLevel = guardian.getMetadata("leveledmobs:level").get(0).asInt();
        }

        // Tỉ lệ drop sẽ tỉ lệ thuận với level hộ vệ. Khi level = 60 thì drop rate đạt 100% như cũ.
        double dropMult = 1.0;
        if (guardianLevel < 60) {
            dropMult = (double) guardianLevel / 60.0;
        }

        int maxOre = Math.min(32, 10 + (effectiveLooting * 4));
        int totalRegDrops = 5 + random.nextInt(Math.max(1, maxOre - 5 + 1));
        
        // Áp dụng drop multiplier cho số lượng quặng thường
        int finalRegDrops = (int) Math.max(1, totalRegDrops * dropMult);
        event.getDrops().add(new ItemStack(regMat, finalRegDrops));

        double dropChance = 0.10 + (effectiveLooting * 0.15);
        // Áp dụng drop multiplier cho tỉ lệ ra quặng nén ma thuật (Bazaar Item)
        double finalDropChance = dropChance * dropMult;

        if (random.nextDouble() <= finalDropChance) {
            int enchantedAmount = 1;
            if (effectiveLooting >= 5 && random.nextDouble() < 0.5) {
                enchantedAmount = 2;
            }
            event.getDrops().add(createEnchantedItem(regMat, displayName, bzId, enchantedAmount));
        }

        if (guardianLevel < 60) {
            killer.sendMessage(String.format("§e[Hộ Vệ] Sức mạnh Hộ Vệ thấp (Lv.%d) khiến tỉ lệ drop chỉ đạt %.0f%% so với mốc tối đa!", guardianLevel, dropMult * 100));
        }
    }

    private ItemStack createEnchantedItem(Material mat, String displayName, String bazaarId, int amount) {
        ItemStack item = new ItemStack(mat, amount);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(displayName);
            meta.setLore(Arrays.asList("§7Vật phẩm ma thuật nén cao cấp", "§7chuyên dụng giao dịch tại Chợ Bazaar.", "", "§cKhông thể ăn hay sử dụng thô!"));
            NamespacedKey key = new NamespacedKey(plugin, "bazaar_id");
            meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, bazaarId);
            try {
                meta.addEnchant(org.bukkit.enchantments.Enchantment.getByKey(NamespacedKey.minecraft("luck_of_the_sea")), 1, true);
            } catch(Throwable ignored){}
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            item.setItemMeta(meta);
        }
        return item;
    }
}

package org.example.stats;

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
import dev.aurelium.auraskills.api.stat.StatModifier;
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
import dev.aurelium.auraskills.api.user.SkillsUser;
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
import org.bukkit.event.inventory.InventoryClickEvent;
import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
import org.bukkit.event.player.PlayerItemHeldEvent;
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

/**
 * ⚡ Item Stats Listener — Áp dụng chỉ số RPG thực tế lên người chơi
 *
 * Bắt các sự kiện thay đổi trang bị (giáp + tay chính) và:
 *  - Cộng/Trừ AuraSkills StatModifiers cho Strength, CritChance, CritDamage, Defense, Intelligence
 *  - Cộng/Trừ Bukkit Attribute cho Health (GENERIC_MAX_HEALTH)
 *  - Cộng/Trừ WalkSpeed cho Speed
 *
 * Sử dụng cache để chống re-apply spam và đảm bảo cleanup khi logout.
 */
import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
public class ItemStatsListener implements Listener {

    private final JavaPlugin plugin;

    // Cache tổng chỉ số đang được apply cho mỗi player
    // key: UUID, value: double[8] = {strength, critChance, critDamage, health, defense, intelligence, speed, attackSpeed}
    private final Map<UUID, double[]> appliedCache = new HashMap<>();

    // PDC keys prefix (phải khớp với ItemStatsGUI)
    private static final String[] PDC_KEYS = {
        ItemStatsGUI.KEY_STRENGTH,
        ItemStatsGUI.KEY_CRIT_CHANCE,
        ItemStatsGUI.KEY_CRIT_DAMAGE,
        ItemStatsGUI.KEY_HEALTH,
        ItemStatsGUI.KEY_DEFENSE,
        ItemStatsGUI.KEY_INTELLIGENCE,
        ItemStatsGUI.KEY_SPEED
    };

    // AuraSkills modifier name templates (per slot)
    private static final String MOD_PREFIX = "cwe_item_";

    public ItemStatsListener(JavaPlugin plugin) {
        this.plugin = plugin;

        // Periodic resync every 40 ticks (2 seconds) để đảm bảo consistency
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                fullResync(player);
            }
        }, 40L, 40L);
    }

    // ─── EVENTS ───────────────────────────────────────────────────────────────

    @EventHandler(priority = EventPriority.MONITOR)
    public void onJoin(PlayerJoinEvent event) {
        Bukkit.getScheduler().runTaskLater(plugin, () -> fullResync(event.getPlayer()), 10L);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        removeAllModifiers(player);
        appliedCache.remove(player.getUniqueId());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onRespawn(org.bukkit.event.player.PlayerRespawnEvent event) {
        Bukkit.getScheduler().runTaskLater(plugin, () -> fullResync(event.getPlayer()), 1L);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onArmorChange(com.destroystokyo.paper.event.player.PlayerArmorChangeEvent event) {
        fullResync(event.getPlayer());
    }

    /**
     * Bắt sự kiện đổi slot tay chính (1-9 keys).
     */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onItemHeld(PlayerItemHeldEvent event) {
        Bukkit.getScheduler().runTask(plugin, () -> fullResync(event.getPlayer()));
    }

    /**
     * Bắt sự kiện click vào inventory (equip/unequip armor).
     */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        Player player = (Player) event.getWhoClicked();
        Bukkit.getScheduler().runTask(plugin, () -> fullResync(player));
    }

    /**
     * Bắt sự kiện right-click (equip armor bằng right-click).
     */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        // Chỉ re-sync nếu người chơi right-click với item có thể là giáp
        ItemStack item = event.getItem();
        if (item == null) return;
        String name = item.getType().name();
        if (name.contains("HELMET") || name.contains("CHESTPLATE")
                || name.contains("LEGGINGS") || name.contains("BOOTS") || name.contains("ELYTRA")) {
            Bukkit.getScheduler().runTaskLater(plugin, () -> fullResync(player), 1L);
        }
    }

    // ─── FULL RESYNC ──────────────────────────────────────────────────────────

    /**
     * Tính toán lại tổng chỉ số từ tất cả armor slots + tay chính
     * và cập nhật modifiers nếu có thay đổi.
     */
    public void fullResync(Player player) {
        double[] totals = collectTotals(player);
        double[] cached  = appliedCache.getOrDefault(player.getUniqueId(), null);

        // So sánh với cache — nếu không đổi thì bỏ qua
        if (cached != null && arraysEqual(totals, cached)) return;

        // ── Lưu tỷ lệ mana TRƯỚC KHI apply (để giữ % mana sau khi max mana thay đổi) ──
        boolean auraOk = Bukkit.getPluginManager().isPluginEnabled("AuraSkills");
        double manaRatio = -1;
        if (auraOk) {
            dev.aurelium.auraskills.api.user.SkillsUser u =
                    dev.aurelium.auraskills.api.AuraSkillsApi.get().getUser(player.getUniqueId());
            if (u != null && u.getMaxMana() > 0) {
                manaRatio = u.getMana() / u.getMaxMana();
            }
        }

        // Apply modifiers
        applyModifiers(player, totals);
        appliedCache.put(player.getUniqueId(), totals.clone());

        // ── Khôi phục mana theo tỷ lệ CŨ để tránh thay đổi thanh mana đột ngột ──
        if (auraOk && manaRatio >= 0) {
            final double ratio = manaRatio;
            // Delay 1 tick để AuraSkills cập nhật xong maxMana trước khi set mana
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                dev.aurelium.auraskills.api.user.SkillsUser u2 =
                        dev.aurelium.auraskills.api.AuraSkillsApi.get().getUser(player.getUniqueId());
                if (u2 != null && u2.getMaxMana() > 0) {
                    double newMana = u2.getMaxMana() * ratio;
                    // Clamp trong khoảng [0, maxMana]
                    newMana = Math.max(0, Math.min(newMana, u2.getMaxMana()));
                    u2.setMana(newMana);
                }
            }, 1L);
        }
    }


    /**
     * Quét tất cả 4 armor slots + tay chính, cộng dồn các chỉ số từ PDC.
     * Ràng buộc slot: Armor chỉ đọc từ armor slots; mainhand không tính armor stats.
     */
    private double[] collectTotals(Player player) {
        double[] totals = new double[8];

        // Helmet — chỉ đọc nếu thực sự đang đội
        ItemStack helmet = player.getInventory().getHelmet();
        if (helmet != null && helmet.getType().name().contains("HELMET")) {
            addStatsFromItem(helmet, totals);
        }

        // Chestplate — chỉ đọc nếu thực sự đang mặc
        ItemStack chest = player.getInventory().getChestplate();
        if (chest != null && (chest.getType().name().contains("CHESTPLATE") || chest.getType().name().contains("ELYTRA"))) {
            addStatsFromItem(chest, totals);
        }

        // Leggings
        ItemStack legs = player.getInventory().getLeggings();
        if (legs != null && legs.getType().name().contains("LEGGINGS")) {
            addStatsFromItem(legs, totals);
        }

        // Boots
        ItemStack boots = player.getInventory().getBoots();
        if (boots != null && boots.getType().name().contains("BOOTS")) {
            addStatsFromItem(boots, totals);
        }

        // Main hand — TUYỆT ĐỐI không cộng stats giáp khi chỉ cầm tay
        ItemStack mainHand = player.getInventory().getItemInMainHand();
        if (mainHand != null && !mainHand.getType().isAir()) {
            String name = mainHand.getType().name();
            boolean isArmorItem = name.contains("HELMET") || name.contains("CHESTPLATE")
                    || name.contains("LEGGINGS") || name.contains("BOOTS") || name.contains("ELYTRA");
            if (!isArmorItem) {
                double[] mainHandTotals = new double[8];
                addStatsFromItem(mainHand, mainHandTotals);

                // WITHERED: cộng thêm levelMultiplier * player.getLevel() vào strength
                if (mainHand.hasItemMeta()) {
                    PersistentDataContainer pdc = mainHand.getItemMeta().getPersistentDataContainer();
                    if (pdc.has(new NamespacedKey(plugin, "cwe_reforge"), PersistentDataType.STRING)) {
                        String prefix = pdc.get(new NamespacedKey(plugin, "cwe_reforge"), PersistentDataType.STRING);
                        String rarityStr = pdc.has(new NamespacedKey(plugin, ItemStatsGUI.KEY_RARITY), PersistentDataType.STRING)
                                ? pdc.get(new NamespacedKey(plugin, ItemStatsGUI.KEY_RARITY), PersistentDataType.STRING) : "COMMON";
                        org.example.enchant.ReforgeSystem.ReforgeTier t;
                        try { t = org.example.enchant.ReforgeSystem.ReforgeTier.valueOf(rarityStr); }
                        catch (Exception e) { t = org.example.enchant.ReforgeSystem.ReforgeTier.COMMON; }
                        org.example.enchant.ReforgeSystem.ItemCategory cat = getCategoryForReforge(mainHand);
                        org.example.enchant.ReforgeSystem.ReforgeStat bonus =
                                org.example.enchant.ReforgeSystem.getReforgeStat(prefix, cat, t);
                        if (bonus != null && bonus.levelMultiplier > 0) {
                            mainHandTotals[0] += bonus.levelMultiplier * player.getLevel();
                        }
                    }
                }

                for (int i = 0; i < 8; i++) totals[i] += mainHandTotals[i];
            }
        }
        
        // --- DRAGON ARMOR SET BONUSES ---
        String h = getArmorIdStr(player.getInventory().getHelmet());
        String c = getArmorIdStr(player.getInventory().getChestplate());
        String l = getArmorIdStr(player.getInventory().getLeggings());
        String b = getArmorIdStr(player.getInventory().getBoots());

        if (h != null && c != null && l != null && b != null) {
            // Superior Dragon: +5% to all stats
            if (h.equals("cwe_superior_helmet") && c.equals("cwe_superior_chestplate") && l.equals("cwe_superior_leggings") && b.equals("cwe_superior_boots")) {
                for (int i = 0; i < 8; i++) totals[i] *= 1.05;
            }
            // Protector Dragon: +Defense based on missing HP
            else if (h.equals("cwe_protector_helmet") && c.equals("cwe_protector_chestplate") && l.equals("cwe_protector_leggings") && b.equals("cwe_protector_boots")) {
                org.bukkit.attribute.AttributeInstance maxHpAttr = player.getAttribute(org.bukkit.attribute.Attribute.GENERIC_MAX_HEALTH);
                if (maxHpAttr != null) {
                    double missingHpPercent = 1.0 - (player.getHealth() / maxHpAttr.getValue());
                    if (missingHpPercent > 0) {
                        totals[4] *= (1.0 + missingHpPercent); // 4 is Defense
                    }
                }
            }
            // Young Dragon: +70 Speed when HP > 50%
            else if (h.equals("cwe_young_helmet") && c.equals("cwe_young_chestplate") && l.equals("cwe_young_leggings") && b.equals("cwe_young_boots")) {
                org.bukkit.attribute.AttributeInstance maxHpAttr = player.getAttribute(org.bukkit.attribute.Attribute.GENERIC_MAX_HEALTH);
                if (maxHpAttr != null) {
                    double hpPercent = player.getHealth() / maxHpAttr.getValue();
                    if (hpPercent >= 0.5) {
                        totals[6] += 70; // 6 is Speed
                    }
                }
            }
            // Strong Dragon: Buff AOTE (+75 Strength)
            else if (h.equals("cwe_strong_helmet") && c.equals("cwe_strong_chestplate") && l.equals("cwe_strong_leggings") && b.equals("cwe_strong_boots")) {
                ItemStack mh = player.getInventory().getItemInMainHand();
                if (mh != null && mh.hasItemMeta()) {
                    String wepId = mh.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(plugin, "cwe_id"), PersistentDataType.STRING);
                    if ("cwe_aote".equalsIgnoreCase(wepId)) {
                        totals[0] += 75; // +75 Strength if holding AOTE and wearing Strong Dragon
                    }
                }
            }
        }

        return totals;
    }
    
    private String getArmorIdStr(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return null;
        return item.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(plugin, "cwe_id"), PersistentDataType.STRING);
    }

    private void addStatsFromItem(ItemStack item, double[] totals) {
        if (item == null || !item.hasItemMeta()) return;
        PersistentDataContainer pdc = item.getItemMeta().getPersistentDataContainer();

        boolean hasCustomStats = pdc.has(new NamespacedKey(plugin, ItemStatsGUI.KEY_HAS_STATS), PersistentDataType.INTEGER);
        boolean hasReforge     = pdc.has(new NamespacedKey(plugin, "cwe_reforge"), PersistentDataType.STRING);

        double[] baseStats = new double[8];

        // Cộng base custom stats (nếu có)
        if (hasCustomStats) {
            for (int i = 0; i < PDC_KEYS.length; i++) {
                NamespacedKey k = new NamespacedKey(plugin, PDC_KEYS[i]);
                if (pdc.has(k, PersistentDataType.DOUBLE)) {
                    Double val = pdc.get(k, PersistentDataType.DOUBLE);
                    if (val != null) baseStats[i] += val;
                }
            }
        }

        // Cộng reforge bonus (cả custom lẫn vanilla đều đọc được)
        if (hasReforge) {
            String prefix = pdc.get(new NamespacedKey(plugin, "cwe_reforge"), PersistentDataType.STRING);
            String rarityStr = pdc.has(new NamespacedKey(plugin, ItemStatsGUI.KEY_RARITY), PersistentDataType.STRING)
                    ? pdc.get(new NamespacedKey(plugin, ItemStatsGUI.KEY_RARITY), PersistentDataType.STRING)
                    : "COMMON"; // Vanilla fallback
            org.example.enchant.ReforgeSystem.ReforgeTier tier;
            try { tier = org.example.enchant.ReforgeSystem.ReforgeTier.valueOf(rarityStr); }
            catch (Exception e) { tier = org.example.enchant.ReforgeSystem.ReforgeTier.COMMON; }

            org.example.enchant.ReforgeSystem.ItemCategory cat = getCategoryForReforge(item);
            org.example.enchant.ReforgeSystem.ReforgeStat bonus =
                    org.example.enchant.ReforgeSystem.getReforgeStat(prefix, cat, tier);

            if (bonus != null) {
                // Apply baseStatMultiplier to base stats first
                if (bonus.baseStatMultiplier > 1.0) {
                    for (int i = 0; i < 8; i++) {
                        baseStats[i] *= bonus.baseStatMultiplier;
                    }
                }

                totals[0] += baseStats[0] + bonus.strength;
                totals[1] += baseStats[1] + bonus.critChance;
                totals[2] += baseStats[2] + bonus.critDamage;
                totals[3] += baseStats[3] + bonus.health;
                totals[4] += baseStats[4] + bonus.defense;
                totals[5] += baseStats[5] + bonus.intelligence;
                totals[6] += baseStats[6] + bonus.speed;
                totals[7] += baseStats[7] + bonus.attackSpeed;
            } else {
                for (int i = 0; i < 8; i++) totals[i] += baseStats[i];
            }
        } else {
            for (int i = 0; i < 8; i++) totals[i] += baseStats[i];
        }
    }

    private org.example.enchant.ReforgeSystem.ItemCategory getCategoryForReforge(ItemStack item) {
        String name = item.getType().name();
        if (name.contains("BOW") || name.contains("CROSSBOW")) return org.example.enchant.ReforgeSystem.ItemCategory.RANGED;
        if (name.contains("HELMET") || name.contains("CHESTPLATE") || name.contains("LEGGINGS") || name.contains("BOOTS")) return org.example.enchant.ReforgeSystem.ItemCategory.ARMOR;
        if (name.contains("PICKAXE") || name.contains("HOE") || name.contains("SPADE") || name.contains("SHOVEL")) return org.example.enchant.ReforgeSystem.ItemCategory.TOOLS;
        return org.example.enchant.ReforgeSystem.ItemCategory.MELEE;
    }

    // ─── APPLY / REMOVE MODIFIERS ─────────────────────────────────────────────

    /**
     * Áp dụng tất cả chỉ số vào người chơi.
     * idx: 0=strength, 1=critChance, 2=critDamage, 3=health, 4=defense, 5=intel, 6=speed
     */
    private void applyModifiers(Player player, double[] totals) {
        boolean auraEnabled = Bukkit.getPluginManager().isPluginEnabled("AuraSkills");
        SkillsUser user = null;
        if (auraEnabled) {
            user = AuraSkillsApi.get().getUser(player.getUniqueId());
        }

        // ── AuraSkills: Strength ──
        updateAuraModifier(user, "cwe_item_strength",     Stats.STRENGTH,    totals[0]);

        // ── AuraSkills: Crit Chance ──
        updateAuraModifier(user, "cwe_item_crit_chance",  Stats.CRIT_CHANCE, totals[1]);

        // ── AuraSkills: Crit Damage ──
        updateAuraModifier(user, "cwe_item_crit_damage",  Stats.CRIT_DAMAGE, totals[2]);

        // ── Health ──
        if (auraEnabled && user != null) {
            updateAuraModifier(user, "cwe_item_health", Stats.HEALTH, totals[3]);
            applyHealth(player, 0); // Clear bukkit fallback
        } else {
            applyHealth(player, totals[3]);
        }

        // ── AuraSkills: Defense (Toughness) ──
        updateAuraModifier(user, "cwe_item_defense",      Stats.TOUGHNESS,   totals[4]);

        // ── AuraSkills: Intelligence (Wisdom) ──
        updateAuraModifier(user, "cwe_item_intelligence", Stats.WISDOM,       totals[5]);

        // ── Speed ──
        if (auraEnabled && user != null) {
            updateAuraModifier(user, "cwe_item_speed", Stats.SPEED, totals[6]);
            applySpeed(player, 0); // Clear bukkit fallback
        } else {
            applySpeed(player, totals[6]);
        }

        // ── Attack Speed ──
        applyAttackSpeed(player, totals[7]);
    }

    private void updateAuraModifier(SkillsUser user, String modKey, dev.aurelium.auraskills.api.stat.Stat stat, double value) {
        if (user == null) return;

        // Remove old modifier
        if (user.getStatModifier(modKey) != null) {
            user.removeStatModifier(modKey);
        }

        // Add new modifier if > 0
        if (value > 0) {
            user.addStatModifier(new StatModifier(modKey, stat, value, Operation.ADD));
        }
    }

    private void applyHealth(Player player, double healthBonus) {
        AttributeInstance attr = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if (attr == null) return;
        NamespacedKey statKeyMod = new NamespacedKey(plugin, "cwe_stat_health");
        
        // Quét và dọn sạch mọi modifier rác kẹt lại do crash server
        for (org.bukkit.attribute.AttributeModifier mod : new java.util.ArrayList<>(attr.getModifiers())) {
            if (mod.getName().startsWith("cwe_")) {
                attr.removeModifier(mod);
            }
        }
        
        if (healthBonus > 0) {
            attr.addModifier(new org.bukkit.attribute.AttributeModifier(
                statKeyMod,
                healthBonus,
                org.bukkit.attribute.AttributeModifier.Operation.ADD_NUMBER
            ));
        }
    }

    private void applySpeed(Player player, double speedBonus) {
        AttributeInstance speedAttr = player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED);
        if (speedAttr == null) return;
        NamespacedKey statSpeedModKey = new NamespacedKey(plugin, "cwe_stat_speed");
        
        // Dọn sạch rác speed
        for (org.bukkit.attribute.AttributeModifier mod : new java.util.ArrayList<>(speedAttr.getModifiers())) {
            if (mod.getName().startsWith("cwe_")) {
                speedAttr.removeModifier(mod);
            }
        }

        if (speedBonus > 0) {
            speedAttr.addModifier(new org.bukkit.attribute.AttributeModifier(
                statSpeedModKey,
                speedBonus * 0.001,
                org.bukkit.attribute.AttributeModifier.Operation.ADD_NUMBER
            ));
        }
    }

    private void applyAttackSpeed(Player player, double attackSpeedBonus) {
        AttributeInstance atkAttr = player.getAttribute(Attribute.GENERIC_ATTACK_SPEED);
        if (atkAttr == null) return;
        NamespacedKey statAtkModKey = new NamespacedKey(plugin, "cwe_stat_atk_spd");
        atkAttr.removeModifier(statAtkModKey);
        if (attackSpeedBonus > 0) {
            atkAttr.addModifier(new org.bukkit.attribute.AttributeModifier(
                statAtkModKey,
                attackSpeedBonus * 0.01,
                org.bukkit.attribute.AttributeModifier.Operation.ADD_SCALAR
            ));
        }
    }

    /**
     * Xóa tất cả modifiers của player (gọi khi logout hoặc plugin disable).
     */
    private void removeAllModifiers(Player player) {
        boolean auraEnabled = Bukkit.getPluginManager().isPluginEnabled("AuraSkills");
        if (auraEnabled) {
            SkillsUser user = AuraSkillsApi.get().getUser(player.getUniqueId());
            if (user != null) {
                user.removeStatModifier("cwe_item_strength");
                user.removeStatModifier("cwe_item_crit_chance");
                user.removeStatModifier("cwe_item_crit_damage");
                user.removeStatModifier("cwe_item_defense");
                user.removeStatModifier("cwe_item_intelligence");
                user.removeStatModifier("cwe_item_health");
                user.removeStatModifier("cwe_item_speed");
            }
        }

        // Reset health modifier mạnh tay
        AttributeInstance attr = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if (attr != null) {
            for (org.bukkit.attribute.AttributeModifier mod : new java.util.ArrayList<>(attr.getModifiers())) {
                if (mod.getName().startsWith("cwe_")) attr.removeModifier(mod);
            }
        }
        // Reset speed modifier mạnh tay
        AttributeInstance speedAttr = player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED);
        if (speedAttr != null) {
            for (org.bukkit.attribute.AttributeModifier mod : new java.util.ArrayList<>(speedAttr.getModifiers())) {
                if (mod.getName().startsWith("cwe_")) speedAttr.removeModifier(mod);
            }
        }
        // Reset attack speed modifier
        AttributeInstance atkAttr = player.getAttribute(Attribute.GENERIC_ATTACK_SPEED);
        if (atkAttr != null) {
            atkAttr.removeModifier(new NamespacedKey(plugin, "cwe_stat_atk_spd"));
        }
    }

    // ─── UTILS ────────────────────────────────────────────────────────────────

    private boolean arraysEqual(double[] a, double[] b) {
        if (a.length != b.length) return false;
        for (int i = 0; i < a.length; i++) {
            if (Math.abs(a[i] - b[i]) > 0.001) return false;
        }
        return true;
    }

    /**
     * Cho phép các class khác gọi cleanup (ví dụ: khi item bị tháo ra bởi admin)
     */
    public void clearCache(UUID uuid) {
        appliedCache.remove(uuid);
    }
}


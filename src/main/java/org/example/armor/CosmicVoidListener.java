package org.example.armor;

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
import dev.aurelium.auraskills.api.user.SkillsUser;
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
import org.bukkit.event.Listener;
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
import org.bukkit.util.Vector;

import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
import java.util.ArrayList;
import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
import java.util.List;
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
public class CosmicVoidListener implements Listener {
    private final JavaPlugin plugin;
    private final NamespacedKey cweIdKey;

    public CosmicVoidListener(JavaPlugin plugin) {
        this.plugin = plugin;
        this.cweIdKey = new NamespacedKey(plugin, "cwe_id");

        startManaMonitorTask();
    }

    private void startManaMonitorTask() {
        // Chạy ngầm mỗi 10 ticks (0.5s) để check lượng Mana của người chơi
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (hasFullCosmicVoidSet(player)) {
                    checkVoidGravity(player);
                }
            }
        }, 10L, 10L);
    }

    private boolean hasFullCosmicVoidSet(Player player) {
        ItemStack helmet = player.getInventory().getHelmet();
        ItemStack chestplate = player.getInventory().getChestplate();
        ItemStack leggings = player.getInventory().getLeggings();
        ItemStack boots = player.getInventory().getBoots();

        if (helmet == null || chestplate == null || leggings == null || boots == null) return false;

        return isCosmicVoidPart(helmet, "cosmic_void_helmet") &&
               isCosmicVoidPart(chestplate, "cosmic_void_chestplate") &&
               isCosmicVoidPart(leggings, "cosmic_void_leggings") &&
               isCosmicVoidPart(boots, "cosmic_void_boots");
    }

    private boolean isCosmicVoidPart(ItemStack item, String expectedId) {
        if (!item.hasItemMeta()) return false;
        PersistentDataContainer container = item.getItemMeta().getPersistentDataContainer();
        if (container.has(cweIdKey, PersistentDataType.STRING)) {
            String id = container.get(cweIdKey, PersistentDataType.STRING);
            return expectedId.equalsIgnoreCase(id);
        }
        return false;
    }

    private void checkVoidGravity(Player player) {
        // Kiểm tra Cooldown ngầm 45 giây
        if (player.hasMetadata("cwe_void_gravity_cooldown")) {
            long cooldownEnd = player.getMetadata("cwe_void_gravity_cooldown").get(0).asLong();
            if (System.currentTimeMillis() < cooldownEnd) {
                return; // Đang trong thời gian chờ
            }
        }

        // Kiểm tra Mana từ AuraSkills
        SkillsUser user = AuraSkillsApi.get().getUser(player.getUniqueId());
        if (user == null) return;

        double maxMana = user.getMaxMana();
        double currentMana = user.getMana();

        if (maxMana > 0 && currentMana < (maxMana * 0.5)) {
            triggerVoidGravity(player);
        }
    }

    private void triggerVoidGravity(Player player) {
        // Đặt cooldown 45s
        long cooldownEnd = System.currentTimeMillis() + (45 * 1000L);
        player.setMetadata("cwe_void_gravity_cooldown", new FixedMetadataValue(plugin, cooldownEnd));

        Location center = player.getLocation().add(0, 2, 0); // Vị trí treo lơ lửng

        // Quét quái vật (Monster) trong bán kính 5 ô
        List<Monster> caughtMonsters = new ArrayList<>();
        for (Entity e : player.getNearbyEntities(5, 5, 5)) {
            if (e instanceof Monster && !e.isDead() && e.isValid()) {
                caughtMonsters.add((Monster) e);
            }
        }

        if (caughtMonsters.isEmpty()) {
            return; // Nếu không có quái xung quanh thì skill không cần làm gì thêm nhưng vẫn tính cooldown
        }

        player.sendMessage("§5§l[COSMIC VOID] §d§oMana cạn kiệt... Vực Thẳm Không Gian thức tỉnh!");
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1.0f, 0.5f);
        player.getWorld().playSound(player.getLocation(), Sound.BLOCK_PORTAL_TRAVEL, 1.0f, 2.0f);

        // Hút quái và treo bổng lên
        for (Monster monster : caughtMonsters) {
            // Dùng vector để kéo chúng về center
            Vector pull = center.toVector().subtract(monster.getLocation().toVector());
            monster.setVelocity(pull.normalize().multiply(1.5));
            monster.setGravity(false); // Vô hiệu hóa trọng lực
            
            // Đánh dấu để tránh trùng lặp nếu chạy skill khác
            monster.setMetadata("cwe_void_gravity_affected", new FixedMetadataValue(plugin, true));
        }

        // Tạo Particle hút không gian
        player.getWorld().spawnParticle(Particle.PORTAL, player.getLocation(), 100, 3, 3, 3, 0.5);

        // Đếm lùi 3 giây (60 ticks) để thả quái xuống
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            for (Monster monster : caughtMonsters) {
                if (monster.isValid() && !monster.isDead()) {
                    monster.setGravity(true); // Trả lại trọng lực
                    monster.removeMetadata("cwe_void_gravity_affected", plugin);
                }
            }
        }, 60L);
    }
}


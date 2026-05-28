package org.example.enchant;

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
import org.bukkit.event.block.BlockDamageEvent;
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
import org.bukkit.potion.PotionEffect;
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
public class ToolEnchantListener implements Listener {
    private final JavaPlugin plugin;

    public ToolEnchantListener(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * 🔥 ÉP XUNG KHAI THÁC VẠN NĂNG: Khi người chơi bắt đầu bổ cuốc/cúp đập khối block,
     * bơm thẳng hiệu ứng Haste tương thích để bù đắp việc client bị mù chỉ số.
     */
    @EventHandler
    public void onBlockMiningStart(BlockDamageEvent event) {
        Player player = event.getPlayer();
        ItemStack tool = player.getInventory().getItemInMainHand();
        if (tool == null || !tool.hasItemMeta()) return;

        PersistentDataContainer container = tool.getItemMeta().getPersistentDataContainer();
        NamespacedKey effKey = new NamespacedKey(plugin, "enchant_efficiency");

        if (container.has(effKey, PersistentDataType.INTEGER)) {
            int lvl = container.get(effKey, PersistentDataType.INTEGER);
            if (lvl > 0) {
                // 🧬 Giải pháp vạn năng chống thọt phiên bản: 1.21+ gọi là HASTE, đời cũ gọi là FAST_DIGGING
                PotionEffectType hasteEffect = PotionEffectType.getByName("FAST_DIGGING");
                if (hasteEffect == null) {
                    hasteEffect = PotionEffectType.getByName("HASTE");
                }

                if (hasteEffect != null) {
                    // Cấp bùa càng cao, tốc độ đào càng xé gió (Cấp 10 -> Haste 9 đào như sấm sét)
                    int amplifier = Math.max(0, lvl - 1); 
                    
                    // Bơm hiệu ứng kéo dài 4 giây (80 ticks), liên tục làm mới khi người chơi còn đứng đập khối
                    player.addPotionEffect(new PotionEffect(hasteEffect, 80, amplifier, true, false, false));
                }
            }
        }
    }

    /**
     * 🔄 THU HỒI PHÉP THUẬT: Khi người chơi đổi sang ô vật phẩm khác, gỡ ngay hiệu ứng tránh lạm phát đào tay không
     */
    @EventHandler
    public void onItemSwap(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        
        PotionEffectType hasteEffect = PotionEffectType.getByName("FAST_DIGGING");
        if (hasteEffect == null) {
            hasteEffect = PotionEffectType.getByName("HASTE");
        }

        if (hasteEffect != null && player.hasPotionEffect(hasteEffect)) {
            player.removePotionEffect(hasteEffect);
        }
    }
}

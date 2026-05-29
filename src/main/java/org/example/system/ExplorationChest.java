package org.example.system;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.example.core.CustomWeaponEngine;

import java.util.Random;

public class ExplorationChest implements Listener {

    private CustomWeaponEngine plugin;
    private Random random = new Random();

    public ExplorationChest(CustomWeaponEngine plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onChestOpen(PlayerInteractEvent event) {
        if (event.getAction() != org.bukkit.event.block.Action.RIGHT_CLICK_BLOCK) return;
        Block block = event.getClickedBlock();
        if (block == null) return;

        Player player = event.getPlayer();
        
        // Simple identification: Chests placed by admin could have their location saved in config
        String locStr = block.getWorld().getName() + "_" + block.getX() + "_" + block.getY() + "_" + block.getZ();
        
        if (plugin.getConfig().contains("hidden_chests." + locStr)) {
            event.setCancelled(true);
            
            String chestId = plugin.getConfig().getString("hidden_chests." + locStr + ".id");
            NamespacedKey key = new NamespacedKey(plugin, "chest_claimed_" + chestId);
            
            if (player.getPersistentDataContainer().has(key, PersistentDataType.INTEGER)) {
                player.sendMessage(ChatColor.RED + "Bạn đã mở rương này rồi!");
                return;
            }

            // Mark as claimed
            player.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, 1);
            
            // Effects
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
            player.spawnParticle(Particle.FIREWORK, block.getLocation().add(0.5, 1, 0.5), 30, 0.5, 0.5, 0.5, 0.1);
            
            // Rewards
            String chestType = plugin.getConfig().getString("hidden_chests." + locStr + ".type", "common");
            giveLoot(player, chestType);
            player.sendMessage(ChatColor.GOLD + "🌟 Bạn đã tìm thấy một Rương Ẩn (" + chestType + ")!");
        }
    }
    
    private void giveLoot(Player player, String type) {
        if (type.equalsIgnoreCase("gacha")) {
            plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), "eco give " + player.getName() + " 5000");
            player.sendMessage(ChatColor.YELLOW + "+5000 Xu");
            
            // Random furniture
            String[] furnitures = {"bonsai", "teacup", "globe", "cake"};
            String chosen = furnitures[random.nextInt(furnitures.length)];
            ItemStack furn = FurnitureGenerator.getFurniture(plugin, chosen);
            if (furn != null) {
                player.getInventory().addItem(furn);
            }
        } else {
            plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), "eco give " + player.getName() + " 1000");
            player.sendMessage(ChatColor.YELLOW + "+1000 Xu");
        }
    }
}

package org.example.system;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RecipeDiscoveryListener implements Listener {
    private final JavaPlugin plugin;
    private final List<NamespacedKey> customRecipes = new ArrayList<>();

    public RecipeDiscoveryListener(JavaPlugin plugin) {
        this.plugin = plugin;
        cacheCustomRecipes();
    }

    private void cacheCustomRecipes() {
        Iterator<Recipe> it = Bukkit.recipeIterator();
        while (it.hasNext()) {
            Recipe r = it.next();
            if (r instanceof ShapedRecipe) {
                NamespacedKey key = ((ShapedRecipe) r).getKey();
                if (key.getNamespace().equalsIgnoreCase(plugin.getName())) {
                    customRecipes.add(key);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            for (NamespacedKey key : customRecipes) {
                player.discoverRecipe(key);
            }
        }, 20L); // Delay 1 second to ensure recipes are fully loaded and client is ready
    }
}

package org.example.system;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.example.core.CustomWeaponEngine;

public class RecipeManager {
    public static void registerRecipes(CustomWeaponEngine plugin) {
        FileConfiguration lib = plugin.getLibraryConfig();
        
        // 1. Golem Sword
        ItemStack golemSword = lib.getItemStack("items.cwe_golem_sword");
        if (golemSword != null) {
            NamespacedKey key = new NamespacedKey(plugin, "recipe_golem_sword");
            ShapedRecipe recipe = new ShapedRecipe(key, golemSword);
            recipe.shape(" I ", " I ", " S ");
            recipe.setIngredient('I', Material.IRON_BLOCK);
            recipe.setIngredient('S', Material.STICK);
            Bukkit.addRecipe(recipe);
        }

        // 2. Zombie Sword
        ItemStack zombieSword = lib.getItemStack("items.cwe_zombie_sword");
        if (zombieSword != null) {
            NamespacedKey key = new NamespacedKey(plugin, "recipe_zombie_sword");
            ShapedRecipe recipe = new ShapedRecipe(key, zombieSword);
            recipe.shape(" R ", " R ", " S ");
            recipe.setIngredient('R', Material.ROTTEN_FLESH); // Replace with Rotten Flesh block if exists, using item for now
            recipe.setIngredient('S', Material.STICK);
            Bukkit.addRecipe(recipe);
        }

        // 3. Raider Axe
        ItemStack raiderAxe = lib.getItemStack("items.cwe_raider_axe");
        if (raiderAxe != null) {
            NamespacedKey key = new NamespacedKey(plugin, "recipe_raider_axe");
            ShapedRecipe recipe = new ShapedRecipe(key, raiderAxe);
            recipe.shape("GGG", "GAG", "GGG");
            recipe.setIngredient('G', Material.GOLD_BLOCK);
            recipe.setIngredient('A', Material.IRON_AXE);
            Bukkit.addRecipe(recipe);
        }
    }
}

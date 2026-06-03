/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Material
 *  org.bukkit.NamespacedKey
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.Recipe
 *  org.bukkit.inventory.RecipeChoice
 *  org.bukkit.inventory.RecipeChoice$ExactChoice
 *  org.bukkit.inventory.ShapedRecipe
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.persistence.PersistentDataType
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.java.JavaPlugin
 */
package org.example.system;

import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.example.core.CustomWeaponEngine;

public class CustomRecipeManager {
    private final JavaPlugin plugin;

    public CustomRecipeManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void registerRecipes() {
        this.registerHoaLongKiem();
        this.registerIceStaff();
        this.registerThienDaoKiem();
        this.registerHuyetAnhKiem();
        this.registerShadowFang();
        this.registerFireStaff();
        this.registerAstralShepherdWand();
        this.registerBerserkSet();
        this.registerShadowAssassinSet();
        this.registerCosmicVoidSet();
        this.registerLegendarySwords();
    }

    private ItemStack getCraftMaterial(Material mat, String name, String rarity) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            ArrayList<String> lore = new ArrayList<String>();
            lore.add("\u00a77Nguy\u00ean li\u1ec7u ch\u1ebf t\u1ea1o th\u1ea7n kh\u00ed.");
            lore.add("\u00a7f");
            if (name.contains("L\u00f5i Dung Nham")) {
                lore.add("\u00a7eNgu\u1ed3n g\u1ed1c: \u00a7fR\u01a1i ra t\u1eeb Qu\u00e1i v\u1eadt \u1edf Nether/Sa m\u1ea1c");
                lore.add("\u00a7fho\u1eb7c t\u1eeb S\u1ef1 ki\u1ec7n Thi\u00ean th\u1ea1ch L\u1eeda (FIRE).");
            } else if (name.contains("B\u0103ng Tinh C\u1ed5 \u0110\u1ea1i")) {
                lore.add("\u00a7eNgu\u1ed3n g\u1ed1c: \u00a7fR\u01a1i ra t\u1eeb Qu\u00e1i v\u1eadt \u1edf V\u00f9ng B\u0103ng Tuy\u1ebft");
                lore.add("\u00a7fho\u1eb7c t\u1eeb S\u1ef1 ki\u1ec7n Thi\u00ean th\u1ea1ch B\u0103ng (ICE).");
            } else if (name.contains("L\u00f5i N\u0103ng L\u01b0\u1ee3ng Cao C\u1ea5p")) {
                lore.add("\u00a7eNgu\u1ed3n g\u1ed1c: \u00a7fR\u01a1i ra t\u1eeb Qu\u00e1i v\u1eadt \u1edf The End / Deep Dark");
                lore.add("\u00a7fho\u1eb7c t\u1eeb S\u1ef1 ki\u1ec7n Thi\u00ean th\u1ea1ch H\u01b0 Kh\u00f4ng (VOID).");
            }
            meta.setLore(lore);
            meta.getPersistentDataContainer().set(new NamespacedKey((Plugin)this.plugin, "cwe_tier"), PersistentDataType.STRING, rarity);
            item.setItemMeta(meta);
        }
        return item;
    }

    private void registerHoaLongKiem() {
        NamespacedKey key = new NamespacedKey((Plugin)this.plugin, "cwe_hoa_long_kiem");
        ItemStack result = ((CustomWeaponEngine)this.plugin).getLibraryConfig().getItemStack("items.cwe_hoa_long_kiem");
        if (result == null) {
            result = new ItemStack(Material.GOLDEN_SWORD);
        }
        ShapedRecipe recipe = new ShapedRecipe(key, result);
        recipe.shape(new String[]{" L ", " L ", " S "});
        recipe.setIngredient('L', (RecipeChoice)new RecipeChoice.ExactChoice(this.getCraftMaterial(Material.MAGMA_CREAM, "\u00a79L\u00f5i Dung Nham", "RARE")));
        recipe.setIngredient('S', Material.STICK);
        try {
            Bukkit.removeRecipe((NamespacedKey)key);
        }
        catch (Exception exception) {
            // empty catch block
        }
        try {
            Bukkit.addRecipe((Recipe)recipe);
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    private void registerIceStaff() {
        NamespacedKey key = new NamespacedKey((Plugin)this.plugin, "cwe_ice_staff");
        ItemStack result = ((CustomWeaponEngine)this.plugin).getLibraryConfig().getItemStack("items.cwe_ice_staff");
        if (result == null) {
            result = new ItemStack(Material.GOLDEN_SWORD);
        }
        ShapedRecipe recipe = new ShapedRecipe(key, result);
        recipe.shape(new String[]{" B ", " S ", " S "});
        recipe.setIngredient('B', (RecipeChoice)new RecipeChoice.ExactChoice(this.getCraftMaterial(Material.DRAGON_BREATH, "\u00a75B\u0103ng Tinh C\u1ed5 \u0110\u1ea1i", "EPIC")));
        recipe.setIngredient('S', Material.STICK);
        try {
            Bukkit.removeRecipe((NamespacedKey)key);
        }
        catch (Exception exception) {
            // empty catch block
        }
        try {
            Bukkit.addRecipe((Recipe)recipe);
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    private void registerThienDaoKiem() {
        NamespacedKey key = new NamespacedKey((Plugin)this.plugin, "cwe_thien_dao_kiem");
        ItemStack result = ((CustomWeaponEngine)this.plugin).getLibraryConfig().getItemStack("items.cwe_thien_dao_kiem");
        if (result == null) {
            result = new ItemStack(Material.GOLDEN_SWORD);
        }
        ShapedRecipe recipe = new ShapedRecipe(key, result);
        recipe.shape(new String[]{" D ", " D ", " S "});
        recipe.setIngredient('D', Material.DIAMOND_BLOCK);
        recipe.setIngredient('S', Material.STICK);
        try {
            Bukkit.removeRecipe((NamespacedKey)key);
        }
        catch (Exception exception) {
            // empty catch block
        }
        try {
            Bukkit.addRecipe((Recipe)recipe);
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    private void registerHuyetAnhKiem() {
        NamespacedKey key = new NamespacedKey((Plugin)this.plugin, "cwe_huyet_anh_kiem");
        ItemStack result = ((CustomWeaponEngine)this.plugin).getLibraryConfig().getItemStack("items.cwe_huyet_anh_kiem");
        if (result == null) {
            result = new ItemStack(Material.GOLDEN_SWORD);
        }
        ShapedRecipe recipe = new ShapedRecipe(key, result);
        recipe.shape(new String[]{" R ", " N ", " S "});
        recipe.setIngredient('R', (RecipeChoice)new RecipeChoice.ExactChoice(this.getCraftMaterial(Material.BEACON, "\u00a76\u00a7lL\u00f5i N\u0103ng L\u01b0\u1ee3ng Cao C\u1ea5p", "LEGENDARY")));
        recipe.setIngredient('N', Material.NETHERITE_INGOT);
        recipe.setIngredient('S', Material.STICK);
        try {
            Bukkit.removeRecipe((NamespacedKey)key);
        }
        catch (Exception exception) {
            // empty catch block
        }
        try {
            Bukkit.addRecipe((Recipe)recipe);
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    private void registerShadowFang() {
        NamespacedKey key = new NamespacedKey((Plugin)this.plugin, "cwe_shadow_fang");
        ItemStack result = ((CustomWeaponEngine)this.plugin).getLibraryConfig().getItemStack("items.cwe_shadow_fang");
        if (result == null) {
            result = new ItemStack(Material.GOLDEN_SWORD);
        }
        ShapedRecipe recipe = new ShapedRecipe(key, result);
        recipe.shape(new String[]{" E ", " I ", " S "});
        recipe.setIngredient('E', Material.ENDER_PEARL);
        recipe.setIngredient('I', Material.IRON_BLOCK);
        recipe.setIngredient('S', Material.STICK);
        try {
            Bukkit.removeRecipe((NamespacedKey)key);
        }
        catch (Exception exception) {
            // empty catch block
        }
        try {
            Bukkit.addRecipe((Recipe)recipe);
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    private void registerFireStaff() {
        NamespacedKey key = new NamespacedKey((Plugin)this.plugin, "cwe_fire_staff");
        ItemStack result = ((CustomWeaponEngine)this.plugin).getLibraryConfig().getItemStack("items.cwe_fire_staff");
        if (result == null) {
            result = new ItemStack(Material.GOLDEN_SWORD);
        }
        ShapedRecipe recipe = new ShapedRecipe(key, result);
        recipe.shape(new String[]{" B ", " S ", " S "});
        recipe.setIngredient('B', Material.BLAZE_POWDER);
        recipe.setIngredient('S', Material.STICK);
        try {
            Bukkit.removeRecipe((NamespacedKey)key);
        }
        catch (Exception exception) {
            // empty catch block
        }
        try {
            Bukkit.addRecipe((Recipe)recipe);
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    private void registerAstralShepherdWand() {
        NamespacedKey key = new NamespacedKey((Plugin)this.plugin, "cwe_astral_shepherd_wand");
        ItemStack result = ((CustomWeaponEngine)this.plugin).getLibraryConfig().getItemStack("items.cwe_astral_shepherd_wand");
        if (result == null) {
            result = new ItemStack(Material.GOLDEN_SWORD);
        }
        ShapedRecipe recipe = new ShapedRecipe(key, result);
        recipe.shape(new String[]{" E ", " S ", " S "});
        recipe.setIngredient('E', (RecipeChoice)new RecipeChoice.ExactChoice(this.getCraftMaterial(Material.BEACON, "\u00a76\u00a7lL\u00f5i N\u0103ng L\u01b0\u1ee3ng Cao C\u1ea5p", "LEGENDARY")));
        recipe.setIngredient('S', Material.STICK);
        try {
            Bukkit.removeRecipe((NamespacedKey)key);
        }
        catch (Exception exception) {
            // empty catch block
        }
        try {
            Bukkit.addRecipe((Recipe)recipe);
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    private void registerBerserkSet() {
        this.createArmorRecipe("cwe_berserk_helmet", Material.IRON_HELMET, "\u00a79Berserk Helmet", "cwe_berserk_helmet", "III", "I I", "   ", Material.IRON_BLOCK, "\u00a79\u00a7lRARE ARMOR", 80.0, 40.0, 0.0, 0.0, 0.0, 25.0, 0.0);
        this.createArmorRecipe("cwe_berserk_chest", Material.IRON_CHESTPLATE, "\u00a79Berserk Chestplate", "cwe_berserk_chestplate", "I I", "III", "III", Material.IRON_BLOCK, "\u00a79\u00a7lRARE ARMOR", 100.0, 60.0, 0.0, 0.0, 0.0, 30.0, 0.0);
        this.createArmorRecipe("cwe_berserk_legs", Material.IRON_LEGGINGS, "\u00a79Berserk Leggings", "cwe_berserk_leggings", "III", "I I", "I I", Material.IRON_BLOCK, "\u00a79\u00a7lRARE ARMOR", 90.0, 50.0, 0.0, 0.0, 0.0, 20.0, 0.0);
        this.createArmorRecipe("cwe_berserk_boots", Material.IRON_BOOTS, "\u00a79Berserk Boots", "cwe_berserk_boots", "   ", "I I", "I I", Material.IRON_BLOCK, "\u00a79\u00a7lRARE ARMOR", 70.0, 30.0, 0.0, 0.0, 0.0, 15.0, 0.0);
    }

    private void registerLegendarySwords() {
        ItemStack aote = ((CustomWeaponEngine)this.plugin).getLibraryConfig().getItemStack("items.cwe_aote");
        if (aote == null) {
            aote = new ItemStack(Material.IRON_SWORD);
        }
        ShapedRecipe rAote = new ShapedRecipe(new NamespacedKey((Plugin)this.plugin, "recipe_cwe_aote"), aote);
        rAote.shape(new String[]{" E ", " E ", " D "});
        rAote.setIngredient('E', Material.ENDER_PEARL);
        rAote.setIngredient('D', Material.DIAMOND);
        try {
            Bukkit.removeRecipe((NamespacedKey)rAote.getKey());
        }
        catch (Exception exception) {
            // empty catch block
        }
        try {
            Bukkit.addRecipe((Recipe)rAote);
        }
        catch (Exception exception) {
            // empty catch block
        }
        ItemStack aotd = ((CustomWeaponEngine)this.plugin).getLibraryConfig().getItemStack("items.cwe_aotd");
        if (aotd == null) {
            aotd = new ItemStack(Material.IRON_SWORD);
        }
        ShapedRecipe rAotd = new ShapedRecipe(new NamespacedKey((Plugin)this.plugin, "recipe_cwe_aotd"), aotd);
        rAotd.shape(new String[]{" B ", " L ", " B "});
        rAotd.setIngredient('B', Material.DRAGON_BREATH);
        rAotd.setIngredient('L', (RecipeChoice)new RecipeChoice.ExactChoice(this.getCraftMaterial(Material.BEACON, "\u00a76\u00a7lL\u00f5i N\u0103ng L\u01b0\u1ee3ng Cao C\u1ea5p", "LEGENDARY")));
        try {
            Bukkit.removeRecipe((NamespacedKey)rAotd.getKey());
        }
        catch (Exception exception) {
            // empty catch block
        }
        try {
            Bukkit.addRecipe((Recipe)rAotd);
        }
        catch (Exception exception) {
            // empty catch block
        }
        ItemStack livid = ((CustomWeaponEngine)this.plugin).getLibraryConfig().getItemStack("items.cwe_livid_dagger");
        if (livid == null) {
            livid = new ItemStack(Material.IRON_SWORD);
        }
        ShapedRecipe rLivid = new ShapedRecipe(new NamespacedKey((Plugin)this.plugin, "recipe_cwe_livid_dagger"), livid);
        rLivid.shape(new String[]{" O ", " L ", " S "});
        rLivid.setIngredient('O', Material.OBSIDIAN);
        rLivid.setIngredient('L', (RecipeChoice)new RecipeChoice.ExactChoice(this.getCraftMaterial(Material.MAGMA_CREAM, "\u00a79L\u00f5i Dung Nham", "RARE")));
        rLivid.setIngredient('S', Material.STICK);
        try {
            Bukkit.removeRecipe((NamespacedKey)rLivid.getKey());
        }
        catch (Exception exception) {
            // empty catch block
        }
        try {
            Bukkit.addRecipe((Recipe)rLivid);
        }
        catch (Exception exception) {
            // empty catch block
        }
        ItemStack giants = ((CustomWeaponEngine)this.plugin).getLibraryConfig().getItemStack("items.cwe_giants_sword");
        if (giants == null) {
            giants = new ItemStack(Material.IRON_SWORD);
        }
        ShapedRecipe rGiants = new ShapedRecipe(new NamespacedKey((Plugin)this.plugin, "recipe_cwe_giants_sword"), giants);
        rGiants.shape(new String[]{"III", "ILI", " S "});
        rGiants.setIngredient('I', Material.IRON_BLOCK);
        rGiants.setIngredient('L', (RecipeChoice)new RecipeChoice.ExactChoice(this.getCraftMaterial(Material.BEACON, "\u00a76\u00a7lL\u00f5i N\u0103ng L\u01b0\u1ee3ng Cao C\u1ea5p", "LEGENDARY")));
        rGiants.setIngredient('S', Material.STICK);
        try {
            Bukkit.removeRecipe((NamespacedKey)rGiants.getKey());
        }
        catch (Exception exception) {
            // empty catch block
        }
        try {
            Bukkit.addRecipe((Recipe)rGiants);
        }
        catch (Exception exception) {
            // empty catch block
        }
        ItemStack hype = ((CustomWeaponEngine)this.plugin).getLibraryConfig().getItemStack("items.cwe_hyperion");
        if (hype == null) {
            hype = new ItemStack(Material.IRON_SWORD);
        }
        ShapedRecipe rHype = new ShapedRecipe(new NamespacedKey((Plugin)this.plugin, "recipe_cwe_hyperion"), hype);
        rHype.shape(new String[]{" L ", " L ", " S "});
        rHype.setIngredient('L', (RecipeChoice)new RecipeChoice.ExactChoice(this.getCraftMaterial(Material.BEACON, "\u00a76\u00a7lL\u00f5i N\u0103ng L\u01b0\u1ee3ng Cao C\u1ea5p", "LEGENDARY")));
        rHype.setIngredient('S', Material.NETHER_STAR);
        try {
            Bukkit.removeRecipe((NamespacedKey)rHype.getKey());
        }
        catch (Exception exception) {
            // empty catch block
        }
        try {
            Bukkit.addRecipe((Recipe)rHype);
        }
        catch (Exception exception) {
            // empty catch block
        }
        ItemStack emerald = ((CustomWeaponEngine)this.plugin).getLibraryConfig().getItemStack("items.cwe_emerald_blade");
        if (emerald == null) {
            emerald = new ItemStack(Material.IRON_SWORD);
        }
        ShapedRecipe rEmerald = new ShapedRecipe(new NamespacedKey((Plugin)this.plugin, "recipe_cwe_emerald_blade"), emerald);
        rEmerald.shape(new String[]{" E ", " E ", " S "});
        rEmerald.setIngredient('E', Material.EMERALD_BLOCK);
        rEmerald.setIngredient('S', Material.STICK);
        try {
            Bukkit.removeRecipe((NamespacedKey)rEmerald.getKey());
        }
        catch (Exception exception) {
            // empty catch block
        }
        try {
            Bukkit.addRecipe((Recipe)rEmerald);
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    private void registerShadowAssassinSet() {
        this.createArmorRecipe("cwe_shadow_helmet", Material.LEATHER_HELMET, "\u00a78Shadow Assassin Helmet", "cwe_shadow_assassin_helmet", "III", "I I", "   ", Material.OBSIDIAN, "\u00a76\u00a7lEPIC ARMOR", 40.0, 20.0, 15.0, 0.0, 10.0, 10.0, 0.0);
        this.createArmorRecipe("cwe_shadow_chest", Material.LEATHER_CHESTPLATE, "\u00a78Shadow Assassin Chestplate", "cwe_shadow_assassin_chestplate", "I I", "III", "III", Material.OBSIDIAN, "\u00a76\u00a7lEPIC ARMOR", 60.0, 30.0, 20.0, 0.0, 15.0, 15.0, 0.0);
        this.createArmorRecipe("cwe_shadow_legs", Material.LEATHER_LEGGINGS, "\u00a78Shadow Assassin Leggings", "cwe_shadow_assassin_leggings", "III", "I I", "I I", Material.OBSIDIAN, "\u00a76\u00a7lEPIC ARMOR", 50.0, 25.0, 15.0, 0.0, 10.0, 10.0, 0.0);
        this.createArmorRecipe("cwe_shadow_boots", Material.LEATHER_BOOTS, "\u00a78Shadow Assassin Boots", "cwe_shadow_assassin_boots", "   ", "I I", "I I", Material.OBSIDIAN, "\u00a76\u00a7lEPIC ARMOR", 30.0, 15.0, 10.0, 0.0, 5.0, 5.0, 0.0);
    }

    private void registerCosmicVoidSet() {
        this.createAdvancedArmorRecipe("cwe_cosmic_helmet", Material.DIAMOND_HELMET, "\u00a7dCosmic Void Helmet", "cwe_cosmic_void_helmet", "III", "ICI", "   ", Material.DIAMOND_BLOCK, "\u00a75\u00a7lEPIC ARMOR", 150.0, 80.0, 0.0, 0.0, 0.0, 0.0, 50.0);
        this.createAdvancedArmorRecipe("cwe_cosmic_chest", Material.DIAMOND_CHESTPLATE, "\u00a7dCosmic Void Chestplate", "cwe_cosmic_void_chestplate", "I I", "ICI", "III", Material.DIAMOND_BLOCK, "\u00a75\u00a7lEPIC ARMOR", 200.0, 120.0, 0.0, 0.0, 0.0, 0.0, 80.0);
        this.createAdvancedArmorRecipe("cwe_cosmic_legs", Material.DIAMOND_LEGGINGS, "\u00a7dCosmic Void Leggings", "cwe_cosmic_void_leggings", "III", "ICI", "I I", Material.DIAMOND_BLOCK, "\u00a75\u00a7lEPIC ARMOR", 180.0, 100.0, 0.0, 0.0, 0.0, 0.0, 60.0);
        this.createAdvancedArmorRecipe("cwe_cosmic_boots", Material.DIAMOND_BOOTS, "\u00a7dCosmic Void Boots", "cwe_cosmic_void_boots", "   ", "ICI", "I I", Material.DIAMOND_BLOCK, "\u00a75\u00a7lEPIC ARMOR", 120.0, 60.0, 0.0, 0.0, 0.0, 0.0, 40.0);
    }

    private void registerPhantomRangerSet() {
        this.createAdvancedArmorRecipe("cwe_phantom_helmet", Material.CHAINMAIL_HELMET, "\u00a75Phantom Ranger Helmet", "cwe_phantom_ranger_helmet", "III", "ICI", "   ", Material.GOLD_BLOCK, "\u00a75\u00a7lEPIC ARMOR", 20.0, 10.0, 5.0, 10.0, 8.0, 15.0, 25.0);
        this.createAdvancedArmorRecipe("cwe_phantom_chest", Material.CHAINMAIL_CHESTPLATE, "\u00a75Phantom Ranger Chestplate", "cwe_phantom_ranger_chestplate", "I I", "ICI", "III", Material.GOLD_BLOCK, "\u00a75\u00a7lEPIC ARMOR", 20.0, 10.0, 5.0, 10.0, 8.0, 15.0, 25.0);
        this.createAdvancedArmorRecipe("cwe_phantom_legs", Material.CHAINMAIL_LEGGINGS, "\u00a75Phantom Ranger Leggings", "cwe_phantom_ranger_leggings", "III", "ICI", "I I", Material.GOLD_BLOCK, "\u00a75\u00a7lEPIC ARMOR", 20.0, 10.0, 5.0, 10.0, 8.0, 15.0, 25.0);
        this.createAdvancedArmorRecipe("cwe_phantom_boots", Material.CHAINMAIL_BOOTS, "\u00a75Phantom Ranger Boots", "cwe_phantom_ranger_boots", "   ", "ICI", "I I", Material.GOLD_BLOCK, "\u00a75\u00a7lEPIC ARMOR", 20.0, 10.0, 5.0, 10.0, 8.0, 15.0, 25.0);
    }

    private void createArmorRecipe(String keyStr, Material mat, String name, String cweId, String r1, String r2, String r3, Material ingredient, String rarityLore, double hp, double def, double spd, double atkSpd, double critC, double str, double intel) {
        NamespacedKey key = new NamespacedKey((Plugin)this.plugin, keyStr);
        ItemStack result = ((CustomWeaponEngine)this.plugin).getLibraryConfig().getItemStack("items." + cweId);
        if (result == null) {
            result = new ItemStack(mat);
            ItemMeta meta = result.getItemMeta();
            if (meta != null) {
                meta.setDisplayName(name);
                meta.setUnbreakable(true);
                meta.getPersistentDataContainer().set(new NamespacedKey((Plugin)this.plugin, "cwe_id"), PersistentDataType.STRING, cweId);
                result.setItemMeta(meta);
            }
        } else {
            result = result.clone();
        }
        ShapedRecipe recipe = new ShapedRecipe(key, result);
        recipe.shape(new String[]{r1, r2, r3});
        recipe.setIngredient('I', ingredient);
        try {
            Bukkit.removeRecipe((NamespacedKey)key);
        }
        catch (Exception exception) {
            // empty catch block
        }
        try {
            Bukkit.addRecipe((Recipe)recipe);
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    private void createAdvancedArmorRecipe(String keyStr, Material mat, String name, String cweId, String r1, String r2, String r3, Material baseMat, String rarityLore, double hp, double def, double spd, double atkSpd, double critC, double str, double intel) {
        NamespacedKey key = new NamespacedKey((Plugin)this.plugin, keyStr);
        ItemStack result = ((CustomWeaponEngine)this.plugin).getLibraryConfig().getItemStack("items." + cweId);
        if (result == null) {
            result = new ItemStack(mat);
            ItemMeta meta = result.getItemMeta();
            if (meta != null) {
                meta.setDisplayName(name);
                meta.setUnbreakable(true);
                meta.getPersistentDataContainer().set(new NamespacedKey((Plugin)this.plugin, "cwe_id"), PersistentDataType.STRING, cweId);
                result.setItemMeta(meta);
            }
        } else {
            result = result.clone();
        }
        ShapedRecipe recipe = new ShapedRecipe(key, result);
        recipe.shape(new String[]{r1, r2, r3});
        recipe.setIngredient('I', baseMat);
        recipe.setIngredient('C', (RecipeChoice)new RecipeChoice.ExactChoice(this.getCraftMaterial(Material.DRAGON_BREATH, "\u00a75B\u0103ng Tinh C\u1ed5 \u0110\u1ea1i", "EPIC")));
        try {
            Bukkit.removeRecipe((NamespacedKey)key);
        }
        catch (Exception exception) {
            // empty catch block
        }
        try {
            Bukkit.addRecipe((Recipe)recipe);
        }
        catch (Exception exception) {
            // empty catch block
        }
    }
}


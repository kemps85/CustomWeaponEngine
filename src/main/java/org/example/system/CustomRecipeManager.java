package org.example.system;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CustomRecipeManager {
    private final JavaPlugin plugin;

    public CustomRecipeManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void registerRecipes() {
        registerHoaLongKiem();
        registerIceStaff();
        registerThienDaoKiem();
        registerHuyetAnhKiem();
        registerShadowFang();
        registerFireStaff();
        registerAstralShepherdWand();
        
        registerBerserkSet();
        registerShadowAssassinSet();
        registerCosmicVoidSet();
        
        registerLegendarySwords();
    }

    private ItemStack getCraftMaterial(Material mat, String name, String rarity) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            List<String> lore = new ArrayList<>();
            lore.add("§7Nguyên liệu chế tạo thần khí.");
            lore.add("§f");
            if (name.contains("Lõi Dung Nham")) {
                lore.add("§eNguồn gốc: §fRơi ra từ Quái vật ở Nether/Sa mạc");
                lore.add("§fhoặc từ Sự kiện Thiên thạch Lửa (FIRE).");
            } else if (name.contains("Băng Tinh Cổ Đại")) {
                lore.add("§eNguồn gốc: §fRơi ra từ Quái vật ở Vùng Băng Tuyết");
                lore.add("§fhoặc từ Sự kiện Thiên thạch Băng (ICE).");
            } else if (name.contains("Lõi Năng Lượng Cao Cấp")) {
                lore.add("§eNguồn gốc: §fRơi ra từ Quái vật ở The End / Deep Dark");
                lore.add("§fhoặc từ Sự kiện Thiên thạch Hư Không (VOID).");
            }
            meta.setLore(lore);
            meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "cwe_tier"), PersistentDataType.STRING, rarity);
            item.setItemMeta(meta);
        }
        return item;
    }

    private void registerHoaLongKiem() {
        NamespacedKey key = new NamespacedKey(plugin, "cwe_hoa_long_kiem");
        ItemStack result = ((org.example.core.CustomWeaponEngine)plugin).getLibraryConfig().getItemStack("items.cwe_hoa_long_kiem");
        if (result == null) { result = new ItemStack(Material.GOLDEN_SWORD); }
        ShapedRecipe recipe = new ShapedRecipe(key, result);
        recipe.shape(" L ", " L ", " S ");
        recipe.setIngredient('L', new RecipeChoice.ExactChoice(getCraftMaterial(Material.MAGMA_CREAM, "§9Lõi Dung Nham", "RARE")));
        recipe.setIngredient('S', Material.STICK);
        try { Bukkit.removeRecipe(key); } catch (Exception ignored) {}
        try { Bukkit.addRecipe(recipe); } catch (Exception e) {}
    }

    private void registerIceStaff() {
        NamespacedKey key = new NamespacedKey(plugin, "cwe_ice_staff");
        ItemStack result = ((org.example.core.CustomWeaponEngine)plugin).getLibraryConfig().getItemStack("items.cwe_ice_staff");
        if (result == null) { result = new ItemStack(Material.GOLDEN_SWORD); }
        ShapedRecipe recipe = new ShapedRecipe(key, result);
        recipe.shape(" B ", " S ", " S ");
        recipe.setIngredient('B', new RecipeChoice.ExactChoice(getCraftMaterial(Material.DRAGON_BREATH, "§5Băng Tinh Cổ Đại", "EPIC")));
        recipe.setIngredient('S', Material.STICK);
        try { Bukkit.removeRecipe(key); } catch (Exception ignored) {}
        try { Bukkit.addRecipe(recipe); } catch (Exception e) {}
    }

    private void registerThienDaoKiem() {
        NamespacedKey key = new NamespacedKey(plugin, "cwe_thien_dao_kiem");
        ItemStack result = ((org.example.core.CustomWeaponEngine)plugin).getLibraryConfig().getItemStack("items.cwe_thien_dao_kiem");
        if (result == null) { result = new ItemStack(Material.GOLDEN_SWORD); }
        ShapedRecipe recipe = new ShapedRecipe(key, result);
        recipe.shape(" D ", " D ", " S ");
        recipe.setIngredient('D', Material.DIAMOND_BLOCK);
        recipe.setIngredient('S', Material.STICK);
        try { Bukkit.removeRecipe(key); } catch (Exception ignored) {}
        try { Bukkit.addRecipe(recipe); } catch (Exception e) {}
    }

    private void registerHuyetAnhKiem() {
        NamespacedKey key = new NamespacedKey(plugin, "cwe_huyet_anh_kiem");
        ItemStack result = ((org.example.core.CustomWeaponEngine)plugin).getLibraryConfig().getItemStack("items.cwe_huyet_anh_kiem");
        if (result == null) { result = new ItemStack(Material.GOLDEN_SWORD); }
        ShapedRecipe recipe = new ShapedRecipe(key, result);
        recipe.shape(" R ", " N ", " S ");
        recipe.setIngredient('R', new RecipeChoice.ExactChoice(getCraftMaterial(Material.BEACON, "§6§lLõi Năng Lượng Cao Cấp", "LEGENDARY")));
        recipe.setIngredient('N', Material.NETHERITE_INGOT);
        recipe.setIngredient('S', Material.STICK);
        try { Bukkit.removeRecipe(key); } catch (Exception ignored) {}
        try { Bukkit.addRecipe(recipe); } catch (Exception e) {}
    }

    private void registerShadowFang() {
        NamespacedKey key = new NamespacedKey(plugin, "cwe_shadow_fang");
        ItemStack result = ((org.example.core.CustomWeaponEngine)plugin).getLibraryConfig().getItemStack("items.cwe_shadow_fang");
        if (result == null) { result = new ItemStack(Material.GOLDEN_SWORD); }
        ShapedRecipe recipe = new ShapedRecipe(key, result);
        recipe.shape(" E ", " I ", " S ");
        recipe.setIngredient('E', Material.ENDER_PEARL);
        recipe.setIngredient('I', Material.IRON_BLOCK);
        recipe.setIngredient('S', Material.STICK);
        try { Bukkit.removeRecipe(key); } catch (Exception ignored) {}
        try { Bukkit.addRecipe(recipe); } catch (Exception e) {}
    }

    private void registerFireStaff() {
        NamespacedKey key = new NamespacedKey(plugin, "cwe_fire_staff");
        ItemStack result = ((org.example.core.CustomWeaponEngine)plugin).getLibraryConfig().getItemStack("items.cwe_fire_staff");
        if (result == null) { result = new ItemStack(Material.GOLDEN_SWORD); }
        ShapedRecipe recipe = new ShapedRecipe(key, result);
        recipe.shape(" B ", " S ", " S ");
        recipe.setIngredient('B', Material.BLAZE_POWDER);
        recipe.setIngredient('S', Material.STICK);
        try { Bukkit.removeRecipe(key); } catch (Exception ignored) {}
        try { Bukkit.addRecipe(recipe); } catch (Exception e) {}
    }

    private void registerAstralShepherdWand() {
        NamespacedKey key = new NamespacedKey(plugin, "cwe_astral_shepherd_wand");
        ItemStack result = ((org.example.core.CustomWeaponEngine)plugin).getLibraryConfig().getItemStack("items.cwe_astral_shepherd_wand");
        if (result == null) { result = new ItemStack(Material.GOLDEN_SWORD); }
        ShapedRecipe recipe = new ShapedRecipe(key, result);
        recipe.shape(" E ", " S ", " S ");
        recipe.setIngredient('E', new RecipeChoice.ExactChoice(getCraftMaterial(Material.BEACON, "§6§lLõi Năng Lượng Cao Cấp", "LEGENDARY")));
        recipe.setIngredient('S', Material.STICK);
        try { Bukkit.removeRecipe(key); } catch (Exception ignored) {}
        try { Bukkit.addRecipe(recipe); } catch (Exception e) {}
    }

    

    private void registerBerserkSet() {
        createArmorRecipe("cwe_berserk_helmet", Material.IRON_HELMET, "§9Berserk Helmet", "cwe_berserk_helmet", "III", "I I", "   ", Material.IRON_BLOCK, "§9§lRARE ARMOR", 80.0, 40.0, 0.0, 0.0, 0.0, 25.0, 0.0);
        createArmorRecipe("cwe_berserk_chest", Material.IRON_CHESTPLATE, "§9Berserk Chestplate", "cwe_berserk_chestplate", "I I", "III", "III", Material.IRON_BLOCK, "§9§lRARE ARMOR", 100.0, 60.0, 0.0, 0.0, 0.0, 30.0, 0.0);
        createArmorRecipe("cwe_berserk_legs", Material.IRON_LEGGINGS, "§9Berserk Leggings", "cwe_berserk_leggings", "III", "I I", "I I", Material.IRON_BLOCK, "§9§lRARE ARMOR", 90.0, 50.0, 0.0, 0.0, 0.0, 20.0, 0.0);
        createArmorRecipe("cwe_berserk_boots", Material.IRON_BOOTS, "§9Berserk Boots", "cwe_berserk_boots", "   ", "I I", "I I", Material.IRON_BLOCK, "§9§lRARE ARMOR", 70.0, 30.0, 0.0, 0.0, 0.0, 15.0, 0.0);
    }
    
    private void registerLegendarySwords() {
        // AOTE
        ItemStack aote = ((org.example.core.CustomWeaponEngine)plugin).getLibraryConfig().getItemStack("items.cwe_aote");
          if (aote == null) aote = new ItemStack(Material.IRON_SWORD);
        ShapedRecipe rAote = new ShapedRecipe(new NamespacedKey(plugin, "recipe_cwe_aote"), aote);
        rAote.shape(" E ", " E ", " D ");
        rAote.setIngredient('E', Material.ENDER_PEARL);
        rAote.setIngredient('D', Material.DIAMOND);
        try { Bukkit.removeRecipe(rAote.getKey()); } catch(Exception ignored){}
        try { Bukkit.addRecipe(rAote); } catch(Exception ignored){}

        // AOTD
        ItemStack aotd = ((org.example.core.CustomWeaponEngine)plugin).getLibraryConfig().getItemStack("items.cwe_aotd");
          if (aotd == null) aotd = new ItemStack(Material.IRON_SWORD);
        ShapedRecipe rAotd = new ShapedRecipe(new NamespacedKey(plugin, "recipe_cwe_aotd"), aotd);
        rAotd.shape(" B ", " L ", " B ");
        rAotd.setIngredient('B', Material.DRAGON_BREATH);
        rAotd.setIngredient('L', new RecipeChoice.ExactChoice(getCraftMaterial(Material.BEACON, "§6§lLõi Năng Lượng Cao Cấp", "LEGENDARY")));
        try { Bukkit.removeRecipe(rAotd.getKey()); } catch(Exception ignored){}
        try { Bukkit.addRecipe(rAotd); } catch(Exception ignored){}

        // Livid Dagger
        ItemStack livid = ((org.example.core.CustomWeaponEngine)plugin).getLibraryConfig().getItemStack("items.cwe_livid_dagger");
          if (livid == null) livid = new ItemStack(Material.IRON_SWORD);
        ShapedRecipe rLivid = new ShapedRecipe(new NamespacedKey(plugin, "recipe_cwe_livid_dagger"), livid);
        rLivid.shape(" O ", " L ", " S ");
        rLivid.setIngredient('O', Material.OBSIDIAN);
        rLivid.setIngredient('L', new RecipeChoice.ExactChoice(getCraftMaterial(Material.MAGMA_CREAM, "§9Lõi Dung Nham", "RARE")));
        rLivid.setIngredient('S', Material.STICK);
        try { Bukkit.removeRecipe(rLivid.getKey()); } catch(Exception ignored){}
        try { Bukkit.addRecipe(rLivid); } catch(Exception ignored){}

        // Giant's Sword
        ItemStack giants = ((org.example.core.CustomWeaponEngine)plugin).getLibraryConfig().getItemStack("items.cwe_giants_sword");
          if (giants == null) giants = new ItemStack(Material.IRON_SWORD);
        ShapedRecipe rGiants = new ShapedRecipe(new NamespacedKey(plugin, "recipe_cwe_giants_sword"), giants);
        rGiants.shape("III", "ILI", " S ");
        rGiants.setIngredient('I', Material.IRON_BLOCK);
        rGiants.setIngredient('L', new RecipeChoice.ExactChoice(getCraftMaterial(Material.BEACON, "§6§lLõi Năng Lượng Cao Cấp", "LEGENDARY")));
        rGiants.setIngredient('S', Material.STICK);
        try { Bukkit.removeRecipe(rGiants.getKey()); } catch(Exception ignored){}
        try { Bukkit.addRecipe(rGiants); } catch(Exception ignored){}

        // Hyperion
        ItemStack hype = ((org.example.core.CustomWeaponEngine)plugin).getLibraryConfig().getItemStack("items.cwe_hyperion");
          if (hype == null) hype = new ItemStack(Material.IRON_SWORD);
        ShapedRecipe rHype = new ShapedRecipe(new NamespacedKey(plugin, "recipe_cwe_hyperion"), hype);
        rHype.shape(" L ", " L ", " S ");
        rHype.setIngredient('L', new RecipeChoice.ExactChoice(getCraftMaterial(Material.BEACON, "§6§lLõi Năng Lượng Cao Cấp", "LEGENDARY")));
        rHype.setIngredient('S', Material.NETHER_STAR);
        try { Bukkit.removeRecipe(rHype.getKey()); } catch(Exception ignored){}
        try { Bukkit.addRecipe(rHype); } catch(Exception ignored){}
        
        // Emerald Blade
        ItemStack emerald = ((org.example.core.CustomWeaponEngine)plugin).getLibraryConfig().getItemStack("items.cwe_emerald_blade");
          if (emerald == null) emerald = new ItemStack(Material.IRON_SWORD);
        ShapedRecipe rEmerald = new ShapedRecipe(new NamespacedKey(plugin, "recipe_cwe_emerald_blade"), emerald);
        rEmerald.shape(" E ", " E ", " S ");
        rEmerald.setIngredient('E', Material.EMERALD_BLOCK);
        rEmerald.setIngredient('S', Material.STICK);
        try { Bukkit.removeRecipe(rEmerald.getKey()); } catch(Exception ignored){}
        try { Bukkit.addRecipe(rEmerald); } catch(Exception ignored){}
    }

    private void registerShadowAssassinSet() {
        createArmorRecipe("cwe_shadow_helmet", Material.LEATHER_HELMET, "§8Shadow Assassin Helmet", "cwe_shadow_assassin_helmet", "III", "I I", "   ", Material.OBSIDIAN, "§6§lEPIC ARMOR", 40.0, 20.0, 15.0, 0.0, 10.0, 10.0, 0.0);
        createArmorRecipe("cwe_shadow_chest", Material.LEATHER_CHESTPLATE, "§8Shadow Assassin Chestplate", "cwe_shadow_assassin_chestplate", "I I", "III", "III", Material.OBSIDIAN, "§6§lEPIC ARMOR", 60.0, 30.0, 20.0, 0.0, 15.0, 15.0, 0.0);
        createArmorRecipe("cwe_shadow_legs", Material.LEATHER_LEGGINGS, "§8Shadow Assassin Leggings", "cwe_shadow_assassin_leggings", "III", "I I", "I I", Material.OBSIDIAN, "§6§lEPIC ARMOR", 50.0, 25.0, 15.0, 0.0, 10.0, 10.0, 0.0);
        createArmorRecipe("cwe_shadow_boots", Material.LEATHER_BOOTS, "§8Shadow Assassin Boots", "cwe_shadow_assassin_boots", "   ", "I I", "I I", Material.OBSIDIAN, "§6§lEPIC ARMOR", 30.0, 15.0, 10.0, 0.0, 5.0, 5.0, 0.0);
    }

    private void registerCosmicVoidSet() {
        // Epic Armor, requires DRAGON_BREATH in the center.
        createAdvancedArmorRecipe("cwe_cosmic_helmet", Material.DIAMOND_HELMET, "§dCosmic Void Helmet", "cwe_cosmic_void_helmet", "III", "ICI", "   ", Material.DIAMOND_BLOCK, "§5§lEPIC ARMOR", 150.0, 80.0, 0.0, 0.0, 0.0, 0.0, 50.0);
        createAdvancedArmorRecipe("cwe_cosmic_chest", Material.DIAMOND_CHESTPLATE, "§dCosmic Void Chestplate", "cwe_cosmic_void_chestplate", "I I", "ICI", "III", Material.DIAMOND_BLOCK, "§5§lEPIC ARMOR", 200.0, 120.0, 0.0, 0.0, 0.0, 0.0, 80.0);
        createAdvancedArmorRecipe("cwe_cosmic_legs", Material.DIAMOND_LEGGINGS, "§dCosmic Void Leggings", "cwe_cosmic_void_leggings", "III", "ICI", "I I", Material.DIAMOND_BLOCK, "§5§lEPIC ARMOR", 180.0, 100.0, 0.0, 0.0, 0.0, 0.0, 60.0);
        createAdvancedArmorRecipe("cwe_cosmic_boots", Material.DIAMOND_BOOTS, "§dCosmic Void Boots", "cwe_cosmic_void_boots", "   ", "ICI", "I I", Material.DIAMOND_BLOCK, "§5§lEPIC ARMOR", 120.0, 60.0, 0.0, 0.0, 0.0, 0.0, 40.0);
    }

    private void registerPhantomRangerSet() {
        // Archer Epic Armor
        createAdvancedArmorRecipe("cwe_phantom_helmet", Material.CHAINMAIL_HELMET, "§5Phantom Ranger Helmet", "cwe_phantom_ranger_helmet", "III", "ICI", "   ", Material.GOLD_BLOCK, "§5§lEPIC ARMOR", 20.0, 10.0, 5.0, 10.0, 8.0, 15.0, 25.0);
        createAdvancedArmorRecipe("cwe_phantom_chest", Material.CHAINMAIL_CHESTPLATE, "§5Phantom Ranger Chestplate", "cwe_phantom_ranger_chestplate", "I I", "ICI", "III", Material.GOLD_BLOCK, "§5§lEPIC ARMOR", 20.0, 10.0, 5.0, 10.0, 8.0, 15.0, 25.0);
        createAdvancedArmorRecipe("cwe_phantom_legs", Material.CHAINMAIL_LEGGINGS, "§5Phantom Ranger Leggings", "cwe_phantom_ranger_leggings", "III", "ICI", "I I", Material.GOLD_BLOCK, "§5§lEPIC ARMOR", 20.0, 10.0, 5.0, 10.0, 8.0, 15.0, 25.0);
        createAdvancedArmorRecipe("cwe_phantom_boots", Material.CHAINMAIL_BOOTS, "§5Phantom Ranger Boots", "cwe_phantom_ranger_boots", "   ", "ICI", "I I", Material.GOLD_BLOCK, "§5§lEPIC ARMOR", 20.0, 10.0, 5.0, 10.0, 8.0, 15.0, 25.0);
    }

    private void createArmorRecipe(String keyStr, Material mat, String name, String cweId, String r1, String r2, String r3, Material ingredient, String rarityLore, double hp, double def, double spd, double atkSpd, double critC, double str, double intel) {
        
        NamespacedKey key = new NamespacedKey(plugin, keyStr);
        ItemStack result = ((org.example.core.CustomWeaponEngine)plugin).getLibraryConfig().getItemStack("items." + cweId);
        if (result == null) {
            result = new ItemStack(mat);
            ItemMeta meta = result.getItemMeta();
            if (meta != null) {
                meta.setDisplayName(name);
                meta.setUnbreakable(true);
                meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "cwe_id"), org.bukkit.persistence.PersistentDataType.STRING, cweId);
                result.setItemMeta(meta);
            }
        } else {
            result = result.clone();
        }
        ShapedRecipe recipe = new ShapedRecipe(key, result);

        recipe.shape(r1, r2, r3);
        recipe.setIngredient('I', ingredient);
        try { Bukkit.removeRecipe(key); } catch (Exception ignored) {}
        try { Bukkit.addRecipe(recipe); } catch (Exception e) {}
    }

    private void createAdvancedArmorRecipe(String keyStr, Material mat, String name, String cweId, String r1, String r2, String r3, Material baseMat, String rarityLore, double hp, double def, double spd, double atkSpd, double critC, double str, double intel) {
        
        NamespacedKey key = new NamespacedKey(plugin, keyStr);
        ItemStack result = ((org.example.core.CustomWeaponEngine)plugin).getLibraryConfig().getItemStack("items." + cweId);
        if (result == null) {
            result = new ItemStack(mat);
            ItemMeta meta = result.getItemMeta();
            if (meta != null) {
                meta.setDisplayName(name);
                meta.setUnbreakable(true);
                meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "cwe_id"), org.bukkit.persistence.PersistentDataType.STRING, cweId);
                result.setItemMeta(meta);
            }
        } else {
            result = result.clone();
        }
        ShapedRecipe recipe = new ShapedRecipe(key, result);

        recipe.shape(r1, r2, r3);
        recipe.setIngredient('I', baseMat);
        recipe.setIngredient('C', new RecipeChoice.ExactChoice(getCraftMaterial(Material.DRAGON_BREATH, "§5Băng Tinh Cổ Đại", "EPIC")));
        try { Bukkit.removeRecipe(key); } catch (Exception ignored) {}
        try { Bukkit.addRecipe(recipe); } catch (Exception e) {}
    }
}

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
        
        
        
    }

    private ItemStack getCraftMaterial(Material mat, String name, String rarity) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            meta.setUnbreakable(true);
            meta.setLore(Arrays.asList("§7Nguyên liệu chế tạo thần khí."));
            meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "cwe_tier"), PersistentDataType.STRING, rarity);
            item.setItemMeta(meta);
        }
        return item;
    }

    private void registerHoaLongKiem() {
        NamespacedKey key = new NamespacedKey(plugin, "cwe_hoa_long_kiem");
        ItemStack result = new ItemStack(Material.GOLDEN_SWORD);
        ItemMeta meta = result.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("§c§lHỏa Long Kiếm");
            meta.setUnbreakable(true);
            meta.setLore(Arrays.asList(
                "§7Damage: §c+25",
                "§7Strength: §c+10",
                "§f",
                "§6Item Ability: Hỏa Long Phẫn Nộ §e§l[RIGHT CLICK]",
                "§7Phóng ra một quả cầu lửa khổng lồ",
                "§7gây sát thương diện rộng.",
                "§9Mana Cost: §30",
                "§9Cooldown: §a1s",
                "§f",
                "§5§lEPIC SWORD"
            ));
            meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "cwe_damage"), PersistentDataType.DOUBLE, 25.0);
            meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "stat_strength"), PersistentDataType.DOUBLE, 10.0);
            meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "stat_has_stats"), PersistentDataType.INTEGER, 1);
            meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "cwe_is_weapon"), PersistentDataType.INTEGER, 1);
            result.setItemMeta(meta);
        }
        ShapedRecipe recipe = new ShapedRecipe(key, result);
        recipe.shape(" L ", " L ", " S ");
        recipe.setIngredient('L', new RecipeChoice.ExactChoice(getCraftMaterial(Material.MAGMA_CREAM, "§9Lõi Dung Nham", "RARE")));
        recipe.setIngredient('S', Material.STICK);
        try { Bukkit.removeRecipe(key); } catch (Exception ignored) {}
        try { Bukkit.addRecipe(recipe); } catch (Exception e) {}
    }

    private void registerIceStaff() {
        NamespacedKey key = new NamespacedKey(plugin, "cwe_ice_staff");
        ItemStack result = new ItemStack(Material.DIAMOND_HOE);
        ItemMeta meta = result.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("§b§lIce Staff");
            meta.setUnbreakable(true);
            meta.setLore(Arrays.asList(
                "§7Damage: §c+8",
                "§7Intelligence: §a+50",
                "§f",
                "§6Item Ability: Bão Tuyết §e§l[RIGHT CLICK]",
                "§7Bắn ra tia băng giá làm chậm",
                "§7và sát thương kẻ địch.",
                "§9Mana Cost: §330",
                "§9Cooldown: §a3s",
                "§f",
                "§5§lEPIC WAND"
            ));
            meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "cwe_damage"), PersistentDataType.DOUBLE, 8.0);
            meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "stat_intelligence"), PersistentDataType.DOUBLE, 50.0);
            meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "stat_has_stats"), PersistentDataType.INTEGER, 1);
            meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "cwe_is_weapon"), PersistentDataType.INTEGER, 1);
            result.setItemMeta(meta);
        }
        ShapedRecipe recipe = new ShapedRecipe(key, result);
        recipe.shape(" B ", " S ", " S ");
        recipe.setIngredient('B', new RecipeChoice.ExactChoice(getCraftMaterial(Material.DRAGON_BREATH, "§5Băng Tinh Cổ Đại", "EPIC")));
        recipe.setIngredient('S', Material.STICK);
        try { Bukkit.removeRecipe(key); } catch (Exception ignored) {}
        try { Bukkit.addRecipe(recipe); } catch (Exception e) {}
    }

    private void registerThienDaoKiem() {
        NamespacedKey key = new NamespacedKey(plugin, "cwe_thien_dao_kiem");
        ItemStack result = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta meta = result.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("§b§lThiên Đạo Kiếm");
            meta.setUnbreakable(true);
            meta.setLore(Arrays.asList(
                "§7Damage: §c+30",
                "§7Strength: §c+15",
                "§f",
                "§6Item Ability: Thiên Phạt §e§l[RIGHT CLICK]",
                "§7Giáng tia sét xuống đầu kẻ thù.",
                "§9Mana Cost: §30",
                "§9Cooldown: §a1s",
                "§f",
                "§5§lEPIC SWORD"
            ));
            meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "cwe_damage"), PersistentDataType.DOUBLE, 30.0);
            meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "stat_strength"), PersistentDataType.DOUBLE, 15.0);
            meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "stat_has_stats"), PersistentDataType.INTEGER, 1);
            meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "cwe_is_weapon"), PersistentDataType.INTEGER, 1);
            result.setItemMeta(meta);
        }
        ShapedRecipe recipe = new ShapedRecipe(key, result);
        recipe.shape(" D ", " D ", " S ");
        recipe.setIngredient('D', Material.DIAMOND_BLOCK);
        recipe.setIngredient('S', Material.STICK);
        try { Bukkit.removeRecipe(key); } catch (Exception ignored) {}
        try { Bukkit.addRecipe(recipe); } catch (Exception e) {}
    }

    private void registerHuyetAnhKiem() {
        NamespacedKey key = new NamespacedKey(plugin, "cwe_huyet_anh_kiem");
        ItemStack result = new ItemStack(Material.NETHERITE_SWORD);
        ItemMeta meta = result.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("§c§lHuyết Ảnh Kiếm");
            meta.setUnbreakable(true);
            meta.setLore(Arrays.asList(
                "§7Damage: §c+35",
                "§7Crit Damage: §c+50",
                "§f",
                "§6Item Ability: Huyết Lôi §e§l[RIGHT CLICK]",
                "§7Gây sát thương sét đỏ kinh hoàng.",
                "§f",
                "§6§lLEGENDARY SWORD"
            ));
            meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "cwe_damage"), PersistentDataType.DOUBLE, 35.0);
            meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "stat_crit_damage"), PersistentDataType.DOUBLE, 50.0);
            meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "stat_has_stats"), PersistentDataType.INTEGER, 1);
            meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "cwe_is_weapon"), PersistentDataType.INTEGER, 1);
            result.setItemMeta(meta);
        }
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
        ItemStack result = new ItemStack(Material.IRON_SWORD);
        ItemMeta meta = result.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("§9§lĐoản Đao Shadow Fang");
            meta.setUnbreakable(true);
            meta.setLore(Arrays.asList(
                "§7Damage: §c+20",
                "§7Speed: §a+20",
                "§f",
                "§6Item Ability: Tốc Biến §e§l[RIGHT CLICK]",
                "§7Lướt nhanh về phía trước.",
                "§f",
                "§9§lRARE SWORD"
            ));
            meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "cwe_damage"), PersistentDataType.DOUBLE, 20.0);
            meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "stat_speed"), PersistentDataType.DOUBLE, 20.0);
            meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "stat_has_stats"), PersistentDataType.INTEGER, 1);
            meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "cwe_is_weapon"), PersistentDataType.INTEGER, 1);
            result.setItemMeta(meta);
        }
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
        ItemStack result = new ItemStack(Material.BLAZE_ROD);
        ItemMeta meta = result.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("§c§lFire Staff");
            meta.setUnbreakable(true);
            meta.setLore(Arrays.asList(
                "§7Damage: §c+5",
                "§7Intelligence: §a+50",
                "§f",
                "§6Item Ability: Cầu Lửa §e§l[RIGHT CLICK]",
                "§7Bắn ra quả cầu lửa đốt cháy mục tiêu.",
                "§f",
                "§5§lEPIC WAND"
            ));
            meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "cwe_damage"), PersistentDataType.DOUBLE, 5.0);
            meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "stat_intelligence"), PersistentDataType.DOUBLE, 50.0);
            meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "stat_has_stats"), PersistentDataType.INTEGER, 1);
            meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "cwe_is_weapon"), PersistentDataType.INTEGER, 1);
            result.setItemMeta(meta);
        }
        ShapedRecipe recipe = new ShapedRecipe(key, result);
        recipe.shape(" B ", " S ", " S ");
        recipe.setIngredient('B', Material.BLAZE_POWDER);
        recipe.setIngredient('S', Material.STICK);
        try { Bukkit.removeRecipe(key); } catch (Exception ignored) {}
        try { Bukkit.addRecipe(recipe); } catch (Exception e) {}
    }

    private void registerAstralShepherdWand() {
        NamespacedKey key = new NamespacedKey(plugin, "cwe_astral_shepherd_wand");
        ItemStack result = new ItemStack(Material.STICK);
        ItemMeta meta = result.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("§5§lAstral Shepherd Wand");
            meta.setMaxStackSize(1);
            meta.setUnbreakable(true);
            meta.setLore(Arrays.asList(
                "§7Damage: §c+10",
                "§7Intelligence: §a+100",
                "§f",
                "§6Item Ability: Triệu Hồi Cừu §e§l[RIGHT CLICK]",
                "§7Gọi bầy cừu phép thuật nổ tung.",
                "§f",
                "§5§lEPIC WAND"
            ));
            meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "cwe_damage"), PersistentDataType.DOUBLE, 10.0);
            meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "stat_intelligence"), PersistentDataType.DOUBLE, 100.0);
            meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "stat_has_stats"), PersistentDataType.INTEGER, 1);
            meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "cwe_is_weapon"), PersistentDataType.INTEGER, 1);
            result.setItemMeta(meta);
        }
        ShapedRecipe recipe = new ShapedRecipe(key, result);
        recipe.shape(" E ", " S ", " S ");
        recipe.setIngredient('E', new RecipeChoice.ExactChoice(getCraftMaterial(Material.BEACON, "§6§lLõi Năng Lượng Cao Cấp", "LEGENDARY")));
        recipe.setIngredient('S', Material.STICK);
        try { Bukkit.removeRecipe(key); } catch (Exception ignored) {}
        try { Bukkit.addRecipe(recipe); } catch (Exception e) {}
    }

    

    private void registerBerserkSet() {
        createArmorRecipe("cwe_berserk_helmet", Material.IRON_HELMET, "§cBerserk Helmet", "cwe_berserk_helmet", "III", "I I", "   ", Material.IRON_BLOCK, "§6§lEPIC ARMOR", 80.0, 40.0, 0.0, 0.0, 0.0, 25.0, 0.0);
        createArmorRecipe("cwe_berserk_chest", Material.IRON_CHESTPLATE, "§cBerserk Chestplate", "cwe_berserk_chestplate", "I I", "III", "III", Material.IRON_BLOCK, "§6§lEPIC ARMOR", 100.0, 60.0, 0.0, 0.0, 0.0, 30.0, 0.0);
        createArmorRecipe("cwe_berserk_legs", Material.IRON_LEGGINGS, "§cBerserk Leggings", "cwe_berserk_leggings", "III", "I I", "I I", Material.IRON_BLOCK, "§6§lEPIC ARMOR", 90.0, 50.0, 0.0, 0.0, 0.0, 20.0, 0.0);
        createArmorRecipe("cwe_berserk_boots", Material.IRON_BOOTS, "§cBerserk Boots", "cwe_berserk_boots", "   ", "I I", "I I", Material.IRON_BLOCK, "§6§lEPIC ARMOR", 70.0, 30.0, 0.0, 0.0, 0.0, 15.0, 0.0);
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
        ItemStack result = new ItemStack(mat);
        ItemMeta meta = result.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            meta.setUnbreakable(true);
            meta.setLore(Arrays.asList(
                "§7Health: §a+" + hp,
                "§7Defense: §a+" + def,
                (spd > 0 ? "§7Speed: §a+" + spd : ""),
                (atkSpd > 0 ? "§7Attack Speed: §a+" + atkSpd : ""),
                (critC > 0 ? "§7Crit Chance: §c+" + critC + "%" : ""),
                (str > 0 ? "§7Strength: §c+" + str : ""),
                (intel > 0 ? "§7Intelligence: §a+" + intel : ""),
                "§f",
                rarityLore
            ));
            meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "stat_health"), PersistentDataType.DOUBLE, hp);
            meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "stat_defense"), PersistentDataType.DOUBLE, def);
            if(spd > 0) meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "stat_speed"), PersistentDataType.DOUBLE, spd);
            if(atkSpd > 0) meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "stat_attack_speed"), PersistentDataType.DOUBLE, atkSpd);
            if(critC > 0) meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "stat_crit_chance"), PersistentDataType.DOUBLE, critC);
            if(str > 0) meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "stat_strength"), PersistentDataType.DOUBLE, str);
            if(intel > 0) meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "stat_intelligence"), PersistentDataType.DOUBLE, intel);
            meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "stat_has_stats"), PersistentDataType.INTEGER, 1);
            meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "cwe_id"), PersistentDataType.STRING, cweId);
            result.setItemMeta(meta);
        }
        ShapedRecipe recipe = new ShapedRecipe(key, result);
        recipe.shape(r1, r2, r3);
        recipe.setIngredient('I', ingredient);
        try { Bukkit.removeRecipe(key); } catch (Exception ignored) {}
        try { Bukkit.addRecipe(recipe); } catch (Exception e) {}
    }

    private void createAdvancedArmorRecipe(String keyStr, Material mat, String name, String cweId, String r1, String r2, String r3, Material baseMat, String rarityLore, double hp, double def, double spd, double atkSpd, double critC, double str, double intel) {
        NamespacedKey key = new NamespacedKey(plugin, keyStr);
        ItemStack result = new ItemStack(mat);
        ItemMeta meta = result.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            meta.setUnbreakable(true);
            meta.setLore(Arrays.asList(
                "§7Health: §a+" + hp,
                "§7Defense: §a+" + def,
                (spd > 0 ? "§7Speed: §a+" + spd : ""),
                (atkSpd > 0 ? "§7Attack Speed: §a+" + atkSpd : ""),
                (critC > 0 ? "§7Crit Chance: §c+" + critC + "%" : ""),
                (str > 0 ? "§7Strength: §c+" + str : ""),
                (intel > 0 ? "§7Intelligence: §a+" + intel : ""),
                "§f",
                rarityLore
            ));
            // Add custom Set Bonus for Phantom Ranger
            if (cweId.contains("phantom_ranger")) {
                List<String> lore = meta.getLore();
                lore.add(2, "§7Crit Damage: §c+25%");
                meta.setLore(lore);
                meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "stat_crit_damage"), PersistentDataType.DOUBLE, 25.0);
            }
            
            meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "stat_health"), PersistentDataType.DOUBLE, hp);
            meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "stat_defense"), PersistentDataType.DOUBLE, def);
            if(spd > 0) meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "stat_speed"), PersistentDataType.DOUBLE, spd);
            if(atkSpd > 0) meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "stat_attack_speed"), PersistentDataType.DOUBLE, atkSpd);
            if(critC > 0) meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "stat_crit_chance"), PersistentDataType.DOUBLE, critC);
            if(str > 0) meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "stat_strength"), PersistentDataType.DOUBLE, str);
            if(intel > 0) meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "stat_intelligence"), PersistentDataType.DOUBLE, intel);
            meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "stat_has_stats"), PersistentDataType.INTEGER, 1);
            meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "cwe_id"), PersistentDataType.STRING, cweId);
            result.setItemMeta(meta);
        }
        ShapedRecipe recipe = new ShapedRecipe(key, result);
        recipe.shape(r1, r2, r3);
        recipe.setIngredient('I', baseMat);
        recipe.setIngredient('C', new RecipeChoice.ExactChoice(getCraftMaterial(Material.DRAGON_BREATH, "§5Băng Tinh Cổ Đại", "EPIC")));
        try { Bukkit.removeRecipe(key); } catch (Exception ignored) {}
        try { Bukkit.addRecipe(recipe); } catch (Exception e) {}
    }
}

package org.example.bazaar;

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
import org.bukkit.Material;
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
import org.bukkit.enchantments.Enchantment;
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
import org.bukkit.event.block.Action;
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
import org.bukkit.event.inventory.PrepareItemCraftEvent;
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
import org.bukkit.inventory.CraftingInventory;
import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
import org.bukkit.inventory.ItemFlag;
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
import org.bukkit.inventory.meta.ItemMeta;
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
import java.util.Arrays;
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
public class BazaarRecipeListener implements Listener {
    private final JavaPlugin plugin;

    private final Map<Material, String[]> crossRecipes = new HashMap<>();
    private final Map<String, String[]> blockRecipes = new HashMap<>();

    /** Expose crossRecipes for AutoCraftBag to use */
    public Map<Material, String[]> getCrossRecipes() { return crossRecipes; }

    public BazaarRecipeListener(JavaPlugin plugin) {
        this.plugin = plugin;
        setupAutomatedRecipes();
    }

    private void setupAutomatedRecipes() {
        registerCross(Material.WHEAT, "ENCHANTED_WHEAT", "§aEnchanted Wheat", Material.WHEAT);
        registerCross(Material.CARROT, "ENCHANTED_CARROT", "§aEnchanted Carrot", Material.CARROT);
        registerCross(Material.POTATO, "ENCHANTED_POTATO", "§aEnchanted Potato", Material.POTATO);
        registerCross(Material.PUMPKIN, "ENCHANTED_PUMPKIN", "§aEnchanted Pumpkin", Material.PUMPKIN);
        registerCross(Material.MELON_SLICE, "ENCHANTED_MELON", "§aEnchanted Melon Slice", Material.MELON_SLICE);
        registerCross(Material.SUGAR_CANE, "ENCHANTED_SUGAR_CANE", "§aEnchanted Sugar Cane", Material.SUGAR_CANE);
        registerCross(Material.NETHER_WART, "ENCHANTED_NETHER_WART", "§aEnchanted Nether Wart", Material.NETHER_WART);
        registerCross(Material.COCOA_BEANS, "ENCHANTED_COCOA_BEANS", "§aEnchanted Cocoa Beans", Material.COCOA_BEANS);
        registerCross(Material.CACTUS, "ENCHANTED_CACTUS", "§aEnchanted Cactus", Material.CACTUS);
        registerCross(Material.RED_MUSHROOM, "ENCHANTED_RED_MUSHROOM", "§aEnchanted Red Mushroom", Material.RED_MUSHROOM);
        registerCross(Material.BROWN_MUSHROOM, "ENCHANTED_BROWN_MUSHROOM", "§aEnchanted Brown Mushroom", Material.BROWN_MUSHROOM);

        registerCross(Material.ROTTEN_FLESH, "ENCHANTED_ROTTEN_FLESH", "§aEnchanted Rotten Flesh", Material.ROTTEN_FLESH);
        registerCross(Material.BEEF, "ENCHANTED_BEEF", "§aEnchanted Raw Beef", Material.BEEF);
        registerCross(Material.PORKCHOP, "ENCHANTED_PORKCHOP", "§aEnchanted Raw Porkchop", Material.PORKCHOP);
        registerCross(Material.CHICKEN, "ENCHANTED_CHICKEN", "§aEnchanted Raw Chicken", Material.CHICKEN);
        registerCross(Material.MUTTON, "ENCHANTED_MUTTON", "§aEnchanted Raw Mutton", Material.MUTTON);
        registerCross(Material.RABBIT, "ENCHANTED_RABBIT", "§aEnchanted Raw Rabbit", Material.RABBIT);
        registerCross(Material.BONE, "ENCHANTED_BONE", "§aEnchanted Bone", Material.BONE);
        registerCross(Material.GUNPOWDER, "ENCHANTED_GUNPOWDER", "§aEnchanted Gunpowder", Material.GUNPOWDER);
        registerCross(Material.ENDER_PEARL, "ENCHANTED_ENDER_PEARL", "§aEnchanted Ender Pearl", Material.ENDER_PEARL);
        registerCross(Material.SPIDER_EYE, "ENCHANTED_SPIDER_EYE", "§aEnchanted Spider Eye", Material.SPIDER_EYE);
        registerCross(Material.STRING, "ENCHANTED_STRING", "§aEnchanted String", Material.STRING);
        registerCross(Material.SLIME_BALL, "ENCHANTED_SLIME_BALL", "§aEnchanted Slimeball", Material.SLIME_BALL);

        registerCross(Material.COAL, "ENCHANTED_COAL", "§9Enchanted Coal", Material.COAL);
        registerCross(Material.IRON_INGOT, "ENCHANTED_IRON", "§9Enchanted Iron Ingot", Material.IRON_INGOT);
        registerCross(Material.GOLD_INGOT, "ENCHANTED_GOLD", "§9Enchanted Gold Ingot", Material.GOLD_INGOT);
        registerCross(Material.DIAMOND, "ENCHANTED_DIAMOND", "§9Enchanted Diamond", Material.DIAMOND);
        registerCross(Material.EMERALD, "ENCHANTED_EMERALD", "§9Enchanted Emerald", Material.EMERALD);
        registerCross(Material.LAPIS_LAZULI, "ENCHANTED_LAPIS", "§9Enchanted Lapis Lazuli", Material.LAPIS_LAZULI);
        registerCross(Material.REDSTONE, "ENCHANTED_REDSTONE", "§9Enchanted Redstone", Material.REDSTONE);
        registerCross(Material.QUARTZ, "ENCHANTED_QUARTZ", "§9Enchanted Quartz", Material.QUARTZ);
        // Cobblestone & Deepslate
        registerCross(Material.COBBLESTONE, "ENCHANTED_COBBLESTONE", "§9Enchanted Cobblestone", Material.COBBLESTONE);
        registerCross(Material.COBBLED_DEEPSLATE, "ENCHANTED_COBBLED_DEEPSLATE", "§9Enchanted Cobbled Deepslate", Material.COBBLED_DEEPSLATE);

        registerBlock("ENCHANTED_COAL", "ENCHANTED_COAL_BLOCK", "§5Enchanted Block of Coal", Material.COAL_BLOCK);
        registerBlock("ENCHANTED_IRON", "ENCHANTED_IRON_BLOCK", "§5Enchanted Block of Iron", Material.IRON_BLOCK);
        registerBlock("ENCHANTED_GOLD", "ENCHANTED_GOLD_BLOCK", "§5Enchanted Block of Gold", Material.GOLD_BLOCK);
        registerBlock("ENCHANTED_DIAMOND", "ENCHANTED_DIAMOND_BLOCK", "§5Enchanted Block of Diamond", Material.DIAMOND_BLOCK);
        registerBlock("ENCHANTED_EMERALD", "ENCHANTED_EMERALD_BLOCK", "§5Enchanted Block of Emerald", Material.EMERALD_BLOCK);
        registerBlock("ENCHANTED_LAPIS", "ENCHANTED_LAPIS_BLOCK", "§5Enchanted Block of Lapis Lazuli", Material.LAPIS_BLOCK);
        registerBlock("ENCHANTED_REDSTONE", "ENCHANTED_REDSTONE_BLOCK", "§5Enchanted Block of Redstone", Material.REDSTONE_BLOCK);
        registerBlock("ENCHANTED_QUARTZ", "ENCHANTED_QUARTZ_BLOCK", "§5Enchanted Block of Quartz", Material.QUARTZ_BLOCK);
        registerBlock("ENCHANTED_COBBLESTONE", "ENCHANTED_COBBLESTONE_BLOCK", "§5Enchanted Cobblestone Block", Material.COBBLESTONE);
        registerBlock("ENCHANTED_COBBLED_DEEPSLATE", "ENCHANTED_COBBLED_DEEPSLATE_BLOCK", "§5Enchanted Cobbled Deepslate Block", Material.COBBLED_DEEPSLATE);
    }

    private void registerCross(Material input, String id, String displayName, Material outputMat) { crossRecipes.put(input, new String[]{id, displayName, outputMat.name()}); }
    private void registerBlock(String inputId, String id, String displayName, Material outputBlockMat) { blockRecipes.put(inputId, new String[]{id, displayName, outputBlockMat.name()}); }

    @EventHandler
    public void onPrepareCraft(PrepareItemCraftEvent event) {
        ItemStack result = event.getInventory().getResult();
        NamespacedKey key = new NamespacedKey(plugin, "bazaar_id");
        
        if (result != null && result.hasItemMeta() && result.getItemMeta().getPersistentDataContainer().has(key, PersistentDataType.STRING)) {
            event.getInventory().setResult(null);
        }

        ItemStack[] matrix = event.getInventory().getMatrix();
        if (matrix == null || matrix.length < 9) return;

        if (matrix[4] != null && (matrix[4].getType() == Material.GLASS_BOTTLE || matrix[4].getType() == Material.POTION)) {
            if (checkXpMatrixPattern(matrix, Material.LAPIS_LAZULI, null, 1)) {
                event.getInventory().setResult(createCustomXpBottle(1, "§fExperience Bottle", 250, "COMMON"));
                return;
            }
            if (checkXpMatrixPattern(matrix, Material.LAPIS_LAZULI, "ENCHANTED_LAPIS", 1)) {
                event.getInventory().setResult(createCustomXpBottle(2, "§aGrand Experience Bottle", 3000, "UNCOMMON"));
                return;
            }
            if (checkXpMatrixPattern(matrix, Material.LAPIS_BLOCK, "ENCHANTED_LAPIS_BLOCK", 1)) {
                event.getInventory().setResult(createCustomXpBottle(3, "§9Titanic Experience Bottle", 15000, "RARE"));
                return;
            }
        }

        ItemStack firstItem = null;
        for (ItemStack item : matrix) { if (item != null && item.getType() != Material.AIR) { firstItem = item; break; } }
        if (firstItem == null) return;

        if (crossRecipes.containsKey(firstItem.getType())) {
            Material baseMat = firstItem.getType();
            if (validateCrossPattern(matrix, baseMat, 32)) {
                String[] data = crossRecipes.get(baseMat);
                triggerOutput(event, Material.getMaterial(data[2]), data[1], data[0]);
                return;
            }
        }

        if (firstItem.hasItemMeta()) {
            PersistentDataContainer pdc = firstItem.getItemMeta().getPersistentDataContainer();
            if (pdc.has(key, PersistentDataType.STRING)) {
                String insideId = pdc.get(key, PersistentDataType.STRING);
                if (blockRecipes.containsKey(insideId)) {
                    if (validateFullBlockPattern(matrix, insideId, 32)) {
                        String[] data = blockRecipes.get(insideId);
                        triggerOutput(event, Material.getMaterial(data[2]), data[1], data[0]);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getInventory() instanceof CraftingInventory)) return;
        if (event.getRawSlot() != 0) return; 

        ItemStack result = event.getCurrentItem();
        if (result == null || !result.hasItemMeta()) return;

        NamespacedKey key = new NamespacedKey(plugin, "bazaar_id");
        PersistentDataContainer pdc = result.getItemMeta().getPersistentDataContainer();
        if (!pdc.has(key, PersistentDataType.STRING)) return;

        String bzId = pdc.get(key, PersistentDataType.STRING);
        boolean isXpBottle = bzId.startsWith("CWE_XP_BOTTLE_T");

        Player player = (Player) event.getWhoClicked();
        if (event.isShiftClick()) { event.setCancelled(true); player.sendMessage("§cKhông thể Shift-Click để chế tạo vật phẩm đặc biệt!"); return; }

        CraftingInventory inv = (CraftingInventory) event.getInventory();
        ItemStack[] matrix = inv.getMatrix();
        
        boolean isCross = false;
        boolean isBlock = false;

        if (!isXpBottle) {
            ItemStack firstItem = null;
            for (ItemStack item : matrix) { if (item != null && item.getType() != Material.AIR) { firstItem = item; break; } }
            if (firstItem == null) return;

            isCross = crossRecipes.containsKey(firstItem.getType());
            if (!isCross && firstItem.hasItemMeta()) {
                String insideId = firstItem.getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.STRING);
                if (insideId != null && blockRecipes.containsKey(insideId)) isBlock = true;
            }
            if (!isCross && !isBlock) return;
        }

        event.setCancelled(true);

        final ItemStack finalResult = result.clone();
        final boolean finalIsCross = isCross;
        final boolean finalIsBlock = isBlock;
        final boolean finalIsXp = isXpBottle;
        final String finalXpId = bzId;

        Bukkit.getScheduler().runTask(plugin, () -> {
            ItemStack[] currentMatrix = inv.getMatrix();
            
            if (finalIsCross) {
                int[] crossSlots = {1, 3, 4, 5, 7};
                for (int slot : crossSlots) { if (currentMatrix[slot] != null) { int amt = currentMatrix[slot].getAmount() - 32; currentMatrix[slot] = amt <= 0 ? null : currentMatrix[slot]; if(currentMatrix[slot]!=null) currentMatrix[slot].setAmount(amt); } }
            } else if (finalIsBlock) {
                int[] crossSlots = {1, 3, 4, 5, 7};
                for (int slot : crossSlots) { if (currentMatrix[slot] != null) { int amt = currentMatrix[slot].getAmount() - 32; currentMatrix[slot] = amt <= 0 ? null : currentMatrix[slot]; if(currentMatrix[slot]!=null) currentMatrix[slot].setAmount(amt); } }
            } else if (finalIsXp) {
                int deductAmount = 1;
                int[] outerSlots = {1, 3, 5, 6, 7, 8};
                for (int slot : outerSlots) { if (currentMatrix[slot] != null) { int amt = currentMatrix[slot].getAmount() - deductAmount; currentMatrix[slot] = amt <= 0 ? null : currentMatrix[slot]; if(currentMatrix[slot]!=null) currentMatrix[slot].setAmount(amt); } }
                if (currentMatrix[4] != null) { int amt = currentMatrix[4].getAmount() - 1; currentMatrix[4] = amt <= 0 ? null : currentMatrix[4]; if(currentMatrix[4]!=null) currentMatrix[4].setAmount(amt); }
            }

            inv.setMatrix(currentMatrix);
            ItemStack cursor = player.getItemOnCursor();
            if (cursor == null || cursor.getType() == Material.AIR) player.setItemOnCursor(finalResult);
            else if (cursor.isSimilar(finalResult)) cursor.setAmount(cursor.getAmount() + 1);
            player.updateInventory();
        });
    }

    @EventHandler
    public void onExpBottleLaunch(org.bukkit.event.entity.ProjectileLaunchEvent event) {
        if (!(event.getEntity() instanceof org.bukkit.entity.ThrownExpBottle)) return;
        org.bukkit.entity.ThrownExpBottle bottle = (org.bukkit.entity.ThrownExpBottle) event.getEntity();
        
        ItemStack item = bottle.getItem();
        if (item == null || !item.hasItemMeta()) return;

        PersistentDataContainer container = item.getItemMeta().getPersistentDataContainer();
        NamespacedKey idKey = new NamespacedKey(plugin, "bazaar_id");
        NamespacedKey xpKey = new NamespacedKey(plugin, "xp_payout");

        if (container.has(idKey, PersistentDataType.STRING) && container.get(idKey, PersistentDataType.STRING).startsWith("CWE_XP_BOTTLE_T")) {
            int xpToAdd = container.getOrDefault(xpKey, PersistentDataType.INTEGER, 0);
            if (xpToAdd > 0) {
                bottle.setMetadata("cwe_custom_xp", new org.bukkit.metadata.FixedMetadataValue(plugin, xpToAdd));
            }
        }
    }

    @EventHandler
    public void onExpBottleSplash(org.bukkit.event.entity.ExpBottleEvent event) {
        org.bukkit.entity.ThrownExpBottle bottle = event.getEntity();
        if (bottle.hasMetadata("cwe_custom_xp")) {
            int xp = bottle.getMetadata("cwe_custom_xp").get(0).asInt();
            event.setExperience(xp);
        }
    }

    private boolean checkXpMatrixPattern(ItemStack[] matrix, Material mat, String expectedBazaarId, int requiredAmount) {
        int[] itemSlots = {1, 3, 5, 6, 7, 8};
        int[] emptySlots = {0, 2};
        NamespacedKey key = new NamespacedKey(plugin, "bazaar_id");
        
        for (int slot : itemSlots) {
            ItemStack item = matrix[slot];
            if (item == null || item.getType() != mat || item.getAmount() < requiredAmount) return false;
            
            if (expectedBazaarId == null) {
                if (item.hasItemMeta() && item.getItemMeta().getPersistentDataContainer().has(key, PersistentDataType.STRING)) return false;
            } else {
                if (!item.hasItemMeta()) return false;
                String bzId = item.getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.STRING);
                if (!expectedBazaarId.equals(bzId)) return false;
            }
        }
        
        for (int slot : emptySlots) {
            ItemStack item = matrix[slot];
            if (item != null && item.getType() != Material.AIR) return false;
        }
        return true;
    }

    private ItemStack createCustomXpBottle(int tier, String displayName, int xpPayout, String rarity) {
        ItemStack item = new ItemStack(Material.EXPERIENCE_BOTTLE, 1);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(displayName);
            meta.setLore(Arrays.asList(
                "§7Grants §3" + String.format("%,d", xpPayout) + " §7Experience",
                "§7when thrown.",
                "",
                "§eRight-click to throw!",
                "§f",
                org.example.stats.ItemStatsGUI.Rarity.valueOf(rarity).display
            ));
            meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "bazaar_id"), PersistentDataType.STRING, "CWE_XP_BOTTLE_T" + tier);
            meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "xp_payout"), PersistentDataType.INTEGER, xpPayout);
            meta.getPersistentDataContainer().set(new NamespacedKey(plugin, org.example.stats.ItemStatsGUI.KEY_RARITY), PersistentDataType.STRING, rarity);
            
            try {
                meta.addEnchant(Enchantment.getByKey(NamespacedKey.minecraft("luck_of_the_sea")), 1, true);
            } catch (Throwable ignored) {}
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            item.setItemMeta(meta);
        }
        return item;
    }

    private boolean validateCrossPattern(ItemStack[] matrix, Material mat, int requiredAmount) {
        int[] crossSlots = {1, 3, 4, 5, 7}; int[] emptySlots = {0, 2, 6, 8}; NamespacedKey key = new NamespacedKey(plugin, "bazaar_id");
        for (int slot : crossSlots) { ItemStack item = matrix[slot]; if (item == null || item.getType() != mat || item.getAmount() < requiredAmount) return false; if (item.hasItemMeta() && item.getItemMeta().getPersistentDataContainer().has(key, PersistentDataType.STRING)) return false; }
        for (int slot : emptySlots) { ItemStack item = matrix[slot]; if (item != null && item.getType() != Material.AIR) return false; }
        return true;
    }

    private boolean validateFullBlockPattern(ItemStack[] matrix, String targetBazaarId, int requiredAmount) {
        NamespacedKey key = new NamespacedKey(plugin, "bazaar_id");
        int[] crossSlots = {1, 3, 4, 5, 7};
        int[] emptySlots = {0, 2, 6, 8};
        
        for (int slot : crossSlots) {
            ItemStack item = matrix[slot];
            if (item == null || item.getType() == Material.AIR || !item.hasItemMeta()) return false;
            PersistentDataContainer pdc = item.getItemMeta().getPersistentDataContainer();
            if (!pdc.has(key, PersistentDataType.STRING) || !pdc.get(key, PersistentDataType.STRING).equals(targetBazaarId) || item.getAmount() < requiredAmount) return false;
        }
        
        for (int slot : emptySlots) {
            ItemStack item = matrix[slot];
            if (item != null && item.getType() != Material.AIR) return false;
        }
        return true;
    }

    private void triggerOutput(PrepareItemCraftEvent event, Material mat, String display, String bazaarId) {
        ItemStack result = new ItemStack(mat, 1); ItemMeta meta = result.getItemMeta();
        if (meta != null) { meta.setDisplayName(display); meta.setLore(Arrays.asList("§7Vật phẩm ma thuật nén cao cấp", "§7chuyên dụng giao dịch tại Chợ Bazaar.", "", "§cKhông thể ăn hay sử dụng thô!")); NamespacedKey key = new NamespacedKey(plugin, "bazaar_id"); meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, bazaarId); try { meta.addEnchant(Enchantment.getByKey(NamespacedKey.minecraft("luck_of_the_sea")), 1, true); } catch(Throwable ignored){} meta.addItemFlags(ItemFlag.HIDE_ENCHANTS); result.setItemMeta(meta); }
        event.getInventory().setResult(result);
    }

    @EventHandler
    public void onPrepareMendingCraft(PrepareItemCraftEvent event) {
        ItemStack[] matrix = event.getInventory().getMatrix();
        if (matrix == null || matrix.length < 9) return;

        boolean matches = true;

        // Check center
        ItemStack center = matrix[4];
        if (center == null || center.getType() != Material.BOOK) matches = false;

        // Check Up, Down, Left, Right
        int[] crossSlots = {1, 3, 5, 7};
        for (int slot : crossSlots) {
            ItemStack item = matrix[slot];
            if (item == null || item.getType() != Material.IRON_INGOT) matches = false;
        }

        // Check corners (must be empty or air)
        int[] cornerSlots = {0, 2, 6, 8};
        for (int slot : cornerSlots) {
            ItemStack item = matrix[slot];
            if (item != null && item.getType() != Material.AIR) matches = false;
        }

        if (matches) {
            ItemStack mendingBook = new ItemStack(Material.ENCHANTED_BOOK);
            org.bukkit.inventory.meta.EnchantmentStorageMeta mMeta = (org.bukkit.inventory.meta.EnchantmentStorageMeta) mendingBook.getItemMeta();
            if (mMeta != null) {
                mMeta.setDisplayName("§d§lMending I");
                mMeta.addStoredEnchant(org.bukkit.enchantments.Enchantment.getByKey(NamespacedKey.minecraft("mending")), 1, true);
                mMeta.setLore(Arrays.asList("§d§lĐẶC QUYỀN CHẾ TẠO"));
                mMeta.getPersistentDataContainer().set(new NamespacedKey(plugin, "is_custom_mending"), PersistentDataType.INTEGER, 1);
                mMeta.getPersistentDataContainer().set(new NamespacedKey(plugin, "enchant_mending"), PersistentDataType.INTEGER, 1);
                mendingBook.setItemMeta(mMeta);
            }
            event.getInventory().setResult(mendingBook);
        } else {
            ItemStack result = event.getInventory().getResult();
            if (result != null && result.getType() == Material.ENCHANTED_BOOK && result.hasItemMeta()) {
                if (result.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(plugin, "is_custom_mending"), PersistentDataType.INTEGER)) {
                    event.getInventory().setResult(null);
                }
            }
        }
    }

    @EventHandler
    public void onMendingCraftClick(org.bukkit.event.inventory.CraftItemEvent event) {
        ItemStack result = event.getRecipe().getResult();
        if (result == null || result.getType() != Material.ENCHANTED_BOOK || !result.hasItemMeta()) return;

        PersistentDataContainer pdc = result.getItemMeta().getPersistentDataContainer();
        NamespacedKey mendingKey = new NamespacedKey(plugin, "is_custom_mending");
        if (!pdc.has(mendingKey, PersistentDataType.INTEGER)) return;

        Player player = (Player) event.getWhoClicked();

        if (event.isShiftClick()) {
            event.setCancelled(true);
            player.sendMessage("§c[CWE] Không thể Shift-Click để chế tạo Mending đặc quyền!");
            return;
        }

        event.setCancelled(true);

        CraftingInventory inv = event.getInventory();
        ItemStack[] matrix = inv.getMatrix();

        if (matrix[4] != null) {
            int newAmt = matrix[4].getAmount() - 1;
            matrix[4] = newAmt <= 0 ? null : matrix[4];
            if (matrix[4] != null) matrix[4].setAmount(newAmt);
        }

        int[] crossSlots = {1, 3, 5, 7};
        for (int slot : crossSlots) {
            if (matrix[slot] != null) {
                int newAmt = matrix[slot].getAmount() - 1;
                matrix[slot] = newAmt <= 0 ? null : matrix[slot];
                if (matrix[slot] != null) matrix[slot].setAmount(newAmt);
            }
        }

        inv.setMatrix(matrix);

        ItemStack cursor = player.getItemOnCursor();
        if (cursor == null || cursor.getType() == Material.AIR) {
            player.setItemOnCursor(result.clone());
        } else if (cursor.isSimilar(result)) {
            cursor.setAmount(cursor.getAmount() + 1);
        } else {
            for (ItemStack leftover : player.getInventory().addItem(result.clone()).values()) {
                player.getWorld().dropItemNaturally(player.getLocation(), leftover);
            }
        }
        player.updateInventory();
    }

    @EventHandler
    public void onVillagerAcquireMending(org.bukkit.event.entity.VillagerAcquireTradeEvent event) {
        org.bukkit.inventory.MerchantRecipe recipe = event.getRecipe();
        ItemStack result = recipe.getResult();
        if (result != null && result.getType() == Material.ENCHANTED_BOOK) {
            org.bukkit.inventory.meta.EnchantmentStorageMeta mMeta = (org.bukkit.inventory.meta.EnchantmentStorageMeta) result.getItemMeta();
            if (mMeta != null && mMeta.hasStoredEnchant(org.bukkit.enchantments.Enchantment.getByKey(NamespacedKey.minecraft("mending")))) {
                mMeta.removeStoredEnchant(org.bukkit.enchantments.Enchantment.getByKey(NamespacedKey.minecraft("mending")));
                mMeta.addStoredEnchant(org.bukkit.enchantments.Enchantment.getByKey(NamespacedKey.minecraft("unbreaking")), 1, true);
                result.setItemMeta(mMeta);

                org.bukkit.inventory.MerchantRecipe newRecipe = new org.bukkit.inventory.MerchantRecipe(
                    result,
                    recipe.getUses(),
                    recipe.getMaxUses(),
                    recipe.hasExperienceReward(),
                    recipe.getVillagerExperience(),
                    recipe.getPriceMultiplier()
                );
                newRecipe.setIngredients(recipe.getIngredients());
                event.setRecipe(newRecipe);
            }
        }
    }
}

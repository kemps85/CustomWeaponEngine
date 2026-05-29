package org.example.system;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class RecipeGUI implements Listener, CommandExecutor {
    private final JavaPlugin plugin;
    private final List<RecipeEntry> recipes = new ArrayList<>();

    public RecipeGUI(JavaPlugin plugin) {
        this.plugin = plugin;
        Bukkit.getScheduler().runTaskLater(plugin, this::loadRecipes, 20L); // Load after all recipes are registered
    }

    private static class RecipeEntry {
        String id;
        ItemStack result;
        ItemStack[] grid = new ItemStack[9]; // 3x3 grid
        
        RecipeEntry(String id, ItemStack result) {
            this.id = id;
            this.result = result;
        }
    }

    private ItemStack createBazaarItem(Material mat, String name, String bzId) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            meta.setLore(Arrays.asList("§7Vật phẩm ma thuật nén cao cấp", "§7chuyên dụng giao dịch tại Chợ Bazaar."));
            meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "bazaar_id"), PersistentDataType.STRING, bzId);
            try { meta.addEnchant(org.bukkit.enchantments.Enchantment.getByKey(NamespacedKey.minecraft("luck_of_the_sea")), 1, true); } catch(Exception ignored){}
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            item.setItemMeta(meta);
        }
        return item;
    }

    private ItemStack createXpBottle(int tier, String name, String rarity, int xp) {
        ItemStack item = new ItemStack(Material.EXPERIENCE_BOTTLE);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            meta.setLore(Arrays.asList("§7Grants §3" + String.format("%,d", xp) + " §7Experience", "§7when thrown.", "", "§eRight-click to throw!", "§f", org.example.stats.ItemStatsGUI.Rarity.valueOf(rarity).display));
            meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "bazaar_id"), PersistentDataType.STRING, "CWE_XP_BOTTLE_T" + tier);
            item.setItemMeta(meta);
        }
        return item;
    }

    private void loadRecipes() {
        recipes.clear();
        
        // Load dynamically from Bukkit's recipe registry
        Iterator<org.bukkit.inventory.Recipe> iter = Bukkit.recipeIterator();
        while (iter.hasNext()) {
            org.bukkit.inventory.Recipe r = iter.next();
            if (r instanceof org.bukkit.Keyed) {
                NamespacedKey key = ((org.bukkit.Keyed) r).getKey();
                if (key.getNamespace().equals(plugin.getName().toLowerCase()) || key.getKey().startsWith("cwe_") || key.getKey().startsWith("recipe_")) {
                    if (r instanceof org.bukkit.inventory.ShapedRecipe) {
                        org.bukkit.inventory.ShapedRecipe sr = (org.bukkit.inventory.ShapedRecipe) r;
                        RecipeEntry entry = new RecipeEntry(key.getKey(), sr.getResult());
                        
                        String[] shape = sr.getShape();
                        for (int row = 0; row < shape.length; row++) {
                            String rowStr = shape[row];
                            for (int col = 0; col < rowStr.length(); col++) {
                                char c = rowStr.charAt(col);
                                org.bukkit.inventory.RecipeChoice choice = sr.getChoiceMap().get(c);
                                if (choice instanceof org.bukkit.inventory.RecipeChoice.ExactChoice) {
                                    entry.grid[row * 3 + col] = ((org.bukkit.inventory.RecipeChoice.ExactChoice) choice).getChoices().get(0);
                                } else if (choice instanceof org.bukkit.inventory.RecipeChoice.MaterialChoice) {
                                    entry.grid[row * 3 + col] = new ItemStack(((org.bukkit.inventory.RecipeChoice.MaterialChoice) choice).getChoices().get(0));
                                } else if (sr.getIngredientMap().get(c) != null) {
                                    entry.grid[row * 3 + col] = sr.getIngredientMap().get(c);
                                }
                            }
                        }
                        recipes.add(entry);
                    }
                }
            }
        }

        // Hardcode XP Bottles since they are handled via custom events in BazaarRecipeListener
        ItemStack air = new ItemStack(Material.AIR);
        ItemStack glass = new ItemStack(Material.GLASS_BOTTLE);
        ItemStack lapis1 = new ItemStack(Material.LAPIS_LAZULI);
        ItemStack lapis2 = createBazaarItem(Material.LAPIS_LAZULI, "§9Enchanted Lapis Lazuli", "ENCHANTED_LAPIS");
        ItemStack lapis3 = createBazaarItem(Material.LAPIS_BLOCK, "§5Enchanted Block of Lapis", "ENCHANTED_LAPIS_BLOCK");

        RecipeEntry xp1 = new RecipeEntry("cwe_xp_bottle_1", createXpBottle(1, "§fExperience Bottle", "COMMON", 250));
        xp1.grid = new ItemStack[]{air, lapis1, air, lapis1, glass, lapis1, lapis1, lapis1, lapis1};
        recipes.add(xp1);

        RecipeEntry xp2 = new RecipeEntry("cwe_xp_bottle_2", createXpBottle(2, "§aGrand Experience Bottle", "UNCOMMON", 3000));
        xp2.grid = new ItemStack[]{air, lapis2, air, lapis2, glass, lapis2, lapis2, lapis2, lapis2};
        recipes.add(xp2);

        RecipeEntry xp3 = new RecipeEntry("cwe_xp_bottle_3", createXpBottle(3, "§9Titanic Experience Bottle", "RARE", 15000));
        xp3.grid = new ItemStack[]{air, lapis3, air, lapis3, glass, lapis3, lapis3, lapis3, lapis3};
        recipes.add(xp3);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            openMainGUI((Player) sender, 0);
        }
        return true;
    }

    private void openMainGUI(Player player, int page) {
        Inventory gui = Bukkit.createInventory(null, 54, "§8Custom Recipes - Page " + (page + 1));
        
        int start = page * 45;
        for (int i = 0; i < 45; i++) {
            if (start + i < recipes.size()) {
                RecipeEntry entry = recipes.get(start + i);
                ItemStack display = entry.result.clone();
                ItemMeta meta = display.getItemMeta();
                if (meta != null) {
                    List<String> lore = meta.getLore();
                    if (lore == null) lore = new ArrayList<>();
                    lore.add("");
                    lore.add("§eClick để xem công thức!");
                    meta.setLore(lore);
                    meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "recipe_id"), PersistentDataType.STRING, entry.id);
                    display.setItemMeta(meta);
                }
                gui.setItem(i, display);
            }
        }

        ItemStack border = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta bm = border.getItemMeta(); bm.setDisplayName("§7 "); border.setItemMeta(bm);
        for (int i = 45; i < 54; i++) gui.setItem(i, border);

        if (page > 0) {
            ItemStack prev = new ItemStack(Material.ARROW);
            ItemMeta pm = prev.getItemMeta(); pm.setDisplayName("§aTrang Trước");
            pm.getPersistentDataContainer().set(new NamespacedKey(plugin, "recipe_page"), PersistentDataType.INTEGER, page - 1);
            prev.setItemMeta(pm);
            gui.setItem(45, prev);
        }

        if (start + 45 < recipes.size()) {
            ItemStack next = new ItemStack(Material.ARROW);
            ItemMeta nm = next.getItemMeta(); nm.setDisplayName("§aTrang Sau");
            nm.getPersistentDataContainer().set(new NamespacedKey(plugin, "recipe_page"), PersistentDataType.INTEGER, page + 1);
            next.setItemMeta(nm);
            gui.setItem(53, next);
        }

        player.openInventory(gui);
    }

    private void openRecipeView(Player player, String id) {
        RecipeEntry entry = null;
        for (RecipeEntry r : recipes) {
            if (r.id.equals(id)) {
                entry = r;
                break;
            }
        }
        if (entry == null) return;

        Inventory gui = Bukkit.createInventory(null, 54, "§8Công Thức: " + id);
        ItemStack bg = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta bm = bg.getItemMeta(); bm.setDisplayName("§7 "); bg.setItemMeta(bm);
        for (int i = 0; i < 54; i++) gui.setItem(i, bg);

        int[] gridSlots = {10, 11, 12, 19, 20, 21, 28, 29, 30};
        for (int i = 0; i < 9; i++) {
            if (entry.grid[i] != null && entry.grid[i].getType() != Material.AIR) {
                gui.setItem(gridSlots[i], entry.grid[i]);
            } else {
                gui.setItem(gridSlots[i], new ItemStack(Material.AIR));
            }
        }

        gui.setItem(24, entry.result);

        ItemStack back = new ItemStack(Material.ARROW);
        ItemMeta backM = back.getItemMeta(); backM.setDisplayName("§cQuay Lại");
        backM.getPersistentDataContainer().set(new NamespacedKey(plugin, "recipe_back"), PersistentDataType.INTEGER, 1);
        back.setItemMeta(backM);
        gui.setItem(49, back);

        player.openInventory(gui);
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        String title = event.getView().getTitle();
        if (title.startsWith("§8Custom Recipes") || title.startsWith("§8Công Thức:")) {
            event.setCancelled(true);
            Player player = (Player) event.getWhoClicked();
            ItemStack item = event.getCurrentItem();
            if (item == null || !item.hasItemMeta()) return;

            NamespacedKey idKey = new NamespacedKey(plugin, "recipe_id");
            NamespacedKey pageKey = new NamespacedKey(plugin, "recipe_page");
            NamespacedKey backKey = new NamespacedKey(plugin, "recipe_back");

            if (item.getItemMeta().getPersistentDataContainer().has(idKey, PersistentDataType.STRING)) {
                String id = item.getItemMeta().getPersistentDataContainer().get(idKey, PersistentDataType.STRING);
                openRecipeView(player, id);
            } else if (item.getItemMeta().getPersistentDataContainer().has(pageKey, PersistentDataType.INTEGER)) {
                int page = item.getItemMeta().getPersistentDataContainer().get(pageKey, PersistentDataType.INTEGER);
                openMainGUI(player, page);
            } else if (item.getItemMeta().getPersistentDataContainer().has(backKey, PersistentDataType.INTEGER)) {
                openMainGUI(player, 0);
            }
        }
    }
}

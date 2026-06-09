package org.example.bazaar;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * AutoCraftBag - Túi Nén Tự Động
 * Khi ở trong inventory, tự động nén 160 nguyên liệu thô thành 1 Enchanted tương ứng.
 * Không thể đặt xuống đất (cancel BlockPlaceEvent).
 * Craft bằng 7 Enchanted Cobblestone + 1 Bow + 1 Redstone (hình dạng Dispenser vanilla).
 */
public class AutoCraftBag implements Listener {

    private final JavaPlugin plugin;
    private final NamespacedKey BAG_KEY;
    private final NamespacedKey BAZAAR_ID_KEY;

    // Map: Material thô → [bazaar_id enchanted, display name, material_name]
    // Được nạp từ BazaarRecipeListener thông qua constructor
    private final Map<Material, String[]> crossRecipes;

    public AutoCraftBag(JavaPlugin plugin, Map<Material, String[]> crossRecipes) {
        this.plugin = plugin;
        this.crossRecipes = crossRecipes;
        this.BAG_KEY = new NamespacedKey(plugin, "cwe_autocraft_bag");
        this.BAZAAR_ID_KEY = new NamespacedKey(plugin, "bazaar_id");
        startAutoCraftTask();
    }

    // ────────────────────────────────────────────────────────────
    // Static helper: tạo item không cần instance (để ItemGenerator dùng)
    // ────────────────────────────────────────────────────────────

    public static ItemStack createBagItemStatic(JavaPlugin plugin) {
        ItemStack item = new ItemStack(Material.DISPENSER, 1);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("§6§lAuto-Craft Compactor");
            meta.setLore(Arrays.asList(
                "§7Khi nằm trong túi đồ,",
                "§7tự động nén §e160 §7nguyên liệu thô",
                "§7thành §a1 §7phiên bản Enchanted tương ứng.",
                "",
                "§8• Hoạt động mỗi 1 giây",
                "§8• Áp dụng cho MỌI loại nguyên liệu",
                "",
                "§c✖ Không thể đặt xuống đất!",
                "",
                "§5§lEPIC"
            ));
            try {
                meta.addEnchant(Enchantment.getByKey(NamespacedKey.minecraft("luck_of_the_sea")), 1, true);
            } catch (Throwable ignored) {}
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "cwe_autocraft_bag"), PersistentDataType.INTEGER, 1);
            item.setItemMeta(meta);
        }
        return item;
    }

    public static boolean isAutoCraftBagStatic(JavaPlugin plugin, ItemStack item) {
        if (item == null || !item.hasItemMeta()) return false;
        return item.getItemMeta().getPersistentDataContainer()
            .has(new NamespacedKey(plugin, "cwe_autocraft_bag"), PersistentDataType.INTEGER);
    }

    // ────────────────────────────────────────────────────────────
    // Instance methods (dùng trong listener)
    // ────────────────────────────────────────────────────────────

    /**
     * Tạo item AutoCraft Bag (hiển thị là DISPENSER với aura).
     */
    public ItemStack createBagItem() {
        return createBagItemStatic(plugin);
    }

    /**
     * Kiểm tra item có phải AutoCraft Bag không.
     */
    public boolean isAutoCraftBag(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return false;
        PersistentDataContainer pdc = item.getItemMeta().getPersistentDataContainer();
        return pdc.has(BAG_KEY, PersistentDataType.INTEGER);
    }

    // ─────────────────────────────────────────────────────────
    // Auto-Craft Task (mỗi 20 ticks = 1 giây)
    // ─────────────────────────────────────────────────────────

    private void startAutoCraftTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (!hasBagInInventory(player)) continue;
                    performAutoCraft(player);
                }
            }
        }.runTaskTimer(plugin, 20L, 20L);
    }

    /**
     * Kiểm tra player có bag trong inventory không (bao gồm cả hotbar).
     */
    private boolean hasBagInInventory(Player player) {
        for (ItemStack item : player.getInventory().getContents()) {
            if (isAutoCraftBag(item)) return true;
        }
        return false;
    }

    /**
     * Thực hiện nén nguyên liệu: duyệt qua crossRecipes, với mỗi loại
     * nếu player có >= 160 cái thì trừ 160, thêm 1 Enchanted.
     * Lặp lại cho đến khi không đủ nguyên liệu nữa.
     */
    private void performAutoCraft(Player player) {
        for (Map.Entry<Material, String[]> entry : crossRecipes.entrySet()) {
            Material rawMat = entry.getKey();
            String[] data = entry.getValue();
            // data[0] = enchanted bazaar_id, data[1] = display name, data[2] = output material name
            String enchantedId = data[0];
            String displayName = data[1];
            Material outputMat = Material.getMaterial(data[2]);
            if (outputMat == null) continue;

            // Đếm số lượng nguyên liệu thô KHÔNG phải custom item (không có bazaar_id PDC)
            int rawCount = countRawMaterial(player, rawMat);

            while (rawCount >= 160) {
                // Trừ 160 nguyên liệu thô
                removeRawMaterial(player, rawMat, 160);
                // Thêm 1 Enchanted item
                ItemStack enchanted = buildEnchantedItem(outputMat, displayName, enchantedId);
                HashMap<Integer, ItemStack> leftover = player.getInventory().addItem(enchanted);
                // Nếu không còn chỗ trong inventory, thả ra đất
                for (ItemStack drop : leftover.values()) {
                    player.getWorld().dropItemNaturally(player.getLocation(), drop);
                }
                rawCount -= 160;
            }
        }
    }

    /**
     * Đếm số lượng Material thô (không phải custom item) trong inventory.
     */
    private int countRawMaterial(Player player, Material mat) {
        int count = 0;
        for (ItemStack item : player.getInventory().getContents()) {
            if (item == null || item.getType() != mat) continue;
            // Bỏ qua nếu là custom item (có bazaar_id PDC)
            if (item.hasItemMeta() && item.getItemMeta().getPersistentDataContainer().has(BAZAAR_ID_KEY, PersistentDataType.STRING)) continue;
            count += item.getAmount();
        }
        return count;
    }

    /**
     * Xóa đúng 'amount' item thô (không phải custom) khỏi inventory.
     */
    private void removeRawMaterial(Player player, Material mat, int amount) {
        int remaining = amount;
        ItemStack[] contents = player.getInventory().getContents();
        for (int i = 0; i < contents.length && remaining > 0; i++) {
            ItemStack item = contents[i];
            if (item == null || item.getType() != mat) continue;
            if (item.hasItemMeta() && item.getItemMeta().getPersistentDataContainer().has(BAZAAR_ID_KEY, PersistentDataType.STRING)) continue;
            int toRemove = Math.min(remaining, item.getAmount());
            item.setAmount(item.getAmount() - toRemove);
            remaining -= toRemove;
        }
    }

    /**
     * Tạo Enchanted item với đầy đủ PDC + glow effect.
     */
    private ItemStack buildEnchantedItem(Material mat, String displayName, String bazaarId) {
        ItemStack item = new ItemStack(mat, 1);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(displayName);
            meta.setLore(Arrays.asList(
                "§7Vật phẩm ma thuật nén cao cấp",
                "§7chuyên dụng giao dịch tại Chợ Bazaar.",
                "",
                "§cKhông thể ăn hay sử dụng thô!"
            ));
            meta.getPersistentDataContainer().set(BAZAAR_ID_KEY, PersistentDataType.STRING, bazaarId);
            try {
                meta.addEnchant(Enchantment.getByKey(NamespacedKey.minecraft("luck_of_the_sea")), 1, true);
            } catch (Throwable ignored) {}
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            item.setItemMeta(meta);
        }
        return item;
    }

    // ─────────────────────────────────────────────────────────
    // Block Place Prevention
    // ─────────────────────────────────────────────────────────

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockPlace(BlockPlaceEvent event) {
        if (isAutoCraftBag(event.getItemInHand())) {
            event.setCancelled(true);
            event.getPlayer().sendMessage("§c[AutoCraft] Túi nén này không thể đặt xuống đất!");
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) {
            ItemStack item = event.getItem();
            if (isAutoCraftBag(item)) {
                event.setCancelled(true);
            }
        }
    }

    // ─────────────────────────────────────────────────────────
    // Craft Recipe (PrepareItemCraftEvent)
    // Công thức: 7 Enchanted Cobblestone + 1 Bow + 1 Redstone
    // Hình dạng Dispenser:
    //   [EC][EC][EC]
    //   [EC][BW][EC]
    //   [EC][RS][EC]
    // Slots: 0,1,2,3,5,6,8 = EC; 4 = Bow; 7 = Redstone
    // ─────────────────────────────────────────────────────────

    @EventHandler
    public void onPrepareAutoCraftBagCraft(PrepareItemCraftEvent event) {
        ItemStack[] matrix = event.getInventory().getMatrix();
        if (matrix == null || matrix.length < 9) return;

        if (validateBagRecipe(matrix)) {
            event.getInventory().setResult(createBagItem());
        }
    }

    /**
     * Validate công thức craft bag:
     * - Slot 0,1,2,3,5,6,8: Enchanted Cobblestone (custom item, bazaar_id = ENCHANTED_COBBLESTONE)
     * - Slot 4: Bow vanilla (không có bazaar_id)
     * - Slot 7: Redstone vanilla (không có bazaar_id)
     */
    private boolean validateBagRecipe(ItemStack[] matrix) {
        int[] ecSlots = {0, 1, 2, 3, 5, 6, 8};
        // Kiểm tra 7 ô Enchanted Cobblestone
        for (int slot : ecSlots) {
            ItemStack item = matrix[slot];
            if (item == null || item.getType() == Material.AIR) return false;
            if (!item.hasItemMeta()) return false;
            PersistentDataContainer pdc = item.getItemMeta().getPersistentDataContainer();
            String id = pdc.get(BAZAAR_ID_KEY, PersistentDataType.STRING);
            if (!"ENCHANTED_COBBLESTONE".equals(id)) return false;
        }
        // Kiểm tra slot 4: Bow vanilla
        ItemStack bow = matrix[4];
        if (bow == null || bow.getType() != Material.BOW) return false;
        if (bow.hasItemMeta() && bow.getItemMeta().getPersistentDataContainer().has(BAZAAR_ID_KEY, PersistentDataType.STRING)) return false;
        // Kiểm tra slot 7: Redstone vanilla
        ItemStack redstone = matrix[7];
        if (redstone == null || redstone.getType() != Material.REDSTONE) return false;
        if (redstone.hasItemMeta() && redstone.getItemMeta().getPersistentDataContainer().has(BAZAAR_ID_KEY, PersistentDataType.STRING)) return false;

        return true;
    }
}

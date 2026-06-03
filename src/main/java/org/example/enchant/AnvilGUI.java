/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Material
 *  org.bukkit.NamespacedKey
 *  org.bukkit.Sound
 *  org.bukkit.block.Block
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.block.Action
 *  org.bukkit.event.inventory.InventoryClickEvent
 *  org.bukkit.event.inventory.InventoryCloseEvent
 *  org.bukkit.event.inventory.PrepareAnvilEvent
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.Damageable
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.inventory.meta.Repairable
 *  org.bukkit.persistence.PersistentDataContainer
 *  org.bukkit.persistence.PersistentDataType
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.java.JavaPlugin
 */
package org.example.enchant;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.Repairable;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.example.enchant.CustomEnchant;
import org.example.enchant.EnchantManager;
import org.example.enchant.UltimateEnchant;

public class AnvilGUI
implements Listener {
    private final JavaPlugin plugin;
    private final EnchantManager enchantManager;
    private final String guiName = "Anvil";
    private final Map<UUID, Boolean> resultAvailable = new HashMap<UUID, Boolean>();

    public AnvilGUI(JavaPlugin plugin, EnchantManager enchantManager) {
        this.plugin = plugin;
        this.enchantManager = enchantManager;
    }

    @EventHandler
    public void onOpenAnvil(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        Block block = event.getClickedBlock();
        if (block == null) {
            return;
        }
        String matName = block.getType().name();
        if (!matName.contains("ANVIL")) {
            return;
        }
        if (event.getPlayer().isSneaking()) {
            return;
        }
        event.setCancelled(true);
        this.openCustomAnvilMenu(event.getPlayer());
    }

    public void openCustomAnvilMenu(Player player) {
        int[] limeSlots;
        Inventory gui = Bukkit.createInventory(null, (int)54, (String)"Anvil");
        ItemStack gray = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta gMeta = gray.getItemMeta();
        if (gMeta != null) {
            gMeta.setDisplayName("\u00a77 ");
            gray.setItemMeta(gMeta);
        }
        for (int i = 0; i < 54; ++i) {
            gui.setItem(i, gray);
        }
        ItemStack lime = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
        ItemMeta lMeta = lime.getItemMeta();
        if (lMeta != null) {
            lMeta.setDisplayName("\u00a7aH\u01b0\u1edbng d\u1eabn k\u1ebft h\u1ee3p b\u00f9a ch\u00fa");
            lime.setItemMeta(lMeta);
        }
        for (int slot : limeSlots = new int[]{11, 12, 14, 15, 20, 24}) {
            gui.setItem(slot, lime);
        }
        gui.setItem(29, new ItemStack(Material.AIR));
        gui.setItem(33, new ItemStack(Material.AIR));
        gui.setItem(13, new ItemStack(Material.AIR));
        this.setDisabledButton(gui);
        player.openInventory(gui);
    }

    @EventHandler
    public void onAnvilClick(InventoryClickEvent event) {
        if (!event.getView().getTitle().equals("Anvil")) {
            return;
        }
        Player player = (Player)event.getWhoClicked();
        Inventory gui = event.getInventory();
        int slot = event.getSlot();
        if (event.getClick().isShiftClick()) {
            event.setCancelled(true);
            return;
        }
        if (event.getClickedInventory() != event.getView().getTopInventory()) {
            Bukkit.getScheduler().runTask((Plugin)this.plugin, () -> this.updateAnvilResult(gui, player));
            return;
        }
        if (slot != 29 && slot != 33 && slot != 31 && slot != 13) {
            event.setCancelled(true);
            return;
        }
        if (slot == 13) {
            if (this.resultAvailable.getOrDefault(player.getUniqueId(), false).booleanValue()) {
                if (event.getCursor() != null && event.getCursor().getType() != Material.AIR) {
                    event.setCancelled(true);
                    return;
                }
                Bukkit.getScheduler().runTask((Plugin)this.plugin, () -> {
                    ItemStack item = gui.getItem(13);
                    if (item == null || item.getType().isAir()) {
                        this.resultAvailable.put(player.getUniqueId(), false);
                        this.setDisabledButton(gui);
                    }
                });
                return;
            }
            event.setCancelled(true);
            return;
        }
        if (slot == 29 || slot == 33) {
            Bukkit.getScheduler().runTask((Plugin)this.plugin, () -> this.updateAnvilResult(gui, player));
            return;
        }
        if (slot == 31) {
            event.setCancelled(true);
            ItemStack leftItem = gui.getItem(29);
            ItemStack rightItem = gui.getItem(33);
            if (leftItem == null || leftItem.getType().isAir() || rightItem == null || rightItem.getType().isAir()) {
                return;
            }
            CustomEnchant chargeableEnchant = this.getActiveChargeableEnchant(leftItem, rightItem);
            if (chargeableEnchant != null) {
                int currentCharges = this.enchantManager.getCharges(leftItem, chargeableEnchant);
                NamespacedKey key = new NamespacedKey((Plugin)this.plugin, "enchant_" + chargeableEnchant.getId());
                int level = (Integer)leftItem.getItemMeta().getPersistentDataContainer().getOrDefault(key, PersistentDataType.INTEGER, 1);
                int maxCharges = this.enchantManager.getMaxCharges(chargeableEnchant, level);
                int fuelAmount = rightItem.getAmount();
                int chargesToAdd = fuelAmount * 25;
                int finalCharges = Math.min(currentCharges + chargesToAdd, maxCharges);
                int usedFuel = (int)Math.ceil((double)(finalCharges - currentCharges) / 25.0);
                int cost = 2 * usedFuel;
                if (player.getLevel() < cost) {
                    player.sendMessage("\u00a7cM\u00e0y \u0111\u00e9o \u0111\u1ee7 Level XP \u0111\u1ec3 s\u1ea1c pin m\u00f3n \u0111\u1ed3 n\u00e0y ng\u01b0\u1eddi anh em \u01a1i!");
                    player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 0.5f);
                    return;
                }
                ItemStack resultItem = leftItem.clone();
                this.enchantManager.setCharges(resultItem, chargeableEnchant, finalCharges);
                this.enchantManager.rebuildEnchantLore(resultItem);
                player.setLevel(player.getLevel() - cost);
                player.sendMessage("\u00a7a\u26a1 S\u1ea1c pin b\u00e1u v\u1eadt th\u00e0nh c\u00f4ng! \u0110\u00e3 n\u1ea1p \u00a7e+" + (finalCharges - currentCharges) + " pin \u00a7av\u00e0 ti\u00eau hao \u00a7e" + usedFuel + "x nguy\u00ean li\u1ec7u\u00a7a.");
                player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_USE, 1.0f, 1.0f);
                gui.setItem(29, new ItemStack(Material.AIR));
                ItemStack remainingFuel = rightItem.clone();
                remainingFuel.setAmount(rightItem.getAmount() - usedFuel);
                gui.setItem(33, remainingFuel.getAmount() <= 0 ? new ItemStack(Material.AIR) : remainingFuel);
                gui.setItem(13, resultItem);
                this.resultAvailable.put(player.getUniqueId(), true);
                ItemStack barrier = new ItemStack(Material.BARRIER);
                ItemMeta bMeta = barrier.getItemMeta();
                if (bMeta != null) {
                    bMeta.setDisplayName("\u00a7aTh\u00e0nh ph\u1ea9m \u0111\u00e3 s\u1eb5n s\u00e0ng!");
                    bMeta.setLore(Arrays.asList("\u00a77H\u00e3y nh\u1ea5c b\u00e1u v\u1eadt c\u1ee7a m\u00e0y \u1edf \u00f4 s\u1ed1 13 ph\u00eda tr\u00ean nh\u00e9 cu m."));
                    barrier.setItemMeta(bMeta);
                }
                gui.setItem(31, barrier);
                return;
            }
            int reqLevel = this.calculateAnvilCost(leftItem, rightItem);
            if (reqLevel == -1) {
                return;
            }
            if (player.getLevel() < reqLevel) {
                player.sendMessage("\u00a7cM\u00e0y \u0111\u00e9o \u0111\u1ee7 Level XP \u0111\u1ec3 k\u1ebft h\u1ee3p m\u00f3n \u0111\u1ed3 n\u00e0y ng\u01b0\u1eddi anh em \u01a1i!");
                player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 0.5f);
                return;
            }
            ItemStack resultItem = leftItem.clone();
            this.applyCombinationLogic(resultItem, rightItem);
            player.setLevel(player.getLevel() - reqLevel);
            player.sendMessage("\u00a7a\ud83d\udfe9 K\u1ebft h\u1ee3p v\u1eadt ph\u1ea9m th\u00e0nh c\u00f4ng! \u0110\u00e3 kh\u1ea5u tr\u1eeb \u00a7e" + reqLevel + " Exp Levels\u00a7a.");
            player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_USE, 1.0f, 1.0f);
            gui.setItem(29, new ItemStack(Material.AIR));
            gui.setItem(33, new ItemStack(Material.AIR));
            gui.setItem(13, resultItem);
            this.resultAvailable.put(player.getUniqueId(), true);
            ItemStack barrier = new ItemStack(Material.BARRIER);
            ItemMeta bMeta = barrier.getItemMeta();
            if (bMeta != null) {
                bMeta.setDisplayName("\u00a7aTh\u00e0nh ph\u1ea9m \u0111\u00e3 s\u1eb5n s\u00e0ng!");
                bMeta.setLore(Arrays.asList("\u00a77H\u00e3y nh\u1ea5c b\u00e1u v\u1eadt c\u1ee7a m\u00e0y \u1edf \u00f4 s\u1ed1 13 ph\u00eda tr\u00ean nh\u00e9 cu m."));
                barrier.setItemMeta(bMeta);
            }
            gui.setItem(31, barrier);
        }
    }

    private void updateAnvilResult(Inventory gui, Player player) {
        if (this.resultAvailable.getOrDefault(player.getUniqueId(), false).booleanValue()) {
            return;
        }
        ItemStack leftItem = gui.getItem(29);
        ItemStack rightItem = gui.getItem(33);
        if (leftItem == null || leftItem.getType().isAir() || rightItem == null || rightItem.getType().isAir()) {
            gui.setItem(13, new ItemStack(Material.AIR));
            this.setDisabledButton(gui);
            return;
        }
        CustomEnchant chargeableEnchant = this.getActiveChargeableEnchant(leftItem, rightItem);
        if (chargeableEnchant != null) {
            int currentCharges = this.enchantManager.getCharges(leftItem, chargeableEnchant);
            NamespacedKey key = new NamespacedKey((Plugin)this.plugin, "enchant_" + chargeableEnchant.getId());
            int level = (Integer)leftItem.getItemMeta().getPersistentDataContainer().getOrDefault(key, PersistentDataType.INTEGER, 1);
            int maxCharges = this.enchantManager.getMaxCharges(chargeableEnchant, level);
            if (currentCharges >= maxCharges) {
                gui.setItem(13, new ItemStack(Material.AIR));
                ItemStack barrier = new ItemStack(Material.BARRIER);
                ItemMeta bMeta = barrier.getItemMeta();
                if (bMeta != null) {
                    bMeta.setDisplayName("\u00a7cPin \u0111\u00e3 \u0111\u1ea7y!");
                    bMeta.setLore(Arrays.asList("\u00a77B\u00f9a ch\u00fa \u00a7e" + chargeableEnchant.getDisplayName() + " \u00a77tr\u00ean v\u1eadt ph\u1ea9m", "\u00a77\u0111\u00e3 \u0111\u01b0\u1ee3c s\u1ea1c \u0111\u1ea7y pin (" + currentCharges + "/" + maxCharges + "\u26a1)"));
                    barrier.setItemMeta(bMeta);
                }
                gui.setItem(31, barrier);
                return;
            }
            int fuelAmount = rightItem.getAmount();
            int chargesToAdd = fuelAmount * 25;
            int finalCharges = Math.min(currentCharges + chargesToAdd, maxCharges);
            int usedFuel = (int)Math.ceil((double)(finalCharges - currentCharges) / 25.0);
            int cost = 2 * usedFuel;
            ItemStack previewItem = leftItem.clone();
            this.enchantManager.setCharges(previewItem, chargeableEnchant, finalCharges);
            this.enchantManager.rebuildEnchantLore(previewItem);
            gui.setItem(13, previewItem);
            ItemStack anvilBtn = new ItemStack(Material.ANVIL);
            ItemMeta aMeta = anvilBtn.getItemMeta();
            if (aMeta != null) {
                aMeta.setDisplayName("\u00a7a\u26a1 S\u1ea0C PIN B\u1eecA CH\u00da \u26a1");
                if (player.getLevel() < cost) {
                    aMeta.setLore(Arrays.asList("\u00a77N\u1ea1p pin cho b\u00f9a \u00a7e" + chargeableEnchant.getDisplayName(), "\u00a77S\u1ed1 l\u01b0\u1ee3ng n\u1ea1p: \u00a7b+" + (finalCharges - currentCharges) + "\u26a1 (" + finalCharges + "/" + maxCharges + "\u26a1)", "\u00a77Ti\u00eau hao: \u00a76" + usedFuel + "x \u00a77nguy\u00ean li\u1ec7u", "", "\u00a77Chi ph\u00ed n\u1ea1p: \u00a7c" + cost + " Exp Levels", "\u00a7cM\u00e0y kh\u00f4ng \u0111\u1ee7 c\u1ea5p \u0111\u1ed9 kinh nghi\u1ec7m!"));
                } else {
                    aMeta.setLore(Arrays.asList("\u00a77N\u1ea1p pin cho b\u00f9a \u00a7e" + chargeableEnchant.getDisplayName(), "\u00a77S\u1ed1 l\u01b0\u1ee3ng n\u1ea1p: \u00a7b+" + (finalCharges - currentCharges) + "\u26a1 (" + finalCharges + "/" + maxCharges + "\u26a1)", "\u00a77Ti\u00eau hao: \u00a76" + usedFuel + "x \u00a77nguy\u00ean li\u1ec7u", "", "\u00a77Chi ph\u00ed n\u1ea1p: \u00a7e" + cost + " Exp Levels", "\u00a7eClick \u0111\u1ec3 s\u1ea1c pin b\u00e1u v\u1eadt!"));
                }
                anvilBtn.setItemMeta(aMeta);
            }
            gui.setItem(31, anvilBtn);
            return;
        }
        int cost = this.calculateAnvilCost(leftItem, rightItem);
        if (cost == -1) {
            gui.setItem(13, new ItemStack(Material.AIR));
            ItemStack barrier = new ItemStack(Material.BARRIER);
            ItemMeta bMeta = barrier.getItemMeta();
            if (bMeta != null) {
                bMeta.setDisplayName("\u00a7cGh\u00e9p \u0111\u1ed3 \u0111\u00e9o h\u1ee3p l\u1ec7!");
                bMeta.setLore(Arrays.asList("\u00a77B\u00f9a ch\u00fa c\u1ee7a nguy\u00ean li\u1ec7u ph\u1ee5 xung \u0111\u1ed9t m\u00f4n ph\u00e1i", "\u00a77v\u1edbi v\u0169 kh\u00ed ch\u00ednh \u0111ang s\u1edf h\u1eefu \u1edf \u00f4 b\u00ean tr\u00e1i."));
                barrier.setItemMeta(bMeta);
            }
            gui.setItem(31, barrier);
            return;
        }
        ItemStack previewItem = leftItem.clone();
        this.applyCombinationLogic(previewItem, rightItem);
        gui.setItem(13, previewItem);
        ItemStack anvilBtn = new ItemStack(Material.ANVIL);
        ItemMeta aMeta = anvilBtn.getItemMeta();
        if (aMeta != null) {
            aMeta.setDisplayName("\u00a7aCombine Items");
            if (player.getLevel() < cost) {
                aMeta.setLore(Arrays.asList("\u00a77H\u1ee3p nh\u1ea5t to\u00e0n b\u1ed9 b\u00f9a ch\u00fa t\u1eeb \u00f4 b\u00ean ph\u1ea3i v\u00e0o \u00f4 b\u00ean tr\u00e1i.", "", "\u00a77Chi ph\u00ed \u0111\u00fac: \u00a7c" + cost + " Exp Levels", "\u00a7cM\u00e0y kh\u00f4ng \u0111\u1ee7 c\u1ea5p \u0111\u1ed9 kinh nghi\u1ec7m!"));
            } else {
                aMeta.setLore(Arrays.asList("\u00a77H\u1ee3p nh\u1ea5t to\u00e0n b\u1ed9 b\u00f9a ch\u00fa t\u1eeb \u00f4 b\u00ean ph\u1ea3i v\u00e0o \u00f4 b\u00ean tr\u00e1i.", "", "\u00a77Chi ph\u00ed \u0111\u00fac: \u00a7e" + cost + " Exp Levels", "\u00a7eClick chu\u1ed9t \u0111\u1ec3 ti\u1ebfn h\u00e0nh r\u00e8n \u0111\u00fac b\u00e1u v\u1eadt!"));
            }
            anvilBtn.setItemMeta(aMeta);
        }
        gui.setItem(31, anvilBtn);
    }

    private CustomEnchant getActiveChargeableEnchant(ItemStack left, ItemStack right) {
        if (left == null || !left.hasItemMeta() || right == null) {
            return null;
        }
        PersistentDataContainer container = left.getItemMeta().getPersistentDataContainer();
        for (CustomEnchant enchant : CustomEnchant.values()) {
            NamespacedKey key;
            if (!this.enchantManager.isChargeable(enchant) || !container.has(key = new NamespacedKey((Plugin)this.plugin, "enchant_" + enchant.getId()), PersistentDataType.INTEGER) || right.getType() != this.enchantManager.getFuelMaterial(enchant)) continue;
            return enchant;
        }
        return null;
    }

    private boolean hasTridentConflict(ItemStack left, ItemStack right) {
        if (left == null || right == null) {
            return false;
        }
        if (left.getType() != Material.TRIDENT && left.getType() != Material.BOOK && left.getType() != Material.ENCHANTED_BOOK) {
            return false;
        }
        if (right.getType() != Material.TRIDENT && right.getType() != Material.BOOK && right.getType() != Material.ENCHANTED_BOOK) {
            return false;
        }
        ItemMeta leftMeta = left.getItemMeta();
        ItemMeta rightMeta = right.getItemMeta();
        if (leftMeta == null || rightMeta == null) {
            return false;
        }
        PersistentDataContainer leftPDC = leftMeta.getPersistentDataContainer();
        PersistentDataContainer rightPDC = rightMeta.getPersistentDataContainer();
        NamespacedKey loyalty = new NamespacedKey((Plugin)this.plugin, "enchant_loyalty");
        NamespacedKey channeling = new NamespacedKey((Plugin)this.plugin, "enchant_channeling");
        NamespacedKey riptide = new NamespacedKey((Plugin)this.plugin, "enchant_riptide");
        boolean leftLoyalty = leftPDC.has(loyalty, PersistentDataType.INTEGER) && (Integer)leftPDC.get(loyalty, PersistentDataType.INTEGER) > 0;
        boolean leftChanneling = leftPDC.has(channeling, PersistentDataType.INTEGER) && (Integer)leftPDC.get(channeling, PersistentDataType.INTEGER) > 0;
        boolean leftRiptide = leftPDC.has(riptide, PersistentDataType.INTEGER) && (Integer)leftPDC.get(riptide, PersistentDataType.INTEGER) > 0;
        boolean rightLoyalty = rightPDC.has(loyalty, PersistentDataType.INTEGER) && (Integer)rightPDC.get(loyalty, PersistentDataType.INTEGER) > 0;
        boolean rightChanneling = rightPDC.has(channeling, PersistentDataType.INTEGER) && (Integer)rightPDC.get(channeling, PersistentDataType.INTEGER) > 0;
        boolean rightRiptide = rightPDC.has(riptide, PersistentDataType.INTEGER) && (Integer)rightPDC.get(riptide, PersistentDataType.INTEGER) > 0;
        boolean finalLoyalty = leftLoyalty || rightLoyalty;
        boolean finalChanneling = leftChanneling || rightChanneling;
        boolean finalRiptide = leftRiptide || rightRiptide;
        return finalRiptide && (finalLoyalty || finalChanneling);
    }

    private int calculateAnvilCost(ItemStack left, ItemStack right) {
        Damageable dmgMeta;
        if (this.hasTridentConflict(left, right)) {
            return -1;
        }
        boolean isRepairing = false;
        if (left.hasItemMeta() && left.getItemMeta() instanceof Damageable && (dmgMeta = (Damageable)left.getItemMeta()).hasDamage() && dmgMeta.getDamage() > 0) {
            Material rm;
            Material lm = left.getType();
            if (lm == (rm = right.getType())) {
                isRepairing = true;
            } else {
                String name = lm.name();
                if (name.contains("DIAMOND") && rm == Material.DIAMOND) {
                    isRepairing = true;
                } else if (name.contains("IRON") && rm == Material.IRON_INGOT) {
                    isRepairing = true;
                } else if (name.contains("GOLD") && rm == Material.GOLD_INGOT) {
                    isRepairing = true;
                } else if (name.contains("LEATHER") && rm == Material.LEATHER) {
                    isRepairing = true;
                } else if (name.contains("NETHERITE") && rm == Material.NETHERITE_INGOT) {
                    isRepairing = true;
                } else if (name.contains("STONE") && (rm == Material.COBBLESTONE || rm == Material.STONE)) {
                    isRepairing = true;
                } else if (name.contains("WOODEN") && rm.name().contains("PLANKS")) {
                    isRepairing = true;
                }
            }
        }
        boolean hasValidEnchant = false;
        int totalLevelsFound = 0;
        if (right.hasItemMeta()) {
            NamespacedKey key;
            PersistentDataContainer rightContainer = right.getItemMeta().getPersistentDataContainer();
            for (CustomEnchant customEnchant : CustomEnchant.values()) {
                boolean isActualAxe;
                key = new NamespacedKey((Plugin)this.plugin, "enchant_" + customEnchant.getId());
                if (!rightContainer.has(key, PersistentDataType.INTEGER) || left.getType() != Material.BOOK && left.getType() != Material.ENCHANTED_BOOK && !customEnchant.getItemGroup().canApply(left.getType())) continue;
                String matName = left.getType().name();
                boolean bl = isActualAxe = matName.endsWith("_AXE") || matName.equals("AXE");
                if (isActualAxe && (customEnchant.getItemGroup() == CustomEnchant.ItemGroup.SWORD && this.enchantManager.hasEnchantFromGroup(left, CustomEnchant.ItemGroup.TOOL) || customEnchant.getItemGroup() == CustomEnchant.ItemGroup.TOOL && this.enchantManager.hasEnchantFromGroup(left, CustomEnchant.ItemGroup.SWORD))) continue;
                hasValidEnchant = true;
                totalLevelsFound += ((Integer)rightContainer.get(key, PersistentDataType.INTEGER)).intValue();
            }
            for (Enum enum_ : UltimateEnchant.values()) {
                key = new NamespacedKey((Plugin)this.plugin, "ult_enchant_" + ((UltimateEnchant)enum_).getId());
                if (!rightContainer.has(key, PersistentDataType.INTEGER) || left.getType() != Material.BOOK && left.getType() != Material.ENCHANTED_BOOK && !((UltimateEnchant)enum_).getGroup().canApply(left.getType())) continue;
                hasValidEnchant = true;
                totalLevelsFound += (Integer)rightContainer.get(key, PersistentDataType.INTEGER) * 2;
            }
        }
        if (!hasValidEnchant && !isRepairing) {
            return -1;
        }
        int cost = 0;
        if (hasValidEnchant) {
            cost += 10 + totalLevelsFound * 6;
        }
        if (isRepairing) {
            cost += 5;
        }
        return cost;
    }

    private void applyCombinationLogic(ItemStack target, ItemStack ingredient) {
        int ingLvl;
        NamespacedKey key;
        ItemMeta ingMeta;
        Damageable dmgMeta;
        if (this.hasTridentConflict(target, ingredient)) {
            return;
        }
        ItemMeta targetMeta = target.getItemMeta();
        if (targetMeta == null) {
            return;
        }
        if (targetMeta instanceof Damageable && (dmgMeta = (Damageable)targetMeta).hasDamage() && dmgMeta.getDamage() > 0) {
            Material lm = target.getType();
            Material rm = ingredient.getType();
            boolean isRepairing = false;
            if (lm == rm) {
                isRepairing = true;
            } else {
                String name = lm.name();
                if (name.contains("DIAMOND") && rm == Material.DIAMOND) {
                    isRepairing = true;
                } else if (name.contains("IRON") && rm == Material.IRON_INGOT) {
                    isRepairing = true;
                } else if (name.contains("GOLD") && rm == Material.GOLD_INGOT) {
                    isRepairing = true;
                } else if (name.contains("LEATHER") && rm == Material.LEATHER) {
                    isRepairing = true;
                } else if (name.contains("NETHERITE") && rm == Material.NETHERITE_INGOT) {
                    isRepairing = true;
                } else if (name.contains("STONE") && (rm == Material.COBBLESTONE || rm == Material.STONE)) {
                    isRepairing = true;
                } else if (name.contains("WOODEN") && rm.name().contains("PLANKS")) {
                    isRepairing = true;
                }
            }
            if (isRepairing) {
                if (lm == rm) {
                    dmgMeta.setDamage(0);
                } else {
                    short maxDmg = target.getType().getMaxDurability();
                    int healAmount = (int)((double)maxDmg * 0.25);
                    int n = Math.max(0, dmgMeta.getDamage() - healAmount);
                    dmgMeta.setDamage(n);
                }
            }
        }
        if ((ingMeta = ingredient.getItemMeta()) == null) {
            target.setItemMeta(targetMeta);
            return;
        }
        PersistentDataContainer targetContainer = targetMeta.getPersistentDataContainer();
        PersistentDataContainer ingContainer = ingMeta.getPersistentDataContainer();
        for (CustomEnchant customEnchant : CustomEnchant.values()) {
            boolean isActualAxe;
            key = new NamespacedKey((Plugin)this.plugin, "enchant_" + customEnchant.getId());
            if (!ingContainer.has(key, PersistentDataType.INTEGER)) continue;
            ingLvl = (Integer)ingContainer.get(key, PersistentDataType.INTEGER);
            if (target.getType() != Material.BOOK && target.getType() != Material.ENCHANTED_BOOK && !customEnchant.getItemGroup().canApply(target.getType())) continue;
            String matName = target.getType().name();
            boolean bl = isActualAxe = matName.endsWith("_AXE") || matName.equals("AXE");
            if (isActualAxe && (customEnchant.getItemGroup() == CustomEnchant.ItemGroup.SWORD && this.enchantManager.hasEnchantFromGroup(target, CustomEnchant.ItemGroup.TOOL) || customEnchant.getItemGroup() == CustomEnchant.ItemGroup.TOOL && this.enchantManager.hasEnchantFromGroup(target, CustomEnchant.ItemGroup.SWORD))) continue;
            if (targetContainer.has(key, PersistentDataType.INTEGER)) {
                int targetLvl = (Integer)targetContainer.get(key, PersistentDataType.INTEGER);
                int finalLvl = targetLvl == ingLvl ? Math.min(targetLvl + 1, customEnchant.getMaxLevel()) : Math.max(targetLvl, ingLvl);
                targetContainer.set(key, PersistentDataType.INTEGER, finalLvl);
            } else {
                targetContainer.set(key, PersistentDataType.INTEGER, ingLvl);
            }
            if (!this.enchantManager.isChargeable(customEnchant) || targetContainer.has(this.enchantManager.getChargesKey(customEnchant), PersistentDataType.INTEGER)) continue;
            targetContainer.set(this.enchantManager.getChargesKey(customEnchant), PersistentDataType.INTEGER, this.enchantManager.getCharges(target, customEnchant));
        }
        for (Enum enum_ : UltimateEnchant.values()) {
            key = new NamespacedKey((Plugin)this.plugin, "ult_enchant_" + ((UltimateEnchant)enum_).getId());
            if (!ingContainer.has(key, PersistentDataType.INTEGER)) continue;
            ingLvl = (Integer)ingContainer.get(key, PersistentDataType.INTEGER);
            if (target.getType() != Material.BOOK && target.getType() != Material.ENCHANTED_BOOK && !((UltimateEnchant)enum_).getGroup().canApply(target.getType())) continue;
            int finalLvl = ingLvl;
            if (targetContainer.has(key, PersistentDataType.INTEGER)) {
                int targetLvl = (Integer)targetContainer.get(key, PersistentDataType.INTEGER);
                finalLvl = targetLvl == ingLvl ? Math.min(targetLvl + 1, ((UltimateEnchant)enum_).getMaxLevel()) : Math.max(targetLvl, ingLvl);
            }
            this.enchantManager.applyUltimateEnchantToItem(target, (UltimateEnchant)enum_, finalLvl);
        }
        target.setItemMeta(targetMeta);
        this.enchantManager.rebuildEnchantLore(target);
    }

    private void setDisabledButton(Inventory gui) {
        ItemStack barrier = new ItemStack(Material.BARRIER);
        ItemMeta bMeta = barrier.getItemMeta();
        if (bMeta != null) {
            bMeta.setDisplayName("\u00a7cCh\u01b0a \u0111\u1ee7 nguy\u00ean li\u1ec7u!");
            bMeta.setLore(Arrays.asList("\u00a77H\u00e3y \u0111\u1eb7t V\u1eadt ph\u1ea9m ch\u00ednh v\u00e0o \u00f4 b\u00ean TR\u00c1I (Slot 29)", "\u00a77v\u00e0 Nguy\u00ean li\u1ec7u ph\u1ee5/S\u00e1ch v\u00e0o \u00f4 b\u00ean PH\u1ea2I (Slot 33)."));
            barrier.setItemMeta(bMeta);
        }
        gui.setItem(31, barrier);
    }

    @EventHandler
    public void onAnvilClose(InventoryCloseEvent event) {
        int[] slotsToReturn;
        if (!event.getView().getTitle().equals("Anvil")) {
            return;
        }
        Inventory gui = event.getInventory();
        Player player = (Player)event.getPlayer();
        for (int slot : slotsToReturn = new int[]{29, 33}) {
            ItemStack item = gui.getItem(slot);
            if (item == null || item.getType() == Material.AIR) continue;
            for (ItemStack drop : player.getInventory().addItem(new ItemStack[]{item}).values()) {
                player.getWorld().dropItemNaturally(player.getLocation(), drop);
            }
        }
        if (this.resultAvailable.getOrDefault(player.getUniqueId(), false).booleanValue()) {
            ItemStack resultItem = gui.getItem(13);
            if (resultItem != null && resultItem.getType() != Material.AIR) {
                for (ItemStack drop : player.getInventory().addItem(new ItemStack[]{resultItem}).values()) {
                    player.getWorld().dropItemNaturally(player.getLocation(), drop);
                }
            }
            this.resultAvailable.remove(player.getUniqueId());
        }
    }

    @EventHandler
    public void onPrepareVanillaAnvil(PrepareAnvilEvent event) {
        ItemStack left = event.getInventory().getItem(0);
        ItemStack right = event.getInventory().getItem(1);
        if (left == null || right == null) {
            return;
        }
        if (this.hasTridentConflict(left, right)) {
            event.setResult(null);
            return;
        }
        ItemStack result = event.getResult();
        if (result == null) {
            return;
        }
        event.getInventory().setMaximumRepairCost(999);
        ItemMeta resultMeta = result.getItemMeta();
        if (resultMeta != null) {
            if (resultMeta instanceof Repairable) {
                ((Repairable)resultMeta).setRepairCost(0);
                event.getInventory().setRepairCost(1);
            }
            if (resultMeta.isUnbreakable() && resultMeta.getPersistentDataContainer().has(new NamespacedKey((Plugin)this.plugin, "cwe_id"), PersistentDataType.STRING)) {
                if (resultMeta.hasEnchant(Enchantment.MENDING)) {
                    resultMeta.removeEnchant(Enchantment.MENDING);
                }
                if (resultMeta.hasEnchant(Enchantment.UNBREAKING)) {
                    resultMeta.removeEnchant(Enchantment.UNBREAKING);
                }
            }
            result.setItemMeta(resultMeta);
        }
        event.setResult(result);
        ItemMeta leftMeta = left.getItemMeta();
        ItemMeta rightMeta = right.getItemMeta();
        if (leftMeta != null && rightMeta != null && resultMeta != null) {
            int finalLvl;
            int leftLvl;
            int rightLvl;
            NamespacedKey key;
            PersistentDataContainer leftPDC = leftMeta.getPersistentDataContainer();
            PersistentDataContainer rightPDC = rightMeta.getPersistentDataContainer();
            PersistentDataContainer resultPDC = resultMeta.getPersistentDataContainer();
            boolean changed = false;
            for (CustomEnchant customEnchant : CustomEnchant.values()) {
                key = new NamespacedKey((Plugin)this.plugin, "enchant_" + customEnchant.getId());
                if (!rightPDC.has(key, PersistentDataType.INTEGER)) continue;
                rightLvl = (Integer)rightPDC.get(key, PersistentDataType.INTEGER);
                if (left.getType() != Material.BOOK && left.getType() != Material.ENCHANTED_BOOK && !customEnchant.getItemGroup().canApply(left.getType())) continue;
                leftLvl = (Integer)leftPDC.getOrDefault(key, PersistentDataType.INTEGER, 0);
                finalLvl = leftLvl == rightLvl ? Math.min(leftLvl + 1, customEnchant.getMaxLevel()) : Math.max(leftLvl, rightLvl);
                resultPDC.set(key, PersistentDataType.INTEGER, finalLvl);
                changed = true;
            }
            for (Enum enum_ : UltimateEnchant.values()) {
                key = new NamespacedKey((Plugin)this.plugin, "ult_enchant_" + ((UltimateEnchant)enum_).getId());
                if (!rightPDC.has(key, PersistentDataType.INTEGER)) continue;
                rightLvl = (Integer)rightPDC.get(key, PersistentDataType.INTEGER);
                if (left.getType() != Material.BOOK && left.getType() != Material.ENCHANTED_BOOK && !((UltimateEnchant)enum_).getGroup().canApply(left.getType())) continue;
                leftLvl = (Integer)leftPDC.getOrDefault(key, PersistentDataType.INTEGER, 0);
                finalLvl = leftLvl == rightLvl ? Math.min(leftLvl + 1, ((UltimateEnchant)enum_).getMaxLevel()) : Math.max(leftLvl, rightLvl);
                this.enchantManager.applyUltimateEnchantToItem(result, (UltimateEnchant)enum_, finalLvl);
                changed = true;
            }
            if (changed) {
                result.setItemMeta(resultMeta);
                this.enchantManager.rebuildEnchantLore(result);
                event.setResult(result);
            } else {
                result.setItemMeta(resultMeta);
                event.setResult(result);
            }
        }
    }
}


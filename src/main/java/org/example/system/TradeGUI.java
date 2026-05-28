package org.example.system;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.example.core.CustomWeaponEngine;
import net.milkbowl.vault.economy.Economy;

import java.util.*;

public class TradeGUI implements Listener {

    @EventHandler
    public void onPlayerInteractEntity(org.bukkit.event.player.PlayerInteractEntityEvent event) {
        if (event.getRightClicked() instanceof Player && event.getPlayer().isSneaking()) {
            CustomWeaponEngine.getTradeManager().sendRequest(event.getPlayer(), (Player) event.getRightClicked());
        }
    }


    private static class TradeSession {
        Player playerA;
        Player playerB;
        Inventory gui;
        boolean aConfirmed = false;
        boolean bConfirmed = false;
        
        // Trạng thái tiền giao dịch
        double aMoney = 0;
        double bMoney = 0;
        
        // Trạng thái items của từng bên để rollback nếu close
        List<ItemStack> aItems = new ArrayList<>();
        List<ItemStack> bItems = new ArrayList<>();

        public TradeSession(Player a, Player b, Inventory gui) {
            this.playerA = a;
            this.playerB = b;
            this.gui = gui;
        }
    }

    private static final Map<Inventory, TradeSession> activeTrades = new HashMap<>();

    public static void openTrade(Player a, Player b) {
        Inventory gui = Bukkit.createInventory(null, 54, "§8Giao dịch: " + a.getName() + " & " + b.getName());

        // Setup Glass Panes
        ItemStack gray = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta gMeta = gray.getItemMeta();
        gMeta.setDisplayName("§7|");
        gray.setItemMeta(gMeta);

        // Separator (Col 4)
        for (int i = 4; i < 54; i += 9) {
            gui.setItem(i, gray);
        }

        // Action Buttons Player A (45, 46, 47, 48)
        gui.setItem(45, createMoneyButton(10));
        gui.setItem(46, createMoneyButton(100));
        gui.setItem(47, createMoneyButton(1000));
        gui.setItem(48, createConfirmButton(false));

        // Action Buttons Player B (50, 51, 52, 53)
        gui.setItem(50, createMoneyButton(10));
        gui.setItem(51, createMoneyButton(100));
        gui.setItem(52, createMoneyButton(1000));
        gui.setItem(53, createConfirmButton(false));

        TradeSession session = new TradeSession(a, b, gui);
        activeTrades.put(gui, session);

        a.openInventory(gui);
        b.openInventory(gui);
    }

    private static ItemStack createMoneyButton(int amount) {
        ItemStack item = new ItemStack(Material.GOLD_INGOT);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§e+" + amount + "$");
        meta.setLore(Arrays.asList("§7Click để thêm tiền mặt", "§7vào giao dịch."));
        item.setItemMeta(meta);
        return item;
    }

    private static ItemStack createMoneyItem(int amount) {
        ItemStack item = new ItemStack(Material.GOLD_INGOT);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§6" + amount + "$");
        meta.setLore(Arrays.asList("§7Tiền giao dịch", "§7Click để thu hồi."));
        item.setItemMeta(meta);
        return item;
    }

    private static ItemStack createConfirmButton(boolean confirmed) {
        ItemStack item = new ItemStack(confirmed ? Material.LIME_WOOL : Material.RED_WOOL);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(confirmed ? "§aĐã xác nhận!" : "§cChưa xác nhận");
        meta.setLore(Arrays.asList("§7Click để thay đổi", "§7trạng thái xác nhận."));
        item.setItemMeta(meta);
        return item;
    }

    private boolean isPlayerASlot(int slot) {
        int col = slot % 9;
        return col < 4 && slot < 45;
    }

    private boolean isPlayerBSlot(int slot) {
        int col = slot % 9;
        return col > 4 && slot < 45;
    }

    @EventHandler
    public void onTradeDrag(InventoryDragEvent event) {
        if (activeTrades.containsKey(event.getInventory())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onTradeClick(InventoryClickEvent event) {
        Inventory gui = event.getInventory();
        TradeSession session = activeTrades.get(gui);
        if (session == null) return;

        Player clicker = (Player) event.getWhoClicked();
        boolean isPlayerA = clicker.equals(session.playerA);
        boolean isPlayerB = clicker.equals(session.playerB);

        if (!isPlayerA && !isPlayerB) return;

        event.setCancelled(true);

        Economy econ = CustomWeaponEngine.getEconomy();
        if (econ == null) return;

        int slot = event.getSlot();
        
        // Reset confirmation on any change
        if (event.getClickedInventory() == clicker.getInventory() || (isPlayerASlot(slot) || isPlayerBSlot(slot))) {
            if (session.aConfirmed) {
                session.aConfirmed = false;
                gui.setItem(48, createConfirmButton(false));
            }
            if (session.bConfirmed) {
                session.bConfirmed = false;
                gui.setItem(53, createConfirmButton(false));
            }
        }

        // Put item INTO trade
        if (event.getClickedInventory() == clicker.getInventory()) {
            ItemStack item = event.getCurrentItem();
            if (item == null || item.getType() == Material.AIR) return;
            
            int emptySlot = -1;
            for (int i = 0; i < 45; i++) {
                if ((isPlayerA && isPlayerASlot(i)) || (isPlayerB && isPlayerBSlot(i))) {
                    if (gui.getItem(i) == null || gui.getItem(i).getType() == Material.AIR) {
                        emptySlot = i;
                        break;
                    }
                }
            }
            
            if (emptySlot != -1) {
                gui.setItem(emptySlot, item.clone());
                event.setCurrentItem(null);
                if (isPlayerA) session.aItems.add(item.clone());
                if (isPlayerB) session.bItems.add(item.clone());
            } else {
                clicker.sendMessage("§cKhông còn chỗ trống trong khu vực của bạn!");
            }
            return;
        }

        // Click in GUI
        if (event.getClickedInventory() == gui) {
            
            // Player A interacting with their items
            if (isPlayerA && isPlayerASlot(slot)) {
                ItemStack item = gui.getItem(slot);
                if (item != null && item.getType() != Material.AIR) {
                    if (item.hasItemMeta() && item.getItemMeta().hasDisplayName() && item.getItemMeta().getDisplayName().endsWith("$")) {
                        // Undo Money
                        String name = item.getItemMeta().getDisplayName();
                        name = org.bukkit.ChatColor.stripColor(name).replace("$", "");
                        try {
                            double amt = Double.parseDouble(name) * item.getAmount();
                            econ.depositPlayer(clicker, amt);
                            session.aMoney -= amt;
                            gui.setItem(slot, null);
                            clicker.sendMessage("§aĐã hoàn tiền " + amt + "$");
                        } catch (Exception ignored) {}
                    } else {
                        // Undo Item
                        clicker.getInventory().addItem(item);
                        session.aItems.remove(item);
                        gui.setItem(slot, null);
                    }
                }
                return;
            }

            // Player B interacting with their items
            if (isPlayerB && isPlayerBSlot(slot)) {
                ItemStack item = gui.getItem(slot);
                if (item != null && item.getType() != Material.AIR) {
                    if (item.hasItemMeta() && item.getItemMeta().hasDisplayName() && item.getItemMeta().getDisplayName().endsWith("$")) {
                        String name = item.getItemMeta().getDisplayName();
                        name = org.bukkit.ChatColor.stripColor(name).replace("$", "");
                        try {
                            double amt = Double.parseDouble(name) * item.getAmount();
                            econ.depositPlayer(clicker, amt);
                            session.bMoney -= amt;
                            gui.setItem(slot, null);
                            clicker.sendMessage("§aĐã hoàn tiền " + amt + "$");
                        } catch (Exception ignored) {}
                    } else {
                        clicker.getInventory().addItem(item);
                        session.bItems.remove(item);
                        gui.setItem(slot, null);
                    }
                }
                return;
            }

            // Buttons
            if (isPlayerA) {
                if (slot == 45 || slot == 46 || slot == 47) { // Add money
                    int amount = slot == 45 ? 10 : (slot == 46 ? 100 : 1000);
                    if (econ.getBalance(clicker) >= amount) {
                        econ.withdrawPlayer(clicker, amount);
                        session.aMoney += amount;
                        addMoneyItem(gui, true, amount);
                    } else {
                        clicker.sendMessage("§cKhông đủ tiền mặt!");
                    }
                } else if (slot == 48) { // Confirm
                    session.aConfirmed = !session.aConfirmed;
                    gui.setItem(48, createConfirmButton(session.aConfirmed));
                    checkFinish(session);
                }
            } else if (isPlayerB) {
                if (slot == 50 || slot == 51 || slot == 52) {
                    int amount = slot == 50 ? 10 : (slot == 51 ? 100 : 1000);
                    if (econ.getBalance(clicker) >= amount) {
                        econ.withdrawPlayer(clicker, amount);
                        session.bMoney += amount;
                        addMoneyItem(gui, false, amount);
                    } else {
                        clicker.sendMessage("§cKhông đủ tiền mặt!");
                    }
                } else if (slot == 53) {
                    session.bConfirmed = !session.bConfirmed;
                    gui.setItem(53, createConfirmButton(session.bConfirmed));
                    checkFinish(session);
                }
            }
        }
    }
    
    private void addMoneyItem(Inventory gui, boolean isPlayerA, int amount) {
        // Find existing stack
        for (int i = 0; i < 45; i++) {
            if ((isPlayerA && isPlayerASlot(i)) || (!isPlayerA && isPlayerBSlot(i))) {
                ItemStack item = gui.getItem(i);
                if (item != null && item.getType() == Material.GOLD_INGOT && item.hasItemMeta() && item.getItemMeta().getDisplayName().equals("§6" + amount + "$")) {
                    if (item.getAmount() < 64) {
                        item.setAmount(item.getAmount() + 1);
                        return;
                    }
                }
            }
        }
        
        // Find empty slot
        for (int i = 0; i < 45; i++) {
            if ((isPlayerA && isPlayerASlot(i)) || (!isPlayerA && isPlayerBSlot(i))) {
                if (gui.getItem(i) == null || gui.getItem(i).getType() == Material.AIR) {
                    gui.setItem(i, createMoneyItem(amount));
                    return;
                }
            }
        }
    }

    private void checkFinish(TradeSession session) {
        if (session.aConfirmed && session.bConfirmed) {
            Economy econ = CustomWeaponEngine.getEconomy();
            
            // Transfer Money
            if (session.aMoney > 0) econ.depositPlayer(session.playerB, session.aMoney);
            if (session.bMoney > 0) econ.depositPlayer(session.playerA, session.bMoney);
            
            // Transfer Items
            for (ItemStack item : session.aItems) {
                if (item != null) session.playerB.getInventory().addItem(item.clone());
            }
            for (ItemStack item : session.bItems) {
                if (item != null) session.playerA.getInventory().addItem(item.clone());
            }

            session.playerA.sendMessage("§aGiao dịch thành công!");
            session.playerB.sendMessage("§aGiao dịch thành công!");

            // Clear to prevent rollback on close
            session.aItems.clear();
            session.bItems.clear();
            session.aMoney = 0;
            session.bMoney = 0;
            
            activeTrades.remove(session.gui);
            session.playerA.closeInventory();
            session.playerB.closeInventory();
        }
    }

    @EventHandler
    public void onTradeClose(InventoryCloseEvent event) {
        Inventory gui = event.getInventory();
        if (activeTrades.containsKey(gui)) {
            TradeSession session = activeTrades.remove(gui);
            
            // Rollback Items
            for (ItemStack item : session.aItems) {
                if (item != null) session.playerA.getInventory().addItem(item);
            }
            for (ItemStack item : session.bItems) {
                if (item != null) session.playerB.getInventory().addItem(item);
            }
            
            // Rollback Money
            Economy econ = CustomWeaponEngine.getEconomy();
            if (session.aMoney > 0) econ.depositPlayer(session.playerA, session.aMoney);
            if (session.bMoney > 0) econ.depositPlayer(session.playerB, session.bMoney);
            
            session.playerA.sendMessage("§cGiao dịch đã bị hủy!");
            session.playerB.sendMessage("§cGiao dịch đã bị hủy!");
            
            if (event.getPlayer().equals(session.playerA)) {
                if (session.playerB.getOpenInventory().getTopInventory().equals(gui)) {
                    Bukkit.getScheduler().runTaskLater(CustomWeaponEngine.getPlugin(CustomWeaponEngine.class), () -> {
                        session.playerB.closeInventory();
                    }, 1L);
                }
            } else {
                if (session.playerA.getOpenInventory().getTopInventory().equals(gui)) {
                    Bukkit.getScheduler().runTaskLater(CustomWeaponEngine.getPlugin(CustomWeaponEngine.class), () -> {
                        session.playerA.closeInventory();
                    }, 1L);
                }
            }
        }
    }
}

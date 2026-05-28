package org.example.bank;

import org.example.core.CustomWeaponEngine;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.block.Sign;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class BankGui implements Listener {

    private final CustomWeaponEngine plugin;
    private final BankDataManager dataManager;
    private final Economy econ;

    // Lưu trữ hành động đang chờ nhập từ chat: Player UUID -> "DEPOSIT" hoặc "WITHDRAW"
    private final Map<UUID, String> chatInputPlayers = new HashMap<>();

    public BankGui(CustomWeaponEngine plugin, Economy econ) {
        this.plugin = plugin;
        this.dataManager = BankDataManager.getInstance(plugin);
        this.econ = econ;
    }

    /**
     * Mở giao diện Personal Bank Account chính (36 slots)
     */
    public void openMainGui(Player player) {
        Inventory inv = Bukkit.createInventory(null, 36, "Personal Bank Account");
        UUID uuid = player.getUniqueId();
        double balance = dataManager.getBalance(uuid);
        String formattedBalance = BankDataManager.formatCoins(balance);

        // Kính nền màu xám
        ItemStack grayGlass = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta glassMeta = grayGlass.getItemMeta();
        if (glassMeta != null) {
            glassMeta.setDisplayName("§7 ");
            grayGlass.setItemMeta(glassMeta);
        }

        // Hàng 1 (0 - 8) & Hàng 3 (18 - 26) điền đầy kính nền
        for (int i = 0; i <= 8; i++) inv.setItem(i, grayGlass);
        for (int i = 18; i <= 26; i++) inv.setItem(i, grayGlass);

        // Hàng 2 (9 - 17): điền đầy kính nền trước, sau đó đặt các nút chức năng
        for (int i = 9; i <= 17; i++) inv.setItem(i, grayGlass);

        // Slot 11: Deposit Coins (CHEST)
        ItemStack depositBtn = new ItemStack(Material.CHEST);
        ItemMeta depMeta = depositBtn.getItemMeta();
        if (depMeta != null) {
            depMeta.setDisplayName("§aDeposit Coins");
            long timeRemaining = dataManager.getNextInterestTime() - System.currentTimeMillis();
            String formattedTime = BankDataManager.formatTimeRemaining(timeRemaining);

            List<String> lore = new ArrayList<>();
            lore.add("§7Current balance: §6" + formattedBalance);
            lore.add("");
            lore.add("§7Store coins in the bank to keep");
            lore.add("§7them safe while you go on");
            lore.add("§7adventures!");
            lore.add("");
            lore.add("§7You will earn §b2% §7interest every");
            lore.add("§7season for your first §610 million");
            lore.add("§7banked coins.");
            lore.add("");
            lore.add("§7Until interest: §b" + formattedTime);
            depMeta.setLore(lore);
            depositBtn.setItemMeta(depMeta);
        }
        inv.setItem(11, depositBtn);

        // Slot 13: Withdraw Coins (DROPPER)
        ItemStack withdrawBtn = new ItemStack(Material.DROPPER);
        ItemMeta withMeta = withdrawBtn.getItemMeta();
        if (withMeta != null) {
            withMeta.setDisplayName("§aWithdraw Coins");
            List<String> lore = new ArrayList<>();
            lore.add("§7Current balance: §6" + formattedBalance);
            lore.add("");
            lore.add("§7Take your coins out of the bank");
            lore.add("§7in order to spend them.");
            withMeta.setLore(lore);
            withdrawBtn.setItemMeta(withMeta);
        }
        inv.setItem(13, withdrawBtn);

        // Slot 15: Recent Transactions (MAP)
        ItemStack transactionsBtn = new ItemStack(Material.MAP);
        ItemMeta transMeta = transactionsBtn.getItemMeta();
        if (transMeta != null) {
            transMeta.setDisplayName("§aRecent Transactions");
            List<String> lore = new ArrayList<>();
            lore.add("§7Unknown map");
            lore.add("");
            lore.addAll(dataManager.getRecentTransactionsLore(uuid));
            transMeta.setLore(lore);
            transactionsBtn.setItemMeta(transMeta);
        }
        inv.setItem(15, transactionsBtn);

        // Hàng 4 (27 - 35): điền đầy kính nền trước, sau đó đặt các nút điều hướng
        for (int i = 27; i <= 35; i++) inv.setItem(i, grayGlass);

        // Slot 31: Close (BARRIER)
        ItemStack closeBtn = new ItemStack(Material.BARRIER);
        ItemMeta closeMeta = closeBtn.getItemMeta();
        if (closeMeta != null) {
            closeMeta.setDisplayName("§cClose");
            closeMeta.setLore(Collections.singletonList("§7Click to exit bank account."));
            closeBtn.setItemMeta(closeMeta);
        }
        inv.setItem(31, closeBtn);

        // Slot 32: Bank Upgrades (REDSTONE_TORCH)
        ItemStack upgradesBtn = new ItemStack(Material.REDSTONE_TORCH);
        ItemMeta upMeta = upgradesBtn.getItemMeta();
        if (upMeta != null) {
            upMeta.setDisplayName("§eBank Upgrades");
            List<String> lore = new ArrayList<>();
            lore.add("§7Upgrade your bank account");
            lore.add("§7limit to hold more coins!");
            lore.add("§7Current Limit: §650.000.000");
            lore.add("§aMaxed Out!");
            upMeta.setLore(lore);
            upgradesBtn.setItemMeta(upMeta);
        }
        inv.setItem(32, upgradesBtn);

        // Slot 33: Co-op / Guild Bank (IRON_DOOR)
        ItemStack coopBtn = new ItemStack(Material.IRON_DOOR);
        ItemMeta coopMeta = coopBtn.getItemMeta();
        if (coopMeta != null) {
            coopMeta.setDisplayName("§eCo-op / Guild Bank");
            List<String> lore = new ArrayList<>();
            lore.add("§7Switch to your Co-op or");
            lore.add("§7Guild Bank Account.");
            lore.add("§cComing Soon!");
            coopMeta.setLore(lore);
            coopBtn.setItemMeta(coopMeta);
        }
        inv.setItem(33, coopBtn);

        player.openInventory(inv);
    }

    /**
     * Mở giao diện phụ tùy chọn Deposit Coins (27 slots)
     */
    public void openDepositGui(Player player) {
        Inventory inv = Bukkit.createInventory(null, 27, "Deposit Coins");
        double pocket = econ != null ? econ.getBalance(player) : 0.0;

        ItemStack grayGlass = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta glassMeta = grayGlass.getItemMeta();
        if (glassMeta != null) {
            glassMeta.setDisplayName("§7 ");
            grayGlass.setItemMeta(glassMeta);
        }
        for (int i = 0; i < 27; i++) inv.setItem(i, grayGlass);

        // Slot 10: Deposit 25% (GOLD_NUGGET)
        ItemStack nugget = new ItemStack(Material.GOLD_NUGGET);
        ItemMeta nMeta = nugget.getItemMeta();
        if (nMeta != null) {
            nMeta.setDisplayName("§aDeposit 25%");
            nMeta.setLore(Arrays.asList(
                    "§7Deposit §e25% §7of your current pocket coins.",
                    "§7Amount: §6" + BankDataManager.formatCoins(pocket * 0.25)
            ));
            nugget.setItemMeta(nMeta);
        }
        inv.setItem(10, nugget);

        // Slot 12: Deposit 50% (GOLD_INGOT)
        ItemStack ingot = new ItemStack(Material.GOLD_INGOT);
        ItemMeta iMeta = ingot.getItemMeta();
        if (iMeta != null) {
            iMeta.setDisplayName("§aDeposit 50%");
            iMeta.setLore(Arrays.asList(
                    "§7Deposit §e50% §7of your current pocket coins.",
                    "§7Amount: §6" + BankDataManager.formatCoins(pocket * 0.50)
            ));
            ingot.setItemMeta(iMeta);
        }
        inv.setItem(12, ingot);

        // Slot 14: Deposit 100% (GOLD_BLOCK)
        ItemStack block = new ItemStack(Material.GOLD_BLOCK);
        ItemMeta bMeta = block.getItemMeta();
        if (bMeta != null) {
            bMeta.setDisplayName("§aDeposit 100% (All)");
            bMeta.setLore(Arrays.asList(
                    "§7Deposit §eall §7your current pocket coins.",
                    "§7Amount: §6" + BankDataManager.formatCoins(pocket)
            ));
            block.setItemMeta(bMeta);
        }
        inv.setItem(14, block);

        // Slot 16: Specific Amount (PAPER)
        ItemStack paper = new ItemStack(Material.PAPER);
        ItemMeta pMeta = paper.getItemMeta();
        if (pMeta != null) {
            pMeta.setDisplayName("§aSpecific Amount");
            pMeta.setLore(Arrays.asList(
                    "§7Type the exact amount in chat."
            ));
            paper.setItemMeta(pMeta);
        }
        inv.setItem(16, paper);

        // Slot 22: Go Back (ARROW)
        ItemStack back = new ItemStack(Material.ARROW);
        ItemMeta backMeta = back.getItemMeta();
        if (backMeta != null) {
            backMeta.setDisplayName("§cGo Back");
            backMeta.setLore(Collections.singletonList("§7Return to your Bank account."));
            back.setItemMeta(backMeta);
        }
        inv.setItem(22, back);

        player.openInventory(inv);
    }

    /**
     * Mở giao diện phụ tùy chọn Withdraw Coins (27 slots)
     */
    public void openWithdrawGui(Player player) {
        Inventory inv = Bukkit.createInventory(null, 27, "Withdraw Coins");
        double balance = dataManager.getBalance(player.getUniqueId());

        ItemStack grayGlass = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta glassMeta = grayGlass.getItemMeta();
        if (glassMeta != null) {
            glassMeta.setDisplayName("§7 ");
            grayGlass.setItemMeta(glassMeta);
        }
        for (int i = 0; i < 27; i++) inv.setItem(i, grayGlass);

        // Slot 10: Withdraw 25% (GOLD_NUGGET)
        ItemStack nugget = new ItemStack(Material.GOLD_NUGGET);
        ItemMeta nMeta = nugget.getItemMeta();
        if (nMeta != null) {
            nMeta.setDisplayName("§aWithdraw 25%");
            nMeta.setLore(Arrays.asList(
                    "§7Withdraw §e25% §7of your bank balance.",
                    "§7Amount: §6" + BankDataManager.formatCoins(balance * 0.25)
            ));
            nugget.setItemMeta(nMeta);
        }
        inv.setItem(10, nugget);

        // Slot 12: Withdraw 50% (GOLD_INGOT)
        ItemStack ingot = new ItemStack(Material.GOLD_INGOT);
        ItemMeta iMeta = ingot.getItemMeta();
        if (iMeta != null) {
            iMeta.setDisplayName("§aWithdraw 50%");
            iMeta.setLore(Arrays.asList(
                    "§7Withdraw §e50% §7of your bank balance.",
                    "§7Amount: §6" + BankDataManager.formatCoins(balance * 0.50)
            ));
            ingot.setItemMeta(iMeta);
        }
        inv.setItem(12, ingot);

        // Slot 14: Withdraw 100% (GOLD_BLOCK)
        ItemStack block = new ItemStack(Material.GOLD_BLOCK);
        ItemMeta bMeta = block.getItemMeta();
        if (bMeta != null) {
            bMeta.setDisplayName("§aWithdraw 100% (All)");
            bMeta.setLore(Arrays.asList(
                    "§7Withdraw §eall §7your bank balance.",
                    "§7Amount: §6" + BankDataManager.formatCoins(balance)
            ));
            block.setItemMeta(bMeta);
        }
        inv.setItem(14, block);

        // Slot 16: Specific Amount (PAPER)
        ItemStack paper = new ItemStack(Material.PAPER);
        ItemMeta pMeta = paper.getItemMeta();
        if (pMeta != null) {
            pMeta.setDisplayName("§aSpecific Amount");
            pMeta.setLore(Arrays.asList(
                    "§7Type the exact amount in chat."
            ));
            paper.setItemMeta(pMeta);
        }
        inv.setItem(16, paper);

        // Slot 22: Go Back (ARROW)
        ItemStack back = new ItemStack(Material.ARROW);
        ItemMeta backMeta = back.getItemMeta();
        if (backMeta != null) {
            backMeta.setDisplayName("§cGo Back");
            backMeta.setLore(Collections.singletonList("§7Return to your Bank account."));
            back.setItemMeta(backMeta);
        }
        inv.setItem(22, back);

        player.openInventory(inv);
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        String title = event.getView().getTitle();
        if (title.equals("Personal Bank Account") || title.equals("Deposit Coins") || title.equals("Withdraw Coins")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        String title = event.getView().getTitle();
        if (!title.equals("Personal Bank Account") && !title.equals("Deposit Coins") && !title.equals("Withdraw Coins")) {
            return;
        }

        event.setCancelled(true);

        if (!(event.getWhoClicked() instanceof Player)) return;
        Player player = (Player) event.getWhoClicked();
        UUID uuid = player.getUniqueId();
        int slot = event.getRawSlot();

        if (title.equals("Personal Bank Account")) {
            switch (slot) {
                case 11: // Deposit Coins
                    openDepositGui(player);
                    break;
                case 13: // Withdraw Coins
                    openWithdrawGui(player);
                    break;
                case 31: // Close GUI
                    player.closeInventory();
                    break;
            }
        } else if (title.equals("Deposit Coins")) {
            double pocket = econ != null ? econ.getBalance(player) : 0.0;
            switch (slot) {
                case 10: // Deposit 25%
                    executeDeposit(player, Math.floor(pocket * 0.25));
                    break;
                case 12: // Deposit 50%
                    executeDeposit(player, Math.floor(pocket * 0.50));
                    break;
                case 14: // Deposit 100%
                    executeDeposit(player, pocket);
                    break;
                case 16: // Specific Amount
                    chatInputPlayers.put(uuid, "DEPOSIT");
                    player.closeInventory();
                    player.sendMessage("§e[Bank] Vui lòng nhập số tiền bạn muốn GỬI vào ngân hàng (hoặc gõ 'cancel' để hủy):");
                    break;
                case 22: // Go Back
                    openMainGui(player);
                    break;
            }
        } else if (title.equals("Withdraw Coins")) {
            double balance = dataManager.getBalance(uuid);
            switch (slot) {
                case 10: // Withdraw 25%
                    executeWithdraw(player, Math.floor(balance * 0.25));
                    break;
                case 12: // Withdraw 50%
                    executeWithdraw(player, Math.floor(balance * 0.50));
                    break;
                case 14: // Withdraw 100%
                    executeWithdraw(player, balance);
                    break;
                case 16: // Specific Amount
                    chatInputPlayers.put(uuid, "WITHDRAW");
                    player.closeInventory();
                    player.sendMessage("§e[Bank] Vui lòng nhập số tiền bạn muốn RÚT từ ngân hàng (hoặc gõ 'cancel' để hủy):");
                    break;
                case 22: // Go Back
                    openMainGui(player);
                    break;
            }
        }
    }

    private void executeDeposit(Player player, double amount) {
        if (amount <= 0) {
            player.sendMessage("§cSố tiền giao dịch phải lớn hơn 0!");
            openDepositGui(player);
            return;
        }

        double pocket = econ != null ? econ.getBalance(player) : 0.0;
        if (pocket < amount) {
            player.sendMessage("§cBạn không có đủ tiền mặt trên người!");
            openDepositGui(player);
            return;
        }

        // Thực hiện trừ tiền mặt và cộng vào số dư ngân hàng
        if (econ != null) {
            econ.withdrawPlayer(player, amount);
            dataManager.addBalance(player.getUniqueId(), amount);
            dataManager.addTransaction(player.getUniqueId(), "DEPOSIT", amount, player.getName());
            player.sendMessage("§a[Bank] Đã gửi §e+" + BankDataManager.formatCoins(amount) + " coins §avào tài khoản ngân hàng!");
        }
        openMainGui(player);
    }

    private void executeWithdraw(Player player, double amount) {
        if (amount <= 0) {
            player.sendMessage("§cSố tiền giao dịch phải lớn hơn 0!");
            openWithdrawGui(player);
            return;
        }

        double balance = dataManager.getBalance(player.getUniqueId());
        if (balance < amount) {
            player.sendMessage("§cTài khoản ngân hàng của bạn không có đủ số dư!");
            openWithdrawGui(player);
            return;
        }

        // Thực hiện trừ số dư ngân hàng và cộng vào tiền mặt
        if (econ != null) {
            dataManager.addBalance(player.getUniqueId(), -amount);
            econ.depositPlayer(player, amount);
            dataManager.addTransaction(player.getUniqueId(), "WITHDRAW", amount, player.getName());
            player.sendMessage("§a[Bank] Đã rút §e-" + BankDataManager.formatCoins(amount) + " coins §ctừ tài khoản ngân hàng!");
        }
        openMainGui(player);
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        if (!chatInputPlayers.containsKey(uuid)) return;

        event.setCancelled(true);
        String action = chatInputPlayers.remove(uuid);
        String message = event.getMessage().trim();

        if (message.equalsIgnoreCase("cancel")) {
            player.sendMessage("§cGiao dịch đã được hủy bỏ.");
            // Mở lại GUI chính trên main thread
            Bukkit.getScheduler().runTask(plugin, () -> openMainGui(player));
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(message);
        } catch (NumberFormatException e) {
            player.sendMessage("§cSố tiền nhập vào không hợp lệ! Vui lòng nhập số hợp lệ.");
            // Hỏi lại
            chatInputPlayers.put(uuid, action);
            player.sendMessage("§e[Bank] Vui lòng nhập lại số tiền (hoặc gõ 'cancel' để hủy):");
            return;
        }

        if (amount <= 0) {
            player.sendMessage("§cSố tiền nhập vào phải lớn hơn 0!");
            chatInputPlayers.put(uuid, action);
            player.sendMessage("§e[Bank] Vui lòng nhập lại số tiền (hoặc gõ 'cancel' để hủy):");
            return;
        }

        // Chạy giao dịch trên main thread để tránh async safe issues với Vault/Inventories
        Bukkit.getScheduler().runTask(plugin, () -> {
            if (action.equals("DEPOSIT")) {
                executeDeposit(player, Math.floor(amount));
            } else if (action.equals("WITHDRAW")) {
                executeWithdraw(player, Math.floor(amount));
            }
        });
    }
}

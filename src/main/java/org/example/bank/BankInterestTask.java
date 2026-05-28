package org.example.bank;

import org.example.core.CustomWeaponEngine;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class BankInterestTask extends BukkitRunnable {

    private final CustomWeaponEngine plugin;
    private final BankDataManager dataManager;

    public BankInterestTask(CustomWeaponEngine plugin) {
        this.plugin = plugin;
        this.dataManager = BankDataManager.getInstance(plugin);
    }

    @Override
    public void run() {
        long now = System.currentTimeMillis();
        long nextInterest = dataManager.getNextInterestTime();

        if (now >= nextInterest) {
            // Đến thời gian phát lãi!
            payInterest();
            
            // Đặt lại thời gian phát lãi tiếp theo (31 giờ sau)
            long nextTime = System.currentTimeMillis() + (31L * 3600L * 1000L);
            dataManager.setNextInterestTime(nextTime);
            plugin.getLogger().info("🟩 Đã phát lãi suất 2% ngân hàng cho tất cả tài khoản!");
        }
    }

    private void payInterest() {
        if (dataManager.getConfig().getConfigurationSection("players") == null) {
            return;
        }

        for (String key : dataManager.getConfig().getConfigurationSection("players").getKeys(false)) {
            try {
                UUID uuid = UUID.fromString(key);
                double balance = dataManager.getBalance(uuid);

                if (balance > 0) {
                    // Lãi suất chỉ áp dụng tối đa cho 10.000.000 Coins đầu tiên
                    double calculableBalance = Math.min(balance, 10000000.0);
                    double interest = calculableBalance * 0.02;

                    if (interest > 0) {
                        dataManager.addBalance(uuid, interest);
                        dataManager.addTransaction(uuid, "INTEREST", interest, "Bank Interest");

                        // Nếu người chơi đang online, thông báo cho họ biết
                        Player onlinePlayer = Bukkit.getPlayer(uuid);
                        if (onlinePlayer != null) {
                            onlinePlayer.sendMessage("§a[Bank] Bạn nhận được §e+" + BankDataManager.formatCoins(interest) + " coins §alãi suất tiết kiệm từ ngân hàng!");
                        }
                    }
                }
            } catch (IllegalArgumentException e) {
                // Bỏ qua nếu key không phải UUID hợp lệ
            }
        }
        dataManager.save();
    }
}

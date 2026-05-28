package org.example.bank;

import org.example.core.CustomWeaponEngine;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BankDataManager {

    private static BankDataManager instance;
    private final CustomWeaponEngine plugin;
    private File file;
    private FileConfiguration config;

    private BankDataManager(CustomWeaponEngine plugin) {
        this.plugin = plugin;
        reload();
    }

    public static BankDataManager getInstance(CustomWeaponEngine plugin) {
        if (instance == null) {
            instance = new BankDataManager(plugin);
        }
        return instance;
    }

    public static BankDataManager getInstance() {
        return instance;
    }

    public void reload() {
        file = new File(plugin.getDataFolder(), "bank_data.yml");
        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        config = YamlConfiguration.loadConfiguration(file);

        // Khởi tạo thời gian phát lãi nếu chưa có (mặc định 31 giờ)
        if (!config.contains("next-interest-time")) {
            long nextTime = System.currentTimeMillis() + (31L * 3600L * 1000L);
            config.set("next-interest-time", nextTime);
            save();
        }
    }

    public void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public double getBalance(UUID uuid) {
        return config.getDouble("players." + uuid.toString() + ".balance", 0.0);
    }

    public void setBalance(UUID uuid, double amount) {
        config.set("players." + uuid.toString() + ".balance", amount);
        save();
    }

    public void addBalance(UUID uuid, double amount) {
        setBalance(uuid, getBalance(uuid) + amount);
    }

    public long getNextInterestTime() {
        return config.getLong("next-interest-time");
    }

    public void setNextInterestTime(long time) {
        config.set("next-interest-time", time);
        save();
    }

    public FileConfiguration getConfig() {
        return config;
    }

    /**
     * Thêm lịch sử giao dịch mới cho người chơi.
     * Cấu trúc lưu: TYPE;amount;timestamp;playerName
     */
    public void addTransaction(UUID uuid, String type, double amount, String playerName) {
        String path = "players." + uuid.toString() + ".transactions";
        List<String> transactions = config.getStringList(path);
        if (transactions == null) {
            transactions = new ArrayList<>();
        }

        String transactionEntry = type + ";" + amount + ";" + System.currentTimeMillis() + ";" + playerName;
        transactions.add(transactionEntry);

        // Giới hạn chỉ lưu tối đa 20 giao dịch gần nhất
        if (transactions.size() > 20) {
            transactions.remove(0);
        }

        config.set(path, transactions);
        save();
    }

    /**
     * Lấy danh sách 3 giao dịch gần nhất dưới dạng Lore đã format màu sắc.
     */
    public List<String> getRecentTransactionsLore(UUID uuid) {
        List<String> lore = new ArrayList<>();
        String path = "players." + uuid.toString() + ".transactions";
        List<String> transactions = config.getStringList(path);

        if (transactions == null || transactions.isEmpty()) {
            lore.add("§7No recent transactions.");
            return lore;
        }

        int start = Math.max(0, transactions.size() - 3);
        // Lấy 3 giao dịch cuối cùng và đảo ngược thứ tự để giao dịch mới nhất hiện lên đầu
        for (int i = transactions.size() - 1; i >= start; i--) {
            String entry = transactions.get(i);
            String[] parts = entry.split(";");
            if (parts.length < 4) continue;

            String type = parts[0];
            double amount = Double.parseDouble(parts[1]);
            long timestamp = Long.parseLong(parts[2]);
            String name = parts[3];

            String formattedAmount = formatCoins(amount);
            String timeElapsed = formatTimeElapsed(timestamp);

            if (type.equalsIgnoreCase("WITHDRAW")) {
                lore.add("§c- " + formattedAmount + ", " + timeElapsed + " ago §7by §b" + name);
            } else if (type.equalsIgnoreCase("DEPOSIT")) {
                lore.add("§a+ " + formattedAmount + ", " + timeElapsed + " ago §7by §b" + name);
            } else if (type.equalsIgnoreCase("INTEREST")) {
                lore.add("§a+ " + formattedAmount + ", " + timeElapsed + " ago §7by §aBank Interest");
            }
        }
        return lore;
    }

    public static String formatCoins(double value) {
        long amount = Math.round(value);
        return String.format("%,d", amount).replace(',', '.');
    }

    public static String formatTimeElapsed(long timestampMs) {
        long elapsed = System.currentTimeMillis() - timestampMs;
        if (elapsed < 0) elapsed = 0;
        long seconds = elapsed / 1000;
        if (seconds < 60) {
            return seconds + " secs";
        }
        long minutes = seconds / 60;
        if (minutes < 60) {
            return minutes + " mins";
        }
        long hours = minutes / 60;
        if (hours < 24) {
            return hours + " hours";
        }
        long days = hours / 24;
        return days + " days";
    }

    public static String formatTimeRemaining(long ms) {
        if (ms <= 0) return "0m";
        long seconds = ms / 1000;
        long hours = seconds / 3600;
        long minutes = (seconds % 3600) / 60;
        if (hours > 0) {
            return hours + "h, " + minutes + "m";
        } else {
            return minutes + "m";
        }
    }
}

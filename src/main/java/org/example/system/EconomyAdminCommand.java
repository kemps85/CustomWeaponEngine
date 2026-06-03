package org.example.system;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.example.core.CustomWeaponEngine;

public class EconomyAdminCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("cwe.admin")) {
            sender.sendMessage("§cBạn không có quyền sử dụng lệnh này!");
            return true;
        }

        if (args.length < 3) {
            sender.sendMessage("§cSử dụng: /cweeco <give|set> <người_chơi> <số_tiền>");
            return true;
        }

        String action = args[0];
        Player target = Bukkit.getPlayer(args[1]);
        if (target == null || !target.isOnline()) {
            sender.sendMessage("§cNgười chơi không online hoặc không tồn tại!");
            return true;
        }

        double amount;
        try {
            amount = Double.parseDouble(args[2]);
        } catch (NumberFormatException e) {
            sender.sendMessage("§cSố tiền không hợp lệ!");
            return true;
        }

        Economy econ = CustomWeaponEngine.getEconomy();
        if (econ == null) {
            sender.sendMessage("§cChưa kết nối được với Vault Economy!");
            return true;
        }

        if (action.equalsIgnoreCase("give")) {
            econ.depositPlayer(target, amount);
            sender.sendMessage("§aĐã cộng §e$" + amount + " §avào tài khoản của §e" + target.getName());
            target.sendMessage("§aBạn vừa được Admin cộng §e$" + amount + " §avào tài khoản Pocket!");
        } else if (action.equalsIgnoreCase("set")) {
            double current = econ.getBalance(target);
            econ.withdrawPlayer(target, current);
            econ.depositPlayer(target, amount);
            sender.sendMessage("§aĐã set tài khoản của §e" + target.getName() + " §athành §e$" + amount);
            target.sendMessage("§aTài khoản Pocket của bạn đã được Admin set thành §e$" + amount);
        } else {
            sender.sendMessage("§cSử dụng: /cweeco <give|set> <người_chơi> <số_tiền>");
        }

        return true;
    }
}

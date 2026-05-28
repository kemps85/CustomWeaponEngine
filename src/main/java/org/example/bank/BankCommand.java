package org.example.bank;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class BankCommand implements CommandExecutor {

    private final BankGui bankGui;

    public BankCommand(BankGui bankGui) {
        this.bankGui = bankGui;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cChỉ có người chơi trong game mới có thể sử dụng lệnh này!");
            return true;
        }

        Player player = (Player) sender;
        bankGui.openMainGui(player);
        return true;
    }
}

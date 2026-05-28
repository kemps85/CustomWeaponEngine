package org.example.system;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.example.core.CustomWeaponEngine;

public class TradeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Lệnh chỉ dành cho người chơi.");
            return true;
        }
        
        Player player = (Player) sender;
        TradeManager tm = CustomWeaponEngine.getTradeManager();

        if (args.length == 0) {
            player.sendMessage("§cSử dụng: /trade <tên người chơi|accept|cancel>");
            return true;
        }

        if (args[0].equalsIgnoreCase("accept")) {
            tm.acceptRequest(player);
            return true;
        }

        if (args[0].equalsIgnoreCase("cancel")) {
            tm.cancelRequest(player);
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null || !target.isOnline()) {
            player.sendMessage("§cKhông tìm thấy người chơi này!");
            return true;
        }

        if (target.equals(player)) {
            player.sendMessage("§cBạn không thể giao dịch với chính mình!");
            return true;
        }

        if (player.getLocation().distance(target.getLocation()) > 20) {
            player.sendMessage("§cNgười chơi này ở quá xa (> 20 block)!");
            return true;
        }

        tm.sendRequest(player, target);
        return true;
    }
}

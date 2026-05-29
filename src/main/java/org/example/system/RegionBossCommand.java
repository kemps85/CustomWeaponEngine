package org.example.system;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.example.core.CustomWeaponEngine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RegionBossCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("cwe.admin")) {
            sender.sendMessage("§cBạn không có quyền dùng lệnh này!");
            return true;
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("setspawn")) {
            if (!(sender instanceof Player)) return true;
            Player p = (Player) sender;
            String type = args[1].toUpperCase();
            if (type.equals("FIRE") || type.equals("ICE") || type.equals("VOID")) {
                CustomWeaponEngine.regionBossManager.setSpawn(type, p.getLocation().getBlock().getLocation());
                p.sendMessage("§aĐã đặt đấu trường cho Boss " + type + " tại vị trí hiện tại!");
            } else {
                p.sendMessage("§cLoại Boss không hợp lệ! (FIRE, ICE, VOID)");
            }
            return true;
        }
        
        if (args.length == 2 && args[0].equalsIgnoreCase("spawn")) {
            String type = args[1].toUpperCase();
            if (type.equals("FIRE") || type.equals("ICE") || type.equals("VOID")) {
                CustomWeaponEngine.regionBossManager.startEvent(type);
                sender.sendMessage("§aĐã ép Boss " + type + " xuất hiện (Bỏ qua cooldown).");
            } else {
                sender.sendMessage("§cLoại Boss không hợp lệ!");
            }
            return true;
        }

        sender.sendMessage("§6§l══════ BOSS VÙNG ══════");
        sender.sendMessage("§e/cweboss setspawn <FIRE|ICE|VOID> §7- Đặt vị trí đấu trường");
        sender.sendMessage("§e/cweboss spawn <FIRE|ICE|VOID> §7- Ép Boss xuất hiện lập tức");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            completions.add("setspawn");
            completions.add("spawn");
        } else if (args.length == 2) {
            completions.add("FIRE");
            completions.add("ICE");
            completions.add("VOID");
        }
        return completions;
    }
}

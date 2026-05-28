package org.example.system;

import org.example.core.CustomWeaponEngine;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class CWELibraryCommand implements CommandExecutor, TabCompleter {
    private final CustomWeaponEngine plugin;

    public CWELibraryCommand(CustomWeaponEngine plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("cwe.admin")) {
            sender.sendMessage("§cBạn không có quyền dùng lệnh này!");
            return true;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage("§cChỉ người chơi mới có thể dùng lệnh này!");
            return true;
        }
        
        Player player = (Player) sender;

        if (args.length == 0 || args[0].equalsIgnoreCase("lib") || args[0].equalsIgnoreCase("library")) {
            CustomWeaponEngine.getLibraryGUI().openMainMenu(player);
            return true;
        }

        if (args[0].equalsIgnoreCase("save")) {
            if (args.length < 2) {
                player.sendMessage("§cSử dụng: /cwe save <id>");
                return true;
            }
            String id = args[1].toLowerCase();
            ItemStack item = player.getInventory().getItemInMainHand();

            if (item.getType() == Material.AIR) {
                player.sendMessage("§cMày đéo cầm cái gì trên tay cả!");
                return true;
            }

            plugin.getLibraryConfig().set("items." + id, item);
            plugin.saveLibraryConfig();
            player.sendMessage("§a[CWE] Đã lưu trữ vật phẩm trên tay vào thư viện với ID: §e" + id);
            return true;
        }

        if (args[0].equalsIgnoreCase("delete") || args[0].equalsIgnoreCase("remove")) {
            if (args.length < 2) {
                player.sendMessage("§cSử dụng: /cwe delete <id>");
                return true;
            }
            String id = args[1].toLowerCase();
            if (!plugin.getLibraryConfig().contains("items." + id)) {
                player.sendMessage("§c[CWE] Không tìm thấy vật phẩm có ID: §e" + id);
                return true;
            }

            plugin.getLibraryConfig().set("items." + id, null);
            plugin.saveLibraryConfig();
            player.sendMessage("§c[CWE] Đã xóa vĩnh viễn vật phẩm §e" + id + " §ckhỏi thư viện!");
            return true;
        }

        player.sendMessage("§cSử dụng: /cwe <save|delete|lib> [id]");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        if (!sender.hasPermission("cwe.admin")) return completions;

        if (args.length == 1) {
            List<String> subCommands = Arrays.asList("save", "delete", "remove", "lib", "library");
            for (String sub : subCommands) {
                if (sub.startsWith(args[0].toLowerCase())) completions.add(sub);
            }
        } else if (args.length == 2 && (args[0].equalsIgnoreCase("delete") || args[0].equalsIgnoreCase("remove"))) {
            FileConfiguration config = plugin.getLibraryConfig();
            if (config.contains("items")) {
                Set<String> keys = config.getConfigurationSection("items").getKeys(false);
                for (String key : keys) {
                    if (key.toLowerCase().startsWith(args[1].toLowerCase())) {
                        completions.add(key);
                    }
                }
            }
        }
        return completions;
    }
}

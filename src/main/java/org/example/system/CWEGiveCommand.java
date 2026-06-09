package org.example.system;

import org.example.core.CustomWeaponEngine;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

public class CWEGiveCommand implements CommandExecutor, TabCompleter {
    private final CustomWeaponEngine plugin;

    public CWEGiveCommand(CustomWeaponEngine plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("cwe.admin")) {
            sender.sendMessage("§cBạn không có quyền sử dụng lệnh này!");
            return true;
        }

        if (args.length < 2) {
            sender.sendMessage("§cUsage: /cwegive <player> <item_id> [amount]");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage("§cKhông tìm thấy người chơi!");
            return true;
        }

        String itemId = args[1].toLowerCase();
        int amount = 1;
        if (args.length >= 3) {
            try {
                amount = Integer.parseInt(args[2]);
            } catch (NumberFormatException e) {
                sender.sendMessage("§cSố lượng không hợp lệ!");
                return true;
            }
        }

        FileConfiguration library = plugin.getLibraryConfig();
        ItemStack item = library.getItemStack("items." + itemId);

        // Nếu không có trong thư viện, thử tìm trong Code Setup (ItemGenerator)
        if (item == null) {
            ItemGenerator generator = new ItemGenerator(plugin);
            item = generator.generateItem(itemId);
        }

        if (item == null) {
            sender.sendMessage("§cKhông tìm thấy vật phẩm với ID: " + itemId + " trong thư viện library.yml hay Code Setup!");
            return true;
        }

        ItemStack giveItem = item.clone();
        giveItem.setAmount(amount);
        target.getInventory().addItem(giveItem);
        sender.sendMessage("§aĐã cấp " + amount + " " + itemId + " cho " + target.getName());
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p.getName().toLowerCase().startsWith(args[0].toLowerCase())) {
                    completions.add(p.getName());
                }
            }
        } else if (args.length == 2) {
            String query = args[1].toLowerCase();
            FileConfiguration library = plugin.getLibraryConfig();
            if (library.contains("items")) {
                for (String key : library.getConfigurationSection("items").getKeys(false)) {
                    if (key.toLowerCase().startsWith(query)) {
                        completions.add(key);
                    }
                }
            }
            String[] hardcoded = {
                "runaan", "runaan_bow", "shortbow", "juju_shortbow", "astral_shepherd_wand",
                "loi_dung_nham", "lava", "lava_core", "bang_tinh_co_dai", "ice", "ice_crystal",
                "loi_nang_luong", "energy", "energy_core", "fragment_rare", "manh_vo_tinh_tu",
                "fragment_epic", "manh_vo_hon_mang", "fragment_legendary", "manh_vo_anh_sang",
                "fragment_mythic", "manh_vo_vu_tru", "excalibur", "cwe_excalibur",
                "autocraft_bag", "compactor"
            };
            for (String h : hardcoded) {
                if (h.startsWith(query) && !completions.contains(h)) {
                    completions.add(h);
                }
            }
        }
        return completions;
    }
}

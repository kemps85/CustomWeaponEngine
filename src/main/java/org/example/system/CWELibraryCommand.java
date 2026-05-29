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

            // Cập nhật lại ID trong PDC trước khi lưu để khi lấy ra từ LibraryGUI nó không bị lỗi ID cũ
            org.bukkit.inventory.meta.ItemMeta meta = item.getItemMeta();
            if (meta != null) {
                meta.getPersistentDataContainer().set(new org.bukkit.NamespacedKey(plugin, "cwe_id"), org.bukkit.persistence.PersistentDataType.STRING, id);
                item.setItemMeta(meta);
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

        if (args[0].equalsIgnoreCase("chest")) {
            if (args.length < 2) {
                player.sendMessage("§cSử dụng: /cwe chest <create <id> <type>|delete <id>>");
                return true;
            }
            if (args[1].equalsIgnoreCase("create")) {
                if (args.length < 4) {
                    player.sendMessage("§cSử dụng: /cwe chest create <id> <type(gacha|coin)>");
                    return true;
                }
                String id = args[2];
                String type = args[3];
                org.bukkit.block.Block target = player.getTargetBlockExact(5);
                if (target == null || target.getType() == Material.AIR) {
                    player.sendMessage("§cBạn phải nhìn vào một Block để đặt làm Rương Ẩn!");
                    return true;
                }
                String locStr = target.getWorld().getName() + "_" + target.getX() + "_" + target.getY() + "_" + target.getZ();
                plugin.getConfig().set("hidden_chests." + locStr + ".id", id);
                plugin.getConfig().set("hidden_chests." + locStr + ".type", type);
                plugin.saveConfig();
                player.sendMessage("§a[CWE] Đã tạo Rương Thám Hiểm (" + id + " - " + type + ") tại " + locStr);
            } else if (args[1].equalsIgnoreCase("delete")) {
                org.bukkit.block.Block target = player.getTargetBlockExact(5);
                if (target == null || target.getType() == Material.AIR) {
                    player.sendMessage("§cBạn phải nhìn vào Rương Ẩn để xóa!");
                    return true;
                }
                String locStr = target.getWorld().getName() + "_" + target.getX() + "_" + target.getY() + "_" + target.getZ();
                plugin.getConfig().set("hidden_chests." + locStr, null);
                plugin.saveConfig();
                player.sendMessage("§c[CWE] Đã xóa Rương Thám Hiểm tại " + locStr);
            }
            return true;
        }

        if (args[0].equalsIgnoreCase("furniture")) {
            if (args.length < 2) {
                player.sendMessage("§cSử dụng: /cwe furniture <bonsai|teacup|globe|cake|treasure>");
                return true;
            }
            ItemStack furn = org.example.system.FurnitureGenerator.getFurniture(plugin, args[1]);
            if (furn == null) {
                player.sendMessage("§cKhông tìm thấy nội thất: " + args[1]);
                return true;
            }
            player.getInventory().addItem(furn);
            player.sendMessage("§a[CWE] Đã lấy nội thất: " + args[1]);
            return true;
        }

        player.sendMessage("§cSử dụng: /cwe <save|delete|lib|chest|furniture> [args]");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        if (!sender.hasPermission("cwe.admin")) return completions;

        if (args.length == 1) {
            List<String> subCommands = Arrays.asList("save", "delete", "remove", "lib", "library", "chest", "furniture");
            for (String sub : subCommands) {
                if (sub.startsWith(args[0].toLowerCase())) completions.add(sub);
            }
        } else if (args.length == 2 && args[0].equalsIgnoreCase("chest")) {
            completions.addAll(Arrays.asList("create", "delete"));
        } else if (args.length == 2 && args[0].equalsIgnoreCase("furniture")) {
            completions.addAll(Arrays.asList("bonsai", "teacup", "globe", "cake", "treasure"));
        } else if (args.length == 4 && args[0].equalsIgnoreCase("chest") && args[1].equalsIgnoreCase("create")) {
            completions.addAll(Arrays.asList("gacha", "coin"));
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

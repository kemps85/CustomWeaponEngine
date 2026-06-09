package org.example.system;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.example.core.CustomWeaponEngine;
import org.example.enchant.CustomEnchant;
import org.example.enchant.UltimateEnchant;
import org.example.stats.ItemBuilder;

import java.util.ArrayList;
import java.util.List;

public class CustomBookCommand implements CommandExecutor, TabCompleter {

    private final CustomWeaponEngine plugin;

    public CustomBookCommand(CustomWeaponEngine plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("cwe.admin")) {
            sender.sendMessage(ChatColor.RED + "Bạn không có quyền sử dụng lệnh này.");
            return true;
        }

        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Sử dụng: /cwebook <enchant_id|ult_enchant_id> <level>");
            return true;
        }

        String enchantId = args[0].toLowerCase();
        int level;
        try {
            level = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.RED + "Level phải là một số nguyên.");
            return true;
        }

        CustomEnchant customEnchant = null;
        for (CustomEnchant ce : CustomEnchant.values()) {
            if (ce.getId().equalsIgnoreCase(enchantId)) {
                customEnchant = ce;
                break;
            }
        }

        UltimateEnchant ultimateEnchant = null;
        if (customEnchant == null) {
            for (UltimateEnchant ue : UltimateEnchant.values()) {
                if (ue.getId().equalsIgnoreCase(enchantId)) {
                    ultimateEnchant = ue;
                    break;
                }
            }
        }

        if (customEnchant == null && ultimateEnchant == null) {
            sender.sendMessage(ChatColor.RED + "Không tìm thấy Enchant có ID: " + enchantId);
            return true;
        }

        Player player = null;
        if (sender instanceof Player) {
            player = (Player) sender;
        }

        if (player == null) {
            sender.sendMessage(ChatColor.RED + "Lệnh này chỉ có thể sử dụng bởi người chơi.");
            return true;
        }

        ItemStack book = new ItemStack(Material.ENCHANTED_BOOK);
        ItemMeta meta = book.getItemMeta();
        if (meta == null) return true;

        if (customEnchant != null) {
            NamespacedKey key = new NamespacedKey(plugin, "enchant_" + customEnchant.getId());
            meta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, level);
            meta.setDisplayName("§eEnchanted Book");

            List<String> lore = new ArrayList<>();
            lore.add("§7Nhóm: §e" + customEnchant.getItemGroup().name());
            lore.add("§7Cấp được ép: §c" + level + "/" + customEnchant.getMaxLevel());
            lore.add("");
            lore.add("§eHiệu quả: §f" + customEnchant.getDescription());
            lore.add("");
            lore.add("§7Dùng đe (Anvil) để ép vào trang bị.");
            meta.setLore(lore);
        } else {
            NamespacedKey key = new NamespacedKey(plugin, "ult_enchant_" + ultimateEnchant.getId());
            meta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, level);
            meta.setDisplayName("§d§lUltimate Enchanted Book");

            List<String> lore = new ArrayList<>();
            lore.add("§7Nhóm: §dULTIMATE " + ultimateEnchant.getGroup().name());
            lore.add("§7Cấp được ép: §c" + level + "/" + ultimateEnchant.getMaxLevel());
            lore.add("");
            lore.add("§eHiệu quả: §f" + ultimateEnchant.getDescription());
            lore.add("");
            lore.add("§7Dùng đe (Anvil) để ép vào trang bị.");
            meta.setLore(lore);
        }

        book.setItemMeta(meta);

        player.getInventory().addItem(book);
        player.sendMessage(ChatColor.GREEN + "Đã nhận được sách enchant!");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            String partial = args[0].toLowerCase();
            for (CustomEnchant ce : CustomEnchant.values()) {
                if (ce.getId().toLowerCase().startsWith(partial)) {
                    completions.add(ce.getId());
                }
            }
            for (UltimateEnchant ue : UltimateEnchant.values()) {
                if (ue.getId().toLowerCase().startsWith(partial)) {
                    completions.add(ue.getId());
                }
            }
        } else if (args.length == 2) {
            completions.add("1");
            completions.add("2");
            completions.add("3");
            completions.add("4");
            completions.add("5");
        }
        return completions;
    }
}

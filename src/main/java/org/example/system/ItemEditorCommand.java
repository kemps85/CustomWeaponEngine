package org.example.system;

import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
import org.bukkit.ChatColor;
import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
import org.bukkit.command.Command;
import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
import org.bukkit.command.CommandExecutor;
import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
import org.bukkit.command.CommandSender;
import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
import org.bukkit.entity.Player;
import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
import org.bukkit.inventory.ItemStack;
import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
import org.bukkit.inventory.meta.ItemMeta;

import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
import java.util.ArrayList;
import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
import java.util.List;

import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
public class ItemEditorCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§c[CWE] Only players can use this command!");
            return true;
        }
        Player player = (Player) sender;
        if (!player.hasPermission("cwe.admin")) {
            player.sendMessage("§c[CWE] Bạn không có quyền sử dụng lệnh này!");
            return true;
        }
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item == null || item.getType().isAir()) {
            player.sendMessage("§c[CWE] Bạn phải cầm một trang bị trên tay chính!");
            return true;
        }

        if (args.length == 0) {
            player.sendMessage("§e[CWE] Lệnh hợp lệ:\n§a• /cweie rename <tên>\n§a• /cweie lore add <lore>\n§a• /cweie nbt <giá_trị_cwe_id>");
            return true;
        }

        if (args[0].equalsIgnoreCase("rename")) {
            if (args.length < 2) {
                player.sendMessage("§c[CWE] Hướng dẫn: /cweie rename <tên mới>");
                return true;
            }
            StringBuilder sb = new StringBuilder();
            for (int i = 1; i < args.length; i++) {
                sb.append(args[i]).append(" ");
            }
            String name = ChatColor.translateAlternateColorCodes('&', sb.toString().trim());
            ItemMeta meta = item.getItemMeta();
            if (meta != null) {
                meta.setDisplayName(name);
                
                // Update cwe_original_name so ReforgeSystem uses this new name
                org.bukkit.plugin.java.JavaPlugin plugin = org.bukkit.plugin.java.JavaPlugin.getProvidingPlugin(ItemEditorCommand.class);
                org.bukkit.NamespacedKey ogNameKey = new org.bukkit.NamespacedKey(plugin, "cwe_original_name");
                meta.getPersistentDataContainer().set(ogNameKey, org.bukkit.persistence.PersistentDataType.STRING, name);

                item.setItemMeta(meta);
                player.sendMessage("§a[CWE] Đổi tên vật phẩm thành công: " + name);
            }
            return true;
        } else if (args[0].equalsIgnoreCase("lore")) {
            if (args.length < 3 || !args[1].equalsIgnoreCase("add")) {
                player.sendMessage("§c[CWE] Hướng dẫn: /cweie lore add <dòng lore>");
                return true;
            }
            StringBuilder sb = new StringBuilder();
            for (int i = 2; i < args.length; i++) {
                sb.append(args[i]).append(" ");
            }
            String loreLine = ChatColor.translateAlternateColorCodes('&', sb.toString().trim());
            ItemMeta meta = item.getItemMeta();
            if (meta != null) {
                List<String> lore = meta.hasLore() ? meta.getLore() : new ArrayList<>();
                if (lore == null) lore = new ArrayList<>();
                lore.add(loreLine);
                meta.setLore(lore);
                item.setItemMeta(meta);
                player.sendMessage("§a[CWE] Đã thêm dòng lore thành công: " + loreLine);
            }
            return true;
        } else if (args[0].equalsIgnoreCase("nbt")) {
            if (args.length < 2) {
                player.sendMessage("§c[CWE] Hướng dẫn: /cweie nbt <cwe_id> (VD: /cweie nbt cosmic_void_helmet)");
                return true;
            }
            String cweId = args[1].toLowerCase().trim();
            ItemMeta meta = item.getItemMeta();
            if (meta != null) {
                org.bukkit.plugin.java.JavaPlugin plugin = org.bukkit.plugin.java.JavaPlugin.getProvidingPlugin(ItemEditorCommand.class);
                org.bukkit.NamespacedKey key = new org.bukkit.NamespacedKey(plugin, "cwe_id");
                meta.getPersistentDataContainer().set(key, org.bukkit.persistence.PersistentDataType.STRING, cweId);
                item.setItemMeta(meta);
                player.sendMessage("§a[CWE] Đã gán NBT cwe_id = " + cweId + " cho vật phẩm đang cầm!");
            }
            return true;
        } else {
            player.sendMessage("§e[CWE] Lệnh hợp lệ:\n§a• /cweie rename <tên>\n§a• /cweie lore add <lore>\n§a• /cweie nbt <giá_trị_cwe_id>");
        }
        return true;
    }
}


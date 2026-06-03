/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.NamespacedKey
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.command.TabCompleter
 *  org.bukkit.entity.ArmorStand
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.persistence.PersistentDataContainer
 *  org.bukkit.persistence.PersistentDataType
 *  org.bukkit.plugin.Plugin
 */
package org.example.system;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.example.core.CustomWeaponEngine;

public class RegionBossCommand
implements CommandExecutor,
TabCompleter {
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("cwe.admin")) {
            sender.sendMessage("\u00a7cB\u1ea1n kh\u00f4ng c\u00f3 quy\u1ec1n d\u00f9ng l\u1ec7nh n\u00e0y!");
            return true;
        }
        if (args.length == 2 && args[0].equalsIgnoreCase("setspawn")) {
            if (!(sender instanceof Player)) {
                return true;
            }
            Player p = (Player)sender;
            String type = args[1].toUpperCase();
            if (type.equals("FIRE") || type.equals("ICE") || type.equals("VOID")) {
                CustomWeaponEngine.regionBossManager.setSpawn(type, p.getLocation().getBlock().getLocation());
                p.sendMessage("\u00a7a\u0110\u00e3 \u0111\u1eb7t \u0111\u1ea5u tr\u01b0\u1eddng cho Boss " + type + " t\u1ea1i v\u1ecb tr\u00ed hi\u1ec7n t\u1ea1i!");
            } else {
                p.sendMessage("\u00a7cLo\u1ea1i Boss kh\u00f4ng h\u1ee3p l\u1ec7! (FIRE, ICE, VOID)");
            }
            return true;
        }
        if (args.length == 2 && args[0].equalsIgnoreCase("removespawn")) {
            String type = args[1].toUpperCase();
            if (type.equals("FIRE") || type.equals("ICE") || type.equals("VOID")) {
                CustomWeaponEngine.regionBossManager.removeSpawn(type);
                sender.sendMessage("\u00a7a\u0110\u00e3 x\u00f3a \u0111i\u1ec3m spawn v\u00e0 hologram c\u1ee7a Boss " + type + "!");
            } else {
                sender.sendMessage("\u00a7cLo\u1ea1i Boss kh\u00f4ng h\u1ee3p l\u1ec7!");
            }
            return true;
        }
        if (args.length == 2 && args[0].equalsIgnoreCase("spawn")) {
            String type = args[1].toUpperCase();
            if (type.equals("FIRE") || type.equals("ICE") || type.equals("VOID")) {
                CustomWeaponEngine.regionBossManager.startEvent(type);
                sender.sendMessage("\u00a7a\u0110\u00e3 \u00e9p Boss " + type + " xu\u1ea5t hi\u1ec7n (B\u1ecf qua cooldown).");
            } else {
                sender.sendMessage("\u00a7cLo\u1ea1i Boss kh\u00f4ng h\u1ee3p l\u1ec7!");
            }
            return true;
        }
        if (args.length >= 3 && args[0].equalsIgnoreCase("givefragment")) {
            ItemStack fragment;
            Player target = Bukkit.getPlayer((String)args[1]);
            if (target == null) {
                sender.sendMessage("\u00a7cKh\u00f4ng t\u00ecm th\u1ea5y ng\u01b0\u1eddi ch\u01a1i!");
                return true;
            }
            String tier = args[2].toUpperCase();
            if (!(tier.equals("RARE") || tier.equals("EPIC") || tier.equals("LEGENDARY") || tier.equals("MYTHIC"))) {
                sender.sendMessage("\u00a7c\u0110\u1ed9 hi\u1ebfm m\u1ea3nh v\u1ee1 kh\u00f4ng h\u1ee3p l\u1ec7! (RARE, EPIC, LEGENDARY, MYTHIC)");
                return true;
            }
            int amount = 1;
            if (args.length >= 4) {
                try {
                    amount = Integer.parseInt(args[3]);
                }
                catch (NumberFormatException e) {
                    sender.sendMessage("\u00a7cS\u1ed1 l\u01b0\u1ee3ng kh\u00f4ng h\u1ee3p l\u1ec7!");
                    return true;
                }
            }
            if ((fragment = CustomWeaponEngine.regionBossManager.getFragmentByTier(tier)) != null) {
                fragment.setAmount(amount);
                target.getInventory().addItem(new ItemStack[]{fragment});
                sender.sendMessage("\u00a7a\u0110\u00e3 c\u1ea5p " + amount + " M\u1ea3nh V\u1ee1 " + tier + " cho " + target.getName() + "!");
            } else {
                sender.sendMessage("\u00a7cL\u1ed7i khi t\u1ea1o M\u1ea3nh V\u1ee1!");
            }
            return true;
        }
        if (args.length == 1 && args[0].equalsIgnoreCase("cleanup")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("\u00a7cCh\u1ec9 ng\u01b0\u1eddi ch\u01a1i m\u1edbi c\u00f3 th\u1ec3 d\u00f9ng l\u1ec7nh n\u00e0y!");
                return true;
            }
            Player p = (Player)sender;
            int count = 0;
            for (Entity e : p.getNearbyEntities(20.0, 20.0, 20.0)) {
                if (!(e instanceof ArmorStand)) continue;
                ArmorStand as = (ArmorStand)e;
                String name = as.getCustomName();
                boolean isHolo = false;
                PersistentDataContainer pdc = as.getPersistentDataContainer();
                if (pdc.has(new NamespacedKey((Plugin)CustomWeaponEngine.getPlugin(CustomWeaponEngine.class), "cwe_boss_hologram"), PersistentDataType.STRING) || pdc.has(new NamespacedKey((Plugin)CustomWeaponEngine.getPlugin(CustomWeaponEngine.class), "cwe_meteor_chest"), PersistentDataType.INTEGER)) {
                    isHolo = true;
                }
                if (name != null && (name.contains("BOSS") || name.contains("R\u01b0\u01a1ng") || name.contains("KHI\u00caU CHI\u1ebeN"))) {
                    isHolo = true;
                }
                if (!isHolo) continue;
                as.remove();
                ++count;
            }
            p.sendMessage("\u00a7a\u0110\u00e3 d\u1ecdn d\u1eb9p th\u00e0nh c\u00f4ng " + count + " hologram c\u1ee7a CustomWeaponEngine xung quanh b\u1ea1n!");
            return true;
        }
        sender.sendMessage("\u00a76\u00a7l\u2550\u2550\u2550\u2550\u2550\u2550 BOSS V\u00d9NG \u2550\u2550\u2550\u2550\u2550\u2550");
        sender.sendMessage("\u00a7e/cweboss setspawn <FIRE|ICE|VOID> \u00a77- \u0110\u1eb7t v\u1ecb tr\u00ed \u0111\u1ea5u tr\u01b0\u1eddng");
        sender.sendMessage("\u00a7e/cweboss removespawn <FIRE|ICE|VOID> \u00a77- X\u00f3a v\u1ecb tr\u00ed \u0111\u1ea5u tr\u01b0\u1eddng");
        sender.sendMessage("\u00a7e/cweboss spawn <FIRE|ICE|VOID> \u00a77- \u00c9p Boss xu\u1ea5t hi\u1ec7n l\u1eadp t\u1ee9c");
        sender.sendMessage("\u00a7e/cweboss cleanup \u00a77- D\u1ecdn d\u1eb9p hologram l\u1ed7i l\u00e2n c\u1eadn");
        sender.sendMessage("\u00a7e/cweboss givefragment <player> <RARE|EPIC|LEGENDARY|MYTHIC> [amount] \u00a77- C\u1ea5p m\u1ea3nh v\u1ee1");
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        ArrayList<String> completions;
        block6: {
            block7: {
                String input;
                block8: {
                    block5: {
                        completions = new ArrayList<String>();
                        if (args.length != 1) break block5;
                        String input2 = args[0].toLowerCase();
                        for (String sub : Arrays.asList("setspawn", "removespawn", "spawn", "cleanup", "givefragment")) {
                            if (!sub.startsWith(input2)) continue;
                            completions.add(sub);
                        }
                        break block6;
                    }
                    if (args.length != 2) break block7;
                    input = args[1].toLowerCase();
                    if (!args[0].equalsIgnoreCase("givefragment")) break block8;
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        if (!p.getName().toLowerCase().startsWith(input)) continue;
                        completions.add(p.getName());
                    }
                    break block6;
                }
                if (args[0].equalsIgnoreCase("cleanup")) break block6;
                for (String type : Arrays.asList("FIRE", "ICE", "VOID")) {
                    if (!type.toLowerCase().startsWith(input)) continue;
                    completions.add(type);
                }
                break block6;
            }
            if (args.length == 3 && args[0].equalsIgnoreCase("givefragment")) {
                String input = args[2].toLowerCase();
                for (String tier : Arrays.asList("RARE", "EPIC", "LEGENDARY", "MYTHIC")) {
                    if (!tier.toLowerCase().startsWith(input)) continue;
                    completions.add(tier);
                }
            }
        }
        return completions;
    }
}


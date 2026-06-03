/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.milkbowl.vault.economy.Economy
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.NamespacedKey
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.Sound
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.command.TabCompleter
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.inventory.InventoryClickEvent
 *  org.bukkit.event.inventory.InventoryOpenEvent
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.persistence.PersistentDataContainer
 *  org.bukkit.persistence.PersistentDataType
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.java.JavaPlugin
 */
package org.example.guide;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.example.core.CustomWeaponEngine;

public class GuideSystem
implements Listener,
CommandExecutor,
TabCompleter {
    private final JavaPlugin plugin;
    private static final String GUIDE_TITLE = "\u00a79\u2726 C\u1ea9m Nang H\u1ec7 Th\u1ed1ng \u2726";
    private static final String QUEST_TITLE = "\u00a7a\u2726 Nhi\u1ec7m V\u1ee5 H\u01b0\u1edbng D\u1eabn \u2726";
    public static final List<Quest> QUESTS = new ArrayList<Quest>();

    public GuideSystem(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        Player player = (Player)sender;
        if (command.getName().equalsIgnoreCase("guide")) {
            this.openGuideGUI(player);
            return true;
        }
        if (command.getName().equalsIgnoreCase("quests")) {
            this.openQuestGUI(player);
            return true;
        }
        if (command.getName().equalsIgnoreCase("cwequestadmin")) {
            if (!player.hasPermission("cwe.admin")) {
                player.sendMessage("\u00a7cB\u1ea1n kh\u00f4ng c\u00f3 quy\u1ec1n d\u00f9ng l\u1ec7nh admin n\u00e0y!");
                return true;
            }
            if (args.length > 1 && args[0].equalsIgnoreCase("reset")) {
                Player target = Bukkit.getPlayer((String)args[1]);
                if (target == null) {
                    player.sendMessage("\u00a7cKh\u00f4ng t\u00ecm th\u1ea5y ng\u01b0\u1eddi ch\u01a1i n\u00e0y!");
                    return true;
                }
                this.resetPlayerQuests(target);
                player.sendMessage("\u00a7a\u0110\u00e3 reset to\u00e0n b\u1ed9 nhi\u1ec7m v\u1ee5 c\u1ee7a " + target.getName() + "!");
                target.sendMessage("\u00a7cTo\u00e0n b\u1ed9 nhi\u1ec7m v\u1ee5 h\u01b0\u1edbng d\u1eabn c\u1ee7a b\u1ea1n \u0111\u00e3 \u0111\u01b0\u1ee3c Admin reset!");
            } else {
                player.sendMessage("\u00a7cS\u1eed d\u1ee5ng: /cwequestadmin reset <t\u00ean_ng\u01b0\u1eddi_ch\u01a1i>");
            }
            return true;
        }
        return true;
    }

    private void resetPlayerQuests(Player player) {
        ItemMeta meta = player.getInventory().getItemInMainHand().getItemMeta();
        PersistentDataContainer pdc = player.getPersistentDataContainer();
        for (Quest q : QUESTS) {
            pdc.remove(new NamespacedKey((Plugin)this.plugin, q.pdcKey));
            pdc.remove(new NamespacedKey((Plugin)this.plugin, q.pdcKey + "_claimed"));
        }
    }

    private void openGuideGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, (int)27, (String)GUIDE_TITLE);
        ItemStack glass = this.createItem(Material.GRAY_STAINED_GLASS_PANE, "\u00a78 ", new String[0]);
        for (int i = 0; i < 27; ++i) {
            gui.setItem(i, glass);
        }
        gui.setItem(10, this.createItem(Material.GOLD_INGOT, "\u00a76\u00a7l\u2726 CH\u1ee2 \u0110I\u1ec6N T\u1eec BAZAAR", "\u00a77H\u1ec7 th\u1ed1ng trao \u0111\u1ed5i v\u1eadt ph\u1ea9m ho\u00e0n to\u00e0n t\u1ef1 \u0111\u1ed9ng", "\u00a77s\u1eed d\u1ee5ng thu\u1eadt to\u00e1n AMM bi\u1ebfn \u0111\u1ed9ng theo th\u1ecb tr\u01b0\u1eddng.", "\u00a77Nh\u1ea5p v\u00e0o \u0111\u00e2y \u0111\u1ec3 m\u1edf Bazaar tr\u1ef1c ti\u1ebfp!", "", "\u00a7eL\u1ec7nh nhanh: \u00a7f/bazaar ho\u1eb7c /bz"));
        gui.setItem(11, this.createItem(Material.CHEST, "\u00a7e\u00a7l\u2726 NG\u00c2N H\u00c0NG C\u00c1 NH\u00c2N", "\u00a77G\u1eedi ti\u1ec1n ti\u1ebft ki\u1ec7m \u0111\u1ec3 b\u1ea3o v\u1ec7 t\u00e0i s\u1ea3n", "\u00a77v\u00e0 nh\u1eadn l\u00e3i \u0111\u1ecbnh k\u1ef3 1%/ph\u00fat khi online.", "\u00a77Nh\u1ea5p v\u00e0o \u0111\u00e2y \u0111\u1ec3 m\u1edf Ng\u00e2n h\u00e0ng tr\u1ef1c ti\u1ebfp!", "", "\u00a7eL\u1ec7nh nhanh: \u00a7f/bank"));
        gui.setItem(12, this.createItem(Material.ANVIL, "\u00a7d\u00a7l\u2726 H\u1ec6 TH\u1ed0NG TR\u00d9NG \u0110\u00daC (REFORGE)", "\u00a77Roll ng\u1eabu nhi\u00ean c\u00e1c ti\u1ec1n t\u1ed1 thu\u1ed9c t\u00ednh c\u1ef1c m\u1ea1nh", "\u00a77cho v\u0169 kh\u00ed v\u00e0 gi\u00e1p c\u1ee7a b\u1ea1n.", "\u00a77Nh\u1ea5p v\u00e0o \u0111\u00e2y \u0111\u1ec3 m\u1edf Tr\u00f9ng \u0110\u00fac tr\u1ef1c ti\u1ebfp!", "", "\u00a7eL\u1ec7nh nhanh: \u00a7f/reforge"));
        gui.setItem(13, this.createItem(Material.CLOCK, "\u00a7c\u00a7l\u2726 L\u1ed8 TR\u00ccNH PH\u00c1T TRI\u1ec2N & PH\u1ea8M CH\u1ea4T", "\u00a77L\u1ed9 tr\u00ecnh \u0111\u1ed9 hi\u1ebfm t\u1eeb th\u1ea5p \u0111\u1ebfn cao:", "\u00a77Common \u00a77\u27a1 \u00a7aUncommon \u00a77\u27a1 \u00a79Rare \u00a77\u27a1 \u00a75Epic \u00a77\u27a1 \u00a76Legendary \u00a77\u27a1 \u00a7dMythic", "\u00a77H\u00e3y d\u00f9ng m\u00e1y Th\u1ea9m \u0111\u1ecbnh \u0111\u1ec3 \u0111\u00e1nh gi\u00e1 \u0111\u1ed9 hi\u1ebfm", "\u00a77v\u00e0 n\u00e2ng c\u1ea5p c\u00e1c ch\u1ec9 s\u1ed1 g\u1ed1c c\u1ee7a trang b\u1ecb!", "", "\u00a7eL\u1ec7nh nhanh: \u00a7f/appraisal"));
        gui.setItem(14, this.createItem(Material.ENCHANTED_BOOK, "\u00a7b\u00a7l\u2726 H\u1ec6 TH\u1ed0NG C\u01af\u1edcNG H\u00d3A (ENCHANTS)", "\u00a77C\u01b0\u1eddng h\u00f3a b\u00f9a ch\u00fa t\u00f9y bi\u1ebfn l\u00ean t\u1edbi h\u01a1n 50 lo\u1ea1i.", "\u00a77C\u00f3 c\u00e1c b\u00f9a ch\u00fa Ultimate c\u1ef1c \u0111\u1ec9nh mang l\u1ea1i hi\u1ec7u", "\u00a77qu\u1ea3 \u0111\u1ed9t bi\u1ebfn ho\u00e0n to\u00e0n.", "\u00a77\u0110\u1eb7t v\u00e0 click chu\u1ed9t ph\u1ea3i v\u00e0o B\u00e0n Ph\u00f9 Ph\u00e9p", "\u00a77\u0111\u1ec3 m\u1edf giao di\u1ec7n C\u01b0\u1eddng H\u00f3a Custom!", "", "\u00a7eL\u1ed1i ch\u01a1i: \u00a7f\u0110\u1eb7t & Click B\u00e0n Ph\u00f9 Ph\u00e9p"));
        gui.setItem(15, this.createItem(Material.BOOK, "\u00a7a\u00a7l\u2726 S\u00c1CH K\u1ef8 N\u0102NG (SKILL BOOKS)", "\u00a77S\u00e1ch k\u1ef9 n\u0103ng cung c\u1ea5p Full Set Bonus cho gi\u00e1p", "\u00a77ho\u1eb7c Active Ability cho v\u0169 kh\u00ed c\u1ee7a b\u1ea1n.", "\u00a77Nh\u1ea5p v\u00e0o \u0111\u00e2y \u0111\u1ec3 m\u1edf giao di\u1ec7n \u00c9p K\u1ef9 N\u0103ng!", "", "\u00a7eL\u1ec7nh nhanh: \u00a7f/skillbook"));
        gui.setItem(16, this.createItem(Material.MAGMA_CREAM, "\u00a7c\u00a7l\u2726 S\u1ef0 KI\u1ec6N THI\u00caN TH\u1ea0CH & BOSS", "\u00a77Thi\u00ean th\u1ea1ch r\u01a1i ng\u1eabu nhi\u00ean xu\u1ed1ng th\u1ebf gi\u1edbi.", "\u00a77Khai th\u00e1c thi\u00ean th\u1ea1ch \u0111\u1ec3 tri\u1ec7u h\u1ed3i H\u1ed9 v\u1ec7 qu\u1eb7ng", "\u00a77ho\u1eb7c \u0111\u00e1nh b\u1ea1i Boss \u0111\u1ec3 nh\u1eadn m\u1ea3nh v\u1ee1 Reforge!", "", "\u00a7eL\u1ec7nh nhanh: \u00a7fS\u1ef1 ki\u1ec7n di\u1ec5n ra t\u1ef1 \u0111\u1ed9ng."));
        gui.setItem(22, this.createItem(Material.COMPASS, "\u00a7a\u00a7l\u2726 NHI\u1ec6M V\u1ee4 H\u01af\u1edaNG D\u1eaaN T\u00c2N TH\u1ee6 \u2726", "\u00a77L\u00e0m quen t\u1eebng t\u00ednh n\u0103ng c\u1ee7a h\u1ec7 th\u1ed1ng", "\u00a77\u0111\u1ec3 nh\u1eadn \u0111\u01b0\u1ee3c v\u00f4 v\u00e0n Coins th\u01b0\u1edfng mi\u1ec5n ph\u00ed!", "", "\u00a7eClick \u0111\u1ec3 m\u1edf B\u1ea3ng Nhi\u1ec7m V\u1ee5!"));
        player.openInventory(gui);
    }

    private void openQuestGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, (int)27, (String)QUEST_TITLE);
        ItemStack glass = this.createItem(Material.GRAY_STAINED_GLASS_PANE, "\u00a78 ", new String[0]);
        for (int i = 0; i < 27; ++i) {
            gui.setItem(i, glass);
        }
        PersistentDataContainer pdc = player.getPersistentDataContainer();
        int[] slots = new int[]{10, 11, 12, 13, 14, 15};
        for (int i = 0; i < QUESTS.size(); ++i) {
            Quest q = QUESTS.get(i);
            int slot = slots[i];
            boolean completed = pdc.has(new NamespacedKey((Plugin)this.plugin, q.pdcKey), PersistentDataType.INTEGER);
            boolean claimed = pdc.has(new NamespacedKey((Plugin)this.plugin, q.pdcKey + "_claimed"), PersistentDataType.INTEGER);
            if (claimed) {
                gui.setItem(slot, this.createItem(Material.WRITTEN_BOOK, "\u00a77[\u2714] " + q.name + " (Ho\u00e0n t\u1ea5t)", "\u00a78Nhi\u1ec7m v\u1ee5 t\u00e2n th\u1ee7 h\u01b0\u1edbng d\u1eabn.", "\u00a77Y\u00eau c\u1ea7u: " + q.desc, "", "\u00a7aTr\u1ea1ng th\u00e1i: \u0110\u00e3 ho\u00e0n t\u1ea5t & nh\u1eadn th\u01b0\u1edfng!", "\u00a77Ph\u1ea7n th\u01b0\u1edfng: \u00a7e" + String.format("%,.0f Coins", q.reward)));
                continue;
            }
            if (completed) {
                gui.setItem(slot, this.createItem(Material.LIME_DYE, "\u00a7a\u00a7l[\u2605] " + q.name + " (Nh\u1eadn Th\u01b0\u1edfng)", "\u00a77Y\u00eau c\u1ea7u: " + q.desc, "", "\u00a7aTr\u1ea1ng th\u00e1i: \u0110\u00c3 HO\u00c0N TH\u00c0NH!", "\u00a77Ph\u1ea7n th\u01b0\u1edfng: \u00a7e" + String.format("%,.0f Coins", q.reward), "", "\u00a76Click v\u00e0o \u0111\u00e2y \u0111\u1ec3 nh\u1eadn th\u01b0\u1edfng Coins!"));
                continue;
            }
            gui.setItem(slot, this.createItem(Material.GRAY_DYE, "\u00a7c[\u2718] " + q.name + " (Ch\u01b0a xong)", "\u00a77Y\u00eau c\u1ea7u: " + q.desc, "", "\u00a7cTr\u1ea1ng th\u00e1i: Ch\u01b0a th\u1ef1c hi\u1ec7n.", "\u00a77Ph\u1ea7n th\u01b0\u1edfng: \u00a7e" + String.format("%,.0f Coins", q.reward), "", "\u00a7eH\u00e3y click \u0111\u1ec3 c\u1ea9m nang m\u1edf t\u00ednh n\u0103ng li\u00ean quan!"));
        }
        gui.setItem(22, this.createItem(Material.ARROW, "\u00a77\u00ab Quay l\u1ea1i C\u1ea9m Nang", new String[0]));
        player.openInventory(gui);
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        if (!(event.getPlayer() instanceof Player)) {
            return;
        }
        Player player = (Player)event.getPlayer();
        String title = ChatColor.stripColor((String)event.getView().getTitle());
        if (title.contains("Bank Account") || title.contains("Deposit Coins") || title.contains("Withdraw Coins")) {
            this.completeQuest(player, "bank");
        } else if (title.contains("Bazaar")) {
            this.completeQuest(player, "bazaar");
        } else if (title.contains("Reforge")) {
            this.completeQuest(player, "reforge");
        } else if (title.contains("Enchant") || title.contains("Anvil") || title.contains("C\u01b0\u1eddng H\u00f3a")) {
            this.completeQuest(player, "enchant");
        } else if (title.contains("Skill Book")) {
            this.completeQuest(player, "skillbook");
        } else if (title.contains("Appraiser")) {
            this.completeQuest(player, "appraiser");
        }
    }

    private void completeQuest(Player player, String questId) {
        NamespacedKey key;
        Quest q = null;
        for (Quest quest : QUESTS) {
            if (!quest.id.equals(questId)) continue;
            q = quest;
            break;
        }
        if (q == null) {
            return;
        }
        PersistentDataContainer pdc = player.getPersistentDataContainer();
        if (!pdc.has(key = new NamespacedKey((Plugin)this.plugin, q.pdcKey), PersistentDataType.INTEGER)) {
            pdc.set(key, PersistentDataType.INTEGER, 1);
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.5f);
            player.sendTitle("\u00a7a\u00a7lNHI\u1ec6M V\u1ee4 HO\u00c0N TH\u00c0NH!", "\u00a7fG\u00f5 \u00a7e/quests\u00a7f \u0111\u1ec3 nh\u1eadn Coins th\u01b0\u1edfng!", 10, 40, 10);
            player.sendMessage("\u00a7a\u00a7l[\u2726] Ch\u00fac m\u1eebng! B\u1ea1n \u0111\u00e3 ho\u00e0n th\u00e0nh nhi\u1ec7m v\u1ee5: \u00a7e" + q.name);
        }
    }

    @EventHandler
    public void onGuiClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }
        Player player = (Player)event.getWhoClicked();
        String title = event.getView().getTitle();
        if (title.equals(GUIDE_TITLE)) {
            event.setCancelled(true);
            if (event.getClickedInventory() != event.getView().getTopInventory()) {
                return;
            }
            int slot = event.getSlot();
            player.closeInventory();
            Bukkit.getScheduler().runTask((Plugin)this.plugin, () -> {
                if (slot == 10) {
                    player.performCommand("bazaar");
                } else if (slot == 11) {
                    player.performCommand("bank");
                } else if (slot == 12) {
                    player.performCommand("reforge");
                } else if (slot == 13) {
                    player.performCommand("appraisal");
                } else if (slot == 14) {
                    if (player.hasPermission("cwe.admin")) {
                        player.performCommand("adminec");
                    } else {
                        player.sendMessage("\u00a7b[ C\u01b0\u1eddng H\u00f3a ] \u00a7fH\u00e3y ch\u1ebf t\u1ea1o, \u0111\u1eb7t v\u00e0 \u00a7eclick chu\u1ed9t ph\u1ea3i v\u00e0o B\u00e0n Ph\u00f9 Ph\u00e9p (Enchanting Table)\u00a7f trong th\u1ebf gi\u1edbi sinh t\u1ed3n \u0111\u1ec3 m\u1edf giao di\u1ec7n C\u01b0\u1eddng H\u00f3a \u0111\u1eb7c quy\u1ec1n!");
                        player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
                    }
                } else if (slot == 15) {
                    player.performCommand("skillbook");
                } else if (slot == 16) {
                    player.sendMessage("\u00a7e[ Thi\u00ean Th\u1ea1ch ] \u00a7fS\u1ef1 ki\u1ec7n di\u1ec5n ra t\u1ef1 \u0111\u1ed9ng m\u1ed7i gi\u1edd. Khi c\u00f3 th\u00f4ng b\u00e1o tr\u00ean chat, h\u00e3y ch\u1ea1y nhanh t\u1edbi t\u1ecda \u0111\u1ed9 \u0111\u01b0\u1ee3c th\u00f4ng b\u00e1o \u0111\u1ec3 tham gia!");
                } else if (slot == 22) {
                    this.openQuestGUI(player);
                }
            });
            return;
        }
        if (title.equals(QUEST_TITLE)) {
            event.setCancelled(true);
            if (event.getClickedInventory() != event.getView().getTopInventory()) {
                return;
            }
            int slot = event.getSlot();
            if (slot == 22) {
                player.closeInventory();
                Bukkit.getScheduler().runTask((Plugin)this.plugin, () -> this.openGuideGUI(player));
                return;
            }
            int[] slots = new int[]{10, 11, 12, 13, 14, 15};
            int questIdx = -1;
            for (int i = 0; i < slots.length; ++i) {
                if (slots[i] != slot) continue;
                questIdx = i;
                break;
            }
            if (questIdx == -1) {
                return;
            }
            Quest q = QUESTS.get(questIdx);
            PersistentDataContainer pdc = player.getPersistentDataContainer();
            NamespacedKey key = new NamespacedKey((Plugin)this.plugin, q.pdcKey);
            NamespacedKey claimKey = new NamespacedKey((Plugin)this.plugin, q.pdcKey + "_claimed");
            boolean completed = pdc.has(key, PersistentDataType.INTEGER);
            boolean claimed = pdc.has(claimKey, PersistentDataType.INTEGER);
            if (claimed) {
                player.sendMessage("\u00a7cB\u1ea1n \u0111\u00e3 nh\u1eadn th\u01b0\u1edfng nhi\u1ec7m v\u1ee5 n\u00e0y r\u1ed3i!");
            } else if (completed) {
                pdc.set(claimKey, PersistentDataType.INTEGER, 1);
                Economy econ = CustomWeaponEngine.getEconomy();
                if (econ != null) {
                    econ.depositPlayer((OfflinePlayer)player, q.reward);
                }
                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
                player.sendMessage("\u00a7a\u00a7l[\u2714] Nh\u1eadn th\u01b0\u1edfng th\u00e0nh c\u00f4ng! B\u1ea1n nh\u1eadn \u0111\u01b0\u1ee3c \u00a7e" + String.format("%,.0f Coins", q.reward) + "\u00a7a t\u1eeb nhi\u1ec7m v\u1ee5: \u00a7f" + q.name);
                this.openQuestGUI(player);
            } else {
                player.closeInventory();
                Bukkit.getScheduler().runTask((Plugin)this.plugin, () -> {
                    if (q.id.equals("bank")) {
                        player.performCommand("bank");
                    } else if (q.id.equals("bazaar")) {
                        player.performCommand("bazaar");
                    } else if (q.id.equals("reforge")) {
                        player.performCommand("reforge");
                    } else if (q.id.equals("enchant")) {
                        if (player.hasPermission("cwe.admin")) {
                            player.performCommand("adminec");
                        } else {
                            player.sendMessage("\u00a7b[ C\u01b0\u1eddng H\u00f3a ] \u00a7fH\u00e3y ch\u1ebf t\u1ea1o, \u0111\u1eb7t v\u00e0 \u00a7eclick chu\u1ed9t ph\u1ea3i v\u00e0o B\u00e0n Ph\u00f9 Ph\u00e9p (Enchanting Table)\u00a7f ho\u1eb7c \u00a7eC\u00e1i \u0110e (Anvil)\u00a7f trong th\u1ebf gi\u1edbi \u0111\u1ec3 ti\u1ebfn h\u00e0nh C\u01b0\u1eddng h\u00f3a / Gh\u00e9p d\u00f2ng trang b\u1ecb!");
                            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
                        }
                    } else if (q.id.equals("skillbook")) {
                        player.performCommand("skillbook");
                    } else if (q.id.equals("appraiser")) {
                        player.performCommand("appraisal");
                    }
                });
            }
        }
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return new ArrayList<String>();
    }

    private ItemStack createItem(Material mat, String name, String ... lore) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            if (lore.length > 0) {
                meta.setLore(Arrays.asList(lore));
            }
            item.setItemMeta(meta);
        }
        return item;
    }

    static {
        QUESTS.add(new Quest("bank", "G\u1eedi Ti\u1ec1n Ti\u1ebft Ki\u1ec7m", "H\u00e3y m\u1edf giao di\u1ec7n Ng\u00e2n H\u00e0ng c\u00e1 nh\u00e2n (/bank).", 5000.0, "cwe_q_bank"));
        QUESTS.add(new Quest("bazaar", "Kh\u00e1m Ph\u00e1 Th\u1ecb Tr\u01b0\u1eddng", "H\u00e3y m\u1edf giao di\u1ec7n Ch\u1ee3 \u0110i\u1ec7n T\u1eed Bazaar (/bazaar).", 5000.0, "cwe_q_bazaar"));
        QUESTS.add(new Quest("reforge", "Th\u1eed S\u1ee9c Tr\u00f9ng \u0110\u00fac", "H\u00e3y m\u1edf giao di\u1ec7n Tr\u00f9ng \u0110\u00fac Trang B\u1ecb (/reforge).", 5000.0, "cwe_q_reforge"));
        QUESTS.add(new Quest("enchant", "H\u1ecdc H\u1ecfi C\u01b0\u1eddng H\u00f3a", "H\u00e3y m\u1edf giao di\u1ec7n C\u01b0\u1eddng H\u00f3a ho\u1eb7c \u0110e gh\u00e9p \u0111\u1ed3.", 5000.0, "cwe_q_enchant"));
        QUESTS.add(new Quest("skillbook", "Kh\u00e1m Ph\u00e1 K\u1ef9 N\u0103ng", "H\u00e3y m\u1edf giao di\u1ec7n \u00c9p S\u00e1ch K\u1ef9 N\u0103ng (/skillbook).", 5000.0, "cwe_q_skillbook"));
        QUESTS.add(new Quest("appraiser", "Th\u1eed T\u00e0i Th\u1ea9m \u0110\u1ecbnh", "H\u00e3y m\u1edf giao di\u1ec7n M\u00e1y Gi\u00e1m \u0110\u1ecbnh \u0111\u1ed3 (/appraisal).", 10000.0, "cwe_q_appraiser"));
    }

    public static class Quest {
        public String id;
        public String name;
        public String desc;
        public double reward;
        public String pdcKey;

        public Quest(String id, String name, String desc, double reward, String pdcKey) {
            this.id = id;
            this.name = name;
            this.desc = desc;
            this.reward = reward;
            this.pdcKey = pdcKey;
        }
    }
}


package org.example.core;

import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
import org.example.guide.GuideSystem;
import org.example.skillbook.SkillBookSystem;
import org.example.weapon.ArrowDamageListener;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.entity.Entity;
import org.bukkit.entity.WitherSkull;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.NamespacedKey;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;
import java.io.IOException;

import org.jetbrains.annotations.NotNull;
import org.example.enchant.EnchantManager;

public final class CustomWeaponEngine extends JavaPlugin implements CommandExecutor {

    private static CustomWeaponEngine instance;
    private static Economy econ = null;
    private static EnchantManager enchantManager;
    private static org.example.system.TradeManager tradeManager;
    private static org.example.system.LibraryGUI libraryGUI;
    
    public static HeavenlySwordListener weaponEngine;
    public static ShadowAssassinListener assassinEngine;
    public static BerserkListener berserkEngine;
    public static MobDeathListener mobDeathEngine;
    public static org.example.system.RegionBossManager regionBossManager;
    public static org.example.system.MeteorBossManager meteorBossManager;
    
    private org.example.system.HologramManager hologramManager;
    
    private File libraryFile;
    private FileConfiguration libraryConfig;

    public FileConfiguration getLibraryConfig() {
        return libraryConfig;
    }

    public void saveLibraryConfig() {
        try {
            libraryConfig.save(libraryFile);
        } catch (IOException e) {
            getLogger().warning("Could not save library.yml!");
        }
    }

    public static net.milkbowl.vault.economy.Economy getEconomy() { return econ; }
    public static org.example.system.MeteorBossManager getMeteorBossManager() { return meteorBossManager; }
    public org.example.system.HologramManager getHologramManager() { return hologramManager; }

    @Override
    public void onEnable() {
        tradeManager = new org.example.system.TradeManager();
        libraryGUI = new org.example.system.LibraryGUI(this);
        getServer().getPluginManager().registerEvents(libraryGUI, this);

        instance = this;
        saveDefaultConfig();

        // Load library.yml
        libraryFile = new File(getDataFolder(), "library.yml");
        if (!libraryFile.exists()) {
            libraryFile.getParentFile().mkdirs();
            saveResource("library.yml", false);
        }
        libraryConfig = YamlConfiguration.loadConfiguration(libraryFile);

        // 🟩 Đăng ký đồ vào thư viện mỗi lần khởi động để đảm bảo đủ item
        org.example.weapon.legendary.LegendarySwords.registerSwords(this);
        org.example.weapon.legendary.LegendaryArmor.registerArmors(this);
        org.example.weapon.legendary.RareEpicItems.registerItems(this);
        saveLibraryConfig();

        if (!setupEconomy()) {
            getLogger().severe("🟥 KHÔNG TÌM THẤY VÍ TIỀN VAULT! Chợ Bazaar sẽ bị đóng băng.");
        }

        enchantManager = new EnchantManager(this);
        org.example.stats.ItemBuilder.init(this);

        // 🟩 Đăng ký công thức chế tạo Mending đặc quyền
        NamespacedKey recipeKey = new NamespacedKey(this, "custom_mending_book");
        ItemStack mendingBook = new ItemStack(org.bukkit.Material.ENCHANTED_BOOK);
        org.bukkit.inventory.meta.EnchantmentStorageMeta mMeta = (org.bukkit.inventory.meta.EnchantmentStorageMeta) mendingBook.getItemMeta();
        if (mMeta != null) {
            mMeta.setDisplayName("§d§lMending I");
            mMeta.addStoredEnchant(org.bukkit.enchantments.Enchantment.getByKey(NamespacedKey.minecraft("mending")), 1, true);
            mMeta.setLore(java.util.Arrays.asList("§d§lĐẶC QUYỀN CHẾ TẠO"));
            mMeta.getPersistentDataContainer().set(new NamespacedKey(this, "is_custom_mending"), org.bukkit.persistence.PersistentDataType.INTEGER, 1);
            mendingBook.setItemMeta(mMeta);
        }
        org.bukkit.inventory.ShapedRecipe recipe = new org.bukkit.inventory.ShapedRecipe(recipeKey, mendingBook);
        recipe.shape(" I ", "IBI", " I ");
        recipe.setIngredient('I', org.bukkit.Material.IRON_INGOT);
        recipe.setIngredient('B', org.bukkit.Material.BOOK);
        try { getServer().removeRecipe(recipeKey); } catch (Exception ignored) {}
        try { getServer().addRecipe(recipe); } catch (Exception ignored) {}

        // 🟩 Đăng ký công thức vũ khí Custom
        new org.example.system.CustomRecipeManager(this).registerRecipes();

        getServer().getPluginManager().registerEvents(new org.example.system.ReplenishListener(this), this);
        org.example.enchant.EnchantGUI enchantGUI = new org.example.enchant.EnchantGUI(this, enchantManager);
        getServer().getPluginManager().registerEvents(enchantGUI, this);
        getServer().getPluginManager().registerEvents(new org.example.enchant.CombatEnchantListener(this), this);
        getServer().getPluginManager().registerEvents(new org.example.enchant.AnvilGUI(this, enchantManager), this);
        getServer().getPluginManager().registerEvents(new org.example.armor.ArmorEnchantListener(this), this);
        getServer().getPluginManager().registerEvents(new org.example.enchant.UltimateSystemListener(this, enchantManager), this);

        org.example.bazaar.BazaarGUI bazaarEngine = new org.example.bazaar.BazaarGUI(this, econ);
        getServer().getPluginManager().registerEvents(bazaarEngine, this);
        org.example.system.RecipeGUI recipeGui = new org.example.system.RecipeGUI(this);
        getCommand("recipes").setExecutor(recipeGui);
        getServer().getPluginManager().registerEvents(recipeGui, this);
        
        getServer().getPluginManager().registerEvents(new org.example.bazaar.BazaarRecipeListener(this), this);
        getServer().getPluginManager().registerEvents(new org.example.bazaar.BazaarMobDropListener(this), this);

        // 🟩 Đăng ký OreVeinManager
        org.example.system.OreVeinManager oreVeinManager = new org.example.system.OreVeinManager(this);
        getServer().getServicesManager().register(org.example.system.OreVeinManager.class, oreVeinManager, this, org.bukkit.plugin.ServicePriority.Normal);
        if (getCommand("orevein") != null) {
            getCommand("orevein").setExecutor(oreVeinManager);
            getCommand("orevein").setTabCompleter(oreVeinManager);
        }
    
        if (getCommand("bazaar") != null) {
            getCommand("bazaar").setExecutor(bazaarEngine);
        }

        // 🟩 Đăng ký lệnh /adminec -> EnchantGUI CommandExecutor
        if (getCommand("adminec") != null) {
            getCommand("adminec").setExecutor(enchantGUI);
        }

        // 🟩 Đăng ký hệ thống Item Custom Stats Engine
        org.example.stats.ItemStatsGUI itemStatsGUI = new org.example.stats.ItemStatsGUI(this);
        getServer().getPluginManager().registerEvents(itemStatsGUI, this);
        if (getCommand("cwestats") != null) {
            getCommand("cwestats").setExecutor(itemStatsGUI);
        }
        getServer().getPluginManager().registerEvents(new org.example.stats.ItemStatsListener(this), this);
        getServer().getPluginManager().registerEvents(new org.example.stats.VanillaItemUpdater(this), this);
        getServer().getPluginManager().registerEvents(new org.example.stats.CritTagger(this), this);
        getServer().getPluginManager().registerEvents(new org.example.stats.AppraiserGUI(this), this);
        
        regionBossManager = new org.example.system.RegionBossManager(this);
        getServer().getPluginManager().registerEvents(regionBossManager, this);
        
        // 🟩 Đăng ký lệnh /meteorite với Tab-Complete đầy đủ
                if (getCommand("cweboss") != null) {
            org.example.system.RegionBossCommand bossCmd = new org.example.system.RegionBossCommand();
            getCommand("cweboss").setExecutor(bossCmd);
            getCommand("cweboss").setTabCompleter(bossCmd);
        }
        meteorBossManager = new org.example.system.MeteorBossManager(this, regionBossManager);
        getServer().getPluginManager().registerEvents(meteorBossManager, this);

        // 🟩 Đăng ký lệnh /cweie (Custom Item Editor)
        if (getCommand("itemeditor") != null) {
            getCommand("itemeditor").setExecutor(new org.example.system.ItemEditorCommand());
        }

        // --- HỆ THỐNG MỚI (PHỤC HỒI TỪ QUÁN NET) ---
        SkillBookSystem skillBookSystem = new SkillBookSystem(this);
        getServer().getPluginManager().registerEvents(skillBookSystem, this);
        if (getCommand("skillbook") != null) {
            getCommand("skillbook").setExecutor(skillBookSystem);
        }

        GuideSystem guideSystem = new GuideSystem(this);
        getServer().getPluginManager().registerEvents(guideSystem, this);
        if (getCommand("guide") != null) {
            getCommand("guide").setExecutor(guideSystem);
        }
        if (getCommand("quests") != null) {
            getCommand("quests").setExecutor(guideSystem);
        }
        if (getCommand("cwequestadmin") != null) {
            getCommand("cwequestadmin").setExecutor(guideSystem);
        }
        
        getLogger().info("§a CustomWeaponEngine Engine v4.0 (Bazaar AMM & Ore Guardians Updated) hoat dong!");

        // 🟩 Đăng ký hệ thống Gacha Reforge
        ReforgeSystem reforgeSystem = new ReforgeSystem(this, econ);
        getServer().getPluginManager().registerEvents(reforgeSystem, this);
        if (getCommand("reforge") != null) {
            getCommand("reforge").setExecutor(reforgeSystem);
        }
        if (getCommand("delref") != null) {
            getCommand("delref").setExecutor(reforgeSystem);
        }

        assassinEngine = new ShadowAssassinListener();
        getServer().getPluginManager().registerEvents(assassinEngine, this);
    
        if (getCommand("cst") != null) {
            getCommand("cst").setExecutor(this);
            // Tự động dọn rác WitherSkull bị lỗi (NaN) bị kẹt trong chunk
            Bukkit.getPluginManager().registerEvents(new Listener() {
                @EventHandler
                public void onChunkLoad(ChunkLoadEvent event) {
                    for (Entity e : event.getChunk().getEntities()) {
                        if (e instanceof WitherSkull) {
                            e.remove();
                        }
                    }
                }

            }, this);
            getCommand("cst").setTabCompleter((sender, command, alias, args) -> {
                if (args.length == 1) {
                    java.util.List<String> list = new java.util.ArrayList<>();
                    if ("reload".startsWith(args[0].toLowerCase())) list.add("reload");
                    if ("help".startsWith(args[0].toLowerCase())) list.add("help");
                    return list;
                }
                return new java.util.ArrayList<>();
            });
        }

        weaponEngine = new HeavenlySwordListener();
        getServer().getPluginManager().registerEvents(weaponEngine, this);
        
        getServer().getPluginManager().registerEvents(new org.example.weapon.RunaanBowListener(this), this);
        getServer().getPluginManager().registerEvents(new ArrowDamageListener(this), this);

        getServer().getPluginManager().registerEvents(new org.example.weapon.BerserkListener(), this);
        getServer().getPluginManager().registerEvents(new org.example.weapon.legendary.LividDaggerListener(this), this);
        getServer().getPluginManager().registerEvents(new org.example.weapon.legendary.EmeraldBladeListener(this), this);
        getServer().getPluginManager().registerEvents(new org.example.weapon.legendary.ShadowFuryListener(this), this);
        getServer().getPluginManager().registerEvents(new org.example.weapon.legendary.GiantsSwordListener(this), this);
        getServer().getPluginManager().registerEvents(new org.example.weapon.legendary.WitherBladeListener(this), this);

        berserkEngine = new BerserkListener();
        getServer().getPluginManager().registerEvents(berserkEngine, this);

        mobDeathEngine = new MobDeathListener();
        getServer().getPluginManager().registerEvents(mobDeathEngine, this);
        
        // 🟩 Đăng ký Cosmic Void & Astral Shepherd
        getServer().getPluginManager().registerEvents(new org.example.armor.CosmicVoidListener(this), this);
        getServer().getPluginManager().registerEvents(new org.example.weapon.AstralShepherdListener(this), this);
        
        // 🟩 Đăng ký Mana Regen Task
        new org.example.system.ManaRegenTask(this).start();
        
        // 🟩 Đăng ký Health Regen Task
        new org.example.system.HealthRegenTask(this).start();

        // 🟩 Đăng ký CWEBanking
        org.example.bank.BankDataManager.getInstance(this);
        org.example.bank.BankGui bankGui = new org.example.bank.BankGui(this, econ);
        getServer().getPluginManager().registerEvents(bankGui, this);
        if (getCommand("bank") != null) {
            getCommand("bank").setExecutor(new org.example.bank.BankCommand(bankGui));
        }
        new org.example.bank.BankInterestTask(this).runTaskTimer(this, 1200L, 1200L); // 1200 ticks = 1 minute interval

        // 🟩 Đăng ký ChangelogManager
        org.example.system.ChangelogManager changelogManager = new org.example.system.ChangelogManager(this);
        getServer().getPluginManager().registerEvents(changelogManager, this);
        if (getCommand("cweupdate") != null) {
            getCommand("cweupdate").setExecutor(changelogManager);
        }

        if (getCommand("cweeco") != null) {
            getCommand("cweeco").setExecutor(new org.example.system.EconomyAdminCommand());
        }
        
        
        if (getCommand("cwe") != null) {
            getCommand("cwe").setExecutor(new org.example.system.CWELibraryCommand(this));
        }

        if (getCommand("cwegive") != null) {
            org.example.system.CWEGiveCommand giveCmd = new org.example.system.CWEGiveCommand(this);
            getCommand("cwegive").setExecutor(giveCmd);
            getCommand("cwegive").setTabCompleter(giveCmd);
        }
    
        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new CooldownExpansion(weaponEngine).register();
        }
        
        // 🟩 Đăng ký lệnh /setrank (LuckPerms prefix)
        if (getCommand("setrank") != null) {
            org.example.system.SetRankCommand setRankCmd = new org.example.system.SetRankCommand();
            getCommand("setrank").setExecutor(setRankCmd);
            getCommand("setrank").setTabCompleter(setRankCmd);
        }

        if (getCommand("cwebook") != null) {
            org.example.system.CustomBookCommand bookCmd = new org.example.system.CustomBookCommand(this);
            getCommand("cwebook").setExecutor(bookCmd);
            getCommand("cwebook").setTabCompleter(bookCmd);
        }

        // --- Register Other Listeners ---
        getServer().getPluginManager().registerEvents(new org.example.enchant.EnchantTableListener(this), this);


        getServer().getPluginManager().registerEvents(new org.example.system.UpdateNotifier(this), this);
        
        // 🟩 Đăng ký Chat Listener
        getServer().getPluginManager().registerEvents(new org.example.system.ChatListener(this), this);
        
        // 🟩 Đăng ký Khám Phá Rương
        getServer().getPluginManager().registerEvents(new org.example.system.ExplorationChest(this), this);

        // Đăng ký các Legendary Listeners
        getServer().getPluginManager().registerEvents(new org.example.weapon.legendary.AOTDListener(this), this);
        getServer().getPluginManager().registerEvents(new org.example.weapon.legendary.AOTEListener(this), this);
        getServer().getPluginManager().registerEvents(new org.example.weapon.legendary.EpicWeaponListeners(this), this);
        getServer().getPluginManager().registerEvents(new org.example.weapon.legendary.ArmorSetListener(this), this);
        getServer().getPluginManager().registerEvents(new org.example.weapon.legendary.SlayerDungeonListener(this), this);
        
        // Bật các Task ngầm cho Set Giáp
        new org.example.weapon.legendary.DragonTask(this).runTaskTimer(this, 0L, 20L); // 1 giay
        
        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new org.example.system.CWEExpansion(this).register();
            getLogger().info("PlaceholderAPI tim thay! Da dang ky %cwe_actionbar_mana%.");
        }
        
        // Khoi tao Hologram Manager
        hologramManager = new org.example.system.HologramManager(this);
        Bukkit.getScheduler().runTaskLater(this, () -> {
            hologramManager.updateHolograms();
        }, 40L); // Delay 2s de cho DecentHolograms hoat dong truoc
        
        getLogger().info("🟩 CustomWeaponEngine Engine v4.0 (Bazaar AMM & Ore Guardians Updated) hoat dong!");
    }


    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) return false;
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) return false;
        econ = rsp.getProvider();
        return econ != null;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!command.getName().equalsIgnoreCase("cst")) return false;

        if (!sender.hasPermission("cst.admin")) {
            sender.sendMessage("§cYou don't have permission!");
            return true;
        }

        if (args.length > 0 && args[0].equalsIgnoreCase("reload")) {
            reloadConfig();
            if (org.example.bank.BankDataManager.getInstance() != null) {
                org.example.bank.BankDataManager.getInstance().reload();
            }
            if (weaponEngine != null) weaponEngine.clearCooldowns();
            if (assassinEngine != null) assassinEngine.clearCooldowns(); 
            if (berserkEngine != null) berserkEngine.clearCache(); 
            if (hologramManager != null) hologramManager.updateHolograms();
            sender.sendMessage("§a[CustomWeaponEngine] Configuration reloaded!");
            return true;
        }

        // Mặc định hoặc /cst help
        sender.sendMessage("§e====================================================");
        sender.sendMessage("§6§l    ✦ CUSTOM WEAPON ENGINE - ADMIN COMMANDS ✦");
        sender.sendMessage("§e====================================================");
        sender.sendMessage("§a/cst reload §7- Tải lại toàn bộ cấu hình hệ thống.");
        sender.sendMessage("§a/cst help §7- Hiển thị danh sách các lệnh admin.");
        sender.sendMessage("§a/cwe save <item_id> §7- Lưu NBT vật phẩm cầm tay vào library.yml.");
        sender.sendMessage("§a/cwegive <player> <item_id> [amount] §7- Cấp phát vật phẩm custom.");
        sender.sendMessage("§a/adminec §7- Giao diện Admin cường hóa Enchant/Ultimate tùy chọn.");
        sender.sendMessage("§a/cwestats §7- Giao diện chỉnh sửa chỉ số RPG đặc biệt.");
        sender.sendMessage("§a/cweie rename/lore/nbt §7- Biên tập NBT vật phẩm cầm trên tay.");
        sender.sendMessage("§a/delref §7- Xóa thuộc tính Reforge của trang bị đang cầm.");
        sender.sendMessage("§a/meteorite start <FIRE|ICE|VOID> §7- Kích hoạt sự kiện Thiên Thạch.");
        sender.sendMessage("§a/meteorite stop §7- Dừng khẩn cấp sự kiện Thiên Thạch.");
        sender.sendMessage("§a/setrank <player> <prefix> §7- Đổi prefix LuckPerms trực tiếp.");
        sender.sendMessage("§a/orevein <create|delete|list|info|mininglvl> §7- Quản lý mỏ quặng.");
        sender.sendMessage("§e====================================================");
        return true;
    }

    @Override
    public void onDisable() {
        org.bukkit.event.HandlerList.unregisterAll(this);
        if (weaponEngine != null) weaponEngine.clearCooldowns();
        if (assassinEngine != null) assassinEngine.clearCooldowns();
        
        if (berserkEngine != null) {
            berserkEngine.clearCache();
        }
        if (regionBossManager != null) {
            regionBossManager.cleanupOnDisable();
        }
        if (meteorBossManager != null) {
            meteorBossManager.cleanupOnDisable();
        }
        
        getLogger().info("§c CustomWeaponEngine đã giải phóng RAM thành công!");
    }

    public static CustomWeaponEngine getInstance() { return instance; }
    public static org.example.system.TradeManager getTradeManager() { return tradeManager; }
    public static org.example.system.LibraryGUI getLibraryGUI() { return libraryGUI; }

    public static EnchantManager getEnchantManager() { return enchantManager; }
}

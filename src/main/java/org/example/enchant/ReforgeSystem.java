package org.example.enchant;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.example.stats.ItemStatsGUI;
import org.example.core.CustomWeaponEngine;

import java.util.*;

public class ReforgeSystem implements Listener, CommandExecutor {

    private final JavaPlugin plugin;
    private final Economy econ;

    // ── GUI Titles ──────────────────────────────────────────────────────────────
    private static final String MENU_TITLE      = "§6✦ Reforge Menu";
    private static final String NORMAL_TITLE    = "§8⚒ Normal Reforge";
    private static final String EXCLUSIVE_TITLE = "§5✦ Exclusive Reforge";

    // ── Costs ───────────────────────────────────────────────────────────────────
    private static final int NORMAL_COST    = 2_000;
    private static final int EXCLUSIVE_COST = 20_000;

    private static final Random random = new Random();

    public ReforgeSystem(JavaPlugin plugin, Economy econ) {
        this.plugin = plugin;
        this.econ = econ;
    }

    // ===========================================================================
    // PREFIX DATA — ENUMS & STATS
    // ===========================================================================

    public enum ItemCategory { MELEE, RANGED, ARMOR, TOOLS }
    public enum ReforgeTier  { COMMON, UNCOMMON, RARE, EPIC, LEGENDARY, MYTHIC, NONE }

    public static class ReforgeStat {
        public String name;
        public double strength = 0, critChance = 0, critDamage = 0, speed = 0,
                      intelligence = 0, health = 0, defense = 0, attackSpeed = 0;
        /** Multiplier dùng cho WITHERED: sát thương = strength + levelMultiplier * playerLevel */
        public double levelMultiplier = 0;
        public double baseStatMultiplier = 1.0;

        public ReforgeStat(String name) { this.name = name; }
        public ReforgeStat str(double v)    { this.strength      = v; return this; }
        public ReforgeStat cc(double v)     { this.critChance    = v; return this; }
        public ReforgeStat cd(double v)     { this.critDamage    = v; return this; }
        public ReforgeStat spd(double v)    { this.speed         = v; return this; }
        public ReforgeStat intel(double v)  { this.intelligence  = v; return this; }
        public ReforgeStat hp(double v)     { this.health        = v; return this; }
        public ReforgeStat def(double v)    { this.defense       = v; return this; }
        public ReforgeStat atkSpd(double v) { this.attackSpeed   = v; return this; }
        public ReforgeStat lvlMult(double v){ this.levelMultiplier = v; return this; }
        public ReforgeStat baseMult(double v){ this.baseStatMultiplier = v; return this; }
    }

    // ── NORMAL pool ─────────────────────────────────────────────────────────────
    private static final Map<ItemCategory, Map<String, Map<ReforgeTier, ReforgeStat>>> REFORGE_MAP = new HashMap<>();

    // ── EXCLUSIVE pool ───────────────────────────────────────────────────────────
    private static final Map<ItemCategory, Map<String, Map<ReforgeTier, ReforgeStat>>> EXCLUSIVE_MAP = new HashMap<>();

    static {
        // ════════════════════════════════════════════
        //  NORMAL POOL
        // ════════════════════════════════════════════

        // --- MELEE NORMAL ---
        Map<String, Map<ReforgeTier, ReforgeStat>> melee = new LinkedHashMap<>();

        Map<ReforgeTier, ReforgeStat> spicy = new HashMap<>();
        spicy.put(ReforgeTier.COMMON,    new ReforgeStat("Spicy").str(8).cc(2).cd(10));
        spicy.put(ReforgeTier.UNCOMMON,  new ReforgeStat("Spicy").str(11).cc(3).cd(15));
        spicy.put(ReforgeTier.RARE,      new ReforgeStat("Spicy").str(15).cc(4).cd(20));
        spicy.put(ReforgeTier.EPIC,      new ReforgeStat("Spicy").str(20).cc(7).cd(25));
        spicy.put(ReforgeTier.LEGENDARY, new ReforgeStat("Spicy").str(25).cc(10).cd(35));
        melee.put("Spicy", spicy);

        Map<ReforgeTier, ReforgeStat> heroic = new HashMap<>();
        heroic.put(ReforgeTier.COMMON,    new ReforgeStat("Heroic").str(8).spd(1).intel(20));
        heroic.put(ReforgeTier.UNCOMMON,  new ReforgeStat("Heroic").str(11).spd(1).intel(30));
        heroic.put(ReforgeTier.RARE,      new ReforgeStat("Heroic").str(15).spd(1).intel(40));
        heroic.put(ReforgeTier.EPIC,      new ReforgeStat("Heroic").str(20).spd(2).intel(50));
        heroic.put(ReforgeTier.LEGENDARY, new ReforgeStat("Heroic").str(25).spd(3).intel(65));
        melee.put("Heroic", heroic);

        Map<ReforgeTier, ReforgeStat> legendary = new HashMap<>();
        legendary.put(ReforgeTier.COMMON,    new ReforgeStat("Legendary").atkSpd(2).str(6).cc(2).cd(8).intel(8));
        legendary.put(ReforgeTier.UNCOMMON,  new ReforgeStat("Legendary").atkSpd(2).str(9).cc(4).cd(11).intel(11));
        legendary.put(ReforgeTier.RARE,      new ReforgeStat("Legendary").atkSpd(3).str(12).cc(5).cd(15).intel(15));
        legendary.put(ReforgeTier.EPIC,      new ReforgeStat("Legendary").atkSpd(5).str(15).cc(7).cd(20).intel(20));
        legendary.put(ReforgeTier.LEGENDARY, new ReforgeStat("Legendary").atkSpd(7).str(20).cc(9).cd(28).intel(25));
        melee.put("Legendary", legendary);

        Map<ReforgeTier, ReforgeStat> sharp = new HashMap<>();
        sharp.put(ReforgeTier.COMMON,    new ReforgeStat("Sharp").cc(6).cd(10));
        sharp.put(ReforgeTier.UNCOMMON,  new ReforgeStat("Sharp").cc(9).cd(15));
        sharp.put(ReforgeTier.RARE,      new ReforgeStat("Sharp").cc(12).cd(20));
        sharp.put(ReforgeTier.EPIC,      new ReforgeStat("Sharp").cc(14).cd(25));
        sharp.put(ReforgeTier.LEGENDARY, new ReforgeStat("Sharp").cc(17).cd(30));
        melee.put("Sharp", sharp);

        REFORGE_MAP.put(ItemCategory.MELEE, melee);

        // --- RANGED NORMAL ---
        Map<String, Map<ReforgeTier, ReforgeStat>> ranged = new LinkedHashMap<>();

        Map<ReforgeTier, ReforgeStat> rapid = new HashMap<>();
        rapid.put(ReforgeTier.COMMON,    new ReforgeStat("Rapid").str(5).cd(12));
        rapid.put(ReforgeTier.UNCOMMON,  new ReforgeStat("Rapid").str(8).cd(19));
        rapid.put(ReforgeTier.RARE,      new ReforgeStat("Rapid").str(10).cd(25));
        rapid.put(ReforgeTier.EPIC,      new ReforgeStat("Rapid").str(15).cd(35));
        rapid.put(ReforgeTier.LEGENDARY, new ReforgeStat("Rapid").str(22).cd(50));
        ranged.put("Rapid", rapid);

        Map<ReforgeTier, ReforgeStat> unreal = new HashMap<>();
        unreal.put(ReforgeTier.COMMON,    new ReforgeStat("Unreal").atkSpd(2).str(4).cc(4).cd(8));
        unreal.put(ReforgeTier.UNCOMMON,  new ReforgeStat("Unreal").atkSpd(2).str(6).cc(6).cd(13));
        unreal.put(ReforgeTier.RARE,      new ReforgeStat("Unreal").atkSpd(3).str(8).cc(8).cd(17));
        unreal.put(ReforgeTier.EPIC,      new ReforgeStat("Unreal").atkSpd(5).str(11).cc(10).cd(22));
        unreal.put(ReforgeTier.LEGENDARY, new ReforgeStat("Unreal").atkSpd(7).str(15).cc(13).cd(29));
        ranged.put("Unreal", unreal);

        
        Map<ReforgeTier, ReforgeStat> awkward = new HashMap<>();
        awkward.put(ReforgeTier.COMMON, new ReforgeStat("Awkward").cc(10).cd(5).intel(5));
        awkward.put(ReforgeTier.UNCOMMON, new ReforgeStat("Awkward").cc(12).cd(8).intel(8));
        awkward.put(ReforgeTier.RARE, new ReforgeStat("Awkward").cc(15).cd(10).intel(10));
        awkward.put(ReforgeTier.EPIC, new ReforgeStat("Awkward").cc(20).cd(15).intel(15));
        awkward.put(ReforgeTier.LEGENDARY, new ReforgeStat("Awkward").cc(25).cd(20).intel(20));
        ranged.put("Awkward", awkward);

        Map<ReforgeTier, ReforgeStat> rich = new HashMap<>();
        rich.put(ReforgeTier.COMMON, new ReforgeStat("Rich").intel(10));
        rich.put(ReforgeTier.UNCOMMON, new ReforgeStat("Rich").intel(15));
        rich.put(ReforgeTier.RARE, new ReforgeStat("Rich").intel(20));
        rich.put(ReforgeTier.EPIC, new ReforgeStat("Rich").intel(30));
        rich.put(ReforgeTier.LEGENDARY, new ReforgeStat("Rich").intel(40));
        ranged.put("Rich", rich);

        Map<ReforgeTier, ReforgeStat> fine = new HashMap<>();
        fine.put(ReforgeTier.COMMON, new ReforgeStat("Fine").str(3).cc(3).cd(3));
        fine.put(ReforgeTier.UNCOMMON, new ReforgeStat("Fine").str(5).cc(4).cd(5));
        fine.put(ReforgeTier.RARE, new ReforgeStat("Fine").str(7).cc(5).cd(8));
        fine.put(ReforgeTier.EPIC, new ReforgeStat("Fine").str(10).cc(8).cd(12));
        fine.put(ReforgeTier.LEGENDARY, new ReforgeStat("Fine").str(15).cc(10).cd(18));
        ranged.put("Fine", fine);

        Map<ReforgeTier, ReforgeStat> neat = new HashMap<>();
        neat.put(ReforgeTier.COMMON, new ReforgeStat("Neat").cd(10));
        neat.put(ReforgeTier.UNCOMMON, new ReforgeStat("Neat").cd(14));
        neat.put(ReforgeTier.RARE, new ReforgeStat("Neat").cd(18));
        neat.put(ReforgeTier.EPIC, new ReforgeStat("Neat").cd(24));
        neat.put(ReforgeTier.LEGENDARY, new ReforgeStat("Neat").cd(30));
        ranged.put("Neat", neat);

        Map<ReforgeTier, ReforgeStat> hasty = new HashMap<>();
        hasty.put(ReforgeTier.COMMON, new ReforgeStat("Hasty").str(3).cc(15));
        hasty.put(ReforgeTier.UNCOMMON, new ReforgeStat("Hasty").str(5).cc(20));
        hasty.put(ReforgeTier.RARE, new ReforgeStat("Hasty").str(7).cc(25));
        hasty.put(ReforgeTier.EPIC, new ReforgeStat("Hasty").str(10).cc(35));
        hasty.put(ReforgeTier.LEGENDARY, new ReforgeStat("Hasty").str(15).cc(50));
        ranged.put("Hasty", hasty);

        Map<ReforgeTier, ReforgeStat> grand = new HashMap<>();
        grand.put(ReforgeTier.COMMON, new ReforgeStat("Grand").str(15));
        grand.put(ReforgeTier.UNCOMMON, new ReforgeStat("Grand").str(20));
        grand.put(ReforgeTier.RARE, new ReforgeStat("Grand").str(25));
        grand.put(ReforgeTier.EPIC, new ReforgeStat("Grand").str(40));
        grand.put(ReforgeTier.LEGENDARY, new ReforgeStat("Grand").str(50));
        ranged.put("Grand", grand);

        Map<ReforgeTier, ReforgeStat> deadly = new HashMap<>();
        deadly.put(ReforgeTier.COMMON, new ReforgeStat("Deadly").cc(5).cd(10));
        deadly.put(ReforgeTier.UNCOMMON, new ReforgeStat("Deadly").cc(7).cd(15));
        deadly.put(ReforgeTier.RARE, new ReforgeStat("Deadly").cc(10).cd(20));
        deadly.put(ReforgeTier.EPIC, new ReforgeStat("Deadly").cc(14).cd(28));
        deadly.put(ReforgeTier.LEGENDARY, new ReforgeStat("Deadly").cc(18).cd(35));
        ranged.put("Deadly", deadly);

        REFORGE_MAP.put(ItemCategory.RANGED, ranged);

        // --- ARMOR NORMAL ---
        Map<String, Map<ReforgeTier, ReforgeStat>> armor = new LinkedHashMap<>();

        Map<ReforgeTier, ReforgeStat> fierce = new HashMap<>();
        fierce.put(ReforgeTier.COMMON,    new ReforgeStat("Fierce").str(3).cc(2).cd(5));
        fierce.put(ReforgeTier.UNCOMMON,  new ReforgeStat("Fierce").str(4).cc(3).cd(8));
        fierce.put(ReforgeTier.RARE,      new ReforgeStat("Fierce").str(6).cc(4).cd(10));
        fierce.put(ReforgeTier.EPIC,      new ReforgeStat("Fierce").str(8).cc(7).cd(14));
        fierce.put(ReforgeTier.LEGENDARY, new ReforgeStat("Fierce").str(10).cc(10).cd(18));
        armor.put("Fierce", fierce);

        Map<ReforgeTier, ReforgeStat> titanic = new HashMap<>();
        titanic.put(ReforgeTier.COMMON,    new ReforgeStat("Titanic").hp(8).def(8));
        titanic.put(ReforgeTier.UNCOMMON,  new ReforgeStat("Titanic").hp(11).def(11));
        titanic.put(ReforgeTier.RARE,      new ReforgeStat("Titanic").hp(15).def(15));
        titanic.put(ReforgeTier.EPIC,      new ReforgeStat("Titanic").hp(25).def(25));
        titanic.put(ReforgeTier.LEGENDARY, new ReforgeStat("Titanic").hp(40).def(40));
        armor.put("Titanic", titanic);

        Map<ReforgeTier, ReforgeStat> wise = new HashMap<>();
        wise.put(ReforgeTier.COMMON,    new ReforgeStat("Wise").hp(4).intel(25));
        wise.put(ReforgeTier.UNCOMMON,  new ReforgeStat("Wise").hp(6).intel(38));
        wise.put(ReforgeTier.RARE,      new ReforgeStat("Wise").hp(8).intel(50));
        wise.put(ReforgeTier.EPIC,      new ReforgeStat("Wise").hp(12).intel(75));
        wise.put(ReforgeTier.LEGENDARY, new ReforgeStat("Wise").hp(15).intel(100));
        armor.put("Wise", wise);

        Map<ReforgeTier, ReforgeStat> pure = new HashMap<>();
        pure.put(ReforgeTier.COMMON,    new ReforgeStat("Pure").cd(4));
        pure.put(ReforgeTier.UNCOMMON,  new ReforgeStat("Pure").cd(6));
        pure.put(ReforgeTier.RARE,      new ReforgeStat("Pure").cd(8));
        pure.put(ReforgeTier.EPIC,      new ReforgeStat("Pure").cd(12));
        pure.put(ReforgeTier.LEGENDARY, new ReforgeStat("Pure").cd(18));
        armor.put("Pure", pure);

        Map<ReforgeTier, ReforgeStat> perfect = new HashMap<>();
        perfect.put(ReforgeTier.COMMON,    new ReforgeStat("Perfect").str(2).def(4));
        perfect.put(ReforgeTier.UNCOMMON,  new ReforgeStat("Perfect").str(4).def(6));
        perfect.put(ReforgeTier.RARE,      new ReforgeStat("Perfect").str(5).def(8));
        perfect.put(ReforgeTier.EPIC,      new ReforgeStat("Perfect").str(5).def(12));
        perfect.put(ReforgeTier.LEGENDARY, new ReforgeStat("Perfect").str(5).def(18));
        armor.put("Perfect", perfect);

        Map<ReforgeTier, ReforgeStat> clean = new HashMap<>();
        clean.put(ReforgeTier.COMMON,    new ReforgeStat("Clean").hp(5).spd(1));
        clean.put(ReforgeTier.UNCOMMON,  new ReforgeStat("Clean").hp(8).spd(1));
        clean.put(ReforgeTier.RARE,      new ReforgeStat("Clean").hp(10).spd(1));
        clean.put(ReforgeTier.EPIC,      new ReforgeStat("Clean").hp(18).spd(2));
        clean.put(ReforgeTier.LEGENDARY, new ReforgeStat("Clean").hp(30).spd(3));
        armor.put("Clean", clean);

        Map<ReforgeTier, ReforgeStat> light = new HashMap<>();
        light.put(ReforgeTier.COMMON,    new ReforgeStat("Light").spd(1).atkSpd(1));
        light.put(ReforgeTier.UNCOMMON,  new ReforgeStat("Light").spd(2).atkSpd(2));
        light.put(ReforgeTier.RARE,      new ReforgeStat("Light").spd(2).atkSpd(2));
        light.put(ReforgeTier.EPIC,      new ReforgeStat("Light").spd(3).atkSpd(3));
        light.put(ReforgeTier.LEGENDARY, new ReforgeStat("Light").spd(5).atkSpd(5));
        armor.put("Light", light);

        REFORGE_MAP.put(ItemCategory.ARMOR, armor);

        // --- TOOLS NORMAL ---
        Map<String, Map<ReforgeTier, ReforgeStat>> tools = new LinkedHashMap<>();

        Map<ReforgeTier, ReforgeStat> refined = new HashMap<>();
        refined.put(ReforgeTier.COMMON,    new ReforgeStat("Refined").def(2).intel(2));
        refined.put(ReforgeTier.UNCOMMON,  new ReforgeStat("Refined").def(4).intel(4));
        refined.put(ReforgeTier.RARE,      new ReforgeStat("Refined").def(5).intel(5));
        refined.put(ReforgeTier.EPIC,      new ReforgeStat("Refined").def(8).intel(10));
        refined.put(ReforgeTier.LEGENDARY, new ReforgeStat("Refined").def(12).intel(15));
        tools.put("Refined", refined);

        REFORGE_MAP.put(ItemCategory.TOOLS, tools);

        // ════════════════════════════════════════════
        //  EXCLUSIVE POOL
        // ════════════════════════════════════════════

        // --- ARMOR EXCLUSIVE ---
        Map<String, Map<ReforgeTier, ReforgeStat>> exArmor = new LinkedHashMap<>();

        Map<ReforgeTier, ReforgeStat> ancient = new HashMap<>();
        ancient.put(ReforgeTier.COMMON,    new ReforgeStat("Ancient").str(4).cd(3).def(5));
        ancient.put(ReforgeTier.UNCOMMON,  new ReforgeStat("Ancient").str(6).cd(4).def(8));
        ancient.put(ReforgeTier.RARE,      new ReforgeStat("Ancient").str(8).cd(6).def(10));
        ancient.put(ReforgeTier.EPIC,      new ReforgeStat("Ancient").str(12).cd(9).def(15));
        ancient.put(ReforgeTier.LEGENDARY, new ReforgeStat("Ancient").str(18).cd(12).def(25));
        exArmor.put("Ancient", ancient);

        Map<ReforgeTier, ReforgeStat> necrotic = new HashMap<>();
        necrotic.put(ReforgeTier.COMMON,    new ReforgeStat("Necrotic").intel(55));
        necrotic.put(ReforgeTier.UNCOMMON,  new ReforgeStat("Necrotic").intel(82));
        necrotic.put(ReforgeTier.RARE,      new ReforgeStat("Necrotic").intel(110));
        necrotic.put(ReforgeTier.EPIC,      new ReforgeStat("Necrotic").intel(150));
        necrotic.put(ReforgeTier.LEGENDARY, new ReforgeStat("Necrotic").intel(200));
        exArmor.put("Necrotic", necrotic);

        Map<ReforgeTier, ReforgeStat> giant = new HashMap<>();
        giant.put(ReforgeTier.COMMON,    new ReforgeStat("Giant").hp(25));
        giant.put(ReforgeTier.UNCOMMON,  new ReforgeStat("Giant").hp(38));
        giant.put(ReforgeTier.RARE,      new ReforgeStat("Giant").hp(50));
        giant.put(ReforgeTier.EPIC,      new ReforgeStat("Giant").hp(90));
        giant.put(ReforgeTier.LEGENDARY, new ReforgeStat("Giant").hp(140));
        exArmor.put("Giant", giant);

        EXCLUSIVE_MAP.put(ItemCategory.ARMOR, exArmor);

        
        // --- RANGED EXCLUSIVE ---
        Map<String, Map<ReforgeTier, ReforgeStat>> exRanged = new LinkedHashMap<>();

        Map<ReforgeTier, ReforgeStat> precise = new HashMap<>();
        precise.put(ReforgeTier.COMMON, new ReforgeStat("Precise").str(5).cc(4).cd(10));
        precise.put(ReforgeTier.UNCOMMON, new ReforgeStat("Precise").str(8).cc(6).cd(15));
        precise.put(ReforgeTier.RARE, new ReforgeStat("Precise").str(12).cc(8).cd(20));
        precise.put(ReforgeTier.EPIC, new ReforgeStat("Precise").str(18).cc(10).cd(28));
        precise.put(ReforgeTier.LEGENDARY, new ReforgeStat("Precise").str(25).cc(12).cd(35));
        exRanged.put("Precise", precise);

        Map<ReforgeTier, ReforgeStat> spiritual = new HashMap<>();
        spiritual.put(ReforgeTier.COMMON, new ReforgeStat("Spiritual").str(8).cc(4).cd(12));
        spiritual.put(ReforgeTier.UNCOMMON, new ReforgeStat("Spiritual").str(12).cc(6).cd(18));
        spiritual.put(ReforgeTier.RARE, new ReforgeStat("Spiritual").str(18).cc(8).cd(24));
        spiritual.put(ReforgeTier.EPIC, new ReforgeStat("Spiritual").str(25).cc(10).cd(32));
        spiritual.put(ReforgeTier.LEGENDARY, new ReforgeStat("Spiritual").str(32).cc(12).cd(40));
        exRanged.put("Spiritual", spiritual);

        Map<ReforgeTier, ReforgeStat> headstrong = new HashMap<>();
        headstrong.put(ReforgeTier.COMMON, new ReforgeStat("Headstrong").str(15).cd(15));
        headstrong.put(ReforgeTier.UNCOMMON, new ReforgeStat("Headstrong").str(20).cd(20));
        headstrong.put(ReforgeTier.RARE, new ReforgeStat("Headstrong").str(25).cd(25));
        headstrong.put(ReforgeTier.EPIC, new ReforgeStat("Headstrong").str(35).cd(35));
        headstrong.put(ReforgeTier.LEGENDARY, new ReforgeStat("Headstrong").str(45).cd(45));
        exRanged.put("Headstrong", headstrong);

        EXCLUSIVE_MAP.put(ItemCategory.RANGED, exRanged);

        // --- MELEE EXCLUSIVE ---
        Map<String, Map<ReforgeTier, ReforgeStat>> exMelee = new LinkedHashMap<>();

        Map<ReforgeTier, ReforgeStat> fabled = new HashMap<>();
        fabled.put(ReforgeTier.COMMON,    new ReforgeStat("Fabled").str(15).cd(8));
        fabled.put(ReforgeTier.UNCOMMON,  new ReforgeStat("Fabled").str(22).cd(11));
        fabled.put(ReforgeTier.RARE,      new ReforgeStat("Fabled").str(30).cd(15));
        fabled.put(ReforgeTier.EPIC,      new ReforgeStat("Fabled").str(40).cd(20));
        fabled.put(ReforgeTier.LEGENDARY, new ReforgeStat("Fabled").str(50).cd(25));
        exMelee.put("Fabled", fabled);

        Map<ReforgeTier, ReforgeStat> withered = new HashMap<>();
        withered.put(ReforgeTier.COMMON,    new ReforgeStat("Withered").str(8).lvlMult(0.4));
        withered.put(ReforgeTier.UNCOMMON,  new ReforgeStat("Withered").str(11).lvlMult(0.6));
        withered.put(ReforgeTier.RARE,      new ReforgeStat("Withered").str(15).lvlMult(0.8));
        withered.put(ReforgeTier.EPIC,      new ReforgeStat("Withered").str(20).lvlMult(1.0));
        withered.put(ReforgeTier.LEGENDARY, new ReforgeStat("Withered").str(25).lvlMult(1.2));
        exMelee.put("Withered", withered);

        EXCLUSIVE_MAP.put(ItemCategory.MELEE, exMelee);

        // --- EXCLUSIVE NEW POOLS (For Fragments) ---
        // Basic / Rare exclusive
        Map<ReforgeTier, ReforgeStat> reinforced = new HashMap<>();
        reinforced.put(ReforgeTier.COMMON,    new ReforgeStat("Reinforced").hp(15).def(10));
        reinforced.put(ReforgeTier.UNCOMMON,  new ReforgeStat("Reinforced").hp(22).def(15));
        reinforced.put(ReforgeTier.RARE, new ReforgeStat("Reinforced").hp(30).def(20));
        reinforced.put(ReforgeTier.EPIC, new ReforgeStat("Reinforced").hp(50).def(30));
        reinforced.put(ReforgeTier.LEGENDARY, new ReforgeStat("Reinforced").hp(70).def(45));
        reinforced.put(ReforgeTier.MYTHIC, new ReforgeStat("Reinforced").hp(90).def(60));
        exArmor.put("Reinforced", reinforced);
        
        Map<ReforgeTier, ReforgeStat> magnetic = new HashMap<>();
        magnetic.put(ReforgeTier.COMMON,    new ReforgeStat("Magnetic").cd(10));
        magnetic.put(ReforgeTier.UNCOMMON,  new ReforgeStat("Magnetic").cd(15));
        magnetic.put(ReforgeTier.RARE, new ReforgeStat("Magnetic").cd(20));
        magnetic.put(ReforgeTier.EPIC, new ReforgeStat("Magnetic").cd(30));
        magnetic.put(ReforgeTier.LEGENDARY, new ReforgeStat("Magnetic").cd(45));
        magnetic.put(ReforgeTier.MYTHIC, new ReforgeStat("Magnetic").cd(60));
        
        // Epic / Legendary exclusive
        Map<ReforgeTier, ReforgeStat> demonic = new HashMap<>();
        demonic.put(ReforgeTier.COMMON,    new ReforgeStat("Demonic").str(20).cd(15));
        demonic.put(ReforgeTier.UNCOMMON,  new ReforgeStat("Demonic").str(30).cd(22));
        demonic.put(ReforgeTier.RARE, new ReforgeStat("Demonic").str(40).cd(30));
        demonic.put(ReforgeTier.EPIC, new ReforgeStat("Demonic").str(60).cd(45));
        demonic.put(ReforgeTier.LEGENDARY, new ReforgeStat("Demonic").str(90).cd(60));
        demonic.put(ReforgeTier.MYTHIC, new ReforgeStat("Demonic").str(120).cd(80));
        exMelee.put("Demonic", demonic);
        
        Map<ReforgeTier, ReforgeStat> aegis = new HashMap<>();
        aegis.put(ReforgeTier.COMMON,    new ReforgeStat("Aegis").hp(50).def(40));
        aegis.put(ReforgeTier.UNCOMMON,  new ReforgeStat("Aegis").hp(75).def(60));
        aegis.put(ReforgeTier.RARE, new ReforgeStat("Aegis").hp(100).def(80));
        aegis.put(ReforgeTier.EPIC, new ReforgeStat("Aegis").hp(150).def(120));
        aegis.put(ReforgeTier.LEGENDARY, new ReforgeStat("Aegis").hp(220).def(180));
        aegis.put(ReforgeTier.MYTHIC, new ReforgeStat("Aegis").hp(300).def(250));
        exArmor.put("Aegis", aegis);
        
        // Mythic exclusive (Cosmic, Divine)
        Map<ReforgeTier, ReforgeStat> cosmic = new HashMap<>();
        cosmic.put(ReforgeTier.MYTHIC, new ReforgeStat("Cosmic").str(150).cd(100).baseMult(1.2));
        exMelee.put("Cosmic", cosmic);
        
        Map<ReforgeTier, ReforgeStat> divine = new HashMap<>();
        divine.put(ReforgeTier.MYTHIC, new ReforgeStat("Divine").hp(500).def(300).intel(200).baseMult(1.2));
        exArmor.put("Divine", divine);
    }

    // ── Static getters (used by ItemStatsListener) ───────────────────────────────
    public static ReforgeStat getReforgeStat(String prefix, ItemCategory category, ReforgeTier tier) {
        Map<String, Map<ReforgeTier, ReforgeStat>> pool = REFORGE_MAP.get(category);
        if (pool == null || !pool.containsKey(prefix)) {
            // Thử trong exclusive pool
            pool = EXCLUSIVE_MAP.get(category);
        }
        if (pool == null || !pool.containsKey(prefix)) return null;
        return pool.get(prefix).get(tier);
    }

    public static ReforgeStat getExclusiveReforgeStat(String prefix, ItemCategory category, ReforgeTier tier) {
        Map<String, Map<ReforgeTier, ReforgeStat>> pool = EXCLUSIVE_MAP.get(category);
        if (pool == null || !pool.containsKey(prefix)) return null;
        return pool.get(prefix).get(tier);
    }

    // ===========================================================================
    // COMMANDS
    // ===========================================================================

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return true;
        Player player = (Player) sender;

        if (command.getName().equalsIgnoreCase("delref")) {
            if (!player.hasPermission("cwe.admin")) {
                player.sendMessage("§cBạn không có quyền dùng lệnh này!");
                return true;
            }
            handleRemoveReforge(player);
            return true;
        }

        // /reforge → mở Index Menu
        openIndexMenu(player);
        return true;
    }

    private void handleRemoveReforge(Player player) {
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item == null || item.getType().isAir() || !item.hasItemMeta()) {
            player.sendMessage("§cBạn phải cầm vật phẩm có Reforge!");
            return;
        }
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        NamespacedKey reforgeKey = new NamespacedKey(plugin, "cwe_reforge");

        if (!pdc.has(reforgeKey, PersistentDataType.STRING)) {
            player.sendMessage("§cVật phẩm này chưa được Reforge!");
            return;
        }

        ItemCategory cat = getCategory(item, pdc);
        applyReforge(item, meta, pdc, "", ReforgeTier.NONE, cat, false);
        player.sendMessage("§aĐã xoá Reforge khỏi vật phẩm thành công!");
    }

    // ===========================================================================
    // GUI — INDEX MENU (27 ô)
    // ===========================================================================

    private void openIndexMenu(Player player) {
        Inventory gui = Bukkit.createInventory(null, 27, MENU_TITLE);

        // Fill toàn bộ bằng kính xám
        ItemStack glass = createItem(Material.GRAY_STAINED_GLASS_PANE, "§8 ");
        for (int i = 0; i < 27; i++) gui.setItem(i, glass);

        // Slot 11: Normal Reforge
        ItemStack normalBtn = createItem(Material.ANVIL,
            "§a§l⚒ NORMAL REFORGE",
            "§7Chi phí: §e2,000 Coins",
            "§7Roll ngẫu nhiên tiền tố thuộc tính.",
            "§7Áp dụng cho: §fTất cả trang bị.",
            "",
            "§eClick để mở!"
        );
        gui.setItem(11, normalBtn);

        // Slot 15: Exclusive Reforge
        ItemStack exclusiveBtn = createItem(Material.NETHERITE_UPGRADE_SMITHING_TEMPLATE,
            "§d§l✦ EXCLUSIVE REFORGE",
            "§7Chi phí: §d20,000 Coins",
            "§7Mở khoá tiền tố đặc quyền cực mạnh.",
            "§7Áp dụng cho: §fGiáp & Vũ khí cận chiến.",
            "",
            "§dClick để mở!"
        );
        gui.setItem(15, exclusiveBtn);

        // Slot 22: Đóng
        gui.setItem(22, createItem(Material.BARRIER, "§cĐóng Giao Diện"));

        player.openInventory(gui);
    }

    // ===========================================================================
    // GUI — NORMAL REFORGE (54 ô)
    // ===========================================================================

    private void openNormalReforgeGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, NORMAL_TITLE);
        fillNormalReforgeGUI(gui);
        player.openInventory(gui);
    }

    private void fillNormalReforgeGUI(Inventory gui) {
        ItemStack redGlass  = createItem(Material.RED_STAINED_GLASS_PANE, "§8 ");
        ItemStack grayGlass = createItem(Material.GRAY_STAINED_GLASS_PANE, "§8 ");

        for (int i = 0; i < 54; i++) {
            int col = i % 9;
            if (col == 0 || col == 8) gui.setItem(i, redGlass);
            else gui.setItem(i, grayGlass);
        }

        gui.setItem(13, null); // Ô để vật phẩm

        ItemStack activateBtn = createItem(Material.ANVIL,
            "§a§lKÍCH HOẠT NORMAL REFORGE",
            "§7Chi phí: §e2,000 Coins",
            "§7Roll ngẫu nhiên tiền tố thuộc tính.");
        gui.setItem(31, activateBtn);

        gui.setItem(48, createItem(Material.ARROW, "§7« Quay lại Menu"));
        gui.setItem(49, createItem(Material.BARRIER, "§cĐóng Giao Diện"));
    }

    // ===========================================================================
    // GUI — EXCLUSIVE REFORGE (54 ô)
    // ===========================================================================

    private void openExclusiveReforgeGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, EXCLUSIVE_TITLE);
        
        ItemStack grayGlass = createItem(Material.GRAY_STAINED_GLASS_PANE, "§7 ");
        ItemStack redGlass = createItem(Material.RED_STAINED_GLASS_PANE, "§7 ");
        ItemStack lightGrayGlass = createItem(Material.LIGHT_GRAY_STAINED_GLASS_PANE, "§7 ");
        
        for (int i = 0; i <= 8; i++) gui.setItem(i, grayGlass);
        gui.setItem(9, grayGlass); gui.setItem(10, grayGlass);
        gui.setItem(11, redGlass); gui.setItem(12, redGlass);
        gui.setItem(13, createItem(Material.BARRIER, "§cChưa đủ điều kiện", "§7Hãy đặt Trang bị vào ô 21", "§7và Mảnh vỡ vào ô 23."));
        gui.setItem(14, redGlass); gui.setItem(15, redGlass);
        gui.setItem(16, grayGlass); gui.setItem(17, grayGlass);
        
        gui.setItem(18, grayGlass); gui.setItem(19, grayGlass);
        gui.setItem(20, redGlass);
        gui.setItem(21, null);
        gui.setItem(22, createItem(Material.ANVIL, "§d§lKÍCH HOẠT EXCLUSIVE REFORGE", "§7Chi phí: §d20,000 Coins", "§7Click để xoay Gacha!"));
        gui.setItem(23, null);
        gui.setItem(24, redGlass);
        gui.setItem(25, grayGlass); gui.setItem(26, grayGlass);
        
        gui.setItem(27, grayGlass); gui.setItem(28, grayGlass);
        gui.setItem(29, lightGrayGlass);
        gui.setItem(30, grayGlass); gui.setItem(31, grayGlass); gui.setItem(32, grayGlass);
        gui.setItem(33, lightGrayGlass);
        gui.setItem(34, grayGlass); gui.setItem(35, grayGlass);
        
        for (int i = 36; i <= 44; i++) gui.setItem(i, grayGlass);
        
        int[] bottomRed = {45, 46, 47, 48, 50, 51, 53};
        for (int i : bottomRed) gui.setItem(i, redGlass);
        gui.setItem(49, createItem(Material.BARRIER, "§cĐóng Giao Diện"));
        gui.setItem(52, createItem(Material.IRON_INGOT, "§eDanh sách Tiền Tố"));
        
        player.openInventory(gui);
    }
    // ===========================================================================
    // EVENTS
    // ===========================================================================

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        String title = event.getView().getTitle();
        Player player = (Player) event.getWhoClicked();

        if (title.equals(MENU_TITLE)) {
            event.setCancelled(true);
            int slot = event.getRawSlot();
            if (slot == 11) {
                player.closeInventory();
                Bukkit.getScheduler().runTask(plugin, () -> openNormalReforgeGUI(player));
            } else if (slot == 15) {
                player.closeInventory();
                Bukkit.getScheduler().runTask(plugin, () -> openExclusiveReforgeGUI(player));
            } else if (slot == 22) {
                player.closeInventory();
            }
            return;
        }

        if (title.equals(NORMAL_TITLE)) {
            handleReforgeGUIClick(event, player, false);
            return;
        }

        if (title.equals(EXCLUSIVE_TITLE)) {
            handleReforgeGUIClick(event, player, true);
            Bukkit.getScheduler().runTask(plugin, () -> updateExclusiveStatus(event.getView().getTopInventory()));
        }
    }

    private void updateExclusiveStatus(Inventory gui) {
        ItemStack item = gui.getItem(21);
        ItemStack frag = gui.getItem(23);
        boolean hasItem = item != null && !item.getType().isAir();
        boolean hasFrag = frag != null && !frag.getType().isAir();
        
        if (hasItem && hasFrag) {
            gui.setItem(13, createItem(Material.GREEN_STAINED_GLASS_PANE, "§aSẵn sàng Reforge!", "§7Nhấn vào Đe bên dưới để tiến hành."));
        } else {
            gui.setItem(13, createItem(Material.BARRIER, "§cChưa đủ điều kiện", "§7Hãy đặt Trang bị vào ô 21", "§7và Mảnh vỡ vào ô 23."));
        }
    }

    private void handleReforgeGUIClick(InventoryClickEvent event, Player player, boolean exclusive) {
        Inventory gui = event.getView().getTopInventory();
        int slot = event.getRawSlot();

        if (event.getClickedInventory() != gui) {
            if (event.isShiftClick()) event.setCancelled(true);
            return;
        }

        if (exclusive) {
            if (slot != 21 && slot != 23) {
                event.setCancelled(true);
            }
            if (slot == 22) {
                handleExclusiveReforge(player, gui.getItem(21), gui.getItem(23));
            } else if (slot == 49) {
                player.closeInventory();
            }
        } else {
            if (slot != 13) {
                event.setCancelled(true);
            }
            if (slot == 31) {
                handleNormalReforge(player, gui.getItem(13));
            } else if (slot == 48) {
                player.closeInventory();
                Bukkit.getScheduler().runTask(plugin, () -> openIndexMenu(player));
            } else if (slot == 49) {
                player.closeInventory();
            }
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        String title = event.getView().getTitle();
        if (!title.equals(NORMAL_TITLE) && !title.equals(EXCLUSIVE_TITLE)) return;

        Player player = (Player) event.getPlayer();
        Inventory gui = event.getInventory();
        
        if (title.equals(NORMAL_TITLE)) {
            giveItemBack(player, gui, 13);
        } else {
            giveItemBack(player, gui, 21);
            giveItemBack(player, gui, 23);
        }
    }
    
    private void giveItemBack(Player player, Inventory gui, int slot) {
        ItemStack item = gui.getItem(slot);
        if (item != null && !item.getType().isAir()) {
            gui.setItem(slot, null);
            java.util.HashMap<Integer, ItemStack> leftOvers = player.getInventory().addItem(item);
            if (!leftOvers.isEmpty()) {
                for (ItemStack overflow : leftOvers.values()) {
                    player.getWorld().dropItem(player.getLocation(), overflow);
                }
            }
        }
    }

    // ===========================================================================
    // REFORGE LOGIC — NORMAL
    // ===========================================================================

    private void handleNormalReforge(Player player, ItemStack item) {
        if (item == null || item.getType().isAir() || !item.hasItemMeta()) {
            player.sendMessage("§cBạn chưa đặt vật phẩm hợp lệ vào ô!");
            return;
        }

        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();

        ReforgeTier tier = resolveTier(item, pdc);
        if (tier == ReforgeTier.NONE) {
            player.sendMessage("§cChỉ vật phẩm COMMON, UNCOMMON, RARE, EPIC, LEGENDARY mới có thể trùng đúc!");
            return;
        }

        if (econ == null || econ.getBalance(player) < NORMAL_COST) {
            player.sendMessage(String.format("§cBạn không có đủ %,d Coins!", NORMAL_COST));
            return;
        }

        econ.withdrawPlayer(player, NORMAL_COST);
        player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_USE, 1.0f, 1.0f);

        ItemCategory cat = getCategory(item, pdc);
        String prefix = rollNormalPrefix(cat);
        applyReforge(item, meta, pdc, prefix, tier, cat, false);
    }

    // ===========================================================================
    // REFORGE LOGIC — EXCLUSIVE
    // ===========================================================================

    private void handleExclusiveReforge(Player player, ItemStack item, ItemStack fragment) {
        if (item == null || item.getType().isAir() || !item.hasItemMeta()) {
            player.sendMessage("§cBạn chưa đặt vật phẩm hợp lệ vào ô trang bị!");
            return;
        }
        if (fragment == null || fragment.getType().isAir() || !fragment.hasItemMeta()) {
            player.sendMessage("§cBạn chưa đặt Mảnh vỡ Reforge vào ô nguyên liệu!");
            return;
        }

        PersistentDataContainer fPdc = fragment.getItemMeta().getPersistentDataContainer();
        if (!fPdc.has(new NamespacedKey(plugin, "cwe_reforge_fragment"), PersistentDataType.INTEGER)) {
            player.sendMessage("§cĐó không phải là Mảnh vỡ Reforge hợp lệ!");
            return;
        }

        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();

        ItemCategory cat = getCategory(item, pdc);
        if (cat != ItemCategory.ARMOR && cat != ItemCategory.MELEE) {
            player.sendMessage("§dExclusive Reforge chỉ áp dụng cho §lGiáp§d và §lVũ khí cận chiến§d!");
            return;
        }

        ReforgeTier tier = resolveTier(item, pdc);
        if (tier == ReforgeTier.NONE) {
            player.sendMessage("§cChỉ vật phẩm COMMON, UNCOMMON, RARE, EPIC, LEGENDARY mới có thể trùng đúc!");
            return;
        }

        if (econ == null || econ.getBalance(player) < EXCLUSIVE_COST) {
            player.sendMessage(String.format("§cBạn không có đủ %,d Coins!", EXCLUSIVE_COST));
            return;
        }

        econ.withdrawPlayer(player, EXCLUSIVE_COST);
        player.playSound(player.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1.0f, 0.8f);
        
        fragment.setAmount(fragment.getAmount() - 1);

        String fragmentTierStr = fPdc.has(new NamespacedKey(plugin, "cwe_fragment_tier"), PersistentDataType.STRING) 
                ? fPdc.get(new NamespacedKey(plugin, "cwe_fragment_tier"), PersistentDataType.STRING) 
                : "RARE";
                
        String prefix = rollTieredExclusivePrefix(cat, fragmentTierStr);
        
        if ("MYTHIC".equals(fragmentTierStr)) {
            tier = ReforgeTier.MYTHIC;
            pdc.set(new NamespacedKey(plugin, "cwe_tier"), PersistentDataType.STRING, "MYTHIC");
            pdc.set(new NamespacedKey(plugin, ItemStatsGUI.KEY_RARITY), PersistentDataType.STRING, "MYTHIC");
            String dName = meta.getDisplayName();
            String cleanName = org.bukkit.ChatColor.stripColor(dName);
            // Bỏ đi prefix cũ nếu có, để applyReforge thêm vào
            if (cleanName.contains(" ")) {
                cleanName = cleanName.substring(cleanName.indexOf(" ") + 1);
            }
            meta.setDisplayName("§d§l" + cleanName);
        }
        
        applyReforge(item, meta, pdc, prefix, tier, cat, true);
        
        if ("MYTHIC".equals(fragmentTierStr)) {
            // ApplyReforge sẽ set prefix, ta cần đảm bảo màu là màu Hồng
            String newName = org.bukkit.ChatColor.stripColor(meta.getDisplayName());
            meta.setDisplayName("§d§l" + newName);
            item.setItemMeta(meta);
        }
    }

    private String rollTieredExclusivePrefix(ItemCategory cat, String tier) {
        Random r = new Random();
        if ("MYTHIC".equals(tier)) {
            if (cat == ItemCategory.MELEE || cat == ItemCategory.RANGED) return "Cosmic";
            return "Divine";
        } else if ("EPIC".equals(tier) || "LEGENDARY".equals(tier)) {
            if (cat == ItemCategory.MELEE) {
                String[] arr = {"Demonic", "Fabled", "Withered"};
                return arr[r.nextInt(arr.length)];
            } else if (cat == ItemCategory.RANGED) {
                String[] arr = {"Precise", "Spiritual", "Headstrong"};
                return arr[r.nextInt(arr.length)];
            }
            String[] arr = {"Aegis", "Necrotic", "Giant"};
            return arr[r.nextInt(arr.length)];
        } else {
            if (cat == ItemCategory.MELEE) {
                String[] arr = {"Fabled", "Withered"};
                return arr[r.nextInt(arr.length)];
            } else if (cat == ItemCategory.RANGED) {
                String[] arr = {"Precise", "Spiritual"};
                return arr[r.nextInt(arr.length)];
            }
            String[] arr = {"Reinforced", "Ancient"};
            return arr[r.nextInt(arr.length)];
        }
    }

    // ===========================================================================
    // HELPERS
    // ===========================================================================

    /**
     * Phân giải ReforgeTier từ PDC hoặc fallback RARE cho đồ Vanilla.
     * Trả về NONE nếu item không đủ điều kiện.
     */
    private ReforgeTier resolveTier(ItemStack item, PersistentDataContainer pdc) {
        NamespacedKey rarityKey = new NamespacedKey(plugin, ItemStatsGUI.KEY_RARITY);
        if (pdc.has(rarityKey, PersistentDataType.STRING)) {
            String rarityStr = pdc.get(rarityKey, PersistentDataType.STRING);
            try {
                ReforgeTier t = ReforgeTier.valueOf(rarityStr);
                if (t != ReforgeTier.NONE && t != ReforgeTier.MYTHIC) return t;
                return ReforgeTier.NONE;
            } catch (Exception e) {
                return ReforgeTier.NONE;
            }
        }

        // Nếu hoàn toàn chưa có Rarity (chưa được VanillaItemUpdater chạm vào)
        String name = item.getType().name();
        boolean isEquip = name.contains("SWORD") || name.contains("AXE") || name.contains("BOW") ||
                          name.contains("HELMET") || name.contains("CHESTPLATE") ||
                          name.contains("LEGGINGS") || name.contains("BOOTS") ||
                          name.contains("PICKAXE") || name.contains("HOE") || name.contains("SHOVEL") ||
                          item.getType() == Material.STICK || item.getType() == Material.BLAZE_ROD;
        return isEquip ? ReforgeTier.COMMON : ReforgeTier.NONE;
    }

    private ItemCategory getCategory(ItemStack item, PersistentDataContainer pdc) {
        String name = item.getType().name();
        if (name.contains("BOW") || name.contains("CROSSBOW")) return ItemCategory.RANGED;
        if (name.contains("HELMET") || name.contains("CHESTPLATE") ||
            name.contains("LEGGINGS") || name.contains("BOOTS")) return ItemCategory.ARMOR;
        if (name.contains("PICKAXE") || name.contains("HOE") ||
            name.contains("SPADE") || name.contains("SHOVEL")) return ItemCategory.TOOLS;
        return ItemCategory.MELEE;
    }

    private String rollNormalPrefix(ItemCategory cat) {
        Map<String, ?> pool = REFORGE_MAP.get(cat);
        if (pool == null || pool.isEmpty()) return "";
        List<String> keys = new ArrayList<>(pool.keySet());
        return keys.get(random.nextInt(keys.size()));
    }

    private String rollExclusivePrefix(ItemCategory cat) {
        Map<String, ?> pool = EXCLUSIVE_MAP.get(cat);
        if (pool == null || pool.isEmpty()) return "";
        List<String> keys = new ArrayList<>(pool.keySet());
        return keys.get(random.nextInt(keys.size()));
    }

    // ===========================================================================
    // APPLY REFORGE — Xây dựng lại tên + lore
    // ===========================================================================

    private void applyReforge(ItemStack item, ItemMeta meta, PersistentDataContainer pdc,
                               String prefix, ReforgeTier tier, ItemCategory cat, boolean exclusive) {
        // ── Tên gốc ──────────────────────────────────────────────────────────────
        NamespacedKey ogNameKey   = new NamespacedKey(plugin, "cwe_original_name");
        NamespacedKey reforgeKey  = new NamespacedKey(plugin, "cwe_reforge");
        NamespacedKey exclusiveKey= new NamespacedKey(plugin, "cwe_reforge_exclusive");

        String originalName;
        if (pdc.has(ogNameKey, PersistentDataType.STRING)) {
            originalName = pdc.get(ogNameKey, PersistentDataType.STRING);
        } else {
            // Vanilla item có thể không có display name — dùng type name
            originalName = meta.hasDisplayName() ? meta.getDisplayName()
                    : ChatColor.WHITE + formatVanillaName(item.getType().name());
            pdc.set(ogNameKey, PersistentDataType.STRING, originalName);
        }

        // ── Xử lý xoá reforge ────────────────────────────────────────────────────
        if (prefix == null || prefix.isEmpty()) {
            meta.setDisplayName(originalName);
            pdc.remove(reforgeKey);
            pdc.remove(ogNameKey);
            pdc.remove(exclusiveKey);
            meta.setLore(Collections.emptyList());
            item.setItemMeta(meta);
            return;
        }

        // ── Gán tên mới = Màu + Prefix + Tên gốc (không màu) ────────────────────
        String[] colorSplit = splitColorPrefix(originalName);
        String color   = colorSplit[0];
        String rawName = colorSplit[1];
        meta.setDisplayName(color + prefix + " " + rawName);

        // ── Lưu PDC ──────────────────────────────────────────────────────────────
        pdc.set(reforgeKey, PersistentDataType.STRING, prefix);
        if (exclusive) pdc.set(exclusiveKey, PersistentDataType.INTEGER, 1);
        else           pdc.remove(exclusiveKey);

        // ── Rarity & Flags ───────────────────────────────────────────────────────
        boolean isCustomItem = pdc.has(new NamespacedKey(plugin, ItemStatsGUI.KEY_HAS_STATS), PersistentDataType.INTEGER);
        boolean isWeapon = isCustomItem
                ? (pdc.has(new NamespacedKey(plugin, "cwe_is_weapon"), PersistentDataType.INTEGER)
                   && pdc.get(new NamespacedKey(plugin, "cwe_is_weapon"), PersistentDataType.INTEGER) == 1)
                : (cat == ItemCategory.MELEE || cat == ItemCategory.RANGED);

        ItemStatsGUI.Rarity rarityEnum = ItemStatsGUI.Rarity.NONE;
        try { rarityEnum = ItemStatsGUI.Rarity.valueOf(tier.name()); } catch (Exception ignored) {}

        // ── Base stats (0 nếu Vanilla) ───────────────────────────────────────────
        double[] base = new double[7];
        if (isCustomItem) {
            base[0] = getD(pdc, ItemStatsGUI.KEY_STRENGTH);
            base[1] = getD(pdc, ItemStatsGUI.KEY_CRIT_CHANCE);
            base[2] = getD(pdc, ItemStatsGUI.KEY_CRIT_DAMAGE);
            base[3] = getD(pdc, ItemStatsGUI.KEY_HEALTH);
            base[4] = getD(pdc, ItemStatsGUI.KEY_DEFENSE);
            base[5] = getD(pdc, ItemStatsGUI.KEY_INTELLIGENCE);
            base[6] = getD(pdc, ItemStatsGUI.KEY_SPEED);
        }

        NamespacedKey dmgKey    = new NamespacedKey(plugin, "stat_damage");
        NamespacedKey dmgKeyAlt = new NamespacedKey(plugin, "cwe_damage");
        double damage = pdc.has(dmgKey, PersistentDataType.DOUBLE)
                ? pdc.getOrDefault(dmgKey, PersistentDataType.DOUBLE, 0.0)
                : (pdc.has(dmgKeyAlt, PersistentDataType.DOUBLE)
                    ? pdc.getOrDefault(dmgKeyAlt, PersistentDataType.DOUBLE, 0.0) : 0.0);

        String bTitle = getString(pdc, ItemStatsGUI.KEY_SETBONUS_TITLE, "");
        String bD1    = getString(pdc, ItemStatsGUI.KEY_SETBONUS_DESC1, "");
        String bD2    = getString(pdc, ItemStatsGUI.KEY_SETBONUS_DESC2, "");

        // ── Reforge bonus ────────────────────────────────────────────────────────
        ReforgeStat bonus = exclusive
                ? getExclusiveReforgeStat(prefix, cat, tier)
                : getReforgeStat(prefix, cat, tier);

        double bonusAtk = 0;
        if (bonus != null) {
            base[0] += bonus.strength;
            base[1] += bonus.critChance;
            base[2] += bonus.critDamage;
            base[3] += bonus.health;
            base[4] += bonus.defense;
            base[5] += bonus.intelligence;
            base[6] += bonus.speed;
            bonusAtk  = bonus.attackSpeed;
        }

        // ── Thu thập enchant lines (giữ lại) ────────────────────────────────────
        List<String> existingLore = meta.hasLore() ? meta.getLore() : new ArrayList<>();
        List<String> enchantLines = new ArrayList<>();
        for (String line : existingLore) {
            if (isEnchantLoreLine(line)) enchantLines.add(line);
        }

        // ── Xây dựng lore mới ────────────────────────────────────────────────────
        List<String> newLore = new ArrayList<>();
        if (isWeapon) {
            if (damage > 0) newLore.add(String.format("§7Damage: §c+%.0f", damage));
            addStat(newLore, "Strength",     "§c", base[0], bonus != null ? bonus.strength    : 0, prefix, false);
            addStat(newLore, "Attack Speed", "§e", bonusAtk, bonusAtk,                              prefix, true);
            if (damage > 0 || base[0] > 0 || bonusAtk > 0) newLore.add("§f");

            addStat(newLore, "Crit Chance",  "§9", base[1], bonus != null ? bonus.critChance  : 0, prefix, true);
            addStat(newLore, "Crit Damage",  "§5", base[2], bonus != null ? bonus.critDamage  : 0, prefix, true);
            addStat(newLore, "Intelligence", "§a", base[5], bonus != null ? bonus.intelligence: 0, prefix, false);
            if (base[1] > 0 || base[2] > 0 || base[5] > 0) newLore.add("§f");

            // Withered special note
            if (bonus != null && bonus.levelMultiplier > 0) {
                newLore.add(String.format("§8Withered: §7+%.1fx Level", bonus.levelMultiplier));
            }

            if (!bTitle.isEmpty()) {
                String clickType = getString(pdc, ItemStatsGUI.KEY_ABILITY_CLICK, "RIGHT CLICK");
                newLore.add("§6Item Ability: " + bTitle + " §e§l[" + clickType + "]");
                if (!bD1.isEmpty()) newLore.add("§7" + bD1);
                if (!bD2.isEmpty()) newLore.add("§7" + bD2);

                org.bukkit.configuration.file.FileConfiguration config = CustomWeaponEngine.getInstance().getConfig();
                String weaponId = getWeaponIdFromDisplayName(originalName);
                if (weaponId != null) {
                    double mana = config.getDouble("weapons." + weaponId + ".mana-cost", 0);
                    int cd  = config.getInt("weapons." + weaponId + ".cooldown", 0);
                    if (mana > 0) newLore.add("§9Mana Cost: §3" + (int) mana);
                    if (cd   > 0) newLore.add("§9Cooldown: §a" + cd + "s");
                }
                newLore.add("§f");
            }
        } else {
            // ARMOR
            addStat(newLore, "Health",       "§c", base[3], bonus != null ? bonus.health       : 0, prefix, false);
            addStat(newLore, "Defense",      "§a", base[4], bonus != null ? bonus.defense      : 0, prefix, false);
            addStat(newLore, "Strength",     "§c", base[0], bonus != null ? bonus.strength     : 0, prefix, false);
            if (base[3] > 0 || base[4] > 0 || base[0] > 0) newLore.add("§f");

            addStat(newLore, "Crit Chance",  "§9", base[1], bonus != null ? bonus.critChance   : 0, prefix, true);
            addStat(newLore, "Crit Damage",  "§5", base[2], bonus != null ? bonus.critDamage   : 0, prefix, true);
            addStat(newLore, "Speed",        "§f", base[6], bonus != null ? bonus.speed        : 0, prefix, false);
            addStat(newLore, "Intelligence", "§a", base[5], bonus != null ? bonus.intelligence : 0, prefix, false);
            if (base[1] > 0 || base[2] > 0 || base[6] > 0 || base[5] > 0) newLore.add("§f");

            if (!bTitle.isEmpty()) {
                newLore.add("§6Full Set Bonus: " + bTitle);
                if (!bD1.isEmpty()) newLore.add("§7" + bD1);
                if (!bD2.isEmpty()) newLore.add("§7" + bD2);
                newLore.add("§f");
            }
        }

        if (!enchantLines.isEmpty()) {
            newLore.addAll(enchantLines);
            newLore.add("§f");
        }

        // Dòng rarity cuối
        if (rarityEnum != ItemStatsGUI.Rarity.NONE) {
            String rarityLabel = rarityEnum.color + "§l" + rarityEnum.name();
            if (isWeapon) newLore.add(rarityLabel + " " + ItemStatsGUI.getWeaponTypeName(item));
            else          newLore.add(rarityLabel + " " + getArmorOrVanillaTypeName(item));
        } else {
            // Vanilla item fallback: luôn hiển thị RARE + loại item
            newLore.add("§f§lCOMMON " + resolveVanillaTypeName(item));
        }

        meta.setLore(newLore);
        item.setItemMeta(meta);
    }

    // ===========================================================================
    // LORE HELPERS
    // ===========================================================================

    private void addStat(List<String> lore, String label, String colorCode,
                         double totalValue, double bonusValue, String prefix, boolean isPercent) {
        if (totalValue > 0) {
            String pct = isPercent ? "%" : "";
            String line = String.format("§7%s: %s+%.0f%s", label, colorCode, totalValue, pct);
            if (bonusValue > 0 && prefix != null && !prefix.isEmpty()) {
                line += String.format(" §8(%s +%.0f%s)", prefix, bonusValue, pct);
            }
            lore.add(line);
        }
    }

    private String getArmorOrVanillaTypeName(ItemStack item) {
        // Thử custom method trước, fallback về vanilla
        String custom = ItemStatsGUI.getArmorTypeName(item);
        return (custom != null && !custom.isEmpty()) ? custom : resolveVanillaTypeName(item);
    }

    private String resolveVanillaTypeName(ItemStack item) {
        String name = item.getType().name();
        if (name.contains("SWORD"))      return "SWORD";
        if (name.contains("AXE"))        return "AXE";
        if (name.contains("BOW"))        return "BOW";
        if (name.contains("CROSSBOW"))   return "CROSSBOW";
        if (name.contains("PICKAXE"))    return "PICKAXE";
        if (name.contains("HOE"))        return "HOE";
        if (name.contains("SHOVEL") || name.contains("SPADE")) return "SHOVEL";
        if (name.contains("HELMET"))     return "HELMET";
        if (name.contains("CHESTPLATE")) return "CHESTPLATE";
        if (name.contains("LEGGINGS"))   return "LEGGINGS";
        if (name.contains("BOOTS"))      return "BOOTS";
        return "ITEM";
    }

    /** Format DIAMOND_SWORD → Diamond Sword */
    private String formatVanillaName(String typeName) {
        StringBuilder sb = new StringBuilder();
        for (String part : typeName.split("_")) {
            if (!part.isEmpty()) {
                sb.append(Character.toUpperCase(part.charAt(0)))
                  .append(part.substring(1).toLowerCase())
                  .append(" ");
            }
        }
        return sb.toString().trim();
    }

    private String[] splitColorPrefix(String name) {
        StringBuilder color = new StringBuilder();
        int i = 0;
        while (i < name.length()) {
            if (name.charAt(i) == '§' && i + 1 < name.length()) {
                color.append(name.charAt(i)).append(name.charAt(i + 1));
                i += 2;
            } else break;
        }
        return new String[]{color.toString(), name.substring(i)};
    }

    private boolean isEnchantLoreLine(String line) {
        String noColor = org.bukkit.ChatColor.stripColor(line).toLowerCase();
        return noColor.contains("protection")    || noColor.contains("counter strike")  ||
               noColor.contains("feather falling")|| noColor.contains("growth")         ||
               noColor.contains("big brain")     || noColor.contains("smarty pants")    ||
               noColor.contains("sugar rush")    || noColor.contains("rejuvenate")      ||
               noColor.contains("thorns")        || noColor.contains("sharpness")       ||
               noColor.contains("thunder strike")|| noColor.contains("vampirism")       ||
               noColor.contains("telepathy");
    }

    // ===========================================================================
    // PDC HELPERS
    // ===========================================================================

    private double getD(PersistentDataContainer pdc, String key) {
        NamespacedKey nk = new NamespacedKey(plugin, key);
        return pdc.has(nk, PersistentDataType.DOUBLE) ? pdc.get(nk, PersistentDataType.DOUBLE) : 0.0;
    }

    private String getString(PersistentDataContainer pdc, String key, String def) {
        NamespacedKey nk = new NamespacedKey(plugin, key);
        return pdc.has(nk, PersistentDataType.STRING) ? pdc.get(nk, PersistentDataType.STRING) : def;
    }

    private String getWeaponIdFromDisplayName(String displayName) {
        if (displayName == null) return null;
        String clean = org.bukkit.ChatColor.stripColor(displayName);
        if (clean.equalsIgnoreCase("Heavenly Sword"))        return "heavenly_sword";
        if (clean.equalsIgnoreCase("Shadow Assassin Blade")) return "shadow_assassin_blade";
        if (clean.equalsIgnoreCase("Berserk Axe"))           return "berserk_axe";
        if (clean.equalsIgnoreCase("Astral Shepherd Wand"))  return "astral_shepherd_wand";
        if (clean.equalsIgnoreCase("Cosmic Void Sword"))     return "cosmic_void_sword";
        return null;
    }

    // ── Expose KEY_ABILITY_CLICK nếu ItemStatsGUI chưa public ───────────────────
    private static final String KEY_ABILITY_CLICK = "stat_ability_click";

    // ===========================================================================
    // ITEM FACTORY
    // ===========================================================================

    private ItemStack createItem(Material mat, String name, String... lore) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            if (lore.length > 0) meta.setLore(Arrays.asList(lore));
            item.setItemMeta(meta);
        }
        return item;
    }
}


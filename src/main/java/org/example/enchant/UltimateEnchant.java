package org.example.enchant;

import org.bukkit.Material;

public enum UltimateEnchant {
    // Weapons
    ULTIMATE_WISE("Ultimate Wise", 5, ItemGroup.WEAPON),
    SOUL_EATER("Soul Eater", 5, ItemGroup.WEAPON),
    ONE_FOR_ALL("One For All", 1, ItemGroup.WEAPON),
    SWARM("Swarm", 5, ItemGroup.WEAPON),
    COMBO("Combo", 5, ItemGroup.WEAPON),
    INFERNO("Inferno", 5, ItemGroup.WEAPON),
    FATAL_TEMPO("Fatal Tempo", 5, ItemGroup.WEAPON),
    DUPLEX("Duplex", 5, ItemGroup.BOW),
    REND("Rend", 5, ItemGroup.BOW),

    // Armor
    LEGION("Legion", 5, ItemGroup.ARMOR),
    LAST_STAND("Last Stand", 5, ItemGroup.ARMOR),
    WISDOM("Wisdom", 5, ItemGroup.ARMOR),
    BANK("Bank", 5, ItemGroup.ARMOR),
    NO_PAIN_NO_GAIN("No Pain No Gain", 5, ItemGroup.ARMOR),
    HABANERO_TACTICS("Habanero Tactics", 5, ItemGroup.ARMOR),
    BOBBIN_TIME("Bobbin' Time", 5, ItemGroup.ARMOR),

    // Tool
    FLASH("Flash", 5, ItemGroup.TOOL);

    private final String displayName;
    private final int maxLevel;
    private final ItemGroup group;

    UltimateEnchant(String displayName, int maxLevel, ItemGroup group) {
        this.displayName = displayName;
        this.maxLevel = maxLevel;
        this.group = group;
    }

    public String getDisplayName() { return displayName; }
    public int getMaxLevel() { return maxLevel; }
    public ItemGroup getGroup() { return group; }
    public String getId() { return name().toLowerCase(); }

    public enum ItemGroup {
        WEAPON, BOW, ARMOR, TOOL;

        public boolean canApply(Material type) {
            String name = type.name();
            switch (this) {
                case WEAPON: return name.endsWith("_SWORD") || name.endsWith("_AXE") || name.endsWith("_BOW");
                case BOW: return name.endsWith("_BOW") || name.endsWith("CROSSBOW");
                case ARMOR: return name.endsWith("_HELMET") || name.endsWith("_CHESTPLATE") || name.endsWith("_LEGGINGS") || name.endsWith("_BOOTS");
                case TOOL: return name.endsWith("_PICKAXE") || name.endsWith("_SHOVEL") || name.endsWith("_HOE") || name.endsWith("_AXE") || name.equals("FISHING_ROD");
            }
            return false;
        }
    }
}

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 */
package org.example.enchant;

import org.bukkit.Material;

public enum UltimateEnchant {
    ULTIMATE_WISE("Ultimate Wise", 5, ItemGroup.WEAPON, "Gi\u1ea3m 10% Mana ti\u00eau hao khi d\u00f9ng k\u1ef9 n\u0103ng m\u1ed7i c\u1ea5p (T\u1ed1i \u0111a -50%)."),
    SOUL_EATER("Soul Eater", 5, ItemGroup.WEAPON, "Khi h\u1ea1 g\u1ee5c qu\u00e1i, t\u00edch l\u0169y Soul. M\u1ed7i Soul c\u1ed9ng +1 S\u00e1t th\u01b0\u01a1ng cho \u0111\u00f2n ti\u1ebfp theo."),
    ONE_FOR_ALL("One for All", 1, ItemGroup.WEAPON, "T\u0103ng +210% S\u00e1t th\u01b0\u01a1ng c\u01a1 b\u1ea3n (nh\u00e2n 3.1x)."),
    SWARM("Swarm", 5, ItemGroup.WEAPON, "T\u0103ng +2% S\u00e1t th\u01b0\u01a1ng cho m\u1ed7i th\u1ef1c th\u1ec3 xung quanh/c\u1ea5p."),
    COMBO("Combo", 5, ItemGroup.WEAPON, "T\u0103ng +10% S\u00e1t th\u01b0\u01a1ng m\u1ed7i c\u1ea5p."),
    INFERNO("Inferno", 5, ItemGroup.WEAPON, "10%/c\u1ea5p c\u01a1 h\u1ed9i thi\u00eau ch\u00e1y \u0111\u1ecbch v\u00e0 c\u1ed9ng +50 S\u00e1t th\u01b0\u01a1ng/c\u1ea5p."),
    FATAL_TEMPO("Fatal Tempo", 5, ItemGroup.WEAPON, "M\u1ed7i \u0111\u00f2n \u0111\u00e1nh tr\u00fang li\u00ean ti\u1ebfp c\u1ed9ng d\u1ed3n +20% T\u1ed1c \u0111\u1ed9 \u0111\u00e1nh (t\u1ed1i \u0111a 5 stack, bay h\u1ebft sau 3s ng\u1eebng t\u1ea5n c\u00f4ng)."),
    DUPLEX("Duplex", 5, ItemGroup.BOW, "M\u1ed7i ph\u00e1t b\u1eafn th\u00eam 1 m\u0169i t\u00ean ph\u1ee5 (g\u00e2y 60% s\u00e1t th\u01b0\u01a1ng). Kh\u00f4ng t\u1ed1n th\u00eam m\u0169i t\u00ean."),
    REND("Rend", 5, ItemGroup.BOW, "M\u0169i t\u00ean g\u0103m v\u00e0o qu\u00e1i, Chu\u1ed9t Tr\u00e1i gom t\u1ea5t c\u1ea3 l\u1ea1i g\u00e2y s\u00e1t th\u01b0\u01a1ng d\u1ed3n theo s\u1ed1 m\u0169i t\u00ean \u00d7 enchant level."),
    LEGION("Legion", 5, ItemGroup.ARMOR, "M\u1ed7i ng\u01b0\u1eddi ch\u01a1i trong b\u00e1n k\u00ednh 30 block c\u1ed9ng +1% t\u1ea5t c\u1ea3 ch\u1ec9 s\u1ed1 c\u1ee7a b\u1ea1n m\u1ed7i c\u1ea5p."),
    LAST_STAND("Last Stand", 5, ItemGroup.ARMOR, "Khi m\u00e1u \u226450%, gi\u1ea3m s\u00e1t th\u01b0\u01a1ng nh\u1eadn v\u00e0o 5%/c\u1ea5p (t\u1ed1i \u0111a -90%)."),
    WISDOM("Wisdom", 5, ItemGroup.ARMOR, "M\u1ed7i qu\u00e1i v\u1eadt g\u1ea7n b\u00e1n k\u00ednh 10 block c\u1ed9ng +1 Intelligence. C\u1eadp nh\u1eadt m\u1ed7i 2 gi\u00e2y."),
    BANK("Bank", 5, ItemGroup.ARMOR, "Khi ch\u1ebft, gi\u1ea3m 10%/c\u1ea5p s\u1ed1 ti\u1ec1n b\u1ecb m\u1ea5t (c\u1ed9ng d\u1ed3n t\u1ea5t c\u1ea3 m\u1ea3nh gi\u00e1p)."),
    NO_PAIN_NO_GAIN("No Pain No Gain", 5, ItemGroup.ARMOR, "Khi nh\u1eadn \u22655 s\u00e1t th\u01b0\u01a1ng, h\u1ed3i l\u1ea1i 10 HP/c\u1ea5p (Cooldown 2 gi\u00e2y)."),
    HABANERO_TACTICS("Habanero Tactics", 5, ItemGroup.ARMOR, "Ph\u1ea3n l\u1ea1i 10%/c\u1ea5p s\u00e1t th\u01b0\u01a1ng nh\u1eadn v\u00e0o cho k\u1ebb t\u1ea5n c\u00f4ng."),
    BOBBIN_TIME("Bobbin Time", 5, ItemGroup.ARMOR, "Khi m\u00e1u <30%, mi\u1ec5n Poison + gi\u1ea3m 15% cooldown k\u1ef9 n\u0103ng trong 6 gi\u00e2y."),
    FLASH("Flash", 5, ItemGroup.TOOL, "M\u1ed7i l\u1ea7n khai th\u00e1c: 10%/c\u1ea5p c\u01a1 h\u1ed9i t\u1ef1 \u0111\u1ed9ng th\u00eam 1 item t\u01b0\u01a1ng t\u1ef1 v\u00e0o t\u00fai \u0111\u1ed3.");

    private final String displayName;
    private final int maxLevel;
    private final ItemGroup group;
    private final String description;

    private UltimateEnchant(String displayName, int maxLevel, ItemGroup group, String description) {
        this.displayName = displayName;
        this.maxLevel = maxLevel;
        this.group = group;
        this.description = description;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public int getMaxLevel() {
        return this.maxLevel;
    }

    public ItemGroup getGroup() {
        return this.group;
    }

    public String getDescription() {
        return this.description;
    }

    public String getId() {
        return this.name().toLowerCase();
    }

    public static enum ItemGroup {
        WEAPON,
        BOW,
        ARMOR,
        TOOL;


        public boolean canApply(Material type) {
            String name = type.name();
            switch (this.ordinal()) {
                case 0: {
                    return name.endsWith("_SWORD") || name.endsWith("_AXE") || name.equals("BOW") || name.equals("CROSSBOW");
                }
                case 1: {
                    return name.equals("BOW") || name.equals("CROSSBOW");
                }
                case 2: {
                    return name.endsWith("_HELMET") || name.endsWith("_CHESTPLATE") || name.endsWith("_LEGGINGS") || name.endsWith("_BOOTS");
                }
                case 3: {
                    return name.endsWith("_PICKAXE") || name.endsWith("_SHOVEL") || name.endsWith("_HOE") || name.endsWith("_AXE") || name.equals("FISHING_ROD");
                }
            }
            return false;
        }
    }
}


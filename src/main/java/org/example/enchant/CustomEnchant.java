/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 */
package org.example.enchant;

import org.bukkit.Material;

public enum CustomEnchant {
    SHARPNESS("sharpness", "Sharpness", 8, ItemGroup.SWORD, "C\u01b0\u1eddng h\u00f3a v\u1ea1n v\u1eadt, gia t\u0103ng \u0111\u00e1ng k\u1ec3 s\u00e1t th\u01b0\u01a1ng v\u1eadt l\u00fd th\u00f4."),
    CRITICAL("critical", "Critical", 7, ItemGroup.SWORD, "\u0110\u1ed9t ph\u00e1 gi\u1edbi h\u1ea1n, gia t\u0103ng s\u00e1t th\u01b0\u01a1ng v\u00e0 t\u1ef7 l\u1ec7 \u0111\u00f2n \u0111\u00e1nh ch\u00ed m\u1ea1ng."),
    FIRST_STRIKE("first_strike", "First Strike", 5, ItemGroup.SWORD, "Ph\u1ee7 \u0111\u1ea7u k\u1ebb \u0111\u1ecbch b\u1eb1ng l\u01b0\u1ee3ng s\u00e1t th\u01b0\u01a1ng kh\u1ed5ng l\u1ed3 trong \u0111\u00f2n \u0111\u00e1nh \u0111\u1ea7u ti\u00ean."),
    GIANT_KILLER("giant_killer", "Giant Killer", 7, ItemGroup.SWORD, "K\u1ebb th\u00f9 c\u00e0ng nhi\u1ec1u m\u00e1u, s\u00e1t th\u01b0\u01a1ng gi\u00e1ng xu\u1ed1ng c\u00e0ng t\u00e0n b\u1ea1o."),
    EXECUTE("execute", "Execute", 6, ItemGroup.SWORD, "Nh\u1eafm v\u00e0o nh\u01b0\u1ee3c \u0111i\u1ec3m, s\u00e1t th\u01b0\u01a1ng t\u1ef7 l\u1ec7 thu\u1eadn v\u1edbi l\u01b0\u1ee3ng sinh l\u1ef1c \u0111\u00e3 m\u1ea5t c\u1ee7a m\u1ee5c ti\u00eau."),
    LETHALITY("lethality", "Lethality", 6, ItemGroup.SWORD, "Xuy\u00ean th\u1ee7ng ph\u00f2ng ng\u1ef1, t\u01b0\u1edbc \u0111o\u1ea1t d\u1ea7n \u0111i\u1ec3m gi\u00e1p c\u1ee7a m\u1ee5c ti\u00eau sau m\u1ed7i \u0111\u00f2n \u0111\u00e1nh."),
    VAMPIRISM("vampirism", "Vampirism", 6, ItemGroup.SWORD, "Nu\u1ed1t ch\u1eedng sinh kh\u00ed k\u1ebb \u0111\u1ecbch \u0111\u1ec3 h\u1ed3i ph\u1ee5c l\u1ea1i m\u00e1u sau khi k\u1ebft li\u1ec5u."),
    LIFE_STEAL("life_steal", "Life Steal", 5, ItemGroup.SWORD, "R\u00fat l\u1ea5y sinh m\u1ec7nh, chuy\u1ec3n h\u00f3a tr\u1ef1c ti\u1ebfp m\u1ed9t ph\u1ea7n s\u00e1t th\u01b0\u01a1ng g\u00e2y ra th\u00e0nh m\u00e1u."),
    DRAGON_HUNTER("dragon_hunter", "Dragon Hunter", 5, ItemGroup.SWORD, "S\u00e1t kh\u00ed t\u1ecfa ra, mang s\u1ee9c m\u1ea1nh kh\u1eafc ch\u1ebf ch\u00ed t\u1eed chuy\u00ean tr\u1ecb Long T\u1ed9c."),
    LOOTING("looting", "Looting", 3, ItemGroup.SWORD, "Khai m\u1edf t\u00e0i v\u1eadn, t\u0103ng \u0111\u00e1ng k\u1ec3 l\u01b0\u1ee3ng chi\u1ebfn l\u1ee3i ph\u1ea9m r\u1edbt ra t\u1eeb qu\u00e1i v\u1eadt."),
    FIRE_ASPECT("fire_aspect", "Fire Aspect", 2, ItemGroup.SWORD, "T\u1ea9m h\u1ecfa di\u1ec7m v\u00e0o l\u01b0\u1ee1i ki\u1ebfm, thi\u00eau \u0111\u1ed1t k\u1ebb th\u00f9 th\u00e0nh tro b\u1ee5i."),
    POWER("power", "Power", 7, ItemGroup.BOW, "K\u00e9o c\u0103ng d\u00e2y cung, gia t\u0103ng v\u1ea1n ph\u1ea7n uy l\u1ef1c cho t\u1eebng m\u0169i t\u00ean b\u1eafn ra."),
    OVERLOAD("overload", "Overload", 5, ItemGroup.BOW, "\u00c9p xung gi\u1edbi h\u1ea1n, trao cho m\u0169i t\u00ean kh\u1ea3 n\u0103ng b\u1ea1o k\u00edch v\u1edbi s\u1ee9c c\u00f4ng ph\u00e1 h\u1ee7y di\u1ec7t."),
    SNIPER("sniper", "Sniper", 5, ItemGroup.BOW, "L\u1ef1c b\u1eafn x\u00e9 gi\u00f3, s\u00e1t th\u01b0\u01a1ng ti\u1ec5n bay c\u00e0ng xa uy l\u1ef1c c\u00e0ng kh\u1ee7ng khi\u1ebfp."),
    FLAME("flame", "Flame", 1, ItemGroup.BOW, "Gia tr\u00ec h\u1ecfa \u1ea5n, bi\u1ebfn m\u0169i t\u00ean th\u00e0nh ng\u1ecdn l\u1eeda t\u1eed th\u1ea7n thi\u00eau \u0111\u1ed1t m\u1ee5c ti\u00eau."),
    IMPALING("impaling", "Impaling", 5, ItemGroup.TRIDENT, "Ng\u0169 l\u00f4i oanh \u0111\u1ec9nh, \u0111\u00e2m xuy\u00ean l\u1edbp v\u1ea3y c\u1ee9ng c\u00e1p c\u1ee7a qu\u00e1i v\u1eadt d\u01b0\u1edbi \u0111\u00e1y bi\u1ec3n s\u00e2u."),
    LOYALTY("loyalty", "Loyalty", 3, ItemGroup.TRIDENT, "G\u1eafn k\u1ebft huy\u1ebft m\u1ea1ch, \u0110inh Ba sau khi ph\u00f3ng ra s\u1ebd t\u1ef1 \u0111\u1ed9ng quay tr\u1edf v\u1ec1 tay ch\u1ee7 nh\u00e2n."),
    RIPTIDE("riptide", "Riptide", 3, ItemGroup.TRIDENT, "M\u01b0\u1ee3n l\u1ef1c phong ba, ph\u00f3ng ng\u01b0\u1eddi bay \u0111i trong m\u01b0a b\u00e3o ho\u1eb7c khi ng\u1eadp m\u00ecnh d\u01b0\u1edbi d\u00f2ng n\u01b0\u1edbc."),
    CHANNELING("channeling", "Channeling", 1, ItemGroup.TRIDENT, "D\u1eabn \u0111\u1ed9ng thi\u00ean l\u00f4i, g\u1ecdi s\u1ea5m s\u00e9t gi\u00e1ng th\u1eb3ng xu\u1ed1ng m\u1ee5c ti\u00eau trong c\u01a1n d\u00f4ng b\u00e3o."),
    DENSITY("density", "Density", 5, ItemGroup.MACE, "Ng\u01b0ng t\u1ee5 kh\u1ed1i l\u01b0\u1ee3ng, r\u01a1i t\u1eeb c\u00e0ng cao s\u1ee9c \u00e9p gi\u00e1ng xu\u1ed1ng c\u00e0ng mang t\u00ednh h\u1ee7y di\u1ec7t."),
    BREACH("breach", "Breach", 4, ItemGroup.MACE, "L\u1ef1c \u0111\u1eadp rung tr\u1eddi, nghi\u1ec1n n\u00e1t v\u00e0 ph\u1edbt l\u1edd ho\u00e0n to\u00e0n l\u1edbp gi\u00e1p ki\u00ean c\u1ed1 c\u1ee7a k\u1ebb th\u00f9."),
    WIND_BURST("wind_burst", "Wind Burst", 3, ItemGroup.MACE, "Kh\u1ed1ng ch\u1ebf lu\u1ed3ng phong, d\u1ed9i ng\u01b0\u1ee3c ng\u01b0\u1eddi l\u00ean kh\u00f4ng trung sau m\u1ed7i c\u00fa n\u1ec7n tr\u00fang \u0111\u00edch."),
    GROWTH("growth", "Growth", 7, ItemGroup.ARMOR, "C\u1ea3i bi\u1ebfn th\u1ec3 ch\u1ea5t, khu\u1ebfch \u0111\u1ea1i gi\u1edbi h\u1ea1n sinh m\u1ec7nh (HP) l\u00ean m\u1ee9c t\u1ed1i \u0111a."),
    PROTECTION("protection", "Protection", 7, ItemGroup.ARMOR, "Ng\u01b0ng t\u1ee5 c\u01b0\u01a1ng kh\u00ed h\u1ed9 th\u1ec3, gia t\u0103ng \u0111\u1ed9 c\u1ee9ng c\u00e1p v\u00e0 c\u1ea3n ph\u00e1 m\u1ecdi s\u00e1t th\u01b0\u01a1ng."),
    THORNS("thorns", "Thorns", 3, ItemGroup.ARMOR, "Th\u00e2n ph\u1ee7 gai nh\u1ecdn, d\u1ed9i ng\u01b0\u1ee3c m\u1ed9t ph\u1ea7n s\u00e1t th\u01b0\u01a1ng l\u1ea1i nh\u1eefng k\u1ebb d\u00e1m t\u1ea5n c\u00f4ng."),
    REJUVENATE("rejuvenate", "Rejuvenate", 5, ItemGroup.ARMOR, "D\u00f2ng m\u00e1u s\u1ee5c s\u00f4i, th\u00fac \u0111\u1ea9y t\u1ed1c \u0111\u1ed9 t\u00e1i t\u1ea1o sinh l\u1ef1c t\u1ef1 nhi\u00ean trong chi\u1ebfn \u0111\u1ea5u."),
    SUGAR_RUSH("sugar_rush", "Sugar Rush", 3, ItemGroup.BOOTS, "Th\u00e2n nh\u1eb9 t\u1ef1a h\u1ed3ng mao, gia t\u1ed1c di chuy\u1ec3n x\u00e9 gi\u00f3 (Ch\u1ec9 \u00e9p v\u00e0o Gi\u00e0y)."),
    FEATHER_FALLING("feather_falling", "Feather Falling", 10, ItemGroup.BOOTS, "Ng\u1ef1 phong phi h\u00e0nh, x\u00f3a nh\u00f2a s\u00e1t th\u01b0\u01a1ng khi r\u01a1i t\u1eeb t\u1ea7ng kh\u00f4ng (Ch\u1ec9 \u00e9p v\u00e0o Gi\u00e0y)."),
    BIG_BRAIN("big_brain", "Big Brain", 5, ItemGroup.HELMET, "Khai s\u00e1ng linh tr\u00ed, gia t\u0103ng l\u01b0\u1ee3ng l\u1edbn N\u0103ng l\u01b0\u1ee3ng Ph\u00e1p thu\u1eadt - Mana (Ch\u1ec9 \u00e9p v\u00e0o M\u0169)."),
    COUNTER_STRIKE("counter_strike", "Counter Strike", 5, ItemGroup.CHESTPLATE, "Khi b\u1ecb t\u1ed5n th\u01b0\u01a1ng, l\u1eadp t\u1ee9c ng\u01b0ng t\u1ee5 c\u01b0\u01a1ng kh\u00ed b\u1ea3o v\u1ec7 th\u00e2n th\u1ec3 (Ch\u1ec9 \u00e9p v\u00e0o \u00c1o)."),
    SMARTY_PANTS("smarty_pants", "Smarty Pants", 5, ItemGroup.LEGGINGS, "\u0110\u1ea3 th\u00f4ng kinh m\u1ea1ch, b\u1ed5 tr\u1ee3 l\u01b0\u1ee3ng l\u1edbn N\u0103ng l\u01b0\u1ee3ng Ph\u00e1p thu\u1eadt - Mana (Ch\u1ec9 \u00e9p v\u00e0o Qu\u1ea7n)."),
    EFFICIENCY("efficiency", "Efficiency", 10, ItemGroup.TOOL, "\u0110\u1ed9ng t\u00e1c nhanh nh\u01b0 thi\u1ec3m \u0111i\u1ec7n, thu th\u1eadp t\u00e0i nguy\u00ean trong ch\u1edbp m\u1eaft."),
    MINING_FORTUNE("mining_fortune", "Fortune", 5, ItemGroup.TOOL, "Kh\u00ed v\u1eadn hanh th\u00f4ng, thu \u0111\u01b0\u1ee3c l\u01b0\u1ee3ng t\u00e0i nguy\u00ean d\u1ed3i d\u00e0o h\u01a1n khi khai ph\u00e1."),
    PRISTINE("pristine", "Pristine", 5, ItemGroup.TOOL, "Ch\u1ea1m tay v\u00e0o \u0111\u00e1 t\u1ea3ng, \u0111\u00e1nh th\u1ee9c x\u00e1c su\u1ea5t r\u01a1i ra \u0110\u00e1 Qu\u00fd Tinh T\u00fa si\u00eau c\u1ea5p hi\u1ebfm."),
    REPLENISH("replenish", "Replenish", 1, ItemGroup.FARMING, "Ban ph\u01b0\u1edbc cho \u0111\u1ea5t r\u1eefa, t\u1ef1 \u0111\u1ed9ng gieo m\u1ea7m h\u1ea1t gi\u1ed1ng sau m\u1ed7i l\u1ea7n thu ho\u1ea1ch."),
    TELEKINESIS("telekinesis", "Telekinesis", 1, ItemGroup.ALL, "Thao t\u00fang kh\u00f4ng gian, \u0111i\u1ec1u khi\u1ec3n chi\u1ebfn l\u1ee3i ph\u1ea9m bay th\u1eb3ng v\u00e0o trong t\u00fai \u0111\u1ed3."),
    MENDING("mending", "Mending", 1, ItemGroup.ALL, "H\u1ea5p th\u1ee5 linh kh\u00ed c\u1ee7a tr\u1eddi \u0111\u1ea5t, chuy\u1ec3n h\u00f3a kinh nghi\u1ec7m th\u00e0nh \u0111\u1ed9 b\u1ec1n trang b\u1ecb.");

    private final String id;
    private final String displayName;
    private final int maxLevel;
    private final ItemGroup itemGroup;
    private final String description;

    private CustomEnchant(String id, String displayName, int maxLevel, ItemGroup itemGroup, String description) {
        this.id = id;
        this.displayName = displayName;
        this.maxLevel = maxLevel;
        this.itemGroup = itemGroup;
        this.description = description;
    }

    public String getId() {
        return this.id;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public int getMaxLevel() {
        return this.maxLevel;
    }

    public ItemGroup getItemGroup() {
        return this.itemGroup;
    }

    public String getDescription() {
        return this.description;
    }

    public static enum ItemGroup {
        SWORD,
        BOW,
        ARMOR,
        HELMET,
        CHESTPLATE,
        LEGGINGS,
        BOOTS,
        TOOL,
        FARMING,
        TRIDENT,
        MACE,
        ALL;


        public boolean canApply(Material material) {
            if (material == Material.BOOK || material == Material.ENCHANTED_BOOK) {
                return true;
            }
            String name = material.name();
            boolean isActualAxe = (name.endsWith("_AXE") || name.equals("AXE")) && !name.contains("PICKAXE");
            switch (this.ordinal()) {
                case 0: {
                    return name.contains("SWORD") || isActualAxe || material == Material.STICK || material == Material.BLAZE_ROD;
                }
                case 1: {
                    return name.equals("BOW") || name.equals("CROSSBOW");
                }
                case 2: {
                    return name.contains("HELMET") || name.contains("CHESTPLATE") || name.contains("LEGGINGS") || name.contains("BOOTS");
                }
                case 3: {
                    return name.contains("HELMET");
                }
                case 4: {
                    return name.contains("CHESTPLATE");
                }
                case 5: {
                    return name.contains("LEGGINGS");
                }
                case 6: {
                    return name.contains("BOOTS");
                }
                case 7: {
                    return name.contains("PICKAXE") || isActualAxe || name.contains("SHOVEL") || name.contains("HOE");
                }
                case 8: {
                    return name.contains("HOE") || isActualAxe;
                }
                case 9: {
                    return material == Material.TRIDENT;
                }
                case 10: {
                    return material == Material.MACE;
                }
                case 11: {
                    return true;
                }
            }
            return false;
        }
    }
}


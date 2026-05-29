package org.example.enchant;

import org.bukkit.Material;

public enum UltimateEnchant {
    // Weapons
    ULTIMATE_WISE("Hạo Thiên Trí", 5, ItemGroup.WEAPON),
    SOUL_EATER("Phệ Hồn", 5, ItemGroup.WEAPON),
    ONE_FOR_ALL("Thiên Hạ Độc Tôn", 1, ItemGroup.WEAPON),
    SWARM("Bách Quỷ Dạ Hành", 5, ItemGroup.WEAPON),
    COMBO("Tật Phong Liên Kích", 5, ItemGroup.WEAPON),
    INFERNO("Luyện Ngục Diệt Thế", 5, ItemGroup.WEAPON),
    FATAL_TEMPO("Cuồng Bạo Nhịp Kích", 5, ItemGroup.WEAPON),
    DUPLEX("Song Long Tiễn", 5, ItemGroup.BOW),
    REND("Liệt Hồn", 5, ItemGroup.BOW),

    // Armor
    LEGION("Vạn Nhân Tướng", 5, ItemGroup.ARMOR),
    LAST_STAND("Tuyệt Cảnh Phùng Sinh", 5, ItemGroup.ARMOR),
    WISDOM("Cửu Thiên Trí Tuệ", 5, ItemGroup.ARMOR),
    BANK("Bảo Khố", 5, ItemGroup.ARMOR),
    NO_PAIN_NO_GAIN("Khổ Tận Cam Lai", 5, ItemGroup.ARMOR),
    HABANERO_TACTICS("Liệt Hỏa Chiến Pháp", 5, ItemGroup.ARMOR),
    BOBBIN_TIME("Ngư Ông Đắc Lợi", 5, ItemGroup.ARMOR),

    // Tool
    FLASH("Thiểm Điện Phiêu Diêu", 5, ItemGroup.TOOL);

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

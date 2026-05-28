package org.example.enchant;

import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
import org.bukkit.Material;

public enum CustomEnchant {
    
    // ⚔️ COMBAT ENCHANTMENTS
    SHARPNESS("sharpness", "Sharpness", 8, ItemGroup.SWORD, "Tăng sát thương vật lý thô."),
    CRITICAL("critical", "Critical", 7, ItemGroup.SWORD, "Tăng tỷ lệ và sát thương chí mạng."),
    FIRST_STRIKE("first_strike", "First Strike", 5, ItemGroup.SWORD, "Tăng mạnh sát thương ở đòn đánh đầu tiên vào quái."),
    GIANT_KILLER("giant_killer", "Giant Killer", 7, ItemGroup.SWORD, "Tăng sát thương dựa trên máu tối đa của quái."),
    EXECUTE("execute", "Execute", 6, ItemGroup.SWORD, "Tăng sát thương dựa trên lượng máu đã mất của quái."),
    LETHALITY("lethality", "Lethality", 6, ItemGroup.SWORD, "Giảm điểm phòng thủ (Defense) của quái vật."),
    VAMPIRISM("vampirism", "Vampirism", 6, ItemGroup.SWORD, "Hồi máu sau khi kết liễu quái."),
    LIFE_STEAL("life_steal", "Life Steal", 5, ItemGroup.SWORD, "Hồi máu dựa trên lượng sát thương thực tế gây ra."),
    DRAGON_HUNTER("dragon_hunter", "Dragon Hunter", 5, ItemGroup.SWORD, "Sát thương chuyên trị Rồng End."),
    // 🔥 VANILLA-STYLE COMBAT (xuất hiện độc lập trong bể Tier II/III Kiếm)
    LOOTING("looting", "Looting", 3, ItemGroup.SWORD, "Tăng số lượng loot rơi khi hạ gục quái vật."),
    FIRE_ASPECT("fire_aspect", "Fire Aspect", 2, ItemGroup.SWORD, "Thiêu đốt mục tiêu khi đánh trúng bằng cận chiến."),

    // 🏹 BOW ENCHANTMENTS
    POWER("power", "Power", 7, ItemGroup.BOW, "Tăng sát thương cho cung."),
    OVERLOAD("overload", "Overload", 5, ItemGroup.BOW, "Cho phép mũi tên có tỷ lệ siêu chí mạng."),
    SNIPER("sniper", "Sniper", 5, ItemGroup.BOW, "Tăng sát thương dựa trên khoảng cách bay của mũi tên."),
    FLAME("flame", "Flame", 1, ItemGroup.BOW, "Thiêu đốt mục tiêu khi bắn trúng."),

    // 🛡️ ARMOR ENCHANTMENTS
    GROWTH("growth", "Growth", 7, ItemGroup.ARMOR, "Tăng lượng Máu tối đa (HP) trực tiếp."),
    PROTECTION("protection", "Protection", 7, ItemGroup.ARMOR, "Tăng mạnh điểm phòng thủ, giảm sát thương nhận vào."),
    THORNS("thorns", "Thorns", 3, ItemGroup.ARMOR, "Phản lại một phần sát thương cho kẻ tấn công."),
    REJUVENATE("rejuvenate", "Rejuvenate", 5, ItemGroup.ARMOR, "Tăng khả năng hồi phục máu tự nhiên mỗi giây."),
    SUGAR_RUSH("sugar_rush", "Sugar Rush", 3, ItemGroup.ARMOR, "Tăng tốc độ di chuyển (Chỉ ép vào Giày)."),
    FEATHER_FALLING("feather_falling", "Feather Falling", 10, ItemGroup.ARMOR, "Giảm mạnh sát thương khi rơi từ trên cao (Chỉ ép vào Giày)."),
    BIG_BRAIN("big_brain", "Big Brain", 5, ItemGroup.ARMOR, "Cộng thêm giáp kháng ma thuật và phòng thủ (Chỉ ép vào Mũ)."),
    COUNTER_STRIKE("counter_strike", "Counter-Strike", 5, ItemGroup.ARMOR, "Tăng mạnh giáp hộ thân khi cận chiến (Chỉ ép vào Áo)."),
    SMARTY_PANTS("smarty_pants", "Smarty Pants", 5, ItemGroup.ARMOR, "Tăng điểm giáp thủ bổ trợ (Chỉ ép vào Quần)."),

    // ⛏️ MINING & FARMING
    EFFICIENCY("efficiency", "Efficiency", 10, ItemGroup.TOOL, "Tăng tốc độ đập khối block."),
    MINING_FORTUNE("mining_fortune", "Mining Fortune", 5, ItemGroup.TOOL, "Tăng số lượng tài nguyên quặng nhận được khi đào."),
    PRISTINE("pristine", "Pristine", 5, ItemGroup.TOOL, "Tỷ lệ đào phọt ra Đá Quý Tinh Tú siêu cấp hiếm quý."),
    REPLENISH("replenish", "Replenish", 1, ItemGroup.FARMING, "Tự động gieo lại hạt giống khi thu hoạch cây chín."),
    TELEKINESIS("telekinesis", "Telekinesis", 1, ItemGroup.ALL, "Vật phẩm rơi tự động bay thẳng vào túi đồ."),
    
    // 🔮 CHẾ TẠO ĐỘC QUYỀN CHỢ BAZAAR
    MENDING("mending", "Mending", 1, ItemGroup.ALL, "Tự động tiêu thụ điểm kinh nghiệm để sửa chữa độ bền trang bị.");

    private final String id;
    private final String displayName;
    private final int maxLevel;
    private final ItemGroup itemGroup;
    private final String description;

    CustomEnchant(String id, String displayName, int maxLevel, ItemGroup itemGroup, String description) {
        this.id = id;
        this.displayName = displayName;
        this.maxLevel = maxLevel;
        this.itemGroup = itemGroup;
        this.description = description;
    }

    public String getId() { return id; }
    public String getDisplayName() { return displayName; }
    public int getMaxLevel() { return maxLevel; }
    public ItemGroup getItemGroup() { return itemGroup; }
    public String getDescription() { return description; }

    public enum ItemGroup {
        SWORD, BOW, ARMOR, TOOL, FARMING, ALL;

        public boolean canApply(Material material) {
            if (material == Material.BOOK || material == Material.ENCHANTED_BOOK) {
                return true;
            }
            
            String name = material.name();
            boolean isActualAxe = (name.endsWith("_AXE") || name.equals("AXE")) && !name.contains("PICKAXE");
            switch (this) {
                case SWORD:
                    return name.contains("SWORD") || isActualAxe || material == Material.STICK || material == Material.BLAZE_ROD;
                case BOW:
                    return name.equals("BOW") || name.equals("CROSSBOW");
                case ARMOR:
                    return name.contains("HELMET") || name.contains("CHESTPLATE") || name.contains("LEGGINGS") || name.contains("BOOTS");
                case TOOL:
                    return name.contains("PICKAXE") || isActualAxe || name.contains("SHOVEL") || name.contains("HOE");
                case FARMING:
                    return name.contains("HOE") || isActualAxe;
                case ALL:
                    return true;
                default:
                    return false;
            }
        }
    }
}

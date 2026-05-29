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
    SHARPNESS("sharpness", "Phong Duệ", 8, ItemGroup.SWORD, "Cường hóa vạn vật, gia tăng đáng kể sát thương vật lý thô."),
    CRITICAL("critical", "Chí Mạng", 7, ItemGroup.SWORD, "Đột phá giới hạn, gia tăng sát thương và tỷ lệ đòn đánh chí mạng."),
    FIRST_STRIKE("first_strike", "Tiên Phát Chế Nhân", 5, ItemGroup.SWORD, "Phủ đầu kẻ địch bằng lượng sát thương khổng lồ trong đòn đánh đầu tiên."),
    GIANT_KILLER("giant_killer", "Cự Nhân Sát Thủ", 7, ItemGroup.SWORD, "Kẻ thù càng nhiều máu, sát thương giáng xuống càng tàn bạo."),
    EXECUTE("execute", "Đoạt Mệnh", 6, ItemGroup.SWORD, "Nhắm vào nhược điểm, sát thương tỷ lệ thuận với lượng sinh lực đã mất của mục tiêu."),
    LETHALITY("lethality", "Phá Giáp", 6, ItemGroup.SWORD, "Xuyên thủng phòng ngự, tước đoạt dần điểm giáp của mục tiêu sau mỗi đòn đánh."),
    VAMPIRISM("vampirism", "Huyết Ma", 6, ItemGroup.SWORD, "Nuốt chửng sinh khí kẻ địch để hồi phục lại máu sau khi kết liễu."),
    LIFE_STEAL("life_steal", "Phệ Huyết", 5, ItemGroup.SWORD, "Rút lấy sinh mệnh, chuyển hóa trực tiếp một phần sát thương gây ra thành máu."),
    DRAGON_HUNTER("dragon_hunter", "Đồ Long", 5, ItemGroup.SWORD, "Sát khí tỏa ra, mang sức mạnh khắc chế chí tử chuyên trị Long Tộc."),
    LOOTING("looting", "Thái Kim", 3, ItemGroup.SWORD, "Khai mở tài vận, tăng đáng kể lượng chiến lợi phẩm rớt ra từ quái vật."),
    FIRE_ASPECT("fire_aspect", "Hỏa Thiêu", 2, ItemGroup.SWORD, "Tẩm hỏa diệm vào lưỡi kiếm, thiêu đốt kẻ thù thành tro bụi."),

    // 🏹 BOW ENCHANTMENTS
    POWER("power", "Cường Lực", 7, ItemGroup.BOW, "Kéo căng dây cung, gia tăng vạn phần uy lực cho từng mũi tên bắn ra."),
    OVERLOAD("overload", "Quá Tải", 5, ItemGroup.BOW, "Ép xung giới hạn, trao cho mũi tên khả năng bạo kích với sức công phá hủy diệt."),
    SNIPER("sniper", "Thần Tiễn", 5, ItemGroup.BOW, "Lực bắn xé gió, sát thương tiễn bay càng xa uy lực càng khủng khiếp."),
    FLAME("flame", "Hỏa Tiễn", 1, ItemGroup.BOW, "Gia trì hỏa ấn, biến mũi tên thành ngọn lửa tử thần thiêu đốt mục tiêu."),

    // 🔱 TRIDENT ENCHANTMENTS
    IMPALING("impaling", "Xiên Thấu", 5, ItemGroup.TRIDENT, "Ngũ lôi oanh đỉnh, đâm xuyên lớp vảy cứng cáp của quái vật dưới đáy biển sâu."),
    LOYALTY("loyalty", "Trung Thành", 3, ItemGroup.TRIDENT, "Gắn kết huyết mạch, Đinh Ba sau khi phóng ra sẽ tự động quay trở về tay chủ nhân."),
    RIPTIDE("riptide", "Trực Chỉ", 3, ItemGroup.TRIDENT, "Mượn lực phong ba, phóng người bay đi trong mưa bão hoặc khi ngập mình dưới dòng nước."),
    CHANNELING("channeling", "Triệu Lôi", 1, ItemGroup.TRIDENT, "Dẫn động thiên lôi, gọi sấm sét giáng thẳng xuống mục tiêu trong cơn dông bão."),

    // 🔨 MACE ENCHANTMENTS
    DENSITY("density", "Trọng Lực", 5, ItemGroup.MACE, "Ngưng tụ khối lượng, rơi từ càng cao sức ép giáng xuống càng mang tính hủy diệt."),
    BREACH("breach", "Phá Giáp", 4, ItemGroup.MACE, "Lực đập rung trời, nghiền nát và phớt lờ hoàn toàn lớp giáp kiên cố của kẻ thù."),
    WIND_BURST("wind_burst", "Bùng Nổ Gió", 3, ItemGroup.MACE, "Khống chế luồng phong, dội ngược người lên không trung sau mỗi cú nện trúng đích."),

    // 🛡️ ARMOR ENCHANTMENTS
    GROWTH("growth", "Sinh Lực", 7, ItemGroup.ARMOR, "Cải biến thể chất, khuếch đại giới hạn sinh mệnh (HP) lên mức tối đa."),
    PROTECTION("protection", "Kim Chung Tráo", 7, ItemGroup.ARMOR, "Ngưng tụ cương khí hộ thể, gia tăng độ cứng cáp và cản phá mọi sát thương."),
    THORNS("thorns", "Phản Phệ", 3, ItemGroup.ARMOR, "Thân phủ gai nhọn, dội ngược một phần sát thương lại những kẻ dám tấn công."),
    REJUVENATE("rejuvenate", "Hồi Xuân", 5, ItemGroup.ARMOR, "Dòng máu sục sôi, thúc đẩy tốc độ tái tạo sinh lực tự nhiên trong chiến đấu."),
    SUGAR_RUSH("sugar_rush", "Thần Hành", 3, ItemGroup.BOOTS, "Thân nhẹ tựa hồng mao, gia tốc di chuyển xé gió (Chỉ ép vào Giày)."),
    FEATHER_FALLING("feather_falling", "Khinh Công", 10, ItemGroup.BOOTS, "Ngự phong phi hành, xóa nhòa sát thương khi rơi từ tầng không (Chỉ ép vào Giày)."),
    BIG_BRAIN("big_brain", "Đại Não", 5, ItemGroup.HELMET, "Khai sáng linh trí, gia tăng lượng lớn Năng lượng Pháp thuật - Mana (Chỉ ép vào Mũ)."),
    COUNTER_STRIKE("counter_strike", "Nghịch Lân", 5, ItemGroup.CHESTPLATE, "Khi bị tổn thương, lập tức ngưng tụ cương khí bảo vệ thân thể (Chỉ ép vào Áo)."),
    SMARTY_PANTS("smarty_pants", "Minh Mẫn", 5, ItemGroup.LEGGINGS, "Đả thông kinh mạch, bổ trợ lượng lớn Năng lượng Pháp thuật - Mana (Chỉ ép vào Quần)."),

    // ⛏️ MINING & FARMING
    EFFICIENCY("efficiency", "Thần Tốc", 10, ItemGroup.TOOL, "Động tác nhanh như thiểm điện, thu thập tài nguyên trong chớp mắt."),
    MINING_FORTUNE("mining_fortune", "Vượng Khí", 5, ItemGroup.TOOL, "Khí vận hanh thông, thu được lượng tài nguyên dồi dào hơn khi khai phá."),
    PRISTINE("pristine", "Tinh Hoa", 5, ItemGroup.TOOL, "Chạm tay vào đá tảng, đánh thức xác suất rơi ra Đá Quý Tinh Tú siêu cấp hiếm."),
    REPLENISH("replenish", "Phồn Thực", 1, ItemGroup.FARMING, "Ban phước cho đất rữa, tự động gieo mầm hạt giống sau mỗi lần thu hoạch."),
    TELEKINESIS("telekinesis", "Ngự Vật", 1, ItemGroup.ALL, "Thao túng không gian, điều khiển chiến lợi phẩm bay thẳng vào trong túi đồ."),
    
    // 🔮 CHẾ TẠO ĐỘC QUYỀN CHỢ BAZAAR
    MENDING("mending", "Tu Bổ", 1, ItemGroup.ALL, "Hấp thụ linh khí của trời đất, chuyển hóa kinh nghiệm thành độ bền trang bị.");

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
        SWORD, BOW, ARMOR, HELMET, CHESTPLATE, LEGGINGS, BOOTS, TOOL, FARMING, TRIDENT, MACE, ALL;

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
                case HELMET:
                    return name.contains("HELMET");
                case CHESTPLATE:
                    return name.contains("CHESTPLATE");
                case LEGGINGS:
                    return name.contains("LEGGINGS");
                case BOOTS:
                    return name.contains("BOOTS");
                case TOOL:
                    return name.contains("PICKAXE") || isActualAxe || name.contains("SHOVEL") || name.contains("HOE");
                case FARMING:
                    return name.contains("HOE") || isActualAxe;
                case TRIDENT:
                    return material == Material.TRIDENT;
                case MACE:
                    return material == Material.MACE;
                case ALL:
                    return true;
                default:
                    return false;
            }
        }
    }
}

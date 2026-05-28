package org.example.enchant;

import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
import org.bukkit.Material;
import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
import org.bukkit.block.Block;

import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
public class BookshelfScanner {

    /**
     * Thuật toán quét không gian 3D đếm số lượng kệ sách xung quanh bàn enchant
     * Giới hạn tối đa 15 kệ sách chuẩn vanilla của Minecraft
     */
    public static int countBookshelves(Block tableBlock) {
        int count = 0;

        // Quét phạm vi bán kính 2 block chiều ngang (X, Z) và 1 block chiều dọc (Y)
        for (int z = -2; z <= 2; z++) {
            for (int x = -2; x <= 2; x++) {
                // Chỉ quét các block nằm ở vòng ngoài (bán kính bằng 2)
                if (x == -2 || x == 2 || z == -2 || z == 2) {
                    for (int y = 0; y <= 1; y++) {
                        Block b = tableBlock.getRelative(x, y, z);
                        
                        if (b.getType() == Material.BOOKSHELF) {
                            // 🛠️ Điều kiện ép: Khoảng không giữa bàn và kệ sách phải là AIR
                            // (Nếu bị đặt đuốc, đặt thảm chắn đường là đéo tính điểm)
                            Block space = tableBlock.getRelative(x / 2, y, z / 2);
                            if (space.getType() == Material.AIR) {
                                count++;
                            }
                        }
                    }
                }
            }
        }
        // Khống chế mốc tối đa 15 kệ sách để tính toán chỉ số cho cân bằng
        return Math.min(count, 15);
    }
}

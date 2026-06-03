<div align="center">
  <h1>⚔️ CustomWeaponEngine (CWE)</h1>
  <p><b>Hệ sinh thái vũ khí, giáp, và tính năng nhập vai RPG tối thượng dành cho Minecraft (Paper 1.21).</b></p>
</div>

---

## 📖 Giới thiệu
**CustomWeaponEngine (CWE)** là một Plugin Minecraft mang đậm phong cách của *Hypixel Skyblock*. CWE cung cấp một bộ khung (Framework) vô cùng mạnh mẽ cho phép triển khai hàng loạt vũ khí huyền thoại, giáp bộ (Armor Sets), cơ chế cường hóa (Enchant/Reforge), hệ thống kinh tế (Bank/Bazaar/Trade) và các sự kiện thế giới (Meteorite). Toàn bộ được Việt Hóa chuẩn phong cách RPG.

Hệ thống chỉ số cơ bản của trang bị (Damage, Strength, Crit Chance, Crit Damage, Intelligence) được tích hợp trực tiếp qua lõi **AuraSkills**.

---

## 🚀 Các Tính Năng Nổi Bật

### 1. Kho Vũ Khí Huyền Thoại (Skyblock Weapons)
Hệ thống vũ khí sở hữu Kỹ Năng (Item Ability) và NBT riêng biệt:
- **Batch 1 (Siêu phẩm End-game)**: Aspect of the End (Tốc biến), Aspect of the Dragons (Hất văng), Livid Dagger (Đâm lén x2 sát thương), Shadow Fury (Lướt trảm 5 mục tiêu), Giant's Sword (Nện đất), Wither Blades (Hyperion, Valkyrie, Scylla, Astraea - Vụ nổ Wither), Emerald Blade (Sát thương tỷ lệ với tiền ví).
- **Vũ khí Mid/Early-game**: Pigman Sword, Fel Sword (Cộng dồn sát thương theo số quái giết), Zombie Sword (Hồi máu đồng minh), Golem Sword, Raider Axe (Thợ săn tiền thưởng).

## ⚡ Tính Năng Chính (Core Features)

### 🌿 Khám Phá & Xây Dựng (Phong cách Genshin Impact)
*   **Rương Ẩn Thám Hiểm (Exploration Chests):** Hệ thống rương ẩn giấu trên bản đồ, mỗi người chơi chỉ được mở 1 lần duy nhất để nhận Xu và phần thưởng ngẫu nhiên.
*   **Hệ Thống Đồ Nội Thất (Cosmetic Furniture):** Cung cấp các vật phẩm trang trí như Cây Bonsai, Tách Trà, Bánh Kem, Quả Địa Cầu... bằng hệ thống Base64 Texture Player Heads siêu đẹp.
*   **Tương thích tối đa AuraSkills:** Khuyến khích người chơi trồng trọt, khai thác và xây dựng để nhận chỉ số thụ động.

### ⚔️ Vũ Khí & Giáp (Weapons & Armor)
*   **Trang Bị Không Thể Phá Hủy (Unbreakable):** Toàn bộ Vũ khí và Giáp xịn đều có thuộc tính Unbreakable ẩn, không bao giờ hỏng. Mending và Unbreaking Vanilla sẽ tự động bị từ chối ép vào đồ Custom.
*   **Sửa Chữa (Repair Anvil):** Sửa đồ bằng khoáng sản cơ bản (Sắt, Vàng, Da) với chi phí cố định 5 Level tại đe ép Custom.
*   **Reforge Thông Minh:** Tính năng Gacha đúc lại chỉ số. Tự động đổi tiền tố tránh trùng lặp tên ngớ ngẩn (VD: *Wise Wise Dragon Armor* -> *Very Wise Dragon Armor*).
*   **Hệ thống Runaan & Juju Shortbow:** Cơ chế bắn không gồng, xuyên thủng mục tiêu, tên tự động đuổi (Homing Arrows).

### 2. Kỷ Nguyên Chiến Giáp (Armor Sets & Full Set Bonus)
Hơn **13 Bộ Giáp (52 món đồ)** với cơ chế nội tại khi mặc đủ bộ (Full Set Bonus):
- **Wither Armor Series (Bộ Tứ Wither)**: Necron (Đấu sĩ), Storm (Pháp sư), Maxor (Cung thủ/Tốc độ), Goldor (Đỡ đòn). Sở hữu nội tại *Witherborn* (Triệu hồi Wither mini bắn quái vật).
- **Bát Long Giáp (8 Bộ Rồng)**: Superior, Strong, Wise, Unstable, Young, Old, Protector, Holy (Tất cả đều có nội tại Huyết Mạch Rồng riêng biệt).
- **Trang bị độc quyền khác**: 
  - *Tarantula Armor*: Nhảy kép, x2 sát thương đòn thứ 4.
  - *Shadow Assassin*: Cộng dồn vĩnh viễn Strength sau mỗi lần giết quái.
  - *Ender Armor*: X2 toàn bộ chỉ số khi xuống The End, giảm sát thương từ Enderman.
  - *Werewolf, Hardened Diamond, Golem Armor*.

### 3. Giao Diện Quản Lý Thư Viện (`/cwelib`)
- Nơi lưu trữ an toàn 100% các trang bị tùy chỉnh của máy chủ (Lưu trong `library.yml`).
- Tách biệt 2 danh mục thông minh: **Vũ Khí (Weapons)** và **Giáp (Armors)**.
- Mục Giáp được tối ưu hóa: Chỉ hiển thị Mũ (Helmet). Khi Click vào Mũ sẽ tự động phát đủ Bộ 4 Mảnh (Mũ, Áo, Quần, Giày).

### 4. Nâng Cấp Hệ Thống Bảo Tồn (CWE v4.6 Hotfix)
- **Hệ thống sửa lỗi quét vật phẩm (VanillaItemUpdater):** Ngăn chặn bug hệ thống làm mất các dòng chữ "Item Ability" và "Full Set Bonus" khi cập nhật chỉ số của đồ cũ. Trả lại toàn bộ chỉ số RPG cho đồ cũ.
- **Dọn dẹp rác bộ nhớ (Attribute Memory Leak):** Sửa lỗi kẹt Máu/Tốc Độ vô hạn do Minecraft Vanilla lưu lại vĩnh viễn trên người chơi khi server bị crash.
- Tăng cường và sửa lỗi kỹ năng AOTD (Thêm LAVA cực kỳ đẹp mắt), vá lỗi gậy Cừu rớt lông/thịt khi nổ.

### 4. Hệ Thống Chế Tạo & Kinh Tế Toàn Diện
- **Chế tạo (Crafting)**: Cung cấp API tương tác với `ShapedRecipe` của Bukkit để người chơi tự chế tác đồ Mid/Early-game (Golem Sword, Raider Axe...) ngay trên Bàn chế tạo thường.
- **Ngân hàng (Bank)**: Gửi tiền, rút tiền, và nhận lãi suất (Interest) định kỳ.
- **Chợ Đen (Bazaar)**: Mua bán nguyên vật liệu với cơ chế Biến động giá cung-cầu.
- **Trade An Toàn**: Hệ thống giao dịch (Giao diện GUI) an toàn tuyệt đối giữa 2 người chơi. Tích hợp trao đổi cả Item lẫn Tiền mặt (Pocket Money).
- **Item Lock**: Khóa vật phẩm rơi ra từ Quái Vật. "Của ai người nấy nhặt", chống hiện tượngKS đồ.

### 5. Cường Hóa & Chế Tác
- **Reforge System (Tái Tổ Hợp)**: Tiêu thụ tiền để đập thêm tiền tố vào tên vật phẩm (VD: *Spicy* Livid Dagger, *Fierce* Necron's Helmet) giúp cộng dồn chỉ số.
- **Custom Enchants**: Giao diện cường hóa bằng Lapis (`/cweenchant`) cùng hệ thống Phù phép tối thượng (Ultimate Enchants).

### 6. Sự Kiện Thế Giới: Thiên Thạch (Meteorite)
- Thỉnh thoảng bầu trời sẽ giáng xuống những quả Thiên Thạch khổng lồ.
- **Thiết kế địa hình thông minh**: Thay vì đục lỗ phá hỏng map, thiên thạch sẽ tự động san bằng địa hình tạo ra một Đấu Trường Phẳng (Bán kính 25 khối).
- Tự động triệu hồi các Siêu Boss đặc biệt để người chơi tham gia săn lùng.

### 7. Dashboard Quản Trị (Web Local)
- Tích hợp một trang Web Local (HTML/JS) đóng vai trò làm Dashboard điều khiển Server.
- Cung cấp tính năng gửi lệnh Admin, kiểm tra chỉ số quái vật và trạng thái người chơi.

---

## 🛠 Hướng Dẫn Cài Đặt

1. Yêu cầu **Paper 1.21** trở lên.
2. Yêu cầu cài đặt Plugin phụ thuộc: **Vault**, Plugin Kinh tế (vd: **EssentialsX**), và **AuraSkills** (để tính toán chỉ số).
3. Đặt file `CustomWeaponEngine.jar` vào thư mục `plugins/`.
4. Khởi động lại máy chủ và tận hưởng!

> Mọi đóng góp, báo cáo lỗi (Bug) vui lòng tạo Issue trên kho lưu trữ.

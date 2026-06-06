# 📜 Changelog — CustomWeaponEngine (CWE)

Mọi thay đổi đáng chú ý của dự án sẽ được ghi lại tại đây.

---

## [1.2-SNAPSHOT] — 2026-05-31

### ✨ Tính Năng Mới
- **SkillBookSystem**: Hệ thống Sách Kỹ Năng cho phép kết hợp `Item Ability` và `Full Set Bonus` vào đồ Vanilla.
  - 5 cuốn cho Giáp (2 Rare, 2 Epic, 1 Legendary): `Dragon's Protection`, `Magical Synergy`, `Vampiric Touch`, `Adrenaline Rush`, `Cosmic Overlord`.
  - 5 cuốn cho Cận Chiến/Melee (Kiếm, Rìu): `Tornado Spin`, `Sharp Edge`, `Shadow Teleport`, `Bloodlust`, `Cosmic Smash`.
  - Hỗ trợ mở rộng cho các loại vũ khí khác (Trident, Bow...).
- **GuideSystem (`/guide`)**: Hệ thống hướng dẫn tương tác cho người chơi mới với giao diện GUI.
- **`/quests`**: Command cho hệ thống nhiệm vụ dành cho newbie.
- **ArrowDamageListener**: Tên bắn từ Cung và Trident giờ luôn nhận đầy đủ chỉ số stat từ vũ khí (Damage, Strength, Crit).
- **BazaarGUI**: Thêm các loại gỗ thường và gỗ Enchanted vào danh sách mua bán tại Chợ Đen.
- **CustomRecipeManager**: Bổ sung thêm nhiều công thức chế tạo Custom mới.
- **Exclusive Reforge GUI**: Làm lại giao diện cho tính năng Tái Tổ Hợp Độc Quyền (Exclusive Reforge).

### 🔧 Sửa Lỗi & Cải Tiến
- **Trident Enchant Conflict**: Cấm người chơi ép chung `Riptide` với `Loyalty`/`Channeling` lên cùng một cây Đinh Ba (đúng chuẩn Vanilla Minecraft).
- **Witherborn Ability đã bị XÓA VĨNH VIỄN**: Kỹ năng nội tại Witherborn của bộ giáp Wither bị loại bỏ hoàn toàn do gây crash server và flood Entity nghiêm trọng (TPS drop).
  - Xóa `WitherbornTask`.
  - Xóa toàn bộ tham chiếu trong `LegendaryArmor`.
  - Thêm logic tự động quét và xóa đồ Witherborn khỏi Inventory người chơi khi login.
- **AuraSkills Dependency**: Chuyển từ `scope=system` (đường dẫn cứng) sang `scope=provided` — yêu cầu file `lib/AuraSkills-2.3.12.jar` phải có mặt trong thư mục dự án.
- **Phục hồi code từ session quán net**: Khôi phục toàn bộ code bị mất do không commit từ máy tính bên ngoài, thông qua kỹ thuật decompile JAR.

---

## [1.1] — 2026-05-30

### ✨ Tính Năng Mới
- Tích hợp **MythicMobs** làm nguồn Boss cho Sự kiện Thiên Thạch và khu vực Boss.
- Thêm hệ thống **Region Boss** với cơ chế rewards Gacha theo hạng.

### 🔧 Sửa Lỗi
- Vá lỗi `ClassCastException` khi MythicMobs được tải sau CWE dẫn đến crash server khi spawn Boss.
- Cấu hình FTP auto-deploy qua `maven-antrun-plugin` để tự động upload JAR lên server sau mỗi lần Build thành công.

---

## [1.0] — 2026-05-29

### 🚀 Ra Mắt
- Khởi tạo dự án **CustomWeaponEngine**.
- Hệ thống vũ khí Legendary (Aspect of the End, Aspect of the Dragons, Livid Dagger, Shadow Fury, Wither Blades...).
- Hệ thống Giáp Bộ (13+ bộ giáp với Full Set Bonus).
- Hệ thống Kinh Tế: Bank, Bazaar, Trade GUI.
- Custom Enchant & Reforge System.
- Sự Kiện Thiên Thạch (Meteorite Event).
- Hệ thống Rương Ẩn (Exploration Chests).
- Quản lý thư viện trang bị qua `/cwelib`.
- Tích hợp AuraSkills để tính chỉ số RPG.

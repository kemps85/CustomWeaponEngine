## 📢 5. HỆ THỐNG QUẢN LÝ & THÔNG BÁO CẬP NHẬT (CHANGELOG SYSTEM)

Hệ thống quản lý phiên bản và thông báo cập nhật tự động độc quyền của `CustomWeaponEngine`. Tự động đồng bộ giữa cấu hình `config.yml` và bộ nhớ động trên dữ liệu người chơi (PersistentDataContainer), giúp truyền tải thông tin update trực quan mà không gây phiền nhiễu cho thành viên.

### 🎮 Các Tính Năng Dành Cho Người Chơi
* **Hệ Thống Trùng Đúc Gacha Mới:**
    * Sử dụng lệnh `/reforge` hoặc `/rf` để mở giao diện tổng hợp.
    * Hỗ trợ 2 chế độ: `Normal Reforge` (2,000 Coins) và `Exclusive Reforge` (20,000 Coins).
    * **Tương thích đồ Vanilla:** Cho phép gạch tên giới hạn cũ, cường hóa được cả trang bị thường (Sắt, Kim Cương...) lên phân hạng **RARE**.
    * Bổ sung 5 tiền tố đặc quyền tối cao: `ANCIENT`, `NECROTIC`, `GIANT`, `FABLED`, `WITHERED`.
    * Tiền tố `WITHERED` sở hữu cơ chế đột biến: Cộng Sức mạnh tỉ lệ thuận theo Level hiện tại của người chơi.
    * **Chống bug chỉ số:** Chỉ số Reforge từ Giáp chỉ kích hoạt khi thực sự mặc trên người (EquipmentSlot), chặn hoàn toàn nếu chỉ cầm trên tay. Giữ nguyên tên đã đổi qua lệnh `/cweie rename`.
* **Cân Bằng Mana & Kỹ Năng:**
    * Tích hợp lõi hồi phục Mana tự động: **Hồi 3% Mana tối đa mỗi giây**.
    * Gậy Cừu (*Astral Shepherd's Wand*) tiêu hao 30 Mana và vá triệt để lỗi chọn mục tiêu lên quái vật.
* **Mở Rộng Giao Dịch Bazaar:**
    * Hỗ trợ giao dịch các loại quặng cường hóa thế hệ mới.
    * Mở rộng danh mục nông sản: Thịt heo, gà, cừu, thỏ, mắt nhện, slime ball và phiên bản nén cường hóa (Enchanted) của chúng.

---

### 📡 Cơ Chế Thông Báo Thông Minh (Client-Side Smart Notification)
* **Lưu trữ ngầm:** Sử dụng thẻ NBT ẩn của Paper API (`cwe_read_changelog_version`) găm trực tiếp vào Player Data.
* **Hoạt động:** Khi người chơi kết nối vào máy chủ (`PlayerJoinEvent`), hệ thống kiểm tra số phiên bản cập nhật mà người chơi đã đọc:
    * Nếu `phiên bản người chơi < phiên bản server`: Gửi một tin nhắn tương tác dạng JSON Chat:
        > `§6§l[NHẤP VÀO ĐÂY] §eađể xem chi tiết cập nhật hoặc gõ §b/cweupdate§e!`
    * Tin nhắn tích hợp sẵn thuộc tính di chuột hiện Tooltip giải thích và thuộc tính nhấp chuột tự động thực thi lệnh đọc update.
    * Ngay sau lần gửi đầu tiên, hệ thống lưu trạng thái đã đọc. Người chơi đăng nhập các lần tiếp theo **sẽ không bị nhắc nhở lại** cho đến khi có update mới.

---

### 💻 Lệnh Điều Khiển (Commands)
* `/cweupdate` (Hoặc viết tắt: `/cwechangelog`): Lệnh công cộng dành cho toàn bộ người chơi để mở bảng tra cứu, xem lại toàn bộ nội dung bản cập nhật bất kỳ lúc nào ngay trong khung chat với định dạng màu sắc rõ ràng.

---

### 🛠️ Quy Trình Phát Hành Bản Cập Nhật Mới (Dành Cho Admin)
Mỗi khi tiến hành nâng cấp, bảo trì hoặc thêm tính năng mới cho server, Admin thực hiện theo các bước sau để kích nổ thông báo toàn server:

1.  Mở file `config.yml`, tìm đến mục cấu hình `changelog` ở cuối file.
2.  Chỉnh sửa/Thêm nội dung các dòng cập nhật vào danh sách `lines`.
3.  Tăng chỉ số `version` lên 1 đơn vị (Ví dụ từ `version: 1` lên `version: 2`).
4.  Gõ lệnh `/cst reload` trong game để nạp lại cấu hình.

**Cấu trúc mẫu trong `config.yml`:**
```yaml
changelog:
  version: 2
  lines:
    - "&6&l[UPDATE FABLED] &eHệ Thống Reforge Gacha Mới (/reforge)!"
    - "&a- Thêm 5 tiền tố đặc quyền: Ancient, Necrotic, Giant, Fabled, Withered."
    - "&a- Giáp Reforge chỉ kích hoạt chỉ số khi thực sự mặc trên người."
    - "&b- Hệ thống hồi phục Mana tự động: hồi 3% Mana tối đa/giây."
    - "&d- Mở rộng giao dịch nông sản nén và quặng mới tại Bazaar."

package org.example.system;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AntiMacroListener implements Listener {

    private final JavaPlugin plugin;
    private final Map<UUID, PlayerHitData> hitDataMap = new HashMap<>();

    public AntiMacroListener(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    private static class PlayerHitData {
        float lastYaw = 0;
        float lastPitch = 0;
        int sameAngleCount = 0;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerAttack(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) {
            return;
        }
        
        Player player = (Player) event.getDamager();
        UUID uuid = player.getUniqueId();
        Location loc = player.getLocation();
        
        float currentYaw = loc.getYaw();
        float currentPitch = loc.getPitch();

        PlayerHitData data = hitDataMap.computeIfAbsent(uuid, k -> new PlayerHitData());

        // Kiểm tra xem góc nhìn có thay đổi một tí xíu nào không (so sánh chính xác tuyệt đối)
        if (currentYaw == data.lastYaw && currentPitch == data.lastPitch) {
            data.sameAngleCount++;
        } else {
            data.lastYaw = currentYaw;
            data.lastPitch = currentPitch;
            data.sameAngleCount = 0; // Reset nếu có vẩy chuột
        }

        // Nếu chém trúng quái 100 nhát liên tiếp mà không nhúc nhích chuột 1 pixel nào
        if (data.sameAngleCount >= 80) {
            
            // Từ nhát thứ 80 đến 99: Hiện cảnh báo Action Bar nhưng VẪN CHO ĐÁNH
            if (data.sameAngleCount < 100) {
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§c⚠️ [Anti-AFK] Vui lòng rê chuột một chút, nếu không bạn sẽ bị phạt!"));
                if (data.sameAngleCount % 5 == 0) {
                    player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 2.0f);
                }
                return; // Thoát ra, không Cancel event
            }

            // Từ nhát 100 trở đi: Chặn sát thương
            event.setCancelled(true);
            
            // Ở chính xác nhát chém thứ 100, sút thẳng về Spawn
            if (data.sameAngleCount == 100) {
                // Đưa người chơi về điểm Spawn gốc của thế giới đầu tiên (Overworld)
                player.teleport(org.bukkit.Bukkit.getWorlds().get(0).getSpawnLocation());
                
                player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 0.5f);
                player.sendMessage("");
                player.sendMessage("§c§l[HỆ THỐNG] §ePhát hiện nghi vấn sử dụng phần mềm Auto-Click / Kẹp chuột AFK!");
                player.sendMessage("§c§l[HỆ THỐNG] §eBạn đã bị dịch chuyển về Spawn. Vui lòng chơi game trung thực!");
                player.sendMessage("");
                
                // Bắn thêm thông báo cho Admin biết
                org.bukkit.Bukkit.getLogger().info("[Anti-AFK] Đã sút " + player.getName() + " về spawn vì tội kẹp chuột Auto-Click!");
            }
            
            // Nếu nó vẫn ngoan cố cắm chuột sau khi bị sút về spawn
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§c⚠️ Phát hiện Auto-Click! Sát thương đã bị vô hiệu hóa!"));
        }
    }
}

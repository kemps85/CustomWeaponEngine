package org.example.system;

import org.bukkit.entity.Player;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TradeManager {
    private final Map<UUID, UUID> tradeRequests = new HashMap<>();
    private final Map<UUID, Long> requestTimes = new HashMap<>();

    public void sendRequest(Player sender, Player target) {
        tradeRequests.put(target.getUniqueId(), sender.getUniqueId());
        requestTimes.put(target.getUniqueId(), System.currentTimeMillis());
        
        sender.sendMessage("§a[Giao dịch] §7Đã gửi yêu cầu giao dịch đến §e" + target.getName() + "§7.");
        target.sendMessage("§a[Giao dịch] §e" + sender.getName() + " §7muốn giao dịch với bạn. Gõ §a/trade accept §7để đồng ý hoặc §c/trade cancel §7để từ chối. (Hết hạn sau 30s)");
    }

    public void acceptRequest(Player target) {
        if (!tradeRequests.containsKey(target.getUniqueId())) {
            target.sendMessage("§c[Giao dịch] §7Bạn không có yêu cầu giao dịch nào!");
            return;
        }
        
        long time = requestTimes.getOrDefault(target.getUniqueId(), 0L);
        if (System.currentTimeMillis() - time > 30000) {
            target.sendMessage("§c[Giao dịch] §7Yêu cầu giao dịch đã hết hạn!");
            tradeRequests.remove(target.getUniqueId());
            requestTimes.remove(target.getUniqueId());
            return;
        }

        UUID senderId = tradeRequests.get(target.getUniqueId());
        Player sender = org.bukkit.Bukkit.getPlayer(senderId);

        tradeRequests.remove(target.getUniqueId());
        requestTimes.remove(target.getUniqueId());

        if (sender == null || !sender.isOnline()) {
            target.sendMessage("§c[Giao dịch] §7Người chơi kia đã offline!");
            return;
        }

        if (sender.getLocation().distance(target.getLocation()) > 20) {
            target.sendMessage("§c[Giao dịch] §7Người chơi kia ở quá xa (> 20 block)!");
            sender.sendMessage("§c[Giao dịch] §7Người chơi kia ở quá xa (> 20 block)!");
            return;
        }

        TradeGUI.openTrade(sender, target);
    }
    
    public void cancelRequest(Player target) {
        if (tradeRequests.containsKey(target.getUniqueId())) {
            tradeRequests.remove(target.getUniqueId());
            requestTimes.remove(target.getUniqueId());
            target.sendMessage("§c[Giao dịch] §7Đã từ chối yêu cầu giao dịch.");
        } else {
            target.sendMessage("§c[Giao dịch] §7Bạn không có yêu cầu giao dịch nào!");
        }
    }
}

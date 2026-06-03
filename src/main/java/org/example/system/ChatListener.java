package org.example.system;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.example.core.CustomWeaponEngine;

public class ChatListener implements Listener {

    private Chat chat = null;

    public ChatListener(CustomWeaponEngine plugin) {
        setupChat();
    }

    private boolean setupChat() {
        if (Bukkit.getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Chat> rsp = Bukkit.getServer().getServicesManager().getRegistration(Chat.class);
        if (rsp != null) {
            chat = rsp.getProvider();
        }
        return chat != null;
    }

    @EventHandler
    public void onPlayerChat(AsyncChatEvent event) {
        Player player = event.getPlayer();
        String prefix = "";
        
        if (chat != null) {
            prefix = chat.getPlayerPrefix(player);
        }
        
        if (prefix == null) prefix = "";
        
        // Phòng hờ nếu Vault không xử lý được
        if (prefix.isEmpty() && Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            prefix = me.clip.placeholderapi.PlaceholderAPI.setPlaceholders(player, "%luckperms_prefix%");
        }
        if (prefix == null) prefix = "";
        
        // Thêm khoảng trắng nếu có prefix
        if (!prefix.isEmpty() && !prefix.endsWith(" ")) {
            prefix += " ";
        }

        LegacyComponentSerializer serializer = LegacyComponentSerializer.legacyAmpersand();
        Component prefixComp = serializer.deserialize(prefix);
        
        event.renderer((source, sourceDisplayName, message, viewer) -> 
            Component.text("").append(prefixComp).append(sourceDisplayName).append(Component.text(" §7» §f")).append(message)
        );
    }
}

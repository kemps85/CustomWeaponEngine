package org.example.system;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.example.core.CustomWeaponEngine;
import org.example.weapon.legendary.ManaHelper;
import org.jetbrains.annotations.NotNull;

public class CWEExpansion extends PlaceholderExpansion {

    private final CustomWeaponEngine plugin;

    public CWEExpansion(CustomWeaponEngine plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "cwe";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Antigravity";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0";
    }

    @Override
    public boolean persist() {
        return true; 
    }

    @Override
    public String onRequest(OfflinePlayer offlinePlayer, @NotNull String params) {
        if (offlinePlayer == null || !offlinePlayer.isOnline()) {
            return "";
        }
        
        Player player = offlinePlayer.getPlayer();
        if (params.equalsIgnoreCase("actionbar_mana")) {
            return ManaHelper.getActionbarMessage(player);
        }
        
        return "";
    }
}

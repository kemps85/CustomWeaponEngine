package org.example.system;

import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
import org.bukkit.OfflinePlayer;
import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
import org.jetbrains.annotations.NotNull;

import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
public class CooldownExpansion extends PlaceholderExpansion {

    private final HeavenlySwordListener weaponEngine;

    public CooldownExpansion(HeavenlySwordListener weaponEngine) {
        this.weaponEngine = weaponEngine;
    }

    @Override
    public @NotNull String getAuthor() {
        return "ThangDev";
    }

    @Override
    public @NotNull String getIdentifier() {
        return "cst"; // Placeholder prefix, e.g., %cst_cooldown_mystic_staff%
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
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        if (player == null) return "";

        // Check weapon cooldown remaining: %cst_cooldown_<weapon_id>%
        if (params.startsWith("cooldown_")) {
            String weaponId = params.replace("cooldown_", "");
            long timeLeft = weaponEngine.getRemainingCooldown(player.getUniqueId(), weaponId);
            
            // 🟩 FIXED: Changed "§aSẵn sàng" to "§aReady" for full English consistency
            return timeLeft > 0 ? timeLeft + "s" : "§aReady";
        }

        return null;
    }
}

package org.example.system;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.example.core.CustomWeaponEngine;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FurnitureGenerator {

    public static ItemStack getFurniture(CustomWeaponEngine plugin, String id) {
        String base64 = "";
        String name = "";
        String rarity = "RARE";
        
        switch (id.toLowerCase()) {
            case "bonsai":
                base64 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGZhOTUyMTNhNTRiMThjNDk2ODFhMmRmZGIwZmM3M2NlMTFiZTRjMDI0OWVkZmJjOTdiNDhkODNiZjZiNGZhZSJ9fX0=";
                name = "Bonsai Tree";
                break;
            case "teacup":
                base64 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGJmNzI1NTU1YTdmODVkZjUzY2QxYjc1YzE2YmY1ZTVkZTBjZTgyOTk0NWRhNGMyZTYyMjQzMGZmZTliNzE1MiJ9fX0=";
                name = "Tea Cup";
                break;
            case "treasure":
                base64 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTMyZTM1NWViZDljMjEyMmE3ODU1NTYzZjZlM2MyNTI0MDZlNGEzNDI3ZTM1ZDE5MTYwNzliOTljZDIzM2ZiOCJ9fX0=";
                name = "Treasure Pile";
                rarity = "LEGENDARY";
                break;
            case "globe":
                base64 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjFkZjc0NDM3MzViYzQ2YWEzMzcyNmRhNGRmNTc2YzFiNzBlNjNhNmFmYWNjMjA2NzcyYWZmYjJkMzdmZTE4ZCJ9fX0=";
                name = "Globe";
                rarity = "EPIC";
                break;
            case "cake":
                base64 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzhkNjcwZjgxMjhiODhlNWEyZTExMzU2NTQ3ZGNhNTljOTI2ZTRlZGZhZTIwODViMmFlNDRkYjJjZjllZTUwNSJ9fX0=";
                name = "Strawberry Cake";
                break;
            default:
                return null;
        }

        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
        // Fallback since we can't easily use skull profiles in 1.21 without complex NMS/Paper API.
        // Paper 1.20+ PlayerProfile API:
        try {
            org.bukkit.profile.PlayerProfile profile = Bukkit.createProfile(UUID.randomUUID());
            org.bukkit.profile.PlayerTextures textures = profile.getTextures();
            java.net.URL url = new java.net.URL(getTextureUrl(base64));
            textures.setSkin(url);
            profile.setTextures(textures);
            
            org.bukkit.inventory.meta.SkullMeta skullMeta = (org.bukkit.inventory.meta.SkullMeta) skull.getItemMeta();
            skullMeta.setOwnerProfile(profile);
            skull.setItemMeta(skullMeta);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ItemMeta meta = skull.getItemMeta();
        if (meta != null) {
            String color = rarity.equals("LEGENDARY") ? "§6" : rarity.equals("EPIC") ? "§5" : "§9";
            meta.setDisplayName(color + name);
            
            List<String> lore = new ArrayList<>();
            lore.add("§7Vật phẩm trang trí.");
            lore.add("§7Đặt trên mặt đất để làm đẹp.");
            lore.add("");
            lore.add(color + "§l" + rarity + " FURNITURE");
            meta.setLore(lore);
            
            meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "cwe_furniture"), PersistentDataType.STRING, id);
            meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "cwe_id"), PersistentDataType.STRING, "furniture_" + id);
            skull.setItemMeta(meta);
        }
        return skull;
    }

    private static String getTextureUrl(String base64) {
        String decoded = new String(java.util.Base64.getDecoder().decode(base64));
        // {"textures":{"SKIN":{"url":"http://textures.minecraft.net/texture/..."}}}
        int start = decoded.indexOf("http://");
        int end = decoded.indexOf('"', start);
        if (start != -1 && end != -1) {
            return decoded.substring(start, end);
        }
        return "";
    }
}

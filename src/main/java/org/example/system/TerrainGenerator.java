package org.example.system;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.Random;

public class TerrainGenerator {

    private static final Random random = new Random();

    public static void generateCrater(Location center, String elementType) {
        World world = center.getWorld();
        if (world == null) return;

        int radius = 25;
        int radiusSq = radius * radius;
        
        Material surfaceMat1, surfaceMat2, surfaceMat3, coreMat1, coreMat2;
        boolean hasPillars = false;
        
        if ("FIRE".equals(elementType)) {
            surfaceMat1 = Material.NETHERRACK;
            surfaceMat2 = Material.MAGMA_BLOCK;
            surfaceMat3 = Material.BASALT;
            coreMat1 = Material.GOLD_ORE;
            coreMat2 = Material.OBSIDIAN;
        } else if ("ICE".equals(elementType)) {
            surfaceMat1 = Material.SNOW_BLOCK;
            surfaceMat2 = Material.ICE;
            surfaceMat3 = Material.POWDER_SNOW; 
            coreMat1 = Material.DIAMOND_ORE;
            coreMat2 = Material.PACKED_ICE;
        } else if ("VOID".equals(elementType)) {
            surfaceMat1 = Material.END_STONE;
            surfaceMat2 = Material.OBSIDIAN;
            surfaceMat3 = Material.PURPUR_BLOCK;
            coreMat1 = Material.OBSIDIAN; 
            coreMat2 = Material.CRYING_OBSIDIAN;
            hasPillars = true;
        } else {
            surfaceMat1 = Material.COBBLED_DEEPSLATE;
            surfaceMat2 = Material.BLACKSTONE;
            surfaceMat3 = Material.STONE;
            coreMat1 = Material.IRON_ORE;
            coreMat2 = Material.COBBLESTONE;
        }

        int centerY = center.getBlockY();
        boolean isWater = world.getBlockAt(center).getType() == Material.WATER;

        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                int distSq = x * x + z * z;
                if (distSq <= radiusSq) {
                    Location loc = center.clone().add(x, 0, z);
                    Block surface = world.getBlockAt(loc);
                    
                    // Tạo mặt phẳng
                    double roll = random.nextDouble();
                    if (roll < 0.3) {
                        surface.setType(surfaceMat1);
                        if ("ICE".equals(elementType) && roll < 0.05) surface.setType(Material.POWDER_SNOW);
                        if ("FIRE".equals(elementType) && roll < 0.02) surface.getRelative(org.bukkit.block.BlockFace.UP).setType(Material.FIRE);
                    } else if (roll < 0.45) {
                        surface.setType(surfaceMat2);
                    } else if (roll < 0.55) {
                        surface.setType(surfaceMat3);
                    } else {
                        surface.setType(surfaceMat1);
                    }

                    // Dọn dẹp phía trên mặt phẳng để có chỗ đánh nhau
                    for (int y = 1; y <= 15; y++) {
                        Block above = world.getBlockAt(loc.clone().add(0, y, 0));
                        if (above.getType() != Material.AIR && above.getType() != Material.FIRE) {
                            above.setType(Material.AIR);
                        }
                    }

                    // Nếu là nước, tạo phễu bên dưới
                    if (isWater) {
                        for (int yOffset = 1; yOffset <= 10; yOffset++) {
                            double currentRadius = radius * (10.0 - yOffset) / 10.0;
                            if (distSq <= currentRadius * currentRadius) {
                                Block below = world.getBlockAt(loc.clone().add(0, -yOffset, 0));
                                below.setType(random.nextBoolean() ? coreMat1 : coreMat2);
                            }
                        }
                    }
                }
            }
        }
        
        // VOID: Cột Obsidian
        if (hasPillars) {
            for (int i = 0; i < 5; i++) {
                int px = (int) (random.nextDouble() * radius * 1.5 - radius * 0.75);
                int pz = (int) (random.nextDouble() * radius * 1.5 - radius * 0.75);
                if (px * px + pz * pz > radiusSq) continue;
                
                Block base = world.getBlockAt(center.getBlockX() + px, centerY, center.getBlockZ() + pz);
                int height = 15 + random.nextInt(6); 
                for (int h = 0; h < height; h++) {
                    world.getBlockAt(base.getLocation().add(0, h, 0)).setType(Material.OBSIDIAN);
                    world.getBlockAt(base.getLocation().add(1, h, 0)).setType(Material.OBSIDIAN);
                    world.getBlockAt(base.getLocation().add(-1, h, 0)).setType(Material.OBSIDIAN);
                    world.getBlockAt(base.getLocation().add(0, h, 1)).setType(Material.OBSIDIAN);
                    world.getBlockAt(base.getLocation().add(0, h, -1)).setType(Material.OBSIDIAN);
                }
            }
        }
    }

    public static void generateUndergroundCave(Location center, String oreType) {
        World world = center.getWorld();
        if (world == null) return;

        int carveRadius = 10;
        int shellRadius = 12;
        
        Material oreMat = getMaterialFromType(oreType);

        // Quét hình cầu
        for (int x = -shellRadius; x <= shellRadius; x++) {
            for (int y = -shellRadius; y <= shellRadius; y++) {
                for (int z = -shellRadius; z <= shellRadius; z++) {
                    double distanceSq = x * x + y * y + z * z;
                    Block b = world.getBlockAt(center.clone().add(x, y, z));

                    // Ruột rỗng
                    if (distanceSq <= carveRadius * carveRadius) {
                        b.setType(Material.AIR);
                    } 
                    // Vỏ hang động (Trát quặng)
                    else if (distanceSq <= shellRadius * shellRadius) {
                        double roll = random.nextDouble();
                        if (roll < 0.30) {
                            b.setType(oreMat); // 30% quặng
                        } else if (roll < 0.60) {
                            b.setType(Material.DEEPSLATE);
                        } else if (roll < 0.90) {
                            b.setType(Material.COBBLED_DEEPSLATE);
                        } else {
                            b.setType(Material.STONE);
                        }
                    }
                }
            }
        }
    }

    private static Material getMaterialFromType(String type) {
        switch (type.toUpperCase()) {
            case "DIAMOND": return Material.DIAMOND_ORE;
            case "IRON": return Material.IRON_ORE;
            case "GOLD": return Material.GOLD_ORE;
            case "LAPIS": return Material.LAPIS_ORE;
            case "REDSTONE": return Material.REDSTONE_ORE;
            case "COPPER": return Material.COPPER_ORE;
            case "EMERALD": return Material.EMERALD_ORE;
            case "SPATIAL_RIFT_FRAGMENT": return Material.OBSIDIAN; // Thiên thạch hư không
            default: return Material.STONE;
        }
    }
}

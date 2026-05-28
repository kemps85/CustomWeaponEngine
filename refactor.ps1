$root = "C:\Users\Administrator\Downloads\CustomWeaponEngine - Testing Version\src\main\java\org\example"

# 1. Create Directories
$dirs = @("core", "weapon", "armor", "bazaar", "stats", "system")
foreach ($d in $dirs) {
    if (-not (Test-Path "$root\$d")) {
        New-Item -ItemType Directory -Path "$root\$d" | Out-Null
    }
}

# 2. Rename Files First (so references can be tracked)
if (Test-Path "$root\AuraLevelBridge.java") { Rename-Item "$root\AuraLevelBridge.java" "CustomWeaponEngine.java" }
if (Test-Path "$root\ThienDaoKiemListener.java") { Rename-Item "$root\ThienDaoKiemListener.java" "HeavenlySwordListener.java" }
if (Test-Path "$root\CstShadowAssassin.java") { Rename-Item "$root\CstShadowAssassin.java" "ShadowAssassinListener.java" }

# 3. Move Files
Move-Item "$root\CustomWeaponEngine.java" "$root\core\" -ErrorAction SilentlyContinue
Move-Item "$root\HeavenlySwordListener.java" "$root\weapon\" -ErrorAction SilentlyContinue
Move-Item "$root\ShadowAssassinListener.java" "$root\weapon\" -ErrorAction SilentlyContinue
Move-Item "$root\BerserkListener.java" "$root\weapon\" -ErrorAction SilentlyContinue
Move-Item "$root\enchant\AstralShepherdListener.java" "$root\weapon\" -ErrorAction SilentlyContinue

Move-Item "$root\ArmorManager.java" "$root\armor\" -ErrorAction SilentlyContinue
Move-Item "$root\enchant\ArmorEnchantListener.java" "$root\armor\" -ErrorAction SilentlyContinue
Move-Item "$root\enchant\CosmicVoidListener.java" "$root\armor\" -ErrorAction SilentlyContinue

Move-Item "$root\enchant\BazaarGUI.java" "$root\bazaar\" -ErrorAction SilentlyContinue
Move-Item "$root\enchant\BazaarMobDropListener.java" "$root\bazaar\" -ErrorAction SilentlyContinue
Move-Item "$root\enchant\BazaarRecipeListener.java" "$root\bazaar\" -ErrorAction SilentlyContinue

Move-Item "$root\enchant\ItemStatsGUI.java" "$root\stats\" -ErrorAction SilentlyContinue
Move-Item "$root\enchant\ItemStatsListener.java" "$root\stats\" -ErrorAction SilentlyContinue

Move-Item "$root\ItemEditorCommand.java" "$root\system\" -ErrorAction SilentlyContinue
Move-Item "$root\OreVeinManager.java" "$root\system\" -ErrorAction SilentlyContinue
Move-Item "$root\MobDeathListener.java" "$root\system\" -ErrorAction SilentlyContinue
Move-Item "$root\enchant\ReplenishListener.java" "$root\system\" -ErrorAction SilentlyContinue
Move-Item "$root\CooldownExpansion.java" "$root\system\" -ErrorAction SilentlyContinue

# 4. Text Replacements
$files = Get-ChildItem -Path $root -Recurse -Filter "*.java"

foreach ($file in $files) {
    $content = Get-Content $file.FullName -Raw

    # Rename Classes
    $content = $content -replace "AuraLevelBridge", "CustomWeaponEngine"
    $content = $content -replace "ThienDaoKiemListener", "HeavenlySwordListener"
    $content = $content -replace "CstShadowAssassin", "ShadowAssassinListener"
    $content = $content -replace "ThienDaoKiem", "HeavenlySword"

    # Fix Fully Qualified Names
    $content = $content -replace "org\.example\.enchant\.AstralShepherdListener", "org.example.weapon.AstralShepherdListener"
    $content = $content -replace "org\.example\.enchant\.ArmorEnchantListener", "org.example.armor.ArmorEnchantListener"
    $content = $content -replace "org\.example\.enchant\.CosmicVoidListener", "org.example.armor.CosmicVoidListener"
    $content = $content -replace "org\.example\.enchant\.BazaarGUI", "org.example.bazaar.BazaarGUI"
    $content = $content -replace "org\.example\.enchant\.BazaarMobDropListener", "org.example.bazaar.BazaarMobDropListener"
    $content = $content -replace "org\.example\.enchant\.BazaarRecipeListener", "org.example.bazaar.BazaarRecipeListener"
    $content = $content -replace "org\.example\.enchant\.ItemStatsGUI", "org.example.stats.ItemStatsGUI"
    $content = $content -replace "org\.example\.enchant\.ItemStatsListener", "org.example.stats.ItemStatsListener"
    $content = $content -replace "org\.example\.ItemEditorCommand", "org.example.system.ItemEditorCommand"
    $content = $content -replace "org\.example\.OreVeinManager", "org.example.system.OreVeinManager"
    $content = $content -replace "org\.example\.enchant\.ReplenishListener", "org.example.system.ReplenishListener"

    # Fix Package Declaration dynamically based on directory
    $parentFolder = Split-Path (Split-Path $file.FullName) -Leaf
    if ($parentFolder -ne "example") {
        $expectedPackage = "package org.example.$parentFolder;"
        $content = $content -replace "package org\.example(?:\.enchant)?;", $expectedPackage
    }

    # Some common imports that might be missing due to movement
    $imports = @"
import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
"@
    
    # We will inject wildcard imports to all files to prevent missing class errors
    # Find the first import or class declaration and insert our imports
    if ($content -notmatch "import org\.example\.core") {
        $content = $content -replace "(?m)^(import |public class |class |public final class )", "$imports`r`n`$1"
    }

    Set-Content -Path $file.FullName -Value $content
}

# Update plugin.yml
$pluginYml = "C:\Users\Administrator\Downloads\CustomWeaponEngine - Testing Version\src\main\resources\plugin.yml"
$ymlContent = Get-Content $pluginYml -Raw
$ymlContent = $ymlContent -replace "main: org\.example\.AuraLevelBridge", "main: org.example.core.CustomWeaponEngine"
Set-Content -Path $pluginYml -Value $ymlContent

Write-Output "Refactor complete."

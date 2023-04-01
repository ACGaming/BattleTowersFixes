package mod.acgaming.btfixes.config;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import mod.acgaming.btfixes.AS_TowerDestroyerNew;
import mod.acgaming.btfixes.AS_WorldGenTowerNew;
import mod.acgaming.btfixes.BTFixes;

@Config(modid = BTFixes.MODID, name = "battletowers_fixes")
public class BTFixesConfig
{
    @Config.RequiresWorldRestart
    @Config.Name("Tower Variants")
    @Config.Comment
        ({
            "Usage of the tower variant builder:",
            "THEME;WALL_BLOCK;WALL_BLOCK_METADATA;LIGHT_BLOCK;LIGHT_BLOCK_METADATA;FLOOR_BLOCK;FLOOR_BLOCK_METADATA;STAIR_BLOCK;STAIR_BLOCK_METADATA",
            "Available themes: default, sand, red_sand, snow, water, foliage, rare"
        })
    public static String[] towerVariants = new String[]
        {
            "default;minecraft:cobblestone;0;minecraft:torch;0;minecraft:double_stone_slab;0;minecraft:stone_stairs;0",
            "default;minecraft:stone;0;minecraft:torch;0;minecraft:double_stone_slab;0;minecraft:stone_brick_stairs;0",
            "sand;minecraft:sandstone;0;minecraft:torch;0;minecraft:double_stone_slab;0;minecraft:sandstone_stairs;0",
            "red_sand;minecraft:red_sandstone;0;minecraft:torch;0;minecraft:double_stone_slab;0;minecraft:red_sandstone_stairs;0",
            "snow;minecraft:ice;0;minecraft:air;0;minecraft:snow;0;minecraft:quartz_stairs;0",
            "water;minecraft:mossy_cobblestone;0;minecraft:torch;0;minecraft:double_stone_slab;0;minecraft:stone_stairs;0",
            "water;minecraft:prismarine;1;minecraft:sea_lantern;0;minecraft:prismarine;2;minecraft:quartz_stairs;0",
            "foliage;minecraft:log;3;minecraft:web;0;minecraft:dirt;0;minecraft:jungle_stairs;0",
            "rare;minecraft:netherrack;0;minecraft:glowstone;0;minecraft:soul_sand;0;minecraft:nether_brick_stairs;0"
        };

    @Config.RequiresWorldRestart
    @Config.Name("Buffer Time")
    @Config.Comment({"Time in ticks when generating should start after world load", "Used for makeshift Loot Tweaker compatibility"})
    public static int bufferTime = 100;

    @Config.RequiresWorldRestart
    @Config.Name("Destruction Blacklist")
    @Config.Comment
        ({
            "Blocks that don't get destroyed by collapsing towers",
            "Syntax: modid:block"
        })
    public static String[] destructionBlacklist = new String[]
        {
            "tombmanygraves:grave_block"
        };

    @Mod.EventBusSubscriber(modid = BTFixes.MODID)
    public static class EventHandler
    {
        @SubscribeEvent
        public static void onConfigChanged(final ConfigChangedEvent.OnConfigChangedEvent event)
        {
            if (event.getModID().equals(BTFixes.MODID))
            {
                ConfigManager.sync(BTFixes.MODID, Config.Type.INSTANCE);
                AS_WorldGenTowerNew.initTowerVariants();
                AS_TowerDestroyerNew.initDestructionBlacklist();
                BTFixes.LOGGER.info(BTFixes.NAME + " config reloaded");
            }
        }
    }
}
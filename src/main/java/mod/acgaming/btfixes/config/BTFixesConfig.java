package mod.acgaming.btfixes.config;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import mod.acgaming.btfixes.BTFixes;

@Config(modid = BTFixes.MODID, name = "battletowers_fixes")
public class BTFixesConfig
{
    @Config.RequiresMcRestart
    @Config.Name("Tower Variants")
    @Config.Comment({"Usage of the tower variant builder:",
        "UNIQUE_NAME;THEME;WALL_BLOCK;WALL_BLOCK_METADATA;LIGHT_BLOCK;LIGHT_BLOCK_METADATA;FLOOR_BLOCK;FLOOR_BLOCK_METADATA;STAIR_BLOCK;STAIR_BLOCK_METADATA",
        "Available themes: default, sand, snow, water, foliage, nether"
    })
    public static String[] towerVariants = new String[]
        {
            "cobble_variant;default;minecraft:cobblestone;0;minecraft:torch;0;minecraft:double_stone_slab;0;minecraft:stone_stairs;0",
            "stone_variant;default;minecraft:stone;0;minecraft:torch;0;minecraft:double_stone_slab;0;minecraft:stone_brick_stairs;0",
            "sandstone_variant;sand;minecraft:sandstone;0;minecraft:torch;0;minecraft:double_stone_slab;0;minecraft:sandstone_stairs;0",
            "ice_variant;snow;minecraft:ice;0;minecraft:air;0;minecraft:snow;0;minecraft:quartz_stairs;0",
            "mossy_variant;water;minecraft:mossy_cobblestone;0;minecraft:torch;0;minecraft:double_stone_slab;0;minecraft:stone_stairs;0",
            "jungle_variant;foliage;minecraft:log;3;minecraft:web;0;minecraft:dirt;0;minecraft:jungle_stairs;0",
            "nether_variant;nether;minecraft:netherrack;0;minecraft:glowstone;0;minecraft:soul_sand;0;minecraft:nether_brick_stairs;0"
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
            }
        }
    }
}
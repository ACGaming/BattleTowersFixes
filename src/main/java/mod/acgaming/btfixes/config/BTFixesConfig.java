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
    @Config.RequiresWorldRestart
    @Config.Name("Cobblestone Variants")
    @Config.Comment("If cobblestone tower variants should be allowed to generate.")
    public static boolean cobblestoneVariant = true;

    @Config.RequiresWorldRestart
    @Config.Name("Stone Variants")
    @Config.Comment("If stone tower variants should be allowed to generate.")
    public static boolean stoneVariant = true;

    @Config.RequiresWorldRestart
    @Config.Name("Sandstone Variants")
    @Config.Comment("If sandstone tower variants should be allowed to generate.")
    public static boolean sandstoneVariant = true;

    @Config.RequiresWorldRestart
    @Config.Name("Mossy Variants")
    @Config.Comment("If mossy tower variants should be allowed to generate.")
    public static boolean mossyVariant = true;

    @Config.RequiresWorldRestart
    @Config.Name("Ice Variants")
    @Config.Comment("If ice tower variants should be allowed to generate.")
    public static boolean iceVariant = true;

    @Config.RequiresWorldRestart
    @Config.Name("Jungle Variants")
    @Config.Comment("If jungle tower variants should be allowed to generate.")
    public static boolean jungleVariant = true;

    @Config.RequiresWorldRestart
    @Config.Name("Nether Variants")
    @Config.Comment("If nether tower variants should be allowed to generate.")
    public static boolean netherVariant = true;

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
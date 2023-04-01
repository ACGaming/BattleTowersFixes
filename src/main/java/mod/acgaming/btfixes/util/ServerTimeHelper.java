package mod.acgaming.btfixes.util;

import net.minecraft.world.DimensionType;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class ServerTimeHelper
{
    public static long startingTime = -1;

    @SubscribeEvent
    public static void onLoad(WorldEvent.Load event)
    {
        if (event.getWorld().provider.getDimensionType() == DimensionType.OVERWORLD && startingTime == -1)
        {
            startingTime = event.getWorld().getTotalWorldTime();
        }
    }

    @SubscribeEvent
    public static void onUnload(WorldEvent.Unload event)
    {
        if (event.getWorld().provider.getDimensionType() == DimensionType.OVERWORLD)
        {
            startingTime = -1;
        }
    }
}
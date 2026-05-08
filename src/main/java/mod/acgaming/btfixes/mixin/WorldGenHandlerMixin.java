package mod.acgaming.btfixes.mixin;

import java.util.Random;

import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.event.world.WorldEvent;
import atomicstryker.battletowers.common.WorldGenHandler;
import mod.acgaming.btfixes.config.BTFixesConfig;
import mod.acgaming.btfixes.util.ServerTimeHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = WorldGenHandler.class, remap = false)
public class WorldGenHandlerMixin
{
    @Inject(method = "generate", at = @At("HEAD"), cancellable = true)
    private void btfixes$generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider, CallbackInfo ci)
    {
        if (btfixes$shouldBlockDimension(world))
        {
            ci.cancel();
            return;
        }
        if (world.getTotalWorldTime() < ServerTimeHelper.startingTime + BTFixesConfig.bufferTime)
        {
            ci.cancel();
        }
    }

    @Inject(method = "eventWorldLoad", at = @At("HEAD"), cancellable = true)
    private void btfixes$eventWorldLoad(WorldEvent.Load event, CallbackInfo ci)
    {
        /*
         * Keep BattleTowers' original worldMap cleanup behavior.
         * Normally this happens inside eventWorldLoad(), but if we cancel at HEAD
         * for a blocked dimension, preserve this part manually.
         */
        if (WorldGenHandler.shouldClearWorldMap)
        {
            WorldGenHandler.wipeWorldHandles();
            WorldGenHandler.shouldClearWorldMap = false;
        }
        if (btfixes$shouldBlockDimension(event.getWorld()))
        {
            ci.cancel();
        }
    }

    @Inject(method = "eventWorldSave", at = @At("HEAD"), cancellable = true)
    private void btfixes$eventWorldSave(WorldEvent.Save event, CallbackInfo ci)
    {
        if (btfixes$shouldBlockDimension(event.getWorld()))
        {
            ci.cancel();
        }
    }

    private static boolean btfixes$shouldBlockDimension(World world)
    {
        if (world == null || world.provider == null)
        {
            return false;
        }
        boolean dimensionIsListed = btfixes$isDimensionListed(world.provider.getDimension());

        if (BTFixesConfig.dimensionListIsWhitelist)
        {
            return !dimensionIsListed;
        }
        return dimensionIsListed;
    }

    private static boolean btfixes$isDimensionListed(int dimension)
    {
        if (BTFixesConfig.dimensionBlacklist == null)
        {
            return false;
        }
        for (int listedDimension : BTFixesConfig.dimensionBlacklist)
        {
            if (dimension == listedDimension)
            {
                return true;
            }
        }
        return false;
    }
}
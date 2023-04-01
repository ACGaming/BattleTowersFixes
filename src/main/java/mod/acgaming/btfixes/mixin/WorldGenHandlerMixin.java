package mod.acgaming.btfixes.mixin;

import java.util.Random;

import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;

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
    @Inject(method = "generate", at = @At(value = "HEAD"), cancellable = true)
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider, CallbackInfo ci)
    {
        if (world.getTotalWorldTime() < ServerTimeHelper.startingTime + BTFixesConfig.bufferTime) ci.cancel();
    }
}
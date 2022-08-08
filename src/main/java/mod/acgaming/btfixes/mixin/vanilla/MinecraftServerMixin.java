package mod.acgaming.btfixes.mixin.vanilla;

import net.minecraft.server.MinecraftServer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin
{
    /**
     * @reason Disable initial world chunk load. This makes world load much faster, but in exchange
     * the player may see incomplete chunks (like when teleporting to a new area).
     * @author ZombieHDGaming
     */
    @Overwrite
    public void initialWorldChunkLoad() {}
}
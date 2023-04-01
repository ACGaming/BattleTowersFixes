package mod.acgaming.btfixes.mixin;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import atomicstryker.battletowers.common.AS_TowerDestroyer;
import mod.acgaming.btfixes.AS_TowerDestroyerNew;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = AS_TowerDestroyer.class, remap = false)
public abstract class AS_TowerDestroyerMixin
{
    @Shadow
    private World world;
    @Shadow
    private int xGolem;
    @Shadow
    private int zGolem;

    @Shadow
    protected abstract double yCoord();

    /**
     * @author ACGaming
     * @reason Include destruction blacklist
     */
    @Overwrite
    private void cleanUpStragglerBlocks()
    {
        int ytemp = (int) this.yCoord();
        for (int xIterator = -8; xIterator < 8; ++xIterator)
        {
            for (int zIterator = -8; zIterator < 8; ++zIterator)
            {
                for (int yIterator = 1; yIterator < 9; ++yIterator)
                {
                    Block block = this.world.getBlockState(new BlockPos(this.xGolem + xIterator, ytemp + yIterator, this.zGolem + zIterator)).getBlock();
                    if (block != Blocks.AIR && !AS_TowerDestroyerNew.destructionBlacklist.contains(block))
                    {
                        this.world.setBlockToAir(new BlockPos(this.xGolem + xIterator, ytemp + yIterator, this.zGolem + zIterator));
                    }
                }
            }
        }
    }
}
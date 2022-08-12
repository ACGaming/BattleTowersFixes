package mod.acgaming.btfixes.mixin;

import java.util.Arrays;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import atomicstryker.battletowers.common.AS_WorldGenTower;
import mod.acgaming.btfixes.config.BTFixesConfig;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(AS_WorldGenTower.class)
public abstract class AS_WorldGenTowerMixin
{
    @Shadow
    private static int candidatecount;
    @Shadow
    private static int[][] candidates;
    @Shadow
    @Final
    private static int maxHoleDepthInBase;
    @Shadow
    public String failState;

    /**
     * @author ACGaming
     * @reason Better configurability
     */
    @Overwrite(remap = false)
    public int getChosenTowerOrdinal(World world, Random random, int ix, int jy, int kz)
    {
        AS_WorldGenTower.TowerTypes towerChosen;
        int countWater = 0;
        int countSand = 0;
        int countSnow = 0;
        int countFoliage = 0;
        int countElse = 0;

        for (int ccounter = 0; ccounter < candidatecount; ccounter++)
        {
            int[] pair = candidates[ccounter];
            int checkBlockY = getSurfaceBlockHeight(world, ix + pair[0], kz + pair[1]);

            Block ID = world.getBlockState(new BlockPos(ix + pair[0], checkBlockY, kz + pair[1])).getBlock();

            if (world.getBlockState(new BlockPos(ix + pair[0], checkBlockY + 1, kz + pair[1])).getBlock() == Blocks.SNOW || ID == Blocks.ICE)
            {
                countSnow++;
            }
            else if (ID == Blocks.SAND || ID == Blocks.SANDSTONE)
            {
                countSand++;
            }
            else if (ID == Blocks.WATER)
            {
                countWater++;
            }
            else if (ID == Blocks.LEAVES || ID == Blocks.WATERLILY || ID == Blocks.LOG || ID == Blocks.LOG2)
            {
                countFoliage++;
            }
            else countElse++;

            if (Math.abs(checkBlockY - jy) > maxHoleDepthInBase)
            {
                failState = "Uneven Surface, diff value: " + Math.abs(checkBlockY - jy);
                return -1;
            }

            for (int ycounter2 = 1; ycounter2 <= 3; ycounter2++)
            {
                ID = world.getBlockState(new BlockPos(ix + pair[0], (checkBlockY + ycounter2), kz + pair[1])).getBlock();
                if (isBannedBlockID(ID))
                {
                    failState = "Surface banned Block of ID: " + ID + " at height: " + ycounter2;
                    return -1;
                }
            }

            for (int ycounter = 1; ycounter <= 5; ycounter++)
            {
                ID = world.getBlockState(new BlockPos(ix + pair[0], checkBlockY - ycounter, kz + pair[1])).getBlock();

                if (ID == Blocks.AIR || isBannedBlockID(ID))
                {
                    failState = "Depth check - Banned Block or hole, Depth: " + ycounter + " ID: " + ID;
                    return -1;
                }
            }
        }

        // System.err.println("Snow: "+countSnow+" Sand: "+countSand+" Water:
        // "+countWater+" else: "+countElse);

        int[] nums = {countWater, countSnow, countSand, countFoliage, countElse};
        Arrays.sort(nums);
        int result = nums[nums.length - 1];

        // System.err.println("Picked max value of "+result);

        if (countSand == result)
        {
            towerChosen = AS_WorldGenTower.TowerTypes.SandStone;
        }
        else if (countSnow == result)
        {
            towerChosen = AS_WorldGenTower.TowerTypes.Ice;
        }
        else if (countWater == result)
        {
            towerChosen = AS_WorldGenTower.TowerTypes.CobbleStoneMossy;
        }
        else if (countFoliage == result)
        {
            towerChosen = AS_WorldGenTower.TowerTypes.Jungle;
        }
        else // standard is cobblestone, really rare should be nether
        {
            if (random.nextInt(10) == 0)
            {
                towerChosen = AS_WorldGenTower.TowerTypes.Netherrack;
            }
            else
            {
                towerChosen = (random.nextInt(5) == 0) ? AS_WorldGenTower.TowerTypes.SmoothStone : AS_WorldGenTower.TowerTypes.CobbleStone;
            }
        }

        if (!BTFixesConfig.netherVariant && towerChosen == AS_WorldGenTower.TowerTypes.Netherrack) return -1;
        else if (!BTFixesConfig.iceVariant && towerChosen == AS_WorldGenTower.TowerTypes.Ice) return -1;
        else if (!BTFixesConfig.sandstoneVariant && towerChosen == AS_WorldGenTower.TowerTypes.SandStone) return -1;
        else if (!BTFixesConfig.jungleVariant && towerChosen == AS_WorldGenTower.TowerTypes.Jungle) return -1;
        else if (!BTFixesConfig.mossyVariant && towerChosen == AS_WorldGenTower.TowerTypes.CobbleStoneMossy) return -1;
        else if (!BTFixesConfig.stoneVariant && towerChosen == AS_WorldGenTower.TowerTypes.SmoothStone) return -1;
        else if (!BTFixesConfig.cobblestoneVariant && towerChosen == AS_WorldGenTower.TowerTypes.CobbleStone) return -1;
        else return towerChosen.ordinal();
    }

    @Shadow
    protected abstract int getSurfaceBlockHeight(World world, int x, int z);

    @Shadow
    protected abstract boolean isBannedBlockID(Block iD);
}
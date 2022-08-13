package mod.acgaming.btfixes.mixin;

import java.util.*;

import net.minecraft.block.Block;
import net.minecraft.block.BlockTorch;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import atomicstryker.battletowers.common.*;
import mod.acgaming.btfixes.AS_WorldGenTowerNew;
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
        int towerChosen;
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

        int[] nums = {countWater, countSnow, countSand, countFoliage, countElse};
        Arrays.sort(nums);
        int result = nums[nums.length - 1];

        if (AS_WorldGenTowerNew.mapNameTheme.containsValue("sand") && countSand == result)
        {
            towerChosen = AS_WorldGenTowerNew.getRandomTowerOrdinal("sand", random);
        }
        else if (AS_WorldGenTowerNew.mapNameTheme.containsValue("snow") && countSnow == result)
        {
            towerChosen = AS_WorldGenTowerNew.getRandomTowerOrdinal("snow", random);
        }
        else if (AS_WorldGenTowerNew.mapNameTheme.containsValue("water") && countWater == result)
        {
            towerChosen = AS_WorldGenTowerNew.getRandomTowerOrdinal("water", random);
        }
        else if (AS_WorldGenTowerNew.mapNameTheme.containsValue("foliage") && countFoliage == result)
        {
            towerChosen = AS_WorldGenTowerNew.getRandomTowerOrdinal("foliage", random);
        }
        else
        {
            if (AS_WorldGenTowerNew.mapNameTheme.containsValue("nether") && random.nextInt(10) == 0)
            {
                towerChosen = AS_WorldGenTowerNew.getRandomTowerOrdinal("nether", random);
            }
            else
            {
                towerChosen = AS_WorldGenTowerNew.getRandomTowerOrdinal("default", random);
            }
        }

        return towerChosen;
    }

    /**
     * @author ACGaming
     * @reason Redirect to new tower types
     */
    @Overwrite(remap = false)
    @SuppressWarnings("deprecation")
    public void generate(World world, Random random, int ix, int jy, int kz, int towerchoice, boolean underground)
    {
        Block towerWallBlockID = AS_WorldGenTowerNew.wallBlockID.get(towerchoice);
        int towerWallMeta = AS_WorldGenTowerNew.wallBlockMetaData.get(towerchoice);

        Block towerLightBlockID = AS_WorldGenTowerNew.lightBlockID.get(towerchoice);
        int towerLightMeta = AS_WorldGenTowerNew.lightBlockMetaData.get(towerchoice);

        Block towerFloorBlockID = AS_WorldGenTowerNew.floorBlockID.get(towerchoice);
        int towerFloorMeta = AS_WorldGenTowerNew.floorBlockMetaData.get(towerchoice);

        Block towerStairBlockID = AS_WorldGenTowerNew.stairBlockID.get(towerchoice);
        int towerStairMeta = AS_WorldGenTowerNew.stairBlockMetaData.get(towerchoice);

        int startingHeight = underground ? Math.max(jy - 70, 15) : jy - 6;
        int maximumHeight = underground ? jy + 7 : 120;
        int floor = 1;
        int builderHeight = startingHeight;
        boolean topFloor = false;

        for (; builderHeight < maximumHeight; builderHeight += 7)
        {
            if (builderHeight + 7 >= maximumHeight)
            {
                topFloor = true;
            }

            for (int floorIterator = 0; floorIterator < 7; floorIterator++)
            {
                if (floor == 1 && floorIterator < 4)
                {
                    floorIterator = 4;
                }
                for (int xIterator = -7; xIterator < 7; xIterator++)
                {
                    for (int zIterator = -7; zIterator < 7; zIterator++)
                    {
                        int iCurrent = xIterator + ix;
                        int jCurrent = floorIterator + builderHeight;
                        int zCurrent = zIterator + kz;

                        if (zIterator == -7)
                        {
                            if (xIterator > -5 && xIterator < 4)
                            {
                                buildWallPiece(world, iCurrent, jCurrent, zCurrent, towerWallBlockID, floor, floorIterator);
                            }
                            continue;
                        }
                        if (zIterator == -6 || zIterator == -5)
                        {
                            if (xIterator == -5 || xIterator == 4)
                            {
                                buildWallPiece(world, iCurrent, jCurrent, zCurrent, towerWallBlockID, floor, floorIterator);
                                continue;
                            }
                            if (zIterator == -6)
                            {
                                if (xIterator == (floorIterator + 1) % 7 - 3)
                                {
                                    if (!(underground && floor == 1))
                                    {
                                        world.setBlockState(new BlockPos(iCurrent, jCurrent, zCurrent), towerStairBlockID.getStateFromMeta(towerStairMeta));
                                    }
                                    if (floorIterator == 5)
                                    {
                                        world.setBlockState(new BlockPos(iCurrent - 7, jCurrent, zCurrent), towerFloorBlockID.getStateFromMeta(towerFloorMeta));
                                    }
                                    if (floorIterator == 6 && topFloor)
                                    {
                                        buildWallPiece(world, iCurrent, jCurrent, zCurrent, towerWallBlockID, floor, floorIterator);
                                    }
                                    continue;
                                }
                                if (xIterator < 4 && xIterator > -5)
                                {
                                    world.setBlockState(new BlockPos(iCurrent, jCurrent, zCurrent), Blocks.AIR.getDefaultState());
                                }
                                continue;
                            }
                            if (xIterator <= -5 || xIterator >= 5)
                            {
                                continue;
                            }
                            if (floorIterator != 0 && floorIterator != 6 || xIterator != -4 && xIterator != 3)
                            {
                                if (floorIterator == 5 && (xIterator == 3 || xIterator == -4))
                                {
                                    buildFloorPiece(world, iCurrent, jCurrent, zCurrent, towerFloorBlockID, towerFloorMeta);
                                }
                                else
                                {
                                    buildWallPiece(world, iCurrent, jCurrent, zCurrent, towerWallBlockID, floor, floorIterator);
                                }
                            }
                            else
                            {
                                world.setBlockState(new BlockPos(iCurrent, jCurrent, zCurrent), Blocks.AIR.getDefaultState());
                            }
                            continue;
                        }
                        if (zIterator == -4 || zIterator == -3 || zIterator == 2 || zIterator == 3)
                        {
                            if (xIterator == -6 || xIterator == 5)
                            {
                                buildWallPiece(world, iCurrent, jCurrent, zCurrent, towerWallBlockID, floor, floorIterator);
                                continue;
                            }
                            if (xIterator <= -6 || xIterator >= 5)
                            {
                                continue;
                            }
                            if (floorIterator == 5)
                            {
                                buildFloorPiece(world, iCurrent, jCurrent, zCurrent, towerFloorBlockID, towerFloorMeta);
                                continue;
                            }
                            if (world.getBlockState(new BlockPos(iCurrent, jCurrent, zCurrent)).getBlock() != Blocks.CHEST)
                            {
                                world.setBlockState(new BlockPos(iCurrent, jCurrent, zCurrent), Blocks.AIR.getDefaultState());
                            }
                            continue;
                        }
                        if (zIterator < 2)
                        {
                            if (xIterator == -7 || xIterator == 6)
                            {
                                if (floorIterator < 0 || floorIterator > 3 || underground || zIterator != -1 && zIterator != 0)
                                {
                                    buildWallPiece(world, iCurrent, jCurrent, zCurrent, towerWallBlockID, floor, floorIterator);
                                }
                                else
                                {
                                    world.setBlockState(new BlockPos(iCurrent, jCurrent, zCurrent), Blocks.AIR.getDefaultState());
                                }
                                continue;
                            }
                            if (floorIterator == 5)
                            {
                                buildFloorPiece(world, iCurrent, jCurrent, zCurrent, towerFloorBlockID, towerFloorMeta);
                            }
                            else
                            {
                                world.setBlockState(new BlockPos(iCurrent, jCurrent, zCurrent), Blocks.AIR.getDefaultState());
                            }
                            continue;
                        }
                        if (zIterator == 4)
                        {
                            if (xIterator == -5 || xIterator == 4)
                            {
                                buildWallPiece(world, iCurrent, jCurrent, zCurrent, towerWallBlockID, floor, floorIterator);
                                continue;
                            }
                            if (xIterator <= -5 || xIterator >= 4)
                            {
                                continue;
                            }
                            if (floorIterator == 5)
                            {
                                buildFloorPiece(world, iCurrent, jCurrent, zCurrent, towerFloorBlockID, towerFloorMeta);
                            }
                            else
                            {
                                world.setBlockState(new BlockPos(iCurrent, jCurrent, zCurrent), Blocks.AIR.getDefaultState());
                            }
                            continue;
                        }
                        if (zIterator == 5)
                        {
                            if (xIterator == -4 || xIterator == -3 || xIterator == 2 || xIterator == 3)
                            {
                                buildWallPiece(world, iCurrent, jCurrent, zCurrent, towerWallBlockID, floor, floorIterator);
                                continue;
                            }
                            if (xIterator <= -3 || xIterator >= 2)
                            {
                                continue;
                            }
                            if (floorIterator == 5)
                            {
                                buildFloorPiece(world, iCurrent, jCurrent, zCurrent, towerFloorBlockID, towerFloorMeta);
                            }
                            else
                            {
                                buildWallPiece(world, iCurrent, jCurrent, zCurrent, towerWallBlockID, floor, floorIterator);
                            }
                            continue;
                        }
                        if (xIterator <= -3 || xIterator >= 2)
                        {
                            continue;
                        }
                        if (floorIterator < 0 || floorIterator > 3 || xIterator != -1 && xIterator != 0)
                        {
                            buildWallPiece(world, iCurrent, jCurrent, zCurrent, towerWallBlockID, floor, floorIterator);
                        }
                        else
                        {
                            buildWallPiece(world, iCurrent, jCurrent, zCurrent, towerWallBlockID, floor, floorIterator);
                        }
                    }

                }
            }

            if (floor == 2)
            {
                world.setBlockState(new BlockPos(ix + 3, builderHeight, kz - 5), towerWallBlockID.getStateFromMeta(towerWallMeta));
                world.setBlockState(new BlockPos(ix + 3, builderHeight - 1, kz - 5), towerWallBlockID.getStateFromMeta(towerWallMeta));
            }
            if ((!underground && topFloor) || (underground && floor == 1))
            {
                AS_EntityGolem entitygolem = new AS_EntityGolem(world, towerchoice);
                entitygolem.setLocationAndAngles(ix + 0.5D, builderHeight + 6, kz + 0.5D, world.rand.nextFloat() * 360F, 0.0F);
                world.spawnEntity(entitygolem);
            }
            else
            {
                world.setBlockState(new BlockPos(ix + 2, builderHeight + 6, kz + 2), Blocks.MOB_SPAWNER.getDefaultState());
                TileEntityMobSpawner tileentitymobspawner = (TileEntityMobSpawner) world.getTileEntity(new BlockPos(ix + 2, builderHeight + 6, kz + 2));
                if (tileentitymobspawner != null)
                {
//                        if (!DungeonTweaksCompat.isLoaded)
//                        {
                    tileentitymobspawner.getSpawnerBaseLogic().setEntityId(getMobType(world.rand));
//                        }
//                        else
//                        {
//                            DungeonTweaksCompat.fireDungeonSpawn(tileentitymobspawner, world, random, towerChosen);
//                        }
                }

                world.setBlockState(new BlockPos(ix - 3, builderHeight + 6, kz + 2), Blocks.MOB_SPAWNER.getDefaultState());
                tileentitymobspawner = (TileEntityMobSpawner) world.getTileEntity(new BlockPos(ix - 3, builderHeight + 6, kz + 2));
                if (tileentitymobspawner != null)
                {
//                        if (!DungeonTweaksCompat.isLoaded)
//                        {
                    tileentitymobspawner.getSpawnerBaseLogic().setEntityId(getMobType(world.rand));
//                        }
//                        else
//                        {
//                            DungeonTweaksCompat.fireDungeonSpawn(tileentitymobspawner, world, random, towerChosen);
//                        }
                }
            }
            // chest pedestal
            world.setBlockState(new BlockPos(ix, builderHeight + 6, kz + 3), towerFloorBlockID.getDefaultState());
            world.setBlockState(new BlockPos(ix - 1, builderHeight + 6, kz + 3), towerFloorBlockID.getDefaultState());

            if (builderHeight + 56 >= 120 && floor == 1)
            {
                floor = 2;
            }

            // chest
            TowerStageItemManager floorChestManager;
            if (!underground)
            {
                floorChestManager = topFloor ? WorldGenHandler.getTowerStageManagerForFloor(10) : WorldGenHandler.getTowerStageManagerForFloor(floor);
            }
            else
            {
                floorChestManager = floor == 1 ? WorldGenHandler.getTowerStageManagerForFloor(10) : WorldGenHandler.getTowerStageManagerForFloor(Math.abs(11 - floor));
            }

            for (int chestlength = 0; chestlength < 2; chestlength++)
            {
                world.setBlockState(new BlockPos(ix - chestlength, builderHeight + 7, kz + 3), Blocks.CHEST.getStateFromMeta(2));
                TileEntityChest tileentitychest = (TileEntityChest) world.getTileEntity(new BlockPos(ix - chestlength, builderHeight + 7, kz + 3));
                if (tileentitychest != null)
                {
                    int count = underground ? AS_BattleTowersCore.instance.itemGenerateAttemptsPerFloor * 2 : AS_BattleTowersCore.instance.itemGenerateAttemptsPerFloor;
                    List<ItemStack> generatedStacks = floorChestManager.getStageItemStacks(world, world.rand, tileentitychest, count);
                    List<Integer> freeSlots = new ArrayList<>(tileentitychest.getSizeInventory());
                    for (int i = 0; i < tileentitychest.getSizeInventory(); i++)
                    {
                        freeSlots.add(i);
                    }
                    Iterator<ItemStack> iterator = generatedStacks.iterator();
                    while (iterator.hasNext() && !freeSlots.isEmpty())
                    {
                        Integer slot = freeSlots.get(world.rand.nextInt(freeSlots.size()));
                        freeSlots.remove(slot);
                        tileentitychest.setInventorySlotContents(slot, iterator.next());
                    }
                }
            }

            if (towerLightBlockID == Blocks.TORCH)
            {
                world.setBlockState(new BlockPos(ix + 3, builderHeight + 2, kz - 6), towerLightBlockID.getStateFromMeta(0).withProperty(BlockTorch.FACING, EnumFacing.SOUTH), 2);
                world.setBlockState(new BlockPos(ix - 4, builderHeight + 2, kz - 6), towerLightBlockID.getStateFromMeta(0).withProperty(BlockTorch.FACING, EnumFacing.SOUTH), 2);
                world.setBlockState(new BlockPos(ix + 1, builderHeight + 2, kz - 4), towerLightBlockID.getStateFromMeta(0).withProperty(BlockTorch.FACING, EnumFacing.SOUTH), 2);
                world.setBlockState(new BlockPos(ix - 2, builderHeight + 2, kz - 4), towerLightBlockID.getStateFromMeta(0).withProperty(BlockTorch.FACING, EnumFacing.SOUTH), 2);
            }
            else
            {
                world.setBlockState(new BlockPos(ix + 3, builderHeight + 2, kz - 6), towerLightBlockID.getStateFromMeta(towerLightMeta));
                world.setBlockState(new BlockPos(ix - 4, builderHeight + 2, kz - 6), towerLightBlockID.getStateFromMeta(towerLightMeta));
                world.setBlockState(new BlockPos(ix + 1, builderHeight + 2, kz - 4), towerLightBlockID.getStateFromMeta(towerLightMeta));
                world.setBlockState(new BlockPos(ix - 2, builderHeight + 2, kz - 4), towerLightBlockID.getStateFromMeta(towerLightMeta));
            }

            for (int l3 = 0; l3 < (floor * 4 + towerchoice) - 8 && !topFloor; l3++)
            {
                int k4 = 5 - world.rand.nextInt(12);
                int k5 = builderHeight + 5;
                int j6 = 5 - world.rand.nextInt(10);
                if (j6 < -2 && k4 < 4 && k4 > -5 && k4 != 1 && k4 != -2)
                {
                    continue;
                }
                k4 += ix;
                j6 += kz;
                if (world.getBlockState(new BlockPos(k4, k5, j6)).getBlock() == towerFloorBlockID && world.getBlockState(new BlockPos(k4, k5 + 1, j6)).getBlock() != Blocks.MOB_SPAWNER)
                {
                    world.setBlockState(new BlockPos(k4, k5, j6), Blocks.AIR.getDefaultState());
                }
            }
            floor++;
        }
        System.out.println("Dimension " + world.provider.getDimension() + " Battle Tower spawned at [ " + ix + " | " + kz + " ], underground: " + underground);
    }

    @Shadow
    protected abstract void buildFloorPiece(World world, int i, int j, int k, Block towerFloorBlockID, int towerFloorMeta);

    @Shadow
    protected abstract ResourceLocation getMobType(Random random);

    @Shadow
    protected abstract void buildWallPiece(World world, int i, int j, int k, Block towerWallBlockID, int floor, int floorIterator);

    @Shadow
    protected abstract int getSurfaceBlockHeight(World world, int x, int z);

    @Shadow
    protected abstract boolean isBannedBlockID(Block iD);
}
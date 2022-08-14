package mod.acgaming.btfixes;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import mod.acgaming.btfixes.config.BTFixesConfig;

public class AS_WorldGenTowerNew
{
    public static List<String> themeList = new ArrayList<>();
    public static List<Block> wallBlockID = new ArrayList<>();
    public static List<Integer> wallBlockMetaData = new ArrayList<>();
    public static List<Block> lightBlockID = new ArrayList<>();
    public static List<Integer> lightBlockMetaData = new ArrayList<>();
    public static List<Block> floorBlockID = new ArrayList<>();
    public static List<Integer> floorBlockMetaData = new ArrayList<>();
    public static List<Block> stairBlockID = new ArrayList<>();
    public static List<Integer> stairBlockMetaData = new ArrayList<>();

    public static void initTowerVariants()
    {
        themeList.clear();
        wallBlockID.clear();
        wallBlockMetaData.clear();
        lightBlockID.clear();
        lightBlockMetaData.clear();
        floorBlockID.clear();
        floorBlockMetaData.clear();
        stairBlockID.clear();
        stairBlockMetaData.clear();

        String[] variantsSplit;

        BTFixes.LOGGER.info("*** REGISTERING BATTLE TOWER VARIANTS ***");

        for (String variant : BTFixesConfig.towerVariants)
        {
            variantsSplit = variant.split(";");

            if (!variantsSplit[0].matches("default|sand|red_sand|snow|water|foliage|rare")) throw new IllegalArgumentException("[Battle Towers Fixes] Invalid theme name '" + variantsSplit[1] + "' detected, check your configs!");

            BTFixes.LOGGER.info("Theme: " + variantsSplit[0]);
            themeList.add(variantsSplit[0]);

            BTFixes.LOGGER.info("Wall Block: " + getBlock(variantsSplit[1]));
            wallBlockID.add(getBlock(variantsSplit[1]));
            wallBlockMetaData.add(Integer.parseInt(variantsSplit[2]));

            BTFixes.LOGGER.info("Light Block: " + getBlock(variantsSplit[3]));
            lightBlockID.add(getBlock(variantsSplit[3]));
            lightBlockMetaData.add(Integer.parseInt(variantsSplit[4]));

            BTFixes.LOGGER.info("Floor Block: " + getBlock(variantsSplit[5]));
            floorBlockID.add(getBlock(variantsSplit[5]));
            floorBlockMetaData.add(Integer.parseInt(variantsSplit[6]));

            BTFixes.LOGGER.info("Stair Block: " + getBlock(variantsSplit[7]));
            stairBlockID.add(getBlock(variantsSplit[7]));
            stairBlockMetaData.add(Integer.parseInt(variantsSplit[8]));

            BTFixes.LOGGER.info("*****************************************");
        }
    }

    public static Block getBlock(String blockName)
    {
        return ForgeRegistries.BLOCKS.getValue(new ResourceLocation(blockName));
    }

    public static int getRandomTowerOrdinal(String theme, Random random)
    {
        List<Integer> indices = new ArrayList<>();
        int randomIndex;

        for (int i = 0; i < themeList.size(); i++)
        {
            if (themeList.get(i).equals(theme)) indices.add(i);
        }

        if (indices.size() > 1) randomIndex = indices.get(random.nextInt(indices.size()));
        else randomIndex = indices.get(0);

        return randomIndex;
    }
}
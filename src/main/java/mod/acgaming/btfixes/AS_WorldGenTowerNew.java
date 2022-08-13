package mod.acgaming.btfixes;

import java.util.*;

import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import mod.acgaming.btfixes.config.BTFixesConfig;

public class AS_WorldGenTowerNew
{
    public static Map<String, String> mapNameTheme = new HashMap<>();
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
        String[] variantsSplit;

        for (String variant : BTFixesConfig.towerVariants)
        {
            variantsSplit = variant.split(";");

            if (mapNameTheme.containsKey(variantsSplit[0])) throw new IllegalArgumentException("[Battle Towers Fixes] Duplicate unique name '" + variantsSplit[0] + "' detected, check your configs!");
            if (!variantsSplit[1].matches("default|sand|snow|water|foliage|nether")) throw new IllegalArgumentException("[Battle Towers Fixes] Invalid theme name '" + variantsSplit[1] + "' detected, check your configs!");

            System.out.println(variantsSplit[0] + " " + variantsSplit[1]);
            mapNameTheme.put(variantsSplit[0], variantsSplit[1]);

            System.out.println(getBlock(variantsSplit[2]));
            wallBlockID.add(getBlock(variantsSplit[2]));
            wallBlockMetaData.add(Integer.parseInt(variantsSplit[3]));

            System.out.println(getBlock(variantsSplit[4]));
            lightBlockID.add(getBlock(variantsSplit[4]));
            lightBlockMetaData.add(Integer.parseInt(variantsSplit[5]));

            System.out.println(getBlock(variantsSplit[6]));
            floorBlockID.add(getBlock(variantsSplit[6]));
            floorBlockMetaData.add(Integer.parseInt(variantsSplit[7]));

            System.out.println(getBlock(variantsSplit[8]));
            stairBlockID.add(getBlock(variantsSplit[8]));
            stairBlockMetaData.add(Integer.parseInt(variantsSplit[9]));
        }
    }

    public static Block getBlock(String blockName)
    {
        return ForgeRegistries.BLOCKS.getValue(new ResourceLocation(blockName));
    }

    public static int getRandomTowerOrdinal(String theme, Random random)
    {
        List<Integer> indices = new ArrayList<>();
        int randomIndex = 0;

        for (int i = 0; i < mapNameTheme.size(); i++)
        {
            if (mapNameTheme.containsValue(theme))
            {
                indices.add(i);
            }
        }

        if (indices.size() > 1) randomIndex = indices.get(random.nextInt(indices.size()));
        return randomIndex;
    }
}
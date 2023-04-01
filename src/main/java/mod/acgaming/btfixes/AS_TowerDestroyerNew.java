package mod.acgaming.btfixes;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import mod.acgaming.btfixes.config.BTFixesConfig;

public class AS_TowerDestroyerNew
{
    public static List<Block> destructionBlacklist = new ArrayList<>();

    public static void initDestructionBlacklist()
    {
        destructionBlacklist.clear();
        try
        {
            BTFixes.LOGGER.info("*** INITIALIZING DESTRUCTION BLACKLIST ***");
            for (String config : BTFixesConfig.destructionBlacklist)
            {
                ResourceLocation resLoc = new ResourceLocation(config);
                if (ForgeRegistries.BLOCKS.containsKey(resLoc))
                {
                    destructionBlacklist.add(ForgeRegistries.BLOCKS.getValue(resLoc));
                    BTFixes.LOGGER.info("Added: " + resLoc);
                }
            }
            BTFixes.LOGGER.info("******************************************");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        BTFixes.LOGGER.info("Destruction blacklist initialized");
    }
}
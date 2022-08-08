package mod.acgaming.btfixes.mixin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTable;

import atomicstryker.battletowers.common.TowerStageItemManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(TowerStageItemManager.class)
public class TowerStageItemManagerMixin
{
    @Shadow
    private Object[] itemID;
    @Shadow
    private int[] itemDamage;
    @Shadow
    private int[] chanceToSpawn;
    @Shadow
    private int[] minAmount;
    @Shadow
    private int[] maxAmount;

    /**
     * @author ACGaming
     * @reason Fix parsing
     */
    @Overwrite(remap = false)
    public List<ItemStack> getStageItemStacks(World world, Random rand, TileEntityChest teChest, int count)
    {
        List<ItemStack> result = new ArrayList<>();
        LootTable loottable;
        int number;

        for (int index = 0; result.size() < count && index < this.itemID.length; ++index)
        {
            if (this.itemID[index] instanceof Item && rand.nextInt(100) < this.chanceToSpawn[index])
            {
                result.add(new ItemStack((Item) this.itemID[index], this.minAmount[index] + rand.nextInt(this.maxAmount[index]), this.itemDamage[index]));
            }
            else if (this.itemID[index] instanceof Block && rand.nextInt(100) < this.chanceToSpawn[index])
            {
                result.add(new ItemStack((Block) this.itemID[index], this.minAmount[index] + rand.nextInt(this.maxAmount[index]), this.itemDamage[index]));
            }
            else if (this.itemID[index] instanceof String)
            {
                String[] split = ((String) this.itemID[index]).split(":");
                if (split.length == 4) loottable = world.getLootTableManager().getLootTableFromLocation(new ResourceLocation(split[1], split[2]));
                else loottable = world.getLootTableManager().getLootTableFromLocation(new ResourceLocation(split[1]));
                List<ItemStack> generatedItems = loottable.generateLootForPools(rand, (new LootContext.Builder((WorldServer) world)).build());
                if (split.length == 4) number = Integer.parseInt(split[3]);
                else number = Integer.parseInt(split[2]);
                if (number > 0)
                {
                    while (generatedItems.size() > number)
                    {
                        Collections.shuffle(generatedItems);
                        generatedItems.remove(generatedItems.size() - 1);
                    }
                    result.addAll(generatedItems);
                }
            }
        }
        return result;
    }
}
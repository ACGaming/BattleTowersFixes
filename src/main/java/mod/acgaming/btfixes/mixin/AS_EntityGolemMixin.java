package mod.acgaming.btfixes.mixin;

import net.minecraft.entity.monster.EntityMob;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

import atomicstryker.battletowers.common.AS_EntityGolem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AS_EntityGolem.class)
public abstract class AS_EntityGolemMixin extends EntityMob
{
    public AS_EntityGolemMixin(World worldIn)
    {
        super(worldIn);
    }

    @Inject(method = "onUpdate", at = @At(value = "HEAD"), cancellable = true)
    public void onUpdate(CallbackInfo ci)
    {
        if (!this.world.isRemote && this.world.getDifficulty() == EnumDifficulty.PEACEFUL)
        {
            ci.cancel();
        }
    }
}
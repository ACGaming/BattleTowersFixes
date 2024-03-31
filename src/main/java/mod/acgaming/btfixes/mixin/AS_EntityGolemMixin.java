package mod.acgaming.btfixes.mixin;

import net.minecraft.entity.monster.EntityMob;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

import atomicstryker.battletowers.common.AS_EntityGolem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
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

    @Shadow
    public abstract void setDormant();

    @Inject(method = "onUpdate", at = @At(value = "HEAD"), cancellable = true)
    public void onUpdate(CallbackInfo ci)
    {
        if (this.world.getDifficulty() == EnumDifficulty.PEACEFUL)
        {
            this.setDormant();
            super.onUpdate();
            ci.cancel();
        }
    }
}
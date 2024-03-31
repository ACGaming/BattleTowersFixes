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

@Mixin(value = AS_EntityGolem.class, remap = false)
public abstract class AS_EntityGolemMixin extends EntityMob
{
    protected AS_EntityGolemMixin(World worldIn)
    {
        super(worldIn);
    }

    @Shadow
    public abstract void setDormant();

    @Inject(method = "checkForVictim", at = @At(value = "HEAD"), cancellable = true)
    public void checkForVictim(CallbackInfo ci)
    {
        if (this.world.getDifficulty() == EnumDifficulty.PEACEFUL)
        {
            this.setAttackTarget(null);
            this.setDormant();
            ci.cancel();
        }
    }

    @Inject(method = "setAwake", at = @At(value = "HEAD"), cancellable = true)
    public void setAwake(CallbackInfo ci)
    {
        if (this.world.getDifficulty() == EnumDifficulty.PEACEFUL)
        {
            ci.cancel();
        }
    }
}
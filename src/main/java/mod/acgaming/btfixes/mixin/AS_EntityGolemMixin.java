package mod.acgaming.btfixes.mixin;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

import atomicstryker.battletowers.common.AS_EntityGolem;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = AS_EntityGolem.class, remap = false)
public abstract class AS_EntityGolemMixin extends EntityMob
{
    @Shadow
    private int noTargetCountdown;

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
            this.setDormant();
            ci.cancel();
        }
    }

    @Inject(method = "onUpdate", at = @At(value = "HEAD"))
    private void onUpdate(CallbackInfo ci)
    {
        if (!this.world.isRemote)
        {
            EntityLivingBase target = this.getAttackTarget();

            if (target != null && !target.isEntityAlive())
            {
                this.setAttackTarget(null);
            }
        }
    }

    @Redirect(method = "onUpdate", at = @At(value = "FIELD", target = "Latomicstryker/battletowers/common/AS_EntityGolem;noTargetCountdown:I", opcode = Opcodes.PUTFIELD, ordinal = 1))
    private void btfixes$fixTargetCooldown(AS_EntityGolem instance, int value)
    {
        EntityLivingBase target = this.getAttackTarget();

        if (target != null && target.isEntityAlive())
        {
            this.noTargetCountdown = value;
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

    @Inject(method = "setDormant", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/datasync/EntityDataManager;set(Lnet/minecraft/network/datasync/DataParameter;Ljava/lang/Object;)V"))
    private void setDormant(CallbackInfo ci)
    {
        this.setAttackTarget(null);
    }
}
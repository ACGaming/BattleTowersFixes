package mod.acgaming.btfixes.mixin.vanilla;

import net.minecraft.entity.monster.EntityMob;

import atomicstryker.battletowers.common.AS_EntityGolem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityMob.class)
public class EntityMobMixin
{
    @Inject(method = "onUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/monster/EntityMob;setDead()V"), cancellable = true)
    public void onUpdate(CallbackInfo ci)
    {
        if ((Object) this instanceof AS_EntityGolem) ci.cancel();
    }
}
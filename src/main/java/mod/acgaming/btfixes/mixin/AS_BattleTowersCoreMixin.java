package mod.acgaming.btfixes.mixin;

import atomicstryker.battletowers.common.AS_BattleTowersCore;
import mod.acgaming.btfixes.AS_TowerDestroyerNew;
import mod.acgaming.btfixes.AS_WorldGenTowerNew;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = AS_BattleTowersCore.class, remap = false)
public class AS_BattleTowersCoreMixin
{
    @Inject(method = "load", at = @At(value = "TAIL"))
    public void load(CallbackInfo ci)
    {
        AS_WorldGenTowerNew.initTowerVariants();
        AS_TowerDestroyerNew.initDestructionBlacklist();
    }
}
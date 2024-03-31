package mod.acgaming.btfixes.util;

import net.minecraft.entity.monster.EntityMob;

public class Reference
{
    public static boolean isEntityGolem(EntityMob entityMob)
    {
        try
        {
            Class<?> golemClass = Class.forName("atomicstryker.battletowers.common.AS_EntityGolem");
            return golemClass.isInstance(entityMob);
        }
        catch (ClassNotFoundException ignored) {}
        return false;
    }
}
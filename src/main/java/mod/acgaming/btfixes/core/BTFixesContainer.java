package mod.acgaming.btfixes.core;

import com.google.common.eventbus.EventBus;
import net.minecraftforge.fml.common.DummyModContainer;
import net.minecraftforge.fml.common.LoadController;
import net.minecraftforge.fml.common.ModMetadata;

public class BTFixesContainer extends DummyModContainer
{
    public BTFixesContainer()
    {
        super(new ModMetadata());
        ModMetadata meta = this.getMetadata();
        meta.modId = "rpglootreduxcore";
        meta.name = "RPGLoot Redux Core";
        meta.description = "Core functionality of RPGLoot Redux";
        meta.version = "1.12.2-1.0.0";
        meta.authorList.add("ACGaming");
    }

    @Override
    public boolean registerBus(EventBus bus, LoadController controller)
    {
        bus.register(this);
        return true;
    }
}
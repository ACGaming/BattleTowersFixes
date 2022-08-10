package mod.acgaming.btfixes;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(modid = BTFixes.MODID,
    name = BTFixes.NAME,
    version = BTFixes.VERSION,
    acceptedMinecraftVersions = "[1.12.2]",
    dependencies = "required-after:mixinbooter;required-after:battletowers")
public class BTFixes
{
    public static final String MODID = "btfixes";
    public static final String NAME = "BTFixes";
    public static final String VERSION = "1.12.2-1.0.2";
    public static final Logger LOGGER = LogManager.getLogger();

    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        LOGGER.info("BTFixes initialized");
    }
}
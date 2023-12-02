package mods.thecomputerizer.specifiedspawning.core;

import mods.thecomputerizer.theimpossiblelibrary.util.file.LogUtil;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;

public class Constants {
    public static final String MODID = "specifiedspawning";
    public static final String NAME = "Specified Spawning";
    public static final String VERSION = "0.5.0";
    public static final String DEPENDENCIES = "required-after:forge@[14.23.5.2860,);required-after:mixinbooter@[8.1,);" +
            "required-after:theimpossiblelibrary@[0.3.0,);";
    public static final Random RANDOM = new Random();
    public static final Logger LOGGER = LogManager.getLogger(NAME);

    public static void logVerbose(Level level, String msg, Object ... parameters) {
        if(ConfigManager.INSTANCE.isMoreLogging()) LogUtil.logLevel(LOGGER,level,msg,parameters);
    }
}

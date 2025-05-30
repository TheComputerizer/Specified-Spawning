package mods.thecomputerizer.specifiedspawning.core;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;

public class Constants {
    
    public static final String MODID = "specifiedspawning";
    public static final String NAME = "Specified Spawning";
    public static final String VERSION = "0.6.2";
    public static final String DEPENDENCIES = "required-after:forge@[14.23.5.2860,);" +
            "required-after:mixinbooter@[8.1,);" +
            "required-after:theimpossiblelibrary@[0.4.0,);";
    public static final boolean PRODUCTION_ENV = false;
    public static final Random RANDOM = new Random();
    public static final Logger LOGGER = LogManager.getLogger(NAME);

    public static void logVerbose(Level level, String msg, Object ... parameters) {
        if(ConfigManager.isMoreLogging()) LOGGER.log(level,msg,parameters);
    }
}
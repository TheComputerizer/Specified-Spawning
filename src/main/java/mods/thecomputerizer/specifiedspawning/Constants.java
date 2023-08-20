package mods.thecomputerizer.specifiedspawning;

import mods.thecomputerizer.theimpossiblelibrary.util.file.LogUtil;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

public class Constants {
    public static final String MODID = "specifiedspawning";
    public static final String NAME = "Specified Spawning";
    public static final String VERSION = "0.1.0";
    public static final String DEPENDENCIES = "required-after:forge@[14.23.5.2860,);required-after:theimpossiblelibrary;";
    public static final Logger LOGGER = LogManager.getLogger(NAME);
    public static final File CONFIG = ConfigManager.getOrMakeConfigFile();

    public static void logVerbose(Level level,String msg, Object ... parameters) {
        if(ConfigManager.INSTANCE.isMoreLogging()) LogUtil.logLevel(LOGGER,level,msg,parameters);
    }
}

package mods.thecomputerizer.specifiedspawning;

import mods.thecomputerizer.theimpossiblelibrary.util.file.FileUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class Constants {
    public static final String MODID = "specifiedspawning";
    public static final String NAME = "Specified Spawning";
    public static final String VERSION = "0.1.0";
    public static final String DEPENDENCIES = "required-after:forge@[14.23.5.2860,);required-after:theimpossiblelibrary;";
    public static final Logger LOGGER = LogManager.getLogger(NAME);
    public static final File CONFIG = getOrMakeConfigFile();

    private static File getOrMakeConfigFile() {
        String path = "config/"+Constants.MODID+".toml";
        File file = new File(path);
        if(!file.exists()) FileUtil.writeLinesToFile(file,getHeaderLines(),false);
        return file;
    }

    private static List<String> getHeaderLines() {
        return Arrays.asList("# Please refer to the wiki page located at https://github.com/TheComputerizer/Specified-Spawning/wiki",
                "# or the discord server located at https://discord.gg/FZHXFYp8fc",
                "# for any specific questions you might have regarding this config file","");
    }
}

package mods.thecomputerizer.specifiedspawning;

import mods.thecomputerizer.specifiedspawning.core.Constants;
import mods.thecomputerizer.theimpossiblelibrary.common.toml.Holder;
import mods.thecomputerizer.theimpossiblelibrary.common.toml.Table;
import mods.thecomputerizer.theimpossiblelibrary.common.toml.TomlPart;
import mods.thecomputerizer.theimpossiblelibrary.util.file.FileUtil;
import mods.thecomputerizer.theimpossiblelibrary.util.file.TomlUtil;
import org.apache.logging.log4j.Level;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class ConfigManager {

    public static final File CONFIG = getOrMakeConfigFile();

    public static File getOrMakeConfigFile() {
        String path = "config/"+Constants.MODID+".toml";
        File file = new File(path);
        if(!file.exists()) FileUtil.writeLinesToFile(file,getDefaultLines(),false);
        return file;
    }

    private static List<String> getDefaultLines() {
        return Arrays.asList("# Please refer to the wiki page located at https://github.com/TheComputerizer/Specified-Spawning/wiki",
                "# or the discord server located at https://discord.gg/FZHXFYp8fc",
                "# for any specific questions you might have regarding this config file",
                "",
                "",
                "# General Config Options",
                "",
                "# Write more things to the log. Helpful for debugging purposes. Default value is false",
                "more_logging = false",
                "");
    }

    public static ConfigManager INSTANCE;

    public static void loadInstance() {
        try {
            INSTANCE = new ConfigManager(TomlUtil.readFully(CONFIG));
            if(INSTANCE.moreLogging) {
                Constants.LOGGER.info("Parsing config file at {}", CONFIG.getPath());
                int lineNum = 1;
                for (String line : INSTANCE.parsedConfig.toLines(TomlPart.COMMENT, TomlPart.BLANK_LINE)) {
                    Constants.logVerbose(Level.DEBUG, "Parsed config line [{}]: '{}'", lineNum, line);
                    lineNum++;
                }
            }
        } catch (IOException ex) {
            Constants.LOGGER.error("Failed to parse config file at {}!", CONFIG.getPath(),ex);
        }
    }

    private final Holder parsedConfig;
    private final boolean moreLogging;

    private ConfigManager(Holder parsedConfig) {
        this.parsedConfig = parsedConfig;
        this.moreLogging = this.parsedConfig.getValOrDefault("more_logging",false);
    }

    public boolean isMoreLogging() {
        return this.moreLogging;
    }

    public Collection<Table> getTables() {
        return this.parsedConfig.getTables().values();
    }
}

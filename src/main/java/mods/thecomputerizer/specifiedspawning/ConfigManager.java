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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

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
                "",
                "# For organizational purposes if you so choose, you can add extra config files to 'config/SpecifiedSpawning'.",
                "# Add the paths to those extra configs here in the order that you want them to be loaded. Do not include",
                "# 'config/SpecifiedSpawning' or '.toml' in your file paths as those are handles automatically",
                "# This file will always be loaded first",
                "other_configs = [  ]",
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
    private final List<Holder> otherConfigs;
    private final boolean moreLogging;

    private ConfigManager(Holder parsedConfig) {
        this.parsedConfig = parsedConfig;
        this.otherConfigs = new ArrayList<>();
        this.moreLogging = this.parsedConfig.getValOrDefault("more_logging",false);
        collectOtherConfigs(this.parsedConfig.getValOrDefault("other_configs",new ArrayList<>()));
    }

    private void collectOtherConfigs(List<String> filePaths) {
        for(String path : filePaths) {
            File file = FileUtil.generateNestedFile("config/SpecifiedSpawning/"+path+".toml",false);
            if(Objects.nonNull(file)) {
                Holder holder;
                try {
                    holder = TomlUtil.readFully(file);
                } catch (IOException ex) {
                    Constants.LOGGER.error("Failed to parse other config file at {}!",file.getPath(),ex);
                    holder = null;
                }
                if(Objects.nonNull(holder)) this.otherConfigs.add(holder);
            }
        }
    }

    public boolean isMoreLogging() {
        return this.moreLogging;
    }

    public List<Table> getTables() {
        List<Table> ret = new ArrayList<>(this.parsedConfig.getTables().values());
        for(Holder other : this.otherConfigs)
            ret.addAll(other.getTables().values());
        return ret;
    }
}

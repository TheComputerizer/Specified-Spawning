package mods.thecomputerizer.specifiedspawning.core;

import mods.thecomputerizer.shadowed.moandjiezana.toml.Toml;
import mods.thecomputerizer.specifiedspawning.rules.group.SpawnGroup;
import mods.thecomputerizer.theimpossiblelibrary.common.toml.Holder;
import mods.thecomputerizer.theimpossiblelibrary.common.toml.Table;
import mods.thecomputerizer.theimpossiblelibrary.common.toml.TomlPart;
import mods.thecomputerizer.theimpossiblelibrary.util.file.FileUtil;
import mods.thecomputerizer.theimpossiblelibrary.util.file.TomlUtil;
import net.minecraft.launchwrapper.Launch;
import org.apache.logging.log4j.Level;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.*;

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

    public static void loadSpawnGroups() {
        try {
            Launch.classLoader.addURL(new File("mods/theimpossiblelibrary-1.12.2-"+Constants.TIL_VERSION+".jar").toURI().toURL());
        } catch (MalformedURLException e) {
            throw new RuntimeException("Could not add The Impossible Library to the Class Loader >:(");
        }
        Toml toml = TomlUtil.get(CONFIG);
        for(Toml groupToml : toml.getTables("group"))
            new SpawnGroup.Builder(groupToml);
    }

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
    private final Map<Integer,Holder> otherConfigs;
    private final boolean moreLogging;

    private ConfigManager(Holder parsedConfig) {
        this.parsedConfig = parsedConfig;
        this.otherConfigs = new HashMap<>();
        this.moreLogging = this.parsedConfig.getValOrDefault("more_logging",false);
        collectOtherConfigs(this.parsedConfig.getValOrDefault("other_configs",new ArrayList<>()));
    }

    private void collectOtherConfigs(List<String> filePaths) {
        int index = 1;
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
                if(Objects.nonNull(holder)) {
                    this.otherConfigs.put(index,holder);
                    index++;
                }
            }
        }
    }

    public boolean isMoreLogging() {
        return this.moreLogging;
    }

    public Map<Integer,List<Table>> getTableMap() {
        Map<Integer,List<Table>> ret = new HashMap<>();
        Map<Integer,Table> tableMap = this.parsedConfig.getTables();
        ret.put(0,new ArrayList<>(tableMap.values()));
        int max = getMaxIndex(tableMap);
        for(int i=1;i<=this.otherConfigs.size();i++) {
            Holder other = this.otherConfigs.get(i);
            if(Objects.nonNull(other)) {
                tableMap = other.getTables();
                ret.put(max,new ArrayList<>(tableMap.values()));
                max = max+getMaxIndex(tableMap);
            }
        }
        return ret;
    }

    private int getMaxIndex(Map<Integer,Table> tableMap) {
        int max = 1;
        for(Integer maxIndex : tableMap.keySet())
            if(maxIndex>max) max = maxIndex;
        return max+1;
    }
}

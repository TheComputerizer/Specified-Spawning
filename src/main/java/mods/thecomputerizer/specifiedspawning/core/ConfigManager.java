package mods.thecomputerizer.specifiedspawning.core;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import mods.thecomputerizer.specifiedspawning.rules.group.SpawnGroup.Builder;
import mods.thecomputerizer.theimpossiblelibrary.api.io.FileHelper;
import mods.thecomputerizer.theimpossiblelibrary.api.toml.Toml;

import javax.annotation.Nullable;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

import static mods.thecomputerizer.specifiedspawning.core.Constants.LOGGER;
import static mods.thecomputerizer.specifiedspawning.core.Constants.MODID;
import static net.minecraft.launchwrapper.Launch.classLoader;
import static org.apache.logging.log4j.Level.DEBUG;

public class ConfigManager {

    public static final String FOLDER_PATH = "config/SpecifiedSpawning";
    public static File CONFIG;
    public static ConfigManager INSTANCE;
    
    private static void addSpawnGroupBuilder(Toml group) {
        new Builder(group.hasEntry("name") ? group.getValueString("name") : "hostile",
                group.hasEntry("count") ? Optional.of(group.getValueInt("count",0)) : Optional.empty(),
                optionalBool(group,"peaceful"), optionalBool(group,"animal"),
                optionalBool(group,"aquatic"));
    }
    
    public static void checkLibraryLoaded() {
        try {
            Class.forName("mods.thecomputerizer.theimpossiblelibrary.api.toml.Toml");
        } catch(Throwable ignored) { //An exception here should mean the libary source isn't loaded for some reason
            try {
                classLoader.addURL(getLibraryLocation());
                Class.forName("mods.thecomputerizer.theimpossiblelibrary.api.toml.Toml");
            } catch(Throwable t) {
                LOGGER.error("Library not found!",t);
            }
        }
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
                "# For organizational purposes if you so choose, you can add extra config files to '"+FOLDER_PATH+"'.",
                "# Add the paths to those extra configs here in the order that you want them to be loaded. Do not include",
                "# '"+FOLDER_PATH+"' or '.toml' in your file paths as those are handles automatically",
                "# This file will always be loaded first",
                "other_configs = [  ]",
                "");
    }
    
    public static File getOrMakeConfigFile() {
        String path = "config/"+MODID+".toml";
        File file = new File(path);
        if(!file.exists()) FileHelper.writeLines(file,getDefaultLines(),false);
        return file;
    }

    private static File findTILFile() {
        File mods = new File("mods");
        if(mods.exists()) {
            File[] files = mods.listFiles((dir,name) -> name.contains("theimpossiblelibrary") && name.endsWith(".jar"));
            if(Objects.nonNull(files) && files.length>0) return files[0];
        }
        return null;
    }
    
    private static URL getLibraryLocation() {
        try {
            File file = findTILFile();
            if(Objects.nonNull(file)) return file.toURI().toURL();
            else throw new MalformedURLException("what");
        } catch (MalformedURLException e) {
            throw new RuntimeException("Could not add The Impossible Library to the Class Loader >:(");
        }
    }

    public static void loadInstance() {
        checkLibraryLoaded();
        try {
            INSTANCE = new ConfigManager(Toml.readFile(CONFIG));
            if(INSTANCE.moreLogging) {
                LOGGER.info("Parsing config file at {}",CONFIG.getPath());
                int lineNum = 1;
                List<String> lines = new ArrayList<>();
                INSTANCE.parsedConfig.write(lines,0,true);
                for(String line : lines) {
                    Constants.logVerbose(DEBUG,"Parsed config line [{}]: '{}'",lineNum,line);
                    lineNum++;
                }
            }
        } catch(Exception ex) {
            LOGGER.error("Failed to parse config file at {}!",CONFIG.getPath(),ex);
        }
    }
    
    public static void loadSpawnGroups(boolean isProdEnv) {
        if(isProdEnv) classLoader.addURL(getLibraryLocation());
        CONFIG = getOrMakeConfigFile();
        Toml toml;
        try {
            toml = Toml.readFile(CONFIG);
        } catch(Throwable t) {
            LOGGER.error("Failed to parse TOML file from {}",CONFIG);
            return;
        }
        if(toml.hasTable("group"))
            for(Toml groupToml : toml.getTableArray("group"))
                addSpawnGroupBuilder(groupToml);
    }
    
    private static Optional<Boolean> optionalBool(Toml group, String var) {
        return group.hasEntry(var) ? Optional.of(group.getValueBool(var,false)) : Optional.empty();
    }

    private final Toml parsedConfig;
    private final Map<Integer,Toml> otherConfigs;
    private final boolean moreLogging;

    private ConfigManager(Toml parsedConfig) {
        this.parsedConfig = parsedConfig;
        this.otherConfigs = new HashMap<>();
        this.moreLogging = this.parsedConfig.getValueBool("more_logging",false);
        collectOtherConfigs(this.parsedConfig.getValueArray("other_configs"));
    }

    private void collectOtherConfigs(@Nullable List<?> filePaths) {
        if(Objects.isNull(filePaths)) return;
        int index = 1;
        for(Object path : filePaths) {
            if(Objects.isNull(path)) continue;
            File file = FileHelper.get(new File(FOLDER_PATH),path+".toml",false);
            if(Objects.nonNull(file)) {
                Toml toml;
                try {
                    toml = Toml.readFile(file);
                } catch(Exception ex) {
                    LOGGER.error("Failed to parse other config file at {}!",file.getPath(),ex);
                    toml = null;
                }
                if(Objects.nonNull(toml)) {
                    this.otherConfigs.put(index, toml);
                    index++;
                }
            }
        }
    }

    public boolean isMoreLogging() {
        return this.moreLogging;
    }

    public Map<Integer,List<Toml>> getTableMap() {
        Map<Integer,List<Toml>> ret = new Int2ObjectOpenHashMap<>();
        List<Toml> tables = this.parsedConfig.getAllTables();
        ret.put(0,new ArrayList<>(tables));
        int max = tables.size();
        for(int i=1;i<=this.otherConfigs.size();i++) {
            Toml other = this.otherConfigs.get(i);
            if(Objects.nonNull(other)) {
                List<Toml> otherTables = other.getAllTables();
                ret.put(max,new ArrayList<>(otherTables));
                max = max+otherTables.size();
            }
        }
        return ret;
    }
}
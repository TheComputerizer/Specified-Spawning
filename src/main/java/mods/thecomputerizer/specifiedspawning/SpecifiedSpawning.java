package mods.thecomputerizer.specifiedspawning;

import mods.thecomputerizer.specifiedspawning.core.ConfigManager;
import mods.thecomputerizer.specifiedspawning.core.Constants;
import mods.thecomputerizer.specifiedspawning.core.SpawnManager;
import mods.thecomputerizer.specifiedspawning.rules.RuleManager;
import mods.thecomputerizer.specifiedspawning.world.ReloadCommand;
import mods.thecomputerizer.specifiedspawning.world.SHHooks;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.*;
import org.apache.logging.log4j.Level;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Mod(modid = Constants.MODID, name = Constants.NAME, version = Constants.VERSION, dependencies = Constants.DEPENDENCIES)
public class SpecifiedSpawning {

    private static final Set<String> CHECKED_MODS = Collections.synchronizedSet(new HashSet<>());
    private static final Set<String> LOADED_MODS = Collections.synchronizedSet(new HashSet<>());
    public static boolean RULES_BUILT = false;

    public SpecifiedSpawning() {
        ConfigManager.loadInstance();
        RuleManager.parseRuleTables();
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        RuleManager.parseRuleSelectors();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {}

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        SpawnManager.loadDefaultSpawnEntries();
        SpawnManager.buildSpawnGroups();
    }

    @Mod.EventHandler
    public void loadComplete(FMLLoadCompleteEvent event) {
        RuleManager.buildRules();
        RULES_BUILT = true;
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new ReloadCommand());
        RuleManager.testSpecificBiome();
        RuleManager.testEnumIterator();
    }

    public static void reload() {
        RULES_BUILT = false;
        Constants.logVerbose(Level.INFO,"Reloading configs for world");
        if(isModLoaded("scalingdifficulty")) SHHooks.setLoadedScalingDifficultySelector(false);
        SpawnManager.clear();
        RuleManager.clear();
        ConfigManager.loadInstance();
        RuleManager.parseRuleTables();
        RuleManager.parseRuleSelectors();
        SpawnManager.buildSpawnGroups();
        RuleManager.buildRules();
        RULES_BUILT = true;
    }

    public static boolean isModLoaded(String modid) {
        if(!CHECKED_MODS.contains(modid)) {
            CHECKED_MODS.add(modid);
            if(Loader.isModLoaded(modid)) LOADED_MODS.add(modid);
        }
        return LOADED_MODS.contains(modid);
    }
}

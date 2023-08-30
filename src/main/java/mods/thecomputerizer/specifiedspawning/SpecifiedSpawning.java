package mods.thecomputerizer.specifiedspawning;

import mods.thecomputerizer.specifiedspawning.core.Constants;
import mods.thecomputerizer.specifiedspawning.rules.RuleManager;
import mods.thecomputerizer.specifiedspawning.util.EnumUtil;
import mods.thecomputerizer.specifiedspawning.world.ReloadCommand;
import mods.thecomputerizer.specifiedspawning.world.SHHooks;
import mods.thecomputerizer.specifiedspawning.world.SpawnManager;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Mod(modid = Constants.MODID, name = Constants.NAME, version = Constants.VERSION, dependencies = Constants.DEPENDENCIES)
public class SpecifiedSpawning {

    private static final Set<String> CHECKED_MODS = Collections.synchronizedSet(new HashSet<>());
    private static final Set<String> LOADED_MODS = Collections.synchronizedSet(new HashSet<>());

    public SpecifiedSpawning() {
        EnumUtil.buildDefaultConstructorTypes();
        ConfigManager.loadInstance();
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        RuleManager.parseRuleTables();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        SpawnManager.loadDefaults();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        SpawnManager.buildSpawnGroups();
        RuleManager.buildRules();
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new ReloadCommand());
    }

    public static void reload() {
        if(isModLoaded("scalingdifficulty")) SHHooks.setLoadedScalingDifficultySelector(false);
        SpawnManager.clear();
        RuleManager.clear();
        ConfigManager.loadInstance();
        RuleManager.parseRuleTables();
        SpawnManager.loadDefaultSpawnGroups();
        SpawnManager.buildSpawnGroups();
        RuleManager.buildRules();
    }

    public static boolean isModLoaded(String modid) {
        if(!CHECKED_MODS.contains(modid)) {
            CHECKED_MODS.add(modid);
            if(Loader.isModLoaded(modid)) LOADED_MODS.add(modid);
        }
        return LOADED_MODS.contains(modid);
    }
}

package mods.thecomputerizer.specifiedspawning;

import mods.thecomputerizer.specifiedspawning.rules.RuleManager;
import mods.thecomputerizer.specifiedspawning.util.EnumUtil;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

@Mod(modid = Constants.MODID, name = Constants.NAME, version = Constants.VERSION, dependencies = Constants.DEPENDENCIES)
public class SpecifiedSpawning {

    public SpecifiedSpawning() {
        EnumUtil.buildDefaultConstructorTypes();
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        RuleManager.parseConfig();
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

    }

    public static void reload() {
        SpawnManager.clear();
        RuleManager.clear();
        RuleManager.parseConfig();
        SpawnManager.loadDefaults();
        SpawnManager.buildSpawnGroups();
        RuleManager.buildRules();
    }
}

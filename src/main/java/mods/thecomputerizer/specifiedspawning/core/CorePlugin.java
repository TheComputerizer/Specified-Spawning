package mods.thecomputerizer.specifiedspawning.core;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.Name;
import zone.rong.mixinbooter.IEarlyMixinLoader;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static mods.thecomputerizer.specifiedspawning.core.Constants.LOGGER;
import static mods.thecomputerizer.specifiedspawning.core.Constants.NAME;

@Name(NAME+" Core")
public class CorePlugin implements IFMLLoadingPlugin, IEarlyMixinLoader {

    static {
        LOGGER.info("Loading core plugin");
        ConfigManager.loadSpawnGroups(false);
    }

    @Override public String[] getASMTransformerClass() {
        return new String[0];
    }

    @Override public String getModContainerClass() {
        return CoreContainer.class.getName();
    }
    
    @Override public @Nullable String getSetupClass() {
        return null;
    }

    @Override public void injectData(Map<String,Object> data) {}

    @Override public String getAccessTransformerClass() {
        return null;
    }

    @Override public List<String> getMixinConfigs() {
        return Collections.singletonList("specifiedspawning_vanilla.mixin.json");
    }
}
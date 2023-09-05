package mods.thecomputerizer.specifiedspawning.core;

import zone.rong.mixinbooter.ILateMixinLoader;

import java.util.Collections;
import java.util.List;

@SuppressWarnings("unused")
public class ModMixinLoader implements ILateMixinLoader {

    @Override
    public List<String> getMixinConfigs() {
        return Collections.singletonList("specifiedspawning_mods.mixin.json");
    }
}

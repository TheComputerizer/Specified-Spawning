package mods.thecomputerizer.specifiedspawning.core;

import mods.thecomputerizer.theimpossiblelibrary.api.core.annotation.IndirectCallers;
import zone.rong.mixinbooter.ILateMixinLoader;

import java.util.Collections;
import java.util.List;

@IndirectCallers
public class ModMixinLoader implements ILateMixinLoader {

    @Override public List<String> getMixinConfigs() {
        return Collections.singletonList("specifiedspawning_mods.mixin.json");
    }
}
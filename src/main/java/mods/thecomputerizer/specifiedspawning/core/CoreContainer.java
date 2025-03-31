package mods.thecomputerizer.specifiedspawning.core;

import com.google.common.eventbus.EventBus;
import net.minecraftforge.fml.common.DummyModContainer;
import net.minecraftforge.fml.common.LoadController;
import net.minecraftforge.fml.common.ModMetadata;

import static mods.thecomputerizer.specifiedspawning.core.Constants.VERSION;

public class CoreContainer extends DummyModContainer {

    public CoreContainer() {
        super(new ModMetadata());
        ModMetadata meta = this.getMetadata();
        meta.modId = "specifiedspawningcore";
        meta.name = "Specified Spawning Mixin Loader";
        meta.description = "Loads Early Mixin Stuff for Specified Spawning";
        meta.version = VERSION;
        meta.authorList.add("The_Computerizer");
    }

    @SuppressWarnings("UnstableApiUsage")
    @Override public boolean registerBus(EventBus bus, LoadController controller) {
        bus.register(this);
        return true;
    }
}

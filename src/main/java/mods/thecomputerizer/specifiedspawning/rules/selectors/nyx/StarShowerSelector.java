package mods.thecomputerizer.specifiedspawning.rules.selectors.nyx;

import de.ellpeck.nyx.capabilities.NyxWorld;
import de.ellpeck.nyx.lunarevents.StarShower;
import mods.thecomputerizer.specifiedspawning.rules.selectors.AbstractSelector;
import mods.thecomputerizer.specifiedspawning.rules.selectors.SelectorType;
import mods.thecomputerizer.theimpossiblelibrary.api.toml.Toml;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Objects;

import static mods.thecomputerizer.specifiedspawning.rules.selectors.SelectorType.STARSHOWER;

public class StarShowerSelector extends AbstractSelector {

    public static StarShowerSelector makeSelector(Toml table) {
        if(Objects.isNull(table)) return null;
        return new StarShowerSelector(false);
    }

    protected StarShowerSelector(boolean isInverted) {
        super(isInverted);
    }

    @Override protected boolean isValidInner(BlockPos pos, World world, String ruleDescriptor) {
        return NyxWorld.get(world).currentEvent instanceof StarShower;
    }

    @Override public boolean isNonBasic() {
        return true;
    }

    @Override public SelectorType getType() {
        return STARSHOWER;
    }
}
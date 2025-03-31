package mods.thecomputerizer.specifiedspawning.rules.selectors.nyx;

import de.ellpeck.nyx.capabilities.NyxWorld;
import de.ellpeck.nyx.lunarevents.HarvestMoon;
import mods.thecomputerizer.specifiedspawning.rules.selectors.AbstractSelector;
import mods.thecomputerizer.specifiedspawning.rules.selectors.SelectorType;
import mods.thecomputerizer.theimpossiblelibrary.api.toml.Toml;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Objects;

import static mods.thecomputerizer.specifiedspawning.rules.selectors.SelectorType.HARVESTMOON;

public class HarvestmoonSelector extends AbstractSelector {

    public static HarvestmoonSelector makeSelector(Toml table) {
        if(Objects.isNull(table)) return null;
        return new HarvestmoonSelector(false);
    }

    protected HarvestmoonSelector(boolean isInverted) {
        super(isInverted);
    }

    @Override protected boolean isValidInner(BlockPos pos, World world, String ruleDescriptor) {
        return NyxWorld.get(world).currentEvent instanceof HarvestMoon;
    }

    @Override public boolean isNonBasic() {
        return true;
    }

    @Override public SelectorType getType() {
        return HARVESTMOON;
    }
}
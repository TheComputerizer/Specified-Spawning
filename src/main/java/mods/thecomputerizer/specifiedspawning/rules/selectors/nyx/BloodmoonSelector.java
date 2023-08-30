package mods.thecomputerizer.specifiedspawning.rules.selectors.nyx;

import de.ellpeck.nyx.capabilities.NyxWorld;
import de.ellpeck.nyx.lunarevents.BloodMoon;
import mods.thecomputerizer.specifiedspawning.rules.selectors.AbstractSelector;
import mods.thecomputerizer.specifiedspawning.rules.selectors.SelectorType;
import mods.thecomputerizer.theimpossiblelibrary.common.toml.Table;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Objects;

public class BloodmoonSelector extends AbstractSelector {

    public static BloodmoonSelector makeSelector(Table table) {
        if(Objects.isNull(table)) return null;
        return new BloodmoonSelector(false);
    }

    protected BloodmoonSelector(boolean isInverted) {
        super(isInverted);
    }

    @Override
    protected boolean isValidInner(BlockPos pos, World world, String ruleDescriptor) {
        return NyxWorld.get(world).currentEvent instanceof BloodMoon;
    }

    @Override
    public boolean isNonBasic() {
        return true;
    }

    @Override
    public SelectorType getType() {
        return SelectorType.BLOODMOON;
    }
}

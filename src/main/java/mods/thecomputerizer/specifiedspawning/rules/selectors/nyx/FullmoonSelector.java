package mods.thecomputerizer.specifiedspawning.rules.selectors.nyx;

import de.ellpeck.nyx.capabilities.NyxWorld;
import de.ellpeck.nyx.lunarevents.FullMoon;
import mods.thecomputerizer.specifiedspawning.rules.selectors.AbstractSelector;
import mods.thecomputerizer.specifiedspawning.rules.selectors.SelectorType;
import mods.thecomputerizer.theimpossiblelibrary.common.toml.Table;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Objects;

public class FullmoonSelector extends AbstractSelector {

    public static FullmoonSelector makeSelector(Table table) {
        if(Objects.isNull(table)) return null;
        return new FullmoonSelector(false);
    }

    protected FullmoonSelector(boolean isInverted) {
        super(isInverted);
    }

    @Override
    protected boolean isValidInner(BlockPos pos, World world, String ruleDescriptor) {
        return NyxWorld.get(world).currentEvent instanceof FullMoon;
    }

    @Override
    public boolean isNonBasic() {
        return true;
    }

    @Override
    public SelectorType getType() {
        return SelectorType.FULLMOON;
    }
}
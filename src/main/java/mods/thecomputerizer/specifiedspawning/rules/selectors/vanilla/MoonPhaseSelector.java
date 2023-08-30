package mods.thecomputerizer.specifiedspawning.rules.selectors.vanilla;

import mods.thecomputerizer.specifiedspawning.rules.selectors.AbstractSelector;
import mods.thecomputerizer.specifiedspawning.rules.selectors.SelectorType;
import mods.thecomputerizer.specifiedspawning.util.ParsingUtils;
import mods.thecomputerizer.theimpossiblelibrary.common.toml.Table;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;
import java.util.Objects;

public class MoonPhaseSelector extends AbstractSelector {

    public static MoonPhaseSelector makeSelector(Table table) {
        if(Objects.isNull(table)) return null;
        List<Integer> moonPhase = ParsingUtils.getIntList(table.getVarMap().get("moonPhase"));
        return moonPhase.isEmpty() ? null :
                new MoonPhaseSelector(table.getValOrDefault("inverted",false), moonPhase);
    }

    private final List<Integer> allowedPhases;

    protected MoonPhaseSelector(boolean isInverted, List<Integer> allowedPhases) {
        super(isInverted);
        this.allowedPhases = allowedPhases;
    }

    @Override
    protected boolean isValidInner(BlockPos pos, World world, String ruleDescriptor) {
        return this.allowedPhases.contains(world.getMoonPhase()+1);
    }

    @Override
    public boolean isNonBasic() {
        return true;
    }

    @Override
    public SelectorType getType() {
        return SelectorType.MOONPHASE;
    }
}

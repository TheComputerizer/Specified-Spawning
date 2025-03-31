package mods.thecomputerizer.specifiedspawning.rules.selectors.vanilla;

import mods.thecomputerizer.specifiedspawning.rules.selectors.AbstractSelector;
import mods.thecomputerizer.specifiedspawning.rules.selectors.SelectorType;
import mods.thecomputerizer.specifiedspawning.util.ParsingUtils;
import mods.thecomputerizer.theimpossiblelibrary.api.toml.Toml;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;
import java.util.Objects;

import static mods.thecomputerizer.specifiedspawning.rules.selectors.SelectorType.MOONPHASE;

public class MoonPhaseSelector extends AbstractSelector {

    public static MoonPhaseSelector makeSelector(Toml table) {
        if(Objects.isNull(table)) return null;
        List<Integer> moonPhase = ParsingUtils.getIntList(table.getEntryValuesAsMap().get("moon_phase"));
        return moonPhase.isEmpty() ? null :
                new MoonPhaseSelector(table.getValueBool("inverted",false),moonPhase);
    }

    private final List<Integer> allowedPhases;

    protected MoonPhaseSelector(boolean isInverted, List<Integer> allowedPhases) {
        super(isInverted);
        this.allowedPhases = allowedPhases;
    }

    @Override protected boolean isValidInner(BlockPos pos, World world, String ruleDescriptor) {
        return this.allowedPhases.contains(world.getMoonPhase()+1);
    }

    @Override public boolean isNonBasic() {
        return true;
    }

    @Override public SelectorType getType() {
        return MOONPHASE;
    }
}
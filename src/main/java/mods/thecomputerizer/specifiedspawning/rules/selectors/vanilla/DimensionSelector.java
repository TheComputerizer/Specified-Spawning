package mods.thecomputerizer.specifiedspawning.rules.selectors.vanilla;

import mods.thecomputerizer.specifiedspawning.core.Constants;
import mods.thecomputerizer.specifiedspawning.rules.selectors.AbstractSelector;
import mods.thecomputerizer.specifiedspawning.rules.selectors.SelectorType;
import mods.thecomputerizer.specifiedspawning.util.ParsingUtils;
import mods.thecomputerizer.theimpossiblelibrary.api.text.TextHelper;
import mods.thecomputerizer.theimpossiblelibrary.api.toml.Toml;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;
import java.util.Objects;

import static mods.thecomputerizer.specifiedspawning.rules.selectors.SelectorType.DIMENSION;
import static org.apache.logging.log4j.Level.DEBUG;

public class DimensionSelector extends AbstractSelector {

    public static DimensionSelector makeSelector(Toml table) {
        if(Objects.isNull(table)) return null;
        List<Integer> dimensions = ParsingUtils.getIntList(table.getEntryValuesAsMap().get("dimension"));
        return dimensions.isEmpty() ? null : new DimensionSelector(dimensions);
    }

    private final List<Integer> validDimensions;

    private DimensionSelector(List<Integer> dimensions) {
        super(false);
        this.validDimensions = dimensions;
        Constants.logVerbose(DEBUG,"Instantiated new dimension selector with valid dimensions {}",
                             TextHelper.arrayToString(" ", dimensions.toArray()));
    }

    @Override protected boolean isValidInner(BlockPos pos, World world, String ruleDescriptor) {
        int spawnDim = world.provider.getDimension();
        for(int dim : this.validDimensions)
            if(spawnDim==dim) return true;
        return false;
    }

    @Override public boolean isNonBasic() {
        return true;
    }

    @Override public SelectorType getType() {
        return DIMENSION;
    }

    @Override public String toString() {
        return "Dimension Selector ("+TextHelper.arrayToString(" ",this.validDimensions.toArray())+")";
    }
}

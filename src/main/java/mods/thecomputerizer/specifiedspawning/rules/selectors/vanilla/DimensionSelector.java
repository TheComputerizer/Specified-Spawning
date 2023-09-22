package mods.thecomputerizer.specifiedspawning.rules.selectors.vanilla;

import mods.thecomputerizer.specifiedspawning.core.Constants;
import mods.thecomputerizer.specifiedspawning.rules.selectors.AbstractSelector;
import mods.thecomputerizer.specifiedspawning.rules.selectors.SelectorType;
import mods.thecomputerizer.specifiedspawning.util.ParsingUtils;
import mods.thecomputerizer.theimpossiblelibrary.common.toml.Table;
import mods.thecomputerizer.theimpossiblelibrary.util.TextUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.logging.log4j.Level;

import java.util.List;
import java.util.Objects;

public class DimensionSelector extends AbstractSelector {

    public static DimensionSelector makeSelector(Table table) {
        if(Objects.isNull(table)) return null;
        List<Integer> dimensions = ParsingUtils.getIntList(table.getVarMap().get("dimension"));
        return dimensions.isEmpty() ? null : new DimensionSelector(dimensions);
    }

    private final List<Integer> validDimensions;

    private DimensionSelector(List<Integer> dimensions) {
        super(false);
        this.validDimensions = dimensions;
        Constants.logVerbose(Level.DEBUG,"Instantiated new dimension selector with valid dimensions {}",
                TextUtil.arrayToString(" ",dimensions.toArray()));
    }

    @Override
    protected boolean isValidInner(BlockPos pos, World world, String ruleDescriptor) {
        int spawnDim = world.provider.getDimension();
        for(int dim : this.validDimensions)
            if(spawnDim==dim) return true;
        return false;
    }

    @Override
    public boolean isNonBasic() {
        return true;
    }

    @Override
    public SelectorType getType() {
        return SelectorType.DIMENSION;
    }

    @Override
    public String toString() {
        return "Dimension Selector ("+TextUtil.arrayToString(" ",this.validDimensions.toArray())+")";
    }
}

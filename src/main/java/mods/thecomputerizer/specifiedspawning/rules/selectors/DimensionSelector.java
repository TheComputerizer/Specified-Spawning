package mods.thecomputerizer.specifiedspawning.rules.selectors;

import mods.thecomputerizer.specifiedspawning.Constants;
import mods.thecomputerizer.specifiedspawning.util.ParsingUtils;
import mods.thecomputerizer.theimpossiblelibrary.common.toml.Table;
import mods.thecomputerizer.theimpossiblelibrary.util.TextUtil;
import org.apache.logging.log4j.Level;

import java.util.List;
import java.util.Objects;

public class DimensionSelector implements ISelector<Integer> {

    private final List<Integer> validDimensions;

    public static DimensionSelector makeSelector(Table table) {
        if(Objects.isNull(table)) return null;
        List<Integer> dimensions = ParsingUtils.getIntList(table.getVarMap().get("dimension"));
        return dimensions.isEmpty() ? null : new DimensionSelector(dimensions);
    }

    private DimensionSelector(List<Integer> dimensions) {
        this.validDimensions = dimensions;
        Constants.logVerbose(Level.DEBUG,"Instantiated new dimension selector with valid dimensions {}",
                TextUtil.arrayToString(" ",dimensions.toArray()));
    }

    @Override
    public boolean isValid(Integer spawnDim, String ruleDescriptor) {
        for(int dim : this.validDimensions)
            if(spawnDim==dim) return true;
        return false;
    }

    @Override
    public boolean isBasic() {
        return false;
    }
}

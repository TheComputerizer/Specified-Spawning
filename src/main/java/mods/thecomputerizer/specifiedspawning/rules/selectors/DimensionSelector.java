package mods.thecomputerizer.specifiedspawning.rules.selectors;

import mods.thecomputerizer.specifiedspawning.rules.ParseUtil;
import mods.thecomputerizer.theimpossiblelibrary.common.toml.Table;

import java.util.List;

public class DimensionSelector implements ISelector<Integer> {

    private final List<Integer> validDimensions;

    public static DimensionSelector makeSelector(Table table) {
        return new DimensionSelector(ParseUtil.getIntList(table.getVarMap().get("dimension")));
    }

    private DimensionSelector(List<Integer> dimensions) {
        this.validDimensions = dimensions;
    }

    public boolean isValid(Integer spawnDim) {
        if(this.validDimensions.isEmpty()) return true;
        for(int dim : this.validDimensions)
            if(spawnDim==dim) return true;
        return false;
    }
}

package mods.thecomputerizer.specifiedspawning.rules.selectors;

import mods.thecomputerizer.theimpossiblelibrary.common.toml.Table;

public class LightSelector implements ISelector<Integer> {

    public static LightSelector makeSelector(Table table) {
        return new LightSelector(table.getValOrDefault("min",0),table.getValOrDefault("max",7));
    }

    private final int min;
    private final int max;

    private LightSelector(int min, int max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public boolean isValid(Integer level) {
        return level>=this.min && level<=this.max;
    }



    @Override
    public boolean isBasic() {
        return false;
    }
}

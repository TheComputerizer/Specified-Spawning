package mods.thecomputerizer.specifiedspawning.rules.selectors;

import mods.thecomputerizer.specifiedspawning.Constants;
import mods.thecomputerizer.theimpossiblelibrary.common.toml.Table;
import org.apache.logging.log4j.Level;

import java.util.Objects;

public class HeightSelector implements ISelector<Integer> {

    private final int minY;
    private final int maxY;

    public static HeightSelector makeSelector(Table table) {
        if(Objects.isNull(table)) return null;
        return new HeightSelector(table.getValOrDefault("min",0),table.getValOrDefault("max",255));
    }

    private HeightSelector(int minY, int maxY) {
        Constants.logVerbose(Level.DEBUG,"Instantiated new height selector with min {} and max {}",minY,maxY);
        this.minY = minY;
        this.maxY = maxY;
    }

    @Override
    public boolean isValid(Integer yPos, String ruleDescriptor) {
        return yPos>=this.minY && yPos<=this.maxY;
    }

    @Override
    public boolean isBasic() {
        return false;
    }
}

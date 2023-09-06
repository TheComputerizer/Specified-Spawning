package mods.thecomputerizer.specifiedspawning.rules.selectors.vanilla;

import mods.thecomputerizer.specifiedspawning.rules.selectors.AbstractSelector;
import mods.thecomputerizer.specifiedspawning.rules.selectors.SelectorType;
import mods.thecomputerizer.theimpossiblelibrary.common.toml.Table;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Objects;

public class LightSelector extends AbstractSelector {

    public static LightSelector makeSelector(Table table) {
        if(Objects.isNull(table)) return null;
        return new LightSelector(table.getValOrDefault("inverted",false),
                table.getValOrDefault("min",0),table.getValOrDefault("max",7));
    }

    private final int min;
    private final int max;

    private LightSelector(boolean isInverted, int min, int max) {
        super(isInverted);
        this.min = min;
        this.max = max;
    }

    @Override
    protected boolean isValidInner(BlockPos pos, World world, String ruleDescriptor) {
        int level = world.getLight(pos,true);
        return level>=this.min && level<=this.max;
    }



    @Override
    public boolean isNonBasic() {
        return true;
    }

    @Override
    public SelectorType getType() {
        return SelectorType.LIGHT;
    }
}

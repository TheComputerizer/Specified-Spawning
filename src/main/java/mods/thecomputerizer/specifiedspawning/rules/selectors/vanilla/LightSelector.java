package mods.thecomputerizer.specifiedspawning.rules.selectors.vanilla;

import mods.thecomputerizer.specifiedspawning.rules.selectors.AbstractSelector;
import mods.thecomputerizer.specifiedspawning.rules.selectors.SelectorType;
import mods.thecomputerizer.theimpossiblelibrary.api.toml.Toml;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Objects;

import static mods.thecomputerizer.specifiedspawning.rules.selectors.SelectorType.LIGHT;

public class LightSelector extends AbstractSelector {

    public static LightSelector makeSelector(Toml table) {
        if(Objects.isNull(table)) return null;
        return new LightSelector(table.getValueBool("inverted",false),
                table.getValueInt("min",0),
                table.getValueInt("max",7));
    }

    private final int min;
    private final int max;

    private LightSelector(boolean isInverted, int min, int max) {
        super(isInverted);
        this.min = min;
        this.max = max;
    }

    @Override protected boolean isValidInner(BlockPos pos, World world, String ruleDescriptor) {
        int level = world.getLight(pos,true);
        return level>=this.min && level<=this.max;
    }
    
    @Override public boolean isNonBasic() {
        return true;
    }

    @Override public SelectorType getType() {
        return LIGHT;
    }
}
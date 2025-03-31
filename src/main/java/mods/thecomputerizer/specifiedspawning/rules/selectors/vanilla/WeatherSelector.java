package mods.thecomputerizer.specifiedspawning.rules.selectors.vanilla;

import mods.thecomputerizer.specifiedspawning.rules.selectors.AbstractSelector;
import mods.thecomputerizer.specifiedspawning.rules.selectors.SelectorType;
import mods.thecomputerizer.theimpossiblelibrary.api.toml.Toml;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Objects;

import static mods.thecomputerizer.specifiedspawning.rules.selectors.SelectorType.WEATHER;

public class WeatherSelector extends AbstractSelector {
    
    public static WeatherSelector makeSelector(Toml table) {
        if(Objects.isNull(table)) return null;
        return new WeatherSelector(table.getValueBool("inverted",false),
                table.hasEntry("type") ? table.getValueString("type") : "rain");
    }

    private final String type;

    protected WeatherSelector(boolean isInverted, String type) {
        super(isInverted);
        this.type = type;
    }

    @Override protected boolean isValidInner(BlockPos pos, World world, String ruleDescriptor) {
        boolean isRaining = world.isRainingAt(pos);
        switch(this.type) {
            case "rain": return isRaining;
            case "storm": return isRaining && world.isThundering();
            case "snow": return isRaining && world.canSnowAt(pos,false);
            default: return !isRaining;
        }
    }

    @Override public boolean isNonBasic() {
        return true;
    }

    @Override public SelectorType getType() {
        return WEATHER;
    }
}
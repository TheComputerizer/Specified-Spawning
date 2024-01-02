package mods.thecomputerizer.specifiedspawning.rules.selectors.vanilla;

import mods.thecomputerizer.specifiedspawning.rules.selectors.AbstractSelector;
import mods.thecomputerizer.specifiedspawning.rules.selectors.SelectorType;
import mods.thecomputerizer.theimpossiblelibrary.common.toml.Table;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Objects;

public class WeatherSelector extends AbstractSelector {
    public static WeatherSelector makeSelector(Table table) {
        if(Objects.isNull(table)) return null;
        return new WeatherSelector(table.getValOrDefault("inverted",false),
                table.getValOrDefault("type","rain"));
    }

    private final String type;

    protected WeatherSelector(boolean isInverted, String type) {
        super(isInverted);
        this.type = type;
    }

    @Override
    protected boolean isValidInner(BlockPos pos, World world, String ruleDescriptor) {
        boolean isRaining = world.isRainingAt(pos);
        if(this.type.matches("rain")) return isRaining;
        if(this.type.matches("storm")) return isRaining && world.isThundering();
        if(this.type.matches("snow")) return isRaining && world.canSnowAt(pos,false);
        return !isRaining;
    }

    @Override
    public boolean isNonBasic() {
        return true;
    }

    @Override
    public SelectorType getType() {
        return SelectorType.WEATHER;
    }
}
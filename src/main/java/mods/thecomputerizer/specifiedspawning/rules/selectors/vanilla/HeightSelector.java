package mods.thecomputerizer.specifiedspawning.rules.selectors.vanilla;

import mods.thecomputerizer.specifiedspawning.rules.selectors.AbstractSelector;
import mods.thecomputerizer.specifiedspawning.rules.selectors.SelectorType;
import mods.thecomputerizer.theimpossiblelibrary.common.toml.Table;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Objects;

public class HeightSelector extends AbstractSelector {

    public static HeightSelector makeSelector(Table table) {
        if(Objects.isNull(table)) return null;
        return new HeightSelector(table.getValOrDefault("inverted",false),
                table.getValOrDefault("min",0),table.getValOrDefault("max",255),
                table.getValOrDefault("check_sky",false));
    }

    private final int minY;
    private final int maxY;
    private final boolean checkSky;

    private HeightSelector(boolean isInverted, int minY, int maxY, boolean checkSky) {
        super(isInverted);
        this.minY = minY;
        this.maxY = maxY;
        this.checkSky = checkSky;
    }

    @Override
    public boolean isValidInner(BlockPos pos, World world, String ruleDescriptor) {
        int yPos = pos.getY();
        boolean validPos = yPos>=this.minY && yPos<=this.maxY;
        return validPos && (!this.checkSky || world.canSeeSky(pos));
    }

    @Override
    public boolean isNonBasic() {
        return true;
    }

    @Override
    public SelectorType getType() {
        return SelectorType.HEIGHT;
    }
}

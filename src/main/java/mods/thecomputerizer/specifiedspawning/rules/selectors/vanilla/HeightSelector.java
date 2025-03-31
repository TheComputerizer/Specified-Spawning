package mods.thecomputerizer.specifiedspawning.rules.selectors.vanilla;

import mods.thecomputerizer.specifiedspawning.rules.selectors.AbstractSelector;
import mods.thecomputerizer.specifiedspawning.rules.selectors.SelectorType;
import mods.thecomputerizer.theimpossiblelibrary.api.toml.Toml;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Objects;

import static mods.thecomputerizer.specifiedspawning.rules.selectors.SelectorType.HEIGHT;

public class HeightSelector extends AbstractSelector {

    public static HeightSelector makeSelector(Toml table) {
        if(Objects.isNull(table)) return null;
        return new HeightSelector(table.getValueBool("inverted",false),
                table.getValueInt("min",0),
                table.getValueInt("max",255),
                table.getValueBool("check_sky",false));
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

    @Override public boolean isValidInner(BlockPos pos, World world, String ruleDescriptor) {
        int yPos = pos.getY();
        boolean validPos = yPos>=this.minY && yPos<=this.maxY;
        return validPos && (!this.checkSky || world.canSeeSky(pos));
    }

    @Override public boolean isNonBasic() {
        return true;
    }

    @Override public SelectorType getType() {
        return HEIGHT;
    }
}
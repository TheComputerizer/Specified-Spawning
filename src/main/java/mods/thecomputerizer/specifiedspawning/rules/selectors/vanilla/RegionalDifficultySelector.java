package mods.thecomputerizer.specifiedspawning.rules.selectors.vanilla;

import mods.thecomputerizer.specifiedspawning.rules.selectors.AbstractSelector;
import mods.thecomputerizer.specifiedspawning.rules.selectors.SelectorType;
import mods.thecomputerizer.theimpossiblelibrary.api.toml.Toml;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Objects;

import static mods.thecomputerizer.specifiedspawning.rules.selectors.SelectorType.REGIONALDIFFICULTY;

public class RegionalDifficultySelector extends AbstractSelector {

    public static RegionalDifficultySelector makeSelector(Toml table) {
        if(Objects.isNull(table)) return null;
        return new RegionalDifficultySelector(table.getValueBool("inverted",false),
                table.getValueFloat("min",0f),
                table.getValueFloat("max",6.75f));
    }

    private final float min;
    private final float max;

    protected RegionalDifficultySelector(boolean isInverted, float min, float max) {
        super(isInverted);
        this.min = min;
        this.max = max;
    }

    @Override protected boolean isValidInner(BlockPos pos, World world, String ruleDescriptor) {
        float dif = world.getDifficultyForLocation(pos).getAdditionalDifficulty();
        return this.min<=dif && this.max>=dif;
    }

    @Override public boolean isNonBasic() {
        return true;
    }

    @Override public SelectorType getType() {
        return REGIONALDIFFICULTY;
    }
}
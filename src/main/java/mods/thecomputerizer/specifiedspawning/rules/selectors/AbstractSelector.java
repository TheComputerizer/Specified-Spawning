package mods.thecomputerizer.specifiedspawning.rules.selectors;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class AbstractSelector implements ISelector {

    private final boolean isInverted;

    protected AbstractSelector(boolean isInverted) {
        this.isInverted = isInverted;
    }

    protected boolean getValid(boolean isValid) {
        return (isValid && !this.isInverted) || (!isValid && this.isInverted);
    }

    @Override public boolean isValid(BlockPos pos, World world, String ruleDescriptor) {
        return getValid(isValidInner(pos,world,ruleDescriptor));
    }

    protected abstract boolean isValidInner(BlockPos pos, World world, String ruleDescriptor);
}
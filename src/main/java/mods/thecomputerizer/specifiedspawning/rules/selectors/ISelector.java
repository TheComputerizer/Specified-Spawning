package mods.thecomputerizer.specifiedspawning.rules.selectors;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface ISelector {

    boolean isValid(BlockPos pos, World world, String ruleDescriptor);
    SelectorType getType();
    boolean isNonBasic();
}

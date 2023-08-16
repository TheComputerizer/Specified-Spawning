package mods.thecomputerizer.specifiedspawning.rules;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public abstract class DynamicRule implements IRule {
    @Override
    public boolean doesPass(World world, Entity entity) {
        return false;
    }
}

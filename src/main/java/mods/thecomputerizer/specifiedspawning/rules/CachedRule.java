package mods.thecomputerizer.specifiedspawning.rules;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public abstract class CachedRule implements IRule {


    public abstract boolean shouldUpdateCache();

    public abstract void updateCache();

    public abstract boolean doesCachePass();

    @Override
    public boolean doesPass(World world, Entity entity) {
        return false;
    }
}

package mods.thecomputerizer.specifiedspawning.rules;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public abstract class SingletonRule implements IRule {

    private final boolean doesPass;

    public SingletonRule() {
        this.doesPass = doesInitiallyPass();
    }

    protected abstract boolean doesInitiallyPass();

    @Override
    public boolean doesPass(World world, Entity entity) {
        return this.doesPass;
    }
}

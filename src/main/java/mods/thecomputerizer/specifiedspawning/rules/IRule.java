package mods.thecomputerizer.specifiedspawning.rules;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public interface IRule {

    boolean doesPass(World world, Entity entity);
}

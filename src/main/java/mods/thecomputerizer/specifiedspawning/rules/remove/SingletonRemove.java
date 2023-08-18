package mods.thecomputerizer.specifiedspawning.rules.remove;

import mods.thecomputerizer.specifiedspawning.SpawnManager;
import mods.thecomputerizer.specifiedspawning.rules.SingletonRule;
import mods.thecomputerizer.specifiedspawning.rules.selectors.BiomeSelector;
import mods.thecomputerizer.specifiedspawning.rules.selectors.EntitySelector;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.registry.EntityEntry;

import java.util.Set;

public class SingletonRemove extends SingletonRule {


    public SingletonRemove(EntitySelector entitySelector, Set<BiomeSelector> biomeSelectors) {
        super(entitySelector, biomeSelectors);
    }

    @Override
    protected void apply(Biome biome) {
        for(EntityEntry entity : getEntities())
            biome.getSpawnableList(SpawnManager.getEntityType(entity.getEntityClass()))
                    .removeIf(entry -> entry.entityClass==entity.getEntityClass());
    }
}

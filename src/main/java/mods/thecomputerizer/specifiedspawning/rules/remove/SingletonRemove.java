package mods.thecomputerizer.specifiedspawning.rules.remove;

import mods.thecomputerizer.specifiedspawning.Constants;
import mods.thecomputerizer.specifiedspawning.world.SpawnManager;
import mods.thecomputerizer.specifiedspawning.rules.SingletonRule;
import mods.thecomputerizer.specifiedspawning.rules.selectors.BiomeSelector;
import mods.thecomputerizer.specifiedspawning.rules.selectors.EntitySelector;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.registry.EntityEntry;
import scala.collection.immutable.Stream;

import java.util.Set;

public class SingletonRemove extends SingletonRule {


    public SingletonRemove(EntitySelector entitySelector, Set<BiomeSelector> biomeSelectors) {
        super(entitySelector, biomeSelectors);
    }

    @Override
    protected void apply(Biome biome) {
        for(EntityEntry entity : getEntities()) {
            Constants.LOGGER.error("SINGLETON REMOVING ENTITY {} FROM BIOME {}",entity.getEntityClass().getName(),biome.getRegistryName());
            biome.getSpawnableList(SpawnManager.getEntityType(entity.getEntityClass()))
                    .removeIf(entry -> entry.entityClass == entity.getEntityClass());
        }
    }
}

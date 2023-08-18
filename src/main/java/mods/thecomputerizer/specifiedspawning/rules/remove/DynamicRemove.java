package mods.thecomputerizer.specifiedspawning.rules.remove;

import mods.thecomputerizer.specifiedspawning.SpawnManager;
import mods.thecomputerizer.specifiedspawning.rules.DynamicRule;
import mods.thecomputerizer.specifiedspawning.rules.selectors.BiomeSelector;
import mods.thecomputerizer.specifiedspawning.rules.selectors.EntitySelector;
import mods.thecomputerizer.specifiedspawning.rules.selectors.ISelector;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.registry.EntityEntry;

import java.util.HashSet;
import java.util.Set;

public class DynamicRemove extends DynamicRule {

    public DynamicRemove(EntitySelector entitySelector, Set<BiomeSelector> biomeSelectors, Set<ISelector<?>> dynamicSelectors) {
        super(entitySelector, biomeSelectors, dynamicSelectors);
    }

    @Override
    protected Set<Biome.SpawnListEntry> apply(Biome biome) {
        Set<Biome.SpawnListEntry> ret = new HashSet<>();
        for(EntityEntry entity : getEntities())
            for(Biome.SpawnListEntry entry : biome.getSpawnableList(SpawnManager.getEntityType(entity.getEntityClass())))
                if(entry.entityClass==entity.getEntityClass())
                    ret.add(entry);
        return ret;
    }
}

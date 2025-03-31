package mods.thecomputerizer.specifiedspawning.rules.remove;

import mods.thecomputerizer.specifiedspawning.rules.SingletonRule;
import mods.thecomputerizer.specifiedspawning.rules.group.SpawnGroup;
import mods.thecomputerizer.specifiedspawning.rules.selectors.vanilla.BiomeSelector;
import mods.thecomputerizer.specifiedspawning.rules.selectors.vanilla.EntitySelector;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.registry.EntityEntry;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public class SingletonRemove extends SingletonRule implements IRemoveRule {

    public SingletonRemove(String groupName,List<EntitySelector> entitySelectors,Set<BiomeSelector> biomeSelectors) {
        super(groupName,entitySelectors, biomeSelectors);
    }

    @Override protected void apply(Biome biome, Collection<SpawnGroup> groups) {
        for(EntityEntry entity : getEntities())
            for(SpawnGroup group : groups)
                biome.getSpawnableList(group.getType()).removeIf(entry ->
                                entry.entityClass==entity.getEntityClass());
    }
}
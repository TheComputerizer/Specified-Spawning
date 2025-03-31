package mods.thecomputerizer.specifiedspawning.rules.remove;

import mods.thecomputerizer.specifiedspawning.mixin.access.ISpawnGroupObject;
import mods.thecomputerizer.specifiedspawning.rules.DynamicRule;
import mods.thecomputerizer.specifiedspawning.rules.group.SpawnGroup;
import mods.thecomputerizer.specifiedspawning.rules.selectors.vanilla.EntitySelector;
import mods.thecomputerizer.specifiedspawning.rules.selectors.ISelector;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.SpawnListEntry;
import net.minecraftforge.fml.common.registry.EntityEntry;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DynamicRemove extends DynamicRule implements IRemoveRule {

    public DynamicRemove(String groupName,List<EntitySelector> entitySelectors,Set<ISelector> dynamicSelectors) {
        super(groupName,entitySelectors,dynamicSelectors);
    }

    @Override protected Set<SpawnListEntry> apply(Biome biome, Collection<SpawnGroup> groups) {
        Set<SpawnListEntry> ret = new HashSet<>();
        for(EntityEntry entity : getEntities()) {
            for(SpawnGroup group : groups) {
                for(SpawnListEntry entry : biome.getSpawnableList(group.getType())) {
                    if(entry.entityClass == entity.getEntityClass()) {
                        ((ISpawnGroupObject) entry).specifiedspawning$setSpawnGroup(group,false);
                        ret.add(entry);
                    }
                }
            }
        }
        return ret;
    }

    @Override public boolean isRemoval() {
        return true;
    }
}
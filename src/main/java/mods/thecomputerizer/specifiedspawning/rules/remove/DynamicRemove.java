package mods.thecomputerizer.specifiedspawning.rules.remove;

import mods.thecomputerizer.specifiedspawning.mixin.access.ISpawnGroupObject;
import mods.thecomputerizer.specifiedspawning.rules.DynamicRule;
import mods.thecomputerizer.specifiedspawning.rules.selectors.vanilla.EntitySelector;
import mods.thecomputerizer.specifiedspawning.rules.selectors.ISelector;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.registry.EntityEntry;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DynamicRemove extends DynamicRule implements IRemoveRule {

    public DynamicRemove(String groupName,List<EntitySelector> entitySelectors,Set<ISelector> dynamicSelectors) {
        super(groupName, entitySelectors, dynamicSelectors);
    }

    @Override
    protected Set<Biome.SpawnListEntry> apply(Biome biome) {
        Set<Biome.SpawnListEntry> ret = new HashSet<>();
        for(EntityEntry entity : getEntities()) {
            for(Biome.SpawnListEntry entry : biome.getSpawnableList(getSpawnGroup().getType())) {
                if(entry.entityClass == entity.getEntityClass()) {
                    ((ISpawnGroupObject)entry).specifiedspawning$setSpawnGroup(getSpawnGroup(),false);
                    ret.add(entry);
                }
            }
        }
        return ret;
    }

    @Override
    public boolean isRemoval() {
        return true;
    }
}

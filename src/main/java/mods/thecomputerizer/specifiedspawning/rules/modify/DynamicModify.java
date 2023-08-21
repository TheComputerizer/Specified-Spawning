package mods.thecomputerizer.specifiedspawning.rules.modify;

import mods.thecomputerizer.specifiedspawning.mixin.access.ISpawnGroupObject;
import mods.thecomputerizer.specifiedspawning.rules.DynamicRule;
import mods.thecomputerizer.specifiedspawning.rules.group.SpawnGroup;
import mods.thecomputerizer.specifiedspawning.rules.selectors.EntitySelector;
import mods.thecomputerizer.specifiedspawning.rules.selectors.ISelector;
import mods.thecomputerizer.specifiedspawning.world.SpawnManager;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.registry.EntityEntry;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DynamicModify extends DynamicRule implements IModifyRule {

    private final String newGroupName;
    public DynamicModify(String groupName,String newGroupName,List<EntitySelector> entitySelectors,Set<ISelector<?>> dynamicSelectors) {
        super(groupName,entitySelectors, dynamicSelectors);
        this.newGroupName = newGroupName;
    }

    @Override
    protected Set<Biome.SpawnListEntry> apply(Biome biome) {
        Set<Biome.SpawnListEntry> ret = new HashSet<>();
        for(EntityEntry entity : getEntities()) {
            Set<Biome.SpawnListEntry> modifiedGroupEntries = new HashSet<>();
            biome.getSpawnableList(getSpawnGroup().getType()).removeIf(entry -> {
                if(entry.entityClass == entity.getEntityClass()) {
                    if(shouldChangeGroup()) {
                        modifiedGroupEntries.add(entry);
                        ret.add(entry);
                        return true;
                    } else {
                        ((ISpawnGroupObject)entry).specifiedspawning$setSpawnGroup(getSpawnGroup());
                        ret.add(entry);
                    }
                }
                return false;
            });
            SpawnGroup newGroup = SpawnManager.getSpawnGroup(this.newGroupName);
            List<Biome.SpawnListEntry> entries = biome.getSpawnableList(newGroup.getType());
            for(Biome.SpawnListEntry entry : modifiedGroupEntries) {
                ((ISpawnGroupObject)entry).specifiedspawning$setSpawnGroup(newGroup);
                entries.add(entry);
            }
        }
        return ret;
    }

    private boolean shouldChangeGroup() {
        return getGroupName().matches(this.newGroupName);
    }

    @Override
    public boolean isRemoval() {
        return false;
    }
}

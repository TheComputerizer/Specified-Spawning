package mods.thecomputerizer.specifiedspawning.rules.modify;

import mods.thecomputerizer.specifiedspawning.mixin.access.IPotentialJockey;
import mods.thecomputerizer.specifiedspawning.mixin.access.ISpawnGroupObject;
import mods.thecomputerizer.specifiedspawning.rules.DynamicRule;
import mods.thecomputerizer.specifiedspawning.rules.group.SpawnGroup;
import mods.thecomputerizer.specifiedspawning.rules.selectors.vanilla.EntitySelector;
import mods.thecomputerizer.specifiedspawning.rules.selectors.ISelector;
import mods.thecomputerizer.specifiedspawning.core.SpawnManager;
import mods.thecomputerizer.specifiedspawning.world.entity.Jockey;
import mods.thecomputerizer.theimpossiblelibrary.api.toml.Toml;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.SpawnListEntry;
import net.minecraftforge.fml.common.registry.EntityEntry;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static mods.thecomputerizer.specifiedspawning.core.Constants.LOGGER;

public class DynamicModify extends DynamicRule implements IModifyRule {

    private final String newGroupName;
    private final boolean modifySpawnCounts;
    private final List<Jockey> jockeys;

    public DynamicModify(String groupName, String newGroupName, boolean modifySpawnCounts, List<EntitySelector> entitySelectors,
                         Set<ISelector> dynamicSelectors, List<Toml> jockeyTables) {
        super(groupName,entitySelectors, dynamicSelectors);
        this.newGroupName = newGroupName;
        this.modifySpawnCounts = modifySpawnCounts;
        this.jockeys = jockeyTables.stream().map(Jockey::parse).collect(Collectors.toList());
    }

    @Override protected Set<SpawnListEntry> apply(Biome biome, Collection<SpawnGroup> groups) {
        Set<SpawnListEntry> ret = new HashSet<>();
        for(EntityEntry entity : getEntities()) {
            for(SpawnGroup group : groups) {
                if(Objects.isNull(group)) continue;
                Set<SpawnListEntry> modifiedGroupEntries = new HashSet<>();
                biome.getSpawnableList(group.getType()).removeIf(entry -> {
                    if(entry.entityClass==entity.getEntityClass()) {
                        if(this.modifySpawnCounts) {
                            entry.minGroupCount = getEntitySpawnCount(true);
                            entry.maxGroupCount = getEntitySpawnCount(false);
                        }
                        if(shouldChangeGroup()) {
                            modifiedGroupEntries.add(entry);
                            ret.add(entry);
                            return true;
                        } else {
                            ((ISpawnGroupObject) entry).specifiedspawning$setSpawnGroup(group,true);
                            for(Jockey jockey : this.jockeys)
                                ((IPotentialJockey) entry).specifiedspawning$addJockey(jockey);
                            ret.add(entry);
                        }
                    }
                    return false;
                });
                SpawnGroup newGroup = SpawnManager.getSpawnGroup(this.newGroupName);
                if(Objects.isNull(newGroup)) {
                    LOGGER.error("Failed to find spawn group {} for dynamic-modify rule! Falling back to {}",
                                 this.newGroupName,SpawnManager.getSpawnGroupName(group));
                    newGroup = group;
                }
                EnumCreatureType newType = newGroup.getType();
                if(Objects.isNull(newType)) {
                    LOGGER.error("Failed to retrieve creature type for spawn group {}",
                                 SpawnManager.getSpawnGroupName(newGroup));
                    continue;
                }
                List<SpawnListEntry> entries = biome.getSpawnableList(newType);
                for(SpawnListEntry entry : modifiedGroupEntries) {
                    ((ISpawnGroupObject) entry).specifiedspawning$setSpawnGroup(newGroup,true);
                    for(Jockey jockey : this.jockeys) ((IPotentialJockey)entry).specifiedspawning$addJockey(jockey);
                    entries.add(entry);
                }
            }
        }
        return ret;
    }

    private boolean shouldChangeGroup() {
        return !getGroupName().matches(this.newGroupName);
    }

    @Override public boolean isRemoval() {
        return false;
    }
}
package mods.thecomputerizer.specifiedspawning.rules.modify;

import mods.thecomputerizer.specifiedspawning.mixin.access.IPotentialJockey;
import mods.thecomputerizer.specifiedspawning.mixin.access.ISpawnGroupObject;
import mods.thecomputerizer.specifiedspawning.rules.SingletonRule;
import mods.thecomputerizer.specifiedspawning.rules.group.SpawnGroup;
import mods.thecomputerizer.specifiedspawning.rules.selectors.vanilla.BiomeSelector;
import mods.thecomputerizer.specifiedspawning.rules.selectors.vanilla.EntitySelector;
import mods.thecomputerizer.specifiedspawning.world.SpawnManager;
import mods.thecomputerizer.specifiedspawning.world.entity.Jockey;
import mods.thecomputerizer.theimpossiblelibrary.common.toml.Table;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.registry.EntityEntry;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class SingletonModify extends SingletonRule implements IModifyRule {

    private final String newGroupName;
    private final boolean modifySpawnCounts;
    private final List<Jockey> jockeys;

    public SingletonModify(String groupName, String newGroupName, boolean modifySpawnCounts, List<EntitySelector> entitySelectors,
                           Set<BiomeSelector> biomeSelectors, List<Table> jockeyTables) {
        super(groupName,entitySelectors, biomeSelectors);
        this.newGroupName = newGroupName;
        this.modifySpawnCounts = modifySpawnCounts;
        this.jockeys = jockeyTables.stream().map(Jockey::parse).collect(Collectors.toList());
    }

    @Override
    protected void apply(Biome biome) {
        for(EntityEntry entity : getEntities()) {
            Set<Biome.SpawnListEntry> modifiedGroupEntries = new HashSet<>();
            biome.getSpawnableList(getSpawnGroup().getType()).removeIf(entry -> {
                if(entry.entityClass == entity.getEntityClass()) {
                    if(this.modifySpawnCounts) {
                        entry.minGroupCount = getEntitySpawnCount(true);
                        entry.maxGroupCount = getEntitySpawnCount(false);
                    }
                    if(shouldChangeGroup()) {
                        modifiedGroupEntries.add(entry);
                        return true;
                    } else {
                        ((ISpawnGroupObject)entry).specifiedspawning$setSpawnGroup(getSpawnGroup(),true);
                        for(Jockey jockey : this.jockeys)
                            ((IPotentialJockey)entry).specifiedspawning$addJockey(jockey);
                    }
                }
                return false;
            });
            SpawnGroup newGroup = SpawnManager.getSpawnGroup(this.newGroupName);
            List<Biome.SpawnListEntry> entries = biome.getSpawnableList(newGroup.getType());
            for(Biome.SpawnListEntry entry : modifiedGroupEntries) {
                ((ISpawnGroupObject)entry).specifiedspawning$setSpawnGroup(newGroup,true);
                for(Jockey jockey : this.jockeys)
                    ((IPotentialJockey)entry).specifiedspawning$addJockey(jockey);
                entries.add(entry);
            }
        }
    }

    private boolean shouldChangeGroup() {
        return !getGroupName().matches(this.newGroupName);
    }
}

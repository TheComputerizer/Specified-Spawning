package mods.thecomputerizer.specifiedspawning.rules.spawn;

import mods.thecomputerizer.specifiedspawning.mixin.access.IPotentialJockey;
import mods.thecomputerizer.specifiedspawning.mixin.access.ISpawnGroupObject;
import mods.thecomputerizer.specifiedspawning.rules.DynamicRule;
import mods.thecomputerizer.specifiedspawning.rules.group.SpawnGroup;
import mods.thecomputerizer.specifiedspawning.rules.selectors.vanilla.EntitySelector;
import mods.thecomputerizer.specifiedspawning.rules.selectors.ISelector;
import mods.thecomputerizer.specifiedspawning.world.entity.Jockey;
import mods.thecomputerizer.theimpossiblelibrary.api.toml.Toml;
import net.minecraft.entity.EntityLiving;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.SpawnListEntry;
import net.minecraftforge.fml.common.registry.EntityEntry;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static mods.thecomputerizer.specifiedspawning.core.Constants.LOGGER;
import static net.minecraftforge.fml.common.registry.ForgeRegistries.BIOMES;

public class DynamicSpawn extends DynamicRule implements ISpawnRule {

    private final List<Jockey> jockeys;

    public DynamicSpawn(String groupName, List<EntitySelector> entitySelectors, Set<ISelector> dynamicSelectors,
                        List<Toml> jockeyTables) {
        super(groupName,entitySelectors, dynamicSelectors);
        this.jockeys = jockeyTables.stream().map(Jockey::parse).collect(Collectors.toList());
    }

    @SuppressWarnings({"unchecked","DataFlowIssue"})
    @Override protected Set<SpawnListEntry> apply(Biome biome, Collection<SpawnGroup> groups) {
        Set<SpawnListEntry> ret = new HashSet<>();
        for(EntityEntry entity : getEntities()) {
            if(EntityLiving.class.isAssignableFrom(entity.getEntityClass())) {
                for(SpawnGroup group : groups) {
                    SpawnListEntry entry = new SpawnListEntry((Class<? extends EntityLiving>)entity.getEntityClass(),
                            getEntityWeight(),getEntitySpawnCount(true),getEntitySpawnCount(false));
                    biome.getSpawnableList(group.getType()).add(entry);
                    ((ISpawnGroupObject)entry).specifiedspawning$setSpawnGroup(group, true);
                    for(Jockey jockey : this.jockeys) ((IPotentialJockey)entry).specifiedspawning$addJockey(jockey);
                    ret.add(entry);
                }
            } else LOGGER.error("Cannot add entity of class {} to the biome {}! Only living entities are" +
                    "currently supported!",entity.getEntityClass(),BIOMES.getKey(biome));
        }
        return ret;
    }

    @Override public boolean isRemoval() {
        return false;
    }

    @Override public void onEntityInitialSpawn() {}
}
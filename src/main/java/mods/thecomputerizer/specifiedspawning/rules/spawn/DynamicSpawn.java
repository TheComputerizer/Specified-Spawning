package mods.thecomputerizer.specifiedspawning.rules.spawn;

import mods.thecomputerizer.specifiedspawning.core.Constants;
import mods.thecomputerizer.specifiedspawning.mixin.access.IPotentialJockey;
import mods.thecomputerizer.specifiedspawning.mixin.access.ISpawnGroupObject;
import mods.thecomputerizer.specifiedspawning.rules.DynamicRule;
import mods.thecomputerizer.specifiedspawning.rules.selectors.vanilla.EntitySelector;
import mods.thecomputerizer.specifiedspawning.rules.selectors.ISelector;
import mods.thecomputerizer.specifiedspawning.world.entity.Jockey;
import mods.thecomputerizer.theimpossiblelibrary.common.toml.Table;
import net.minecraft.entity.EntityLiving;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class DynamicSpawn extends DynamicRule implements ISpawnRule {

    private final List<Jockey> jockeys;

    public DynamicSpawn(String groupName, List<EntitySelector> entitySelectors, Set<ISelector> dynamicSelectors,
                        List<Table> jockeyTables) {
        super(groupName,entitySelectors, dynamicSelectors);
        this.jockeys = jockeyTables.stream().map(Jockey::parse).collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Set<Biome.SpawnListEntry> apply(Biome biome) {
        Set<Biome.SpawnListEntry> ret = new HashSet<>();
        for(EntityEntry entity : getEntities()) {
            if(EntityLiving.class.isAssignableFrom(entity.getEntityClass())) {
                Biome.SpawnListEntry entry = new Biome.SpawnListEntry((Class<? extends EntityLiving>)entity.getEntityClass(),
                        getEntityWeight(),getEntitySpawnCount(true), getEntitySpawnCount(false));
                biome.getSpawnableList(getSpawnGroup().getType()).add(entry);
                ((ISpawnGroupObject)entry).specifiedspawning$setSpawnGroup(getSpawnGroup(),true);
                for(Jockey jockey : this.jockeys)
                    ((IPotentialJockey)entry).specifiedspawning$addJockey(jockey);
                ret.add(entry);
            } else Constants.LOGGER.error("Cannot add entity of class {} to the biome {}! Only living entities are" +
                    "currently supported!",entity.getEntityClass(), ForgeRegistries.BIOMES.getKey(biome));
        }
        return ret;
    }

    @Override
    public boolean isRemoval() {
        return false;
    }

    @Override
    public void onEntityInitialSpawn() {

    }
}

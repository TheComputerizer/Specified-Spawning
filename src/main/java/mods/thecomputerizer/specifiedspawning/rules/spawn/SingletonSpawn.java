package mods.thecomputerizer.specifiedspawning.rules.spawn;

import mods.thecomputerizer.specifiedspawning.core.Constants;
import mods.thecomputerizer.specifiedspawning.mixin.access.IPotentialJockey;
import mods.thecomputerizer.specifiedspawning.mixin.access.ISpawnGroupObject;
import mods.thecomputerizer.specifiedspawning.rules.SingletonRule;
import mods.thecomputerizer.specifiedspawning.rules.group.SpawnGroup;
import mods.thecomputerizer.specifiedspawning.rules.selectors.vanilla.BiomeSelector;
import mods.thecomputerizer.specifiedspawning.rules.selectors.vanilla.EntitySelector;
import mods.thecomputerizer.specifiedspawning.world.entity.Jockey;
import mods.thecomputerizer.theimpossiblelibrary.common.toml.Table;
import net.minecraft.entity.EntityLiving;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class SingletonSpawn extends SingletonRule implements ISpawnRule {

    private final List<Jockey> jockeys;

    public SingletonSpawn(String groupName, List<EntitySelector> entitySelectors, Set<BiomeSelector> biomeSelectors,
                          List<Table> jockeyTables) {
        super(groupName, entitySelectors, biomeSelectors);
        this.jockeys = jockeyTables.stream().map(Jockey::parse).collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void apply(Biome biome, Collection<SpawnGroup> groups) {
        for(EntityEntry entity : getEntities()) {
            if(EntityLiving.class.isAssignableFrom(entity.getEntityClass())) {
                for(SpawnGroup group : groups) {
                    Biome.SpawnListEntry entry = new Biome.SpawnListEntry((Class<? extends EntityLiving>) entity.getEntityClass(),
                            getEntityWeight(), getEntitySpawnCount(true), getEntitySpawnCount(false));
                    for(Jockey jockey : this.jockeys) ((IPotentialJockey) entry).specifiedspawning$addJockey(jockey);
                    ((ISpawnGroupObject)entry).specifiedspawning$setSpawnType(getSpawnType());
                    biome.getSpawnableList(group.getType()).add(entry);
                }
            }
            else Constants.LOGGER.error("Cannot add entity of class {} to the biome {}! Only living entities are" +
                    "currently supported!",entity.getEntityClass(),ForgeRegistries.BIOMES.getKey(biome));
        }
    }

    @Override
    public void onEntityInitialSpawn() {

    }
}

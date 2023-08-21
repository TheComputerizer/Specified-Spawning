package mods.thecomputerizer.specifiedspawning.rules.spawn;

import mods.thecomputerizer.specifiedspawning.core.Constants;
import mods.thecomputerizer.specifiedspawning.mixin.access.ISpawnGroupObject;
import mods.thecomputerizer.specifiedspawning.rules.DynamicRule;
import mods.thecomputerizer.specifiedspawning.rules.selectors.EntitySelector;
import mods.thecomputerizer.specifiedspawning.rules.selectors.ISelector;
import net.minecraft.entity.EntityLiving;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DynamicSpawn extends DynamicRule implements ISpawnRule {

    public DynamicSpawn(String groupName,List<EntitySelector> entitySelectors,Set<ISelector<?>> dynamicSelectors) {
        super(groupName,entitySelectors, dynamicSelectors);
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
                ((ISpawnGroupObject)entry).specifiedspawning$setSpawnGroup(getSpawnGroup());
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
}

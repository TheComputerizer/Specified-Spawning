package mods.thecomputerizer.specifiedspawning.rules.spawn;

import mods.thecomputerizer.specifiedspawning.Constants;
import mods.thecomputerizer.specifiedspawning.SpawnManager;
import mods.thecomputerizer.specifiedspawning.rules.DynamicRule;
import mods.thecomputerizer.specifiedspawning.rules.selectors.BiomeSelector;
import mods.thecomputerizer.specifiedspawning.rules.selectors.EntitySelector;
import mods.thecomputerizer.specifiedspawning.rules.selectors.ISelector;
import net.minecraft.entity.EntityLiving;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.HashSet;
import java.util.Set;

public class DynamicSpawn extends DynamicRule {

    public DynamicSpawn(EntitySelector entitySelector, Set<BiomeSelector> biomeSelectors, Set<ISelector<?>> dynamicSelectors) {
        super(entitySelector, biomeSelectors, dynamicSelectors);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Set<Biome.SpawnListEntry> apply(Biome biome) {
        Set<Biome.SpawnListEntry> ret = new HashSet<>();
        for(EntityEntry entity : getEntities()) {
            if(EntityLiving.class.isAssignableFrom(entity.getEntityClass())) {
                Biome.SpawnListEntry entry = new Biome.SpawnListEntry((Class<? extends EntityLiving>)entity.getEntityClass(),
                        getEntityWeight(),getEntitySpawnCount(true), getEntitySpawnCount(false));
                biome.getSpawnableList(SpawnManager.getEntityType(entity.getEntityClass())).add(entry);
                ret.add(entry);
            } else Constants.LOGGER.error("Cannot add entity of class {} to the biome {}! Only living entities are" +
                    "currently supported!",entity.getEntityClass(), ForgeRegistries.BIOMES.getKey(biome));
        }
        return ret;
    }
}

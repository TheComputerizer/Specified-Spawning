package mods.thecomputerizer.specifiedspawning.rules.spawn;

import mods.thecomputerizer.specifiedspawning.Constants;
import mods.thecomputerizer.specifiedspawning.SpawnManager;
import mods.thecomputerizer.specifiedspawning.rules.SingletonRule;
import mods.thecomputerizer.specifiedspawning.rules.selectors.BiomeSelector;
import mods.thecomputerizer.specifiedspawning.rules.selectors.EntitySelector;
import net.minecraft.entity.EntityLiving;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.Set;

public class SingletonSpawn extends SingletonRule {

    public SingletonSpawn(EntitySelector entitySelector, Set<BiomeSelector> biomeSelectors) {
        super(entitySelector, biomeSelectors);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void apply(Biome biome) {
        for(EntityEntry entity : getEntities()) {
            if(EntityLiving.class.isAssignableFrom(entity.getEntityClass()))
                biome.getSpawnableList(SpawnManager.getEntityType(entity.getEntityClass()))
                        .add(new Biome.SpawnListEntry((Class<? extends EntityLiving>)entity.getEntityClass(),getEntityWeight(),
                                getEntitySpawnCount(true),getEntitySpawnCount(false)));
            else Constants.LOGGER.error("Cannot add entity of class {} to the biome {}! Only living entities are" +
                    "currently supported!",entity.getEntityClass(),ForgeRegistries.BIOMES.getKey(biome));
        }
    }
}

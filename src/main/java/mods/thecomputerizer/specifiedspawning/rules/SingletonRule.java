package mods.thecomputerizer.specifiedspawning.rules;

import mods.thecomputerizer.specifiedspawning.rules.selectors.BiomeSelector;
import mods.thecomputerizer.specifiedspawning.rules.selectors.EntitySelector;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.registry.EntityEntry;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public abstract class SingletonRule extends AbstractRule {

    private final EntitySelector entitySelector;
    private final Set<BiomeSelector> biomeSelectors;
    private Set<EntityEntry> entities;
    private Set<Biome> biomes;

    public SingletonRule(EntitySelector entitySelector, Set<BiomeSelector> biomeSelectors) {
        this.entitySelector = entitySelector;
        this.biomeSelectors = biomeSelectors;
    }

    @Override
    public void setup() {
        if(Objects.isNull(this.entitySelector)) this.entities = new HashSet<>();
        else this.entities = getEntities(this.entitySelector);
        if(Objects.isNull(this.biomeSelectors) || this.biomeSelectors.isEmpty()) this.biomes = new HashSet<>();
        else this.biomes = getBiomes(this.biomeSelectors);
    }

    public void apply() {
        for(Biome biome : this.biomes) apply(biome);
    }

    protected abstract void apply(Biome biome);

    protected Set<EntityEntry> getEntities() {
        return this.entities;
    }

    protected int getEntityWeight() {
        return this.entitySelector.getWeight();
    }

    protected int getEntitySpawnCount(boolean min) {
        return min ? this.entitySelector.getMinGroupSpawn() : this.entitySelector.getMaxGroupSpawn();
    }

    protected Set<Biome> getBiomes() {
        return this.biomes;
    }
}

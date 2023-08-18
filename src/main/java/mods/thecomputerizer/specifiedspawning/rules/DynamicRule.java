package mods.thecomputerizer.specifiedspawning.rules;

import mods.thecomputerizer.specifiedspawning.rules.selectors.*;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.EntityEntry;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public abstract class DynamicRule extends AbstractRule {

    private final EntitySelector entitySelector;
    private final Set<BiomeSelector> biomeSelectors;
    private final Set<ISelector<?>> dynamicSelectors;
    private Set<EntityEntry> entities;
    private Set<Biome> biomes;
    private Set<Biome.SpawnListEntry> affectedSpawnEntries;

    public DynamicRule(EntitySelector entitySelector, Set<BiomeSelector> biomeSelectors, Set<ISelector<?>> dynamicSelectors) {
        this.entitySelector = entitySelector;
        this.biomeSelectors = biomeSelectors;
        this.dynamicSelectors = dynamicSelectors;
    }

    @Override
    public void setup() {
        if(Objects.isNull(this.entitySelector)) this.entities = new HashSet<>();
        else this.entities = getEntities(this.entitySelector);
        if(Objects.isNull(this.biomeSelectors) || this.biomeSelectors.isEmpty()) this.biomes = new HashSet<>();
        else this.biomes = getBiomes(this.biomeSelectors);
    }

    public void apply() {
        this.affectedSpawnEntries = new HashSet<>();
        for(Biome biome : this.biomes) this.affectedSpawnEntries.addAll(apply(biome));
    }

    protected abstract Set<Biome.SpawnListEntry> apply(Biome biome);

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

    protected boolean applyDynamicSelectors(int light, int dim, World world) {
        for(ISelector<?> selector : this.dynamicSelectors) {
            if(selector instanceof LightSelector)
                if(!applyLight((LightSelector)selector,light)) return false;
            if(selector instanceof DimensionSelector)
                if(!applyDimension((DimensionSelector)selector,dim)) return false;
            if(selector instanceof GamestageSelector)
                if(!applyGamestages((GamestageSelector)selector,world)) return false;
        }
        return true;
    }

    protected boolean applyLight(LightSelector selector, int light) {
        return selector.isValid(light);
    }

    protected boolean applyDimension(DimensionSelector selector, int dim) {
        return selector.isValid(dim);
    }

    protected boolean applyGamestages(GamestageSelector selector, World world) {
        return !Loader.isModLoaded("gamestages") || selector.isValid(world);
    }
}

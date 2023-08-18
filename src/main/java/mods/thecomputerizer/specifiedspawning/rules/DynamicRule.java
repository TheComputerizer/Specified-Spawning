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
    private final Set<DimensionSelector> dimensionSelectors;
    private final Set<GamestageSelector> gamestageSelectors;
    private final Set<LightSelector> lightSelectors;
    private final Set<HeightSelector> heightSelectors;
    private Set<EntityEntry> entities;
    private Set<Biome> biomes;

    public DynamicRule(EntitySelector entitySelector, Set<ISelector<?>> dynamicSelectors) {
        this.entitySelector = entitySelector;
        this.biomeSelectors = new HashSet<>();
        this.dimensionSelectors = new HashSet<>();
        this.gamestageSelectors = new HashSet<>();
        this.lightSelectors = new HashSet<>();
        this.heightSelectors = new HashSet<>();
        for(ISelector<?> selector : dynamicSelectors) {
            if(selector instanceof BiomeSelector) this.biomeSelectors.add((BiomeSelector)selector);
            if(selector instanceof DimensionSelector) this.dimensionSelectors.add((DimensionSelector)selector);
            if(selector instanceof GamestageSelector) this.gamestageSelectors.add((GamestageSelector)selector);
            if(selector instanceof LightSelector) this.lightSelectors.add((LightSelector)selector);
            if(selector instanceof HeightSelector) this.heightSelectors.add((HeightSelector)selector);
        }
    }

    @Override
    public void setup() {
        if(Objects.isNull(this.entitySelector)) this.entities = new HashSet<>();
        else this.entities = getEntities(this.entitySelector);
        if(Objects.isNull(this.biomeSelectors) || this.biomeSelectors.isEmpty()) this.biomes = new HashSet<>();
        else this.biomes = getBiomes(this.biomeSelectors);
    }

    public Set<Biome.SpawnListEntry> apply() {
        Set<Biome.SpawnListEntry> ret = new HashSet<>();
        for(Biome biome : this.biomes) ret.addAll(apply(biome));
        return ret;
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

    public boolean checkDimension(int dim) {
        for(DimensionSelector selector : this.dimensionSelectors)
            if(selector.isValid(dim)) return true;
        return false;
    }

    public boolean checkGamestages(World world) {
        if(!Loader.isModLoaded("gamestages")) return true;
        for(GamestageSelector selector : this.gamestageSelectors)
            if(selector.isValid(world)) return true;
        return false;
    }

    public boolean checkLight(int light) {
        for(LightSelector selector : this.lightSelectors)
            if(selector.isValid(light)) return true;
        return false;
    }

    public boolean checkHeight(int yPos) {
        for(HeightSelector selector : this.heightSelectors)
            if(selector.isValid(yPos)) return true;
        return false;
    }

    public abstract boolean isRemoval();
}

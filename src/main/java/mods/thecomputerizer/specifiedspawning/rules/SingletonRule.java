package mods.thecomputerizer.specifiedspawning.rules;

import mods.thecomputerizer.specifiedspawning.core.Constants;
import mods.thecomputerizer.specifiedspawning.rules.selectors.vanilla.BiomeSelector;
import mods.thecomputerizer.specifiedspawning.rules.selectors.vanilla.EntitySelector;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import org.apache.logging.log4j.Level;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public abstract class SingletonRule extends AbstractRule {

    private final List<EntitySelector> entitySelectors;
    private final Set<BiomeSelector> biomeSelectors;
    private Set<EntityEntry> entities;
    private Set<Biome> biomes;

    public SingletonRule(String groupName, List<EntitySelector> entitySelectors, Set<BiomeSelector> biomeSelectors) {
        super(groupName);
        this.entitySelectors = entitySelectors;
        this.biomeSelectors = biomeSelectors;
    }

    @Override
    public void setup() {
        setRuleDescriptor();
        Constants.logVerbose(Level.INFO,"Setting up {} rule",this.ruleDescriptor);
        if(Objects.isNull(this.entitySelectors) || this.entitySelectors.isEmpty())
            this.entities = new HashSet<>(ForgeRegistries.ENTITIES.getValuesCollection());
        else this.entities = getEntities(this.entitySelectors);
        if(Objects.isNull(this.biomeSelectors) || this.biomeSelectors.isEmpty())
            this.biomes = new HashSet<>(ForgeRegistries.BIOMES.getValuesCollection());
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
        return this.entitySelectors.get(0).getWeight();
    }

    protected int getEntitySpawnCount(boolean min) {
        return min ? this.entitySelectors.get(0).getMinGroupSpawn() : this.entitySelectors.get(0).getMaxGroupSpawn();
    }
}

package mods.thecomputerizer.specifiedspawning.rules;

import mods.thecomputerizer.specifiedspawning.core.Constants;
import mods.thecomputerizer.specifiedspawning.rules.group.SpawnGroup;
import mods.thecomputerizer.specifiedspawning.rules.selectors.vanilla.BiomeSelector;
import mods.thecomputerizer.specifiedspawning.rules.selectors.vanilla.EntitySelector;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLiving.SpawnPlacementType;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.registry.EntityEntry;

import javax.annotation.Nullable;
import java.util.*;

import static net.minecraftforge.fml.common.registry.ForgeRegistries.BIOMES;
import static net.minecraftforge.fml.common.registry.ForgeRegistries.ENTITIES;
import static org.apache.logging.log4j.Level.INFO;

public abstract class SingletonRule extends AbstractRule {

    private final List<EntitySelector> entitySelectors;
    private final Set<BiomeSelector> biomeSelectors;
    private Set<EntityEntry> entities;
    private Set<Biome> biomes;
    private SpawnPlacementType cachedSpawnType;

    public SingletonRule(String groupName, List<EntitySelector> entitySelectors, Set<BiomeSelector> biomeSelectors) {
        super(groupName);
        this.entitySelectors = entitySelectors;
        this.biomeSelectors = biomeSelectors;
    }

    @Override public void setup() {
        setRuleDescriptor();
        Constants.logVerbose(INFO,"Setting up {} rule",this.ruleDescriptor);
        if(Objects.isNull(this.entitySelectors) || this.entitySelectors.isEmpty()) {
            this.entities = new HashSet<>(ENTITIES.getValuesCollection());
            this.entities.removeIf(entry -> !EntityLiving.class.isAssignableFrom(entry.getEntityClass()));
        }
        else this.entities = getEntities(this.entitySelectors);
        if(Objects.isNull(this.biomeSelectors) || this.biomeSelectors.isEmpty())
            this.biomes = new HashSet<>(BIOMES.getValuesCollection());
        else this.biomes = getBiomes(this.biomeSelectors);
    }

    public void apply() {
        Collection<SpawnGroup> groups = getSpawnGroups();
        for(Biome biome : this.biomes)
            if(Objects.nonNull(biome)) apply(biome,groups);
    }

    protected abstract void apply(Biome biome, Collection<SpawnGroup> groups);

    protected Set<EntityEntry> getEntities() {
        return this.entities;
    }

    protected int getEntityWeight() {
        return this.entitySelectors.get(0).getWeight();
    }

    protected int getEntitySpawnCount(boolean min) {
        return min ? this.entitySelectors.get(0).getMinGroupSpawn() : this.entitySelectors.get(0).getMaxGroupSpawn();
    }

    protected @Nullable SpawnPlacementType getSpawnType() {
        if(Objects.isNull(this.cachedSpawnType)) {
            for(EntitySelector selector : this.entitySelectors) {
                this.cachedSpawnType = selector.getSpawnType();
                if(Objects.nonNull(this.cachedSpawnType)) break;
            }
        }
        return this.cachedSpawnType;
    }



    protected boolean shouldIgnoreSpawnConditions() {
        for(EntitySelector selector : this.entitySelectors)
            if(selector.shouldIgnoreSpawnConditions()) return true;
        return false;
    }
}
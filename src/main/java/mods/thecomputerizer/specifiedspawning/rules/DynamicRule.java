package mods.thecomputerizer.specifiedspawning.rules;

import mods.thecomputerizer.specifiedspawning.core.Constants;
import mods.thecomputerizer.specifiedspawning.rules.group.SpawnGroup;
import mods.thecomputerizer.specifiedspawning.rules.selectors.ISelector;
import mods.thecomputerizer.specifiedspawning.rules.selectors.SelectorType;
import mods.thecomputerizer.specifiedspawning.rules.selectors.scalinghealth.ScalingDifficultySelector;
import mods.thecomputerizer.specifiedspawning.rules.selectors.vanilla.BiomeSelector;
import mods.thecomputerizer.specifiedspawning.rules.selectors.vanilla.EntitySelector;
import mods.thecomputerizer.specifiedspawning.rules.selectors.vanilla.SpawnBlockSelector;
import mods.thecomputerizer.specifiedspawning.world.SHHooks;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.logging.log4j.Level;

import java.util.*;
import java.util.function.Function;

public abstract class DynamicRule extends AbstractRule {

    private final List<EntitySelector> entitySelectors;
    private final Set<BiomeSelector> biomeSelectors;
    private final Set<ISelector> dynamicSelectors;
    private final Map<SelectorType,Tuple<Integer,MutableInt>> dynamicTypeMap;
    private Set<EntityEntry> entities;
    private Set<Biome> biomes;
    public boolean shouldLogExcessively;
    public boolean returnImmediately;

    public DynamicRule(String groupName, List<EntitySelector> entitySelectors, Set<ISelector> dynamicSelectors) {
        super(groupName);
        this.entitySelectors = entitySelectors;
        this.biomeSelectors = new HashSet<>();
        final Map<SelectorType,MutableInt> counterMap = new HashMap<>();
        dynamicSelectors.removeIf(selector -> {
            SelectorType type = selector.getType();
            if(type==SelectorType.SCALINGDIFFICULTY) SHHooks.setLoadedScalingDifficultySelector(true);
            counterMap.putIfAbsent(type,new MutableInt());
            counterMap.get(type).increment();
            boolean ret = selector instanceof BiomeSelector;
            if(ret) this.biomeSelectors.add((BiomeSelector)selector);
            return ret;
        });
        this.dynamicSelectors = dynamicSelectors;
        this.dynamicTypeMap = new HashMap<>();
        for(Map.Entry<SelectorType,MutableInt> typeEntry : counterMap.entrySet()) {
            int total = typeEntry.getValue().getValue();
            if(total>0) this.dynamicTypeMap.put(typeEntry.getKey(),new Tuple<>(total,new MutableInt()));
        }
    }

    @Override
    public void setup() {
        setRuleDescriptor();
        Constants.logVerbose(Level.INFO,"Setting up {} rule with {} dynamic selector types",this.ruleDescriptor,this.dynamicTypeMap.size());
        logExcessiveCollection(this.dynamicTypeMap.entrySet(), entry ->
                "(Map Entry) | Key("+entry.getKey().toString()+") | Value("+tupleToString(entry.getValue())+")");
        if(Objects.isNull(this.entitySelectors) || this.entitySelectors.isEmpty()) {
            this.entities = new HashSet<>(ForgeRegistries.ENTITIES.getValuesCollection());
            this.entities.removeIf(entry -> !EntityLiving.class.isAssignableFrom(entry.getEntityClass()));
        }
        else this.entities = getEntities(this.entitySelectors);
        if(Objects.isNull(this.biomeSelectors) || this.biomeSelectors.isEmpty())
            this.biomes = new HashSet<>(ForgeRegistries.BIOMES.getValuesCollection());
        else this.biomes = getBiomes(this.biomeSelectors);
        for(ISelector selector : this.dynamicSelectors)
            if(selector.getType()==SelectorType.SPAWNBLOCK)
                cacheBlockSelector((SpawnBlockSelector)selector);
        logExcessiveDebug("This rule is being assigned to {} unique entity entries in {} unique biomes",this.entities.size(),this.biomes.size());
    }

    public void cacheBlockSelector(SpawnBlockSelector selector) {
        for(Block block : ForgeRegistries.BLOCKS.getValuesCollection())
            selector.isResourceValid(block,this.ruleDescriptor);
    }

    public Set<Biome.SpawnListEntry> apply() {
        Set<Biome.SpawnListEntry> ret = new HashSet<>();
        Collection<SpawnGroup> groups = getSpawnGroups();
        for(Biome biome : this.biomes) ret.addAll(apply(biome,groups));
        return ret;
    }

    protected abstract Set<Biome.SpawnListEntry> apply(Biome biome, Collection<SpawnGroup> groups);

    protected Set<EntityEntry> getEntities() {
        return this.entities;
    }

    protected int getEntityWeight() {
        return this.entitySelectors.get(0).getWeight();
    }

    protected int getEntitySpawnCount(boolean min) {
        return min ? this.entitySelectors.get(0).getMinGroupSpawn() : this.entitySelectors.get(0).getMaxGroupSpawn();
    }

    public boolean checkSelectors(BlockPos pos, WorldServer world) {
        //logExcessiveCollection(this.dynamicSelectors,Object::toString);
        for(ISelector selector : this.dynamicSelectors) {
            if(selector instanceof ScalingDifficultySelector)
                ((ScalingDifficultySelector)selector).setPlayerData(SHHooks.getCachedData());
            if(selector.isValid(pos, world, this.ruleDescriptor)) {
                this.dynamicTypeMap.get(selector.getType()).getSecond().increment();
            }
        }
        boolean ret = true;
        //logExcessiveCollection(this.dynamicTypeMap.entrySet(), entry ->
                //"(Map Entry) | Key("+entry.getKey().toString()+") | Value("+tupleToString(entry.getValue())+")");
        for(Tuple<Integer,MutableInt> typeCounter : this.dynamicTypeMap.values()) {
            MutableInt counter = typeCounter.getSecond();
            if(counter.getValue()<=0) ret = false;
            counter.setValue(0);
        }
        return ret;
    }

    protected String tupleToString(Tuple<?,?> tuple) {
        return "(Tuple) | First("+tuple.getFirst()+") | Second("+tuple.getSecond()+")";
    }

    protected <E> void logExcessiveCollection(Collection<E> c, Function<E,String> entryFunc) {
        if(this.shouldLogExcessively) {
            Constants.logVerbose(Level.INFO,"Excessively logging collection with {} entries",c.size());
            for (E element : c)
                Constants.logVerbose(Level.INFO, entryFunc.apply(element));
        }
    }

    protected void logExcessiveDebug(String msg, Object ... parameters) {
        doExcessiveLogging(Level.DEBUG,msg,parameters);
    }

    protected void logExcessiveInfo(String msg, Object ... parameters) {
        doExcessiveLogging(Level.INFO,msg,parameters);
    }

    private void doExcessiveLogging(Level level, String msg, Object ... parameters) {
        if(this.shouldLogExcessively)
            Constants.logVerbose(level,msg,parameters);
    }

    public abstract boolean isRemoval();

    @Override
    public String toString() {
        return this.ruleDescriptor;
    }
}

package mods.thecomputerizer.specifiedspawning.core;

import mods.thecomputerizer.specifiedspawning.rules.group.SpawnGroup;
import mods.thecomputerizer.specifiedspawning.rules.group.SpawnGroup.Builder;
import mods.thecomputerizer.specifiedspawning.util.ThreadSafety;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.SpawnListEntry;

import java.util.*;
import java.util.Map.Entry;

import static net.minecraftforge.fml.common.registry.ForgeRegistries.BIOMES;
import static org.apache.logging.log4j.Level.INFO;

public class SpawnManager {

    private static final Map<String,EnumCreatureType> EXISTING_CREATURE_TYPES = ThreadSafety.newMap(HashMap::new);
    private static final Map<EnumCreatureType,Builder> BUILDERS_BY_TYPE = ThreadSafety.newMap(HashMap::new);
    private static final Map<String,SpawnGroup> SPAWN_GROUPS = ThreadSafety.newMap(HashMap::new);
    private static final Map<EnumCreatureType, SpawnGroup> SPAWN_GROUP_ENUMS = ThreadSafety.newMap(HashMap::new);
    private static final Map<Biome,Map<EnumCreatureType,List<SpawnListEntry>>> DEFAULT_SPAWN_ENTRIES = ThreadSafety.newMap(HashMap::new);
    private static final Map<Class<? extends Entity>,EnumCreatureType> CREATURE_TYPE_MAP = ThreadSafety.newMap(HashMap::new);

    public static void addExistingCreatureType(String name, EnumCreatureType type) {
        EXISTING_CREATURE_TYPES.put(name,type);
    }
    
    public static void clear() {
        for(Entry<Biome,Map<EnumCreatureType,List<SpawnListEntry>>> biomeEntry : DEFAULT_SPAWN_ENTRIES.entrySet()) {
            Biome biome = biomeEntry.getKey();
            for(Entry<EnumCreatureType,List<SpawnListEntry>> creatureTypeEntry : biomeEntry.getValue().entrySet()) {
                EnumCreatureType type = creatureTypeEntry.getKey();
                List<SpawnListEntry> spawnEntries = creatureTypeEntry.getValue();
                biome.getSpawnableList(type).clear();
                biome.getSpawnableList(type).addAll(spawnEntries);
            }
        }
        CREATURE_TYPE_MAP.clear();
    }

    public static void buildSpawnGroups() {
        for(Builder builder : BUILDERS_BY_TYPE.values()) {
            Constants.logVerbose(INFO,"Building spawn group '{}'",builder.getName());
            SpawnGroup group = builder.build();
            SPAWN_GROUPS.put(builder.getName(),group);
            SPAWN_GROUP_ENUMS.put(group.getType(),group);
        }
    }
    
    public static Collection<SpawnGroup> getAllSpawnGroups() {
        return Collections.unmodifiableCollection(SPAWN_GROUPS.values());
    }
    
    public static int getDynamicMaxNumberOfCreature(EnumCreatureType type, int defVal) {
        SpawnGroup group = SPAWN_GROUP_ENUMS.get(type);
        return Objects.nonNull(group) ? group.getCount() : defVal;
    }
    
    public static EnumCreatureType getExistingCreatureType(String name) {
        return EXISTING_CREATURE_TYPES.get(name);
    }
    
    public static SpawnGroup getSpawnGroup(String groupName) {
        return SPAWN_GROUPS.get(groupName);
    }
    
    public static String getSpawnGroupName(SpawnGroup group) {
        for(Entry<String,SpawnGroup> entry : SPAWN_GROUPS.entrySet())
            if(group==entry.getValue()) return entry.getKey();
        return null;
    }
    
    public static boolean hasBuilder(EnumCreatureType type) {
        return BUILDERS_BY_TYPE.containsKey(type);
    }
    
    public static boolean isExistingCreatureType(String name) {
        return EXISTING_CREATURE_TYPES.containsKey(name);
    }
    
    public static void loadDefaultSpawnEntries() {
        for(Biome biome : BIOMES.getValuesCollection()) {
            DEFAULT_SPAWN_ENTRIES.putIfAbsent(biome,new HashMap<>());
            for(EnumCreatureType creatureType : EnumCreatureType.values()) {
                DEFAULT_SPAWN_ENTRIES.get(biome).put(creatureType,new ArrayList<>());
                for(SpawnListEntry entry : biome.getSpawnableList(creatureType))
                    DEFAULT_SPAWN_ENTRIES.get(biome).get(creatureType).add(entry);
            }
        }
    }
    
    public static void setTypeBuilder(EnumCreatureType type, Builder builder) {
        BUILDERS_BY_TYPE.put(type,builder);
    }
}
package mods.thecomputerizer.specifiedspawning.core;

import mods.thecomputerizer.specifiedspawning.rules.group.SpawnGroup;
import mods.thecomputerizer.specifiedspawning.util.ThreadSafety;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import org.apache.logging.log4j.Level;

import java.util.*;

public class SpawnManager {

    private static final Map<String,EnumCreatureType> EXISTING_CREATURE_TYPES = ThreadSafety.newMap(HashMap::new);
    private static final Map<EnumCreatureType,SpawnGroup.Builder> BUILDERS_BY_TYPE = ThreadSafety.newMap(HashMap::new);
    private static final Map<String, SpawnGroup> SPAWN_GROUPS = ThreadSafety.newMap(HashMap::new);
    private static final Map<EnumCreatureType, SpawnGroup> SPAWN_GROUP_ENUMS = ThreadSafety.newMap(HashMap::new);
    private static final Map<Biome,Map<EnumCreatureType,List<Biome.SpawnListEntry>>> DEFAULT_SPAWN_ENTRIES = ThreadSafety.newMap(HashMap::new);
    private static final Map<Class<? extends Entity>,EnumCreatureType> CREATURE_TYPE_MAP = ThreadSafety.newMap(HashMap::new);

    public static void loadDefaultSpawnEntries() {
        for(Biome biome : ForgeRegistries.BIOMES.getValuesCollection()) {
            DEFAULT_SPAWN_ENTRIES.putIfAbsent(biome,new HashMap<>());
            for(EnumCreatureType creatureType : EnumCreatureType.values()) {
                DEFAULT_SPAWN_ENTRIES.get(biome).put(creatureType,new ArrayList<>());
                for(Biome.SpawnListEntry entry : biome.getSpawnableList(creatureType))
                    DEFAULT_SPAWN_ENTRIES.get(biome).get(creatureType).add(entry);
            }
        }
    }

    public static void addExistingCreatureType(String name, EnumCreatureType type) {
        EXISTING_CREATURE_TYPES.put(name,type);
    }

    public static boolean isExistingCreatureType(String name) {
        return EXISTING_CREATURE_TYPES.containsKey(name);
    }

    public static EnumCreatureType getExistingCreatureType(String name) {
        return EXISTING_CREATURE_TYPES.get(name);
    }

    public static void setTypeBuilder(EnumCreatureType type, SpawnGroup.Builder builder) {
        BUILDERS_BY_TYPE.put(type,builder);
    }

    public static boolean hasBuilder(EnumCreatureType type) {
        return BUILDERS_BY_TYPE.containsKey(type);
    }

    public static void buildSpawnGroups() {
        for(SpawnGroup.Builder builder : BUILDERS_BY_TYPE.values()) {
            Constants.logVerbose(Level.INFO,"Building spawn group '{}'",builder.getName());
            SpawnGroup group = builder.build();
            SPAWN_GROUPS.put(builder.getName(),group);
            SPAWN_GROUP_ENUMS.put(group.getType(),group);
        }
    }
    public static SpawnGroup getSpawnGroup(String groupName) {
        return SPAWN_GROUPS.get(groupName);
    }

    public static Collection<SpawnGroup> getAllSpawnGroups() {
        return Collections.unmodifiableCollection(SPAWN_GROUPS.values());
    }

    public static int getDynamicMaxNumberOfCreature(EnumCreatureType type, int defVal) {
        SpawnGroup group = SPAWN_GROUP_ENUMS.get(type);
        return Objects.nonNull(group) ? group.getCount() : defVal;
    }

    public static void clear() {
        for(Map.Entry<Biome,Map<EnumCreatureType,List<Biome.SpawnListEntry>>> biomeEntry : DEFAULT_SPAWN_ENTRIES.entrySet()) {
            Biome biome = biomeEntry.getKey();
            for(Map.Entry<EnumCreatureType,List<Biome.SpawnListEntry>> creatureTypeEntry : biomeEntry.getValue().entrySet()) {
                EnumCreatureType type = creatureTypeEntry.getKey();
                List<Biome.SpawnListEntry> spawnEntries = creatureTypeEntry.getValue();
                biome.getSpawnableList(type).clear();
                biome.getSpawnableList(type).addAll(spawnEntries);
            }
        }
        CREATURE_TYPE_MAP.clear();
    }
}

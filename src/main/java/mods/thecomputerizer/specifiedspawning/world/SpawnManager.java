package mods.thecomputerizer.specifiedspawning.world;

import mods.thecomputerizer.specifiedspawning.core.Constants;
import mods.thecomputerizer.specifiedspawning.rules.IRuleBuilder;
import mods.thecomputerizer.specifiedspawning.rules.RuleManager;
import mods.thecomputerizer.specifiedspawning.rules.RuleType;
import mods.thecomputerizer.specifiedspawning.rules.group.SpawnGroup;
import mods.thecomputerizer.specifiedspawning.util.ThreadSafety;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import org.apache.logging.log4j.Level;

import java.util.*;

public class SpawnManager {

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

    public static void loadDefaultSpawnGroups() {
        RuleManager.addDefaultGroups(new SpawnGroup.Builder("hostile",EnumCreatureType.MONSTER),
                new SpawnGroup.Builder("passive",EnumCreatureType.CREATURE),
                new SpawnGroup.Builder("aquatic",EnumCreatureType.WATER_CREATURE),
                new SpawnGroup.Builder("ambient",EnumCreatureType.AMBIENT));
    }

    public static void loadDefaults() {
        loadDefaultSpawnGroups();
        loadDefaultSpawnEntries();
    }

    public static void buildSpawnGroups() {
        for(IRuleBuilder b : RuleManager.RULE_BUILDERS.get(RuleType.GROUP)) {
            SpawnGroup.Builder builder = (SpawnGroup.Builder)b;
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
        SPAWN_GROUPS.clear();
        CREATURE_TYPE_MAP.clear();
    }
}

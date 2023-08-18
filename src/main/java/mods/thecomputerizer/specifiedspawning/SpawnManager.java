package mods.thecomputerizer.specifiedspawning;

import mods.thecomputerizer.specifiedspawning.rules.group.SpawnGroup;
import mods.thecomputerizer.specifiedspawning.util.ThreadSafety;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.*;

public class SpawnManager {

    private static final Set<SpawnGroup.Builder> SPAWN_GROUP_BUILDERS = ThreadSafety.newSet(HashSet::new);
    private static final Map<String, SpawnGroup> SPAWN_GROUPS = ThreadSafety.newMap(HashMap::new);
    private static final Map<Biome,Map<EnumCreatureType,List<Biome.SpawnListEntry>>> DEFAULT_SPAWN_ENTRIES = ThreadSafety.newMap(HashMap::new);
    private static final Map<Class<? extends Entity>,EnumCreatureType> CREATURE_TYPE_MAP = ThreadSafety.newMap(HashMap::new);

    public static void loadDefaultSpawnEntries() {
        for(Biome biome : ForgeRegistries.BIOMES.getValuesCollection()) {
            DEFAULT_SPAWN_ENTRIES.putIfAbsent(biome,new HashMap<>());
            for(EnumCreatureType creatureType : EnumCreatureType.values())
                DEFAULT_SPAWN_ENTRIES.get(biome).put(creatureType,biome.getSpawnableList(creatureType));
        }
    }

    public static void loadDefaultSpawnGroups() {
        SPAWN_GROUP_BUILDERS.add(new SpawnGroup.Builder("hostile",EnumCreatureType.MONSTER));
        SPAWN_GROUP_BUILDERS.add(new SpawnGroup.Builder("passive",EnumCreatureType.CREATURE));
        SPAWN_GROUP_BUILDERS.add(new SpawnGroup.Builder("aquatic",EnumCreatureType.WATER_CREATURE));
        SPAWN_GROUP_BUILDERS.add(new SpawnGroup.Builder("ambient",EnumCreatureType.AMBIENT));
    }

    public static void loadDefaults() {
        loadDefaultSpawnGroups();
        loadDefaultSpawnEntries();
    }

    public static void buildSpawnGroups() {
        for(SpawnGroup.Builder builder : SPAWN_GROUP_BUILDERS)
            SPAWN_GROUPS.put(builder.getName(),builder.build());
    }

    public static EnumCreatureType getEntityType(Class<? extends Entity> entityClass) {
        return CREATURE_TYPE_MAP.get(entityClass);
    }

    public static boolean hasRules(EntityLiving entity) {
        return false;
    }

    public static boolean checkWorld(World world) {
        return false;
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
        SPAWN_GROUP_BUILDERS.clear();
        CREATURE_TYPE_MAP.clear();
    }
}

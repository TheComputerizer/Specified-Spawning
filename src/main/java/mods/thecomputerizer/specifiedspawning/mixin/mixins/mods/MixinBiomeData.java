package mods.thecomputerizer.specifiedspawning.mixin.mixins.mods;

import logictechcorp.libraryex.world.biome.data.BiomeData;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Pseudo
@Mixin(value = BiomeData.class, remap = false)
public class MixinBiomeData {

    @Shadow @Final protected Map<EnumCreatureType, List<Biome.SpawnListEntry>> entitySpawns;

    @Shadow @Final protected Biome biome;

    /**
     * @author The_Computerizer
     * @reason Prioritize spawn entries from Specified Spawning
     */
    @Overwrite
    public void addEntitySpawn(EnumCreatureType type, Biome.SpawnListEntry spawnListEntry) {
        this.entitySpawns.putIfAbsent(type,new ArrayList<>(this.biome.getSpawnableList(type)));
        boolean shouldAdd = true;
        for(EnumCreatureType type1 : EnumCreatureType.values()) {
            for(Biome.SpawnListEntry entry : this.biome.getSpawnableList(type1)) {
                if(entry.entityClass == spawnListEntry.entityClass) {
                    shouldAdd = false;
                    break;
                }
            }
            if(!shouldAdd) break;
        }
        if(shouldAdd) this.entitySpawns.get(type).add(spawnListEntry);
    }
}

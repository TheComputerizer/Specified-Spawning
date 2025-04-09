package mods.thecomputerizer.specifiedspawning.mixin.mixins.vanilla;

import com.google.common.collect.Lists;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.SpawnListEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;
import java.util.Map;

@Mixin(Biome.class)
public class MixinBiome {

    @Shadow protected List<SpawnListEntry> spawnableMonsterList;
    @Shadow protected List<SpawnListEntry> spawnableCreatureList;
    @Shadow protected List<SpawnListEntry> spawnableWaterCreatureList;
    @Shadow protected List<SpawnListEntry> spawnableCaveCreatureList;
    @Shadow(remap = false) protected Map<EnumCreatureType,List<SpawnListEntry>> modSpawnableLists;

    /**
     * @author The_Computerizer
     * @reason Catch OOB exceptions when other mods are trying to iterate through EnumCreatureType values
     */
    @Overwrite public List<SpawnListEntry> getSpawnableList(EnumCreatureType creatureType) {
        switch(creatureType) {
            case AMBIENT: return this.spawnableCaveCreatureList;
            case CREATURE: return this.spawnableCreatureList;
            case MONSTER: return this.spawnableMonsterList;
            case WATER_CREATURE: return this.spawnableWaterCreatureList;
            default: {
                if(!this.modSpawnableLists.containsKey(creatureType))
                    this.modSpawnableLists.put(creatureType,Lists.newArrayList());
                return this.modSpawnableLists.get(creatureType);
            }
        }
    }
}
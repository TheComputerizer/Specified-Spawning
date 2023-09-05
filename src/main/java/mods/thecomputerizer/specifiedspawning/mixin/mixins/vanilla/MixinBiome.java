package mods.thecomputerizer.specifiedspawning.mixin.mixins.vanilla;

import com.google.common.collect.Lists;
import mods.thecomputerizer.specifiedspawning.core.Constants;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.world.biome.Biome;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Mixin(Biome.class)
public class MixinBiome {

    @Shadow protected List<Biome.SpawnListEntry> spawnableMonsterList;

    @Shadow protected List<Biome.SpawnListEntry> spawnableCreatureList;

    @Shadow protected List<Biome.SpawnListEntry> spawnableWaterCreatureList;

    @Shadow protected List<Biome.SpawnListEntry> spawnableCaveCreatureList;

    @Shadow(remap = false) protected Map<EnumCreatureType, List<Biome.SpawnListEntry>> modSpawnableLists;

    /**
     * @author The_Computerizer
     * @reason Catch OOB exceptions when other mods are trying to iterate through EnumCreatureType values
     */
    @Overwrite
    public List<Biome.SpawnListEntry> getSpawnableList(EnumCreatureType creatureType) {
        try {
            switch (creatureType) {
                case MONSTER:
                    return this.spawnableMonsterList;
                case CREATURE:
                    return this.spawnableCreatureList;
                case WATER_CREATURE:
                    return this.spawnableWaterCreatureList;
                case AMBIENT:
                    return this.spawnableCaveCreatureList;
                default:
                    if (!this.modSpawnableLists.containsKey(creatureType))
                        this.modSpawnableLists.put(creatureType, Lists.newArrayList());
                    return this.modSpawnableLists.get(creatureType);
            }
        } catch (ArrayIndexOutOfBoundsException ex) {
            Constants.logVerbose(Level.ERROR,"OOB {}",creatureType);
            return new ArrayList<>();
        }
    }
}

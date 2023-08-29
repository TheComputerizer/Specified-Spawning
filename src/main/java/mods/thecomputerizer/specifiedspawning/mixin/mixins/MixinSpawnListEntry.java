package mods.thecomputerizer.specifiedspawning.mixin.mixins;

import mods.thecomputerizer.specifiedspawning.mixin.access.IPotentialJockey;
import mods.thecomputerizer.specifiedspawning.mixin.access.ISpawnGroupObject;
import mods.thecomputerizer.specifiedspawning.rules.group.SpawnGroup;
import mods.thecomputerizer.specifiedspawning.world.entity.Jockey;
import net.minecraft.entity.EntityLiving;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Mixin(value = Biome.SpawnListEntry.class, remap = false)
public class MixinSpawnListEntry implements ISpawnGroupObject, IPotentialJockey {

    @Unique
    private SpawnGroup specifiedspawning$spawnGroup;

    @Unique
    private boolean specifiedspawning$isModifiedSpawn;

    @Unique
    private final List<Jockey> specifiedspawning$potentialJockeys = new ArrayList<>();

    @Inject(at = @At("RETURN"), method = "newInstance")
    private void specifiedspawning$newInstance(World world, CallbackInfoReturnable<EntityLiving> cir) {
        if(Objects.nonNull(this.specifiedspawning$spawnGroup)) {
            EntityLiving entity = cir.getReturnValue();
            ((ISpawnGroupObject)entity).specifiedspawning$setSpawnGroup(
                    this.specifiedspawning$spawnGroup, this.specifiedspawning$isModifiedSpawn);
            for(Jockey jockey : this.specifiedspawning$potentialJockeys)
                ((IPotentialJockey)entity).specifiedspawning$addJockey(jockey);
        }
    }

    @Override
    public void specifiedspawning$setSpawnGroup(SpawnGroup group, boolean isModifiedSpawn) {
        this.specifiedspawning$spawnGroup = group;
        this.specifiedspawning$isModifiedSpawn = isModifiedSpawn;
    }

    @Override
    public void specifiedspawning$addJockey(Jockey jockey) {
        this.specifiedspawning$potentialJockeys.add(jockey);
    }
}

package mods.thecomputerizer.specifiedspawning.mixin.mixins.vanilla;

import mods.thecomputerizer.specifiedspawning.mixin.access.IPotentialJockey;
import mods.thecomputerizer.specifiedspawning.mixin.access.ISpawnGroupObject;
import mods.thecomputerizer.specifiedspawning.rules.DynamicRule;
import mods.thecomputerizer.specifiedspawning.rules.IRule;
import mods.thecomputerizer.specifiedspawning.rules.group.SpawnGroup;
import mods.thecomputerizer.specifiedspawning.world.entity.Jockey;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLiving.SpawnPlacementType;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Mixin(value = Biome.SpawnListEntry.class, remap = false)
public class MixinSpawnListEntry implements ISpawnGroupObject, IPotentialJockey {

    @Unique private final List<DynamicRule> specifiedspawning$assignedRules = new ArrayList<>();

    @Unique private SpawnGroup specifiedspawning$spawnGroup;

    @Unique private boolean specifiedspawning$isModifiedSpawn;
    @Unique private SpawnPlacementType specifiedspawning$cachedSpawnType;

    @Unique private final List<Jockey> specifiedspawning$potentialJockeys = new ArrayList<>();

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

    @Override
    public void specifiedspawning$addDynamicRule(DynamicRule rule) {
        this.specifiedspawning$assignedRules.add(rule);
    }

    @Override
    public void specifiedspawning$sortRules() {
        this.specifiedspawning$assignedRules.sort(Comparator.comparingInt(IRule::getOrder));
    }

    @Override
    public List<DynamicRule> specifiedspawning$getDynamicRules() {
        return this.specifiedspawning$assignedRules;
    }

    @Override
    public SpawnPlacementType specifiedspawning$getSpawnType(SpawnPlacementType defType) {
        return Objects.nonNull(this.specifiedspawning$cachedSpawnType) ? this.specifiedspawning$cachedSpawnType : defType;
    }

    @Override
    public void specifiedspawning$setSpawnType(SpawnPlacementType cachedType) {
        this.specifiedspawning$cachedSpawnType = cachedType;
    }
}

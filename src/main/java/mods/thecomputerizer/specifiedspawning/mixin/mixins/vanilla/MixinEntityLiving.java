package mods.thecomputerizer.specifiedspawning.mixin.mixins.vanilla;

import mods.thecomputerizer.specifiedspawning.mixin.access.IPotentialJockey;
import mods.thecomputerizer.specifiedspawning.world.entity.Jockey;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.util.WeightedRandom;
import net.minecraft.world.DifficultyInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(EntityLiving.class)
public abstract class MixinEntityLiving implements IPotentialJockey {

    @Unique private final List<Jockey> specifiedspawning$potentialJockeys = new ArrayList<>();

    @SuppressWarnings("DataFlowIssue")
    @Inject(at = @At("TAIL"), method = "onInitialSpawn")
    private void specifiedspawning$onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata,
            CallbackInfoReturnable<IEntityLivingData> cir) {
        EntityLiving living = (EntityLiving)(Object)this;
        if(!living.isBeingRidden() && !this.specifiedspawning$potentialJockeys.isEmpty()) {
            Jockey jockey = WeightedRandom.getRandomItem(living.world.rand,this.specifiedspawning$potentialJockeys);
            if(jockey.isValidJockey()) jockey.spawnRider(living,difficulty);
        }
    }

    @Override public void specifiedspawning$addJockey(Jockey jockey) {
        this.specifiedspawning$potentialJockeys.add(jockey);
    }
}
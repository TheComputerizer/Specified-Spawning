package mods.thecomputerizer.specifiedspawning.mixin.mixins;

import mods.thecomputerizer.specifiedspawning.core.Constants;
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

    @Unique
    private final List<Jockey> specifiedspawning$potentialJockeys = new ArrayList<>();

    @Inject(at = @At("TAIL"), method = "onInitialSpawn")
    private void specifiedspawning$onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata,
                                                  CallbackInfoReturnable<IEntityLivingData> cir) {
        EntityLiving living = (EntityLiving)(Object)this;
        if(!living.isBeingRidden()) {
            Constants.LOGGER.error("PRE RANDOM");
            Jockey jockey = WeightedRandom.getRandomItem(living.world.rand,this.specifiedspawning$potentialJockeys);
            Constants.LOGGER.error("POST RANDOM");
            if(jockey.isValidJockey()) jockey.spawnRider(living,difficulty);
            Constants.LOGGER.error("POST SPAWN");
        }
    }

    @Override
    public void specifiedspawning$addJockey(Jockey jockey) {
        this.specifiedspawning$potentialJockeys.add(jockey);
    }
}

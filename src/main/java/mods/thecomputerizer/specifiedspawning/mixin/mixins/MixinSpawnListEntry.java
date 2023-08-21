package mods.thecomputerizer.specifiedspawning.mixin.mixins;

import mods.thecomputerizer.specifiedspawning.mixin.access.ISpawnGroupObject;
import mods.thecomputerizer.specifiedspawning.rules.group.SpawnGroup;
import net.minecraft.entity.EntityLiving;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(value = Biome.SpawnListEntry.class, remap = false)
public abstract class MixinSpawnListEntry implements ISpawnGroupObject {

    @Unique
    private SpawnGroup specifiedspawning$SpawnGroup;

    @Inject(at = @At("RETURN"), method = "newInstance")
    private void specifiedspawning$newInstance(World world, CallbackInfoReturnable<EntityLiving> cir) {
        if(Objects.nonNull(this.specifiedspawning$SpawnGroup))
            ((ISpawnGroupObject) cir.getReturnValue()).specifiedspawning$setSpawnGroup(this.specifiedspawning$SpawnGroup);
    }

    @Override
    public void specifiedspawning$setSpawnGroup(SpawnGroup group) {
        this.specifiedspawning$SpawnGroup = group;
    }
}

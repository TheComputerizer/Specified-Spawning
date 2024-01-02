package mods.thecomputerizer.specifiedspawning.mixin.mixins.vanilla;

import mods.thecomputerizer.specifiedspawning.mixin.access.ISpawnGroupObject;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLiving.SpawnPlacementType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldEntitySpawner;
import net.minecraft.world.biome.Biome.SpawnListEntry;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Objects;

@Mixin(WorldEntitySpawner.class)
public abstract class MixinWorldEntitySpawner {

    @Shadow
    public static boolean canCreatureTypeSpawnAtLocation(SpawnPlacementType type, World world, BlockPos pos) {
        return false;
    }

    @Unique SpawnListEntry specifiedSpawning$cachedSpawnEntry;

    @Redirect(at = @At(value = "FIELD", target = "Lnet/minecraft/world/biome/Biome$SpawnListEntry;"+
            "entityClass:Ljava/lang/Class;", opcode = Opcodes.GETFIELD),
            method = "findChunksForSpawning")
    private Class<? extends EntityLiving> specifiedspawning$cacheSpawnEntry(SpawnListEntry entry) {
        this.specifiedSpawning$cachedSpawnEntry = entry;
        return entry.entityClass;
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/WorldEntitySpawner;"+
            "canCreatureTypeSpawnAtLocation(Lnet/minecraft/entity/EntityLiving$SpawnPlacementType;"+
            "Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)Z"), method = "findChunksForSpawning")
    private boolean specifiedspawning$redirectSpawnPlacement(SpawnPlacementType type, World world, BlockPos pos) {
        type = Objects.nonNull(this.specifiedSpawning$cachedSpawnEntry) ?
                ((ISpawnGroupObject)this.specifiedSpawning$cachedSpawnEntry).specifiedspawning$getSpawnType(type) : type;
        this.specifiedSpawning$cachedSpawnEntry = null;
        return canCreatureTypeSpawnAtLocation(type, world, pos);
    }



    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/EntityLiving;getCanSpawnHere()Z"),
            method = "findChunksForSpawning")
    private boolean specifiedspawning$redirectCanSpawn(EntityLiving entity) {
        return ((ISpawnGroupObject)entity).specifiedspawning$shouldIgnoreSpawnConditions() || entity.getCanSpawnHere();
    }
}

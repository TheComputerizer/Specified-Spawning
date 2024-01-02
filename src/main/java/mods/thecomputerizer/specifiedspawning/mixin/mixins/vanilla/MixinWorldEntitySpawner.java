package mods.thecomputerizer.specifiedspawning.mixin.mixins.vanilla;

import mods.thecomputerizer.specifiedspawning.mixin.access.ISpawnGroupObject;
import net.minecraft.entity.EntityLiving.SpawnPlacementType;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldEntitySpawner;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.Biome.SpawnListEntry;
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

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/WorldServer;"+
            "canCreatureTypeSpawnHere(Lnet/minecraft/entity/EnumCreatureType;"+
            "Lnet/minecraft/world/biome/Biome$SpawnListEntry;Lnet/minecraft/util/math/BlockPos;)Z"),
            method = "findChunksForSpawning")
    private boolean specifiedspawning$cacheSpawnEntry(
            WorldServer server, EnumCreatureType type, SpawnListEntry entry, BlockPos pos) {
        this.specifiedSpawning$cachedSpawnEntry = entry;
        return server.canCreatureTypeSpawnHere(type,entry,pos);
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/WorldEntitySpawner;"+
            "canCreatureTypeSpawnAtLocation(Lnet/minecraft/entity/EntityLiving$SpawnPlacementType;"+
            "Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)Z"), method = "findChunksForSpawning")
    private boolean specifiedspawning$redirectSpawnPlacement(SpawnPlacementType type, World world, BlockPos pos) {
        type = Objects.nonNull(this.specifiedSpawning$cachedSpawnEntry) ?
                ((ISpawnGroupObject)this.specifiedSpawning$cachedSpawnEntry).specifiedspawning$getSpawnType(type) : type;
        this.specifiedSpawning$cachedSpawnEntry = null;
        return canCreatureTypeSpawnAtLocation(type,world,pos);
    }
}

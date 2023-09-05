package mods.thecomputerizer.specifiedspawning.mixin.mixins.vanilla;

import mods.thecomputerizer.specifiedspawning.mixin.access.ISpawnGroupObject;
import mods.thecomputerizer.specifiedspawning.rules.group.SpawnGroup;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;

import java.util.Objects;

@Mixin(value = Entity.class, remap = false)
public abstract class MixinEntity implements ISpawnGroupObject {

    @Unique
    private SpawnGroup specifiedspawning$spawnGroup;

    @SuppressWarnings({"unused", "FieldCanBeLocal"})
    @Unique
    private boolean specifiedspawning$isModifiedSpawn;

    /**
     * @author The_Computerizer
     * @reason Replace creature type reference with custom spawn group logic
     */
    @SuppressWarnings("ConstantValue")
    @Overwrite
    public boolean isCreatureType(EnumCreatureType type, boolean forSpawnCount) {
        if (forSpawnCount && ((Object)this instanceof EntityLiving) && ((EntityLiving)(Object)this).isNoDespawnRequired()) return false;
        return Objects.nonNull(this.specifiedspawning$spawnGroup) ? this.specifiedspawning$spawnGroup.getType()==type :
                type.getCreatureClass().isAssignableFrom(this.getClass());
    }

    @Override
    public void specifiedspawning$setSpawnGroup(SpawnGroup group, boolean isModifiedSpawn) {
        this.specifiedspawning$spawnGroup = group;
        this.specifiedspawning$isModifiedSpawn = isModifiedSpawn;
    }
}

package mods.thecomputerizer.specifiedspawning.mixin.mixins;

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
    private SpawnGroup specifiedspawning$SpawnGroup;

    /**
     * @author The_Computerizer
     * @reason Replace creature type reference with custom spawn group logic
     */
    @SuppressWarnings("ConstantValue")
    @Overwrite
    public boolean isCreatureType(EnumCreatureType type, boolean forSpawnCount) {
        if (forSpawnCount && ((Object)this instanceof EntityLiving) && ((EntityLiving)(Object)this).isNoDespawnRequired()) return false;
        return Objects.nonNull(this.specifiedspawning$SpawnGroup) ? this.specifiedspawning$SpawnGroup.getType()==type :
                type.getCreatureClass().isAssignableFrom(this.getClass());
    }

    @Override
    public void specifiedspawning$setSpawnGroup(SpawnGroup group) {
        this.specifiedspawning$SpawnGroup = group;
    }
}

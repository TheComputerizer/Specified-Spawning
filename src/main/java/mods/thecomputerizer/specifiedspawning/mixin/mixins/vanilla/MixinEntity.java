package mods.thecomputerizer.specifiedspawning.mixin.mixins.vanilla;

import mods.thecomputerizer.specifiedspawning.mixin.access.ISpawnGroupObject;
import mods.thecomputerizer.specifiedspawning.rules.DynamicRule;
import mods.thecomputerizer.specifiedspawning.rules.group.SpawnGroup;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLiving.SpawnPlacementType;
import net.minecraft.entity.EnumCreatureType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Mixin(value = Entity.class, remap = false)
public abstract class MixinEntity implements ISpawnGroupObject {

    @Unique private SpawnGroup specifiedspawning$spawnGroup;

    @SuppressWarnings({"unused", "FieldCanBeLocal"})
    @Unique private boolean specifiedspawning$isModifiedSpawn;

    @Unique private SpawnPlacementType specifiedspawning$cachedSpawnType;
    @Unique private boolean specifiedspawning$ignoreSpawnConditions;

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

    @Override
    public void specifiedspawning$addDynamicRule(DynamicRule rule) {}

    @Override
    public void specifiedspawning$sortRules() {}

    @Override
    public List<DynamicRule> specifiedspawning$getDynamicRules() {
        return new ArrayList<>();
    }

    @Override
    public EntityLiving.SpawnPlacementType specifiedspawning$getSpawnType(EntityLiving.SpawnPlacementType defType) {
        return Objects.nonNull(this.specifiedspawning$cachedSpawnType) ? this.specifiedspawning$cachedSpawnType : defType;
    }

    @Override
    public void specifiedspawning$setSpawnType(EntityLiving.SpawnPlacementType cachedType) {
        this.specifiedspawning$cachedSpawnType = cachedType;
    }

    @Override
    public void specifiedspawning$setIgnoreSpawnConditions(boolean ignore) {
        this.specifiedspawning$ignoreSpawnConditions = ignore;
    }

    @Override
    public boolean specifiedspawning$shouldIgnoreSpawnConditions() {
        return this.specifiedspawning$ignoreSpawnConditions;
    }
}

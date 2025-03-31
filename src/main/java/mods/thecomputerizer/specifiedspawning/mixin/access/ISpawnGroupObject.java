package mods.thecomputerizer.specifiedspawning.mixin.access;

import mods.thecomputerizer.specifiedspawning.rules.DynamicRule;
import mods.thecomputerizer.specifiedspawning.rules.group.SpawnGroup;
import net.minecraft.entity.EntityLiving.SpawnPlacementType;

import java.util.List;

public interface ISpawnGroupObject {

    void specifiedspawning$setSpawnGroup(SpawnGroup group, boolean isModifiedSpawn);
    void specifiedspawning$addDynamicRule(DynamicRule rule);
    void specifiedspawning$sortRules();
    List<DynamicRule> specifiedspawning$getDynamicRules();
    SpawnPlacementType specifiedspawning$getSpawnType(SpawnPlacementType defType);
    void specifiedspawning$setSpawnType(SpawnPlacementType cachedType);
    void specifiedspawning$setIgnoreSpawnConditions(boolean ignore);
    boolean specifiedspawning$shouldIgnoreSpawnConditions();
}
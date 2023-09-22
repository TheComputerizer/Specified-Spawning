package mods.thecomputerizer.specifiedspawning.mixin.access;

import mods.thecomputerizer.specifiedspawning.rules.DynamicRule;
import mods.thecomputerizer.specifiedspawning.rules.group.SpawnGroup;

import java.util.List;

public interface ISpawnGroupObject {

    void specifiedspawning$setSpawnGroup(SpawnGroup group, boolean isModifiedSpawn);
    void specifiedspawning$addDynamicRule(DynamicRule rule);
    void specifiedspawning$sortRules();
    List<DynamicRule> specifiedspawning$getDynamicRules();
}

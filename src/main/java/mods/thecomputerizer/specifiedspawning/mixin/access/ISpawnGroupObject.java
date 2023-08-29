package mods.thecomputerizer.specifiedspawning.mixin.access;

import mods.thecomputerizer.specifiedspawning.rules.group.SpawnGroup;

public interface ISpawnGroupObject {

    void specifiedspawning$setSpawnGroup(SpawnGroup group, boolean isModifiedSpawn);
}

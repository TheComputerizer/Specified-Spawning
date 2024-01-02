package mods.thecomputerizer.specifiedspawning.util;

import net.minecraft.entity.EntityLiving.SpawnPlacementType;
import net.minecraftforge.common.util.EnumHelper;

import javax.annotation.Nullable;

public class AddedEnums {

    public static final SpawnPlacementType ANY_SPAWN = EnumHelper.addSpawnPlacementType("all",null);

    public static @Nullable SpawnPlacementType getSpawnType(String type) {
        if(type.matches("ground")) return SpawnPlacementType.ON_GROUND;
        if(type.matches("water")) return SpawnPlacementType.IN_WATER;
        if(type.matches("air")) return SpawnPlacementType.IN_AIR;
        if(type.matches("any")) return ANY_SPAWN;
        return null;
    }
}

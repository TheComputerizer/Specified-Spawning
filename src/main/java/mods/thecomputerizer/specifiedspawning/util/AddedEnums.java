package mods.thecomputerizer.specifiedspawning.util;

import net.minecraft.entity.EntityLiving.SpawnPlacementType;
import net.minecraftforge.common.util.EnumHelper;

import javax.annotation.Nullable;

import static net.minecraft.entity.EntityLiving.SpawnPlacementType.IN_AIR;
import static net.minecraft.entity.EntityLiving.SpawnPlacementType.IN_WATER;
import static net.minecraft.entity.EntityLiving.SpawnPlacementType.ON_GROUND;

public class AddedEnums {

    public static final SpawnPlacementType ANY_SPAWN =
            EnumHelper.addSpawnPlacementType("all",(access,pos) -> true);

    public static @Nullable SpawnPlacementType getSpawnType(String type) {
        switch(type) {
            case "air": return IN_AIR;
            case "any": return ANY_SPAWN;
            case "ground": return ON_GROUND;
            case "water": return IN_WATER;
            default: return null;
        }
    }
}

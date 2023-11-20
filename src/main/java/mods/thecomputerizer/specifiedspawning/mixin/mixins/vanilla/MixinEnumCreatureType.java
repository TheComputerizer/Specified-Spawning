package mods.thecomputerizer.specifiedspawning.mixin.mixins.vanilla;

import mods.thecomputerizer.specifiedspawning.core.SpawnManager;
import mods.thecomputerizer.specifiedspawning.rules.group.SpawnGroup;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.passive.IAnimals;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;
import java.util.Optional;

@Mixin(EnumCreatureType.class)
public abstract class MixinEnumCreatureType {

    @Shadow(remap = false) @Final @Mutable private static EnumCreatureType[] $VALUES;

    @Shadow @Final private int maxNumberOfCreature;

    @SuppressWarnings("SameParameterValue")
    @Invoker(value = "<init>")
    private static EnumCreatureType specifiedspawning$construct(
            String name, int ordinal,Class <? extends IAnimals> classType, int maxCreatures, Material mat,
            boolean isPeaceful, boolean isAnimal) {
        throw new IllegalStateException("Unreachable");
    }

    @Inject(at = @At("TAIL"), method = "<clinit>")
    private static void specifiedspawning$classInit(CallbackInfo cb) {
        for(EnumCreatureType type : $VALUES) {
            if(type==EnumCreatureType.MONSTER)
                SpawnManager.addExistingCreatureType("hostile",type);
            else if(type==EnumCreatureType.CREATURE)
                SpawnManager.addExistingCreatureType("passive",type);
            else if(type==EnumCreatureType.AMBIENT)
                SpawnManager.addExistingCreatureType("ambient",type);
            else if(type==EnumCreatureType.WATER_CREATURE)
                SpawnManager.addExistingCreatureType("aquatic",type);
            else SpawnManager.addExistingCreatureType(type.name().toLowerCase(),type);
        }
        for(SpawnGroup.Builder builder : SpawnGroup.getBuilders()) {
            String name = builder.getName();
            if(SpawnManager.isExistingCreatureType(name)) {
                EnumCreatureType type = SpawnManager.getExistingCreatureType(name);
                Optional<Integer> potentialCount = builder.getCount();
                builder.setCount(potentialCount.orElse(type.getMaxNumberOfCreature()));
                Optional<Boolean> potentialPeaceful = builder.getPeaceful();
                builder.setPeaceful(potentialPeaceful.orElse(type.getPeacefulCreature()));
                Optional<Boolean> potentialAnimal = builder.getAnimal();
                builder.setAnimal(potentialAnimal.orElse(type.getAnimal()));
                Optional<Boolean> potentialAquatic = builder.getPeaceful();
                builder.setAquatic(potentialAquatic.orElse(false));
                builder.setCreatureType(type);
                SpawnManager.setTypeBuilder(type,builder);
            } else {
                int count = builder.getCount().orElse(20);
                builder.setCount(count);
                boolean isPeaceful = builder.getPeaceful().orElse(false);
                builder.setPeaceful(isPeaceful);
                boolean isAnimal = builder.getAnimal().orElse(false);
                builder.setAnimal(isAnimal);
                boolean isAquatic = builder.getAquatic().orElse(false);
                builder.setAquatic(isAquatic);
                EnumCreatureType newType = specifiedspawning$construct(name.toUpperCase(),$VALUES.length,IAnimals.class,
                        count,isAquatic ? Material.WATER : Material.AIR,isPeaceful,isAnimal);
                builder.setCreatureType(newType);
                $VALUES = Arrays.copyOf($VALUES,$VALUES.length+1);
                $VALUES[$VALUES.length-1] = newType;
                SpawnManager.setTypeBuilder(newType,builder);
            }
        }
        for(EnumCreatureType type : $VALUES) {
            if(!SpawnManager.hasBuilder(type)) {
                String name = type.name().toLowerCase();
                if(type==EnumCreatureType.MONSTER)
                    name = "hostile";
                else if(type==EnumCreatureType.CREATURE)
                    name = "passive";
                else if(type==EnumCreatureType.AMBIENT)
                    name = "ambient";
                else if(type==EnumCreatureType.WATER_CREATURE)
                    name = "aquatic";
                SpawnGroup.Builder builder = new SpawnGroup.Builder(name);
                builder.setCount(type.getMaxNumberOfCreature());
                builder.setPeaceful(type.getPeacefulCreature());
                builder.setAnimal(type.getAnimal());
                builder.setAquatic(false);
                builder.setCreatureType(type);
                SpawnManager.setTypeBuilder(type,builder);
            }
        }
    }

    /**
     * @author The_Computerizer
     * @reason Allow for dynamic mob caps so the game does not have to be restarted for spawn groups to be reset.
     */
    @Overwrite
    public int getMaxNumberOfCreature() {
        return SpawnManager.getDynamicMaxNumberOfCreature((EnumCreatureType)(Object)this,this.maxNumberOfCreature);
    }
}

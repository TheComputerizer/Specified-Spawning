package mods.thecomputerizer.specifiedspawning.util;

import mods.thecomputerizer.specifiedspawning.core.Constants;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.passive.IAnimals;
import net.minecraftforge.common.util.EnumHelper;
import org.apache.commons.lang3.EnumUtils;

import java.util.*;

/**
 * Various utility methods for adding, modifying, and checking enums
 */
public class EnumUtil {

    private static final Map<Class<? extends Enum<?>>,Class<?>[]> ENUM_CONSTRUCTOR_TYPES = ThreadSafety.newMap(HashMap::new);
    private static final Map<String,EnumCreatureType> ENUM_REFERENCE_MAP = ThreadSafety.newMap(HashMap::new);

    public static void buildDefaultConstructorTypes() {
    }

    public static <E extends Enum<E>> void addCachedConstructor(Class<E> enumClass, Class<?> ... constructorParameterTypes) {
        EnumHelper.testEnum(enumClass,constructorParameterTypes);
        ENUM_CONSTRUCTOR_TYPES.put(enumClass,constructorParameterTypes);
    }

    /**
     * Checks whether the input string is a valid reference to an EnumCreatureType.
     * Adds missing vanilla references if necessary
     */
    public static boolean isValidCreatureReference(String name) {
        if(Objects.isNull(ENUM_REFERENCE_MAP) || Objects.isNull(name) || name.isEmpty()) return false;
        if(ENUM_REFERENCE_MAP.containsKey(name)) return true;
        switch (name) {
            case "hostile" : {
                ENUM_REFERENCE_MAP.put(name,EnumCreatureType.MONSTER);
                return true;
            }
            case "passive" : {
                ENUM_REFERENCE_MAP.put(name,EnumCreatureType.CREATURE);
                return true;
            }
            case "aquatic" : {
                ENUM_REFERENCE_MAP.put(name,EnumCreatureType.WATER_CREATURE);
                return true;
            }
            case "ambient" : {
                ENUM_REFERENCE_MAP.put(name,EnumCreatureType.AMBIENT);
                return true;
            }
            default: return false;
        }
    }

    /**
     * Gets an EnumCreatureType or makes a new one if the name is unknown.
     * Returns null in the case of a null or empty name or if it fails to make a new enum.
     */
    public static EnumCreatureType getOrCreateEnumType(String name) {
        return isValidCreatureReference(name) ? ENUM_REFERENCE_MAP.get(name) : makeDefaultEnumCreatureType(name);
    }

    private static EnumCreatureType makeDefaultEnumCreatureType(String name) {
        return makeNewEnumCreatureType(name, IAnimals.class,20,Material.AIR,false,false);
    }

    /**
     * Makes a new EnumCreatureType with the given name. Returns null in the case of a null or empty name.
     * If an enum constant with the given name already exists that will be returned.
     */
    public static EnumCreatureType makeNewEnumCreatureType(String name,Class<? extends IAnimals> animalType,int count,
                                                           Material spawnMaterial,boolean isPeaceful,boolean isAnimal) {
        if(Objects.isNull(name) || name.isEmpty()) return null;
        name = name.toUpperCase();
        EnumCreatureType ret;
        if(hasEnumField(EnumCreatureType.class,name))
            ret =  EnumUtils.getEnum(EnumCreatureType.class,name);
        else ret = EnumHelper.addCreatureType(name,animalType,count,spawnMaterial,isPeaceful,isAnimal);
        if(Objects.nonNull(ret)) ENUM_REFERENCE_MAP.put(name,ret);
        return ret;
    }

    /**
     * Assumes addCachedConstructor has already been called for the input enum class. Returns null if the class has
     * not been cached, the input parameters array is a different length than the parameter classes array from the
     * cache, or if the parameter from the parameters array is not extensible from the parameter classes array at the
     * same index.
     */
    public static <E extends Enum<E>> E addEnumFromCache(Class<E> enumClass, String enumConstant, Object ... parameters) {
        Class<?>[] parameterTypes = ENUM_CONSTRUCTOR_TYPES.get(enumClass);
        if(Objects.isNull(parameterTypes)) {
            Constants.LOGGER.error("Enum class {} is unknown! Constant {} will not be added. Make sure to " +
                            "call EnumUtil#addCachedConstructor before trying to add a new constant with this class!",
                    enumClass.getName(),enumConstant);
            return null;
        }
        if(parameterTypes.length!=parameters.length) {
            Constants.LOGGER.error("Failed to add constant {} for enum class {}! The number of input " +
                            "parameters [{}] do not match the stored amount [{}]!",enumConstant,enumClass.getName(),
                    parameters.length,parameterTypes.length);
            return null;
        }
        for(int i=0;i<parameters.length;i++) {
            Class<?> type = parameterTypes[i];
            Object parameter = parameters[i];
            Class<?> input = Objects.nonNull(parameter) ? parameter.getClass() : null;
            if(Objects.nonNull(input) && !type.isAssignableFrom(input)) {
                Constants.LOGGER.error("Failed to add constant {} for enum class {}! Input parameter at index" +
                                " {} of type {} is not extensible from the expected type {}!",enumConstant,
                        enumClass.getName(),i,input.getName(),type.getName());
                return null;
            }
        }
        return EnumHelper.addEnum(enumClass,enumConstant,parameterTypes,parameters);
    }

    /**
     * Returns false if something fails or errors.
     */
    public static <E extends Enum<E>> boolean hasEnumField(Class<E> enumClass, String declaredName) {
        if(Objects.isNull(enumClass)) {
            Constants.LOGGER.error("Unable to check enum constant '{}' for null class",declaredName);
            return false;
        }
        if(Objects.isNull(declaredName) || declaredName.isEmpty()) {
            Constants.LOGGER.error("Unable to check null or empty enum constant name for class '{}'",enumClass.getName());
            return false;
        }
        return EnumUtils.isValidEnum(enumClass,declaredName);
    }
}

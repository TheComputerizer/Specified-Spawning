package mods.thecomputerizer.specifiedspawning.util;


import java.util.*;

/**
 * Various utility methods for parsing rules
 */
public class ParsingUtils {

    @SuppressWarnings("unchecked")
    public static List<String> getStringList(Object var) {
        if(Objects.isNull(var)) return new ArrayList<>();
        if(var instanceof String) return Collections.singletonList((String)var);
        if(var instanceof List<?>) {
            List<?> list = (List<?>)var;
            if(list.isEmpty()) return new ArrayList<>();
            for(Object element : (List<?>)var) {
                if(!(element instanceof String)) return new ArrayList<>();
                break;
            }
            return (List<String>)var;
        }
        return new ArrayList<>();
    }


    public static List<Integer> getIntList(Object var) {
        if(Objects.isNull(var)) return new ArrayList<>();
        if(var instanceof Number) return Collections.singletonList(((Number)var).intValue());
        if(var instanceof String) {
            try {
                return Collections.singletonList(Integer.parseInt((String)var));
            } catch (NumberFormatException ex) {
                return new ArrayList<>();
            }
        }
        if(var instanceof List<?>) {
            List<?> list = (List<?>)var;
            if(list.isEmpty()) return new ArrayList<>();
            List<Integer> ret = new ArrayList<>();
            for(Object element : list) {
                if(element instanceof Number) ret.add(((Number)element).intValue());
                else try {
                    ret.add(Integer.parseInt(element.toString()));
                } catch (NumberFormatException ex) {
                    return new ArrayList<>();
                }
            }
            return ret;
        }
        return new ArrayList<>();
    }
}
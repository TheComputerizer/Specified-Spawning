package mods.thecomputerizer.specifiedspawning.rules;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ParseUtil {

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

    @SuppressWarnings("unchecked")
    public static List<Integer> getIntList(Object var) {
        if(Objects.isNull(var)) return new ArrayList<>();
        if(var instanceof Integer) return Collections.singletonList((Integer)var);
        if(var instanceof List<?>) {
            List<?> list = (List<?>)var;
            if(list.isEmpty()) return new ArrayList<>();
            for(Object element : (List<?>)var) {
                if(!(element instanceof Integer)) return new ArrayList<>();
                break;
            }
            return (List<Integer>)var;
        }
        return new ArrayList<>();
    }
}

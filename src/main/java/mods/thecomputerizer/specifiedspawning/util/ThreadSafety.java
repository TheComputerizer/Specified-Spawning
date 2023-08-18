package mods.thecomputerizer.specifiedspawning.util;

import java.util.*;
import java.util.function.Supplier;

public class ThreadSafety {

    public static <K,V> Map<K,V> newMap(Supplier<? extends Map<K,V>> mapSupplier) {
        Map<K,V> map = mapSupplier.get();
        return NavigableMap.class.isAssignableFrom(map.getClass()) ?
                Collections.synchronizedNavigableMap((NavigableMap<K,V>)map) :
                (SortedMap.class.isAssignableFrom(map.getClass()) ?
                        Collections.synchronizedSortedMap((SortedMap<K,V>)map) : Collections.synchronizedMap(map));
    }

    public static <E> Collection<E> newCollection(Supplier<? extends Collection<E>> collectionSupplier) {
        return Collections.synchronizedCollection(collectionSupplier.get());
    }

    public static <E,S extends List<E>> List<E> newList(Supplier<S> listSupplier) {
        return Collections.synchronizedList(listSupplier.get());
    }

    public static <E,S extends Set<E>> Set<E> newSet(Supplier<S> setSupplier) {
        Set<E> set = setSupplier.get();
        return NavigableSet.class.isAssignableFrom(set.getClass()) ?
                Collections.synchronizedNavigableSet((NavigableSet<E>)set) :
                (SortedSet.class.isAssignableFrom(set.getClass()) ?
                        Collections.synchronizedSortedSet((SortedSet<E>)set) : Collections.synchronizedSet(set));
    }
}

package mods.thecomputerizer.specifiedspawning.rules.selectors;

public interface ISelector<T> {

    boolean isValid(T value, String ruleDescriptor);
    boolean isBasic();
}

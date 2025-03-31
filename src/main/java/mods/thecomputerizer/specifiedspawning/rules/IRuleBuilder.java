package mods.thecomputerizer.specifiedspawning.rules;

public interface IRuleBuilder {

    IRule build();
    void parseSelectors();
}
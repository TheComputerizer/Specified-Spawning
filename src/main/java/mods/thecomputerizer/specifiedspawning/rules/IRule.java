package mods.thecomputerizer.specifiedspawning.rules;


public interface IRule {

    void setOrder(int index);
    int getOrder();
    void setup();
}
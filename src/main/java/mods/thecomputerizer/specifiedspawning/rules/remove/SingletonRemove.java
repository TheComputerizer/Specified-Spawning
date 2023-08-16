package mods.thecomputerizer.specifiedspawning.rules.remove;

import mods.thecomputerizer.specifiedspawning.rules.SingletonRule;

public class SingletonRemove extends SingletonRule {



    @Override
    protected boolean doesInitiallyPass() {
        return false;
    }
}

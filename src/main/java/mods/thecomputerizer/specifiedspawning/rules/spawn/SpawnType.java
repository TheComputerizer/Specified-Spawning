package mods.thecomputerizer.specifiedspawning.rules.spawn;

import mods.thecomputerizer.specifiedspawning.rules.IRule;
import mods.thecomputerizer.specifiedspawning.rules.IRuleBuilder;
import mods.thecomputerizer.specifiedspawning.rules.IRuleType;
import mods.thecomputerizer.theimpossiblelibrary.common.toml.Table;

public class SpawnType implements IRuleType {
    @Override
    public boolean isValidSubRule(String subRule) {
        return false;
    }

    @Override
    public boolean isValidParameter(String parameter) {
        return false;
    }

    @Override
    public IRuleBuilder parseRuleTable(Table ruleTable) {
        return null;
    }
}

package mods.thecomputerizer.specifiedspawning.rules.modify;

import mods.thecomputerizer.specifiedspawning.rules.IRule;
import mods.thecomputerizer.specifiedspawning.rules.IRuleBuilder;
import mods.thecomputerizer.specifiedspawning.rules.IRuleType;
import mods.thecomputerizer.theimpossiblelibrary.common.toml.Table;

public class ModifyType implements IRuleType {

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

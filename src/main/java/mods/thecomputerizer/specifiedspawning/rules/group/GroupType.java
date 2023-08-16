package mods.thecomputerizer.specifiedspawning.rules.group;

import mods.thecomputerizer.specifiedspawning.rules.IRule;
import mods.thecomputerizer.specifiedspawning.rules.IRuleBuilder;
import mods.thecomputerizer.specifiedspawning.rules.IRuleType;
import mods.thecomputerizer.theimpossiblelibrary.common.toml.Table;

import java.util.Arrays;
import java.util.List;

public class GroupType implements IRuleType {

    private final List<String> validParameters;

    public GroupType() {
        this.validParameters = Arrays.asList("name","count","weight");
    }

    @Override
    public boolean isValidSubRule(String subRule) {
        return false;
    }

    @Override
    public boolean isValidParameter(String parameter) {
        return this.validParameters.contains(parameter);
    }

    @Override
    public IRuleBuilder parseRuleTable(Table ruleTable) {
        return null;
    }
}

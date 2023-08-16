package mods.thecomputerizer.specifiedspawning.rules;

import mods.thecomputerizer.theimpossiblelibrary.common.toml.Table;

public interface IRuleType {

    boolean isSubRule(String subRule);
    IRuleBuilder parseRuleTable(Table ruleTable);
}

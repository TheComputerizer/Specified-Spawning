package mods.thecomputerizer.specifiedspawning.rules;

import mods.thecomputerizer.theimpossiblelibrary.common.toml.Table;

public interface IRuleType {

    IRuleBuilder parseRuleTable(Table ruleTable);
}

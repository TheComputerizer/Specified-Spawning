package mods.thecomputerizer.specifiedspawning.rules.remove;

import mods.thecomputerizer.specifiedspawning.rules.IRuleBuilder;
import mods.thecomputerizer.specifiedspawning.rules.IRuleType;
import mods.thecomputerizer.theimpossiblelibrary.common.toml.Table;

public class RemoveType implements IRuleType {

    @Override
    public IRuleBuilder parseRuleTable(Table ruleTable) {
        return new RemoveRuleBuilder(ruleTable);
    }
}

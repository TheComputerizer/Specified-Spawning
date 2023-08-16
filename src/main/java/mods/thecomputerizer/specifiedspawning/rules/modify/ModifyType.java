package mods.thecomputerizer.specifiedspawning.rules.modify;

import mods.thecomputerizer.specifiedspawning.rules.IRuleBuilder;
import mods.thecomputerizer.specifiedspawning.rules.IRuleType;
import mods.thecomputerizer.theimpossiblelibrary.common.toml.Table;

public class ModifyType implements IRuleType {

    @Override
    public IRuleBuilder parseRuleTable(Table ruleTable) {
        return new ModifyRuleBuilder(ruleTable);
    }
}

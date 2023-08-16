package mods.thecomputerizer.specifiedspawning.rules.spawn;

import mods.thecomputerizer.specifiedspawning.rules.IRuleBuilder;
import mods.thecomputerizer.specifiedspawning.rules.IRuleType;
import mods.thecomputerizer.theimpossiblelibrary.common.toml.Table;

public class SpawnType implements IRuleType {

    @Override
    public IRuleBuilder parseRuleTable(Table ruleTable) {
        return new SpawnRuleBuilder(ruleTable);
    }
}

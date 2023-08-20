package mods.thecomputerizer.specifiedspawning.rules;

import mods.thecomputerizer.specifiedspawning.rules.group.SpawnGroup;
import mods.thecomputerizer.specifiedspawning.rules.modify.ModifyRuleBuilder;
import mods.thecomputerizer.specifiedspawning.rules.remove.RemoveRuleBuilder;
import mods.thecomputerizer.specifiedspawning.rules.spawn.SpawnRuleBuilder;
import mods.thecomputerizer.theimpossiblelibrary.common.toml.Table;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public enum RuleType {

    SPAWN(true,"spawn",SpawnRuleBuilder::new),
    MODIFY(true,"modify",ModifyRuleBuilder::new),
    REMOVE(true,"remove",RemoveRuleBuilder::new),
    GROUP(true,"group",SpawnGroup.Builder::new);

    private static final Map<String, RuleType> BASE_RULES = new HashMap<>();
    private static final Map<String, RuleType> BY_NAME = new HashMap<>();

    public static boolean isBaseRule(String rule) {
        return BASE_RULES.containsKey(rule);
    }

    public static RuleType getRuleType(String rule) {
        return BY_NAME.get(rule);
    }

    private final boolean isBase;
    private final String ruleName;
    private final Function<Table,IRuleBuilder> typeBuilder;

    RuleType(boolean isBase, String ruleName, Function<Table,IRuleBuilder> typeBuilder) {
        this.isBase = isBase;
        this.ruleName = ruleName;
        this.typeBuilder = typeBuilder;
    }

    public String getRuleName() {
        return this.ruleName;
    }

    public IRuleBuilder parseRuleBuilder(Table table) {
        return this.typeBuilder.apply(table);
    }

    static {
        for(RuleType ruleType : values()) {
            if(ruleType.isBase) BASE_RULES.put(ruleType.ruleName, ruleType);
            BY_NAME.put(ruleType.ruleName, ruleType);
        }
    }
}

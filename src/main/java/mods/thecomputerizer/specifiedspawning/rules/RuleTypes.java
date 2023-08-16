package mods.thecomputerizer.specifiedspawning.rules;

import mods.thecomputerizer.specifiedspawning.rules.group.GroupType;
import mods.thecomputerizer.specifiedspawning.rules.modify.ModifyType;
import mods.thecomputerizer.specifiedspawning.rules.remove.RemoveType;
import mods.thecomputerizer.specifiedspawning.rules.spawn.SpawnType;
import mods.thecomputerizer.theimpossiblelibrary.common.toml.Table;

import java.util.HashMap;
import java.util.Map;

public enum RuleTypes {

    SPAWN(true,"spawn",new SpawnType()),
    MODIFY(true,"modify",new ModifyType()),
    REMOVE(true,"remove",new RemoveType()),
    GROUP(true,"group",new GroupType());

    private static final Map<String, RuleTypes> BASE_RULES = new HashMap<>();
    private static final Map<String, RuleTypes> BY_NAME = new HashMap<>();

    public static boolean isBaseRule(String rule) {
        return BASE_RULES.containsKey(rule);
    }

    public static RuleTypes getRuleType(String rule) {
        return BY_NAME.get(rule);
    }

    private final boolean isBase;
    private final String name;
    private final IRuleType ruleType;

    RuleTypes(boolean isBase, String name, IRuleType ruleType) {
        this.isBase = isBase;
        this.name = name;
        this.ruleType = ruleType;
    }

    public IRuleBuilder parseRuleBuilder(Table table) {
        return this.ruleType.parseRuleTable(table);
    }

    static {
        for(RuleTypes ruleTypes : values()) {
            if(ruleTypes.isBase) BASE_RULES.put(ruleTypes.name, ruleTypes);
            BY_NAME.put(ruleTypes.name, ruleTypes);
        }
    }
}

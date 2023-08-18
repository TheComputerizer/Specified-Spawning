package mods.thecomputerizer.specifiedspawning.rules;

import mods.thecomputerizer.specifiedspawning.Constants;
import mods.thecomputerizer.specifiedspawning.util.ThreadSafety;
import mods.thecomputerizer.theimpossiblelibrary.common.toml.Table;
import mods.thecomputerizer.theimpossiblelibrary.util.file.TomlUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class RuleManager {
    public static final Map<RuleType,HashSet<IRuleBuilder>> RULE_BUILDERS = ThreadSafety.newMap(HashMap::new);
    public static final Set<DynamicRule> DYNAMIC_RULES = ThreadSafety.newSet(HashSet::new);

    public static void parseConfig() {
        try {
            for (Table table : TomlUtil.readFully(Constants.CONFIG).getTables().values()) {
                String ruleName = table.getName();
                if (RuleType.isBaseRule(ruleName)) {
                    RuleType type = RuleType.getRuleType(ruleName);
                    RULE_BUILDERS.putIfAbsent(type, new HashSet<>());
                    RULE_BUILDERS.get(type).add(type.parseRuleBuilder(table));
                }
            }
        } catch (IOException ex) {
            Constants.LOGGER.error("Failed to parse config file at {}!",Constants.CONFIG.getPath(),ex);
        }
    }

    public static void buildRules() {
        for(HashSet<IRuleBuilder> builderSet : RULE_BUILDERS.values()) {
            for (IRuleBuilder builder : builderSet) {
                IRule rule = builder.build();
                rule.setup();
                if(rule instanceof SingletonRule) ((SingletonRule)rule).apply();
                else if(rule instanceof DynamicRule) {
                    ((DynamicRule)rule).apply();
                    DYNAMIC_RULES.add((DynamicRule)rule);
                }
            }
        }
    }

    public static void clear() {
        DYNAMIC_RULES.clear();
        RULE_BUILDERS.clear();
    }
}

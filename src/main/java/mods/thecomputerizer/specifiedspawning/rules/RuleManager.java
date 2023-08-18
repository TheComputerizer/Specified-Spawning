package mods.thecomputerizer.specifiedspawning.rules;

import mods.thecomputerizer.specifiedspawning.Constants;
import mods.thecomputerizer.specifiedspawning.util.ThreadSafety;
import mods.thecomputerizer.theimpossiblelibrary.common.toml.Table;
import mods.thecomputerizer.theimpossiblelibrary.util.file.TomlUtil;
import net.minecraft.world.biome.Biome;

import java.io.IOException;
import java.util.*;

public class RuleManager {
    public static final Map<RuleType,HashSet<IRuleBuilder>> RULE_BUILDERS = ThreadSafety.newMap(HashMap::new);
    public static final Map<Biome.SpawnListEntry,Set<DynamicRule>> DYNAMIC_RULE_MAP = ThreadSafety.newMap(HashMap::new);

    public static void parseConfig() {
        try {
            for (Table table : TomlUtil.readFully(Constants.CONFIG).getTables().values()) {
                String ruleName = table.getName();
                if (RuleType.isBaseRule(ruleName)) {
                    RuleType type = RuleType.getRuleType(ruleName);
                    Constants.LOGGER.error("ADDING RULE TYPE {}",type.name());
                    RULE_BUILDERS.putIfAbsent(type, new HashSet<>());
                    RULE_BUILDERS.get(type).add(type.parseRuleBuilder(table));
                }
            }
        } catch (IOException ex) {
            Constants.LOGGER.error("Failed to parse config file at {}!",Constants.CONFIG.getPath(),ex);
        }
    }

    public static void buildRules() {
        Constants.LOGGER.error("BUILDING RULES");
        for(HashSet<IRuleBuilder> builderSet : RULE_BUILDERS.values()) {
            for (IRuleBuilder builder : builderSet) {
                Constants.LOGGER.error("BUILDING RULE {}",builder.getClass().getName());
                IRule rule = builder.build();
                Constants.LOGGER.error("BUILT RULE {}",rule.getClass().getName());
                rule.setup();
                if(rule instanceof SingletonRule) ((SingletonRule)rule).apply();
                else if(rule instanceof DynamicRule) {
                    DynamicRule dynamic = ((DynamicRule)rule);
                    for(Biome.SpawnListEntry entry : dynamic.apply()) {
                        DYNAMIC_RULE_MAP.putIfAbsent(entry,new HashSet<>());
                        DYNAMIC_RULE_MAP.get(entry).add(dynamic);
                    }
                }
            }
        }
    }

    public static boolean hasCachedRules(Biome.SpawnListEntry entry) {
        return DYNAMIC_RULE_MAP.containsKey(entry);
    }

    public static Set<DynamicRule> getCachedRules(Biome.SpawnListEntry entry) {
        return DYNAMIC_RULE_MAP.get(entry);
    }

    public static void clear() {
        DYNAMIC_RULE_MAP.clear();
        RULE_BUILDERS.clear();
    }
}

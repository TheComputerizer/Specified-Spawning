package mods.thecomputerizer.specifiedspawning.rules;

import mods.thecomputerizer.specifiedspawning.ConfigManager;
import mods.thecomputerizer.specifiedspawning.core.Constants;
import mods.thecomputerizer.specifiedspawning.rules.group.SpawnGroup;
import mods.thecomputerizer.specifiedspawning.rules.spawn.DynamicSpawn;
import mods.thecomputerizer.specifiedspawning.rules.spawn.SpawnRuleBuilder;
import mods.thecomputerizer.specifiedspawning.util.ThreadSafety;
import mods.thecomputerizer.theimpossiblelibrary.common.toml.Table;
import net.minecraft.world.biome.Biome;
import org.apache.logging.log4j.Level;

import java.util.*;

public class RuleManager {
    public static final Map<RuleType,HashSet<IRuleBuilder>> RULE_BUILDERS = ThreadSafety.newMap(HashMap::new);
    public static final Map<Biome.SpawnListEntry,List<DynamicRule>> DYNAMIC_RULE_MAP = ThreadSafety.newMap(HashMap::new);

    public static void parseRuleTables() {
        for(Map.Entry<Integer,List<Table>> tablesEntry : ConfigManager.INSTANCE.getTableMap().entrySet()) {
            for(Table table : tablesEntry.getValue()) {
                String ruleName = table.getName();
                if (RuleType.isBaseRule(ruleName)) {
                    RuleType type = RuleType.getRuleType(ruleName);
                    if (!RULE_BUILDERS.containsKey(type)) {
                        Constants.logVerbose(Level.INFO, "Adding new rule type {}", type.name());
                        RULE_BUILDERS.putIfAbsent(type, new HashSet<>());
                    }
                    RULE_BUILDERS.get(type).add(type.parseRuleBuilder(table,tablesEntry.getKey()));
                }
            }
        }
    }

    public static void addDefaultGroups(SpawnGroup.Builder ... builders) {
        RULE_BUILDERS.putIfAbsent(RuleType.GROUP,new HashSet<>());
        RULE_BUILDERS.get(RuleType.GROUP).addAll(Arrays.asList(builders));
    }

    public static void buildRules() {
        Constants.logVerbose(Level.INFO,"Building rules from {} known types",RULE_BUILDERS.size());
        for(Map.Entry<RuleType,HashSet<IRuleBuilder>> typeEntry : RULE_BUILDERS.entrySet()) {
            String ruleName = typeEntry.getKey().getRuleName();
            HashSet<IRuleBuilder> builders = typeEntry.getValue();
            Constants.logVerbose(Level.INFO,"Building {} rules from rule type '{}'",builders.size(),ruleName);
            for(IRuleBuilder builder : builders) {
                IRule rule = builder.build();
                if(rule instanceof DynamicSpawn) ((SpawnRuleBuilder)builder).enableExcessiveLogging((DynamicSpawn)rule);
                rule.setup();
                if(rule instanceof SingletonRule) ((SingletonRule)rule).apply();
                else if(rule instanceof DynamicRule) {
                    DynamicRule dynamic = ((DynamicRule)rule);
                    Set<Biome.SpawnListEntry> entries = dynamic.apply();
                    Constants.logVerbose(Level.DEBUG,"Assigning {} rule to {} spawn entries",dynamic.ruleDescriptor,entries.size());
                    for(Biome.SpawnListEntry entry : entries) {
                        DYNAMIC_RULE_MAP.putIfAbsent(entry,new ArrayList<>());
                        DYNAMIC_RULE_MAP.get(entry).add(dynamic);
                    }
                }
            }
        }
        for(List<DynamicRule> cachedRules : DYNAMIC_RULE_MAP.values())
            cachedRules.sort(Comparator.comparingInt(IRule::getOrder));
    }

    public static boolean hasCachedRules(Biome.SpawnListEntry entry) {
        return DYNAMIC_RULE_MAP.containsKey(entry);
    }

    public static List<DynamicRule> getCachedRules(Biome.SpawnListEntry entry) {
        return DYNAMIC_RULE_MAP.get(entry);
    }

    public static void clear() {
        DYNAMIC_RULE_MAP.clear();
        RULE_BUILDERS.clear();
    }
}

package mods.thecomputerizer.specifiedspawning.rules;

import mods.thecomputerizer.specifiedspawning.core.ConfigManager;
import mods.thecomputerizer.specifiedspawning.core.Constants;
import mods.thecomputerizer.specifiedspawning.mixin.access.ISpawnGroupObject;
import mods.thecomputerizer.specifiedspawning.rules.spawn.DynamicSpawn;
import mods.thecomputerizer.specifiedspawning.rules.spawn.SpawnRuleBuilder;
import mods.thecomputerizer.specifiedspawning.util.ThreadSafety;
import mods.thecomputerizer.theimpossiblelibrary.api.toml.Toml;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.world.biome.Biome.SpawnListEntry;

import java.util.*;
import java.util.Map.Entry;

import static mods.thecomputerizer.specifiedspawning.core.Constants.LOGGER;
import static net.minecraft.init.Biomes.HELL;
import static org.apache.logging.log4j.Level.DEBUG;
import static org.apache.logging.log4j.Level.INFO;

public class RuleManager {
    public static final Map<RuleType,Set<IRuleBuilder>> RULE_BUILDERS = ThreadSafety.newMap(HashMap::new);

    public static void parseRuleTables() {
        for(Entry<Integer,List<Toml>> tablesEntry : ConfigManager.getTableEntries()) {
            for(Toml table : tablesEntry.getValue()) {
                String ruleName = table.getName();
                if(RuleType.isBaseRule(ruleName)) {
                    RuleType type = RuleType.getRuleType(ruleName);
                    if(!RULE_BUILDERS.containsKey(type)) {
                        Constants.logVerbose(INFO,"Adding new rule type {}",type.name());
                        RULE_BUILDERS.putIfAbsent(type,new HashSet<>());
                    }
                    RULE_BUILDERS.get(type).add(type.parseRuleBuilder(table,tablesEntry.getKey()));
                }
            }
        }
    }

    public static void parseRuleSelectors() {
        for(Set<IRuleBuilder> builderSet : RULE_BUILDERS.values())
            for(IRuleBuilder builder : builderSet) builder.parseSelectors();
    }

    public static void buildRules() {
        Constants.logVerbose(INFO,"Building rules from {} known types",RULE_BUILDERS.size());
        Set<SpawnListEntry> toSort = new HashSet<>();
        for(Entry<RuleType,Set<IRuleBuilder>> typeEntry : RULE_BUILDERS.entrySet()) {
            String ruleName = typeEntry.getKey().getRuleName();
            Set<IRuleBuilder> builders = typeEntry.getValue();
            Constants.logVerbose(INFO,"Building {} rules from rule type '{}'",builders.size(),ruleName);
            for(IRuleBuilder builder : builders) {
                IRule rule = builder.build();
                if(rule instanceof DynamicSpawn) ((SpawnRuleBuilder)builder).enableExcessiveLogging((DynamicSpawn)rule);
                rule.setup();
                if(rule instanceof SingletonRule) ((SingletonRule)rule).apply();
                else if(rule instanceof DynamicRule) {
                    DynamicRule dynamic = ((DynamicRule)rule);
                    Set<SpawnListEntry> entries = dynamic.apply();
                    Constants.logVerbose(DEBUG,"Assigning {} rule to {} spawn entries",dynamic.toString(),entries.size());
                    for(SpawnListEntry entry : entries) {
                        ((ISpawnGroupObject)entry).specifiedspawning$addDynamicRule(dynamic);
                        toSort.add(entry);
                    }
                }
            }
        }
        for(SpawnListEntry entry : toSort) ((ISpawnGroupObject)entry).specifiedspawning$sortRules();
        testSpecificBiome();
        testEnumIterator();
    }

    public static void testSpecificBiome() {
        if(ConfigManager.isMoreLogging()) {
            LOGGER.debug("Testing spawn entries for hell biome");
            for(EnumCreatureType creature : EnumCreatureType.values()) {
                LOGGER.debug("Testing spawn entries for creature type {}",creature);
                for(SpawnListEntry entry : HELL.getSpawnableList(creature)) {
                    List<DynamicRule> rules = ((ISpawnGroupObject)entry).specifiedspawning$getDynamicRules();
                    if(!rules.isEmpty())
                        LOGGER.debug("Entry for class {} has {} cached rules",entry.entityClass.getName(),
                                     rules.size());
                    else LOGGER.debug("Entry for class {} has 0 cached rules",entry.entityClass.getName());
                }
            }
        }
    }

    public static void testEnumIterator() {
        if(ConfigManager.isMoreLogging()) {
            LOGGER.debug("Testing enum iterator");
            for(EnumCreatureType type : EnumCreatureType.values()) LOGGER.debug("Enum Value - {}",type);
        }
    }

    public static void clear() {
        RULE_BUILDERS.clear();
    }
}
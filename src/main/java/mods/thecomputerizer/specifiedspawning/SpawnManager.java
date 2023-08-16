package mods.thecomputerizer.specifiedspawning;

import mods.thecomputerizer.specifiedspawning.rules.IRuleBuilder;
import mods.thecomputerizer.specifiedspawning.rules.RuleTypes;
import mods.thecomputerizer.specifiedspawning.rules.group.SpawnGroup;
import mods.thecomputerizer.theimpossiblelibrary.common.toml.Table;
import mods.thecomputerizer.theimpossiblelibrary.util.file.TomlUtil;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class SpawnManager {

    private static final Map<RuleTypes,HashSet<IRuleBuilder>> BASE_RULES = new HashMap<>();
    private static final Map<String, SpawnGroup> SPAWN_GROUPS = new HashMap<>();

    public static void buildDefaults() {
        SPAWN_GROUPS.put("hostile",new SpawnGroup.Builder().setCount(70).setWeight(100).build());
        SPAWN_GROUPS.put("passive",new SpawnGroup.Builder().setCount(10).setWeight(100).build());
        SPAWN_GROUPS.put("aquatic",new SpawnGroup.Builder().setCount(5).setWeight(100).build());
        SPAWN_GROUPS.put("cave",new SpawnGroup.Builder().setCount(15).setWeight(50).build());
    }

    public static void parseConfig(File file) throws IOException {
        for(Table table : TomlUtil.readFully(file).getTables().values()) {
            String ruleName = table.getName();
            if(RuleTypes.isBaseRule(ruleName)) {
                RuleTypes type = RuleTypes.getRuleType(ruleName);
                BASE_RULES.putIfAbsent(type,new HashSet<>());
                BASE_RULES.get(type).add(type.parseRuleBuilder(table));
            }
        }
    }
}

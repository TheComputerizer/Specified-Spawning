package mods.thecomputerizer.specifiedspawning.rules.modify;

import mods.thecomputerizer.specifiedspawning.rules.IRule;
import mods.thecomputerizer.specifiedspawning.rules.IRuleBuilder;
import mods.thecomputerizer.specifiedspawning.rules.selectors.EntitySelector;
import mods.thecomputerizer.specifiedspawning.rules.selectors.ISelector;
import mods.thecomputerizer.specifiedspawning.rules.selectors.SelectorType;
import mods.thecomputerizer.theimpossiblelibrary.common.toml.Table;

import java.util.*;

public class ModifyRuleBuilder implements IRuleBuilder {

    private final List<EntitySelector> entitySelectors;
    private final Set<ISelector<?>> selectorSet;
    private final String groupName;
    private final String newGroupName;
    private final boolean modifySpawnCounts;
    private final List<Table> jockeyTables;

    public ModifyRuleBuilder(Table ruleTable) {
        this.groupName = ruleTable.getValOrDefault("group","hostile");
        this.newGroupName = ruleTable.getValOrDefault("new_group",this.groupName);
        this.modifySpawnCounts = ruleTable.getValOrDefault("modify_spawn_counts",false);
        this.entitySelectors = new ArrayList<>();
        for(Table entityTable : ruleTable.getTablesByName("entity"))
            this.entitySelectors.add((EntitySelector)SelectorType.ENTITY.makeSelector(entityTable));
        this.selectorSet = new HashSet<>();
        parseSelectors(ruleTable);
        this.jockeyTables = ruleTable.getTablesByName("jockey");
    }

    private void parseSelectors(Table ruleTable) {
        for(SelectorType type : SelectorType.values()) {
            if(type!=SelectorType.ENTITY) {
                if(type.isSubTable()) {
                    for(Table table : ruleTable.getTablesByName(type.getName())) {
                        ISelector<?> selector = type.makeSelector(table);
                        if(Objects.nonNull(selector)) this.selectorSet.add(selector);
                    }
                } else {
                    ISelector<?> selector = type.makeSelector(ruleTable);
                    if(Objects.nonNull(selector)) this.selectorSet.add(selector);
                }
            }
        }
    }

    @Override
    public IRule build() {
        return new DynamicModify(this.groupName,this.newGroupName,this.modifySpawnCounts,this.entitySelectors,
                this.selectorSet,this.jockeyTables);
    }
}

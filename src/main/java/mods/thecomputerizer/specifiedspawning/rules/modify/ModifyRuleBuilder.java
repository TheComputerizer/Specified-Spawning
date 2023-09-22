package mods.thecomputerizer.specifiedspawning.rules.modify;

import mods.thecomputerizer.specifiedspawning.rules.DynamicRule;
import mods.thecomputerizer.specifiedspawning.rules.IRule;
import mods.thecomputerizer.specifiedspawning.rules.IRuleBuilder;
import mods.thecomputerizer.specifiedspawning.rules.selectors.vanilla.BiomeSelector;
import mods.thecomputerizer.specifiedspawning.rules.selectors.vanilla.EntitySelector;
import mods.thecomputerizer.specifiedspawning.rules.selectors.ISelector;
import mods.thecomputerizer.specifiedspawning.rules.selectors.SelectorType;
import mods.thecomputerizer.theimpossiblelibrary.common.toml.Table;

import java.util.*;

public class ModifyRuleBuilder implements IRuleBuilder {

    private final Table ruleTable;
    private final List<EntitySelector> entitySelectors;
    private final Set<ISelector> selectorSet;
    private final String groupName;
    private final String newGroupName;
    private final boolean modifySpawnCounts;
    private final List<Table> jockeyTables;
    private final int adjustedIndex;
    private final boolean returnImmediately;

    public ModifyRuleBuilder(Table ruleTable, int order) {
        this.ruleTable = ruleTable;
        this.groupName = ruleTable.getValOrDefault("group","hostile");
        this.newGroupName = ruleTable.getValOrDefault("new_group",this.groupName);
        this.modifySpawnCounts = ruleTable.getValOrDefault("modify_spawn_counts",false);
        this.entitySelectors = new ArrayList<>();
        for(Table entityTable : ruleTable.getTablesByName("entity"))
            this.entitySelectors.add((EntitySelector)SelectorType.ENTITY.makeSelector(entityTable));
        this.selectorSet = new HashSet<>();
        this.jockeyTables = ruleTable.getTablesByName("jockey");
        this.adjustedIndex = ruleTable.getAbsoluteIndex()+order;
        this.returnImmediately = ruleTable.getValOrDefault("return_immediately",false);
    }

    @Override
    public void parseSelectors() {
        for(SelectorType type : SelectorType.values()) {
            if(type!=SelectorType.ENTITY) {
                if(type.isSubTable()) {
                    for(Table table : this.ruleTable.getTablesByName(type.getName())) {
                        ISelector selector = type.makeSelector(table);
                        if(Objects.nonNull(selector)) this.selectorSet.add(selector);
                    }
                } else {
                    ISelector selector = type.makeSelector(this.ruleTable);
                    if(Objects.nonNull(selector)) this.selectorSet.add(selector);
                }
            }
        }
    }

    @Override
    public IRule build() {
        IRule rule = isBasic() ? buildBasic() : new DynamicModify(this.groupName,this.newGroupName,this.modifySpawnCounts,
                this.entitySelectors, this.selectorSet,this.jockeyTables);
        rule.setOrder(this.adjustedIndex);
        if(rule instanceof DynamicRule) ((DynamicRule)rule).returnImmediately = this.returnImmediately;
        return rule;
    }

    private IRule buildBasic() {
        Set<BiomeSelector> biomeSelectors  = new HashSet<>();
        for(ISelector selector : this.selectorSet)
            if(selector instanceof BiomeSelector)
                biomeSelectors.add((BiomeSelector)selector);
        return new SingletonModify(this.groupName,this.newGroupName,this.modifySpawnCounts,this.entitySelectors,
                biomeSelectors,this.jockeyTables);
    }

    private boolean isBasic() {
        for(ISelector selector : this.selectorSet)
            if(selector.isNonBasic()) return false;
        return true;
    }
}

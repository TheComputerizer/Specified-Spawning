package mods.thecomputerizer.specifiedspawning.rules.modify;

import mods.thecomputerizer.specifiedspawning.rules.DynamicRule;
import mods.thecomputerizer.specifiedspawning.rules.IRule;
import mods.thecomputerizer.specifiedspawning.rules.IRuleBuilder;
import mods.thecomputerizer.specifiedspawning.rules.selectors.vanilla.BiomeSelector;
import mods.thecomputerizer.specifiedspawning.rules.selectors.vanilla.EntitySelector;
import mods.thecomputerizer.specifiedspawning.rules.selectors.ISelector;
import mods.thecomputerizer.specifiedspawning.rules.selectors.SelectorType;
import mods.thecomputerizer.theimpossiblelibrary.api.toml.Toml;

import java.util.*;

import static mods.thecomputerizer.specifiedspawning.rules.selectors.SelectorType.ENTITY;

public class ModifyRuleBuilder implements IRuleBuilder {

    private final Toml ruleTable;
    private final List<EntitySelector> entitySelectors;
    private final Set<ISelector> selectorSet;
    private final String groupName;
    private final String newGroupName;
    private final boolean modifySpawnCounts;
    private final List<Toml> jockeyTables;
    private final int adjustedIndex;
    private final boolean returnImmediately;

    public ModifyRuleBuilder(Toml rule, int order) {
        this.ruleTable = rule;
        this.groupName = rule.hasEntry("group") ? rule.getValueString("group") : "hostile";
        this.newGroupName = rule.hasEntry("new_group") ? rule.getValueString("new_group") : this.groupName;
        this.modifySpawnCounts = rule.getValueBool("modify_spawn_counts",false);
        this.entitySelectors = new ArrayList<>();
        if(rule.hasTable("entity"))
            for(Toml entityTable : rule.getTableArray("entity"))
                this.entitySelectors.add((EntitySelector)ENTITY.makeSelector(entityTable));
        this.selectorSet = new HashSet<>();
        this.jockeyTables = rule.hasTable("jockey") ?
                Arrays.asList(rule.getTableArray("jockey")) : Collections.emptyList();
        this.adjustedIndex = order;
        this.returnImmediately = rule.getValueBool("return_immediately",false);
    }

    @Override public void parseSelectors() {
        for(SelectorType type : SelectorType.values()) {
            if(type!=ENTITY) {
                if(type.isSubTable()) {
                    String typeName = type.getName();
                    if(this.ruleTable.hasTable(typeName)) {
                        for(Toml table : this.ruleTable.getTableArray(typeName)) {
                            ISelector selector = type.makeSelector(table);
                            if(Objects.nonNull(selector)) this.selectorSet.add(selector);
                        }
                    }
                } else {
                    ISelector selector = type.makeSelector(this.ruleTable);
                    if(Objects.nonNull(selector)) this.selectorSet.add(selector);
                }
            }
        }
    }

    @Override public IRule build() {
        IRule rule = isBasic() ? buildBasic() : new DynamicModify(this.groupName,this.newGroupName,
                this.modifySpawnCounts,this.entitySelectors,this.selectorSet,this.jockeyTables);
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
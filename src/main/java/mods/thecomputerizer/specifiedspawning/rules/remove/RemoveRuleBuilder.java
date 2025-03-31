package mods.thecomputerizer.specifiedspawning.rules.remove;

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

public class RemoveRuleBuilder implements IRuleBuilder {

    private final Toml ruleTable;
    private final List<EntitySelector> entitySelectors;
    private final Set<ISelector> selectorSet;
    private final String groupName;
    private final int adjustedIndex;
    private final boolean returnImmediately;

    public RemoveRuleBuilder(Toml rule, int order) {
        this.ruleTable = rule;
        this.groupName = rule.hasEntry("group") ? rule.getValueString("group") : "hostile";
        this.entitySelectors = new ArrayList<>();
        if(rule.hasTable("entity"))
            for(Toml entityTable : rule.getTableArray("entity"))
                this.entitySelectors.add((EntitySelector)ENTITY.makeSelector(entityTable));
        this.selectorSet = new HashSet<>();
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
        IRule rule = isBasic() ? buildBasic() : new DynamicRemove(this.groupName,this.entitySelectors,this.selectorSet);
        rule.setOrder(this.adjustedIndex);
        if(rule instanceof DynamicRule) ((DynamicRule)rule).returnImmediately = this.returnImmediately;
        return rule;
    }

    private IRule buildBasic() {
        Set<BiomeSelector> biomeSelectors  = new HashSet<>();
        for(ISelector selector : this.selectorSet)
            if(selector instanceof BiomeSelector) biomeSelectors.add((BiomeSelector)selector);
        return new SingletonRemove(this.groupName,this.entitySelectors,biomeSelectors);
    }

    private boolean isBasic() {
        for(ISelector selector : this.selectorSet)
            if(selector.isNonBasic()) return false;
        return true;
    }
}
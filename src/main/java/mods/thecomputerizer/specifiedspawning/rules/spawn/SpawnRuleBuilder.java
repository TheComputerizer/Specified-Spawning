package mods.thecomputerizer.specifiedspawning.rules.spawn;

import mods.thecomputerizer.specifiedspawning.rules.IRule;
import mods.thecomputerizer.specifiedspawning.rules.IRuleBuilder;
import mods.thecomputerizer.specifiedspawning.rules.selectors.BiomeSelector;
import mods.thecomputerizer.specifiedspawning.rules.selectors.EntitySelector;
import mods.thecomputerizer.specifiedspawning.rules.selectors.ISelector;
import mods.thecomputerizer.specifiedspawning.rules.selectors.SelectorType;
import mods.thecomputerizer.theimpossiblelibrary.common.toml.Table;

import java.util.*;

public class SpawnRuleBuilder implements IRuleBuilder {

    private final List<EntitySelector> entitySelectors;
    private final Set<ISelector<?>> selectorSet;
    private final String groupName;
    private final List<Table> jockeyTables;

    public SpawnRuleBuilder(Table ruleTable) {
        this.groupName = ruleTable.getValOrDefault("group","hostile");
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
        return isBasic() ? buildBasic() : new DynamicSpawn(this.groupName,this.entitySelectors,this.selectorSet,this.jockeyTables);
    }

    private IRule buildBasic() {
        Set<BiomeSelector> biomeSelectors  = new HashSet<>();
        for(ISelector<?> selector : this.selectorSet)
            if(selector instanceof BiomeSelector)
                biomeSelectors.add((BiomeSelector)selector);
        return new SingletonSpawn(this.groupName,this.entitySelectors,biomeSelectors,this.jockeyTables);
    }

    private boolean isBasic() {
        for(ISelector<?> selector : this.selectorSet)
            if(!selector.isBasic()) return false;
        return true;
    }
}

package mods.thecomputerizer.specifiedspawning.rules.remove;

import mods.thecomputerizer.specifiedspawning.Constants;
import mods.thecomputerizer.specifiedspawning.rules.IRule;
import mods.thecomputerizer.specifiedspawning.rules.IRuleBuilder;
import mods.thecomputerizer.specifiedspawning.rules.selectors.BiomeSelector;
import mods.thecomputerizer.specifiedspawning.rules.selectors.EntitySelector;
import mods.thecomputerizer.specifiedspawning.rules.selectors.ISelector;
import mods.thecomputerizer.specifiedspawning.rules.selectors.SelectorType;
import mods.thecomputerizer.theimpossiblelibrary.common.toml.Table;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class RemoveRuleBuilder implements IRuleBuilder {

    private final EntitySelector entitySelector;
    private final Set<ISelector<?>> selectorSet;

    public RemoveRuleBuilder(Table ruleTable) {
        this.entitySelector = (EntitySelector) SelectorType.ENTITY.makeSelector(ruleTable.getTableByName("entity"));
        this.selectorSet = new HashSet<>();
        parseSelectors(ruleTable);
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
        return isBasic() ? buildBasic() : new DynamicRemove(this.entitySelector,this.selectorSet);
    }

    private IRule buildBasic() {
        Set<BiomeSelector> biomeSelectors  = new HashSet<>();
        for(ISelector<?> selector : this.selectorSet)
            if(selector instanceof BiomeSelector)
                biomeSelectors.add((BiomeSelector)selector);
        return new SingletonRemove(this.entitySelector,biomeSelectors);
    }

    private boolean isBasic() {
        Constants.LOGGER.error("TESTING BASIC REMOVE FOR {} ENTRIES",this.selectorSet.size());
        for(ISelector<?> selector : this.selectorSet) {
            Constants.LOGGER.error("TESTING BASIC REMOVE {}",selector.getClass().getName());
            if(!selector.isBasic()) return false;
        }
        return true;
    }
}

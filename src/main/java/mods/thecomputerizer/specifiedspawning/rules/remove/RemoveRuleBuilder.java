package mods.thecomputerizer.specifiedspawning.rules.remove;

import mods.thecomputerizer.specifiedspawning.ConfigManager;
import mods.thecomputerizer.specifiedspawning.core.Constants;
import mods.thecomputerizer.specifiedspawning.rules.IRule;
import mods.thecomputerizer.specifiedspawning.rules.IRuleBuilder;
import mods.thecomputerizer.specifiedspawning.rules.selectors.vanilla.BiomeSelector;
import mods.thecomputerizer.specifiedspawning.rules.selectors.vanilla.EntitySelector;
import mods.thecomputerizer.specifiedspawning.rules.selectors.ISelector;
import mods.thecomputerizer.specifiedspawning.rules.selectors.SelectorType;
import mods.thecomputerizer.theimpossiblelibrary.common.toml.Table;
import org.apache.logging.log4j.Level;

import java.util.*;

public class RemoveRuleBuilder implements IRuleBuilder {

    private final List<EntitySelector> entitySelectors;
    private final Set<ISelector> selectorSet;
    private final String groupName;

    public RemoveRuleBuilder(Table ruleTable) {
        this.groupName = ruleTable.getValOrDefault("group","hostile");
        this.entitySelectors = new ArrayList<>();
        for(Table entityTable : ruleTable.getTablesByName("entity"))
            this.entitySelectors.add((EntitySelector)SelectorType.ENTITY.makeSelector(entityTable));
        this.selectorSet = new HashSet<>();
        parseSelectors(ruleTable);
    }

    private void parseSelectors(Table ruleTable) {
        for(SelectorType type : SelectorType.values()) {
            if(type!=SelectorType.ENTITY) {
                if(type.isSubTable()) {
                    for(Table table : ruleTable.getTablesByName(type.getName())) {
                        ISelector selector = type.makeSelector(table);
                        if(Objects.nonNull(selector)) this.selectorSet.add(selector);
                    }
                } else {
                    ISelector selector = type.makeSelector(ruleTable);
                    if(Objects.nonNull(selector)) this.selectorSet.add(selector);
                }
            }
        }
    }

    @Override
    public IRule build() {
        if(ConfigManager.INSTANCE.isMoreLogging()) {
            String isNull = Objects.isNull(this.entitySelectors) ? "null" : "nonnull";
            Constants.logVerbose(Level.DEBUG,"Building rule with {} entity selector", isNull);
        }
        return isBasic() ? buildBasic() : new DynamicRemove(this.groupName,this.entitySelectors,this.selectorSet);
    }

    private IRule buildBasic() {
        Set<BiomeSelector> biomeSelectors  = new HashSet<>();
        for(ISelector selector : this.selectorSet)
            if(selector instanceof BiomeSelector)
                biomeSelectors.add((BiomeSelector)selector);
        return new SingletonRemove(this.groupName,this.entitySelectors,biomeSelectors);
    }

    private boolean isBasic() {
        for(ISelector selector : this.selectorSet) {
            if(selector.isNonBasic()) return false;
        }
        return true;
    }
}

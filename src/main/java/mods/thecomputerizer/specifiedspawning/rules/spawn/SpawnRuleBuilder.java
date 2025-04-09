package mods.thecomputerizer.specifiedspawning.rules.spawn;

import mods.thecomputerizer.specifiedspawning.core.Constants;
import mods.thecomputerizer.specifiedspawning.rules.DynamicRule;
import mods.thecomputerizer.specifiedspawning.rules.IRule;
import mods.thecomputerizer.specifiedspawning.rules.IRuleBuilder;
import mods.thecomputerizer.specifiedspawning.rules.selectors.gamestages.GamestageSelector;
import mods.thecomputerizer.specifiedspawning.rules.selectors.vanilla.BiomeSelector;
import mods.thecomputerizer.specifiedspawning.rules.selectors.vanilla.EntitySelector;
import mods.thecomputerizer.specifiedspawning.rules.selectors.ISelector;
import mods.thecomputerizer.specifiedspawning.rules.selectors.SelectorType;
import mods.thecomputerizer.specifiedspawning.world.Events;
import mods.thecomputerizer.theimpossiblelibrary.api.toml.Toml;

import java.util.*;

import static mods.thecomputerizer.specifiedspawning.rules.selectors.SelectorType.ENTITY;
import static org.apache.logging.log4j.Level.DEBUG;

public class SpawnRuleBuilder implements IRuleBuilder {

    private final Toml ruleTable;
    private final List<EntitySelector> entitySelectors;
    private final Set<ISelector> selectorSet;
    private final String groupName;
    private final List<Toml> jockeyTables;
    private final boolean excessiveLogging;
    private final int adjustedIndex;
    private final boolean returnImmediately;

    public SpawnRuleBuilder(Toml rule, int order) {
        this.ruleTable = rule;
        this.groupName = rule.hasEntry("group") ? rule.getValueString("group") : "hostile";
        this.entitySelectors = new ArrayList<>();
        if(rule.hasTable("entity"))
            for(Toml entityTable : rule.getTableArray("entity"))
                this.entitySelectors.add((EntitySelector)ENTITY.makeSelector(entityTable));
        this.selectorSet = new HashSet<>();
        this.jockeyTables = rule.hasTable("jockey") ?
                Arrays.asList(rule.getTableArray("jockey")) : Collections.emptyList();
        this.excessiveLogging = rule.getValueBool("excessive_logging",false);
        this.adjustedIndex = order;
        this.returnImmediately = rule.getValueBool("return_immediately",false);
    }
    
    private void addSelector(SelectorType type, Toml table) {
        ISelector selector = type.makeSelector(table);
        if(Objects.nonNull(selector)) {
            this.selectorSet.add(selector);
            if(selector instanceof GamestageSelector) Events.addStageSelector((GamestageSelector)selector);
        }
    }
    
    @Override public void parseSelectors() {
        for(SelectorType type : SelectorType.values()) {
            if(type!=ENTITY) {
                if(type.isSubTable()) {
                    String typeName = type.getName();
                    if(this.ruleTable.hasTable(typeName))
                        for(Toml table : this.ruleTable.getTableArray(typeName)) addSelector(type,table);
                } else addSelector(type,this.ruleTable);
            }
        }
    }

    @Override public IRule build() {
        IRule rule =  isBasic() ? buildBasic() :
                new DynamicSpawn(this.groupName,this.entitySelectors,this.selectorSet,this.jockeyTables);
        rule.setOrder(this.adjustedIndex);
        if(rule instanceof DynamicRule) ((DynamicRule)rule).returnImmediately = this.returnImmediately;
        return rule;
    }

    private IRule buildBasic() {
        Set<BiomeSelector> biomeSelectors = new HashSet<>();
        for(ISelector selector : this.selectorSet)
            if(selector instanceof BiomeSelector) biomeSelectors.add((BiomeSelector)selector);
        return new SingletonSpawn(this.groupName,this.entitySelectors,biomeSelectors,this.jockeyTables);
    }

    private boolean isBasic() {
        for(ISelector selector : this.selectorSet)
            if(selector.isNonBasic()) return false;
        return true;
    }

    public void enableExcessiveLogging(DynamicSpawn rule) {
        Constants.logVerbose(DEBUG,"Is immediate? {}",this.returnImmediately);
        rule.shouldLogExcessively = this.excessiveLogging;
    }
}
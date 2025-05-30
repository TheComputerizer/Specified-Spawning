package mods.thecomputerizer.specifiedspawning.rules;

import mods.thecomputerizer.specifiedspawning.rules.group.IGroupRule;
import mods.thecomputerizer.specifiedspawning.rules.group.SpawnGroup;
import mods.thecomputerizer.specifiedspawning.rules.modify.IModifyRule;
import mods.thecomputerizer.specifiedspawning.rules.remove.IRemoveRule;
import mods.thecomputerizer.specifiedspawning.rules.selectors.vanilla.BiomeSelector;
import mods.thecomputerizer.specifiedspawning.rules.selectors.vanilla.EntitySelector;
import mods.thecomputerizer.specifiedspawning.rules.selectors.ResourceSelector;
import mods.thecomputerizer.specifiedspawning.core.SpawnManager;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.*;

import static net.minecraftforge.fml.common.registry.ForgeRegistries.BIOMES;
import static net.minecraftforge.fml.common.registry.ForgeRegistries.ENTITIES;

public abstract class AbstractRule implements IRule {

    protected String ruleDescriptor;
    private final String groupName;
    private int order;

    protected AbstractRule(String groupName) {
        this.groupName = groupName;
    }

    protected String getGroupName() {
        return this.groupName;
    }

    public Collection<SpawnGroup> getSpawnGroups() {
        if("all".equals(this.groupName)) return SpawnManager.getAllSpawnGroups();
        SpawnGroup group = SpawnManager.getSpawnGroup(this.groupName);
        return Objects.nonNull(group) ? Collections.singletonList(group) : Collections.emptyList();
    }

    protected void setRuleDescriptor() {
        String singleton = this instanceof SingletonRule ? "singleton" : "dynamic";
        String type = "spawn";
        if(this instanceof IGroupRule) type = "group";
        else if(this instanceof IModifyRule) type = "modify";
        else if(this instanceof IRemoveRule) type = "remove";
        this.ruleDescriptor = singleton+"-"+type;
    }

    private <V extends IForgeRegistryEntry<V>> Set<V> getRegSet(IForgeRegistry<V> reg, ResourceSelector<V> selector) {
        Set<V> ret = new HashSet<>();
        for(V entry : reg.getValuesCollection())
            if(selector.isResourceValid(entry,this.ruleDescriptor)) ret.add(entry);
        return ret;
    }

    protected Set<EntityEntry> getEntities(EntitySelector selector) {
        return getRegSet(ENTITIES,selector);
    }

    protected Set<EntityEntry> getEntities(Collection<EntitySelector> selectors) {
        Set<EntityEntry> ret = new HashSet<>();
        for(EntitySelector selector : selectors) ret.addAll(getEntities(selector));
        return ret;
    }

    protected Set<Biome> getBiomes(BiomeSelector selector) {
        return getRegSet(BIOMES,selector);
    }

    protected Set<Biome> getBiomes(Collection<BiomeSelector> selectors) {
        Set<Biome> ret = new HashSet<>();
        for(BiomeSelector selector : selectors) ret.addAll(getBiomes(selector));
        return ret;
    }

    @Override public void setOrder(int index) {
        this.order = index;
    }

    @Override public int getOrder() {
        return this.order;
    }
}
package mods.thecomputerizer.specifiedspawning.rules.selectors.vanilla;

import mods.thecomputerizer.specifiedspawning.core.Constants;
import mods.thecomputerizer.specifiedspawning.rules.selectors.ResourceSelector;
import mods.thecomputerizer.specifiedspawning.rules.selectors.SelectorType;
import mods.thecomputerizer.specifiedspawning.util.AddedEnums;
import mods.thecomputerizer.theimpossiblelibrary.api.toml.Toml;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLiving.SpawnPlacementType;
import net.minecraftforge.fml.common.registry.EntityEntry;

import java.util.Objects;

import static mods.thecomputerizer.specifiedspawning.rules.selectors.SelectorType.ENTITY;
import static net.minecraftforge.fml.common.registry.ForgeRegistries.ENTITIES;
import static org.apache.logging.log4j.Level.DEBUG;

public class EntitySelector extends ResourceSelector<EntityEntry> {

    public static EntitySelector makeSelector(Toml table) {
        if(Objects.isNull(table)) return null;
        Constants.logVerbose(DEBUG,"Making entity selector from table {}",table.getName());
        return new EntitySelector(table.getValueBool("inverted",false),
                table.hasTable("mod") ? table.getValueString("mod") : "",
                table.hasTable("entity") ? table.getValueString("entity") : "",
                table.hasTable("matcher") ? table.getValueString("matcher") : "",
                table.hasTable("type") ? table.getValueString("type") : "def",
                table.getValueBool("ignoreSpawnConditions",false),
                table.getValueInt("min_group_size",1),
                table.getValueInt("max_group_size",1),
                table.getValueInt("weight",10));
    }

    private final SpawnPlacementType spawnType;
    private final boolean ignoreSpawnConditions;
    private final int minGroupSpawn;
    private final int maxGroupSpawn;
    private final int weight;

    private EntitySelector(boolean isInverted, String mod, String entityID, String matcher, String spawnType,
                           boolean ignoreSpawnConditions,int minGroupSpawn, int maxGroupSpawn, int weight) {
        super(isInverted,mod,entityID,matcher);
        this.spawnType = AddedEnums.getSpawnType(spawnType);
        this.ignoreSpawnConditions = ignoreSpawnConditions;
        if(minGroupSpawn<=0) minGroupSpawn = 1;
        if(maxGroupSpawn<minGroupSpawn) maxGroupSpawn = minGroupSpawn;
        this.minGroupSpawn = minGroupSpawn;
        this.maxGroupSpawn = maxGroupSpawn;
        this.weight = weight;
    }

    public int getMinGroupSpawn() {
        return this.minGroupSpawn;
    }

    public int getMaxGroupSpawn() {
        return maxGroupSpawn;
    }

    public SpawnPlacementType getSpawnType() {
        return this.spawnType;
    }

    public int getWeight() {
        return this.weight;
    }

    @Override public boolean isResourceValid(EntityEntry entity, String ruleDescriptor) {
        if(Objects.isNull(entity) || !EntityLiving.class.isAssignableFrom(entity.getEntityClass())) return false;
        return isResourceValid(ENTITIES.getKey(entity),"entity",ruleDescriptor);
    }

    @Override public boolean isNonBasic() {
        return false;
    }

    @Override public SelectorType getType() {
        return ENTITY;
    }

    public boolean shouldIgnoreSpawnConditions() {
        return this.ignoreSpawnConditions;
    }
}
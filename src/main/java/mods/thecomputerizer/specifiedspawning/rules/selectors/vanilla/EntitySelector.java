package mods.thecomputerizer.specifiedspawning.rules.selectors.vanilla;

import mods.thecomputerizer.specifiedspawning.core.Constants;
import mods.thecomputerizer.specifiedspawning.rules.selectors.ResourceSelector;
import mods.thecomputerizer.specifiedspawning.rules.selectors.SelectorType;
import mods.thecomputerizer.theimpossiblelibrary.common.toml.Table;
import net.minecraft.entity.EntityLiving;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import org.apache.logging.log4j.Level;

import java.util.Objects;

public class EntitySelector extends ResourceSelector<EntityEntry> {

    public static EntitySelector makeSelector(Table table) {
        if(Objects.isNull(table)) return null;
        Constants.logVerbose(Level.DEBUG,"Making entity selector from table {}",table.getName());
        return new EntitySelector(table.getValOrDefault("inverted",false),
                table.getValOrDefault("mod",""),table.getValOrDefault("entity",""),
                table.getValOrDefault("matcher",""),table.getValOrDefault("min_group_size",1),
                table.getValOrDefault("max_group_size",1),table.getValOrDefault("weight",10));
    }

    private final int minGroupSpawn;
    private final int maxGroupSpawn;
    private final int weight;

    private EntitySelector(boolean isInverted, String mod, String entityID, String matcher, int minGroupSpawn,
                           int maxGroupSpawn, int weight) {
        super(isInverted,mod,entityID,matcher);
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

    public int getWeight() {
        return this.weight;
    }

    @Override
    public boolean isResourceValid(EntityEntry entity, String ruleDescriptor) {
        if(Objects.isNull(entity) || !EntityLiving.class.isAssignableFrom(entity.getEntityClass())) return false;
        return isResourceValid(ForgeRegistries.ENTITIES.getKey(entity),"entity",ruleDescriptor);
    }

    @Override
    public boolean isNonBasic() {
        return false;
    }

    @Override
    public SelectorType getType() {
        return SelectorType.ENTITY;
    }
}

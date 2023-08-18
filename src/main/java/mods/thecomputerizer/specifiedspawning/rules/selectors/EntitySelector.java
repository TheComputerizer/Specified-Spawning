package mods.thecomputerizer.specifiedspawning.rules.selectors;

import mods.thecomputerizer.specifiedspawning.Constants;
import mods.thecomputerizer.theimpossiblelibrary.common.toml.Table;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.Objects;

public class EntitySelector extends ResourceSelector implements ISelector<EntityEntry> {

    public static EntitySelector makeSelector(Table table) {
        if(Objects.isNull(table)) return null;
        return new EntitySelector(table.getValOrDefault("mod",""),
                table.getValOrDefault("entity",""),table.getValOrDefault("matcher",""),
                table.getValOrDefault("min_group_size",1),
                table.getValOrDefault("max_group_size",1),table.getValOrDefault("weight",10));
    }

    private final int minGroupSpawn;
    private final int maxGroupSpawn;
    private final int weight;

    private EntitySelector(String mod, String entityID, String matcher, int minGroupSpawn, int maxGroupSpawn, int weight) {
        super(mod,entityID,matcher);
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
    public boolean isValid(EntityEntry entity) {
        Constants.LOGGER.error("IS ENTITY NULL {}",Objects.isNull(entity));
        if(Objects.isNull(entity)) return false;
        Constants.LOGGER.error("IS ENTITY VALID FOR SELECTOR {}",entity.getEntityClass().getName());
        return isResourceValid(ForgeRegistries.ENTITIES.getKey(entity));
    }

    @Override
    public boolean isBasic() {
        return true;
    }
}

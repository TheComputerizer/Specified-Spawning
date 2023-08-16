package mods.thecomputerizer.specifiedspawning.rules.selectors;

import mods.thecomputerizer.theimpossiblelibrary.common.toml.Table;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.util.ResourceLocation;

import java.util.Objects;

public class EntitySelector implements ISelector<Entity> {

    public static EntitySelector makeSelector(Table table) {
        return new EntitySelector(table.getValOrDefault("mod",""),
                table.getValOrDefault("entity",""),table.getValOrDefault("matcher",""));
    }

    private final String mod;
    private final String entityID;
    private final String matcher;

    private EntitySelector(String mod, String entityID, String matcher) {
        this.mod = mod.isEmpty() ? null : mod;
        this.entityID = entityID.isEmpty() ? null : entityID;
        this.matcher = matcher.isEmpty() ? null : matcher;
    }

    public boolean isValid(Entity entity) {
        if(Objects.isNull(entity)) return false;
        ResourceLocation res = EntityList.getKey(entity);
        if(Objects.isNull(res)) return false;
        return (Objects.isNull(this.mod) || res.getNamespace().matches(this.mod)) &&
                (Objects.isNull(this.entityID) || res.toString().matches(this.entityID)) &&
                (Objects.isNull(this.matcher) || res.toString().contains(this.matcher));
    }
}

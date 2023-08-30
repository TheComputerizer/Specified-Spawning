package mods.thecomputerizer.specifiedspawning.world.entity;

import mods.thecomputerizer.specifiedspawning.core.Constants;
import mods.thecomputerizer.specifiedspawning.rules.selectors.vanilla.EntitySelector;
import mods.thecomputerizer.theimpossiblelibrary.common.toml.Table;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.WeightedRandom;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Jockey extends WeightedRandom.Item {

    public static Jockey parse(Table jockeyTable) {
        EntitySelector selector = EntitySelector.makeSelector(jockeyTable);
        if(Objects.nonNull(selector)) {
            List<EntityEntry> entries = new ArrayList<>();
            for(EntityEntry entry : ForgeRegistries.ENTITIES.getValuesCollection())
                if(selector.isResourceValid(entry,"jockey")) entries.add(entry);
            EntityEntry rider = entries.isEmpty() ? null : entries.get(Constants.RANDOM.nextInt(entries.size()));
            Jockey jockey = new Jockey(rider,jockeyTable.getValOrDefault("weight",10));
            for(Table subJockeyTable : jockeyTable.getTablesByName("jockey")) {
                Jockey subJockey = parse(subJockeyTable);
                if(Objects.nonNull(subJockey)) jockey.addSubJockey(subJockey);
            }
            return jockey;
        }
        return null;
    }

    private final List<Jockey> subJockeys;
    private final EntityEntry rider;

    private Jockey(EntityEntry rider, int weight) {
        super(weight);
        this.subJockeys = new ArrayList<>();
        this.rider = rider;
    }

    public void addSubJockey(Jockey jockey) {
        this.subJockeys.add(jockey);
    }

    public boolean isValidJockey() {
        return Objects.nonNull(this.rider);
    }

    public void spawnRider(Entity vehicle, DifficultyInstance difficulty) {
        World world = vehicle.getEntityWorld();
        Entity entity = this.rider.newInstance(world);
        entity.setLocationAndAngles(vehicle.posX,vehicle.posY,vehicle.posZ,vehicle.rotationYaw,0f);
        if(entity instanceof EntityLiving) ((EntityLiving)entity).onInitialSpawn(difficulty,null);
        world.spawnEntity(entity);
        entity.startRiding(vehicle);
        if(!this.subJockeys.isEmpty()) {
            if(!entity.isBeingRidden()) {
                Jockey jockey = WeightedRandom.getRandomItem(entity.world.rand,this.subJockeys);
                if(jockey.isValidJockey()) jockey.spawnRider(entity,difficulty);
            }
        }
    }
}

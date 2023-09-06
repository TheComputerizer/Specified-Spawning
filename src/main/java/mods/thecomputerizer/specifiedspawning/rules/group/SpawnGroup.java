package mods.thecomputerizer.specifiedspawning.rules.group;

import mods.thecomputerizer.specifiedspawning.rules.IRule;
import mods.thecomputerizer.specifiedspawning.rules.IRuleBuilder;
import mods.thecomputerizer.specifiedspawning.util.EnumUtil;
import mods.thecomputerizer.theimpossiblelibrary.common.toml.Table;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.entity.passive.IAnimals;

public class SpawnGroup implements IRule, IGroupRule {

    private final EnumCreatureType type;
    private final int count;
    private final int weight;
    private final boolean isPeaceful;
    private final boolean isAnimal;
    private final boolean isAquatic;

    private SpawnGroup(EnumCreatureType type, int count, int weight, boolean peaceful, boolean animal, boolean aquatic) {
        this.type = type;
        this.count = count;
        this.weight = weight;
        this.isPeaceful = peaceful;
        this.isAnimal = animal;
        this.isAquatic = aquatic;
    }

    public EnumCreatureType getType() {
        return this.type;
    }

    public int getCount() {
        return this.count;
    }

    public int getWeight() {
        return weight;
    }

    public boolean isPeaceful() {
        return isPeaceful;
    }

    public boolean isAnimal() {
        return isAnimal;
    }

    public boolean isAquatic() {
        return isAquatic;
    }

    public Material getSpawnMaterial() {
        return this.isAquatic ? Material.WATER : Material.AIR;
    }

    @Override
    public void setOrder(int index) {

    }

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public void setup() {

    }

    public static class Builder implements IRuleBuilder {

        private final String name;
        private final EnumCreatureType creatureType;
        private final int count;
        private final int weight;
        private final boolean isPeaceful;
        private final boolean isAnimal;
        private final boolean isAquatic;

        public Builder(Table table, int order) {
            this.name = table.getValOrDefault("name","hostile");
            if(EnumUtil.isValidCreatureReference(this.name)) {
                this.creatureType = EnumUtil.getOrCreateEnumType(this.name);
                this.count = table.getValOrDefault("count",this.creatureType.getMaxNumberOfCreature());
                this.weight = table.getValOrDefault("weight",100);
                this.isPeaceful = table.getValOrDefault("peaceful",this.creatureType.getPeacefulCreature());
                this.isAnimal = table.getValOrDefault("animal",this.creatureType.getAnimal());
                this.isAquatic = table.getValOrDefault("aquatic",EntityWaterMob.class.isAssignableFrom(this.creatureType.getCreatureClass()));
            } else {
                this.count = table.getValOrDefault("count",20);
                this.weight = table.getValOrDefault("weight",100);
                this.isPeaceful = table.getValOrDefault("peaceful",false);
                this.isAnimal = table.getValOrDefault("animal",false);
                this.isAquatic = table.getValOrDefault("aquatic",false);
                this.creatureType = EnumUtil.makeNewEnumCreatureType(this.name,IAnimals.class,this.count,
                        this.isAquatic ? Material.WATER : Material.AIR,this.isPeaceful,this.isAnimal);
            }
        }

        /**
         * Used internally to convert the vanilla EnumCreatureType values
         */
        public Builder(String name, EnumCreatureType type) {
            this.name = name;
            this.creatureType = type;
            this.count = this.creatureType.getMaxNumberOfCreature();
            this.weight = 100-this.count;
            this.isPeaceful = this.creatureType.getPeacefulCreature();
            this.isAnimal = this.creatureType.getAnimal();
            this.isAquatic = EntityWaterMob.class.isAssignableFrom(this.creatureType.getCreatureClass());
        }

        public String getName() {
            return this.name;
        }

        public SpawnGroup build() {
            return new SpawnGroup(this.creatureType,this.count,this.weight,this.isPeaceful,this.isAnimal,this.isAquatic);
        }
    }
}

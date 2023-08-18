package mods.thecomputerizer.specifiedspawning.rules.group;

import mods.thecomputerizer.specifiedspawning.rules.IRule;
import mods.thecomputerizer.specifiedspawning.rules.IRuleBuilder;
import mods.thecomputerizer.specifiedspawning.util.EnumUtil;
import mods.thecomputerizer.theimpossiblelibrary.common.toml.Table;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.passive.EntityWaterMob;

public class SpawnGroup implements IRule {

    private final EnumCreatureType type;
    private final int weight;

    private SpawnGroup(EnumCreatureType type, int weight) {
        this.type = type;
        this.weight = weight;
    }

    public EnumCreatureType getType() {
        return this.type;
    }

    public int getCount() {
        return this.type.getMaxNumberOfCreature();
    }

    public int getWeight() {
        return weight;
    }

    @Override
    public void setup() {

    }

    public static class Builder implements IRuleBuilder {

        private final String name;
        private final EnumCreatureType creatureType;
        private int count;
        private int weight;
        private boolean isPeaceful;
        private boolean isAnimal;
        private boolean isAquatic;

        public Builder(Table table) {
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
                this.creatureType = EnumUtil.makeNewEnumCreatureType(this.name,null,this.count,
                        this.isAquatic ? Material.WATER : Material.AIR,this.isPeaceful,this.isAnimal);
            }
        }

        /**
         * Used internally to convert the vanilla EnumCreatureType values
         */
        public Builder(String name, EnumCreatureType type) {
            this.name = name;
            this.creatureType = type;
            this.weight = 100-type.getMaxNumberOfCreature();
        }

        public String getName() {
            return this.name;
        }

        public SpawnGroup build() {
            return new SpawnGroup(this.creatureType,this.weight);
        }
    }
}

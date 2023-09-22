package mods.thecomputerizer.specifiedspawning.rules.group;

import mods.thecomputerizer.specifiedspawning.rules.IRule;
import mods.thecomputerizer.specifiedspawning.rules.IRuleBuilder;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EnumCreatureType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class SpawnGroup implements IRule, IGroupRule {

    private static final List<Builder> BUILDERS = new ArrayList<>();

    public static List<Builder> getBuilders() {
        return Collections.unmodifiableList(BUILDERS);
    }

    private final EnumCreatureType type;
    private final int count;
    private final int weight;
    private final boolean isPeaceful;
    private final boolean isAnimal;
    private final boolean isAquatic;

    private SpawnGroup(EnumCreatureType type, int count, boolean isPeaceful, boolean isAnimal, boolean isAquatic) {
        this.type = type;
        this.count = count;
        this.weight = 100;
        this.isPeaceful = isPeaceful;
        this.isAnimal = isAnimal;
        this.isAquatic = isAquatic;
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

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public static class Builder implements IRuleBuilder {

        private final String name;
        private final Optional<Integer> potentialCount;
        private final Optional<Boolean> potentialPeaceful;
        private final Optional<Boolean> potentialAnimal;
        private final Optional<Boolean> potentialAquatic;
        private EnumCreatureType creatureType;
        private int count;
        private boolean isPeaceful;
        private boolean isAnimal;
        private boolean isAquatic;

        public Builder(String name, Optional<Integer> count, Optional<Boolean> isPeaceful, Optional<Boolean> isAnimal,
                       Optional<Boolean> isAquatic) {
            this.name = name;
            this.potentialCount = count;
            this.potentialPeaceful = isPeaceful;
            this.potentialAnimal = isAnimal;
            this.potentialAquatic = isAquatic;
            BUILDERS.add(this);
        }

        /**
         * Called for default unused spawn groups
         */
        public Builder(String name) {
            this.name = name;
            this.potentialCount = Optional.empty();
            this.potentialPeaceful = Optional.empty();
            this.potentialAnimal = Optional.empty();
            this.potentialAquatic = Optional.empty();
        }

        @Override
        public void parseSelectors() {}

        public String getName() {
            return this.name;
        }

        public void setCreatureType(EnumCreatureType type) {
            this.creatureType = type;
        }

        public Optional<Integer> getCount() {
            return this.potentialCount;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public Optional<Boolean> getPeaceful() {
            return this.potentialPeaceful;
        }

        public void setPeaceful(boolean isPeaceful) {
            this.isPeaceful = isPeaceful;
        }

        public Optional<Boolean> getAnimal() {
            return this.potentialAnimal;
        }

        public void setAnimal(boolean isAnimal) {
            this.isAnimal = isAnimal;
        }

        public Optional<Boolean> getAquatic() {
            return this.potentialAquatic;
        }

        public void setAquatic(boolean isAquatic) {
            this.isAquatic = isAquatic;
        }

        public SpawnGroup build() {
            return new SpawnGroup(this.creatureType,this.count,this.isPeaceful,this.isAnimal,this.isAquatic);
        }
    }
}

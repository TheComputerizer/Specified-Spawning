package mods.thecomputerizer.specifiedspawning.rules.group;

import mods.thecomputerizer.shadowed.moandjiezana.toml.Toml;
import mods.thecomputerizer.specifiedspawning.rules.IRule;
import mods.thecomputerizer.specifiedspawning.rules.IRuleBuilder;
import mods.thecomputerizer.theimpossiblelibrary.util.file.TomlUtil;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EnumCreatureType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class SpawnGroup implements IRule, IGroupRule {

    private static final List<Builder> BUILDERS = new ArrayList<>();
    private static boolean canBuild = true;

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

    public static class Builder implements IRuleBuilder {

        private final Toml table;
        private final String name;
        private EnumCreatureType creatureType;
        private int count;
        private boolean isPeaceful;
        private boolean isAnimal;
        private boolean isAquatic;

        public Builder(Toml table) {
            this.name = TomlUtil.readIfExists(table,"name","hostile");
            this.table = table;
            BUILDERS.add(this);
        }

        /**
         * Called for default unused spawn groups
         */
        public Builder(String name) {
            this.name = name;
            this.table = null;
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
            return this.table.containsPrimitive("count") ?
                    Optional.of(TomlUtil.readIfExists(this.table,"count",0)) : Optional.empty();
        }

        public void setCount(int count) {
            this.count = count;
        }

        public Optional<Boolean> getPeaceful() {
            return this.table.containsPrimitive("peaceful") ?
                    Optional.of(TomlUtil.readIfExists(this.table,"peaceful",false)) : Optional.empty();
        }

        public void setPeaceful(boolean isPeaceful) {
            this.isPeaceful = isPeaceful;
        }

        public Optional<Boolean> getAnimal() {
            return this.table.containsPrimitive("animal") ?
                    Optional.of(TomlUtil.readIfExists(this.table,"animal",false)) : Optional.empty();
        }

        public void setAnimal(boolean isAnimal) {
            this.isAnimal = isAnimal;
        }

        public Optional<Boolean> getAquatic() {
            return this.table.containsPrimitive("aquatic") ?
                    Optional.of(TomlUtil.readIfExists(this.table,"aquatic",false)) : Optional.empty();
        }

        public void setAquatic(boolean isAquatic) {
            this.isAquatic = isAquatic;
        }

        public SpawnGroup build() {
            return new SpawnGroup(this.creatureType,this.count,this.isPeaceful,this.isAnimal,this.isAquatic);
        }
    }
}

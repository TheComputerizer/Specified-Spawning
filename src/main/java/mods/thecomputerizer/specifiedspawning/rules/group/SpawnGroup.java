package mods.thecomputerizer.specifiedspawning.rules.group;

public class SpawnGroup {

    private final int count;
    private final int weight;

    private SpawnGroup(int count, int weight) {
        this.count = count;
        this.weight = weight;
    }

    public int getCount() {
        return this.count;
    }

    public int getWeight() {
        return weight;
    }

    public static class Builder {

        private int count;
        private int weight;

        public Builder setCount(int count) {
            this.count = count;
            return this;
        }

        public Builder setWeight(int weight) {
            this.weight = weight;
            return this;
        }

        public SpawnGroup build() {
            return new SpawnGroup(this.count,this.weight);
        }
    }
}

package mods.thecomputerizer.specifiedspawning.rules.selectors;

import mods.thecomputerizer.specifiedspawning.SpecifiedSpawning;
import mods.thecomputerizer.specifiedspawning.rules.selectors.gamestages.GamestageSelector;
import mods.thecomputerizer.specifiedspawning.rules.selectors.nyx.BloodmoonSelector;
import mods.thecomputerizer.specifiedspawning.rules.selectors.nyx.FullmoonSelector;
import mods.thecomputerizer.specifiedspawning.rules.selectors.nyx.HarvestmoonSelector;
import mods.thecomputerizer.specifiedspawning.rules.selectors.nyx.StarShowerSelector;
import mods.thecomputerizer.specifiedspawning.rules.selectors.scalinghealth.ScalingDifficultySelector;
import mods.thecomputerizer.specifiedspawning.rules.selectors.vanilla.*;
import mods.thecomputerizer.theimpossiblelibrary.common.toml.Table;

import java.util.Objects;
import java.util.function.Function;

public enum SelectorType {

    BIOME("biome",true,null,BiomeSelector::makeSelector),
    BLOODMOON("bloodmoon",true,"nyx",BloodmoonSelector::makeSelector),
    DIMENSION("dimension",false,null,DimensionSelector::makeSelector),
    ENTITY("entity",true,null,EntitySelector::makeSelector),
    FULLMOON("fullmoon",true,"nyx",FullmoonSelector::makeSelector),
    GAMESTAGE("gamestage",true,"gamestages",GamestageSelector::makeSelector),
    HARVESTMOON("harvestmoon",true,"nyx",HarvestmoonSelector::makeSelector),
    HEIGHT("height",true,null,HeightSelector::makeSelector),
    LIGHT("light",true,null,LightSelector::makeSelector),
    MOONPHASE("moonphase",true,null,MoonPhaseSelector::makeSelector),
    REGIONALDIFFICULTY("regionaldifficulty",true,null,RegionalDifficultySelector::makeSelector),
    SCALINGDIFFICULTY("scalingdifficulty",true,"scalingdifficulty",ScalingDifficultySelector::makeSelector),
    SPAWNBLOCK("spawnblock",true,null,SpawnBlockSelector::makeSelector),
    STARSHOWER("starshower",true,"nyx",StarShowerSelector::makeSelector),
    STRUCTURE("structure",true,null,StructureSelector::makeSelector);


    private final String name;
    private final boolean subTable;
    private final String requiredMod;
    private final Function<Table,ISelector> creatorFunction;

    SelectorType(String name, boolean subTable, String requiredMod, Function<Table,ISelector> creatorFunction) {
        this.name = name;
        this.subTable = subTable;
        this.requiredMod = requiredMod;
        this.creatorFunction = creatorFunction;
    }

    public String getName() {
        return this.name;
    }

    public boolean isSubTable() {
        return this.subTable;
    }

    public ISelector makeSelector(Table table) {
        if(Objects.isNull(table)) return null;
        if(Objects.nonNull(this.requiredMod) && !SpecifiedSpawning.isModLoaded(this.requiredMod)) return null;
        return this.creatorFunction.apply(table);
    }
}

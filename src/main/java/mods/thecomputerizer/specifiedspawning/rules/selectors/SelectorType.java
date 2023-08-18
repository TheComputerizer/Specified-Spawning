package mods.thecomputerizer.specifiedspawning.rules.selectors;

import mods.thecomputerizer.theimpossiblelibrary.common.toml.Table;

import java.util.*;
import java.util.function.Function;

public enum SelectorType {

    BIOME("biome",true,BiomeSelector::makeSelector),
    DIMENSION("dimension",false,DimensionSelector::makeSelector),
    ENTITY("entity",true,EntitySelector::makeSelector),
    GAMESTAGE("gamestage",true,GamestageSelector::makeSelector),
    LIGHT("light",true,LightSelector::makeSelector),
    HEIGHT("height",true,HeightSelector::makeSelector);


    private final String name;
    private final boolean subTable;
    private final Function<Table,ISelector<?>> creatorFunction;

    SelectorType(String name, boolean subTable, Function<Table,ISelector<?>> creatorFunction) {
        this.name = name;
        this.subTable = subTable;
        this.creatorFunction = creatorFunction;
    }

    public String getName() {
        return this.name;
    }

    public boolean isSubTable() {
        return this.subTable;
    }

    public ISelector<?> makeSelector(Table table) {
        if(Objects.isNull(table)) return null;
        return this.creatorFunction.apply(table);
    }
}

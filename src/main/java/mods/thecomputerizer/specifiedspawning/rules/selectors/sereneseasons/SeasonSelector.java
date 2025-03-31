package mods.thecomputerizer.specifiedspawning.rules.selectors.sereneseasons;

import mods.thecomputerizer.specifiedspawning.rules.selectors.AbstractSelector;
import mods.thecomputerizer.specifiedspawning.rules.selectors.SelectorType;
import mods.thecomputerizer.theimpossiblelibrary.api.toml.Toml;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import sereneseasons.api.season.Season;
import sereneseasons.api.season.SeasonHelper;

import java.util.Objects;

import static mods.thecomputerizer.specifiedspawning.rules.selectors.SelectorType.SEASON;

public class SeasonSelector extends AbstractSelector {

    public static SeasonSelector makeSelector(Toml table) {
        if(Objects.isNull(table)) return null;
        return new SeasonSelector(table.getValueBool("inverted",false),
                table.hasEntry("season") ? table.getValueString("season") : "spring");
    }

    private final String season;

    private SeasonSelector(boolean isInverted, String season) {
        super(isInverted);
        this.season = season;
    }

    @Override protected boolean isValidInner(BlockPos pos, World world, String ruleDescriptor) {
        Season season = SeasonHelper.getSeasonState(world).getSeason();
        return normalizedSeasonName().equals(season.name().toLowerCase());
    }

    private String normalizedSeasonName() {
        switch(this.season) {
            case "0": return "spring";
            case "1": return "summer";
            case "2":
            case "fall": return "autumn";
            case "3": return "winter";
            default: return this.season;
        }
    }

    @Override public SelectorType getType() {
        return SEASON;
    }

    @Override public boolean isNonBasic() {
        return true;
    }
}
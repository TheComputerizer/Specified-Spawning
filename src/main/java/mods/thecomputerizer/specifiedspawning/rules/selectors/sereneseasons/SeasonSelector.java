package mods.thecomputerizer.specifiedspawning.rules.selectors.sereneseasons;

import mods.thecomputerizer.specifiedspawning.core.Constants;
import mods.thecomputerizer.specifiedspawning.rules.selectors.AbstractSelector;
import mods.thecomputerizer.specifiedspawning.rules.selectors.SelectorType;
import mods.thecomputerizer.specifiedspawning.rules.selectors.gamestages.GamestageSelector;
import mods.thecomputerizer.specifiedspawning.util.ParsingUtils;
import mods.thecomputerizer.theimpossiblelibrary.common.toml.Table;
import mods.thecomputerizer.theimpossiblelibrary.util.TextUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.logging.log4j.Level;
import sereneseasons.api.season.Season;
import sereneseasons.api.season.SeasonHelper;

import java.util.List;
import java.util.Objects;

public class SeasonSelector extends AbstractSelector {

    public static SeasonSelector makeSelector(Table table) {
        if(Objects.isNull(table)) return null;
        return new SeasonSelector(table.getValOrDefault("inverted",false),
                table.getValOrDefault("season","spring"));
    }

    private final String season;

    private SeasonSelector(boolean isInverted, String season) {
        super(isInverted);
        this.season = season;
    }

    @Override
    protected boolean isValidInner(BlockPos pos, World world, String ruleDescriptor) {
        Season season = SeasonHelper.getSeasonState(world).getSeason();
        return normalizedSeasonName().matches(season.name().toLowerCase());
    }

    private String normalizedSeasonName() {
        if(this.season.matches("0")) return "spring";
        if(this.season.matches("1")) return "summer";
        if(this.season.matches("2") || this.season.matches("fall")) return "autumn";
        if(this.season.matches("3")) return "winter";
        return this.season;
    }

    @Override
    public SelectorType getType() {
        return SelectorType.SEASON;
    }

    @Override
    public boolean isNonBasic() {
        return true;
    }
}

package mods.thecomputerizer.specifiedspawning.rules.selectors.gamestages;

import mods.thecomputerizer.specifiedspawning.core.Constants;
import mods.thecomputerizer.specifiedspawning.rules.selectors.AbstractSelector;
import mods.thecomputerizer.specifiedspawning.rules.selectors.SelectorType;
import mods.thecomputerizer.specifiedspawning.util.ParsingUtils;
import mods.thecomputerizer.theimpossiblelibrary.common.toml.Table;
import mods.thecomputerizer.theimpossiblelibrary.util.TextUtil;
import net.darkhax.gamestages.GameStageHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.logging.log4j.Level;

import java.util.List;
import java.util.Objects;

public class GamestageSelector extends AbstractSelector {

    public static GamestageSelector makeSelector(Table table) {
        if(Objects.isNull(table)) return null;
        return new GamestageSelector(table.getValOrDefault("inverted",false),
                ParsingUtils.getStringList(table.getVarMap().get("gamestage")),
                table.getValOrDefault("is_whitelist",true),
                table.getValOrDefault("all_stages",true),
                table.getValOrDefault("all_players",false));
    }

    private final List<String> stageNames;
    private final boolean isWhitelist;
    private final boolean allStages;
    private final boolean allPlayers;

    private GamestageSelector(boolean isInverted, List<String> stageNames, boolean isWhitelist, boolean allStages,
                              boolean allPlayers) {
        super(isInverted);
        this.stageNames = stageNames;
        this.isWhitelist = isWhitelist;
        this.allStages = allStages;
        this.allPlayers = allPlayers;
        Constants.logVerbose(Level.DEBUG,"Instantiated new gamestage selector with stages {}",TextUtil.listToString(stageNames," "));
    }

    @Override
    public boolean isValidInner(BlockPos pos, World world, String ruleDescriptor) {
        List<EntityPlayer> players = world.playerEntities;
        if(players.isEmpty()) return false;
        boolean any = false;
        for(EntityPlayer player : players) {
            if(checkPlayerStages(player)) {
                if(!this.allPlayers) return true;
                any = true;
            } else if(this.allPlayers) return false;
        }
        return any;
    }

    private boolean checkPlayerStages(EntityPlayer player) {
        if(this.allStages)
            return this.isWhitelist ? GameStageHelper.hasAllOf(player,this.stageNames) :
                    !GameStageHelper.hasAnyOf(player,this.stageNames);
        boolean hasAny = GameStageHelper.hasAnyOf(player,this.stageNames);
        return this.isWhitelist == hasAny;
    }

    @Override
    public boolean isNonBasic() {
        return true;
    }

    @Override
    public SelectorType getType() {
        return SelectorType.GAMESTAGE;
    }
}

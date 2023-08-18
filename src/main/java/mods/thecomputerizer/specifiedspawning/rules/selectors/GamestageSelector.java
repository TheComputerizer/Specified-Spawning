package mods.thecomputerizer.specifiedspawning.rules.selectors;

import mods.thecomputerizer.specifiedspawning.util.ParsingUtils;
import mods.thecomputerizer.theimpossiblelibrary.common.toml.Table;
import net.darkhax.gamestages.GameStageHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import java.util.List;
import java.util.Objects;

public class GamestageSelector implements ISelector<World> {

    public static GamestageSelector makeSelector(Table table) {
        if(Objects.isNull(table)) return null;
        return new GamestageSelector(ParsingUtils.getStringList(table.getVarMap().get("gamestage")),
                table.getValOrDefault("isWhitelist",true),
                table.getValOrDefault("allStages",true),
                table.getValOrDefault("allPlayers",false));
    }

    private final List<String> stageNames;
    private final boolean isWhitelist;
    private final boolean allStages;
    private final boolean allPlayers;

    private GamestageSelector(List<String> stageNames, boolean isWhitelist, boolean allStages, boolean allPlayers) {
        this.stageNames = stageNames;
        this.isWhitelist = isWhitelist;
        this.allStages = allStages;
        this.allPlayers = allPlayers;
    }

    @Override
    public boolean isValid(World world) {
        List<EntityPlayer> players = world.playerEntities;
        if(players.isEmpty()) return false;
        for(EntityPlayer player : players) {
            if(checkPlayerStages(player)) {
                if(!this.allPlayers) return true;
            } else if(this.allPlayers) return false;
        }
        return true;
    }

    private boolean checkPlayerStages(EntityPlayer player) {
        if(this.allStages)
            return this.isWhitelist ? GameStageHelper.hasAllOf(player,this.stageNames) :
                    !GameStageHelper.hasAnyOf(player,this.stageNames);
        boolean hasAny = GameStageHelper.hasAnyOf(player,this.stageNames);
        return this.isWhitelist == hasAny;
    }



    @Override
    public boolean isBasic() {
        return false;
    }
}

package mods.thecomputerizer.specifiedspawning.rules.selectors.gamestages;

import mods.thecomputerizer.specifiedspawning.core.Constants;
import mods.thecomputerizer.specifiedspawning.rules.selectors.AbstractSelector;
import mods.thecomputerizer.specifiedspawning.rules.selectors.SelectorType;
import mods.thecomputerizer.specifiedspawning.util.ParsingUtils;
import mods.thecomputerizer.theimpossiblelibrary.api.text.TextHelper;
import mods.thecomputerizer.theimpossiblelibrary.api.toml.Toml;
import net.darkhax.gamestages.GameStageHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static mods.thecomputerizer.specifiedspawning.rules.selectors.SelectorType.GAMESTAGE;
import static org.apache.logging.log4j.Level.DEBUG;

//TODO Caching for gamestages to fix performance issues
public class GamestageSelector extends AbstractSelector {

    public static GamestageSelector makeSelector(Toml table) {
        if(Objects.isNull(table)) return null;
        return new GamestageSelector(table.getValueBool("inverted",false),
                ParsingUtils.getStringList(table.getEntryValuesAsMap().get("gamestage")),
                table.getValueBool("is_whitelist",true),
                table.getValueBool("all_stages",true),
                table.getValueBool("all_players",false));
    }

    private final Map<EntityPlayer,PlayerCache> cacheMap;
    private final List<String> stageNames;
    private final boolean isWhitelist;
    private final boolean allStages;
    private final boolean allPlayers;

    private GamestageSelector(boolean isInverted, List<String> stageNames, boolean isWhitelist, boolean allStages,
                              boolean allPlayers) {
        super(isInverted);
        this.cacheMap = new HashMap<>();
        this.stageNames = stageNames;
        this.isWhitelist = isWhitelist;
        this.allStages = allStages;
        this.allPlayers = allPlayers;
        Constants.logVerbose(DEBUG,"Instantiated new gamestage selector with stages {}",
                             TextHelper.fromIterable(stageNames," "));
    }
    
    public void cache(EntityPlayer player) {
        if(!this.cacheMap.containsKey(player)) this.cacheMap.put(player,new PlayerCache(player));
        this.cacheMap.get(player).cache(this.stageNames);
    }
    
    public void clearCache() {
        this.cacheMap.clear();
    }
    
    @Override public SelectorType getType() {
        return GAMESTAGE;
    }
    
    private boolean invalidPlayer(EntityPlayer player) {
        return player instanceof FakePlayer || !(player instanceof EntityPlayerMP);
    }
    
    private boolean isValidAll(Collection<EntityPlayer> players) {
        boolean anyValidPlayer = false;
        for(EntityPlayer player : players) {
            if(invalidPlayer(player)) continue;
            anyValidPlayer = true;
            PlayerCache cache = this.cacheMap.get(player);
            if(Objects.isNull(cache) || !cache.check(this.allStages,this.isWhitelist)) return false;
        }
        if(!anyValidPlayer) return !this.isWhitelist;
        return true;
    }
    
    private boolean isValidAny(Collection<EntityPlayer> players) {
        for(EntityPlayer player : players) {
            if(invalidPlayer(player)) continue;
            PlayerCache cache = this.cacheMap.get(player);
            if(Objects.nonNull(cache) && cache.check(this.allStages,this.isWhitelist)) return true;
        }
        return false;
    }

    @Override public boolean isValidInner(BlockPos pos, World world, String ruleDescriptor) {
        List<EntityPlayer> players = world.playerEntities;
        if(players.isEmpty()) return !this.isWhitelist;
        return this.allPlayers ? isValidAll(players) : isValidAny(players);
    }

    @Override public boolean isNonBasic() {
        return true;
    }
    
    public void removeFromCache(EntityPlayer player) {
        this.cacheMap.remove(player);
    }

    @Override public String toString() {
        return "Gamestage Selector ("+TextHelper.arrayToString(" ",this.stageNames.toArray())+")";
    }
    
    private static final class PlayerCache {
        
        private final EntityPlayer player;
        private boolean hasAny;
        private boolean hasAll;
        private boolean hasNone;
        
        PlayerCache(EntityPlayer player) {
            this.player = player;
        }
        
        void cache(Collection<String> stageNames) {
            this.hasAll = Objects.nonNull(this.player) && GameStageHelper.hasAllOf(this.player,stageNames);
            this.hasAny = this.hasAll ||
                          (Objects.nonNull(this.player) && GameStageHelper.hasAnyOf(this.player,stageNames));
            this.hasNone = !this.hasAny;
        }
        
        boolean check(boolean all, boolean whitelist) {
            return whitelist ? (all ? this.hasAll : this.hasAny) : this.hasNone;
        }
    }
}
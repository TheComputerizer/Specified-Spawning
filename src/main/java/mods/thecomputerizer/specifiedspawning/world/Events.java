package mods.thecomputerizer.specifiedspawning.world;

import mods.thecomputerizer.specifiedspawning.SpecifiedSpawning;
import mods.thecomputerizer.specifiedspawning.mixin.access.ISpawnGroupObject;
import mods.thecomputerizer.specifiedspawning.rules.DynamicRule;
import mods.thecomputerizer.specifiedspawning.rules.selectors.gamestages.GamestageSelector;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.world.WorldEvent.PotentialSpawns;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import static mods.thecomputerizer.specifiedspawning.core.Constants.MODID;
import static net.minecraftforge.fml.common.eventhandler.EventPriority.LOWEST;
import static net.minecraftforge.fml.common.gameevent.TickEvent.Phase.END;

@EventBusSubscriber(modid=MODID)
public class Events {
    
    private static final Set<GamestageSelector> STAGE_SELECTORS = new HashSet<>();
    private static final AtomicInteger TICK_COUNTER = new AtomicInteger();
    
    public static void addStageSelector(GamestageSelector selector) {
        STAGE_SELECTORS.add(selector);
    }
    
    public static void clearStageSelectors() {
        STAGE_SELECTORS.forEach(GamestageSelector::clearCache);
        STAGE_SELECTORS.clear();
    }

    @SubscribeEvent
    public static void onGetPotentialSpawns(PotentialSpawns event) {
        if(event.getWorld() instanceof WorldServer) {
            WorldServer world = (WorldServer)event.getWorld();
            BlockPos pos = event.getPos();
            if(SpecifiedSpawning.isModLoaded("scalingdifficulty")) SHHooks.cachePlayerData(pos,world);
            event.getList().removeIf(entry -> {
                boolean ret = false;
                for(DynamicRule rule : ((ISpawnGroupObject)entry).specifiedspawning$getDynamicRules()) {
                    if(rule.checkSelectors(pos,world)) ret = rule.isRemoval();
                    else ret = !rule.isRemoval();
                    if(rule.returnImmediately) return ret;
                }
                return ret;
            });
        }
    }
    
    @SubscribeEvent(priority = LOWEST)
    public static void onPlayerLeave(PlayerLoggedOutEvent event) {
        EntityPlayer player = event.player;
        if(!player.world.isRemote)
            for(GamestageSelector selector : STAGE_SELECTORS) selector.removeFromCache(player);
    }
    
    @SubscribeEvent(priority = LOWEST)
    public static void onPlayerTick(PlayerTickEvent event) {
        if(event.phase!=END) return;
        EntityPlayer player = event.player;
        if(!player.world.isRemote && TICK_COUNTER.addAndGet(1)>5) {
            for(GamestageSelector selector : STAGE_SELECTORS) selector.cache(player);
            TICK_COUNTER.set(0);
        }
    }
}
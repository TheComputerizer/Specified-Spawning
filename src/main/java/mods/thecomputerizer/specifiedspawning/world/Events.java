package mods.thecomputerizer.specifiedspawning.world;

import mods.thecomputerizer.specifiedspawning.SpecifiedSpawning;
import mods.thecomputerizer.specifiedspawning.core.Constants;
import mods.thecomputerizer.specifiedspawning.rules.DynamicRule;
import mods.thecomputerizer.specifiedspawning.rules.RuleManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid=Constants.MODID)
public class Events {

    @SubscribeEvent
    public static void onGetPotentialSpawns(WorldEvent.PotentialSpawns ev) {
        if(ev.getWorld() instanceof WorldServer) {
            WorldServer world = (WorldServer)ev.getWorld();
            BlockPos pos = ev.getPos();
            if(SpecifiedSpawning.isModLoaded("scalingdifficulty")) SHHooks.cachePlayerData(pos,world);
            ev.getList().removeIf(entry -> {
                if(!RuleManager.hasCachedRules(entry)) return false;
                boolean ret = false;
                for(DynamicRule rule : RuleManager.getCachedRules(entry)) {
                    if(rule.checkSelectors(pos,world)) return rule.isRemoval();
                    else ret = !rule.isRemoval();
                }
                return ret;
            });
        }
    }
}

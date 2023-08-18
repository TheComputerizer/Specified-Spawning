package mods.thecomputerizer.specifiedspawning.world;

import mods.thecomputerizer.specifiedspawning.Constants;
import mods.thecomputerizer.specifiedspawning.rules.DynamicRule;
import mods.thecomputerizer.specifiedspawning.rules.RuleManager;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid= Constants.MODID)
public class Events {

    //@SubscribeEvent
    public static void onGetPotentialSpawns(WorldEvent.PotentialSpawns ev) {
        if(ev.getWorld() instanceof WorldServer) {
            WorldServer world = (WorldServer)ev.getWorld();
            int dimension = world.provider.getDimension();
            int yPos = ev.getPos().getY();
            int light = world.getLight(ev.getPos(),true);
            ev.getList().removeIf(entry -> {
                if(!RuleManager.hasCachedRules(entry)) return false;
                boolean ret = false;
                for(DynamicRule rule : RuleManager.getCachedRules(entry)) {
                    if(rule.checkDimension(dimension) && rule.checkGamestages(world) && rule.checkHeight(yPos) &&
                            rule.checkLight(light)) return rule.isRemoval();
                    else ret = !rule.isRemoval();
                }
                return ret;
            });
        }
    }
}

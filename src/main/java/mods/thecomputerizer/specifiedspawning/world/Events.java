package mods.thecomputerizer.specifiedspawning.world;

import mods.thecomputerizer.specifiedspawning.SpecifiedSpawning;
import mods.thecomputerizer.specifiedspawning.mixin.access.ISpawnGroupObject;
import mods.thecomputerizer.specifiedspawning.rules.DynamicRule;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.world.WorldEvent.PotentialSpawns;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static mods.thecomputerizer.specifiedspawning.core.Constants.MODID;

@EventBusSubscriber(modid=MODID)
public class Events {

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
}
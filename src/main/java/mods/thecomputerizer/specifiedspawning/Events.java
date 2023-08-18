package mods.thecomputerizer.specifiedspawning;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid=Constants.MODID)
public class Events {

    @SubscribeEvent
    public static void onCheckSpawn(LivingSpawnEvent.CheckSpawn ev) {
        if(!ev.isSpawner()) {
            Biome biome = ev.getWorld().getBiome(new BlockPos(ev.getX(),ev.getY(),ev.getZ()));
        }
    }
}

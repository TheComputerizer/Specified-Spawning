package mods.thecomputerizer.specifiedspawning.world;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.silentchaos512.scalinghealth.config.Config;
import net.silentchaos512.scalinghealth.utils.SHPlayerDataHandler;

import javax.annotation.Nullable;
import java.util.Objects;

public class SHHooks {

    private static boolean NEEDS_CHECKING = false;
    private static @Nullable SHPlayerDataHandler.PlayerData CACHED_PLAYER_DATA;

    public static void cachePlayerData(BlockPos pos, WorldServer world) {
        if(NEEDS_CHECKING) {
            EntityPlayer player = world.getClosestPlayer(pos.getX(), pos.getY(), pos.getZ(), 120, false);
            if (Objects.nonNull(player)) CACHED_PLAYER_DATA = SHPlayerDataHandler.get(player);
        }
    }

    public static SHPlayerDataHandler.PlayerData getCachedData() {
        return CACHED_PLAYER_DATA;
    }

    public static void setLoadedScalingDifficultySelector(boolean loaded) {
        NEEDS_CHECKING = loaded;
    }

    public static float getMaxDifficulty() {
        return Config.Difficulty.maxValue;
    }
}

package mods.thecomputerizer.specifiedspawning.rules.selectors.scalinghealth;

import mods.thecomputerizer.specifiedspawning.rules.selectors.AbstractSelector;
import mods.thecomputerizer.specifiedspawning.rules.selectors.SelectorType;
import mods.thecomputerizer.specifiedspawning.world.SHHooks;
import mods.thecomputerizer.theimpossiblelibrary.api.toml.Toml;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.silentchaos512.scalinghealth.utils.SHPlayerDataHandler;

import javax.annotation.Nullable;
import java.util.Objects;

import static mods.thecomputerizer.specifiedspawning.rules.selectors.SelectorType.SCALINGDIFFICULTY;

public class ScalingDifficultySelector extends AbstractSelector {
    
    public static ScalingDifficultySelector makeSelector(Toml table) {
        if(Objects.isNull(table)) return null;
        return new ScalingDifficultySelector(table.getValueBool("inverted",false),
                table.getValueFloat("min",0f),
                table.getValueFloat("max",SHHooks.getMaxDifficulty()));
    }

    private final float min;
    private final float max;
    private @Nullable SHPlayerDataHandler.PlayerData cachedPlayerData;

    protected ScalingDifficultySelector(boolean isInverted, float min, float max) {
        super(isInverted);
        this.min = min;
        this.max = max;
    }

    public void setPlayerData(@Nullable SHPlayerDataHandler.PlayerData data) {
        this.cachedPlayerData = data;
    }

    @Override protected boolean isValidInner(BlockPos pos, World world, String ruleDescriptor) {
        return Objects.nonNull(this.cachedPlayerData) && this.min<=this.cachedPlayerData.getDifficulty() &&
                this.max>=this.cachedPlayerData.getDifficulty();
    }

    @Override public boolean isNonBasic() {
        return true;
    }

    @Override public SelectorType getType() {
        return SCALINGDIFFICULTY;
    }
}
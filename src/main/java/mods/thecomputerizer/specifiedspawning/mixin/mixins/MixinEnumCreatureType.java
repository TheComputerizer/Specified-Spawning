package mods.thecomputerizer.specifiedspawning.mixin.mixins;

import mods.thecomputerizer.specifiedspawning.world.SpawnManager;
import net.minecraft.entity.EnumCreatureType;
import org.spongepowered.asm.mixin.*;

@Mixin(EnumCreatureType.class)
public abstract class MixinEnumCreatureType {

    @Shadow @Final private int maxNumberOfCreature;

    /**
     * @author The_Computerizer
     * @reason Allow for dynamic mob caps so the game does not have to be restarted for spawn groups to be reset.
     */
    @Overwrite
    public int getMaxNumberOfCreature() {
        return SpawnManager.getDynamicMaxNumberOfCreature((EnumCreatureType)(Object)this,this.maxNumberOfCreature);
    }
}

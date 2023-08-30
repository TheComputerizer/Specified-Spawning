package mods.thecomputerizer.specifiedspawning.rules.selectors.vanilla;

import mods.thecomputerizer.specifiedspawning.rules.selectors.ResourceSelector;
import mods.thecomputerizer.specifiedspawning.rules.selectors.SelectorType;
import mods.thecomputerizer.theimpossiblelibrary.common.toml.Table;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class SpawnBlockSelector extends ResourceSelector<Block> {
    public static SpawnBlockSelector makeSelector(Table table) {
        if(Objects.isNull(table)) return null;
        return new SpawnBlockSelector(table.getValOrDefault("inverted",false),
                table.getValOrDefault("mod",""),table.getValOrDefault("block",""),
                table.getValOrDefault("matcher",""));
    }

    private final Set<Block> cachedBlocks;

    protected SpawnBlockSelector(boolean isInverted, String mod, String block, String matcher) {
        super(isInverted,mod,block,matcher);
        this.cachedBlocks = new HashSet<>();
        this.cachedBlocks.add(Blocks.AIR);
    }

    @Override
    protected boolean isValidInner(BlockPos pos, World world, String ruleDescriptor) {
        Block block = world.getBlockState(pos).getBlock();
        Block block1 = world.getBlockState(pos.down()).getBlock();
        return this.cachedBlocks.contains(block) || this.cachedBlocks.contains(block1);
    }

    @Override
    public boolean isResourceValid(Block block, String ruleDescriptor) {
        if(Objects.isNull(block)) return false;
        boolean ret = isResourceValid(ForgeRegistries.BLOCKS.getKey(block),"block",ruleDescriptor);
        if(ret) this.cachedBlocks.add(block);
        return ret;
    }

    @Override
    public boolean isNonBasic() {
        return true;
    }

    @Override
    public SelectorType getType() {
        return SelectorType.SPAWNBLOCK;
    }
}
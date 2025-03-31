package mods.thecomputerizer.specifiedspawning.rules.selectors.vanilla;

import mods.thecomputerizer.specifiedspawning.rules.selectors.AbstractSelector;
import mods.thecomputerizer.specifiedspawning.rules.selectors.SelectorType;
import mods.thecomputerizer.specifiedspawning.util.ParsingUtils;
import mods.thecomputerizer.theimpossiblelibrary.api.toml.Toml;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import java.util.List;
import java.util.Objects;

import static mods.thecomputerizer.specifiedspawning.rules.selectors.SelectorType.STRUCTURE;

public class StructureSelector extends AbstractSelector {
    
    public static StructureSelector makeSelector(Toml table) {
        if(Objects.isNull(table)) return null;
        return new StructureSelector(table.getValueBool("inverted",false),
                ParsingUtils.getStringList(table.getEntryValuesAsMap().get("structure")));
    }

    private final List<String> structNames;

    protected StructureSelector(boolean isInverted, List<String> structNames) {
        super(isInverted);
        this.structNames = structNames;
    }

    @Override protected boolean isValidInner(BlockPos pos, World world, String ruleDescriptor) {
        for(String structure : this.structNames)
            if(((WorldServer)world).getChunkProvider().isInsideStructure(world,structure,pos)) return true;
        return false;
    }

    @Override public boolean isNonBasic() {
        return true;
    }

    @Override public SelectorType getType() {
        return STRUCTURE;
    }
}
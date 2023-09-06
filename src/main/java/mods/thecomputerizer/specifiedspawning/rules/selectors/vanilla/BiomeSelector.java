package mods.thecomputerizer.specifiedspawning.rules.selectors.vanilla;

import mods.thecomputerizer.specifiedspawning.rules.selectors.ResourceSelector;
import mods.thecomputerizer.specifiedspawning.rules.selectors.SelectorType;
import mods.thecomputerizer.theimpossiblelibrary.common.toml.Table;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.Objects;

public class BiomeSelector extends ResourceSelector<Biome> {

    public static BiomeSelector makeSelector(Table table) {
        if(Objects.isNull(table)) return null;
        return new BiomeSelector(table.getValOrDefault("inverted",false),
                table.getValOrDefault("mod",""),table.getValOrDefault("biome",""),
                table.getValOrDefault("matcher",""));
    }

    private BiomeSelector(boolean isInverted, String mod, String biomeID, String matcher) {
        super(isInverted,mod,biomeID,matcher);
    }

    @Override
    public boolean isResourceValid(Biome biome, String ruleDescriptor) {
        if(Objects.isNull(biome)) return false;
        return isResourceValid(ForgeRegistries.BIOMES.getKey(biome),"biome",ruleDescriptor);
    }

    @Override
    public boolean isNonBasic() {
        return false;
    }

    @Override
    public SelectorType getType() {
        return SelectorType.BIOME;
    }
}

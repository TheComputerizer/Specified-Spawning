package mods.thecomputerizer.specifiedspawning.rules.selectors.vanilla;

import mods.thecomputerizer.specifiedspawning.rules.selectors.ResourceSelector;
import mods.thecomputerizer.specifiedspawning.rules.selectors.SelectorType;
import mods.thecomputerizer.theimpossiblelibrary.api.toml.Toml;
import net.minecraft.world.biome.Biome;

import java.util.Objects;

import static mods.thecomputerizer.specifiedspawning.rules.selectors.SelectorType.BIOME;
import static net.minecraftforge.fml.common.registry.ForgeRegistries.BIOMES;

public class BiomeSelector extends ResourceSelector<Biome> {

    public static BiomeSelector makeSelector(Toml table) {
        if(Objects.isNull(table)) return null;
        return new BiomeSelector(table.getValueBool("inverted",false),
                table.hasTable("mod") ? table.getValueString("mod") : "",
                table.hasTable("biome") ? table.getValueString("biome") : "",
                table.hasTable("matcher") ? table.getValueString("matcher") : "");
    }

    private BiomeSelector(boolean isInverted, String mod, String biomeID, String matcher) {
        super(isInverted,mod,biomeID,matcher);
    }

    @Override public boolean isResourceValid(Biome biome, String ruleDescriptor) {
        if(Objects.isNull(biome)) return false;
        return isResourceValid(BIOMES.getKey(biome),"biome",ruleDescriptor);
    }

    @Override public boolean isNonBasic() {
        return false;
    }

    @Override public SelectorType getType() {
        return BIOME;
    }
}
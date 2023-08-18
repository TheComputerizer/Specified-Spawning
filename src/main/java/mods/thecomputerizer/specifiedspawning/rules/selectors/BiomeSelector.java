package mods.thecomputerizer.specifiedspawning.rules.selectors;

import mods.thecomputerizer.theimpossiblelibrary.common.toml.Table;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.Objects;

public class BiomeSelector extends ResourceSelector implements ISelector<Biome> {

    public static BiomeSelector makeSelector(Table table) {
        if(Objects.isNull(table)) return null;
        return new BiomeSelector(table.getValOrDefault("mod",""),
                table.getValOrDefault("biome",""),table.getValOrDefault("matcher",""));
    }

    private BiomeSelector(String mod, String biomeID, String matcher) {
        super(mod,biomeID,matcher);
    }

    @Override
    public boolean isValid(Biome biome) {
        if(Objects.isNull(biome)) return false;
        return isResourceValid(ForgeRegistries.BIOMES.getKey(biome));
    }

    @Override
    public boolean isBasic() {
        return true;
    }
}

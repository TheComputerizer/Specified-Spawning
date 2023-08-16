package mods.thecomputerizer.specifiedspawning.rules.selectors;

import mods.thecomputerizer.theimpossiblelibrary.common.toml.Table;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.Objects;

public class BiomeSelector implements ISelector<Biome> {

    public static BiomeSelector makeSelector(Table table) {
        return new BiomeSelector(table.getValOrDefault("mod",""),
                table.getValOrDefault("biome",""),table.getValOrDefault("matcher",""));
    }

    private final String mod;
    private final String biomeID;
    private final String matcher;

    private BiomeSelector(String mod, String biomeID, String matcher) {
        this.mod = mod.isEmpty() ? null : mod;
        this.biomeID = biomeID.isEmpty() ? null : biomeID;
        this.matcher = matcher.isEmpty() ? null : matcher;
    }

    public boolean isValid(Biome biome) {
        if(Objects.isNull(biome)) return false;
        ResourceLocation res = ForgeRegistries.BIOMES.getKey(biome);
        if(Objects.isNull(res)) return false;
        return (Objects.isNull(this.mod) || res.getNamespace().matches(this.mod)) &&
                (Objects.isNull(this.biomeID) || res.toString().matches(this.biomeID)) &&
                (Objects.isNull(this.matcher) || res.toString().contains(this.matcher));
    }
}

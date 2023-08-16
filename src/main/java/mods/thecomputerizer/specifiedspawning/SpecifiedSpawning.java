package mods.thecomputerizer.specifiedspawning;

import mods.thecomputerizer.theimpossiblelibrary.util.file.FileUtil;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Mod(modid = Constants.MODID, name = Constants.NAME, version = Constants.VERSION, dependencies = Constants.DEPENDENCIES)
public class SpecifiedSpawning {

    public SpecifiedSpawning() {

    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        String path = "config/" + Constants.MODID + ".toml";
        try {
            SpawnManager.parseConfig(getOrMakeConfigFile(path));
        } catch (IOException ex) {
            Constants.LOGGER.error("Failed to parse rule file at {}!",path,ex);
        }
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {

    }

    private File getOrMakeConfigFile(String path) {
        File file = new File(path);
        if(!file.exists()) FileUtil.writeLinesToFile(file,getHeaderLines(),false);
        return file;
    }

    private List<String> getHeaderLines() {
        return Arrays.asList("# Please refer to the wiki page located at https://github.com/TheComputerizer/Specified-Spawning/wiki",
                "# or the discord server located at https://discord.gg/FZHXFYp8fc",
                "# for any specific questions you might have regarding this config file","");
    }
}

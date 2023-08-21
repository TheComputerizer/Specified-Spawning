package mods.thecomputerizer.specifiedspawning.world;

import mods.thecomputerizer.specifiedspawning.core.Constants;
import mods.thecomputerizer.specifiedspawning.SpecifiedSpawning;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

import javax.annotation.Nonnull;

public class ReloadCommand extends CommandBase {
    @Override
    @Nonnull
    public String getName() {
        return Constants.MODID;
    }

    @Override
    @Nonnull
    public String getUsage(@Nonnull ICommandSender sender) {
        return "Specified Spawning commands initiated";
    }

    @Override
    public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args) {
        if(args.length==1 && args[0].matches("reload")) {
            SpecifiedSpawning.reload();
            notifyCommandListener(sender, this, "command.specifiedspawning.success");
        }
        else notifyCommandListener(sender, this, "command.specifiedspawning.help");
    }
}

package mods.thecomputerizer.specifiedspawning.world;

import mods.thecomputerizer.specifiedspawning.core.Constants;
import mods.thecomputerizer.specifiedspawning.SpecifiedSpawning;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.server.MinecraftServer;

import javax.annotation.Nonnull;
import java.util.Objects;

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
        if(args.length==0) notify(sender, "help");
        else if(args[0].matches("reload")) {
            SpecifiedSpawning.reload();
            notify(sender, "success");
        }
        else if(args[0].matches("test")) {
            if(args.length>1) {
                if(args[1].matches("enum")) printEnumValues(sender,EnumCreatureType.values());
                else notify(sender,"test.help");
            } else notify(sender,"test.help");
        } else notify(sender, "help");
    }

    private <T extends Enum<T>> void printEnumValues(@Nonnull ICommandSender sender, @Nonnull T[] values) {
        if(values.length==0) notify(sender,"test.enum.empty");
        else {
            T first = values[0];
            if(Objects.isNull(first)) notify(sender,"test.enum.error");
            else {
                String className = first.getClass().getName();
                notify(sender,"test.enum.success",className,first);
                if(values.length>1)
                    for(int i=1;i<values.length;i++)
                        notify(sender,"test.enum.success",className,values[i]);
            }
        }
    }

    private void notify(@Nonnull ICommandSender sender, @Nonnull String langKeyEnding, Object ... langArgs) {
        notifyCommandListener(sender,this,buildLang(langKeyEnding),langArgs);
    }

    private String buildLang(@Nonnull String langKeyEnding) {
        return "command."+Constants.MODID+"."+langKeyEnding;
    }
}

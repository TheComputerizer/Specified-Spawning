package mods.thecomputerizer.specifiedspawning.world;

import mcp.MethodsReturnNonnullByDefault;
import mods.thecomputerizer.specifiedspawning.SpecifiedSpawning;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static mods.thecomputerizer.specifiedspawning.core.Constants.MODID;

@MethodsReturnNonnullByDefault @ParametersAreNonnullByDefault
public class ReloadCommand extends CommandBase {
    
    @Override public String getName() {
        return MODID;
    }

    @Override public String getUsage(ICommandSender sender) {
        return "Specified Spawning commands initiated";
    }

    @Override public void execute(MinecraftServer server, ICommandSender sender, String ... args) {
        if(args.length==0) {
            notify(sender,"help");
            return;
        }
        switch(args[0]) {
            case "reload": {
                SpecifiedSpawning.reload();
                notify(sender,"success");
                return;
            }
            case "test": {
                if(args.length>1) {
                    if(args[1].matches("enum")) printEnumValues(sender,EnumCreatureType.values());
                    else notify(sender,"test.help");
                } else notify(sender,"test.help");
                return;
            }
            default: notify(sender,"help");
        }
    }

    private <T extends Enum<T>> void printEnumValues(ICommandSender sender, T[] values) {
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
        return "command."+MODID+"."+langKeyEnding;
    }

    @Override public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender,
            String[] args, @Nullable BlockPos targetPos) {
        if(args.length==1) return Arrays.asList("reload","test");
        else if(args.length==2 && "test".equals(args[0])) return Collections.singletonList("enum");
        return Collections.emptyList();
    }
}

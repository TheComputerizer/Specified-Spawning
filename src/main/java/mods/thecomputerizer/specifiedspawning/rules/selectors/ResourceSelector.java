package mods.thecomputerizer.specifiedspawning.rules.selectors;

import mods.thecomputerizer.specifiedspawning.core.Constants;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.logging.log4j.Level;

import java.util.Objects;
import java.util.function.Function;

public abstract class ResourceSelector<T> extends AbstractSelector {

    private final String mod;
    private final String regID;
    private final String matcher;

    protected ResourceSelector(boolean isInverted, String mod, String regID, String matcher) {
        super(isInverted);
        this.mod = mod.isEmpty() ? null : mod;
        this.regID = regID.isEmpty() ? null : regID;
        this.matcher = matcher.isEmpty() ? null : matcher;
        Constants.logVerbose(Level.DEBUG,"Instantiated new Resource Selector with mod '{}', registry id '{}', and " +
                "matcher '{}'",this.mod,this.regID,this.matcher);
    }

    protected boolean isValidInner(BlockPos pos, World world, String ruleDescriptor) {
        return true;
    }

    public abstract boolean isResourceValid(T obj, String ruleDescriptor);

    protected boolean isResourceValid(ResourceLocation res, String fromType, String ruleDescriptor) {
        if(Objects.isNull(res)) return false;
        int status = calculateStatus(1,res,this.mod,res1 -> res1.getNamespace().matches(this.mod));
        if(status==2) return logValid(true,res,fromType,ruleDescriptor);
        status = calculateStatus(status,res,this.regID,res1 -> res1.toString().matches(this.regID));
        if(status==2) return logValid(true,res,fromType,ruleDescriptor);
        status = calculateStatus(status,res,this.matcher,res1 -> res1.toString().contains(this.matcher));
        return logValid(status<=2,res,fromType,ruleDescriptor);
    }

    private boolean logValid(boolean result, ResourceLocation res, String fromType, String ruleDescriptor) {
        if(result) Constants.logVerbose(Level.INFO,"Verified {} with id {} for a {} rule",fromType,res,ruleDescriptor);
        return result;
    }

    private int calculateStatus(int prev,ResourceLocation res,String type,Function<ResourceLocation,Boolean> func) {
        if(prev==2) return 2;
        int status = Objects.isNull(type) ? 1 : (func.apply(res) ? 2 : 3);
        return status==1 ? prev : status;
    }
}

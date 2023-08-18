package mods.thecomputerizer.specifiedspawning.rules.selectors;

import net.minecraft.util.ResourceLocation;

import java.util.Objects;
import java.util.function.Function;

public abstract class ResourceSelector {

    private final String mod;
    private final String regID;
    private final String matcher;

    protected ResourceSelector(String mod, String regID, String matcher) {
        this.mod = mod.isEmpty() ? null : mod;
        this.regID = regID.isEmpty() ? null : regID;
        this.matcher = matcher.isEmpty() ? null : matcher;
    }

    protected boolean isResourceValid(ResourceLocation res) {
        if(Objects.isNull(res)) return false;
        int status = calculateStatus(1,res,this.mod,res1 -> res1.getNamespace().matches(this.mod));
        if(status==2) return true;
        status = calculateStatus(status,res,this.regID,res1 -> res1.toString().matches(this.regID));
        if(status==2) return true;
        status = calculateStatus(status,res,this.matcher,res1 -> res1.toString().contains(this.matcher));
        return status<=2;
    }

    private int calculateStatus(int prev,ResourceLocation res,String type,Function<ResourceLocation,Boolean> func) {
        if(prev==2) return 2;
        int status = Objects.isNull(type) ? 1 : (func.apply(res) ? 2 : 3);
        return status==1 ? prev : status;
    }
}

package cxmc.lua;

import java.util.concurrent.TimeUnit;

import org.bukkit.entity.Player;

import cxmc.LuaScript;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.data.DataMutateResult;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import net.milkbowl.vault.permission.Permission;

public class LuaPermHandler {
    private LuaScript instance;
    private LuckPerms luckPerms;
    private Permission Vpermission;
    public LuaPermHandler(LuaScript instance){
        this.instance = instance;
        this.Vpermission = instance.getVPermission();
        this.luckPerms = instance.getlLuckPerms();
        if(luckPerms == null){
            this.instance.getLogger().warning("Permission support disabled.(only LuckPerms now are supported.)");
        }

    }

    public User loadUser(Player player) {
        if (!player.isOnline()) {
            throw new IllegalStateException("Player is offline");
        }
        
        return luckPerms.getUserManager().getUser(player.getUniqueId());
    }

    public boolean HasPermission(Player player,String perm){
        return player.hasPermission(perm);
    }
    public boolean InGroup(Player player,String group){
        if(this.luckPerms != null){
            return player.hasPermission("group."+group);
        }
        return false;
    }
    public void addPermission(Player player,String perm){
        if(this.luckPerms != null){
            User user = loadUser(player);
            DataMutateResult result = user.data().add(Node.builder(perm).build());
            if(!result.wasSuccessful()){
                instance.getLogger().warning("Error changing permission.");
            }
            luckPerms.getUserManager().saveUser(user);
        }
    }
    public void removePermisson(Player player,String perm){
        if(this.luckPerms != null){
            User user = loadUser(player);
            DataMutateResult result = user.data().remove(Node.builder(perm).build());
            if(!result.wasSuccessful()){
                instance.getLogger().warning("Error changing permission.");
            }
            luckPerms.getUserManager().saveUser(user);
        }
    }
    public void addTempPermisson(Player player,String perm,int seconds){
        if(this.luckPerms != null){
            User user = loadUser(player);
            DataMutateResult result = user.data().remove(Node.builder(perm).expiry(seconds,TimeUnit.SECONDS).build());
            if(!result.wasSuccessful()){
                instance.getLogger().warning("Error changing permission.");
            }
            luckPerms.getUserManager().saveUser(user);
        }
    }
}
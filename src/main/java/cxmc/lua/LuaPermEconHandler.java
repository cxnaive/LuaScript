package cxmc.lua;

import java.util.concurrent.TimeUnit;

import org.bukkit.entity.Player;

import cxmc.LuaScript;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.data.DataMutateResult;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import net.milkbowl.vault.permission.Permission;

public class LuaPermEconHandler {
    private LuaScript instance;
    private LuckPerms luckPerms;
    private Permission Vpermission;
    private Economy Veconomy;
    public LuaPermEconHandler(LuaScript instance){
        this.instance = instance;
        this.Vpermission = instance.getVPermission();
        this.luckPerms = instance.getlLuckPerms();
        this.Veconomy = instance.getVEconomy();
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
        else if(this.Vpermission != null){
            return this.Vpermission.playerInGroup(player, group);
        }
        return false;
    }
    public boolean addPermission(Player player,String perm){
        boolean res = false;
        if(this.luckPerms != null){
            User user = loadUser(player);
            DataMutateResult result = user.data().add(Node.builder(perm).build());
            res = result.wasSuccessful();
            luckPerms.getUserManager().saveUser(user);
        }
        else if(this.Vpermission != null){
            res = this.Vpermission.playerAdd(null,player, perm);
        }
        if(!res) instance.getLogger().warning("Error changing permission.");
        return res;
    }
    public boolean removePermisson(Player player,String perm){
        boolean res = false;
        if(this.luckPerms != null){
            User user = loadUser(player);
            DataMutateResult result = user.data().remove(Node.builder(perm).build());
            res = result.wasSuccessful();
            luckPerms.getUserManager().saveUser(user);
        }
        else if(this.Vpermission != null){
            res = this.Vpermission.playerRemove(null, player, perm);
        }
        if(!res) instance.getLogger().warning("Error changing permission.");
        return res;
    }
    public boolean addTempPermisson(Player player,String perm,int seconds){
        if(this.luckPerms != null){
            User user = loadUser(player);
            DataMutateResult result = user.data().remove(Node.builder(perm).expiry(seconds,TimeUnit.SECONDS).build());
            if(!result.wasSuccessful()){
                instance.getLogger().warning("Error changing permission.");
            }
            luckPerms.getUserManager().saveUser(user);
            return result.wasSuccessful();
        }
        else if(this.Vpermission != null){
            instance.getLogger().warning("temporary permission only supports LuckPerms.");
        }
        return false;
    }
    public double getBalance(Player player){
        if(this.Veconomy != null){
            return this.Veconomy.getBalance(player);
        }
        return 0;
    }
    public boolean depositPlayer(Player player,double amount){
        if(this.Veconomy != null){
            EconomyResponse result = this.Veconomy.depositPlayer(player, amount);
            if(!result.transactionSuccess()){
                instance.getLogger().warning("Error: "+result.errorMessage);
                return false;
            }
            return true;
        }
        return false;
    }
    public boolean withdrawPlayer(Player player,double amount){
        if(this.Veconomy != null){
            EconomyResponse result = this.Veconomy.withdrawPlayer(player, amount);
            if(!result.transactionSuccess()){
                instance.getLogger().warning("Error: "+result.errorMessage);
                return false;
            }
            return true;
        }
        return false;
    }
}
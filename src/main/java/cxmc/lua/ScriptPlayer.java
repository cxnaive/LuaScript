package cxmc.lua;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import cxmc.LuaScript;

public class ScriptPlayer {
    private Player player;
    private LuaScript instance;
    public ScriptPlayer(Player player,LuaScript instance){
        this.player = player;
        this.instance = instance;
    }
    public String getDisplayname(){
        return player.getDisplayName();
    }
    public String getName(){
        return player.getName();
    }
    public boolean performCommand(String cmd){
        return Bukkit.getServer().dispatchCommand(player,cmd);
    }
    public float getExhaustion​(){
        return player.getExhaustion();
    }
    public int getLevel(){
        return player.getLevel();
    }
    public int getFoodLevel(){
        return player.getFoodLevel();
    }
    public double getHealthScale(){
        return player.getHealthScale();
    }
    public boolean isSneaking(){
        return player.isSneaking();
    }
    public boolean isSprinting(){
        return player.isSprinting();
    }
    public boolean hasPermission(String perm){
        return instance.getluaPermHandler().HasPermission(player, perm);
    }
    public boolean InGroup(String group){
        return instance.getluaPermHandler().InGroup(player, group);
    }
    public void AddPermission(String perm){
        instance.getluaPermHandler().addPermission(player, perm);
    }
    public void RemovePermission(String perm){
        instance.getluaPermHandler().removePermisson(player, perm);
    }
    public void chat(String msg){
        Bukkit.getServer().dispatchCommand(player,"/say "+msg);
        //player.chat(msg);
    }
}
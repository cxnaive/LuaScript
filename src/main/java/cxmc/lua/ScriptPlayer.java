package cxmc.lua;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

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
    public void runcmd(String cmd){
        new BukkitRunnable(){
            @Override
            public void run() {
                player.performCommand(cmd);
            }
        }.runTask(instance);
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
    public float getFlySpeed(){
        return player.getFlySpeed();
    }
    public float getWalkSpeed(){
        return player.getWalkSpeed();
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
        new BukkitRunnable(){
            @Override
            public void run() {
                player.chat(msg);
            }
        }.runTask(instance);
        
    }
}
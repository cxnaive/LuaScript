package cxmc.lua;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;

import cxmc.LuaScript;
import cxmc.text.TextBuilder;

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
    public void runCmd(String cmd){
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
        return instance.getluaPermEconHandler().HasPermission(player, perm);
    }
    public boolean inGroup(String group){
        return instance.getluaPermEconHandler().InGroup(player, group);
    }
    public void addPermission(String perm){
        instance.getluaPermEconHandler().addPermission(player, perm);
    }
    public void removePermission(String perm){
        instance.getluaPermEconHandler().removePermisson(player, perm);
    }
    public void deposit(double amount){
        instance.getluaPermEconHandler().depositPlayer(player, amount);
    }
    public void cost(double amount){
        instance.getluaPermEconHandler().withdrawPlayer(player, amount);
    }
    public PlayerInventory getInventory(){
        return player.getInventory();
    }
    public Inventory getEnderChest(){
        return player.getEnderChest();
    }
    public double getBalance(){
        return instance.getluaPermEconHandler().getBalance(player);
    }
    public void sendMessage(Object value){
        TextBuilder msg = (TextBuilder)value;
        player.spigot().sendMessage(msg.build());
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
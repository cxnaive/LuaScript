package cxmc.cmd;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

import cxmc.LuaScript;
import cxmc.text.TextBuilder;
import net.md_5.bungee.api.ChatColor;


public class ClearWorldExecutor extends LuaScriptExecutor {
    private LuaScript instance;
    public ClearWorldExecutor(LuaScript instance){
        this.instance = instance;
    }
    @Override
    public List<String> LeafTabComplete(CommandSender sender,Queue<String> args){
        List<World> worlds = Bukkit.getWorlds();
        List<String> coms = new ArrayList<>();
        for(World world:worlds){
            coms.add(world.getName());
        }
        return coms;
    }
    @Override
    public boolean RunAsLeaf(CommandSender sender, String[] args) {
        if(args.length == 1){
            World world = Bukkit.getWorld(args[0]);
            if(world != null){
                instance.getLuaLoader().ClearWorld(world);;
                sender.spigot().sendMessage(Success);
            }
            else {
                sender.spigot().sendMessage(TextBuilder.of("Invalid world name.").setColor(ChatColor.RED).build());
            }
        }
        else{
            sender.spigot().sendMessage(ArgNumErr);
        }
        return true;
    }
}
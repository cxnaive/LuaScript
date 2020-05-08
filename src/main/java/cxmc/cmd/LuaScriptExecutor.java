package cxmc.cmd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import org.bukkit.command.CommandSender;

import cxmc.text.TextBuilder;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;

public abstract class LuaScriptExecutor{
    HashMap<String,LuaScriptExecutor> SubCommands;
    List<String> TabCompleteStrings;
    String permission;

    public static final TextComponent PlayerErr = TextBuilder.of("You need to specify an online player!").setColor(ChatColor.RED).build();
    public static final TextComponent ArgNumErr = TextBuilder.of("Invalid argument number.").setColor(ChatColor.RED).build();
    public static final TextComponent Success = TextBuilder.of("Success!").setColor(ChatColor.GREEN).build();
    public LuaScriptExecutor(){
        SubCommands = new HashMap<>();
        TabCompleteStrings = new ArrayList<>();
        this.permission = null;
    }
    public void SetPermission(String permission){
        this.permission = permission;
    }
    public void RegisterSubcommand(String name,LuaScriptExecutor executor){
        SubCommands.put(name, executor);
    }
    public List<String> GetTabComplete(CommandSender sender,Queue<String> args){
        if(args.size() > 1){
            String subname = args.poll();
            if(SubCommands.containsKey(subname)){
                LuaScriptExecutor sub = SubCommands.get(subname);
                return sub.GetTabComplete(sender, args);
            }
            else return new ArrayList<>();
        }
        String tempname = args.peek();
        if(tempname == null) tempname = "";
        Set<String> subnames = SubCommands.keySet(); 
        List<String> coms = LeafTabComplete(sender, args);
        for(String sub:subnames){
            if(sub.startsWith(tempname)){
                coms.add(sub);
            }
        }
        return coms;
    }
    public boolean RunCommand(CommandSender sender,Queue<String> args){
        String tempname = args.peek();
        if(tempname == null) tempname = "";
        if(SubCommands.containsKey(tempname)){
            args.poll();
            LuaScriptExecutor sub = SubCommands.get(tempname);
            return sub.RunCommand(sender, args); 
        }
        if(this.permission != null && !sender.hasPermission(this.permission)){
            sender.spigot().sendMessage(TextBuilder.of("You don't have the permission to execute this commmand.").setColor(ChatColor.RED).build());
            return false;
        }
        String[] rargs = new String[args.size()];
        for(int i = 0;i < rargs.length;++i){
            rargs[i] = args.poll();
        }
        return RunAsLeaf(sender, rargs);
    }
    public abstract boolean RunAsLeaf(CommandSender sender,String[] args);
    public List<String> LeafTabComplete(CommandSender sender,Queue<String> args){
        return new ArrayList<>();
    }
}
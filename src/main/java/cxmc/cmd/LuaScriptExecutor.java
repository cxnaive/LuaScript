package cxmc.cmd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import org.bukkit.command.CommandSender;

public abstract class LuaScriptExecutor{
    HashMap<String,LuaScriptExecutor> SubCommands;
    List<String> TabCompleteStrings;

    public LuaScriptExecutor(){
        SubCommands = new HashMap<>();
        TabCompleteStrings = new ArrayList<>();
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
        List<String> coms = new ArrayList<>();
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
        String[] rargs = new String[args.size()];
        for(int i = 0;i < args.size();++i){
            rargs[i] = args.poll();
        }
        return RunAsLeaf(sender, rargs);
    }
    public abstract boolean RunAsLeaf(CommandSender sender,String[] args);
}
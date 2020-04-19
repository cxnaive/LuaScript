package cxmc.cmd;


import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import cxmc.LuaScript;

public class RootExecutor extends LuaScriptExecutor implements TabCompleter,CommandExecutor {

    private LuaScript instance;
    public RootExecutor(LuaScript instance){
        super();
        this.instance = instance;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Queue<String> qargs = new LinkedList<>();
        for(int i = 0;i < args.length;++i){
            qargs.offer(args[i]);
        }
        return RunCommand(sender, qargs);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command,String alias, String[] args) {
        Queue<String> qargs = new LinkedList<>();
        for(int i = 0;i < args.length;++i){
            qargs.offer(args[i]);
        }
        return GetTabComplete(sender, qargs);
    }

    @Override
    public boolean RunAsLeaf(CommandSender sender, String[] args) {
        sender.sendMessage("LuaScript V"+instance.getVersion());
        return true;
    }
}
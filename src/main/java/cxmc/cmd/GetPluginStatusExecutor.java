package cxmc.cmd;



import org.bukkit.command.CommandSender;

import cxmc.LuaScript;


public class GetPluginStatusExecutor extends LuaScriptExecutor {
    private LuaScript instance;
    public GetPluginStatusExecutor(LuaScript instance){
        this.instance = instance;
    }
    @Override
    public boolean RunAsLeaf(CommandSender sender, String[] args) {
        if(args.length > 0){
            sender.spigot().sendMessage(ArgNumErr);
        }
        else{
            sender.spigot().sendMessage(instance.getPluginStat().info());
        }
        return true;
    }

}
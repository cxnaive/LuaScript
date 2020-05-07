package cxmc.cmd;



import org.bukkit.command.CommandSender;

import cxmc.LuaScript;
import cxmc.text.TextBuilder;


public class KillRunningExecutor extends LuaScriptExecutor {
    private LuaScript instance;
    public KillRunningExecutor(LuaScript instance){
        this.instance = instance;
    }
    @Override
    public boolean RunAsLeaf(CommandSender sender, String[] args) {
        if(args.length != 1){
            sender.spigot().sendMessage(ArgNumErr);
            return true;
        }
        String runID = args[0];
        if(instance.getLuaRunner().RunningIDs().contains(runID)){
            instance.getLuaRunner().kill(runID);
        }
        else{
            sender.spigot().sendMessage(TextBuilder.of("RunID not found!").build());
        }
        return true;
    }
}
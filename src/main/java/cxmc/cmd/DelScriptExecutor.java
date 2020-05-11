package cxmc.cmd;

import java.util.List;
import java.util.Queue;

import org.bukkit.command.CommandSender;

import cxmc.LuaScript;
import cxmc.text.TextBuilder;
import net.md_5.bungee.api.ChatColor;


public class DelScriptExecutor extends LuaScriptExecutor {
    private LuaScript instance;
    public DelScriptExecutor(LuaScript instance){
        this.instance = instance;
    }
    @Override
    public List<String> LeafTabComplete(CommandSender sender,Queue<String> args){
        return instance.getLuaLoader().GetScriptIDALL();
    }
    @Override
    public boolean RunAsLeaf(CommandSender sender, String[] args) {
        if(args.length == 1){
            String ScriptID = args[0];
            if(instance.getH2Manager().HasScript(ScriptID)){
                instance.getLuaLoader().DeleteScript(ScriptID);
            }
            else{
                sender.spigot().sendMessage(TextBuilder.of("Invalid Script ID.").setColor(ChatColor.AQUA).build());
            }
        }
        else{
            sender.spigot().sendMessage(ArgNumErr);
        }
        return true;
    }
}
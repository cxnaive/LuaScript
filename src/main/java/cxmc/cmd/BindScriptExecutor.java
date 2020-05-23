package cxmc.cmd;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import cxmc.LuaScript;
import cxmc.text.TextBuilder;
import net.md_5.bungee.api.ChatColor;


public class BindScriptExecutor extends LuaScriptExecutor {
    private LuaScript instance;
    public BindScriptExecutor(LuaScript instance){
        this.instance = instance;
    }
    @Override
    public List<String> LeafTabComplete(CommandSender sender,Queue<String> args){
        List<String> coms = new ArrayList<>();
        coms.addAll(instance.getLuaLoader().GetScriptIDALL());
        return coms;
    }
    
    @Override
    public boolean RunAsLeaf(CommandSender sender, String[] args) {
        if(!(sender instanceof Player)){
            sender.spigot().sendMessage(PlayerErr);
            return true;
        }
        Player player = (Player) sender;
        if(args.length == 1){
            String ScriptID = args[0];
            if(!instance.getH2Manager().HasScript(ScriptID)){
                sender.spigot().sendMessage(TextBuilder.of("Script ").setColor(ChatColor.AQUA)
                .append(TextBuilder.of(ScriptID).setColor(ChatColor.YELLOW).build())
                .append(TextBuilder.of(" does not exists.").setColor(ChatColor.AQUA).build())
                .build());
            }
            instance.getExtraDataLoader().setBindedScriptID(player, ScriptID);
        }
        else{
            sender.spigot().sendMessage(ArgNumErr);
        }
        return true;
    }

}
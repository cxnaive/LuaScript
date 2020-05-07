package cxmc.cmd;



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
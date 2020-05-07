package cxmc.cmd;



import org.bukkit.command.CommandSender;

import cxmc.LuaScript;
import cxmc.text.TextBuilder;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ClickEvent.Action;


public class LoadScriptExecutor extends LuaScriptExecutor {
    private LuaScript instance;
    public LoadScriptExecutor(LuaScript instance){
        this.instance = instance;
    }
    @Override
    public boolean RunAsLeaf(CommandSender sender, String[] args) {
        String ScriptID;
        String Content;
        if(args.length == 1){
            ScriptID = args[0];
            Content = instance.getFileLoader().ReadScript(args[0]);
            if(Content == null){
                sender.spigot().sendMessage(TextBuilder.of("Error loading script "+args[0]).setColor(ChatColor.RED).build());
            }
            if(instance.getH2Manager().HasScript(ScriptID)){
                sender.spigot().sendMessage(TextBuilder.of("Script "+ScriptID+" already exists.").setColor(ChatColor.YELLOW)
                .append(TextBuilder.of("Click Here").setColor(ChatColor.RED).setUnderlined(true).setClickEvent(new ClickEvent(Action.RUN_COMMAND, "/luas loadscript "+ScriptID+" -y")).build())
                .append(TextBuilder.of(" to replace the old one.").setColor(ChatColor.YELLOW).build())
                .build());
                return true;
            }
            instance.getLuaLoader().SetScript(ScriptID, Content);
        }
        else if(args.length == 2 && args[1].equalsIgnoreCase("-y")){
            ScriptID = args[0];
            Content = instance.getFileLoader().ReadScript(args[0]);
            if(Content == null){
                sender.spigot().sendMessage(TextBuilder.of("Error loading script "+args[0]).setColor(ChatColor.RED).build());
                return true;
            }
            instance.getLuaLoader().SetScript(ScriptID, Content);
        }
        else{
            sender.spigot().sendMessage(ArgNumErr);
        }
        return true;
    }

}
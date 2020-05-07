package cxmc.cmd;



import java.util.List;

import org.bukkit.command.CommandSender;

import cxmc.LuaScript;
import cxmc.text.TextBuilder;
import net.md_5.bungee.api.ChatColor;


public class ListScriptExecutor extends LuaScriptExecutor {
    private LuaScript instance;
    public ListScriptExecutor(LuaScript instance){
        this.instance = instance;
    }
    @Override
    public boolean RunAsLeaf(CommandSender sender, String[] args) {
        List<String> ScriptIDs = instance.getLuaLoader().GetScriptIDALL();
        TextBuilder builder = new TextBuilder("");
        for(String id:ScriptIDs){
            builder.append(TextBuilder.of(id+'\n').setColor(ChatColor.AQUA).build());
        }
        builder.append(TextBuilder.of(""+ScriptIDs.size()).setColor(ChatColor.GREEN).build());
        builder.append(TextBuilder.of(" scripts in total.").setColor(ChatColor.YELLOW).build());
        sender.spigot().sendMessage(builder.build());
        return true;
    }
}
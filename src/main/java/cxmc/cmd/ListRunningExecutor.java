package cxmc.cmd;




import java.util.Set;

import org.bukkit.command.CommandSender;

import cxmc.LuaScript;
import cxmc.text.TextBuilder;
import net.md_5.bungee.api.ChatColor;


public class ListRunningExecutor extends LuaScriptExecutor {
    private LuaScript instance;
    public ListRunningExecutor(LuaScript instance){
        this.instance = instance;
    }
    
    @Override
    public boolean RunAsLeaf(CommandSender sender, String[] args) {
        Set<String> RunningIDs = instance.getLuaRunner().RunningIDs();
        TextBuilder builder = new TextBuilder("");
        for(String id:RunningIDs){
            builder.append(TextBuilder.of(id+'\n').setColor(ChatColor.AQUA).build());
        }
        builder.append(TextBuilder.of(""+RunningIDs.size()).setColor(ChatColor.GREEN).build());
        builder.append(TextBuilder.of(" running scripts in total.").setColor(ChatColor.YELLOW).build());
        sender.spigot().sendMessage(builder.build());
        return true;
    }
}
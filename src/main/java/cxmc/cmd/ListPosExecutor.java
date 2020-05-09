package cxmc.cmd;

import java.util.List;

import org.bukkit.command.CommandSender;

import cxmc.LuaScript;
import cxmc.essentials.Pair;
import cxmc.essentials.ScriptPos;
import cxmc.text.TextBuilder;
import net.md_5.bungee.api.ChatColor;


public class ListPosExecutor extends LuaScriptExecutor {
    private LuaScript instance;
    public ListPosExecutor(LuaScript instance){
        this.instance = instance;
    }
    @Override
    public boolean RunAsLeaf(CommandSender sender, String[] args) {
        List<Pair<ScriptPos,String>> Poses = instance.getLuaLoader().GetPosALL();
        TextBuilder builder = new TextBuilder("");
        for(Pair<ScriptPos,String> value:Poses){
            builder.append(TextBuilder.of(value.getKey().toString()+":").setColor(ChatColor.AQUA).append(TextBuilder.of(value.getValue()+"\n").setColor(ChatColor.YELLOW).build()).build());
        }
        builder.append(TextBuilder.of(""+Poses.size()).setColor(ChatColor.GREEN).build());
        builder.append(TextBuilder.of(" poses in total.").setColor(ChatColor.YELLOW).build());
        sender.spigot().sendMessage(builder.build());
        return true;
    }
}
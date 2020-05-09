package cxmc.cmd;


import org.bukkit.command.CommandSender;

import cxmc.LuaScript;
import cxmc.text.TextBuilder;
import net.md_5.bungee.api.ChatColor;


public class ClearPosExecutor extends LuaScriptExecutor {
    private LuaScript instance;
    public ClearPosExecutor(LuaScript instance){
        this.instance = instance;
    }
    @Override
    public boolean RunAsLeaf(CommandSender sender, String[] args) {
        instance.getLuaLoader().ClearPos();
        sender.spigot().sendMessage(TextBuilder.of("Sccessfully cleared all poses.").setColor(ChatColor.GREEN).build());
        return true;
    }
}
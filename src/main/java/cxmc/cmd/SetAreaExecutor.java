package cxmc.cmd;


import java.util.HashMap;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import cxmc.LuaScript;
import cxmc.extra.SelectArea;
import cxmc.lua.LuaLoader;
import cxmc.text.TextBuilder;
import net.md_5.bungee.api.ChatColor;


public class SetAreaExecutor extends LuaScriptExecutor {
    private LuaScript instance;
    public SetAreaExecutor(LuaScript instance){
        this.instance = instance;
    }
    @Override
    public boolean RunAsLeaf(CommandSender sender, String[] args) {
        if(sender instanceof Player){
            if(args.length != 1){
                sender.spigot().sendMessage(ArgNumErr);
                return true;
            }
            Player player = (Player)sender;
            SelectArea area = instance.getExtraDataLoader().getPlayerArea(player);
            if(area.state != 2){
                player.spigot().sendMessage(TextBuilder.of("You need to choose two points to define an area!").setColor(ChatColor.RED).build());
            }
            else if(area.a.world.getUID() != area.b.world.getUID()){
                player.spigot().sendMessage(TextBuilder.of("The two points must be in the same world!").setColor(ChatColor.RED).build());
            }
            else{
                instance.getLuaLoader().SetArea(area.a.getmin(area.b), area.a.getmax(area.b), args[0], LuaLoader.NullScript, new HashMap<>());
                player.spigot().sendMessage(TextBuilder.of("Successfully set area ").setColor(ChatColor.AQUA).append(TextBuilder.of(args[0]).setColor(ChatColor.GREEN).build()).build());
            }
        }
        else{
            sender.spigot().sendMessage(PlayerCmd);
        }
        return true;
    }
}
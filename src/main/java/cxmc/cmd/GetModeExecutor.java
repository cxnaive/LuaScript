package cxmc.cmd;



import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import cxmc.LuaScript;
import cxmc.extra.PlayerMode;
import cxmc.text.TextBuilder;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.HoverEvent.Action;


public class GetModeExecutor extends LuaScriptExecutor {
    private LuaScript instance;
    public GetModeExecutor(LuaScript instance){
        this.instance = instance;
    }
    @Override
    public boolean RunAsLeaf(CommandSender sender, String[] args) {
        Player player;
        if(args.length == 0){
            if(sender instanceof Player){
                player = (Player) sender;
            }
            else{
                sender.spigot().sendMessage(PlayerErr);
                return false;
            }
        }
        else if(args.length == 1){
            String name = args[0];
            player = Bukkit.getServer().getPlayerExact(name);
            if(player == null){
                sender.spigot().sendMessage(PlayerErr);
                return false;
            }
        }
        else{
            sender.spigot().sendMessage(ArgNumErr);
            return false;
        }
        TextComponent msg = instance.getExtraDataLoader().getPlayerMode(player).getText();
        if(msg.toPlainText().equalsIgnoreCase(PlayerMode.SetMode.name())){
            msg.setUnderlined(true);
            String ScriptID = instance.getExtraDataLoader().getBindedScriptID(player);
            if(ScriptID == null) ScriptID = "NULL";
            msg.setHoverEvent(new HoverEvent(Action.SHOW_TEXT,new ComponentBuilder( "Bind Script:" ).color(ChatColor.AQUA).append(TextBuilder.of(ScriptID).setColor(ChatColor.YELLOW).build()).create()));
        }
        sender.spigot().sendMessage(msg);
        return true;
    }

}
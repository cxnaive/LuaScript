package cxmc.cmd;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import cxmc.LuaScript;
import cxmc.extra.PlayerMode;
import cxmc.text.TextBuilder;
import net.md_5.bungee.api.ChatColor;
public class SetModeExecutor extends LuaScriptExecutor {
    private LuaScript instance;
    public SetModeExecutor(LuaScript instance){
        this.instance = instance;
    }
    @Override
    public boolean RunAsLeaf(CommandSender sender, String[] args) {
        Player player;
        String modename;
        if(args.length == 1){
            if(sender instanceof Player){
                player = (Player) sender;
            }
            else{
                sender.spigot().sendMessage(PlayerErr);
                return false;
            }
            modename = args[0];
        }
        else if(args.length == 2){
            String name = args[0];
            player = Bukkit.getServer().getPlayerExact(name);
            if(player == null){
                sender.spigot().sendMessage(PlayerErr);
                return false;
            }
            modename = args[1];
        }
        else{
            sender.spigot().sendMessage(ArgNumErr);
            return false;
        }
        PlayerMode mode = PlayerMode.getMode(modename);
        if(mode == null){
            sender.spigot().sendMessage(TextBuilder.of("Invalid mode name.").setColor(ChatColor.RED).build());
            return false;
        }
        instance.getExtraDataLoader().setPlayerMode(player, mode);
        sender.spigot().sendMessage(Success);
        return true;
    }
}
package cxmc.cmd;



import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import cxmc.LuaScript;


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

        sender.spigot().sendMessage(instance.getExtraDataLoader().getPlayerMode(player).getText());
        return true;
    }

}
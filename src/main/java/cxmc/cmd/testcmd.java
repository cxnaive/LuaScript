package cxmc.cmd;

import org.bukkit.command.CommandSender;

public class testcmd extends LuaScriptExecutor {

    @Override
    public boolean RunAsLeaf(CommandSender sender, String[] args) {
        sender.sendMessage("Test Cmd for luas!");
        return true;
    }

}
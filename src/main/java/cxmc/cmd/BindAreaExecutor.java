package cxmc.cmd;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import org.bukkit.command.CommandSender;


import cxmc.LuaScript;



public class BindAreaExecutor extends LuaScriptExecutor {
    private LuaScript instance;
    public BindAreaExecutor(LuaScript instance){
        this.instance = instance;
    }
    @Override
    public List<String> LeafTabComplete(CommandSender sender,Queue<String> args){
        return new ArrayList<>();
    }
    @Override
    public boolean RunAsLeaf(CommandSender sender, String[] args) {
        return true;
    }
}
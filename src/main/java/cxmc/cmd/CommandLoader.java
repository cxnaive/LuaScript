package cxmc.cmd;

import cxmc.LuaScript;

public class CommandLoader {
    public CommandLoader(LuaScript instance){
        GetModeExecutor getModeExecutor = new GetModeExecutor(instance);
        getModeExecutor.SetPermission("luascript.getmode");

        SetModeExecutor setModeExecutor = new SetModeExecutor(instance);
        setModeExecutor.SetPermission("luascript.setmode");

        RootExecutor RootCMD = new RootExecutor(instance);
        RootCMD.SetPermission("luascript.info");
        RootCMD.RegisterSubcommand("getmode", getModeExecutor);
        RootCMD.RegisterSubcommand("setmode", setModeExecutor);
        
        instance.getCommand("luascript").setExecutor(RootCMD);
        instance.getCommand("luascript").setTabCompleter(RootCMD);
    }
}
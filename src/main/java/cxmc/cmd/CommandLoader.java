package cxmc.cmd;

import cxmc.LuaScript;

public class CommandLoader {
    public CommandLoader(LuaScript instance){
        testcmd test = new testcmd();
        RootExecutor RootCMD = new RootExecutor(instance);
        RootCMD.RegisterSubcommand("test", test);
        instance.getCommand("luas").setExecutor(RootCMD);
        instance.getCommand("luas").setTabCompleter(RootCMD);
    }
}
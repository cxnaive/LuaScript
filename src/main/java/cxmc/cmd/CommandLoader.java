package cxmc.cmd;

import cxmc.LuaScript;

public class CommandLoader {
    public CommandLoader(LuaScript instance){
        GetModeExecutor getModeExecutor = new GetModeExecutor(instance);
        getModeExecutor.SetPermission("luascript.mode.get");

        GetPluginStatusExecutor getPluginStatus = new GetPluginStatusExecutor(instance);
        getPluginStatus.SetPermission("luascript.stat.get");

        SetModeExecutor setModeExecutor = new SetModeExecutor(instance);
        setModeExecutor.SetPermission("luascript.mode.set");

        BindScriptExecutor bindScriptExecutor = new BindScriptExecutor(instance);
        bindScriptExecutor.SetPermission("luascript.script.bind");

        LoadScriptExecutor loadScriptExecutor = new LoadScriptExecutor(instance);
        loadScriptExecutor.SetPermission("luascript.script.bind");

        ListScriptExecutor listScriptExecutor = new ListScriptExecutor(instance);
        listScriptExecutor.SetPermission("luascript.script.list");

        DelScriptExecutor delScriptExecutor = new DelScriptExecutor(instance);
        delScriptExecutor.SetPermission("luascript.script.delete");

        ListRunningExecutor listRunningExecutor = new ListRunningExecutor(instance);
        listRunningExecutor.SetPermission("luascript.running.list");

        KillRunningExecutor killRunningExecutor = new KillRunningExecutor(instance);
        killRunningExecutor.SetPermission("luascript.running.kill");

        ClearPosExecutor clearPosExecutor = new ClearPosExecutor(instance);
        clearPosExecutor.SetPermission("luascript.pos.clear");

        ClearWorldExecutor clearWorldExecutor = new ClearWorldExecutor(instance);
        clearWorldExecutor.SetPermission("luascript.pos.clear");

        ListPosExecutor listPosExecutor = new ListPosExecutor(instance);
        listPosExecutor.SetPermission("luascript.pos.list");

        SetAreaExecutor setAreaExecutor = new SetAreaExecutor(instance);
        setAreaExecutor.SetPermission("luascript.area.set");

        RootExecutor RootCMD = new RootExecutor(instance);
        RootCMD.SetPermission("luascript.info");
        RootCMD.RegisterSubcommand("getmode", getModeExecutor);
        RootCMD.RegisterSubcommand("setmode", setModeExecutor);
        RootCMD.RegisterSubcommand("getstat", getPluginStatus);
        RootCMD.RegisterSubcommand("bindscript", bindScriptExecutor);
        RootCMD.RegisterSubcommand("loadscript", loadScriptExecutor);
        RootCMD.RegisterSubcommand("listscript", listScriptExecutor);
        RootCMD.RegisterSubcommand("delscript", delScriptExecutor);
        RootCMD.RegisterSubcommand("listpos", listPosExecutor);
        RootCMD.RegisterSubcommand("listrunning", listRunningExecutor);
        RootCMD.RegisterSubcommand("killrunning", killRunningExecutor);
        RootCMD.RegisterSubcommand("clearpos", clearPosExecutor);
        RootCMD.RegisterSubcommand("clearworld", clearWorldExecutor);
        RootCMD.RegisterSubcommand("setarea", setAreaExecutor);

        instance.getCommand("luascript").setExecutor(RootCMD);
        instance.getCommand("luascript").setTabCompleter(RootCMD);
    }
}
package cxmc.config;

import org.bukkit.configuration.file.FileConfiguration;

import cxmc.LuaScript;

public class ConfigLoader {
    private LuaScript instance;
    private FileConfiguration config;
    public boolean EnableEconomy;
    public boolean DebugMode;
    public ConfigLoader(LuaScript instance){
        this.instance = instance;
        instance.saveDefaultConfig();
        config = instance.getConfig();
        EnableEconomy = config.getBoolean("EnableEconomy");
        DebugMode = config.getBoolean("DebugMode");
    }
    public void apply(){
        this.instance.getPluginStat().isDebugMode = DebugMode;
        this.instance.getPluginStat().isEconEnabled = EnableEconomy;
    }
    public void SaveConfig(){
        config.set("EnableEconomy", EnableEconomy);
        config.set("DebugMode",DebugMode);
        instance.saveConfig();
    }
}
package cxmc.config;

import org.bukkit.configuration.file.FileConfiguration;

import cxmc.LuaScript;

public class ConfigLoader {
    private LuaScript instance;
    private FileConfiguration config;
    public boolean EnableEconomy; 
    public ConfigLoader(LuaScript instance){
        this.instance = instance;
        instance.saveDefaultConfig();
        config = instance.getConfig();
        EnableEconomy = config.getBoolean("EnableEconomy");
    }
    public void SaveConfig(){
        config.set("EnableEconomy", EnableEconomy);
        instance.saveConfig();
    }
}
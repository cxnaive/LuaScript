package cxmc;
import org.bukkit.plugin.java.JavaPlugin;

import cxmc.cmd.CommandLoader;
import cxmc.config.ConfigLoader;
import cxmc.extra.ExtraDataLoader;
import cxmc.file.*;
import cxmc.lua.*;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import cxmc.h2.*;

public class LuaScript extends JavaPlugin implements PluginDef {
    private LuaLoader lualoader;
    private LuaRunner luarunner;
    private FileLoader fileLoader;
    private H2Manager h2Manager;
    private ConfigLoader configLoader;
    private ExtraDataLoader extraDataLoader;
    private Economy Veconomy;
    private Permission Vpermission;
    

    private final String version = "1.0";
    @Override
    public void onEnable() {
        getLogger().info("Loading Lua Environment");
        lualoader = new LuaLoader(this);
        luarunner = new LuaRunner();
        fileLoader = new FileLoader("LuaScript");
        getLogger().info("Loading Config");
        configLoader = new ConfigLoader(this);
        getLogger().info("Loading H2 database.");
        h2Manager = new H2Manager("LuaScript/h2", "luascript", "luascript");
        new CommandLoader(this);
        extraDataLoader = new ExtraDataLoader();
    }

    @Override
    public void onDisable() {
        getLogger().info("Saving Lua Environment.");
        lualoader.close();
        luarunner.close();
        getLogger().info("Saving H2 database.");
        h2Manager.CloseConnect();
        getLogger().info("Saving Config.");
        configLoader.SaveConfig();
    }

    @Override
    public LuaRunner getLuaRunner() {
        return luarunner;
    }

    @Override
    public LuaLoader getLuaLoader() {
        return lualoader;
    }

    @Override
    public H2Manager getH2Manager() {
        return h2Manager;
    }

    @Override
    public FileLoader getFileLoader(){
        return fileLoader;
    }

    @Override
    public String getVersion() {
        return version;
    }

    public ConfigLoader getConfigLoader(){
        return configLoader;
    }

    public ExtraDataLoader getExtraDataLoader(){
        return extraDataLoader;
    }
}
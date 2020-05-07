package cxmc;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import cxmc.cmd.CommandLoader;
import cxmc.config.ConfigLoader;
import cxmc.event.EventLoader;
import cxmc.extra.ExtraDataLoader;
import cxmc.extra.IDgenerator;
import cxmc.file.*;
import cxmc.lua.*;
import net.luckperms.api.LuckPerms;
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
    private LuckPerms luckPerms;
    private LuaPermHandler luaPermHandler;
    private PluginStat pluginStat;
    private IDgenerator idgenerator;

    private final String version = "1.0";
    @Override
    public void onEnable() {
        pluginStat = new PluginStat();
        getLogger().info("Loading Lua Environment");
        lualoader = new LuaLoader(this);
        luarunner = new LuaRunner();
        fileLoader = new FileLoader(getDataFolder().getAbsolutePath()+"\\scripts");
        getLogger().info("Loading Config");
        configLoader = new ConfigLoader(this);
        getLogger().info("Loading H2 database.");
        h2Manager = new H2Manager(getDataFolder().getAbsolutePath()+"\\h2", "luascript", "luascript",this);
        pluginStat.isH2Enabled = h2Manager.TryConnect();
        new CommandLoader(this);
        extraDataLoader = new ExtraDataLoader();
        if(!SetupVaultEcon()){
            getLogger().info("Vault Economy Api not found, Econnmy will be disabled");
            pluginStat.isEconEnabled = false;
        }
        if(!SetupVaultPerm()){
            getLogger().info("Vault Permission Api not found, Vault Permission support will be disabled");
            pluginStat.isVpermEnabled = false;
        }
        if(!SetupLuckPerms()){
            getLogger().info("LuckPerms API not found, LuckPerms Permission support will be disabled");
            pluginStat.isLuckPermsEnabled = false;
        }
        if(Vpermission != null && luckPerms != null){
            getLogger().info("Vault Permission and LuckPerms both exists,Valut Permission will be disabled");
            pluginStat.isVpermEnabled = false;
        }
        configLoader.apply();
        luaPermHandler = new LuaPermHandler(this);
        getServer().getPluginManager().registerEvents(new EventLoader(this), this);
        idgenerator = new IDgenerator();
    }
    
    boolean SetupVaultEcon(){
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if(rsp == null) return false;
        Veconomy = rsp.getProvider();
        return Veconomy != null;
    }

    boolean SetupVaultPerm(){
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        if (rsp == null) {
            return false;
        }
        Vpermission = rsp.getProvider();
        return Vpermission != null;
    }

    boolean SetupLuckPerms(){
        if (getServer().getPluginManager().getPlugin("LuckPerms") == null) {
            return false;
        }
        RegisteredServiceProvider<LuckPerms> provider = getServer().getServicesManager().getRegistration(LuckPerms.class);
        if(provider == null) return false;
        luckPerms = provider.getProvider();
        return luckPerms != null;
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
    public Economy getVEconomy(){
        return Veconomy;
    }
    public Permission getVPermission(){
        return Vpermission;
    }
    public LuckPerms getlLuckPerms(){
        return luckPerms;
    }
    public LuaPermHandler getluaPermHandler(){
        return luaPermHandler;
    }

    @Override
    public PluginStat getPluginStat(){
        return pluginStat;
    }
    public IDgenerator getIDgenerator(){
        return idgenerator;
    }
}
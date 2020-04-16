package cxmc;
import org.bukkit.plugin.java.JavaPlugin;
import cxmc.file.*;
import cxmc.lua.*;
import cxmc.h2.*;

public class LuaScript extends JavaPlugin implements PluginDef {
    private LuaLoader lualoader;
    private LuaRunner luarunner;
    private FileLoader fileLoader;
    private H2Manager h2Manager;
    @Override
    public void onEnable() {
        getLogger().info("Hello, SpigotMC!");
    }
    @Override
    public void onDisable() {
        getLogger().info("See you again, SpigotMC!");
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
}
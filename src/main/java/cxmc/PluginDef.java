package cxmc;


import cxmc.h2.H2Manager;
import cxmc.lua.LuaLoader;
import cxmc.lua.LuaRunner;
import cxmc.file.FileLoader;

public interface PluginDef {
    public LuaRunner getLuaRunner();
    public LuaLoader getLuaLoader();
    public H2Manager getH2Manager();
    public FileLoader getFileLoader();
    public String getVersion();
}
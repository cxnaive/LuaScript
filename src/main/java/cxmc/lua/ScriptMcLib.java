package cxmc.lua;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.ZeroArgFunction;

import cxmc.LuaScript;

public class ScriptMcLib extends TwoArgFunction{
    public static String libname = "scriptmc";
    private LuaScript instance;
    public ScriptMcLib(LuaScript instance){
        this.instance = instance;
    }
    @Override
    public LuaValue call(LuaValue modname,LuaValue env){
        LuaValue library = tableOf();
        library.set("time", new TimeFunc());
        library.set("sleep",new SleepFunc());
        library.set("servermsg",new ServerMsgFunc());
        library.set("runcmd",new RunCmdFunc());
        env.set(libname,library);
        env.get("package").get("loaded").set(libname,library);
        return library;
    }
    
    public class TimeFunc extends ZeroArgFunction{
        @Override
        public LuaValue call(){
            return LuaValue.valueOf(System.currentTimeMillis());
        }
    }

    public class SleepFunc extends OneArgFunction{
        @Override
        public LuaValue call(LuaValue arg){
            try{
                Thread.sleep(arg.checklong());
                return LuaValue.valueOf("success");
            } catch (Exception ex){
                return LuaValue.valueOf(ex.getMessage());                                  
            }
        }
    }

    public class ServerMsgFunc extends OneArgFunction{
        @Override
        public LuaValue call(LuaValue arg){
            new BukkitRunnable(){
                @Override
                public void run() {
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),"say "+arg.checkjstring());
                }
            }.runTask(instance);
            
            return LuaValue.valueOf("success");
        }
    }
    
    public class RunCmdFunc extends OneArgFunction{
        @Override
        public LuaValue call(LuaValue arg){
            new BukkitRunnable(){
                @Override
                public void run() {
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),arg.checkjstring());
                }
            }.runTask(instance);

            return LuaValue.valueOf("success");
        }
    }
}
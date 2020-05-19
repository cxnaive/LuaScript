package cxmc.lua;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.ZeroArgFunction;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;

import cxmc.LuaScript;
import cxmc.text.TextBuilder;
import net.md_5.bungee.api.ChatColor;

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
        library.set("color",new ColorFunc());
        library.set("text", new TextFunc());
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

    public class ColorFunc extends OneArgFunction{
        @Override
        public LuaValue call(LuaValue arg){
            String name = arg.checkjstring();
            for(ChatColor color: ChatColor.class.getEnumConstants()){
                if(name.equalsIgnoreCase(color.getName())){
                    return CoerceJavaToLua.coerce(color);
                }
            }
            return LuaValue.NIL;
        }
    }

    public class TextFunc extends OneArgFunction{
        @Override
        public LuaValue call(LuaValue arg){
            String str = arg.checkjstring();
            TextBuilder now = TextBuilder.of(str);
            return CoerceJavaToLua.coerce(now);
        }
    }
}
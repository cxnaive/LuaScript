package cxmc.scheduler;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;

import cxmc.LuaScript;
import cxmc.essentials.ScriptPos;
import cxmc.extra.Defs;
import cxmc.lua.LuaLoader;
import cxmc.lua.ScriptPlayer;

public class CheckPosTask extends BukkitRunnable {
    private LuaScript instance;
    public CheckPosTask(LuaScript instance){
        this.instance = instance;
    }
    @Override
    public void run() {
        for(Player player:Bukkit.getServer().getOnlinePlayers()){
            ScriptPos nowp = new ScriptPos(player.getLocation().getBlock());
            ScriptPos lastp = instance.getExtraDataLoader().getPlayerPos(player);
            instance.getExtraDataLoader().setPlayerPos(player, nowp);
            if(!nowp.equals(lastp)){
                nowp = new ScriptPos(nowp.x,nowp.y-1,nowp.z,nowp.world);
                //player.sendMessage(nowp.toString());
                if(instance.getH2Manager().HasPos(nowp)){
                    LuaLoader loader = instance.getLuaLoader();
                    Globals script = loader.GetScript(nowp);
                    if(script !=  null){
                        HashMap<String,Object> StoredVars = loader.GetVars(nowp);
                        ScriptPlayer luaplayer = new ScriptPlayer(player, instance);
                        LuaValue[] args = {CoerceJavaToLua.coerce(luaplayer),CoerceJavaToLua.coerce(StoredVars)};
                        instance.getLuaRunner().runFunc(script, instance.getIDgenerator().GetPosRunID(nowp), Defs.WalkFunc, args);
                    }
                }
            }
        }
    }
    
}
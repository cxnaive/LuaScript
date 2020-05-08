package cxmc.extra;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;

import cxmc.essentials.ScriptPos;

public class ExtraDataLoader {
    public HashMap<UUID,PlayerMode> StoredPlayerMode;
    public HashMap<UUID,String> BindedScriptID;
    public HashMap<UUID,ScriptPos> StoredPlayerPoses;
    public ExtraDataLoader(){
        StoredPlayerMode = new HashMap<>();
        BindedScriptID = new HashMap<>();
        StoredPlayerPoses = new HashMap<>();
    }
    public PlayerMode getPlayerMode(Player player){
        PlayerMode mode = StoredPlayerMode.get(player.getUniqueId());
        if(mode == null) mode = PlayerMode.Disabled;
        return mode;
    }
    public void setPlayerMode(Player player,PlayerMode mode){
        StoredPlayerMode.put(player.getUniqueId(), mode);
    }
    public String getBindedScriptID(Player player){
        return BindedScriptID.get(player.getUniqueId());
    }
    public void setBindedScriptID(Player player,String ScriptID){
        BindedScriptID.put(player.getUniqueId(), ScriptID);
    }
    public ScriptPos getPlayerPos(Player player){
        ScriptPos pos = StoredPlayerPoses.get(player.getUniqueId());
        if(pos == null) pos = new ScriptPos(player.getLocation().getBlock());
        return pos;
    }
    public void setPlayerPos(Player player,ScriptPos pos){
        StoredPlayerPoses.put(player.getUniqueId(), pos);
    }
}
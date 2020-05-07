package cxmc.extra;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;

public class ExtraDataLoader {
    public HashMap<UUID,PlayerMode> StoredPlayerMode;
    public HashMap<UUID,String> BindedScriptID;
    public ExtraDataLoader(){
        StoredPlayerMode = new HashMap<>();
        BindedScriptID = new HashMap<>();
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
}
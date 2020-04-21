package cxmc.extra;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;

public class ExtraDataLoader {
    public HashMap<UUID,PlayerMode> StoredPlayerMode;
    public ExtraDataLoader(){
        StoredPlayerMode = new HashMap<>();
    }
    public PlayerMode getPlayerMode(Player player){
        PlayerMode mode = StoredPlayerMode.get(player.getUniqueId());
        if(mode == null) mode = PlayerMode.Disabled;
        return mode;
    }
    public void setPlayerMode(Player player,PlayerMode mode){
        StoredPlayerMode.put(player.getUniqueId(), mode);
    }
}
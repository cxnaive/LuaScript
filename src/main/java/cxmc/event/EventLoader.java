package cxmc.event;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;

import cxmc.LuaScript;
import cxmc.essentials.Pair;
import cxmc.essentials.ScriptPos;
import cxmc.extra.Defs;
import cxmc.extra.PlayerMode;
import cxmc.extra.SelectArea;
import cxmc.lua.LuaLoader;
import cxmc.lua.ScriptPlayer;
import cxmc.text.TextBuilder;
import net.md_5.bungee.api.ChatColor;

public class EventLoader implements Listener {
    private LuaScript instance;
    public EventLoader(LuaScript instance){
        this.instance = instance;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onBlockBurn(BlockBurnEvent event){
        ScriptPos pos = new ScriptPos(event.getBlock());
        if(instance.getH2Manager().HasPos(pos)){
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onBlockExplode(BlockExplodeEvent event){
        Iterator<Block> iterator = event.blockList().iterator();
        while(iterator.hasNext()){
            Block now = iterator.next();
            ScriptPos pos = new ScriptPos(now);
            if(instance.getH2Manager().HasPos(pos)){
                iterator.remove();
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onEntityExplode(EntityExplodeEvent event){
        Iterator<Block> iterator = event.blockList().iterator();
        while(iterator.hasNext()){
            Block now = iterator.next();
            ScriptPos pos = new ScriptPos(now);
            if(instance.getH2Manager().HasPos(pos)){
                iterator.remove();
            }
        }
    }

    void ExecuteBlock(ScriptPos pos,Player player,ItemStack onhand,String hand){
        //instance.getLogger().warning("0");
        LuaLoader loader = instance.getLuaLoader();
        Globals script = loader.GetScript(pos);
        if(script != null){
            //instance.getLogger().warning("1");
            HashMap<String,Object> StoredVars = loader.GetVars(pos);
            ScriptPlayer luaplayer = new ScriptPlayer(player, instance);
            LuaValue[] args = {CoerceJavaToLua.coerce(luaplayer),CoerceJavaToLua.coerce(onhand),CoerceJavaToLua.coerce(StoredVars),LuaValue.valueOf(hand)};
            instance.getLuaRunner().runFunc(script, instance.getIDgenerator().GetPosRunID(pos), Defs.ClickFunc, args);
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event){
        if(!event.hasBlock()){
            return;
        }
        Player player = event.getPlayer();
        ScriptPos pos = new ScriptPos(event.getClickedBlock());
       // instance.getLogger().warning(event.getAction().name());
        if(event.getAction() == Action.LEFT_CLICK_BLOCK){
            ItemStack onhand = event.hasItem() ? event.getItem().clone():null;
            ExecuteBlock(pos, player, onhand, Defs.LeftClick);
            if(instance.getH2Manager().HasPos(pos)) event.setCancelled(true);
        }
        if(event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getHand() == EquipmentSlot.OFF_HAND){
            PlayerMode mode = instance.getExtraDataLoader().getPlayerMode(player);
            LuaLoader loader = instance.getLuaLoader();
            String ScriptID;
            switch(mode){
                case Disabled:
                    ItemStack onhand = event.hasItem() ? event.getItem().clone():null;
                    ExecuteBlock(pos, player, onhand, Defs.RightClick);
                    break;
                case ViewMode:
                    ScriptID = loader.GetPosSID(pos);
                    List<Pair<String,String>> areas = loader.GetAreaByPoss(pos);
                    if(ScriptID != null){
                        player.spigot().sendMessage(TextBuilder.of("ScriptID: ").setColor(ChatColor.AQUA).append(TextBuilder.of(ScriptID).setColor(ChatColor.YELLOW).build()).build());
                    }
                    if(!areas.isEmpty()){
                        for(Pair<String,String> area:areas){
                            player.spigot().sendMessage(TextBuilder.of(area.getKey()+": ").setColor(ChatColor.AQUA).append(TextBuilder.of(area.getValue()).setColor(ChatColor.YELLOW).build()).build());   
                        }
                        player.spigot().sendMessage(TextBuilder.of(""+areas.size()).setColor(ChatColor.GREEN).append(TextBuilder.of(" areas in total.").setColor(ChatColor.AQUA).build()).build());
                    }
                    if(ScriptID == null && areas.isEmpty()) player.spigot().sendMessage(TextBuilder.of("No script on this block.").setColor(ChatColor.AQUA).build());
                    break;
                case DelMode:
                    if(instance.getH2Manager().HasPos(pos)){
                        loader.DeletePos(pos);
                    }
                    else player.spigot().sendMessage(TextBuilder.of("No script on this block.").setColor(ChatColor.AQUA).build());
                    player.spigot().sendMessage(TextBuilder.of("Successfully delete ScriptID.").setColor(ChatColor.AQUA).build());
                    break;
                case SetMode:
                    ScriptID = instance.getExtraDataLoader().getBindedScriptID(player);
                    if(ScriptID == null){
                        player.spigot().sendMessage(TextBuilder.of("You need to bind a script first!").setColor(ChatColor.AQUA).build());
                        break;
                    }
                    if(!instance.getH2Manager().HasScript(ScriptID)){
                        player.spigot().sendMessage(TextBuilder.of("Invalid or Unstored ScriptID!").setColor(ChatColor.AQUA).build());
                        break;
                    }
                    loader.SetPos(pos, ScriptID, new HashMap<>());
                    player.spigot().sendMessage(TextBuilder.of("Successfully set ScriptID.").setColor(ChatColor.AQUA).build());
                    break;
                case AreaMode:
                    SelectArea area = instance.getExtraDataLoader().getPlayerArea(player);
                    if(area.state == 0 || area.state == 2){
                        area.a = pos;
                        area.state = 1;
                        instance.getExtraDataLoader().SetPlayerArea(player, area);
                        player.spigot().sendMessage(TextBuilder.of("First "+pos.toString()).setColor(ChatColor.AQUA).build());
                    }
                    else if(area.state == 1){
                        area.b = pos;
                        area.state = 2;
                        instance.getExtraDataLoader().SetPlayerArea(player, area);
                        player.spigot().sendMessage(TextBuilder.of("Second "+pos.toString()).setColor(ChatColor.AQUA).build());
                    }
                default:
                    player.spigot().sendMessage(TextBuilder.of("Invalid palyer mode.").setColor(ChatColor.RED).build());
                    break;
            }
        }
    }
    
}
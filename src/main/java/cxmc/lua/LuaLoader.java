package cxmc.lua;

import cxmc.*;
import cxmc.essentials.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.luaj.vm2.Globals;
import org.luaj.vm2.lib.jse.JsePlatform;

public class LuaLoader {
    private PluginDef instance;
    HashMap<String,Globals> StoredScripts;
    HashMap<String,HashMap<String,Object>> StoredVars;
    public LuaLoader(PluginDef instance){
        this.StoredScripts = new HashMap<>();
        this.StoredVars = new HashMap<>();
        this.instance = instance;
    }
    public HashMap<String,Object> LoadVarsFromH2(ScriptPos pos){
        HashMap<String,Object> now = instance.getH2Manager().GetPosVars(pos);
        if(now != null){
            StoredVars.put(pos.toString(), now);
            return now;
        }
        return null;
    }
    public HashMap<String,Object> LoadVarsFromH2(String AreaID){
        HashMap<String,Object> now = instance.getH2Manager().GetAreaVars(AreaID);
        if(now != null){
            StoredVars.put(AreaID, now);
            return now;
        }
        return null;
    }
    public Globals LoadScriptFromH2(ScriptPos pos){
        Globals script = JsePlatform.standardGlobals();
        try{
            String luastr = instance.getH2Manager().GetPosScript(pos);
            script.load(new LuaDebugLib());
            script.load(new ScriptMcLib());
            script.load(luastr,"@"+pos.toString(),script).call();
            StoredScripts.put(pos.toString(), script);
            return script;
        } catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
    public Globals LoadScriptFromH2(String AreaID){
        Globals script = JsePlatform.standardGlobals();
        try{
            String luastr = instance.getH2Manager().GetAreaScript(AreaID);
            script.load(new LuaDebugLib());
            script.load(new ScriptMcLib());
            script.load(luastr,"@"+AreaID,script).call();
            StoredScripts.put(AreaID, script);
            return script;
        } catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
    public String GetScriptContent(String SID){
        return instance.getH2Manager().GetScriptBySID(SID);
    }
    public List<String> GetAreaBySID(String SID){
        return instance.getH2Manager().GetAreaBySID(SID);
    }
    public List<ScriptPos> GetPosBySID(String SID){
        return instance.getH2Manager().GetPosBySID(SID);
    }
    public List<Pair<String,String>> GetAreaALL(){
        return instance.getH2Manager().GetAreaALL();
    }
    public List<Pair<ScriptPos,String>> GetPosALL(){
        return instance.getH2Manager().GetPosALL();
    }
    public List<String> GetScriptIDALL(){
        return instance.getH2Manager().GetSIDALL();
    }
    public String GetPosSID(ScriptPos pos){
        return instance.getH2Manager().GetPosSID(pos);
    }
    public String GetAreaSID(String AreaID){
        return instance.getH2Manager().GetAreaSID(AreaID);
    }
    public Globals GetScript(ScriptPos pos){
        Globals stored = StoredScripts.get(pos.toString());
        if(stored == null) stored = LoadScriptFromH2(pos);
        return stored;
    }
    public Globals GetScript(String AreaID){
        Globals stored = StoredScripts.get(AreaID);
        if(stored == null) stored = LoadScriptFromH2(AreaID);
        return stored;
    }
    public HashMap<String,Object> GetVars(String AreaID){
        HashMap<String,Object> now = StoredVars.get(AreaID);
        if(now == null) now = LoadVarsFromH2(AreaID);
        return now;
    }
    public HashMap<String,Object> GetVars(ScriptPos pos){
        HashMap<String,Object> now = StoredVars.get(pos.toString());
        if(now == null) now = LoadVarsFromH2(pos);
        return now;
    }
    public Pair<ScriptPos,ScriptPos> GetAABB(String AreaID){
        return instance.getH2Manager().GetAreaAABB(AreaID);
    }
    public boolean SetScript(String ScriptID,String Content){
        if(instance.getH2Manager().HasScript(ScriptID)){
            instance.getH2Manager().UpdateScript(ScriptID, Content);
            List<ScriptPos> AffectedPoses = GetPosBySID(ScriptID);
            List<String> AffectedAreas = GetAreaBySID(ScriptID);
            RefreshScriptArea(AffectedAreas);
            RefreshScriptPos(AffectedPoses);
            return true;
        }
        else return instance.getH2Manager().PutScript(ScriptID, Content);
    }
    public boolean SetPos(ScriptPos pos,String ScriptID,HashMap<String,Object> values){
        if(instance.getH2Manager().HasPos(pos)){
            instance.getH2Manager().UpdatePos(pos, ScriptID, values);
            RefreshScriptPos(pos);
            RefreshVarsPos(pos);
            return true;
        }
        else return instance.getH2Manager().PutPos(pos, ScriptID, values);
    }

    public boolean SetPosVars(ScriptPos pos,HashMap<String,Object> values){
        if(instance.getH2Manager().HasPos(pos)){
            StoredVars.put(pos.toString(), values);
            return true;
        }
        return false;
        //return instance.h2Manager.UpdatePosVars(pos, values);
    }
    public boolean SetAreaVars(String AreaID,HashMap<String,Object> values){
        if(instance.getH2Manager().HasArea(AreaID)){
            StoredVars.put(AreaID, values);
            return true;
        }
        return false;
        //return instance.h2Manager.UpdateAreaVars(AreaID, values);
    }
    public boolean SetArea(String AreaID,String ScriptID,HashMap<String,Object> values){
        RefreshScriptArea(AreaID);
        RefreshVarsArea(AreaID);
        return instance.getH2Manager().UpdateArea(AreaID, ScriptID, values);
    }
    public boolean SetArea(ScriptPos pos1,ScriptPos pos2,String AreaID,String ScriptID,HashMap<String,Object> values){
        RefreshScriptArea(AreaID);
        RefreshVarsArea(AreaID);
        if(instance.getH2Manager().HasArea(AreaID)){
            return instance.getH2Manager().DeleteArea(AreaID);
        }
        return instance.getH2Manager().PutArea(pos1, pos2, AreaID, ScriptID, values);
    }
    public void RefreshScriptArea(List<String> AreaIDs){
        for(String id:AreaIDs){
            if(StoredScripts.containsKey(id)) StoredScripts.remove(id);
        }
    }
    public void RefreshVarsArea(List<String> AreaIDs){
        for(String id:AreaIDs){
            if(StoredVars.containsKey(id)) StoredVars.remove(id);
        }
    }
    public void RefreshScriptPos(List<ScriptPos> Poses){
        for(ScriptPos pos:Poses){
            if(StoredScripts.containsKey(pos.toString())) StoredScripts.remove(pos.toString());
        }
    }
    public void RefreshVarsPos(List<ScriptPos> Poses){
        for(ScriptPos pos:Poses){
            if(StoredVars.containsKey(pos.toString())) StoredVars.remove(pos.toString());
        }
    }
    public void RefreshScriptArea(String AreaID){
        if(StoredScripts.containsKey(AreaID)) StoredScripts.remove(AreaID);
    }
    public void RefreshVarsArea(String AreaID){
        if(StoredVars.containsKey(AreaID)) StoredVars.remove(AreaID);
    }
    public void RefreshScriptPos(ScriptPos pos){
        if(StoredScripts.containsKey(pos.toString())) StoredScripts.remove(pos.toString());
    }
    public void RefreshVarsPos(ScriptPos pos){
        if(StoredVars.containsKey(pos.toString())) StoredVars.remove(pos.toString());
    }
    public void close(){
        for(Entry<String,HashMap<String,Object>> entry:StoredVars.entrySet()){
            if(entry.getKey().startsWith("P:")){
                instance.getH2Manager().UpdatePosVars(ScriptPos.BuildFromStr(entry.getKey()), entry.getValue());
            }
            else{
                instance.getH2Manager().UpdateAreaVars(entry.getKey(), entry.getValue());
            }
        }
    }
}
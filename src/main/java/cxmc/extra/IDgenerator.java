package cxmc.extra;

import java.util.HashMap;

import cxmc.essentials.ScriptPos;

public class IDgenerator {
    HashMap<ScriptPos,Integer> ScriptPosMap;
    public IDgenerator(){
        ScriptPosMap = new HashMap<>();
    }
    public String GetPosRunID(ScriptPos pos){
        if(!ScriptPosMap.containsKey(pos)) ScriptPosMap.put(pos, 0);
        int count = ScriptPosMap.get(pos);
        ScriptPosMap.put(pos, count + 1);
        return "#Block:"+pos.toString()+"-"+count;
    }
}
package cxmc.extra;

import cxmc.essentials.ScriptPos;

public class SelectArea {
    public int state;
    public ScriptPos a,b;
    public SelectArea(){
        state = 0;
        a = b = null;
    }
}
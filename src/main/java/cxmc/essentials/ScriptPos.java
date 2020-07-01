package cxmc.essentials;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Block;

public class ScriptPos {
    public int x,y,z;
    public World world;
    public ScriptPos(int x,int y,int z,World world){
        this.x = x;
        this.y = y;
        this.z = z;
        this.world = world;
    }
    public ScriptPos(Block block){
        this.x = block.getX();
        this.y = block.getY();
        this.z = block.getZ();
        this.world = block.getWorld();
    }
    public ScriptPos getmin(ScriptPos other){
        return new ScriptPos(Math.min(this.x, other.x),Math.min(this.y, other.y),Math.min(this.z, other.z),this.world);
    }
    public ScriptPos getmax(ScriptPos other){
        return new ScriptPos(Math.max(this.x, other.x),Math.max(this.y, other.y),Math.max(this.z, other.z),this.world);
    }
    @Override
    public String toString(){
        return "P:"+this.x+","+this.y+","+this.z+","+world.getName();
    }
    @Override
    public final int hashCode(){
        return toString().hashCode();
    }
    @Override
    public boolean equals(Object object){
        if(this == object) return true;
        if(object instanceof ScriptPos){
            ScriptPos other = (ScriptPos)object;
            return this.x == other.x && this.y == other.y && this.z == other.z && this.world.getUID().equals(other.world.getUID());
        }
        else return false;
    }
    public static ScriptPos BuildFromStr(String str){
        String[] vars = str.substring(2).split(",");
        return new ScriptPos(Integer.parseInt(vars[0]),Integer.parseInt(vars[1]),Integer.parseInt(vars[2]),Bukkit.getWorld(vars[3]));
    }
}
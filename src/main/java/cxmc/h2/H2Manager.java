package cxmc.h2;

import cxmc.LuaScript;
import cxmc.essentials.*;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.World;

/*
SID---script ID
ID---area ID
VARS---variables
*/
public class H2Manager {
    private static final String DRIVER_CLASS = "org.h2.Driver";
    
    private static final String POS_STRING = "POS";
    private static final String POS_STRUCT = "(X INT,Y INT,Z INT,WORLD CHAR(36),SID VARCHAR(255),VARS BLOB)";
    private static final String POS_COLUMNS = "(X,Y,Z,WORLD,SID,VARS)";

    private static final String AREA_STRING = "AREA";
    private static final String AREA_STRUCT = "(X1 INT,Y1 INT,Z1 INT,X2 INT,Y2 INT,Z2 INT,WORLD CHAR(36),ID VARCHAR(255),SID VARCHAR(255),VARS BLOB)";
    private static final String AREA_COLUMNS = "(X1,Y1,Z1,X2,Y2,Z2,WORLD,ID,SID,VARS)";

    private static final String SCRIPT_STRING = "SCRIPT";
    private static final String SCRIPT_STRUCT = "(SID VARCHAR(255),CONTENT VARCHAR(65535))";
    private static final String SCRIPT_COLUMNS = "(SID,CONTENT)";
    
    private PreparedStatement PUT_SCRIPT,PUT_POS,PUT_AREA;
    private PreparedStatement HAS_POS,HAS_AREA,HAS_SID;
    private PreparedStatement GET_POS_SCRIPT,GET_POS_SID,GET_AREA_SID,GET_AREA_SCRIPT,
                            GET_POS_VARS,GET_AREA_VARS,GET_AREA_AABB,GET_POS_BY_SID,GET_AREA_BY_SID,
                            GET_POS_ALL,GET_AREA_ALL,GET_SID_ALL,GET_SCRIPT_BY_SID,GET_POS_BY_WLD,GET_AREA_BY_POS;
    private PreparedStatement UPD_SCRIPT,UPD_POS_VAR,UPD_AREA_VAR,UPD_POS,UPD_AREA;
    private PreparedStatement DEL_SCRIPT,DEL_POS,DEL_AREA;
    private PreparedStatement CLS_POS,CLS_AREA,CLS_SCRIPT,CLS_WORLD;
    private final String USER;
    private final String PASSWORD;
    private final String PATH;
    Connection conn;

    private LuaScript instance;
    public H2Manager(final String path, final String username, final String password,final LuaScript instance) {
        this.PATH = path;
        this.PASSWORD = password;
        this.USER = username;
        this.instance = instance;
    }

    public boolean TryConnect() {
        final String JDBC_URL = "jdbc:h2:" + this.PATH + ";AUTO_RECONNECT=TRUE";
        try {
            Class.forName(DRIVER_CLASS);
            conn = DriverManager.getConnection(JDBC_URL, this.USER, this.PASSWORD);
            final Statement statement = conn.createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS " + POS_STRING + POS_STRUCT);
            statement.execute("CREATE TABLE IF NOT EXISTS " + AREA_STRING + AREA_STRUCT);
            statement.execute("CREATE TABLE IF NOT EXISTS " + SCRIPT_STRING + SCRIPT_STRUCT);
            statement.close();
            PUT_SCRIPT = conn.prepareStatement("INSERT INTO " + SCRIPT_STRING + SCRIPT_COLUMNS + " VALUES(?,?)");
            PUT_POS = conn.prepareStatement("INSERT INTO " + POS_STRING + POS_COLUMNS + " VALUES(?,?,?,?,?,?)");
            PUT_AREA = conn
                    .prepareStatement("INSERT INTO " + AREA_STRING + AREA_COLUMNS + " VALUES(?,?,?,?,?,?,?,?,?,?)");

            HAS_POS = conn.prepareStatement("SELECT COUNT(*) FROM " + POS_STRING + " WHERE X = ? AND Y = ? AND Z = ? AND WORLD = ?");
            HAS_AREA = conn.prepareStatement("SELECT COUNT(*) FROM " + AREA_STRING + " WHERE ID = ?");
            HAS_SID = conn.prepareStatement("SELECT COUNT(*) FROM " + SCRIPT_STRING + " WHERE SID = ?");
            
            GET_POS_SCRIPT = conn.prepareStatement("SELECT CONTENT FROM " + SCRIPT_STRING 
                    + " WHERE SID IN (SELECT SID FROM " + POS_STRING + " WHERE X = ? AND Y = ? AND Z = ? AND WORLD = ?)");
            GET_AREA_SCRIPT = conn.prepareStatement("SELECT CONTENT FROM " + SCRIPT_STRING
                    + " WHERE SID IN (SELECT SID FROM " + AREA_STRING + " WHERE ID = ?)");
            GET_POS_SID = conn.prepareStatement("SELECT SID FROM "+POS_STRING+" WHERE X = ? AND Y = ? AND Z = ? AND WORLD = ?");
            GET_AREA_SID = conn.prepareStatement("SELECT SID FROM "+AREA_STRING+" WHERE ID = ?");
            GET_POS_VARS = conn.prepareStatement("SELECT VARS FROM "+POS_STRING+" WHERE X = ? AND Y = ? AND Z = ? AND WORLD = ?");
            GET_AREA_VARS = conn.prepareStatement("SELECT VARS FROM "+AREA_STRING+" WHERE ID = ?");
            GET_AREA_AABB = conn.prepareStatement("SELECT X1,Y1,Z1,X2,Y2,Z2,WORLD FROM "+AREA_STRING+" WHERE ID = ?");
            GET_POS_BY_WLD = conn.prepareStatement("SELECT X,Y,Z,WORLD FROM "+POS_STRING+" WHERE WORLD = ?");
            GET_POS_BY_SID = conn.prepareStatement("SELECT X,Y,Z,WORLD FROM "+POS_STRING+" WHERE SID = ?");
            GET_AREA_BY_SID = conn.prepareStatement("SELECT ID FROM "+AREA_STRING+" WHERE SID = ?");
            GET_POS_ALL = conn.prepareStatement("SELECT X,Y,Z,WORLD,SID FROM "+POS_STRING);
            GET_AREA_ALL = conn.prepareStatement("SELECT ID,SID FROM "+AREA_STRING);
            GET_SID_ALL = conn.prepareStatement("SELECT SID FROM "+SCRIPT_STRING);
            GET_SCRIPT_BY_SID = conn.prepareStatement("SELECT CONTENT FROM "+SCRIPT_STRING+" WHERE SID = ?");
            GET_AREA_BY_POS = conn.prepareStatement("SELECT ID,SID FROM "+AREA_STRING+" WHERE X1 <= ? AND ? <= X2 AND Y1 <= ? AND ? <= Y2 AND Z1 <= ? AND ? <= Z2 AND WORLD = ?");

            UPD_SCRIPT = conn.prepareStatement("UPDATE " + SCRIPT_STRING + " SET CONTENT = ? WHERE SID = ?");
            UPD_POS_VAR = conn.prepareStatement(
                    "UPDATE " + POS_STRING + " SET VARS = ? WHERE X = ? AND Y = ? AND Z = ? AND WORLD = ?");
            UPD_AREA_VAR = conn
                    .prepareStatement("UPDATE " + AREA_STRING + " SET VARS = ? WHERE ID = ?");
            UPD_POS = conn.prepareStatement("UPDATE "+ POS_STRING + " SET SID = ?,VARS = ? WHERE X = ? AND Y = ? AND Z = ? AND WORLD = ?");
            UPD_AREA = conn.prepareStatement("UPDATE " + AREA_STRING + " SET SID = ?,VARS = ? WHERE ID = ?");
            
            DEL_SCRIPT = conn.prepareStatement("DELETE FROM "+POS_STRING+" WHERE SID = ?;"+"DELETE FROM "+AREA_STRING+" WHERE SID = ?;"+"DELETE FROM "+SCRIPT_STRING+" WHERE SID = ?");
            DEL_POS = conn.prepareStatement("DELETE FROM "+POS_STRING+" WHERE X = ? AND Y = ? AND Z = ? AND WORLD = ?");
            DEL_AREA = conn.prepareStatement("DELETE FROM "+AREA_STRING+" WHERE ID = ?");

            CLS_AREA = conn.prepareStatement("DELETE FROM "+AREA_STRING);
            CLS_POS = conn.prepareStatement("DELETE FROM "+POS_STRING);
            CLS_SCRIPT = conn.prepareStatement("DELETE FROM "+SCRIPT_STRING);
            CLS_WORLD = conn.prepareStatement("DELETE FROM " +POS_STRING+ " WHERE WORLD = ?");
            return true;
        } catch (final Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public void CloseConnect() {
        try {
            PUT_SCRIPT.close();
            PUT_POS.close();
            PUT_AREA.close();

            HAS_SID.close();
            HAS_POS.close();
            HAS_AREA.close();

            GET_POS_SCRIPT.close();
            GET_AREA_SCRIPT.close();
            GET_SCRIPT_BY_SID.close();
            GET_POS_SID.close();
            GET_POS_BY_WLD.close();
            GET_AREA_SID.close();
            GET_POS_VARS.close();
            GET_AREA_VARS.close();
            GET_AREA_AABB.close();
            GET_POS_BY_SID.close();
            GET_AREA_BY_SID.close();
            GET_POS_ALL.close();
            GET_AREA_ALL.close();
            GET_SID_ALL.close();
            
            UPD_SCRIPT.close();
            UPD_POS_VAR.close();
            UPD_AREA_VAR.close();
            UPD_POS.close();
            UPD_AREA.close();

            DEL_SCRIPT.close();
            DEL_POS.close();
            DEL_AREA.close();
            
            CLS_SCRIPT.close();
            CLS_POS.close();
            CLS_AREA.close();
            CLS_WORLD.close();
            conn.close();
        } catch (final Exception ex) {
            ex.printStackTrace();
        }
    }

    public boolean ClearScript(){
        try{
            CLS_SCRIPT.executeUpdate();
            return true;
        } catch (final Exception ex){
            if(instance.getPluginStat().isDebugMode){
                ex.printStackTrace();
            }
            return false;
        }
    }

    public boolean ClearPos(){
        try{
            CLS_POS.executeUpdate();
            return true;
        } catch (final Exception ex){
            if(instance.getPluginStat().isDebugMode){
                ex.printStackTrace();
            }
            return false;
        }
    }

    public boolean ClearArea(){
        try{
            CLS_AREA.executeUpdate();
            return true;
        } catch (final Exception ex){
            if(instance.getPluginStat().isDebugMode){
                ex.printStackTrace();
            }
            return false;
        }
    }

    public boolean ClearWorld(final UUID world){
        try{
            CLS_WORLD.setString(1, world.toString());
            CLS_WORLD.executeUpdate();
            return true;
        } catch (final Exception ex){
            if(instance.getPluginStat().isDebugMode){
                ex.printStackTrace();
            }
            return false;
        }
    }

    public boolean DeleteScript(final String ScriptID){
        try{
            DEL_SCRIPT.setString(1, ScriptID);
            DEL_SCRIPT.setString(2, ScriptID);
            DEL_SCRIPT.setString(3, ScriptID);
            DEL_SCRIPT.executeUpdate();
            return true;
        } catch (final Exception ex){
            if(instance.getPluginStat().isDebugMode){
                ex.printStackTrace();
            }
            return false;
        }
    }

    public boolean DeletePos(final ScriptPos pos){
        try {
            DEL_POS.setInt(1, pos.x);
            DEL_POS.setInt(2, pos.y);
            DEL_POS.setInt(3, pos.z);
            DEL_POS.setString(4, pos.world.getUID().toString());
            DEL_POS.executeUpdate();
            return true;
        } catch (final Exception ex) {
            if(instance.getPluginStat().isDebugMode){
                ex.printStackTrace();
            }
            return false;
        }
    }

    public boolean DeleteArea(final String AreaID){
        try {
            DEL_AREA.setString(1, AreaID);
            DEL_AREA.executeUpdate();
            return true;
        } catch (final Exception ex) {
            if(instance.getPluginStat().isDebugMode){
                ex.printStackTrace();
            }
            return false;
        }
    }

    public boolean HasScript(final String ScriptID) {
        try {
            HAS_SID.setString(1, ScriptID);
            final ResultSet result = HAS_SID.executeQuery();
            result.next();
            final int cnt = result.getInt(1);
            return cnt == 1;
        } catch (final Exception ex) {
            if(instance.getPluginStat().isDebugMode){
                ex.printStackTrace();
            }
            return false;
        }
    }

    public boolean HasPos(final ScriptPos pos) {
        try {
            HAS_POS.setInt(1, pos.x);
            HAS_POS.setInt(2, pos.y);
            HAS_POS.setInt(3, pos.z);
            HAS_POS.setString(4, pos.world.getUID().toString());
            final ResultSet result = HAS_POS.executeQuery();
            result.next();
            final int cnt = result.getInt(1);
            return cnt == 1;
        } catch (final Exception ex) {
            if(instance.getPluginStat().isDebugMode){
                ex.printStackTrace();
            }
            return false;
        }
    }

    public boolean HasArea(final String AreaID) {
        try {
            HAS_AREA.setString(1, AreaID);
            final ResultSet result = HAS_AREA.executeQuery();
            result.next();
            final int cnt = result.getInt(1);
            return cnt == 1;
        } catch (final Exception ex) {
            if(instance.getPluginStat().isDebugMode){
                ex.printStackTrace();
            }
            return false;
        }
    }

    public String GetPosScript(final ScriptPos pos) {
        try {
            GET_POS_SCRIPT.setInt(1, pos.x);
            GET_POS_SCRIPT.setInt(2, pos.y);
            GET_POS_SCRIPT.setInt(3, pos.z);
            GET_POS_SCRIPT.setString(4, pos.world.getUID().toString());
            final ResultSet result = GET_POS_SCRIPT.executeQuery();
            result.next();
            final String now = result.getString(1);
            return now;
        } catch (final Exception ex) {
            if(instance.getPluginStat().isDebugMode){
                ex.printStackTrace();
            }
            return null;
        }
    }

    public String GetAreaScript(final String AreaID) {
        try {
            GET_AREA_SCRIPT.setString(1, AreaID);
            final ResultSet result = GET_AREA_SCRIPT.executeQuery();
            result.next();
            final String now = result.getString(1);
            return now;
        } catch (final Exception ex) {
            if(instance.getPluginStat().isDebugMode){
                ex.printStackTrace();
            }
            return null;
        }
    }
    
    @SuppressWarnings("unchecked")
    private HashMap<String,Object> Blob2Map(Blob inBlob) throws Exception{
        InputStream is = inBlob.getBinaryStream();
        BufferedInputStream bis = new BufferedInputStream(is);
        byte[] buff = new byte[(int) inBlob.length()];
        bis.read(buff, 0, buff.length);
        ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(buff));
        return (HashMap<String,Object>)in.readObject();
    }
    
    public HashMap<String,Object> GetPosVars(final ScriptPos pos) {
        try {
            GET_POS_VARS.setInt(1, pos.x);
            GET_POS_VARS.setInt(2, pos.y);
            GET_POS_VARS.setInt(3, pos.z);
            GET_POS_SCRIPT.setString(4, pos.world.getUID().toString());
            final ResultSet result = GET_POS_VARS.executeQuery();
            result.next();
            return Blob2Map(result.getBlob(1));
        } catch (final Exception ex) {
            if(instance.getPluginStat().isDebugMode){
                ex.printStackTrace();
            }
            return null;
        }
    }

    public HashMap<String,Object> GetAreaVars(final String AreaID) {
        try {
            GET_AREA_VARS.setString(1, AreaID);
            final ResultSet result = GET_AREA_VARS.executeQuery();
            result.next();
            return Blob2Map(result.getBlob(1));
        } catch (final Exception ex) {
            if(instance.getPluginStat().isDebugMode){
                ex.printStackTrace();
            }
            return null;
        }
    }

    public Pair<ScriptPos,ScriptPos> GetAreaAABB(final String AreaID){
        try{
            GET_AREA_AABB.setString(1, AreaID);
            ResultSet result = GET_AREA_AABB.executeQuery();
            result.next();
            World now = Bukkit.getWorld(UUID.fromString(result.getString(4)));
            return new Pair<ScriptPos,ScriptPos>(new ScriptPos(result.getInt(1), result.getInt(2), result.getInt(3),now),new ScriptPos(result.getInt(4), result.getInt(5), result.getInt(6),now));
        }catch(Exception ex){
            if(instance.getPluginStat().isDebugMode){
                ex.printStackTrace();
            }
            return null;
        }
    }

    //public 

    public String GetPosSID(final ScriptPos pos){
        try{
            GET_POS_SID.setInt(1, pos.x);
            GET_POS_SID.setInt(2, pos.y);
            GET_POS_SID.setInt(3, pos.z);
            GET_POS_SID.setString(4, pos.world.getUID().toString());
            ResultSet result =  GET_POS_SID.executeQuery();
            result.next();
            return result.getString(1);
        }catch(Exception ex){
            if(instance.getPluginStat().isDebugMode){
                ex.printStackTrace();
            }
            return null;
        }
    }

    public List<ScriptPos> GetPosByWorld(final UUID wuuid){
        try{
            GET_POS_BY_WLD.setString(1, wuuid.toString());
            ResultSet result = GET_POS_BY_WLD.executeQuery();
            List<ScriptPos> now = new ArrayList<>();
            while(result.next()){
                World world = Bukkit.getWorld(UUID.fromString(result.getString(4)));
                now.add(new ScriptPos(result.getInt(1), result.getInt(2), result.getInt(3), world));
            }
            return now;
        } catch (Exception ex){
            if(instance.getPluginStat().isDebugMode){
                ex.printStackTrace();
            }
            return null;
        }
    }

    public String GetAreaSID(final String AreaID){
        try{
            GET_AREA_SID.setString(1, AreaID);
            ResultSet result =  GET_AREA_SID.executeQuery();
            result.next();
            return result.getString(1);
        }catch(Exception ex){
            if(instance.getPluginStat().isDebugMode){
                ex.printStackTrace();
            }
            return null;
        }
    }

    public List<ScriptPos> GetPosBySID(final String SID){
        try{
            GET_POS_BY_SID.setString(1, SID);
            ResultSet result = GET_POS_BY_SID.executeQuery();
            List<ScriptPos> now = new ArrayList<>();
            while(result.next()){
                World world = Bukkit.getWorld(UUID.fromString(result.getString(4)));
                now.add(new ScriptPos(result.getInt(1), result.getInt(2), result.getInt(3), world));
            }
            return now;
        } catch (Exception ex){
            if(instance.getPluginStat().isDebugMode){
                ex.printStackTrace();
            }
            return null;
        }
    }

    public List<String> GetAreaBySID(final String SID){
        try{
            GET_AREA_BY_SID.setString(1, SID);
            ResultSet result = GET_AREA_BY_SID.executeQuery();
            List<String> now = new ArrayList<>();
            while(result.next()){
                now.add(result.getString(1));
            }
            return now;
        } catch (Exception ex){
            if(instance.getPluginStat().isDebugMode){
                ex.printStackTrace();
            }
            return null;
        }
    }

    public String GetScriptBySID(final String SID){
        try{
            GET_SCRIPT_BY_SID.setString(1, SID);
            ResultSet result = GET_SCRIPT_BY_SID.executeQuery();
            result.next();
            return result.getString(1);
        } catch (Exception ex){
            if(instance.getPluginStat().isDebugMode){
                ex.printStackTrace();
            }
            return null;
        }
    }

    public List<Pair<ScriptPos,String>> GetPosALL(){
        try{
            ResultSet result = GET_POS_ALL.executeQuery();
            List<Pair<ScriptPos,String>> now = new ArrayList<>();
            while(result.next()){
                World world = Bukkit.getWorld(UUID.fromString(result.getString(4)));
                ScriptPos npos = new ScriptPos(result.getInt(1), result.getInt(2), result.getInt(3), world);
                now.add(new Pair<ScriptPos,String>(npos,result.getString(5)));
            }
            return now;
        } catch (Exception ex){
            if(instance.getPluginStat().isDebugMode){
                ex.printStackTrace();
            }
            return null;
        }
    }

    public List<Pair<String,String>> GetAreaALL(){
        try{
            ResultSet result = GET_AREA_ALL.executeQuery();
            List<Pair<String,String>> now = new ArrayList<>();
            while(result.next()){
                now.add(new Pair<String,String>(result.getString(1),result.getString(2)));
            }
            return now;
        } catch (Exception ex){
            if(instance.getPluginStat().isDebugMode){
                ex.printStackTrace();
            }
            return null;
        }
    }

    public List<Pair<String,String>> GetAreaByPos(ScriptPos pos){
        try{
            GET_AREA_BY_POS.setInt(1, pos.x);
            GET_AREA_BY_POS.setInt(2, pos.x);
            GET_AREA_BY_POS.setInt(3, pos.y);
            GET_AREA_BY_POS.setInt(4, pos.y);
            GET_AREA_BY_POS.setInt(5, pos.z);
            GET_AREA_BY_POS.setInt(6, pos.z);
            GET_AREA_BY_POS.setString(7, pos.world.getUID().toString());
            ResultSet result = GET_AREA_BY_POS.executeQuery();
            List<Pair<String,String>> now = new ArrayList<>();
            while(result.next()){
                now.add(new Pair<String,String>(result.getString(1),result.getString(2)));
            }
            return now;
        } catch (Exception ex){
            if(instance.getPluginStat().isDebugMode){
                ex.printStackTrace();
            }
            return null;
        }
    }

    public List<String> GetSIDALL(){
        try{
            ResultSet result = GET_SID_ALL.executeQuery();
            List<String> now = new ArrayList<>();
            while(result.next()){
                now.add(result.getString(1));
            }
            return now;
        } catch (Exception ex){
            if(instance.getPluginStat().isDebugMode){
                ex.printStackTrace();
            }
            return null;
        }
    }

    public boolean UpdatePos(final ScriptPos pos,final String ScriptID, final HashMap<String,Object> values){
        try{
            UPD_POS.setString(1, ScriptID);
            UPD_POS.setObject(2, values);
            UPD_POS.setInt(3, pos.x);
            UPD_POS.setInt(4, pos.y);
            UPD_POS.setInt(5, pos.z);
            UPD_POS.setString(6, pos.world.getUID().toString());
            UPD_POS.executeUpdate();
            return true;
        } catch(Exception ex){
            if(instance.getPluginStat().isDebugMode){
                ex.printStackTrace();
            }
            return false;
        }
    }

    public boolean UpdateArea(final String AreaID,final String ScriptID, final HashMap<String,Object> values){
        try{
            UPD_POS.setString(1, ScriptID);
            UPD_POS.setObject(2, values);
            UPD_POS.setString(3, AreaID);
            int af = UPD_POS.executeUpdate();
            return af == 1;
        } catch(Exception ex){
            if(instance.getPluginStat().isDebugMode){
                ex.printStackTrace();
            }
            return false;
        }
    }

    public boolean UpdateScript(final String ScriptID, final String Content) {
        try {
            UPD_SCRIPT.setString(1, Content);
            UPD_SCRIPT.setString(2, ScriptID);
            UPD_SCRIPT.executeUpdate();
            return true;
        } catch (final Exception ex) {
            if(instance.getPluginStat().isDebugMode){
                ex.printStackTrace();
            }
            return false;
        }
    }

    public boolean UpdatePosVars(final ScriptPos pos, final HashMap<String,Object> values) {
        try {
            UPD_POS_VAR.setObject(1, values);
            UPD_POS_VAR.setInt(2, pos.x);
            UPD_POS_VAR.setInt(3, pos.y);
            UPD_POS_VAR.setInt(4, pos.z);
            UPD_POS_VAR.setString(5, pos.world.getUID().toString());
            int af = UPD_POS_VAR.executeUpdate();
            return af == 1;
        } catch (final Exception ex) {
            if(instance.getPluginStat().isDebugMode){
                ex.printStackTrace();
            }
            return false;
        }
    }

    public boolean UpdateAreaVars(final String AreaID, final HashMap<String,Object> values) {
        try {
            UPD_AREA_VAR.setObject(1, values);
            UPD_AREA_VAR.setString(2, AreaID);
            int af = UPD_AREA_VAR.executeUpdate();
            return af == 1;
        } catch (final Exception ex) {
            if(instance.getPluginStat().isDebugMode){
                ex.printStackTrace();
            }
            return false;
        }
    }

    public boolean PutScript(final String ScriptID, final String Content) {
        try {
            PUT_SCRIPT.setString(1, ScriptID);
            PUT_SCRIPT.setString(2, Content);
            PUT_SCRIPT.executeUpdate();
            return true;
        } catch (final Exception ex) {
            if(instance.getPluginStat().isDebugMode){
                ex.printStackTrace();
            }
            return false;
        }
    }

    public boolean PutPos(final ScriptPos pos, final String ScriptID, final HashMap<String,Object> values) {
        try {
            PUT_POS.setInt(1, pos.x);
            PUT_POS.setInt(2, pos.y);
            PUT_POS.setInt(3, pos.z);
            PUT_POS.setString(4, pos.world.getUID().toString());
            PUT_POS.setString(5, ScriptID);
            PUT_POS.setObject(6, values);
            PUT_POS.executeUpdate();
            return true;
        } catch (final Exception ex) {
            if(instance.getPluginStat().isDebugMode){
                ex.printStackTrace();
            }
            return false;
        }
    }

    public boolean PutArea(final ScriptPos pos1, final ScriptPos pos2, final String AreaID, final String ScriptID,
            final HashMap<String,Object> values) {
        try {
            PUT_AREA.setInt(1, pos1.x);
            PUT_AREA.setInt(2, pos1.y);
            PUT_AREA.setInt(3, pos1.z);
            PUT_AREA.setInt(4, pos2.x);
            PUT_AREA.setInt(5, pos2.y);
            PUT_AREA.setInt(6, pos2.z);
            PUT_AREA.setString(7, pos1.world.getUID().toString());
            PUT_AREA.setString(8, AreaID);
            PUT_AREA.setString(9, ScriptID);
            PUT_AREA.setObject(10, values);
            int af = PUT_AREA.executeUpdate();
            return af == 1;
        } catch (final Exception ex) {
            if(instance.getPluginStat().isDebugMode){
                ex.printStackTrace();
            }
            return false;
        }
    }
}
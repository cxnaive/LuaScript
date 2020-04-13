package cxmc;
import org.bukkit.plugin.java.JavaPlugin;
/**
 * Hello world!
 *
 */
public class LuaScript extends JavaPlugin {
    @Override
    public void onEnable() {
        getLogger().info("Hello, SpigotMC!");
    }
    @Override
    public void onDisable() {
        getLogger().info("See you again, SpigotMC!");
    }
}

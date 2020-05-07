package cxmc;

import cxmc.text.TextBuilder;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;

public class PluginStat {
    public boolean isEconEnabled;
    public boolean isVpermEnabled;
    public boolean isH2Enabled;
    public boolean isLuckPermsEnabled;
    public boolean isDebugMode;
    public PluginStat(){
        isEconEnabled = true;
        isH2Enabled = false;
        isVpermEnabled = true;
        isLuckPermsEnabled = true;
        isDebugMode = false;
    }
    String bool2str(boolean v){
        return v ? "On\n":"Off\n";
    }
    ChatColor bool2color(boolean v){
        return v ? ChatColor.GREEN:ChatColor.RED;
    }
    public TextComponent info(){
        return TextBuilder.of("Economy:").setColor(ChatColor.AQUA)
        .append(TextBuilder.of(bool2str(isEconEnabled)).setColor(bool2color(isEconEnabled)).build())
        .append(TextBuilder.of("Debug Mode:").setColor(ChatColor.AQUA).build())
        .append(TextBuilder.of(bool2str(isDebugMode)).setColor(bool2color(isDebugMode)).build())
        .append(TextBuilder.of("Vault Permission:").setColor(ChatColor.AQUA).build())
        .append(TextBuilder.of(bool2str(isVpermEnabled)).setColor(bool2color(isVpermEnabled)).build())
        .append(TextBuilder.of("LuckPerms Permission:").setColor(ChatColor.AQUA).build())
        .append(TextBuilder.of(bool2str(isLuckPermsEnabled)).setColor(bool2color(isLuckPermsEnabled)).build())
        .append(TextBuilder.of("Plugin Status:").setColor(ChatColor.AQUA).build())
        .append(TextBuilder.of(bool2str(isH2Enabled)).setColor(bool2color(isH2Enabled)).build())
        .build();
    }
}
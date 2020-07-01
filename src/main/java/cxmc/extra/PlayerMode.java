package cxmc.extra;

import cxmc.text.TextBuilder;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;

public enum PlayerMode {
    Disabled,ViewMode,DelMode,SetMode,AreaMode;
    public TextComponent getText(){
        switch(this){
            case Disabled:
                return TextBuilder.of(this.name()).setColor(ChatColor.GRAY).build();
            case ViewMode:
                return TextBuilder.of(this.name()).setColor(ChatColor.AQUA).build();
            case DelMode:
                return TextBuilder.of(this.name()).setColor(ChatColor.RED).build();
            case SetMode:
                return TextBuilder.of(this.name()).setColor(ChatColor.YELLOW).build();
            case AreaMode:
                return TextBuilder.of(this.name()).setColor(ChatColor.GOLD).build();
            default:
                return null;
        }
    }
    public static PlayerMode getMode(String text){
        text = text.toLowerCase();
        switch(text){
            case "disabled":
                return PlayerMode.Disabled;
            case "viewmode":
                return PlayerMode.ViewMode;
            case "delmode":
                return PlayerMode.DelMode;
            case "setmode":
                return PlayerMode.SetMode;
            case "areamode":
                return PlayerMode.AreaMode;
            default:
                return null;
        }
    }
}
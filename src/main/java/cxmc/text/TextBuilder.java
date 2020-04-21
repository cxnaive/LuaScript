package cxmc.text;


import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;

public class TextBuilder {
    TextComponent content;
    public TextBuilder(String content){
        this.content = new TextComponent(content);
    }
    public TextBuilder setColor(ChatColor color){
        this.content.setColor(color);
        return this;
    }
    public TextBuilder setBold(boolean isBold){
        this.content.setBold(isBold);
        return this;
    }
    public TextBuilder setUnderlined(boolean underlined){
        this.content.setUnderlined(underlined);
        return this;
    }
    public TextBuilder append(String content){
        this.content.addExtra(content);
        return this;
    }
    public TextBuilder append(TextComponent text){
        this.content.addExtra(text);
        return this;
    }
    public TextComponent build(){
        return this.content;
    }
    public static TextBuilder of(String content){
        return new TextBuilder(content);
    }
}
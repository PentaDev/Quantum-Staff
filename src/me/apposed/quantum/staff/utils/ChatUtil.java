package me.apposed.quantum.staff.utils;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChatUtil {

    public static String translate(String msg){
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    public static void translateMSG(Player p, String msg){
        p.sendMessage(translate(msg));
    }

    public static void translateMSG(CommandSender cs, String msg){
        cs.sendMessage(translate(msg));
    }

    public static void noPermission(Player p){
        translateMSG(p, "&cNo Permission.");
    }

    public static void noConsole(CommandSender cs){
        translateMSG(cs, "&cConsole cannot do this.");
    }
}

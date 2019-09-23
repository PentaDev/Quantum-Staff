package me.apposed.quantum.staff.commands;

import me.apposed.quantum.staff.utils.ChatUtil;
import me.apposed.quantum.staff.utils.StaffUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class VanishCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if(cmd.getName().equalsIgnoreCase("vanish")){
            if(cs instanceof Player){
                // Is a player
                Player p = (Player) cs;
                if(p.hasPermission("quantum.staff.vanish")){
                    // Has permission
                    if(StaffUtil.isVanished(p)){
                        // Is Vanished
                        if(StaffUtil.isInStaffMode(p)){
                            // Unvanish and Update items
                            StaffUtil.setVanished(p, false, true);
                            return true;
                        }
                        // Unvanish
                        StaffUtil.setVanished(p, false, false);
                        return true;
                    }
                    // Is not vanished
                    if(StaffUtil.isInStaffMode(p)){
                        // Vanish and Update items
                        StaffUtil.setVanished(p, true, true);
                        return true;
                    }
                    // Vanish
                    StaffUtil.setVanished(p, true, false);
                    return true;
                }
                // No permission
                ChatUtil.noPermission(p);
                return true;
            }
            // Console
            ChatUtil.noConsole(cs);
            return true;
        }
        return false;
    }
}

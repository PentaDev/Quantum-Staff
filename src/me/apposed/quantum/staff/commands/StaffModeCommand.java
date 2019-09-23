package me.apposed.quantum.staff.commands;

import me.apposed.quantum.staff.utils.ChatUtil;
import me.apposed.quantum.staff.utils.StaffUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StaffModeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if(cmd.getName().equalsIgnoreCase("staffmode")){
            if(cs instanceof Player){
                // Is a player
                Player p = (Player) cs;
                if(p.hasPermission("quantum.staff.staffmode")){
                    // Has Permission
                    if(StaffUtil.isInStaffMode(p)){
                        // In Staff Mode
                        StaffUtil.leaveStaffMode(p);
                        return true;
                    }
                    // Not in Staff Mode
                    StaffUtil.enterStaffMode(p);
                    return true;
                }
                // Console
                ChatUtil.noPermission(p);
                return true;
            }
            ChatUtil.noConsole(cs);
            return true;
        }
        return false;
    }
}

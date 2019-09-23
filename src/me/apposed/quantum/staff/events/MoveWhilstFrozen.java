package me.apposed.quantum.staff.events;

import me.apposed.quantum.staff.Quantum;
import me.apposed.quantum.staff.utils.ChatUtil;
import me.apposed.quantum.staff.utils.StaffUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class MoveWhilstFrozen implements Listener {

	@EventHandler
	public void onMoveFrozen(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		if (StaffUtil.isFrozen(p)) {
			// user is frozen
			e.setTo(e.getFrom());
			ChatUtil.translateMSG(p, Quantum.getInstance().getConfig().getString("currently_frozen").replaceAll("%teamspeak%", Quantum.getInstance().getConfig().getString("teamspeak_ip")));
		}
	}
}

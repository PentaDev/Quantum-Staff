package me.apposed.quantum.staff.events;

import me.apposed.quantum.staff.Quantum;
import me.apposed.quantum.staff.utils.ChatUtil;
import me.apposed.quantum.staff.utils.ItemBuilder;
import me.apposed.quantum.staff.utils.StaffUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class StaffModeEvents implements Listener {

	HashMap<Player, Location> availableTP = new HashMap<>();

	@EventHandler
	public void vanishChange(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		Action a = e.getAction();
		if (StaffUtil.inStaffMode.contains(p)) {
			if (a == Action.RIGHT_CLICK_BLOCK || a == Action.RIGHT_CLICK_AIR) {
				if (p.getItemInHand().getItemMeta().getDisplayName().contains("Toggle Vanish") && StaffUtil.isVanished(p)) {
					StaffUtil.setVanished(p, false, true);
					return;
				}
				if (p.getItemInHand().getItemMeta().getDisplayName().contains("Toggle Vanish") && !StaffUtil.isVanished(p)) {
					StaffUtil.setVanished(p, true, true);
					return;
				}
			}
		}
	}

	@EventHandler
	public void clockInteract(PlayerInteractEvent e) {
		ItemStack clock = new ItemBuilder(Material.WATCH).setName(ChatUtil.translate(Quantum.getInstance().getConfig().getString("staffmode.items.clock.name"))).build();

		Player p = e.getPlayer();
		if (StaffUtil.isInStaffMode(p) && e.getItem().equals(clock)) {
			if (e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR) {
				for (Player all : Bukkit.getOnlinePlayers()) {
					availableTP.put(all, all.getLocation());
				}

				if (availableTP.size() == 1) {
					ChatUtil.translateMSG(p, "&cThere are no players to be teleported to.");
				} else {
					Random random = new Random();
					List<Player> keys = new ArrayList<>(availableTP.keySet());
					Player teleportedTo = keys.get(random.nextInt(keys.size()));

					p.teleport(availableTP.get(teleportedTo));
					ChatUtil.translateMSG(p, Quantum.getInstance().getConfig().getString("teleport_message").replaceAll("%player%", teleportedTo.getName()));
				}
			}
		}
	}

	@EventHandler
	public void bookInteract(PlayerInteractEntityEvent e) {
		ItemStack book = new ItemBuilder(Material.BOOK).setName(ChatUtil.translate(Quantum.getInstance().getConfig().getString("staffmode.items.book.name"))).build();

		Player p = e.getPlayer();
		Player t = (Player) e.getRightClicked();

		if (StaffUtil.isInStaffMode(p) && p.getItemInHand().equals(book)) {
			p.openInventory(t.getInventory());
			ChatUtil.translateMSG(p, Quantum.getInstance().getConfig().getString("inspect_inventory_message").replaceAll("%player%", t.getName()));
		}
	}

	@EventHandler
	public void iceInteract(PlayerInteractEntityEvent e) {
		ItemStack ice = new ItemBuilder(Material.PACKED_ICE).setName(ChatUtil.translate(Quantum.getInstance().getConfig().getString("staffmode.items.ice.name"))).build();


		Player p = e.getPlayer();
		Player t = (Player) e.getRightClicked();

		if (StaffUtil.isInStaffMode(p) && p.getItemInHand().equals(ice)) {
			StaffUtil.toggleFreeze(p, t);
		}
	}


	@EventHandler
	public void moveWhilstFrozen(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		// TODO: This event
	}


}

package me.apposed.quantum.staff.events;

import me.apposed.quantum.staff.Quantum;
import me.apposed.quantum.staff.utils.ChatUtil;
import me.apposed.quantum.staff.utils.StaffUtil;
import org.bukkit.block.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;

import java.util.HashSet;

public class CancelEvents implements Listener {

	private final HashSet<String> haveInventoriesOpen = new HashSet<>();

	@EventHandler
	public void itemPickup(PlayerPickupItemEvent e) {
		Player p = e.getPlayer();
		if (StaffUtil.inStaffMode.contains(p) || StaffUtil.isVanished(p)) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onDamage(EntityDamageByEntityEvent e) {
		Player p = (Player) e.getDamager();
		Player victim = (Player) e.getEntity();
		if (StaffUtil.inStaffMode.contains(p) && victim instanceof Player || StaffUtil.isVanished(p) && victim instanceof Player) {
			e.setCancelled(true);
			ChatUtil.translateMSG(p, "&cYou cannot harm people whilst vanished or in staffmode.");
		}
	}

	@EventHandler
	public void itemDrop(PlayerDropItemEvent e) {
		Player p = e.getPlayer();
		if (StaffUtil.inStaffMode.contains(p) || StaffUtil.isVanished(p)) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void breakBlock(BlockBreakEvent e) {
		Player p = e.getPlayer();
		if (StaffUtil.inStaffMode.contains(p) || StaffUtil.isVanished(p)) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void placeBlock(BlockPlaceEvent e) {
		Player p = e.getPlayer();
		if (StaffUtil.inStaffMode.contains(p) || StaffUtil.isVanished(p)) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void inventoryMoveInteract(InventoryMoveItemEvent e) {
		Player p = (Player) e.getInitiator().getHolder();
		if (StaffUtil.inStaffMode.contains(p) || StaffUtil.isVanished(p)) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void inventoryInteractEvent(InventoryInteractEvent e) {
		Player p = (Player) e.getWhoClicked();
		if (StaffUtil.inStaffMode.contains(p) || StaffUtil.isVanished(p)) {
			e.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void silentInteract(PlayerInteractEvent event) {
		final Player player = event.getPlayer();
		if (chestFakeInUse(player.getName()) && !player.isSneaking() && (event.getAction() == Action.RIGHT_CLICK_BLOCK) && StaffUtil.isVanished(player)) {
			final Block block = event.getClickedBlock();
			Inventory inventory = null;
			final BlockState blockState = block.getState();
			boolean fake = false;
			switch (block.getType()) {
				case TRAPPED_CHEST:
				case CHEST:
					final Chest chest = (Chest) blockState;
					inventory = Quantum.getInstance().getServer().createInventory(player, chest.getInventory().getSize());
					inventory.setContents(chest.getInventory().getContents());
					fake = true;
					break;
				case DISPENSER:
					inventory = ((Dispenser) blockState).getInventory();
					break;
				case HOPPER:
					inventory = ((Hopper) blockState).getInventory();
					break;
				case DROPPER:
					inventory = ((Dropper) blockState).getInventory();
					break;
				case FURNACE:
					inventory = ((Furnace) blockState).getInventory();
					break;
				case BREWING_STAND:
					inventory = ((BrewingStand) blockState).getInventory();
					break;
				case BEACON:
					inventory = ((Beacon) blockState).getInventory();
					break;
			}
			if (inventory != null) {
				event.setCancelled(true);
				if (fake) {
					chestFakeOpen(player.getName());
				}
				player.openInventory(inventory);
				return;
			}
		}
		if (StaffUtil.isVanished(player)) {
			event.setCancelled(true);
			return;
		}
	}

	@EventHandler
	public void itemMove(InventoryMoveItemEvent e) {
		Player p = (Player) e.getInitiator();
		if (StaffUtil.inStaffMode.contains(p)) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void itemDrag(InventoryDragEvent e) {
		Player p = (Player) e.getWhoClicked();
		if (StaffUtil.inStaffMode.contains(p)) {
			e.setCancelled(true);
		}
	}

	@EventHandler

	public void onLeave(PlayerQuitEvent e) {
		chestFakeClose(e.getPlayer().getName());
	}

	public void chestFakeClose(String name) {
		synchronized (this.haveInventoriesOpen) {
			this.haveInventoriesOpen.remove(name);
		}
	}

	public boolean chestFakeInUse(String name) {
		synchronized (this.haveInventoriesOpen) {
			return this.haveInventoriesOpen.contains(name);
		}
	}

	public void chestFakeOpen(String name) {
		synchronized (this.haveInventoriesOpen) {
			this.haveInventoriesOpen.add(name);
		}
	}
}

package me.apposed.quantum.staff.utils;

import me.apposed.quantum.staff.Quantum;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class StaffUtil {

	private static HashMap<UUID, ItemStack[]> inventoryContents = new HashMap<>();
	private static HashMap<UUID, ItemStack[]> inventoryArmorContents = new HashMap<>();

	public static ArrayList<Player> vanishedPlayers = new ArrayList<>();
	public static ArrayList<Player> inStaffMode = new ArrayList<>();
	public static ArrayList<Player> frozenPlayers = new ArrayList<>();

	public static void updateGamemode(Player p, GameMode gamemode) {
		p.setGameMode(gamemode);
		ChatUtil.translateMSG(p, Quantum.getInstance().getConfig().getString("gamemode_update").replaceAll("%gamemode%", p.getGameMode().toString().toUpperCase()));
	}

	public static void enterStaffMode(Player p) {
		ChatUtil.translateMSG(p, Quantum.getInstance().getConfig().getString("enter_staffmode"));
		updateGamemode(p, GameMode.CREATIVE);
		inStaffMode.add(p);
		setVanished(p, true, false);

		saveInventory(p);
		p.getInventory().clear();

		ItemStack clock = new ItemBuilder(Material.WATCH).setName(ChatUtil.translate(Quantum.getInstance().getConfig().getString("staffmode.items.clock.name"))).is;
		ItemStack compass = new ItemBuilder(Material.COMPASS).setName(ChatUtil.translate(Quantum.getInstance().getConfig().getString("staffmode.items.compass.name"))).is;
		ItemStack book = new ItemBuilder(Material.BOOK).setName(ChatUtil.translate(Quantum.getInstance().getConfig().getString("staffmode.items.book.name"))).is;
		ItemStack ice = new ItemBuilder(Material.PACKED_ICE).setName(ChatUtil.translate(Quantum.getInstance().getConfig().getString("staffmode.items.ice.name"))).is;
		ItemStack vanished = new ItemBuilder(Material.INK_SACK).setAmount(1).setDurability((short) 10).setName(ChatUtil.translate(Quantum.getInstance().getConfig().getString("staffmode.items.vanish.name"))).is;
		ItemStack visible = new ItemBuilder(Material.INK_SACK).setAmount(1).setDurability((short) 8).setName(ChatUtil.translate(Quantum.getInstance().getConfig().getString("staffmode.items.vanish.name"))).is;

		Inventory inv = p.getInventory();

		inv.setItem(0, clock);
		inv.setItem(1, compass);
		inv.setItem(2, book);
		inv.setItem(3, ice);


		if (isVanished(p)) {
			inv.setItem(8, vanished);
		} else {
			inv.setItem(8, visible);
		}
	}

	public static void leaveStaffMode(Player p) {
		inStaffMode.remove(p);
		ChatUtil.translateMSG(p, Quantum.getInstance().getConfig().getString("leave_staffmode"));
		updateGamemode(p, GameMode.SURVIVAL);
		setVanished(p, false, false);

		Inventory inv = p.getInventory();
		inv.clear();
		loadInventory(p);
	}

	private static void saveInventory(Player p) {
		inventoryContents.remove(p.getUniqueId());
		inventoryArmorContents.remove(p.getUniqueId());
		inventoryContents.put(p.getUniqueId(), p.getInventory().getContents());
		inventoryArmorContents.put(p.getUniqueId(), p.getInventory().getArmorContents());
	}

	private static void loadInventory(Player p) {
		if (inventoryContents.containsKey(p.getUniqueId()) && (inventoryArmorContents.containsKey(p.getUniqueId()))) {
			p.getInventory().setContents(inventoryContents.get(p.getUniqueId()));
			p.getInventory().setArmorContents(inventoryArmorContents.get(p.getUniqueId()));
			inventoryContents.remove(p.getUniqueId());
			inventoryArmorContents.remove(p.getUniqueId());
		}
	}

	public static boolean isInStaffMode(Player p) {
		return inStaffMode.contains(p);
	}

	public static void setVanished(Player p, boolean mode, boolean updateItems) {
		if (mode) {
			vanishedPlayers.add(p);
			for (Player all : Bukkit.getOnlinePlayers()) {
				if (!all.hasPermission("quantum.staff")) {
					all.hidePlayer(p);
				}
			}
			ChatUtil.translateMSG(p, Quantum.getInstance().getConfig().getString("vanish_update").replaceAll("%status%", "&aon"));
			if (updateItems) {
				updateVanishItem(p);
			}
		} else {
			vanishedPlayers.remove(p);
			for (Player all : Bukkit.getOnlinePlayers()) {
				all.showPlayer(p);
			}
			ChatUtil.translateMSG(p, Quantum.getInstance().getConfig().getString("vanish_update").replaceAll("%status%", "&coff"));
			if (updateItems) {
				updateVanishItem(p);
			}
		}
	}

	public static boolean isVanished(Player p) {
		return vanishedPlayers.contains(p);
	}

	public static void updateVanishItem(Player p) {
		if (inStaffMode.contains(p)) {
			Inventory inv = p.getInventory();

			ItemStack vanished = new ItemBuilder(Material.INK_SACK).setAmount(1).setDurability((short) 10).setName(ChatUtil.translate(Quantum.getInstance().getConfig().getString("staffmode.items.vanish.name"))).is;
			ItemStack visible = new ItemBuilder(Material.INK_SACK).setAmount(1).setDurability((short) 8).setName(ChatUtil.translate(Quantum.getInstance().getConfig().getString("staffmode.items.vanish.name"))).is;

			if (isVanished(p)) {
				inv.setItem(8, vanished);
			} else {
				inv.setItem(8, visible);
			}
		}
	}

	public static void toggleFreeze(Player frozenBy, Player frozen) {
		if (isFrozen(frozen)) {
			// Unfreeze Player
			frozenPlayers.remove(frozen);
			ChatUtil.translateMSG(frozen, Quantum.getInstance().getConfig().getString("unfrozen_frozen"));
			ChatUtil.translateMSG(frozenBy, Quantum.getInstance().getConfig().getString("unfrozen_executor").replaceAll("%player%", frozen.getName()));
			for (Player staff : Bukkit.getOnlinePlayers()) {
				if (staff.hasPermission("quantum.staff")) {
					ChatUtil.translateMSG(staff, Quantum.getInstance().getConfig().getString("unfrozen_staff").replaceAll("%executor%", frozenBy.getName()).replaceAll("%player%", frozen.getName()));
				}
			}
		} else {
			// Freeze Player
			frozenPlayers.add(frozen);
			ChatUtil.translateMSG(frozen, Quantum.getInstance().getConfig().getString("frozen_frozen"));
			ChatUtil.translateMSG(frozenBy, Quantum.getInstance().getConfig().getString("frozen_executor").replaceAll("%player%", frozen.getName()));
			for (Player staff : Bukkit.getOnlinePlayers()) {
				if (staff.hasPermission("quantum.staff")) {
					ChatUtil.translateMSG(staff, Quantum.getInstance().getConfig().getString("frozen_staff").replaceAll("%executor%", frozenBy.getName()).replaceAll("%player%", frozen.getName()));
				}
			}
		}
	}

	public static boolean isFrozen(Player p) {
		return frozenPlayers.contains(p);
	}

}

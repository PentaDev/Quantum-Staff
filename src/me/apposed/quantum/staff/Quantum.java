package me.apposed.quantum.staff;

import me.apposed.quantum.staff.commands.StaffModeCommand;
import me.apposed.quantum.staff.commands.VanishCommand;
import me.apposed.quantum.staff.events.CancelEvents;
import me.apposed.quantum.staff.events.MoveWhilstFrozen;
import me.apposed.quantum.staff.events.StaffModeEvents;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Quantum extends JavaPlugin {

	private static Quantum instance;

	@Override
	public void onEnable() {
		System.out.println("Initializing Quantum Staff");
		System.out.println("Registering config.yml file...");
		this.registerConfig();
		System.out.println("Successfully Registered config.yml file!");
		if (this.checkLicense()) {
			System.out.println("--------------------");
			System.out.println("License \"" + this.getConfig().getString("license_key") + "\" Validated");
			System.out.println("Thanks for purchasing Quantum Staff");
			System.out.println("Need Help? Have Questions? Developer's Discord - Alf#7040");
			System.out.println("--------------------");
			System.out.println("Registering Instances...");
			this.registerInstances();
			System.out.println("Successfully Registered instances!");
			System.out.println("Registering Commands...");
			this.registerCommands();
			System.out.println("Successfully Registered commands!");
			System.out.println("Registering Events...");
			this.registerEvents();
			System.out.println("Successfully Registered events!");
			System.out.println("Registering Managers...");
			this.registerManagers();
			System.out.println("Successfully Registered managers!");
		} else {
			System.out.println("--------------------");
			System.out.println("License Invalid");
			System.out.println("Leaked Version of Quantum Staff");
			System.out.println("Mistake? Developers Discord - Alf#7040");
			System.out.println("--------------------");
			Bukkit.getServer().shutdown();
		}
	}

	@Override
	public void onDisable() {
		System.out.println("--------------------");
		System.out.println("Disabling Quantum Staff");
		System.out.println("Need Support? Developers Discord - Alf#7040");
		System.out.println("--------------------");
		this.saveConfig();
	}

	public static Quantum getInstance() {
		return instance;
	}

	public void registerConfig() {
		this.getConfig().options().copyDefaults(true);
		this.saveDefaultConfig();
		this.saveConfig();
	}

	public boolean checkLicense() {
		if (this.getConfig().contains("license_key")) {
			String license = getConfig().getString("license_key");

			try {
				URL url = new URL("https://nohaxjustbridgers.000webhostapp.com/quantum/staff/license_keys.txt");
				// Testing HWID - e3ef6e966a8c
				Scanner input = new Scanner(url.openStream());

				List<String> lines = new ArrayList<>();

				int lineNum = 0;
				while (input.hasNextLine()) {
					String line = input.nextLine();
					lineNum++;
					lines.add(line);
				}

				if (!lines.contains(license)) {
					return false;
				} else {
					return true;
				}
			} catch (IOException ex) {
				ex.printStackTrace();
				return false;
			}
		} else {
			return false;
		}
	}

	public void registerInstances() {
		instance = this;
	}

	public void registerCommands() {
		getCommand("staffmode").setExecutor(new StaffModeCommand());
		getCommand("vanish").setExecutor(new VanishCommand());
	}

	public void registerEvents() {
		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new CancelEvents(), this);
		pm.registerEvents(new StaffModeEvents(), this);
		pm.registerEvents(new MoveWhilstFrozen(), this);
	}

	public void registerManagers() {

	}

}

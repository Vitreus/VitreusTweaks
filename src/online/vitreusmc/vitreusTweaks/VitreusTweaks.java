package online.vitreusmc.vitreusTweaks;

import java.util.logging.Logger;

import org.bukkit.Server;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import online.vitreusmc.vitreusTweaks.armorStand.ManipulateArmorStandCommand;
import online.vitreusmc.vitreusTweaks.gags.PickupCommand;
import online.vitreusmc.vitreusTweaks.sleepVoting.SleepListener;
import online.vitreusmc.vitreusTweaks.sleepVoting.SleepyCommand;

// Main plugin class. Everything starts here.
public class VitreusTweaks extends JavaPlugin {

	// Class Fields
	private Server server;
	private Logger logger;
	private FileConfiguration config;
	
	// Runs when Spigot enables the plugin.
	@Override
	public void onEnable() {		
		this.server = getServer();
		this.logger = getLogger();
		this.config = getConfig();
		
		logger.info("VitreusTweaks Enabled");
		
		setupConfig();
		registerListeners();
		registerExecutors();
	}
	
	private void setupConfig() {
		saveConfig();
	}
	
	// Initializes event listeners and registers them with the plugin manager.
	private void registerListeners() {
		Listener sleepListener = new SleepListener();
		
		server.getPluginManager().registerEvents(sleepListener, this);
	}
	
	// Initializes command executors and registers them with the plugin manager.
	private void registerExecutors() {
		CommandExecutor manipulateArmorStandCommand = new ManipulateArmorStandCommand();
		CommandExecutor sleepyCommand = new SleepyCommand();
		CommandExecutor pickupCommand = new PickupCommand();
		
		getCommand("manipulate_armor_stand").setExecutor(manipulateArmorStandCommand);
		getCommand("sleepy").setExecutor(sleepyCommand);
		getCommand("pickup").setExecutor(pickupCommand);			
	}
}

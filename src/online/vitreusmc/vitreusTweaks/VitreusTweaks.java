package online.vitreusmc.vitreusTweaks;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import online.vitreusmc.vitreusTweaks.armorstand.ManipulateArmorStandCommand;
import online.vitreusmc.vitreusTweaks.end.DragonListener;
import online.vitreusmc.vitreusTweaks.gags.PickupCommand;
import online.vitreusmc.vitreusTweaks.navigation.CompassCommand;
import online.vitreusmc.vitreusTweaks.navigation.MapCommand;
import online.vitreusmc.vitreusTweaks.navigation.PortalCommand;
import online.vitreusmc.vitreusTweaks.sleep.SleepListener;
import online.vitreusmc.vitreusTweaks.sleep.SleepyCommand;

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
		server.getPluginManager().registerEvents(new SleepListener(), this);
		server.getPluginManager().registerEvents(new DragonListener(), this);
	}
	
	// Initializes command executors and registers them with the plugin manager.
	private void registerExecutors() {
		getCommand("armorstand").setExecutor(new ManipulateArmorStandCommand());
		getCommand("sleepy").setExecutor(new SleepyCommand());
		getCommand("pickup").setExecutor(new PickupCommand());	
		getCommand("map").setExecutor(new MapCommand());
		getCommand("compass").setExecutor(new CompassCommand());
		getCommand("portal").setExecutor(new PortalCommand());
	}
}

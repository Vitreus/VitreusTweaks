package online.vitreusmc.vitreusTweaks;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Server;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import online.vitreusmc.vitreusTweaks.armorstand.ManipulateArmorStandCommand;
import online.vitreusmc.vitreusConnect.VitreusDB;
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
	private YamlConfiguration dbConfig = new YamlConfiguration();
	private VitreusDB database;
	
	// Runs when Spigot enables the plugin.
	@Override
	public void onEnable() {		
		this.server = getServer();
		this.logger = getLogger();
		this.config = getConfig();
		
		logger.info("VitreusTweaks Enabled");
		
		setupDatabase();
		registerListeners();
		registerExecutors();
	}
	
	private void setupDatabase() {
		loadDatabaseConfig();
		
		database = new VitreusDB(dbConfig);
		database.connect();
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
	
	private void loadDatabaseConfig() {
		File configFile = new File(getDataFolder().getParentFile().getParentFile(), "vmc-db.yml");
		
		try {
			if (configFile.createNewFile()) logger.log(Level.WARNING, "Database Configuration was just Created and needs to be Populated...");
			
			dbConfig.load(configFile);
			dbConfig.addDefault("db.name", "vitreus");
			dbConfig.addDefault("db.host", "localhost");
			dbConfig.addDefault("db.port", 27017);
			dbConfig.addDefault("db.user.authDBName", "auth");
			dbConfig.addDefault("db.user.name", "admin");
			dbConfig.addDefault("db.user.password", "password");
			dbConfig.options().copyDefaults(true);
			dbConfig.save(configFile);
		} catch (Exception exception) {
			logger.log(Level.SEVERE, "Database Configuration Failed to Load..." + exception.getMessage(), exception);
		}

	}
}

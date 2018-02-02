package online.vitreusmc.vitreusTweaks;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Server;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import online.vitreusmc.vitreusTweaks.armorstand.ManipulateArmorStandCommand;
import online.vitreusmc.vitreusConnect.VitreusDB;
import online.vitreusmc.vitreusTweaks.end.DragonListener;
import online.vitreusmc.vitreusTweaks.gags.PickupCommand;
import online.vitreusmc.vitreusTweaks.navigation.CompassCommand;
import online.vitreusmc.vitreusTweaks.navigation.MapCommand;
import online.vitreusmc.vitreusTweaks.navigation.PortalCommand;
import online.vitreusmc.vitreusTweaks.sleep.SleepListener;
import online.vitreusmc.vitreusTweaks.sleep.SleepyCommand;

public class VitreusTweaks extends JavaPlugin {

	private Server server;
	private Logger logger;
	private YamlConfiguration DBConfig = new YamlConfiguration();
	private VitreusDB database;
	
	@Override
	public void onEnable() {		
		this.server = getServer();
		this.logger = getLogger();
				
		setupDatabase();
		registerListeners();
		registerExecutors();
	}
	
	private void setupDatabase() {
		loadDatabaseConfig();
		
		database = new VitreusDB(DBConfig);
		database.connect();
	}
	
	private void registerListeners() {	
		server.getPluginManager().registerEvents(new SleepListener(), this);
		server.getPluginManager().registerEvents(new DragonListener(), this);
	}
	
	private void registerExecutors() {
		getCommand("armorstand").setExecutor(new ManipulateArmorStandCommand());
		getCommand("sleepy").setExecutor(new SleepyCommand());
		getCommand("pickup").setExecutor(new PickupCommand());	
		getCommand("map").setExecutor(new MapCommand());
		getCommand("compass").setExecutor(new CompassCommand());
		getCommand("portal").setExecutor(new PortalCommand());
	}
	
	private void loadDatabaseConfig() {
		// Loads database configuration file located in the server's base directory.
		File configFile = new File("./vmc-db.yml");

		try {
			if (configFile.createNewFile()) logger.log(Level.WARNING, "Database Configuration was just Created and needs to be Populated...");
			
			DBConfig.load(configFile);
			DBConfig.addDefault("db.name", "vitreus");
			DBConfig.addDefault("db.host", "localhost");
			DBConfig.addDefault("db.port", 27017);
			DBConfig.addDefault("db.user.authDBName", "auth");
			DBConfig.addDefault("db.user.name", "admin");
			DBConfig.addDefault("db.user.password", "password");
			DBConfig.options().copyDefaults(true);
			DBConfig.save(configFile);
		}
		catch (IOException exception) {
			logger.log(Level.SEVERE, "Could not load database configuration file" + exception.getMessage(), exception);
		}
		catch (InvalidConfigurationException exception) {
			logger.log(Level.SEVERE, "Could not load database configuration file" + exception.getMessage(), exception);
		}

	}
}

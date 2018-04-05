package com.vitreusmc.tweaks;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Server;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.mongodb.morphia.Datastore;

import com.vitreusmc.lib.database.DatastoreFactory;
import com.vitreusmc.tweaks.armorstand.ManipulateArmorStandCommand;
import com.vitreusmc.tweaks.end.DragonListener;
import com.vitreusmc.tweaks.gags.PickupCommand;
import com.vitreusmc.tweaks.navigation.CompassCommand;
import com.vitreusmc.tweaks.navigation.MapCommand;
import com.vitreusmc.tweaks.navigation.PortalCommand;
import com.vitreusmc.tweaks.sleep.SleepMonitor;

public class VitreusTweaks extends JavaPlugin {

	private Server server;
	private Logger logger;
	private YamlConfiguration DBConfig = new YamlConfiguration();
	private Datastore datastore;
	
	@Override
	public void onEnable() {		
		this.server = getServer();
		this.logger = getLogger();
				
		setupDatabase();
		registerTriggers();
	}
	
	private void setupDatabase() {
		loadDatabaseConfig();
		
		datastore = DatastoreFactory.create(DBConfig);
	}
	
	private void registerTriggers() {
		SleepMonitor sleepMonitor = new SleepMonitor();
		
		getCommand("armorstand").setExecutor(new ManipulateArmorStandCommand());
		getCommand("sleepy").setExecutor(sleepMonitor);
		getCommand("pickup").setExecutor(new PickupCommand());	
		getCommand("map").setExecutor(new MapCommand());
		getCommand("compass").setExecutor(new CompassCommand());
		getCommand("portal").setExecutor(new PortalCommand());
		
		server.getPluginManager().registerEvents(new DragonListener(), this);
		server.getPluginManager().registerEvents(sleepMonitor, this);
	}
	
	private void loadDatabaseConfig() {
		// Loads database configuration file located in the server's base directory.
		File configFile = new File("./vit-db.yml");

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

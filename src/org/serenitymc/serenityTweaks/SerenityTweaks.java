package org.serenitymc.serenityTweaks;

import java.util.logging.Logger;

import org.bukkit.plugin.java.JavaPlugin;

// Main plugin class. Everything starts here.
public class SerenityTweaks extends JavaPlugin {

	// Class Fields
	private Logger logger;
	
	// Runs when Spigot enables the plugin.
	@Override
	public void onEnable() {
		this.logger = getLogger();
		
		registerListeners();
		registerExecutors();
	}
	
	// Initializes event listeners and registers them with the plugin manager.
	private void registerListeners() {
		
	}
	
	// Initializes command executors and registers them with the plugin manager.
	private void registerExecutors() {
		
	}
}

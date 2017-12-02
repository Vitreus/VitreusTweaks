package online.vitreusmc.vitreusTweaks.end;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EnderDragonChangePhaseEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import net.md_5.bungee.api.ChatColor;
import online.vitreusmc.vitreusTweaks.VitreusTweaks;

public class DragonListener implements Listener {

	private static JavaPlugin plugin = JavaPlugin.getPlugin(VitreusTweaks.class);
	
	@EventHandler
	public void onDragonStateChange(EnderDragonChangePhaseEvent event) {
		EnderDragon dragon = event.getEntity();
		Random random = new Random();

		if (!dragon.isCustomNameVisible()) {
			nameDragon(dragon);
		}
		
		if (random.nextBoolean()) {
			return;
		}
		
		switch (random.nextInt(6)) {
			case 0: {
				sendDragonMessage(dragon, "Ready or not...");
				sendDragonMessage(dragon, "Here I come!");
				break;
			}
			case 1: {
				sendDragonMessage(dragon, "GGrrr...");
				break;
			}
			case 2: {
				sendDragonMessage(dragon, "You lousey pests!");
				break;
			}
			case 3: {
				sendDragonMessage(dragon, "Hah! You grow tired human! I can see it!");
				break;
			}
		}
	}
	
	@EventHandler
	public void onDragonHit(EntityDamageByEntityEvent event) {
		Entity entity = event.getEntity();
		Random random = new Random();
		EnderDragon dragon;
		Entity harmer = event.getDamager();
		String harmerName = harmer.getName();
		
		if (!entity.getType().equals(EntityType.ENDER_DRAGON)) {
			return;
		}
		
		if (harmer.getType().equals(EntityType.ARROW)) {
			harmerName = "human";
		}
		
		dragon = (EnderDragon) entity;
		
		if (!dragon.isCustomNameVisible()) {
			nameDragon(dragon);
		}
		
		if (random.nextBoolean()) {
			return;
		}
		
		switch (random.nextInt(10)) {
			case 0: {
				sendDragonMessage(dragon, "You hit me!");
				break;
			}
			case 1: {
				sendDragonMessage(dragon, "Rarr! " + "You dare to annoy me " + harmerName + "?");
				break;
			}
			case 2: {
				sendDragonMessage(dragon, "I'll make a meal of you yet " + harmerName + "!");
				break;
			}
			case 3: {
				sendDragonMessage(dragon, "That hurts you puny animal!");
				break;
			}
		}
	}
	
	@EventHandler
	public void onDragonSlain(EntityDeathEvent event) {
		Entity entity = event.getEntity();
		Random random = new Random();
		EnderDragon dragon;
		
		if (!entity.getType().equals(EntityType.ENDER_DRAGON)) {
			return;
		}
		
		dragon = (EnderDragon) entity;
		
		if (!dragon.isCustomNameVisible()) {
			nameDragon(dragon);
		}
		
		switch (random.nextInt(4)) {
			case 0: {
				sendDragonMessage(dragon, "NNNNOOOOO!!!!!");
				break;
			}
			case 1: {
				sendDragonMessage(dragon, "I have fallen... Avenge me my children!");
				break;
			}
			case 2: {
				sendDragonMessage(dragon, "I will return to conquer what is mine!");
				break;
			}
			case 3: {
				sendDragonMessage(dragon, "How have I been defeated by meekly humans?");
				break;
			}
		}
	}
	
	@EventHandler
	public void onDragonSpawned(EntitySpawnEvent event) {
		Entity entity = event.getEntity();
		Random random = new Random();
		EnderDragon dragon;
		
		if (!entity.getType().equals(EntityType.ENDER_DRAGON)) {
			return;
		}
		
		dragon = (EnderDragon) entity;

		nameDragon(dragon);
	}
	
	@EventHandler
	private void onPlayerEnterEnd(PlayerChangedWorldEvent event) {
		Collection<World> worlds = Bukkit.getWorlds();
		Collection<EnderDragon> dragons = new ArrayList<EnderDragon>();		
		
		for (World world : worlds) {
			dragons.addAll(world.getEntitiesByClass(EnderDragon.class));
		}
		
		if (dragons.isEmpty()) {
			return;
		}
		
		for (EnderDragon dragon : dragons) {
			if (dragon.isCustomNameVisible()) {
				return;
			}
			
			nameDragon(dragon);
		}
	}
	
	private static void nameDragon(EnderDragon dragon) {
		Random random = new Random();
		
		switch (random.nextInt(4)) {
			case 0: {
				dragon.setCustomName("Esmer the Dragon");
				sendDragonMessage(dragon, "RRRRAAARR! You dare to awaken me?");
				break;
			}
			case 1: {
				dragon.setCustomName("Timel of the Torn Veil");
				sendDragonMessage(dragon, "My flames will pierce your souls.");
				break;
			}
			case 2: {
				dragon.setCustomName("Mother of the End");
				sendDragonMessage(dragon, "Ooooh... Children of Earth!");
				
				new BukkitRunnable() {
					@Override
					public void run() {
						sendDragonMessage(dragon, "Tastey...");
					}
				}.runTaskLaterAsynchronously(plugin, 30);
				
				break;
			}
			case 3: {
				dragon.setCustomName("The Dragon of Day's End");
				sendDragonMessage(dragon, "Hmff... You threaten me not. You will all die. Fools...");
				break;
			}
		}
	
		dragon.setCustomNameVisible(true);
	}
	
	private static void sendDragonMessage(EnderDragon dragon, String message) {
		Collection<Entity> entities = dragon.getNearbyEntities(300, 300, 300);

		for (Entity entity : entities) {
			entity.sendMessage(ChatColor.LIGHT_PURPLE + dragon.getCustomName() + ChatColor.GRAY + "" + ChatColor.BOLD + ": " + ChatColor.RESET + "" + ChatColor.DARK_PURPLE + message);
		}
	}
}

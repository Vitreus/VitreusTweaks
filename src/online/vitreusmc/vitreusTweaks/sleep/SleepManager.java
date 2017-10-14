package online.vitreusmc.vitreusTweaks.sleep;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import net.md_5.bungee.api.ChatColor;
import online.vitreusmc.vitreusTweaks.VitreusTweaks;

public class SleepManager {

	private static ArrayList<Player> sleepingPlayers = new ArrayList<Player>();
	private static ArrayList<Player> sleepyPlayers = new ArrayList<Player>();
	private static World overworld;
	private static boolean someoneSleeping;
	private static boolean voting;
	
	public static void sendStatusUpdate(Server server) {
		server.broadcastMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "Sleep: " + ChatColor.RESET + ChatColor.DARK_AQUA + "Number of Sleepy Sheep > " + getSleepingPercent() + "%");
	}
	
	public static void startSleepVote(Server server) {
		server.broadcastMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "Sleep: " + ChatColor.RESET + ChatColor.DARK_AQUA + "Someone fell asleep and is dreaming of sheep! Type " + ChatColor.BOLD + ChatColor.AQUA + "/sleepy" + ChatColor.RESET + ChatColor.DARK_AQUA + " to join them!");
	}
	
	public static void failSleepVote(Server server) {
		server.broadcastMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "Sleep: " + ChatColor.RESET + ChatColor.RED + "No one is sleeping anymore!");
		sleepingPlayers = new ArrayList<Player>();
		sleepyPlayers = new ArrayList<Player>();
		someoneSleeping = false;
		voting = false;
	}
	
	public static void passSleepVote(Server server) {		
		new BukkitRunnable() {
			@Override
			public void run() {
				server.broadcastMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "Sleep: " + ChatColor.RESET + ChatColor.GREEN + "Good morning... Vitreus!");
				overworld.setTime(0);
				
				if (overworld.hasStorm()) {
					overworld.setWeatherDuration(1);					
				}
				
				sleepingPlayers = new ArrayList<Player>();
				sleepyPlayers = new ArrayList<Player>();
				someoneSleeping = false;
				voting = false;
			}
		}.runTaskLaterAsynchronously(JavaPlugin.getPlugin(VitreusTweaks.class), 20);
	}
	
	public static void addSleepingPlayer(Player player) {
		Server server = player.getServer();		
		overworld = player.getWorld();
		
		sleepingPlayers.add(player);
		
		if (getSleepingPercent() > 50 && voting) {
			sendStatusUpdate(server);
			passSleepVote(server);
			voting = false;
		} else {
			sendStatusUpdate(server);
			voting = true;
			someoneSleeping = true;
		}		
	}
	
	public static void removeSleepingPlayer(Player player) {
		Server server = player.getServer();
		
		sleepingPlayers.remove(player);
		
		if (sleepingPlayers.size() > 0) {
			sendStatusUpdate(server);
		} else if (voting) {
			failSleepVote(server);
			voting = false;
			someoneSleeping = false;
		}
	}
	
	public static void addSleepyPlayer(Player player) {
		Server server = player.getServer();
		
		sleepyPlayers.add(player);
		
		if (getSleepingPercent() > 50 && voting) {
			passSleepVote(server);
			voting = false;
		} else {
			sendStatusUpdate(server);
		}	
	}
	
	public static void removeSleepyPlayer(Player player) {
		Server server = player.getServer();
		
		sleepyPlayers.remove(player);
		
		sendStatusUpdate(server);
	}
	
	public static double getSleepingPercent() {
		List<Player> onlinePlayers = overworld.getPlayers();
		List<Player> totalSleepingPlayers = new ArrayList();
		
		totalSleepingPlayers.addAll(sleepingPlayers);
		totalSleepingPlayers.addAll(sleepyPlayers);
				
		return (int) (((double) ((float) totalSleepingPlayers.size() / (float) onlinePlayers.size())) * 100);
	}
	
	public static boolean isSomeoneSleeping() {
		return someoneSleeping;
	}
	
	public static ArrayList<Player> getSleepingPlayers() {
		return sleepingPlayers;
	}
	
	public static ArrayList<Player> getSleepyPlayers() {
		return sleepyPlayers;
	}
}

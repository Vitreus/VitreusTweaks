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
	private static boolean someoneSleeping;
	private static boolean voting;
	
	public static void sendStatusUpdate(Server server, World world) {
		server.broadcastMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "Sleep: " + ChatColor.RESET + ChatColor.DARK_AQUA + "Number of Sleepy Sheep > " + getSleepingPercent(world) + "%");
	}
	
	public static void startSleepVote(Server server, World world) {
		server.broadcastMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "Sleep: " + ChatColor.RESET + ChatColor.DARK_AQUA + "Someone fell asleep and is dreaming of sheep! Type " + ChatColor.BOLD + ChatColor.AQUA + "/sleepy" + ChatColor.RESET + ChatColor.DARK_AQUA + " to join them!");
	}
	
	public static void failSleepVote(Server server, World world) {
		server.broadcastMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "Sleep: " + ChatColor.RESET + ChatColor.RED + "No one is sleeping anymore!");
		sleepingPlayers = new ArrayList<Player>();
		sleepyPlayers = new ArrayList<Player>();
		someoneSleeping = false;
		voting = false;
	}
	
	public static void passSleepVote(Server server, World world) {
		new BukkitRunnable() {
			@Override
			public void run() {
				server.broadcastMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "Sleep: " + ChatColor.RESET + ChatColor.GREEN + "Good morning... Vitreus!");
				world.setTime(0);
				
				if (world.hasStorm()) {
					world.setWeatherDuration(1);					
				}
				
				sleepingPlayers = new ArrayList<Player>();
				sleepyPlayers = new ArrayList<Player>();
				someoneSleeping = false;
				voting = false;
			}
		}.runTaskLaterAsynchronously(JavaPlugin.getPlugin(VitreusTweaks.class), 20);
	}
	
	public static void addSleepingPlayer(Player player) {
		World world = player.getWorld();
		Server server = player.getServer();
		
		sleepingPlayers.add(player);
		
		if (getSleepingPercent(world) > 50 && voting) {
			sendStatusUpdate(server, world);
			passSleepVote(server, world);
			voting = false;
		} else {
			sendStatusUpdate(server, world);
			voting = true;
			someoneSleeping = true;
		}		
	}
	
	public static void removeSleepingPlayer(Player player) {
		World world = player.getWorld();
		Server server = player.getServer();
		
		sleepingPlayers.remove(player);
		
		if (sleepingPlayers.size() > 0) {
			sendStatusUpdate(server, world);
		} else if (voting) {
			failSleepVote(server, world);
			voting = false;
			someoneSleeping = false;
		}
	}
	
	public static void addSleepyPlayer(Player player) {
		World world = player.getWorld();
		Server server = player.getServer();
		
		sleepyPlayers.add(player);
		
		if (getSleepingPercent(world) > 50 && voting) {
			passSleepVote(server, world);
			voting = false;
		} else {
			sendStatusUpdate(server, world);
		}	
	}
	
	public static void removeSleepyPlayer(Player player) {
		World world = player.getWorld();
		Server server = player.getServer();
		
		sleepyPlayers.remove(player);
		
		sendStatusUpdate(server, world);
	}
	
	public static double getSleepingPercent(World world) {
		List<Player> onlinePlayers = world.getPlayers();
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

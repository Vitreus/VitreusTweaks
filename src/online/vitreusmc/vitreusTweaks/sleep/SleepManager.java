package online.vitreusmc.vitreusTweaks.sleep;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class SleepManager {

	private static ArrayList<Player> sleepingPlayers = new ArrayList<Player>();
	private static ArrayList<Player> sleepyPlayers = new ArrayList<Player>();
	private static boolean someoneSleeping;
	
	
	public static void sendStatusUpdate(Server server, World world) {
		server.broadcastMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "Sleep: " + ChatColor.RESET + ChatColor.DARK_AQUA + "Number of Sleepy Sheep > " + getSleepingPercent(world) + "%");
	}
	
	public static void startSleepVote(Server server, World world) {
		server.broadcastMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "Sleep: " + ChatColor.RESET + ChatColor.DARK_AQUA + "Someone fell asleep and is dreaming of sheep! Type " + ChatColor.BOLD + ChatColor.AQUA + "/sleepy" + ChatColor.RESET + ChatColor.DARK_AQUA + " to join them!");
	}
	
	public static void failSleepVote(Server server, World world) {
		server.broadcastMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "Sleep: " + ChatColor.RESET + ChatColor.RED + "No one is sleeping anymore!");
	}
	
	public static void passSleepVote(Server server, World world) {
		server.broadcastMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "Sleep: " + ChatColor.RESET + ChatColor.GREEN + "Good morning... Vitreus!");
		world.setTime(0);
	}
	
	public static void addSleepingPlayer(Player player) {
		World world = player.getWorld();
		Server server = player.getServer();
		
		sleepingPlayers.add(player);
		
		if (getSleepingPercent(world) > 50) {
			passSleepVote(server, world);
		} else {
			sendStatusUpdate(server, world);
			someoneSleeping = true;
		}		
	}
	
	public static void removeSleepingPlayer(Player player) {
		World world = player.getWorld();
		Server server = player.getServer();
		
		sleepingPlayers.remove(player);
		
		if (sleepingPlayers.size() > 0) {
			sendStatusUpdate(server, world);
		} else {
			failSleepVote(server, world);
			someoneSleeping = false;
		}
	}
	
	public static void addSleepyPlayer(Player player) {
		World world = player.getWorld();
		Server server = player.getServer();
		
		sleepyPlayers.add(player);
		
		if (getSleepingPercent(world) > 50) {
			passSleepVote(server, world);
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

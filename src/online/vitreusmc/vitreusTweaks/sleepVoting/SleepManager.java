package online.vitreusmc.vitreusTweaks.sleepVoting;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

import net.md_5.bungee.api.ChatColor;

public class SleepManager {

	private static ArrayList<Player> sleepingPlayers = new ArrayList<Player>();
	private static ArrayList<Player> sleepyPlayers = new ArrayList<Player>();
	private static double sleepyRatio;
	private static boolean someoneSleeping;
	
	public static void addSleepingPlayer(Player sleepingPlayer) {
		World world = sleepingPlayer.getWorld();
		Server server = sleepingPlayer.getServer();
		
		sleepingPlayers.add(sleepingPlayer);
		checkSleepyRatio();
		
		if (!someoneSleeping) {
			updateSleepyRatio(world);
			server.broadcastMessage(sleepingPlayer.getDisplayName() + ChatColor.GREEN + " is lying down. Type " + ChatColor.RED + ChatColor.BOLD + "/sleepy" + ChatColor.RESET + ChatColor.GREEN + " if you're sleepy too!");
			someoneSleeping = true;
		} else {
			updateSleepyRatio(world);
			server.broadcastMessage(sleepingPlayer.getDisplayName() + ChatColor.GREEN + " is lying down too! " + (sleepyRatio * 100) + "% of players are sleepy.");			
		}
	}
	
	public static void removeSleepingPlayer(Player awokenPlayer) {
		World world = awokenPlayer.getWorld();
		Server server = awokenPlayer.getServer();
		
		sleepingPlayers.remove(awokenPlayer);
		
		if (sleepingPlayers.size() == 0 && someoneSleeping) {
			updateSleepyRatio(world);
			server.broadcastMessage(ChatColor.GREEN + "Nobody is lying down anymore!");
			someoneSleeping = false;
		} else {
			updateSleepyRatio(world);
			server.broadcastMessage(ChatColor.GREEN + "Someone woke up! " + (sleepyRatio * 100) + "% of players are sleepy!" );
		}
	}
	
	public static void addSleepyPlayer(Player sleepyPlayer) {
		World world = sleepyPlayer.getWorld();
		Server server = sleepyPlayer.getServer();
		
		if (!someoneSleeping) {
			return;
		}
		
		if (sleepingPlayers.contains(sleepyPlayer)) {
			return;
		}
		
		sleepyPlayers.add(sleepyPlayer);
		
		updateSleepyRatio(world);
		server.broadcastMessage(ChatColor.GREEN + "I heard a yawn! " + sleepyPlayer.getDisplayName() + ChatColor.GREEN + " is sleepy, along with " + (sleepyRatio * 100) + "% of players.");
		checkSleepyRatio();
	}
	
	public static void removeSleepyPlayer(Player awokenPlayer) {
		World world = awokenPlayer.getWorld();
		Server server = awokenPlayer.getServer();
		
		sleepyPlayers.remove(awokenPlayer);

		updateSleepyRatio(world);
		server.broadcastMessage(ChatColor.GREEN + "Someone sucked up some coffee and is no longer sleepy! " + (sleepyRatio * 100) + "% of players are still sleepy.");
	}
	
	private static void updateSleepyRatio(World world) {
		sleepyRatio = (sleepingPlayers.size() + sleepyPlayers.size()) / world.getPlayers().size();
	}
	
	private static boolean checkSleepyRatio() {
		Player player = sleepingPlayers.get(0);
		World world = player.getWorld();
		BukkitScheduler scheduler = Bukkit.getScheduler();
		
		updateSleepyRatio(world);
		
		if (sleepyRatio > 0.5) {
			scheduler.runTaskLaterAsynchronously(Bukkit.getPluginManager().getPlugin("VitreusTweaks"), new BukkitRunnable() {	
				@Override
				public void run() {
					sleepingPlayers.get(0).getWorld().setTime(0);
					sleepingPlayers = new ArrayList<Player>();
					sleepyPlayers = new ArrayList<Player>();
				}
			}, 4);

			return true;
		} else {
			return false;
		}
	}
}

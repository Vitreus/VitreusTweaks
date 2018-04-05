package com.vitreusmc.tweaks.sleep;

import java.util.Collection;

import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.vitreusmc.tweaks.VitreusTweaks;

import net.md_5.bungee.api.ChatColor;

public class SleepMonitor implements Listener, CommandExecutor {

	private VitreusTweaks plugin = JavaPlugin.getPlugin(VitreusTweaks.class);
	private Server server = plugin.getServer();
	private boolean voting = false;
	
	@EventHandler
	public void onBedEnter(PlayerBedEnterEvent event) {
		event.getPlayer().addScoreboardTag("sleeping");
		updateVote(event.getBed().getWorld());
	}

	@EventHandler
	public void onBedLeave(PlayerBedLeaveEvent event) {
		event.getPlayer().removeScoreboardTag("sleeping");
		updateVote(event.getBed().getWorld());
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if (!(sender instanceof Player))
			return false;
		
		Player player = (Player) sender;
		World world = player.getWorld();
		
		if (!isSomeoneSleeping()) {
			player.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "Sleep: " + ChatColor.RESET + "" + ChatColor.RED + "Nobody else is sleeping! Go get some coffee or something sleepy butt!");
			return true;
		}
		
		if (world.getEnvironment() == Environment.NETHER) {
			player.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "Sleep: " + ChatColor.RESET + "" + ChatColor.RED + "Your face is too busy melting to sleep. Besides, you're in another dimension.");
			return true;
		}
		
		if (world.getEnvironment() == Environment.THE_END) {
			player.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "Sleep: " + ChatColor.RESET + "" + ChatColor.DARK_PURPLE + "All you see when you close your eyes are strange, purple eyes... You try to sleep, but you find their gaze untiring, like yourself. Besides, you're in completely seperate dimension.");
			return true;
		}
		
		player.addScoreboardTag("sleeping");
		updateVote(world);
		return true;
	}
	
	private void updateVote(World world) {
		double sleepingPercent = getSleepingPercent();
		
		if (sleepingPercent > 0.5) {
			world.setTime(0);
		}
		
		if (world.getTime() > 1200) {
			if (!voting) 
				server.broadcastMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "Sleep: " + ChatColor.RESET + ChatColor.DARK_AQUA + "Someone fell asleep and is dreaming of sheep! Type " + ChatColor.BOLD + ChatColor.AQUA + "/sleepy" + ChatColor.RESET + ChatColor.DARK_AQUA + " to join them!");
			else
				server.broadcastMessage(ChatColor.AQUA + " ▶ " + ChatColor.BOLD + ChatColor.RESET + ChatColor.DARK_AQUA + "Sleepy Sheep: " + ChatColor.AQUA + ChatColor.BOLD + (int) (sleepingPercent * 100) + "%");	
			voting = true;
		} else if (world.getTime() < 1200 && voting) {
			server.broadcastMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "Sleep: " + ChatColor.RESET + ChatColor.GREEN + "Good morning... Vitreus!");
			voting = false;
			plugin.getServer().getOnlinePlayers().forEach(player -> player.removeScoreboardTag("sleeping"));
			if (world.hasStorm())
				world.setWeatherDuration(1);					
		}
		
	}
	
	private double getSleepingPercent() {
		Collection<? extends Player> players = plugin.getServer().getOnlinePlayers();
		int sleepingPlayers = 0;
		double percentSleeping;
		
		for (Player player : players) {
			if (player.isSleeping())
				sleepingPlayers++;
			else if (player.getScoreboardTags().contains("sleeping"))
				sleepingPlayers++;
			else if (!player.getWorld().getEnvironment().equals(Environment.NORMAL))
				sleepingPlayers++;
		}
		
		percentSleeping = (float) sleepingPlayers / (float) players.size();
				
		return percentSleeping;
	}
	
	private boolean isSomeoneSleeping() {
		boolean sleeping = false;
		
		for (Player player : plugin.getServer().getOnlinePlayers()) {
			if (player.isSleeping())
				sleeping = true;
		}
		
		return sleeping;
	}
	
}

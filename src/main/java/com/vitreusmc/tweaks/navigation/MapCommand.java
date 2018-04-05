package com.vitreusmc.tweaks.navigation;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.chat.TextComponent;

public class MapCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		Player player;
		Location location;
		int x;
		int y;
		int z;
		String url;
		TextComponent message = new TextComponent();
		
		if (!(sender instanceof Player)) {
			return false;
		}
		
		player = (Player) sender;
		location = player.getLocation();
		x = (int) location.getX();
		y = (int) location.getY();
		z = (int) location.getZ();
		url = String.format("http://vitreus-mc.com/dynmap/?worldname=vitreus-world-one&mapname=surface&zoom=5&x=%d&y=%d&z=%d", x, y, z);
		
		TextComponent urlComponent = new TextComponent();
		urlComponent.setColor(ChatColor.GREEN);
		urlComponent.setClickEvent(new ClickEvent(Action.OPEN_URL, url));
		urlComponent.addExtra(url);
		
		message.addExtra(ChatColor.BLUE + "" + ChatColor.BOLD + "Player: " + ChatColor.RESET + "" + player.getPlayerListName() + "\n");
		message.addExtra(ChatColor.BLUE + "" + ChatColor.BOLD + "Coordinates: " + ChatColor.RESET + "" + ChatColor.DARK_GREEN + "X: " + ChatColor.GREEN + x + ChatColor.DARK_GREEN +  " Y: " + ChatColor.GREEN + y + ChatColor.DARK_GREEN + " Z: " + ChatColor.GREEN + z + ChatColor.DARK_GREEN + " World: " + ChatColor.GREEN + location.getWorld().getEnvironment().toString() + "\n");
		message.addExtra(ChatColor.BLUE + "" + ChatColor.BOLD + "Map: ");
		message.addExtra(urlComponent);
		
		Bukkit.spigot().broadcast(message);
		
		return true;
	}

}

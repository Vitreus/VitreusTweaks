package online.vitreusmc.vitreusTweaks.navigation;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class CompassCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		Player player;
		String target;
		Location targetLocation = null;
		
		if (!(sender instanceof Player)) {
			return false;
		}
		
		if (args.length <= 0) {
			return false;
		}
		
		
		player = (Player) sender;
		target = args[0];
		
		if (args.length == 3) {
			try {
				int x = Integer.parseInt(args[0]);
				int y = Integer.parseInt(args[1]);
				int z = Integer.parseInt(args[2]);

				targetLocation = new Location(player.getWorld(), x, y, z);
			} catch (Exception exception) {}
		}
		
		switch (target) {
			case "spawn": {
				targetLocation = player.getWorld().getSpawnLocation();				
				break;
			}
			case "bed": {
				targetLocation = player.getBedSpawnLocation();				
				break;
			}
			case "here": {
				targetLocation = player.getLocation();				
				break;
			}
		}
		
		if (targetLocation == null) {
			player.sendMessage(ChatColor.BOLD + "" + ChatColor.GOLD + "Compass: " + ChatColor.RESET + "" + ChatColor.RED + "Invalid Arguments Supplied!");
			return false;
		}
		
		if (targetLocation != null) {
			player.setCompassTarget(targetLocation);
			player.sendMessage(ChatColor.BOLD + "" + ChatColor.GOLD + "Compass: " + ChatColor.RESET + ChatColor.DARK_GREEN + "Your Compass has been Set to " + ChatColor.GREEN + "(" + (int) targetLocation.getX() + ", " + (int) targetLocation.getY() + ", " + (int) targetLocation.getBlockZ() + ")" + ChatColor.DARK_GREEN + "!");			
			
			return true;
		}
		
		return false;
	}

}

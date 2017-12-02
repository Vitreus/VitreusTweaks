package online.vitreusmc.vitreusTweaks.navigation;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class PortalCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		Location location;
		World world;
		Player player;
		
		if (!(sender instanceof Player)) {
			return false;
		}
		
		player = (Player) sender;
		location = player.getLocation();

		player.sendMessage(createMessage(location));
				
		return true;
	}

	private static String createMessage(Location location) {
		Environment environment = location.getWorld().getEnvironment();
		int x = location.getBlockX();
		int y = location.getBlockY();
		int z = location.getBlockZ();
				
		if (environment == Environment.NETHER) {
			x = x * 8;
			z = z * 8;
		} else if (environment == Environment.NORMAL) {
			x = x / 8;
			z = z / 8;
		}
		
		return (ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "Portal: " + ChatColor.RESET + "" + ChatColor.DARK_PURPLE + "(" + ChatColor.LIGHT_PURPLE + x + ChatColor.DARK_PURPLE + ", " + ChatColor.LIGHT_PURPLE + y + ChatColor.DARK_PURPLE + ", " + ChatColor.LIGHT_PURPLE + z + ChatColor.DARK_PURPLE + ")" + ChatColor.GRAY + "" + ChatColor.ITALIC + "\n(The Nether Nexus Tunnels are located at Y:115 in the Nether!)");
	}
	
}

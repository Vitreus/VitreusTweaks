package online.vitreusmc.vitreusTweaks.sleep;

import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class SleepyCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		Player player;
		World world;
		
		if (!(sender instanceof Player)) {
			return false;
		}
		
		player = (Player) sender;
		world = player.getWorld();
		
		if (!SleepManager.isSomeoneSleeping()) {
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
		
		if (!(SleepManager.getSleepyPlayers().contains(player)) && !(SleepManager.getSleepingPlayers().contains(player))) {
			SleepManager.addSleepyPlayer(player);	
			return true;
		}
		
		return false;
	}

}

package online.vitreusmc.vitreusTweaks.sleepVoting;

import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
		
		SleepManager.addSleepyPlayer(player);
		
		return true;
	}

}

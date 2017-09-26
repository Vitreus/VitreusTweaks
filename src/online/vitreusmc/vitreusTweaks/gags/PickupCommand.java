package online.vitreusmc.vitreusTweaks.gags;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.google.common.collect.Iterables;


public class PickupCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		Player player;
		List<Entity> entities;
		Optional<Entity> targetEntity;
		
		if (!(sender instanceof Player)) {
			return false;
		}
		
		player = (Player) sender;
		
		try {
			entities = player.getNearbyEntities(4, 4, 4);
			targetEntity = entities.stream().reduce((a, b) -> {
				if ((b.getLocation().distance(player.getLocation()) <= (a.getLocation().distance(player.getLocation())))) {
					return b;
				} else {
					return a;
				}
			});
			
			if (targetEntity.isPresent()) {
				player.addPassenger(targetEntity.get());				
			} else {
				throw new CommandException();
			}
			
		} catch (CommandException exception) {
			return false;
		}
		
		return false;
	}

}

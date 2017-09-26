package online.vitreusmc.vitreusTweaks.sleepVoting;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import online.vitreusmc.vitreusTweaks.sleepVoting.SleepManager;

public class SleepListener implements Listener {
	
	@EventHandler
	public void onBedEnter(PlayerBedEnterEvent event) {
		Player sleepingPlayer = event.getPlayer();
		
		SleepManager.addSleepingPlayer(sleepingPlayer);
	}

	@EventHandler
	public void onBedLeave(PlayerBedLeaveEvent event) {
		Player awokenPlayer = event.getPlayer();
		
		SleepManager.removeSleepingPlayer(awokenPlayer);
	}
}

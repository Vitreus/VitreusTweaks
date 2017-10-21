package online.vitreusmc.vitreusTweaks.sleep;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;

import online.vitreusmc.vitreusTweaks.sleep.SleepManager;

public class SleepListener implements Listener {
	
	@EventHandler
	public void onBedEnter(PlayerBedEnterEvent event) {
		
		if (!SleepManager.isSomeoneSleeping()) {
			SleepManager.startSleepVote(Bukkit.getServer());
		}
		
		Player sleepingPlayer = event.getPlayer();
		SleepManager.addSleepingPlayer(sleepingPlayer);
	}

	@EventHandler
	public void onBedLeave(PlayerBedLeaveEvent event) {
		Player awokenPlayer = event.getPlayer();
		SleepManager.removeSleepingPlayer(awokenPlayer);
	}
}

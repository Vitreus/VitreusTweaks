package online.vitreusmc.vitreusTweaks.armorStand;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.logging.Logger;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import online.vitreusmc.vitreusTweaks.VitreusTweaks;
import online.vitreusmc.vitreusTweaks.armorStand.ui.ManipulationTextComponent;

public class ManipulateArmorStandCommand implements CommandExecutor {

	private JavaPlugin plugin;
	private Logger logger;
	
	public ManipulateArmorStandCommand() {
		this.plugin = JavaPlugin.getPlugin(VitreusTweaks.class);
		this.logger = plugin.getLogger();
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		final int xRadius = 16;
		final int yRadius = 16;
		final int zRadius = 16;
		
		ArmorStand nearestArmorStand;
		Location senderLocation;
		String action;
		Player player;
		
		if (!(sender instanceof Player)) {
			logger.info("Sender is not a player!");
			return false;
		}
		
		try {
			action = args[0];
		} catch (Exception exception) {
			TextComponent manipulateTextComponent = ManipulationTextComponent.get();
			
			sender.spigot().sendMessage(manipulateTextComponent);
			
			return true;
		}
		
		senderLocation = ((Player) sender).getLocation();
		nearestArmorStand = getNearestArmorStand(senderLocation, xRadius, yRadius, zRadius);		
		player = (Player) sender;
		
		switch (action) {
			case "adjust": {
				String targetPart = args[1];
				double additionalX = Double.parseDouble(args[2]);
				double additionalY = Double.parseDouble(args[3]);
				double additionalZ = Double.parseDouble(args[4]);
				EulerAngle additionalRotation = new EulerAngle(additionalX, additionalY, additionalZ);
				ArmorStand result = adjust(nearestArmorStand, additionalRotation, targetPart);
				
				if (result == null) {
					sender.sendMessage("Unknown Part: " + targetPart);
				}
				
				break;
			}
			case "set": {
				String targetPart = args[1];
				double additionalX = Double.parseDouble(args[2]);
				double additionalY = Double.parseDouble(args[3]);
				double additionalZ = Double.parseDouble(args[4]);
				EulerAngle rotation = new EulerAngle(additionalX, additionalY, additionalZ);
				ArmorStand result = set(nearestArmorStand, rotation, targetPart);
				
				if (result == null) {
					sender.sendMessage("Unknown Part: " + targetPart);
				}
				
				break;
			}
			case "toggle": {
				String targetOption = args[1];
				ArmorStand result = toggle(nearestArmorStand, targetOption, player);
				
				if (result == null) {
					sender.sendMessage("Unknown Option: " + targetOption);
				}
				
				break;
			}
			case "move": {
				//TargetPart is not needed for movement, but is in the command ran as a placeholder, hence the starting argument.
				double additionalX = Double.parseDouble(args[2]);
				double additionalY = Double.parseDouble(args[3]);
				double additionalZ = Double.parseDouble(args[4]);
				Vector moveVector = new Vector(additionalX, additionalY, additionalZ);
				
				move(nearestArmorStand, moveVector);

				break;
			}
			case "change": {
				String option = args[1];
				String value = "";
				
				for (int i = 2; i < args.length; i++) {
					value += args[i];
				}
				
				ArmorStand result = change(nearestArmorStand, option, value);
				
				if (result == null) {
					player.sendMessage("Unknown Option: " + option);
				}
				
				break;
			}
			case "give": {
				String targetPart = args[1];
				
				ArmorStand result = give(nearestArmorStand, targetPart, player);
				
				if (result == null) {
					player.sendMessage("Unknown Part: " + targetPart);
				}
				
				break;
			}
			default: {
				sender.sendMessage("Unknown Action: " + action);
				
				break;
			}
		}
			
		return true;
	}
	
	private ArmorStand getNearestArmorStand(Location location, double rx, double ry, double rz) {
		World targetWorld = location.getWorld();
		Collection<Entity> nearbyEntities = targetWorld.getNearbyEntities(location, rx, ry, rz);
		ArrayList<ArmorStand> nearbyArmorStands = new ArrayList<ArmorStand>();
		Optional<ArmorStand> targetArmorStand;
		
		for (Entity entity : nearbyEntities) {
			if (entity instanceof ArmorStand) {
				nearbyArmorStands.add((ArmorStand) entity);
			}
		}
		
		targetArmorStand = nearbyArmorStands.stream().reduce((a, b) -> {
			
			if (b.getLocation().distance(location) <= a.getLocation().distance(location)) {
				return b;
			} else {
				return a;
			}
		});
		
		if (targetArmorStand.isPresent()) {
			return targetArmorStand.get();
		} else {
			return null;
		}
	}
	
	private ArmorStand move(ArmorStand targetArmorStand, Vector moveVector) {
		Location currentLocation = targetArmorStand.getLocation();
		
		targetArmorStand.teleport(currentLocation.add(moveVector));
		
		return targetArmorStand;
	}
	
	private ArmorStand toggle(ArmorStand targetArmorStand, String option, Player player) {
		switch (option) {
			case "visible": {
				toggleVisible(targetArmorStand);
				break;
			}
			case "gravity": {
				toggleGravity(targetArmorStand);
				player.sendTitle("Gravity", ChatColor.DARK_GRAY + "|" + targetArmorStand.hasGravity() + "|", 8, 8, 8);
				break;
			}
			case "size": {
				toggleSize(targetArmorStand);
				break;
			}
			case "base_plate": {
				toggleBasePlate(targetArmorStand);
				break;
			}
			case "arms": {
				toggleArms(targetArmorStand);
				break;
			}
			default: {
				return null;
			}
		}
		
		return targetArmorStand;
	}
	
	private ArmorStand toggleGravity(ArmorStand targetArmorStand) {
		if (targetArmorStand.hasGravity()) {
			targetArmorStand.setGravity(false);
		} else {
			targetArmorStand.setGravity(true);
		}
		
		return targetArmorStand;
	}
	
	private ArmorStand toggleBasePlate(ArmorStand targetArmorStand) {
		if (targetArmorStand.hasBasePlate()) {
			targetArmorStand.setBasePlate(false);
		} else {
			targetArmorStand.setBasePlate(true);
		}
		
		return targetArmorStand;
	}
	
	private ArmorStand toggleArms(ArmorStand targetArmorStand) {
		if (targetArmorStand.hasArms()) {
			targetArmorStand.setArms(false);
		} else {
			targetArmorStand.setArms(true);
		}
		
		return targetArmorStand;
	}
	
	private ArmorStand toggleSize(ArmorStand targetArmorStand) {
		if (targetArmorStand.isSmall()) {
			targetArmorStand.setSmall(false);
		} else {
			targetArmorStand.setSmall(true);
		}
		
		return targetArmorStand;
	}
	
	private ArmorStand toggleVisible(ArmorStand targetArmorStand) {
		if (targetArmorStand.isVisible()) {
			targetArmorStand.setVisible(false);
		} else {
			targetArmorStand.setVisible(true);
		}
		
		return targetArmorStand;
	}
	
	private ArmorStand change(ArmorStand targetArmorStand, String option, String value) {
		switch (option) {
			case "name": {
				targetArmorStand.setCustomName(value);
				targetArmorStand.setCustomNameVisible(true);
				break;
			}
			default: {
				return null;
			}
		}
		
		return targetArmorStand;
	}
	
	private ArmorStand give(ArmorStand targetArmorStand, String targetPart, Player player) {
		ItemStack newItem = player.getEquipment().getItemInMainHand();
		ItemStack oldItem;
		
		switch (targetPart) {
			case "head": {
				oldItem = targetArmorStand.getEquipment().getHelmet();
				targetArmorStand.getEquipment().setHelmet(newItem);
				break;
			}
			case "left_arm": {
				oldItem = targetArmorStand.getEquipment().getItemInOffHand();
				targetArmorStand.getEquipment().setItemInOffHand(newItem);
				break;
			}
			case "right_arm": {
				oldItem = targetArmorStand.getEquipment().getItemInMainHand();
				targetArmorStand.getEquipment().setItemInMainHand(newItem);
				break;
			}
			default: {
				return null;
			}
		}
		
		player.getEquipment().setItemInMainHand(oldItem);
		
		return targetArmorStand;
	}
	
	private ArmorStand adjust(ArmorStand targetArmorStand, EulerAngle additionalRotation, String targetPart) {
		switch (targetPart) {
			case "head": {
				adjustHead(targetArmorStand, additionalRotation);
				break;
			}
			case "left_arm": {
				adjustLeftArm(targetArmorStand, additionalRotation);
				break;
			}
			case "right_arm": {
				adjustRightArm(targetArmorStand, additionalRotation);
				break;
			}
			case "left_leg": {
				adjustLeftLeg(targetArmorStand, additionalRotation);
				break;
			}
			case "right_leg": {
				adjustRightLeg(targetArmorStand, additionalRotation);
				break;
			}
			default: {
				return null;
			}
		}
		
		return targetArmorStand;
	}
	
	private ArmorStand adjustHead(ArmorStand targetArmorStand, EulerAngle additionalRotation) {
		EulerAngle currentRotation = targetArmorStand.getHeadPose();
		double additionalX = additionalRotation.getX();
		double additionalY = additionalRotation.getY();
		double additionalZ = additionalRotation.getZ();
		
		targetArmorStand.setHeadPose(currentRotation.add(additionalX, additionalY, additionalZ));
		
		return targetArmorStand;
	}
	
	private ArmorStand adjustLeftArm(ArmorStand targetArmorStand, EulerAngle additionalRotation) {
		EulerAngle currentRotation = targetArmorStand.getLeftArmPose();
		double additionalX = additionalRotation.getX();
		double additionalY = additionalRotation.getY();
		double additionalZ = additionalRotation.getZ();
		
		targetArmorStand.setLeftArmPose(currentRotation.add(additionalX, additionalY, additionalZ));
		
		return targetArmorStand;
	}
	
	private ArmorStand adjustRightArm(ArmorStand targetArmorStand, EulerAngle additionalRotation) {
		EulerAngle currentRotation = targetArmorStand.getRightArmPose();
		double additionalX = additionalRotation.getX();
		double additionalY = additionalRotation.getY();
		double additionalZ = additionalRotation.getZ();
		
		targetArmorStand.setRightArmPose(currentRotation.add(additionalX, additionalY, additionalZ));
		
		return targetArmorStand;
	}
	
	private ArmorStand adjustLeftLeg(ArmorStand targetArmorStand, EulerAngle additionalRotation) {
		EulerAngle currentRotation = targetArmorStand.getLeftLegPose();
		double additionalX = additionalRotation.getX();
		double additionalY = additionalRotation.getY();
		double additionalZ = additionalRotation.getZ();
		
		targetArmorStand.setLeftLegPose(currentRotation.add(additionalX, additionalY, additionalZ));
		
		return targetArmorStand;
	}
	
	private ArmorStand adjustRightLeg(ArmorStand targetArmorStand, EulerAngle additionalRotation) {
		EulerAngle currentRotation = targetArmorStand.getRightLegPose();
		double additionalX = additionalRotation.getX();
		double additionalY = additionalRotation.getY();
		double additionalZ = additionalRotation.getZ();
		
		targetArmorStand.setRightLegPose(currentRotation.add(additionalX, additionalY, additionalZ));
		
		return targetArmorStand;
	}

	private ArmorStand setHead(ArmorStand targetArmorStand, EulerAngle rotation) {
		targetArmorStand.setHeadPose(rotation);
		
		return targetArmorStand;
	}
	
	private ArmorStand set(ArmorStand targetArmorStand, EulerAngle rotation, String targetPart) {
		switch (targetPart) {
			case "head": {
				setHead(targetArmorStand, rotation);
				break;
			}
			case "left_arm": {
				setLeftArm(targetArmorStand, rotation);
				break;
			}
			case "right_arm": {
				setRightArm(targetArmorStand, rotation);
				break;
			}
			case "left_leg": {
				setLeftLeg(targetArmorStand, rotation);
				break;
			}
			case "right_leg": {
				setRightLeg(targetArmorStand, rotation);
				break;
			}
			default: {
				return null;
			}
		}
		
		return targetArmorStand;
	}
	
	private ArmorStand setLeftArm(ArmorStand targetArmorStand, EulerAngle rotation) {
		targetArmorStand.setLeftArmPose(rotation);
		
		return targetArmorStand;
	}
	
	private ArmorStand setRightArm(ArmorStand targetArmorStand, EulerAngle rotation) {
		targetArmorStand.setRightArmPose(rotation);
		
		return targetArmorStand;
	}
	
	private ArmorStand setLeftLeg(ArmorStand targetArmorStand, EulerAngle rotation) {
		targetArmorStand.setLeftLegPose(rotation);
		
		return targetArmorStand;
	}
	
	private ArmorStand setRightLeg(ArmorStand targetArmorStand, EulerAngle rotation) {
		targetArmorStand.setRightLegPose(rotation);
		
		return targetArmorStand;
	}
}

package online.vitreusmc.vitreusTweaks.armorStand.ui;

import org.bukkit.util.EulerAngle;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.chat.TextComponent;

public class ManipulationTextComponent {

	private static TextComponent baseComponent;
	
	public static TextComponent get() {
		if (baseComponent == null) {
			baseComponent = new TextComponent();
		
			TextComponent adjustmentComponent = createAdjustmentComponent();
			TextComponent optionsComponent = createOptionsComponent();
			TextComponent positionComponent = createPositionComponent();
			
			baseComponent.setColor(ChatColor.GREEN);
			baseComponent.setBold(true);
			baseComponent.addExtra("               Armor Stand Manipulation");
			baseComponent.addExtra("\n");
			baseComponent.addExtra("- - - - - - - - - - - - - - - - - - - - - - - - -");
			baseComponent.addExtra("\n");
			baseComponent.addExtra(optionsComponent);
			baseComponent.addExtra("\n");
			baseComponent.addExtra("- - - - - - - - - - - - - - - - - - - - - - - - -");
			baseComponent.addExtra("\n");
			baseComponent.addExtra(adjustmentComponent);
			baseComponent.addExtra("- - - - - - - - - - - - - - - - - - - - - - - - -");
			baseComponent.addExtra("\n");
			baseComponent.addExtra(positionComponent);
			baseComponent.addExtra("- - - - - - - - - - - - - - - - - - - - - - - - -");
		}
		
		return baseComponent;
	}

	private static TextComponent createOptionsComponent() {
		TextComponent optionsComponent = new TextComponent();
			TextComponent visibleToggleComponent = createOptionsToggleComponent("visible", "Visible");
			TextComponent gravityToggleComponent = createOptionsToggleComponent("gravity", "Gravity");
			TextComponent sizeToggleComponent = createOptionsToggleComponent("size", "Size");
			TextComponent basePlateToggleComponent = createOptionsToggleComponent("base_plate", "Base Plate");
			TextComponent armsToggleComponent = createOptionsToggleComponent("arms", "Arms");
			TextComponent nameInputComponent = createOptionsInputComponent("name", "Name");
			TextComponent headGiveComponent = createOptionsGiveComponent("head", "Helmet");
			TextComponent leftArmGiveComponent = createOptionsGiveComponent("left_arm", "Left Arm");
			TextComponent rightArmGiveComponent = createOptionsGiveComponent("right_arm", "Right Arm");

			optionsComponent.addExtra(" ");
			optionsComponent.addExtra(visibleToggleComponent);
			optionsComponent.addExtra("  |  ");
			optionsComponent.addExtra(gravityToggleComponent);
			optionsComponent.addExtra("  |  ");
			optionsComponent.addExtra(sizeToggleComponent);
			optionsComponent.addExtra("  |  ");
			optionsComponent.addExtra(basePlateToggleComponent);
			optionsComponent.addExtra("  |  ");
			optionsComponent.addExtra(armsToggleComponent);
			optionsComponent.addExtra("\n");
			optionsComponent.addExtra("- - - - - - - - - - - - - - - - - - - - - - - - -");
			optionsComponent.addExtra("\n");
			optionsComponent.addExtra(" ");
			optionsComponent.addExtra(nameInputComponent);
			optionsComponent.addExtra("  |  ");
			optionsComponent.addExtra(headGiveComponent);
			optionsComponent.addExtra("  |  ");
			optionsComponent.addExtra(leftArmGiveComponent);
			optionsComponent.addExtra("  |  ");
			optionsComponent.addExtra(rightArmGiveComponent);
			
		return optionsComponent;
	}
	
	private static TextComponent createOptionsToggleComponent(String option, String label) {
		TextComponent toggleComponent = new TextComponent();
			toggleComponent.addExtra(label);
			toggleComponent.setClickEvent(new ClickEvent(Action.RUN_COMMAND, "/manipulate_armor_stand toggle " + option));
			
		return toggleComponent;
	}
	
	private static TextComponent createOptionsInputComponent(String option, String label) {
		TextComponent inputComponent = new TextComponent();
			inputComponent.addExtra(label);
			inputComponent.setClickEvent(new ClickEvent(Action.SUGGEST_COMMAND, "/manipulate_armor_stand change " + option + " "));
		
		return inputComponent;
	}
	
	private static TextComponent createOptionsGiveComponent(String targetPart, String label) {
		TextComponent inputComponent = new TextComponent();
			inputComponent.addExtra(label);
			inputComponent.setClickEvent(new ClickEvent(Action.RUN_COMMAND, "/manipulate_armor_stand give " + targetPart));
		
		return inputComponent;
	}
	
	private static TextComponent createAdjustmentComponent() {
		TextComponent adjustmentComponent = new TextComponent();
			TextComponent headComponent = createAdjustmentPartComponent("head", "Head");
			TextComponent leftArmComponent = createAdjustmentPartComponent("left_arm", "Left Arm");
			TextComponent rightArmComponent = createAdjustmentPartComponent("right_arm", "Right Arm");
			TextComponent leftLegComponent = createAdjustmentPartComponent("left_leg", "Left Leg");
			TextComponent rightLegComponent = createAdjustmentPartComponent("right_leg", "Right Leg");
			
			adjustmentComponent.setColor(ChatColor.WHITE);
			adjustmentComponent.setBold(false);
			
			adjustmentComponent.addExtra("    ");
			adjustmentComponent.addExtra(headComponent);
			adjustmentComponent.addExtra("\n");
			adjustmentComponent.addExtra("    ");
			adjustmentComponent.addExtra(leftArmComponent);
			adjustmentComponent.addExtra("\n");
			adjustmentComponent.addExtra("    ");
			adjustmentComponent.addExtra(rightArmComponent);
			adjustmentComponent.addExtra("\n");
			adjustmentComponent.addExtra("    ");
			adjustmentComponent.addExtra(leftLegComponent);
			adjustmentComponent.addExtra("\n");
			adjustmentComponent.addExtra("    ");
			adjustmentComponent.addExtra(rightLegComponent);
			adjustmentComponent.addExtra("\n");
		
		return adjustmentComponent;
	}
	
	private static TextComponent createAdjustmentPartComponent(String targetPart, String label) {
		TextComponent partComponent = new TextComponent();

		TextComponent controlComponent = createAdjustmentControlComponent(targetPart, "adjust");
		
		TextComponent labelComponent = new TextComponent("   | " + label);
			labelComponent.setBold(true);
			labelComponent.setClickEvent(new ClickEvent(Action.RUN_COMMAND, "/manipulate_armor_stand set " + targetPart + " 0 0 0"));
		
			
		partComponent.addExtra(controlComponent);
		partComponent.addExtra(" ");
		partComponent.addExtra(labelComponent);
		
		return partComponent;
	}
	
	private static TextComponent createAdjustmentControlComponent(String targetPart, String action) {
		TextComponent controlComponent = new TextComponent();
		
		TextComponent xAdjustmentComponent = createAdjustmentFineControlComponent(targetPart, "X: ", new EulerAngle(0.2, 0.0, 0.0), action);
		TextComponent yAdjustmentComponent = createAdjustmentFineControlComponent(targetPart, "Y: ", new EulerAngle(0.0, 0.2, 0.0), action);
		TextComponent zAdjustmentComponent = createAdjustmentFineControlComponent(targetPart, "Z: ", new EulerAngle(0.0, 0.0, 0.2), action);
		
		controlComponent.addExtra(xAdjustmentComponent);
		controlComponent.addExtra("    ");
		controlComponent.addExtra(yAdjustmentComponent);
		controlComponent.addExtra("    ");
		controlComponent.addExtra(zAdjustmentComponent);
		
		return controlComponent;
	}
	
	private static TextComponent createAdjustmentFineControlComponent(String targetPart, String axis, EulerAngle rotationAdjustment, String action) {
		TextComponent adjustmentComponent = new TextComponent();
		
		TextComponent addComponent = new TextComponent(" + ");
			addComponent.setClickEvent(new ClickEvent(Action.RUN_COMMAND, "/manipulate_armor_stand " + action + " " + targetPart + " " + rotationAdjustment.getX() + " " + rotationAdjustment.getY() + " " + rotationAdjustment.getZ()));
			addComponent.setColor(ChatColor.DARK_GREEN);
			addComponent.setBold(true);
		TextComponent subtractComponent = new TextComponent(" - ");
			subtractComponent.setClickEvent(new ClickEvent(Action.RUN_COMMAND, "/manipulate_armor_stand " + action + " " + targetPart + " " + -rotationAdjustment.getX() + " " + -rotationAdjustment.getY() + " " + -rotationAdjustment.getZ()));
			subtractComponent.setColor(ChatColor.DARK_RED);
			subtractComponent.setBold(true);
			
		adjustmentComponent.addExtra(axis + "[");
		adjustmentComponent.addExtra(addComponent);
		adjustmentComponent.addExtra("|");
		adjustmentComponent.addExtra(subtractComponent);
		adjustmentComponent.addExtra("]");
			
		return adjustmentComponent;
	}
	
	private static TextComponent createPositionComponent() {
		TextComponent positionComponent = new TextComponent();
			TextComponent positionControlComponent = createAdjustmentControlComponent("body", "move");
			positionComponent.addExtra("     ");
			positionComponent.addExtra(positionControlComponent);
			positionComponent.addExtra("\n");
		return positionComponent;
	}
}

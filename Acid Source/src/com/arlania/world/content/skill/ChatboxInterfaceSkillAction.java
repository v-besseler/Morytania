package com.arlania.world.content.skill;

import com.arlania.model.input.impl.EnterAmountOfBonesToSacrifice;
import com.arlania.model.input.impl.EnterAmountOfBowsToString;
import com.arlania.model.input.impl.EnterAmountOfGemsToCut;
import com.arlania.model.input.impl.EnterAmountToCook;
import com.arlania.model.input.impl.EnterAmountToFletch;
import com.arlania.model.input.impl.EnterAmountToSpin;
import com.arlania.world.content.skill.impl.cooking.Cooking;
import com.arlania.world.content.skill.impl.crafting.Flax;
import com.arlania.world.content.skill.impl.crafting.Gems;
import com.arlania.world.content.skill.impl.fletching.Fletching;
import com.arlania.world.content.skill.impl.prayer.BonesOnAltar;
import com.arlania.world.entity.impl.player.Player;

public class ChatboxInterfaceSkillAction {

	public static void handleChatboxInterfaceButtons(Player player, int buttonId) {
		if(!player.getClickDelay().elapsed(3000) || player.getInputHandling() != null && handleMakeXInterfaces(player, buttonId))
			return;
		int amount = buttonId == 2799 ? 1 : buttonId == 2798 ? 5 : buttonId == 1747 ? 28 : -1;
		if(player.getInputHandling() == null || amount <= 0) {
			player.getPacketSender().sendInterfaceRemoval();
			return;
		}
		if(player.getInputHandling() instanceof EnterAmountOfGemsToCut)
			Gems.cutGem(player, amount, player.getSelectedSkillingItem());
		else if(player.getInputHandling() instanceof EnterAmountToCook)
			Cooking.cook(player, player.getSelectedSkillingItem(), amount);
		else if(player.getInputHandling() instanceof EnterAmountToSpin)
			Flax.spinFlax(player, amount);
		else if(player.getInputHandling() instanceof EnterAmountOfBonesToSacrifice)
			BonesOnAltar.offerBones(player, amount);
		else if(player.getInputHandling() instanceof EnterAmountOfBowsToString)
			Fletching.stringBow(player, amount);
		player.getClickDelay().reset();
	}

	public static boolean handleMakeXInterfaces(Player player, int buttonId) {
		if(buttonId == 8886 || buttonId == 8890 || buttonId == 8894 || buttonId == 8871 || buttonId == 8875 || buttonId == 1748) { // Fletching X amount

			if(player.getInputHandling() instanceof EnterAmountToFletch) {
				((EnterAmountToFletch)player.getInputHandling()).setButton(buttonId);
			}

			player.getPacketSender().sendEnterAmountPrompt("How many would you like to make?");
			return true;
		}
		return false;
	}
}
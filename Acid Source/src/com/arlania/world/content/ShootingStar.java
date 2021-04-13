package com.arlania.world.content;

import com.arlania.model.Animation;
import com.arlania.model.GameObject;
import com.arlania.model.Position;
import com.arlania.util.Misc;
import com.arlania.util.Stopwatch;
import com.arlania.world.World;
import com.arlania.world.entity.impl.player.Player;

public class ShootingStar {

	private static final int TIME = 1800000;
	public static final int MAXIMUM_MINING_AMOUNT = 600;
	
	private static Stopwatch timer = new Stopwatch().reset();
	public static CrashedStar CRASHED_STAR = null;
	private static LocationData LAST_LOCATION = null;
	
	public static class CrashedStar {
		
		public CrashedStar(GameObject starObject, LocationData starLocation) {
			this.starObject = starObject;
			this.starLocation = starLocation;
		}
		
		private GameObject starObject;
		private LocationData starLocation;
		
		public GameObject getStarObject() {
			return starObject;
		}
		
		public LocationData getStarLocation() {
			return starLocation;
		}
	}

	public static enum LocationData {
		LOCATION_1(new Position(3053, 3301), "south of the Catherby Farming patches", "Farming"),
		LOCATION_2(new Position(3094, 3484), "south of the Edgeville bank", "Edgeville"),
		LOCATION_3(new Position(2480, 3433), "at the Gnome Agility Course", "Gnome Course"),
		LOCATION_4(new Position(2745, 3445), "in the middle of the Flax field", "Flax Field"),
		LOCATION_5(new Position(3206, 3269), "in the yak field", "Yak Field"),
		LOCATION_6(new Position(2594, 4326), "in Puro Puro", "Puro Puro"),
		LOCATION_7(new Position(3368, 3269), "in the Duel Arena", "Duel Arena"),
		LOCATION_8(new Position(1746, 5327), "in the Ancient cavern", "Ancient Cavern"),
		LOCATION_9(new Position(2882, 9800), "in the Taverly dungeon", "Taverly Dung."),
		LOCATION_10(new Position(2666, 2648), "at the Void knight island", "Pest Control"),
		LOCATION_11(new Position(3566, 3297), "on the Barrows hills", "Barrows"),
		LOCATION_12(new Position(2986, 3599), "in the Wilderness (near the western dragons)", "West Dragons"),
		LOCATION_13(new Position(3091, 3528), "in the Wilderness (Edgeville)", "Edgeville Wild"),
		LOCATION_14(new Position(2582, 4844), "near the Runecrafting Fire altar", "Fire Altar"),
		LOCATION_15(new Position(3478, 4834), "near the Runecrafting Water altar", "Water Altar"),
		LOCATION_16(new Position(2793, 4830), "near the Runecrafting Mind altar", "Mind Altar"),
		LOCATION_17(new Position(2846, 4829), "near the Runecrafting Air altar", "Air Altar"),
		LOCATION_18(new Position(2995, 3911), "outside the Wilderness Agility Course", "Wild. Course");

		private LocationData(Position spawnPos, String clue, String playerPanelFrame) {
			this.spawnPos = spawnPos;
			this.clue = clue;
			this.playerPanelFrame = playerPanelFrame;
		}

		private Position spawnPos;
		private String clue;
		public String playerPanelFrame;
	}
	public static LocationData getLocation() {
		return LAST_LOCATION;
	}

	public static LocationData getRandom() {
		LocationData star = LocationData.values()[Misc.getRandom(LocationData.values().length - 1)];
		return star;
	}

	public static void sequence() {
		if(CRASHED_STAR == null) {
			if(timer.elapsed(TIME)) {
				LocationData locationData = getRandom();
				if(LAST_LOCATION != null) {
					if(locationData == LAST_LOCATION) {
						locationData = getRandom();
					}
				}
				LAST_LOCATION = locationData;
				CRASHED_STAR = new CrashedStar(new GameObject(38660, locationData.spawnPos), locationData);
				CustomObjects.spawnGlobalObject(CRASHED_STAR.starObject);
				World.sendMessage("<img=10> @blu@[Shooting Star]@bla@ A star has just crashed "+locationData.clue+"!");
				World.getPlayers().forEach(p -> p.getPacketSender().sendString(26623, "@or2@Crashed star: @gre@"+ShootingStar.CRASHED_STAR.getStarLocation().playerPanelFrame+""));
				timer.reset();
			}
		} else {
			if(CRASHED_STAR.starObject.getPickAmount() >= MAXIMUM_MINING_AMOUNT) {
				despawn(false);
				timer.reset();
			}
		}
	}

	public static void despawn(boolean respawn) {
		if(respawn) {
			timer.reset(0);
		} else {
			timer.reset();
		}
		if(CRASHED_STAR != null) {
			for(Player p : World.getPlayers()) {
				if(p == null) {
					continue;
				}
				p.getPacketSender().sendString(26623, "@or2@Crashed star: @gre@N/A ");
				if(p.getInteractingObject() != null && p.getInteractingObject().getId() == CRASHED_STAR.starObject.getId()) {
					p.performAnimation(new Animation(65535));
					p.getPacketSender().sendClientRightClickRemoval();
					p.getSkillManager().stopSkilling();
					p.getPacketSender().sendMessage("The star has been fully mined.");
				}
			}
			CustomObjects.deleteGlobalObject(CRASHED_STAR.starObject);
			CRASHED_STAR = null;
		}
	}
}

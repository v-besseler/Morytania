package com.arlania.world.entity.impl.npc;

import com.arlania.engine.task.TaskManager;
import com.arlania.engine.task.impl.NPCDeathTask;
import com.arlania.model.Direction;
import com.arlania.model.Position;
import com.arlania.model.Locations.Location;
import com.arlania.model.definitions.NpcDefinition;
import com.arlania.util.JsonLoader;
import com.arlania.world.World;
import com.arlania.world.content.Cows;
import com.arlania.world.content.combat.CombatFactory;
import com.arlania.world.content.combat.CombatType;
import com.arlania.world.content.combat.effect.CombatPoisonEffect.PoisonType;
import com.arlania.world.content.combat.strategy.CombatStrategies;
import com.arlania.world.content.combat.strategy.CombatStrategy;
import com.arlania.world.content.combat.strategy.impl.KalphiteQueen;
import com.arlania.world.content.combat.strategy.impl.Nex;
import com.arlania.world.content.skill.impl.hunter.Hunter;
import com.arlania.world.content.skill.impl.hunter.PuroPuro;
import com.arlania.world.content.skill.impl.runecrafting.DesoSpan;
import com.arlania.world.entity.impl.Character;
import com.arlania.world.entity.impl.npc.NPCMovementCoordinator.Coordinator;
import com.arlania.world.entity.impl.player.Player;
import com.google.gson.Gson;
import com.google.gson.JsonObject;


/**
 * Represents a non-playable character, which players can interact with.
 * @author Gabriel Hannason
 */

public class NPC extends Character {

	public NPC(int id, Position position) {
		super(position);
		NpcDefinition definition = NpcDefinition.forId(id);
		if(definition == null)
			throw new NullPointerException("NPC "+id+" is not defined!");
		this.defaultPosition = position;
		this.id = id;
		this.definition = definition;
		this.defaultConstitution = definition.getHitpoints() < 100 ? 100 : definition.getHitpoints();
		this.constitution = defaultConstitution;
		setLocation(Location.getLocation(this));
	}

	public void sequence() {
		/**
		 * HP restoration
		 */
		if(constitution < defaultConstitution) {
			if(!isDying) {
				if(getLastCombat().elapsed((id == 13447 || id == 3200 ? 50000 : 5000)) && !getCombatBuilder().isAttacking() && getLocation() != Location.PEST_CONTROL_GAME && getLocation() != Location.DUNGEONEERING) {
					setConstitution(constitution + (int)(defaultConstitution * 0.1));
					if(constitution > defaultConstitution)
						setConstitution(defaultConstitution);
				}
			}
		}
	}

	@Override
	public void appendDeath() {
		if(!isDying && !summoningNpc) {
			TaskManager.submit(new NPCDeathTask(this));
			isDying = true;
		}
	}

	@Override
	public int getConstitution() {
		return constitution;
	}

	@Override
	public NPC setConstitution(int constitution) {
		this.constitution = constitution;
		if(this.constitution <= 0)
			appendDeath();
		return this;
	}

	@Override
	public void heal(int heal) {
		if ((this.constitution + heal) > getDefaultConstitution()) {
			setConstitution(getDefaultConstitution());
			return;
		}
		setConstitution(this.constitution + heal);
	}


	@Override
	public int getBaseAttack(CombatType type) {
		return getDefinition().getAttackBonus();
	}

	@Override
	public int getAttackSpeed() {
		return this.getDefinition().getAttackSpeed();
	}


	@Override
	public int getBaseDefence(CombatType type) {

		if (type == CombatType.MAGIC)
			return getDefinition().getDefenceMage();
		else if (type == CombatType.RANGED)
			return getDefinition().getDefenceRange();

		return getDefinition().getDefenceMelee();
	}

	@Override
	public boolean isNpc() {
		return true;
	}

	@Override
	public boolean equals(Object other) {
		return other instanceof NPC && ((NPC)other).getIndex() == getIndex();
	}

	@Override
	public int getSize() {
		return getDefinition().getSize();
	}

	@Override
	public void poisonVictim(Character victim, CombatType type) {
		if (getDefinition().isPoisonous()) {
			CombatFactory.poisonEntity(
					victim,
					type == CombatType.RANGED || type == CombatType.MAGIC ? PoisonType.MILD
							: PoisonType.EXTRA);
		}

	}

	/**
	 * Prepares the dynamic json loader for loading world npcs.
	 * 
	 * @return the dynamic json loader.
	 * @throws Exception
	 *             if any errors occur while preparing for load.
	 */
	public static void init() {
		new JsonLoader() {
			@Override
			public void load(JsonObject reader, Gson builder) {

				int id = reader.get("npc-id").getAsInt();
				Position position = builder.fromJson(reader.get("position").getAsJsonObject(), Position.class);
				Coordinator coordinator = builder.fromJson(reader.get("walking-policy").getAsJsonObject(), Coordinator.class);
				Direction direction = Direction.valueOf(reader.get("face").getAsString());
				NPC npc = new NPC(id, position);
				npc.movementCoordinator.setCoordinator(coordinator);
				npc.setDirection(direction);
				World.register(npc);
				if(id > 5070 && id < 5081) {
					Hunter.HUNTER_NPC_LIST.add(npc);
				}
				position = null;
				coordinator = null;
				direction = null;
			}

			@Override
			public String filePath() {
				return "./data/def/json/world_npcs.json";
			}
		}.load();

		Nex.spawn();
		PuroPuro.spawn();
		DesoSpan.spawn();
		Cows.spawnMainNPCs();

		KalphiteQueen.spawn(1158, new Position(3485, 9509));
	}

	@Override
	public CombatStrategy determineStrategy() {
		return CombatStrategies.getStrategy(id);
	}

	public boolean switchesVictim() {
		if(getLocation() == Location.DUNGEONEERING) {
			return true;
		}
		return id == 6263 || id == 6265 || id == 6203 || id == 6208 || id == 6206 || id == 6247 || id == 6250 || id == 3200 || id == 4540 || id == 1158 || id == 1160 || id == 8133 || id == 13447 || id == 13451 || id == 13452 || id == 13453 || id == 13454 || id == 2896 || id == 2882 || id == 2881 || id == 6260;
	}

	public int getAggressionDistance() {
		int distance = 7;
		
		/*switch(id) {
		}*/
		if(Nex.nexMob(id)) {
			distance = 60;
		} else if(id == 2896) {
			distance = 50;
		}
		return distance;
	}

	/*
	 * Fields
	 */
	/** INSTANCES **/
	private final Position defaultPosition;
	private NPCMovementCoordinator movementCoordinator = new NPCMovementCoordinator(this);
	private Player spawnedFor;
	private NpcDefinition definition;

	/** INTS **/
	private final int id;
	private int constitution = 100;
	private int defaultConstitution;
	private int transformationId = -1;

	/** BOOLEANS **/
	private boolean[] attackWeakened = new boolean[3], strengthWeakened = new boolean[3];
	private boolean summoningNpc, summoningCombat;
	private boolean isDying;
	private boolean visible = true;
	private boolean healed, chargingAttack;
	private boolean findNewTarget;
	
	/*
	 * Getters and setters
	 */

	public int getId() {
		return id;
	}

	public Position getDefaultPosition() {
		return defaultPosition;
	}

	public int getDefaultConstitution() {
		return defaultConstitution;
	}

	public int getTransformationId() {
		return transformationId;
	}

	public void setTransformationId(int transformationId) {
		this.transformationId = transformationId;
	}

	public boolean isVisible() {
		return visible;
	}
	
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public void setDying(boolean isDying) {
		this.isDying = isDying;
	}
	
	public void setDefaultConstitution(int defaultConstitution) {
		this.defaultConstitution = defaultConstitution;
	}

	/**
	 * @return the statsWeakened
	 */
	public boolean[] getDefenceWeakened() {
		return attackWeakened;
	}

	public void setSummoningNpc(boolean summoningNpc) {
		this.summoningNpc = summoningNpc;
	}

	public boolean isSummoningNpc() {
		return summoningNpc;
	}

	public boolean isDying() {
		return isDying;
	}

	/**
	 * @return the statsBadlyWeakened
	 */
	public boolean[] getStrengthWeakened() {
		return strengthWeakened;
	}

	public NPCMovementCoordinator getMovementCoordinator() {
		return movementCoordinator;
	}

	public NpcDefinition getDefinition() {
		return definition;
	}

	public Player getSpawnedFor() {
		return spawnedFor;
	}

	public NPC setSpawnedFor(Player spawnedFor) {
		this.spawnedFor = spawnedFor;
		return this;
	}

	public boolean hasHealed() {
		return healed;
	}

	public void setHealed(boolean healed) {
		this.healed = healed;
	}

	public boolean isChargingAttack() {
		return chargingAttack;
	}

	public NPC setChargingAttack(boolean chargingAttack) {
		this.chargingAttack = chargingAttack;
		return this;
	}
	
	public boolean findNewTarget() {
		return findNewTarget;
	}
	
	public void setFindNewTarget(boolean findNewTarget) {
		this.findNewTarget = findNewTarget;
	}
	
	public boolean summoningCombat() {
		return summoningCombat;
	}
	
	public void setSummoningCombat(boolean summoningCombat) {
		this.summoningCombat = summoningCombat;
	}

	public void removeInstancedNpcs(Location loc, int height) {
		int checks = loc.getX().length - 1;
		for(int i = 0; i <= checks; i+=2) {
			if(getPosition().getX() >= loc.getX()[i] && getPosition().getX() <= loc.getX()[i + 1]) {
				if(getPosition().getY() >= loc.getY()[i] && getPosition().getY() <= loc.getY()[i + 1]) {
					if(getPosition().getZ() == height) {
						World.deregister(this);
					}
				}
			}
		}
	}
	public void countInstancedNpcs(Location loc, int height, int npcId) {
		int checks = loc.getX().length - 1;
		int ii = 0;
		for(int i = 0; i <= checks; i+=2) {
			if(getPosition().getX() >= loc.getX()[i] && getPosition().getX() <= loc.getX()[i + 1]) {
				if(getPosition().getY() >= loc.getY()[i] && getPosition().getY() <= loc.getY()[i + 1]) {
					if(getPosition().getZ() == height) {
						if(getId() == npcId) {
							ii++;
							System.out.println(ii);
						}
					}
				}
			}
		}
	}

	
}

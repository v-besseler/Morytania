package com.arlania.world.content.combat.strategy.impl;

import com.arlania.model.GroundItem;
import com.arlania.model.Item;
import com.arlania.world.content.RNG;
import com.arlania.world.entity.impl.GroundItemManager;
import com.arlania.world.entity.impl.npc.NPC;
import com.arlania.world.entity.impl.player.Player;

public class Joker {
    public static int[] JOKER_ARMOR = new int[] {20226, 20227, 20228, 20229, 20230};
    public static int ROCKET_LAUNCHER = 20231;

    public static void handleDrop(Player player, NPC npc) {
        if (player == null || npc == null)
            return;

        player.setBossPoints(player.getBossPoints() + 1);

        GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(537), npc.getPosition().copy(), player.getUsername(), false, 100, false, -1));


        int randomNumber = RNG.randInt(1000);

        //System.out.println(randomNumber);

        if(randomNumber < 2) {
            int drop = RNG.randElement(JOKER_ARMOR);
            // any armor piece = 1/500
            GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(drop), npc.getPosition().copy(), player.getUsername(), false, 100, false, -1));
        } else if (randomNumber < 7){
            // bronze box = 1/200
            GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(20374), npc.getPosition().copy(), player.getUsername(), false, 100, false, -1));
        } else if (randomNumber < 12) {
            // silver box = 1/500
            GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(20375), npc.getPosition().copy(), player.getUsername(), false, 100, false, -1));
        } else if (randomNumber == 12){
            // gold box = 1/1000
            GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(20376), npc.getPosition().copy(), player.getUsername(), false, 100, false, -1));
        } else if (randomNumber == 13){
            // rocket launcher 1/1000
            GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(ROCKET_LAUNCHER), npc.getPosition().copy(), player.getUsername(), false, 100, false, -1));
        }
    }
}

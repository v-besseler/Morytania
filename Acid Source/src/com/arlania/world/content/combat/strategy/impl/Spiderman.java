package com.arlania.world.content.combat.strategy.impl;

import com.arlania.model.GroundItem;
import com.arlania.model.Item;
import com.arlania.world.content.RNG;
import com.arlania.world.entity.impl.GroundItemManager;
import com.arlania.world.entity.impl.npc.NPC;
import com.arlania.world.entity.impl.player.Player;

public class Spiderman {

    public static int[] SPIDERMAN_DROPS = new int[] {20220, 20221, 20222};

    public static void handleDrop(Player player, NPC npc) {
        if (player == null || npc == null)
            return;

        player.setBossPoints(player.getBossPoints() + 1);

        GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(537), npc.getPosition().copy(), player.getUsername(), false, 100, false, -1));


        int randomNumber = RNG.randInt(1000);

        //System.out.println(randomNumber);

        if(randomNumber < 4) {
            int drop = RNG.randElement(SPIDERMAN_DROPS);
            // any armor piece = 1/250
            GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(drop), npc.getPosition().copy(), player.getUsername(), false, 100, false, -1));
        } else if (randomNumber < 9){
            // bronze box = 1/200
            GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(20374), npc.getPosition().copy(), player.getUsername(), false, 100, false, -1));
        } else if (randomNumber < 11) {
            // silver box = 1/500
            GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(20375), npc.getPosition().copy(), player.getUsername(), false, 100, false, -1));
        } else if (randomNumber == 11){
            // gold box = 1/1000
            GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(20376), npc.getPosition().copy(), player.getUsername(), false, 100, false, -1));
        }
    }
}

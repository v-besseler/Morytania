package com.arlania.world.content.combat.strategy.impl;

import com.arlania.model.GroundItem;
import com.arlania.model.Item;
import com.arlania.world.content.RNG;
import com.arlania.world.entity.impl.GroundItemManager;
import com.arlania.world.entity.impl.npc.NPC;
import com.arlania.world.entity.impl.player.Player;

public class Voldemort {
    public static int[] VOLD_ARMOR = new int[] {20339, 20340, 20341, 20342, 20343};
    public static int VOLD_WAND = 20338;

    public static void handleDrop(Player player, NPC npc) {
        if (player == null || npc == null)
            return;

        player.setBossPoints(player.getBossPoints() + 1);

        GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(537), npc.getPosition().copy(), player.getUsername(), false, 100, false, -1));


        int randomNumber = RNG.randInt(1000);

        //System.out.println(randomNumber);

        if(randomNumber < 2) {
            int drop = RNG.randElement(VOLD_ARMOR);
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
            // wand 1/1000
            GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(VOLD_WAND), npc.getPosition().copy(), player.getUsername(), false, 100, false, -1));
        }
    }
}

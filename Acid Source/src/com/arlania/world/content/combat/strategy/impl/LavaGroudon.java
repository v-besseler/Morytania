package com.arlania.world.content.combat.strategy.impl;

import com.arlania.model.GroundItem;
import com.arlania.model.Item;
import com.arlania.world.content.RNG;
import com.arlania.world.entity.impl.GroundItemManager;
import com.arlania.world.entity.impl.npc.NPC;
import com.arlania.world.entity.impl.player.Player;

public class LavaGroudon {
    public static int[] LAVA_ARMOR = new int[] {20327, 20328, 20329, 20331, 20332, 20333, 20335, 20336, 20337};
    public static int[] LAVA_WEAPONS = new int[] {20326, 20330, 20334};


    public static void handleDrop(Player player, NPC npc) {
        if (player == null || npc == null)
            return;

        player.setBossPoints(player.getBossPoints() + 1);

        GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(537), npc.getPosition().copy(), player.getUsername(), false, 100, false, -1));


        int randomNumber = RNG.randInt(1000);

        //System.out.println(randomNumber);

        if(randomNumber < 10) {
            int drop = RNG.randElement(LAVA_ARMOR);
            // any armor piece = 1/100
            GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(drop), npc.getPosition().copy(), player.getUsername(), false, 100, false, -1));
        } else if (randomNumber < 15){
            // bronze box = 1/200
            GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(20374), npc.getPosition().copy(), player.getUsername(), false, 100, false, -1));
        } else if (randomNumber < 17) {
            // silver box = 1/500
            GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(20375), npc.getPosition().copy(), player.getUsername(), false, 100, false, -1));
        } else if (randomNumber == 17){
            // gold box = 1/1000
            GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(20376), npc.getPosition().copy(), player.getUsername(), false, 100, false, -1));
        } else if (randomNumber < 22){
            // any weapon 1/200
            int drop = RNG.randElement(LAVA_WEAPONS);
            GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(drop), npc.getPosition().copy(), player.getUsername(), false, 100, false, -1));
        }
    }
}

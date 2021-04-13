package com.arlania.world.content.minigames.impl;

import com.arlania.model.GroundItem;
import com.arlania.model.Item;
import com.arlania.world.entity.impl.GroundItemManager;
import com.arlania.world.entity.impl.npc.NPC;
import com.arlania.world.entity.impl.player.Player;

import java.util.Random;

public class Thugbob {

    private static final int[] MINIGUNS = new int[]{20366, 20367, 20368, 20369, 20370, 20371, 20372, 20373};

    public static void handleDrop(Player player, NPC npc) {
        if (player == null || npc == null)
            return;

        Random rng = new Random();

        switch (getMinigun(player)) {
            case 20366:
                if(rng.nextInt(25) == 24) {
                    GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(20366), npc.getPosition().copy(), player.getUsername(), false, 100, false, -1));
                    break;
                }

            case 20367:
                if(rng.nextInt(50) == 49) {
                    GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(20367), npc.getPosition().copy(), player.getUsername(), false, 100, false, -1));
                    break;
                }

            case 20368:
                if(rng.nextInt(100) == 99) {
                    GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(20368), npc.getPosition().copy(), player.getUsername(), false, 100, false, -1));
                    break;
                }

            case 20369:
                if(rng.nextInt(200) == 199) {
                    GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(20369), npc.getPosition().copy(), player.getUsername(), false, 100, false, -1));
                    break;
                }

            case 20370:
                if(rng.nextInt(300) == 299) {
                    GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(20370), npc.getPosition().copy(), player.getUsername(), false, 100, false, -1));
                    break;
                }

            case 20371:
                if(rng.nextInt(400) == 399) {
                    GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(20371), npc.getPosition().copy(), player.getUsername(), false, 100, false, -1));
                    break;
                }

            case 20372:
                if(rng.nextInt(500) == 499) {
                    GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(20372), npc.getPosition().copy(), player.getUsername(), false, 100, false, -1));
                    break;
                }

            case 20373:
                if(rng.nextInt(1000) == 999) {
                    GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(20373), npc.getPosition().copy(), player.getUsername(), false, 100, false, -1));
                    break;
                }

        }
    }


    public static int getMinigun(Player player){
        int rightIndex = -1;
        for(int i = 0; i < MINIGUNS.length; i++){
            if(player.getInventory().contains(MINIGUNS[i]) || player.getEquipment().contains(MINIGUNS[i]) || player.getBank().contains(MINIGUNS[i])) {
                rightIndex = i;
            }
        }
        if(rightIndex != 7){
            rightIndex++;
        }
        return MINIGUNS[rightIndex];
    }
}

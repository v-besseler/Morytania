package com.arlania.world.content.minigames.impl;

import com.arlania.model.GroundItem;
import com.arlania.model.Item;
import com.arlania.world.entity.impl.GroundItemManager;
import com.arlania.world.entity.impl.npc.NPC;
import com.arlania.world.entity.impl.player.Player;

import java.util.Random;

public class KingdomHearts {

    private static final int[] KEYBLADES = new int[]{20246, 20355, 20356, 20357, 20358, 20359, 20360, 20361};

    public static void handleDrop(Player player, NPC npc) {
        if (player == null || npc == null)
            return;

        Random rng = new Random();

        switch (getKeyblade(player)) {
            case 20246:
                if(rng.nextInt(25) == 24) {
                    GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(20246), npc.getPosition().copy(), player.getUsername(), false, 100, false, -1));
                    break;
                }

            case 20355:
                if(rng.nextInt(50) == 49) {
                    GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(20355), npc.getPosition().copy(), player.getUsername(), false, 100, false, -1));
                    break;
                }

            case 20356:
                if(rng.nextInt(100) == 99) {
                    GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(20356), npc.getPosition().copy(), player.getUsername(), false, 100, false, -1));
                    break;
                }

            case 20357:
                if(rng.nextInt(200) == 199) {
                    GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(20357), npc.getPosition().copy(), player.getUsername(), false, 100, false, -1));
                    break;
                }

            case 20358:
                if(rng.nextInt(300) == 299) {
                    GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(20358), npc.getPosition().copy(), player.getUsername(), false, 100, false, -1));
                    break;
                }

            case 20359:
                if(rng.nextInt(400) == 399) {
                    GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(20359), npc.getPosition().copy(), player.getUsername(), false, 100, false, -1));
                    break;
                }

            case 20360:
                if(rng.nextInt(500) == 499) {
                    GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(20360), npc.getPosition().copy(), player.getUsername(), false, 100, false, -1));
                    break;
                }

            case 20361:
                if(rng.nextInt(1000) == 999) {
                    GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(20361), npc.getPosition().copy(), player.getUsername(), false, 100, false, -1));
                    break;
                }

        }
    }


    public static int getKeyblade(Player player){
        int rightIndex = -1;
        for(int i = 0; i < KEYBLADES.length; i++){
            if(player.getInventory().contains(KEYBLADES[i]) || player.getEquipment().contains(KEYBLADES[i]) || player.getBank().contains(KEYBLADES[i])) {
                rightIndex = i;
            }
        }
        if(rightIndex != 7){
            rightIndex++;
        }
        return KEYBLADES[rightIndex];
    }
}

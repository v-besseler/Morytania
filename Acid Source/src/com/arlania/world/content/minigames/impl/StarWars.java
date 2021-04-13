package com.arlania.world.content.minigames.impl;

import com.arlania.model.GroundItem;
import com.arlania.model.Item;
import com.arlania.world.entity.impl.GroundItemManager;
import com.arlania.world.entity.impl.npc.NPC;
import com.arlania.world.entity.impl.player.Player;

import java.util.Random;

public class StarWars {


    private static final int[] LIGHTSABERS = new int[] {20232, 20233, 20234, 20235, 20236, 20237, 20238, 20239};

    public static void handleDrop(Player player, NPC npc) {
        if(player == null || npc == null)
            return;

        Random rng = new Random();

        switch(getLightsaber(player)){
            case 20232:
                if(rng.nextInt(25) == 24) {
                    GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(20232), npc.getPosition().copy(), player.getUsername(), false, 100, false, -1));
                    break;
                }

            case 20233:
                if(rng.nextInt(50) == 49) {
                    GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(20233), npc.getPosition().copy(), player.getUsername(), false, 100, false, -1));
                    break;
                }

            case 20234:
                if(rng.nextInt(100) == 99) {
                    GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(20234), npc.getPosition().copy(), player.getUsername(), false, 100, false, -1));
                    break;
                }

            case 20235:
                if(rng.nextInt(200) == 199) {
                    GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(20235), npc.getPosition().copy(), player.getUsername(), false, 100, false, -1));
                    break;
                }

            case 20236:
                if(rng.nextInt(300) == 299) {
                    GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(20236), npc.getPosition().copy(), player.getUsername(), false, 100, false, -1));
                    break;
                }

            case 20237:
                if(rng.nextInt(400) == 399) {
                    GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(20237), npc.getPosition().copy(), player.getUsername(), false, 100, false, -1));
                    break;
                }

            case 20238:
                if(rng.nextInt(500) == 499) {
                    GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(20238), npc.getPosition().copy(), player.getUsername(), false, 100, false, -1));
                    break;
                }

            case 20239:
                if(rng.nextInt(1000) == 999) {
                    GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(20239), npc.getPosition().copy(), player.getUsername(), false, 100, false, -1));
                    break;
                }
        }



    }

    public static int getLightsaber(Player player){
        int rightIndex = -1;
        for(int i = 0; i < LIGHTSABERS.length; i++){
            if(player.getInventory().contains(LIGHTSABERS[i]) || player.getEquipment().contains(LIGHTSABERS[i]) || player.getBank().contains(LIGHTSABERS[i])) {
                rightIndex = i;
            }
        }
        if(rightIndex != 7){
            rightIndex++;
        }
        return LIGHTSABERS[rightIndex];
    }

}

package com.arlania.world.content;

public class MBoxes {
    public static int[] CRIMSON_ARMOR = new int[] {20293, 20294, 20295, 20296, 20297, 20298, 20299, 20300, 20301};
    public static int[] LAVA_ARMOR = new int[] {20327, 20328, 20329, 20331, 20332, 20333, 20335, 20336, 20337};
    public static int[] LAVA_WEAPONS = new int[] {20326, 20330, 20334};
    public static int[] DBZ_ARMOR = new int[] {20252, 20253, 20254, 20255, 20256, 20257, 20258, 20259, 20260,20261};
    public static int[] MARVEL_ARMOR = new int[] {20217, 20218, 20219, 20220, 20221, 20222, 20223, 20224, 20225};
    public static int[] BEAST_ARMOR = new int[] {20308, 20309, 20310, 20311, 20312, 20313, 20314, 20315, 20316, 20317, 20318, 20319, 20320, 20321, 20322, 20323, 20324, 20325};
    public static int[] ALTAIR_JOKER_VOLD_ARMOR = new int[] {20203, 20204, 20205, 20206, 20207, 20339, 20340, 20341, 20342, 20343, 20226, 20227, 20228, 20229, 20230};
    public static int[] SLAY_SHAM_ARCH_ARMOR = new int[] {20214,20215,20216, 20304, 20305, 20306, 20271, 20272, 20273, 20274, 20275};
    public static int[] AMET_SOL_NECRO_ARMOR = new int[] {20349, 20350, 20351, 20352, 20353, 20354, 20264, 20265, 20266, 20267, 20268};
    public static int[] ALTAIR_JOKER_VOLD_WEAPONS = new int[] {20201, 20202, 20231, 20338};
    public static int[] ICE_WEAPONS = new int[] {20362, 20363, 20364, 20365};
    public static int[] SLAY_SHAM_ARCH_WEAPONS = new int[] {20212, 20213, 20307, 20276};
    public static int[] THOR_BFG_GAUNTS_WEAPONS = new int[] {20292, 20283, 20284, 20250};
    public static int[] AMET_SOL_GUTHIX_WEAPONS = new int[] {20287, 20288, 20269, 20270,20348};

    public static int bondBoxLoot(){
        int lootChance = RNG.randInt(100);

        if(lootChance == 0) {
            return 20383; //$100 bond
        } else if (lootChance < 50){
            return 20379; //$5 bond
        } else if (lootChance < 81) {
            return 20380; //$10 bond
        } else if (lootChance < 95) {
            return 20381; //$25 bond
        } else {
            return 20382; //$50 bond
        }
    }

    public static int bronzeLoot(){
        int lootChance = RNG.randInt(100);

        //System.out.println("Bronze roll: " + lootChance);

        if(lootChance == 0){
            return RNG.randElement(LAVA_WEAPONS);
        } else if(lootChance <= 65){
            return RNG.randElement(LAVA_ARMOR);
        } else if (lootChance <= 95){
            return RNG.randElement(DBZ_ARMOR);
        } else {
            return RNG.randElement(MARVEL_ARMOR);
        }
    }

    public static int silverLoot(){
        int lootChance = RNG.randInt(500);

        //System.out.println("Silver roll: " + lootChance);

        if(lootChance == 0){
            return RNG.randElement(ICE_WEAPONS);
        } else if(lootChance < 300){
            return RNG.randElement(MARVEL_ARMOR);
        } else if(lootChance < 488){
            return RNG.randElement(BEAST_ARMOR);
        } else if(lootChance < 498){
            return RNG.randElement(ALTAIR_JOKER_VOLD_ARMOR);
        } else {
            return RNG.randElement(ALTAIR_JOKER_VOLD_WEAPONS);
        }
    }

    public static int goldLoot(){
        int lootChance = RNG.randInt(500);

        //System.out.println("Gold roll: " + lootChance);

        if(lootChance == 0){
            return RNG.randElement(SLAY_SHAM_ARCH_WEAPONS);
        } else if (lootChance < 300) {
            return RNG.randElement(BEAST_ARMOR);
        } else if (lootChance < 475) {
            return RNG.randElement(ALTAIR_JOKER_VOLD_ARMOR);
        } else if (lootChance < 495) {
            return RNG.randElement(SLAY_SHAM_ARCH_ARMOR);
        } else {
            return RNG.randElement(ALTAIR_JOKER_VOLD_WEAPONS);
        }
    }

    public static int acidLoot(){
        int lootChance = RNG.randInt(500);

        //System.out.println("Acid roll: " + lootChance);

        if(lootChance == 0) {
            return RNG.randElement(AMET_SOL_GUTHIX_WEAPONS);
        } else if(lootChance < 3) {
            return RNG.randElement(AMET_SOL_NECRO_ARMOR);
        } else if (lootChance < 6) {
            return RNG.randElement(THOR_BFG_GAUNTS_WEAPONS);
        } else if (lootChance < 16) {
            return RNG.randElement(SLAY_SHAM_ARCH_WEAPONS);
        } else if (lootChance < 31) {
            return RNG.randElement(SLAY_SHAM_ARCH_ARMOR);
        } else if (lootChance < 56 ) {
            return RNG.randElement(ICE_WEAPONS);
        } else if (lootChance < 101) {
            return RNG.randElement(ALTAIR_JOKER_VOLD_WEAPONS);
        } else if (lootChance < 151) {
            return RNG.randElement(ALTAIR_JOKER_VOLD_ARMOR);
        } else {
            return RNG.randElement(BEAST_ARMOR);
        }


    }

}

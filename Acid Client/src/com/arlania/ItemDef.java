package com.arlania;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class ItemDef {

    private static int[] prices;
    private static List<Integer> untradeableItems = new ArrayList<Integer>();

    public static void nullLoader() {
        modelCache = null;
        spriteCache = null;
        streamIndices = null;
        cache = null;
        stream = null;
    }

	private int[] originalModelColor;
    

    public boolean dialogueModelFetched(int j) {
        int k = maleDialogue;
        int l = maleDialogueModel;
        if (j == 1) {
            k = femaleDialogue;
            l = femaleDialogueModel;
        }
        if (k == -1) {
            return true;
        }
        boolean flag = true;
        if (!Model.modelIsFetched(k)) {
            flag = false;
        }
        if (l != -1 && !Model.modelIsFetched(l)) {
            flag = false;
        }
        return flag;
    }

    public Model getDialogueModel(int gender) {
        int k = maleDialogue;
        int l = maleDialogueModel;
        if (gender == 1) {
            k = femaleDialogue;
            l = femaleDialogueModel;
        }
        if (k == -1) {
            return null;
        }
        Model model = Model.fetchModel(k);



        if (l != -1) {
            Model model_1 = Model.fetchModel(l);
            Model models[] = {model, model_1};
            model = new Model(2, models);
        }
        if (editedModelColor != null) {
            for (int i1 = 0; i1 < editedModelColor.length; i1++) {
                model.recolour(editedModelColor[i1], newModelColor[i1]);
            }
        }
        return model;
    }

    public boolean equipModelFetched(int gender) {
        int fistModel = maleEquip1;
        int secondModel = maleEquip2;
        int thirdModel = maleEquip3;
        if (gender == 1) {
            fistModel = femaleEquip1;
            secondModel = femaleEquip2;
            thirdModel = femaleEquip3;
        }
        if (fistModel == -1) {
            return true;
        }
        boolean flag = true;
        if (!Model.modelIsFetched(fistModel)) {
            flag = false;
        }
        if (secondModel != -1 && !Model.modelIsFetched(secondModel)) {
            flag = false;
        }
        if (thirdModel != -1 && !Model.modelIsFetched(thirdModel)) {
            flag = false;
        }
        return flag;
    }

    public Model getEquipModel(int gender) {
        int j = maleEquip1;
        int k = maleEquip2;
        int l = maleEquip3;
        if (gender == 1) {
            j = femaleEquip1;
            k = femaleEquip2;
            l = femaleEquip3;
        }
        if (j == -1) {
            return null;
        }
        Model model = Model.fetchModel(j);

		if(model == null) {
			return null;
		}


        if (k != -1) {
            if (l != -1) {
                Model model_1 = Model.fetchModel(k);
                Model model_3 = Model.fetchModel(l);
                Model model_1s[] = {model, model_1, model_3};
                model = new Model(3, model_1s);
            } else {
                Model model_2 = Model.fetchModel(k);
                Model models[] = {model, model_2};
                model = new Model(2, models);
            }
        }
        //if (j == 62367)
        //model.translate(68, 7, -8);
        if (gender == 0 && maleYOffset != 0) {
            model.translate(0, maleYOffset, 0);
        } else if (gender == 1 && femaleYOffset != 0) {
            model.translate(0, femaleYOffset, 0);
        }
        if (editedModelColor != null) {
            for (int i1 = 0; i1 < editedModelColor.length; i1++) {
                model.recolour(editedModelColor[i1], newModelColor[i1]);
            }
        }
        return model;
    }

    public void setDefaults() {
        untradeable = false;
        modelID = 0;
        name = null;
        description = null;
        editedModelColor = null;
        newModelColor = null;
        modelZoom = 2000;
        rotationY = 0;
        rotationX = 0;
        modelOffsetX = 0;
        modelOffset1 = 0;
        modelOffsetY = 0;
        stackable = false;
        value = 0;
        membersObject = false;
        groundActions = null;
        actions = null;
        maleEquip1 = -1;
        maleEquip2 = -1;
        maleYOffset = 0;
        maleXOffset = 0;
        femaleEquip1 = -1;
        femaleEquip2 = -1;
        femaleYOffset = 0;
        maleEquip3 = -1;
        femaleEquip3 = -1;
        maleDialogue = -1;
        maleDialogueModel = -1;
        femaleDialogue = -1;
        femaleDialogueModel = -1;
        stackIDs = null;
        stackAmounts = null;
        certID = -1;
        certTemplateID = -1;
        sizeX = 128;
        sizeY = 128;
        sizeZ = 128;
        shadow = 0;
        lightness = 0;
        team = 0;
        lendID = -1;
        lentItemID = -1;
    }

    public static void unpackConfig(CacheArchive streamLoader) {
        /*
		 * stream = new Stream(FileOperations.ReadFile("./Cache/obj.dat"));
		 * Stream stream = new
		 * Stream(FileOperations.ReadFile("./Cache/obj.idx"));
         */
        stream = new Stream(streamLoader.getDataForName("obj.dat"));
        Stream stream = new Stream(streamLoader.getDataForName("obj.idx"));
        totalItems = stream.readUnsignedWord();
        streamIndices = new int[totalItems + 1000];
        int i = 2;
        for (int j = 0; j < totalItems; j++) {
            streamIndices[j] = i;
            i += stream.readUnsignedWord();
        }
        cache = new ItemDef[10];
        for (int k = 0; k < 10; k++) {
            cache[k] = new ItemDef();
        }
        setSettings();
    }

	public static ItemDef copyRotations(ItemDef itemDef, int id) {
		ItemDef itemDef2 = ItemDef.forID(id);
		itemDef.modelOffsetY = itemDef2.modelOffsetY;
		itemDef.modelOffsetX = itemDef2.modelOffsetX;
		itemDef.modelOffsetY = itemDef2.modelOffsetY;
		itemDef.modelOffset1 = itemDef2.modelOffset1;
		itemDef.modelZoom = itemDef2.modelZoom;
		itemDef.rotationX = itemDef2.rotationX;
		itemDef.rotationY = itemDef2.rotationY;
		return itemDef;
	}

	public enum CustomItems {
		//PINK_DILDO(18351, 20, 20, 20, true), // 18983
		;

		private int copy;
		private int inventory;
		private int female;
		private int male;
		private boolean weapon;
		private int[] editedModelColor;
		private int[] originalModelColor;
		private boolean copyDef;
		
		CustomItems(int copy, int model, boolean weapon) {
			this.setCopy(copy);
			this.setInventory(model);
			this.setFemale(model);
			this.setMale(model);
			this.setWeapon(weapon);
		}
		
		CustomItems(int copy, int inventory, int wield, boolean weapon) {
			this.setCopy(copy);
			this.setInventory(inventory);
			this.setFemale(wield);
			this.setMale(wield);
			this.setWeapon(weapon);
		}

		CustomItems(int copy, int inventory, int female, int male, boolean weapon) {
			this.setCopy(copy);
			this.setInventory(inventory);
			this.setFemale(female);
			this.setMale(male);
			this.setWeapon(weapon);
		}
		
		CustomItems(int copy, int[] editedModelColor, int[] originalModelColor) {
			setCopyDef(true);
			this.setCopy(copy);
			this.editedModelColor = editedModelColor;
			this.originalModelColor = originalModelColor;
		}

		public int getCopy() {
			return copy;
		}

		public void setCopy(int copy) {
			this.copy = copy;
		}

		public int getInventory() {
			return inventory;
		}

		public void setInventory(int inventory) {
			this.inventory = inventory;
		}

		public int getFemale() {
			return female;
		}

		public void setFemale(int female) {
			this.female = female;
		}

		public int getMale() {
			return male;
		}

		public void setMale(int male) {
			this.male = male;
		}

		public boolean isWeapon() {
			return weapon;
		}

		public void setWeapon(boolean weapon) {
			this.weapon = weapon;
		}

		public int[] getEditedModelColor() {
			return editedModelColor;
		}

		public void setEditedModelColor(int[] editedModelColor) {
			this.editedModelColor = editedModelColor;
		}

		public int[] getOriginalModelColor() {
			return originalModelColor;
		}

		public void setOriginalModelColor(int[] originalModelColor) {
			this.originalModelColor = originalModelColor;
		}

		public boolean isCopyDef() {
			return copyDef;
		}

		public void setCopyDef(boolean copyDef) {
			this.copyDef = copyDef;
		}
	}
	public static String ucFirst(String str) {
		str = str.toLowerCase().replaceAll("_", " ");
		if (str.length() > 1) {
			str = str.substring(0, 1).toUpperCase() + str.substring(1);
		} else {
			return str.toUpperCase();
		}
		return str;
	}
    
    public static ItemDef forID(int i) {
        for (int j = 0; j < 10; j++) {
            if (cache[j].id == i) {
                return cache[j];
            }
        }
        cacheIndex = (cacheIndex + 1) % 10;
        ItemDef itemDef = cache[cacheIndex];
        if (i >= streamIndices.length) {
            itemDef.id = 1;
            itemDef.setDefaults();
            return itemDef;
        }
        stream.currentOffset = streamIndices[i];
        itemDef.id = i;
        itemDef.setDefaults();
        itemDef.readValues(stream);
        if (itemDef.certTemplateID != -1) {
            itemDef.toNote();
        }
        if (itemDef.lentItemID != -1) {
            itemDef.toLend();
        }
        if (itemDef.id == i && itemDef.editedModelColor == null) {
            itemDef.editedModelColor = new int[1];
            itemDef.newModelColor = new int[1];
            itemDef.editedModelColor[0] = 0;
            itemDef.newModelColor[0] = 1;
        }
        if (untradeableItems.contains(itemDef.id)) {
            itemDef.untradeable = true;
        }
        itemDef.value = prices[itemDef.id];
        int custom_start = 18888;
		//System.out.println("Custom items: "+CustomItems.values().length);	
		for (CustomItems custom : CustomItems.values()) {
			if (i == custom_start + custom.ordinal()) {
				itemDef = copyRotations(itemDef, custom.getCopy());
				itemDef.name = ucFirst(custom.name());
				if(custom.isCopyDef()) {
					ItemDef def2 = ItemDef.forID(custom.getCopy());
					itemDef.modelID = def2.modelID;
					itemDef.maleEquip1 = def2.maleEquip1;
					itemDef.femaleEquip1 = def2.femaleEquip1;
					itemDef.editedModelColor = custom.editedModelColor;
					itemDef.newModelColor = custom.originalModelColor;
				} else {
					itemDef.modelID = custom.getInventory();
					itemDef.maleEquip1 = custom.getMale();
					itemDef.femaleEquip1 = custom.getFemale();
				}
				itemDef.actions = new String[5];
				itemDef.actions[1] = custom.isWeapon() ? "Wield" : "Wear";
			}
		}
        switch (i) {
        case 18904:
			itemDef.name = "Blessed staff of light";
			itemDef.modelZoom = 1853;
			itemDef.rotationX = 1508;
			itemDef.rotationY = 364;
			itemDef.modelOffsetY = 21;
			itemDef.modelOffset1 = 1;
			itemDef.groundActions = new String[] { null, null, "Take", null, null };
			itemDef.actions = new String[] { null, "Wear", null, null, "Drop" };
			itemDef.modelID = 34518;
			itemDef.maleEquip1 = 34508;
			itemDef.femaleEquip1 = 34508;
			break;
        case 18902:
			itemDef.name = "Trident of the swamp";
			itemDef.modelZoom = 2421;
			itemDef.rotationY = 1549;
			itemDef.rotationX = 1818;
			itemDef.modelOffsetY = 9;
			itemDef.modelOffsetX = 290;
			itemDef.groundActions = new String[] { null, null, "Take", null, null };
			itemDef.actions = new String[] { null, "Wear", null, null, "Drop" };
			itemDef.modelID = 19223;
			itemDef.maleEquip1 = 14400;
			itemDef.femaleEquip1 = 14400;
			break;
		case 18903:
			itemDef.name = "Granite maul (or)";
			itemDef.modelZoom = 1789;
			itemDef.rotationY = 157;
			itemDef.rotationX = 148;
			itemDef.groundActions = new String[] { null, null, "Take", null, null };
			itemDef.actions = new String[] { null, "Wear", null, null, "Drop" };
			itemDef.modelID = 28990;
			itemDef.maleEquip1 = 28992;
			itemDef.femaleEquip1 = 28992;
			break;
        case 18895:
			itemDef.name = "Necklace of anguish";
			itemDef.modelZoom = 1020;
			itemDef.rotationY = 332;
			itemDef.rotationX = 2020;
			itemDef.modelOffsetY = -16;
			itemDef.actions = new String[5];
			itemDef.actions[1] = "Wear";
			itemDef.actions[4] = "Drop";
			itemDef.modelID = 31510;
			itemDef.maleEquip1 = 31228;
			itemDef.femaleEquip1 = 31228;
			break;
		case 18896:
			itemDef.name = "Amulet of torture";
			itemDef.modelZoom = 620;
			itemDef.rotationY = 424;
			itemDef.rotationX = 68;
			itemDef.modelOffset1 = 1;
			itemDef.modelOffsetY = 16;
			itemDef.actions = new String[5];
			itemDef.actions[1] = "Wear";
			itemDef.actions[4] = "Drop";
			itemDef.modelID = 31524;
			itemDef.maleEquip1 = 31227;
			itemDef.femaleEquip1 = 31227;
			break;
		case 18897:
			itemDef.name = "Occult necklace";
			itemDef.modelZoom = 589;
			itemDef.rotationY = 431;
			itemDef.rotationX = 81;
			itemDef.modelOffset1 = 3;
			itemDef.modelOffsetY = 21;
			itemDef.actions = new String[5];
			itemDef.actions[1] = "Wear";
			itemDef.actions[4] = "Drop";
			itemDef.modelID = 28438;
			itemDef.maleEquip1 = 28445;
			itemDef.femaleEquip1 = 28445;
			break;
		case 18898:
			itemDef.name = "Ring of suffering";
			itemDef.modelZoom = 830;
			itemDef.rotationY = 322;
			itemDef.rotationX = 135;
			itemDef.modelOffset1 = -1;
			itemDef.modelOffsetY = 1;
			itemDef.actions = new String[5];
			itemDef.actions[1] = "Wear";
			itemDef.actions[4] = "Drop";
			itemDef.modelID = 31519;
			itemDef.maleEquip1 = 31519;
			itemDef.femaleEquip1 = 31519;
			break;
        case 18888:
			itemDef.actions = new String[5];
			itemDef.actions[1] = "Wear";
			itemDef.actions[4] = "Drop";
			itemDef.modelID = 32794;
			itemDef.name = "Ancestral hat";
			itemDef.modelZoom = 1236;
			itemDef.rotationY = 118;
			itemDef.rotationX = 10;
			itemDef.modelOffset1 = 0;
			itemDef.modelOffsetY = -12;
			itemDef.femaleEquip1 = 32663;
			itemDef.maleEquip1 = 32655;
			itemDef.description = "The hat of a powerful sorceress from a bygone era.";
			break;
		case 18889:
			itemDef.actions = new String[5];
			itemDef.actions[1] = "Wear";
			itemDef.actions[4] = "Drop";
			itemDef.modelID = 32790;
			itemDef.name = "Ancestral robe top";
			itemDef.modelZoom = 1358;
			itemDef.rotationY = 514;
			itemDef.rotationX = 2041;
			itemDef.modelOffset1 = 0;
			itemDef.modelOffsetY = -3;
			itemDef.femaleEquip1 = 32664;
			itemDef.maleEquip1 = 32657;
			itemDef.maleEquip2 = 32658; // male arms
			itemDef.femaleEquip2 = 32665; // female arms
			itemDef.description = "The robe top of a powerful sorceress from a bygone era.";
			break;
		case 18890:
			itemDef.actions = new String[5];
			itemDef.actions[1] = "Wear";
			itemDef.actions[4] = "Drop";
			itemDef.modelID = 32787;
			itemDef.name = "Ancestral robe bottom";
			itemDef.modelZoom = 1690;
			itemDef.rotationY = 435;
			itemDef.rotationX = 9;
			itemDef.modelOffset1 = 1;
			itemDef.modelOffsetY = 7;
			itemDef.femaleEquip1 = 32653;
			itemDef.maleEquip1 = 32662;
			itemDef.description = "The robe bottom of a powerful sorceress from a bygone era.";
			break;
		case 18891:
			itemDef.actions = new String[5];
			itemDef.actions[1] = "Wear";
			itemDef.actions[4] = "Drop";
			itemDef.modelID = 35751;
			itemDef.name = "Justiciar faceguard";
			itemDef.modelZoom = 777;
			itemDef.rotationY = 22;
			itemDef.rotationX = 1972;
			itemDef.modelOffset1 = 1;
			itemDef.modelOffsetY = -1;
			itemDef.femaleEquip1 = 35361;
			itemDef.maleEquip1 = 35349;
			break;
		case 18892:
			itemDef.actions = new String[5];
			itemDef.actions[1] = "Wear";
			itemDef.actions[4] = "Drop";
			itemDef.modelID = 35750;
			itemDef.name = "Justiciar chestguard";
			itemDef.modelZoom = 1310;
			itemDef.rotationY = 432;
			itemDef.rotationX = 0;
			itemDef.modelOffset1 = 1;
			itemDef.modelOffsetY = 4;
			itemDef.femaleEquip1 = 35368;
			itemDef.maleEquip1 = 35359;
			break;
		case 18893:
			itemDef.actions = new String[5];
			itemDef.actions[1] = "Wear";
			itemDef.actions[4] = "Drop";
			itemDef.modelID = 35752;
			itemDef.name = "Justiciar legguards";
			itemDef.modelZoom = 1720;
			itemDef.rotationY = 468;
			itemDef.rotationX = 0;
			itemDef.modelOffset1 = 0;
			itemDef.modelOffsetY = 0;
			itemDef.femaleEquip1 = 35367;
			itemDef.maleEquip1 = 35356;
			break;
		case 18894:
			itemDef.actions = new String[5];
			itemDef.actions[1] = "Wield";
			itemDef.actions[4] = "Drop";
			itemDef.modelID = 35739;
			itemDef.name = "Ghrazi rapier";
			itemDef.modelZoom = 2064;
			itemDef.rotationY = 0;
			itemDef.rotationX = 1603;
			itemDef.modelOffset1 = 5;
			itemDef.modelOffsetX = 552;
			itemDef.modelOffsetY = -18;
			itemDef.femaleEquip1 = 35369;
			itemDef.maleEquip1 = 35374;
			break;
		case 18899:
			itemDef.actions = new String[5];
			itemDef.actions[1] = "Wield";
			itemDef.actions[4] = "Drop";
			itemDef.modelID = 35742;
			itemDef.name = "Scythe of vitur";
			itemDef.modelZoom = 2105;
			itemDef.rotationY = 327;
			itemDef.rotationX = 23;
			itemDef.modelOffset1 = 1;
			itemDef.modelOffsetX = 0;
			itemDef.modelOffsetY = 17;
			itemDef.femaleEquip1 = 35371;
			itemDef.maleEquip1 = 35371;
			break;
		case 18900:
			itemDef.actions = new String[5];
			itemDef.actions[1] = "Wield";
			itemDef.actions[4] = "Drop";
			itemDef.modelID = 35744;
			itemDef.name = "Sanguinesti staff";
			itemDef.modelZoom = 2258;
			itemDef.rotationY = 552;
			itemDef.rotationX = 1558;
			itemDef.modelOffset1 = 1;
			itemDef.modelOffsetY = 7;
			itemDef.femaleEquip1 = 35372;
			itemDef.maleEquip1 = 35372;
			break;
		case 18901:
			itemDef.actions = new String[5];
			itemDef.actions[1] = "Wield";
			itemDef.actions[4] = "Drop";
			itemDef.modelID = 35745;
			itemDef.name = "Avernic defender";
			itemDef.modelZoom = 717;
			itemDef.rotationY = 498;
			itemDef.rotationX = 256;
			itemDef.modelOffset1 = 8;
			itemDef.modelOffsetX = 2047;
			itemDef.modelOffsetY = 8;
			itemDef.femaleEquip1 = 35377;
			itemDef.maleEquip1 = 35376;
			break;
		case 20998:
			itemDef.actions = new String[5];
			itemDef.actions[1] = "Wear";
			itemDef.actions[4] = "Drop";
			itemDef.modelID = 32799;
			itemDef.name = "Twisted bow";
			itemDef.modelZoom = 2000;
			itemDef.rotationY = 720;
			itemDef.rotationX = 1500;
			itemDef.modelOffset1 = 3;
			itemDef.modelOffsetY = 1;
			itemDef.femaleEquip1 = 32674;
			itemDef.maleEquip1 = 32674;
			itemDef.description = "A mystical bow carved from the twisted remains of the Great Olm.";
			break;
        case 20061:
        	itemDef.modelID = 10247;
        	itemDef.name = "Abyssal vine whip";
        	itemDef.description = "Abyssal vine whip";
        	itemDef.modelZoom = 848;
        	itemDef.rotationY = 324;
        	itemDef.rotationX = 1808;
        	itemDef.modelOffset1 = 5;
        	itemDef.modelOffsetY = 38;
        	itemDef.maleEquip1 = 10253;
        	itemDef.femaleEquip1 = 10253;
        	itemDef.groundActions = new String[] { null, null, "Take", null, null };
			itemDef.actions = new String[] { null, "Wear", null, null, "Drop" };
        	break;
		case 20010: 
		      itemDef.name = "Trickster robe";
		      itemDef.description = "Its a Trickster robe";
		      itemDef.maleEquip1 = 44786;
		      itemDef.femaleEquip1 = 44786;
		      itemDef.modelID = 45329;
		      itemDef.rotationY = 593;
		      itemDef.rotationX = 2041;
		      itemDef.modelZoom = 1420;
		      itemDef.modelOffsetY = 0;
		      itemDef.modelOffset1 = 0;
	          itemDef.groundActions = new String[] { null, null, "Take", null, null };
		      itemDef.actions = new String[5];
		      itemDef.actions[1] = "Wear";
		      break;
		    case 20011: 
		      itemDef.name = "Trickster robe legs";
		      itemDef.description = "Its a Trickster robe";
		      itemDef.maleEquip1 = 44770;
		      itemDef.femaleEquip1 = 44770;
		      itemDef.modelID = 45335;
		      itemDef.rotationY = 567;
		      itemDef.rotationX = 1023;
		      itemDef.modelZoom = 2105;
		      itemDef.modelOffset1 = 0;
		      itemDef.modelOffsetY = 0;
	          itemDef.groundActions = new String[] { null, null, "Take", null, null };
		      itemDef.actions = new String[5];
		      itemDef.actions[1] = "Wear";
		      break;
		    case 20012: 
		      itemDef.name = "Trickster helm";
		      itemDef.description = "Its a Trickster helm";
		      itemDef.maleEquip1 = 44764;
		      itemDef.femaleEquip1 = 44764;
		      itemDef.modelID = 45328;
		      itemDef.rotationY = 5;
		      itemDef.rotationX = 1889;
		      itemDef.modelZoom = 738;
		      itemDef.modelOffsetY = 0;
		      itemDef.modelOffset1 = 0;
	          itemDef.groundActions = new String[] { null, null, "Take", null, null };
		      itemDef.actions = new String[5];
		      itemDef.actions[1] = "Wear";
		      break;
		    case 20013: 
		        itemDef.modelID = 44633;
		        itemDef.name = "Vanguard helm";
		        itemDef.modelZoom = 855;
		        itemDef.rotationY = 1966;
		        itemDef.rotationX = 5;
		        itemDef.modelOffsetY = 4;
		        itemDef.modelOffset1 = -1;
	            itemDef.groundActions = new String[] { null, null, "Take", null, null };
		        itemDef.actions = new String[5];
		        itemDef.actions[1] = "Wear";
		        itemDef.maleEquip1 = 44769;
		        itemDef.femaleEquip1 = 44769;
		        break;
		      case 20014: 
		        itemDef.modelID = 44627;
		        itemDef.name = "Vanguard body";
		        itemDef.modelZoom = 1513;
		        itemDef.rotationX = 2041;
		        itemDef.rotationY = 593;
		        itemDef.modelOffset1 = 3;
		        itemDef.modelOffsetY = -11;
	            itemDef.groundActions = new String[] { null, null, "Take", null, null };
		        itemDef.actions = new String[5];
		        itemDef.actions[1] = "Wear";
		        itemDef.maleEquip1 = 44812;
		        itemDef.femaleEquip1 = 44812;
		        break;
		      case 14062: 
		        itemDef.modelID = 50011;
		        itemDef.name = "Vanguard legs";
		        itemDef.modelZoom = 1711;
		        itemDef.rotationX = 0;
		        itemDef.rotationY = 360;
		        itemDef.modelOffset1 = 3;
		        itemDef.modelOffsetY = -11;
	            itemDef.groundActions = new String[] { null, null, "Take", null, null };
		        itemDef.actions = new String[5];
		        itemDef.actions[1] = "Wear";
		        itemDef.maleEquip1 = 44771;
		        itemDef.femaleEquip1 = 44771;
		        break;
		      case 19020:
					itemDef.modelID = 44699;
					itemDef.name = "Vanguard gloves";
					itemDef.modelZoom = 830;
					itemDef.rotationY = 536;
					itemDef.rotationX = 0;
					itemDef.modelOffsetX = 9;
					itemDef.modelOffsetY = 3;
					itemDef.groundActions = new String[] { null, null, "Take", null, null };
					itemDef.actions = new String[] { null, "Wear", null, null, "Drop" };
					itemDef.maleEquip1 = 44758;
					itemDef.femaleEquip1 = 44758;
					break;
				case 19021:
					itemDef.modelID = 44700;
					itemDef.name = "Vanguard boots";
					itemDef.modelZoom = 848;
					itemDef.rotationY = 141;
					itemDef.rotationX = 141;
					itemDef.modelOffset1 = 4;
					itemDef.modelOffsetY = 0;
					itemDef.groundActions = new String[] { null, null, "Take", null, null };
					itemDef.actions = new String[] { null, "Wear", null, null, "Drop" };
					itemDef.maleEquip1 = 44752;
					itemDef.femaleEquip1 = 44752;
					break;
		      case 20016: 
		          itemDef.modelID = 44704;
		          itemDef.name = "Battle-mage helm";
		          itemDef.modelZoom = 658;
		          itemDef.rotationX = 1898;
		          itemDef.rotationY = 2;
		          itemDef.modelOffset1 = 12;
		          itemDef.modelOffsetY = 3;
		          itemDef.groundActions = new String[] { null, null, "Take", null, null };
		          itemDef.actions = new String[5];
		          itemDef.actions[1] = "Wear";
		          itemDef.maleEquip1 = 44767;
		          itemDef.femaleEquip1 = 44767;
		          break;
		        case 20017: 
		          itemDef.modelID = 44631;
		          itemDef.name = "Battle-mage robe";
		          itemDef.modelZoom = 1382;
		          itemDef.rotationX = 3;
		          itemDef.rotationY = 488;
		          itemDef.modelOffset1 = 1;
		          itemDef.modelOffsetY = 0;
		          itemDef.groundActions = new String[] { null, null, "Take", null, null };
		          itemDef.actions = new String[5];
		          itemDef.actions[1] = "Wear";
		          itemDef.maleEquip1 = 44818;
		          itemDef.femaleEquip1 = 44818;
		          break;
		        case 20018: 
		          itemDef.modelID = 44672;
		          itemDef.name = "Battle-mage robe legs";
		          itemDef.modelZoom = 1842;
		          itemDef.rotationX = 1024;
		          itemDef.rotationY = 498;
		          itemDef.modelOffset1 = 4;
		          itemDef.modelOffsetY = -1;
		          itemDef.groundActions = new String[] { null, null, "Take", null, null };
		          itemDef.actions = new String[5];
		          itemDef.actions[1] = "Wear";
		          itemDef.maleEquip1 = 44775;
		          itemDef.femaleEquip1 = 44775;
		          break;
        case 20019: 
            itemDef.modelID = 45316;
            itemDef.name = "Trickster boots";
            itemDef.modelZoom = 848;
            itemDef.rotationY = 141;
            itemDef.rotationX = 141;
            itemDef.modelOffset1 = -9;
            itemDef.modelOffsetY = 0;
            itemDef.groundActions = new String[] { null, null, "Take", null, null };
            itemDef.actions = new String[5];
            itemDef.actions[1] = "Wear";
            itemDef.maleEquip1 = 44757;
            itemDef.femaleEquip1 = 44757;
            break;
          case 20020: 
            itemDef.modelID = 45317;
            itemDef.name = "Trickster gloves";
            itemDef.modelZoom = 830;
            itemDef.rotationX = 150;
            itemDef.rotationY = 536;
            itemDef.modelOffset1 = 1;
            itemDef.modelOffsetY = 3;
            itemDef.groundActions = new String[] { null, null, "Take", null, null };
            itemDef.actions = new String[5];
            itemDef.actions[1] = "Wear";
            itemDef.maleEquip1 = 44761;
            itemDef.femaleEquip1 = 44761;
            break;
          case 20021: 
            itemDef.modelID = 44662;
            itemDef.name = "Battle-mage boots";
            itemDef.modelZoom = 987;
            itemDef.rotationX= 1988;
            itemDef.rotationY = 188;
            itemDef.modelOffset1 = -8;
            itemDef.modelOffsetY = 5;
            itemDef.groundActions = new String[] { null, null, "Take", null, null };
            itemDef.actions = new String[5];
            itemDef.actions[1] = "Wear";
            itemDef.maleEquip1 = 44755;
            itemDef.femaleEquip1 = 44755;
            break;
          case 20022: 
            itemDef.modelID = 44573;
            itemDef.name = "Battle-mage gloves";
            itemDef.modelZoom = 1053;
            itemDef.rotationX = 0;
            itemDef.rotationY = 536;
            itemDef.modelOffset1 = 3;
            itemDef.modelOffsetY = 0;
            itemDef.groundActions = new String[] { null, null, "Take", null, null };
            itemDef.actions = new String[5];
            itemDef.actions[1] = "Wear";
            itemDef.maleEquip1 = 44762;
            itemDef.femaleEquip1 = 44762;
            break;
          	case 11554: 
          		itemDef.name = "Abyssal tentacle";
          		itemDef.modelZoom = 840;
          		itemDef.rotationY = 280;
          		itemDef.rotationX = 121;
          		itemDef.modelOffsetY = 56;
          		itemDef.groundActions = new String[5];
          		itemDef.groundActions[2] = "Take";
          		itemDef.actions = new String[5];
          		itemDef.actions[1] = "Wear";
          		itemDef.actions[4] = "Drop";
          		itemDef.modelID = 28439;
          		itemDef.maleEquip1 = 45006;
          		itemDef.femaleEquip1 = 43500;
          		break;
		case 11926:
			itemDef.name = "Odium ward";
			itemDef.modelZoom = 1200;
			itemDef.rotationY = 568;
			itemDef.rotationX = 1836;
			itemDef.modelOffsetX = 2;
			itemDef.modelOffsetY = 3;
			itemDef.newModelColor = new int[] { 15252 };
			itemDef.editedModelColor = new int[] { 908 };
			itemDef.modelID = 9354;
			itemDef.actions[1] = "Wield";
			itemDef.actions[4] = "Drop";
			itemDef.maleEquip1 = 9347;
			itemDef.femaleEquip1 = 9347;
			break;
		case 11288:
		      itemDef.actions = new String[5];
		      itemDef.actions[1] = "Wear";
		      itemDef.actions[4] = "Drop";
		      itemDef.editedModelColor = new int[1];
		      itemDef.newModelColor = new int[1];
		      itemDef.editedModelColor[0] = 926;
		      itemDef.newModelColor[0] = 196608;
		      itemDef.modelID = 2438;
		      itemDef.modelZoom = 730;
		      itemDef.rotationY = 516;
		      itemDef.rotationX = 0;
		      itemDef.modelOffset1 = 0;
		      itemDef.modelOffsetY = -10;
		      itemDef.maleEquip1 = 3188;
		      itemDef.femaleEquip1 = 3192;
		      itemDef.name = "Black h'ween Mask";
		      itemDef.description = "Aaaarrrghhh... I'm a monster.";
		      break;
		case 11924:
			itemDef.name = "Malediction ward";
			itemDef.modelZoom = 1200;
			itemDef.rotationY = 568;
			itemDef.rotationX = 1836;
			itemDef.modelOffsetX = 2;
			itemDef.modelOffsetY = 3;
			itemDef.newModelColor = new int[] { -21608 };
			itemDef.editedModelColor = new int[] { 908 };
			itemDef.modelID = 9354;
			itemDef.actions[1] = "Wield";
			itemDef.actions[4] = "Drop";
			itemDef.maleEquip1 = 9347;
			itemDef.femaleEquip1 = 9347;
			break;
		case 12282: 
		      itemDef.name = "Serpentine helm";
		      itemDef.modelID = 19220;
		      itemDef.modelZoom = 700;
		      itemDef.rotationX = 1724;
		      itemDef.rotationY = 215;
		      itemDef.modelOffsetX = 17;
		      itemDef.femaleEquip1 = 14398;
		      itemDef.maleEquip1 = 14395;
	          itemDef.groundActions = new String[] { null, null, "Take", null, null };
		      itemDef.actions[1] = "Wear";
		      itemDef.actions[4] = "Drop";
		      break;
		case 12279: 
		      itemDef.name = "Magma helm";
		      itemDef.modelID = 29205;
		      itemDef.modelZoom = 700;
		      itemDef.rotationX = 1724;
		      itemDef.rotationY = 215;
		      itemDef.modelOffsetX = 17;
	          itemDef.groundActions = new String[] { null, null, "Take", null, null };
		      itemDef.femaleEquip1 = 14426;
		      itemDef.maleEquip1 = 14424;
		      itemDef.actions[1] = "Wear";
		      itemDef.actions[4] = "Drop";
		      break;
		case 12278: 
		      itemDef.name = "Tanzanite helm";
		      itemDef.modelID = 29213;
		      itemDef.modelZoom = 700;
		      itemDef.rotationX = 1724;
		      itemDef.rotationY = 215;
		      itemDef.modelOffsetX = 17;
	          itemDef.groundActions = new String[] { null, null, "Take", null, null };
		      itemDef.femaleEquip1 = 23994;
		      itemDef.maleEquip1 = 14421;
		      itemDef.actions[1] = "Wear";
		      itemDef.actions[4] = "Drop";
		      break;
		case 13239:
			itemDef.name = "Primordial boots";
			itemDef.modelID = 29397;
			itemDef.modelZoom = 976;
			itemDef.rotationY = 147;
			itemDef.rotationX = 279;
			itemDef.modelOffsetX = 5;
			itemDef.modelOffsetY = 5;
			itemDef.maleEquip1 = 29250;
			itemDef.femaleEquip1 = 29255;
			itemDef.groundActions = new String[] { null, null, "Take", null, null };
			itemDef.actions = new String[] { null, "Wear", null, null, "Drop" };
			break;
		case 12708:
			itemDef.name = "Pegasian boots";
			itemDef.modelID = 29396;
			itemDef.actions = new String[5];
			itemDef.actions[1] = "Wear";
			itemDef.groundActions = new String[5];
			itemDef.groundActions[1] = "Take";
			itemDef.modelZoom = 900;
			itemDef.rotationY = 165;
			itemDef.rotationX = 279;
			itemDef.modelOffsetX = 3;
			itemDef.modelOffsetY =-7;
			itemDef.maleEquip1 = 29252;
			itemDef.femaleEquip1 = 29253;
			break;
			case 16137:
                itemDef.name = "Thok's Sword";
                break;
        case 1543:
                itemDef.name = "Wilderness Key";
                break;
	case 15356:
                itemDef.name = "$5 Scroll";
                itemDef.actions = new String[5];
                itemDef.actions[4] = "Drop";
                itemDef.actions[0] = "Claim";
                ItemDef itemDef18 = ItemDef.forID(761);
                itemDef.modelID = itemDef18.modelID;
                itemDef.modelOffset1 = itemDef18.modelOffset1;
                itemDef.modelOffsetX = itemDef18.modelOffsetX;
                itemDef.modelOffsetY = itemDef18.modelOffsetY;
                itemDef.modelZoom = itemDef18.modelZoom;
                break;
        case 15355:
                itemDef.name = "$10 Scroll";
                itemDef.actions = new String[5];
                itemDef.actions[4] = "Drop";
                itemDef.actions[0] = "Claim";
                ItemDef itemDef19 = ItemDef.forID(761);
                itemDef.modelID = itemDef19.modelID;
                itemDef.modelOffset1 = itemDef19.modelOffset1;
                itemDef.modelOffsetX = itemDef19.modelOffsetX;
                itemDef.modelOffsetY = itemDef19.modelOffsetY;
                itemDef.modelZoom = itemDef19.modelZoom;
                break;
         case 15359:
                itemDef.name = "$25 Scroll";
                itemDef.actions = new String[5];
                itemDef.actions[4] = "Drop";
                itemDef.actions[0] = "Claim";
                ItemDef itemDef20 = ItemDef.forID(761);
                itemDef.modelID = itemDef20.modelID;
                itemDef.modelOffset1 = itemDef20.modelOffset1;
                itemDef.modelOffsetX = itemDef20.modelOffsetX;
                itemDef.modelOffsetY = itemDef20.modelOffsetY;
                itemDef.modelZoom = itemDef20.modelZoom;
                break;
         case 15358:
                itemDef.name = "$50 Scroll";
                itemDef.actions = new String[5];
                itemDef.actions[4] = "Drop";
                itemDef.actions[0] = "Claim";
                ItemDef itemDef21 = ItemDef.forID(761);
                itemDef.modelID = itemDef21.modelID;
                itemDef.modelOffset1 = itemDef21.modelOffset1;
                itemDef.modelOffsetX = itemDef21.modelOffsetX;
                itemDef.modelOffsetY = itemDef21.modelOffsetY;
                itemDef.modelZoom = itemDef21.modelZoom;
                break;

			case 13235:
			itemDef.name = "Eternal boots";
			itemDef.modelID = 29394;
			itemDef.modelZoom = 976;
			itemDef.rotationY = 147;
			itemDef.rotationX = 279;
			itemDef.modelOffsetX = 5;
			itemDef.modelOffsetY = 5;
			itemDef.maleEquip1 = 29249;
			itemDef.femaleEquip1 = 29254;
			itemDef.groundActions = new String[] { null, null, "Take", null, null };
			itemDef.actions = new String[] { null, "Wear", null, null, "Drop" };
				break;
						        case 20059: 
			            itemDef.name = "Drygore rapier";
			            itemDef.modelZoom = 1145;
			            itemDef.rotationX = 2047;
			            itemDef.rotationY = 254;
			            itemDef.modelOffset1 = -3;
			            itemDef.modelOffsetY = -45;
			            itemDef.groundActions = new String[] { null, null, "Take", null, null };
			            itemDef.actions = new String[] { null, "Wield", "Check-charges", null, "Drop" };
			            itemDef.modelID = 14000;
			            itemDef.maleEquip1 = 14001;
			            itemDef.femaleEquip1 = 14001;
			            break;
			          case 20057: 
			            itemDef.name = "Drygore longsword";
			            itemDef.modelZoom = 1645;
			            itemDef.rotationX = 377;
			            itemDef.rotationY = 444;
			            itemDef.modelOffset1 = 3;
			            itemDef.modelOffsetY = 0;
			            itemDef.groundActions = new String[] { null, null, "Take", null, null };
			            itemDef.actions = new String[] { null, "Wield", "Check-charges", null, "Drop" };
			            itemDef.modelID = 14022;
			            itemDef.maleEquip1 = 14023;
			            itemDef.femaleEquip1 = 14023;
			            break;
			          case 20058: 
			            itemDef.name = "Drygore mace";
			            itemDef.modelZoom = 1118;
			            itemDef.rotationX = 28;
			            itemDef.rotationY = 235;
			            itemDef.modelOffset1 = -1;
			            itemDef.modelOffsetY = -47;
			            itemDef.groundActions = new String[] { null, null, "Take", null, null };
			            itemDef.actions = new String[] { null, "Wield", "Check-charges", null, "Drop" };
			            itemDef.modelID = 14024;
			            itemDef.maleEquip1 = 14025;
			            itemDef.femaleEquip1 = 14025;
			            break;
			            /**END OF OSRS ITEMS**/
            case 19670:
                itemDef.name = "Vote scroll";
                itemDef.actions = new String[5];
                itemDef.actions[4] = "Drop";
                itemDef.actions[0] = "Claim";
                itemDef.actions[2] = "Claim-All";
                break;
            case 10034:
            case 10033:
                itemDef.actions = new String[]{null, null, null, null, "Drop"};
                break;
            case 13727:
                itemDef.actions = new String[]{null, null, null, null, "Drop"};
                break;
            case 6500:
                itemDef.modelID = 9123;
                itemDef.name = "Charming imp";
                //	itemDef.modelZoom = 672;
                //	itemDef.rotationY = 85;
                //	itemDef.rotationX = 1867;
                itemDef.actions = new String[]{null, null, "Check", "Config", "Drop"};
                break;
			case 13045:
				itemDef.name = "Abyssal bludgeon";
				itemDef.modelZoom = 2611;
				itemDef.rotationY = 552;
				itemDef.rotationX = 1508;
				itemDef.modelOffsetY = 3;
				itemDef.modelOffset1 = -17;
				itemDef.groundActions = new String[] { null, null, "Take", null, null };
				itemDef.actions = new String[] { null, "Wield", "Check", "Uncharge", "Drop" };
				itemDef.modelID = 29597;
				itemDef.maleEquip1 = 29424;
				itemDef.femaleEquip1 = 29424;
			break;
			case 13047:
				itemDef.name = "Abyssal dagger";
				itemDef.modelZoom = 1347;
				itemDef.rotationY = 1589;
				itemDef.rotationX = 781;
				itemDef.modelOffsetY = 3;
				itemDef.modelOffset1 = -5;
				itemDef.groundActions = new String[] { null, null, "Take", null, null };
				itemDef.actions = new String[] { null, "Wield", "Check", "Uncharge", "Drop" };
				itemDef.modelID = 29598;
				itemDef.maleEquip1 = 29425;
				itemDef.femaleEquip1 = 29425;
			break;
		    case 500: 
		      itemDef.modelID = 2635;
		      itemDef.name = "Black Party Hat";
		      itemDef.description = "Black Party Hat";
		      itemDef.modelZoom = 440;
		      itemDef.rotationX = 1845;
		      itemDef.rotationY = 121;
		      itemDef.modelOffset1 = 0;
		      itemDef.modelOffsetY = 1;
		      itemDef.stackable = false;
		      itemDef.value = 1;
		      itemDef.membersObject = true;
		      itemDef.maleEquip1 = 187;
		      itemDef.femaleEquip1 = 363;
		      itemDef.actions = new String[5];
		      itemDef.actions[1] = "Wear";
		      itemDef.actions[4] = "Drop";
		      itemDef.newModelColor = new int[1];
		      itemDef.editedModelColor = new int[] { 926 };
		      break;
		    case 11551: 
		      itemDef.actions = new String[5];
		      itemDef.actions[1] = "Wear";
		      itemDef.newModelColor = new int[1];
		      itemDef.editedModelColor = new int[1];
		      itemDef.newModelColor[0] = 6020;
		      itemDef.editedModelColor[0] = 933;
		      itemDef.modelID = 2537;
		      itemDef.modelZoom = 540;
		      itemDef.rotationX = 72;
		      itemDef.rotationY = 136;
		      itemDef.modelOffset1 = 0;
		      itemDef.modelOffsetY = 0;
		      itemDef.maleEquip1 = 189;
		      itemDef.femaleEquip1 = 366;
		      itemDef.name = "Black santa hat";
		      itemDef.description = "It's a Black santa hat.";
		      break;
		    case 12284: 
		      itemDef.name = "Toxic staff of the dead";
		      itemDef.modelID = 19224;
		      itemDef.modelZoom = 2150;
		      itemDef.rotationX = 1010;
		      itemDef.rotationY = 512;
		      itemDef.femaleEquip1 = 14402;
		      itemDef.maleEquip1 = 49001;
		      itemDef.actions = new String[5];
		      itemDef.actions[1] = "Wield";
		      itemDef.actions[2] = "Check";
		      itemDef.actions[4] = "Uncharge";
	          itemDef.groundActions = new String[] { null, null, "Take", null, null };
		      itemDef.editedModelColor = new int[1];
		      itemDef.editedModelColor[0] = 17467;
		      itemDef.newModelColor = new int[1];
		      itemDef.newModelColor[0] = 21947;
		      break;
            case 12605:
                itemDef.name = "Treasonous ring";
                itemDef.modelZoom = 750;
                itemDef.rotationY = 342;
                itemDef.rotationX = 252;
                itemDef.modelOffset1 = -3;
                itemDef.modelOffsetY = -12;
                itemDef.groundActions = new String[]{null, null, "Take", null, null};
                itemDef.actions = new String[]{null, "Wear", null, null, "Drop"};
                itemDef.modelID = 43900;
                break;
                
               /** clue scrolls **/ 
            case 2714:
                itemDef.name = "Casket";
                break;
            case 6183:
            	itemDef.name = "@red@Donation Box";
            	break;
            case 2677:
            case 2678:
            case 2679:
            case 2680:
            case 2681:
            case 2682:
            case 2683:
            case 2684:
            case 2685:
                itemDef.name = "Clue Scroll";
                break;
                
            case 13051:
                itemDef.name = "Armadyl crossbow";
                itemDef.modelZoom = 1325;
                itemDef.rotationY = 240;
                itemDef.rotationX = 110;
                itemDef.modelOffset1 = -6;
                itemDef.modelOffsetY = -40;
                itemDef.newModelColor = new int[]{115, 107, 10167, 10171};
                itemDef.editedModelColor = new int[]{5409, 5404, 6449, 7390};
                itemDef.groundActions = new String[]{null, null, "Take", null, null};
                itemDef.actions = new String[]{null, "Wield", null, null, "Drop"};
                itemDef.modelID = 19967;
                itemDef.maleEquip1 = 19839;
                itemDef.femaleEquip1 = 19839;
                break;

            case 4454:
            	itemDef.name = "Kodai wand";
            	itemDef.modelZoom = 1417;
            	itemDef.rotationY = 552;
            	itemDef.rotationX = 1006;
            	itemDef.modelOffsetY = 1;
                    itemDef.actions = new String[] { null, "Wield", null, null, "Drop" };
            	itemDef.modelID = 32789;
            	itemDef.maleEquip1 = 32669;
            	itemDef.femaleEquip1 = 32669;
            	itemDef.editedModelColor = new int[1];
            	itemDef.newModelColor = new int[1];
            	itemDef.editedModelColor[0] = 0;
            	itemDef.newModelColor[0] = 4;
            	break;
            case 4453:
            itemDef.actions = new String[5];
            itemDef.actions[1] = "Wield";
            itemDef.modelID = 32797; // Drop/Inv Model
            itemDef.maleEquip1 = 32668; // Male Wield Model
            itemDef.femaleEquip1 = 32668; // Female Wield
            itemDef.modelZoom = 1230;
            itemDef.rotationY = 236;
            itemDef.rotationX = 236;
            itemDef.modelOffset1 = -5;
            itemDef.modelOffsetY = -36;
            itemDef.stackable = false;
            itemDef.name = "Dragon hunter crossbow"; // Item Name
            itemDef.description = "A crossbow specialising in hunting dragons."; // Item
            break;
            case 4452:
            	itemDef.name = "Twisted buckler";
            	itemDef.modelZoom = 920;
            	itemDef.rotationY = 291;
            	itemDef.rotationX = 2031;
            	itemDef.modelOffset1 = 0;
            	itemDef.modelOffsetY = -3;
            	itemDef.groundActions = new String[5];
            	itemDef.groundActions[2] = "Take";
            	itemDef.actions = new String[5];
            	itemDef.actions[1] = "Wear";
            	itemDef.actions[4] = "Drop";
            	itemDef.modelID = 32793;
            	itemDef.maleEquip1 = 32667;
            	itemDef.femaleEquip1 = 32667;
            break;
            case 4448:
            	itemDef.name = "Dinh's bulwark";
            	itemDef.modelZoom = 1620;
            	itemDef.rotationY = 291;
            	itemDef.rotationX = 1224;
            	itemDef.modelOffset1 = 0;
            	itemDef.modelOffsetY = -3;
            	itemDef.groundActions = new String[5];
            	itemDef.groundActions[2] = "Take";
            	itemDef.actions = new String[5];
            	itemDef.actions[1] = "Wear";
            	itemDef.actions[4] = "Drop";
            	itemDef.modelID = 32801;
            	itemDef.maleEquip1 = 32671;
            	itemDef.femaleEquip1 = 32671;
            break;
            case 4450:
            	itemDef.name = "Elder maul";
            	itemDef.modelZoom = 1800;
            	itemDef.rotationY = 308;
            	itemDef.rotationX = 36;
            	itemDef.modelOffset1 = 7;
            	itemDef.actions = new String[] { null, "Wield", "null", null, "Destroy" };
            	itemDef.modelID = 32792;
            	itemDef.maleEquip1 = 32678;
            	itemDef.femaleEquip1 = 32678;
            	itemDef.editedModelColor = new int[1];
            	itemDef.newModelColor = new int[1];
            	itemDef.editedModelColor[0] = 0;
            	itemDef.newModelColor[0] = 4;
            	break;
            
            case 12927:
            	itemDef.name = "Magma blowpipe";
            	itemDef.modelZoom = 1158;
            	itemDef.rotationY = 768;
            	itemDef.rotationX = 189;
            	itemDef.modelOffset1 = -7;
            	itemDef.modelOffsetY = 4;
            	itemDef.groundActions = new String[] { null, null, "Take", null, null };
            	itemDef.actions = new String[] { null, "Wield", "Check", "Unload", "Uncharge" };
            	itemDef.newModelColor = new int[] { 8134, 5058, 926, 957, 3008, 1321, 86, 41, 49, 7110, 3008, 1317 };
            	itemDef.editedModelColor = new int[] { 48045, 49069, 48055, 49083, 50114, 33668, 29656, 29603, 33674, 33690, 33680, 33692 };
            	itemDef.modelID = 19219;
            	itemDef.maleEquip1 = 14403;
            	itemDef.femaleEquip1 = 14403;
            	break;
            case 12926:
                itemDef.modelID = 25000;
                itemDef.name = "Toxic blowpipe";
                itemDef.description = "It's a Toxic blowpipe";
                itemDef.modelZoom = 1158;
                itemDef.rotationY = 768;
                itemDef.rotationX = 189;
                itemDef.modelOffset1 = -7;
                itemDef.modelOffsetY = 4;
                itemDef.maleEquip1 = 14403;
                itemDef.femaleEquip1 = 14403;
                itemDef.actions = new String[]{null, "Wield", "Check", "Unload", "Drop"};
                itemDef.groundActions = new String[]{null, null, "Take", null, null};
                break;
            case 13058:
                itemDef.name = "Trident of the seas";
                itemDef.description = "A weapon from the deep.";
                itemDef.femaleEquip1 = 1052;
                itemDef.maleEquip1 = 1052;
                itemDef.modelID = 1051;
                itemDef.rotationY = 420;
                itemDef.rotationX = 1130;
                itemDef.modelZoom = 2755;
                itemDef.modelOffsetY = 0;
                itemDef.modelOffset1 = 0;
                itemDef.actions = new String[5];
                itemDef.actions[1] = "Wield";
                itemDef.actions[3] = "Check";
                break;
            case 12601:
                itemDef.name = "Ring of the gods";
                itemDef.modelZoom = 900;
                itemDef.rotationY = 393;
                itemDef.rotationX = 1589;
                itemDef.modelOffset1 = -9;
                itemDef.modelOffsetY = -12;
                itemDef.groundActions = new String[]{null, null, "Take", null, null};
                itemDef.actions = new String[]{null, "Wear", null, null, "Drop"};
                itemDef.modelID = 33009;
                break;
            case 12603:
                itemDef.name = "Tyrannical ring";
                itemDef.modelZoom = 592;
                itemDef.rotationY = 285;
                itemDef.rotationX = 1163;
                itemDef.groundActions = new String[]{null, null, "Take", null, null};
                itemDef.actions = new String[]{null, "Wear", null, null, "Drop"};
                itemDef.modelID = 28823;
                break;
            case 20555:
    			itemDef.name = "Dragon warhammer";
    			itemDef.modelID = 4041;
    			itemDef.rotationY = 1450;
    			itemDef.rotationX = 1900;
    			itemDef.modelZoom = 1605;
    			itemDef.groundActions = new String[] { null, null, "Take", null, null };
    			itemDef.actions = new String[] { null, "Wield", null, null, "Drop" };
    			itemDef.maleEquip1 = 4037;
    			itemDef.femaleEquip1 = 4038;
    			break;
            case 11613:
            	itemDef.actions = new String[5];
    			itemDef.actions[1] = "Wield";
    			itemDef.modelID = 13701;
    			itemDef.modelZoom = 1560;
    			itemDef.rotationY = 344;
    			itemDef.rotationX = 1104;	
    			itemDef.modelOffsetY = -14;
    			itemDef.modelOffsetX = 0;
    			itemDef.maleEquip1 = 13700;
    			itemDef.femaleEquip1 = 13700;
    			itemDef.name = "Dragon Kiteshield";
    			itemDef.description = "A Dragon Kiteshield!";
                break;
            case 4151: 
            	itemDef.actions = new String[5];
            	itemDef.actions[1] = "Wield";
            	itemDef.name =  "Abyssal whip";
            	itemDef.description = "Lowest powered whip";
            	itemDef.modelID = 5412;//Inv & Ground
            	 itemDef.modelZoom = 840;
                 itemDef.rotationY = 280;
                 itemDef.rotationX = 0;
                 itemDef.modelOffsetX = 0;
                 itemDef.modelOffsetY = 56;
            	itemDef.maleEquip1 = 5409;//Male Wield View
            	itemDef.femaleEquip1 = 5409;//Female Wield View
            	break;
           
            case 11995:
                itemDef.name = "Pet Chaos elemental";
                itemDef.groundActions = new String[] { null, null, "Take", null, null };
    			itemDef.actions = new String[] { null, null, "Summon", null, "Drop" };
    			itemDef.modelZoom = 6000;
    			itemDef.rotationX = 0;
    			itemDef.rotationY = 0;
    			itemDef.modelID = 11216;
    			itemDef.modelOffset1 = -3;
    			itemDef.modelOffsetY = 0;
    			break;
            case 11996:
    			itemDef.name = "Pet King black dragon";
    			itemDef.groundActions = new String[] { null, null, "Take", null, null };
    			itemDef.actions = new String[] { null, null, "Summon", null, "Drop" };
    			itemDef.modelZoom = 3800;
    			itemDef.rotationX = 0;
    			itemDef.rotationY = 0;
    			itemDef.modelID = 17414;
    			itemDef.modelOffset1 = 0;
    			itemDef.modelOffsetY = 0;
    			break;
            case 11997:
    			itemDef.name = "Pet General graardor";
    			itemDef.groundActions = new String[] { null, null, "Take", null, null };
    			itemDef.actions = new String[] { null, null, "Summon", null, "Drop" };
    			itemDef.modelZoom = 1250;
    			itemDef.rotationX = 0;
    			itemDef.rotationY = 0;
    			itemDef.modelID = 27789;
    			itemDef.modelOffset1 = -3;
    			itemDef.modelOffsetY = 200;
    			break;
            case 11978:
    			itemDef.name = "Pet TzTok-Jad";
    			itemDef.groundActions = new String[] { null, null, "Take", null, null };
    			itemDef.actions = new String[] { null, null, "Summon", null, "Drop" };
    			itemDef.modelZoom = 10000;
    			itemDef.rotationX = 553;
    			itemDef.rotationY = 0;
    			itemDef.modelID = 34131;
    			itemDef.modelOffset1 = -3;
    			itemDef.modelOffsetY = -30;
    			break;
            case 12001:
    			itemDef.name = "Pet Corporeal beast";
    			itemDef.groundActions = new String[] { null, null, "Take", null, null };
    			itemDef.actions = new String[] { null, null, "Summon", null, "Drop" };
    			itemDef.modelZoom = 10000;
    			itemDef.rotationX = 553;
    			itemDef.rotationY = 0;
    			itemDef.modelID = 40955;
    			itemDef.modelOffset1 = -3;
    			itemDef.modelOffsetY = -30;
    			break;
            case 12002:
    			itemDef.name = "Pet Kree'arra";
    			itemDef.groundActions = new String[] { null, null, "Take", null, null };
    			itemDef.actions = new String[] { null, null, "Summon", null, "Drop" };
    			itemDef.modelZoom = 8000;
    			itemDef.rotationX = 0;
    			itemDef.rotationY = 0;
    			itemDef.modelID = 28003;
    			itemDef.modelOffset1 = -20;
    			itemDef.modelOffsetY = 0;
    			break;
            case 12003:
    			itemDef.name = "Pet K'ril tsutsaroth";
    			itemDef.groundActions = new String[] { null, null, "Take", null, null };
    			itemDef.actions = new String[] { null, null, "Summon", null, "Drop" };
    			itemDef.modelZoom = 8000;
    			itemDef.rotationX = 0;
    			itemDef.rotationY = 0;
    			itemDef.modelID = 27768;
    			itemDef.modelOffset1 = 5;
    			itemDef.modelOffsetY = 0;
    			break;
            case 12004:
    			itemDef.name = "Pet Commander zilyana";
    			itemDef.groundActions = new String[] { null, null, "Take", null, null };
    			itemDef.actions = new String[] { null, null, "Summon", null, "Drop" };
    			itemDef.modelZoom = 2500;
    			itemDef.rotationX = 0;
    			itemDef.rotationY = 0;
    			itemDef.modelID = 28057;
    			itemDef.modelOffset1 = 5;
    			itemDef.modelOffsetY = 0;
    			break;
            case 12005:
    			itemDef.name = "Pet Dagannoth supreme";
    			itemDef.groundActions = new String[] { null, null, "Take", null, null };
    			itemDef.actions = new String[] { null, null, "Summon", null, "Drop" };
    			itemDef.modelZoom = 4560;
    			itemDef.rotationX = 1868;
    			itemDef.rotationY = 2042;
    			itemDef.modelID = 9941;
    			itemDef.modelOffset1 = 0;
    			itemDef.modelOffsetY = 0;
    			break;
            case 12006:
    			itemDef.name = "Pet Dagannoth prime";
    			itemDef.groundActions = new String[] { null, null, "Take", null, null };
    			itemDef.actions = new String[] { null, null, "Summon", null, "Drop" };
    			itemDef.modelZoom = 4560;
    			itemDef.rotationX = 1868;
    			itemDef.rotationY = 2042;
    			itemDef.modelID = 9941;
    			itemDef.modelOffset1 = 0;
    			itemDef.modelOffsetY = 0;
    			itemDef.newModelColor = new int[] { 5931, 1688, 21530, 21534 };
    			itemDef.editedModelColor = new int[] { 11930, 27144, 16536, 16540 };
    			break;
            case 11990:
    			itemDef.name = "Pet Dagannoth rex";
    			itemDef.groundActions = new String[] { null, null, "Take", null, null };
    			itemDef.actions = new String[] { null, null, "Summon", null, "Drop" };
    			itemDef.modelZoom = 4560;
    			itemDef.rotationX = 1868;
    			itemDef.rotationY = 2042;
    			itemDef.modelID = 9941;
    			itemDef.modelOffset1 = 0;
    			itemDef.modelOffsetY = 0;
    			itemDef.newModelColor = new int[] { 7322, 7326, 10403, 2595 };
    			itemDef.editedModelColor = new int[] { 16536, 16540, 27144, 2477 };
    			break;
            case 11991:
    			itemDef.name = "Pet Frost dragon";
    			itemDef.groundActions = new String[] { null, null, "Take", null, null };
    			itemDef.actions = new String[] { null, null, "Summon", null, "Drop" };
    			itemDef.modelZoom = 5060;
    			itemDef.rotationX = 1868;
    			itemDef.rotationY = 2042;
    			itemDef.modelID = 56767;
    			itemDef.modelOffset1 = 0;
    			itemDef.modelOffsetY = 0;
    			break;
            case 11992:
    			itemDef.name = "Pet Tormented demon";
    			itemDef.groundActions = new String[] { null, null, "Take", null, null };
    			itemDef.actions = new String[] { null, null, "Summon", null, "Drop" };
    			itemDef.modelZoom = 5060;
    			itemDef.rotationX = 1868;
    			itemDef.rotationY = 2042;
    			itemDef.modelID = 44733;
    			itemDef.modelOffset1 = 0;
    			itemDef.modelOffsetY = 0;
    			break;
            case 11993:
    			itemDef.name = "Pet Kalphite queen";
    			itemDef.groundActions = new String[] { null, null, "Take", null, null };
    			itemDef.actions = new String[] { null, null, "Summon", null, "Drop" };
    			itemDef.modelZoom = 7060;
    			itemDef.rotationX = 1868;
    			itemDef.rotationY = 2042;
    			itemDef.modelID = 24597;
    			itemDef.modelOffset1 = 0;
    			itemDef.modelOffsetY = 0;
    			break;
            case 11994:
    			itemDef.name = "Pet Slash bash";
    			itemDef.groundActions = new String[] { null, null, "Take", null, null };
    			itemDef.actions = new String[] { null, null, "Summon", null, "Drop" };
    			itemDef.modelZoom = 7060;
    			itemDef.rotationX = 1868;
    			itemDef.rotationY = 2042;
    			itemDef.modelID = 46141;
    			itemDef.modelOffset1 = 0;
    			itemDef.modelOffsetY = 0;
    			break;
            case 11989:
    			itemDef.name = "Pet Phoenix";
    			itemDef.groundActions = new String[] { null, null, "Take", null, null };
    			itemDef.actions = new String[] { null, null, "Summon", null, "Drop" };
    			itemDef.modelZoom = 7060;
    			itemDef.rotationX = 1868;
    			itemDef.rotationY = 2042;
    			itemDef.modelID = 45412;
    			itemDef.modelOffset1 = 0;
    			itemDef.modelOffsetY = 0;
    			break;
            case 11988:
    			itemDef.name = "Pet Bandos avatar";
    			itemDef.groundActions = new String[] { null, null, "Take", null, null };
    			itemDef.actions = new String[] { null, null, "Summon", null, "Drop" };
    			itemDef.modelZoom = 6060;
    			itemDef.rotationX = 1868;
    			itemDef.rotationY = 2042;
    			itemDef.modelID = 46058;
    			itemDef.modelOffset1 = 0;
    			itemDef.modelOffsetY = 0;
    			break;
            case 11987:
    			itemDef.name = "Pet Nex";
    			itemDef.groundActions = new String[] { null, null, "Take", null, null };
    			itemDef.actions = new String[] { null, null, "Summon", null, "Drop" };
    			itemDef.modelZoom = 5000;
    			itemDef.rotationX = 1868;
    			itemDef.rotationY = 2042;
    			itemDef.modelID = 62717;
    			itemDef.modelOffset1 = 0;
    			itemDef.modelOffsetY = 0;
    			break;
            case 11986:
    			itemDef.name = "Pet Jungle strykewyrm";
    			itemDef.groundActions = new String[] { null, null, "Take", null, null };
    			itemDef.actions = new String[] { null, null, "Summon", null, "Drop" };
    			itemDef.modelZoom = 7060;
    			itemDef.rotationX = 1868;
    			itemDef.rotationY = 2042;
    			itemDef.modelID = 51852;
    			itemDef.modelOffset1 = 0;
    			itemDef.modelOffsetY = 0;
    			break;
            case 11985:
    			itemDef.name = "Pet Desert strykewyrm";
    			itemDef.groundActions = new String[] { null, null, "Take", null, null };
    			itemDef.actions = new String[] { null, null, "Summon", null, "Drop" };
    			itemDef.modelZoom = 7060;
    			itemDef.rotationX = 1868;
    			itemDef.rotationY = 2042;
    			itemDef.modelID = 51848;
    			itemDef.modelOffset1 = 0;
    			itemDef.modelOffsetY = 0;
    			break;
            case 11984:
    			itemDef.name = "Pet Ice strykewyrm";
    			itemDef.groundActions = new String[] { null, null, "Take", null, null };
    			itemDef.actions = new String[] { null, null, "Summon", null, "Drop" };
    			itemDef.modelZoom = 7060;
    			itemDef.rotationX = 1868;
    			itemDef.rotationY = 2042;
    			itemDef.modelID = 51847;
    			itemDef.modelOffset1 = 0;
    			itemDef.modelOffsetY = 0;
    			break;
            case 11983:
    			itemDef.name = "Pet Green dragon";
    			itemDef.groundActions = new String[] { null, null, "Take", null, null };
    			itemDef.actions = new String[] { null, null, "Summon", null, "Drop" };
    			itemDef.modelZoom = 5060;
    			itemDef.rotationX = 1868;
    			itemDef.rotationY = 2042;
    			itemDef.modelID = 49142;
    			itemDef.modelOffset1 = 0;
    			itemDef.modelOffsetY = 0;
    			break;
            case 11982:
    			itemDef.name = "Pet Baby blue dragon";
    			itemDef.groundActions = new String[] { null, null, "Take", null, null };
    			itemDef.actions = new String[] { null, null, "Summon", null, "Drop" };
    			itemDef.modelZoom = 3060;
    			itemDef.rotationX = 1868;
    			itemDef.rotationY = 2042;
    			itemDef.modelID = 57937;
    			itemDef.modelOffset1 = 0;
    			itemDef.modelOffsetY = 0;
    			break;
            case 11981:
    			itemDef.name = "Pet Blue dragon";
    			itemDef.groundActions = new String[] { null, null, "Take", null, null };
    			itemDef.actions = new String[] { null, null, "Summon", null, "Drop" };
    			itemDef.modelZoom = 5060;
    			itemDef.rotationX = 1868;
    			itemDef.rotationY = 2042;
    			itemDef.modelID = 49137;
    			itemDef.modelOffset1 = 0;
    			itemDef.modelOffsetY = 0;
    			break;
            case 11979:
    			itemDef.name = "Pet Black dragon";
    			itemDef.groundActions = new String[] { null, null, "Take", null, null };
    			itemDef.actions = new String[] { null, null, "Summon", null, "Drop" };
    			itemDef.modelZoom = 5060;
    			itemDef.rotationX = 1868;
    			itemDef.rotationY = 2042;
    			itemDef.modelID = 14294;
    			itemDef.modelOffset1 = 0;
    			itemDef.modelOffsetY = 0;
    			break;
            case 20079:
    			itemDef.name = "Pet Vet'ion";
    			itemDef.modelID = 28300;
                itemDef.modelZoom = 8060;
     		   	 itemDef.rotationX = 1868;
     			 itemDef.rotationY = 2042;
                itemDef.modelZoom = 8060;
                itemDef.groundActions = new String[]{null, null, "Take", null, null};
                itemDef.actions = new String[]{null, null, "Summon", null, "Drop"};
                 break;
            case 20080:
    			 itemDef.name = "Pet Cerberus";
                 itemDef.modelID = 29270;
                 itemDef.modelZoom = 8060;
      		   	 itemDef.rotationX = 1868;
      			 itemDef.rotationY = 2042;
                 itemDef.modelZoom = 8060;
                 itemDef.groundActions = new String[]{null, null, "Take", null, null};
                 itemDef.actions = new String[]{null, null, "Summon", null, "Drop"};
                 break;
    		case 20081:
    			itemDef.name = "Pet Scorpia";
    			itemDef.groundActions = new String[] { null, null, "Take", null, null };
    			itemDef.actions = new String[] { null, null, "Summon", null, "Drop" };
    			itemDef.modelZoom = 3347;
    			itemDef.rotationX = 189;
    			itemDef.rotationY = 121;
    			itemDef.modelID = 28293;
    			itemDef.modelOffset1 = 12;
    			itemDef.modelOffsetY = -10;
    			break;
    		case 20082:
    			itemDef.name = "Pet Skotizo";
    			itemDef.groundActions = new String[] { null, null, "Take", null, null };
    			itemDef.actions = new String[] { null, null, "Summon", null, "Drop" };
    			itemDef.modelZoom = 13000;
    			itemDef.rotationX = 1868;
    			itemDef.rotationY = 2042;
    			itemDef.modelID = 31653;
    			itemDef.modelOffset1 = 0;
    			itemDef.modelOffsetY = 0;
    			break;
    		case 20083:
    			itemDef.name = "Pet Venenatis";
    			itemDef.groundActions = new String[] { null, null, "Take", null, null };
    			itemDef.actions = new String[] { null, null, "Summon", null, "Drop" };
    			itemDef.modelZoom = 4080;
    			itemDef.rotationX = 67;
    			itemDef.rotationY = 67;
    			itemDef.modelID = 28294;
    			itemDef.modelOffset1 = 9;
    			itemDef.modelOffsetY = -4;
    			break;
    		case 20085:
    			itemDef.name = "Pet Callisto";
    			itemDef.modelID = 28298;
                itemDef.modelZoom = 8060;
     			itemDef.rotationX = 1868;
     			itemDef.rotationY = 2042;
                itemDef.modelZoom = 8060;
                itemDef.groundActions = new String[]{null, null, "Take", null, null};
                itemDef.actions = new String[]{null, null, "Summon", null, "Drop"};
                 break;
    		case 20086:
    			 itemDef.name = "Pet Snakeling";
                 itemDef.modelID = 14408;
                 itemDef.modelZoom = 8060;
     			 itemDef.rotationX = 1868;
     			 itemDef.rotationY = 2042;
                 itemDef.modelZoom = 8060;
                 itemDef.groundActions = new String[]{null, null, "Take", null, null};
                 itemDef.actions = new String[]{null, null, "Summon", null, "Drop"};
                 break;
    		case 14914:
    			 itemDef.name = "Pet Snakeling";
                 itemDef.modelID = 14409;
                 itemDef.modelZoom = 8060;
     			 itemDef.rotationX = 1868;
     			 itemDef.rotationY = 2042;
                 itemDef.modelZoom = 8060;
                 itemDef.groundActions = new String[]{null, null, "Take", null, null};
                 itemDef.actions = new String[]{null, null, "Summon", null, "Drop"};
                 break;
    		case 20103:
   			 itemDef.name = "Pet Kraken";
                itemDef.modelID = 28231;
                itemDef.modelZoom = 8060;
    			 itemDef.rotationX = 1868;
    			 itemDef.rotationY = 2042;
                itemDef.modelZoom = 8060;
                itemDef.groundActions = new String[]{null, null, "Take", null, null};
                itemDef.actions = new String[]{null, null, "Summon", null, "Drop"};
                break;
    		case 14916:
    			 itemDef.name = "Pet Snakeling";
                 itemDef.modelID = 14407;
                 itemDef.modelZoom = 8060;
     			 itemDef.rotationX = 1868;
     			 itemDef.rotationY = 2042;
                 itemDef.modelZoom = 8060;
                 itemDef.groundActions = new String[]{null, null, "Take", null, null};
                 itemDef.actions = new String[]{null, null, "Summon", null, "Drop"};
                 break;
    		case 20087:
    			itemDef.name = "Pet Lizardman Shaman";
    			itemDef.groundActions = new String[] { null, null, "Take", null, null };
    			itemDef.actions = new String[] { null, null, "Summon", null, "Drop" };
    			itemDef.modelZoom = 8060;
    			itemDef.rotationX = 1868;
    			itemDef.rotationY = 2042;
    			itemDef.modelID = 4039;
    			itemDef.modelOffset1 = 0;
    			itemDef.modelOffsetY = 0;
    			break;
    		case 20088:
    			itemDef.name = "Pet WildyWyrm";
    			itemDef.groundActions = new String[] { null, null, "Take", null, null };
    			itemDef.actions = new String[] { null, null, "Summon", null, "Drop" };
    			itemDef.modelZoom = 6060;
    			itemDef.rotationX = 1868;
    			itemDef.rotationY = 2042;
    			itemDef.modelID = 63604;
    			itemDef.modelOffset1 = 0;
    			itemDef.modelOffsetY = 0;
    			break;
    		case 20089:
    			itemDef.name = "Pet smoke devil";
			    itemDef.modelID = 28442;
			    itemDef.modelZoom = 3984;
			    itemDef.rotationY = 9;
			    itemDef.rotationX = 1862;
			    itemDef.modelOffsetY = 20;
			    itemDef.groundActions = new String[] { null, null, "Take", null, null };
			    itemDef.actions = new String[] { null, null, "Summon", null, "Drop" };
			    break;
    		case 20090:
    			itemDef.name = "Pet Abyssal Sire";
    			itemDef.groundActions = new String[] { null, null, "Take", null, null };
    			itemDef.actions = new String[] { null, null, "Summon", null, "Drop" };
    			itemDef.modelZoom = 12060;
    			itemDef.rotationX = 1868;
    			itemDef.rotationY = 2042;
    			itemDef.modelID = 29477;
    			itemDef.modelOffset1 = 0;
    			itemDef.modelOffsetY = 0;
    			break;
    		case 11971:
    			itemDef.name = "Pet Bork";
    			itemDef.groundActions = new String[] { null, null, "Take", null, null };
    			itemDef.actions = new String[] { null, null, "Summon", null, "Drop" };
    			itemDef.modelZoom = 6560;
    			itemDef.rotationX = 1868;
    			itemDef.rotationY = 2042;
    			itemDef.modelID = 40590;
    			itemDef.modelOffset1 = 0;
    			itemDef.modelOffsetY = 0;
    			break;
    		case 11972:
    			itemDef.name = "Pet Barrelchest";
    			itemDef.groundActions = new String[] { null, null, "Take", null, null };
    			itemDef.actions = new String[] { null, null, "Summon", null, "Drop" };
    			itemDef.modelZoom = 5560;
    			itemDef.rotationX = 1868;
    			itemDef.rotationY = 2042;
    			itemDef.modelID = 22790;
    			itemDef.modelOffset1 = 0;
    			itemDef.modelOffsetY = 0;
    			break;
            case 14667:
                itemDef.name = "Zombie fragment";
                itemDef.modelID = ItemDef.forID(14639).modelID;
                break;
            case 15182:
                itemDef.actions[0] = "Bury";
                break;
            
            case 2996:
                itemDef.name = "Agility ticket";
                break;
            case 5510:
            case 5512:
            case 5509:
                itemDef.actions = new String[]{"Fill", null, "Empty", "Check", null, null};
                break;
            case 11998:
                itemDef.name = "Scimitar";
                itemDef.actions = new String[]{null, null, null, null, null, null};
                break;
            case 11999:
                itemDef.name = "Scimitar";
                itemDef.actions = new String[]{null, null, null, null, null, null};
                itemDef.modelZoom = 700;
                itemDef.rotationX = 0;
                itemDef.rotationY = 350;
                itemDef.modelID = 2429;
                itemDef.modelOffsetX = itemDef.modelOffset1 = 0;
                itemDef.stackable = true;
                itemDef.certID = 11998;
                itemDef.certTemplateID = 799;
                break;
            case 10025:
            	itemDef.name = "Charm Box";
            	itemDef.actions = new String[] {"Open", null, null, null, null};
            	break;
            case 1389:
                itemDef.name = "Staff";
                itemDef.actions = new String[]{null, null, null, null, null, null};
                break;
            case 1390:
                itemDef.name = "Staff";
                itemDef.actions = new String[]{null, null, null, null, null, null};
                break;
            case 17401:
                itemDef.name = "Damaged Hammer";
                itemDef.actions = new String[]{null, null, null, null, null, null};
                break;
            case 17402:
                itemDef.name = "Damaged Hammer";
                itemDef.actions = new String[]{null, null, null, null, null, null};
                itemDef.modelZoom = 760;
                itemDef.rotationX = 28;
                itemDef.rotationY = 552;
                itemDef.modelID = 2429;
                itemDef.modelOffsetX = itemDef.modelOffset1 = 0;
                itemDef.stackable = true;
                itemDef.certID = 17401;
                itemDef.certTemplateID = 799;
                break;
            case 15009:
                itemDef.name = "Gold Ring";
                itemDef.actions = new String[]{null, null, null, null, null, null};
                break;
            case 15010:
                itemDef.modelID = 2429;
                itemDef.name = "Gold Ring";
                itemDef.actions = new String[]{null, null, null, null, null, null};
                itemDef.modelZoom = 760;
                itemDef.rotationX = 28;
                itemDef.rotationY = 552;
                itemDef.modelOffsetX = itemDef.modelOffset1 = 0;
                itemDef.stackable = true;
                itemDef.certID = 15009;
                itemDef.certTemplateID = 799;
                break;

            case 11884:
                itemDef.actions = new String[]{"Open", null, null, null, null, null};
                break;
            
            case 15262:
                itemDef.actions = new String[5];
                itemDef.actions[0] = "Open";
                itemDef.actions[2] = "Open-All";
                break;
            case 6570:
                itemDef.actions[2] = "Upgrade";
                break;
            case 4155:
                itemDef.name = "Slayer gem";
                itemDef.actions = new String[]{"Activate", null, "Social-Slayer", null, "Destroy"};
                break;
            case 13663:
                itemDef.name = "Stat reset cert.";
                itemDef.actions = new String[5];
                itemDef.actions[4] = "Drop";
                itemDef.actions[0] = "Open";
                break;
            case 13653:
                itemDef.name = "Energy fragment";
                break;
            case 292:
                itemDef.name = "Ingredients book";
                break;
            case 15707:
                itemDef.actions = new String[5];
                itemDef.actions[1] = "Wear";
                itemDef.actions[0] = "Create Party";
                break;

            case 14044:
                itemDef.name = "Black Party Hat";
                itemDef.modelID = 2635;
                itemDef.description = "A rare black party hat";
                itemDef.editedModelColor = new int[1];
    			itemDef.newModelColor = new int[1];
    			itemDef.editedModelColor[0] = 926;
    			itemDef.newModelColor[0] = 0;
                itemDef.modelZoom = 440;
            	itemDef.rotationX = 1852;
    			itemDef.rotationY = 76;
    			itemDef.modelOffsetX = 1;
    			itemDef.modelOffsetY = 1;
                itemDef.maleEquip1 = 187;
                itemDef.femaleEquip1 = 363;
                itemDef.groundActions = new String[5];
                itemDef.groundActions[2] = "Take";
                itemDef.actions = new String[5];
                itemDef.actions[1] = "Wear";
                itemDef.actions[4] = "Drop";
                break;

            case 14050:
                itemDef.name = "Black Santa Hat";
                itemDef.modelID = 2537;
                itemDef.description = "A rare black santa hat!";
                itemDef.editedModelColor = new int[1];
    			itemDef.newModelColor = new int[1];
    			itemDef.editedModelColor[0] = 933;
    			itemDef.newModelColor[0] = 0;
                itemDef.modelZoom = 540;
            	itemDef.rotationX = 136;
    			itemDef.rotationY = 72;
    			itemDef.modelOffsetX = 0;
    			itemDef.modelOffsetY = -3;
                itemDef.maleEquip1 = 189;
                itemDef.femaleEquip1 = 366;
                itemDef.groundActions = new String[5];
                itemDef.groundActions[2] = "Take";
                itemDef.actions = new String[5];
                itemDef.actions[1] = "Wear";
                itemDef.actions[4] = "Drop";
                break;
            case 14501:
                itemDef.modelID = 44574;
                itemDef.maleEquip1 = 43693;
                itemDef.femaleEquip1 = 43693;
                break;
            case 19111:
                itemDef.name = "TokHaar-Kal";
                itemDef.value = 60000;
                itemDef.maleEquip1 = 62575;
                itemDef.femaleEquip1 = 62582;
                itemDef.groundActions = new String[5];
                itemDef.groundActions[2] = "Take";
                itemDef.modelOffset1 = -4;
                itemDef.modelID = 62592;
                itemDef.stackable = false;
                itemDef.description = "A cape made of ancient, enchanted rocks.";
                itemDef.modelZoom = 1616;
                itemDef.actions = new String[5];
                itemDef.actions[1] = "Wear";
                itemDef.actions[4] = "Drop";
                itemDef.modelOffset1 = 0;
                itemDef.rotationY = 339;
                itemDef.rotationX = 192;
                break;
            case 13262:

            	 ItemDef itemDef2 = ItemDef.forID(12458);
                 itemDef.modelID = itemDef2.modelID;
                 itemDef.modelOffset1 = itemDef2.modelOffset1;
                 itemDef.modelOffsetX = itemDef2.modelOffsetX;
                 itemDef.modelOffsetY = itemDef2.modelOffsetY;
                 itemDef.modelZoom = itemDef2.modelZoom;
                itemDef.name = itemDef2.name;
                itemDef.actions = itemDef2.actions;
                break;
            case 10942:
                itemDef.name = "$5 Scroll";
                itemDef.actions = new String[5];
                itemDef.actions[4] = "Drop";
                itemDef.actions[0] = "Claim";
                itemDef2 = ItemDef.forID(761);
                itemDef.modelID = itemDef2.modelID;
                itemDef.modelOffset1 = itemDef2.modelOffset1;
                itemDef.modelOffsetX = itemDef2.modelOffsetX;
                itemDef.modelOffsetY = itemDef2.modelOffsetY;
                itemDef.modelZoom = itemDef2.modelZoom;
                break;
            case 10934:
                itemDef.name = "$10 Scroll";
                itemDef.actions = new String[5];
                itemDef.actions[4] = "Drop";
                itemDef.actions[0] = "Claim";
                itemDef2 = ItemDef.forID(761);
                itemDef.modelID = itemDef2.modelID;
                itemDef.modelOffset1 = itemDef2.modelOffset1;
                itemDef.modelOffsetX = itemDef2.modelOffsetX;
                itemDef.modelOffsetY = itemDef2.modelOffsetY;
                itemDef.modelZoom = itemDef2.modelZoom;
                break;
            case 10935:
                itemDef.name = "$25 Scroll";
                itemDef.actions = new String[5];
                itemDef.actions[4] = "Drop";
                itemDef.actions[0] = "Claim";
                itemDef2 = ItemDef.forID(761);
                itemDef.modelID = itemDef2.modelID;
                itemDef.modelOffset1 = itemDef2.modelOffset1;
                itemDef.modelOffsetX = itemDef2.modelOffsetX;
                itemDef.modelOffsetY = itemDef2.modelOffsetY;
                itemDef.modelZoom = itemDef2.modelZoom;
                break;
            case 10943:
                itemDef.name = "$50 Scroll";
                itemDef.actions = new String[5];
                itemDef.actions[4] = "Drop";
                itemDef.actions[0] = "Claim";
                itemDef2 = ItemDef.forID(761);
                itemDef.modelID = itemDef2.modelID;
                itemDef.modelOffset1 = itemDef2.modelOffset1;
                itemDef.modelOffsetX = itemDef2.modelOffsetX;
                itemDef.modelOffsetY = itemDef2.modelOffsetY;
                itemDef.modelZoom = itemDef2.modelZoom;
                break;
            case 15084:
                itemDef.actions[0] = "Roll";
                itemDef.name = "Dice (up to 100)";
                itemDef2 = ItemDef.forID(15098);
                itemDef.modelID = itemDef2.modelID;
                itemDef.modelOffset1 = itemDef2.modelOffset1;
                itemDef.modelOffsetX = itemDef2.modelOffsetX;
                itemDef.modelOffsetY = itemDef2.modelOffsetY;
                itemDef.modelZoom = itemDef2.modelZoom;
                break;
            case 995:
                itemDef.name = "Coins";
                itemDef.actions = new String[5];
                itemDef.actions[4] = "Drop";
                itemDef.actions[3] = "Add-to-pouch";
                break;
            case 17291:
                itemDef.name = "Blood necklace";
                itemDef.actions = new String[]{null, "Wear", null, null, null, null};
                break;
            case 20084:
                itemDef.name = "Golden Maul";
                break;
            case 6199:
                itemDef.name = "Mystery Box";
                itemDef.actions = new String[5];
                itemDef.actions[0] = "Open";
                break;
            case 15501:
                itemDef.name = "Legendary Mystery Box";
                itemDef.actions = new String[5];
                itemDef.actions[0] = "Open";
                break;
            case 6568: // To replace Transparent black with opaque black.
                itemDef.editedModelColor = new int[1];
                itemDef.newModelColor = new int[1];
                itemDef.editedModelColor[0] = 0;
                itemDef.newModelColor[0] = 2059;
                break;
            case 996:
            case 997:
            case 998:
            case 999:
            case 1000:
            case 1001:
            case 1002:
            case 1003:
            case 1004:
                itemDef.name = "Coins";
                break;

            case 14017:
                itemDef.name = "Brackish blade";
                itemDef.modelZoom = 1488;
                itemDef.rotationY = 276;
                itemDef.rotationX = 1580;
                itemDef.modelOffsetY = 1;
                itemDef.groundActions = new String[]{null, null, "Take", null, null};
                itemDef.actions = new String[]{null, "Wield", null, null, "Drop"};
                itemDef.modelID = 64593;
                itemDef.maleEquip1 = 64704;
                itemDef.femaleEquip2 = 64704;
                break;

            case 15220:
                itemDef.name = "Berserker ring (i)";
                itemDef.modelZoom = 600;
                itemDef.rotationY = 324;
                itemDef.rotationX = 1916;
                itemDef.modelOffset1 = 3;
                itemDef.modelOffsetY = -15;
                itemDef.groundActions = new String[5];
                itemDef.groundActions[2] = "Take";
                itemDef.actions = new String[5];
                itemDef.actions[1] = "Wear";
                itemDef.actions[4] = "Drop";
                itemDef.modelID = 7735; // if it doesn't work try 7735
                itemDef.maleEquip1 = -1;
                // itemDefinition.maleArm = -1;
                itemDef.femaleEquip1 = -1;
                // itemDefinition.femaleArm = -1;
                break;

            case 14019:
    			itemDef.modelID = 65262;
    			itemDef.name = "Max Cape";
    			itemDef.description = "A cape worn by those who've achieved 99 in all skills.";
    			itemDef.modelZoom = 1385;
    			itemDef.modelOffset1 = 0;
    			itemDef.modelOffsetY = 24;
    			itemDef.rotationY = 279;
    			itemDef.rotationX = 948;
    			itemDef.maleEquip1 = 65300;
    			itemDef.femaleEquip1 = 65322;
    			itemDef.groundActions = new String[5];
    			itemDef.groundActions[2] = "Take";
    			itemDef.actions = new String[5];
    			itemDef.actions[1] = "Wear";
    			itemDef.actions[2] = "Customize";
    			itemDef.editedModelColor = new int[4];
    		    itemDef.newModelColor = new int[4];
    		    itemDef.editedModelColor[0] = 65214; //red
    			itemDef.editedModelColor[1] = 65200; // darker red
    			itemDef.editedModelColor[2] = 65186; //dark red
    			itemDef.editedModelColor[3] = 62995; //darker red
    			itemDef.newModelColor[0] = 65214;//cape
    			itemDef.newModelColor[1] = 65200;//cape
    			itemDef.newModelColor[2] = 65186;//outline
    			itemDef.newModelColor[3] = 62995;//cape
    			break;
            case 14020:
                itemDef.name = "Veteran hood";
                itemDef.description = "A hood worn by Chivalry's veterans.";
                itemDef.modelZoom = 760;
                itemDef.rotationY = 11;
                itemDef.rotationX = 81;
                itemDef.modelOffset1 = 1;
                itemDef.modelOffsetY = -3;
                itemDef.groundActions = new String[]{null, null, "Take", null, null};
                itemDef.actions = new String[]{null, "Wear", null, null, "Drop"};
                itemDef.modelID = 65271;
                itemDef.maleEquip1 = 65289;
                itemDef.femaleEquip1 = 65314;
                break;
            case 14021:
                itemDef.modelID = 65261;
                itemDef.name = "Veteran Cape";
                itemDef.description = "A cape worn by Chivalry's veterans.";
                itemDef.modelZoom = 760;
                itemDef.modelOffset1 = 0;
                itemDef.modelOffsetY = 24;
                itemDef.rotationY = 279;
                itemDef.rotationX = 948;
                itemDef.maleEquip1 = 65305;
                itemDef.femaleEquip1 = 65318;
                itemDef.groundActions = new String[5];
                itemDef.groundActions[2] = "Take";
                itemDef.actions = new String[5];
                itemDef.actions[1] = "Wear";
                break;
            case 14022:
                itemDef.modelID = 65270;
                itemDef.name = "Completionist Cape";
                itemDef.description = "We'd pat you on the back, but this cape would get in the way.";
                itemDef.modelZoom = 1385;
                itemDef.modelOffset1 = 0;
                itemDef.modelOffsetY = 24;
                itemDef.rotationY = 279;
                itemDef.rotationX = 948;
                itemDef.maleEquip1 = 65297;
                itemDef.femaleEquip1 = 65297;
                itemDef.groundActions = new String[5];
                itemDef.groundActions[2] = "Take";
                itemDef.actions = new String[5];
                itemDef.actions[1] = "Wear";
                break;
            case 9666:
            case 11814:
            case 11816:
            case 11818:
            case 11820:
            case 11822:
            case 11824:
            case 11826:
            case 11828:
            case 11830:
            case 11832:
            case 11834:
            case 11836:
            case 11838:
            case 11840:
            case 11842:
            case 11844:
            case 11846:
            case 11848:
            case 11850:
            case 11852:
            case 11854:
            case 11856:
            case 11858:
            case 11860:
            case 11862:
            case 11864:
            case 11866:
            case 11868:
            case 11870:
            case 11874:
            case 11876:
            case 11878:
            case 11882:
            case 11886:
            case 11890:
            case 11894:
            case 11898:
            case 11902:
            case 11904:
            case 11906:
            case 11928:
            case 11930:
            case 11938:
            case 11942:
            case 11944:
            case 11946:
            case 14525:
            case 14527:
            case 14529:
            case 14531:
            case 19588:
            case 19592:
            case 19596:
            case 11908:
            case 11910:
            case 11912:
            case 11914:
            case 11916:
            case 11618:
            case 11920:
            case 11922:
            case 11960:
            case 11962:
            case 11967:
            case 19586:
            case 19584:
            case 19590:
            case 19594:
            case 19598:
                itemDef.actions = new String[5];
                itemDef.actions[0] = "Open";
                break;

            case 14004:
                itemDef.name = "Staff of light";
                itemDef.modelID = 51845;
                itemDef.editedModelColor = new int[11];
                itemDef.newModelColor = new int[11];
                itemDef.editedModelColor[0] = 7860;
                itemDef.newModelColor[0] = 38310;
                itemDef.editedModelColor[1] = 7876;
                itemDef.newModelColor[1] = 38310;
                itemDef.editedModelColor[2] = 7892;
                itemDef.newModelColor[2] = 38310;
                itemDef.editedModelColor[3] = 7884;
                itemDef.newModelColor[3] = 38310;
                itemDef.editedModelColor[4] = 7868;
                itemDef.newModelColor[4] = 38310;
                itemDef.editedModelColor[5] = 7864;
                itemDef.newModelColor[5] = 38310;
                itemDef.editedModelColor[6] = 7880;
                itemDef.newModelColor[6] = 38310;
                itemDef.editedModelColor[7] = 7848;
                itemDef.newModelColor[7] = 38310;
                itemDef.editedModelColor[8] = 7888;
                itemDef.newModelColor[8] = 38310;
                itemDef.editedModelColor[9] = 7872;
                itemDef.newModelColor[9] = 38310;
                itemDef.editedModelColor[10] = 7856;
                itemDef.newModelColor[10] = 38310;
                itemDef.modelZoom = 2256;
                itemDef.rotationX = 456;
                itemDef.rotationY = 513;
                itemDef.modelOffset1 = 0;
                itemDef.modelOffset1 = 0;
                itemDef.maleEquip1 = 51795;
                itemDef.femaleEquip1 = 51795;
                itemDef.groundActions = new String[5];
                itemDef.groundActions[2] = "Take";
                itemDef.actions = new String[5];
                itemDef.actions[1] = "Wear";
                itemDef.actions[4] = "Drop";
                break;

            case 14005:
                itemDef.name = "Staff of light";
                itemDef.modelID = 51845;
                itemDef.editedModelColor = new int[11];
                itemDef.newModelColor = new int[11];
                itemDef.editedModelColor[0] = 7860;
                itemDef.newModelColor[0] = 432;
                itemDef.editedModelColor[1] = 7876;
                itemDef.newModelColor[1] = 432;
                itemDef.editedModelColor[2] = 7892;
                itemDef.newModelColor[2] = 432;
                itemDef.editedModelColor[3] = 7884;
                itemDef.newModelColor[3] = 432;
                itemDef.editedModelColor[4] = 7868;
                itemDef.newModelColor[4] = 432;
                itemDef.editedModelColor[5] = 7864;
                itemDef.newModelColor[5] = 432;
                itemDef.editedModelColor[6] = 7880;
                itemDef.newModelColor[6] = 432;
                itemDef.editedModelColor[7] = 7848;
                itemDef.newModelColor[7] = 432;
                itemDef.editedModelColor[8] = 7888;
                itemDef.newModelColor[8] = 432;
                itemDef.editedModelColor[9] = 7872;
                itemDef.newModelColor[9] = 432;
                itemDef.editedModelColor[10] = 7856;
                itemDef.newModelColor[10] = 432;
                itemDef.modelZoom = 2256;
                itemDef.rotationX = 456;
                itemDef.rotationY = 513;
                itemDef.modelOffset1 = 0;
                itemDef.modelOffset1 = 0;
                itemDef.maleEquip1 = 51795;
                itemDef.femaleEquip1 = 51795;
                itemDef.groundActions = new String[5];
                itemDef.groundActions[2] = "Take";
                itemDef.actions = new String[5];
                itemDef.actions[1] = "Wear";
                itemDef.actions[4] = "Drop";
                break;

            case 14006:
                itemDef.name = "Staff of light";
                itemDef.modelID = 51845;
                itemDef.editedModelColor = new int[11];
                itemDef.newModelColor = new int[11];
                itemDef.editedModelColor[0] = 7860;
                itemDef.newModelColor[0] = 24006;
                itemDef.editedModelColor[1] = 7876;
                itemDef.newModelColor[1] = 24006;
                itemDef.editedModelColor[2] = 7892;
                itemDef.newModelColor[2] = 24006;
                itemDef.editedModelColor[3] = 7884;
                itemDef.newModelColor[3] = 24006;
                itemDef.editedModelColor[4] = 7868;
                itemDef.newModelColor[4] = 24006;
                itemDef.editedModelColor[5] = 7864;
                itemDef.newModelColor[5] = 24006;
                itemDef.editedModelColor[6] = 7880;
                itemDef.newModelColor[6] = 24006;
                itemDef.editedModelColor[7] = 7848;
                itemDef.newModelColor[7] = 24006;
                itemDef.editedModelColor[8] = 7888;
                itemDef.newModelColor[8] = 24006;
                itemDef.editedModelColor[9] = 7872;
                itemDef.newModelColor[9] = 24006;
                itemDef.editedModelColor[10] = 7856;
                itemDef.newModelColor[10] = 24006;
                itemDef.modelZoom = 2256;
                itemDef.rotationX = 456;
                itemDef.rotationY = 513;
                itemDef.modelOffset1 = 0;
                itemDef.modelOffset1 = 0;
                itemDef.maleEquip1 = 51795;
                itemDef.femaleEquip1 = 51795;
                itemDef.groundActions = new String[5];
                itemDef.groundActions[2] = "Take";
                itemDef.actions = new String[5];
                itemDef.actions[1] = "Wear";
                itemDef.actions[4] = "Drop";
                break;
            case 14007:
                itemDef.name = "Staff of light";
                itemDef.modelID = 51845;
                itemDef.editedModelColor = new int[11];
                itemDef.newModelColor = new int[11];
                itemDef.editedModelColor[0] = 7860;
                itemDef.newModelColor[0] = 14285;
                itemDef.editedModelColor[1] = 7876;
                itemDef.newModelColor[1] = 14285;
                itemDef.editedModelColor[2] = 7892;
                itemDef.newModelColor[2] = 14285;
                itemDef.editedModelColor[3] = 7884;
                itemDef.newModelColor[3] = 14285;
                itemDef.editedModelColor[4] = 7868;
                itemDef.newModelColor[4] = 14285;
                itemDef.editedModelColor[5] = 7864;
                itemDef.newModelColor[5] = 14285;
                itemDef.editedModelColor[6] = 7880;
                itemDef.newModelColor[6] = 14285;
                itemDef.editedModelColor[7] = 7848;
                itemDef.newModelColor[7] = 14285;
                itemDef.editedModelColor[8] = 7888;
                itemDef.newModelColor[8] = 14285;
                itemDef.editedModelColor[9] = 7872;
                itemDef.newModelColor[9] = 14285;
                itemDef.editedModelColor[10] = 7856;
                itemDef.newModelColor[10] = 14285;
                itemDef.modelZoom = 2256;
                itemDef.rotationX = 456;
                itemDef.rotationY = 513;
                itemDef.modelOffset1 = 0;
                itemDef.modelOffset1 = 0;
                itemDef.maleEquip1 = 51795;
                itemDef.femaleEquip1 = 51795;
                itemDef.groundActions = new String[5];
                itemDef.groundActions[2] = "Take";
                itemDef.actions = new String[5];
                itemDef.actions[1] = "Wear";
                itemDef.actions[4] = "Drop";
                break;
            case 14003:
                itemDef.name = "Robin hood hat";
                itemDef.modelID = 3021;
                itemDef.editedModelColor = new int[3];
                itemDef.newModelColor = new int[3];
                itemDef.editedModelColor[0] = 15009;
                itemDef.newModelColor[0] = 30847;
                itemDef.editedModelColor[1] = 17294;
                itemDef.newModelColor[1] = 32895;
                itemDef.editedModelColor[2] = 15252;
                itemDef.newModelColor[2] = 30847;
                itemDef.modelZoom = 650;
                itemDef.rotationY = 2044;
                itemDef.rotationX = 256;
                itemDef.modelOffset1 = -3;
                itemDef.modelOffsetY = -5;
                itemDef.maleEquip1 = 3378;
                itemDef.femaleEquip1 = 3382;
                itemDef.maleDialogue = 3378;
                itemDef.femaleDialogue = 3382;
                itemDef.groundActions = new String[5];
                itemDef.groundActions[2] = "Take";
                itemDef.actions = new String[5];
                itemDef.actions[1] = "Wear";
                itemDef.actions[4] = "Drop";
                break;

            case 14001:
                itemDef.name = "Robin hood hat";
                itemDef.modelID = 3021;
                itemDef.editedModelColor = new int[3];
                itemDef.newModelColor = new int[3];
                itemDef.editedModelColor[0] = 15009;
                itemDef.newModelColor[0] = 10015;
                itemDef.editedModelColor[1] = 17294;
                itemDef.newModelColor[1] = 7730;
                itemDef.editedModelColor[2] = 15252;
                itemDef.newModelColor[2] = 7973;
                itemDef.modelZoom = 650;
                itemDef.rotationY = 2044;
                itemDef.rotationX = 256;
                itemDef.modelOffset1 = -3;
                itemDef.modelOffsetY = -5;
                itemDef.maleEquip1 = 3378;
                itemDef.femaleEquip1 = 3382;
                itemDef.maleDialogue = 3378;
                itemDef.femaleDialogue = 3382;
                itemDef.groundActions = new String[5];
                itemDef.groundActions[2] = "Take";
                itemDef.actions = new String[5];
                itemDef.actions[1] = "Wear";
                itemDef.actions[4] = "Drop";
                break;

            case 14002:
                itemDef.name = "Robin hood hat";
                itemDef.modelID = 3021;
                itemDef.editedModelColor = new int[3];
                itemDef.newModelColor = new int[3];
                itemDef.editedModelColor[0] = 15009;
                itemDef.newModelColor[0] = 35489;
                itemDef.editedModelColor[1] = 17294;
                itemDef.newModelColor[1] = 37774;
                itemDef.editedModelColor[2] = 15252;
                itemDef.newModelColor[2] = 35732;
                itemDef.modelZoom = 650;
                itemDef.rotationY = 2044;
                itemDef.rotationX = 256;
                itemDef.modelOffset1 = -3;
                itemDef.modelOffsetY = -5;
                itemDef.maleEquip1 = 3378;
                itemDef.femaleEquip1 = 3382;
                itemDef.maleDialogue = 3378;
                itemDef.femaleDialogue = 3382;
                itemDef.groundActions = new String[5];
                itemDef.groundActions[2] = "Take";
                itemDef.actions = new String[5];
                itemDef.actions[1] = "Wear";
                itemDef.actions[4] = "Drop";
                break;

            case 14000:
                itemDef.name = "Robin hood hat";
                itemDef.modelID = 3021;
                itemDef.editedModelColor = new int[3];
                itemDef.newModelColor = new int[3];
                itemDef.editedModelColor[0] = 15009;
                itemDef.newModelColor[0] = 3745;
                itemDef.editedModelColor[1] = 17294;
                itemDef.newModelColor[1] = 3982;
                itemDef.editedModelColor[2] = 15252;
                itemDef.newModelColor[2] = 3988;
                itemDef.modelZoom = 650;
                itemDef.rotationY = 2044;
                itemDef.rotationX = 256;
                itemDef.modelOffsetX = 1;
                itemDef.modelOffsetY = -5;
                itemDef.groundActions = new String[5];
                itemDef.groundActions[2] = "Take";
                itemDef.actions = new String[5];
                itemDef.actions[1] = "Wear";
                itemDef.actions[4] = "Drop";
                itemDef.maleEquip1 = 3378;
                itemDef.femaleEquip1 = 3382;
                itemDef.maleDialogue = 3378;
                itemDef.femaleDialogue = 3382;
                break;
            	
            case 20000:
                itemDef.actions = new String[5];
                itemDef.actions[1] = "Wear";
                itemDef.actions[4] = "Drop";
                itemDef.modelID = 53835;
                itemDef.name = "Steadfast boots";
                itemDef.modelZoom = 900;
                itemDef.rotationY = 165;
                itemDef.rotationX = 99;
                itemDef.modelOffset1 = 3;
                itemDef.modelOffsetY = -7;
                itemDef.maleEquip1 = 53327;
                itemDef.femaleEquip1 = 53643;
                itemDef.description = "A pair of Steadfast boots.";
                break;

            case 20001:
                itemDef.actions = new String[5];
                itemDef.actions[1] = "Wear";
                itemDef.actions[4] = "Drop";
                itemDef.modelID = 53828;
                itemDef.name = "Glaiven boots";
                itemDef.modelZoom = 900;
                itemDef.rotationY = 165;
                itemDef.rotationX = 99;
                itemDef.modelOffset1 = 3;
                itemDef.modelOffsetY = -7;
                itemDef.femaleEquip1 = 53309;
                itemDef.maleEquip1 = 53309;
                itemDef.description = "A pair of Glaiven boots.";
                break;

            case 20002:
                itemDef.actions = new String[5];
                itemDef.actions[1] = "Wear";
                itemDef.actions[4] = "Drop";
                itemDef.description = "A pair of Ragefire boots.";
                itemDef.modelID = 53897;
                itemDef.name = "Ragefire boots";
                itemDef.modelZoom = 900;
                itemDef.rotationY = 165;
                itemDef.rotationX = 99;
                itemDef.modelOffset1 = 3;
                itemDef.modelOffsetY = -7;
                itemDef.maleEquip1 = 53330;
                itemDef.femaleEquip1 = 53651;
                break;

            case 14018:
                itemDef.modelID = 5324;
                itemDef.name = "Ornate katana";
                itemDef.modelZoom = 2025;
                itemDef.rotationX = 593;
                itemDef.rotationY = 2040;
                itemDef.modelOffset1 = 5;
                itemDef.modelOffsetY = 1;
                itemDef.value = 50000;
                itemDef.membersObject = true;
                itemDef.maleEquip1 = 5324;
                itemDef.femaleEquip1 = 5324;
                itemDef.actions = new String[5];
                itemDef.actions[1] = "Wield";
                itemDef.actions[4] = "Destroy";
                break;
            case 14008:
                itemDef.modelID = 62714;
                itemDef.name = "Torva full helm";
                itemDef.description = "Torva full helm";
                itemDef.modelZoom = 672;
                itemDef.rotationY = 85;
                itemDef.rotationX = 1867;
                itemDef.modelOffset1 = 0;
                itemDef.modelOffsetY = -3;
                itemDef.maleEquip1 = 62738;
                itemDef.femaleEquip1 = 62754;
                itemDef.groundActions = new String[5];
                itemDef.groundActions[2] = "Take";
                itemDef.actions = new String[5];
                itemDef.actions[1] = "Wear";
                itemDef.actions[2] = "Check-charges";
                itemDef.actions[4] = "Drop";
                itemDef.maleDialogue = 62729;
                itemDef.femaleDialogue = 62729;
                break;
                
            case 14009:
                itemDef.modelID = 62699;
                itemDef.name = "Torva platebody";
                itemDef.description = "Torva platebody";
                itemDef.modelZoom = 1506;
                itemDef.rotationY = 473;
                itemDef.rotationX = 2042;
                itemDef.modelOffset1 = 0;
                itemDef.modelOffsetY = 0;
                itemDef.maleEquip1 = 62746;
                itemDef.femaleEquip1 = 62762;
                itemDef.groundActions = new String[5];
                itemDef.groundActions[2] = "Take";
                itemDef.actions = new String[5];
                itemDef.actions[1] = "Wear";
                itemDef.actions[2] = "Check-charges";
                itemDef.actions[4] = "Drop";
                break;

            case 14010:
                itemDef.modelID = 62701;
                itemDef.name = "Torva platelegs";
                itemDef.description = "Torva platelegs";
                itemDef.modelZoom = 1740;
                itemDef.rotationY = 474;
                itemDef.rotationX = 2045;
                itemDef.modelOffset1 = 0;
                itemDef.modelOffsetY = -5;
                itemDef.maleEquip1 = 62743;
                itemDef.femaleEquip1 = 62760;
                itemDef.groundActions = new String[5];
                itemDef.groundActions[2] = "Take";
                itemDef.actions = new String[5];
                itemDef.actions[1] = "Wear";
                itemDef.actions[2] = "Check-charges";
                itemDef.actions[4] = "Drop";
                break;

            case 14011:
                itemDef.modelID = 62693;
                itemDef.name = "Pernix cowl";
                itemDef.description = "Pernix cowl";
                itemDef.modelZoom = 800;
                itemDef.rotationY = 532;
                itemDef.rotationX = 14;
                itemDef.modelOffset1 = -1;
                itemDef.modelOffsetY = 1;
                itemDef.maleEquip1 = 62739;
                itemDef.femaleEquip1 = 62756;
                itemDef.groundActions = new String[5];
                itemDef.groundActions[2] = "Take";
                itemDef.actions = new String[5];
                itemDef.actions[1] = "Wear";
                itemDef.actions[2] = "Check-charges";
                itemDef.actions[4] = "Drop";
                itemDef.maleDialogue = 62731;
                itemDef.femaleDialogue = 62727;
                itemDef.editedModelColor = new int[2];
                itemDef.newModelColor = new int[2];
                itemDef.editedModelColor[0] = 4550;
                itemDef.newModelColor[0] = 0;
                itemDef.editedModelColor[1] = 4540;
                itemDef.newModelColor[1] = 0;
                break;

            case 14012:
                itemDef.modelID = 62709;
                itemDef.name = "Pernix body";
                itemDef.description = "Pernix body";
                itemDef.modelZoom = 1378;
                itemDef.rotationY = 485;
                itemDef.rotationX = 2042;
                itemDef.modelOffset1 = -1;
                itemDef.modelOffsetY = 7;
                itemDef.maleEquip1 = 62744;
                itemDef.femaleEquip1 = 62765;
                itemDef.groundActions = new String[5];
                itemDef.groundActions[2] = "Take";
                itemDef.actions = new String[5];
                itemDef.actions[1] = "Wear";
                itemDef.actions[2] = "Check-charges";
                itemDef.actions[4] = "Drop";
                break;

            case 14013:
                itemDef.modelID = 62695;
                itemDef.name = "Pernix chaps";
                itemDef.description = "Pernix chaps";
                itemDef.modelZoom = 1740;
                itemDef.rotationY = 504;
                itemDef.rotationX = 0;
                itemDef.modelOffset1 = 4;
                itemDef.modelOffsetY = 3;
                itemDef.maleEquip1 = 62741;
                itemDef.femaleEquip1 = 62757;
                itemDef.groundActions = new String[5];
                itemDef.groundActions[2] = "Take";
                itemDef.actions = new String[5];
                itemDef.actions[1] = "Wear";
                itemDef.actions[2] = "Check-charges";
                itemDef.actions[4] = "Drop";
                break;
            case 14014:
                itemDef.modelID = 62710;
                itemDef.name = "Virtus mask";
                itemDef.description = "Virtus mask";
                itemDef.modelZoom = 928;
                itemDef.rotationY = 406;
                itemDef.rotationX = 2041;
                itemDef.modelOffset1 = 1;
                itemDef.modelOffsetY = -5;
                itemDef.maleEquip1 = 62736;
                itemDef.femaleEquip1 = 62755;
                itemDef.groundActions = new String[5];
                itemDef.groundActions[2] = "Take";
                itemDef.actions = new String[5];
                itemDef.actions[1] = "Wear";
                itemDef.actions[2] = "Check-charges";
                itemDef.actions[4] = "Drop";
                itemDef.maleDialogue = 62728;
                itemDef.femaleDialogue = 62728;
                break;

            case 14015:
                itemDef.modelID = 62704;
                itemDef.name = "Virtus robe top";
                itemDef.description = "Virtus robe top";
                itemDef.modelZoom = 1122;
                itemDef.rotationY = 488;
                itemDef.rotationX = 3;
                itemDef.modelOffset1 = 1;
                itemDef.modelOffsetY = 0;
                itemDef.maleEquip1 = 62748;
                itemDef.femaleEquip1 = 62764;
                itemDef.groundActions = new String[5];
                itemDef.groundActions[2] = "Take";
                itemDef.actions = new String[5];
                itemDef.actions[1] = "Wear";
                itemDef.actions[2] = "Check-charges";
                itemDef.actions[4] = "Drop";
                break;

            case 14016:
                itemDef.modelID = 62700;
                itemDef.name = "Virtus robe legs";
                itemDef.description = "Virtus robe legs";
                itemDef.modelZoom = 1740;
                itemDef.rotationY = 498;
                itemDef.rotationX = 2045;
                itemDef.modelOffset1 = -1;
                itemDef.modelOffsetY = 4;
                itemDef.maleEquip1 = 62742;
                itemDef.femaleEquip1 = 62758;
                itemDef.groundActions = new String[5];
                itemDef.groundActions[2] = "Take";
                itemDef.actions = new String[5];
                itemDef.actions[1] = "Wear";
                itemDef.actions[2] = "Check-charges";
                itemDef.actions[4] = "Drop";
                break;
            /*case 14207:
                itemDef.name = "Potion flask";
                itemDef.modelZoom = 804;
                itemDef.rotationY = 131;
                itemDef.rotationX = 198;
                itemDef.modelOffsetX = 1;
                itemDef.modelOffset1 = -1;
                itemDef.groundActions[2] = "Take";
                itemDef.modelID = 61741;
                itemDef.actions = new String[]{"Drink", null, null, null, null, null};
                break;
            case 14200:
                itemDef.name = "Prayer flask (6)";
                itemDef.description = "6 doses of prayer potion.";
                itemDef.modelZoom = 804;
                itemDef.rotationY = 131;
                itemDef.rotationX = 198;
                itemDef.modelOffsetX = 1;
                itemDef.modelOffset1 = -1;
                itemDef.newModelColor = new int[]{28488};
                itemDef.editedModelColor = new int[]{33715};
                itemDef.groundActions[2] = "Take";
                itemDef.actions = new String[]{"Drink", null, null, null, null, null};
                itemDef.modelID = 61732;
                break;
            case 14198:
                itemDef.name = "Prayer flask (5)";
                itemDef.description = "5 doses of prayer potion.";
                itemDef.modelZoom = 804;
                itemDef.rotationY = 131;
                itemDef.rotationX = 198;
                itemDef.modelOffsetX = 1;
                itemDef.modelOffset1 = -1;
                itemDef.newModelColor = new int[]{28488};
                itemDef.editedModelColor = new int[]{33715};
                itemDef.groundActions[2] = "Take";
                itemDef.actions = new String[]{"Drink", null, null, null, null, null};
                itemDef.modelID = 61729;
                break;
            case 14196:
                itemDef.name = "Prayer flask (4)";
                itemDef.description = "4 doses of prayer potion.";
                itemDef.modelZoom = 804;
                itemDef.rotationY = 131;
                itemDef.rotationX = 198;
                itemDef.modelOffsetX = 1;
                itemDef.modelOffset1 = -1;
                itemDef.newModelColor = new int[]{28488};
                itemDef.editedModelColor = new int[]{33715};
                itemDef.groundActions[2] = "Take";
                itemDef.actions = new String[]{"Drink", null, null, null, null, null};
                itemDef.modelID = 61764;
                break;
            case 14194:
                itemDef.name = "Prayer flask (3)";
                itemDef.description = "3 doses of prayer potion.";
                itemDef.modelZoom = 804;
                itemDef.rotationY = 131;
                itemDef.rotationX = 198;
                itemDef.modelOffsetX = 1;
                itemDef.modelOffset1 = -1;
                itemDef.newModelColor = new int[]{28488};
                itemDef.editedModelColor = new int[]{33715};
                itemDef.groundActions[2] = "Take";
                itemDef.actions = new String[]{"Drink", null, null, null, null, null};
                itemDef.modelID = 61727;
                break;
            case 14192:
                itemDef.name = "Prayer flask (2)";
                itemDef.description = "2 doses of prayer potion.";
                itemDef.modelZoom = 804;
                itemDef.rotationY = 131;
                itemDef.rotationX = 198;
                itemDef.modelOffsetX = 1;
                itemDef.modelOffset1 = -1;
                itemDef.newModelColor = new int[]{28488};
                itemDef.editedModelColor = new int[]{33715};
                itemDef.groundActions[2] = "Take";
                itemDef.actions = new String[]{"Drink", null, null, null, null, null};
                itemDef.modelID = 61731;
                break;
            case 14190:
                itemDef.name = "Prayer flask (1)";
                itemDef.description = "1 dose of prayer potion.";
                itemDef.modelZoom = 804;
                itemDef.rotationY = 131;
                itemDef.rotationX = 198;
                itemDef.modelOffsetX = 1;
                itemDef.modelOffset1 = -1;
                itemDef.newModelColor = new int[]{28488};
                itemDef.editedModelColor = new int[]{33715};
                itemDef.groundActions[2] = "Take";
                itemDef.actions = new String[]{"Drink", null, null, null, null, null};
                itemDef.modelID = 61812;
                break;
            case 14188:
                itemDef.name = "Super attack flask (6)";
                itemDef.description = "6 doses of super attack potion.";
                itemDef.modelZoom = 804;
                itemDef.rotationY = 131;
                itemDef.rotationX = 198;
                itemDef.modelOffsetX = 1;
                itemDef.modelOffset1 = -1;
                itemDef.newModelColor = new int[]{43848};
                itemDef.editedModelColor = new int[]{33715};
                itemDef.groundActions[2] = "Take";
                itemDef.actions = new String[]{"Drink", null, null, null, null, null};
                itemDef.modelID = 61732;
                break;
            case 14186:
                itemDef.name = "Super attack flask (5)";
                itemDef.description = "5 doses of super attack potion.";
                itemDef.modelZoom = 804;
                itemDef.rotationY = 131;
                itemDef.rotationX = 198;
                itemDef.modelOffsetX = 1;
                itemDef.modelOffset1 = -1;
                itemDef.newModelColor = new int[]{43848};
                itemDef.editedModelColor = new int[]{33715};
                itemDef.groundActions[2] = "Take";
                itemDef.actions = new String[]{"Drink", null, null, null, null, null};
                itemDef.modelID = 61729;
                break;
            case 14184:
                itemDef.name = "Super attack flask (4)";
                itemDef.description = "4 doses of super attack potion.";
                itemDef.modelZoom = 804;
                itemDef.rotationY = 131;
                itemDef.rotationX = 198;
                itemDef.modelOffsetX = 1;
                itemDef.modelOffset1 = -1;
                itemDef.newModelColor = new int[]{43848};
                itemDef.editedModelColor = new int[]{33715};
                itemDef.groundActions[2] = "Take";
                itemDef.actions = new String[]{"Drink", null, null, null, null, null};
                itemDef.modelID = 61764;
                break;
            case 14182:
                itemDef.name = "Super attack flask (3)";
                itemDef.description = "3 doses of super attack potion.";
                itemDef.modelZoom = 804;
                itemDef.rotationY = 131;
                itemDef.rotationX = 198;
                itemDef.modelOffsetX = 1;
                itemDef.modelOffset1 = -1;
                itemDef.newModelColor = new int[]{43848};
                itemDef.editedModelColor = new int[]{33715};
                itemDef.groundActions[2] = "Take";
                itemDef.actions = new String[]{"Drink", null, null, null, null, null};
                itemDef.modelID = 61727;
                break;
            case 14180:
                itemDef.name = "Super attack flask (2)";
                itemDef.description = "2 doses of super attack potion.";
                itemDef.modelZoom = 804;
                itemDef.rotationY = 131;
                itemDef.rotationX = 198;
                itemDef.modelOffsetX = 1;
                itemDef.modelOffset1 = -1;
                itemDef.newModelColor = new int[]{43848};
                itemDef.editedModelColor = new int[]{33715};
                itemDef.groundActions[2] = "Take";

                itemDef.modelID = 61731;
                break;
            case 14178:
                itemDef.name = "Super attack flask (1)";
                itemDef.description = "1 dose of super attack potion.";
                itemDef.modelZoom = 804;
                itemDef.rotationY = 131;
                itemDef.rotationX = 198;
                itemDef.modelOffsetX = 1;
                itemDef.modelOffset1 = -1;
                itemDef.newModelColor = new int[]{43848};
                itemDef.editedModelColor = new int[]{33715};
                itemDef.groundActions[2] = "Take";
                itemDef.actions = new String[]{"Drink", null, null, null, null, null};
                itemDef.modelID = 61812;
                break;
            case 14176:
                itemDef.name = "Super strength flask (6)";
                itemDef.description = "6 doses of super strength potion.";
                itemDef.modelZoom = 804;
                itemDef.rotationY = 131;
                itemDef.rotationX = 198;
                itemDef.modelOffsetX = 1;
                itemDef.modelOffset1 = -1;
                itemDef.newModelColor = new int[]{119};
                itemDef.editedModelColor = new int[]{33715};
                itemDef.groundActions[2] = "Take";
                itemDef.actions = new String[]{"Drink", null, null, null, null, null};
                itemDef.modelID = 61732;
                break;
            case 14174:
                itemDef.name = "Super strength flask (5)";
                itemDef.description = "5 doses of super strength potion.";
                itemDef.modelZoom = 804;
                itemDef.rotationY = 131;
                itemDef.rotationX = 198;
                itemDef.modelOffsetX = 1;
                itemDef.modelOffset1 = -1;
                itemDef.newModelColor = new int[]{119};
                itemDef.editedModelColor = new int[]{33715};
                itemDef.groundActions[2] = "Take";
                itemDef.actions = new String[]{"Drink", null, null, null, null, null};
                itemDef.modelID = 61729;
                break;
            case 14172:
                itemDef.name = "Super strength flask (4)";
                itemDef.description = "4 doses of super strength potion.";
                itemDef.modelZoom = 804;
                itemDef.rotationY = 131;
                itemDef.rotationX = 198;
                itemDef.modelOffsetX = 1;
                itemDef.modelOffset1 = -1;
                itemDef.newModelColor = new int[]{119};
                itemDef.editedModelColor = new int[]{33715};
                itemDef.groundActions[2] = "Take";
                itemDef.actions = new String[]{"Drink", null, null, null, null, null};
                itemDef.modelID = 61764;
                break;
            case 14170:
                itemDef.name = "Super strength flask (3)";
                itemDef.description = "3 doses of super strength potion.";
                itemDef.modelZoom = 804;
                itemDef.rotationY = 131;
                itemDef.rotationX = 198;
                itemDef.modelOffsetX = 1;
                itemDef.modelOffset1 = -1;
                itemDef.newModelColor = new int[]{119};
                itemDef.editedModelColor = new int[]{33715};
                itemDef.groundActions[2] = "Take";
                itemDef.actions = new String[]{"Drink", null, null, null, null, null};
                itemDef.modelID = 61727;
                break;
            case 14168:
                itemDef.name = "Super strength flask (2)";
                itemDef.description = "2 doses of super strength potion.";
                itemDef.modelZoom = 804;
                itemDef.rotationY = 131;
                itemDef.rotationX = 198;
                itemDef.modelOffsetX = 1;
                itemDef.modelOffset1 = -1;
                itemDef.newModelColor = new int[]{119};
                itemDef.editedModelColor = new int[]{33715};
                itemDef.groundActions[2] = "Take";
                itemDef.actions = new String[]{"Drink", null, null, null, null, null};
                itemDef.modelID = 61731;
                break;
            case 14166:
                itemDef.name = "Super strength flask (1)";
                itemDef.description = "1 dose of super strength potion.";
                itemDef.modelZoom = 804;
                itemDef.rotationY = 131;
                itemDef.rotationX = 198;
                itemDef.modelOffsetX = 1;
                itemDef.modelOffset1 = -1;
                itemDef.newModelColor = new int[]{119};
                itemDef.editedModelColor = new int[]{33715};
                itemDef.groundActions[2] = "Take";
                itemDef.actions = new String[]{"Drink", null, null, null, null, null};
                itemDef.modelID = 61812;
                break;
            case 14164:
                itemDef.name = "Super defence flask (6)";
                itemDef.description = "6 doses of super defence potion.";
                itemDef.modelZoom = 804;
                itemDef.rotationY = 131;
                itemDef.rotationX = 198;
                itemDef.modelOffsetX = 1;
                itemDef.modelOffset1 = -1;
                itemDef.newModelColor = new int[]{8008};
                itemDef.editedModelColor = new int[]{33715};
                itemDef.groundActions[2] = "Take";
                itemDef.actions = new String[]{"Drink", null, null, null, null, null};
                itemDef.modelID = 61732;
                break;
            case 14162:
                itemDef.name = "Super defence flask (5)";
                itemDef.description = "5 doses of super defence potion.";
                itemDef.modelZoom = 804;
                itemDef.rotationY = 131;
                itemDef.rotationX = 198;
                itemDef.modelOffsetX = 1;
                itemDef.modelOffset1 = -1;
                itemDef.newModelColor = new int[]{8008};
                itemDef.editedModelColor = new int[]{33715};
                itemDef.groundActions[2] = "Take";
                itemDef.actions = new String[]{"Drink", null, null, null, null, null};
                itemDef.modelID = 61729;
                break;
            case 14160:
                itemDef.name = "Super defence flask (4)";
                itemDef.description = "4 doses of super defence potion.";
                itemDef.modelZoom = 804;
                itemDef.rotationY = 131;
                itemDef.rotationX = 198;
                itemDef.modelOffsetX = 1;
                itemDef.modelOffset1 = -1;
                itemDef.newModelColor = new int[]{8008};
                itemDef.editedModelColor = new int[]{33715};
                itemDef.groundActions[2] = "Take";
                itemDef.actions = new String[]{"Drink", null, null, null, null, null};
                itemDef.modelID = 61764;
                break;
            case 14158:
                itemDef.name = "Super defence flask (3)";
                itemDef.description = "3 doses of super defence potion.";
                itemDef.modelZoom = 804;
                itemDef.rotationY = 131;
                itemDef.rotationX = 198;
                itemDef.modelOffsetX = 1;
                itemDef.modelOffset1 = -1;
                itemDef.newModelColor = new int[]{8008};
                itemDef.editedModelColor = new int[]{33715};
                itemDef.groundActions[2] = "Take";
                itemDef.actions = new String[]{"Drink", null, null, null, null, null};
                itemDef.modelID = 61727;
                break;
            case 14156:
                itemDef.name = "Super defence flask (2)";
                itemDef.description = "2 doses of super defence potion.";
                itemDef.modelZoom = 804;
                itemDef.rotationY = 131;
                itemDef.rotationX = 198;
                itemDef.modelOffsetX = 1;
                itemDef.modelOffset1 = -1;
                itemDef.newModelColor = new int[]{8008};
                itemDef.editedModelColor = new int[]{33715};
                itemDef.groundActions[2] = "Take";
                itemDef.actions = new String[]{"Drink", null, null, null, null, null};
                itemDef.modelID = 61731;
                break;
            case 14154:
                itemDef.name = "Super defence flask (1)";
                itemDef.description = "1 dose of super defence potion.";
                itemDef.modelZoom = 804;
                itemDef.rotationY = 131;
                itemDef.rotationX = 198;
                itemDef.modelOffsetX = 1;
                itemDef.modelOffset1 = -1;
                itemDef.newModelColor = new int[]{8008};
                itemDef.editedModelColor = new int[]{33715};
                itemDef.groundActions[2] = "Take";
                itemDef.actions = new String[]{"Drink", null, null, null, null, null};
                itemDef.modelID = 61812;
                break;
            case 14152:
                itemDef.name = "Ranging flask (6)";
                itemDef.description = "6 doses of ranging potion.";
                itemDef.modelZoom = 804;
                itemDef.rotationY = 131;
                itemDef.rotationX = 198;
                itemDef.modelOffsetX = 1;
                itemDef.modelOffset1 = -1;
                itemDef.newModelColor = new int[]{36680};
                itemDef.editedModelColor = new int[]{33715};
                itemDef.groundActions[2] = "Take";
                itemDef.actions = new String[]{"Drink", null, null, null, null, null};
                itemDef.modelID = 61732;
                break;
            case 14150:
                itemDef.name = "Ranging flask (5)";
                itemDef.description = "5 doses of ranging potion.";
                itemDef.modelZoom = 804;
                itemDef.rotationY = 131;
                itemDef.rotationX = 198;
                itemDef.modelOffsetX = 1;
                itemDef.modelOffset1 = -1;
                itemDef.newModelColor = new int[]{36680};
                itemDef.editedModelColor = new int[]{33715};
                itemDef.groundActions[2] = "Take";
                itemDef.actions = new String[]{"Drink", null, null, null, null, null};
                itemDef.modelID = 61729;
                break;
            case 14148:
                itemDef.name = "Ranging flask (4)";
                itemDef.description = "4 doses of ranging potion.";
                itemDef.modelZoom = 804;
                itemDef.rotationY = 131;
                itemDef.rotationX = 198;
                itemDef.modelOffsetX = 1;
                itemDef.modelOffset1 = -1;
                itemDef.newModelColor = new int[]{36680};
                itemDef.editedModelColor = new int[]{33715};
                itemDef.groundActions[2] = "Take";

                itemDef.modelID = 61764;
                break;
            case 14146:
                itemDef.name = "Ranging flask (3)";
                itemDef.description = "3 doses of ranging potion.";
                itemDef.modelZoom = 804;
                itemDef.rotationY = 131;
                itemDef.rotationX = 198;
                itemDef.modelOffsetX = 1;
                itemDef.modelOffset1 = -1;
                itemDef.newModelColor = new int[]{36680};
                itemDef.editedModelColor = new int[]{33715};
                itemDef.groundActions[2] = "Take";
                itemDef.actions = new String[]{"Drink", null, null, null, null, null};
                itemDef.modelID = 61727;
                break;
            case 14144:
                itemDef.name = "Ranging flask (2)";
                itemDef.description = "2 doses of ranging potion.";
                itemDef.modelZoom = 804;
                itemDef.rotationY = 131;
                itemDef.rotationX = 198;
                itemDef.modelOffsetX = 1;
                itemDef.modelOffset1 = -1;
                itemDef.newModelColor = new int[]{36680};
                itemDef.editedModelColor = new int[]{33715};
                itemDef.groundActions[2] = "Take";
                itemDef.actions = new String[]{"Drink", null, null, null, null, null};
                itemDef.modelID = 61731;
                break;
            case 14142:
                itemDef.name = "Ranging flask (1)";
                itemDef.description = "1 dose of ranging potion.";
                itemDef.modelZoom = 804;
                itemDef.rotationY = 131;
                itemDef.rotationX = 198;
                itemDef.modelOffsetX = 1;
                itemDef.modelOffset1 = -1;
                itemDef.newModelColor = new int[]{36680};
                itemDef.editedModelColor = new int[]{33715};
                itemDef.groundActions[2] = "Take";
                itemDef.actions = new String[]{"Drink", null, null, null, null, null};
                itemDef.modelID = 61812;
                break;
            case 14140:
                itemDef.name = "Super antipoison flask (6)";
                itemDef.description = "6 doses of super antipoison.";
                itemDef.modelZoom = 804;
                itemDef.rotationY = 131;
                itemDef.rotationX = 198;
                itemDef.modelOffsetX = 1;
                itemDef.modelOffset1 = -1;
                itemDef.newModelColor = new int[]{62404};
                itemDef.editedModelColor = new int[]{33715};
                itemDef.groundActions[2] = "Take";
                itemDef.actions = new String[]{"Drink", null, null, null, null, null};
                itemDef.modelID = 61732;
                break;
            case 14138:
                itemDef.name = "Super antipoison flask (5)";
                itemDef.description = "5 doses of super antipoison.";
                itemDef.modelZoom = 804;
                itemDef.rotationY = 131;
                itemDef.rotationX = 198;
                itemDef.modelOffsetX = 1;
                itemDef.modelOffset1 = -1;
                itemDef.newModelColor = new int[]{62404};
                itemDef.editedModelColor = new int[]{33715};
                itemDef.groundActions[2] = "Take";
                itemDef.actions = new String[]{"Drink", null, null, null, null, null};
                itemDef.modelID = 61729;
                break;
            case 14136:
                itemDef.name = "Super antipoison flask (4)";
                itemDef.description = "4 doses of super antipoison.";
                itemDef.modelZoom = 804;
                itemDef.rotationY = 131;
                itemDef.rotationX = 198;
                itemDef.modelOffsetX = 1;
                itemDef.modelOffset1 = -1;
                itemDef.newModelColor = new int[]{62404};
                itemDef.editedModelColor = new int[]{33715};
                itemDef.groundActions[2] = "Take";
                itemDef.actions = new String[]{"Drink", null, null, null, null, null};
                itemDef.modelID = 61764;
                break;
            case 14134:
                itemDef.name = "Super antipoison flask (3)";
                itemDef.description = "3 doses of super antipoison.";
                itemDef.modelZoom = 804;
                itemDef.rotationY = 131;
                itemDef.rotationX = 198;
                itemDef.modelOffsetX = 1;
                itemDef.modelOffset1 = -1;
                itemDef.newModelColor = new int[]{62404};
                itemDef.editedModelColor = new int[]{33715};
                itemDef.groundActions[2] = "Take";
                itemDef.actions = new String[]{"Drink", null, null, null, null, null};
                itemDef.modelID = 61727;
                break;
            case 14132:
                itemDef.name = "Super antipoison flask (2)";
                itemDef.description = "2 doses of super antipoison.";
                itemDef.modelZoom = 804;
                itemDef.rotationY = 131;
                itemDef.rotationX = 198;
                itemDef.modelOffsetX = 1;
                itemDef.modelOffset1 = -1;
                itemDef.newModelColor = new int[]{62404};
                itemDef.editedModelColor = new int[]{33715};
                itemDef.groundActions[2] = "Take";
                itemDef.actions = new String[]{"Drink", null, null, null, null, null};
                itemDef.modelID = 61731;
                break;
            case 4706:
                itemDef.modelID = 62692;
                itemDef.name = "Zaryte bow";
                itemDef.modelZoom = 1703;
                itemDef.rotationY = 221;
                itemDef.rotationX = 404;
                itemDef.modelOffsetX = 0;
                itemDef.modelOffsetY = -13;
                itemDef.maleEquip1 = 62750;
                itemDef.femaleEquip1 = 62750;
                itemDef.groundActions = new String[5];
                itemDef.groundActions[2] = "Take";
                itemDef.actions = new String[5];
                itemDef.actions[1] = "Wield";  
                itemDef.actions[4] = "Drop";
            break;
            case 4705:
                itemDef.modelID = 6701;
                itemDef.name = "Abyssal vine whip";
                itemDef.description = "A weapon from the Abyss, interlaced with a vicious jade vine.";
                itemDef.modelZoom = 900;
                itemDef.rotationY = 324;
                itemDef.rotationX = 1808;
                itemDef.modelOffsetX = 1;
                itemDef.modelOffsetY = 3;
                itemDef.maleEquip1 = 6700;
                itemDef.femaleEquip1 = 6700;
                itemDef.actions = new String[5];
                itemDef.actions[1] = "Weild";
                break;
            case 14130:
                itemDef.name = "Super antipoison flask (1)";
                itemDef.description = "1 dose of super antipoison.";
                itemDef.modelZoom = 804;
                itemDef.rotationY = 131;
                itemDef.rotationX = 198;
                itemDef.modelOffsetX = 1;
                itemDef.modelOffset1 = -1;
                itemDef.newModelColor = new int[]{62404};
                itemDef.editedModelColor = new int[]{33715};
                itemDef.groundActions[2] = "Take";
                itemDef.actions = new String[]{"Drink", null, null, null, null, null};
                itemDef.modelID = 61812;
                break;
            case 14128:
                itemDef.name = "Saradomin brew flask (6)";
                itemDef.description = "6 doses of saradomin brew.";
                itemDef.modelZoom = 804;
                itemDef.rotationY = 131;
                itemDef.rotationX = 198;
                itemDef.modelOffsetX = 1;
                itemDef.modelOffset1 = -1;
                itemDef.newModelColor = new int[]{10939};
                itemDef.editedModelColor = new int[]{33715};
                itemDef.groundActions[2] = "Take";
                itemDef.actions = new String[]{"Drink", null, null, null, null, null};
                itemDef.modelID = 61732;
                itemDef.anInt196 = 40;
			itemDef.anInt184 = 200;
                break;
            case 14126:
                itemDef.name = "Saradomin brew flask (5)";
                itemDef.description = "5 doses of saradomin brew.";
                itemDef.modelZoom = 804;
                itemDef.rotationY = 131;
                itemDef.rotationX = 198;
                itemDef.modelOffsetX = 1;
                itemDef.modelOffset1 = -1;
                itemDef.newModelColor = new int[]{10939};
                itemDef.editedModelColor = new int[]{33715};
                itemDef.groundActions[2] = "Take";
                itemDef.actions = new String[]{"Drink", null, null, null, null, null};
                itemDef.modelID = 61729;
                break;
            case 14124:
                itemDef.name = "Saradomin brew flask (4)";
                itemDef.description = "4 doses of saradomin brew.";
                itemDef.modelZoom = 804;
                itemDef.rotationY = 131;
                itemDef.rotationX = 198;
                itemDef.modelOffsetX = 1;
                itemDef.modelOffset1 = -1;
                itemDef.newModelColor = new int[]{10939};
                itemDef.editedModelColor = new int[]{33715};
                itemDef.groundActions[2] = "Take";
                itemDef.actions = new String[]{"Drink", null, null, null, null, null};
                itemDef.modelID = 61764;
                break;
            case 14122:
                itemDef.name = "Saradomin brew flask (3)";
                itemDef.description = "3 doses of saradomin brew.";
                itemDef.modelZoom = 804;
                itemDef.rotationY = 131;
                itemDef.rotationX = 198;
                itemDef.modelOffsetX = 1;
                itemDef.modelOffset1 = -1;
                itemDef.newModelColor = new int[]{10939};
                itemDef.editedModelColor = new int[]{33715};
                itemDef.groundActions[2] = "Take";
                itemDef.actions = new String[]{"Drink", null, null, null, null, null};
                itemDef.modelID = 61727;
                break;
            case 14419:
                itemDef.name = "Saradomin brew flask (2)";
                itemDef.description = "2 doses of saradomin brew.";
                itemDef.modelZoom = 804;
                itemDef.rotationY = 131;
                itemDef.rotationX = 198;
                itemDef.modelOffsetX = 1;
                itemDef.modelOffset1 = -1;
                itemDef.newModelColor = new int[]{10939};
                itemDef.editedModelColor = new int[]{33715};
                itemDef.groundActions[2] = "Take";
                itemDef.actions = new String[]{"Drink", null, null, null, null, null};
                itemDef.modelID = 61731;
                break;
            case 14417:
                itemDef.name = "Saradomin brew flask (1)";
                itemDef.description = "1 dose of saradomin brew.";
                itemDef.modelZoom = 804;
                itemDef.rotationY = 131;
                itemDef.rotationX = 198;
                itemDef.modelOffsetX = 1;
                itemDef.modelOffset1 = -1;
                itemDef.newModelColor = new int[]{10939};
                itemDef.editedModelColor = new int[]{33715};
                itemDef.groundActions[2] = "Take";
                itemDef.actions = new String[]{"Drink", null, null, null, null, null};
                itemDef.modelID = 61812;
                break;
            case 14415:
                itemDef.name = "Super restore flask (6)";
                itemDef.description = "6 doses of super restore potion.";
                itemDef.modelZoom = 804;
                itemDef.rotationY = 131;
                itemDef.rotationX = 198;
                itemDef.modelOffsetX = 1;
                itemDef.modelOffset1 = -1;
                itemDef.newModelColor = new int[]{62135};
                itemDef.editedModelColor = new int[]{33715};
                itemDef.groundActions[2] = "Take";
                itemDef.actions = new String[]{"Drink", null, null, null, null, null};
                itemDef.modelID = 61732;
                break;
            case 14413:
                itemDef.name = "Super restore flask (5)";
                itemDef.description = "5 doses of super restore potion.";
                itemDef.modelZoom = 804;
                itemDef.rotationY = 131;
                itemDef.rotationX = 198;
                itemDef.modelOffsetX = 1;
                itemDef.modelOffset1 = -1;
                itemDef.newModelColor = new int[]{62135};
                itemDef.editedModelColor = new int[]{33715};
                itemDef.groundActions[2] = "Take";
                itemDef.actions = new String[]{"Drink", null, null, null, null, null};
                itemDef.modelID = 61729;
                break;
            case 14411:
                itemDef.name = "Super restore flask (4)";
                itemDef.description = "4 doses of super restore potion.";
                itemDef.modelZoom = 804;
                itemDef.rotationY = 131;
                itemDef.rotationX = 198;
                itemDef.modelOffsetX = 1;
                itemDef.modelOffset1 = -1;
                itemDef.newModelColor = new int[]{62135};
                itemDef.editedModelColor = new int[]{33715};
                itemDef.groundActions[2] = "Take";
                itemDef.actions = new String[]{"Drink", null, null, null, null, null};
                itemDef.modelID = 61764;
                break;
            case 14409:
                itemDef.name = "Super restore flask (3)";
                itemDef.description = "3 doses of super restore potion.";
                itemDef.modelZoom = 804;
                itemDef.rotationY = 131;
                itemDef.rotationX = 198;
                itemDef.modelOffsetX = 1;
                itemDef.modelOffset1 = -1;
                itemDef.newModelColor = new int[]{62135};
                itemDef.editedModelColor = new int[]{33715};
                itemDef.groundActions[2] = "Take";
                itemDef.actions = new String[]{"Drink", null, null, null, null, null};
                itemDef.modelID = 61727;
                break;
            case 14407:
                itemDef.name = "Super restore flask (2)";
                itemDef.description = "2 doses of super restore potion.";
                itemDef.modelZoom = 804;
                itemDef.rotationY = 131;
                itemDef.rotationX = 198;
                itemDef.modelOffsetX = 1;
                itemDef.modelOffset1 = -1;
                itemDef.newModelColor = new int[]{62135};
                itemDef.editedModelColor = new int[]{33715};
                itemDef.groundActions[2] = "Take";
                itemDef.actions = new String[]{"Drink", null, null, null, null, null};
                itemDef.modelID = 61731;
                break;
            case 14405:
                itemDef.name = "Super restore flask (1)";
                itemDef.description = "1 dose of super restore potion.";
                itemDef.modelZoom = 804;
                itemDef.rotationY = 131;
                itemDef.rotationX = 198;
                itemDef.modelOffsetX = 1;
                itemDef.modelOffset1 = -1;
                itemDef.newModelColor = new int[]{62135};
                itemDef.editedModelColor = new int[]{33715};
                itemDef.groundActions[2] = "Take";
                itemDef.actions = new String[]{"Drink", null, null, null, null, null};
                itemDef.modelID = 61812;
                break;
            case 14403:
                itemDef.name = "Magic flask (6)";
                itemDef.description = "6 doses of magic potion.";
                itemDef.modelZoom = 804;
                itemDef.rotationY = 131;
                itemDef.rotationX = 198;
                itemDef.modelOffsetX = 1;
                itemDef.modelOffset1 = -1;
                itemDef.newModelColor = new int[]{37440};
                itemDef.editedModelColor = new int[]{33715};
                itemDef.groundActions[2] = "Take";
                itemDef.actions = new String[]{"Drink", null, null, null, null, null};
                itemDef.modelID = 61732;
                break;
            case 14401:
                itemDef.name = "Magic flask (5)";
                itemDef.description = "5 doses of magic potion.";
                itemDef.modelZoom = 804;
                itemDef.rotationY = 131;
                itemDef.rotationX = 198;
                itemDef.modelOffsetX = 1;
                itemDef.modelOffset1 = -1;
                itemDef.newModelColor = new int[]{37440};
                itemDef.editedModelColor = new int[]{33715};
                itemDef.groundActions[2] = "Take";
                itemDef.actions = new String[]{"Drink", null, null, null, null, null};
                itemDef.modelID = 61729;
                break;
            case 14399:
                itemDef.name = "Magic flask (4)";
                itemDef.description = "4 doses of magic potion.";
                itemDef.modelZoom = 804;
                itemDef.rotationY = 131;
                itemDef.rotationX = 198;
                itemDef.modelOffsetX = 1;
                itemDef.modelOffset1 = -1;
                itemDef.newModelColor = new int[]{37440};
                itemDef.editedModelColor = new int[]{33715};
                itemDef.groundActions[2] = "Take";
                itemDef.actions = new String[]{"Drink", null, null, null, null, null};
                itemDef.modelID = 61764;
                break;
            case 14397:
                itemDef.name = "Magic flask (3)";
                itemDef.description = "3 doses of magic potion.";
                itemDef.modelZoom = 804;
                itemDef.rotationY = 131;
                itemDef.rotationX = 198;
                itemDef.modelOffsetX = 1;
                itemDef.modelOffset1 = -1;
                itemDef.newModelColor = new int[]{37440};
                itemDef.editedModelColor = new int[]{33715};
                itemDef.groundActions[2] = "Take";
                itemDef.actions = new String[]{"Drink", null, null, null, null, null};
                itemDef.modelID = 61727;
                break;
            case 14395:
                itemDef.name = "Magic flask (2)";
                itemDef.description = "2 doses of magic potion.";
                itemDef.modelZoom = 804;
                itemDef.rotationY = 131;
                itemDef.rotationX = 198;
                itemDef.modelOffsetX = 1;
                itemDef.modelOffset1 = -1;
                itemDef.newModelColor = new int[]{37440};
                itemDef.editedModelColor = new int[]{33715};
                itemDef.groundActions[2] = "Take";
                itemDef.actions = new String[]{"Drink", null, null, null, null, null};
                itemDef.modelID = 61731;
                break;
            case 14393:
                itemDef.name = "Magic flask (1)";
                itemDef.description = "1 dose of magic potion.";
                itemDef.modelZoom = 804;
                itemDef.rotationY = 131;
                itemDef.rotationX = 198;
                itemDef.modelOffsetX = 1;
                itemDef.modelOffset1 = -1;
                itemDef.newModelColor = new int[]{37440};
                itemDef.editedModelColor = new int[]{33715};
                itemDef.groundActions[2] = "Take";
                itemDef.actions = new String[]{"Drink", null, null, null, null, null};
                itemDef.modelID = 61812;
                break;
            case 14385:
                itemDef.name = "Recover special flask (6)";
                itemDef.description = "6 doses of recover special.";
                itemDef.modelZoom = 804;
                itemDef.rotationY = 131;
                itemDef.rotationX = 198;
                itemDef.modelOffsetX = 1;
                itemDef.modelOffset1 = -1;
                itemDef.newModelColor = new int[]{38222};
                itemDef.editedModelColor = new int[]{33715};
                itemDef.groundActions[2] = "Take";
                itemDef.actions = new String[]{"Drink", null, null, null, null, null};
                itemDef.modelID = 61732;
                break;
            case 14383:
                itemDef.name = "Recover special flask (5)";
                itemDef.description = "5 doses of recover special.";
                itemDef.modelZoom = 804;
                itemDef.rotationY = 131;
                itemDef.rotationX = 198;
                itemDef.modelOffsetX = 1;
                itemDef.modelOffset1 = -1;
                itemDef.newModelColor = new int[]{38222};
                itemDef.editedModelColor = new int[]{33715};
                itemDef.groundActions[2] = "Take";
                itemDef.actions = new String[]{"Drink", null, null, null, null, null};
                itemDef.modelID = 61729;
                break;
            case 14381:
                itemDef.name = "Recover special flask (4)";
                itemDef.description = "4 doses of recover special.";
                itemDef.modelZoom = 804;
                itemDef.rotationY = 131;
                itemDef.rotationX = 198;
                itemDef.modelOffsetX = 1;
                itemDef.modelOffset1 = -1;
                itemDef.newModelColor = new int[]{38222};
                itemDef.editedModelColor = new int[]{33715};
                itemDef.groundActions[2] = "Take";
                itemDef.actions = new String[]{"Drink", null, null, null, null, null};
                itemDef.modelID = 61764;
                break;
            case 14379:
                itemDef.name = "Recover special flask (3)";
                itemDef.description = "3 doses of recover special.";
                itemDef.modelZoom = 804;
                itemDef.rotationY = 131;
                itemDef.rotationX = 198;
                itemDef.modelOffsetX = 1;
                itemDef.modelOffset1 = -1;
                itemDef.newModelColor = new int[]{38222};
                itemDef.editedModelColor = new int[]{33715};
                itemDef.groundActions[2] = "Take";
                itemDef.actions = new String[]{"Drink", null, null, null, null, null};
                itemDef.modelID = 61727;
                break;
            case 14377:
                itemDef.name = "Recover special flask (2)";
                itemDef.description = "2 doses of recover special.";
                itemDef.modelZoom = 804;
                itemDef.rotationY = 131;
                itemDef.rotationX = 198;
                itemDef.modelOffsetX = 1;
                itemDef.modelOffset1 = -1;
                itemDef.newModelColor = new int[]{38222};
                itemDef.editedModelColor = new int[]{33715};
                itemDef.groundActions[2] = "Take";
                itemDef.actions = new String[]{"Drink", null, null, null, null, null};
                itemDef.modelID = 61731;
                break;
            case 14375:
                itemDef.name = "Recover special flask (1)";
                itemDef.description = "1 dose of recover special.";
                itemDef.modelZoom = 804;
                itemDef.rotationY = 131;
                itemDef.rotationX = 198;
                itemDef.modelOffsetX = 1;
                itemDef.modelOffset1 = -1;
                itemDef.newModelColor = new int[]{38222};
                itemDef.editedModelColor = new int[]{33715};
                itemDef.groundActions[2] = "Take";
                itemDef.actions = new String[]{"Drink", null, null, null, null, null};
                itemDef.modelID = 61812;
                break;
            case 14373:
                itemDef.name = "Extreme attack flask (6)";
                itemDef.description = "6 doses of extreme attack potion.";
                itemDef.modelZoom = 804;
                itemDef.rotationY = 131;
                itemDef.rotationX = 198;
                itemDef.modelOffsetX = 1;
                itemDef.modelOffset1 = -1;
                itemDef.newModelColor = new int[]{33112};
                itemDef.editedModelColor = new int[]{33715};
                itemDef.groundActions[2] = "Take";
                itemDef.actions = new String[]{"Drink", null, null, null, null, null};
                itemDef.modelID = 61732;
                break;
            case 14371:
                itemDef.name = "Extreme attack flask (5)";
                itemDef.description = "5 doses of extreme attack potion.";
                itemDef.modelZoom = 804;
                itemDef.rotationY = 131;
                itemDef.rotationX = 198;
                itemDef.modelOffsetX = 1;
                itemDef.modelOffset1 = -1;
                itemDef.newModelColor = new int[]{33112};
                itemDef.editedModelColor = new int[]{33715};
                itemDef.groundActions[2] = "Take";
                itemDef.actions = new String[]{"Drink", null, null, null, null, null};
                itemDef.modelID = 61729;
                break;
            case 14369:
                itemDef.name = "Extreme attack flask (4)";
                itemDef.description = "4 doses of extreme attack potion.";
                itemDef.modelZoom = 804;
                itemDef.rotationY = 131;
                itemDef.rotationX = 198;
                itemDef.modelOffsetX = 1;
                itemDef.modelOffset1 = -1;
                itemDef.newModelColor = new int[]{33112};
                itemDef.editedModelColor = new int[]{33715};
                itemDef.groundActions[2] = "Take";
                itemDef.actions = new String[]{"Drink", null, null, null, null, null};
                itemDef.modelID = 61764;
                break;
            case 14367:
                itemDef.name = "Extreme attack flask (3)";
                itemDef.description = "3 doses of extreme attack potion.";
                itemDef.modelZoom = 804;
                itemDef.rotationY = 131;
                itemDef.rotationX = 198;
                itemDef.modelOffsetX = 1;
                itemDef.modelOffset1 = -1;
                itemDef.newModelColor = new int[]{33112};
                itemDef.editedModelColor = new int[]{33715};
                itemDef.groundActions[2] = "Take";
                itemDef.actions = new String[]{"Drink", null, null, null, null, null};
                itemDef.modelID = 61727;
                break;
            case 14365:
                itemDef.name = "Extreme attack flask (2)";
                itemDef.description = "2 doses of extreme attack potion.";
                itemDef.modelZoom = 804;
                itemDef.rotationY = 131;
                itemDef.rotationX = 198;
                itemDef.modelOffsetX = 1;
                itemDef.modelOffset1 = -1;
                itemDef.newModelColor = new int[]{33112};
                itemDef.editedModelColor = new int[]{33715};
                itemDef.groundActions[2] = "Take";
                itemDef.actions = new String[]{"Drink", null, null, null, null, null};
                itemDef.modelID = 61731;
                break;
            case 14363:
                itemDef.name = "Extreme attack flask (1)";
                itemDef.description = "1 dose of extreme attack potion.";
                itemDef.modelZoom = 804;
                itemDef.rotationY = 131;
                itemDef.rotationX = 198;
                itemDef.modelOffsetX = 1;
                itemDef.modelOffset1 = -1;
                itemDef.newModelColor = new int[]{33112};
                itemDef.editedModelColor = new int[]{33715};
                itemDef.groundActions[2] = "Take";
                itemDef.actions = new String[]{"Drink", null, null, null, null, null};
                itemDef.modelID = 61812;
                break;
            case 14361:
                itemDef.name = "Extreme strength flask (6)";
                itemDef.description = "6 doses of extreme strength potion.";
                itemDef.modelZoom = 804;
                itemDef.rotationY = 131;
                itemDef.rotationX = 198;
                itemDef.modelOffsetX = 1;
                itemDef.modelOffset1 = -1;
                itemDef.newModelColor = new int[]{127};
                itemDef.editedModelColor = new int[]{33715};
                itemDef.groundActions[2] = "Take";
                itemDef.actions = new String[]{"Drink", null, null, null, null, null};
                itemDef.modelID = 61732;
                break;
            case 14359:
                itemDef.name = "Extreme strength flask (5)";
                itemDef.description = "5 doses of extreme strength potion.";
                itemDef.modelZoom = 804;
                itemDef.rotationY = 131;
                itemDef.rotationX = 198;
                itemDef.modelOffsetX = 1;
                itemDef.modelOffset1 = -1;
                itemDef.newModelColor = new int[]{127};
                itemDef.editedModelColor = new int[]{33715};
                itemDef.groundActions[2] = "Take";
                itemDef.actions = new String[]{"Drink", null, null, null, null, null};
                itemDef.modelID = 61729;
                break;
            case 14357:
                itemDef.name = "Extreme strength flask (4)";
                itemDef.description = "4 doses of extreme strength potion.";
                itemDef.modelZoom = 804;
                itemDef.rotationY = 131;
                itemDef.rotationX = 198;
                itemDef.modelOffsetX = 1;
                itemDef.modelOffset1 = -1;
                itemDef.newModelColor = new int[]{127};
                itemDef.editedModelColor = new int[]{33715};
                itemDef.groundActions[2] = "Take";
                itemDef.actions = new String[]{"Drink", null, null, null, null, null};
                itemDef.modelID = 61764;
                break;
            case 14355:
                itemDef.name = "Extreme strength flask (3)";
                itemDef.description = "3 doses of extreme strength potion.";
                itemDef.modelZoom = 804;
                itemDef.rotationY = 131;
                itemDef.rotationX = 198;
                itemDef.modelOffsetX = 1;
                itemDef.modelOffset1 = -1;
                itemDef.newModelColor = new int[]{127};
                itemDef.editedModelColor = new int[]{33715};
                itemDef.groundActions[2] = "Take";
                itemDef.actions = new String[]{"Drink", null, null, null, null, null};
                itemDef.modelID = 61727;
                break;
            case 14353:
                itemDef.name = "Extreme strength flask (2)";
                itemDef.description = "2 doses of extreme strength potion.";
                itemDef.modelZoom = 804;
                itemDef.rotationY = 131;
                itemDef.rotationX = 198;
                itemDef.modelOffsetX = 1;
                itemDef.modelOffset1 = -1;
                itemDef.newModelColor = new int[]{127};
                itemDef.editedModelColor = new int[]{33715};
                itemDef.groundActions[2] = "Take";
                itemDef.actions = new String[]{"Drink", null, null, null, null, null};
                itemDef.modelID = 61731;
                break;
            case 14351:
                itemDef.name = "Extreme strength flask (1)";
                itemDef.description = "1 dose of extreme strength potion.";
                itemDef.modelZoom = 804;
                itemDef.rotationY = 131;
                itemDef.rotationX = 198;
                itemDef.modelOffsetX = 1;
                itemDef.modelOffset1 = -1;
                itemDef.newModelColor = new int[]{127};
                itemDef.editedModelColor = new int[]{33715};
                itemDef.groundActions[2] = "Take";
                itemDef.actions = new String[]{"Drink", null, null, null, null, null};
                itemDef.modelID = 61812;
                break;
            case 14349:
                itemDef.name = "Extreme defence flask (6)";
                itemDef.description = "6 doses of extreme defence potion.";
                itemDef.modelZoom = 804;
                itemDef.rotationY = 131;
                itemDef.rotationX = 198;
                itemDef.modelOffsetX = 1;
                itemDef.modelOffset1 = -1;
                itemDef.newModelColor = new int[]{10198};
                itemDef.editedModelColor = new int[]{33715};
                itemDef.groundActions[2] = "Take";
                itemDef.actions = new String[]{"Drink", null, null, null, null, null};
                itemDef.modelID = 61732;
                break;
            case 14347:
                itemDef.name = "Extreme defence flask (5)";
                itemDef.description = "5 doses of extreme defence potion.";
                itemDef.modelZoom = 804;
                itemDef.rotationY = 131;
                itemDef.rotationX = 198;
                itemDef.modelOffsetX = 1;
                itemDef.modelOffset1 = -1;
                itemDef.newModelColor = new int[]{10198};
                itemDef.editedModelColor = new int[]{33715};
                itemDef.groundActions[2] = "Take";
                itemDef.actions = new String[]{"Drink", null, null, null, null, null};
                itemDef.modelID = 61729;
                break;
            case 14345:
                itemDef.name = "Extreme defence flask (4)";
                itemDef.description = "4 doses of extreme defence potion.";
                itemDef.modelZoom = 804;
                itemDef.rotationY = 131;
                itemDef.rotationX = 198;
                itemDef.modelOffsetX = 1;
                itemDef.modelOffset1 = -1;
                itemDef.newModelColor = new int[]{10198};
                itemDef.editedModelColor = new int[]{33715};
                itemDef.groundActions[2] = "Take";
                itemDef.actions = new String[]{"Drink", null, null, null, null, null};
                itemDef.modelID = 61764;
                break;
            case 14343:
                itemDef.name = "Extreme defence flask (3)";
                itemDef.description = "3 doses of extreme defence potion.";
                itemDef.modelZoom = 804;
                itemDef.rotationY = 131;
                itemDef.rotationX = 198;
                itemDef.modelOffsetX = 1;
                itemDef.modelOffset1 = -1;
                itemDef.newModelColor = new int[]{10198};
                itemDef.editedModelColor = new int[]{33715};
                itemDef.groundActions[2] = "Take";
                itemDef.actions = new String[]{"Drink", null, null, null, null, null};
                itemDef.modelID = 61727;
                break;
            case 14341:
                itemDef.name = "Extreme defence flask (2)";
                itemDef.description = "2 doses of extreme defence potion.";
                itemDef.modelZoom = 804;
                itemDef.rotationY = 131;
                itemDef.rotationX = 198;
                itemDef.modelOffsetX = 1;
                itemDef.modelOffset1 = -1;
                itemDef.newModelColor = new int[]{10198};
                itemDef.editedModelColor = new int[]{33715};
                itemDef.groundActions[2] = "Take";
                itemDef.actions = new String[]{"Drink", null, null, null, null, null};
                itemDef.modelID = 61731;
                break;
            case 14339:
                itemDef.name = "Extreme defence flask (1)";
                itemDef.description = "1 dose of extreme defence potion.";
                itemDef.modelZoom = 804;
                itemDef.rotationY = 131;
                itemDef.rotationX = 198;
                itemDef.modelOffsetX = 1;
                itemDef.modelOffset1 = -1;
                itemDef.newModelColor = new int[]{10198};
                itemDef.editedModelColor = new int[]{33715};
                itemDef.groundActions[2] = "Take";
                itemDef.actions = new String[]{"Drink", null, null, null, null, null};
                itemDef.modelID = 61812;
                break;
            case 14337:
                itemDef.name = "Extreme magic flask (6)";
                itemDef.description = "6 doses of extreme magic potion.";
                itemDef.modelZoom = 804;
                itemDef.rotationY = 131;
                itemDef.rotationX = 198;
                itemDef.modelOffsetX = 1;
                itemDef.modelOffset1 = -1;
                itemDef.newModelColor = new int[]{33490};
                itemDef.editedModelColor = new int[]{33715};
                itemDef.groundActions[2] = "Take";
                itemDef.actions = new String[]{"Drink", null, null, null, null, null};
                itemDef.modelID = 61732;
                break;
            case 14335:
                itemDef.name = "Extreme magic flask (5)";
                itemDef.description = "5 doses of extreme magic potion.";
                itemDef.modelZoom = 804;
                itemDef.rotationY = 131;
                itemDef.rotationX = 198;
                itemDef.modelOffsetX = 1;
                itemDef.modelOffset1 = -1;
                itemDef.newModelColor = new int[]{33490};
                itemDef.editedModelColor = new int[]{33715};
                itemDef.groundActions[2] = "Take";
                itemDef.actions = new String[]{"Drink", null, null, null, null, null};
                itemDef.modelID = 61729;
                break;
            case 14333:
                itemDef.name = "Extreme magic flask (4)";
                itemDef.description = "4 doses of extreme magic potion.";
                itemDef.modelZoom = 804;
                itemDef.rotationY = 131;
                itemDef.rotationX = 198;
                itemDef.modelOffsetX = 1;
                itemDef.modelOffset1 = -1;
                itemDef.newModelColor = new int[]{33490};
                itemDef.editedModelColor = new int[]{33715};
                itemDef.groundActions[2] = "Take";
                itemDef.actions = new String[]{"Drink", null, null, null, null, null};
                itemDef.modelID = 61764;
                break;
            case 14331:
                itemDef.name = "Extreme magic flask (3)";
                itemDef.description = "3 doses of extreme magic potion.";
                itemDef.modelZoom = 804;
                itemDef.rotationY = 131;
                itemDef.rotationX = 198;
                itemDef.modelOffsetX = 1;
                itemDef.modelOffset1 = -1;
                itemDef.newModelColor = new int[]{33490};
                itemDef.editedModelColor = new int[]{33715};
                itemDef.groundActions[2] = "Take";
                itemDef.actions = new String[]{"Drink", null, null, null, null, null};
                itemDef.modelID = 61727;
                break;
            case 14329:
                itemDef.name = "Extreme magic flask (2)";
                itemDef.description = "2 doses of extreme magic potion.";
                itemDef.modelZoom = 804;
                itemDef.rotationY = 131;
                itemDef.rotationX = 198;
                itemDef.modelOffsetX = 1;
                itemDef.modelOffset1 = -1;
                itemDef.newModelColor = new int[]{33490};
                itemDef.editedModelColor = new int[]{33715};
                itemDef.groundActions[2] = "Take";
                itemDef.actions = new String[]{"Drink", null, null, null, null, null};
                itemDef.modelID = 61731;
                break;
            case 14327:
                itemDef.name = "Extreme magic flask (1)";
                itemDef.description = "1 dose of extreme magic potion.";
                itemDef.modelZoom = 804;
                itemDef.rotationY = 131;
                itemDef.rotationX = 198;
                itemDef.modelOffsetX = 1;
                itemDef.modelOffset1 = -1;
                itemDef.newModelColor = new int[]{33490};
                itemDef.editedModelColor = new int[]{33715};
                itemDef.groundActions[2] = "Take";
                itemDef.actions = new String[]{"Drink", null, null, null, null, null};
                itemDef.modelID = 61812;
                break;
            case 14325:
                itemDef.name = "Extreme ranging flask (6)";
                itemDef.description = "6 doses of extreme ranging potion.";
                itemDef.modelZoom = 804;
                itemDef.rotationY = 131;
                itemDef.rotationX = 198;
                itemDef.modelOffsetX = 1;
                itemDef.modelOffset1 = -1;
                itemDef.newModelColor = new int[]{13111};
                itemDef.editedModelColor = new int[]{33715};
                itemDef.groundActions[2] = "Take";
                itemDef.actions = new String[]{"Drink", null, null, null, null, null};
                itemDef.modelID = 61732;
                break;
            case 14323:
                itemDef.name = "Extreme ranging flask (5)";
                itemDef.description = "5 doses of extreme ranging potion.";
                itemDef.modelZoom = 804;
                itemDef.rotationY = 131;
                itemDef.rotationX = 198;
                itemDef.modelOffsetX = 1;
                itemDef.modelOffset1 = -1;
                itemDef.newModelColor = new int[]{13111};
                itemDef.editedModelColor = new int[]{33715};
                itemDef.groundActions[2] = "Take";
                itemDef.actions = new String[]{"Drink", null, null, null, null, null};
                itemDef.modelID = 61729;
                break;
            case 14321:
                itemDef.name = "Extreme ranging flask (4)";
                itemDef.description = "4 doses of extreme ranging potion.";
                itemDef.modelZoom = 804;
                itemDef.rotationY = 131;
                itemDef.rotationX = 198;
                itemDef.modelOffsetX = 1;
                itemDef.modelOffset1 = -1;
                itemDef.newModelColor = new int[]{13111};
                itemDef.editedModelColor = new int[]{33715};
                itemDef.groundActions[2] = "Take";
                itemDef.actions = new String[]{"Drink", null, null, null, null, null};
                itemDef.modelID = 61764;
                break;
            case 14319:
                itemDef.name = "Extreme ranging flask (3)";
                itemDef.description = " 3 doses of extreme ranging potion.";
                itemDef.modelZoom = 804;
                itemDef.rotationY = 131;
                itemDef.rotationX = 198;
                itemDef.modelOffsetX = 1;
                itemDef.modelOffset1 = -1;
                itemDef.newModelColor = new int[]{13111};
                itemDef.editedModelColor = new int[]{33715};
                itemDef.groundActions[2] = "Take";
                itemDef.actions = new String[]{"Drink", null, null, null, null, null};
                itemDef.modelID = 61727;
                break;
            case 14317:
                itemDef.name = "Extreme ranging flask (2)";
                itemDef.description = "2 doses of extreme ranging potion.";
                itemDef.modelZoom = 804;
                itemDef.rotationY = 131;
                itemDef.rotationX = 198;
                itemDef.modelOffsetX = 1;
                itemDef.modelOffset1 = -1;
                itemDef.newModelColor = new int[]{13111};
                itemDef.editedModelColor = new int[]{33715};
                itemDef.groundActions[2] = "Take";
                itemDef.actions = new String[]{"Drink", null, null, null, null, null};
                itemDef.modelID = 61731;
                break;
            case 14315:
                itemDef.name = "Extreme ranging flask (1)";
                itemDef.description = "1 dose of extreme ranging potion.";
                itemDef.modelZoom = 804;
                itemDef.rotationY = 131;
                itemDef.rotationX = 198;
                itemDef.modelOffsetX = 1;
                itemDef.modelOffset1 = -1;
                itemDef.newModelColor = new int[]{13111};
                itemDef.editedModelColor = new int[]{33715};
                itemDef.groundActions[2] = "Take";
                itemDef.actions = new String[]{"Drink", null, null, null, null, null};
                itemDef.modelID = 61812;
                break;
            case 14313:
                itemDef.name = "Super prayer flask (6)";
                itemDef.description = "6 doses of super prayer potion.";
                itemDef.modelZoom = 804;
                itemDef.rotationY = 131;
                itemDef.rotationX = 198;
                itemDef.modelOffsetX = 1;
                itemDef.modelOffset1 = -1;
                itemDef.newModelColor = new int[]{3016};
                itemDef.editedModelColor = new int[]{33715};
                itemDef.groundActions[2] = "Take";
                itemDef.actions = new String[]{"Drink", null, null, null, null, null};
                itemDef.modelID = 61732;
                break;
            case 14311:
                itemDef.name = "Super prayer flask (5)";
                itemDef.description = "5 doses of super prayer potion.";
                itemDef.modelZoom = 804;
                itemDef.rotationY = 131;
                itemDef.rotationX = 198;
                itemDef.modelOffsetX = 1;
                itemDef.modelOffset1 = -1;
                itemDef.newModelColor = new int[]{3016};
                itemDef.editedModelColor = new int[]{33715};
                itemDef.groundActions[2] = "Take";
                itemDef.actions = new String[]{"Drink", null, null, null, null, null};
                itemDef.modelID = 61729;
                break;
            case 14309:
                itemDef.name = "Super prayer flask (4)";
                itemDef.description = "4 doses of super prayer potion.";
                itemDef.modelZoom = 804;
                itemDef.rotationY = 131;
                itemDef.rotationX = 198;
                itemDef.modelOffsetX = 1;
                itemDef.modelOffset1 = -1;
                itemDef.newModelColor = new int[]{3016};
                itemDef.editedModelColor = new int[]{33715};
                itemDef.groundActions[2] = "Take";
                itemDef.actions = new String[]{"Drink", null, null, null, null, null};
                itemDef.modelID = 61764;
                break;
            case 14307:
                itemDef.name = "Super prayer flask (3)";
                itemDef.description = "3 doses of super prayer potion.";
                itemDef.modelZoom = 804;
                itemDef.rotationY = 131;
                itemDef.rotationX = 198;
                itemDef.modelOffsetX = 1;
                itemDef.modelOffset1 = -1;
                itemDef.newModelColor = new int[]{3016};
                itemDef.editedModelColor = new int[]{33715};
                itemDef.groundActions[2] = "Take";
                itemDef.actions = new String[]{"Drink", null, null, null, null, null};
                itemDef.modelID = 61727;
                break;
            case 14305:
                itemDef.name = "Super prayer flask (2)";
                itemDef.description = "2 doses of super prayer potion.";
                itemDef.modelZoom = 804;
                itemDef.rotationY = 131;
                itemDef.rotationX = 198;
                itemDef.modelOffsetX = 1;
                itemDef.modelOffset1 = -1;
                itemDef.newModelColor = new int[]{3016};
                itemDef.editedModelColor = new int[]{33715};
                itemDef.groundActions[2] = "Take";
                itemDef.actions = new String[]{"Drink", null, null, null, null, null};
                itemDef.modelID = 61731;
                break;
            case 14303:
                itemDef.name = "Super prayer flask (1)";
                itemDef.description = "1 dose of super prayer potion.";
                itemDef.modelZoom = 804;
                itemDef.rotationY = 131;
                itemDef.rotationX = 198;
                itemDef.modelOffsetX = 1;
                itemDef.modelOffset1 = -1;
                itemDef.newModelColor = new int[]{3016};
                itemDef.editedModelColor = new int[]{33715};
                itemDef.groundActions[2] = "Take";
                itemDef.actions = new String[]{"Drink", null, null, null, null, null};
                itemDef.modelID = 61812;
                break;
            case 14301:
                itemDef.name = "Overload flask (6)";
                itemDef.description = "6 doses of overload potion.";
                itemDef.modelZoom = 804;
                itemDef.rotationY = 131;
                itemDef.rotationX = 198;
                itemDef.modelOffsetX = 1;
                itemDef.modelOffset1 = -1;
                itemDef.newModelColor = new int[]{0};
                itemDef.editedModelColor = new int[]{33715};
                itemDef.groundActions[2] = "Take";
                itemDef.actions = new String[]{"Drink", null, null, null, null, null};
                itemDef.modelID = 61732;
                break;
            case 14299:
                itemDef.name = "Overload flask (5)";
                itemDef.description = "5 doses of overload potion.";
                itemDef.modelZoom = 804;
                itemDef.rotationY = 131;
                itemDef.rotationX = 198;
                itemDef.modelOffsetX = 1;
                itemDef.modelOffset1 = -1;
                itemDef.newModelColor = new int[]{0};
                itemDef.editedModelColor = new int[]{33715};
                itemDef.groundActions[2] = "Take";
                itemDef.actions = new String[]{"Drink", null, null, null, null, null};
                itemDef.modelID = 61729;
                break;
            case 14297:
                itemDef.name = "Overload flask (4)";
                itemDef.description = "4 doses of overload potion.";
                itemDef.modelZoom = 804;
                itemDef.rotationY = 131;
                itemDef.rotationX = 198;
                itemDef.modelOffsetX = 1;
                itemDef.modelOffset1 = -1;
                itemDef.newModelColor = new int[]{0};
                itemDef.editedModelColor = new int[]{33715};
                itemDef.groundActions[2] = "Take";
                itemDef.actions = new String[]{"Drink", null, null, null, null, null};
                itemDef.modelID = 61764;
                break;
            case 14295:
                itemDef.name = "Overload flask (3)";
                itemDef.description = "3 doses of overload potion.";
                itemDef.modelZoom = 804;
                itemDef.rotationY = 131;
                itemDef.rotationX = 198;
                itemDef.modelOffsetX = 1;
                itemDef.modelOffset1 = -1;
                itemDef.newModelColor = new int[]{0};
                itemDef.editedModelColor = new int[]{33715};
                itemDef.groundActions[2] = "Take";
                itemDef.actions = new String[]{"Drink", null, null, null, null, null};
                itemDef.modelID = 61727;
                break;
            case 14293:
                itemDef.name = "Overload flask (2)";
                itemDef.description = "2 doses of overload potion.";
                itemDef.modelZoom = 804;
                itemDef.rotationY = 131;
                itemDef.rotationX = 198;
                itemDef.modelOffsetX = 1;
                itemDef.modelOffset1 = -1;
                itemDef.newModelColor = new int[]{0};
                itemDef.editedModelColor = new int[]{33715};
                itemDef.groundActions[2] = "Take";
                itemDef.actions = new String[]{"Drink", null, null, null, null, null};
                itemDef.modelID = 61731;
                break;
            case 14291:
                itemDef.name = "Overload flask (1)";
                itemDef.description = "1 dose of overload potion.";
                itemDef.modelZoom = 804;
                itemDef.rotationY = 131;
                itemDef.rotationX = 198;
                itemDef.modelOffsetX = 1;
                itemDef.modelOffset1 = -1;
                itemDef.newModelColor = new int[]{0};
                itemDef.editedModelColor = new int[]{33715};
                itemDef.actions = new String[]{"Drink", null, null, null, null, null};
                itemDef.groundActions[2] = "Take";

                itemDef.modelID = 61812;
                break;
            case 14289:
                itemDef.name = "Prayer renewal flask (6)";
                itemDef.description = "6 doses of prayer renewal.";
                itemDef.modelZoom = 804;
                itemDef.rotationY = 131;
                itemDef.rotationX = 198;
                itemDef.modelOffsetX = 1;
                itemDef.modelOffset1 = -1;
                itemDef.newModelColor = new int[]{926};
                itemDef.editedModelColor = new int[]{33715};
                itemDef.groundActions[2] = "Take";
                itemDef.actions = new String[]{"Drink", null, null, null, null, null};
                itemDef.modelID = 61732;
                break;
            case 14287:
                itemDef.name = "Prayer renewal flask (5)";
                itemDef.description = "5 doses of prayer renewal.";
                itemDef.modelZoom = 804;
                itemDef.rotationY = 131;
                itemDef.rotationX = 198;
                itemDef.modelOffsetX = 1;
                itemDef.modelOffset1 = -1;
                itemDef.newModelColor = new int[]{926};
                itemDef.editedModelColor = new int[]{33715};
                itemDef.groundActions[2] = "Take";
                itemDef.actions = new String[]{"Drink", null, null, null, null, null};
                itemDef.modelID = 61729;
                break;
            case 15123:
                itemDef.name = "Prayer renewal flask (4)";
                itemDef.description = "4 doses of prayer renewal potion.";
                itemDef.modelZoom = 804;
                itemDef.rotationY = 131;
                itemDef.rotationX = 198;
                itemDef.modelOffsetX = 1;
                itemDef.modelOffset1 = -1;
                itemDef.newModelColor = new int[]{926};
                itemDef.editedModelColor = new int[]{33715};
                itemDef.groundActions[2] = "Take";
                itemDef.actions = new String[]{"Drink", null, null, null, null, null};
                itemDef.modelID = 61764;
                break;
            case 15121:
                itemDef.name = "Prayer renewal flask (3)";
                itemDef.description = "3 doses of prayer renewal potion.";
                itemDef.modelZoom = 804;
                itemDef.rotationY = 131;
                itemDef.rotationX = 198;
                itemDef.modelOffsetX = 1;
                itemDef.modelOffset1 = -1;
                itemDef.newModelColor = new int[]{926};
                itemDef.editedModelColor = new int[]{33715};
                itemDef.groundActions[2] = "Take";
                itemDef.actions = new String[]{"Drink", null, null, null, null, null};
                itemDef.modelID = 61727;
                break;
            case 15119:
                itemDef.name = "Prayer renewal flask (2)";
                itemDef.description = "2 doses of prayer renewal potion.";
                itemDef.modelZoom = 804;
                itemDef.rotationY = 131;
                itemDef.rotationX = 198;
                itemDef.modelOffsetX = 1;
                itemDef.modelOffset1 = -1;
                itemDef.newModelColor = new int[]{926};
                itemDef.editedModelColor = new int[]{33715};
                itemDef.groundActions[2] = "Take";
                itemDef.actions = new String[]{"Drink", null, null, null, null, null};
                itemDef.modelID = 61731;
                break;
            case 7340:
                itemDef.name = "Prayer renewal flask (1)";
                itemDef.description = "1 dose of prayer renewal potion";
                itemDef.modelZoom = 804;
                itemDef.rotationY = 131;
                itemDef.rotationX = 198;
                itemDef.modelOffsetX = 1;
                itemDef.modelOffset1 = -1;
                itemDef.newModelColor = new int[]{926};
                itemDef.editedModelColor = new int[]{33715};
                itemDef.groundActions[2] = "Take";
                itemDef.actions = new String[]{"Drink", null, null, null, null, null};
                itemDef.modelID = 61812;
                break;*/
        }

		itemDef = forID2(i, itemDef);
        return itemDef;
    }

    public static ItemDef forID2(final int i, ItemDef itemDef){
    	switch (i){

			case 20200:
				itemDef.modelID = 64000;
				itemDef.name = "Owner cape";
				itemDef.description = "Victor's cape.";
				itemDef.modelZoom = 2513;
				itemDef.modelOffset1 = -4;
				itemDef.modelOffsetY = 12;
				itemDef.rotationY = 461;
				itemDef.rotationX = 1070;
				itemDef.maleEquip1 = 64001;
				itemDef.femaleEquip1 = 64001;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20201:
				itemDef.modelID = 64015;
				itemDef.name = "Altair's sword";
				itemDef.description = "The right-hand sword of the famous assassin Altair.";
				itemDef.modelZoom = 1800;
				itemDef.rotationX = 948;
				itemDef.rotationY = 320;
				itemDef.modelOffset1 = -4;
				itemDef.modelOffsetY = 12;
				itemDef.maleEquip1 = 64016;
				itemDef.femaleEquip1 = 64016;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20202:
				itemDef.modelID = 64015;
				itemDef.name = "Altair's sword left-hand";
				itemDef.description = "The left-hand sword of the famous assassin Altair.";
				itemDef.modelZoom = 1800;
				itemDef.rotationX = 583;
				itemDef.rotationY = 374;
				itemDef.modelOffset1 = -4;
				itemDef.modelOffsetY = 12;
				itemDef.maleEquip1 = 64017;
				itemDef.femaleEquip1 = 64017;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20203:
				itemDef.modelID = 64006;
				itemDef.name = "Altair's hood";
				itemDef.description = "The hood of the famous assassin Altair.";
				itemDef.modelZoom = 861;
				itemDef.rotationX = 43;
				itemDef.rotationY = 9;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = -4;
				itemDef.maleEquip1 = 64007;
				itemDef.femaleEquip1 = 64007;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20204:
				itemDef.modelID = 64008;
				itemDef.name = "Altair's robe top";
				itemDef.description = "The robe top of the famous assassin Altair.";
				itemDef.modelZoom = 1626;
				itemDef.rotationX = 0;
				itemDef.rotationY = 496;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = 0;
				itemDef.maleEquip1 = 64009;
				itemDef.femaleEquip1 = 64009;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20205:
				itemDef.modelID = 64010;
				itemDef.name = "Altair's robe bottom";
				itemDef.description = "The robe bottom of the famous assassin Altair.";
				itemDef.modelZoom = 1626;
				itemDef.rotationX = 1783;
				itemDef.rotationY = 530;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = 0;
				itemDef.maleEquip1 = 64011;
				itemDef.femaleEquip1 = 64011;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20206:
				itemDef.modelID = 64014;
				itemDef.name = "Altair's boots";
				itemDef.description = "The boots of the famous assassin Altair.";
				itemDef.modelZoom = 1070;
				itemDef.rotationX = 1783;
				itemDef.rotationY = 530;
				itemDef.modelOffset1 = 12;
				itemDef.modelOffsetY = 0;
				itemDef.maleEquip1 = 64014;
				itemDef.femaleEquip1 = 64014;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20207:
				itemDef.modelID = 64012;
				itemDef.name = "Altair's gloves";
				itemDef.description = "The gloves of the famous assassin Altair.";
				itemDef.modelZoom = 965;
				itemDef.rotationX = 0;
				itemDef.rotationY = 530;
				itemDef.modelOffset1 = 12;
				itemDef.modelOffsetY = 0;
				itemDef.maleEquip1 = 64013;
				itemDef.femaleEquip1 = 64013;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20208:
				itemDef.modelID = 64018;
				itemDef.name = "Link sword";
				itemDef.description = "It's dangerous to go alone! Take this.";
				itemDef.modelZoom = 1780;
				itemDef.rotationX = 1261;
				itemDef.rotationY = 320;
				itemDef.modelOffset1 = -4;
				itemDef.modelOffsetY = 12;
				itemDef.maleEquip1 = 64019;
				itemDef.femaleEquip1 = 64019;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20209:
				itemDef.modelID = 64020;
				itemDef.name = "Link mask";
				itemDef.description = "Well excuse me, princess...";
				itemDef.modelZoom = 930;
				itemDef.rotationX = 304;
				itemDef.rotationY = 0;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = -4;
				itemDef.maleEquip1 = 64021;
				itemDef.femaleEquip1 = 64021;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20210:
				itemDef.modelID = 64022;
				itemDef.name = "Link body";
				itemDef.description = "Well excuse me, princess...";
				itemDef.modelZoom = 1452;
				itemDef.rotationX = 2043;
				itemDef.rotationY = 530;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = 0;
				itemDef.maleEquip1 = 64023;
				itemDef.femaleEquip1 = 64023;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20211:
				itemDef.modelID = 64024;
				itemDef.name = "Link legs";
				itemDef.description = "Well excuse me, princess...";
				itemDef.modelZoom = 1452;
				itemDef.rotationX = 0;
				itemDef.rotationY = 461;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = 5;
				itemDef.maleEquip1 = 64025;
				itemDef.femaleEquip1 = 64025;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20212:
				itemDef.modelID = 64032;
				itemDef.name = "Slayer sword";
				itemDef.description = "Slayer4life.";
				itemDef.modelZoom = 1974;
				itemDef.rotationX = 983;
				itemDef.rotationY = 320;
				itemDef.modelOffset1 = -4;
				itemDef.modelOffsetY = 12;
				itemDef.maleEquip1 = 64033;
				itemDef.femaleEquip1 = 64033;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20213:
				itemDef.modelID = 64032;
				itemDef.name = "Slayer sword left-hand";
				itemDef.description = "Slayer4life.";
				itemDef.modelZoom = 1974;
				itemDef.rotationX = 565;
				itemDef.rotationY = 320;
				itemDef.modelOffset1 = -4;
				itemDef.modelOffsetY = 12;
				itemDef.maleEquip1 = 64034;
				itemDef.femaleEquip1 = 64034;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20214:
				itemDef.modelID = 64031;
				itemDef.name = "Slayer helm";
				itemDef.description = "Slayer4life.";
				itemDef.modelZoom = 617;
				itemDef.rotationX = 0;
				itemDef.rotationY = 0;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = 0;
				itemDef.maleEquip1 = 64028;
				itemDef.femaleEquip1 = 64028;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20215:
				itemDef.modelID = 64030;
				itemDef.name = "Slayer platebody";
				itemDef.description = "Slayer4life.";
				itemDef.modelZoom = 1313;
				itemDef.rotationX = 0;
				itemDef.rotationY = 583;
				itemDef.modelOffset1 = 2;
				itemDef.modelOffsetY = 2;
				itemDef.maleEquip1 = 64027;
				itemDef.femaleEquip1 = 64027;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20216:
				itemDef.modelID = 64029;
				itemDef.name = "Slayer platelegs";
				itemDef.description = "Slayer4life.";
				itemDef.modelZoom = 1574;
				itemDef.rotationX = 0;
				itemDef.rotationY = 461;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = 0;
				itemDef.maleEquip1 = 64026;
				itemDef.femaleEquip1 = 64026;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20217:
				itemDef.modelID = 64038;
				itemDef.name = "Captain america mask";
				itemDef.description = "I'm just a kid from Brooklyn...";
				itemDef.modelZoom = 687;
				itemDef.rotationX = 0;
				itemDef.rotationY = 513;
				itemDef.modelOffset1 = -10;
				itemDef.modelOffsetY = -20;
				itemDef.maleEquip1 = 64035;
				itemDef.femaleEquip1 = 64035;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20218:
				itemDef.modelID = 64040;
				itemDef.name = "Captain america body";
				itemDef.description = "I'm just a kid from Brooklyn...";
				itemDef.modelZoom = 1557;
				itemDef.rotationX = 0;
				itemDef.rotationY = 530;
				itemDef.modelOffset1 = -12;
				itemDef.modelOffsetY = 0;
				itemDef.maleEquip1 = 64037;
				itemDef.femaleEquip1 = 64037;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20219:
				itemDef.modelID = 64039;
				itemDef.name = "Captain america legs";
				itemDef.description = "I'm just a kid from Brooklyn...";
				itemDef.modelZoom = 1783;
				itemDef.rotationX = 0;
				itemDef.rotationY = 617;
				itemDef.modelOffset1 = -16;
				itemDef.modelOffsetY = 0;
				itemDef.maleEquip1 = 64036;
				itemDef.femaleEquip1 = 64036;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20220:
				itemDef.modelID = 64041;
				itemDef.name = "Spiderman mask";
				itemDef.description = "Your friendly neighborhood Spider-Man.";
				itemDef.modelZoom = 600;
				itemDef.rotationX = 61;
				itemDef.rotationY = 322;
				itemDef.modelOffset1 = -60;
				itemDef.modelOffsetY = 100;
				itemDef.maleEquip1 = 64044;
				itemDef.femaleEquip1 = 64044;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20221:
				itemDef.modelID = 64043;
				itemDef.name = "Spiderman body";
				itemDef.description = "Your friendly neighborhood Spider-Man.";
				itemDef.modelZoom = 1574;
				itemDef.rotationX = 0;
				itemDef.rotationY = 635;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = 40;
				itemDef.maleEquip1 = 64046;
				itemDef.femaleEquip1 = 64046;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20222:
				itemDef.modelID = 64042;
				itemDef.name = "Spiderman legs";
				itemDef.description = "Your friendly neighborhood Spider-Man.";
				itemDef.modelZoom = 1574;
				itemDef.rotationX = 0;
				itemDef.rotationY = 548;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = 12;
				itemDef.maleEquip1 = 64045;
				itemDef.femaleEquip1 = 64045;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20223:
				itemDef.modelID = 64047;
				itemDef.name = "Ironman helm";
				itemDef.description = "Genius, billionaire, playboy, philanthropist.";
				itemDef.modelZoom = 496;
				itemDef.rotationX = 0;
				itemDef.rotationY = 113;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = -4;
				itemDef.maleEquip1 = 64048;
				itemDef.femaleEquip1 = 64048;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20224:
				itemDef.modelID = 64049;
				itemDef.name = "Ironman body";
				itemDef.description = "Genius, billionaire, playboy, philanthropist.";
				itemDef.modelZoom = 1417;
				itemDef.rotationX = 0;
				itemDef.rotationY = 461;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = 0;
				itemDef.maleEquip1 = 64050;
				itemDef.femaleEquip1 = 64050;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20225:
				itemDef.modelID = 64051;
				itemDef.name = "Ironman legs";
				itemDef.description = "Genius, billionaire, playboy, philanthropist.";
				itemDef.modelZoom = 1417;
				itemDef.rotationX = 0;
				itemDef.rotationY = 530;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = 4;
				itemDef.maleEquip1 = 64052;
				itemDef.femaleEquip1 = 64052;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20226:
				itemDef.modelID = 64053;
				itemDef.name = "Joker mask";
				itemDef.description = "Why so serious?!";
				itemDef.modelZoom = 600;
				itemDef.rotationX = 183;
				itemDef.rotationY = 78;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = 0;
				itemDef.maleEquip1 = 64054;
				itemDef.femaleEquip1 = 64054;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20227:
				itemDef.modelID = 64055;
				itemDef.name = "Joker body";
				itemDef.description = "Why so serious?!";
				itemDef.modelZoom = 1435;
				itemDef.rotationX = 0;
				itemDef.rotationY = 496;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = 0;
				itemDef.maleEquip1 = 64056;
				itemDef.femaleEquip1 = 64056;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20228:
				itemDef.modelID = 64057;
				itemDef.name = "Joker legs";
				itemDef.description = "Why so serious?!";
				itemDef.modelZoom = 2200;
				itemDef.rotationX = 0;
				itemDef.rotationY = 496;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = 0;
				itemDef.maleEquip1 = 64058;
				itemDef.femaleEquip1 = 64058;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20229:
				itemDef.modelID = 64061;
				itemDef.name = "Joker boots";
				itemDef.description = "Why so serious?!";
				itemDef.modelZoom = 965;
				itemDef.rotationX = 235;
				itemDef.rotationY = 496;
				itemDef.modelOffset1 = 10;
				itemDef.modelOffsetY = 0;
				itemDef.maleEquip1 = 64061;
				itemDef.femaleEquip1 = 64061;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20230:
				itemDef.modelID = 64059;
				itemDef.name = "Joker gloves";
				itemDef.description = "Why so serious?!";
				itemDef.modelZoom = 983;
				itemDef.rotationX = 0;
				itemDef.rotationY = 304;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = 0;
				itemDef.maleEquip1 = 64060;
				itemDef.femaleEquip1 = 64060;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20231:
				itemDef.modelID = 64062;
				itemDef.name = "Rocket launcher";
				itemDef.description = "BOOM!";
				itemDef.modelZoom = 1191;
				itemDef.rotationX = 0;
				itemDef.rotationY = 34;
				itemDef.modelOffset1 = 5;
				itemDef.modelOffsetY = 0;
				itemDef.maleEquip1 = 64063;
				itemDef.femaleEquip1 = 64063;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20232:
				itemDef.modelID = 64072;
				itemDef.name = "Red lightsaber";
				itemDef.description = "A blade made of pure energy.";
				itemDef.modelZoom = 2400;
				itemDef.rotationX = 217;
				itemDef.rotationY = 453;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = 0;
				itemDef.maleEquip1 = 64064;
				itemDef.femaleEquip1 = 64064;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20233:
				itemDef.modelID = 64073;
				itemDef.name = "Green lightsaber";
				itemDef.description = "A blade made of pure energy.";
				itemDef.modelZoom = 2400;
				itemDef.rotationX = 217;
				itemDef.rotationY = 453;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = 0;
				itemDef.maleEquip1 = 64065;
				itemDef.femaleEquip1 = 64065;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20234:
				itemDef.modelID = 64074;
				itemDef.name = "Blue lightsaber";
				itemDef.description = "A blade made of pure energy.";
				itemDef.modelZoom = 2400;
				itemDef.rotationX = 217;
				itemDef.rotationY = 453;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = 0;
				itemDef.maleEquip1 = 64066;
				itemDef.femaleEquip1 = 64066;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20235:
				itemDef.modelID = 64075;
				itemDef.name = "Yellow lightsaber";
				itemDef.description = "A blade made of pure energy.";
				itemDef.modelZoom = 2400;
				itemDef.rotationX = 217;
				itemDef.rotationY = 453;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = 0;
				itemDef.maleEquip1 = 64067;
				itemDef.femaleEquip1 = 64067;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20236:
				itemDef.modelID = 64076;
				itemDef.name = "Cyan lightsaber";
				itemDef.description = "A blade made of pure energy.";
				itemDef.modelZoom = 2400;
				itemDef.rotationX = 217;
				itemDef.rotationY = 453;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = 0;
				itemDef.maleEquip1 = 64068;
				itemDef.femaleEquip1 = 64068;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20237:
				itemDef.modelID = 64077;
				itemDef.name = "Pink lightsaber";
				itemDef.description = "A blade made of pure energy.";
				itemDef.modelZoom = 2400;
				itemDef.rotationX = 217;
				itemDef.rotationY = 453;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = 0;
				itemDef.maleEquip1 = 64069;
				itemDef.femaleEquip1 = 64069;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20238:
				itemDef.modelID = 64078;
				itemDef.name = "White lightsaber";
				itemDef.description = "A blade made of pure energy.";
				itemDef.modelZoom = 2400;
				itemDef.rotationX = 217;
				itemDef.rotationY = 453;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = 0;
				itemDef.maleEquip1 = 64070;
				itemDef.femaleEquip1 = 64070;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20239:
				itemDef.modelID = 64079;
				itemDef.name = "Black lightsaber";
				itemDef.description = "A blade made of pure energy.";
				itemDef.modelZoom = 2400;
				itemDef.rotationX = 217;
				itemDef.rotationY = 453;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = 0;
				itemDef.maleEquip1 = 64071;
				itemDef.femaleEquip1 = 64071;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20240:
				itemDef.modelID = 64084;
				itemDef.name = "Darth vader helm";
				itemDef.description = "You don't know the power of the dark side!";
				itemDef.modelZoom = 948;
				itemDef.rotationX = 0;
				itemDef.rotationY = 0;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = 0;
				itemDef.maleEquip1 = 64083;
				itemDef.femaleEquip1 = 64083;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20241:
				itemDef.modelID = 64081;
				itemDef.name = "Darth vader body";
				itemDef.description = "You don't know the power of the dark side!";
				itemDef.modelZoom = 1452;
				itemDef.rotationX = 0;
				itemDef.rotationY = 513;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = 0;
				itemDef.maleEquip1 = 64080;
				itemDef.femaleEquip1 = 64080;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20242:
				itemDef.modelID = 64090;
				itemDef.name = "Darth vader legs";
				itemDef.description = "You don't know the power of the dark side!";
				itemDef.modelZoom = 1922;
				itemDef.rotationX = 0;
				itemDef.rotationY = 600;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = 0;
				itemDef.maleEquip1 = 64082;
				itemDef.femaleEquip1 = 64082;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20243:
				itemDef.modelID = 64089;
				itemDef.name = "Darth vader boots";
				itemDef.description = "You don't know the power of the dark side!";
				itemDef.modelZoom = 1122;
				itemDef.rotationX = 374;
				itemDef.rotationY = 374;
				itemDef.modelOffset1 = 8;
				itemDef.modelOffsetY = 0;
				itemDef.maleEquip1 = 64089;
				itemDef.femaleEquip1 = 64089;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20244:
				itemDef.modelID = 64086;
				itemDef.name = "Darth vader gloves";
				itemDef.description = "You don't know the power of the dark side!";
				itemDef.modelZoom = 739;
				itemDef.rotationX = 61;
				itemDef.rotationY = 339;
				itemDef.modelOffset1 = 8;
				itemDef.modelOffsetY = 0;
				itemDef.maleEquip1 = 64085;
				itemDef.femaleEquip1 = 64085;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20245:
				itemDef.modelID = 64088;
				itemDef.name = "Darth vader cape";
				itemDef.description = "You don't know the power of the dark side!";
				itemDef.modelZoom = 2583;
				itemDef.rotationX = 1365;
				itemDef.rotationY = 339;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = 0;
				itemDef.maleEquip1 = 64087;
				itemDef.femaleEquip1 = 64087;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20246:
				itemDef.modelID = 64092;
				itemDef.name = "Keyblade [I]";
				itemDef.description = "A key to a magical kingdom.";
				itemDef.modelZoom = 1780;
				itemDef.rotationX = 739;
				itemDef.rotationY = 1452;
				itemDef.modelOffset1 = 6;
				itemDef.modelOffsetY = 12;
				itemDef.maleEquip1 = 64091;
				itemDef.femaleEquip1 = 64091;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20247:
				itemDef.modelID = 64093;
				itemDef.name = "Sora mask";
				itemDef.description = "I've been having these weird thoughts lately...";
				itemDef.modelZoom = 896;
				itemDef.rotationX = 0;
				itemDef.rotationY = 0;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = -4;
				itemDef.maleEquip1 = 64094;
				itemDef.femaleEquip1 = 64094;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20248:
				itemDef.modelID = 64095;
				itemDef.name = "Sora body";
				itemDef.description = "I've been having these weird thoughts lately...";
				itemDef.modelZoom = 1435;
				itemDef.rotationX = 0;
				itemDef.rotationY = 513;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = -8;
				itemDef.maleEquip1 = 64096;
				itemDef.femaleEquip1 = 64096;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20249:
				itemDef.modelID = 64097;
				itemDef.name = "Sora legs";
				itemDef.description = "I've been having these weird thoughts lately...";
				itemDef.modelZoom = 1435;
				itemDef.rotationX = 0;
				itemDef.rotationY = 513;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = 0;
				itemDef.maleEquip1 = 64098;
				itemDef.femaleEquip1 = 64098;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20250:
				itemDef.modelID = 64099;
				itemDef.name = "Thor's hammer";
				itemDef.description = "Do I look to be in a gaming mood?";
				itemDef.modelZoom = 1643;
				itemDef.rotationX = 1539;
				itemDef.rotationY = 391;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = 0;
				itemDef.maleEquip1 = 64100;
				itemDef.femaleEquip1 = 64100;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20251:
				itemDef.modelID = 64101;
				itemDef.name = "Captain america shield";
				itemDef.description = "I'm just a kid from Brooklyn...";
				itemDef.modelZoom = 1643;
				itemDef.rotationX = 1539;
				itemDef.rotationY = 391;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = 0;
				itemDef.maleEquip1 = 64102;
				itemDef.femaleEquip1 = 64102;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20252:
				itemDef.modelID = 64107;
				itemDef.name = "Goku mask";
				itemDef.description = "Let's give it everything we've got!";
				itemDef.modelZoom = 965;
				itemDef.rotationX = 0;
				itemDef.rotationY = 0;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = 0;
				itemDef.maleEquip1 = 64106;
				itemDef.femaleEquip1 = 64106;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20253:
				itemDef.modelID = 64110;
				itemDef.name = "Goku body";
				itemDef.description = "Let's give it everything we've got!";
				itemDef.modelZoom = 1417;
				itemDef.rotationX = 0;
				itemDef.rotationY = 530;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = 0;
				itemDef.maleEquip1 = 64111;
				itemDef.femaleEquip1 = 64111;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20254:
				itemDef.modelID = 64109;
				itemDef.name = "Goku legs";
				itemDef.description = "Let's give it everything we've got!";
				itemDef.modelZoom = 1800;
				itemDef.rotationX = 0;
				itemDef.rotationY = 530;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = 0;
				itemDef.maleEquip1 = 64108;
				itemDef.femaleEquip1 = 64108;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20255:
				itemDef.modelID = 64103;
				itemDef.name = "Goku boots";
				itemDef.description = "Let's give it everything we've got!";
				itemDef.modelZoom = 826;
				itemDef.rotationX = 357;
				itemDef.rotationY = 287;
				itemDef.modelOffset1 = 6;
				itemDef.modelOffsetY = 0;
				itemDef.maleEquip1 = 64103;
				itemDef.femaleEquip1 = 64103;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20256:
				itemDef.modelID = 64105;
				itemDef.name = "Goku gloves";
				itemDef.description = "Let's give it everything we've got!";
				itemDef.modelZoom = 635;
				itemDef.rotationX = 0;
				itemDef.rotationY = 548;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = 0;
				itemDef.maleEquip1 = 64104;
				itemDef.femaleEquip1 = 64104;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20257:
				itemDef.modelID = 64116;
				itemDef.name = "Vegeta mask";
				itemDef.description = "Push through the Pain. Giving Up Hurts More.";
				itemDef.modelZoom = 896;
				itemDef.rotationX = 0;
				itemDef.rotationY = 0;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = -4;
				itemDef.maleEquip1 = 64115;
				itemDef.femaleEquip1 = 64115;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20258:
				itemDef.modelID = 64120;
				itemDef.name = "Vegeta body";
				itemDef.description = "Push through the Pain. Giving Up Hurts More.";
				itemDef.modelZoom = 1435;
				itemDef.rotationX = 0;
				itemDef.rotationY = 548;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = 0;
				itemDef.maleEquip1 = 64119;
				itemDef.femaleEquip1 = 64119;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20259:
				itemDef.modelID = 64118;
				itemDef.name = "Vegeta legs";
				itemDef.description = "Push through the Pain. Giving Up Hurts More.";
				itemDef.modelZoom = 1800;
				itemDef.rotationX = 0;
				itemDef.rotationY = 548;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = 0;
				itemDef.maleEquip1 = 64117;
				itemDef.femaleEquip1 = 64117;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20260:
				itemDef.modelID = 64112;
				itemDef.name = "Vegeta boots";
				itemDef.description = "Push through the Pain. Giving Up Hurts More.";
				itemDef.modelZoom = 896;
				itemDef.rotationX = 252;
				itemDef.rotationY = 130;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = 0;
				itemDef.maleEquip1 = 64112;
				itemDef.femaleEquip1 = 64112;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20261:
				itemDef.modelID = 64114;
				itemDef.name = "Vegeta gloves";
				itemDef.description = "Push through the Pain. Giving Up Hurts More.";
				itemDef.modelZoom = 670;
				itemDef.rotationX = 0;
				itemDef.rotationY = 461;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = 0;
				itemDef.maleEquip1 = 64113;
				itemDef.femaleEquip1 = 64113;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20262:
				itemDef.modelID = 64121;
				itemDef.name = "Toxic glaive";
				itemDef.description = "Don't be toxic.";
				itemDef.modelZoom = 2217;
				itemDef.rotationX = 896;
				itemDef.rotationY = 530;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = 14;
				itemDef.maleEquip1 = 64123;
				itemDef.femaleEquip1 = 64123;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20263:
				itemDef.modelID = 64121;
				itemDef.name = "Toxic glaive left-hand";
				itemDef.description = "Don't be toxic.";
				itemDef.modelZoom = 2217;
				itemDef.rotationX = 426;
				itemDef.rotationY = 530;
				itemDef.modelOffset1 = -4;
				itemDef.modelOffsetY = -12;
				itemDef.maleEquip1 = 64122;
				itemDef.femaleEquip1 = 64122;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20264:
				itemDef.modelID = 64125;
				itemDef.name = "Purple helmet";
				itemDef.description = "It's a purple helmet.";
				itemDef.modelZoom = 600;
				itemDef.rotationX = 0;
				itemDef.rotationY = 0;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = -4;
				itemDef.maleEquip1 = 64124;
				itemDef.femaleEquip1 = 64124;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20265:
				itemDef.modelID = 64127;
				itemDef.name = "Purple platebody";
				itemDef.description = "It's a purple platebody.";
				itemDef.modelZoom = 1696;
				itemDef.rotationX = 0;
				itemDef.rotationY = 478;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = 0;
				itemDef.maleEquip1 = 64126;
				itemDef.femaleEquip1 = 64126;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20266:
				itemDef.modelID = 64129;
				itemDef.name = "Purple platelegs";
				itemDef.description = "It's a purple platelegs.";
				itemDef.modelZoom = 1904;
				itemDef.rotationX = 0;
				itemDef.rotationY = 374;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = 8;
				itemDef.maleEquip1 = 64128;
				itemDef.femaleEquip1 = 64128;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20267:
				itemDef.modelID = 64134;
				itemDef.name = "Purple boots";
				itemDef.description = "It's a purple boots.";
				itemDef.modelZoom = 861;
				itemDef.rotationX = 287;
				itemDef.rotationY = 270;
				itemDef.modelOffset1 = 8;
				itemDef.modelOffsetY = 12;
				itemDef.maleEquip1 = 64134;
				itemDef.femaleEquip1 = 64134;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20268:
				itemDef.modelID = 64133;
				itemDef.name = "Purple gloves";
				itemDef.description = "It's a purple gloves.";
				itemDef.modelZoom = 1330;
				itemDef.rotationX = 235;
				itemDef.rotationY = 322;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = 0;
				itemDef.maleEquip1 = 64132;
				itemDef.femaleEquip1 = 64132;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20269:
				itemDef.modelID = 64131;
				itemDef.name = "Purple battle axe";
				itemDef.description = "It's a purple battle axe.";
				itemDef.modelZoom = 2496;
				itemDef.rotationX = 304;
				itemDef.rotationY = 652;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = 0;
				itemDef.maleEquip1 = 64130;
				itemDef.femaleEquip1 = 64130;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20270:
				itemDef.modelID = 64136;
				itemDef.name = "Purple shield";
				itemDef.description = "It's a purple shield.";
				itemDef.modelZoom = 1696;
				itemDef.rotationX = 1017;
				itemDef.rotationY = 600;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = 0;
				itemDef.maleEquip1 = 64135;
				itemDef.femaleEquip1 = 64135;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20271:
				itemDef.modelID = 64143;
				itemDef.name = "Archmage hat";
				itemDef.description = "The hat of a powerful mage.";
				itemDef.modelZoom = 1122;
				itemDef.rotationX = 2009;
				itemDef.rotationY = 530;
				itemDef.modelOffset1 = -8;
				itemDef.modelOffsetY = -12;
				itemDef.maleEquip1 = 64137;
				itemDef.femaleEquip1 = 64137;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20272:
				itemDef.modelID = 64144;
				itemDef.name = "Archmage robe top";
				itemDef.description = "The robe top of a powerful mage.";
				itemDef.modelZoom = 1470;
				itemDef.rotationX = 0;
				itemDef.rotationY = 600;
				itemDef.modelOffset1 = 4;
				itemDef.modelOffsetY = 12;
				itemDef.maleEquip1 = 64138;
				itemDef.femaleEquip1 = 64138;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20273:
				itemDef.modelID = 64145;
				itemDef.name = "Archmage robe bottom";
				itemDef.description = "The robe bottom of a powerful mage.";
				itemDef.modelZoom = 1748;
				itemDef.rotationX = 0;
				itemDef.rotationY = 600;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = 0;
				itemDef.maleEquip1 = 64139;
				itemDef.femaleEquip1 = 64139;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20274:
				itemDef.modelID = 64146;
				itemDef.name = "Archmage gloves";
				itemDef.description = "The gloves of a powerful mage.";
				itemDef.modelZoom = 1383;
				itemDef.rotationX = 281;
				itemDef.rotationY = 270;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = 0;
				itemDef.maleEquip1 = 64140;
				itemDef.femaleEquip1 = 64140;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20275:
				itemDef.modelID = 64141;
				itemDef.name = "Archmage boots";
				itemDef.description = "The boots of a powerful mage.";
				itemDef.modelZoom = 1157;
				itemDef.rotationX = 322;
				itemDef.rotationY = 270;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = 0;
				itemDef.maleEquip1 = 64141;
				itemDef.femaleEquip1 = 64141;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20276:
				itemDef.modelID = 64147;
				itemDef.name = "Archmage battlestaff";
				itemDef.description = "The battlestaff of a powerful mage.";
				itemDef.modelZoom = 2409;
				itemDef.rotationX = 1174;
				itemDef.rotationY = 235;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = 0;
				itemDef.maleEquip1 = 64142;
				itemDef.femaleEquip1 = 64142;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20277:
				itemDef.modelID = 64148;
				itemDef.name = "Acid helmet";
				itemDef.description = "Acid PS starter helm.";
				itemDef.modelZoom = 548;
				itemDef.rotationX = 0;
				itemDef.rotationY = 0;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = -6;
				itemDef.maleEquip1 = 64149;
				itemDef.femaleEquip1 = 64149;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20278:
				itemDef.modelID = 64150;
				itemDef.name = "Acid platebody";
				itemDef.description = "Acid PS starter platebody.";
				itemDef.modelZoom = 1348;
				itemDef.rotationX = 0;
				itemDef.rotationY = 496;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = 0;
				itemDef.maleEquip1 = 64151;
				itemDef.femaleEquip1 = 64151;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20279:
				itemDef.modelID = 64152;
				itemDef.name = "Acid platelegs";
				itemDef.description = "Acid PS starter platelegs.";
				itemDef.modelZoom = 1661;
				itemDef.rotationX = 0;
				itemDef.rotationY = 548;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = 0;
				itemDef.maleEquip1 = 64153;
				itemDef.femaleEquip1 = 64153;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20280:
				itemDef.modelID = 64157;
				itemDef.name = "Acid scimitar";
				itemDef.description = "Acid PS starter scimitar.";
				itemDef.modelZoom = 2426;
				itemDef.rotationX = 757;
				itemDef.rotationY = 461;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = 0;
				itemDef.maleEquip1 = 64154;
				itemDef.femaleEquip1 = 64154;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20281:
				itemDef.modelID = 64158;
				itemDef.name = "Acid staff";
				itemDef.description = "Acid PS starter staff.";
				itemDef.modelZoom = 3000;
				itemDef.rotationX = 1209;
				itemDef.rotationY = 374;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = -9;
				itemDef.maleEquip1 = 64155;
				itemDef.femaleEquip1 = 64155;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20282:
				itemDef.modelID = 64159;
				itemDef.name = "Acid bow";
				itemDef.description = "Acid PS starter bow.";
				itemDef.modelZoom = 3000;
				itemDef.rotationX = 1261;
				itemDef.rotationY = 426;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = -9;
				itemDef.maleEquip1 = 64156;
				itemDef.femaleEquip1 = 64156;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20283:
				itemDef.modelID = 64160;
				itemDef.name = "BFG";
				itemDef.description = "Big fucking gun.";
				itemDef.modelZoom = 1748;
				itemDef.rotationX = 670;
				itemDef.rotationY = 426;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = 1;
				itemDef.maleEquip1 = 64161;
				itemDef.femaleEquip1 = 64161;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20284:
				itemDef.modelID = 64160;
				itemDef.name = "BFG offhand";
				itemDef.description = "Big fucking gun offhand.";
				itemDef.modelZoom = 1748;
				itemDef.rotationX = 252;
				itemDef.rotationY = 478;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = 0;
				itemDef.maleEquip1 = 64162;
				itemDef.femaleEquip1 = 64162;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20285:
				itemDef.modelID = 64163;
				itemDef.name = "Brazilian BFG";
				itemDef.description = "Brazilian big fucking gun.";
				itemDef.modelZoom = 1643;
				itemDef.rotationX = 635;
				itemDef.rotationY = 478;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = 0;
				itemDef.maleEquip1 = 64164;
				itemDef.femaleEquip1 = 64164;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20286:
				itemDef.modelID = 64163;
				itemDef.name = "Brazilian BFG offhand";
				itemDef.description = "Brazilian big fucking gun offhand.";
				itemDef.modelZoom = 1643;
				itemDef.rotationX = 235;
				itemDef.rotationY = 478;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = 0;
				itemDef.maleEquip1 = 64165;
				itemDef.femaleEquip1 = 64165;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20287:
				itemDef.modelID = 64168;
				itemDef.name = "Guthix staff";
				itemDef.description = "The staff of the man himself.";
				itemDef.modelZoom = 3000;
				itemDef.rotationX = 1417;
				itemDef.rotationY = 530;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = 0;
				itemDef.maleEquip1 = 64169;
				itemDef.femaleEquip1 = 64169;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20288:
				itemDef.modelID = 64171;
				itemDef.name = "Guthix shield";
				itemDef.description = "The shield of the man himself.";
				itemDef.modelZoom = 1800;
				itemDef.rotationX = 983;
				itemDef.rotationY = 565;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = 0;
				itemDef.maleEquip1 = 64170;
				itemDef.femaleEquip1 = 64170;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20289:
				itemDef.modelID = 64166;
				itemDef.name = "Guthix amulet";
				itemDef.description = "The amulet of the man himself.";
				itemDef.modelZoom = 757;
				itemDef.rotationX = 0;
				itemDef.rotationY = 617;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = 4;
				itemDef.maleEquip1 = 64167;
				itemDef.femaleEquip1 = 64167;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20290:
				itemDef.modelID = 64173;
				itemDef.name = "Cookie monster mask";
				itemDef.description = "Keep calm and eat cookies.";
				itemDef.modelZoom = 774;
				itemDef.rotationX = 0;
				itemDef.rotationY = 0;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = -6;
				itemDef.maleEquip1 = 64172;
				itemDef.femaleEquip1 = 64172;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20291:
				itemDef.modelID = 64174;
				itemDef.name = "Rainbow lance";
				itemDef.description = "Really colorful lance.";
				itemDef.modelZoom = 2287;
				itemDef.rotationX = 217;
				itemDef.rotationY = 478;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = 0;
				itemDef.maleEquip1 = 64175;
				itemDef.femaleEquip1 = 64175;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20292:
				itemDef.modelID = 64176;
				itemDef.name = "Infinity gauntlets";
				itemDef.description = "Infinity gauntlets.";
				itemDef.modelZoom = 652;
				itemDef.rotationX = 9;
				itemDef.rotationY = 478;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = 4;
				itemDef.maleEquip1 = 64177;
				itemDef.femaleEquip1 = 64177;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20293:
				itemDef.modelID = 64180;
				itemDef.name = "Crimson torva helm";
				itemDef.description = "Why you examining this?";
				itemDef.modelZoom = 757;
				itemDef.rotationX = 0;
				itemDef.rotationY = 0;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = 0;
				itemDef.maleEquip1 = 64181;
				itemDef.femaleEquip1 = 64181;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20294:
				itemDef.modelID = 64178;
				itemDef.name = "Crimson torva platebody";
				itemDef.description = "Why you examining this?";
				itemDef.modelZoom = 1557;
				itemDef.rotationX = 0;
				itemDef.rotationY = 548;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = 0;
				itemDef.maleEquip1 = 64179;
				itemDef.femaleEquip1 = 64179;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20295:
				itemDef.modelID = 64182;
				itemDef.name = "Crimson torva platelegs";
				itemDef.description = "Why you examining this?";
				itemDef.modelZoom = 1696;
				itemDef.rotationX = 0;
				itemDef.rotationY = 513;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = 0;
				itemDef.maleEquip1 = 64183;
				itemDef.femaleEquip1 = 64183;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20296:
				itemDef.modelID = 64190;
				itemDef.name = "Crimson pernix cowl";
				itemDef.description = "Why you examining this?";
				itemDef.modelZoom = 774;
				itemDef.rotationX = 0;
				itemDef.rotationY = 548;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = 0;
				itemDef.maleEquip1 = 64191;
				itemDef.femaleEquip1 = 64191;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20297:
				itemDef.modelID = 64192;
				itemDef.name = "Crimson pernix body";
				itemDef.description = "Why you examining this?";
				itemDef.modelZoom = 1452;
				itemDef.rotationX = 0;
				itemDef.rotationY = 513;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = 0;
				itemDef.maleEquip1 = 64193;
				itemDef.femaleEquip1 = 64193;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20298:
				itemDef.modelID = 64194;
				itemDef.name = "Crimson pernix chaps";
				itemDef.description = "Why you examining this?";
				itemDef.modelZoom = 1730;
				itemDef.rotationX = 0;
				itemDef.rotationY = 513;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = 0;
				itemDef.maleEquip1 = 64195;
				itemDef.femaleEquip1 = 64195;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20299:
				itemDef.modelID = 64184;
				itemDef.name = "Crimson virtus mask";
				itemDef.description = "Why you examining this?";
				itemDef.modelZoom = 791;
				itemDef.rotationX = 0;
				itemDef.rotationY = 478;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = 0;
				itemDef.maleEquip1 = 64185;
				itemDef.femaleEquip1 = 64185;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20300:
				itemDef.modelID = 64186;
				itemDef.name = "Crimson virtus top";
				itemDef.description = "Why you examining this?";
				itemDef.modelZoom = 1348;
				itemDef.rotationX = 0;
				itemDef.rotationY = 478;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = 0;
				itemDef.maleEquip1 = 64187;
				itemDef.femaleEquip1 = 64187;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20301:
				itemDef.modelID = 64188;
				itemDef.name = "Crimson virtus bottom";
				itemDef.description = "Why you examining this?";
				itemDef.modelZoom = 1835;
				itemDef.rotationX = 1904;
				itemDef.rotationY = 478;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = 0;
				itemDef.maleEquip1 = 64189;
				itemDef.femaleEquip1 = 64189;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20302:
				itemDef.modelID = 64197;
				itemDef.name = "Sora gloves";
				itemDef.description = "I've been having these weird thoughts lately...";
				itemDef.modelZoom = 1243;
				itemDef.rotationX = 0;
				itemDef.rotationY = 478;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = 0;
				itemDef.maleEquip1 = 64196;
				itemDef.femaleEquip1 = 64196;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20303:
				itemDef.modelID = 64198;
				itemDef.name = "Sora boots";
				itemDef.description = "I've been having these weird thoughts lately...";
				itemDef.modelZoom = 930;
				itemDef.rotationX = 304;
				itemDef.rotationY = 287;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = 0;
				itemDef.maleEquip1 = 64198;
				itemDef.femaleEquip1 = 64198;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20304:
				itemDef.modelID = 64199;
				itemDef.name = "Shaman full helm";
				itemDef.description = "The full helmet of a very powerful shaman.";
				itemDef.modelZoom = 739;
				itemDef.rotationX = 183;
				itemDef.rotationY = 0;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = -4;
				itemDef.maleEquip1 = 64200;
				itemDef.femaleEquip1 = 64198;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20305:
				itemDef.modelID = 64203;
				itemDef.name = "Shaman platebody";
				itemDef.description = "The platebody of a very powerful shaman.";
				itemDef.modelZoom = 1417;
				itemDef.rotationX = 0;
				itemDef.rotationY = 496;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = 0;
				itemDef.maleEquip1 = 64204;
				itemDef.femaleEquip1 = 64204;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20306:
				itemDef.modelID = 64201;
				itemDef.name = "Shaman platelegs";
				itemDef.description = "The platelegs of a very powerful shaman.";
				itemDef.modelZoom = 1835;
				itemDef.rotationX = 0;
				itemDef.rotationY = 496;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = 0;
				itemDef.maleEquip1 = 64202;
				itemDef.femaleEquip1 = 64202;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20307:
				itemDef.modelID = 64206;
				itemDef.name = "Shaman bow";
				itemDef.description = "The bow of a very powerful shaman.";
				itemDef.modelZoom = 1835;
				itemDef.rotationX = 757;
				itemDef.rotationY = 704;
				itemDef.modelOffset1 = 14;
				itemDef.modelOffsetY = 0;
				itemDef.maleEquip1 = 64205;
				itemDef.femaleEquip1 = 64205;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20308:
				itemDef.modelID = 64215;
				itemDef.name = "Battlemage helmet";
				itemDef.description = "The helm of a powerful battlemage.";
				itemDef.modelZoom = 843;
				itemDef.rotationX = 0;
				itemDef.rotationY = 461;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = 0;
				itemDef.maleEquip1 = 64214;
				itemDef.femaleEquip1 = 64214;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20309:
				itemDef.modelID = 64208;
				itemDef.name = "Battlemage platebody";
				itemDef.description = "The platebody of a powerful battlemage.";
				itemDef.modelZoom = 1522;
				itemDef.rotationX = 0;
				itemDef.rotationY = 496;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = 0;
				itemDef.maleEquip1 = 64207;
				itemDef.femaleEquip1 = 64207;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20310:
				itemDef.modelID = 64217;
				itemDef.name = "Battlemage platelegs";
				itemDef.description = "The platelegs of a powerful battlemage.";
				itemDef.modelZoom = 1800;
				itemDef.rotationX = 0;
				itemDef.rotationY = 530;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = 0;
				itemDef.maleEquip1 = 64216;
				itemDef.femaleEquip1 = 64216;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20311:
				itemDef.modelID = 64209;
				itemDef.name = "Battlemage boots";
				itemDef.description = "The boots of a powerful battlemage.";
				itemDef.modelZoom = 948;
				itemDef.rotationX = 0;
				itemDef.rotationY = 0;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = 0;
				itemDef.maleEquip1 = 64209;
				itemDef.femaleEquip1 = 64209;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20312:
				itemDef.modelID = 64213;
				itemDef.name = "Battlemage gloves";
				itemDef.description = "The gloves of a powerful battlemage.";
				itemDef.modelZoom = 1470;
				itemDef.rotationX = 0;
				itemDef.rotationY = 443;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = 0;
				itemDef.maleEquip1 = 64212;
				itemDef.femaleEquip1 = 64212;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20313:
				itemDef.modelID = 64211;
				itemDef.name = "Battlemage cape";
				itemDef.description = "The cape of a powerful battlemage.";
				itemDef.modelZoom = 2165;
				itemDef.rotationX = 1052;
				itemDef.rotationY = 409;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = 30;
				itemDef.maleEquip1 = 64210;
				itemDef.femaleEquip1 = 64210;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20314:
				itemDef.modelID = 64235;
				itemDef.name = "Archer helmet";
				itemDef.description = "The helm of a powerful archer.";
				itemDef.modelZoom = 861;
				itemDef.rotationX = 0;
				itemDef.rotationY = 548;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = 0;
				itemDef.maleEquip1 = 64234;
				itemDef.femaleEquip1 = 64234;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20315:
				itemDef.modelID = 64230;
				itemDef.name = "Archer platebody";
				itemDef.description = "The platebody of a powerful archer.";
				itemDef.modelZoom = 1054;
				itemDef.rotationX = 0;
				itemDef.rotationY = 548;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = 0;
				itemDef.maleEquip1 = 64229;
				itemDef.femaleEquip1 = 64229;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20316:
				itemDef.modelID = 64237;
				itemDef.name = "Archer platelegs";
				itemDef.description = "The platelegs of a powerful archer.";
				itemDef.modelZoom = 1696;
				itemDef.rotationX = 0;
				itemDef.rotationY = 548;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = 0;
				itemDef.maleEquip1 = 64236;
				itemDef.femaleEquip1 = 64236;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20317:
				itemDef.modelID = 64231;
				itemDef.name = "Archer boots";
				itemDef.description = "The boots of a powerful archer.";
				itemDef.modelZoom = 896;
				itemDef.rotationX = 0;
				itemDef.rotationY = 0;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = 0;
				itemDef.maleEquip1 = 64231;
				itemDef.femaleEquip1 = 64231;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20318:
				itemDef.modelID = 64239;
				itemDef.name = "Archer gloves";
				itemDef.description = "The gloves of a powerful archer.";
				itemDef.modelZoom = 1435;
				itemDef.rotationX = 0;
				itemDef.rotationY = 478;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = 0;
				itemDef.maleEquip1 = 64238;
				itemDef.femaleEquip1 = 64238;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20319:
				itemDef.modelID = 64233;
				itemDef.name = "Archer cape";
				itemDef.description = "The cape of a powerful archer.";
				itemDef.modelZoom = 2357;
				itemDef.rotationX = 1052;
				itemDef.rotationY = 478;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = 20;
				itemDef.maleEquip1 = 64232;
				itemDef.femaleEquip1 = 64232;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20320:
				itemDef.modelID = 64226;
				itemDef.name = "Warrior helmet";
				itemDef.description = "The helm of a powerful warrior.";
				itemDef.modelZoom = 843;
				itemDef.rotationX = 0;
				itemDef.rotationY = 426;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = 8;
				itemDef.maleEquip1 = 64225;
				itemDef.femaleEquip1 = 64225;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20321:
				itemDef.modelID = 64219;
				itemDef.name = "Warrior platebody";
				itemDef.description = "The platebody of a powerful warrior.";
				itemDef.modelZoom = 1504;
				itemDef.rotationX = 0;
				itemDef.rotationY = 426;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = 0;
				itemDef.maleEquip1 = 64218;
				itemDef.femaleEquip1 = 64218;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20322:
				itemDef.modelID = 64228;
				itemDef.name = "Warrior platelegs";
				itemDef.description = "The platelegs of a powerful warrior.";
				itemDef.modelZoom = 1748;
				itemDef.rotationX = 0;
				itemDef.rotationY = 426;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = 0;
				itemDef.maleEquip1 = 64227;
				itemDef.femaleEquip1 = 64227;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20323:
				itemDef.modelID = 64220;
				itemDef.name = "Warrior boots";
				itemDef.description = "The boots of a powerful warrior.";
				itemDef.modelZoom = 948;
				itemDef.rotationX = 0;
				itemDef.rotationY = 0;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = 0;
				itemDef.maleEquip1 = 64220;
				itemDef.femaleEquip1 = 64220;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20324:
				itemDef.modelID = 64224;
				itemDef.name = "Warrior gloves";
				itemDef.description = "The gloves of a powerful warrior.";
				itemDef.modelZoom = 4504;
				itemDef.rotationX = 0;
				itemDef.rotationY = 461;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = 0;
				itemDef.maleEquip1 = 64223;
				itemDef.femaleEquip1 = 64223;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20325:
				itemDef.modelID = 64222;
				itemDef.name = "Warrior cape";
				itemDef.description = "The cape of a powerful warrior.";
				itemDef.modelZoom = 2235;
				itemDef.rotationX = 1052;
				itemDef.rotationY = 461;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = 16;
				itemDef.maleEquip1 = 64221;
				itemDef.femaleEquip1 = 64221;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20326:
				itemDef.modelID = 64241;
				itemDef.name = "Lava bow";
				itemDef.description = "A bow made of lava.";
				itemDef.modelZoom = 1957;
				itemDef.rotationX = 983;
				itemDef.rotationY = 235;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = 0;
				itemDef.maleEquip1 = 64240;
				itemDef.femaleEquip1 = 64240;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20327:
				itemDef.modelID = 64247;
				itemDef.name = "Lava pernix cowl";
				itemDef.description = "A pernix cowl made of lava.";
				itemDef.modelZoom = 809;
				itemDef.rotationX = 270;
				itemDef.rotationY = 26;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = 0;
				itemDef.maleEquip1 = 64246;
				itemDef.femaleEquip1 = 64246;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20328:
				itemDef.modelID = 64245;
				itemDef.name = "Lava pernix body";
				itemDef.description = "A pernix body made of lava.";
				itemDef.modelZoom = 1348;
				itemDef.rotationX = 0;
				itemDef.rotationY = 530;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = 0;
				itemDef.maleEquip1 = 64244;
				itemDef.femaleEquip1 = 64244;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20329:
				itemDef.modelID = 64243;
				itemDef.name = "Lava pernix chaps";
				itemDef.description = "A pernix chaps made of lava.";
				itemDef.modelZoom = 1661;
				itemDef.rotationX = 0;
				itemDef.rotationY = 530;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = 0;
				itemDef.maleEquip1 = 64242;
				itemDef.femaleEquip1 = 64242;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20330:
				itemDef.modelID = 64248;
				itemDef.name = "Lava staff";
				itemDef.description = "A staff made of lava.";
				itemDef.modelZoom = 1922;
				itemDef.rotationX = 1487;
				itemDef.rotationY = 200;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = 0;
				itemDef.maleEquip1 = 64249;
				itemDef.femaleEquip1 = 64249;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20331:
				itemDef.modelID = 64255;
				itemDef.name = "Lava virtus mask";
				itemDef.description = "A virtus mask made of lava.";
				itemDef.modelZoom = 878;
				itemDef.rotationX = 0;
				itemDef.rotationY = 0;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = -4;
				itemDef.maleEquip1 = 64254;
				itemDef.femaleEquip1 = 64254;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20332:
				itemDef.modelID = 64253;
				itemDef.name = "Lava virtus top";
				itemDef.description = "A virtus top made of lava.";
				itemDef.modelZoom = 1261;
				itemDef.rotationX = 0;
				itemDef.rotationY = 513;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = 0;
				itemDef.maleEquip1 = 64252;
				itemDef.femaleEquip1 = 64252;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20333:
				itemDef.modelID = 64251;
				itemDef.name = "Lava virtus bottom";
				itemDef.description = "A virtus bottom made of lava.";
				itemDef.modelZoom = 1765;
				itemDef.rotationX = 0;
				itemDef.rotationY = 513;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = -8;
				itemDef.maleEquip1 = 64250;
				itemDef.femaleEquip1 = 64250;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20334:
				itemDef.modelID = 64256;
				itemDef.name = "Lava sword";
				itemDef.description = "A sword made of lava.";
				itemDef.modelZoom = 2478;
				itemDef.rotationX = 1539;
				itemDef.rotationY = 513;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = -40;
				itemDef.maleEquip1 = 64257;
				itemDef.femaleEquip1 = 64257;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20335:
				itemDef.modelID = 64260;
				itemDef.name = "Lava torva helm";
				itemDef.description = "A torva helm made of lava.";
				itemDef.modelZoom = 1070;
				itemDef.rotationX = 0;
				itemDef.rotationY = 0;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = -8;
				itemDef.maleEquip1 = 64261;
				itemDef.femaleEquip1 = 64261;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20336:
				itemDef.modelID = 64262;
				itemDef.name = "Lava torva platebody";
				itemDef.description = "A platebody made of lava.";
				itemDef.modelZoom = 1609;
				itemDef.rotationX = 0;
				itemDef.rotationY = 530;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = 0;
				itemDef.maleEquip1 = 64263;
				itemDef.femaleEquip1 = 64263;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20337:
				itemDef.modelID = 64258;
				itemDef.name = "Lava torva platelegs";
				itemDef.description = "A torva platelegs made of lava.";
				itemDef.modelZoom = 1835;
				itemDef.rotationX = 0;
				itemDef.rotationY = 530;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = 0;
				itemDef.maleEquip1 = 64259;
				itemDef.femaleEquip1 = 64259;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20338:
				itemDef.modelID = 64265;
				itemDef.name = "Bone wand";
				itemDef.description = "Avada Kedavra!";
				itemDef.modelZoom = 1504;
				itemDef.rotationX = 1243;
				itemDef.rotationY = 391;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = 6;
				itemDef.maleEquip1 = 64264;
				itemDef.femaleEquip1 = 64264;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20339:
				itemDef.modelID = 64266;
				itemDef.name = "Voldemort mask";
				itemDef.description = "There is no good and evil. There is only power.";
				itemDef.modelZoom = 565;
				itemDef.rotationX = 0;
				itemDef.rotationY = 0;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = 0;
				itemDef.maleEquip1 = 64267;
				itemDef.femaleEquip1 = 64267;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20340:
				itemDef.modelID = 64268;
				itemDef.name = "Voldemort robe top";
				itemDef.description = "There is no good and evil. There is only power.";
				itemDef.modelZoom = 1417;
				itemDef.rotationX = 0;
				itemDef.rotationY = 496;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = 0;
				itemDef.maleEquip1 = 64269;
				itemDef.femaleEquip1 = 64269;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20341:
				itemDef.modelID = 64270;
				itemDef.name = "Voldemort robe legs";
				itemDef.description = "There is no good and evil. There is only power.";
				itemDef.modelZoom = 1696;
				itemDef.rotationX = 0;
				itemDef.rotationY = 496;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = 0;
				itemDef.maleEquip1 = 64271;
				itemDef.femaleEquip1 = 64271;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20342:
				itemDef.modelID = 64274;
				itemDef.name = "Voldemort boots";
				itemDef.description = "There is no good and evil. There is only power.";
				itemDef.modelZoom = 913;
				itemDef.rotationX = 339;
				itemDef.rotationY = 270;
				itemDef.modelOffset1 = 10;
				itemDef.modelOffsetY = 0;
				itemDef.maleEquip1 = 64274;
				itemDef.femaleEquip1 = 64274;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20343:
				itemDef.modelID = 64272;
				itemDef.name = "Voldemort gloves";
				itemDef.description = "There is no good and evil. There is only power.";
				itemDef.modelZoom = 896;
				itemDef.rotationX = 235;
				itemDef.rotationY = 548;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = 0;
				itemDef.maleEquip1 = 64273;
				itemDef.femaleEquip1 = 64273;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20344:
				itemDef.modelID = 64275;
				itemDef.name = "Skeletal helmet";
				itemDef.description = "A helmet made of bones.";
				itemDef.modelZoom = 791;
				itemDef.rotationX = 0;
				itemDef.rotationY = 0;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = 0;
				itemDef.maleEquip1 = 64278;
				itemDef.femaleEquip1 = 64278;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20345:
				itemDef.modelID = 64276;
				itemDef.name = "Skeletal platebody";
				itemDef.description = "A platebody made of bones.";
				itemDef.modelZoom = 1661;
				itemDef.rotationX = 0;
				itemDef.rotationY = 530;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = 0;
				itemDef.maleEquip1 = 64279;
				itemDef.femaleEquip1 = 64279;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20346:
				itemDef.modelID = 64277;
				itemDef.name = "Skeletal platelegs";
				itemDef.description = "A platelegs made of bones.";
				itemDef.modelZoom = 1939;
				itemDef.rotationX = 0;
				itemDef.rotationY = 530;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = 0;
				itemDef.maleEquip1 = 64280;
				itemDef.femaleEquip1 = 64280;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20347:
				itemDef.modelID = 64282;
				itemDef.name = "Skeletal spear";
				itemDef.description = "A spear made of bones.";
				itemDef.modelZoom = 3000;
				itemDef.rotationX = 1330;
				itemDef.rotationY = 183;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = 0;
				itemDef.maleEquip1 = 64281;
				itemDef.femaleEquip1 = 64281;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20348:
				itemDef.modelID = 64284;
				itemDef.name = "Solar bow";
				itemDef.description = "A solar bow.";
				itemDef.modelZoom = 2357;
				itemDef.rotationX = 617;
				itemDef.rotationY = 583;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = 0;
				itemDef.maleEquip1 = 64283;
				itemDef.femaleEquip1 = 64283;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20349:
				itemDef.modelID = 64285;
				itemDef.name = "Solar helm";
				itemDef.description = "A solar helm.";
				itemDef.modelZoom = 600;
				itemDef.rotationX = 0;
				itemDef.rotationY = 0;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = 0;
				itemDef.maleEquip1 = 64286;
				itemDef.femaleEquip1 = 64286;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20350:
				itemDef.modelID = 64287;
				itemDef.name = "Solar platebody";
				itemDef.description = "A solar platebody.";
				itemDef.modelZoom = 1557;
				itemDef.rotationX = 1783;
				itemDef.rotationY = 496;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = 12;
				itemDef.maleEquip1 = 64288;
				itemDef.femaleEquip1 = 64288;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20351:
				itemDef.modelID = 64289;
				itemDef.name = "Solar platelegs";
				itemDef.description = "A solar platelegs.";
				itemDef.modelZoom = 1678;
				itemDef.rotationX = 1800;
				itemDef.rotationY = 461;
				itemDef.modelOffset1 = -4;
				itemDef.modelOffsetY = 12;
				itemDef.maleEquip1 = 64290;
				itemDef.femaleEquip1 = 64290;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20352:
				itemDef.modelID = 64291;
				itemDef.name = "Necromancer hood";
				itemDef.description = "A necromancer's hood.";
				itemDef.modelZoom = 670;
				itemDef.rotationX = 130;
				itemDef.rotationY = 0;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = -6;
				itemDef.maleEquip1 = 64292;
				itemDef.femaleEquip1 = 64292;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20353:
				itemDef.modelID = 64293;
				itemDef.name = "Necromancer robe top";
				itemDef.description = "A necromancer's robe top.";
				itemDef.modelZoom = 1643;
				itemDef.rotationX = 0;
				itemDef.rotationY = 548;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = 0;
				itemDef.maleEquip1 = 64294;
				itemDef.femaleEquip1 = 64294;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20354:
				itemDef.modelID = 64295;
				itemDef.name = "Necromancer robe bottom";
				itemDef.description = "A necromancer robe bottom.";
				itemDef.modelZoom = 2026;
				itemDef.rotationX = 0;
				itemDef.rotationY = 548;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = 0;
				itemDef.maleEquip1 = 64296;
				itemDef.femaleEquip1 = 64296;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20355:
				itemDef.modelID = 64297;
				itemDef.name = "Keyblade [II]";
				itemDef.description = "A key to a magical kingdom.";
				itemDef.modelZoom = 1609;
				itemDef.rotationX = 1035;
				itemDef.rotationY = 548;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = 0;
				itemDef.maleEquip1 = 64298;
				itemDef.femaleEquip1 = 64298;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20356:
				itemDef.modelID = 64299;
				itemDef.name = "Keyblade [III]";
				itemDef.description = "A key to a magical kingdom.";
				itemDef.modelZoom = 1904;
				itemDef.rotationX = 1087;
				itemDef.rotationY = 513;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = 0;
				itemDef.maleEquip1 = 64300;
				itemDef.femaleEquip1 = 64300;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20357:
				itemDef.modelID = 64301;
				itemDef.name = "Keyblade [IV]";
				itemDef.description = "A key to a magical kingdom.";
				itemDef.modelZoom = 1730;
				itemDef.rotationX = 0;
				itemDef.rotationY = 513;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = 0;
				itemDef.maleEquip1 = 64302;
				itemDef.femaleEquip1 = 64302;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20358:
				itemDef.modelID = 64303;
				itemDef.name = "Keyblade [V]";
				itemDef.description = "A key to a magical kingdom.";
				itemDef.modelZoom = 2078;
				itemDef.rotationX = 1052;
				itemDef.rotationY = 513;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = -12;
				itemDef.maleEquip1 = 64304;
				itemDef.femaleEquip1 = 64304;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20359:
				itemDef.modelID = 64305;
				itemDef.name = "Keyblade [VI]";
				itemDef.description = "A key to a magical kingdom.";
				itemDef.modelZoom = 1730;
				itemDef.rotationX = 1035;
				itemDef.rotationY = 513;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = -8;
				itemDef.maleEquip1 = 64306;
				itemDef.femaleEquip1 = 64306;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20360:
				itemDef.modelID = 64307;
				itemDef.name = "Keyblade [VII]";
				itemDef.description = "A key to a magical kingdom.";
				itemDef.modelZoom = 2078;
				itemDef.rotationX = 1243;
				itemDef.rotationY = 513;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = -8;
				itemDef.maleEquip1 = 64308;
				itemDef.femaleEquip1 = 64308;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20361:
				itemDef.modelID = 64309;
				itemDef.name = "Keyblade [VIII]";
				itemDef.description = "A key to a magical kingdom.";
				itemDef.modelZoom = 1713;
				itemDef.rotationX = 1017;
				itemDef.rotationY = 513;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = 0;
				itemDef.maleEquip1 = 64310;
				itemDef.femaleEquip1 = 64310;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20362:
				itemDef.modelID = 64311;
				itemDef.name = "Ice bow";
				itemDef.description = "A bow made of ice.";
				itemDef.modelZoom = 2774;
				itemDef.rotationX = 1174;
				itemDef.rotationY = 652;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = 0;
				itemDef.maleEquip1 = 64312;
				itemDef.femaleEquip1 = 64312;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20363:
				itemDef.modelID = 64313;
				itemDef.name = "Ice staff";
				itemDef.description = "A staff made of ice.";
				itemDef.modelZoom = 2478;
				itemDef.rotationX = 1417;
				itemDef.rotationY = 704;
				itemDef.modelOffset1 = -4;
				itemDef.modelOffsetY = -8;
				itemDef.maleEquip1 = 64314;
				itemDef.femaleEquip1 = 64314;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20364:
				itemDef.modelID = 64315;
				itemDef.name = "Ice sword";
				itemDef.description = "A sword made of ice.";
				itemDef.modelZoom = 1780;
				itemDef.rotationX = 1609;
				itemDef.rotationY = 565;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = 0;
				itemDef.maleEquip1 = 64316;
				itemDef.femaleEquip1 = 64316;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20365:
				itemDef.modelID = 64315;
				itemDef.name = "Ice sword offhand";
				itemDef.description = "An offhand sword made of ice.";
				itemDef.modelZoom = 1780;
				itemDef.rotationX = 1104;
				itemDef.rotationY = 565;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = 0;
				itemDef.maleEquip1 = 64317;
				itemDef.femaleEquip1 = 64317;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20366:
				itemDef.modelID = 64319;
				itemDef.name = "Minigun";
				itemDef.description = "A minigun.";
				itemDef.modelZoom = 1852;
				itemDef.rotationX = 1278;
				itemDef.rotationY = 374;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = 0;
				itemDef.maleEquip1 = 64318;
				itemDef.femaleEquip1 = 64318;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20367:
				itemDef.modelID = 64321;
				itemDef.name = "Gold minigun";
				itemDef.description = "A gold minigun.";
				itemDef.modelZoom = 1800;
				itemDef.rotationX = 1243;
				itemDef.rotationY = 548;
				itemDef.modelOffset1 = -16;
				itemDef.modelOffsetY = -20;
				itemDef.maleEquip1 = 64320;
				itemDef.femaleEquip1 = 64320;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20368:
				itemDef.modelID = 64323;
				itemDef.name = "Nature minigun";
				itemDef.description = "A nature minigun.";
				itemDef.modelZoom = 1991;
				itemDef.rotationX = 1243;
				itemDef.rotationY = 548;
				itemDef.modelOffset1 = -16;
				itemDef.modelOffsetY = -20;
				itemDef.maleEquip1 = 64322;
				itemDef.femaleEquip1 = 64322;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20369:
				itemDef.modelID = 64325;
				itemDef.name = "Frost minigun";
				itemDef.description = "A frost minigun.";
				itemDef.modelZoom = 1991;
				itemDef.rotationX = 1243;
				itemDef.rotationY = 548;
				itemDef.modelOffset1 = -16;
				itemDef.modelOffsetY = -20;
				itemDef.maleEquip1 = 64324;
				itemDef.femaleEquip1 = 64324;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20370:
				itemDef.modelID = 64327;
				itemDef.name = "Lava minigun";
				itemDef.description = "A lava minigun.";
				itemDef.modelZoom = 1852;
				itemDef.rotationX = 1609;
				itemDef.rotationY = 374;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = 0;
				itemDef.maleEquip1 = 64326;
				itemDef.femaleEquip1 = 64326;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20371:
				itemDef.modelID = 64329;
				itemDef.name = "Shadow minigun";
				itemDef.description = "A shadow minigun.";
				itemDef.modelZoom = 1991;
				itemDef.rotationX = 1243;
				itemDef.rotationY = 548;
				itemDef.modelOffset1 = -16;
				itemDef.modelOffsetY = -20;
				itemDef.maleEquip1 = 64328;
				itemDef.femaleEquip1 = 64328;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20372:
				itemDef.modelID = 64331;
				itemDef.name = "Dragon minigun";
				itemDef.description = "A dragon minigun.";
				itemDef.modelZoom = 1991;
				itemDef.rotationX = 1243;
				itemDef.rotationY = 548;
				itemDef.modelOffset1 = -16;
				itemDef.modelOffsetY = -20;
				itemDef.maleEquip1 = 64330;
				itemDef.femaleEquip1 = 64330;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20373:
				itemDef.modelID = 64333;
				itemDef.name = "American minigun";
				itemDef.description = "An american minigun.";
				itemDef.modelZoom = 1991;
				itemDef.rotationX = 1243;
				itemDef.rotationY = 548;
				itemDef.modelOffset1 = -16;
				itemDef.modelOffsetY = -20;
				itemDef.maleEquip1 = 64332;
				itemDef.femaleEquip1 = 64332;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				itemDef.actions = new String[5];
				itemDef.actions[1] = "Wear";
				break;

			case 20374:
				itemDef.name = "Bronze box";
				itemDef.actions = new String[5];
				itemDef.actions[0] = "Open";
				itemDef.modelID = 64334;
				itemDef.modelZoom = 1609;
				itemDef.rotationX = 96;
				itemDef.rotationY = 78;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = 0;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				break;

			case 20375:
				itemDef.name = "Silver box";
				itemDef.actions = new String[5];
				itemDef.actions[0] = "Open";
				itemDef.modelID = 64335;
				itemDef.modelZoom = 1609;
				itemDef.rotationX = 96;
				itemDef.rotationY = 78;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = 0;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				break;

			case 20376:
				itemDef.name = "Gold box";
				itemDef.actions = new String[5];
				itemDef.actions[0] = "Open";
				itemDef.modelID = 64336;
				itemDef.modelZoom = 1609;
				itemDef.rotationX = 96;
				itemDef.rotationY = 78;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = 0;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				break;

			case 20377:
				itemDef.name = "Acid box";
				itemDef.actions = new String[5];
				itemDef.actions[0] = "Open";
				itemDef.modelID = 64337;
				itemDef.modelZoom = 1609;
				itemDef.rotationX = 96;
				itemDef.rotationY = 78;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = 0;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				break;

			case 20378:
				itemDef.name = "Acid chest";
				itemDef.actions = new String[5];
				itemDef.actions[0] = "Open";
				itemDef.modelID = 64338;
				itemDef.modelZoom = 1383;
				itemDef.rotationX = 0;
				itemDef.rotationY = 0;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = 0;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				break;

			case 20379:
				itemDef.name = "$5 bond";
				itemDef.actions = new String[5];
				itemDef.actions[0] = "Claim";
				itemDef.modelID = 64339;
				itemDef.modelZoom = 2252;
				itemDef.rotationX = 0;
				itemDef.rotationY = 478;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = 0;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				break;

			case 20380:
				itemDef.name = "$10 bond";
				itemDef.actions = new String[5];
				itemDef.actions[0] = "Claim";
				itemDef.modelID = 64340;
				itemDef.modelZoom = 2252;
				itemDef.rotationX = 0;
				itemDef.rotationY = 478;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = 0;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				break;

			case 20381:
				itemDef.name = "$25 bond";
				itemDef.actions = new String[5];
				itemDef.actions[0] = "Claim";
				itemDef.modelID = 64341;
				itemDef.modelZoom = 2252;
				itemDef.rotationX = 0;
				itemDef.rotationY = 478;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = 0;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				break;

			case 20382:
				itemDef.name = "$50 bond";
				itemDef.actions = new String[5];
				itemDef.actions[0] = "Claim";
				itemDef.modelID = 64342;
				itemDef.modelZoom = 2252;
				itemDef.rotationX = 0;
				itemDef.rotationY = 478;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = 0;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				break;

			case 20383:
				itemDef.name = "$100 bond";
				itemDef.actions = new String[5];
				itemDef.actions[0] = "Claim";
				itemDef.modelID = 64343;
				itemDef.modelZoom = 2252;
				itemDef.rotationX = 0;
				itemDef.rotationY = 478;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = 0;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				break;

			case 20384:
				itemDef.name = "$1 bond";
				itemDef.actions = new String[5];
				itemDef.actions[0] = "Claim";
				itemDef.modelID = 64344;
				itemDef.modelZoom = 2252;
				itemDef.rotationX = 0;
				itemDef.rotationY = 478;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = 0;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				break;

			case 20385:
				itemDef.name = "Bond box";
				itemDef.actions = new String[5];
				itemDef.actions[0] = "Open";
				itemDef.modelID = 64345;
				itemDef.modelZoom = 1035;
				itemDef.rotationX = 130;
				itemDef.rotationY = 165;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = -12;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				break;

			case 20386:
				itemDef.name = "Bond chest";
				itemDef.actions = new String[5];
				itemDef.actions[0] = "Open";
				itemDef.modelID = 64346;
				itemDef.modelZoom = 1452;
				itemDef.rotationX = 0;
				itemDef.rotationY = 0;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffsetY = 0;
				itemDef.groundActions = new String[5];
				itemDef.groundActions[2] = "Take";
				break;

		}
		return itemDef;
	}






    private void readValues(Stream stream) {
        do {
            int i = stream.readUnsignedByte();
            if (i == 0) {
                return;
            }
            if (i == 1) {
                modelID = stream.readUnsignedWord();
            } else if (i == 2) {
                name = stream.readString();
            } else if (i == 3) {
                description = stream.readString();
            } else if (i == 4) {
                modelZoom = stream.readUnsignedWord();
            } else if (i == 5) {
                rotationY = stream.readUnsignedWord();
            } else if (i == 6) {
                rotationX = stream.readUnsignedWord();
            } else if (i == 7) {
                modelOffset1 = stream.readUnsignedWord();
                if (modelOffset1 > 32767) {
                    modelOffset1 -= 0x10000;
                }
            } else if (i == 8) {
                modelOffsetY = stream.readUnsignedWord();
                if (modelOffsetY > 32767) {
                    modelOffsetY -= 0x10000;
                }
            } else if (i == 10) {
                stream.readUnsignedWord();
            } else if (i == 11) {
                stackable = true;
            } else if (i == 12) {
                value = stream.readUnsignedWord();
            } else if (i == 16) {
                membersObject = true;
            } else if (i == 23) {
                maleEquip1 = stream.readUnsignedWord();
                maleYOffset = stream.readSignedByte();
            } else if (i == 24) {
                maleEquip2 = stream.readUnsignedWord();
            } else if (i == 25) {
                femaleEquip1 = stream.readUnsignedWord();
                femaleYOffset = stream.readSignedByte();
            } else if (i == 26) {
                femaleEquip2 = stream.readUnsignedWord();
            } else if (i >= 30 && i < 35) {
                if (groundActions == null) {
                    groundActions = new String[5];
                }
                groundActions[i - 30] = stream.readString();
                if (groundActions[i - 30].equalsIgnoreCase("hidden")) {
                    groundActions[i - 30] = null;
                }
            } else if (i >= 35 && i < 40) {
                if (actions == null) {
                    actions = new String[5];
                }
                actions[i - 35] = stream.readString();
                if (actions[i - 35].equalsIgnoreCase("null")) {
                    actions[i - 35] = null;
                }
            } else if (i == 40) {
                int j = stream.readUnsignedByte();
                editedModelColor = new int[j];
                newModelColor = new int[j];
                for (int k = 0; k < j; k++) {
                    editedModelColor[k] = stream.readUnsignedWord();
                    newModelColor[k] = stream.readUnsignedWord();
                }
            } else if (i == 78) {
                maleEquip3 = stream.readUnsignedWord();
            } else if (i == 79) {
                femaleEquip3 = stream.readUnsignedWord();
            } else if (i == 90) {
                maleDialogue = stream.readUnsignedWord();
            } else if (i == 91) {
                femaleDialogue = stream.readUnsignedWord();
            } else if (i == 92) {
                maleDialogueModel = stream.readUnsignedWord();
            } else if (i == 93) {
                femaleDialogueModel = stream.readUnsignedWord();
            } else if (i == 95) {
                modelOffsetX = stream.readUnsignedWord();
            } else if (i == 97) {
                certID = stream.readUnsignedWord();
            } else if (i == 98) {
                certTemplateID = stream.readUnsignedWord();
            } else if (i >= 100 && i < 110) {
                if (stackIDs == null) {
                    stackIDs = new int[10];
                    stackAmounts = new int[10];
                }
                stackIDs[i - 100] = stream.readUnsignedWord();
                stackAmounts[i - 100] = stream.readUnsignedWord();
            } else if (i == 110) {
                sizeX = stream.readUnsignedWord();
            } else if (i == 111) {
                sizeY = stream.readUnsignedWord();
            } else if (i == 112) {
                sizeZ = stream.readUnsignedWord();
            } else if (i == 113) {
                shadow = stream.readSignedByte();
            } else if (i == 114) {
                lightness = stream.readSignedByte() * 5;
            } else if (i == 115) {
                team = stream.readUnsignedByte();
            } else if (i == 116) {
                lendID = stream.readUnsignedWord();
            } else if (i == 117) {
                lentItemID = stream.readUnsignedWord();
            }
        } while (true);
    }

    public static void setSettings() {
        try {
            prices = new int[22694];
            int index = 0;
            for (String line : Files.readAllLines(Paths.get(signlink.findcachedir() + "data/data.txt"), Charset.defaultCharset())) {
                prices[index] = Integer.parseInt(line);
                index++;
            }
            for (int i : UNTRADEABLE_ITEMS) {
                untradeableItems.add(i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void toNote() {
        ItemDef itemDef = forID(certTemplateID);
        modelID = itemDef.modelID;
        modelZoom = itemDef.modelZoom;
        rotationY = itemDef.rotationY;
        rotationX = itemDef.rotationX;
        modelOffsetX = itemDef.modelOffsetX;
        modelOffset1 = itemDef.modelOffset1;
        modelOffsetY = itemDef.modelOffsetY;
        editedModelColor = itemDef.editedModelColor;
        newModelColor = itemDef.newModelColor;
        ItemDef itemDef_1 = forID(certID);
        name = itemDef_1.name;
        membersObject = itemDef_1.membersObject;
        value = itemDef_1.value;
        String s = "a";
        char c = itemDef_1.name.charAt(0);
        if (c == 'A' || c == 'E' || c == 'I' || c == 'O' || c == 'U') {
            s = "an";
        }
        description = ("Swap this note at any bank for " + s + " " + itemDef_1.name + ".");
        stackable = true;
    }

    private void toLend() {
        ItemDef itemDef = forID(lentItemID);
        actions = new String[5];
        modelID = itemDef.modelID;
        modelOffset1 = itemDef.modelOffset1;
        rotationX = itemDef.rotationX;
        modelOffsetY = itemDef.modelOffsetY;
        modelZoom = itemDef.modelZoom;
        rotationY = itemDef.rotationY;
        modelOffsetX = itemDef.modelOffsetX;
        value = 0;
        ItemDef itemDef_1 = forID(lendID);
        maleDialogueModel = itemDef_1.maleDialogueModel;
        editedModelColor = itemDef_1.editedModelColor;
        maleEquip3 = itemDef_1.maleEquip3;
        maleEquip2 = itemDef_1.maleEquip2;
        femaleDialogueModel = itemDef_1.femaleDialogueModel;
        maleDialogue = itemDef_1.maleDialogue;
        groundActions = itemDef_1.groundActions;
        maleEquip1 = itemDef_1.maleEquip1;
        name = itemDef_1.name;
        femaleEquip1 = itemDef_1.femaleEquip1;
        membersObject = itemDef_1.membersObject;
        femaleDialogue = itemDef_1.femaleDialogue;
        femaleEquip2 = itemDef_1.femaleEquip2;
        femaleEquip3 = itemDef_1.femaleEquip3;
        newModelColor = itemDef_1.newModelColor;
        team = itemDef_1.team;
        if (itemDef_1.actions != null) {
            for (int i_33_ = 0; i_33_ < 4; i_33_++) {
                actions[i_33_] = itemDef_1.actions[i_33_];
            }
        }
        actions[4] = "Discard";
    }

    public static Sprite getSprite(int i, int j, int k, int zoom) {
        if (k == 0 && zoom != -1) {
            Sprite sprite = (Sprite) spriteCache.get(i);
            if (sprite != null && sprite.maxHeight != j && sprite.maxHeight != -1) {
                sprite.unlink();
                sprite = null;
            }
            if (sprite != null) {
                return sprite;
            }
        }
        ItemDef itemDef = forID(i);
        if (itemDef.stackIDs == null) {
            j = -1;
        }
        if (j > 1) {
            int i1 = -1;
            for (int j1 = 0; j1 < 10; j1++) {
                if (j >= itemDef.stackAmounts[j1] && itemDef.stackAmounts[j1] != 0) {
                    i1 = itemDef.stackIDs[j1];
                }
            }

            if (i1 != -1) {
                itemDef = forID(i1);
            }
        }
        Model model = itemDef.getItemModelFinalised(1);

        if (model == null) {
           return null;
        }

        Sprite sprite = null;
        if (itemDef.certTemplateID != -1) {
            sprite = getSprite(itemDef.certID, 10, -1);
            if (sprite == null) {
                return null;
            }
        }
        if (itemDef.lendID != -1) {
            sprite = getSprite(itemDef.lendID, 50, 0);
            if (sprite == null) {
                return null;
            }
        }
        Sprite sprite2 = new Sprite(32, 32);
        int k1 = Rasterizer.center_x;
        int l1 = Rasterizer.center_y;
        int ai[] = Rasterizer.lineOffsets;
        int ai1[] = DrawingArea.pixels;
        int i2 = DrawingArea.width;
        int j2 = DrawingArea.height;
        int k2 = DrawingArea.topX;
        int l2 = DrawingArea.bottomX;
        int i3 = DrawingArea.topY;
        int j3 = DrawingArea.bottomY;
        Rasterizer.notTextured = false;
        DrawingArea.initDrawingArea(32, 32, sprite2.myPixels);
        DrawingArea.drawPixels(32, 0, 0, 0, 32);
        Rasterizer.setDefaultBounds();
        int k3 = itemDef.modelZoom;
        if (zoom != -1 && zoom != 0) {
            k3 = (itemDef.modelZoom * 100) / zoom;
        }
        if (k == -1) {
            k3 = (int) ((double) k3 * 1.5D);
        }
        if (k > 0) {
            k3 = (int) ((double) k3 * 1.04D);
        }
        int l3 = Rasterizer.SINE[itemDef.rotationY] * k3 >> 16;
        int i4 = Rasterizer.COSINE[itemDef.rotationY] * k3 >> 16;
        model.renderSingle(itemDef.rotationX, itemDef.modelOffsetX, itemDef.rotationY, itemDef.modelOffset1, l3 + model.modelHeight / 2 + itemDef.modelOffsetY, i4 + itemDef.modelOffsetY);
        for (int i5 = 31; i5 >= 0; i5--) {
            for (int j4 = 31; j4 >= 0; j4--) {
                if (sprite2.myPixels[i5 + j4 * 32] != 0) {
                    continue;
                }
                if (i5 > 0 && sprite2.myPixels[(i5 - 1) + j4 * 32] > 1) {
                    sprite2.myPixels[i5 + j4 * 32] = 1;
                    continue;
                }
                if (j4 > 0 && sprite2.myPixels[i5 + (j4 - 1) * 32] > 1) {
                    sprite2.myPixels[i5 + j4 * 32] = 1;
                    continue;
                }
                if (i5 < 31 && sprite2.myPixels[i5 + 1 + j4 * 32] > 1) {
                    sprite2.myPixels[i5 + j4 * 32] = 1;
                    continue;
                }
                if (j4 < 31 && sprite2.myPixels[i5 + (j4 + 1) * 32] > 1) {
                    sprite2.myPixels[i5 + j4 * 32] = 1;
                }
            }

        }

        if (k > 0) {
            for (int j5 = 31; j5 >= 0; j5--) {
                for (int k4 = 31; k4 >= 0; k4--) {
                    if (sprite2.myPixels[j5 + k4 * 32] != 0) {
                        continue;
                    }
                    if (j5 > 0 && sprite2.myPixels[(j5 - 1) + k4 * 32] == 1) {
                        sprite2.myPixels[j5 + k4 * 32] = k;
                        continue;
                    }
                    if (k4 > 0 && sprite2.myPixels[j5 + (k4 - 1) * 32] == 1) {
                        sprite2.myPixels[j5 + k4 * 32] = k;
                        continue;
                    }
                    if (j5 < 31 && sprite2.myPixels[j5 + 1 + k4 * 32] == 1) {
                        sprite2.myPixels[j5 + k4 * 32] = k;
                        continue;
                    }
                    if (k4 < 31 && sprite2.myPixels[j5 + (k4 + 1) * 32] == 1) {
                        sprite2.myPixels[j5 + k4 * 32] = k;
                    }
                }

            }

        } else if (k == 0) {
            for (int k5 = 31; k5 >= 0; k5--) {
                for (int l4 = 31; l4 >= 0; l4--) {
                    if (sprite2.myPixels[k5 + l4 * 32] == 0 && k5 > 0 && l4 > 0 && sprite2.myPixels[(k5 - 1) + (l4 - 1) * 32] > 0) {
                        sprite2.myPixels[k5 + l4 * 32] = 0x302020;
                    }
                }

            }

        }
        if (itemDef.certTemplateID != -1) {
            int l5 = sprite.maxWidth;
            int j6 = sprite.maxHeight;
            sprite.maxWidth = 32;
            sprite.maxHeight = 32;
            sprite.drawSprite(0, 0);
            sprite.maxWidth = l5;
            sprite.maxHeight = j6;
        }
        if (itemDef.lendID != -1) {
            int l5 = sprite.maxWidth;
            int j6 = sprite.maxHeight;
            sprite.maxWidth = 32;
            sprite.maxHeight = 32;
            sprite.drawSprite(0, 0);
            sprite.maxWidth = l5;
            sprite.maxHeight = j6;
        }
        if (k == 0) {
            spriteCache.put(sprite2, i);
        }
        DrawingArea.initDrawingArea(j2, i2, ai1);
        DrawingArea.setDrawingArea(j3, k2, l2, i3);
        Rasterizer.center_x = k1;
        Rasterizer.center_y = l1;
        Rasterizer.lineOffsets = ai;
        Rasterizer.notTextured = true;
        sprite2.maxWidth = itemDef.stackable ? 33 : 32;
        sprite2.maxHeight = j;
        return sprite2;
    }

    public static Sprite getSprite(int i, int j, int k) {
        if (k == 0) {
            Sprite sprite = (Sprite) spriteCache.get(i);
            if (sprite != null && sprite.maxHeight != j && sprite.maxHeight != -1) {
                sprite.unlink();
                sprite = null;
            }
            if (sprite != null) {
                return sprite;
            }
        }
        ItemDef itemDef = forID(i);
        if (itemDef.stackIDs == null) {
            j = -1;
        }
        if (j > 1) {
            int i1 = -1;
            for (int j1 = 0; j1 < 10; j1++) {
                if (j >= itemDef.stackAmounts[j1] && itemDef.stackAmounts[j1] != 0) {
                    i1 = itemDef.stackIDs[j1];
                }
            }
            if (i1 != -1) {
                itemDef = forID(i1);
            }
        }
        Model model = itemDef.getItemModelFinalised(1);
        if (model == null) {
            return null;
        }
        Sprite sprite = null;
        if (itemDef.certTemplateID != -1) {
            sprite = getSprite(itemDef.certID, 10, -1);
            if (sprite == null) {
                return null;
            }
        }
        if (itemDef.lentItemID != -1) {
            sprite = getSprite(itemDef.lendID, 50, 0);
            if (sprite == null) {
                return null;
            }
        }
        Sprite sprite2 = new Sprite(32, 32);
        int k1 = Rasterizer.center_x;
        int l1 = Rasterizer.center_y;
        int ai[] = Rasterizer.lineOffsets;
        int ai1[] = DrawingArea.pixels;
        int i2 = DrawingArea.width;
        int j2 = DrawingArea.height;
        int k2 = DrawingArea.topX;
        int l2 = DrawingArea.bottomX;
        int i3 = DrawingArea.topY;
        int j3 = DrawingArea.bottomY;
        Rasterizer.notTextured = false;
        DrawingArea.initDrawingArea(32, 32, sprite2.myPixels);
        DrawingArea.drawPixels(32, 0, 0, 0, 32);
        Rasterizer.setDefaultBounds();
        int k3 = itemDef.modelZoom;
        if (k == -1) {
            k3 = (int) ((double) k3 * 1.5D);
        }
        if (k > 0) {
            k3 = (int) ((double) k3 * 1.04D);
        }
        int l3 = Rasterizer.SINE[itemDef.rotationY] * k3 >> 16;
        int i4 = Rasterizer.COSINE[itemDef.rotationY] * k3 >> 16;
        model.renderSingle(itemDef.rotationX, itemDef.modelOffsetX, itemDef.rotationY, itemDef.modelOffset1, l3 + model.modelHeight / 2 + itemDef.modelOffsetY, i4 + itemDef.modelOffsetY);
        for (int i5 = 31; i5 >= 0; i5--) {
            for (int j4 = 31; j4 >= 0; j4--) {
                if (sprite2.myPixels[i5 + j4 * 32] == 0) {
                    if (i5 > 0 && sprite2.myPixels[(i5 - 1) + j4 * 32] > 1) {
                        sprite2.myPixels[i5 + j4 * 32] = 1;
                    } else if (j4 > 0 && sprite2.myPixels[i5 + (j4 - 1) * 32] > 1) {
                        sprite2.myPixels[i5 + j4 * 32] = 1;
                    } else if (i5 < 31 && sprite2.myPixels[i5 + 1 + j4 * 32] > 1) {
                        sprite2.myPixels[i5 + j4 * 32] = 1;
                    } else if (j4 < 31 && sprite2.myPixels[i5 + (j4 + 1) * 32] > 1) {
                        sprite2.myPixels[i5 + j4 * 32] = 1;
                    }
                }
            }
        }
        if (k > 0) {
            for (int j5 = 31; j5 >= 0; j5--) {
                for (int k4 = 31; k4 >= 0; k4--) {
                    if (sprite2.myPixels[j5 + k4 * 32] == 0) {
                        if (j5 > 0 && sprite2.myPixels[(j5 - 1) + k4 * 32] == 1) {
                            sprite2.myPixels[j5 + k4 * 32] = k;
                        } else if (k4 > 0 && sprite2.myPixels[j5 + (k4 - 1) * 32] == 1) {
                            sprite2.myPixels[j5 + k4 * 32] = k;
                        } else if (j5 < 31 && sprite2.myPixels[j5 + 1 + k4 * 32] == 1) {
                            sprite2.myPixels[j5 + k4 * 32] = k;
                        } else if (k4 < 31 && sprite2.myPixels[j5 + (k4 + 1) * 32] == 1) {
                            sprite2.myPixels[j5 + k4 * 32] = k;
                        }
                    }
                }
            }
        } else if (k == 0) {
            for (int k5 = 31; k5 >= 0; k5--) {
                for (int l4 = 31; l4 >= 0; l4--) {
                    if (sprite2.myPixels[k5 + l4 * 32] == 0 && k5 > 0 && l4 > 0 && sprite2.myPixels[(k5 - 1) + (l4 - 1) * 32] > 0) {
                        sprite2.myPixels[k5 + l4 * 32] = 0x302020;
                    }
                }
            }
        }
        if (itemDef.certTemplateID != -1) {
            int l5 = sprite.maxWidth;
            int j6 = sprite.maxHeight;
            sprite.maxWidth = 32;
            sprite.maxHeight = 32;
            sprite.drawSprite(0, 0);
            sprite.maxWidth = l5;
            sprite.maxHeight = j6;
        }
        if (itemDef.lentItemID != -1) {
            int l5 = sprite.maxWidth;
            int j6 = sprite.maxHeight;
            sprite.maxWidth = 32;
            sprite.maxHeight = 32;
            sprite.drawSprite(0, 0);
            sprite.maxWidth = l5;
            sprite.maxHeight = j6;
        }
        if (k == 0) {
            spriteCache.put(sprite2, i);
        }
        DrawingArea.initDrawingArea(j2, i2, ai1);
        DrawingArea.setDrawingArea(j3, k2, l2, i3);
        Rasterizer.center_x = k1;
        Rasterizer.center_y = l1;
        Rasterizer.lineOffsets = ai;
        Rasterizer.notTextured = true;
        if (itemDef.stackable) {
            sprite2.maxWidth = 33;
        } else {
            sprite2.maxWidth = 32;
        }
        sprite2.maxHeight = j;
        return sprite2;
    }

    public Model getItemModelFinalised(int amount) {
        if (stackIDs != null && amount > 1) {
            int stackId = -1;
            for (int k = 0; k < 10; k++) {
                if (amount >= stackAmounts[k] && stackAmounts[k] != 0) {
                    stackId = stackIDs[k];
                }
            }
            if (stackId != -1) {
                return forID(stackId).getItemModelFinalised(1);
            }
        }
        Model model = (Model) modelCache.get(id);
        if (model != null) {
            return model;
        }
        model = Model.fetchModel(modelID);
        if (model == null) {
            return null;
        }
        if (sizeX != 128 || sizeY != 128 || sizeZ != 128) {
            model.scaleT(sizeX, sizeZ, sizeY);
        }
        if (editedModelColor != null) {
            for (int l = 0; l < editedModelColor.length; l++) {
                model.recolour(editedModelColor[l], newModelColor[l]);
            }
        }
        model.light(64 + shadow, 768 + lightness, -50, -10, -50, true);
        model.rendersWithinOneTile = true;
        modelCache.put(model, id);
        return model;
    }

    public Model getItemModel(int i) {
        if (stackIDs != null && i > 1) {
            int j = -1;
            for (int k = 0; k < 10; k++) {
                if (i >= stackAmounts[k] && stackAmounts[k] != 0) {
                    j = stackIDs[k];
                }
            }
            if (j != -1) {
                return forID(j).getItemModel(1);
            }
        }
        Model model = Model.fetchModel(modelID);
        if (model == null) {
            return null;
        }
        if (editedModelColor != null) {
            for (int l = 0; l < editedModelColor.length; l++) {
                model.recolour(editedModelColor[l], newModelColor[l]);
            }
        }
        return model;
    }

    public static final int[] UNTRADEABLE_ITEMS
            = {13661, 13262,
                6529, 6950, 1464, 2996, 2677, 2678, 2679, 2680, 2682,
                2683, 2684, 2685, 2686, 2687, 2688, 2689, 2690,
                6570, 12158, 12159, 12160, 12163, 12161, 12162,
                19143, 19149, 19146, 19157, 19162, 19152, 4155,
                8850, 10551, 8839, 8840, 8842, 11663, 11664,
                11665, 3842, 3844, 3840, 8844, 8845, 8846, 8847,
                8848, 8849, 8850, 10551, 7462, 7461, 7460,
                7459, 7458, 7457, 7456, 7455, 7454, 7453, 8839,
                8840, 8842, 11663, 11664, 11665, 10499, 9748,
                9754, 9751, 9769, 9757, 9760, 9763, 9802, 9808,
                9784, 9799, 9805, 9781, 9796, 9793, 9775, 9772,
                9778, 9787, 9811, 9766, 9749, 9755, 9752, 9770,
                9758, 9761, 9764, 9803, 9809, 9785, 9800, 9806,
                9782, 9797, 9794, 9776, 9773, 9779, 9788, 9812,
                9767, 9747, 9753, 9750, 9768, 9756, 9759, 9762,
                9801, 9807, 9783, 9798, 9804, 9780, 9795, 9792,
                9774, 9771, 9777, 9786, 9810, 9765, 9948, 9949,
                9950, 12169, 12170, 12171, 20671, 14641, 14642,
                6188, 10954, 10956, 10958,
                3057, 3058, 3059, 3060, 3061,
                7594, 7592, 7593, 7595, 7596,
                14076, 14077, 14081,
                10840, 10836, 6858, 6859, 10837, 10838, 10839,
                9925, 9924, 9923, 9922, 9921,
                4084, 4565, 20046, 20044, 20045,
                1050, 14595, 14603, 14602, 14605, 11789,
                19708, 19706, 19707,
                4860, 4866, 4872, 4878, 4884, 4896, 4890, 4896, 4902,
                4932, 4938, 4944, 4950, 4908, 4914, 4920, 4926, 4956,
                4926, 4968, 4994, 4980, 4986, 4992, 4998,
                18778, 18779, 18780, 18781,
                13450, 13444, 13405, 15502,
                10548, 10549, 10550, 10551, 10555, 10552, 10553, 2412, 2413, 2414,
                20747,
                18365, 18373, 18371, 15246, 12964, 12971, 12978, 14017,
                757, 8851,
                13855, 13848, 13857, 13856, 13854, 13853, 13852, 13851, 13850, 5509, 13653, 14021, 14020, 19111, 14019, 14022,
                19785, 19786, 18782, 18351, 18349, 18353, 18357, 18355, 18359, 18335
            };

    public ItemDef() {
        id = -1;
    }

    public byte femaleYOffset;
    public int value;
    public int[] editedModelColor;
    public int id;
    public static MemCache spriteCache = new MemCache(100);
    public static MemCache modelCache = new MemCache(50);
    public int[] newModelColor;
    public boolean membersObject;
    public int femaleEquip3;
    public int certTemplateID;
    public int femaleEquip2;
    public int maleEquip1;
    public int maleDialogueModel;
    public int sizeX;
    public String groundActions[];
    public int modelOffset1;
    public String name;
    public static ItemDef[] cache;
    public int femaleDialogueModel;
    public int modelID;
    public int maleDialogue;
    public boolean stackable;
    public String description;
    public int certID;
    public static int cacheIndex;
    public int modelZoom;
    public static Stream stream;
    public int lightness;
    public int maleEquip3;
    public int maleEquip2;
    public String actions[];
    public int rotationY;
    public int sizeZ;
    public int sizeY;
    public int[] stackIDs;
    public int modelOffsetY;
    public static int[] streamIndices;
    public int shadow;
    public int femaleDialogue;
    public int rotationX;
    public int femaleEquip1;
    public int[] stackAmounts;
    public int team;
    public static int totalItems;
    public int modelOffsetX;
    public byte maleYOffset;
    public byte maleXOffset;
    public int lendID;
    public int lentItemID;
    public boolean untradeable;
}

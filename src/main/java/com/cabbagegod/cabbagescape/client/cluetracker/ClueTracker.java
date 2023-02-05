package com.cabbagegod.cabbagescape.client.cluetracker;

import java.util.ArrayList;
import java.util.List;

import com.cabbagegod.cabbagescape.Main;
import com.cabbagegod.cabbagescape.client.cluetracker.ClueStep.Difficulty;
import com.cabbagegod.cabbagescape.data.itemdata.ItemDisplay;
import com.cabbagegod.cabbagescape.data.itemdata.ItemExtra;
import com.cabbagegod.cabbagescape.data.itemdata.ItemExtra__1;
import com.cabbagegod.cabbagescape.data.itemdata.ItemLore;
import com.cabbagegod.cabbagescape.events.EventHandler;
import com.cabbagegod.cabbagescape.ui.ClueHintScreen;
import com.google.gson.Gson;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtElement;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;

public class ClueTracker {
    static List<ClueStep> clueSteps = new ArrayList<ClueStep>();

    public static void Setup(){
        if(clueSteps.size() != 0)
            return;

        clueSteps.add(new ClueStep(1, Difficulty.easy, "Between G.E. and Varrock Treasure Chest area").setCoords(455,63,-541));
        clueSteps.add(new ClueStep(2, Difficulty.easy, "South of Varrock Platebody store / West of Varrock Archery store").setCoords(612,65,-410));
        clueSteps.add(new ClueStep(3, Difficulty.easy, "South-east of Fred the Farmer's house / Right next to the Lumbridge treasure chest").setCoords(548,65,84));
        clueSteps.add(new ClueStep(4, Difficulty.easy, "South of Lumbridge East Mine").setCoords(623,65,399));
        clueSteps.add(new ClueStep(5, Difficulty.easy, "West of Lumbridge Castle / East of encampment at the edge of the Lumbridge Swamp").setCoords(459,63,217));
        clueSteps.add(new ClueStep(6, Difficulty.easy, "West of the Champion's Guild / West of the Varrock West Mine").setCoords(430,65,-243));
        clueSteps.add(new ClueStep(7, Difficulty.easy, "South of Port Sarim docks").setCoords(4,72,355));
        clueSteps.add(new ClueStep(8, Difficulty.easy, "West of Karamja General Store / East of Karamja Volcano").setCoords(-258,65,395));
        clueSteps.add(new ClueStep(9, Difficulty.easy, "South of Rimmington Mine").setCoords(-131,65,179));
        clueSteps.add(new ClueStep(10, Difficulty.easy, "West of the Falador farm / North of Port Sarim").setCoords(4,65,-18));
        clueSteps.add(new ClueStep(11, Difficulty.easy, "North of Sorceress' Garden / South-east of Al Kharid").setCoords(930,67,390));
        clueSteps.add(new ClueStep(12, Difficulty.easy, "West of the Al Kharid Mine").setCoords(809,65,-46));
        clueSteps.add(new ClueStep(13, Difficulty.easy, "West of the eastern Lumbridge Cow pen / East of the Lumbridge Chicken coop").setCoords(660,65,18));
        clueSteps.add(new ClueStep(14, Difficulty.easy, "South of Varrock, inside the fencing West of the Varrock East Mine").setCoords(676,65,-192));
        clueSteps.add(new ClueStep(15, Difficulty.easy, "West of the G.E.").setCoords(338,65,-648));
        clueSteps.add(new ClueStep(16, Difficulty.easy, "North-west of the H.A.M. Hideout / South of the two farms").setCoords(357,65,64));
        clueSteps.add(new ClueStep(17, Difficulty.easy, "The northern tip on the inside of the Lumbridge cow pen (Eastern)").setCoords(687,63, -35));
        clueSteps.add(new ClueStep(18, Difficulty.easy, "West of Barbarian Village / South-east of the Body Altar").setCoords(134,68,-477));
        clueSteps.add(new ClueStep(19, Difficulty.easy, "YSITARLIK; Krystilia").setCoords(264,63,-695));
        clueSteps.add(new ClueStep(20, Difficulty.easy, "UESRIQ; Squire").setCoords(-138,68,-174));
        clueSteps.add(new ClueStep(21, Difficulty.easy, "GEGIA; Aggie").setCoords(202,62,70));
        clueSteps.add(new ClueStep(22, Difficulty.easy, "NED; Ned").setCoords(237,63,78));
        clueSteps.add(new ClueStep(23, Difficulty.easy, "LEEAL; Leela").setCoords(296,65,65));
        clueSteps.add(new ClueStep(24, Difficulty.easy, "ELLKAIDY; Lady Keli").setCoords(321,63,133));
        clueSteps.add(new ClueStep(25, Difficulty.easy, "OEJ; Joe").setCoords(309,63,133));
        clueSteps.add(new ClueStep(26, Difficulty.easy, "AASIS RMREON; Seaman Lorris").setCoords(48,66,247));
        clueSteps.add(new ClueStep(27, Difficulty.easy, "RBUAUY; Aubury").setCoords(708,63,-358));
        clueSteps.add(new ClueStep(28, Difficulty.easy, "OSMCFSEFROTIUC; Customs Officer").setCoords(-204,65,415));
        clueSteps.add(new ClueStep(29, Difficulty.easy, "ETTYH; Hetty").setCoords(-162,63,239));
        clueSteps.add(new ClueStep(30, Difficulty.easy, "YNIRVVISV; Sir Vyvin").setCoords(-112,79,-174));
        clueSteps.add(new ClueStep(31, Difficulty.easy, "GRTUHO; Thurgo").setCoords(-87,65,408));
        clueSteps.add(new ClueStep(32, Difficulty.easy, "IDOCR; Doric").setCoords(-207,61,-508));
        clueSteps.add(new ClueStep(33, Difficulty.easy, "DORLE; Reldo").setCoords(596,62,-639));
        clueSteps.add(new ClueStep(34, Difficulty.easy, "OHRUKA ECDOI; Duke Horacio").setCoords(576,71,185));
        clueSteps.add(new ClueStep(35, Difficulty.easy, "RDRRIS ODIWZAE; Wizard Sedridor").setCoords(284,49,376));
        clueSteps.add(new ClueStep(36, Difficulty.easy, "TAFEERCKARNE; Father Aereck").setCoords(687,64,228));
        clueSteps.add(new ClueStep(37, Difficulty.easy, "RAMIK; Karim").setCoords(751,66,292));
    }

    public void register(){
        UseItemCallback.EVENT.register(((player, world, hand) -> {
            ItemStack handItem = player.getStackInHand(Hand.MAIN_HAND);

            if(Formatting.strip(handItem.getName().getString().toLowerCase()).equals("clue scroll easy")){
                if(handItem.hasNbt() && MinecraftClient.getInstance().currentScreen == null) {
                    NbtElement element = handItem.getNbt().get("display");
                    String nbt = element.asString();

                    Gson gson = new Gson();
                    ItemDisplay display = gson.fromJson(nbt, ItemDisplay.class);
                    List<ItemLore> itemLores = display.getLore();

                    if(!itemLores.isEmpty()){
                        int clueId = -100;

                        //Blame bandit for this
                        for(ItemLore lore : itemLores){
                            for(ItemExtra extra : lore.getExtra()){
                                //Checks if there is no clue id, like in the test server (thanks zombie :P)
                                if(extra.getExtra() == null)
                                    break;

                                for(ItemExtra__1 extra2 : extra.getExtra()) {
                                    try {
                                        clueId = Integer.parseInt(extra2.getText().replaceAll("[\\D]", ""));
                                    } catch (NumberFormatException ignored){ }
                                }
                            }
                        }

                        if(clueId != -100)
                            MinecraftClient.getInstance().setScreen(new ClueHintScreen(getStepFromId(clueId)));
                    }
                }
            }

            return TypedActionResult.pass(ItemStack.EMPTY);
        }));
    }

    public static ClueStep getStepFromId(int stepId){
        for(ClueStep step : clueSteps){
            if(step.getId() == stepId)
                return step;
        }
        return null;
    }
}

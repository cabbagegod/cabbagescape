package com.cabbagegod.cabbagescape.client.potiontimers;

import com.cabbagegod.cabbagescape.client.CabbageScapeClient;
import com.cabbagegod.cabbagescape.util.ThreadingUtil;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class PotionTimerManager {
    private static final PotionTimerManager INSTANCE = new PotionTimerManager();
    List<Potion> potions = getPotions();

    public static PotionTimerManager getInstance() { return INSTANCE; }

    public long lastUsed = 0;

    public HashMap<PotionInstance, Thread> potionThreads = new HashMap<>();

    int lastSlot = -123;
    String lastItemName = "";

    PotionTimerManager(){
        //Track when the player uses an item from their inventory
        ItemTooltipCallback.EVENT.register((itemStack, tooltip, text) -> {
            ClientPlayerEntity player = MinecraftClient.getInstance().player;

            //Make sure player isn't null and we are handling an item in the player invent
            if(player == null || !player.getInventory().contains(itemStack))
                return;

            //Don't use getSlotWithStack, it doesn't work with duplicate items
            int slot = -123;
            for (int i = 0; i < player.getInventory().main.size(); i++) {
                if(player.getInventory().main.get(i) == itemStack){
                    slot = i;
                }
            }

            //int slot = player.getInventory().getSlotWithStack(itemStack);
            String itemName = itemStack.getName().getString();

            //Check if selected slot has changed
            if(lastSlot == slot){
                //Check if the slot is the same but the name has changed
                if(!lastItemName.equals(itemName)){
                    Potion potion = parsePotionName(lastItemName);
                    if(potion != null){
                        onUsePotion(potion);
                    }
                }
            }

            lastSlot = slot;
            lastItemName = itemName;
        });

        //Track when the player has right clicked a potion in their hand
        UseItemCallback.EVENT.register(((player, world, hand) -> {
            //Check to make sure the player is the local player and not somebody nearby
            if(!player.isMainPlayer())
                return TypedActionResult.pass(ItemStack.EMPTY);

            ItemStack handItem = player.getStackInHand(Hand.MAIN_HAND);

            if(handItem == null)
                return TypedActionResult.pass(ItemStack.EMPTY);

            String itemName = Formatting.strip(handItem.getName().getString().toLowerCase());

            if(Objects.nonNull(itemName)){
                Potion potion = parsePotionName(itemName);
                if(potion != null){
                    onUsePotion(potion);
                }
            }

            return TypedActionResult.pass(ItemStack.EMPTY);
        }));
    }

    void onUsePotion(Potion potion){
        if(lastUsed + 1000 < System.currentTimeMillis()) {
            lastUsed = System.currentTimeMillis();

            //Interrupt old thread for selected potion if it's still running
            for(PotionInstance potionInstance : potionThreads.keySet()){
                if(potionInstance.name.equals(potion.getLiteralName())){
                    if (potionThreads.get(potionInstance).isAlive()) {
                        potionThreads.get(potionInstance).interrupt();
                    }

                    break;
                }
            }

            PotionInstance potionInst = new PotionInstance(lastUsed, potion.getLength(), potion.getLiteralName(), potion.getResourcePath());

            //Start new thread for potion
            potionThreads.put(potionInst, new Thread(() -> {
                ThreadingUtil.wait((potion.getLength() - 15) * 1000);
                //Check if the thread was interrupted, this means the player has refreshed their potion
                if (!potionThreads.get(potionInst).isInterrupted()) {
                    //Send notification
                    if (MinecraftClient.getInstance().player != null) {
                        CabbageScapeClient.notificationManager.sendNotification("Your " + potion.getLiteralName() + " is about to expire!");
                        ThreadingUtil.wait(15 * 1000);
                    }
                }
                potionThreads.remove(potionInst);
            }));
            potionThreads.get(potionInst).start();
        }
    }

    Potion parsePotionName(String name){
        for(Potion potion : potions){
            if(name.toLowerCase().contains(potion.getLiteralName())){
                return potion;
            }
        }
        return null;
    }

    List<Potion> getPotions(){
        List<Potion> potions = new ArrayList<Potion>();
        potions.add(new Potion(3 * 60, "super antifire potion", "textures/item/custom/diamond_shovel/herblore_or_potion/super_antifire_potion/super_antifire_potion_4.png"));
        potions.add(new Potion(6 * 60, "antifire potion", "textures/item/custom/diamond_shovel/herblore_or_potion/antifire_potion/antifire_potion_4.png"));
        potions.add(new Potion(12 * 60, "extended antifire", "textures/item/custom/diamond_shovel/herblore_or_potion/extended_antifire/extended_antifire_4.png"));
        potions.add(new Potion(6 * 60, "extended super antifire", "textures/item/custom/diamond_shovel/herblore_or_potion/extended_super_antifire/extended_super_antifire_4.png"));
        potions.add(new Potion(3 * 60, "agility potion", "textures/item/custom/diamond_shovel/herblore_or_potion/agility_potion/agility_potion_4.png"));
        potions.add(new Potion(4 * 60, "magic potion", "textures/item/custom/diamond_shovel/herblore_or_potion/magic_potion/magic_potion_4.png"));
        potions.add(new Potion(3 * 60, "hunter potion", "textures/item/custom/diamond_shovel/herblore_or_potion/hunter_potion/hunter_potion_4.png"));
        potions.add(new Potion(3 * 60, "fishing potion", "textures/item/custom/diamond_shovel/herblore_or_potion/fishing_potion/fishing_potion_4.png"));

        return potions;
    }
}

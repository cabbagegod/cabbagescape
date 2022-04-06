package com.cabbagegod.cabbagescape.client.potiontimers;

import com.cabbagegod.cabbagescape.notifications.TrayNotification;
import com.cabbagegod.cabbagescape.util.ThreadingUtil;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class PotionTimerManager {
    private static final PotionTimerManager INSTANCE = new PotionTimerManager();
    List<Potion> potions = getPotions();

    public static PotionTimerManager getInstance() { return INSTANCE; }

    long lastUsed = 0;

    HashMap<Potion, Thread> potionThreads = new HashMap<>();

    PotionTimerManager(){
        /*
        ItemTooltipCallback.EVENT.register((itemStack, tooltip, text) -> {
            if(MinecraftClient.getInstance().player == null)
                return;


            MinecraftClient.getInstance().player.getInventory().
            MinecraftClient.getInstance().player.sendChatMessage("tooltip");
        });*/

        //Track when a player has right clicked a potion in their hand
        UseItemCallback.EVENT.register(((player, world, hand) -> {
            ItemStack handItem = player.getStackInHand(Hand.MAIN_HAND);

            if(handItem == null)
                return TypedActionResult.pass(ItemStack.EMPTY);

            String itemName = Formatting.strip(handItem.getName().getString().toLowerCase());

            if(Objects.requireNonNull(itemName).contains("potion")){
                Potion potion = parsePotionName(itemName);
                if(potion != null){
                    usedPotion(potion);
                }
            }

            return TypedActionResult.pass(ItemStack.EMPTY);
        }));
    }

    void usedPotion(Potion potion){
        if(lastUsed + 1000 < System.currentTimeMillis()) {
            lastUsed = System.currentTimeMillis();

            //Interrupt old thread for selected potion if it's still running
            if(potionThreads.containsKey(potion)){
                if (potionThreads.get(potion).isAlive()) {
                    potionThreads.get(potion).interrupt();
                }
                potionThreads.remove(potion);
            }


            potionThreads.put(potion, new Thread(() -> {
                ThreadingUtil.wait((potion.getLength() - 15) * 1000);
                //Make sure the hashmap still has the potion
                if(potionThreads.containsKey(potion)) {
                    //Check if the thread was interrupted, this means the player has refreshed their potion
                    if (!potionThreads.get(potion).isInterrupted()) {
                        if (MinecraftClient.getInstance().player != null) {
                            MinecraftClient.getInstance().player.sendMessage(new LiteralText("Your " + potion.getLiteralName() + " is about to expire!"), false);
                        }
                    }
                }
            }));
            potionThreads.get(potion).start();
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

        potions.add(new Potion(20, "test potion"));
        potions.add(new Potion(6 * 60, "antifire potion"));
        potions.add(new Potion(12 * 60, "extended antifire"));
        potions.add(new Potion(3 * 60, "super antifire potion"));
        potions.add(new Potion(6 * 60, "extended super antifire"));
        potions.add(new Potion(3 * 60, "agility potion"));
        potions.add(new Potion(4 * 60, "magic potion"));
        potions.add(new Potion(3 * 60, "hunter potion"));
        potions.add(new Potion(3 * 60, "fishing potion"));

        return potions;
    }
}

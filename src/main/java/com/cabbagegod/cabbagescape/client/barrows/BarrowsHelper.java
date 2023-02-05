package com.cabbagegod.cabbagescape.client.barrows;

import com.cabbagegod.cabbagescape.client.blockoutline.PersistentOutlineRenderer;
import com.cabbagegod.cabbagescape.callbacks.DoorUseCallback;
import com.cabbagegod.cabbagescape.events.EventHandler;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;

public class BarrowsHelper implements EventHandler {
    private static final Bounds BarrowsBounds = new Bounds(new Vec3d(1583,17,-78), new Vec3d(1708,100,48));
    private static final Bounds BarrowsUndergroundBounds = new Bounds(new Vec3d(1583,17,-78), new Vec3d(1708,32,48));

    private static boolean isInBarrowsUnderground = false;

    public static List<BlockPos> trackedDoors = new ArrayList<>();

    static boolean runOnce = true;
    static ArmorStandEntity ahrimIndicator;
    static ArmorStandEntity karilIndicator;
    static ArmorStandEntity dharokIndicator;
    static ArmorStandEntity guthanIndicator;
    static ArmorStandEntity toragIndicator;
    static ArmorStandEntity veracIndicator;

    @Override
    public void start() {
        ClientTickEvents.END_CLIENT_TICK.register(BarrowsHelper::tickEvent);
        DoorUseCallback.EVENT.register(BarrowsHelper::onUseDoor);
    }

    private static void tickEvent(MinecraftClient client) {
        if(client.world != null) {
            assert client.player != null;

            //Check if player is in barrows
            boolean isInBarrows = BarrowsBounds.isWithinBounds(client.player.getPos());
            isInBarrowsUnderground = BarrowsUndergroundBounds.isWithinBounds(client.player.getPos());

            if(isInBarrows) {
                //Display the correct door puzzle answer
                //Check if there is a valid screen (there always should be when loaded into a world)
                if (client.player.currentScreenHandler != null) {
                    //Check if the item in the first slot is a diamond sword
                    ItemStack secondItem = client.player.currentScreenHandler.getSlot(1).getStack();
                    if (secondItem.getItem() == Items.DIAMOND_SWORD) {
                        ItemStack answerStack = null;

                        //Check if the diamond sword has a damage value matching a puzzle icon, then display the correct answer if valid
                        if (secondItem.getDamage() == 1513) {
                            answerStack = findAnswer(1516, client);
                        } else if (secondItem.getDamage() == 1519) {
                            answerStack = findAnswer(1523, client);
                        } else if (secondItem.getDamage() == 1525) {
                            answerStack = findAnswer(1530, client);
                        } else if (secondItem.getDamage() == 1531) {
                            answerStack = findAnswer(1536, client);
                        }

                        if (answerStack != null) {
                            if (!answerStack.hasEnchantments())
                                answerStack.addEnchantment(Enchantments.SHARPNESS, 1);
                        }
                    }
                }
            }

            //If they enter barrows add icons
            if(runOnce & isInBarrows){
                addIndicators();
                runOnce = false;
            }
            //If they leave remove icons
            if(!isInBarrows && !runOnce){
                removeIndicators();
                runOnce = true;
            }

            //If they arent underground reset the marked doors
            if(!isInBarrowsUnderground){
                if(trackedDoors.size() == 0) return;

                for (int i = trackedDoors.size() - 1; i >= 0; i--) {
                    BlockPos pos = trackedDoors.get(i);

                    PersistentOutlineRenderer.getInstance().removePos(pos);
                    trackedDoors.remove(i);
                }
            }
        }
    }

    static ItemStack findAnswer(int damage, MinecraftClient client){
        ScreenHandler screen = client.player.currentScreenHandler;

        for (int i = 0; i < screen.getStacks().size(); i++) {
            ItemStack thisStack = screen.getSlot(i).getStack();

            if(thisStack.getDamage() == damage)
                return thisStack;
        }

        return null;
    }

    private static void onUseDoor(PlayerEntity player, BlockPos pos){
        assert MinecraftClient.getInstance().player != null;

        if(isInBarrowsUnderground){
            if(!trackedDoors.contains(pos)) {
                trackedDoors.add(pos);
                PersistentOutlineRenderer.getInstance().addPos(pos);
            }
        }
    }

    private static void addIndicators(){
        MinecraftClient client = MinecraftClient.getInstance();
        assert client.world != null;

        //Icons
        ItemStack protectMagic = new ItemStack(Items.STONE_SHOVEL, 1);
        protectMagic.setDamage(104);
        ItemStack protectRanged = new ItemStack(Items.STONE_SHOVEL, 1);
        protectRanged.setDamage(105);
        ItemStack protectMelee = new ItemStack(Items.STONE_SHOVEL, 1);
        protectMelee.setDamage(106);

        //Armor Stands
        ahrimIndicator = createArmorStand(client, new Vec3d(1651, 75, -12), protectMagic);
        karilIndicator = createArmorStand(client, new Vec3d(1652, 75, 26), protectRanged);
        dharokIndicator = createArmorStand(client, new Vec3d(1680, 75, -42), protectMelee);
        guthanIndicator = createArmorStand(client, new Vec3d(1688, 75, 4), protectMelee);
        toragIndicator = createArmorStand(client, new Vec3d(1617, 75, 5), protectMelee);
        veracIndicator = createArmorStand(client, new Vec3d(1625, 75, -40), protectMelee);
    }

    private static void removeIndicators(){
        if(ahrimIndicator != null)
            ahrimIndicator.remove(Entity.RemovalReason.UNLOADED_WITH_PLAYER);
        if(karilIndicator != null)
            karilIndicator.remove(Entity.RemovalReason.UNLOADED_WITH_PLAYER);
        if(dharokIndicator != null)
            dharokIndicator.remove(Entity.RemovalReason.UNLOADED_WITH_PLAYER);
        if(guthanIndicator != null)
            guthanIndicator.remove(Entity.RemovalReason.UNLOADED_WITH_PLAYER);
        if(toragIndicator != null)
            toragIndicator.remove(Entity.RemovalReason.UNLOADED_WITH_PLAYER);
        if(veracIndicator != null)
            veracIndicator.remove(Entity.RemovalReason.UNLOADED_WITH_PLAYER);
    }

    private static ArmorStandEntity createArmorStand(MinecraftClient client, Vec3d position, ItemStack item){
        ArmorStandEntity entity = new ArmorStandEntity(client.world, position.x, position.y, position.z);
        entity.setNoGravity(true);
        entity.equipStack(EquipmentSlot.HEAD, item);
        client.world.addEntity(42069, entity);

        return entity;
    }
}
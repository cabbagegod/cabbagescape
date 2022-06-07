package com.cabbagegod.cabbagescape.client.grounditems;

import com.cabbagegod.cabbagescape.client.CabbageScapeClient;
import com.cabbagegod.cabbagescape.client.blockoutline.PersistentOutlineRenderer;
import com.cabbagegod.cabbagescape.client.blockoutline.Vector3f;
import com.cabbagegod.cabbagescape.data.GroundItemSettings;
import com.cabbagegod.cabbagescape.data.Settings;
import com.cabbagegod.cabbagescape.util.ParticleUtil;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientEntityEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GroundItemsManager {
    //Ground items
    private static List<Entity> armorStands = new ArrayList<Entity>();

    public static void register(){
        ClientEntityEvents.ENTITY_LOAD.register(GroundItemsManager::OnArmorStandLoad);
        ClientEntityEvents.ENTITY_UNLOAD.register(GroundItemsManager::OnArmorStandUnload);
        ClientTickEvents.END_CLIENT_TICK.register(GroundItemsManager::OnEndTick);
    }

    public static void RemoveArmorStand(Entity e){
        armorStands.remove(e);
    }

    public static void AddArmorStand(Entity e){
        if(EntityIsArmorStand(e)){
            armorStands.add(e);
        }
    }

    private static boolean EntityIsArmorStand(Entity e){
        return e.getType() == EntityType.ARMOR_STAND;
    }

    public static void OnArmorStandLoad(Entity entity, ClientWorld world){
        if(entity.getType() == EntityType.ARMOR_STAND){
            AddArmorStand(entity);
        }
    }

    public static void OnArmorStandUnload(Entity entity, ClientWorld world){
        if(entity.getType() == EntityType.ARMOR_STAND){
            RemoveArmorStand(entity);
            BlockPos itemPos = new BlockPos(entity.getBlockX(), entity.getBlockY(), entity.getBlockZ()).add(0,2,1);

            PersistentOutlineRenderer.getInstance().removePos(itemPos);
        }
    }

    public static void OnEndTick(MinecraftClient client){
        if(client.world != null) {
            CheckArmorStands(client);
        }
    }

    //This disaster logic is used to find/handle ground items
    private static void CheckArmorStands(MinecraftClient client){
        for (Entity entity: armorStands) {
            for (ItemStack itemStack : entity.getArmorItems()){
                if(itemStack == null || itemStack.isEmpty() || itemStack.getItem() == Items.AIR)
                    continue;

                BlockPos itemPos = new BlockPos(entity.getBlockX(), entity.getBlockY(), entity.getBlockZ()).add(0,2,1);
                Settings settings = CabbageScapeClient.settings;

                //If the user's list of tags contains the name of the item that the armor stand is holding
                if(settings.groundItemSettings.searchTags.contains(Formatting.strip(itemStack.getName().getString().toLowerCase()))){
                    DisplayGroundItem(itemPos, settings.groundItemSettings.volume, false);
                } else {
                    boolean playSound = true;
                    for(String tag : settings.groundItemSettings.containsTags){
                        if(Objects.requireNonNull(Formatting.strip(itemStack.getName().getString().toLowerCase())).contains(tag)){
                            DisplayGroundItem(itemPos, settings.groundItemSettings.volume, playSound);
                            playSound = false;
                            break;
                        }
                    }
                }
            }
        }
    }

    private static void DisplayGroundItem(BlockPos itemPos, float volume, boolean playSound){
        GroundItemSettings settings = CabbageScapeClient.settings.groundItemSettings;

        //Get the center of the block that the item is on
        Vector3f itemPosCenter = new Vector3f(itemPos.getX() + .5f, itemPos.getY(), itemPos.getZ() + .5f);

        assert MinecraftClient.getInstance().player != null;

        if(playSound)
            MinecraftClient.getInstance().player.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, volume, 1);

        if(settings.spiralEnabled) {
            ParticleUtil.CreateSpiralParticle(itemPosCenter, new Color((int) settings.itemRed, (int) settings.itemGreen, (int) settings.itemBlue), (int) settings.particleCount);
        }
        if(settings.blockHighlightEnabled) {
            if (!PersistentOutlineRenderer.getInstance().contains(itemPos))
                PersistentOutlineRenderer.getInstance().addPos(itemPos);
        }
    }
}

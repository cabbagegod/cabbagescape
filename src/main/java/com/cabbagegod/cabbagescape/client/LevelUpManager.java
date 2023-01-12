package com.cabbagegod.cabbagescape.client;

import com.cabbagegod.cabbagescape.data.Skill;

import java.util.ArrayList;
import java.util.List;

public class LevelUpManager {
    private static String lastLevelUpMessage = "";
    private static List<Skill> skills;

    //These are used for checking if the player has received a level up
    private static String lastSkill;
    private static int lastLevel;

    //Called from ChatHudMixin every time a new chat message has been received by the client
    public static void receiveMessage(String message){
        if(skills == null)
            populateSkills();

        String lowerMessage = message.toLowerCase();

        //Check if the received message is a "level up" notification
        //Second check ensures it's not coming from a player typing the message..
        if(lowerMessage.contains("level is now") && !lowerMessage.contains("[")){
            Skill currentSkill = checkStringForSkill(lowerMessage);
            if(currentSkill != null){
                int currentLevel = Integer.parseInt(lowerMessage.replaceAll("[^0-9]", ""));

                //Make sure the message is not for the same level up as the last
                if(!currentSkill.name.equals(lastSkill) || lastLevel != currentLevel){
                    lastSkill = currentSkill.name;
                    lastLevel = currentLevel;

                    CabbageScapeClient.takeScreenshotAfterDelayOnMainThread(300);
                }
            }
        }
    }

    private static void populateSkills(){
        if(skills != null)
            return;

        skills = new ArrayList<>();

        skills.add(new Skill("attack"));
        skills.add(new Skill("strength"));
        skills.add(new Skill("defence"));
        skills.add(new Skill("ranged"));
        skills.add(new Skill("prayer"));
        skills.add(new Skill("magic"));
        skills.add(new Skill("runecrafting"));
        skills.add(new Skill("construction"));
        skills.add(new Skill("hitpoints"));
        skills.add(new Skill("agility"));
        skills.add(new Skill("herblore"));
        skills.add(new Skill("thieving"));
        skills.add(new Skill("crafting"));
        skills.add(new Skill("fletching"));
        skills.add(new Skill("slayer"));
        skills.add(new Skill("hunter"));
        skills.add(new Skill("mining"));
        skills.add(new Skill("smithing"));
        skills.add(new Skill("fishing"));
        skills.add(new Skill("cooking"));
        skills.add(new Skill("firemaking"));
        skills.add(new Skill("woodcutting"));
        skills.add(new Skill("farming"));
    }

    private static Skill checkStringForSkill(String string){
        for(Skill skill : skills){
            if(string.contains(skill.name)){
                return skill;
            }
        }

        return null;
    }
}

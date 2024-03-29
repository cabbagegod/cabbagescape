package com.cabbagegod.cabbagescape;

import com.cabbagegod.cabbagescape.client.cluetracker.ClueTracker;
import com.cabbagegod.cabbagescape.events.EventHandler;
import jdk.jfr.Event;
import net.fabricmc.api.ModInitializer;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Set;

public class Main implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger("cabbagescape");

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		LOGGER.info("Loaded CabbageScape");
	}
}

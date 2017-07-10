package me.axieum.mcmod.projectradiation;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

public class Config
{
	
	// Config options
	// --
	
	// Load
	public static void load()
	{
		// [Create] and load the file
		Configuration config = new Configuration(new File(References.CONFIG_FILE));
		config.load();
		
		// Categories
		config.addCustomCategoryComment(References.CONFIG_CATEGORY_ENABLES, "Enable/Disable an aspect of the mod.");

		// Initialise config options
		// --
		
		// Save the config ready for use
		config.save();
	}
	
}

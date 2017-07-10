package me.axieum.mcmod.projectradiation;

import java.util.ArrayList;

import me.axieum.mcmod.projectradiation.event.EventAchievementHandler;
import net.minecraft.stats.Achievement;
import net.minecraftforge.common.AchievementPage;
import net.minecraftforge.common.MinecraftForge;

public class Achievements
{
	
	// Instantiating
	private static ArrayList<Achievement> list = new ArrayList<Achievement>();
	private static AchievementPage page;
	
	// Achievements
	// --
	
	public static void init()
	{
		initAchievements();
		initPages();
		MinecraftForge.EVENT_BUS.register(new EventAchievementHandler());
	}
	
	private static void initAchievements()
	{
		//
	}
	
	private static void initPages()
	{
		if (list.size() > 0)
		{
			page = new AchievementPage("Project Radiation", list.toArray(new Achievement[list.size()]));
			AchievementPage.registerAchievementPage(page);
			list.clear();
		}
	}
	
	public static AchievementPage getAchievementPage()
	{
		return page;
	}
	
}

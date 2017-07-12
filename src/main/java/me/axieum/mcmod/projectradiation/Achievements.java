package me.axieum.mcmod.projectradiation;

import java.util.ArrayList;

import me.axieum.mcmod.projectradiation.event.EventAchievementHandler;
import micdoodle8.mods.galacticraft.core.GCItems;
import net.minecraft.stats.Achievement;
import net.minecraftforge.common.AchievementPage;
import net.minecraftforge.common.MinecraftForge;

public class Achievements
{
	
	// Instantiating
	private static ArrayList<Achievement> list = new ArrayList<Achievement>();
	private static AchievementPage page;
	
	// Achievements
	public static Achievement achievement_landing;
	public static Achievement achievement_craft_oxygen_medium;
	public static Achievement achievement_craft_oxygen_heavy;
	
	public static void init()
	{
		initAchievements();
		initPages();
		MinecraftForge.EVENT_BUS.register(new EventAchievementHandler());
	}
	
	private static void initAchievements()
	{
		// Miscellaneous
		if (Config.ACHIEVEMENTS_LOGIN)
		{
			achievement_landing = new Achievement("achievement.landing", "landing", 0, 0, GCItems.oxTankLight, (Achievement) null).setSpecial().registerStat();
			list.add(achievement_landing);
		}
		
		// Crafting
		if (Config.ACHIEVEMENTS_LOGIN)
			achievement_craft_oxygen_medium = new Achievement("achievement.oxygen.medium", "oxygen.medium", 2, 0, GCItems.oxTankMedium, achievement_landing).registerStat();
		else
			achievement_craft_oxygen_medium = new Achievement("achievement.oxygen.medium", "oxygen.medium", 2, 0, GCItems.oxTankMedium, (Achievement) null).registerStat();
		list.add(achievement_craft_oxygen_medium);
		
		achievement_craft_oxygen_heavy = new Achievement("achievement.oxygen.heavy", "oxygen.heavy", 4, 0, GCItems.oxTankHeavy, achievement_craft_oxygen_medium).registerStat();
		list.add(achievement_craft_oxygen_heavy);
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

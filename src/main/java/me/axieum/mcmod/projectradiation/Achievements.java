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

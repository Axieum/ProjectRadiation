package me.axieum.mcmod.projectradiation.event;

import me.axieum.mcmod.projectradiation.Achievements;
import me.axieum.mcmod.projectradiation.Config;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;

public class EventAchievementHandler
{
	
	// Handle achievement awarding here.
	
	@SubscribeEvent
	public void onPlayerLogin(PlayerLoggedInEvent e)
	{
		if (Config.ACHIEVEMENTS_LOGIN)
			e.player.addStat(Achievements.achievement_landing);
	}
	
}

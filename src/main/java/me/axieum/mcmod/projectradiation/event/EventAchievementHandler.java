package me.axieum.mcmod.projectradiation.event;

import me.axieum.mcmod.projectradiation.Achievements;
import me.axieum.mcmod.projectradiation.Config;
import micdoodle8.mods.galacticraft.core.GCItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
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
	
	// Crafting achievements
	@SubscribeEvent
	public void onCraftItem(ItemCraftedEvent e)
	{
		// Oxygen Tank Medium
		if (e.crafting.getItem() == GCItems.oxTankMedium)
		{
			e.player.addStat(Achievements.achievement_craft_oxygen_medium);
			return;
		}
		
		// Oxygen Tank Heavy
		if (e.crafting.getItem() == GCItems.oxTankHeavy)
		{
			e.player.addStat(Achievements.achievement_craft_oxygen_heavy);
			return;
		}
	}
	
	// Pickup achievements in case they find the crafting item
	@SubscribeEvent
	public void onPickupItem(EntityItemPickupEvent e)
	{
		if (!(e.getEntity() instanceof EntityPlayer))
			return;
		
		// Oxygen Tank Medium
		if (e.getItem().getItem().getItem() == GCItems.oxTankMedium)
		{
			e.getEntityPlayer().addStat(Achievements.achievement_craft_oxygen_medium);
			return;
		}
		
		// Oxygen Tank Heavy
		if (e.getItem().getItem().getItem() == GCItems.oxTankHeavy)
		{
			e.getEntityPlayer().addStat(Achievements.achievement_craft_oxygen_heavy);
			return;
		}
	}
	
}

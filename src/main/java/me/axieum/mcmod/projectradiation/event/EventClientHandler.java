package me.axieum.mcmod.projectradiation.event;

import micdoodle8.mods.galacticraft.api.world.IGalacticraftWorldProvider;
import micdoodle8.mods.galacticraft.core.client.gui.overlay.OverlayOxygenTanks;
import micdoodle8.mods.galacticraft.core.client.gui.overlay.OverlayOxygenWarning;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStatsClient;
import micdoodle8.mods.galacticraft.core.tick.TickHandlerClient;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.OxygenUtil;
import micdoodle8.mods.galacticraft.core.util.PlayerUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent;

public class EventClientHandler
{
	
	@SubscribeEvent
	public void onRenderTick(RenderTickEvent e)
	{
		final Minecraft minecraft = FMLClientHandler.instance().getClient();
        final EntityPlayerSP player = minecraft.player;
        final EntityPlayerSP playerBaseClient = PlayerUtil.getPlayerBaseClientFromPlayer(player, false);
        if (player == null || playerBaseClient == null)
        {
            return;
        }
        
        GCPlayerStatsClient stats = GCPlayerStatsClient.get(playerBaseClient);
        
        if (e.phase == Phase.END)
        {
			if (!(playerBaseClient.world.provider instanceof IGalacticraftWorldProvider) && OxygenUtil.shouldDisplayTankGui(minecraft.currentScreen) && !playerBaseClient.isSpectator())
	        {				
	            int var6 = (TickHandlerClient.airRemaining - 90) * -1;
	
	            if (TickHandlerClient.airRemaining <= 0 || TickHandlerClient.airRemaining > 90)
	            {
	                var6 = 90;
	            }
	            
	            int var7 = (TickHandlerClient.airRemaining2 - 90) * -1;
	
	            if (TickHandlerClient.airRemaining2 <= 0 || TickHandlerClient.airRemaining2 > 90)
	            {
	                var7 = 90;
	            }
	            
	            int thermalLevel = stats.getThermalLevel() + 22;
	            
		        // Prevent O2 overlay when in creative mode or without oxygen tanks equipped.
	            if (!playerBaseClient.capabilities.isCreativeMode)
	            	OverlayOxygenTanks.renderOxygenTankIndicator(thermalLevel, var6, var7, !ConfigManagerCore.oxygenIndicatorLeft, !ConfigManagerCore.oxygenIndicatorBottom, Math.abs(thermalLevel - 22) >= 10 && !stats.isThermalLevelNormalising());
	        }
			
	        if (playerBaseClient != null && !stats.isOxygenSetupValid() && !(playerBaseClient.world.provider instanceof IGalacticraftWorldProvider) && minecraft.currentScreen == null && !minecraft.gameSettings.hideGUI && !playerBaseClient.capabilities.isCreativeMode && !playerBaseClient.isSpectator())
	        {
	            OverlayOxygenWarning.renderOxygenWarningOverlay();
	        }
        }
	}
	
}

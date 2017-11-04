package me.axieum.mcmod.projectradiation.event;

import micdoodle8.mods.galacticraft.api.event.oxygen.GCCoreOxygenSuffocationEvent;
import micdoodle8.mods.galacticraft.api.prefab.entity.EntityAutoRocket;
import micdoodle8.mods.galacticraft.api.world.IGalacticraftWorldProvider;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.entities.EntityCelestialFake;
import micdoodle8.mods.galacticraft.core.entities.EntityLanderBase;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import micdoodle8.mods.galacticraft.core.util.CompatibilityManager;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.DamageSourceGC;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.util.OxygenUtil;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventServerHandler
{
	@SubscribeEvent
	public void onPlayerUpdate(LivingEvent.LivingUpdateEvent e)
	{
		final EntityLivingBase entityLiving = e.getEntityLiving();
		
        if (!(entityLiving instanceof EntityPlayerMP))
            return;
        
        final EntityPlayerMP player = (EntityPlayerMP) entityLiving;
		
		// Only update oxygen if not in a Galacticraft dimension as GC will handle its own
		if (!(player.world.provider instanceof IGalacticraftWorldProvider))
		{
	        final int tick = player.ticksExisted - 1;
	        GCPlayerStats stats = GCPlayerStats.get(player);
	        
            if (tick % 40 == 0)
            {
                this.sendAirRemainingPacket(player, stats);
            }
	        
	        updateOxygen(player, stats);
	        
	        if (stats.isOxygenSetupValid() != stats.isLastOxygenSetupValid() || tick % 100 == 0)
	        {
	            GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_UPDATE_OXYGEN_VALIDITY, player.world.provider.getDimension(), new Object[] { stats.isOxygenSetupValid() }), player);
	        }
		}
	}
	
	private void updateOxygen(EntityPlayerMP player, GCPlayerStats stats)
	{
		if (!player.capabilities.isCreativeMode && !(player.getRidingEntity() instanceof EntityLanderBase) && !(player.getRidingEntity() instanceof EntityAutoRocket) && !(player.getRidingEntity() instanceof EntityCelestialFake) && !CompatibilityManager.isAndroid(player))
        {
            final ItemStack tankInSlot = stats.getExtendedInventory().getStackInSlot(2);
            final ItemStack tankInSlot2 = stats.getExtendedInventory().getStackInSlot(3);

            int drainSpacing = OxygenUtil.getDrainSpacing(tankInSlot, tankInSlot2);
            
            // Added to ensure Earth has less oxygen usage compared to other dimensions.
            if (drainSpacing > 0 && player.dimension == 0)
            	drainSpacing = 9;

            if (tankInSlot.isEmpty())
            {
                stats.setAirRemaining(0);
            }
            else
            {
                stats.setAirRemaining(tankInSlot.getMaxDamage() - tankInSlot.getItemDamage());
            }

            if (tankInSlot2.isEmpty())
            {
                stats.setAirRemaining2(0);
            }
            else
            {
                stats.setAirRemaining2(tankInSlot2.getMaxDamage() - tankInSlot2.getItemDamage());
            }

            if (drainSpacing > 0)
            {
                if ((player.ticksExisted - 1) % drainSpacing == 0 && !OxygenUtil.isAABBInBreathableAirBlock(player) && !stats.isUsingPlanetSelectionGui())
                {
                    int toTake = 1;
                    //Take 1 oxygen from Tank 1
                    if (stats.getAirRemaining() > 0)
                    {
                        tankInSlot.damageItem(1, player);
                        stats.setAirRemaining(stats.getAirRemaining() - 1);
                        toTake = 0;
                    }

                    //Alternatively, take 1 oxygen from Tank 2
                    if (toTake > 0 && stats.getAirRemaining2() > 0)
                    {
                        tankInSlot2.damageItem(1, player);
                        stats.setAirRemaining2(stats.getAirRemaining2() - 1);
                        toTake = 0;
                    }
                }
            }
            else
            {
                if ((player.ticksExisted - 1) % 60 == 0)
                {
                    if (OxygenUtil.isAABBInBreathableAirBlock(player))
                    {
                        if (stats.getAirRemaining() < 90 && !tankInSlot.isEmpty())
                        {
                            stats.setAirRemaining(Math.min(stats.getAirRemaining() + 1, tankInSlot.getMaxDamage() - tankInSlot.getItemDamage()));
                        }

                        if (stats.getAirRemaining2() < 90 && !tankInSlot2.isEmpty())
                        {
                            stats.setAirRemaining2(Math.min(stats.getAirRemaining2() + 1, tankInSlot2.getMaxDamage() - tankInSlot2.getItemDamage()));
                        }
                    }
                    else
                    {
                        if (stats.getAirRemaining() > 0)
                        {
                            stats.setAirRemaining(stats.getAirRemaining() - 1);
                        }

                        if (stats.getAirRemaining2() > 0)
                        {
                            stats.setAirRemaining2(stats.getAirRemaining2() - 1);
                        }
                    }
                }
            }

            final boolean airEmpty = stats.getAirRemaining() <= 0 && stats.getAirRemaining2() <= 0;

            if (player.isOnLadder())
            {
                stats.setOxygenSetupValid(stats.isLastOxygenSetupValid());
            }
            else
            {
                stats.setOxygenSetupValid(!((!OxygenUtil.hasValidOxygenSetup(player) || airEmpty) && !OxygenUtil.isAABBInBreathableAirBlock(player)));
            }

            if (!player.world.isRemote && player.isEntityAlive())
            {
            	if (!stats.isOxygenSetupValid())
            	{
        			GCCoreOxygenSuffocationEvent suffocationEvent = new GCCoreOxygenSuffocationEvent.Pre(player);
        			MinecraftForge.EVENT_BUS.post(suffocationEvent);

        			if (!suffocationEvent.isCanceled())
        			{
                		if (stats.getDamageCounter() == 0)
                		{
                			stats.setDamageCounter(ConfigManagerCore.suffocationCooldown);

            				player.attackEntityFrom(DamageSourceGC.oxygenSuffocation, ConfigManagerCore.suffocationDamage * (2 + stats.getIncrementalDamage()) / 2);
            				if (ConfigManagerCore.hardMode) stats.setIncrementalDamage(stats.getIncrementalDamage() + 1);

            				// Radiation effects.
            				if (player.dimension == 0)
            				{
	            				player.addPotionEffect(new PotionEffect(MobEffects.POISON, 120, 0));
	            				player.addPotionEffect(new PotionEffect(MobEffects.HUNGER, 20, 0));
            				}
            				
            				GCCoreOxygenSuffocationEvent suffocationEventPost = new GCCoreOxygenSuffocationEvent.Post(player);
            				MinecraftForge.EVENT_BUS.post(suffocationEventPost);
                		}
        			}
        			else
        				stats.setOxygenSetupValid(true);
            	}
        		else
        			stats.setIncrementalDamage(0);
            }
        }
        else if ((player.ticksExisted - 1) % 20 == 0 && !player.capabilities.isCreativeMode && stats.getAirRemaining() < 90)
        {
            stats.setAirRemaining(stats.getAirRemaining() + 1);
            stats.setAirRemaining2(stats.getAirRemaining2() + 1);
        }
        else if (player.capabilities.isCreativeMode)
        {
            stats.setAirRemaining(90);
            stats.setAirRemaining2(90);
        }
        else
        {
            stats.setOxygenSetupValid(true);
        }
	}
	
	protected void sendAirRemainingPacket(EntityPlayerMP player, GCPlayerStats stats)
    {
        final float f1 = stats.getTankInSlot1().isEmpty() ? 0.0F : stats.getTankInSlot1().getMaxDamage() / 90.0F;
        final float f2 = stats.getTankInSlot2().isEmpty() ? 0.0F : stats.getTankInSlot2().getMaxDamage() / 90.0F;
        GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_AIR_REMAINING, GCCoreUtil.getDimensionID(player.world), new Object[] { MathHelper.floor(stats.getAirRemaining() / f1), MathHelper.floor(stats.getAirRemaining2() / f2), player.getGameProfile().getName() }), player);
    }
}

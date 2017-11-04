package me.axieum.mcmod.projectradiation.proxy;

import me.axieum.mcmod.projectradiation.event.EventServerHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy
{

	public void preInit(FMLPreInitializationEvent event)
    {
		//
    }
	
    public void init(FMLInitializationEvent event)
    {
        //
    }
    
    public void postInit(FMLPostInitializationEvent event)
    {
    	System.out.println("Registering server event handler!");
    	MinecraftForge.EVENT_BUS.register(new EventServerHandler());
    }
	
}

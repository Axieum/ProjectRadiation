package me.axieum.mcmod.projectradiation;

import me.axieum.mcmod.projectradiation.proxy.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = References.MOD_ID, name = References.MOD_NAME, version = References.MOD_VERSION, acceptedMinecraftVersions = References.MC_VERSION, dependencies = References.DEPENDENCIES)
public class ProjectRadiation
{
	
	@SidedProxy(clientSide = References.PROXY_CLIENT, serverSide = References.PROXY_SERVER)
	public static CommonProxy proxy;
	
	@Instance(References.MOD_ID)
	public static ProjectRadiation instance;
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
		// Initialise and load the config
    	Config.load();
    	
    	proxy.preInit(event);
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
    	proxy.init(event);
    }
    
    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
    	// Initialise and register achievements
    	if (Config.ACHIEVEMENTS)
    		Achievements.init();
    	
    	proxy.postInit(event);
    }
    
}

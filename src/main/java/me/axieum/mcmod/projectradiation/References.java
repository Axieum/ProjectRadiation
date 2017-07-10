package me.axieum.mcmod.projectradiation;

public class References
{
	
	// Mod information.
	public static final String MOD_ID = "projectradiation";
	public static final String MOD_NAME = "Project Radiation";
	public static final String MC_VERSION = "[1.11.2]";
	
	public static final int MOD_MAJVERSION = 1;
	public static final int MOD_MINVERSION = 0;
	public static final int MOD_BUILDVERSION = 0;
	public static final String MOD_VERSION = MOD_MAJVERSION + "." + MOD_MINVERSION + "." + MOD_BUILDVERSION;
	
	public static final String DEPENDENCIES_FORGE = "required-after:forge@[13.20.1.2393,); ";
	public static final String DEPENDENCIES_MODS = "required-after:galacticraftcore; required-after:galacticraftplanets; ";
	public static final String DEPENDENCIES = DEPENDENCIES_FORGE + DEPENDENCIES_MODS;

	public static final String PREFIX = MOD_ID + ".";
	public static final String ASSET_PREFIX = MOD_ID;
	
	public static final String PROXY_CLIENT = "me.axieum.mcmod.projectradiation.proxy.ClientProxy";
	public static final String PROXY_SERVER = "me.axieum.mcmod.projectradiation.proxy.ServerProxy";
	
}

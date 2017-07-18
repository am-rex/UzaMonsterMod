package uzammod.common;

import net.minecraft.world.World;

public class CommonProxy
{
	public void registerRender(){}
	public String SideStr(){ return "Server"; }
	public void spawnParticle(World world, String name, double px, double py, double pz, double mx, double my, double mz, float a, float r, float g, float b){}
	public void spawnParticle(World world, String name, double px, double py, double pz, double mx, double my, double mz){}
}

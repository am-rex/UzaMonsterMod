package uzammod;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public class Lib
{
	public static boolean isEntityCreative(Entity entity)
	{
		return (entity instanceof EntityPlayer) && ((EntityPlayer) entity).capabilities.isCreativeMode;
	}

	public static void log(String format, Object... args)
	{
		if(ModSettings.printLog)
		{
			System.out.printf("[" + UzaMonsterMod.MODID + "][" + UzaMonsterMod.proxy.SideStr() + "]" + format + "\n", args);
		}
	}

	public static void errLog(String format, Object... args)
	{
		System.out.printf("[" + UzaMonsterMod.MODID + "][" + UzaMonsterMod.proxy.SideStr() + "]" + format + "\n", args);
	}

	public static void excLog(Exception e)
	{
		errLog("Exception!");
		e.printStackTrace();
	}

	public static double getRadian(double x, double y, double x2, double y2)
	{
		return Math.atan2(y2 - y, x2 - x);
	}

	public static double rad2deg(double radian)
	{
		return radian * 180d / Math.PI;
	}
}

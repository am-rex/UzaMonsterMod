package uzammod;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

public class ModSettings
{
	private static Configuration config;
	private static String configPath;

	public static boolean printLog;

	private ModSettings(){}

	public static void init(String name)
	{
		File file = new File(name);
		config = new Configuration(file);
		setConfigPath(file.toString());
		load();
	}

	public static void load()
	{
		try
		{
			config.load();
			ModSettings.printLog = config.getBoolean("PrintLog", Configuration.CATEGORY_GENERAL, false, "");
		}
		finally
		{
			save();
			Lib.log("Load " + getConfigPath());
		}
	}

	public static void save()
	{
		config.save();
	}


	public static boolean isPrintLog()
	{
		return printLog;
	}

	public static String getConfigPath()
	{
		return configPath;
	}

	public static void setConfigPath(String configPath)
	{
		ModSettings.configPath = configPath;
	}
}

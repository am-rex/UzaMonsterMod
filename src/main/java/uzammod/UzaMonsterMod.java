package uzammod;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.EntityRegistry;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureType;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import uzammod.common.CommonProxy;
import uzammod.common.EntityBreaker;
import uzammod.common.EntityKiller;
import uzammod.common.EntitySmallSpider;

@Mod(modid = UzaMonsterMod.MODID, version = UzaMonsterMod.VERSION)
public class UzaMonsterMod
{
	public static final String MODID = "uzammod";
	public static final String VERSION = "1.0";

	@SidedProxy(clientSide = "uzammod.client.ClientProxy", serverSide = "uzammod.common.CommonProxy")
	public static CommonProxy proxy;

	public static Type[] spawnBiomeTypes1 = new Type[]{
			Type.HOT,
			Type.COLD,
			Type.SPARSE,
			Type.DENSE,
			Type.WET,
			Type.DRY,
			Type.SAVANNA,
			Type.CONIFEROUS,
			Type.JUNGLE,
			Type.SPOOKY,
			Type.DEAD,
			Type.LUSH,
			Type.MAGICAL,
			Type.OCEAN,
			Type.RIVER,
			Type.MESA,
			Type.FOREST,
			Type.PLAINS,
			Type.MOUNTAIN,
			Type.HILLS,
			Type.SWAMP,
			Type.SANDY,
			Type.SNOWY,
			Type.WASTELAND,
			Type.BEACH
	};

	public static Type[] spawnBiomeTypes2 = new Type[]{
			Type.NETHER,
			Type.END
	};


	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		registerModEntity(EntityBreaker.class,		"breaker",		0x5879, 0x404000, 0x808000, 80, 4, 4);
		registerModEntity(EntityKiller.class,		"killer",		0x587A, 0x800000, 0xFF0000, 80, 4, 4);
		registerModEntity(EntitySmallSpider.class,	"smallspider",	0x587B, 0x202020, 0xFF0000, 80, 4, 4);

		proxy.registerRender();
	}

	public void registerModEntity(Class <? extends EntityLiving > entityClass, String name, int id, int fg, int bg, int weightedProb, int min, int max)
	{
		EntityRegistry.registerModEntity(entityClass, name, id, this, 80, 3, true);

		for(Type type : spawnBiomeTypes1)
		{
			EntityRegistry.addSpawn(entityClass, weightedProb, min, max, EnumCreatureType.monster, BiomeDictionary.getBiomesForType(type));
		}

		for(Type type : spawnBiomeTypes2)
		{
			EntityRegistry.addSpawn(entityClass, weightedProb / 8, min, max, EnumCreatureType.monster, BiomeDictionary.getBiomesForType(type));
		}

		EntityList.addMapping(entityClass, name, id, fg, bg);
	}

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
	}
}

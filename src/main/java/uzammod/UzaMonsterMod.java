package uzammod;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureType;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.common.MinecraftForge;
import uzammod.common.CommonProxy;
import uzammod.common.ItemReverseKnockbackBow;
import uzammod.common.entity.EntityBreaker;
import uzammod.common.entity.EntityFoolSkeleton;
import uzammod.common.entity.EntityFreshCow;
import uzammod.common.entity.EntityFreshCowHead;
import uzammod.common.entity.EntityGreenSpider;
import uzammod.common.entity.EntityItem2;
import uzammod.common.entity.EntityKiller;
import uzammod.common.entity.EntityRocketCreeper;
import uzammod.common.entity.EntitySmallSpider;
import uzammod.common.entity.projectile.EntityAcid;
import uzammod.common.entity.projectile.EntityPheromone;
import uzammod.common.entity.projectile.EntityReverseKnockbackArrow;
import uzammod.common.item.ItemModMonsterPlacer;
import uzammod.common.item.ItemPheromone;
import uzammod.common.item.ItemPotionHh2o;

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

	public static ItemModMonsterPlacer		itemSpawnEgg;
	public static ItemPheromone 			itemPheromone;
	public static ItemPotionHh2o			itemHh2o;
	public static ItemReverseKnockbackBow	itemRkBow;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		ModSettings.init("config/UzaMonsterMod.cfg");

		registerModEntity(EntityFreshCow.class,		"freshcow",		0, 0x804040, 0xF00016, 20, 2, 2, EnumCreatureType.creature);
		registerModEntity(EntityBreaker.class,		"breaker",		1, 0x404040, 0x008080, 10, 1, 1, EnumCreatureType.monster);
		registerModEntity(EntityKiller.class,		"killer",		2, 0x804000, 0xFF4000, 20, 2, 2, EnumCreatureType.monster);
		registerModEntity(EntitySmallSpider.class,	"smallspider",	3, 0x502020, 0xC0C0C0, 20, 2, 2, EnumCreatureType.monster);
		registerModEntity(EntityGreenSpider.class,	"greenspider",	4, 0x004000, 0x008000, 10, 2, 2, EnumCreatureType.monster);
		registerModEntity(EntityRocketCreeper.class,"rocketcreeper",5, 0xF0F0F0, 0xFF0000, 20, 2, 2, EnumCreatureType.monster);
		registerModEntity(EntityFoolSkeleton.class,	"foolskeleton", 6, 0xF0F000, 0xCCCCCC, 20, 4, 4, EnumCreatureType.monster);

		EntityRegistry.registerModEntity(EntityFreshCowHead.class, "freshcowhead", 50, this, 80, 5, true);

		EntityRegistry.registerModEntity(EntityAcid.class, 					"acid",			100, this, 200, 3, true);
		EntityRegistry.registerModEntity(EntityPheromone.class,				"pheromone",	101, this, 200, 3, true);
		EntityRegistry.registerModEntity(EntityReverseKnockbackArrow.class,	"rkarrow",		102, this, 200, 3, true);
		EntityRegistry.registerModEntity(EntityItem2.class,					"item2",		103, this, 200, 3, true);

		itemSpawnEgg = new ItemModMonsterPlacer();
		itemSpawnEgg.setUnlocalizedName( MODID + ":spawn_egg");
		itemSpawnEgg.setTextureName(MODID + ":spawn_egg");
		GameRegistry.registerItem(itemSpawnEgg, "spawn_egg");

		itemPheromone = new ItemPheromone();
		itemPheromone.setUnlocalizedName( MODID + ":pheromone");
		itemPheromone.setTextureName(MODID + ":pheromone");
		GameRegistry.registerItem(itemPheromone, "pheromone");

		itemHh2o = new ItemPotionHh2o();
		itemHh2o.setUnlocalizedName( MODID + ":hh2o");
		itemHh2o.setTextureName(MODID + ":hh2o");
		GameRegistry.registerItem(itemHh2o, "hh2o");

		itemRkBow = new ItemReverseKnockbackBow();
		itemRkBow.setUnlocalizedName( MODID + ":rk_bow");
		itemRkBow.setTextureName(MODID + ":rk_bow");
		GameRegistry.registerItem(itemRkBow, "rk_bow");

		proxy.registerRender();

		MinecraftForge.EVENT_BUS.register(new EventHook());
	}

	public void registerModEntity(Class <? extends EntityLiving > entityClass, String name, int id, int fg, int bg, int weightedProb, int min, int max, EnumCreatureType ... creatureType)
	{
		EntityRegistry.registerModEntity(entityClass, name, id, this, 80, 3, true);

		for(EnumCreatureType ct : creatureType)
		{
			for(Type type : spawnBiomeTypes1)
			{
				EntityRegistry.addSpawn(entityClass, weightedProb, min, max, ct, BiomeDictionary.getBiomesForType(type));
			}
			for(Type type : spawnBiomeTypes2)
			{
				EntityRegistry.addSpawn(entityClass, weightedProb / 8, min, max, ct, BiomeDictionary.getBiomesForType(type));
			}
		}

		ItemModMonsterPlacer.AddSpawn(entityClass, id, name, fg, bg);

		Lib.log("%s : %s : %d : FG=%06X BG=%06X : weight %d min %d max %d", entityClass.toString(), name, id, fg, bg, weightedProb, min, max);
	}

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
	}
}

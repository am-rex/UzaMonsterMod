package uzammod.common;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Facing;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import uzammod.UzaMonsterMod;

public class ItemModMonsterPlacer extends Item
{
    public static class EntityEggInfo
    {
    	public final Class <? extends Entity> entityClass;
        public final int spawnedID;
        public final int primaryColor;
        public final int secondaryColor;
        public final String name;

        public EntityEggInfo(Class<? extends Entity> entityClass, int id, String name, int primaryColor, int secondaryColor)
        {
            this.entityClass	= entityClass;
            this.spawnedID		= id;
            this.name			= name;
            this.primaryColor	= primaryColor;
            this.secondaryColor	= secondaryColor;
        }
    }

	public static String getStringFromID(int itemDamage)
	{
		EntityEggInfo oclass = entityEggs.get(itemDamage);
		return oclass != null ? oclass.name : null;
	}

	public static ItemStack getItemStackFromEntity(Entity entity)
	{
		Class cls = entity.getClass();
		for(int i : entityEggs.keySet())
		{
			EntityEggInfo oclass = entityEggs.get(i);
			if(oclass.entityClass == cls)
			{
				return new ItemStack(UzaMonsterMod.itemSpawnEgg, 1, i);
			}
		}
		return null;
	}

	public static Entity createEntityByID(int entityId, World world)
	{
		EntityEggInfo oclass = entityEggs.get(entityId);

		try
		{
			return (Entity)oclass.entityClass.getConstructor(new Class[] {World.class}).newInstance(new Object[] {world});
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public static HashMap<Integer, EntityEggInfo> entityEggs = new LinkedHashMap<Integer, EntityEggInfo>();

	@SideOnly(Side.CLIENT)
	private IIcon theIcon;

	public ItemModMonsterPlacer()
	{
		this.setHasSubtypes(true);
		this.setCreativeTab(CreativeTabs.tabMisc);
		this.setMaxDamage(0);
	}

	public static void AddSpawn(Class clazz, int id, String name, int primaryColor, int secondaryColor)
	{
		entityEggs.put(Integer.valueOf(id), new EntityEggInfo(clazz, id, name, primaryColor, secondaryColor));
	}

	public String getItemStackDisplayName(ItemStack itemStack)
	{
		String s = ("" + StatCollector.translateToLocal(this.getUnlocalizedName() + ".name")).trim();
		String s1 = getStringFromID(itemStack.getItemDamage());

		if (s1 != null)
		{
			s = s + " " + StatCollector.translateToLocal("entity." + s1 + ".name");
		}

		return s;
	}

	@SideOnly(Side.CLIENT)
	public int getColorFromItemStack(ItemStack itemStack, int layer)
	{
		EntityEggInfo entityegginfo = (EntityEggInfo) entityEggs.get(Integer.valueOf(itemStack.getItemDamage()));
		return entityegginfo != null ? (layer == 0 ? entityegginfo.primaryColor : entityegginfo.secondaryColor) : 16777215;
	}

	/**
	 * Callback for item usage. If the item does something special on right clicking, he will have one of those. Return
	 * True if something happen and false if it don't. This is for ITEMS, not BLOCKS
	 */
	public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world,
			int x, int y, int z, int side, float hitX, float hitY, float hitZ)
	{
		if (world.isRemote)
		{
			return true;
		}
		else
		{
			Block block = world.getBlock(x, y, z);
			x += Facing.offsetsXForSide[side];
			y += Facing.offsetsYForSide[side];
			z += Facing.offsetsZForSide[side];
			double d0 = 0.0D;

			if (side == 1 && block.getRenderType() == 11)
			{
				d0 = 0.5D;
			}

			Entity entity = spawnCreature(world, itemStack.getItemDamage(), (double) x + 0.5D, (double) y + d0, (double) z + 0.5D);

			if (entity != null)
			{
				if (entity instanceof EntityLivingBase && itemStack.hasDisplayName())
				{
					((EntityLiving) entity).setCustomNameTag(itemStack.getDisplayName());
				}

				if (!player.capabilities.isCreativeMode)
				{
					--itemStack.stackSize;
				}
			}

			return true;
		}
	}

	/**
	 * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
	 */
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player)
	{
		if (world.isRemote)
		{
			return itemStack;
		}
		else
		{
			MovingObjectPosition movingobjectposition = this.getMovingObjectPositionFromPlayer(world, player, true);

			if (movingobjectposition == null)
			{
				return itemStack;
			}
			else
			{
				if (movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
				{
					int i = movingobjectposition.blockX;
					int j = movingobjectposition.blockY;
					int k = movingobjectposition.blockZ;

					if (!world.canMineBlock(player, i, j, k))
					{
						return itemStack;
					}

					if (!player.canPlayerEdit(i, j, k, movingobjectposition.sideHit, itemStack))
					{
						return itemStack;
					}

					if (world.getBlock(i, j, k) instanceof BlockLiquid)
					{
						Entity entity = spawnCreature(world, itemStack.getItemDamage(), (double) i, (double) j,
								(double) k);

						if (entity != null)
						{
							if (entity instanceof EntityLivingBase && itemStack.hasDisplayName())
							{
								((EntityLiving) entity).setCustomNameTag(itemStack.getDisplayName());
							}

							if (!player.capabilities.isCreativeMode)
							{
								--itemStack.stackSize;
							}
						}
					}
				}

				return itemStack;
			}
		}
	}

	/**
	 * Spawns the creature specified by the egg's type in the location specified by the last three parameters.
	 * Parameters: world, entityID, x, y, z.
	 */
	public static Entity spawnCreature(World world, int entityId, double x, double y, double z)
	{
		if (!entityEggs.containsKey(Integer.valueOf(entityId)))
		{
			return null;
		}
		else
		{
			Entity entity = null;

			for (int j = 0; j < 1; ++j)
			{
				entity = createEntityByID(entityId, world);

				if (entity != null && entity instanceof EntityLivingBase)
				{
					EntityLiving entityliving = (EntityLiving) entity;
					entity.setLocationAndAngles(x, y, z, MathHelper.wrapAngleTo180_float(world.rand.nextFloat() * 360.0F), 0.0F);
					entityliving.rotationYawHead = entityliving.rotationYaw;
					entityliving.renderYawOffset = entityliving.rotationYaw;
					entityliving.onSpawnWithEgg((IEntityLivingData) null);
					world.spawnEntityInWorld(entity);
					entityliving.playLivingSound();
				}
			}

			return entity;
		}
	}

	@SideOnly(Side.CLIENT)
	public boolean requiresMultipleRenderPasses()
	{
		return true;
	}

	/**
	 * Gets an icon index based on an item's damage value and the given render pass
	 */
	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamageForRenderPass(int p_77618_1_, int p_77618_2_)
	{
		return p_77618_2_ > 0 ? this.theIcon : super.getIconFromDamageForRenderPass(p_77618_1_, p_77618_2_);
	}

	/**
	 * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
	 */
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs ct, List list)
	{
		Iterator iterator = entityEggs.values().iterator();

		while (iterator.hasNext())
		{
			EntityEggInfo entityegginfo = (EntityEggInfo) iterator.next();
			list.add(new ItemStack(item, 1, entityegginfo.spawnedID));
		}
	}

	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister icon)
	{
		super.registerIcons(icon);
		this.theIcon = icon.registerIcon(this.getIconString() + "_overlay");
	}
}

package uzammod.common.item;

import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import uzammod.UzaMonsterMod;
import uzammod.common.entity.EntityItem2;

public class ItemPotionHh2o extends Item
{
	public ItemPotionHh2o()
	{
		this.setMaxStackSize( 1 );
		this.setHasSubtypes( true );
		this.setMaxDamage( 0 );
		this.setCreativeTab( CreativeTabs.tabBrewing );
	}

	/**
	 * Returns a list of potion effects for the specified itemstack.
	 */
	public List getEffects(ItemStack p_77832_1_)
	{
		ArrayList<PotionEffect> list = new ArrayList<PotionEffect>();
		list.add( new PotionEffect( 123, 100 * 20 ) );
		return list;
	}

	public boolean hasCustomEntity(ItemStack stack)
	{
		return true;
	}

	public Entity createEntity(World world, Entity location, ItemStack itemstack)
	{
		EntityItem2 entityitem = new EntityItem2( world, location.posX, location.posY, location.posZ, new ItemStack( UzaMonsterMod.itemHh2o ) );
		entityitem.delayBeforeCanPickup = 10;
		entityitem.motionX = location.motionX;
		entityitem.motionY = location.motionY;
		entityitem.motionZ = location.motionZ;
		return entityitem;
	}

	public ItemStack onEaten(ItemStack p_77654_1_, World p_77654_2_, EntityPlayer p_77654_3_)
	{
		if( !p_77654_3_.capabilities.isCreativeMode )
		{
			--p_77654_1_.stackSize;
		}

		return p_77654_1_;
	}

	/**
	 * How long it takes to use or consume an item
	 */
	public int getMaxItemUseDuration(ItemStack p_77626_1_)
	{
		return 32 * 3;
	}

	/**
	 * returns the action that specifies what animation to play when the items is being used
	 */
	public EnumAction getItemUseAction(ItemStack p_77661_1_)
	{
		return EnumAction.drink;
	}

	/**
	 * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
	 */
	public ItemStack onItemRightClick(ItemStack p_77659_1_, World p_77659_2_, EntityPlayer p_77659_3_)
	{
		p_77659_3_.setItemInUse( p_77659_1_, this.getMaxItemUseDuration( p_77659_1_ ) );
		return p_77659_1_;
	}

	/**
	 * Callback for item usage. If the item does something special on right clicking, he will have one of those. Return
	 * True if something happen and false if it don't. This is for ITEMS, not BLOCKS
	 */
	public boolean onItemUse(ItemStack p_77648_1_, EntityPlayer p_77648_2_, World p_77648_3_, int p_77648_4_, int p_77648_5_, int p_77648_6_, int p_77648_7_, float p_77648_8_, float p_77648_9_, float p_77648_10_)
	{
		return false;
	}

	/**
	 * allows items to add custom lines of information to the mouseover description
	 */
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack itemStack, EntityPlayer p_77624_2_, List list, boolean p_77624_4_)
	{
		boolean efct = false;
		String info = "" + StatCollector.translateToLocal( this.getUnlocalizedNameInefficiently( itemStack ) + ".desc" );
		for( String s : info.split( "/" ) )
		{
			if( s.startsWith( "*" ) )
			{
				s = EnumChatFormatting.DARK_GRAY + s;
			}
			else if( efct )
			{
				s = EnumChatFormatting.BLUE + s;
			}
			else if( s.endsWith( ":" ) )
			{
				s = EnumChatFormatting.DARK_PURPLE + s;
				efct = true;
			}
			list.add( s );
		}
	}
}

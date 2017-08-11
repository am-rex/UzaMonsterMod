package uzammod.common.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import uzammod.common.entity.projectile.EntityPheromone;

public class ItemPheromone extends Item
{
	private static final String __OBFID = "CL_00000069";

	public ItemPheromone()
	{
		setMaxDamage( 300 );
		setMaxStackSize( 1 );
		this.setCreativeTab( CreativeTabs.tabMisc );
	}

	/**
	 * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
	 */
	public ItemStack onItemRightClick(ItemStack itemStack, World p_77659_2_, EntityPlayer p_77659_3_)
	{
		if(itemStack.getItemDamage() < itemStack.getMaxDamage())
		{
			if( !p_77659_3_.capabilities.isCreativeMode )
			{
				itemStack.setItemDamage( itemStack.getItemDamage() + 1 );
			}

			p_77659_2_.playSoundAtEntity( p_77659_3_, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F) );

			if( !p_77659_2_.isRemote )
			{
				p_77659_2_.spawnEntityInWorld( new EntityPheromone( p_77659_2_, p_77659_3_ ) );
			}
		}

		return itemStack;
	}
}

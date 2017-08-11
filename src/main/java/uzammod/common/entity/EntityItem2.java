package uzammod.common.entity;

import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.item.ItemExpireEvent;

public class EntityItem2 extends EntityItem
{
	private EntityPlayer closestPlayer;

	public EntityItem2(World p_i1710_1_)
	{
		super( p_i1710_1_ );
	}

	public EntityItem2(World p_i1710_1_, double p_i1710_2_, double p_i1710_4_, double p_i1710_6_, ItemStack p_i1710_8_)
	{
		super( p_i1710_1_, p_i1710_2_, p_i1710_4_, p_i1710_6_, p_i1710_8_ );
	}

	public void onUpdate()
	{
		double d0 = 16;
		if( this.closestPlayer == null || this.closestPlayer.getDistanceSqToEntity( this ) > d0 * d0 )
		{
			this.closestPlayer = this.worldObj.getClosestPlayerToEntity( this, d0 );
		}

		if( this.closestPlayer != null )
		{
			double d1 = (this.closestPlayer.posX - this.posX) / d0;
			double d2 = (this.closestPlayer.posY + (double) this.closestPlayer.getEyeHeight() - this.posY) / d0;
			double d3 = (this.closestPlayer.posZ - this.posZ) / d0;
			double d4 = Math.sqrt( d1 * d1 + d2 * d2 + d3 * d3 );
			double d5 = 1.0D - d4;

			if( d5 > 0.0D )
			{
				d5 *= d5;
				this.motionX += d1 / d4 * d5 * 0.1D;
				this.motionY += d2 / d4 * d5 * 0.1D;
				this.motionZ += d3 / d4 * d5 * 0.1D;
			}
		}

		ItemStack stack = this.getDataWatcher().getWatchableObjectItemStack( 10 );
		if( stack != null && stack.getItem() != null )
		{
			if( stack.getItem().onEntityItemUpdate( this ) )
			{
				return;
			}
		}

		if( this.getEntityItem() == null )
		{
			this.setDead();
		}
		else
		{
			super.onUpdate();

			if( this.delayBeforeCanPickup > 0 )
			{
				--this.delayBeforeCanPickup;
			}

			this.prevPosX = this.posX;
			this.prevPosY = this.posY;
			this.prevPosZ = this.posZ;
			this.motionY -= 0.03999999910593033D;
			this.noClip = this.func_145771_j( this.posX, (this.boundingBox.minY + this.boundingBox.maxY) / 2.0D, this.posZ );
			this.moveEntity( this.motionX, this.motionY, this.motionZ );
			boolean flag = (int) this.prevPosX != (int) this.posX || (int) this.prevPosY != (int) this.posY || (int) this.prevPosZ != (int) this.posZ;

			if( flag || this.ticksExisted % 25 == 0 )
			{
				if( this.worldObj.getBlock( MathHelper.floor_double( this.posX ), MathHelper.floor_double( this.posY ), MathHelper.floor_double( this.posZ ) ).getMaterial() == Material.lava )
				{
					this.motionY = 0.20000000298023224D;
					this.motionX = (double) ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F);
					this.motionZ = (double) ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F);
					this.playSound( "random.fizz", 0.4F, 2.0F + this.rand.nextFloat() * 0.4F );
				}
			}

			float f = 0.98F;

			if( this.onGround )
			{
				f = this.worldObj.getBlock( MathHelper.floor_double( this.posX ), MathHelper.floor_double( this.boundingBox.minY ) - 1, MathHelper.floor_double( this.posZ ) ).slipperiness * 0.98F;
			}

			//	this.motionX *= (double) f;
			this.motionY *= 0.9800000190734863D;
			//	this.motionZ *= (double) f;

			if( this.onGround )
			{
				this.motionY *= -0.5D;
			}

			++this.age;

			ItemStack item = getDataWatcher().getWatchableObjectItemStack( 10 );

			if( !this.worldObj.isRemote && this.age >= lifespan )
			{
				if( item != null )
				{
					ItemExpireEvent event = new ItemExpireEvent( this, (item.getItem() == null ? 6000 : item.getItem().getEntityLifespan( item, worldObj )) );
					if( MinecraftForge.EVENT_BUS.post( event ) )
					{
						lifespan += event.extraLife;
					}
					else
					{
						this.setDead();
					}
				}
				else
				{
					this.setDead();
				}
			}

			if( item != null && item.stackSize <= 0 )
			{
				this.setDead();
			}
		}

	}

}

package uzammod.common.entity;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityFreshCowHead extends EntityLivingBase
{
	public Entity entityBody = null;
	public String entityBodyUniqueID = "";

	public EntityFreshCowHead(World world)
	{
		super( world );
		this.setSize( 0.86F, 0.86F );
		this.yOffset = this.height / 2.0F;
	}

	public EntityFreshCowHead(World world, double x, double y, double z)
	{
		this( world );
		this.setPosition( x, y, z );
	}

	protected void entityInit()
	{
		super.entityInit();
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	public void readEntityFromNBT(NBTTagCompound nbt)
	{
		super.readEntityFromNBT( nbt );
		this.entityBodyUniqueID = nbt.getString( "BodyUniqueID" );
	}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	public void writeEntityToNBT(NBTTagCompound nbt)
	{
		super.writeEntityToNBT( nbt );
		if( this.entityBody != null )
		{
			nbt.setString( "BodyUniqueID", this.entityBody.getUniqueID().toString() );
		}
	}

	public void onUpdate()
	{
		if(!this.worldObj.isRemote  && this.ticksExisted > this.worldObj.difficultySetting.ordinal() * 30 * 20)
		{
			if(this.entityBody!=null)
			{
				attackEntityFrom( DamageSource.generic, 30000 );
				return;
			}
		}
		super.onUpdate();

		if( !this.worldObj.isRemote )
		{
			if( this.entityBody == null )
			{
				if( this.entityBodyUniqueID.isEmpty() || this.ticksExisted > 100 )
				{
					setDead();
				}
				else if( this.ticksExisted % 10 == 0 )
				{
					List list = this.worldObj.loadedEntityList;
					for( int i = 0; i < list.size(); ++i )
					{
						Entity entity = (Entity) list.get( i );
						if( entity instanceof EntityFreshCow && entity.getUniqueID().toString().equals( this.entityBodyUniqueID ) )
						{
							this.entityBody = entity;
							((EntityFreshCow) entity).head = this;
							((EntityFreshCow) entity).setNoHead( 1 );
							break;
						}
					}
				}
			}
			else
			{
				if( this.entityBody.isDead )
				{
					setDead();
				}
			}
		}
	}

	public void setDead()
	{
		super.setDead();

		for( int i = 0; i < 20; i++ )
		{
			this.worldObj.spawnParticle(
				"blockcrack_" + Block.getIdFromBlock( Blocks.redstone_block ) + "_0",
				this.posX,
				this.posY + 0.5,
				this.posZ,
				(rand.nextDouble() - 0.5) * 0.3,
				(rand.nextDouble() - 0.5) * 0.3,
				(rand.nextDouble() - 0.5) * 0.3 );
		}

		if( this.worldObj.isRemote )
		{
			this.worldObj.playSound(
				this.posX + 0.5D,
				this.posY + 0.5D,
				this.posZ + 0.5D,
				"mob.zombie.woodbreak",
				0.5F,
				(rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F,
				false );
		}
	}

	protected boolean canTriggerWalking()
	{
		return false;
	}

	/**
	 * Returns true if other Entities should be prevented from moving through this Entity.
	 */
	public boolean canBeCollidedWith()
	{
		return super.canBeCollidedWith();
	}

	/**
	 * Called when the entity is attacked.
	 */
	public boolean attackEntityFrom(DamageSource ds, float damage)
	{
		if( !this.worldObj.isRemote )
		{
			this.motionX = (rand.nextDouble() - 0.5) * 0.3;
			this.motionY = (rand.nextDouble()) * 0.5;
			this.motionZ = (rand.nextDouble() - 0.5) * 0.3;
		}

		if( this.entityBody != null && ((EntityFreshCow) this.entityBody).attackEntityFromHead( ds, damage ) )
		{
			this.worldObj.playSound(
				this.posX + 0.5D,
				this.posY + 0.5D,
				this.posZ + 0.5D,
				"mob.zombie.wood",
				0.2F,
				(rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F,
				false );
			return true;
		}
		return false;
	}

	/**
	 * Returns true if Entity argument is equal to this Entity
	 */
	public boolean _isEntityEqual(Entity entity)
	{
		return this == entity || this.entityBody == entity;
	}

	@Override
	public ItemStack getHeldItem()
	{
		return null;
	}

	@Override
	public ItemStack getEquipmentInSlot(int p_71124_1_)
	{
		return null;
	}

	@Override
	public void setCurrentItemOrArmor(int p_70062_1_, ItemStack p_70062_2_)
	{
	}

	static final ItemStack[] lastActiveItems = new ItemStack[] {};

	@Override
	public ItemStack[] getLastActiveItems()
	{
		return lastActiveItems;
	}
}

package uzammod.common.entity.projectile;

import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import uzammod.UzaMonsterMod;
import uzammod.common.entity.EntityGreenSpider;

public class EntityPheromone extends EntityThrowable
{
	/** The damage value of the thrown potion that this EntityAcid represents. */
	public EntityPheromone(World p_i1788_1_)
	{
		super( p_i1788_1_ );
	}

	public EntityPheromone(World p_i1790_1_, EntityLivingBase p_i1790_2_)
	{
		super( p_i1790_1_, p_i1790_2_ );
	}

	/**
	 * Gets the amount of gravity to apply to the thrown entity with each tick.
	 */
	protected float getGravityVelocity()
	{
		return 0.01F;
	}

	protected float func_70182_d()
	{
		return 1.00F;
	}

	protected float func_70183_g()
	{
		return -10.0F;
	}

	/**
	 * Called when this EntityThrowable hits a block or entity.
	 */
	protected void onImpact(MovingObjectPosition mop)
	{
		if( !this.worldObj.isRemote )
		{
			spawnSpiders();
			this.playSound( "game.tnt.primed", 1, 1.5F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4F );
			this.setDead();
		}
	}

	public void spawnSpiders()
	{
		List list = this.worldObj.getEntitiesWithinAABB( EntityGreenSpider.class, this.boundingBox.expand( 16, 16, 16 ) );
		int spawnNum = (3 + rand.nextInt( 2 ));
		if(list.size() > 10)
		{
			spawnNum -= list.size() - 10;
		}

		for( int i = 0; i < 50 && spawnNum > 0; i++ )
		{
			double px = this.posX + (rand.nextDouble() - 0.5) * 10;
			double py = this.posY + 4;
			double pz = this.posZ + (rand.nextDouble() - 0.5) * 10;
			for( int y = 0; y < 8; y++ )
			{
				if( this.worldObj.getBlock( (int) px, (int) (py - y), (int) pz ) == Blocks.air )
				{
					if( canSpawn( (int) px, (int) (py - y - 1), (int) pz ) )
					{
						EntityGreenSpider entity = new EntityGreenSpider( this.worldObj, true );
						entity.setPosition( px, py - y, pz );
						this.playSound( "mob.spider.say", 2.0F, 0.4F + rand.nextFloat() * 0.2F );
						//entity.setAttackTarget(this.getAttackTarget());
						entity.rotationYaw = entity.prevRotationYaw = rand.nextInt( 360 );
						this.worldObj.spawnEntityInWorld( entity );
						spawnNum--;
						break;
					}
				}
			}
		}
	}

	public boolean canSpawn(int px, int py, int pz)
	{
		for( int x = -1; x <= 1; x++ )
		{
			for( int z = -1; z <= 1; z++ )
			{
				if( !this.worldObj.getBlock( px + x, py, pz + z ).getMaterial().blocksMovement() )
				{
					return false;
				}
			}
		}
		return true;
	}

	public void setDead()
	{
		super.setDead();
		if( this.worldObj.isRemote )
		{
			for( int i = 0; i < 20; i++ )
			{
				double x = this.posX + (this.rand.nextDouble() - 0.5) * 3.0;
				double y = this.posY + (this.rand.nextDouble() - 0.5) * 3.0;
				double z = this.posZ + (this.rand.nextDouble() - 0.5) * 3.0;
				float color = this.rand.nextFloat() * 0.3F + 0.5F;
				UzaMonsterMod.proxy.spawnParticle(
					this.worldObj,
					"explode",
					x,
					y,
					z,
					(x - this.posX) * 0.02,
					(y - this.posY) * 0.02,
					(z - this.posZ) * 0.02,
					0.9F,
					color * 0.9F,
					color * 0.7F,
					color * 0.3F );
			}
		}
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	public void readEntityFromNBT(NBTTagCompound p_70037_1_)
	{
		super.readEntityFromNBT( p_70037_1_ );
	}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	public void writeEntityToNBT(NBTTagCompound p_70014_1_)
	{
		super.writeEntityToNBT( p_70014_1_ );
	}
}

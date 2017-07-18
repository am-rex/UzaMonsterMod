package uzammod.common.entity;

import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;
import uzammod.Lib;
import uzammod.UzaMonsterMod;

public class EntityAcid extends EntityThrowable
{
	/** The damage value of the thrown potion that this EntityAcid represents. */
	public EntityAcid(World p_i1788_1_)
	{
		super(p_i1788_1_);
	}

	public EntityAcid(World p_i1790_1_, EntityLivingBase p_i1790_2_)
	{
		super(p_i1790_1_, p_i1790_2_);
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
		return 0.00F;
	}

	protected float func_70183_g()
	{
		return -20.0F;
	}

	/**
	 * Called when this EntityThrowable hits a block or entity.
	 */
	protected void onImpact(MovingObjectPosition mop)
	{
		if( !this.worldObj.isRemote )
		{
			boolean dead = false;
			float range = 5;
			AxisAlignedBB axisalignedbb = this.boundingBox.expand(range, range, range);
			List list1 = this.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, axisalignedbb);

			Iterator iterator = list1.iterator();

			while (iterator.hasNext())
			{
				EntityLivingBase entity = (EntityLivingBase) iterator.next();
				double d0 = this.getDistanceSqToEntity(entity);

				if( d0 < (range * range) && entity instanceof EntityGreenSpider == false )
				{
					double d1 = 1.0D - Math.sqrt(d0) / range;
					int diffc = this.worldObj.difficultySetting.ordinal();
					float damage = (float) ((2 + diffc) * d1);

					Lib.log("EntityAcid.onImpact attackEntityFrom %.2f", damage);
					entity.attackEntityFrom(DamageSource.causeIndirectMagicDamage(entity, getThrower()), damage);

					for(int i=0; i<diffc; i++)
					{
						PotionEffect pe = null;
						switch (rand.nextInt(10))
						{
							case 0: pe = new PotionEffect(Potion.blindness.id,		20 + (int) (d1 * 20 * 4),  1); break;
							case 1: pe = new PotionEffect(Potion.digSlowdown.id,	20 + (int) (d1 * 20 * 20), 3); break;
							case 2: pe = new PotionEffect(Potion.poison.id,			20 + (int) (d1 * 20 * 4),  1); break;
							case 3: pe = new PotionEffect(Potion.hunger.id,			20 + (int) (d1 * 20 * 15), 1); break;
							case 4: pe = new PotionEffect(Potion.weakness.id,		20 + (int) (d1 * 20 * 10), 1); break;
							case 5: pe = new PotionEffect(Potion.moveSlowdown.id,	20 + (int) (d1 * 20 * 5),  2); break;
						}
						if(pe != null)
						{
							entity.addPotionEffect(pe);
						}
					}

					dead = true;
				}
			}

			if( dead || mop.typeOfHit != MovingObjectType.ENTITY )
			{
				this.playSound(this.getSplashSound(), 1, 1.5F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4F);
				this.setDead();
			}
		}
	}

	public void setDead()
	{
		super.setDead();
		if(this.worldObj.isRemote)
		{
			for (int i = 0; i < 20; i++)
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
					color * 0.7F,
					color,
					color * 0.7F);
			}
		}
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	public void readEntityFromNBT(NBTTagCompound p_70037_1_)
	{
		super.readEntityFromNBT(p_70037_1_);
	}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	public void writeEntityToNBT(NBTTagCompound p_70014_1_)
	{
		super.writeEntityToNBT(p_70014_1_);
	}
}

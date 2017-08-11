package uzammod.common.entity;

import net.minecraft.block.Block;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIFollowParent;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import uzammod.Lib;
import uzammod.UzaMonsterMod;

public class EntityFreshCow extends EntityModAnimalBase
{
	public EntityFreshCowHead head;

	public EntityFreshCow(World world)
	{
		super(world);

		//		this.parts = new EntityFreshCowPart[] { new EntityFreshCowPart(this, 0.5F, 0.5F) };

		this.setSize(0.9F, 1.3F);
		this.getNavigator().setAvoidsWater(true);
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(3, new EntityAIMate(this, 1.0D));
		//this.tasks.addTask(4, new EntityAITempt(this, 1.25D, Items.wheat, false));
		this.tasks.addTask(5, new EntityAIFollowParent(this, 1.25D));

		this.tasks.addTask(1, new EntityAIAttackOnCollide(this, EntityPlayer.class, 2.0D, false));
		this.tasks.addTask(2, new EntityAIAttackOnCollide(this, EntityVillager.class, 2.0D, true));
		this.tasks.addTask(3, new EntityAIMoveTowardsRestriction(this, 1.0D));
		this.tasks.addTask(4, new EntityAIWander(this, 1.0D));
		this.tasks.addTask(5, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(6, new EntityAILookIdle(this));
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true));
		this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityVillager.class, 0, false));
	}

	public void entityInit()
	{
		super.entityInit();
		this.dataWatcher.addObject(20, new Integer(0));
	}

	public boolean hasHead()
	{
		return this.dataWatcher.getWatchableObjectInt(20) == 0;
	}

	public void setNoHead(int n)
	{
		this.dataWatcher.updateObject(20, new Integer(n));
	}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	public void writeEntityToNBT(NBTTagCompound p_70014_1_)
	{
		super.writeEntityToNBT(p_70014_1_);
		p_70014_1_.setInteger("HeadState", this.dataWatcher.getWatchableObjectInt(20));
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	public void readEntityFromNBT(NBTTagCompound p_70037_1_)
	{
		super.readEntityFromNBT(p_70037_1_);
		setNoHead(new Integer(p_70037_1_.getInteger("HeadState")));
	}

	public boolean attackEntityFrom(DamageSource ds, float damage1)
	{
		float damage = damage1 > getMaxHealth() / 3 * 2 ? getMaxHealth() / 3 * 2 : damage1;

		//	Lib.log("EntityFreshCow.attackEntityFrom %s %.2f -> %.2f", ds.toString(), damage1, damage);

		if( !hasHead() )
		{
			damage = 0;
			return super.attackEntityFrom(ds, damage);
		}
		else
		{
			boolean ret = super.attackEntityFrom(ds, damage);

			if( !this.worldObj.isRemote && ret && getHealth() <= getMaxHealth() * 2 / 3 )
			{
				double mx, my, mz;
				Entity entity = ds.getEntity();
				my = 0.3 + rand.nextDouble() * 0.2;
				if( entity != null )
				{
					double dist = this.getDistanceToEntity(entity);
					mx = ((this.posX - entity.posX) / dist) * 0.5;
					mz = ((this.posZ - entity.posZ) / dist) * 0.5;
				}
				else
				{
					mx = rand.nextDouble() - 0.5;
					mz = rand.nextDouble() - 0.5;
				}
				dropHead(mx, my, mz);
			}
			return ret;
		}
	}

	public void onDeath(DamageSource ds)
	{
		super.onDeath(ds);

		for (int i = 0; i < 20; i++)
		{
			this.worldObj.spawnParticle(
				"blockcrack_" + Block.getIdFromBlock(Blocks.redstone_block) + "_0",
				this.posX,
				this.posY + 0.5,
				this.posZ,
				(rand.nextDouble() - 0.5) * 0.3,
				(rand.nextDouble() - 0.5) * 0.3,
				(rand.nextDouble() - 0.5) * 0.3);
		}

		if( this.head != null )
		{
			this.head.setDead();
		//	this.head = null;
		}
	}

	private void dropHead(double mx, double my, double mz)
	{
		Lib.log("EntityFreshCow.dropHead " + this.toString());

		setNoHead(1);
		this.head = new EntityFreshCowHead(this.worldObj, this.posX, this.posY + 1, this.posZ);
		this.head.entityBody = this;
		this.head.entityBodyUniqueID = getUniqueID().toString();
		this.head.motionX = mx * 1.5;
		this.head.motionY = my * 0.5;
		this.head.motionZ = mz * 1.5;
		this.head.rotationYaw = rand.nextInt(360);
		this.worldObj.spawnEntityInWorld(this.head);
	}

	public boolean attackEntityFromHead(DamageSource p_70097_1_, float p_70097_2_)
	{
		if( super.attackEntityFrom(p_70097_1_, p_70097_2_) )
		{
			return true;
		}

		return false;
	}

	public void setDead()
	{
		super.setDead();
		if( this.head != null )
		{
			this.head.setDead();
		}
	}

	/**
	 * Returns true if the newer Entity AI code should be run
	 */
	public boolean isAIEnabled()
	{
		return true;
	}

	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		this.getAttributeMap().registerAttribute(SharedMonsterAttributes.attackDamage);

		this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(30);
		this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.2);
		this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(1);
	}

	public void onLivingUpdate()
	{
		super.onLivingUpdate();
		if( !this.worldObj.isRemote )
		{
			if( this.head != null )
			{
				if( this.head.isDead )
				{
				//	setDead();
				}
			}
			else if( !hasHead() && this.ticksExisted > 110 )
			{
				setDead();
			}
			if(this.posY < -10)
			{
				setDead();
			}
		}
		else
		{
			if( !hasHead() && this.rand.nextInt(3) == 0 )
			{
				double yaw = this.renderYawOffset - 90;
				double x = -Math.cos((double) yaw * Math.PI / 180.0D) * 0.5D;
				double z = -Math.sin((double) yaw * Math.PI / 180.0D) * 0.5D;
				UzaMonsterMod.proxy.spawnParticle(this.worldObj, "dripLava",
					this.posX + x + (rand.nextDouble() - 0.5) * 0.6,
					this.posY + this.getMountedYOffset() + (rand.nextDouble() - 0.5) * 0.3,
					this.posZ + z + (rand.nextDouble() - 0.5) * 0.5,
					0, -0.01, 0);
			}
		}
	}

	public boolean attackEntityAsMob(Entity target)
	{
		super.attackEntityAsMob(target);

		float f = (float) this.getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue();
		int i = 2;

		if( target instanceof EntityLivingBase )
		{
			f += EnchantmentHelper.getEnchantmentModifierLiving(this, (EntityLivingBase) target);
			i += EnchantmentHelper.getKnockbackModifier(this, (EntityLivingBase) target);
		}

		boolean flag = target.attackEntityFrom(DamageSource.causeMobDamage(this), f);

		if( flag )
		{
			if( i > 0 )
			{
				target.addVelocity(
					(double) (-MathHelper.sin(this.rotationYaw * (float) Math.PI / 180.0F) * (float) i * 0.5F),
					0.1D,
					(double) (MathHelper.cos(this.rotationYaw * (float) Math.PI / 180.0F) * (float) i * 0.5F));
				this.motionX *= 0.6D;
				this.motionZ *= 0.6D;
			}

			if( target instanceof EntityLivingBase )
			{
				EnchantmentHelper.func_151384_a((EntityLivingBase) target, this);
			}

			EnchantmentHelper.func_151385_b(this, target);
		}

		return flag;
	}

	protected float getSoundPitch()
	{
		return super.getSoundPitch() * 0.8F;
	}

	/**
	 * Returns the sound this mob makes while it's alive.
	 */
	protected String getLivingSound()
	{
		return "mob.cow.say";
	}

	/**
	 * Returns the sound this mob makes when it is hurt.
	 */
	protected String getHurtSound()
	{
		return "mob.cow.hurt";
	}

	/**
	 * Returns the sound this mob makes on death.
	 */
	protected String getDeathSound()
	{
		return "mob.cow.hurt";
	}

	protected void func_145780_a(int p_145780_1_, int p_145780_2_, int p_145780_3_, Block p_145780_4_)
	{
		this.playSound("mob.cow.step", 0.15F, 1.0F);
	}

	/**
	 * Returns the volume for the sounds this mob makes.
	 */
	protected float getSoundVolume()
	{
		return 0.2F;
	}

	protected Item getDropItem()
	{
		return Items.rotten_flesh;
	}

	/**
	 * Drop 0-2 items of this living's type. @param par1 - Whether this entity has recently been hit by a player. @param
	 * par2 - Level of Looting used to kill this mob.
	 */
	protected void dropFewItems(boolean p_70628_1_, int p_70628_2_)
	{
		int j = this.rand.nextInt(3) + 1 + this.rand.nextInt(1 + p_70628_2_);

		for (int k = 0; k < j; ++k)
		{
			this.dropItem(Items.rotten_flesh, 1);
		}
	}

	public EntityFreshCow createChild(EntityAgeable p_90011_1_)
	{
		return new EntityFreshCow(this.worldObj);
	}
}

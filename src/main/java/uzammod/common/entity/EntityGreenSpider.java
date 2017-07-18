package uzammod.common.entity;

import java.util.Arrays;
import java.util.List;

import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import uzammod.Lib;
import uzammod.UzaMonsterMod;

public class EntityGreenSpider extends EntityModMobBase implements IEntityAdditionalSpawnData
{
	public int spiderType = 0;
	public boolean friendSpawn = false;
	public int despawnCount = 0;
	public double spawnPointX = 0;
	public double spawnPointY = 0;
	public double spawnPointZ = 0;
	public int attackCooldownCount = 30;
	public Entity surpriseTargetEntity = null;

	static List spawnableBlocks = Arrays.asList(new Block[]{
		Blocks.sand, Blocks.grass, Blocks.dirt
	});

	/** Selector used to determine the entities a wither boss should attack. */
	private static final IEntitySelector attackEntitySelector = new IEntitySelector()
	{
		public boolean isEntityApplicable(Entity entity)
		{
			return !(entity instanceof EntityGreenSpider) && !entity.isInWater();
		}
	};

	public EntityGreenSpider(World world)
	{
		super(world);
		this.setSize(1.4F, 1.2F);
		this.stepHeight = 2.1f;

		this.tasks.addTask(2, new EntityAIAttackOnCollide(this, EntityPlayer.class, 1.0D, false));
		this.tasks.addTask(6, new EntityAIAttackOnCollide(this, EntityLiving.class, 1.0D, false));
		this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 1.0D));
		this.tasks.addTask(7, new EntityAIWander(this, 1.0D));
		this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(8, new EntityAILookIdle(this));
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true));
		this.targetTasks.addTask(4, new EntityAINearestAttackableTarget(this, EntityLivingBase.class, 0, false, false, attackEntitySelector));

		this.experienceValue = 3;

		this.spiderType = rand.nextInt(4) == 0 ? 1 : 0;
	}

	public EntityGreenSpider(World world, boolean friend)
	{
		this(world);
		this.friendSpawn = friend;
	}

	protected void entityInit()
	{
		super.entityInit();
		this.dataWatcher.addObject(16, new Byte((byte) 0));
		this.dataWatcher.addObject(17, new Byte((byte) 0));
	}

	protected boolean isAIEnabled()
	{
		return true;
	}

	public void spawnFriend()
	{
		int x = MathHelper.floor_double(this.posX);
		int y = MathHelper.floor_double(this.posY) + 1;
		int z = MathHelper.floor_double(this.posZ);
		if( this.worldObj.canBlockSeeTheSky(x, y, z) )
		{
		//	List types = Arrays.asList(BiomeDictionary.getTypesForBiome(this.worldObj.getBiomeGenForCoords(x, z)));

//			if( (types.contains(Type.HOT) && types.contains(Type.DRY)) || types.contains(Type.SAVANNA) )
			{
				List list = this.worldObj.getEntitiesWithinAABB(EntityGreenSpider.class, this.boundingBox.expand(16, 8, 16));
				if( list.size() < this.worldObj.difficultySetting.ordinal() * 2 )
				{
					for (int i = 0; i < 30; i++)
					{
						int px = x + rand.nextInt(15) - 7;
						int pz = z + rand.nextInt(15) - 7;
						int py = this.worldObj.getTopSolidOrLiquidBlock(px, pz) - 1;
						Block block = this.worldObj.getBlock(px, py, pz);
						if( (Math.abs(py - this.posY) > 8) || !spawnableBlocks.contains(block))
						{
							continue;
						}
						if( canSpawnPos(px, py, pz) )
						{
							EntityGreenSpider entity = new EntityGreenSpider(this.worldObj, true);
							entity.setPosition(px, py + 1, pz);
							entity.setAttackTarget(this.getAttackTarget());
							this.worldObj.spawnEntityInWorld(entity);
							break;
						}
					}
				}
			}
		}
	}

	public boolean canSpawnPos(int px, int py, int pz)
	{
		Block block = this.worldObj.getBlock(px, py, pz);
		if( !spawnableBlocks.contains(block) )
		{
			for (int k = -1; k <= 1; k++)
			{
				for (int j = -1; j <= 1; j++)
				{
					block = this.worldObj.getBlock(px + k, py, pz + j);
					if( !spawnableBlocks.contains(block) && block != Blocks.air )
					{
						return false;
					}
					block = this.worldObj.getBlock(px + k, py + 1, pz + j);
					if( !spawnableBlocks.contains(block) && block != Blocks.air )
					{
						return false;
					}
				}
			}
		}

		return true;
	}

	public boolean attackEntityFrom(DamageSource ds, float damage)
	{
		boolean ret = false;

		if( ds != ds.inWall && ds != ds.cactus && ds != ds.fall )
		{
			ret = super.attackEntityFrom(ds, damage);

			if( ds.getEntity() instanceof EntityLivingBase && ret && rand.nextInt(3) == 0 && getAttackTarget() != null )
			{
				spawnFriend();
			}
		}

		return ret;
	}

	public boolean isDuringDespawn()
	{
		return this.dataWatcher.getWatchableObjectByte(17) == 1;
	}

	/**
	 * Called to update the entity's position/logic.
	 */
	public void onUpdate()
	{
		super.onUpdate();

		if( !this.worldObj.isRemote )
		{
			onUpdateServer();
		}
		else
		{
			onUpdateClient();
		}

		if( this.ticksExisted < 10 || isDuringDespawn() )
		{
			this.motionX *= 0.1;
			this.motionY *= 0.3;
			this.motionZ *= 0.1;
		}
	}

	public void onUpdateServer()
	{
		this.setBesideClimbableBlock(this.isCollidedHorizontally);

		if( this.isInWater() && this.ticksExisted % 15 == 0 )
		{
			attackEntityFrom(DamageSource.drown, 1 + this.rand.nextInt(3));
		}

		EntityLivingBase target = getAttackTarget();
		if( target != null && !target.isDead && !isDuringDespawn() )
		{
			this.surpriseTargetEntity = null;

			if( rand.nextInt(100) == 0 )
			{
				spawnFriend();
			}

			jumpToTarget(target);

			shootAcid(target);

			if( this.motionY > 0 )
			{
				this.motionX += (target.posX - this.posX) * 0.01;
				this.motionZ += (target.posZ - this.posZ) * 0.01;
			}
		}
		else
		{
			if( this.ticksExisted > 40 &&
				this.onGround &&
				!isDuringDespawn() &&
				rand.nextInt(20 * 5) == 0 &&
				canSpawnPos((int)this.posX - 1, (int)this.posY - 1, (int)this.posZ - 1))
			{
				List list = this.worldObj.getEntitiesWithinAABB(this.getClass(), this.boundingBox.expand(16, 16, 16));

				if( list.size() > 4 )
				{
					this.dataWatcher.updateObject(17, new Byte((byte) 1));
				}
			}

			if( !isDuringDespawn() )
			{
				if(this.surpriseTargetEntity != null)
				{
					if(this.surpriseTargetEntity.isDead)
					{
						this.surpriseTargetEntity = null;
					}
					else
					{
						if( this.motionY > 0 )
						{
							this.motionX += (this.surpriseTargetEntity.posX - this.posX) * 0.001;
							this.motionZ += (this.surpriseTargetEntity.posZ - this.posZ) * 0.001;
						}
					}
				}

				surpriseTarget();
			}
			else
			{
				if( this.despawnCount < 10 )
				{
					this.despawnCount++;
				}
				else if( this.onGround )
				{
					setDead();
					return;
				}
			}
		}

		if( this.attackCooldownCount > 0 )
		{
			this.attackCooldownCount--;
		}
	}

	public void jumpToTarget(Entity target)
	{
		if( this.onGround && rand.nextInt(10) == 0 )
		{
			double dist = getDistanceSq(target.posX, this.posY, target.posZ);
			if( dist > 1.9 * 1.9 )
			{
				if( rand.nextInt(5) == 0 )
				{
					if( this.prevPosX == this.posX && this.prevPosZ == this.posZ )
					{
						this.motionY += 0.5 + rand.nextDouble() * 1.0;
						this.playSound("mob.spider.say", 2.0F, 0.5F);
						Lib.log("N : %.3f", this.motionY);
					}
					else
					{
						Lib.log("G : %f %f : %f %f", this.prevPosX, this.posX, this.prevPosZ, this.posZ);
					}
				}
			}

			if( this.motionY < 0 && target.posY - this.posY > 2 )
			{
				this.motionY += (target.posY - this.posY) * 0.3;
				this.motionY += rand.nextDouble() * 0.1;
				Lib.log("Y : %.3f", this.motionY);
				this.playSound("mob.spider.say", 2.0F, 0.5F);
			}
		}
	}

	public void shootAcid(Entity target)
	{
		if( this.spiderType == 1 && this.onGround && this.attackCooldownCount == 0 )
		{
			if( this.getDistanceSqToEntity(target) < 35 * 35 )
			{
				EntityAcid entitysnowball = new EntityAcid(this.worldObj, this);
				double d0 = target.posX - this.posX;
				double d1 = target.posY + (double) target.getEyeHeight() - 1.1 - entitysnowball.posY;
				double d2 = target.posZ - this.posZ;
				float f1 = MathHelper.sqrt_double(d0 * d0 + d2 * d2) * 0.2F;
				entitysnowball.setThrowableHeading(d0, d1 + (double) f1, d2, 0.6F, 12.0F);
				this.playSound("random.bow", 1.0F, 1.0F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
				this.worldObj.spawnEntityInWorld(entitysnowball);

				this.attackCooldownCount = 20 + this.rand.nextInt(20);

				this.getNavigator().clearPathEntity();
			}
		}
	}

	public void surpriseTarget()
	{
		if( rand.nextInt(100) == 0 && this.onGround && this.worldObj.canBlockSeeTheSky((int) this.posX, (int) this.posY, (int) this.posZ) )
		{
			this.surpriseTargetEntity = null;
			int surpriseTargetDist = 60 * 60 * 60;
			List list = this.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, this.boundingBox.expand(50, 50, 50));
			for (Object o : list)
			{
				EntityLivingBase entity = (EntityLivingBase) o;
				if( entity instanceof EntityGreenSpider == false && !entity.isInWater() && !Lib.isEntityCreative(entity) && canAttackClass(entity.getClass()) )
				{
					if( this.worldObj.canBlockSeeTheSky((int) entity.posX, (int) entity.posY, (int) entity.posZ) )
					{
						double dist = this.getDistanceSqToEntity(entity);
						if( dist < surpriseTargetDist )
						{
							this.surpriseTargetEntity = entity;
							surpriseTargetDist = (int) dist;
						}
					}
				}
			}

			if( this.surpriseTargetEntity != null )
			{
				int num = 3 + this.rand.nextInt(3);
				list = this.worldObj.getEntitiesWithinAABB(EntityGreenSpider.class, this.boundingBox.expand(16, 16, 16));
				for (Object o : list)
				{
					EntityGreenSpider friend = (EntityGreenSpider) o;
					if( friend.getAttackTarget() == null && friend.onGround && friend.motionY <= 0 && !friend.isDuringDespawn() && !friend.isInWater() )
					{
						if( this.surpriseTargetEntity.posY > friend.posY )
						{
							friend.motionY += (this.surpriseTargetEntity.posY - friend.posY) * 0.04;
						}
						friend.motionY += Math.sqrt(surpriseTargetDist) * 0.05;
						friend.motionY += rand.nextDouble() * 0.05;
						if( friend.motionY < 0.1 )
						{
							friend.motionY = 0.1;
						}
						friend.motionX += (this.surpriseTargetEntity.posX - friend.posX) * 0.08;
						friend.motionZ += (this.surpriseTargetEntity.posZ - friend.posZ) * 0.08;
						friend.rotationYaw = -90.0F + (float) Lib.rad2deg(Lib.getRadian(friend.posX, friend.posZ, this.surpriseTargetEntity.posX, this.surpriseTargetEntity.posZ));
						Lib.log("surpriseTarget %.0f, %.2f %.2f %.2f", Math.sqrt(surpriseTargetDist), friend.motionX, friend.motionY, friend.motionZ);

						num--;
						if( num <= 0 )
						{
							break;
						}
					}
				}
			}
		}
	}

	public void onUpdateClient()
	{
		if( (this.friendSpawn && this.ticksExisted < 10) || isDuringDespawn() )
		{
			if( this.ticksExisted == 1 )
			{
				this.spawnPointX = this.posX;
				this.spawnPointY = this.posY;
				this.spawnPointZ = this.posZ;
			}

			for (int i = 0; i < 30; i++)
			{
				float color = this.rand.nextFloat() * 0.3F + 0.7F;
				UzaMonsterMod.proxy.spawnParticle(
					this.worldObj,
					"explode",
					this.spawnPointX + (this.rand.nextDouble() - 0.5) * 3.0,
					this.spawnPointY + this.rand.nextDouble() * 1.0 - 0.5,
					this.spawnPointZ + (this.rand.nextDouble() - 0.5) * 3.0,
					0,
					0,
					0,
					0.9F,
					color,
					color,
					color * 0.7F);
			}
		}

		if(this.despawnCount < 20 && isDuringDespawn())
		{
			if( this.despawnCount == 0 )
			{
				this.spawnPointX = this.posX;
				this.spawnPointY = this.posY;
				this.spawnPointZ = this.posZ;
			}
			this.despawnCount++;
		}
	}

	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(20.0D);
		this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.4);
		this.getEntityAttribute(SharedMonsterAttributes.knockbackResistance).setBaseValue(0.6);
	}

	/**
	 * Finds the closest player within 16 blocks to attack, or null if this Entity isn't interested in attacking
	 * (Animals, Spiders at day, peaceful PigZombies).
	 */
	protected Entity findPlayerToAttack()
	{
		EntityPlayer entityplayer = this.worldObj.getClosestVulnerablePlayerToEntity(this, 32.0D);
		return entityplayer != null && this.canEntityBeSeen(entityplayer) ? entityplayer : null;
	}

	/**
	 * Returns the sound this mob makes while it's alive.
	 */
	protected String getLivingSound()
	{
		return "mob.spider.say";
	}

	/**
	 * Returns the sound this mob makes when it is hurt.
	 */
	protected String getHurtSound()
	{
		return "mob.spider.say";
	}

	/**
	 * Returns the sound this mob makes on death.
	 */
	protected String getDeathSound()
	{
		return "mob.spider.death";
	}

	protected void func_145780_a(int p_145780_1_, int p_145780_2_, int p_145780_3_, Block p_145780_4_)
	{
		this.playSound("mob.spider.step", 0.15F, 1.0F);
	}

	/**
	 * Basic mob attack. Default to touch of death in EntityCreature. Overridden by each mob to define their attack.
	 */
	protected void attackEntity(Entity p_70785_1_, float p_70785_2_)
	{
		super.attackEntity(p_70785_1_, p_70785_2_);
	}

	/**
	 * Drop 0-2 items of this living's type. @param par1 - Whether this entity has recently been hit by a player. @param
	 * par2 - Level of Looting used to kill this mob.
	 */
	protected void dropFewItems(boolean p_70628_1_, int p_70628_2_)
	{
		super.dropFewItems(p_70628_1_, p_70628_2_);

		if( p_70628_1_ && (this.rand.nextInt(3) == 0 || this.rand.nextInt(1 + p_70628_2_) > 0) )
		{
			this.dropItem(Items.spider_eye, 1);
		}
	}

	/**
	 * returns true if this entity is by a ladder, false otherwise
	 */
	public boolean isOnLadder()
	{
		return this.isBesideClimbableBlock();
	}

	/**
	 * Sets the Entity inside a web block.
	 */
	public void setInWeb()
	{
	}

	/**
	 * Get this Entity's EnumCreatureAttribute
	 */
	public EnumCreatureAttribute getCreatureAttribute()
	{
		return EnumCreatureAttribute.ARTHROPOD;
	}

	public boolean isPotionApplicable(PotionEffect p_70687_1_)
	{
		return p_70687_1_.getPotionID() == Potion.poison.id ? false : super.isPotionApplicable(p_70687_1_);
	}

	/**
	 * Returns true if the WatchableObject (Byte) is 0x01 otherwise returns false. The WatchableObject is updated using
	 * setBesideClimableBlock.
	 */
	public boolean isBesideClimbableBlock()
	{
		return (this.dataWatcher.getWatchableObjectByte(16) & 1) != 0;
	}

	/**
	 * Updates the WatchableObject (Byte) created in entityInit(), setting it to 0x01 if par1 is true or 0x00 if it is
	 * false.
	 */
	public void setBesideClimbableBlock(boolean p_70839_1_)
	{
		byte b0 = this.dataWatcher.getWatchableObjectByte(16);

		if( p_70839_1_ )
		{
			b0 = (byte) (b0 | 1);
		}
		else
		{
			b0 &= -2;
		}

		this.dataWatcher.updateObject(16, Byte.valueOf(b0));
	}

	public IEntityLivingData onSpawnWithEgg(IEntityLivingData p_110161_1_)
	{
		this.addPotionEffect(new PotionEffect(Potion.moveSpeed.id, Integer.MAX_VALUE, rand.nextInt(2)));
		return super.onSpawnWithEgg(p_110161_1_);
	}

	@Override
	public void writeSpawnData(ByteBuf buffer)
	{
		buffer.writeByte(this.spiderType);
		buffer.writeBoolean(this.friendSpawn);
	}

	@Override
	public void readSpawnData(ByteBuf additionalData)
	{
		this.spiderType = additionalData.readByte();
		this.friendSpawn = additionalData.readBoolean();
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound nbt)
	{
		super.writeEntityToNBT(nbt);

		nbt.setByte("SpiderType", (byte)this.spiderType);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound nbt)
	{
		super.readEntityFromNBT(nbt);

		this.spiderType = nbt.getByte("SpiderType");
	}
}

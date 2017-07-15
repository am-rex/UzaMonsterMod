package uzammod.common;

import java.util.List;
import java.util.Random;

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
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import scala.actors.threadpool.Arrays;

public class EntityGreenSpider extends EntityMob
{
	public int despawnCount = 0;

	/** Selector used to determine the entities a wither boss should attack. */
	private static final IEntitySelector attackEntitySelector = new IEntitySelector()
	{
		private static final String __OBFID = "CL_00001662";

		/**
		 * Return whether the specified entity is applicable to this filter.
		 */
		public boolean isEntityApplicable(Entity p_82704_1_)
		{
			return p_82704_1_ instanceof EntityLivingBase &&
					p_82704_1_ instanceof EntityGreenSpider == false;
			//((EntityLivingBase) p_82704_1_).getCreatureAttribute() != EnumCreatureAttribute.ARTHROPOD;
		}
	};

	public EntityGreenSpider(World p_i1743_1_)
	{
		super(p_i1743_1_);
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
		this.targetTasks.addTask(4,
				new EntityAINearestAttackableTarget(this, EntityLiving.class, 0, false, false, attackEntitySelector));
	}

	protected void entityInit()
	{
		super.entityInit();
		this.dataWatcher.addObject(16, new Byte((byte) 0));
	}

	protected boolean isAIEnabled()
	{
		return true;
	}

	public boolean attackEntityFrom(DamageSource ds, float p_70097_2_)
	{
		boolean ret = false;

		if (ds != ds.inWall)
		{
			ret = super.attackEntityFrom(ds, p_70097_2_);
		}

		if (ds.getEntity() instanceof EntityLivingBase && ret && rand.nextInt(3) == 0)
		{
			int x = MathHelper.floor_double(this.posX);
			int y = MathHelper.floor_double(this.posY) + 1;
			int z = MathHelper.floor_double(this.posZ);
			if (this.worldObj.canBlockSeeTheSky(x, y, z))
			{
				List types = Arrays.asList(BiomeDictionary.getTypesForBiome(this.worldObj.getBiomeGenForCoords(x, z)));

				if (types.contains(Type.HOT) && types.contains(Type.DRY))
				{
					List list = this.worldObj.getEntitiesWithinAABB(EntityGreenSpider.class,
							this.boundingBox.expand(16, 8, 16));
					if (list.size() < this.worldObj.difficultySetting.ordinal() * 2)
					{
						for (int i = 0; i < 10; i++)
						{
							int px = x + rand.nextInt(15) - 7;
							int pz = z + rand.nextInt(15) - 7;
							if (i == 9 || this.worldObj.getTopBlock(px, pz) == Blocks.sand)
							{
								int py = this.worldObj.getTopSolidOrLiquidBlock(px, pz);
								if (Math.abs(py - y) < 4)
								{
									EntityGreenSpider entity = new EntityGreenSpider(this.worldObj);
									entity.setPosition(px, py, pz);
									entity.setAttackTarget(this.getAttackTarget());
									this.worldObj.spawnEntityInWorld(entity);
									break;
								}
							}
						}
					}
				}
			}
		}

		return ret;
	}

	/**
	 * Called to update the entity's position/logic.
	 */
	public void onUpdate()
	{
		super.onUpdate();

		if (!this.worldObj.isRemote)
		{
			this.setBesideClimbableBlock(this.isCollidedHorizontally);

			if (this.isInWater() && this.ticksExisted % 15 == 0)
			{
				attackEntityFrom(DamageSource.drown, 1 + this.rand.nextInt(3));
			}

			if (getAttackTarget() != null && !getAttackTarget().isDead)
			{
				this.despawnCount = 0;
			}
			else
			{
				this.despawnCount += this.despawnCount < 20? 0: 1;

				if (this.despawnCount >= 20 && this.ticksExisted % (20 * 5) == 10)
				{
					List list = this.worldObj.getEntitiesWithinAABB(this.getClass(),
							this.boundingBox.expand(16, 16, 16));

					if (list.size() > 4)
					{
						setDead();
					}
				}
			}

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

	protected Item getDropItem()
	{
		return Items.string;
	}

	/**
	 * Drop 0-2 items of this living's type. @param par1 - Whether this entity has recently been hit by a player. @param
	 * par2 - Level of Looting used to kill this mob.
	 */
	protected void dropFewItems(boolean p_70628_1_, int p_70628_2_)
	{
		super.dropFewItems(p_70628_1_, p_70628_2_);

		if (p_70628_1_ && (this.rand.nextInt(3) == 0 || this.rand.nextInt(1 + p_70628_2_) > 0))
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

		if (p_70839_1_)
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
		Object p_110161_1_1 = super.onSpawnWithEgg(p_110161_1_);

		if (p_110161_1_1 == null)
		{
			p_110161_1_1 = new EntityGreenSpider.GroupData();

			//	if (this.worldObj.difficultySetting == EnumDifficulty.HARD && this.worldObj.rand.nextFloat() < 0.1F
			//			* this.worldObj.func_147462_b(this.posX, this.posY, this.posZ))
			{
				((EntityGreenSpider.GroupData) p_110161_1_1).func_111104_a(this.worldObj.rand);
			}
		}

		if (p_110161_1_1 instanceof EntityGreenSpider.GroupData)
		{
			int i = ((EntityGreenSpider.GroupData) p_110161_1_1).field_111105_a;

			if (i > 0 && Potion.potionTypes[i] != null)
			{
				this.addPotionEffect(new PotionEffect(i, Integer.MAX_VALUE, 0));
			}
		}

		return (IEntityLivingData) p_110161_1_1;
	}

	public static class GroupData implements IEntityLivingData
	{
		public int field_111105_a;
		private static final String __OBFID = "CL_00001700";

		public void func_111104_a(Random p_111104_1_)
		{
			int i = p_111104_1_.nextInt(5);

			if (i <= 1)
			{
				this.field_111105_a = Potion.moveSpeed.id;
			}
			else if (i <= 2)
			{
				this.field_111105_a = Potion.damageBoost.id;
			}
			else if (i <= 3)
			{
				this.field_111105_a = Potion.regeneration.id;
			}
			else if (i <= 4)
			{
				this.field_111105_a = Potion.invisibility.id;
			}
			this.field_111105_a = Potion.moveSpeed.id;
		}
	}
}
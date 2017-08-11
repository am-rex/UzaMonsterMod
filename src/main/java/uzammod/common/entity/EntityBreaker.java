package uzammod.common.entity;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBreakDoor;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeModContainer;
import uzammod.Lib;
import uzammod.common.entity.ai.EntityAIMoveNearTargetBlock;

public class EntityBreaker extends EntityModMobBase
{

	protected static final IAttribute field_110186_bp = (new RangedAttribute("zombie.spawnReinforcements", 0.0D, 0.0D,
			1.0D)).setDescription("Spawn Reinforcements Chance");
	private final EntityAIBreakDoor field_146075_bs = new EntityAIBreakDoor(this);
	/** Ticker used to determine the time remaining for this zombie to convert into a villager when cured. */
	private boolean field_146076_bu = false;
	private float field_146074_bv = -1.0F;
	private float field_146073_bw;

	private List targetBlocks = Arrays.asList(
			new Block[] {
					Blocks.torch,
					Blocks.fence_gate,
					Blocks.glass_pane,
					Blocks.redstone_torch,
					Blocks.lever
			});

	public EntityBreaker(World p_i1745_1_)
	{
		super(p_i1745_1_);
		this.getNavigator().setBreakDoors(true);
		//this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 1.0D));
		this.tasks.addTask(5, new EntityAIMoveNearTargetBlock(this, targetBlocks));
		this.tasks.addTask(7, new EntityAIWander(this, 1.0D));
		this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(8, new EntityAILookIdle(this));
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
		this.setSize(0.6F, 1.8F);
		this.stepHeight = 1.1f;
	}

	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(100.0D * this.worldObj.difficultySetting.ordinal());
		this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(80.0D);
		this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.3);
		this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(0.5);
		this.getAttributeMap().registerAttribute(field_110186_bp).setBaseValue(this.rand.nextDouble() * ForgeModContainer.zombieSummonBaseChance);
	}

	protected void entityInit()
	{
		super.entityInit();
		this.getDataWatcher().addObject(12, Byte.valueOf((byte) 0));
		this.getDataWatcher().addObject(13, Byte.valueOf((byte) 0));
		this.getDataWatcher().addObject(14, Byte.valueOf((byte) 0));
	}

	public boolean canAttackClass(Class p_70686_1_)
	{
		return EntityGhast.class != p_70686_1_;
	}

	/**
	 * Returns the current armor value as determined by a call to InventoryPlayer.getTotalArmorValue
	 */
	public int getTotalArmorValue()
	{
		int i = super.getTotalArmorValue() + 2;

		if (i > 20)
		{
			i = 20;
		}

		return i;
	}

	/**
	 * Returns true if the newer Entity AI code should be run
	 */
	protected boolean isAIEnabled()
	{
		return true;
	}

	public boolean func_146072_bX()
	{
		return this.field_146076_bu;
	}

	public void func_146070_a(boolean p_146070_1_)
	{
		if (this.field_146076_bu != p_146070_1_)
		{
			this.field_146076_bu = p_146070_1_;

			if (p_146070_1_)
			{
				this.tasks.addTask(1, this.field_146075_bs);
			}
			else
			{
				this.tasks.removeTask(this.field_146075_bs);
			}
		}
	}

	/**
	 * Get the experience points the entity currently has.
	 */
	protected int getExperiencePoints(EntityPlayer p_70693_1_)
	{
		return super.getExperiencePoints(p_70693_1_);
	}


	/**
	 * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
	 * use this to react to sunlight and start to burn.
	 */
	public void onLivingUpdate()
	{
		if (this.worldObj.isDaytime() && !this.worldObj.isRemote && !this.isChild())
		{
			float f = this.getBrightness(1.0F);

			if (f > 0.5F && this.rand.nextFloat() * 30.0F < (f - 0.4F) * 2.0F
					&& this.worldObj.canBlockSeeTheSky(MathHelper.floor_double(this.posX),
							MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ)))
			{
				boolean flag = true;
				ItemStack itemstack = this.getEquipmentInSlot(4);

				if (itemstack != null)
				{
					if (itemstack.isItemStackDamageable())
					{
						itemstack.setItemDamage(itemstack.getItemDamageForDisplay() + this.rand.nextInt(2));

						if (itemstack.getItemDamageForDisplay() >= itemstack.getMaxDamage())
						{
							this.renderBrokenItemStack(itemstack);
							this.setCurrentItemOrArmor(4, (ItemStack) null);
						}
					}

					flag = false;
				}

				if (flag)
				{
					this.setFire(2);
				}
			}
		}

		if (this.isRiding() && this.getAttackTarget() != null && this.ridingEntity instanceof EntityChicken)
		{
			((EntityLiving) this.ridingEntity).getNavigator().setPath(this.getNavigator().getPath(), 1.5D);
		}

		super.onLivingUpdate();
	}

	/**
	 * Called when the entity is attacked.
	 */
	public boolean attackEntityFrom(DamageSource p_70097_1_, float p_70097_2_)
	{
		if (!super.attackEntityFrom(p_70097_1_, p_70097_2_))
		{
			return false;
		}
		else
		{
			return true;
		}
	}

	/**
	 * Called to update the entity's position/logic.
	 */
	public void onUpdate()
	{
		super.onUpdate();

		if (!this.worldObj.isRemote && this.ticksExisted % 5 == 0)
		{
			final int[] A = new int[] {
					-1, -1, 0,   1, -1, 0,   0, -1, 0,   0, -1, -1,   0, -1, 1,
					-1,  0, 0,   1,  0, 0,   0,  0, 0,   0,  0, -1,   0,  0, 1,
					-1,  1, 0,   1,  1, 0,   0,  1, 0,   0,  1, -1,   0,  1, 1,
					-1,  2, 0,   1,  2, 0,   0,  2, 0,   0,  2, -1,   0,  2, 1
			};
			for (int i = 0; i < A.length / 3; i++)
			{
				int x = ((int)(this.posX - 1)) + A[i * 3 + 0];
				int y = ((int)(this.posY))       + A[i * 3 + 1];
				int z = ((int)(this.posZ - 1)) + A[i * 3 + 2];
				Block block = this.worldObj.getBlock(x, y, z);

				if (targetBlocks.contains(block))
				{
					this.worldObj.func_147480_a(x, y, z, true);
					Lib.log("Break block : %d %d %d %s", x, y, z, block.toString());
					break;
				}
			}
		}
	}

	public boolean attackEntityAsMob(Entity p_70652_1_)
	{
		boolean flag = super.attackEntityAsMob(p_70652_1_);

		if (flag)
		{
			int i = this.worldObj.difficultySetting.getDifficultyId();

			if (this.getHeldItem() == null && this.isBurning() && this.rand.nextFloat() < (float) i * 0.3F)
			{
				p_70652_1_.setFire(2 * i);
			}
		}

		return flag;
	}

	/**
	 * Returns the sound this mob makes while it's alive.
	 */
	protected String getLivingSound()
	{
		return "mob.zombie.say";
	}

	/**
	 * Returns the sound this mob makes when it is hurt.
	 */
	protected String getHurtSound()
	{
		return "mob.zombie.hurt";
	}

	/**
	 * Returns the sound this mob makes on death.
	 */
	protected String getDeathSound()
	{
		return "mob.zombie.death";
	}

	protected void func_145780_a(int p_145780_1_, int p_145780_2_, int p_145780_3_, Block p_145780_4_)
	{
		this.playSound("mob.zombie.step", 0.15F, 1.0F);
	}

	protected Item getDropItem()
	{
		return Items.rotten_flesh;
	}

	/**
	 * Get this Entity's EnumCreatureAttribute
	 */
	public EnumCreatureAttribute getCreatureAttribute()
	{
		return EnumCreatureAttribute.UNDEAD;
	}

	protected void dropRareDrop(int p_70600_1_)
	{
		switch (this.rand.nextInt(3))
		{
		case 0:
			this.dropItem(Items.iron_ingot, 1);
			break;
		case 1:
			this.dropItem(Items.carrot, 1);
			break;
		case 2:
			this.dropItem(Items.potato, 1);
		}
	}

	/**
	 * Makes entity wear random armor based on difficulty
	 */
	protected void addRandomArmor()
	{
		super.addRandomArmor();

		if (this.rand.nextInt(2) == 0)
		{
			this.setCurrentItemOrArmor(0, new ItemStack(Items.iron_pickaxe));
		}
		else
		{
			this.setCurrentItemOrArmor(0, new ItemStack(Items.iron_axe));
		}
	}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	public void writeEntityToNBT(NBTTagCompound p_70014_1_)
	{
		super.writeEntityToNBT(p_70014_1_);

		p_70014_1_.setBoolean("CanBreakDoors", this.func_146072_bX());
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	public void readEntityFromNBT(NBTTagCompound p_70037_1_)
	{
		super.readEntityFromNBT(p_70037_1_);

		this.func_146070_a(p_70037_1_.getBoolean("CanBreakDoors"));
	}

	/**
	 * This method gets called when the entity kills another one.
	 */
	public void onKillEntity(EntityLivingBase p_70074_1_)
	{
		super.onKillEntity(p_70074_1_);

		if ((this.worldObj.difficultySetting == EnumDifficulty.NORMAL
				|| this.worldObj.difficultySetting == EnumDifficulty.HARD) && p_70074_1_ instanceof EntityVillager)
		{
			if (this.worldObj.difficultySetting != EnumDifficulty.HARD && this.rand.nextBoolean())
			{
				return;
			}

			EntityZombie entityzombie = new EntityZombie(this.worldObj);
			entityzombie.copyLocationAndAnglesFrom(p_70074_1_);
			this.worldObj.removeEntity(p_70074_1_);
			entityzombie.onSpawnWithEgg((IEntityLivingData) null);
			entityzombie.setVillager(true);

			if (p_70074_1_.isChild())
			{
				entityzombie.setChild(true);
			}

			this.worldObj.spawnEntityInWorld(entityzombie);
			this.worldObj.playAuxSFXAtEntity((EntityPlayer) null, 1016, (int) this.posX, (int) this.posY,
					(int) this.posZ, 0);
		}
	}

	public IEntityLivingData onSpawnWithEgg(IEntityLivingData p_110161_1_)
	{
		Object p_110161_1_1 = super.onSpawnWithEgg(p_110161_1_);
		float f = this.worldObj.func_147462_b(this.posX, this.posY, this.posZ);
		this.setCanPickUpLoot(this.rand.nextFloat() < 0.55F * f);

		/*
		if (p_110161_1_1 == null)
		{
		    p_110161_1_1 = new EntityZombie.GroupData(this.worldObj.rand.nextFloat() < ForgeModContainer.zombieBabyChance, this.worldObj.rand.nextFloat() < 0.05F, null);
		}

		if (p_110161_1_1 instanceof EntityZombie.GroupData)
		{
		    EntityZombie.GroupData groupdata = (EntityZombie.GroupData)p_110161_1_1;

		    if (groupdata.field_142046_b)
		    {
		        this.setVillager(true);
		    }

		    if (groupdata.field_142048_a)
		    {
		        this.setChild(true);

		        if ((double)this.worldObj.rand.nextFloat() < 0.05D)
		        {
		            List list = this.worldObj.selectEntitiesWithinAABB(EntityChicken.class, this.boundingBox.expand(5.0D, 3.0D, 5.0D), IEntitySelector.field_152785_b);

		            if (!list.isEmpty())
		            {
		                EntityChicken entitychicken = (EntityChicken)list.get(0);
		                entitychicken.func_152117_i(true);
		                this.mountEntity(entitychicken);
		            }
		        }
		        else if ((double)this.worldObj.rand.nextFloat() < 0.05D)
		        {
		            EntityChicken entitychicken1 = new EntityChicken(this.worldObj);
		            entitychicken1.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);
		            entitychicken1.onSpawnWithEgg((IEntityLivingData)null);
		            entitychicken1.func_152117_i(true);
		            this.worldObj.spawnEntityInWorld(entitychicken1);
		            this.mountEntity(entitychicken1);
		        }
		    }
		}
		*/

		this.func_146070_a(this.rand.nextFloat() < f * 0.1F);
		this.addRandomArmor();
		this.enchantEquipment();

		if (this.getEquipmentInSlot(4) == null)
		{
			Calendar calendar = this.worldObj.getCurrentDate();

			if (calendar.get(2) + 1 == 10 && calendar.get(5) == 31 && this.rand.nextFloat() < 0.25F)
			{
				this.setCurrentItemOrArmor(4,
						new ItemStack(this.rand.nextFloat() < 0.1F ? Blocks.lit_pumpkin : Blocks.pumpkin));
				this.equipmentDropChances[4] = 0.0F;
			}
		}

		this.getEntityAttribute(SharedMonsterAttributes.knockbackResistance).applyModifier(
				new AttributeModifier("Random spawn bonus", this.rand.nextDouble() * 0.05000000074505806D, 0));
		double d0 = this.rand.nextDouble() * 1.5D
				* (double) this.worldObj.func_147462_b(this.posX, this.posY, this.posZ);

		if (d0 > 1.0D)
		{
			this.getEntityAttribute(SharedMonsterAttributes.followRange)
					.applyModifier(new AttributeModifier("Random zombie-spawn bonus", d0, 2));
		}

		if (this.rand.nextFloat() < f * 0.05F)
		{
			this.getEntityAttribute(field_110186_bp).applyModifier(
					new AttributeModifier("Leader zombie bonus", this.rand.nextDouble() * 0.25D + 0.5D, 0));
			this.getEntityAttribute(SharedMonsterAttributes.maxHealth).applyModifier(
					new AttributeModifier("Leader zombie bonus", this.rand.nextDouble() * 3.0D + 1.0D, 2));
			this.func_146070_a(true);
		}

		return (IEntityLivingData) p_110161_1_1;
	}

	/**
	 * Called when a player interacts with a mob. e.g. gets milk from a cow, gets into the saddle on a pig.
	 */
	public boolean interact(EntityPlayer p_70085_1_)
	{
		return false;
	}

	@SideOnly(Side.CLIENT)
	public void handleHealthUpdate(byte p_70103_1_)
	{
		if (p_70103_1_ == 16)
		{
			this.worldObj.playSound(this.posX + 0.5D, this.posY + 0.5D, this.posZ + 0.5D, "mob.zombie.remedy",
					1.0F + this.rand.nextFloat(), this.rand.nextFloat() * 0.7F + 0.3F, false);
		}
		else
		{
			super.handleHealthUpdate(p_70103_1_);
		}
	}

	public void func_146071_k(boolean p_146071_1_)
	{
		this.func_146069_a(p_146071_1_ ? 0.5F : 1.0F);
	}

	/**
	 * Sets the width and height of the entity. Args: width, height
	 */
	protected final void setSize(float p_70105_1_, float p_70105_2_)
	{
		boolean flag = this.field_146074_bv > 0.0F && this.field_146073_bw > 0.0F;
		this.field_146074_bv = p_70105_1_;
		this.field_146073_bw = p_70105_2_;

		if (!flag)
		{
			this.func_146069_a(1.0F);
		}
	}

	protected final void func_146069_a(float p_146069_1_)
	{
		super.setSize(this.field_146074_bv * p_146069_1_, this.field_146073_bw * p_146069_1_);
	}
}

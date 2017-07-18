package uzammod.common.entity;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.MathHelper;
import uzammod.Lib;

public class EntityAIMoveNearTargetBlock extends EntityAIBase
{
	private EntityCreature entityObj;
	private final List targetBlocks;
	private int targetPosX = -1;
	private int targetPosY = -1;
	private int targetPosZ = -1;
	private static final String __OBFID = "CL_00001596";
	private Random rand = new Random();
	private int searchPos;

	public EntityAIMoveNearTargetBlock(EntityCreature p_i1637_1_, List blocks)
	{
		this.entityObj = p_i1637_1_;
		this.targetBlocks = blocks;
		this.setMutexBits(1);
		this.searchPos = 0;
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	public boolean shouldExecute()
	{
		if (this.entityObj.getRNG().nextInt(50) != 0)
		{
		}
		else if (this.targetPosY != -1 && this.entityObj.getDistanceSq(this.targetPosX, this.targetPosY, this.targetPosZ) < 4.0D)
		{
		}
		else
		{
			this.searchPos = (this.searchPos + 1) % 8;

			int i = MathHelper.floor_double(this.entityObj.posX) + ( (this.searchPos & 0x02) != 0? -7: 7 ) + ( (this.searchPos & 0x04) != 0? 0: rand.nextInt(64)-32 );
			int j = MathHelper.floor_double(this.entityObj.posY);
			int k = MathHelper.floor_double(this.entityObj.posZ) + ( (this.searchPos & 0x01) != 0? -7: 7 ) + ( (this.searchPos & 0x04) != 0? 0: rand.nextInt(64)-32 );

			for (int y = -6; y <= 6; y++)
			{
				if (j + y >= 1 && j + y < 256)
				{
					for (int x = -8; x <= 8; x++)
					{
						for (int z = -8; z <= 8; z++)
						{
							Block block = this.entityObj.worldObj.getBlock(i + x, j + y, k + z);
							if (this.targetBlocks.contains(block))
							{
								this.targetPosX = i + x;
								this.targetPosY = j + y;
								this.targetPosZ = k + z;
								Lib.log("Target block : %d %d %d %s", this.targetPosX, this.targetPosY, this.targetPosZ, block.toString());
								return true;
							}
						}
					}
				}
			}
		}
		return false;
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	public boolean continueExecuting()
	{
		return !this.entityObj.getNavigator().noPath();
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	public void startExecuting()
	{
		Lib.log("    tryMoveToXYZ : %d %d %d %s", this.targetPosX, this.targetPosY, this.targetPosZ,
				this.entityObj.getNavigator().tryMoveToXYZ(this.targetPosX, this.targetPosY, this.targetPosZ, 1.0D )? "true":"false");
		this.targetPosY = -1;
	}

	/**
	 * Resets the task
	 */
	public void resetTask()
	{
		this.targetPosX = -1;
		this.targetPosY = -1;
		this.targetPosZ = -1;
	}
}

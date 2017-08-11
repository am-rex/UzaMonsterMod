package uzammod.client.particle;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

@SideOnly(Side.CLIENT)
public class EntityDropParticleFX extends EntityFX
{
	/** the material type for dropped items/blocks */
	private Material materialType;
	/** The height of the current bob */
	private int bobTimer;

	public EntityDropParticleFX(World p_i1203_1_, double p_i1203_2_, double p_i1203_4_, double p_i1203_6_, Material p_i1203_8_)
	{
		super(p_i1203_1_, p_i1203_2_, p_i1203_4_, p_i1203_6_, 0.0D, 0.0D, 0.0D);
		this.motionX = this.motionY = this.motionZ = 0.0D;

		if( p_i1203_8_ == Material.water )
		{
			this.particleRed = 0.0F;
			this.particleGreen = 0.0F;
			this.particleBlue = 1.0F;
		}
		else
		{
			this.particleRed = 1.0F;
			this.particleGreen = 0.0F;
			this.particleBlue = 0.0F;
		}

		this.setParticleTextureIndex(113);
		this.setSize(0.01F, 0.01F);
		this.particleGravity = 0.06F;
		this.materialType = p_i1203_8_;
		this.bobTimer = 40;
		this.particleMaxAge = (int) (64.0D / (Math.random() * 0.8D + 0.2D));
		this.motionX = this.motionY = this.motionZ = 0.0D;
		this.particleScale *= 1.5;
	}

	public int getBrightnessForRender(float p_70070_1_)
	{
		return this.materialType == Material.water ? super.getBrightnessForRender(p_70070_1_) : 257;
	}

	/**
	 * Gets how bright this entity is.
	 */
	public float getBrightness(float p_70013_1_)
	{
		return this.materialType == Material.water ? super.getBrightness(p_70013_1_) : 1.0F;
	}

	/**
	 * Called to update the entity's position/logic.
	 */
	public void onUpdate()
	{
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;

		if( this.materialType == Material.water )
		{
			this.particleRed = 0.2F;
			this.particleGreen = 0.3F;
			this.particleBlue = 1.0F;
		}
		else
		{
			this.particleRed = 1.0F;
			this.particleGreen = 2.0F / (float) (40 - this.bobTimer + 16);
			this.particleBlue = 2.0F / (float) (40 - this.bobTimer + 8);
		}

		this.motionY -= (double) this.particleGravity;

		if( this.bobTimer-- > 0 )
		{
			//	this.motionX *= 0.02D;
			//	this.motionY *= 0.02D;
			//	this.motionZ *= 0.02D;
			this.setParticleTextureIndex(113);
		}
		else
		{
			this.setParticleTextureIndex(112);
		}

		this.moveEntity(this.motionX, this.motionY, this.motionZ);
		this.motionX *= 0.9800000190734863D;
		this.motionY *= 0.9800000190734863D;
		this.motionZ *= 0.9800000190734863D;

		if( this.particleMaxAge-- <= 0 )
		{
			this.setDead();
		}

		if( this.onGround )
		{
			if( this.materialType == Material.water )
			{
				this.setDead();
				this.worldObj.spawnParticle("splash", this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D);
			}
			else
			{
				this.setParticleTextureIndex(114);
			}

			if( this.motionX == 0 && this.motionZ == 0 )
			{
				this.motionX = (rand.nextDouble() - 0.5) * 0.001;
				this.motionZ = (rand.nextDouble() - 0.5) * 0.001;
			}
			//	this.motionX *= 0.699999988079071D;
			//	this.motionZ *= 0.699999988079071D;
		}

		Material material = this.worldObj.getBlock(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ)).getMaterial();

		if( material.isLiquid() || material.isSolid() )
		{
			double d0 = (double) ((float) (MathHelper.floor_double(this.posY) + 1)
				- BlockLiquid.getLiquidHeightPercent(this.worldObj.getBlockMetadata(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ))));

			if( this.posY < d0 )
			{
				this.setDead();
			}
		}
	}

	public void renderParticle(Tessellator p_70539_1_, float p_70539_2_, float p_70539_3_, float p_70539_4_, float p_70539_5_, float p_70539_6_, float p_70539_7_)
	{
		float f6 = (float) this.particleTextureIndexX / 16.0F;
		float f7 = f6 + 0.0624375F;
		float f8 = (float) this.particleTextureIndexY / 16.0F;
		float f9 = f8 + 0.0624375F;
		float f10 = 0.1F * this.particleScale;

		if( this.particleIcon != null )
		{
			f6 = this.particleIcon.getMinU();
			f7 = this.particleIcon.getMaxU();
			f8 = this.particleIcon.getMinV();
			f9 = this.particleIcon.getMaxV();
		}

		float f11 = (float) (this.prevPosX + (this.posX - this.prevPosX) * (double) p_70539_2_ - interpPosX);
		float f12 = (float) (this.prevPosY + (this.posY - this.prevPosY) * (double) p_70539_2_ - interpPosY);
		float f13 = (float) (this.prevPosZ + (this.posZ - this.prevPosZ) * (double) p_70539_2_ - interpPosZ);
		p_70539_1_.setColorRGBA_F(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha);
		p_70539_1_.addVertexWithUV((double) (f11 - p_70539_3_ * f10 - p_70539_6_ * f10), (double) (f12 - p_70539_4_ * f10), (double) (f13 - p_70539_5_ * f10 - p_70539_7_ * f10), (double) f7, (double) f9);
		p_70539_1_.addVertexWithUV((double) (f11 - p_70539_3_ * f10 + p_70539_6_ * f10), (double) (f12 + p_70539_4_ * f10), (double) (f13 - p_70539_5_ * f10 + p_70539_7_ * f10), (double) f7, (double) f8);
		p_70539_1_.addVertexWithUV((double) (f11 + p_70539_3_ * f10 + p_70539_6_ * f10), (double) (f12 + p_70539_4_ * f10), (double) (f13 + p_70539_5_ * f10 + p_70539_7_ * f10), (double) f6, (double) f8);
		p_70539_1_.addVertexWithUV((double) (f11 + p_70539_3_ * f10 - p_70539_6_ * f10), (double) (f12 - p_70539_4_ * f10), (double) (f13 + p_70539_5_ * f10 - p_70539_7_ * f10), (double) f6, (double) f9);
	}
}

package uzammod.client.renderer;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import uzammod.UzaMonsterMod;
import uzammod.client.model.ModelFoolSkeleton;
import uzammod.common.entity.EntityFoolSkeleton;

@SideOnly(Side.CLIENT)
public class RenderFoolSkeleton extends RenderBiped
{
	private static final ResourceLocation skeletonTextures = new ResourceLocation( UzaMonsterMod.MODID, "textures/entity/fool_skeleton.png" );
//	private static final ResourceLocation witherSkeletonTextures = new ResourceLocation( "textures/entity/skeleton/wither_skeleton.png" );
	private static final String __OBFID = "CL_00001023";

	public RenderFoolSkeleton()
	{
		super( new ModelFoolSkeleton(), 0.5F );
	}

	/**
	 * Allows the render to do any OpenGL state modifications necessary before the model is rendered. Args:
	 * entityLiving, partialTickTime
	 */
	protected void preRenderCallback(EntityFoolSkeleton p_77041_1_, float p_77041_2_)
	{
		if( p_77041_1_.getSkeletonType() == 1 )
		{
			GL11.glScalef( 1.2F, 1.2F, 1.2F );
		}
	}

	protected void func_82422_c()
	{
		GL11.glTranslatef( 0.09375F, 0.1875F, 0.0F );
	}

	/**
	 * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
	 */
	protected ResourceLocation getEntityTexture(EntityFoolSkeleton p_110775_1_)
	{
		return skeletonTextures;//p_110775_1_.getSkeletonType() == 1 ? witherSkeletonTextures : skeletonTextures;
	}

	/**
	 * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
	 */
	protected ResourceLocation getEntityTexture(EntityLiving p_110775_1_)
	{
		return this.getEntityTexture( (EntityFoolSkeleton) p_110775_1_ );
	}

	/**
	 * Allows the render to do any OpenGL state modifications necessary before the model is rendered. Args:
	 * entityLiving, partialTickTime
	 */
	protected void preRenderCallback(EntityLivingBase p_77041_1_, float p_77041_2_)
	{
		this.preRenderCallback( (EntityFoolSkeleton) p_77041_1_, p_77041_2_ );
	}

	/**
	 * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
	 */
	protected ResourceLocation getEntityTexture(Entity p_110775_1_)
	{
		return this.getEntityTexture( (EntityFoolSkeleton) p_110775_1_ );
	}
}

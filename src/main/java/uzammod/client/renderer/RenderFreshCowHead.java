package uzammod.client.renderer;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import uzammod.UzaMonsterMod;
import uzammod.common.entity.EntityFreshCowHead;

@SideOnly(Side.CLIENT)
public class RenderFreshCowHead extends Render
{
	private static final ResourceLocation cowTextures = new ResourceLocation(UzaMonsterMod.MODID, "textures/entity/fresh_cow.png");

	final ModelBase model;

	public RenderFreshCowHead(ModelBase p_i1253_1_, float p_i1253_2_)
	{
		super();
		this.model = p_i1253_1_;
	}

	/**
	 * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
	 */
	protected ResourceLocation getEntityTexture(EntityFreshCowHead p_110775_1_)
	{
		return cowTextures;
	}

	/**
	 * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
	 */
	protected ResourceLocation getEntityTexture(Entity p_110775_1_)
	{
		return this.getEntityTexture((EntityFreshCowHead) p_110775_1_);
	}

	@Override
	public void doRender(Entity p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_)
	{
		GL11.glPushMatrix();
		GL11.glTranslatef((float) p_76986_2_, (float) p_76986_4_, (float) p_76986_6_);
		GL11.glRotatef(180.0F - p_76986_8_, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(90, 1.0F, 0.0F, 0.0F);
		float f2 = (float) 0 - p_76986_9_;
		float f3 = 0 - p_76986_9_;

		if( f3 < 0.0F )
		{
			f3 = 0.0F;
		}

		if( f2 > 0.0F )
		{
		//	GL11.glRotatef(MathHelper.sin(f2) * f2 * f3 / 10.0F * (float) p_76986_1_.getForwardDirection(), 1.0F, 0.0F, 0.0F);
		}

		float f4 = 0.75F;
		GL11.glScalef(f4, f4, f4);
		GL11.glScalef(1.0F / f4, 1.0F / f4, 1.0F / f4);
		this.bindEntityTexture(p_76986_1_);
		GL11.glScalef(-1.0F, -1.0F, 1.0F);
		this.model.render(p_76986_1_, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
		GL11.glPopMatrix();
	}
}

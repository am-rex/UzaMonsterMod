package uzammod.client.renderer;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import uzammod.common.entity.EntityGreenSpider;

@SideOnly(Side.CLIENT)
public class ModelGreenSpider extends ModelBase
{
	public ModelRenderer spiderHead;
	public ModelRenderer spiderNeck;
	public ModelRenderer spiderBody;
	public ModelRenderer spiderLegFR2;
	public ModelRenderer spiderLegRL2;
	public ModelRenderer spiderLegFR3;
	public ModelRenderer spiderLegRL3;
	public ModelRenderer spiderLegRR3;
	public ModelRenderer spiderLegFL3;
	public ModelRenderer spiderLegRR2;
	public ModelRenderer spiderLegFL2;

	public ModelRenderer spiderLegFR1;
	public ModelRenderer spiderLegRL1;
	public ModelRenderer spiderLegRR1;
	public ModelRenderer spiderLegFL1;

	public ModelGreenSpider()
	{
		float f = 0.0F;
		byte b0 = 15;
		this.spiderHead = new ModelRenderer(this, 32, 4);
		this.spiderHead.addBox(-4.0F, -4.0F, -8.0F, 8, 8, 8, f);
		this.spiderHead.setRotationPoint(0.0F, (float) b0, -3.0F);
		this.spiderNeck = new ModelRenderer(this, 0, 0);
		this.spiderNeck.addBox(-3.0F, -3.0F, -3.0F, 6, 6, 6, f);
		this.spiderNeck.setRotationPoint(0.0F, (float) b0, 0.0F);
		this.spiderBody = new ModelRenderer(this, 0, 12);
		this.spiderBody.addBox(-5.0F, -4.0F, -6.0F, 10, 8, 12, f);
		this.spiderBody.setRotationPoint(0.0F, (float) b0, 9.0F);

		this.spiderLegFR2 = new ModelRenderer(this, 18, 0);
		this.spiderLegFR2.addBox(-15.0F, -1.0F, -1.0F, 16, 2, 2, f);
		this.spiderLegFR2.setRotationPoint(-4.0F, (float) b0, 2.0F);
		this.spiderLegRL2 = new ModelRenderer(this, 18, 0);
		this.spiderLegRL2.addBox(-1.0F, -1.0F, -1.0F, 16, 2, 2, f);
		this.spiderLegRL2.setRotationPoint(4.0F, (float) b0, 2.0F);

		this.spiderLegFR3 = new ModelRenderer(this, 18, 0);
		this.spiderLegFR3.addBox(-15.0F, -1.0F, -1.0F, 16, 2, 2, f);
		this.spiderLegFR3.setRotationPoint(-4.0F, (float) b0, 1.0F);
		this.spiderLegRL3 = new ModelRenderer(this, 18, 0);
		this.spiderLegRL3.addBox(-1.0F, -1.0F, -1.0F, 16, 2, 2, f);
		this.spiderLegRL3.setRotationPoint(4.0F, (float) b0, 1.0F);
		this.spiderLegRR3 = new ModelRenderer(this, 18, 0);
		this.spiderLegRR3.addBox(-15.0F, -1.0F, -1.0F, 16, 2, 2, f);
		this.spiderLegRR3.setRotationPoint(-4.0F, (float) b0, 0.0F);
		this.spiderLegFL3 = new ModelRenderer(this, 18, 0);
		this.spiderLegFL3.addBox(-1.0F, -1.0F, -1.0F, 16, 2, 2, f);
		this.spiderLegFL3.setRotationPoint(4.0F, (float) b0, 0.0F);

		this.spiderLegRR2 = new ModelRenderer(this, 18, 0);
		this.spiderLegRR2.addBox(-15.0F, -1.0F, -1.0F, 16, 2, 2, f);
		this.spiderLegRR2.setRotationPoint(-4.0F, (float) b0, -1.0F);
		this.spiderLegFL2 = new ModelRenderer(this, 18, 0);
		this.spiderLegFL2.addBox(-1.0F, -1.0F, -1.0F, 16, 2, 2, f);
		this.spiderLegFL2.setRotationPoint(4.0F, (float) b0, -1.0F);

		this.spiderLegFR1 = new ModelRenderer(this, 18, 0);
		this.spiderLegFR1.addBox(-15.0F, -1.0F, -1.0F, 16, 2, 2, f);
		this.spiderLegFR1.setRotationPoint(-4.0F, (float) b0, 1.0F);
		this.spiderLegRL1 = new ModelRenderer(this, 18, 0);
		this.spiderLegRL1.addBox(-1.0F, -1.0F, -1.0F, 16, 2, 2, f);
		this.spiderLegRL1.setRotationPoint(4.0F, (float) b0, 1.0F);
		this.spiderLegRR1 = new ModelRenderer(this, 18, 0);
		this.spiderLegRR1.addBox(-15.0F, -1.0F, -1.0F, 16, 2, 2, f);
		this.spiderLegRR1.setRotationPoint(-4.0F, (float) b0, 0.0F);
		this.spiderLegFL1 = new ModelRenderer(this, 18, 0);
		this.spiderLegFL1.addBox(-1.0F, -1.0F, -1.0F, 16, 2, 2, f);
		this.spiderLegFL1.setRotationPoint(4.0F, (float) b0, 0.0F);

		this.spiderLegFR1.addChild(this.spiderLegFR2);
		this.spiderLegRR1.addChild(this.spiderLegRR2);
		this.spiderLegFL1.addChild(this.spiderLegFL2);
		this.spiderLegRL1.addChild(this.spiderLegRL2);

		this.spiderLegFR2.addChild(this.spiderLegFR3);
		this.spiderLegRR2.addChild(this.spiderLegRR3);
		this.spiderLegFL2.addChild(this.spiderLegFL3);
		this.spiderLegRL2.addChild(this.spiderLegRL3);

	}

	/**
	 * Sets the models various rotation angles then renders the model.
	 */
	public void render(Entity p_78088_1_, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_,
			float p_78088_6_, float p_78088_7_)
	{
		EntityGreenSpider spider = (EntityGreenSpider) p_78088_1_;
		GL11.glPushMatrix();
		GL11.glTranslated(0, -0.1, 0);
		GL11.glScaled(1.2, 1.2, 1.2);

		if( spider.friendSpawn && spider.ticksExisted < 10 )
		{
			GL11.glRotated( -90 + (spider.ticksExisted * 9), 1, 0, 0);
		}
		else if( spider.isDuringDespawn() )
		{
			GL11.glRotated(spider.despawnCount * 9, 1, 0, 0);
		}

		this.setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, p_78088_7_, p_78088_1_);
		this.spiderHead.render(p_78088_7_);
		this.spiderNeck.render(p_78088_7_);
		this.spiderBody.render(p_78088_7_);
		this.spiderLegFR1.render(p_78088_7_);
		this.spiderLegRR1.render(p_78088_7_);
		this.spiderLegFL1.render(p_78088_7_);
		this.spiderLegRL1.render(p_78088_7_);
		GL11.glPopMatrix();
	}

	/**
	 * Sets the model's various rotation angles. For bipeds, par1 and par2 are used for animating the movement of arms
	 * and legs, where par1 represents the time(so that arms and legs swing back and forth) and par2 represents how
	 * "far" arms and legs can swing at most.
	 */
	public void setRotationAngles(float p_78087_1_, float p_78087_2_, float p_78087_3_, float p_78087_4_,
			float p_78087_5_, float p_78087_6_, Entity p_78087_7_)
	{
		this.spiderHead.rotateAngleY = 0;//p_78087_4_ / (180F / (float) Math.PI);
		this.spiderHead.rotateAngleX = p_78087_5_ / (180F / (float) Math.PI);
		this.spiderBody.rotateAngleX = 40 / (180F / (float) Math.PI);
		this.spiderBody.offsetY = -0.2F;
		float f6 = ((float) Math.PI / 4F);
		float f8 = 0.3926991F;

		this.spiderLegFL1.offsetY = -0.3F;
		this.spiderLegFL1.offsetZ =  0.9F;
		this.spiderLegFL1.rotateAngleY =  1.1F;
		this.spiderLegFL1.rotateAngleZ =  f6 * 0.7F;
		this.spiderLegFL2.offsetX =  0.6F;
		this.spiderLegFL2.offsetY = -0.9F;
		this.spiderLegFL2.offsetZ =  0.1F;
		this.spiderLegFL2.rotateAngleY = f8 * 2.0F;
		this.spiderLegFL2.rotateAngleZ = -0.5F;
		this.spiderLegFL3.offsetX =  0.6F;
		this.spiderLegFL3.offsetY = -0.9F;
		this.spiderLegFL3.rotateAngleY = -f8 * 1.0F;
		this.spiderLegFL3.rotateAngleZ = f6 * 2.3F;

		this.spiderLegFR1.offsetY = -0.3F;
		this.spiderLegFR1.offsetZ =  0.8F;
		this.spiderLegFR1.rotateAngleY  = -1.1F;
		this.spiderLegFR1.rotateAngleZ = -f6 * 0.7F;
		this.spiderLegFR2.offsetX = -0.6F;
		this.spiderLegFR2.offsetY = -0.9F;
		this.spiderLegFR2.offsetZ = -0.1F;
		this.spiderLegFR2.rotateAngleY = -f8 * 2.0F;
		this.spiderLegFR2.rotateAngleZ =  0.5F;
		this.spiderLegFR3.offsetX = -0.6F;
		this.spiderLegFR3.offsetY = -0.9F;
		this.spiderLegFR3.rotateAngleY =  f8 * 1.0F;
		this.spiderLegFR3.rotateAngleZ = -f6 * 2.3F;

		this.spiderLegRL1.offsetX = -0.2F;
		this.spiderLegRL1.offsetY = -0.1F;
		this.spiderLegRL1.offsetZ =  0.1F;
		this.spiderLegRL1.rotateAngleY =  0.7F;
		this.spiderLegRL1.rotateAngleZ =  0.3F;
		this.spiderLegRL2.offsetX =  0.7F;
		this.spiderLegRL2.offsetY = -0.9F;
		this.spiderLegRL2.offsetZ = -0.1F;
		this.spiderLegRL2.rotateAngleX = -0.5F;
		this.spiderLegRL2.rotateAngleY = -2.5F;
		this.spiderLegRL2.rotateAngleZ =  1.0F;
		this.spiderLegRL3.offsetX =  0.6F;
		this.spiderLegRL3.offsetY = -0.9F;
		this.spiderLegRL3.offsetZ = -0.1F;
		this.spiderLegRL3.rotateAngleY =  0.6F;
		this.spiderLegRL3.rotateAngleZ =  f6 * 2.3F;

		this.spiderLegRR1.offsetX =  0.2F;
		this.spiderLegRR1.offsetY = -0.1F;
		this.spiderLegRR1.offsetZ =  0.1F;
		this.spiderLegRR1.rotateAngleY = -0.7F;
		this.spiderLegRR1.rotateAngleZ = -0.3F;
		this.spiderLegRR2.offsetX = -0.7F;
		this.spiderLegRR2.offsetY = -0.9F;
		this.spiderLegRR2.offsetZ =  0.1F;
		this.spiderLegRR2.rotateAngleX = -0.5F;
		this.spiderLegRR2.rotateAngleY =  2.5F;
		this.spiderLegRR2.rotateAngleZ = -1.0F;
		this.spiderLegRR3.offsetX = -0.6F;
		this.spiderLegRR3.offsetY = -0.9F;
		this.spiderLegRR3.offsetZ = -0.0F;
		this.spiderLegRR3.rotateAngleY = -0.6F;
		this.spiderLegRR3.rotateAngleZ = -f6 * 2.3F;

		float f9  = -(MathHelper.cos(p_78087_1_ * 0.6662F * 2.0F) * 0.4F) * p_78087_2_;
		float f10 = -(MathHelper.cos(p_78087_1_ * 0.6662F * 2.0F + (float) Math.PI) * 0.4F) * p_78087_2_;
		float f11 = -(MathHelper.cos(p_78087_1_ * 0.6662F * 2.0F + ((float) Math.PI / 2F)) * 0.4F) * p_78087_2_;
		float f12 = -(MathHelper.cos(p_78087_1_ * 0.6662F * 2.0F + ((float) Math.PI * 3F / 2F)) * 0.4F) * p_78087_2_;
		float f13 = Math.abs(MathHelper.sin(p_78087_1_ * 0.6662F) * 0.4F) * p_78087_2_;
		float f14 = Math.abs(MathHelper.sin(p_78087_1_ * 0.6662F + (float) Math.PI) * 0.4F) * p_78087_2_;
		float f15 = Math.abs(MathHelper.sin(p_78087_1_ * 0.6662F + ((float) Math.PI / 2F)) * 0.4F) * p_78087_2_;
		float f16 = Math.abs(MathHelper.sin(p_78087_1_ * 0.6662F + ((float) Math.PI * 3F / 2F)) * 0.4F) * p_78087_2_;

		/*
		this.spiderLegFR2.rotateAngleY += f9;
		this.spiderLegRL2.rotateAngleY += -f9;
		this.spiderLegFR3.rotateAngleY += f9;
		this.spiderLegRL3.rotateAngleY += -f9;
		this.spiderLegRR3.rotateAngleY += f11;
		this.spiderLegFL3.rotateAngleY += -f11;
		this.spiderLegRR2.rotateAngleY += f12;
		this.spiderLegFL2.rotateAngleY += -f12;
		this.spiderLegFR2.rotateAngleZ += f13;
		this.spiderLegRL2.rotateAngleZ += -f13;
		this.spiderLegFR3.rotateAngleZ += f14;
		this.spiderLegRL3.rotateAngleZ += -f14;
		this.spiderLegRR3.rotateAngleZ += f15;
		this.spiderLegFL3.rotateAngleZ += -f15;
		this.spiderLegRR2.rotateAngleZ += f16;
		this.spiderLegFL2.rotateAngleZ += -f16;
		*/
		this.spiderLegFL1.rotateAngleY += -f11;
		this.spiderLegFL1.rotateAngleZ += -f15;
		this.spiderLegFR1.rotateAngleY +=  f9;
		this.spiderLegFR1.rotateAngleZ +=  f14;
		this.spiderLegRL1.rotateAngleY += -f9;
		this.spiderLegRL1.rotateAngleZ += -f14;
		this.spiderLegRR1.rotateAngleY +=  f11;
		this.spiderLegRR1.rotateAngleZ +=  f15;
	}
}

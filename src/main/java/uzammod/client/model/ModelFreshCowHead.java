package uzammod.client.model;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import uzammod.common.entity.EntityFreshCowHead;

@SideOnly(Side.CLIENT)
public class ModelFreshCowHead extends ModelBase
{
	public ModelRenderer head = new ModelRenderer(this, 0, 0);

	public ModelFreshCowHead()
	{
		super();
		this.head = new ModelRenderer(this, 0, 0);
		this.head.addBox(-4.0F, -4.0F, -6.0F, 8, 8, 6, 0.0F);
		this.head.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.head.setTextureOffset(22, 0).addBox(-5.0F, -5.0F, -4.0F, 1, 3, 1, 0.0F);
		this.head.setTextureOffset(22, 0).addBox(4.0F, -5.0F, -4.0F, 1, 3, 1, 0.0F);
	}

	/**
	 * Sets the models various rotation angles then renders the model.
	 */
	public void render(Entity p_78088_1_, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float p_78088_7_)
	{
		EntityFreshCowHead cow = (EntityFreshCowHead) p_78088_1_;
		this.setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, p_78088_7_, p_78088_1_);

		if( this.isChild )
		{
			float f6 = 2.0F;
			GL11.glPushMatrix();
			this.head.render(p_78088_7_);
			GL11.glPopMatrix();
			GL11.glPushMatrix();
			GL11.glScalef(1.0F / f6, 1.0F / f6, 1.0F / f6);
			GL11.glTranslatef(0.0F, 24.0F * p_78088_7_, 0.0F);
			GL11.glPopMatrix();
		}
		else
		{
			this.head.render(p_78088_7_);
		}
	}

	public void setRotationAngles(float p_78087_1_, float p_78087_2_, float p_78087_3_, float p_78087_4_, float p_78087_5_, float p_78087_6_, Entity p_78087_7_)
	{
		this.head.rotateAngleY = p_78087_4_ / (180F / (float)Math.PI);

		float f6 = (MathHelper.sin(p_78087_1_ * 0.02F) * 0.1F + 1.25F) * p_78087_4_;



		/*
		this.coverRight.rotateAngleY = (float) Math.PI + f6;
		this.coverLeft.rotateAngleY = -f6;
		this.pagesRight.rotateAngleY = f6;
		this.pagesLeft.rotateAngleY = -f6;
		this.flippingPageRight.rotateAngleY = f6 - f6 * 2.0F * p_78087_2_;
		this.flippingPageLeft.rotateAngleY = f6 - f6 * 2.0F * p_78087_3_;
		this.pagesRight.rotationPointX = MathHelper.sin(f6);
		this.pagesLeft.rotationPointX = MathHelper.sin(f6);
		this.flippingPageRight.rotationPointX = MathHelper.sin(f6);
		this.flippingPageLeft.rotationPointX = MathHelper.sin(f6);

        this.head.rotateAngleX = p_78087_5_ / (180F / (float)Math.PI);
        this.head.rotateAngleY = p_78087_4_ / (180F / (float)Math.PI);
        this.bill.rotateAngleX = this.head.rotateAngleX;
        this.bill.rotateAngleY = this.head.rotateAngleY;
        this.chin.rotateAngleX = this.head.rotateAngleX;
        this.chin.rotateAngleY = this.head.rotateAngleY;
        this.body.rotateAngleX = ((float)Math.PI / 2F);
        this.rightLeg.rotateAngleX = MathHelper.cos(p_78087_1_ * 0.6662F) * 1.4F * p_78087_2_;
        this.leftLeg.rotateAngleX = MathHelper.cos(p_78087_1_ * 0.6662F + (float)Math.PI) * 1.4F * p_78087_2_;
        this.rightWing.rotateAngleZ = p_78087_3_;
        this.leftWing.rotateAngleZ = -p_78087_3_;
		*/
	}
}

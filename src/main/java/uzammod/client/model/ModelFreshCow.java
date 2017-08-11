package uzammod.client.model;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelQuadruped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import uzammod.common.entity.EntityFreshCow;

@SideOnly(Side.CLIENT)
public class ModelFreshCow extends ModelQuadruped
{
	public ModelFreshCow()
	{
		super(12, 0.0F);
		this.head = new ModelRenderer(this, 0, 0);
		this.head.addBox(-4.0F, -4.0F, -6.0F, 8, 8, 6, 0.0F);
		this.head.setRotationPoint(0.0F, 4.0F, -8.0F);
		this.head.setTextureOffset(22, 0).addBox(-5.0F, -5.0F, -4.0F, 1, 3, 1, 0.0F);
		this.head.setTextureOffset(22, 0).addBox(4.0F, -5.0F, -4.0F, 1, 3, 1, 0.0F);
		this.body = new ModelRenderer(this, 18, 4);
		this.body.addBox(-6.0F, -10.0F, -7.0F, 12, 18, 10, 0.0F);
		this.body.setRotationPoint(0.0F, 5.0F, 2.0F);
		this.body.setTextureOffset(52, 0).addBox(-2.0F, 2.0F, -8.0F, 4, 6, 1);
		--this.leg1.rotationPointX;
		++this.leg2.rotationPointX;
		this.leg1.rotationPointZ += 0.0F;
		this.leg2.rotationPointZ += 0.0F;
		--this.leg3.rotationPointX;
		++this.leg4.rotationPointX;
		--this.leg3.rotationPointZ;
		--this.leg4.rotationPointZ;
		this.field_78151_h += 2.0F;
	}

	/**
	 * Sets the models various rotation angles then renders the model.
	 */
	public void render(Entity p_78088_1_, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float p_78088_7_)
	{
		EntityFreshCow cow = (EntityFreshCow) p_78088_1_;
		this.setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, p_78088_7_, p_78088_1_);

		if( this.isChild )
		{
			float f6 = 2.0F;
			GL11.glPushMatrix();
			GL11.glTranslatef(0.0F, this.field_78145_g * p_78088_7_, this.field_78151_h * p_78088_7_);
			if(cow.hasHead())
			{
				this.head.render(p_78088_7_);
			}
			GL11.glPopMatrix();
			GL11.glPushMatrix();
			GL11.glScalef(1.0F / f6, 1.0F / f6, 1.0F / f6);
			GL11.glTranslatef(0.0F, 24.0F * p_78088_7_, 0.0F);
			this.body.render(p_78088_7_);
			this.leg1.render(p_78088_7_);
			this.leg2.render(p_78088_7_);
			this.leg3.render(p_78088_7_);
			this.leg4.render(p_78088_7_);
			GL11.glPopMatrix();
		}
		else
		{
			if(cow.hasHead())
			{
				this.head.render(p_78088_7_);
			}
			this.body.render(p_78088_7_);
			this.leg1.render(p_78088_7_);
			this.leg2.render(p_78088_7_);
			this.leg3.render(p_78088_7_);
			this.leg4.render(p_78088_7_);
		}
	}
}

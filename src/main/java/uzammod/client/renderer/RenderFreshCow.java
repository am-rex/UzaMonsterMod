package uzammod.client.renderer;


import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import uzammod.UzaMonsterMod;
import uzammod.common.entity.EntityFreshCow;

@SideOnly(Side.CLIENT)
public class RenderFreshCow extends RenderLiving
{
    private static final ResourceLocation cowTextures = new ResourceLocation(UzaMonsterMod.MODID, "textures/entity/fresh_cow.png");

    public RenderFreshCow(ModelBase p_i1253_1_, float p_i1253_2_)
    {
        super(p_i1253_1_, p_i1253_2_);
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(EntityFreshCow p_110775_1_)
    {
        return cowTextures;
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(Entity p_110775_1_)
    {
        return this.getEntityTexture((EntityFreshCow)p_110775_1_);
    }
}
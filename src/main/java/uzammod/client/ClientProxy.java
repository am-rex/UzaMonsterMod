package uzammod.client;

import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.init.Items;
import net.minecraft.world.World;
import uzammod.client.particle.EntityExplodeFX;
import uzammod.client.renderer.RenderAcid;
import uzammod.client.renderer.RenderBreaker;
import uzammod.client.renderer.RenderGreenSpider;
import uzammod.client.renderer.RenderKiller;
import uzammod.client.renderer.RenderSmallSpider;
import uzammod.common.CommonProxy;
import uzammod.common.entity.EntityAcid;
import uzammod.common.entity.EntityBreaker;
import uzammod.common.entity.EntityGreenSpider;
import uzammod.common.entity.EntityKiller;
import uzammod.common.entity.EntitySmallSpider;

public class ClientProxy extends CommonProxy
{
	public void registerRender()
	{
		RenderingRegistry.registerEntityRenderingHandler(EntityBreaker.class, new RenderBreaker());
		RenderingRegistry.registerEntityRenderingHandler(EntityKiller.class, new RenderKiller());
		RenderingRegistry.registerEntityRenderingHandler(EntitySmallSpider.class, new RenderSmallSpider());
		RenderingRegistry.registerEntityRenderingHandler(EntityGreenSpider.class, new RenderGreenSpider());

		RenderingRegistry.registerEntityRenderingHandler(EntityAcid.class, new RenderAcid(Items.slime_ball));
	}

	public String SideStr()
	{
		return "Client";
	}

	public EntityFX createEntity(World world, String name, double px, double py, double pz, double mx, double my, double mz)
	{
		EntityFX fx = null;

		if( name.equals("explode") )
		{
			fx = new EntityExplodeFX(world, px, py, pz, mx, my, mz);
		}

		return fx;
	}

	public void spawnParticle(World world, String name, double px, double py, double pz, double mx, double my, double mz, float a, float r, float g, float b)
	{
		EntityFX fx = createEntity(world, name, px, py, pz, mx, my, mz);

		if( fx != null )
		{
			fx.setAlphaF(a);
			fx.setRBGColorF(r,g,b);
			Minecraft.getMinecraft().effectRenderer.addEffect(fx);
		}
	}

	public void spawnParticle(World world, String name, double px, double py, double pz, double mx, double my, double mz)
	{
		EntityFX fx = createEntity(world, name, px, py, pz, mx, my, mz);

		if( fx != null )
		{
			Minecraft.getMinecraft().effectRenderer.addEffect(fx);
		}
	}
}

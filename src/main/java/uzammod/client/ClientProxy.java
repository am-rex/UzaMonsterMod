package uzammod.client;

import cpw.mods.fml.client.registry.RenderingRegistry;
import uzammod.common.CommonProxy;
import uzammod.common.EntityBreaker;
import uzammod.common.EntityKiller;
import uzammod.common.EntitySmallSpider;

public class ClientProxy extends CommonProxy
{
	public void registerRender()
	{
		RenderingRegistry.registerEntityRenderingHandler(EntityBreaker.class, new RenderBreaker());
		RenderingRegistry.registerEntityRenderingHandler(EntityKiller.class, new RenderKiller());
		RenderingRegistry.registerEntityRenderingHandler(EntitySmallSpider.class, new RenderSmallSpider());
	}
}

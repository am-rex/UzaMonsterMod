package uzammod.client;

import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.init.Items;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;
import uzammod.UzaMonsterMod;
import uzammod.client.model.ModelFreshCow;
import uzammod.client.model.ModelFreshCowHead;
import uzammod.client.particle.EntityDropParticleFX;
import uzammod.client.particle.EntityExplodeFX;
import uzammod.client.renderer.ItemRkBowRenderer;
import uzammod.client.renderer.RenderBreaker;
import uzammod.client.renderer.RenderEntityItem;
import uzammod.client.renderer.RenderFoolSkeleton;
import uzammod.client.renderer.RenderFreshCow;
import uzammod.client.renderer.RenderFreshCowHead;
import uzammod.client.renderer.RenderGreenSpider;
import uzammod.client.renderer.RenderKiller;
import uzammod.client.renderer.RenderReverseKnockbackArrow;
import uzammod.client.renderer.RenderRocketCreeper;
import uzammod.client.renderer.RenderSmallSpider;
import uzammod.common.CommonProxy;
import uzammod.common.entity.EntityBreaker;
import uzammod.common.entity.EntityFoolSkeleton;
import uzammod.common.entity.EntityFreshCow;
import uzammod.common.entity.EntityFreshCowHead;
import uzammod.common.entity.EntityGreenSpider;
import uzammod.common.entity.EntityKiller;
import uzammod.common.entity.EntityRocketCreeper;
import uzammod.common.entity.EntitySmallSpider;
import uzammod.common.entity.projectile.EntityAcid;
import uzammod.common.entity.projectile.EntityPheromone;
import uzammod.common.entity.projectile.EntityReverseKnockbackArrow;

public class ClientProxy extends CommonProxy
{
	public void registerRender()
	{
		RenderingRegistry.registerEntityRenderingHandler(EntityBreaker.class, new RenderBreaker());
		RenderingRegistry.registerEntityRenderingHandler(EntityKiller.class, new RenderKiller());
		RenderingRegistry.registerEntityRenderingHandler(EntitySmallSpider.class, new RenderSmallSpider());
		RenderingRegistry.registerEntityRenderingHandler(EntityGreenSpider.class, new RenderGreenSpider());
		RenderingRegistry.registerEntityRenderingHandler(EntityRocketCreeper.class, new RenderRocketCreeper());
		RenderingRegistry.registerEntityRenderingHandler(EntityFreshCow.class, new RenderFreshCow(new ModelFreshCow(), 1.0F));
		RenderingRegistry.registerEntityRenderingHandler(EntityFreshCowHead.class, new RenderFreshCowHead(new ModelFreshCowHead(), 1.0F));
		RenderingRegistry.registerEntityRenderingHandler(EntityFoolSkeleton.class, new RenderFoolSkeleton());

		RenderingRegistry.registerEntityRenderingHandler(EntityAcid.class, new RenderEntityItem(Items.slime_ball));
		RenderingRegistry.registerEntityRenderingHandler(EntityPheromone.class, new RenderEntityItem(UzaMonsterMod.itemPheromone));
		RenderingRegistry.registerEntityRenderingHandler(EntityReverseKnockbackArrow.class, new RenderReverseKnockbackArrow());

		MinecraftForgeClient.registerItemRenderer( UzaMonsterMod.itemRkBow, new ItemRkBowRenderer() );
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
		else if( name.equals("dripLava") )
		{
			fx = new EntityDropParticleFX(world, px, py, pz, Material.lava);
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

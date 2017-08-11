package uzammod;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import uzammod.common.entity.EntityItem2;

public class EventHook
{
	@SubscribeEvent
	public void onEvent_LivingDeathEvent(LivingDeathEvent event)
	{
		if( !event.entity.worldObj.isRemote )
		{
			int n = event.entity.worldObj.rand.nextInt( 1 + event.entity.worldObj.difficultySetting.ordinal() );
			for( int i = 0; i < n; i++ )
			{
				EntityItem entityitem = new EntityItem2( event.entity.worldObj,
					event.entity.posX, event.entity.posY + 1, event.entity.posZ,
					new ItemStack( UzaMonsterMod.itemHh2o, 1 ) );

				entityitem.delayBeforeCanPickup = 10;

				event.entity.worldObj.spawnEntityInWorld( entityitem );
			}
		}
	}

}

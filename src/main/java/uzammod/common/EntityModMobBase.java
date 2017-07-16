package uzammod.common;

import net.minecraft.entity.monster.EntityMob;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntityModMobBase extends EntityMob
{
	public EntityModMobBase(World p_i1738_1_)
	{
		super(p_i1738_1_);
	}

	public ItemStack getPickedResult(MovingObjectPosition target)
	{
		ItemStack item = ItemModMonsterPlacer.getItemStackFromEntity(this);
		return item!=null? item: super.getPickedResult(target);
	}
}

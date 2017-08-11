package uzammod.common.entity;

import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import uzammod.common.item.ItemModMonsterPlacer;

public abstract class EntityModAnimalBase extends EntityAnimal
{
	public EntityModAnimalBase(World p_i1681_1_)
	{
		super(p_i1681_1_);
	}

	public ItemStack getPickedResult(MovingObjectPosition target)
	{
		ItemStack item = ItemModMonsterPlacer.getItemStackFromEntity(this);
		return item != null ? item : super.getPickedResult(target);
	}
}

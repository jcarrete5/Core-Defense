package com.jasonalexllc.tower;

import java.awt.Image;
import com.jasonalexllc.level.Mob;
import com.jasonalexllc.main.CoreDefense;
import com.jasonalexllc.main.Tile;

/**
 * Basic tower that throws pickaxes at its foes
 * @author Jason Carrete, Alex Berman
 * @since Jun 19, 2014
 */
public class PickaxeTower extends Tower
{
	private static final long serialVersionUID = -5222779550089892274L;
	
	public PickaxeTower()
	{
		super(2, 5, .01, 250, CoreDefense.sprites[0][CoreDefense.TOWER]);
	}
	
	public void attack(Mob m, Tile tile)
	{		
		if
		(
			m.getX() <= tile.getX() + super.getRangePixels() + 49 &&
			m.getX() >= tile.getX() - super.getRangePixels() &&
			m.getY() <= tile.getY() + super.getRangePixels() + 49 &&
			m.getY() >= tile.getY() - super.getRangePixels() &&
			m.isAlive()
		)
		{
			//throw a new Attack
			if((int)atk == 1)
			{
				this.setImage(CoreDefense.sprites[1][CoreDefense.TOWER]);
				
				attackQueue.add(new Attack(new Image[] {CoreDefense.sprites[0][CoreDefense.ATTACK]}, tile.getX(), tile.getY(), m));
			}
			
			atk = atk < 1 ? atk + atkSpeed : atk - 1.0;
			
			if(atk >= 0.25)
				this.setImage(CoreDefense.sprites[0][CoreDefense.TOWER]);
			
			//remove attacks from the attackQueue that are off the screen
			for(Attack atk : attackQueue.toArray(new Attack[attackQueue.size()])) //convert to a regular array to avoid ConcurrentModificationException
				if(atk.getX() + 25 < 0 || atk.getX() >= 800 || atk.getY() + 25 < 0 || atk.getY() >= 800)
					attackQueue.remove(atk);
				else if(atk.hasHitMob() && m.isAlive())
				{
					attackQueue.remove(atk);
					m.hit();
				}
		}
		else
			this.setImage(CoreDefense.sprites[0][CoreDefense.TOWER]);
	}
	
	public Tower getInstance()
	{
		return new PickaxeTower();
	}
	
	public String getName()
	{
		return "Pickaxe Tower";
	}
	
	public String getDesc()
	{
		return "This tower will throw pickaxes at its foes.";
	}
}

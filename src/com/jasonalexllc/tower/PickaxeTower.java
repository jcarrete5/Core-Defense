package com.jasonalexllc.tower;

import java.awt.Graphics2D;
import com.jasonalexllc.main.Tile;
import com.jasonalexllc.mob.Mob;

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
		super(2, 5, .01, 250, "assets/pickaxeTower_idle.png");
	}
	
	public void attack(Mob m, Graphics2D g2, Tile tile)
	{		
		if
		(
			m.getX() <= tile.getX() + super.getRangePixels() + 51 &&
			m.getX() >= tile.getX() - super.getRangePixels() - 1 &&
			m.getY() <= tile.getY() + super.getRangePixels() + 51 &&
			m.getY() >= tile.getY() - super.getRangePixels() - 1
		)
		{
			//throw a new Attack
			if((int)atk == 1)
			{
				attackQueue.add(new Attack(new String[] {"assets/pickaxeP.png"}, tile.getX(), tile.getY(), m));
			}
			
			atk = atk < 1 ? atk + atkSpeed : 0;
		}
	}
	
	public Tower getInstance()
	{
		return new PickaxeTower();
	}
}

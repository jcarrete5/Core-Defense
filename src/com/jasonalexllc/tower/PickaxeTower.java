package com.jasonalexllc.tower;

import java.awt.Graphics2D;
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
		super(2, 5, .01, 250, "assets/towers/pickaxeTower_idle.png");
	}
	
	public void attack(Mob m, Graphics2D g2, Tile tile)
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
				this.setImage(CoreDefense.getImage("assets/towers/pickaxeTower_atk.png"));
				
				attackQueue.add(new Attack(new String[] {"assets/attacks/pickaxe_1.png", "assets/attacks/pickaxe_2.png", "assets/attacks/pickaxe_3.png",
						"assets/attacks/pickaxe_4.png"}, tile.getX(), tile.getY(), m));
			}
			
			if(imgSwap >= 0.2)
				this.setImage(CoreDefense.getImage("assets/towers/pickaxeTower_idle.png"));
			
			float f = 0.007f; //lower value = longer duration of the atk image
			imgSwap = imgSwap < 0.2 ? imgSwap += f : 0;
			
			atk = atk < 1 ? atk + atkSpeed : 0;
			
			//remove attacks from the attackQueue that are off the screen
			for(Attack atk : attackQueue.toArray(new Attack[attackQueue.size()])) //convert to a regular array to avoid ConcurrentModificationException
				if(atk.getX() + 25 < 0 || atk.getX() >= 800 || atk.getY() + 25 < 0 || atk.getY() >= 800)
					attackQueue.remove(atk);
				else if(atk.hasHitMob() && m.isAlive())
					m.hit();
		}
		else
			this.setImage(CoreDefense.getImage("assets/towers/pickaxeTower_idle.png"));
	}
	
	public Tower getInstance()
	{
		return new PickaxeTower();
	}
}

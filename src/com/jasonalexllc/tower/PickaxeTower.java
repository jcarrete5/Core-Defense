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
	
	private Projectile pickaxe;

	public PickaxeTower()
	{
		super(2, 5, .01, 250, "assets/pickaxeTower_idle.png");
		pickaxe = new Projectile(new String[] {"assets/pickaxeP.png"}, 1, 2);
	}
	
	public void attack(Mob m, Graphics2D g2, Tile tile)
	{
//		System.out.println("Mob's (x, y): (" + m.getX() + ", " + m.getY() + ")");
//		System.out.println("Tower's (x, y): (" + tile.getX() + ", " + tile.getY() + ")" + " range: " + tile.getTower().getRangePixels());
		
		if
		(
			m.getX() <= tile.getX() + super.getRangePixels() &&
			m.getX() >= tile.getX() - super.getRangePixels() &&
			m.getY() <= tile.getY() + super.getRangePixels() &&
			m.getY() >= tile.getY() - super.getRangePixels()
		)
		{
			if((int)atk == 1)
				pickaxe.anim(m, g2, tile);

			System.out.println("in rng");
			atk = atk < 1 ? atk + atkSpeed : 0;
		}
		
	}
	
	public Tower getInstance()
	{
		return new PickaxeTower();
	}
}

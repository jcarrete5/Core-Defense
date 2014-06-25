package com.jasonalexllc.tower;

import java.awt.Graphics2D;
import com.jasonalexllc.main.Tile;
import com.jasonalexllc.mob.Mob;

/**
 * A tower that swings its wrecking ball to attack foes
 * @author Jason Carrete
 * @since Jun 20, 2014
 */
public class CraneTower extends Tower
{
	private static final long serialVersionUID = -8196617486406243592L;

	public CraneTower()
	{
		super(1, 10, .01, 300, "assets/towers/crane_idle.png");
	}
	
	public void attack(Mob m, Graphics2D g2, Tile tile)
	{
		
	}
	
	public Tower getInstance()
	{
		return new CraneTower();
	}
}

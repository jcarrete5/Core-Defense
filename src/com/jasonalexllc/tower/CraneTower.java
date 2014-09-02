package com.jasonalexllc.tower;

import com.jasonalexllc.level.Mob;
import com.jasonalexllc.main.CoreDefense;
import com.jasonalexllc.main.Tile;

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
		super(1, 1, .01, 300, CoreDefense.sprites[2][CoreDefense.TOWER]);
	}
	
	public void attack(Mob m, Tile tile)
	{
		
	}
	
	public Tower getInstance()
	{
		return new CraneTower();
	}
	
	public String getName()
	{
		return "Crane Tower";
	}
	
	public String getDesc()
	{
		return "Redneck swings his giant wrecking ball at his foes.";
	}
}

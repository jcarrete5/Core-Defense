package com.jasonalexllc.tower;

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
		super(1, 10, .01, 300, "assets/crane_idle.png");
	}
	
	public void attack(int x, int y)
	{
		
	}
	
	public Tower getInstance()
	{
		return new CraneTower();
	}
}

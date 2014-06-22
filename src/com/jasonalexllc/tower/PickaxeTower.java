package com.jasonalexllc.tower;

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
		super(3, 5, .01, 250, "assets/pickaxeTower_idle.png");
	}
	
	public void attack(int x, int y)
	{
		
	}
	
	public Tower getInstance()
	{
		return new PickaxeTower();
	}
}

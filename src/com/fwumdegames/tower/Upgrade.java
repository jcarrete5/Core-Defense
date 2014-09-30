package com.fwumdegames.tower;

import javax.swing.ImageIcon;

/**
 * Characteristics about each upgrade for a specific tower
 * @author Jason Carrete, Alex Berman
 * @since Jun 19, 2014
 */
public class Upgrade extends ImageIcon
{
	private static final long serialVersionUID = 5295113993428296299L;
	
	private int rngInc, dmgInc, cost;
	private double atkSpeedInc;

	public Upgrade(int cost, int rngInc, int dmgInc, double atk, String imgPath)
	{
		super(Upgrade.class.getResource(imgPath));
		this.rngInc = rngInc;
		this.dmgInc = dmgInc;
		this.cost = cost;
		atkSpeedInc = atk;
	}
	
	public int getCost()
	{
		return cost;
	}
	
	public int getRngInc()
	{
		return rngInc;
	}
	
	public int getDmgInc()
	{
		return dmgInc;
	}
	
	public double getAtkSpeedInc()
	{
		return atkSpeedInc;
	}
}

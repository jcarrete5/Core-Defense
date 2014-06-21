package com.jasonalexllc.tower;

import java.awt.Graphics2D;
import javax.swing.ImageIcon;
import com.jasonalexllc.main.CoreDefense;
import com.jasonalexllc.main.Game;

/**
 * Abstract class that defines how all towers in the game should function
 * @author Jason Carrete, Alex Berman
 * @since Jun 18, 2014
 */
public abstract class Tower extends ImageIcon
{
	private static final long serialVersionUID = -3346762436378513557L;
	
	protected int range, dmg, cost, p1Next = -1, p2Next = -1;
	protected double atkSpeed;
	protected Upgrade[] path1, path2;
	
	/**
	 * 
	 * @param range
	 * @param dmg
	 * @param atkSpeed
	 * @param cost
	 * @param imgPath
	 */
	protected Tower(int range, int dmg, double atkSpeed, int cost, String imgPath)
	{
		super(CoreDefense.class.getResource(imgPath));
		this.range = range;
		this.dmg = dmg;
		this.atkSpeed = atkSpeed;
		this.cost = cost;
	}
	
	/**
	 * Fires the projectile at the specified coordinates
	 * @param x
	 * @param y
	 */
	public abstract void attack(int x, int y);
	
	/**
	 * 
	 * @param u
	 * @return true if the upgrade is completely, false if the upgrade can't be completed
	 */
	public boolean upgrade(int upgradePath)
	{
		Upgrade u = null;
		if(upgradePath == 1 && p1Next < path1.length - 1)
			u = path1[p1Next++];
		else if(upgradePath == 2 && p2Next < path2.length - 1)
			u = path2[p2Next++];
		
		if(u != null && u.getCost() <= Game.money)
		{
			range += u.getRngInc();
			dmg += u.getDmgInc();
			atkSpeed += u.getAtkSpeedInc();
			this.setImage(u.getImage());
			return true;
		}
		else
			return false;
	}
	
	public void drawUpgradeScreen(Graphics2D g2)
	{
		
	}
	
	public int getCost()
	{
		return cost;
	}
}

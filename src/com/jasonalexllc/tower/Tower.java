package com.jasonalexllc.tower;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import com.jasonalexllc.main.*;
import com.jasonalexllc.mob.Mob;

/**
 * Abstract class that defines how all towers in the game should function
 * @author Jason Carrete, Alex Berman
 * @since Jun 18, 2014
 */
public abstract class Tower extends ImageIcon
{
	private static final long serialVersionUID = -3346762436378513557L;

	public ArrayList<Attack> attackQueue = new ArrayList<>();
	public boolean viewUpgradeScreen = false;

	/**
	 * Denotes when the next attack should start from the tower 1 = should attack, {x in R | 0 <= x < 1} = shouldn't attack
	 */
	protected double atk = 1;
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
	public abstract void attack(Mob m, Graphics2D g2, Tile tile);
	
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
	
	public int getRangePixels()
	{
		return range * 50;
	}
	
	public boolean drawUpgradeScreen(Graphics2D g2)
	{
		if(viewUpgradeScreen)
		{
			Stroke s = g2.getStroke();
			
			//draw the background
			g2.setColor(new Color(0, 0, 0, 100));
			g2.fillRect(100, 0, 600, 200);
			
			//draw the X
			g2.setColor(Color.white);
			g2.setStroke(new BasicStroke(3));
			g2.drawLine(675, 5, 695, 25); // \
			g2.drawLine(695, 5, 675, 25); // /
			
			//reset the stroke change from drawing any upgrade screens
			g2.setStroke(s);
		}
		
		return viewUpgradeScreen;
	}
	
	public abstract Tower getInstance();
	
	public int getCost()
	{
		return cost;
	}
}

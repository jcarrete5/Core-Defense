package com.jasonalexllc.main;

import java.awt.Graphics2D;
import java.awt.Image;
import javax.swing.ImageIcon;
import com.jasonalexllc.tower.Tower;

/**
 * Keeps track of the tiles in the game
 * @author Jason Carrete, Alex Berman
 * @since Jun 21, 2014
 */
public class Tile extends ImageIcon
{
	private static final long serialVersionUID = 2780222818493615777L;
	public static final int PATH = 0, STONE = 1, MOUND = 2, FAULT = 3;

	private int x, y, type;
	private Tower tower;
	
	public Tile(Image img, int x, int y, int type)
	{
		this.setImage(img);
		this.x = x;
		this.y = y;
		this.type = type;
	}
	
	public int getType()
	{
		return type;
	}
	
	public int getX()
	{
		return x;
	}
	
	public int getY()
	{
		return y;
	}
	
	public Tower getTower()
	{
		return tower;
	}
	
	public boolean hasTower()
	{
		return tower != null;
	}
	
	public void addTower(Tower t)
	{
		tower = t;
		if(Game.money != Game.UNLIMITED)
			Game.money -= t != null ? t.getCost() : 0;
	}
	
	public void removeTower()
	{
		tower = null;
	}
	
	public void draw(Graphics2D g2)
	{
		g2.drawImage(this.getImage(), x, y, null);
		if(hasTower())
			g2.drawImage(tower.getImage(), x, y, null);
	}
	
	public String toString()
	{
		return "x pos: " + x + "\ny pos: " + y;
	}
}

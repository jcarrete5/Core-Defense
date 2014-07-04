package com.jasonalexllc.tower;

import java.awt.Image;
import com.jasonalexllc.level.Mob;

/**
 * All attacks extend this class
 * @author Jason Carrete, Alex Berman
 * @since Jul 4, 2014
 */
public abstract class Attack
{
	protected Image[] anim;
	
	/**
	 * Attack's current coordinates
	 */
	protected int x1, y1;
	protected double r, ySpeed, xSpeed;
	protected Mob target;
	
	protected Attack(String[] anim, int x, int y, Mob m)
	{
		
	}
}

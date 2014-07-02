package com.jasonalexllc.tower;

import java.awt.*;
import com.jasonalexllc.level.Mob;
import com.jasonalexllc.main.CoreDefense;

/**
 * Represents an attack that is currently happening on the grid
 * @author Jason Carrete, Alex Berman
 * @since Jun 23, 2014
 */
public class Attack
{
	private Image[] anim;
	
	/**
	 * Attack's current coordinates
	 */
	private int x1, y1;
	private double r, ySpeed, xSpeed;
	
	/**
	 * Creates a new attack that can be thrown by a tower
	 * @param anim
	 * @param x starting position of the Attack
	 * @param y starting position of the Attack
	 * @param m
	 */
	public Attack(String[] anim, int x, int y, Mob m)
	{
		this.anim = new Image[anim.length];
		for(int i = 0; i < anim.length; i++)
			this.anim[i] = CoreDefense.getImage(anim[i]);
		
		x1 = x;
		y1 = y;
		
		/* sqrt((x2 - x1)^2 + (y2 - y1)^2) -- distance formula
		 * m = (y2 - y1) / (x2 - x1) -- slope formula
		 */
		int yPrime = (m.getY() + 25) - (y1 + 25), xPrime = (m.getX() + 25) - (x1 + 25); //find the slope of the line between the mob and the tower
		
		//higher numbers = slower movement
		float pPer10ms = 10f;
		xSpeed = xPrime / pPer10ms;
		ySpeed = yPrime / pPer10ms;
	}
	
	public void draw(Graphics2D g2)
	{
		g2.drawImage(anim[(int)r], x1 + 6, y1 + 6, null);
		r = r > anim.length - 0.8 ? 0 : r + 0.2; //controls how long each rotation is visible for. lower numbers = slower rotation
		
		x1 += (int)xSpeed;
		y1 += (int)ySpeed;
		
	}
}

package com.fwumdegames.tower;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import com.fwumdegames.level.Mob;

/**
 * Represents an attack that is currently happening on the grid
 * @author Jason Carrete, Alex Berman
 * @since Jul 17, 2014
 */
public class Attack
{
	protected Image[] anim;

	/**
	 * Attack's current coordinates
	 */
	protected int x1, y1;
	protected double r, ySpeed, xSpeed;
	protected Mob target;
	
	/**
	 * Creates a new attack that can be thrown by a tower
	 * @param anim
	 * @param x starting position of the Attack
	 * @param y starting position of the Attack
	 * @param m
	 */
	public Attack(Image[] anim, int x, int y, Mob m)
	{
		this.anim = new Image[anim.length];
		this.anim = anim;
		
		target = m;
		
		x1 = x;
		y1 = y;
		
		/*
		 *  sqrt((x2 - x1)^2 + (y2 - y1)^2) -- distance formula
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
		g2.drawImage(anim[(int)r], x1 + 25, y1 + 25, null);
		r = r > anim.length - 0.8 ? 0 : r + 0.5; //controls how long each rotation is visible for. lower numbers = slower rotation
		
		x1 += (int)xSpeed;
		y1 += (int)ySpeed;
		
		//after redrawing attack, recalculate the path of the attack to make it more accurate
		int yPrime = (target.getY() + 25) - (y1 + 25), xPrime = (target.getX() + 25) - (x1 + 25); //find the slope of the line between the mob and the tower
		
		//higher numbers = slower movement
		float pPer10ms = 10f;
		xSpeed = xPrime / pPer10ms;
		ySpeed = yPrime / pPer10ms;
	}
	
	public int getX()
	{
		return x1;
	}
	
	public int getY()
	{
		return y1;
	}
	
	public boolean hasHitMob()
	{
		Rectangle targetHitBox = new Rectangle(target.getX(), target.getY(), 50, 50);
		Rectangle atkHitBox = new Rectangle(x1 + 25, y1 + 25, 1, 1);
		
		return targetHitBox.intersects(atkHitBox);
	}
}

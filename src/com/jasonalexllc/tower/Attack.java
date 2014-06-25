package com.jasonalexllc.tower;

import java.awt.Graphics2D;
import java.awt.Image;
import javax.swing.ImageIcon;
import com.jasonalexllc.main.CoreDefense;
import com.jasonalexllc.mob.Mob;

/**
 * 
 * @author Jason Carrete, Alex Berman
 * @since Jun 23, 2014
 */
public class Attack
{
	private Image[] anim;
	private double[] rotation = {0, Math.toRadians(90), Math.toRadians(180), Math.toRadians(270)};
	
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
			this.anim[i] = new ImageIcon(CoreDefense.class.getResource(anim[i])).getImage();
		
		x1 = x;
		y1 = y;
		
		/* sqrt((x2 - x1)^2 + (y2 - y1)^2) -- distance formula
		 * m = (y2 - y1) / (x2 - x1) -- slope formula
		 */
		int yPrime = (m.getY() + 25) - (y1 + 25), xPrime = (m.getX() + 25) - (x1 + 25); //find the slope of the line between the mob and the tower
		
		//higher numbers = slower movement
		float pPerIteration = 10f;
		xSpeed = xPrime / pPerIteration;
		ySpeed = yPrime / pPerIteration;
	}
	
	public void draw(Graphics2D g2)
	{
		g2.rotate(rotation[(int)r], x1 + 12, y1 + 12);
		g2.drawImage(anim[0], x1, y1, null);
		g2.rotate(-1 * rotation[(int)r], x1 + 12, y1 + 12);
		
		r = r >= 3.0 ? 0 : r + 0.2; //controls how long each rotation is visible for. lower numbers = slower rotation
		x1 += (int)xSpeed;
		y1 += (int)ySpeed;
	}
}

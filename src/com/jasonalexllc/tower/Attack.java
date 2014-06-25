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
	private int x1, y1, r;
	private double ySpeed, xSpeed;
	
	public Attack(String[] anim, int x, int y, Mob m)
	{
		this.anim = new Image[anim.length];
		for(int i = 0; i < anim.length; i++)
			this.anim[i] = new ImageIcon(CoreDefense.class.getResource(anim[i])).getImage();
		
		x1 = x;
		y1 = y;
		
		/*sqrt((x2 - x1)^2 + (y2 - y1)^2) -- distance formula
		 *
		 * 800 pixels = 160 iterations of moving the attack 5 pixels **every 10ms
		 * m.getX() - x **
		 */
		int yPrime = (m.getY() + 25) - y1, xPrime = (m.getX() + 25) - x1;
		int d = (int)Math.sqrt(Math.pow(xPrime, 2) + Math.pow(yPrime, 2)); // distance between the center of the mob and the center of the tower
		int iterate = d / 7 + (d % 7 == 0 ? 0 : 1);
		
		xSpeed = xPrime / (double)iterate;
		ySpeed = yPrime / (double)iterate;
	}
	
	public void draw(Graphics2D g2)
	{
		g2.rotate(/*rotation[r]*/Math.toRadians(180), x1, y1);
		g2.drawImage(anim[0], x1, y1, null);
		g2.rotate(-1 * Math.toRadians(180), x1, y1);
		
//		r = r == 3 ? 0 : r++;
		x1 += (int)xSpeed;
		y1 += (int)ySpeed;
	}
}

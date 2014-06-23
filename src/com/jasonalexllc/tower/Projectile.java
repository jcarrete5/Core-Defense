package com.jasonalexllc.tower;

import java.awt.Graphics2D;
import java.awt.Image;
import javax.swing.ImageIcon;
import com.jasonalexllc.main.CoreDefense;
import com.jasonalexllc.main.Tile;
import com.jasonalexllc.mob.Mob;

/**
 * 
 * @author Jason Carrete, Alex Berman
 * @since Jun 22, 2014
 */
public class Projectile
{
	private Image[] anim;
	public int dmg, spd;
	private int pos, x, y;
	
	public Projectile(String[] anim, int damage, int speed/*pixels/10ms*/)
	{
		this.anim = new Image[anim.length];
		for(int i = 0; i < anim.length; i++)
			this.anim[i] = new ImageIcon(CoreDefense.class.getResource(anim[i])).getImage();
		
		dmg = damage;
		spd = speed;
	}
	
	public void anim(Mob m, Graphics2D g2, Tile tile)
	{
		Runnable r = () ->
		{
			x = tile.getX() + 25;
			y = tile.getY() + 25;
			
			int ySpeed = y - m.getY(), xSpeed = x - m.getX();
//			System.out.println(ySpeed + " " + xSpeed);
			int gcd = gcd(ySpeed, xSpeed);
//			System.out.println(gcd);
			ySpeed /= gcd;
			xSpeed /= gcd;
//
//			System.out.println(ySpeed + " " + xSpeed);
//			System.out.println();
			
//			while(y != m.getY() || x != m.getX())
//			{
//				System.out.println("atk");
//				g2.drawImage(anim[0], x, y, null);
//				y -= ySpeed;
//				x -= xSpeed;
//			}
		};
		
		new Thread(r, "Projectile Thread").start();
	}
	
	private int gcd(int a, int b)
	{
		return b == 0 ? a : gcd(b, a % b);
	}
	
	public Image getNextImage()
	{
		Image i = anim[pos];
		pos = pos >= anim.length - 1 ? 0 : pos++;
		return i;
	}
}

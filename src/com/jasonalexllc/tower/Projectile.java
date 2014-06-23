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
	private int pos;
	
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
		System.out.println("atk");
	}
	
	public Image getNextImage()
	{
		Image i = anim[pos];
		pos = pos >= anim.length - 1 ? 0 : pos++;
		return i;
	}
}

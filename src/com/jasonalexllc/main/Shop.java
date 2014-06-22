package com.jasonalexllc.main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Scanner;
import com.jasonalexllc.tower.*;

/**
 * Has all the towers that can be bought in the game
 * @author Jason Carrete, Alex Berman
 * @since Jun 19, 2014
 */
public class Shop
{
	public boolean opened = false;
	
	public Tower[][] towers = new Tower[2][8];
	
	public Shop()
	{
		towers[0][0] = new PickaxeTower();
		towers[0][1] = new CraneTower();
	}
	
	public void open(Graphics2D g2)
	{
		if(opened)
		{
			//draws shop background
			g2.setColor(new Color(0, 0, 0, 100));
			g2.fillRect(200, 700, 400, 200);
			
			int col = Game.curMousePos.x / 50 - 4, row = Game.curMousePos.y / 50 - 14;
			
			//update the info screen depending on which tower is currently being hovered over
			if(row >= 0 && col >= 0 && towers[row][col] != null)
			{
				if(towers[row][col] instanceof PickaxeTower)
					drawInfo(g2, "Pickaxe Tower", towers[row][col].getCost(), "This tower will throw pickaxes at its foes.");
				else if(towers[row][col] instanceof CraneTower)
					drawInfo(g2, "Crane Tower", towers[row][col].getCost(), "Redneck swings his giant wrecking ball at his foes.");
				
				//highlight the tower currently being hovered over
				g2.setColor(new Color(255, 255, 255, 100));
				g2.fillRect(col * 50 + 200, row * 50 + 700, 50, 50);
			}
			
			//draw towers in the shop
			for(int r = 0; r < towers.length; r++)
				for(int c = 0; c < towers[0].length; c++)
					if(towers[r][c] != null)
					{
						g2.drawImage(towers[r][c].getImage(), c * 50 + 200, r * 50 + 700, null);
						if(Game.money < towers[r][c].getCost())
						{
							g2.setColor(new Color(255, 0, 0, 100));
							g2.fillRect(c * 50 + 200, r * 50 + 700, 50, 50);
						}
					}		
		}
	}
	
	private void drawInfo(Graphics2D g2, String name, int cost, String desc)
	{
		g2.fillRect(0, 700, 150, 100);
		g2.setColor(Color.white);
		g2.drawString(name, 5, 715);
		g2.drawString("Cost: " + cost, 5, 730);
		
		String[] s = new String[desc.length() / 19 + 1];
		for (int j = 0; j < s.length; j++)
			s[j] = "";
			
		int index = 0;
		Scanner scanDesc = new Scanner(desc);
		while(scanDesc.hasNext())
		{
			s[index] += scanDesc.next() + " ";
			if(s[index].length() - 1 >= 19)
				index++;
		}
		
		for(int i = 0, p = 745; i < s.length; i++, p += 15)
			g2.drawString(s[i], 5, p);
			
		scanDesc.close();
	}
	
	public Tower buyTower(int row, int col)
	{
		if(Game.money >= towers[row][col].getCost())
			return towers[row][col];
		else
			return null;
	}
}

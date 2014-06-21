 package com.jasonalexllc.main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import com.jasonalexllc.mob.Mob;
import com.jasonalexllc.tower.Tower;

/**
 * The main drawing thread
 * @author Jason Carrete, Alex Berman
 * @since Jun 21, 2014
 */
public class Game extends JPanel implements MouseListener, MouseMotionListener
{
	private static final long serialVersionUID = -1168167031210268222L;
	
	public static int lives = 200;
	public static int money = 850;
	
	private Tile[][] grid;
	private Shop shop;
	private boolean paused = false;
	
	public Tower curTower;
	public static Point curMousePos;
	private Mob m;
	
	public Game(Tile[][] grid, Shop s)
	{
		this.grid = grid;
		shop = s;
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
	}
	
	public Game(int interval, Tile[][] grid, Shop s)
	{
		this(grid, s);
		
		Runnable r = () -> 
		{
			while(true)
			{
				//when paused just infinitely repeat this loop until it is unpaused
				while(paused)
					Thread.yield();
				
				try
				{
					Thread.sleep(interval);
				}
				catch(InterruptedException e)
				{
					e.printStackTrace();
				}
				
				this.repaint();
			}
		};
		Image[] sprt = {new ImageIcon(CoreDefense.class.getResource("assets/stoneman_1.png")).getImage(), new ImageIcon(CoreDefense.class.getResource("assets/stoneman_2.png")).getImage()};
		m = new Mob(0, 50, sprt);
		new Thread(r, "Game Thread").start();
	}
	
	public void pause()
	{
		paused = true;
	}
	
	public void unpause()
	{
		paused = false;
	}
	
	public boolean isPaused()
	{
		return paused;
	}
	
	@Override
	public void paintComponent(Graphics g)
	{
		Graphics2D g2 = (Graphics2D)g;
		super.paintComponent(g2);
		//draw the grid and draw towers if any are on the grid
		for(Tile[] row : grid)
			for(Tile t : row)
			{
				t.draw(g2);
				if(t.hasTower())
					t.getTower().drawUpgradeScreen(g2);
			}
		m.draw(g2);
		//drag tower when selected from the shop
		if(curTower != null)
			g2.drawImage(curTower.getImage(), curMousePos.x - 25, curMousePos.y - 25, this);
		//open the shop
		shop.open(g2);
		
		//draw lives and money
		g2.setColor(Color.white);
		g2.drawString("Lives: " + lives, 5, 10);
		g2.drawString("Money: " + money, 5, 25);
		
	}

	public void mouseClicked(MouseEvent e)
	{
		if(shop.opened)
		{
			int col = e.getPoint().x / 50 - 4, row = e.getPoint().y / 50 - 14;
			
			if(col >= 0 && row >= 0)
			{
				curTower = shop.buyTower(row, col);

				//if you have less money than the tower is worth after clicking, then leave the shop open
				shop.opened = money < shop.towers[row][col].getCost() ? true : false;
			}
		}
		else if(curTower != null)
		{
			int col = e.getPoint().x / 50, row = e.getPoint().y / 50;
			
			if(grid[row][col].getType() != Tile.PATH && grid[row][col].getType() != Tile.FAULT)
				grid[row][col].addTower(curTower);
			
			curTower = null;
		}
		else
		{
			int col = e.getPoint().x / 50, row = e.getPoint().y / 50;
			
			if(grid[row][col].hasTower())
			{
				
			}
		}
		
	}

	public void mousePressed(MouseEvent e)
	{
		
	}

	public void mouseReleased(MouseEvent e)
	{
		
	}
	
	public void mouseEntered(MouseEvent e)
	{
		
	}
	
	public void mouseExited(MouseEvent e)
	{
		
	}

	public void mouseDragged(MouseEvent e)
	{
		
	}

	public void mouseMoved(MouseEvent e)
	{
		curMousePos = e.getPoint();
	}
}

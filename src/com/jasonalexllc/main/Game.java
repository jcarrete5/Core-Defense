 package com.jasonalexllc.main;

import java.awt.*;
import java.awt.event.*;
import javax.swing.JPanel;
import com.jasonalexllc.level.Mob;
import com.jasonalexllc.tower.*;

/**
 * The main drawing thread
 * @author Jason Carrete, Alex Berman
 * @since Jun 21, 2014
 */
public class Game extends JPanel implements MouseListener, MouseMotionListener
{
	private static final long serialVersionUID = -1168167031210268222L;
	
	public static final int UNLIMITED = -1, EASY = 0, MEDIUM = 1, HARD = 2, SANDBOX = 3;
	public static int lives;
	public static int money;
	
	private Tile[][] grid;
	private Shop shop;
	private boolean paused = false;
	
	public Tower curTower;
	public static Point curMousePos;
	private Mob m;
	
	public Game(int interval, Tile[][] grid, Shop s, int difficulty)
	{
		this.setDoubleBuffered(true);
		this.setLayout(null);		
		this.grid = grid;
		shop = s;
		
		//apply the difficulty TODO eventually remove the difficulty setting in the lvl1.txt and have the user specify the level/difficulty
		switch(difficulty)
		{
		case EASY:
			lives = 200;
			money = 850;
			break;
		case MEDIUM:
			lives = 150;
			money = 700;
			break;
		case HARD:
			lives = 100;
			money = 500;
			break;
		case SANDBOX:
			lives = UNLIMITED;
			money = UNLIMITED;
			break;
		}
		
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.setFocusable(true);
		this.addKeyListener(new KeyListener()
		{
			public void keyTyped(KeyEvent e) {}
			
			public void keyPressed(KeyEvent e)
			{
				//pause game and open the shop
				if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
					if(isPaused())
						unpause();
					else
						pause();
				else if(!isPaused() && curTower == null && e.getKeyCode() == KeyEvent.VK_S) //open the shop screen
					s.opened = !s.opened;
			}
			
			public void keyReleased(KeyEvent e) {}
		});
		
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
		
		m = new Mob(0, 50, 1, grid);
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
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		super.paintComponent(g2);
		
		//draw the grid and draw towers if any are on the grid
		for(Tile[] row : grid)
			for(Tile t : row)
				t.draw(g2);

		//draw attacks moving towards a mob if it is within a towers range
		for(Tile[] row : grid)
			for(Tile t : row)
				if(t.hasTower())
				{
					t.getTower().attack(m, g2, t);
					for(Attack a : t.getTower().attackQueue)
						a.draw(g2);
				}

		m.draw(g2);
		
		//draw upgrade screens if they are needed
		outer: for(Tile[] row : grid)
			for(Tile t : row)
				if(t.hasTower())
				{
					boolean b = t.getTower().drawUpgradeScreen(g2);
					if(b) break outer;
				}
		
		//drag tower when selected from the shop
		if(curTower != null)
			g2.drawImage(curTower.getImage(), curMousePos.x - 25, curMousePos.y - 25, this);
		
		//open the shop
		shop.open(g2);
		
		//draw lives and money
		g2.setColor(Color.white);
		g2.drawString("Lives: " + (lives == UNLIMITED ? "infinite" : lives), 5, 10);
		g2.drawString("Money: " + (money == UNLIMITED ? "infinite" : money), 5, 25);
		
	}

	public void mouseClicked(MouseEvent e)
	{
		if(shop.opened)
		{
			int col = e.getPoint().x / 50 - 4, row = e.getPoint().y / 50 - 14;
			
			if(col >= 0 && row >= 0)
			{
				curTower = shop.buyTower(row, col);
				
				//if you have less money than the tower is worth (unless in sandbox) after clicking, then leave the shop open
				shop.opened = shop.towers[row][col] != null && (money < shop.towers[row][col].getCost() && !(money == UNLIMITED)) ? true : false;
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
			
			//if a tower is showing an upgrade screen already it is bound to 'already'
			Tower already = null;
			outer: for(Tile[] r : grid)
				for(Tile t : r)
					if(t.hasTower() && t.getTower().viewUpgradeScreen)
					{
						already = t.getTower();
						break outer;
					}
			
			if(grid[row][col].hasTower()) //if the clicked tile has a tower
			{
				if(!grid[row][col].getTower().viewUpgradeScreen) //and the clicked tower isn't showing an upgrade screen
					if(already == null) //and there isn't a tower already showing an upgrade screen, then show the upgrade screen for the clicked tower
						grid[row][col].getTower().viewUpgradeScreen = true;
					else //if there is an upgrade screen already showing, then show the upgrade screen for the clicked tower and not for the one already showing
					{
						already.viewUpgradeScreen = false;
						grid[row][col].getTower().viewUpgradeScreen = true;
					}
			}
			else if(already != null) //if another tower is showing an upgrade screen and your mouse is on the X, then close the update screen
			{
				Point p = e.getPoint();
				if(p.x >= 675 && p.x <= 695 && p.y >= 5 && p.y <= 25)
					already.viewUpgradeScreen = false;
			}
		}
	}

	public void mousePressed(MouseEvent e) {}

	public void mouseReleased(MouseEvent e) {}
	
	public void mouseEntered(MouseEvent e) {}
	
	public void mouseExited(MouseEvent e) {}

	public void mouseDragged(MouseEvent e) {}

	public void mouseMoved(MouseEvent e)
	{
		curMousePos = e.getPoint();
	}
}

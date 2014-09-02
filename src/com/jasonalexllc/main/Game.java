 package com.jasonalexllc.main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import com.jasonalexllc.level.Wave;
import com.jasonalexllc.tower.Attack;
import com.jasonalexllc.tower.Tower;

/**
 * The main drawing thread
 * @author Jason Carrete, Alex Berman
 * @since Jul 17, 2014
 */
public class Game extends JPanel implements MouseListener, MouseMotionListener
{
	private static final long serialVersionUID = -1168167031210268222L;
	
	public static final int UNLIMITED = -1, EASY = 0, MEDIUM = 1, HARD = 2, SANDBOX = 3;
	public static int lives;
	public static int money;
	public static final int options = 1;
	
	private static final Color SPD_COLOR = new Color(255, 0, 0, 125); //default speed color
	public static final Color BG_COLOR = new Color(0, 0, 0, 100); //background color. (slightly transparent black)

	private ArrayList<Wave> waves;
	private boolean startWave = false;
	private Tile[][] grid;
	private Shop shop;
	public static int clockSpd;
	private Color spdColor = SPD_COLOR;
	private boolean paused = false;
	
	public Tower curTower;
	private int curWave = 0;
	public static Point curMousePos;
	
	private boolean flag = true;
	private boolean run = true;
	
	public Game(int interval, Tile[][] grid, Shop s, int difficulty, ArrayList<Wave> waves)
	{
		super(null);
		clockSpd = interval;
		this.setDoubleBuffered(true);	
		this.grid = grid;
		shop = s;
		
		//apply the difficulty
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
			while(run)
			{				
				try
				{
					Thread.sleep(clockSpd);
				}
				catch(InterruptedException e)
				{
					e.printStackTrace();
				}
				
				if(waves.get(curWave).isDone())
				{
					curWave = curWave + 1 >= waves.size() ? -1 : curWave + 1;
					startWave = false;
					clockSpd = 10;
					spdColor = SPD_COLOR;
				}
					
				if(curWave == -1) //TODO win scenario
				{
					JOptionPane.showMessageDialog(this, "You Won...", "Victory", JOptionPane.PLAIN_MESSAGE);
					run = false;
					removeAllTowers();
				}
				else if(lives <= 0) //TODO lose scenario
				{
					JOptionPane.showMessageDialog(this, "You Lose...", "Failure", JOptionPane.PLAIN_MESSAGE);
					run = false;
					removeAllTowers();
				}
				else
					this.repaint();
			}
			
			//bring the player back to the main menu
			CoreDefense.mainMenu();
		};
		
		this.waves = waves;
		new Thread(r, "Game Thread").start();
	}
	
	private void removeAllTowers()
	{
		for(int row = 0; row < grid.length; row++)
			for(int col = 0; col < grid[0].length; col++)
				grid[row][col].removeTower();
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
		
		if(!isPaused())
		{
			super.paintComponent(g2);	
			flag = true;
			
			//draw the grid and draw towers if any are on the grid
			for(Tile[] row : grid)
				for(Tile t : row)
					t.draw(g2);
	
			//draw attacks moving towards a mob if it is within a towers range
			for(Tile[] row : grid)
				for(Tile tile : row)
					if(tile.hasTower())
					{
						tile.getTower().attack(waves.get(curWave).get(0), tile); //attack the first mob in the wave array
						for(Attack a : tile.getTower().attackQueue)
							a.draw(g2);
					}
	
			//draw mobs that should be drawn at the current wave
			if(startWave)
				for(int i = 0; i < waves.get(curWave).size(); i++)
				{
					if(!waves.get(curWave).get(i).isOnScreen())
						waves.get(curWave).get(i).spawn();
					
					if(waves.get(curWave).get(i).isOnScreen())
						waves.get(curWave).get(i).draw(g2);
					else if(!waves.get(curWave).get(i).isAlive())
						waves.get(curWave).remove(i);
				}
			
			//draw upgrade screens if they are needed
			outer: for(Tile[] row : grid)
				for(Tile t : row)
					if(t.hasTower())
					{
						boolean b = t.getTower().drawUpgradeScreen(g2);
						if(b) break outer; //only show one upgrade screen at a time and save time by breaking when it displays the upgrade screen
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
			
			// play/fast-forward button
			Shape s = new Ellipse2D.Float(770, 5, 25, 25);
			g2.setColor(BG_COLOR);
			g2.fill(s);
			g2.setColor(spdColor);
			if(startWave)
			{
				//draw fast-forward button
				g2.fillPolygon(new int[] {776, 776, 783}, new int[] {11, 24, 17}, 3);
				g2.fillPolygon(new int[] {783, 783, 790}, new int[] {11, 24, 17}, 3);
			}
			else
			{
				//draw play button
				g2.fillPolygon(new int[] {777, 777, 789}, new int[] {11, 24, 17}, 3);
			}
		}
		else //if the game is paused
		{
			if(flag)
			{
				g2.setColor(BG_COLOR);
				g2.fillRect(0, 0, 800, 800);
				
				JPanel optionsPane = new JPanel(new GridLayout(1, options));
				optionsPane.setBackground(new Color(0, 0, 0, 0));
				optionsPane.setBounds(0, 0, 800, 800);
				this.add(optionsPane);
				
				flag = false;
			}
		}
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
		else if(e.getPoint().x >= 770 && e.getPoint().x < 795 && e.getPoint().y >= 5 && e.getPoint().y < 30) //play/fast-forward is pressed
		{
			if(startWave) //button is fast-forward
				if(clockSpd == 10)
				{
					clockSpd = 5;
					spdColor = new Color(255, 0, 0, 175);
				}
				else
				{
					clockSpd = 10;
					spdColor = SPD_COLOR;
				}
			else //button is play
				startWave = true;
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

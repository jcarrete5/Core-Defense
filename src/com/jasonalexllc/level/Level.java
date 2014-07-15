package com.jasonalexllc.level;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Scanner;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import org.w3c.dom.Element;
import com.jasonalexllc.main.CoreDefense;
import com.jasonalexllc.main.Game;
import com.jasonalexllc.main.Shop;
import com.jasonalexllc.main.Tile;

/**
 * Levels are displayed in the order they appear in the file
 * @author Jason Carrete, Alex Berman
 * @since Jul 3, 2014
 */
public class Level
{
	private Element lvlNode;
	
	private int d;
	private Tile[][] grid = new Tile[16][16];
	private ImageIcon icon;
	
	public Level(Element lvlNode)
	{
		this.lvlNode = lvlNode;
		
		icon = new ImageIcon(lvlNode.getAttribute("img"));
		
		String layout = lvlNode.getFirstChild().getFirstChild().getNodeValue();
		
		//create the grid for the specified level
		Scanner scanLayout = new Scanner(layout);
		for(int row = 0; row < grid.length; row++)
			for(int col = 0; col < grid[0].length; col++)
			{
				//terminate the program if there aren't enough Tile Type to load the map
				if(!scanLayout.hasNext())
				{
					JOptionPane.showMessageDialog(null, "Tile Type deficiency in the \"levels.xml\" file at level: \"" + lvlNode.getAttribute("lvlName") + "\"."
							+ "\nFix this error to continue", "Level Loading Error", JOptionPane.ERROR_MESSAGE);
					System.exit(1);
				}
				
				int type = Integer.parseInt(scanLayout.next());
				String imgPath = "";
				switch(type)
				{
				case Tile.PATH:
					imgPath = "assets/path.png";
					break;
				case Tile.STONE:
					imgPath = "assets/stone.png";
					break;
				case Tile.PLATE:
					imgPath = "assets/plate.png";
					break;
				case Tile.FAULT:
					imgPath = "assets/fault.png";
					break;
				default: //handle what happens if the level creator uses an invalid Tile Type
					JOptionPane.showMessageDialog(null, "Invalid XML Level Element - lvlName=\"" + this + "\":\nRogue Tile type of: " + type, "Error", JOptionPane.ERROR_MESSAGE);
					System.exit(1);
					break;
				}
				
				
				grid[row][col] = new Tile(CoreDefense.getImage(imgPath), col * 50, row * 50, type);
			}
		
		scanLayout.close();
	}
	
	/**
	 * Loads the level and starts the game
	 */
	public void load(JFrame frame)
	{
		d = option(0);
		Game game = null;
		Shop s = null;
		if(d != -2)
		{
			frame.getContentPane().removeAll();
			s = new Shop();
			game = new Game(10, grid, s, d);
		}
		else
			return;
		
		game.setBounds(0, 0, 800, 800);
		frame.getContentPane().add(game);
	}
	
	private int option(int d)
	{
		int option = JOptionPane.showOptionDialog(null, "Play on " + (d == Game.EASY ? "EASY" : d == Game.MEDIUM ? "MEDIUM" : d == Game.HARD ? "HARD" : "SANDBOX") + " ?",
				"Difficulty Selection", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
		if(option == JOptionPane.YES_OPTION)
			return d;
		else if(option == JOptionPane.NO_OPTION && d != 3)
			return option(d + 1);
		else
			return -2;
	}
	
	public void setDifficulty(int d)
	{
		this.d = d;
	}
	
	public int getDifficulty()
	{
		return d;
	}
	
	public ImageIcon getImageIcon()
	{
		return icon;
	}
	
	/**
	 * @return the name of the Level
	 */
	public String toString()
	{
		return lvlNode.getAttribute("lvlName");
	}
}

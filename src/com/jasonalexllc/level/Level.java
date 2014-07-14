package com.jasonalexllc.level;

import java.awt.Image;
import java.util.Scanner;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import org.w3c.dom.Element;
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
	private Image icon;
	
	public Level(Element lvlNode)
	{
		this.lvlNode = lvlNode;
		
		icon = new ImageIcon(lvlNode.getAttribute("img")).getImage();
		
		Scanner scanLayout = new Scanner(lvlNode.getFirstChild().getFirstChild().getNodeValue());
		for(int row = 0; row < grid.length; row++)
		{
			for(int col = 0; col < grid[0].length; col++)
			{
				int type = Integer.parseInt(scanLayout.next());
				switch(type)
				{
				case Tile.PATH:
					break;
				case Tile.STONE:
					break;
				case Tile.PLATE:
					break;
				case Tile.FAULT:
					break;
				default:
					JOptionPane.showMessageDialog(null, "Invalid XML Level Element - lvlName=\"" + this + "\":\nRogue Tile type of: " + type, "Error", JOptionPane.ERROR_MESSAGE);
					System.exit(1);
					break;
				}
			}
			
			System.out.println();
		}
		
		scanLayout.close();
	}
	
	public void setDifficulty(int d)
	{
		this.d = d;
	}
	
	public int getDifficulty()
	{
		return d;
	}
	
	public Image getImage()
	{
		return icon;
	}
	
	/**
	 * Loads the level and starts the game
	 */
	public void load()
	{
		
	}
	
	/**
	 * @return the name of the Level
	 */
	public String toString()
	{
		return lvlNode.getAttribute("lvlName");
	}
}

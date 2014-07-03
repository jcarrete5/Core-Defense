package com.jasonalexllc.level;

import java.awt.Image;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import com.jasonalexllc.main.CoreDefense;
import com.jasonalexllc.main.Game;
import com.jasonalexllc.main.Shop;
import com.jasonalexllc.main.Tile;

/**
 * 
 * @author Jason Carrete, Alex Berman
 * @since Jul 3, 2014
 */
public class Level
{
	private int d;
	private Tile[][] grid = new Tile[16][16];
	
	public Level(File lvlMap, int difficulty)
	{
		d = difficulty;
		
		Scanner scan = null;
		try
		{
			scan = new Scanner(lvlMap);
		}
		catch(FileNotFoundException ex)
		{
			System.out.println("level couldn't be loaded");
			System.exit(-1);
		}
		
		//read the file and create a 2D array of tiles specified by the layout of the lvl file
		for(int row = 0; row < grid.length; row++)
			for(int col = 0; col < grid[0].length; col++)
			{
				int type = Integer.parseInt(scan.next());
				Image img = null;
				switch(type)
				{
				case Tile.PATH:
					img = CoreDefense.getImage("assets/path.png"); 
					break;
				case Tile.STONE:
					img = CoreDefense.getImage("assets/stone.png");
					break;
				case Tile.MOUND:
					img = CoreDefense.getImage("assets/mound.png");
					break;
				case Tile.FAULT:
					img = CoreDefense.getImage("assets/fault.png");
					break;
				}
				
				grid[row][col] = new Tile(img, col * 50, row * 50, type);
			}
	}
	
	public void load()
	{
		Game game = new Game(10, grid, new Shop(), d);
	}
}

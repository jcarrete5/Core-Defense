package com.jasonalexllc.main;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;

/**
 * A game where rock people are trying to blow up the core of the Earth and you have to defend the core with towers.
 * @author Jason Carrete, Alex Berman
 * @since Jun 21, 2014
 */
public class CoreDefense
{
	public static Tile[][] grid;
	
	public static void main(String[] args)
	{
		//Create the game window
		JFrame frame = new JFrame("Core Defense");
		frame.setLayout(null);
		frame.setIconImage(new ImageIcon(CoreDefense.class.getResource("assets/icon.png")).getImage());
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setPreferredSize(new Dimension(800, 800));
		frame.setVisible(true);
				
		//read lvl1.txt
		Scanner readFile = null;
		try
		{
			readFile = new Scanner(new File("maps/lvl1.txt"));
		}
		catch(FileNotFoundException e)
		{
			e.printStackTrace();
			System.exit(-1);
		}
		
		//get the difficulty
		int difficulty = Integer.parseInt(readFile.next());
		
		grid = new Tile[16][16];
		for(int row = 0; row < grid.length; row++)
			for(int col = 0; col < grid[0].length; col++)
			{
				//acquire the right image for the integer
				URL path = null;
				int type = Integer.parseInt(readFile.next());
				switch(type)
				{
				case Tile.PATH:
					path = CoreDefense.class.getResource("assets/path.png");
					break;
				case Tile.STONE:
					path = CoreDefense.class.getResource("assets/stone.png");
					break;
				case Tile.MOUND:
					break;
				case Tile.FAULT:
					break;
				}
				
				grid[row][col] = new Tile(path, col * 50, row * 50, type);
			}

		Shop shop = new Shop();
		Game game = new Game(10, grid, shop, difficulty);
		game.setBounds(0, 0, 800, 800);
		frame.add(game);
		
		frame.addKeyListener(new KeyListener()
		{
			public void keyTyped(KeyEvent e) {}
			
			public void keyPressed(KeyEvent e)
			{
				//pause game and open the shop
				if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
				{
					if(game.isPaused())
						game.unpause();
					else
						game.pause();
				}
				else if(!game.isPaused() && game.curTower == null && e.getKeyCode() == KeyEvent.VK_S) //open the shop screen
					shop.opened = !shop.opened;
			}

			public void keyReleased(KeyEvent e) {}
		});
		
		frame.pack();
		frame.setLocationRelativeTo(null);
	}
}

package com.jasonalexllc.level;

import java.util.*;
import javax.swing.*;
import org.w3c.dom.*;
import com.jasonalexllc.main.*;

/**
 * Levels are displayed in the order they appear in the file.
 * This class aids in the creation of Game objects
 * @author Jason Carrete, Alex Berman
 * @since Jul 17, 2014
 */
public class Level
{
	private Element lvlNode;
	
	private int d;
	private Tile[][] grid = new Tile[16][16];
	private ArrayList<Wave> waves;
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
		Element wavesNode = (Element)lvlNode.getElementsByTagName("Waves").item(0);
		NodeList waveNodes = wavesNode.getChildNodes();
		int xStart = Integer.parseInt(wavesNode.getAttribute("xStart"));
		int yStart = Integer.parseInt(wavesNode.getAttribute("yStart"));
		waves = new ArrayList<>(waveNodes.getLength());
		waves.add(new Wave(((Element)waveNodes.item(0)), xStart, yStart, grid));
		
		SwingWorker<Void, Wave> worker = new SwingWorker<Void, Wave>()
		{
			@Override
			protected void done()
			{
				System.out.println("Done loading waves");
			}

			@Override
			protected Void doInBackground() throws Exception
			{
				for(int i = 1; i < waveNodes.getLength(); i++)
				{
					Wave wave = new Wave(((Element)waveNodes.item(i)), xStart, yStart, grid);
					publish(wave);
				}
				
				return null;
			}

			@Override
			protected void process(List<Wave> chunks)
			{
				for(int i = 0; i < chunks.size(); i++)
					waves.add(chunks.get(i));
			}
		};
		worker.execute();
		
		d = option(0, frame);
		Game game = null;
		Shop s = null;
		if(d != -2)
		{
			frame.getContentPane().removeAll();
			s = new Shop();
			game = new Game(10, grid, s, d, waves);
		}
		else
			return;
		
		game.setBounds(0, 0, 800, 800);
		frame.getContentPane().add(game);
	}
	
	private int option(int d, JFrame frame)
	{
		int option = JOptionPane.showOptionDialog(frame, "Play on " + (d == Game.EASY ? "EASY" : d == Game.MEDIUM ? "MEDIUM" : d == Game.HARD ? "HARD" : "SANDBOX") + " ?",
				"Difficulty Selection", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
		if(option == JOptionPane.YES_OPTION)
			return d;
		else if(option == JOptionPane.NO_OPTION && d != 3)
			return option(d + 1, frame);
		else
			return -2;
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

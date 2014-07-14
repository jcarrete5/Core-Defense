package com.jasonalexllc.main;

import java.awt.Dimension;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import com.jasonalexllc.level.Level;

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
		frame.setIconImage(getImage("assets/icon.png"));
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(new Dimension(806, 828)); //weird numbers to account for the size of the size of the border
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
		
		//use the levels.xml to display all of the levels that can be played
		Document lvlDoc = getDocument("levels/levels.xml");
		Element root = lvlDoc.getDocumentElement();
		NodeList lvlElements = root.getElementsByTagName("Level");
		
		//take the NodeList of the levels and make it into a Level array
		Level[] level = new Level[lvlElements.getLength()];
		for(int i = 0; i < lvlElements.getLength(); i++)
			level[i] = new Level((Element)lvlElements.item(i));
		
//		//read lvl1.txt
//		Scanner readFile = null;
//		try
//		{
//			readFile = new Scanner(new File("levels/lvl1.lvl"));
//		}
//		catch(FileNotFoundException e)
//		{
//			e.printStackTrace();
//			System.exit(-1);
//		}
//		
//		//get the difficulty
//		int difficulty = Integer.parseInt(readFile.next());
//		
//		//create the grid
//		grid = new Tile[16][16];
//		for(int row = 0; row < grid.length; row++)
//			for(int col = 0; col < grid[0].length; col++)
//			{
//				//acquire the right image for the integer
//				Image img = null;
//				int type = Integer.parseInt(readFile.next());
//				switch(type)
//				{
//				case Tile.PATH:
//					img = getImage("assets/path.png");
//					break;
//				case Tile.STONE:
//					img = getImage("assets/stone.png");
//					break;
//				case Tile.PLATE:
//					break;
//				case Tile.FAULT:
//					break;
//				}
//				
//				grid[row][col] = new Tile(img, col * 50, row * 50, type);
//			}
//		
//		Shop shop = new Shop();
//		Game game = new Game(10, grid, shop, difficulty);
//		game.setBounds(0, 0, 800, 800);
//		
//		frame.addKeyListener(new KeyListener()
//		{
//			public void keyTyped(KeyEvent e) {}
//			
//			public void keyPressed(KeyEvent e)
//			{
//				//pause game and open the shop
//				if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
//					if(game.isPaused())
//						game.unpause();
//					else
//						game.pause();
//				else if(!game.isPaused() && game.curTower == null && e.getKeyCode() == KeyEvent.VK_S) //open the shop screen
//					shop.opened = !shop.opened;
//			}
//			
//			public void keyReleased(KeyEvent e) {}
//		});
//		
//		frame.add(game);
	}
	
	/**
	 * Used as a shortcut to get an image for the game
	 * @param imgPath
	 * @return an image that is used element of Core Defense
	 */
	public static Image getImage(String imgPath)
	{
		return new ImageIcon(CoreDefense.class.getResource(imgPath)).getImage();
	}
	
	private static ErrorHandler getErrorHandler()
	{
		return new ErrorHandler()
		{
			public void warning(SAXParseException exception) throws SAXException
			{
				System.out.println("SAX Warning");
			}

			public void error(SAXParseException exception) throws SAXException
			{
				exception.printStackTrace();
				throw new SAXException();
			}

			public void fatalError(SAXParseException exception) throws SAXException
			{
				exception.printStackTrace();
				throw new SAXException();
			}
		};
	}
	
	public static Document getDocument(String path)
	{
		try
		{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setIgnoringComments(true);
			factory.setIgnoringElementContentWhitespace(true);
			factory.setValidating(true);
			DocumentBuilder builder = factory.newDocumentBuilder();
			builder.setErrorHandler(getErrorHandler());
			return builder.parse(new InputSource(path));
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.exit(1);
		}
		
		return null;
	}
}

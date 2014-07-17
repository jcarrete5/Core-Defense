package com.jasonalexllc.main;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.*;
import com.jasonalexllc.level.Level;

/**
 * A game where rock people are trying to blow up the core of the Earth and you have to defend the core with towers.
 * @author Jason Carrete, Alex Berman
 * @since Jul 17, 2014
 */
public class CoreDefense
{	
	public static void main(String[] args)
	{
		Runnable thread = () ->
		{
			//Create the game window
			JFrame frame = new JFrame("Core Defense");
			frame.setLayout(null);
			frame.setIconImage(getImage("assets/icon.png"));
			frame.setResizable(false);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.getContentPane().setPreferredSize(new Dimension(800, 800));
			
			//use the levels.xml to display all of the levels that can be played
			Document lvlDoc = getDocument("levels/levels.xml");
			Element root = lvlDoc.getDocumentElement();
			NodeList pgNodes = root.getElementsByTagName("Page");
			
			//take the NodeList of the levels and make it into a 3D Level array
			Level[][][] levels = new Level[pgNodes.getLength()][3][3];
			for(int d = 0; d < levels.length; d++)
			{
				Element pg = (Element)pgNodes.item(d);
				for(int row = 0; row < pg.getChildNodes().getLength(); row++)
				{
					Element r = (Element)pg.getElementsByTagName("Row").item(row);
					for(int col = 0; col < r.getChildNodes().getLength(); col++)
						levels[d][row][col] = new Level((Element)r.getElementsByTagName("Level").item(col));
				}
			}
			
			//display each level on the first page TODO add a button that scrolls through the pages
			JButton[][][] lvlButtons = new JButton[levels.length][levels[0].length][levels[0][0].length];
			for(int row = 0; row < levels[0].length; row++)
				for(int col = 0; col < levels[0][0].length; col++)
					if(levels[0][row][col] != null)
					{
						lvlButtons[0][row][col] = new JButton(levels[0][row][col].getImageIcon());
						ActionListener al = (ActionEvent e) ->
						{
							for(int d = 0; d < levels.length; d++)
								for(int r = 0; r < levels[0].length; r++)
									for(int c = 0; c < levels[0][0].length; c++)
										if(lvlButtons[d][r][c] == e.getSource())
											levels[d][r][c].load(frame);
						};
						lvlButtons[0][row][col].addActionListener(al);
						lvlButtons[0][row][col].setBounds(col * 250 + 100, row * 250 + 100, 100, 100);
						
						JLabel title = new JLabel(levels[0][row][col].toString());
						title.setBounds(250 * col + 100, 250 * row + 210, 100, 15);
						
						frame.getContentPane().add(lvlButtons[0][row][col]);
						frame.getContentPane().add(title);
					}
	
			frame.pack();
			frame.setVisible(true);
			frame.setLocationRelativeTo(null);
		};
		
		SwingUtilities.invokeLater(thread);
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

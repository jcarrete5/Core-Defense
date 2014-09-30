package com.fwumdegames.level;

import java.util.ArrayList;
import org.w3c.dom.*;
import com.fwumdegames.main.Tile;

/**
 * Represents a single Wave in an array of Waves
 * @author Jason Carrete
 * @since Jul 17, 2014
 */
public class Wave extends ArrayList<Mob>
{
	private static final long serialVersionUID = 2952714411305371573L;
	private int totalSpawnTime = 0;
	
	public Wave(Element wave, int x, int y, Tile[][] grid)
	{
		NodeList mobs = wave.getChildNodes();
		for(int i = 0; i < mobs.getLength(); i++)
		{
			int rank = Integer.parseInt(((Element)mobs.item(i)).getAttribute("rank"));
			int amount = Integer.parseInt(((Element)mobs.item(i)).getAttribute("amount"));
			double spawnTime = Double.parseDouble(((Element)mobs.item(i)).getAttribute("spawnTime"));
			for(int j = 0; j < amount; j++)
			{
				this.add(new Mob(x, y, rank, grid, totalSpawnTime));
				totalSpawnTime += spawnTime;
			}
		}
	}
	
	/**
	 * If all mobs in the wave are dead, then the wave is completed
	 * @return true if all mobs in the wave are dead, otherwise false
	 */
	public boolean isDone()
	{
		boolean result = false;
		
		for(Mob m : this)
		{
			if(result = m.isAlive())
				break;
		}
		
		return !result;
	}
}

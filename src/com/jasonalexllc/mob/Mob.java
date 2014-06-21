package com.jasonalexllc.mob;

import com.jasonalexllc.main.CoreDefense;
import com.jasonalexllc.main.Tile;
import java.util.Random;
import java.awt.Graphics2D;
import java.awt.Image;

/**
 * Handles mob generation in game
 * @author Jason Carrete, Alex Berman
 * @since Jun 20, 
 */
public class Mob 
{
    private Image[] sprite;
    private int speed, x, y, damage, indexOfSprite, comingFrom;
    public static final int UP = 0, RIGHT = 1, DOWN = 2, LEFT = 3;
    
    public Mob(int speed, int x, int y, Image[] sprite, int damage)
    {
    	this.sprite = sprite;
    	this.x = x;
    	this.y = y;
    	this.speed = speed;
    	this.damage = damage;
    	indexOfSprite = 0;
    	comingFrom = LEFT;
    }
    
    public Mob(int x, int y, Image[] sprite)
    {
    	this(1, x, y, sprite, 1);
    }
    
    public int getDamage()
    {
    	return damage;
    }
    
    public boolean move(int direction)
    {
    	boolean ret = true;
    	
    	if(direction < 0 || direction > 3)
    		ret = false;
    	
    	else if(direction == UP && canMove(UP))
			y -= speed;
		
    	else if(direction == RIGHT && canMove(RIGHT))
			x += speed;
		
    	else if(direction == DOWN && canMove(DOWN))
			y += speed;
		
    	else if(direction == LEFT && canMove(LEFT))
			x -= speed;
    	
    	return ret;
    }
    
    private boolean canMove(int direction)
    {
    	boolean ret = true;
    	
    	if(direction < 0 || direction > 3)
    		ret = false;
    	
    	else
    	{
    		if(direction == UP)
    			ret = CoreDefense.grid[x/50][(y - speed)/50].getType() == Tile.PATH;
    		
    		else if(direction == RIGHT)
    			ret = CoreDefense.grid[((x + speed)/50) + 1][y/50].getType() == Tile.PATH;
    		
    		else if(direction == DOWN)
    			ret = CoreDefense.grid[x/50][((y + speed)/50) + 1].getType() == Tile.PATH;
    		
    		else if(direction == LEFT)
    			ret = CoreDefense.grid[(x - speed)/50][y/50].getType() == Tile.PATH;
    	}
    	
    	return ret;
    }
    
    public void draw(Graphics2D g2)
    {
    	g2.drawImage(sprite[indexOfSprite], x, y, null);
    	indexOfSprite++;
    	if(indexOfSprite >= sprite.length)
    		indexOfSprite = 0;
    }
    
    public void autoMove()
    {
    	boolean works;
    	if(comingFrom + 2 < 4)
    		works = move(comingFrom + 2);
    	else
    		works = move(comingFrom - 2);
    	
    	if(!works)
    	{
   			boolean[] directions = new boolean[4];
   			for(int ryanC = 0; ryanC < directions.length; ryanC++)
    		{
    			if(canMove(ryanC) && ryanC != comingFrom)
    				directions[ryanC] = true;
   			}
   			Random rand = new Random();
       		int randIndex = rand.nextInt(directions.length);
       		while(!directions[randIndex])
        		randIndex = rand.nextInt(directions.length);
       		move(randIndex);
    	}
    }
}
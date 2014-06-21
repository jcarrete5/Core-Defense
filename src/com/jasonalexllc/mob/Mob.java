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
    
    public boolean move(int direction)
    {
    	boolean ret = false;
    	
    	if(direction < 0 || direction > 3)
    		ret = false;
    	
    	else if(direction == UP && canMove(UP))
    	{
			y -= speed;
			ret = true;
    	}
		
    	else if(direction == RIGHT && canMove(RIGHT))
    	{
			x += speed;
			ret = true;
    	}
    	
    	else if(direction == DOWN && canMove(DOWN))
    	{
			y += speed;
			ret = true;
    	}
		
    	else if(direction == LEFT && canMove(LEFT))
    	{
			x -= speed;
			ret = true;
    	}
    	
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
    			ret = CoreDefense.grid[((y - speed)/50)][(x+1)/50].getType() == Tile.PATH;
    		
    		else if(direction == RIGHT)
    			ret = CoreDefense.grid[(y+1)/50][((x + speed)/50) + 1].getType() == Tile.PATH;
    		
    		else if(direction == DOWN)
    			ret = CoreDefense.grid[((y + speed)/50) + 1][(x+1)/50].getType() == Tile.PATH;
    		
    		else if(direction == LEFT)
    			ret = CoreDefense.grid[(y+1)/50][(x - speed)/50].getType() == Tile.PATH;
    	}
    	
    	return ret;
    }
    
    public void draw(Graphics2D g2)
    {
    	//g2.drawImage(sprite[indexOfSprite], x, y, null);
    	autoMove();
    	g2.drawRect(x, y, 50, 50);
    	indexOfSprite = indexOfSprite >= sprite.length ? 0 : indexOfSprite + 1;
    }
    
    public void autoMove()
    {
    	boolean works;
    	if(comingFrom + 2 <= 4)
    		works = move(comingFrom + 2);
    	else
    		works = move(comingFrom - 2);
    	
    	if(!works)
    	{
   			boolean[] directions = new boolean[4];
   			for(int ryanC = 0; ryanC < directions.length; ryanC++) //lol
    		{
    			if(canMove(ryanC) && ryanC != comingFrom && ryanC != (comingFrom + 2 <= 4 ? comingFrom + 2 : comingFrom - 2))
    				directions[ryanC] = true;
   			}
   			Random rand = new Random();
       		int randIndex = rand.nextInt(directions.length);
       		while(!directions[randIndex])
        		randIndex = rand.nextInt(directions.length);
       		comingFrom = randIndex+2 >= 4 ? randIndex - 2 : randIndex + 2;
       		move(randIndex);
    	}
    }
}
package com.jasonalexllc.mob;

import com.jasonalexllc.main.CoreDefense;
import com.jasonalexllc.main.Game;
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
    private int damage, comingFrom;
    private double speed, x, y;
    private double indexOfSprite;
    public static final int UP = 0, RIGHT = 1, DOWN = 2, LEFT = 3;
    private boolean alive = true;
    private boolean[][][] usedDirs = new boolean[800][800][4];//Boolean 3D array of which direction has already been tried
    
    public Mob(double speed, int x, int y, Image[] sprite, int damage)
    {
    	this.sprite = sprite;
    	this.x = x;
    	this.y = y;
    	this.speed = speed;
    	this.damage = damage;
    	indexOfSprite = 0;
    	comingFrom = LEFT;//Starts moving from the left
    }
    
    public Mob(int x, int y, Image[] sprite)
    {
    	this(0.25, x, y, sprite, 1);//Default speed: 0.25	Default Damage: 1
    }
    
    /**
     * Moves the mob in a direction if possible.
     * @param direction
     * @return Whether or not the move is successful
     */
    public boolean move(int direction)
    {
    	boolean ret = false;
    	
    	if(direction < 0 || direction > 3)
    		ret = false;
    	
    	else if(direction == UP && canMove(UP))
    	{
			y -= speed;
			ret = true;
			usedDirs[(int)x/50][(int)y/50][UP] = true;
    	}
		
    	else if(direction == RIGHT && canMove(RIGHT))
    	{
			x += speed;
			ret = true;
			usedDirs[(int)x/50][(int)y/50][RIGHT] = true;
    	}
    	
    	else if(direction == DOWN && canMove(DOWN))
    	{
			y += speed;
			ret = true;
			usedDirs[(int)x/50][(int)y/50][DOWN] = true;
    	}
		
    	else if(direction == LEFT && canMove(LEFT))
    	{
			x -= speed;
			ret = true;
			usedDirs[(int)x/50][(int)y/50][LEFT] = true;
    	}
    	if(x > 730 || y > 730 || x < 0 || y < 0)
    	{
    		alive = false;
    		Game.lives -= damage;
    	}
    	
    	return ret;
    }
    /**
     * 
     * @param direction
     * @return Whether it can move in the specified direction
     */
    private boolean canMove(int direction)
    {
    	boolean ret = true;
    	
    	if(direction < 0 || direction > 3)
    		ret = false;
    	
    	else
    	{
    		if(direction == UP)
    			ret = CoreDefense.grid[((int)(y - speed)/50)][((int)x+1)/50].getType() == Tile.PATH 
    				&& !usedDirs[(int)x][(int)y][UP];
    		
    		else if(direction == RIGHT)
    			ret = CoreDefense.grid[((int)y + 1)/50][(((int)(x + speed)/50) + 1)].getType() == Tile.PATH 
    				&& !usedDirs[(int)x][(int)y][RIGHT];
    		
    		else if(direction == DOWN)
    			ret = CoreDefense.grid[(((int)(y + speed)/50) + 1)][((int)x+1)/50].getType() == Tile.PATH 
    				&& !usedDirs[(int)x][(int)y][DOWN];
    		
    		else if(direction == LEFT)
    			ret = CoreDefense.grid[((int)y+1)/50][((int)(x - speed)/50)].getType() == Tile.PATH 
    					&& !usedDirs[(int)x][(int)y][LEFT];
    	}
    	
    	return ret;
    }
    
    public void draw(Graphics2D g2)
    {
    	if(alive)
    	{
    		indexOfSprite += .02;
    		if(indexOfSprite > sprite.length)
    			indexOfSprite = 0;
    		g2.drawImage(sprite[(int)indexOfSprite], (int)x, (int)y, null);
    		autoMove();
    	}
    }
    /**
     * Automatically moves the mob once in the direction it was moving if possible.
     * Otherwise, a new direction is chosen
     */
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
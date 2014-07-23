package com.jasonalexllc.level;

import com.jasonalexllc.main.*;
import java.util.Random;
import java.awt.*;

/**
 * Handles mob generation in game
 * @author Jason Carrete, Alex Berman
 * @since Jun 20, 
 */
public class Mob 
{
    private Image[] sprite;
    private int damage, comingFrom, level;
    private double speed, x, y, indexOfSprite;
    public static final int UP = 0, RIGHT = 1, DOWN = 2, LEFT = 3;
    private boolean alive = true, diedOnce = true;
    private boolean[][][] usedDirs = new boolean[800][800][4]; //Boolean 3D array of which direction has already been tried
    private int[][] path;
    
	private Image[][] sprites = 
		{
			{
				CoreDefense.getImage("assets/mobs/dirtman_1.png"), CoreDefense.getImage("assets/mobs/dirtman_2_4.png"), CoreDefense.getImage("assets/mobs/dirtman_3.png"),
				CoreDefense.getImage("assets/mobs/dirtman_2_4.png") 
			},
			{
				CoreDefense.getImage("assets/mobs/stoneman_1.png"), CoreDefense.getImage("assets/mobs/stoneman_2_4.png"), CoreDefense.getImage("assets/mobs/stoneman_3.png"),
				CoreDefense.getImage("assets/mobs/stoneman_2_4.png")
			}
		};
    
    public Mob(double speed, double x, double y, int damage, int level)
    {
    	this.sprite = sprites[level];
    	this.x = x;
    	this.y = y;
    	this.speed = speed;
    	this.damage = damage;
    	this.level = level;
    	double origx = this.x;
    	double origy = this.y;
    	//this.pathFind(this.getX(), this.getY(), new boolean[800][800][4]);
    	indexOfSprite = 0;
    	comingFrom = LEFT; //Starts moving from the left
    	path = new int[16][16];
    	for(int  i = 0; i < path[0].length; i++)
    	{
    		for(int j = 0; j < path.length; j++)
    			path[i][j] = -1;
    	}

    }
    
    public Mob(double x, double y,  int level)
    {
    	this(0.5, x, y, 1, level); //Default speed: 0.25, Default Damage: 1
    }
    
    public void draw(Graphics2D g2)
    {
    	if(alive)
    	{
    		indexOfSprite += 0.035;
    		if(indexOfSprite > sprite.length)
    			indexOfSprite = 0;
    		
    		double rotate = 0;
    		if(comingFrom == UP)
    			rotate = Math.toRadians(90);
    		else if(comingFrom == RIGHT)
    			rotate = Math.toRadians(180);
    		else if(comingFrom == DOWN)
    			rotate = Math.toRadians(270);
    		
    		g2.rotate(rotate, (int)x + 25, (int)y + 25);
    		
    		g2.drawImage(sprite[(int)indexOfSprite], (int)x, (int)y, null);
    		
    		g2.rotate((-1 * rotate), (int)x + 25, (int)y + 25);
    		autoMove();
    	}
    }
    
    public int getX()
    {
    	return (int)x;
    }
    
    public int getY()
    {
    	return (int)y;
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
    	try
    	{
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
    	}
    	catch(ArrayIndexOutOfBoundsException ex)
    	{
    		if(diedOnce && alive)
    		{
    			Game.lives -= damage;
    			diedOnce = false;
    		}
    		
    		if(x >= 800 || y >= 800)
    			alive = false;
    	}
    	
    	return ret;
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
    			if(canMove(ryanC) && ryanC != comingFrom && ryanC != (comingFrom + 2 <= 4 ? comingFrom + 2 : comingFrom - 2))
    				directions[ryanC] = true;
   			
   			Random rand = new Random();
       		int randIndex = rand.nextInt(directions.length);
       		
       		while(!directions[randIndex])
        		randIndex = rand.nextInt(directions.length);
       		
       		comingFrom = randIndex+2 >= 4 ? randIndex - 2 : randIndex + 2;
       		move(randIndex);
       		x = (int)(x+0.5);
       		y = (int)(y+0.5);
    	}
    }
    
    public boolean isAlive()
    {
    	return alive;
    }
    
    public void hit()
    {
    	level--;
    	if(level == -1)
    		alive = false;
    	else
    		sprite = sprites[level];
    }
    
//    public boolean pathFind(int x, int y, boolean[][][] used)
//    {
//    	boolean ret = false;
//    	Random rand = new Random();
//    	int nextX = x, nextY = y;
//    	this.x = x;
//    	this.y = y;
//    	path[y/50][x/50] = ret && pathFind( x, y, used);
//    	return ret && pathFind( x, y, used);
//    }
}
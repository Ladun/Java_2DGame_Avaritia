package com.ladun.game.objects.environments;

import com.ladun.engine.GameContainer;
import com.ladun.engine.Renderer;
import com.ladun.game.GameManager;
import com.ladun.game.components.AABBComponent;
import com.ladun.game.objects.GameObject;

public class Slope extends GameObject{

	
	float x1,y1,x2,y2;
	boolean dirIsRight;
	
	public Slope(int posX, int posY,int width, boolean dirIsRight)
	{
		this.tag 		= "slope";
		this.posX 		= posX;
		this.posY 		= posY;
		this.width 		= width;
		this.height 	= GameManager.TS;
		this.dirIsRight = dirIsRight;
		
		x1  	= posX;
		x2 		= posX + width;
		if(dirIsRight){
			y1  	= posY;
			y2 		= posY + height;
		}
		else{
			y1  	= posY + height;
			y2 		= posY;
		}
		
		
		
		this.addComponent(new AABBComponent(this));
	}
	@Override
	public void update(GameContainer gc, float dt) {
		// TODO Auto-generated method stub
		this.updateComponents(gc, dt);
		
	}

	@Override
	public void render(GameContainer gc, Renderer r) {
		// TODO Auto-generated method stub\.
		
		//r.drawFillRect((int)x1, (int)y1, 1, 1, 0xff558899);
		//r.drawFillRect((int)x2, (int)y2, 1, 1, 0xff558899);
		
		this.renderComponents(gc, r);
	}
	
	public float checkCol(int x, int halfW, int y)
	{
		
		
		//return -1;
		
		
		int ox, oy; 
	//	if(Math.abs(halfW) >= 16)
	//		halfW = 15;
		
		oy = y;
		if(dirIsRight)
			ox = x - halfW;
		else
			ox = x + halfW;

		if(ox  < x1 || ox  > x2 )
			return -1;

		//±³Á¡
		double atX = ((x2 - x1) * (oy - y1))/ (y2 - y1) + x1;

		if(dirIsRight)
		{	
			if(ox < atX)
			{
				return Math.abs(  ( ((y2 - y1) / (x2 - x1)) * (ox  - x1) + y1) - oy);
			}
		}
		else{
			if(ox > atX)
			{
				return Math.abs((((y2 - y1) / (x2 - x1)) * (ox  - x1) + y1) - oy);

			}
		}
		return -1;
	}

	public float checkCol(int x, int y)
	{
		float equationY = (((y2 - y1) / (x2 - x1)) * (x  - x1) + y1);
		
		if(equationY > y)
		{
			return equationY - y;
		}
		return -1;
	}
	
	@Override
	public void collision(GameObject other) {
		
	}

}

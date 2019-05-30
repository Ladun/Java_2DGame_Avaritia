package com.ladun.game.objects.environments;

import com.ladun.engine.GameContainer;
import com.ladun.engine.Renderer;
import com.ladun.game.objects.GameObject;

public class Water extends GameObject{
	
	private float 	fx = 0;
	private float 	fa = 0;
	
	private int 	color;
	
	
	
	public Water(int posX,int posY,int width, int height,int color)
	{
		this.tag 	= "Water";
		this.posX 	= posX;
		this.posY 	= posY;
		this.width	= width;
		this.height = height;
		this.color 	= color;
	}

	@Override
	public void update(GameContainer gc, float dt) {
		// TODO Auto-generated method stub
		fa++;
		fx = fa;
	}

	@Override
	public void render(GameContainer gc, Renderer r) {
		// TODO Auto-generated method stub
		
		r.drawWater((int)posX, (int)posY, width, height, (int)fx,color);
	}

	@Override
	public void collision(GameObject other) {
		// TODO Auto-generated method stub
		
	}

	
}

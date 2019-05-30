package com.ladun.game.objects.environments;

import com.ladun.engine.GameContainer;
import com.ladun.engine.Renderer;
import com.ladun.game.components.AABBComponent;
import com.ladun.game.objects.GameObject;

public class OneWayPlatform extends GameObject{

	
	private int color = (int)(Math.random() * Integer.MAX_VALUE);
	
	
	public OneWayPlatform(int x, int y,int width,int height)
	{
		this.tag = "OneWay";
		this.width = width;
		this.height = height;
		this.padding = 0;
		this.paddingTop = 0;
		this.posX = x;
		this.posY = y;
		
		this.addComponent(new AABBComponent(this));
	}
	
	@Override
	public void update(GameContainer gc, float dt) {
		// TODO Auto-generated method stub
		
		this.updateComponents(gc, dt);
	}

	@Override
	public void render(GameContainer gc, Renderer r) {
		// TODO Auto-generated method stub

		r.drawFillRect((int)posX, (int)posY, width, height, color);
		this.renderComponents(gc, r);
	}

	@Override
	public void collision(GameObject other) {
		// TODO Auto-generated method stub
		
	}
	
	

}

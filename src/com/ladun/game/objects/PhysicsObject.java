package com.ladun.game.objects;

import com.ladun.engine.GameContainer;
import com.ladun.engine.Renderer;
import com.ladun.game.components.AABBComponent;

public class PhysicsObject extends GameObject{

	private boolean collided;

	public PhysicsObject(String tag,float posX,float posY,int width, int height) {
		// TODO Auto-generated constructor stub
		this.tag	= tag;
		this.posX	= posX;
		this.posY 	= posY;
		this.width	= width;
		this.height = height;
		
		this.addComponent(new AABBComponent(this));
	}
	@Override
	public void update(GameContainer gc, float dt) {
		// TODO Auto-generated method stub
		collided = false;
		this.updateComponents(gc, dt);
	}

	@Override
	public void render(GameContainer gc, Renderer r) {
		// TODO Auto-generated method stub
		this.renderComponents(gc, r);
	}

	@Override
	public void collision(GameObject other) {
		// TODO Auto-generated method stub
		if(other.getTag().equalsIgnoreCase("player") || other.getTag().equalsIgnoreCase("observer"))
		{
			collided = true;
		}
	}
	public boolean isCollided() {
		return collided;
	}

}

package com.ladun.game.objects.creatures;

import com.ladun.engine.GameContainer;
import com.ladun.engine.Renderer;
import com.ladun.game.components.AABBComponent;
import com.ladun.game.objects.GameObject;

public class AttackBox extends GameObject {
	
	
	private GameObject parent;
	
	public AttackBox(GameObject parent)
	{
		this.tag		= "AttackBox";
		this.active 	= false;
		this.parent		= parent;

		this.addComponent(new AABBComponent(this));
	}

	@Override
	public void update(GameContainer gc, float dt) {
		
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
		
	}

	
	public void Active(int x,int y)
	{
		this.posX 		= x;
		this.posY 		= y;
		this.active 	= true;
	}
	
	public void Active(int x,int y,int width, int height)
	{
		this.Active(x,y);
		this.width 		= width;
		this.height		= height; 
	}

	public GameObject getParent() {
		return parent;
	}
	
	
}

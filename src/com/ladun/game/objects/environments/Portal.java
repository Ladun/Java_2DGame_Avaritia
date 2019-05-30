package com.ladun.game.objects.environments;

import com.ladun.engine.GameContainer;
import com.ladun.engine.Renderer;
import com.ladun.game.components.AABBComponent;
import com.ladun.game.objects.GameObject;
import com.ladun.game.objects.Interactable;
import com.ladun.game.objects.creatures.Player;

public class Portal extends GameObject implements Interactable{
	
	private static float 		MOVING_TIME		= .5f;
	
	private float		desX;
	private float 		desY;
	private float 		time;

	private boolean 	isMoving;//포탈을 통해 이동중
	
	private GameObject	target;
	private float		targetX;
	private float		targetY;
	
	public Portal(float posX, float posY, float desX, float desY,int width, int height)
	{
		this.tag		= "portal";
		this.posX 		= posX;
		this.posY		= posY;
		this.desX		= desX;
		this.desY 		= desY;
		this.width		= width;
		this.height		= height;
		
		this.addComponent(new AABBComponent(this));
	}
	
	@Override
	public boolean Interact(GameObject go, boolean bool) {
		// TODO Auto-generated method stub
		target 		= go;
		isMoving 	= true;
		target.setActive(false);
		targetX		= (target.getPosX() - desX);
		targetY		= (target.getPosY() - desY);
		return false;
	}

	@Override
	public void update(GameContainer gc, float dt) {
		// TODO Auto-generated method stub
		if(isMoving)
		{
			time+= dt;

			float tx = targetX * (1 / MOVING_TIME * dt);
			float ty = targetY * (1 / MOVING_TIME * dt);
			((Player)target).setPos(tx,ty);
			if( time > MOVING_TIME)
			{
				target.setActive(true);
				isMoving 	= false;
				time		= 0;
			}
		}
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

}

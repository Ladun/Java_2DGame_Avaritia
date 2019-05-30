package com.ladun.game.objects;

import com.ladun.engine.GameContainer;
import com.ladun.engine.Renderer;
import com.ladun.engine.gfx.Font;

public class displayDamage extends GameObject{
	
	private float damage;
	
	private float speed = 40;
	private float lifeTime = 1;
	
	
	public displayDamage(float damage,float posX,float posY)
	{
		this.tag 	= "displayDamage";
		this.damage = damage;
		this.posX 	= posX;
		this.posY 	= posY;
	}

	@Override
	public void update(GameContainer gc, float dt) {
		lifeTime 	-= dt;
		posY 		-= speed * dt;
		
		if(lifeTime <= 0)
		{
			this.destroy = true;
		}
	}

	@Override
	public void render(GameContainer gc, Renderer r) {
		
		r.drawText(String.valueOf((int)damage), (int)posX, (int)posY, 0xffffaa00,Font.STANDARD);
		
	}

	@Override
	public void collision(GameObject other) {
		// TODO Auto-generated method stub
		
	}
}

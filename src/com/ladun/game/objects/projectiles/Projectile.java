package com.ladun.game.objects.projectiles;

import com.ladun.game.GameManager;
import com.ladun.game.objects.GameObject;

public abstract class Projectile extends GameObject{
	

	protected int tileX, tileY;
	protected float offX, offY;

	protected int damage;
	
	protected float speed = 300f;
	
	public Projectile(int tileX, int tileY,float offX, float offY,int damage)
	{
		this.tag 	= "Projectile";
		
		this.damage	= damage;
		this.tileX 	= tileX;
		this.tileY 	= tileY;
		this.offX 	= offX;
		this.offY	= offY;
		posX 		= tileX * GameManager.TS + offX;
		posY 		= tileY * GameManager.TS + offY;
		

	}

	public int getDamage() {
		DestroyObject();
		return damage;
	}

	public abstract void DestroyObject();


}

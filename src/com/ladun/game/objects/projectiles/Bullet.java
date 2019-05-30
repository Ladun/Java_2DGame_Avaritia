package com.ladun.game.objects.projectiles;

import com.ladun.engine.GameContainer;
import com.ladun.engine.Renderer;
import com.ladun.game.GameManager;
import com.ladun.game.components.AABBComponent;
import com.ladun.game.objects.GameObject;
import com.ladun.game.objects.creatures.AliveObject;
import com.ladun.game.objects.creatures.Enemy;
import com.ladun.game.objects.environments.Door;

public class Bullet extends Projectile{
	
	private float radian;
	private int dy;
	
	public Bullet(int tileX,int tileY, float offX, float offY,int dy,
				  float speed, boolean isMirrorX, int damage)
	{
		super(tileX, tileY, offX, offY, damage);
		this.speed 	= speed ;
		this.dy		= dy;
		if(isMirrorX)
			this.speed 		*= -1;
		
		this.width		= 4 + (int)(Math.abs(speed) * GameContainer.getUpdateCap());
		radian = (float)Math.atan2(dy,width);
		
		this.height 	= 3;
		this.addComponent(new AABBComponent(this));
	}
	@Override
	public void update(GameContainer gc, float dt) {
		// TODO Auto-generated method stub

		Moving(gm,dt);
		
		AdjustPosition();
		this.updateComponents(gc, dt);
	}

	@Override
	public void render(GameContainer gc, Renderer r) {
		// TODO Auto-generated method stub
		r.drawLine((int)posX, (int)posY, (int)posX + width, (int)posY + dy, 0xaaffeeaa);
		this.renderComponents(gc, r);
	}

	@Override
	public void collision(GameObject other) {
		// TODO Auto-generated method stub
		
		if(other.getTag().equalsIgnoreCase(("Platform")))
			DestroyObject();
		else if (other.getTag().equalsIgnoreCase("Door") )
		{
			Door d = (Door)other;
			if(!d.isOpened() && d.getHealth() > 0){
				AABBComponent 	myC		= (AABBComponent) this.findComponent("aabb");
				AABBComponent 	otherC 	= (AABBComponent) other.findComponent("aabb");
				
				int otherCenter = otherC.getCenterX() - d.getCenterModify();
				
				if(myC.getCenterX() < otherCenter)
				{
					int distance 	= (myC.getHalfWidth() + 3) - (otherCenter - myC.getCenterX());
					if(distance > 0){
						d.hit();
						DestroyObject();
					}
				}
				if(myC.getCenterX() > otherCenter)
				{
					int distance 	= (myC.getHalfWidth() + 3) - (myC.getCenterX() - otherCenter);
					if(distance > 0){
						d.hit();
						DestroyObject();
					}
				}
			}
		}
		else if(other instanceof Enemy)
		{
			if(!((AliveObject)other).isDieing()){
				((Enemy)other).Hit(this.damage);
				DestroyObject();
			}
		}
	}
		
	public void Moving(GameManager gm, float dt)
	{
		float dir = Math.signum(speed);
		int tx 	= tileX;
		int ty1 = tileY,ty2 = tileY;
		
		offX += (speed * dt) * Math.cos(radian);
		offY += (speed * dt) * Math.sin(radian);
		
		if(height - paddingTop < GameManager.TS/ 2){
			if(offY + paddingTop < 0){
				ty1--;
			}
			if(offY +(height - paddingTop) < 0)
			{
				ty2--;
			}
		}

		if(dir > 0)
		{

			if(gm.getMap().getCollision(tx, ty1) ||
					gm.getMap().getCollision(tx, ty2))
			{
				if(offX + width - padding * 2> padding){
					DestroyObject();
				}
					
			}
		}
		else
		{
			if(gm.getMap().getCollision(tx-1, ty1)||
					gm.getMap().getCollision(tx-1, ty2))
			{
				if(offX < -padding){
					DestroyObject();
				}
			}
		}
		
	}
	
	public void AdjustPosition()
	{
		//Final position
		if(offY > GameManager.TS / 2)
		{
			tileY++;
			offY -= GameManager.TS;
		}
		if(offY <  -GameManager.TS / 2)
		{
			tileY--;
			offY += GameManager.TS;
		}
		
		if(offX > GameManager.TS / 2)
		{
			tileX++;
			offX -= GameManager.TS;
		}
		if(offX <  -GameManager.TS / 2)
		{
			tileX--;
			offX += GameManager.TS;
		}
		//--

		posX = tileX * GameManager.TS + offX;
		posY = tileY * GameManager.TS + offY;
	}
	
	public void DestroyObject()
	{
		this.active = false;
		this.destroy = true;
	}
	

	
}

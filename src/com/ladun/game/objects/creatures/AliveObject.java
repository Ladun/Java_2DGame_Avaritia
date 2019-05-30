package com.ladun.game.objects.creatures;

import com.ladun.game.GameManager;
import com.ladun.game.components.AABBComponent;
import com.ladun.game.objects.GameObject;
import com.ladun.game.objects.displayDamage;
import com.ladun.game.objects.environments.Door;
import com.ladun.game.objects.environments.OneWayPlatform;
import com.ladun.game.objects.environments.Platform;
import com.ladun.game.objects.environments.Slope;

public abstract class AliveObject extends GameObject implements IDamageable{	
	
	public enum DirState  {RIGHT, LEFT}

	protected float 	maxHealth;
	protected float 	health;
	
	protected int 		tileX, tileY;
	protected float 	offX, offY;
	
	protected float 	speed;
	protected float 	fallSpeed;
	protected float 	jump;
	protected float 	fallDistance ;
	protected boolean 	ground;
	protected boolean 	groundLast;
	protected boolean 	dieing;
	protected boolean	attacking;
	//Move--------------------------------------------------------------------------
	protected DirState	dirState;
	protected boolean 	moving;
	protected boolean 	collisionDetected;
	protected boolean 	isHanging;
	//Hit---------------------------------------------------------------------------
	protected float 	nextHitTime		= 0;
	protected float 	hitBetween		= .2f;
	protected boolean 	hitting;
	//------------------------------------------------------------------------------
	
	public AliveObject(int tileX,int tileY,float health)
	{
		this.tileX 		= tileX;
		this.tileY		= tileY;
		this.offX 		= 0;
		this.offY 		= 0;
		this.posX 		= tileX * GameManager.TS ;
		this.posY 		= tileY * GameManager.TS ;
		this.maxHealth 	= health;		
		this.health		= this.maxHealth;
		
		this.dirState 	= DirState.RIGHT;
	}
	
	@Override
	public void collision(GameObject other)
	{
		collisionDetected = true;
		if(other.getTag().equalsIgnoreCase("Slope"))
		{
			AABBComponent myC 		= (AABBComponent) this.findComponent("aabb");
			
			float distance = ((Slope)other).checkCol(
					myC.getCenterX(),
					myC.getHalfWidth(),
					myC.getCenterY() + myC.getHalfHeight());
			
			
			if(distance != -1)
			{
				offY 			-= distance;
				posY 			-= distance;
				ground 			= true;
				fallDistance 	= 0;
				myC.setCenterY(myC.getCenterY() - (int)distance);
			}
			
		}
		else if(other.getTag().equalsIgnoreCase("Platform"))
		{
			if(!isHanging){
				AABBComponent myC 		= (AABBComponent) this.findComponent("aabb");
				AABBComponent otherC 	= (AABBComponent) other.findComponent("aabb");

				if(Math.abs(myC.getLastCenterX() - otherC.getLastCenterX()) < myC.getHalfWidth() + otherC.getHalfWidth()){
					if(myC.getCenterY() < otherC.getCenterY())
					{
						int distance 	= (myC.getHalfHeight() + otherC.getHalfHeight()) - (otherC.getCenterY() - myC.getCenterY());
						offY 			-= distance;
						posY 			-= distance;
						myC.setCenterY(myC.getCenterY() - distance);
						fallDistance 	= 0;
						ground 			= true;
					}
					if(myC.getCenterY() > otherC.getCenterY())
					{
						int distance 	= (myC.getHalfHeight() + otherC.getHalfHeight()) - (myC.getCenterY() - otherC.getCenterY());
						offY 			+= distance;
						posY 			+= distance;
						myC.setCenterY(myC.getCenterY() + distance);
						fallDistance 	= 0;
					}
				}
				else
				{
					if(myC.getCenterX() < otherC.getCenterX())
					{
						int distance 	= (myC.getHalfWidth() + otherC.getHalfWidth()) - (otherC.getCenterX() - myC.getCenterX());
						offX 			-= distance;
						posX 			-= distance;
						moving 			= false;
						myC.setCenterX(myC.getCenterX() - distance);
					}
					if(myC.getCenterX() > otherC.getCenterX())
					{
						int distance 	= (myC.getHalfWidth() + otherC.getHalfWidth()) - (myC.getCenterX() - otherC.getCenterX());
						offX 			+= distance;
						posX 			+= distance;
						moving 			= false;
						myC.setCenterX(myC.getCenterX() + distance);
					}
				}
			}
		}
		else if(other.getTag().equalsIgnoreCase("OneWayPlatform"))
		{
			AABBComponent myC 		= (AABBComponent) this.findComponent("aabb");
			AABBComponent otherC 	= (AABBComponent) other.findComponent("aabb");
			
			//if(Math.abs(myC.getLastCenterX() - otherC.getLastCenterX()) < myC.getHalfWidth() + otherC.getHalfWidth()){
			if(fallDistance > 0){
				//System.out.println(fallDistance);
				if(myC.getLastCenterY() + myC.getHalfHeight() -.5<= otherC.getLastCenterY() - (otherC.getHalfHeight() - fallDistance))
				{
					int distance 	= (myC.getHalfHeight() + otherC.getHalfHeight()) - (otherC.getCenterY() - myC.getCenterY());
					offY 			-= distance;
					posY 			-= distance;
					myC.setCenterY(myC.getCenterY() - distance);
					fallDistance 	= 0;
					ground 			= true;
				}

			}
		}
		else if(other.getTag().equalsIgnoreCase("Door"))
		{
			if(!((Door)other).isOpened()){
				AABBComponent 	myC		= (AABBComponent) this.findComponent("aabb");
				AABBComponent 	otherC 	= (AABBComponent) other.findComponent("aabb");
				Door 			door	= (Door)other;
	
				int otherCenter = otherC.getCenterX() - door.getCenterModify();
	
				if(myC.getCenterX() < otherCenter)
				{
					int distance 	= (myC.getHalfWidth() + 3) - (otherCenter - myC.getCenterX());
					if(distance > 0){
						offX 			-= distance;
						posX 			-= distance;
						moving 			= false;
						myC.setCenterX(myC.getCenterX() - distance);
					}
				}
				if(myC.getCenterX() > otherCenter)
				{
					int distance 	= (myC.getHalfWidth() + 3) - (myC.getCenterX() - otherCenter);
					if(distance > 0){
						offX 			+= distance;
						posX 			+= distance;
						moving 			= false;
						myC.setCenterX(myC.getCenterX() + distance);
					}
				}
			}
		}
		else if(other.getTag().equalsIgnoreCase("Water"))
		{
			
		}
		else collisionDetected = false;
	}
	
	@Override
	public void TakeDamage(int damage) {

		if(nextHitTime >= hitBetween){
			gm.addObject(new displayDamage(damage,posX,posY));
	
			nextHitTime = 0;
			health -= damage;
			if(health <= 0)
				Dead();
		}
		
	}
	
	private void Dead()
	{
		this.dieing = true;
		//this.active = false;
	}
	
	//Getter and Setter
	public void recoveryHealth(float h)
	{
		health += h;
		if(health > maxHealth)
			health = maxHealth;
	}	
	public int getTileX() {
		return tileX;
	}
	public float getHealth() {
		return health;
	}
	public float getMaxHealth() {
		return maxHealth;
	}
	public int getTileY() {
		return tileY;
	}
	public float getOffX() {
		return offX;
	}
	public float getOffY() {
		return offY;
	}
	public boolean isDieing() {
		return dieing;
	}


	public boolean isAttacking() {
		return attacking;
	}
	
	
	
	
}

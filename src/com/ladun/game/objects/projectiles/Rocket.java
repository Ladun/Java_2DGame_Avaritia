package com.ladun.game.objects.projectiles;

import com.ladun.engine.GameContainer;
import com.ladun.engine.Renderer;
import com.ladun.engine.gfx.ImageTile;
import com.ladun.game.GameManager;
import com.ladun.game.components.AABBComponent;
import com.ladun.game.objects.GameObject;
import com.ladun.game.objects.creatures.AliveObject;
import com.ladun.game.objects.creatures.AttackBox;
import com.ladun.game.objects.creatures.Enemy;
import com.ladun.game.objects.environments.Door;
import com.ladun.game.objects.particles.ParticleSystem;

public class Rocket extends Projectile{
	
	private ImageTile image = new ImageTile("/Resources/Object/Projectile/Rocket.png",16,8);
	
	private float anim 					= 0;
	private float bulletAnimMaxIndex	= 4;
	
	private AttackBox attackBox;
	
	public Rocket(int tileX,int tileY, float offX, float offY,
				  float speed, boolean isMirrorX, int damage)
	{
		super(tileX, tileY, offX, offY, damage);
		this.speed 	= speed ;
		if(isMirrorX)
			this.speed 		*= -1;
		
		image.setMirrorX(isMirrorX);
		
		
		this.width		= 16 + (int)(Math.abs(speed) * GameContainer.getUpdateCap());
		
		this.height 	= 8;
		
		this.attackBox 	= new AttackBox(this);
		this.addComponent(new AABBComponent(this));
	}
	@Override
	public void update(GameContainer gc, float dt) {
		// TODO Auto-generated method stub

		Moving(gm,dt);
		
		for(int i = 0; i < 2; i ++){
			float effectPosX = (Math.signum(speed) > 0 )? posX - 6 - i * 9 + 3:posX + 14 + i * 9;
			float effectPosY = posY - 2;
			gm.addObject(new ParticleSystem(null,
					effectPosX,effectPosY,
					.3f,1,
					0,0,25,
					0,.2f,
					false,
					new ImageTile("/Resources/Object/Particle/Fire_Particle.png",12,12),
					ParticleSystem.MoveType.Circle));
		}
		
		
		AdjustPosition();
		
		anim += dt * 10;
		if(anim > bulletAnimMaxIndex)
			anim = 0;
		
		this.updateComponents(gc, dt);
	}

	@Override
	public void render(GameContainer gc, Renderer r) {
		// TODO Auto-generated method stub
		
		r.drawImageTile(image,(int)posX, (int)posY,(int)anim,0);
		
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
		
		offX += (speed * dt);
		
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
		gm.addObject(new ParticleSystem(null,
				posX,posY,
				.15f,40,
				1.5f,0,3,
				1,.075f,
				false,
				new ImageTile("/Resources/Object/Particle/FireBall_Particle.png",12,12),
				ParticleSystem.MoveType.Circle));
		this.destroy = true;
	}
}

package com.ladun.game.objects.projectiles;

import com.ladun.engine.GameContainer;
import com.ladun.engine.Renderer;
import com.ladun.engine.gfx.ImageTile;
import com.ladun.engine.gfx.Light;
import com.ladun.game.GameManager;
import com.ladun.game.components.AABBComponent;
import com.ladun.game.objects.GameObject;
import com.ladun.game.objects.particles.ParticleSystem;

public class FireBall extends Projectile{
	

	private ImageTile image = new ImageTile("/Resources/Object/Projectile/FireBall.png",32,32);
	
	private float anim 					= 0;
	private float bulletAnimMaxIndex	= 4;
	private Light fireL;
	
	
	public FireBall(int tileX, int tileY, float offX, float offY, boolean isMirrorX) {
		super(tileX, tileY, offX, offY, 1);

		this.speed 		= 300f;
		this.image.setMirrorX(isMirrorX);
		if(isMirrorX)
			speed 		*= -1;

		this.width 		= 16;
		this.height 	= 13;
		this.padding 	= 0;
		this.paddingTop = 3;
		

		fireL = new Light(24,0xffffe1a7,1);

		this.addComponent(new AABBComponent(this));

	}	
	
	@Override
	public void update(GameContainer gc, float dt) {
		// TODO Auto-generated method stub
		
		Moving(gm,dt);
		
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
		//r.drawLight(fireL, (int)(posX + width / 2), (int)(posY + height / 2 + paddingTop / 2));
		
		this.renderComponents(gc, r);
	}
	@Override
	public void collision(GameObject other) {
	}
	
	public void Moving(GameManager gm, float dt)
	{
		float dir = Math.signum(speed);
		int tx 	= tileX;
		int ty1 = tileY,ty2 = tileY;
		
		offX += speed * dt;
		
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

		gm.addObject(new ParticleSystem(this,
			posX,posY,
			.3f,10,
			1f,.2f,15,
			0,.2f,
			false,
			new ImageTile("/FireBall_Particle.png",16,16),
			ParticleSystem.MoveType.Circle));
		
		this.destroy = true;
	}
	

}

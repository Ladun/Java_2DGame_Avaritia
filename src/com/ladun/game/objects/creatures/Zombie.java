package com.ladun.game.objects.creatures;

import com.ladun.engine.GameContainer;
import com.ladun.engine.Renderer;
import com.ladun.engine.gfx.ImageTile;
import com.ladun.game.GameManager;
import com.ladun.game.components.AABBComponent;
import com.ladun.game.objects.GameObject;
import com.ladun.game.objects.creatures.AliveObject.DirState;
import com.ladun.game.objects.environments.Door;
import com.ladun.game.objects.environments.Slope;
import com.ladun.game.objects.particles.ParticleSystem;

public class Zombie extends Enemy {
	
	public enum AnimState {IDLE,WALK,RUN,HIT,ATTACK,DEAD};
	
	
	private ImageTile 	image 	= new ImageTile("/Resources/Object/Creatures/Zombie1.png",64,64);

	
	//Animation---------------------------------------------------------------------
	private float 		anim 			= 0;
	private int 		animType 		= 0;
	private float 		animSpeed		= 10;
	private int[] 		animMaxIndex 	= {6,8,9,3,10,11};
	//Moving------------------------------------------------------------------------
	private int 		movingRandom;
	private float 		nextMovingTime 	= 0;
	private float 		movingBetween	= .5f;
	private boolean		chasing			= false;
	private GameObject 	target			= null;
	//Other-------------------------------------------------------------------------
	private float 		runSpeed 		= 75;
	private float 		walkSpeed		= 25f;
	//------------------------------------------------------------------------------
	
	private AttackBox 	attackBox;
	
	public Zombie(int tileX, int tileY) {
		super(tileX, tileY,3,1); 
		this.target 	= gm.getObject("Player");
		this.tag 		= "zombie";
		this.fallSpeed  = 10 ;
		this.jump 		= -3.5f;
		this.hitBetween	= 0;
		this.width 		= GameManager.TS;
		this.height 	= GameManager.TS;
		this.padding 	= 10;
		this.paddingTop = 4;

		this.walkSpeed	= (int)(Math.random() * (walkSpeed)) + walkSpeed;
		this.runSpeed	= (int)(Math.random() * (runSpeed  + 25)) + runSpeed;
		this.speed 		= walkSpeed;
		//playerImage.setLightBlock(Light.FULL);
		
		this.attackBox 	= new AttackBox(this);
		
		this.addComponent(new AABBComponent(this));
	}
	

	@Override
	public void update(GameContainer gc, float dt) {
		if(!dieing ){
			nextHitTime += dt;
			nextMovingTime += dt;
	
			if(!hitting ){
				if(readyToAttack){
					if(anim + dt * animSpeed >= animMaxIndex[animType])
					{
						readyToAttack = false;
						attacking = false;
						animSpeed = 10;
					}
					else if(anim >= 8)
					{
						attacking = false;
						attackBox.setActive(false);
					}else if(anim >= 5)
					{
						attacking = true;
						
						int attackBoxWidth = 24;
						int x = (dirState == DirState.RIGHT)? (int)posX + padding  + 3:(int)posX - (attackBoxWidth - (width - padding)) - 3;
						int y = (int)posY + 10;
						attackBox.Active(x, y,attackBoxWidth,8);
					} 
				}
				else{
					if(Math.abs(target.getPosX() - posX) < gc.getWidth()/5 && Math.abs(target.getPosY() - posY) < 48)
						chasing = true;
					else 
						chasing = false;
					if(!chasing){
						if(nextMovingTime > movingBetween){
							movingRandom = 	(int)(Math.random() * 3);
							nextMovingTime = 0;
						}
					}
					else
					{
						if(target != null)
						{
							if(nextMovingTime > movingBetween / 2){
								if(target.getPosX() > this.posX){
									movingRandom = 0;
								}
								else{
									movingRandom = 1;
								}
								nextMovingTime = 0;
							}
						}
					}
				//Left Right----------------------------------------------------------------------------------------
					if(movingRandom == 1){
						moving = true;
						dirState = DirState.LEFT;
						image.setMirrorX(true);
						if(chasing)
						{
							speed = runSpeed;
							animSpeed = 13;
							setAnim(AnimState.RUN.ordinal());
						}
						else{
							speed = walkSpeed;
							animSpeed = 10;
							setAnim(AnimState.WALK.ordinal());
						}
						if(gm.getMap().getCollision(tileX - 1, tileY) || gm.getMap().getCollision(tileX - 1, tileY + (int)Math.signum((int)offY)))
						{
							offX -= dt * speed;
							if(offX  < -padding)
								offX = -padding;
						}
						else{
							offX -= dt * speed;
						}
					}
					else if(movingRandom == 0)
					{
						moving = true;
						dirState = DirState.RIGHT;
						image.setMirrorX(false);
						if(chasing)
						{
							speed = runSpeed;
							animSpeed = 13;
							setAnim(AnimState.RUN.ordinal());
						}
						else{
			
							speed = walkSpeed;
							animSpeed = 10;
							setAnim(AnimState.WALK.ordinal());
						}
						if(gm.getMap().getCollision(tileX + 1, tileY) ||gm.getMap().getCollision(tileX + 1, tileY + (int)Math.signum((int)offY))){	
							offX += dt * speed;
							if(offX  > padding)
								offX = padding;
					
						}
						else {
							offX += dt * speed;
							
						}
					}
					else {
						moving = false;
					}
					//End of Left Right----------------------------------------------------------------------------------------
					if(target.isActive()){
						if(Math.abs(target.getPosX() - posX) < 24 && Math.abs(target.getPosY() - posY) < 32)
						{
							readyToAttack = true;
							animSpeed = 20;

							setAnim(AnimState.ATTACK.ordinal());
						}
					}
				}
			}
	
			//Gravity--------------------------------------------------------------------------------------------------
			fallDistance += dt * fallSpeed;
			if(fallDistance < 0){
				if ((gm.getMap().getCollision(tileX, tileY - 1) || 
						gm.getMap().getCollision(tileX + (int)Math.signum((int)Math.abs(offX) > padding ? offX : 0), tileY - 1)) 
						&& offY < -paddingTop)
				{
					fallDistance 	= 0;
					offY 			= -paddingTop;
				}
			}
				
			if(fallDistance > 0){
				if ((gm.getMap().getCollision(tileX, tileY + 1) ||
						gm.getMap().getCollision(tileX + (int)Math.signum((int)Math.abs(offX) > padding ? offX : 0), tileY + 1)) 
						&& offY > 0)
				{
					fallDistance 	= 0;
					offY 			= 0;
					ground 			= true;
				}
			}
				
			offY += fallDistance;
			//End of Gravity--------------------------------------------------------------------------------------------------
			
			if(hitting)
			{
				if(anim + dt * animSpeed >= animMaxIndex[animType])
				{
					hitting = false;
				}
			}
			
			//Final position-------------------------------------------------------------------------------------------------
			AdjustPosition();
			//End of Final position------------------------------------------------------------------------------------------
	
			
			//Idle
			if(!moving && ground && !hitting && !readyToAttack){
				setAnim(AnimState.IDLE.ordinal());
			}
			
			
			
		}
		else {
			this.readyToAttack = false;
			this.attacking		= false;
			setAnim(AnimState.DEAD.ordinal());
			animSpeed = 15;
			if(anim + dt * animSpeed >= animMaxIndex[animType])
			{
				this.destroy = true;
			}
			
		}
		anim 	+= dt * animSpeed;
		if(anim >= animMaxIndex[animType])
			anim = 0;

		groundLast 	= ground;
		moving 		= false;
		

		if(attackBox.isActive())
			attackBox.update(gc, dt);
		this.updateComponents(gc, dt);
	}	
	
	@Override
	public void render(GameContainer gc, Renderer r) {
		//----------------------------------------------------------------------------------
		r.setzDepth(Renderer.OBJECT_LAYER);
		r.drawImageTile(image, (int)posX - 16,(int)posY - 32, (int)anim, animType);	
		//r.drawLight(pLight, (int)posX + width / 2, (int)posY + height /2);
		//----------------------------------------------------------------------------------

		if(attackBox.isActive())
			attackBox.render(gc, r);
		this.renderComponents(gc, r);
	}
	@Override
	public void Hit(int damage)
	{

		this.readyToAttack = false;
		this.attacking		= false;
		this.TakeDamage(damage);
		hitting = true;
		nextHitTime = 0;
		setAnim(AnimState.HIT.ordinal());
		
		gm.addObject(new ParticleSystem(null,
				posX,posY,
				1f,5,
				1f,.2f,15,
				0,.2f,
				true,
				new ImageTile("/Resources/Object/Particle/Blood_Particle.png",16,16),
				ParticleSystem.MoveType.Circle));
		
		if(dirState == DirState.RIGHT)
		{
			offX -= 6;
		}
		else
			offX += 6;
		
		AdjustPosition();
	}
	@Override
	public void collision(GameObject other) {
		// TODO Auto-generated method stub/*
		super.collision(other);
		
		if(collisionDetected)
			return;
		
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
	
	public void setAnim(int index)
	{
		if(animType != index){
			
			anim		= 0;
		}
		animType 	= index;
	}

	public void setPos(float offX, float offY) {
		this.offX -= offX;
		this.offY -= offY;
		AdjustPosition();
	}

}

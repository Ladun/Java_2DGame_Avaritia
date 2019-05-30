package com.ladun.game.objects.creatures;

import com.ladun.engine.GameContainer;
import com.ladun.engine.Renderer;
import com.ladun.engine.gfx.ImageTile;
import com.ladun.game.GameManager;
import com.ladun.game.components.AABBComponent;
import com.ladun.game.objects.GameObject;
import com.ladun.game.objects.particles.ParticleSystem;

public class Tanker extends Enemy{
	public enum AnimState {IDLE,RUN,ATTACK,DEAD};
	
	
	private ImageTile 	image 	= new ImageTile("/Resources/Object/Creatures/SpecialZombie1.png",96,96);

	
	//Animation---------------------------------------------------------------------
	private float 		anim 			= 0;
	private int 		animType 		= 0;
	private float 		animSpeed		= 10;
	private int[] 		animMaxIndex 	= {5,8,10,3,10,11};
	//Moving------------------------------------------------------------------------
	private int 		movingRandom;
	private float 		nextMovingTime 	= 0;
	private float 		movingBetween	= .5f;
	private GameObject 	target			= null;
	private boolean 	inSlope;
	//Other-------------------------------------------------------------------------
	private float 		runSpeed 		= 200f;
	private float 		walkSpeed		= 25f;
	//------------------------------------------------------------------------------
	
	private AttackBox 	attackBox;

	public Tanker(int tileX, int tileY) {
		super(tileX, tileY,200,4); 
		Init();
		
		this.speed 		= walkSpeed;

		
		this.attackBox	= new AttackBox(this);
		this.addComponent(new AABBComponent(this));
	}
	
	public Tanker(int tileX,int tileY, float walkSpeed, float runSpeed,float health,int damage)
	{
		super(tileX, tileY,health,damage); 
		Init();
		this.walkSpeed 		= walkSpeed;
		this.runSpeed 		= runSpeed;
		
		this.speed 			= walkSpeed;
		
		this.attackBox	= new AttackBox(this);
		this.addComponent(new AABBComponent(this));
	}
	
	private void Init()
	{
		this.target 	= gm.getObject("Player");
		this.tag 		= "Tanker";
		this.fallSpeed  = 10 ;
		this.jump 		= -3.5f;
		this.hitBetween	= 0;
		this.width 		= GameManager.TS;
		this.height 	= GameManager.TS;
		this.padding 	= -12;
		this.paddingTop = -32;

	}

	@Override
	public void update(GameContainer gc, float dt) {
		// TODO Auto-generated method stub
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
						
						int attackBoxWidth = this.width - this.padding * 2 + 10;
						int attackBoxHeight = this.height - this.paddingTop;
						int x = (dirState == DirState.RIGHT)? (int)posX + padding  + 14:(int)posX - (attackBoxWidth - (width - padding)) - 14;
						int y = (int)posY + paddingTop;
						attackBox.Active(x, y,attackBoxWidth,attackBoxHeight);
					} 
				}
				else{
					
						if(nextMovingTime > movingBetween){
							movingRandom = 	(int)(Math.random() * 3);
							nextMovingTime = 0;
						}if(target != null)
						{
							AABBComponent 	myC		= (AABBComponent) this.findComponent("aabb");
							AABBComponent 	otherC 	= (AABBComponent) target.findComponent("aabb");
							if(nextMovingTime > movingBetween / 2){
								if(otherC.getCenterX()> myC.getCenterX()){
									movingRandom = 0;
								}
								else{
									movingRandom = 1;
								}
								nextMovingTime = 0;
							}
						}
				//Left Right----------------------------------------------------------------------------------------
					if(movingRandom == 1){
						moving = true;
						dirState = DirState.LEFT;
						image.setMirrorX(true);
						speed = runSpeed;
						animSpeed = 8;
						setAnim(AnimState.RUN.ordinal());
						
						if(!inSlope && gm.getMap().getCollision(tileX - 1, tileY) || gm.getMap().getCollision(tileX - 1, tileY + (int)Math.signum((int)offY)))
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
						speed = runSpeed;
						animSpeed = 8;
						setAnim(AnimState.RUN.ordinal());
						
						if(!inSlope && gm.getMap().getCollision(tileX + 1, tileY) ||gm.getMap().getCollision(tileX + 1, tileY + (int)Math.signum((int)offY))){	
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
					//Attacking
					if(target.isActive()){
						if(Math.abs(target.getPosX() - posX) < 24 && Math.abs(target.getPosY() - posY) < 32)
						{
							readyToAttack = true;
							animSpeed = 15f;
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
					ground 			= 
							true;
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
				gm.addObject(new ParticleSystem(this,
						posX,posY,
						3f,30,
						3f,.2f,15,
						0,.2f,
						true,
						new ImageTile("/Resources/Object/Particle/Blood_Particle.png",16,16),
						ParticleSystem.MoveType.Circle));
				this.destroy = true;
			}
			
		}
		anim 	+= dt * animSpeed;
		if(anim >= animMaxIndex[animType])
			anim = 0;

		groundLast 	= ground;
		moving 		= false;
		inSlope 	= false;

		if(attackBox.isActive())
			this.attackBox.update(gc, dt);
		this.updateComponents(gc, dt);
	}

	@Override
	public void render(GameContainer gc, Renderer r) {
		// TODO Auto-generated method stub
		//----------------------------------------------------------------------------------
		r.setzDepth(Renderer.OBJECT_LAYER);
		r.drawImageTile(image, (int)posX - 32,(int)posY - 64, (int)anim, animType);	
		//r.drawLight(pLight, (int)posX + width / 2, (int)posY + height /2);
		//----------------------------------------------------------------------------------
		if(attackBox.isActive())
			this.attackBox.render(gc, r);
		this.renderComponents(gc, r);
	}

	@Override
	public void Hit(int damage) {
		// TODO Auto-generated method stub


		this.TakeDamage(damage);
		nextHitTime = 0;
		gm.addObject(new ParticleSystem(null,
				posX,posY,
				1f,5,
				1f,.2f,15,
				0,.2f,
				true,
				new ImageTile("/Resources/Object/Particle/Blood_Particle.png",16,16),
				ParticleSystem.MoveType.Circle));
	}

	@Override
	public void collision(GameObject other) {
		// TODO Auto-generated method stub

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
}

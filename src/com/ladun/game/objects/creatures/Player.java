package com.ladun.game.objects.creatures;

import com.ladun.engine.GameContainer;
import com.ladun.engine.Renderer;
import com.ladun.engine.gfx.Font;
import com.ladun.engine.gfx.ImageTile;
import com.ladun.engine.gfx.Light;
import com.ladun.game.GameManager;
import com.ladun.game.Items.AssaultRifle;
import com.ladun.game.Items.DropItem;
import com.ladun.game.Items.Gun;
import com.ladun.game.Items.Item;
import com.ladun.game.Items.Pistol;
import com.ladun.game.Items.Shotgun;
import com.ladun.game.UI.UserInterface;
import com.ladun.game.components.AABBComponent;
import com.ladun.game.objects.GameObject;
import com.ladun.game.objects.Interactable;
import com.ladun.game.objects.environments.Door;
import com.ladun.game.objects.environments.Ladder;
import com.ladun.game.objects.environments.OneWayPlatform;
import com.ladun.game.objects.environments.Platform;
import com.ladun.game.objects.environments.Portal;
import com.ladun.game.objects.environments.Slope;
import com.ladun.game.objects.particles.ParticleSystem;

public class Player extends AliveObject {
	
	public enum AnimState {IDLE,WALK,RUN,INJUMP,PISTOL,ASLF,ATTACK,DEAD,SHOTGUN,ROLL,BAZOOKA};
	
	
	private ImageTile 	playerImage 	= new ImageTile("/Resources/Object/Creatures/Character.png",64,64);
	private Light 		pLight;
	private Light		shootLight;
	
	//Animation---------------------------------------------------------------------
	private float 		anim ;
	private int 		animType ;
	private float 		animSpeed		= 10;
	private int[] 		animMaxIndex 	= {6,8,9,0,4,4,9,7,5,7,6};
	//Boolean--------------------------------------------------------------------------
	private boolean 	lastRunning	;
	private boolean		readyToattack;
	private boolean		alreadyAttack;
	private boolean 	adrenaline;
	//Shooting----------------------------------------------------------------------
	private Gun			currentGun;
	private boolean 	readyToShoot;
	private boolean 	canShoot;
	private int			muzzleX			= 29;
	private int		 	muzzleY			= 10;
	private boolean		lastShoot;
	//ItemUse-----------------------------------------------------------------------
	private float 		itemUseTime; //사용 중 일때 시간 측정
	private float 		itemUsePercent;// 아이템 사용시간/ 현재 아이템 사용 측정 시간
	private boolean		itemUsing;
	private boolean 	reloading;
	//Evasion-----------------------------------------------------------------------
	private boolean 	avoiding;
	//Other-------------------------------------------------------------------------
	private float 		adrenalineTime;
	private final int   adrenalineFTime = 10;
	private float 		runSpeed 		= 150f * 5;
	private float 		walkSpeed		= 100f;
	private float		curStamina		= 5;
	private float		maxStamina		= 5;
	//------------------------------------------------------------------------------
	private AttackBox 	attackBox ;
	//------------------------------------------------------------------------------

	
	private Interactable curInter;
	
	
	public Player(int tileX, int tileY) {
		super(tileX,tileY,10); 
		this.tag 		= "player";
		this.speed 		= walkSpeed ;
		this.fallSpeed  = 10 ;
		this.jump 		= -3.5f;
		this.width 		= GameManager.TS;
		this.height 	= GameManager.TS;
		this.padding 	= 10;
		this.paddingTop = 4;
		this.hitBetween	= .2f;

		this.pLight 	= new Light(130,0xffffffff,.8f);
		this.shootLight	= new Light(150,0xffffffff, 1f);//0xffffe869
		//playerImage.setLightBlock(Light.FULL);
		
		this.attackBox	= new AttackBox(this);
		
		
		this.addComponent(new AABBComponent(this));
		
/*
		gm.addObject(new ParticleSystem(this,
				12, 0, 
				1f, 5, 
				-.45f, 0,6,
				-1, .1f,
				false,
				new ImageTile("/Resources/Object/Particle/Fire_Particle.png",12,12),
				ParticleSystem.MoveType.Vertical));*/
	}
	

	@Override
	public void update(GameContainer gc, float dt) {
		if(!dieing){
			nextHitTime += dt;
			if(currentGun != null)
				currentGun.TimeAdd(dt);
			
			if(adrenaline)
			{
				adrenalineTime += dt;
				
				if(adrenalineTime >= adrenalineFTime)
				{
					adrenaline 		= false;
					adrenalineTime 	= 0;
				}
			}
			
			
			
			
			if(!readyToShoot && !itemUsing	&& !readyToattack){				
				if(!avoiding){
					//Left and Right--------------------------------------------------
					if(gc.getInput().isKey(GameManager.KEYRIGHT))
					{
						dirState	= DirState.RIGHT;
						playerImage.setMirrorX(false); 
						if(!isHanging){
							if(gc.getInput().isKey(GameManager.KEYRUN)){
								if(curStamina > 0){
									if(!lastRunning){
										speed 		= (adrenaline)? runSpeed * 1.2f: runSpeed; 
										curStamina	-= dt;
										animSpeed 	= 13f;
										if(ground)
											setAnim(AnimState.RUN.ordinal());
									}
								}
								else 
								{	
									speed 		= (adrenaline)? walkSpeed * 1.2f: walkSpeed;
									animSpeed 	= 10f;
									if(ground)
									setAnim(AnimState.WALK.ordinal());
									lastRunning = true;
									curStamina	= 0;
								}
							}
							else{
								speed 		= (adrenaline)? walkSpeed * 1.2f: walkSpeed;
								animSpeed 	= 10f;
								if(ground)
								setAnim(AnimState.WALK.ordinal());
							}
							offX		+= dt * speed;
							if(gm.getMap().getCollision(tileX + 1, tileY) ||gm.getMap().getCollision(tileX + 1, tileY + (int)Math.signum((int)offY))){	
								if(offX  > padding)
									offX 	= padding;
						
							}
							moving		= true;
						}
					}
					if(gc.getInput().isKey(GameManager.KEYLEFT))
					{
						dirState	= DirState.LEFT;
						playerImage.setMirrorX(true);
						if(!isHanging){
							if(gc.getInput().isKey(GameManager.KEYRUN)){
								if(curStamina > 0){
									if(!lastRunning){
										speed 		= (adrenaline)? runSpeed * 1.2f: runSpeed;
										curStamina	-= dt;
										animSpeed 	= 13f;
										if(ground)
										setAnim(AnimState.RUN.ordinal());
									}
								}
								else 
								{	
									speed 		= (adrenaline)? walkSpeed * 1.2f: walkSpeed;
									animSpeed 	= 10f;
									if(ground)
									setAnim(AnimState.WALK.ordinal());
									lastRunning = true;
									curStamina	= 0;
								}
							}
							else{
								speed 		= (adrenaline)? walkSpeed * 1.2f: walkSpeed;
								animSpeed 	= 10f;
								if(ground)
								setAnim(AnimState.WALK.ordinal());
							}
							offX 		-= dt * speed;
							if(gm.getMap().getCollision(tileX - 1, tileY) || gm.getMap().getCollision(tileX - 1, tileY + (int)Math.signum((int)offY)))
							{
								if(offX  < -padding)
									offX = -padding;
							}
							moving 		= true;
						}
					}
				
					//End of Left and Right--------------------------------------------------
				}
				
				
				//Jump and Gravity--------------------------------------------------
				if(!avoiding){
					if(gc.getInput().isKeyDown(GameManager.KEYJUMP))
					{
						if(curStamina > maxStamina / 4){
							if(!gm.getMap().getCollision(tileX, tileY))
								isHanging = false;
							if(ground){
								fallDistance 	= jump;
								curStamina -=maxStamina / 4;
							}
						}
					}
				}
		
				if(!isHanging){
						
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
				}
				//End of Jump and Gravity--------------------------------------------------
				
				//Interact-----------------------------------------------------------------
				if(gc.getInput().isKeyDown(GameManager.KEYINTERACT))
				{
					//DropItem Start-----------------------------------------------
					if(curInter instanceof DropItem)
					{
						((DropItem)curInter).Interact(this, true);
					}
					//End of DropItem----------------------------------------------
					//Portal Start-------------------------------------------------
					if(curInter instanceof Portal)
					{
						((Portal)curInter).Interact(this, true);
					}
					//End of Portal------------------------------------------------
					//Door Start---------------------------------------------------
					if(curInter instanceof Door)
					{
						((Door)curInter).Interact(this, true);
		
					}
					//End of Door--------------------------------------------------
				}
				
				
				//Ladder Start-------------------------------------------------
				if(!avoiding){
					if(curInter instanceof Ladder){
						
						float ladderSpeed = speed;
						if(gc.getInput().isKeyDown(GameManager.KEYUP))
						{
							if(!((Ladder)curInter).Interact(this,true)){
								isHanging 	= true;
								ground 		= true;
								fallDistance= 0;
								tileX 		= ((Ladder)curInter).getTileX();
								offX 		= ((Ladder)curInter).getOffX();
							}
						}
						else if(gc.getInput().isKeyDown(GameManager.KEYDOWN))
						{
							if(!((Ladder)curInter).Interact(this,false)){
								isHanging 	= true;
								ground 		= true;
								fallDistance= 0;
								tileX 		= ((Ladder)curInter).getTileX();
								offX 		= ((Ladder)curInter).getOffX();
							}
						}
						else if(gc.getInput().isKey(GameManager.KEYUP))
						{				
							if(isHanging)
								offY 		-= dt * (ladderSpeed );
			
			
							if(((Ladder)curInter).Interact(this,true)){
								offY 		+= dt * (ladderSpeed ) ;
								isHanging 	= false;
							}
							
						}
						else if(gc.getInput().isKey(GameManager.KEYDOWN))
						{
							if(!((Ladder)curInter).Interact(this,false))
								offY 		+= dt * (ladderSpeed ) ;
			
							if(((Ladder)curInter).Interact(this,false))
								isHanging 	= false;
						}
					}
		
				}
			}
			//End of Ladder--------------------------------------------------

			//Avoiding-----------------------------------------------------------------
			if(!isHanging && ground){
				if(avoiding){
					if(anim + dt * animSpeed >= animMaxIndex[animType])
					{
						avoiding = false;
						animSpeed = 10;
					}
					else {
						int dir = (dirState == DirState.RIGHT)? 1: -1;
						float avoidSpeed = 100;
						
						if(anim >= 6){
							avoidSpeed = 100;
						}
						else if(anim >= 3){
							avoidSpeed = 200;
							
						}
						else if(anim >= 1){
							avoidSpeed = 100;
							
						}
						
						

						offX 		+= dt * avoidSpeed * dir;
						if(gm.getMap().getCollision(tileX + dir, tileY) || gm.getMap().getCollision(tileX + dir, tileY + (int)Math.signum((int)offY)))
						{
							if(dir == 1){
								if(offX  > padding)
									offX = padding;
							}
							else {
								if(offX  < -padding)
									offX = -padding;								
							}
						}
					}
				}
				else{
					if(gc.getInput().isKeyDown(GameManager.KEYDOWN))
					{
						//if(gc.getInput().isKey(GameManager.KEYLEFT) || gc.getInput().isKey(GameManager.KEYRIGHT)){
							avoiding = true;
							animSpeed = 18;
							
							setAnim(AnimState.ROLL.ordinal());
						//}
						
					}
				}
			}
			//End of Avoiding----------------------------------------------------------
	
			//ItemUse--------------------------------------------------------	
			if(!isHanging && !readyToShoot && !readyToattack && ground){
				if(UserInterface.getSingleton().getCurrentItem() != null && !reloading){
	
					if(gc.getInput().isKeyDown(GameManager.KEYUSE))
					{
						itemUsing 	= true;
					}				
					else if(gc.getInput().isKeyUp(GameManager.KEYUSE))
					{
						itemUsing 	= false;
						itemUseTime = 0;
					}
					if(itemUsing)
					{
						Item i = UserInterface.getSingleton().getCurrentItem();
						itemUseTime += dt;
						itemUsePercent = itemUseTime / i.getUsingTime();
						
						if(itemUsePercent >= 1)
						{
							itemUsing 	= false;
							itemUseTime = 0;
							UserInterface.getSingleton().ItemUse(this);
						}
					}
					
				}
				if(currentGun != null && !itemUsing && currentGun.isCanReload())
				{			
					if(UserInterface.getSingleton().getInv().getBulletsCount(currentGun.getID())> 0){
						if(gc.getInput().isKeyDown(GameManager.KEYRELOAD))
						{
							reloading 	= true;
							
						}				
						else if(gc.getInput().isKeyUp(GameManager.KEYRELOAD))
						{
							reloading 	= false;
							itemUseTime = 0;
						}
						if(reloading)
						{
							Gun g = currentGun;
							itemUseTime += dt;
							itemUsePercent = itemUseTime / g.getUsingTime();
							
							if(itemUsePercent >= 1)
							{
								if(currentGun instanceof Pistol)
								{
									GameManager.getSingleton().getSoundClips().get("Pistol_Reload").play();
								}else if(currentGun instanceof AssaultRifle)
								{
									GameManager.getSingleton().getSoundClips().get("AR_Reload").play();
								}else if(currentGun instanceof Shotgun)
								{
									GameManager.getSingleton().getSoundClips().get("Shotgun_Reload").play();
								}
								reloading 	= false;
								itemUseTime = 0;
								currentGun.Use(this);
							}
						}
					}
				}
				if(currentGun == null && UserInterface.getSingleton().getCurrentItem() == null){
					if(itemUsing){
						itemUsing = false;
						itemUseTime = 0;
					}
				}
			}
			//End of ItemUse-------------------------------------------------
			
			
			//Attacking-------------------------------------------------------
			if(!isHanging && ground && !avoiding){
				if(currentGun != null){
		
					if(currentGun.getShootType() == Gun.ShootType.Auto)
					{
						lastShoot = false;
					}
					else if(gc.getInput().isKeyUp(GameManager.KEYATTACK))
					{
						lastShoot = false;
					}
					if(gc.getInput().isKey(GameManager.KEYATTACK))
					{
						if(!readyToShoot && ground && !lastShoot){
							if(currentGun.Trigger()){
								readyToShoot	= true;
								lastShoot		= true;
								if(currentGun.getName().equalsIgnoreCase("Pistol")){
		
									animSpeed = 15f;
									setAnim(AnimState.PISTOL.ordinal());
								}
								else if(currentGun.getName().equalsIgnoreCase("AR")){
									animSpeed = 40f;
									setAnim(AnimState.ASLF.ordinal());
								}else if(currentGun.getName().equalsIgnoreCase("Shotgun")){
									animSpeed = 15f;
									setAnim(AnimState.SHOTGUN.ordinal());
								}
								else if(currentGun.getName().equalsIgnoreCase("Bazooka")){
									animSpeed = 15f;
									setAnim(AnimState.BAZOOKA.ordinal());
								}
							}
						}
					}
					if(readyToShoot)
					{
						if(anim + dt * animSpeed >= animMaxIndex[animType])
						{
							readyToShoot = false;
							canShoot = false;
							animSpeed = 10f;
						}
						else if(anim >= 1)
						{
			
							if(!canShoot){
						
								if(currentGun instanceof Shotgun)
								{

									GameManager.getSingleton().getSoundClips().get("ShotgunShoot").play();
								}
								else
									GameManager.getSingleton().getSoundClips().get("Shoot").play();
								int shotOffX	= (dirState == DirState.RIGHT)? muzzleX: GameManager.TS - muzzleX;
								currentGun.Shoot(tileX,tileY,offX+ shotOffX, offY + muzzleY,(dirState == DirState.RIGHT)?false: true);
								//gm.addObject(new Bullet(tileX, tileY, offX + shotOffX, offY + muzzleY,
								//						(dirState == DirState.RIGHT)?false: true,
								//						1));
			
							}canShoot = true;
						}
					}
				}
				else
				{
					if(gc.getInput().isKey(GameManager.KEYATTACK))
					{
						if(!readyToattack){
							if(curStamina > maxStamina / 5){
								curStamina -= maxStamina / 5;
								readyToattack = true;
								animSpeed = 20f;
								
							//	padding = 2;
								
								setAnim(AnimState.ATTACK.ordinal());
							}
						}
					}
					if(readyToattack)
					{
						if(anim + dt * animSpeed >= animMaxIndex[animType])
						{
							readyToattack = false;
							alreadyAttack = false;
							attacking = false;
							animSpeed = 10;

							attackBox.setActive(false);
						//	padding = 10;
						}
						else if(anim >= 4 && !alreadyAttack)
						{
							
							attacking = true;alreadyAttack = true;
							
							int attackBoxWidth = 24;
							int x = (dirState == DirState.RIGHT)? (int)posX + padding  + 3:(int)posX - (attackBoxWidth - (width - padding)) - 3;
							int y = (int)posY + 10;
							attackBox.Active(x, y,attackBoxWidth,8);
							//GameManager.getSingleton().getSoundClips().get("Punch").play();
						}
						else if (alreadyAttack)
							attacking = false;
					}
				}
			}
			//End of Shooting--------------------------------------------------------
			
			//Other Keys-------------------------------------------------------------
			if(!readyToShoot && !readyToattack ){
				if(gc.getInput().isKeyDown(GameManager.KEYITEMSWITCH))
				{
					UserInterface.getSingleton().SwitchItem();
				}
				else if(gc.getInput().isKeyDown(GameManager.KEYWEAPONSWITCH))
				{
					UserInterface.getSingleton().SwitchGun();
				}
				else if(gc.getInput().isKeyDown(GameManager.KEYDROP))
				{
					UserInterface.getSingleton().getInv().DropItem();
				}	
			}
			//-----------------------------------------------------------------------
			
			//Final position---------------------------------------------------------
			AdjustPosition();
			//End of Final position--------------------------------------------------
	
			
			//Idle
			if(!moving && !readyToShoot && ground && !readyToattack && !avoiding){
				setAnim(AnimState.IDLE.ordinal());
				RecoveryStamina(dt);
			}
			if(lastRunning||animType != AnimState.RUN.ordinal() )
			{
				RecoveryStamina(dt);
			}
		}
		else {
			this.readyToattack = false;
			this.attacking		= false;
			setAnim(AnimState.DEAD.ordinal());
			animSpeed = 15;
			if(anim + dt * animSpeed >= animMaxIndex[animType] - 1)
			{
				GameManager.getSingleton().setFailded(true);
				//this.destroy = true;
			}
		}

		anim 	+= dt * animSpeed;
		if(anim >= animMaxIndex[animType])
			anim = 0;
		
		//Jump Animation---------------------------------------------------------
		if(!dieing){
			if((int)fallDistance != 0){
				setAnim(AnimState.INJUMP.ordinal());
				ground 	= false;
			}
			
			if(ground && !groundLast)
			{  
				setAnim(AnimState.INJUMP.ordinal());
				anim = 1;
			}
		}
		//-----------------------------------------------------------------------
		
		
		groundLast 	= ground;
		moving 		= false;
		if(gc.getInput().isKeyUp(GameManager.KEYRUN))
			lastRunning = false;
		curInter 	= null;
		
		if(attackBox.isActive())
			attackBox.update(gc, dt);
		this.updateComponents(gc, dt);
	}	
	
	@Override
	public void render(GameContainer gc, Renderer r) {
		r.setzDepth(Renderer.OBJECT_LAYER);
		//----------------------------------------------------------------------------------
		if(curInter != null){
			if(!(curInter instanceof Ladder))
			{
				r.drawText("\'E\'", (int)posX + 8, (int)posY-8, 0xffffffff,Font.STANDARD);
		
			}
		}
		//----------------------------------------------------------------------------------
		if(itemUsing || reloading)
		{
			r.drawFillRect((int)posX, (int)posY-4, (int)(GameManager.TS * itemUsePercent), 3, 0xffffffff);
		}
		//----------------------------------------------------------------------------------
		if(adrenaline)
		{
			r.drawUIFillRect(gc.getWidth() -128, gc.getHeight() - 32, 16,16, 0xffffffff);
		}
		//----------------------------------------------------------------------------------
		else if(canShoot){
			int shotOffX	= (dirState == DirState.RIGHT)? muzzleX: GameManager.TS - muzzleX;
			r.drawLight(shootLight, (int)posX + shotOffX, (int)posY + muzzleY);
		}
		//----------------------------------------------------------------------------------
		
		r.drawImageTile(playerImage, (int)posX - GameManager.TS/2,(int)posY - GameManager.TS, (int)anim, animType);	
		r.drawRect((int)posX - GameManager.TS/2,(int)posY - GameManager.TS, GameManager.TS, GameManager.TS, 0x00FF00);
		r.drawLight(pLight, (int)posX + width / 2, (int)posY + height /2);
		//----------------------------------------------------------------------------------
		

		if(attackBox.isActive())
			attackBox.render(gc, r);
		this.renderComponents(gc, r);
	}

	@Override
	public void collision(GameObject other) {
		// TODO Auto-generated method stub
		
		if(other instanceof Interactable)
		{
			curInter = (Interactable)other;
		}
		super.collision(other);
		
		if(collisionDetected)
			return;
		
		if(other instanceof AttackBox)
		{
			if(!avoiding){
				if(((AttackBox)other).getParent() != this)
				{
					if(((AttackBox)other).getParent() instanceof Enemy)
					{
						Enemy 	parent	= (Enemy)((AttackBox)other).getParent();
						
						if(parent.isAttacking())
						{
							TakeDamage(parent.getDamage());
						}
					}
				}
			}
		}
	}
	
	private void RecoveryStamina(float rs)
	{
		curStamina += rs;
		if(curStamina > maxStamina)
			curStamina = maxStamina;
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
			lastRunning = false;
			anim		= 0;
		}
		animType 	= index;
	}
	
	public void Recovery()
	{
		this.health = 10;
		this.active = true;
	}

	
	public DirState getDirState() {
		return dirState;
	}


	public void setPos(float offX, float offY) {
		this.offX -= offX;
		this.offY -= offY;
		AdjustPosition();
	}
	
	public void setTile(int tileX, int tileY)
	{
		this.tileX = tileX;
		this.tileY = tileY;
		AdjustPosition();
	}


	public float getCurStamina() {
		return curStamina;
	}


	public float getMaxStamina() {
		return maxStamina;
	}


	public Gun getCurrentGun() {
		return currentGun;
	}


	public boolean isAdrenaline() {
		return adrenaline;
	}


	public void setAdrenaline(boolean adrenaline) {
		this.adrenaline = adrenaline;
	}


	public void setCurrentGun(Gun currentGun) {
		this.currentGun = currentGun;
	}


}

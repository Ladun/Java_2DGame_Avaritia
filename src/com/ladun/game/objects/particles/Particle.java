package com.ladun.game.objects.particles;

import java.util.Random;

import com.ladun.engine.GameContainer;
import com.ladun.engine.Renderer;
import com.ladun.engine.gfx.ImageTile;
import com.ladun.game.GameManager;
import com.ladun.game.components.AABBComponent;
import com.ladun.game.objects.GameObject;
import com.ladun.game.objects.environments.Platform;
import com.ladun.game.objects.environments.Slope;

public class Particle extends GameObject{

	private ParticleSystem.MoveType type;
	
	private boolean alive = false;
	
	private float 	posX,posY;
	private float 	offX,offY;
	private int 	tileX,tileY;
	
	
	private float 	life;
	private float 	time;
	private float 	speed;
	private float 	gravity;
	
	
	
	private int 	maxTileX, maxTileY;
	private int 	imageTileW,imageTileH;
	private float 	anim 		= 0;
	private float 	animSpeed 	= 0;
	private int 	animType 	= 0;
		
	private boolean physics 	= false;
	private boolean collided	= false;
	
	
	private Random 	rand 		= new Random();
	
	
	private double 	xa,ya; 
	
	public Particle(float posX,float posY,
			float life,
			int row,int column,
			float speed,float gravity,float animSpeed,
			int imageTileW,int imageTileH, boolean physics,
			ParticleSystem.MoveType type)
	{
		
		this.tag 		= "particle";
		this.posX 		= posX;
		this.posY 		= posY;
		this.tileX 		= Math.round(posX / GameManager.TS);
		this.tileY 		= Math.round(posY / GameManager.TS);
		this.offX 		= ((posX / GameManager.TS)- tileX);
		this.offY 		= ((posY / GameManager.TS)- tileY);
		this.physics 	= physics;
		
		this.life 		= life;
		this.imageTileW = imageTileW;
		this.imageTileH = imageTileH;
		this.maxTileX 	= column;
		this.maxTileY 	= row;
		this.speed 		= speed;
		this.gravity 	= gravity;
		this.animSpeed 	= animSpeed;
		this.animType 	= rand.nextInt(maxTileY);
		this.type		= type;
		this.width 		= imageTileW;
		this.height 	= imageTileH / 2;
		this.paddingTop	= imageTileH / 2 - 4;
		this.padding	= imageTileW / 2 - 2;

		ResetAcceleration(offX,offY);
		
		if(physics)
			this.addComponent(new AABBComponent(this));
		
	}

	@Override
	public void update(GameContainer gc, float dt) {
		// TODO Auto-generated method stub
		offX += xa;
		offY += ya;
		
		if(physics){
			if(checkCol() || collided)
			{
				offX -= xa;
				offY -= ya;
			}	
		}
		ya += gravity;
		

		AdjustPosition();
		
				
		if(anim+ dt * animSpeed < maxTileX)
			anim += dt * animSpeed;
		
		time += dt;
		if(time > life){
			alive = false;
		}
		
		collided = false;
		
		if(physics)
			this.updateComponents(gc, dt);
	}
	
	public void render(GameContainer gc, Renderer r,ImageTile image)
	{
		r.drawImageTile(image, 
				(int)posX, (int)posY ,
				(int)anim,
				animType
				);
		
		this.render(gc, r);
	}

	@Override
	public void render(GameContainer gc, Renderer r) {
		// TODO Auto-generated method stub
		
		if(physics)
			this.renderComponents(gc, r);
	}

	@Override
	public void collision(GameObject other) {
		// TODO Auto-generated method stub
		if(other instanceof Platform)
		{	
			collided = true;
		}
		else if(other instanceof Slope)
		{
			AABBComponent myC 		= (AABBComponent) this.findComponent("aabb");
			
			float distance = ((Slope)other).checkCol(
					myC.getCenterX(),
					myC.getHalfWidth(),
					myC.getCenterY() + myC.getHalfHeight());
			
			
			if(distance != -1)
			{
				collided 	= true;
				offX 		-= xa;
				offY 		-= distance;
				posY 		-= distance;
				myC.setCenterY(myC.getCenterY() - (int)distance);
			}
		}
	}
	
	private void ResetAcceleration(float offX,float offY)
	{
		if(type == ParticleSystem.MoveType.Circle){
			this.xa 	= (rand.nextGaussian()) * speed;
			this.ya 	= (rand.nextGaussian()) * speed;
		}
		else if(type == ParticleSystem.MoveType.Vertical){
			this.xa 	= 0;
			this.ya 	= (rand.nextFloat() + .5f) * speed;
			this.offX   = (float) (offX + rand.nextGaussian() * speed*5);		
		}
		else if(type == ParticleSystem.MoveType.Horizontal){
			this.xa 	= (rand.nextFloat() + .5f) * speed;
			this.ya 	= 0;	
			this.offY   = (float) (offY + rand.nextGaussian() * speed*5);		
		}
	}
	
	public void Reset(float posX,float posY)
	{
		this.offX 	= (int)posX % GameManager.TS;
		this.offY 	= (int)posY % GameManager.TS;
		this.tileX 	= (int)(posX / GameManager.TS);
		this.tileY 	= (int)(posY / GameManager.TS);
		time 		= 0;
		anim 		= 0;
		

		ResetAcceleration(offX,offY);
		alive 		= true;
	}

	public boolean checkCol(){
		
		int tx, ty;
		
		tx = tileX;
		ty = tileY;
		
			if(offX + imageTileW / 2 < 0){
				tx--;
			}
			if(offY+ imageTileH / 2< 0){
				ty--;
			}
		
		if(GameManager.getSingleton().getMap().getCollision(tx,ty))
		{
			return true;
		}
		return false;
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
	
	//Getter Setter------------------------------------------------------------------------------

	public float getTime() {
		return time;
	}

	public void setTime(float time) {
		this.time = time;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	public boolean isAlive() {
		return alive;
	}

	public float getAnim() {
		return anim;
	}

	public int getAnimType() {
		return animType;
	}

	public float getPosX() {
		return posX;
	}

	public float getPosY() {
		return posY;
	}	
}

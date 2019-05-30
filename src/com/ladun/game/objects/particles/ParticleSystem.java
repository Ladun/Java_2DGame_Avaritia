package com.ladun.game.objects.particles;

import java.util.ArrayList;

import com.ladun.engine.GameContainer;
import com.ladun.engine.Renderer;
import com.ladun.engine.gfx.ImageTile;
import com.ladun.game.objects.GameObject;

public class ParticleSystem extends GameObject{
	
	public 	enum MoveType { Horizontal,Vertical, Circle}
	
	public static int particleCount = 0;
	
	private ArrayList<Particle> particles = new ArrayList<Particle>();
	
	
	private GameObject parent;
	private int amount;
	private int repeat;// -1 = Infinity , total Play = repeat + 1
	private float betweenRepeatTime;
	
	private ImageTile image;
	
	
	private int st;
	private float time;
	
	
	public ParticleSystem(GameObject parent,
			float posX,float posY,
			float life,int amount,
			float speed,float gravity,float animSpeed,
			int repeat,float between,
			boolean physics,
			ImageTile image,
			MoveType type)
	{

		this.tag 		= "particleSystem";
		this.parent 	= parent;
		this.posX 		= posX;
		this.posY 		= posY;
		this.image 		= image;
		this.amount		= amount;
		this.repeat 	= repeat;
		if(between == 0 )
			between = 1;
		this.betweenRepeatTime = between;
		
		//Make the Particles
		int repeatMaxSizeMultiple = 1;
		if(repeat > 0){
			repeatMaxSizeMultiple = (betweenRepeatTime / life > 1)? 1:(int)(life / betweenRepeatTime) + 1;

			if(repeatMaxSizeMultiple > repeat)
				repeatMaxSizeMultiple = repeat;
		}
		else if (repeat == -1){
			repeatMaxSizeMultiple = (int)(life / betweenRepeatTime) + 1;
		}
		//System.out.println(amount * repeatMaxSizeMultiple);
		for(int i = 0; i < amount * repeatMaxSizeMultiple  ; i ++){
			particles.add(new Particle(posX,posY,
					life,
					(image.getH() / image.getTileH()),
					(image.getW() / image.getTileW()),
					speed,gravity,
					animSpeed,
					image.getTileW(),image.getTileH(),physics,
					type));
			
			particleCount++;
		}
		
		
		st = 0 ;
		//Activate Particles
		for(int i = 0; i < amount;i++)
		{
			particles.get(i + st).Reset(posX,posY);
		}
	}

	@Override
	public void update(GameContainer gc, float dt) {
		

		//Activate Particles
		time += dt;
		if(time >= betweenRepeatTime && (repeat > 0 || repeat == -1))
		{
			if(repeat > 0)
				repeat--;
			time = 0;
			
			st = (st + amount) % (particles.size());
			
			
			for(int i = 0; i < amount;i++)
			{
				Particle p  = particles.get(i + st);
			
				int pX = (parent == null)? (int)posX :(int)posX + (int)parent.getPosX();
				int pY = (parent == null)? (int)posY :(int)posY + (int)parent.getPosY();
				
				p.Reset(pX,pY);

			}
		}
		//end Activate Particles
		
		//Update Particles
		for(int i = 0; i < particles.size(); i ++)
		{
 	 		particles.get(i).update(gc,dt);
			if(!particles.get(i).isAlive()){
				if(repeat == 0)
				{
					particles.remove(i);
					particleCount--;
				}
			}
		}
		if(particles.size() == 0)
		{
			this.destroy = true;
		}
		//end Update Particles
	}

	@Override
	public void render(GameContainer gc, Renderer r) {

		for(int i = 0; i < particles.size(); i++)
		{
			Particle p  = particles.get(i);
			
			if(p.isAlive()){
				p.render(gc, r, image);
			}
		}
	}
	@Override
	public void collision(GameObject other) {
		// TODO Auto-generated method stub
		
	}

}

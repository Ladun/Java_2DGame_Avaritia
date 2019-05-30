package com.ladun.game.objects;

import com.ladun.engine.GameContainer;
import com.ladun.engine.Renderer;
import com.ladun.game.GameManager;
import com.ladun.game.Scene.InGameScene;
import com.ladun.game.objects.creatures.NoHeadZombie;
import com.ladun.game.objects.creatures.Zombie;

public class Spawner extends GameObject{

	
	private float 	nextSpawnTime 	= 0;
	private float 	spawnBetween		= 0;
	
	private String type;

	private int 	count;
	private int 	tileX;
	private int 	tileY;
	public Spawner(int tileX,int tileY,float spawnTime,int count)
	{
		this.count 		= count;
		this.tag 		= "Spawner";
		this.tileX 		= tileX;
		this.tileY		= tileY;
		this.posX		= tileX * GameManager.TS;
		this.posY 		= tileY * GameManager.TS;
		spawnBetween 	= spawnTime;
		
	}
	public Spawner(int tileX,int tileY,float spawnTime,int count,String type)
	{
		this(tileX, tileY, spawnTime,count);
		this.tag 		= "Spawner";
		
	}
	@Override
	public void update(GameContainer gc, float dt) {
		
		if(gm.getCurrentScene() instanceof InGameScene){
				if(((InGameScene)gm.getCurrentScene()).getMap().getEnemys().size() < 150){
				nextSpawnTime += dt;
				if( nextSpawnTime > spawnBetween){
				
					if(type == null){
						float r = (float)Math.random();
						if(r < .8f)
							((InGameScene)gm.getCurrentScene()).getMap().addObject((new Zombie(tileX,tileY)));
						else
							((InGameScene)gm.getCurrentScene()).getMap().addObject((new NoHeadZombie(tileX,tileY)));
					}
					else
					{
						if(type.equalsIgnoreCase("Zombie"))
							((InGameScene)gm.getCurrentScene()).getMap().addObject((new Zombie(tileX,tileY)));
						else if(type.equalsIgnoreCase("NoHeadZombie"))
							((InGameScene)gm.getCurrentScene()).getMap().addObject((new NoHeadZombie(tileX,tileY)));
					}
					nextSpawnTime = 0;
					count --;
				}
			}
		}
		
		if(count <= 0)
			this.destroy = true;
	}

	@Override
	public void render(GameContainer gc, Renderer r) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void collision(GameObject other) {
		// TODO Auto-generated method stub
		
	}

	
}

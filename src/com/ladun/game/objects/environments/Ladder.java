package com.ladun.game.objects.environments;

import com.ladun.engine.GameContainer;
import com.ladun.engine.Renderer;
import com.ladun.game.GameManager;
import com.ladun.game.components.AABBComponent;
import com.ladun.game.objects.GameObject;
import com.ladun.game.objects.Interactable;
import com.ladun.game.objects.creatures.AliveObject;

public class Ladder extends GameObject implements Interactable{

	private int 		tileX;
	private int 		tileY;
	private float 		offX;
	
	private int			length	= 0;
	
	public Ladder(int tileX,int tileY,int offX,int length)
	{
		this.tag 		= "ladder";
		this.tileX 		= tileX;
		this.tileY 		= tileY;
		this.offX 		= offX;
		this.posX 		= tileX * GameManager.TS + offX;
		this.posY 		= tileY * GameManager.TS - 1;
		this.length 	= length;
	
		this.padding	= GameManager.TS / 3 + 4;
		this.width 		= GameManager.TS;
		this.height 	= GameManager.TS * length;
		
		this.addComponent(new AABBComponent(this));
	}
	
	@Override
	public void update(GameContainer gc,  float dt) {
		// TODO Auto-generated method stub
		
		this.updateComponents(gc, dt);
	}

	@Override
	public void render(GameContainer gc, Renderer r) {
		
		this.renderComponents(gc, r);
	}

	@Override
	public void collision(GameObject other) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean Interact(GameObject go,boolean dir) {
		//dir true = up, false = down
		if(go instanceof AliveObject){
			AliveObject h = (AliveObject)go;
			int ty = h.getTileY();
			float oy = h.getOffY();
			if(oy < 0){
				ty--;
				oy += GameManager.TS;
			}
			
			if(dir)
			{
				if(oy + h.getHeight() >= GameManager.TS)
					ty++;
				if(ty < this.tileY)
					return true;
			}
			else
			{
				if(ty > tileY - 1 + length - 1)
						return true;
					
			}
		
		}
		return false;
	}


	
	//Getter And Setter
	public int getTileX() {
		return tileX;
	}

	public float getOffX() {
		return offX;
	}

	public int getLength() {
		return length;
	}

	

}

package com.ladun.game.Scene;

import java.util.ArrayList;

import com.ladun.engine.GameContainer;
import com.ladun.engine.Renderer;
import com.ladun.game.objects.GameObject;

public abstract class AbstractScene {
	

	protected ArrayList<GameObject> 	objects 		= new ArrayList<GameObject>();
	protected boolean					initialized 	= false;
	
	public abstract void init();
	public abstract int getAmbientColor();
	
	public ArrayList<GameObject> getObjects() {
		return objects;
	}
	
	public void update(GameContainer gc, float dt)
	{

		for (int i = 0; i < objects.size(); i++) {
			if(objects.get(i).isActive())
				objects.get(i).update(gc, dt);
			if (objects.get(i).isDestroy()) {
				objects.remove(i);
				i--;
			}
		}
	}
	public void render(GameContainer gc, Renderer r) {

		for (GameObject obj : objects)
			if(obj.isActive())
				obj.render(gc, r);
		
	}

	public boolean isInitialized() {
		return initialized;
	}

}

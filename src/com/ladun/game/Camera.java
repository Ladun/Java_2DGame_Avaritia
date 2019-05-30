package com.ladun.game;

import com.ladun.engine.GameContainer;
import com.ladun.engine.Renderer;
import com.ladun.game.objects.GameObject;

public class Camera {
	
	private float 		offX,offY;
	private float 		lastTargetX,lastTargetY;
	
	private String 		targetTag;
	private GameObject 	target 		= null;
	
	private float cameraSensitivity = 3f;
	
	public Camera(String tag)
	{
		this.targetTag = tag;
	}
	
	public void update(GameContainer gc,float dt)
	{
		target = GameManager.getSingleton().getObject(targetTag);
		/*
		if(target == null)
		{
			target = gm.getObject(targetTag);
		}
		*/
		
		if(target == null)
			return;
		
		float targetX 	= (target.getPosX() + target.getWidth() / 2) - gc.getWidth() / 2;
		float targetY 	= (target.getPosY() + target.getHeight() / 2) - gc.getHeight() /2;
		
		//offX 			= targetX;
		//offY 			= targetY;
		
		if(Math.abs(targetX - offX) < 23 || targetX == lastTargetX)
			offX += dt * (int)(targetX - offX) * cameraSensitivity;
		else offX = targetX - 23 * Math.signum(targetX - offX);
		
		if(Math.abs(targetY - offY) < 23  || targetY == lastTargetY)
			offY += dt * (int)(targetY - offY) * cameraSensitivity;
		else offY = targetY - 23 * Math.signum(targetY - offY);
		

		if(offX < 0)	offX 	= 0;
		if(offX > GameManager.getSingleton().getMap().getLevelW() * GameManager.TS - gc.getWidth())offX = GameManager.getSingleton().getMap().getLevelW()* GameManager.TS  - gc.getWidth();
		if(offY < 0)	offY 	= 0;
		if(offY > GameManager.getSingleton().getMap().getLevelH() * GameManager.TS - gc.getHeight())offY = GameManager.getSingleton().getMap().getLevelH()* GameManager.TS  - gc.getHeight();

		
		
		lastTargetX = targetX;
		lastTargetY = targetY;
		
	}
	
	public void render(Renderer r)
	{
		r.setCamX((int)offX);
		r.setCamY((int)offY);
	}

	public float getOffX() {
		return offX;
	}

	public void setOffX(float offX) {
		this.offX = offX;
	}

	public float getOffY() {
		return offY;
	}

	public void setOffY(float offY) {
		this.offY = offY;
	}

	public String getTargetTag() {
		return targetTag;
	}

	public void setTargetTag(String targetTag) {
		this.targetTag = targetTag;
	}

	public GameObject getTarget() {
		return target;
	}
	
}

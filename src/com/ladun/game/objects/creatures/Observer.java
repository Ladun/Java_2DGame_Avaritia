package com.ladun.game.objects.creatures;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import com.ladun.engine.GameContainer;
import com.ladun.engine.Renderer;
import com.ladun.engine.gfx.ImageTile;
import com.ladun.game.GameManager;
import com.ladun.game.Scene.InGameScene;
import com.ladun.game.objects.GameObject;
import com.ladun.game.objects.particles.ParticleSystem;

public class Observer extends GameObject{

	
	public float 	speed = 100;
	
	
	public Observer(int posX, int posY)
	{
		this.tag	= "observer";
		this.posX 	= posX;
		this.posY 	= posY;
		this.active = false;
	}
	
	private void ParticleTest(GameContainer gc)
	{
		if(gc.getInput().isButtonDown(MouseEvent.BUTTON1))
		{
			int mX = (int)((InGameScene)GameManager.getSingleton().getCurrentScene()).getCamera().getOffX() + gc.getInput().getMouseX();
			int mY = (int)((InGameScene)GameManager.getSingleton().getCurrentScene()).getCamera().getOffY() + gc.getInput().getMouseY();

			if(gc.getInput().isKey(KeyEvent.VK_1))
			{
				gm.addObject(new ParticleSystem(null,
						mX,mY,
						1f,5,
						1f,.2f,15,
						0,.2f,
						true,
						new ImageTile("/Resources/Object/Particle/Blood_Particle.png",16,16),
						ParticleSystem.MoveType.Circle));
			}
			else if(gc.getInput().isKey(KeyEvent.VK_2)){
				gm.addObject(new ParticleSystem(null,
						mX,mY,
						.3f,40,
						1f,0,35,
						1,.1f,
						false,
						new ImageTile("/Resources/Object/Particle/Fire_Particle.png",12,12),
						ParticleSystem.MoveType.Circle));
			}
			else if(gc.getInput().isKey(KeyEvent.VK_3))
			{
				gm.addObject(new ParticleSystem(null,
						mX, mY, 
						1.5f, 5, 
						-.3f, 0,6,
						-1, .1f,
						false,
						new ImageTile("/Resources/Object/Particle/Fire_Particle.png",12,12),
						ParticleSystem.MoveType.Vertical));
			}
			else if(gc.getInput().isKey(KeyEvent.VK_4))
			{
				gm.addObject(new ParticleSystem(null,
						mX,mY,
						1f,15,
						1f,.2f,15,
						-1,.3f,
						true,
						new ImageTile("/Resources/Object/Particle/Blood_Particle.png",16,16),
						ParticleSystem.MoveType.Circle));
			}
		}
	}
	
	@Override
	public void update(GameContainer gc, float dt) {
		// TODO Auto-generated method stub
		
		if(gc.getInput().isKey(GameManager.KEYRIGHT))
			if ( posX + speed * dt < GameManager.getSingleton().getMap().getLevelW() * GameManager.TS)
				posX += speed * dt;
		if(gc.getInput().isKey(GameManager.KEYLEFT))
			if (posX - speed * dt >= 0 )
				posX -= speed * dt;
		if(gc.getInput().isKey(GameManager.KEYUP))
			if (posY - speed * dt >= 0 )
				posY -= speed * dt;
		if(gc.getInput().isKey(GameManager.KEYDOWN))
			if ( posY + speed * dt < GameManager.getSingleton().getMap().getLevelH() * GameManager.TS)
				posY += speed * dt;
		
		if(gc.getInput().isKey(GameManager.KEYRUN))
			speed = 500;
		else
			speed = 100;
		
		ParticleTest(gc);
			
	}
	

	@Override
	public void render(GameContainer gc, Renderer r) {
		// TODO Auto-generated method stub
		r.drawFillRect((int)posX, (int)posY, 5, 5, 0xffff00ff);
	}

	@Override
	public void collision(GameObject other) {
		// TODO Auto-generated method stub
		
	}

}

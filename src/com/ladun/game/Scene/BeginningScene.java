package com.ladun.game.Scene;

import java.awt.event.MouseEvent;
import java.util.Random;

import com.ladun.engine.GameContainer;
import com.ladun.engine.Renderer;
import com.ladun.engine.gfx.Image;
import com.ladun.game.GameManager;
import com.ladun.game.Thread.MapLoader;

public class BeginningScene extends AbstractScene{

	private int 	ambientColor = -1;
	
	private Image 	logo;
	
	private float 		time = 0;
	
	private boolean 	fading;
	private float 		fadingPercent	= 0;
	private float		fadingTime		= .7f;

	public BeginningScene()
	{
		logo = new Image("/Resources/Logo.png");
	}
	
	@Override
	public void init()
	{
		this.fading 		= true;
		this.fadingPercent	= 1; 
		
		//MapLoad();
	}
	
	@Override
	public void update(GameContainer gc, float dt)
	{

		//Scene Fading---------------------------------------------------------------------
		if(fading)
		{
			fadingPercent += dt * ( 1 /fadingTime);
		}
		else {
			time += dt;
			if(gc.getInput().isButtonUp(MouseEvent.BUTTON1))if(!GameManager.getSingleton().isFading()){
				GameManager.getSingleton().SceneChange(GameManager.getSingleton().getMainMenu(),.3f);
			}
		}
		//End of Scene Fading----------------------------------------------------------------
		
		if(time >= 1 )
		{
			if(!GameManager.getSingleton().isFading()){
				GameManager.getSingleton().SceneChange(GameManager.getSingleton().getMainMenu(),.6f);
			}
		}
		
		
	}
	
	@Override
	public void render(GameContainer gc,Renderer r)
	{
		r.setzDepth(Renderer.BACKGROUND_LAYER);
		r.drawFillRect(0, 0, gc.getWidth(), gc.getHeight(), 0xff000000);
		r.drawImage(logo, (gc.getWidth() - logo.getW())/ 2, (gc.getHeight() - logo.getH())/ 2);

		r.setzDepth(Renderer.FORGROUND_LAYER);
		//Scene Fading---------------------------------------------------------------------
		if(fading)
		{
			float alpha = 0;
			if(fadingPercent < 1){
				alpha = 255 * fadingPercent;
			}
			else if (fadingPercent < 1 + fadingTime)
			{
				alpha = 255;
			}
			else if( fadingPercent < 2 + fadingTime)
			{
				alpha = 255 * (2 + fadingTime- fadingPercent);
			}
			else if( fadingPercent >= 2 + fadingTime)
			{
				fading 		= false;
			}
					
			int color = ((int)alpha << 24 | 0x00000000);
			
			r.drawUIFillRect(0, 0, gc.getWidth(), gc.getHeight(), color);
		}
	}
	
	@Override
	public int getAmbientColor()
	{
		return this.ambientColor ;
	}
}

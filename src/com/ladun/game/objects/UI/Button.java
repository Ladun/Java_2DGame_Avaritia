package com.ladun.game.objects.UI;

import java.awt.event.MouseEvent;

import com.ladun.engine.GameContainer;
import com.ladun.engine.Renderer;
import com.ladun.engine.gfx.ImageTile;
import com.ladun.game.objects.GameObject;

public class Button extends GameObject{

	private boolean over;
	private boolean pressed;
	private boolean released;
	
	private ImageTile image;
	
	
	public Button(float x, float y
			, ImageTile buttonImage)
	{
		this.tag 		= "button";
		this.width 		= buttonImage.getTileW();
		this.height 	= buttonImage.getTileH();
		this.posX		= x;
		this.posY		= y;
		this.image 		= buttonImage;
	}
	



	@Override
	public void update(GameContainer gc, float dt) {
		over 			= isOver(gc.getInput().getMouseX(),gc.getInput().getMouseY());
		released 		= false;
		if(over)
		{
			if(gc.getInput().isButtonDown(MouseEvent.BUTTON1))
				pressed 	= true;
			else if(pressed && gc.getInput().isButtonUp(MouseEvent.BUTTON1)){
				pressed 	= false;
				released 	= true;
			}
		}
		if(pressed && gc.getInput().isButtonUp(MouseEvent.BUTTON1)){
			pressed 		= false;
		}
	}


	@Override
	public void render(GameContainer gc, Renderer r) {
		if(pressed)
		{
			r.drawUIImage(image.getTileImage(2, 0), (int)posX,(int)posY);
		}
		else
		{
			if(over)
			{
				r.drawUIImage(image.getTileImage(1, 0), (int)posX,(int)posY);
			}
			else
			{
				r.drawUIImage(image.getTileImage(0, 0), (int)posX,(int)posY);
			}
		}
		
	}


	@Override
	public void collision(GameObject other) {
		// TODO Auto-generated method stub
		
	}
	
	
	boolean isOver(float x, float y)
	{
		return x >= posX && x <= posX + width && y >= posY && y <= posY + height;
	}

	public boolean isPressed() {
		return pressed;
	}

	public boolean isReleased() {
		return released;
	}




	
	public ImageTile getImage() {
		return image;
	}
	

	
}

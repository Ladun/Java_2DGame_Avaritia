package com.ladun.game.objects.UI;

import com.ladun.engine.GameContainer;
import com.ladun.engine.Renderer;
import com.ladun.engine.gfx.Image;
import com.ladun.engine.gfx.ImageTile;
import com.ladun.game.objects.GameObject;

public class Slider extends GameObject{
	
	private int			preMousePosX = 0;
	
	
	private Button 		slider;
	private Image 		backgroundImage;
	
	public Slider( float x, float y
			, Image backgroundImage,ImageTile sliderImage,float percent)
	{
		this.tag 				= "slider";
		this.width 				= backgroundImage.getW();
		this.height 			= backgroundImage.getH();
		this.posX				= x;
		this.posY				= y;
		this.backgroundImage 	= backgroundImage;
		this.slider 			= new Button( x + (width - sliderImage.getTileW()) * percent , y, sliderImage);
	}
	@Override
	public void update(GameContainer gc, float dt) {
		slider.update(gc, dt);
		
		if(slider.isPressed())
		{
			int p = (gc.getInput().getMouseX() - preMousePosX);
			if(InSlider(slider.getPosX() + p))
			{
				slider.setPosX(slider.getPosX() + p);
			}
			else {
				if(slider.getPosX() < posX)
				{
					slider.setPosX(posX);
				}
				else if(slider.getPosX() > posX + width - slider.getWidth())
				{
					slider.setPosX(posX + width - slider.getWidth());
				}
			}
		}
		preMousePosX = gc.getInput().getMouseX();
	}

	@Override
	public void render(GameContainer gc, Renderer r) {		
		r.drawUIImage(backgroundImage, (int)posX,(int)posY);
		slider.render(gc, r);
	}

	@Override
	public void collision(GameObject other) {
		
		
	}
	
	public float getPercent()
	{
		return (slider.getPosX() - posX)/(width - slider.getWidth());
	}
	
	public boolean isPressed()
	{
		return slider.isPressed();
	}

	boolean InSlider(float x)
	{
		return x >= posX & x <= posX + width - slider.getWidth(); 
	}
}

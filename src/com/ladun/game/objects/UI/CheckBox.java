package com.ladun.game.objects.UI;

import com.ladun.engine.GameContainer;
import com.ladun.engine.Renderer;
import com.ladun.engine.gfx.ImageTile;
import com.ladun.game.objects.GameObject;

public class CheckBox extends GameObject{

	private Button 		button;
	private boolean		checked;
	
	
	private boolean 	lastChecked;
	
	public CheckBox(float x, float y
			, ImageTile buttonImage)
	{
		this.tag		= "checkBox";
		this.width 		= buttonImage.getTileW();
		this.height 	= buttonImage.getTileH();
		this.posX		= x;
		this.posY 		= y;
		this.checked 	= false;
		this.button 	= new Button(x,y,buttonImage);
	}
	
	public CheckBox(float x, float y
			, ImageTile buttonImagem,boolean checked)
	{
		this(x,y,buttonImagem);
		this.checked = checked;
	}
	
	@Override
	public void update(GameContainer gc, float dt) {
		// TODO Auto-generated method stub
		button.update(gc, dt);
		
		if(lastChecked)
			lastChecked = !lastChecked;
		
		if(button.isReleased()){
			checked = !checked;
			lastChecked = true;
		}
		
	}

	@Override
	public void render(GameContainer gc, Renderer r) {
		// TODO Auto-generated method stub
		button.render(gc, r);
		if(checked)
		{
			r.drawUIFillRect((int)posX, (int)posY, width, height, 0x66000000);
		}
	}

	@Override
	public void collision(GameObject other) {
		// TODO Auto-generated method stub
		
	}


	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public boolean isLastChecked() {
		return lastChecked;
	}

	
	
}

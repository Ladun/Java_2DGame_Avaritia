package com.ladun.game.UI;

import com.ladun.engine.GameContainer;
import com.ladun.engine.Renderer;
import com.ladun.engine.audio.SoundClip;
import com.ladun.engine.gfx.Font;
import com.ladun.engine.gfx.Image;
import com.ladun.engine.gfx.ImageTile;
import com.ladun.game.GameManager;
import com.ladun.game.objects.UI.Button;
import com.ladun.game.objects.UI.CheckBox;
import com.ladun.game.objects.UI.Slider;

public class OptionMenu {
	private static class Singleton{
		static final OptionMenu single = new OptionMenu();
	}
	
	public static OptionMenu getSingleton()
	{
		return Singleton.single;
	}
	
	
	
	public final int[] widths = {1920,1};
	public final int[] heights = {1080,1};
	
	private boolean 	active;

	//-------------------------------------------------------
	private Button 		back;
	private Button 		resolutionButton;
	private Slider 		volumeSlider;
	private CheckBox	drawAABBCheckBox;
	private CheckBox	dayNightCheckBox;

	//-------------------------------------------------------
	private Image 		backgroundImage;
	
	public OptionMenu()
	{
		back = new Button(320 - GameManager.TS
				,340 - 32, new ImageTile("/Resources/Object/UI/Button/BACK.png",64,32));
		resolutionButton = new Button(320 - 4
				,340 - 150, new ImageTile("/Resources/Object/UI/Button/defaultButton8.png",8,8));
		volumeSlider = new Slider(320 - 64,180 - 62 + 9
				,new Image("/Resources/Object/UI/Slider/Background1.png")
				,new ImageTile("/Resources/Object/UI/Slider/Button1.png",8,16)
				,.93f);
		
		
		drawAABBCheckBox = new CheckBox(320 - 100,180 - 62 + 9
				,new ImageTile("/Resources/Object/UI/Button/defaultButton8.png",8,8));
		dayNightCheckBox = new CheckBox(320 - 100,180 - 62 + 19
				,new ImageTile("/Resources/Object/UI/Button/defaultButton8.png",8,8));
	
		
		
		backgroundImage = new Image("/Resources/Object/UI/SettingBackground.png");
	}
	
	public void update(GameContainer gc, float dt)
	{
		back.update(gc, dt);
		volumeSlider.update(gc, dt);
		resolutionButton.update(gc, dt);
		if(GameManager.isDebugMode){
			drawAABBCheckBox.update(gc, dt);
			dayNightCheckBox.update(gc, dt);
		}
		
		
		if(back.isReleased())
		{
			active = false;
		}
		else if(volumeSlider.isPressed())
		{
			for(String key : GameManager.getSingleton().getSoundClips().keySet())
			{
				SoundClip sc = GameManager.getSingleton().getSoundClips().get(key);
					sc.setVolume(-80 + 86 * volumeSlider.getPercent());
			}
		}
		else if(resolutionButton.isReleased())
		{
			gc.ResizeWindow(1024	, 720,1f);
		}
		
		
		
	}
	
	public void render(GameContainer gc, Renderer r){
		r.setzDepth(Renderer.BACKGROUND_LAYER);
		r.drawUIImage(backgroundImage, 0, 0);
		//r.drawUIFillRect(0, 32, gc.getWidth(), gc.getHeight() - 64, 0xa5000000);
		r.setzDepth(Renderer.OBJECT_LAYER);
		r.drawUIText("SOUND", 320 - 40,  180 - 32 - 40, 0xffcdcdcd, Font.STANDARD_16);
		SoundClip sc = GameManager.getSingleton().getSoundClips().get("Pistol_Reload");
		
		if(sc.getVolume() < 0)
		{
			float scale = 1-(Math.abs(sc.getVolume()) + 6) / 86;
			r.drawUIFillRect(320 - 64,180 - 62 + 14, (int)(128 * scale) , 6, 0xffcdcdcd);
		}
		else {
			float scale = (sc.getVolume() + 80) / 86;
			r.drawUIFillRect(320 - 64,180 - 62 + 14, (int)(128 * scale), 6, 0xffcdcdcd);
		}
		
		
		back.render(gc, r);
		volumeSlider.render(gc, r);
		resolutionButton.render(gc, r);
		
		if(GameManager.isDebugMode){
			drawAABBCheckBox.render(gc, r);
			dayNightCheckBox.render(gc, r);
		}
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	
	public CheckBox getDrawAABBCheckBox() {
		return drawAABBCheckBox;
	}

	public CheckBox getDayNightCheckBox() {
		return dayNightCheckBox;
	}
}

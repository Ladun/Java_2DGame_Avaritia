package com.ladun.game.Scene;

import java.awt.event.KeyEvent;

import com.ladun.engine.GameContainer;
import com.ladun.engine.Renderer;
import com.ladun.engine.gfx.Image;
import com.ladun.engine.gfx.ImageTile;
import com.ladun.game.GameManager;
import com.ladun.game.UI.OptionMenu;
import com.ladun.game.objects.GameObject;
import com.ladun.game.objects.UI.Button;

public class MainMenuScene extends AbstractScene{
	
	private Button[] 	buttons;
	private ImageTile	indicator;
	private int			indicatorIndex 	= 0;
	private int 		ambientColor	= -1;
	private Image		mainMenuImage 	= new Image("/Resources/MainMenu.png");
	

	public MainMenuScene()
	{
		
	}
	
	@Override
	public void init()
	{
		initialized			= true;
		indicator = new ImageTile("/Resources/Object/UI/Indicator.png",32,32);
		buttons				= new Button[4];
		//Start
		buttons[0] 			= new Button(320 - 32,340 - 80,new ImageTile("/Resources/Object/UI/Button/START.png",64,32));
		//Load
		if(GameManager.getSingleton().getUd() != null){
			buttons[1] 		= new Button(320 - 32,340 - 112,new ImageTile("/Resources/Object/UI/Button/LOAD.png",64,32));
		}
		//Option
		buttons[2] 			= new Button(320 - 32,340 - 48,new ImageTile("/Resources/Object/UI/Button/OPTION.png",64,32));
		//Exit
		buttons[3] 			= new Button(320 - 32,340 - 16,new ImageTile("/Resources/Object/UI/Button/EXIT.png",64,32));
	}
	
	@Override
	public void update(GameContainer gc, float dt)
	{
		if(!OptionMenu.getSingleton().isActive()){
			//Update------------------------------------------------------
			for (int i = 0; i < buttons.length; i++) {
				if(buttons[i] != null)
					if(buttons[i].isActive())
						buttons[i].update(gc, dt);
			}
			//Update------------------------------------------------------
			
			
			if(gc.getInput().isKeyDown(GameManager.KEYUP))
			{
				indicatorIndex--;
				if(indicatorIndex > 0)
					if(buttons[indicatorIndex] == null)
						indicatorIndex --;
				if(indicatorIndex < 0)
					indicatorIndex = 0;
			}
			else if(gc.getInput().isKeyDown(GameManager.KEYDOWN))
			{

				indicatorIndex++;

				if(indicatorIndex < buttons.length - 1)
					if(buttons[indicatorIndex] == null)
						indicatorIndex ++;
				
				if(indicatorIndex >buttons.length - 1)
					indicatorIndex = buttons.length - 1;
			}
			else if(gc.getInput().isKeyDown(KeyEvent.VK_ENTER)){
				switch(indicatorIndex)
				{
					case 0: 
						if(!GameManager.getSingleton().isFading()){
							GameManager.getSingleton().SceneChange(GameManager.getSingleton().getInGame(),.5f);
						}
						break;
					case 1: 
						OptionMenu.getSingleton().setActive(true);
						break;
					case 2: 
						OptionMenu.getSingleton().setActive(true);
						break;
					case 3:
						System.exit(0);
						break;
				}
			}
			
			
			if(buttons[0].isReleased())
			{
				if(!GameManager.getSingleton().isFading()){
					GameManager.getSingleton().SceneChange(GameManager.getSingleton().getInGame(),.5f);
				}
			}
			else if(buttons[1] != null)
			{
				if(buttons[1].isReleased())
				{
					
				}
			}
			else if(buttons[2].isReleased())
			{
				OptionMenu.getSingleton().setActive(true);
			}
			else if(buttons[3].isReleased())
			{
				System.exit(0);
			}
		}
		else
		{
			OptionMenu.getSingleton().update(gc, dt);
		}
	}
	@Override
	public void render(GameContainer gc,Renderer r)
	{
		r.setzDepth(Renderer.BACKGROUND_LAYER);
		r.drawImage(mainMenuImage, 0, 0);
		
		r.setzDepth(Renderer.OBJECT_LAYER);
		for (int i = 0; i < buttons.length; i++) {
			if(buttons[i] != null)
				if(buttons[i].isActive())
					buttons[i].render(gc, r);
		}
		
		
		r.drawImageTile(indicator, (int)(buttons[indicatorIndex].getPosX() - indicator.getTileW() ), (int)buttons[indicatorIndex].getPosY(), 0, 0);
		r.drawImageTile(indicator, (int)(buttons[indicatorIndex].getPosX() +buttons[indicatorIndex].getWidth() ),(int) buttons[indicatorIndex].getPosY(), 1, 0);
		
		if(OptionMenu.getSingleton().isActive())
			OptionMenu.getSingleton().render(gc, r);
	}
	
	@Override
	public int getAmbientColor()
	{
		return this.ambientColor ;
	}
}

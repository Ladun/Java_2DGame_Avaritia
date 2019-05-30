package com.ladun.game.UI;

import java.awt.event.KeyEvent;

import com.ladun.engine.GameContainer;
import com.ladun.engine.Renderer;
import com.ladun.engine.gfx.ImageTile;
import com.ladun.game.GameManager;
import com.ladun.game.Scene.InGameScene;
import com.ladun.game.objects.GameObject;
import com.ladun.game.objects.UI.Button;

public class PauseMenu {
	
	public Button[]		buttons;

	private ImageTile	indicator;
	private int			indicatorIndex 	= 0;
	
	
	public PauseMenu(GameContainer gc)
	{
		indicator = new ImageTile("/Resources/Object/UI/Indicator.png",32,32);
		buttons 	= new Button[3];
		//Resume
		buttons[0]	= (new Button(gc.getWidth() / 2 - GameManager.TS
				,gc.getHeight() / 2 - GameManager.TS * 2, new ImageTile("/Resources/Object/UI/Button/Resume.png",64,32)));
		//Option
		buttons[1] 	= (new Button(gc.getWidth() / 2 - GameManager.TS
				,gc.getHeight() / 2- GameManager.TS, new ImageTile("/Resources/Object/UI/Button/OPTION.png",64,32)));
		//Quit
		buttons[2] 	= (new Button(gc.getWidth() / 2 - GameManager.TS
				,gc.getHeight() / 2, new ImageTile("/Resources/Object/UI/Button/QUIT.png",64,32)));
	}
	
	public void update(GameContainer gc,float dt)
	{
		if(!OptionMenu.getSingleton().isActive()){
			//Update------------------------------------------------------
			for (int i = 0; i < buttons.length; i++) {
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
						((InGameScene)(GameManager.getSingleton().getCurrentScene())).SetPause();
						break;
					case 1: 
						OptionMenu.getSingleton().setActive(true);
						break;
					case 2: 
						if(!GameManager.getSingleton().isFading()){
							GameManager.getSingleton().SceneChange(GameManager.getSingleton().getMainMenu(),.5f);
						}
						break;
				}
			}
			
			if(buttons[0].isReleased())
			{
				((InGameScene)(GameManager.getSingleton().getCurrentScene())).SetPause();
			}
			else if(buttons[1].isReleased())
			{
				OptionMenu.getSingleton().setActive(true);
			}
			else if( buttons[2].isReleased())
			{
				if(!GameManager.getSingleton().isFading()){
					GameManager.getSingleton().SceneChange(GameManager.getSingleton().getMainMenu(),.5f);
				}
			}
		}
		else
			OptionMenu.getSingleton().update(gc, dt);
	}
	
	public void render(GameContainer gc,Renderer r)
	{
		r.drawUIFillRect(0, 0, gc.getWidth(), gc.getHeight(), 0xa5000000);
		
		if(OptionMenu.getSingleton().isActive())
		{
			OptionMenu.getSingleton().render(gc, r);
		}
		else{
			for (int i = 0; i < buttons.length; i++) {
				if(buttons[i].isActive())
					buttons[i].render(gc, r);
			}
			
			r.drawUIImageTile(indicator, (int)(buttons[indicatorIndex].getPosX() - indicator.getTileW() ), (int)buttons[indicatorIndex].getPosY(), 0, 0);
			r.drawUIImageTile(indicator, (int)(buttons[indicatorIndex].getPosX() +buttons[indicatorIndex].getWidth() ),(int) buttons[indicatorIndex].getPosY(), 1, 0);
			
		}
	}
}

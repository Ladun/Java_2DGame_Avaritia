package com.ladun.game.UI;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.ladun.engine.GameContainer;
import com.ladun.engine.Renderer;
import com.ladun.engine.gfx.Font;
import com.ladun.engine.gfx.ImageTile;
import com.ladun.game.GameManager;
import com.ladun.game.objects.UI.Button;

public class ClearFailMenu {

	private Button 	exit;

	
	public ClearFailMenu()
	{
		exit = new Button(320 - GameManager.TS
				,340 - 32, new ImageTile("/Resources/Object/UI/Button/EXIT.png",64,32));
	}
	
	public void update(GameContainer gc, float dt)
	{
		exit.update(gc, dt);
		if(exit.isReleased())
		{
			if(GameManager.getSingleton().isClear()){
				try {
	
					File f = new File("ClearTime.txt");
					if(!f.exists())
						f.createNewFile();
					BufferedWriter bw = new BufferedWriter(new FileWriter(f));
					bw.append("Clear Time : " +GameManager.getSingleton().getTime());
					bw.newLine();
					bw.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			System.exit(0);
		}

	}
	
	public void render(GameContainer gc, Renderer r){
		r.drawUIFillRect(0, 0, gc.getWidth(), gc.getHeight(), 0xa5000000);
		if(GameManager.getSingleton().isClear()){
			r.drawUIText("Clear", 320 - 40,  170 - 32, 0xffcdcdcd, Font.STANDARD_16);
			r.drawUIText((int)(GameManager.getSingleton().getTime())  + " s", 320 - 40,  170 - 64, 0xffcdcdcd, Font.STANDARD_16);
		}
		else if(GameManager.getSingleton().isFailded())
			r.drawUIText("Fail", 320 - 28,  170 - 32, 0xffcdcdcd, Font.STANDARD_16);
	
		exit.render(gc, r);
	}
}

package com.ladun.game;

import java.util.ArrayList;

import com.ladun.engine.AbstractGame;
import com.ladun.engine.GameContainer;
import com.ladun.engine.Renderer;
import com.ladun.engine.gfx.Font;
import com.ladun.game.Scene.InGameScene;
import com.ladun.game.objects.GameObject;
import com.ladun.game.objects.creatures.Enemy;

public class DebugWindow extends AbstractGame {

	@Override
	public void init(GameContainer gc) {
		// TODO Auto-generated method stub

		gc.getRenderer().setAmbientColor(-1);
	}

	@Override
	public void update(GameContainer gc, float dt) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void render(GameContainer gc, Renderer r) {

		if(GameManager.getSingleton() == null)
			return;
		if(GameManager.getSingleton().getCurrentScene() == null)
			return;
		
		ArrayList<GameObject> gs = GameManager.getSingleton().getObjects();
		r.drawText("Name\t(PosX, PosY)\tActive", 0, 10, 0xffffff00,Font.STANDARD);

		for(int i = 0; i < gs.size();i++)
		{
			if( 10 * i + 30 > 900)
				break;
			GameObject g = gs.get(i);
			
			r.drawText(
					g.getTag() + "\t" + "(" + g.getPosX() + ", "  +g.getPosY() + ")\t" + g.isActive()
					, 0, 10 * i + 30, 0xffffffff,Font.STANDARD);
		}

		if(GameManager.getSingleton().getCurrentScene() instanceof InGameScene)
		{
			InGameScene igs = (InGameScene)(GameManager.getSingleton().getCurrentScene());
			if(igs.getMap()!=null){
			ArrayList<GameObject> mgs = igs.getMap().getObjects();
			for(int i = gs.size(); i < mgs.size() + gs.size();i++)
			{
				if( 10 * i + 30 > 900)
					break;
				GameObject g = mgs.get(i - gs.size());
				
				r.drawText(
						g.getTag() + "\t" + "(" + (int)g.getPosX() + ", "  +(int)g.getPosY() + ")\t" + g.isActive()
						, 0, 10 * i + 30, 0xffffffff,Font.STANDARD);
			}

			ArrayList<Enemy> es = igs.getMap().getEnemys();
			for(int i = gs.size() + mgs.size(); i < es.size() + gs.size() + mgs.size();i++)
			{
				if( 10 * i + 30 > 900)
					break;
				Enemy g = es.get(i - gs.size() - mgs.size());
				
				r.drawText(
						g.getTag() + "\t" + "(" + (int)g.getPosX() + ", "  +(int)g.getPosY() + ")\t" + g.isActive()
						, 0, 10 * i + 30, 0xffffffff,Font.STANDARD);
			}
			}
		}
	}

}

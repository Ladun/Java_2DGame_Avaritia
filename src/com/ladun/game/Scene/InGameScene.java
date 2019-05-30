 package com.ladun.game.Scene;

import com.ladun.engine.GameContainer;
import com.ladun.engine.Renderer;
import com.ladun.engine.gfx.Font;
import com.ladun.game.Camera;
import com.ladun.game.GameManager;
import com.ladun.game.Map;
import com.ladun.game.Physics;
import com.ladun.game.Thread.MapLoader;
import com.ladun.game.UI.OptionMenu;
import com.ladun.game.UI.UserInterface;
import com.ladun.game.components.AABBComponent;
import com.ladun.game.objects.creatures.Observer;
import com.ladun.game.objects.creatures.Player;

public class InGameScene extends AbstractScene{

	public  static int		NIGHT_BACKGROUND_COLOR 	= 0xff35395a;
		
	public	static int		NIGHT_COLOR 			= 0xff111111;
	public	static int 		DAWN_COLOR 				= 0xff515263;
	public	static int 		DAY_COLOR				= 0xffffffff;
	
	public 	static String 	START_MAP_NAME			= "TestMap";
	
	
	private int 		currentBackground	= NIGHT_BACKGROUND_COLOR;//0xffa1ece7;	
	private String		currentMapName		= "";
	private String		nextMapName 		= "";
	private int			sp;// 스폰 위치 (앞에서 나온느지 뒤에서 나오는지)
	
	
	private Map 					currentMap;
	private	UserInterface			ui;
	private Camera 					camera;
	
	private boolean 				pause;
	
	
	
	public InGameScene()
	{	
	
	}
	
	@Override
	public void init()
	{
		initialized 		= true;
		camera 				= new Camera("player");
		ui 					= UserInterface.getSingleton();
		
		objects.add(new Player(32, 32));
		objects.add(new Observer(32 ,32 * 22));	

		MapLoad(START_MAP_NAME);
		nextMapName = START_MAP_NAME;
		sp = 1;
	}
	
	private void MapLoad(String mapName){
		if(!MapLoader.getMaps().containsKey(mapName)){
			(new MapLoader(mapName)).start();
		}
		
	}
	
	private void ChangeTheMap(GameContainer gc)
	{
		if(!GameManager.getSingleton().isLoading()){
			if(nextMapName.length() != 0){
				if(MapLoader.getMaps().containsKey(nextMapName)){
					if(!currentMapName.equalsIgnoreCase(nextMapName))
					{
						//맵 이동--------------------------------------------
						currentMap = MapLoader.getMaps().get(nextMapName);
						if(sp == 1)
							((Player)GameManager.getSingleton().getObject("Player")).setTile(currentMap.getlX(), currentMap.getlY());
						else if (sp == 0)
							((Player)GameManager.getSingleton().getObject("Player")).setTile(currentMap.getrX(), currentMap.getrY());
							
						nextMapName = "";
						
						gc.getRenderer().setAmbientColor(currentMap.getAmbientColor());
						
					}
				}
			}
		}
	}
	
	@Override
	public void update(GameContainer gc, float dt)
	{
		if(currentMap != null){
				if(!pause){
					currentMap.update(gc, dt);
					super.update(gc, dt);
				
					if(!GameManager.getSingleton().isFading()){
						AABBComponent paabb = (AABBComponent)GameManager.getSingleton().getObject("Player").findComponent("aabb");
						if(paabb.getCenterX()- paabb.getHalfWidth() <= 0 )
						{
							if(currentMap.getPreMap().length() != 0){
		
								if(currentMap.getPreMap().equalsIgnoreCase("Clear")){
									GameManager.getSingleton().setClear(true);
								}
								else{
									GameManager.getSingleton().StartFading(.5f);
									currentMapName 	= currentMap.getName();
									nextMapName		= currentMap.getPreMap();
									MapLoad(nextMapName);
									sp = 0;
								}
							}
						}
						else if (paabb.getCenterX() + paabb.getHalfWidth() >= currentMap.getLevelW() * 32)
						{
							if(currentMap.getNextMap().length() != 0){
								if(currentMap.getNextMap().equalsIgnoreCase("Clear")){
									if(GameManager.getSingleton().getEnemy("Tanker") == null)
										GameManager.getSingleton().setClear(true);
								}
								else{
									GameManager.getSingleton().StartFading(.5f);
									currentMapName 	= currentMap.getName();
									nextMapName		= currentMap.getNextMap();
									MapLoad(nextMapName);
									sp = 1;
								}
							}
						}
					}
				
					camera.update(gc, dt);
		
					Physics.update();
				}
			}
		
		if(GameManager.getSingleton().isFadingBlack())
		{
			ChangeTheMap(gc);

		}
		
		ui.update(gc, dt);
		
	}
	
	@Override
	public void render(GameContainer gc, Renderer r)
	{
		
		if(currentMap != null){
			r.setzDepth(Renderer.BACKGROUND_LAYER);
			r.drawFillRect(0, 0,currentMap.getLevelW() * GameManager.TS, currentMap.getLevelH() * GameManager.TS, currentBackground);
			camera.render(r);
			//
			r.drawImage(currentMap.getBackgroundImage(), 0 , 0);
			r.drawImage(currentMap.getLevelImage(), 0 , 0);
			//Object Rendering------------------------------------------
			r.setzDepth(Renderer.OBJECT_LAYER);
			super.render(gc, r);
			currentMap.render(gc, r);
			//----------------------------------------------------------
		}
		ui.render(gc, r);
		
		if(OptionMenu.getSingleton().getDayNightCheckBox().isLastChecked()){
			if(OptionMenu.getSingleton().getDayNightCheckBox().isChecked())
				r.setAmbientColor(DAY_COLOR);
			else
				r.setAmbientColor(NIGHT_COLOR);
		}
		
	}
	
	
	
	@Override
	public int getAmbientColor() {
		// TODO Auto-generated method stub
		if(currentMap != null)
			return this.currentMap.getAmbientColor();
		return -1;
	}//0xff555555
	
	public UserInterface getUI()
	{
		return ui;
	}
	
	public Camera getCamera()
	{
		return camera;
	}

	public Map getMap() {
		return currentMap;
	}

	
	public void SetPause()
	{
		if(pause)
			pause =false;
		else
			pause = true;
	}
	
	public boolean isPause()
	{
		return pause;
	}

}

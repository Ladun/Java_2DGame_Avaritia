package com.ladun.game.Thread;

import java.util.HashMap;

import com.ladun.engine.gfx.Image;
import com.ladun.engine.gfx.ImageTile;
import com.ladun.engine.gfx.Light;
import com.ladun.game.GameManager;
import com.ladun.game.Map;
import com.ladun.game.Items.Adrenaline;
import com.ladun.game.Items.AssaultRifle;
import com.ladun.game.Items.Bandage;
import com.ladun.game.Items.Bazooka;
import com.ladun.game.Items.DropItem;
import com.ladun.game.Items.FirstAidKit;
import com.ladun.game.Items.Pistol;
import com.ladun.game.Items.Shotgun;
import com.ladun.game.Scene.InGameScene;
import com.ladun.game.objects.Spawner;
import com.ladun.game.objects.creatures.Tanker;
import com.ladun.game.objects.environments.Door;
import com.ladun.game.objects.environments.Door.Type;
import com.ladun.game.objects.environments.Ladder;
import com.ladun.game.objects.environments.Platform;
import com.ladun.game.objects.environments.Portal;
import com.ladun.game.objects.environments.Slope;
import com.ladun.game.objects.environments.Water;

public class MapLoader extends AbstractThread{
	
	public enum LoadType {MapLoad, CreatureSpawn}

	private static HashMap<String,Map>	maps		= new HashMap<String,Map>();
	
	private String mapName;
	
	private	 String 		Door_Image 			= "/Resources/Map/Door1.png";
	private	 String 		DoorG_Image 		= "/Resources/Map/Door2.png";
	
	
	public MapLoader(String mapName)
	{
		this.mapName = mapName;
		thread 	= new Thread(this);
	}
	
	public synchronized void start()
	{
		if(thread.getState() == Thread.State.NEW){
			
			thread.start();
			
		}
	}	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		Load();
	}
	
	private synchronized void Load()
	{
		GameManager.getSingleton().setLoading(true);
		System.out.println("Loading ( MapLoader ) ");

		MapLoad();
		CreatureSpawn();
		
		
		GameManager.getSingleton().setLoading(false);
		System.out.println("Loading Finish ( MapLoader )");
	}

	private void MapLoad(){
		
		int ts = GameManager.TS;
		
		switch(mapName)
		{
			case "TestMap":{
				maps.put("TestMap", new Map("TestMap","","","TestMap_Background.png", "TestMap_Design.png","TestMap_Collision.png",Light.FULL,InGameScene.DAY_COLOR));
				

				maps.get("TestMap").addObject(new Platform(10*ts + 16	,	 29 * ts,	 ts/2,	ts * 7));
				maps.get("TestMap").addObject(new Platform(11*ts		,	 36 * ts, ts * 7, 	ts/2));
				maps.get("TestMap").addObject(new Platform(18*ts, 35 * ts, ts * 7, ts/2));
				maps.get("TestMap").addObject(new Slope(17 * ts, 35*ts, ts, false));
				//maps.get("TestMap").addObject(new Water(10*ts + ts/2, 38 * ts, ts * 20, ts/2, 0xaa50abff));
				
				maps.get("TestMap").MakeLadder(new Ladder[]{
						new Ladder(15,27,0,11),
						new Ladder(87,28,0,11),
						new Ladder(91,28,0,11)
				});
				
				
				maps.get("TestMap").addObject(new DropItem(new Bazooka(), 4 * 32, 37* 32 + 16));
				maps.get("TestMap").addObject(new DropItem(new Shotgun(), 5 * 32, 37* 32 + 16));
				maps.get("TestMap").addObject(new DropItem(new AssaultRifle(), 6 * 32, 37* 32 + 16));
				maps.get("TestMap").addObject(new DropItem(new Pistol(), 7 * 32, 37* 32 + 16));
				
				for(int i = 0; i < 10; i ++)
				{
					maps.get("TestMap").addObject(new DropItem(new Image("/Resources/Object/Item/Pistol_Magazine.png"), 7 * 32	, 39 * 32,0));
					maps.get("TestMap").addObject(new DropItem(new Image("/Resources/Object/Item/AK_Magazine.png"), 6 * 32	, 39 * 32,1));
					maps.get("TestMap").addObject(new DropItem(new Image("/Resources/Object/Item/Shotgun_Magazine.png"), 5 * 32	, 39 * 32,2));
					maps.get("TestMap").addObject(new DropItem(new Image("/Resources/Object/Item/Bazooka_Magazine.png"), 4 * 32	, 39 * 32,3));
					
				}
				break;		
			}
			case "City1":{
				maps.put("City1",new Map("City1","","City2","City1_Background.png", "City1_Design.png","City1_Collision.png",Light.FULL,InGameScene.NIGHT_COLOR));
				
				maps.get("City1").MakeBuilding(3,20,19,19,2,
						new Door[]{
								new Door(new ImageTile(Door_Image,32,32),20,19,false,Type.Wood),
								new Door(new ImageTile(Door_Image,32,32),38,19,true,Type.Wood) },
						new Portal[]{
								new Portal(29 * 32		,19 * 32 - 13	,28 * 32	,17 * 32	,39	,45),
								new Portal(29 * 32		,17 * 32 - 13	,28 * 32	,15 * 32	,39	,45),
								new Portal(28 * 32 - 7	,17 * 32 - 13	,29 * 32	,19 * 32	,39	,45),
								new Portal(28 * 32 - 7	,15 * 32 - 13	,29 * 32	,17 * 32	,39	,45)
						});
				maps.get("City1").MakeBuilding(4,45,19,12,2,
						new Door[]{
								new Door(new ImageTile(Door_Image,32,32),45,19,false,Type.Wood),
								new Door(new ImageTile(Door_Image,32,32),56,19,true,Type.Wood) },
						new Portal[]{
								new Portal(51 * 32		,19 * 32 - 13	,50 * 32	,17 * 32	,39	,45),
								new Portal(51 * 32		,17 * 32 - 13	,50 * 32	,15 * 32	,39	,45),
								new Portal(51 * 32		,15 * 32 - 13	,50 * 32	,13 * 32	,39	,45),
								new Portal(50 * 32 - 7	,17 * 32 - 13	,51 * 32	,19 * 32	,39	,45),
								new Portal(50 * 32 - 7	,15 * 32 - 13	,51 * 32	,17 * 32	,39	,45),
								new Portal(50 * 32 - 7	,13 * 32 - 13	,51 * 32	,15 * 32	,39	,45)
						});
				
				
				maps.get("City1").addObject(new DropItem(new Bandage(), 25 * 32 - 4, 19 * 32 ));
				maps.get("City1").addObject(new DropItem(new Image("/Resources/Object/Item/Pistol_Magazine.png"), 23 * 32	, 17 * 32 - 16,0));
				maps.get("City1").addObject(new DropItem(new Image("/Resources/Object/Item/Pistol_Magazine.png"), (21 + (int)(Math.random() * 15)) * 32	, 15 * 32,0));
				maps.get("City1").addObject(new DropItem(new Pistol(), 23 * 32 - 4, 19 * 32 - 16));
				maps.get("City1").addObject(new DropItem(new Image("/Resources/Object/Item/Pistol_Magazine.png"), (46 + (int)(Math.random() * 11)) * 32	, 17 * 32,0));
				maps.get("City1").addObject(new DropItem(new Image("/Resources/Object/Item/Pistol_Magazine.png"), (46 + (int)(Math.random() * 11)) * 32	, 15 * 32,0));

				
				break;		
			}
			case "City2":{
				maps.put("City2",new Map("City2","City1","Clear","City2_Background.png", "City2_Design.png","City2_Collision.png",Light.FULL,InGameScene.NIGHT_COLOR));
				maps.get("City2").MakeBuilding(3,24,31,13,2,
						new Door[]{
								new Door(new ImageTile(DoorG_Image,32,32),24,31,false,Type.Glass),
								new Door(new ImageTile(DoorG_Image,32,32),36,31,true,Type.Glass) },
						new Portal[]{
								new Portal(30 * 32		,31 * 32 - 13	,29 * 32	,29 * 32	,39	,45),
								new Portal(30 * 32		,29 * 32 - 13	,29 * 32	,27 * 32	,39	,45),
								new Portal(29 * 32 - 7	,29 * 32 - 13	,30 * 32	,31 * 32	,39	,45),
								new Portal(29 * 32 - 7	,27 * 32 - 13	,30 * 32	,29 * 32	,39	,45),
								new Portal(25 * 32		,27 * 32 - 13	,25 * 32	,25 * 32	,39	,45),
								
						});
				maps.get("City2").MakeBuilding(1,24,25,4,2,
						new Door[]{
								new Door(new ImageTile(Door_Image,32,32),27,25,true,Type.Wood)},
						new Portal[]{
								new Portal(25 * 32 - 7	,25 * 32 - 13	,25 * 32	,27 * 32	,39	,45)
						});
				
				maps.get("City2").MakeBuilding(4,43,31,15,2,
						new Door[]{
								new Door(new ImageTile(Door_Image,32,32),43,31,false,Type.Wood),
								new Door(new ImageTile(Door_Image,32,32),57,31,true,Type.Wood) },
						new Portal[]{
								new Portal(51 * 32		,31 * 32 - 13	,50 * 32	,29 * 32	,39	,45),
								new Portal(51 * 32		,29 * 32 - 13	,50 * 32	,27 * 32	,39	,45),
								new Portal(51 * 32		,27 * 32 - 13	,50 * 32	,25 * 32	,39	,45),
								new Portal(51 * 32		,25 * 32 - 13	,50 * 32	,23 * 32	,39	,45),
								new Portal(50 * 32 - 7	,29 * 32 - 13	,51 * 32	,31 * 32	,39	,45),
								new Portal(50 * 32 - 7	,27 * 32 - 13	,51 * 32	,29 * 32	,39	,45),
								new Portal(50 * 32 - 7	,25 * 32 - 13	,51 * 32	,27 * 32	,39	,45),
								
						});
				maps.get("City2").MakeBuilding(1,48,23,5,2,
						new Door[]{
								new Door(new ImageTile(Door_Image,32,32),48,23,false,Type.Wood),
								new Door(new ImageTile(Door_Image,32,32),52,23,true,Type.Wood)},
						new Portal[]{
								new Portal(50 * 32 - 7	,23 * 32 - 13	,51 * 32	,25 * 32	,39	,45),
						});
				maps.get("City2").MakeBuilding(5,81,31,25,3,
						new Door[]{
								new Door(new ImageTile(DoorG_Image,32,32),81,31,false,Type.Glass),
								new Door(new ImageTile(DoorG_Image,32,32),105,31,true,Type.Glass),
								new Door(new ImageTile(DoorG_Image,32,32),90,28,true,Type.Glass),
								new Door(new ImageTile(DoorG_Image,32,32),90,25,true,Type.Glass),
								new Door(new ImageTile(DoorG_Image,32,32),90,22,true,Type.Glass), 
								new Door(new ImageTile(DoorG_Image,32,32),90,19,true,Type.Glass) }, 
						new Portal[]{
								new Portal(88 * 32		,31 * 32 - 13	,87 * 32	,28 * 32	,39	,45),
								new Portal(88 * 32		,28 * 32 - 13	,87 * 32	,25 * 32	,39	,45),
								new Portal(88 * 32		,25 * 32 - 13	,87 * 32	,22 * 32	,39	,45),
								new Portal(88 * 32		,22 * 32 - 13	,87 * 32	,19 * 32	,39	,45),
								new Portal(87 * 32 - 7	,28 * 32 - 13	,88 * 32	,31 * 32	,39	,45),
								new Portal(87 * 32 - 7	,25 * 32 - 13	,88 * 32	,28 * 32	,39	,45),
								new Portal(87 * 32 - 7	,22 * 32 - 13	,88 * 32	,25 * 32	,39	,45),
								new Portal(87 * 32 - 7	,19 * 32 - 13	,88 * 32	,22 * 32	,39	,45),
								
						});		

				maps.get("City2").addObject(new DropItem(new AssaultRifle(), 45 * 32 , 25 * 32));
				maps.get("City2").addObject(new DropItem(new FirstAidKit(), 97 * 32 , 28 * 32));
				maps.get("City2").addObject(new DropItem(new Image("/Resources/Object/Item/AK_Magazine.png"), 97 * 32 - 8, 28 * 32,1));
				maps.get("City2").addObject(new DropItem(new FirstAidKit(), 97 * 32 , 25 * 32));
				maps.get("City2").addObject(new DropItem(new Image("/Resources/Object/Item/AK_Magazine.png"), 97 * 32 - 8, 25 * 32,1));
				maps.get("City2").addObject(new DropItem(new Image("/Resources/Object/Item/Pistol_Magazine.png"), 97 * 32 - 8, 25 * 32,0));
				maps.get("City2").addObject(new DropItem(new FirstAidKit(), 97 * 32 , 22 * 32));
				maps.get("City2").addObject(new DropItem(new Image("/Resources/Object/Item/AK_Magazine.png"), 97 * 32 - 8, 22 * 32,1));

				maps.get("City2").addObject(new DropItem(new Shotgun(), 97 * 32 , 19 * 32 ));
				maps.get("City2").addObject(new DropItem(new Image("/Resources/Object/Item/Shotgun_Magazine.png"), 97 * 32 + 8, 19 * 32,2));
				maps.get("City2").addObject(new DropItem(new Image("/Resources/Object/Item/Shotgun_Magazine.png"), 97 * 32 + 4, 19 * 32,2));
				
				
				
				maps.get("City2").addObject(new Platform(125 * 32 + 3, 30* 32, 4, 64));
				maps.get("City2").addObject(new Platform(124 * 32 - 10, 31* 32 + 4, 20, 28));
				maps.get("City2").addObject(new Platform(125 * 32 - 21, 31* 32 - 14, 20, 46));
				
				maps.get("City2").addObject(new DropItem(new Adrenaline(), 123 * 32 , 31 * 32));
				maps.get("City2").addObject(new DropItem(new FirstAidKit(), 123 * 32 -4, 31 * 32));
				maps.get("City2").addObject(new DropItem(new Image("/Resources/Object/Item/Pistol_Magazine.png"), 123 * 32 - 8, 31 * 32,0));
				maps.get("City2").addObject(new DropItem(new Image("/Resources/Object/Item/AK_Magazine.png"), 123 * 32 - 12, 31 * 32,1));
				maps.get("City2").addObject(new DropItem(new Image("/Resources/Object/Item/Shotgun_Magazine.png"), 123 * 32 - 16, 31 * 32,2));

				break;
			}
		}
		
		if(maps.containsKey(mapName))
			if(!maps.get(mapName).isLoad())
				maps.get(mapName).loadLevel();
	}
	
	private void CreatureSpawn()
	{
		switch(mapName)
		{
			case "TestMap" : {
	
				maps.get("TestMap").addObject(new Tanker(15, 34));
				break;
			}
			case "City1":{	
					maps.get("City1").addObject(new Spawner(23,17,0,3));
					maps.get("City1").addObject(new Spawner(23,15,0,2));
					maps.get("City1").addObject(new Spawner(47,17,0,3));
					maps.get("City1").addObject(new Spawner(47,15,0,2));
					break;
				}
			case "City2":{
				maps.get("City2").addObject(new Tanker(150, 31));
				maps.get("City2").addObject(new Spawner(26,31,0,3));
				
				maps.get("City2").addObject(new Spawner(26,29,0,3));
				
				maps.get("City2").addObject(new Spawner(45,31,0,3));
				
				maps.get("City2").addObject(new Spawner(45,29,0,3));
				
				maps.get("City2").addObject(new Spawner(45,27,0,3));

				int spx = 95;
				for(int i = 0; i <5; i++)
				{
					maps.get("City2").addObject(new Spawner(spx, 31 - i * 3,0,6));
				}
				//------------------------------------------------------
				break;
			}
		}
	}
	
	public static HashMap<String, Map> getMaps() {
		return maps;
	}
	
}

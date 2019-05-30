package com.ladun.game.objects.environments;

import java.util.ArrayList;

import com.ladun.engine.GameContainer;
import com.ladun.engine.Renderer;
import com.ladun.game.GameManager;
import com.ladun.game.objects.GameObject;
import com.ladun.game.objects.PhysicsObject;

public class Building extends GameObject{
	
	private final static int DRAK_COLOR = 0x00000000;

	private ArrayList<GameObject> objects = new ArrayList<GameObject>();
	
	private PhysicsObject[] inCheckObject; // 플레이어가 건물 안에 들어와 있는지 확인하는 List
	
	
	private String buildingCheckTag = "BuildingInCheck";
	
	
	private float[] checkPercent;
	
	public Building(int layer,int startX,int startY,int width,int height,Door[] doors)
	{
		inCheckObject = new PhysicsObject[layer];
		checkPercent  = new float[layer];
		//height = 층마다 높이
		int ts 		= GameManager.TS;
		int tsDiv 	= ts / 2;

		this.tag 	= "Building";
		this.posX 	= startX * ts;
		this.posY 	= startY * ts;
		
		for(int i = 0; i < layer;i++)
		{
			inCheckObject[i] = new PhysicsObject(buildingCheckTag,
					this.posX + tsDiv - 6,
					this.posY - i *  (ts) * 2 - tsDiv - 2,
					(width - 1) * ts + 6 * 2,
					height *ts - tsDiv + 4);
			checkPercent[i] = 1;
		}
		
		
		//Building Collision Make
		boolean[] collision = new boolean[layer * 2];
		for(int i = 0; i < doors.length;i++)
		{
			Door d = doors[i];
			objects.add(d);
			 if (d.isRight()){
					//오른쪽 
					objects.add(new Platform((int)d.getPosX() + tsDiv		, (int)d.getPosY() - tsDiv- (height - 2) *  ts
							, tsDiv 			, tsDiv + (height - 2) *  ts));
					if(((int)d.getPosX()  / ts) == startX || ((int)d.getPosX()  / ts) == startX + width - 1)
						collision[(((int)(startY - (d.getPosY() / ts))) / height ) * 2 + 1] = true;
			}
			 else
			{	
				//왼쪽

				objects.add(new Platform((int)d.getPosX()		, (int)d.getPosY() - tsDiv - (height - 2) *  ts
										, tsDiv 			, tsDiv + (height - 2) *  ts));
				
			
				if(((int)d.getPosX()  / ts) == startX || ((int)d.getPosX()  / ts) == startX + width - 1)
					collision[(((int)(startY - (d.getPosY() / ts))) / height ) * 2] = true;
			}
		}
		for(int i = 0; i < layer; i++)
		{

			objects.add(new Platform(startX * ts		, (startY -( height - 1) - i * height) * ts			
									, width * ts		, tsDiv));
			//왼쪽 벽
			if(!collision[i * 2])
			objects.add(new Platform(startX * ts		, (startY - ( height - 1) - i * height) * ts + tsDiv
									, tsDiv 	, tsDiv + ts * (height - 1)	));
			//오른쪽 벽
			if(!collision[i * 2 + 1])
			objects.add(new Platform((startX + width - 1) * ts + tsDiv	, (startY - ( height - 1) - i * height) * ts + tsDiv	
									, tsDiv 	, tsDiv + ts * (height - 1)	));
		}
	}
	
	public Building(int layer,int startX,int startY,int width,int height,Door[] doors,Portal[] portals)
	{
		this(layer, startX, startY, width, height, doors);
		for(int i = 0; i < portals.length; i++)
		{
			objects.add(portals[i]);
		}
	}

	@Override
	public void update(GameContainer gc, float dt) {

		
		
		for (int i = 0; i < objects.size(); i++) {
			if(objects.get(i).isActive())
				objects.get(i).update(gc, dt);
			if (objects.get(i).isDestroy()) {
				objects.remove(i);
				i--;
			}
		}
		
		for (int i = 0; i < inCheckObject.length; i++) {
			
			if(!inCheckObject[i].isCollided())
			{
				checkPercent[i] += dt * 2;
				if(checkPercent[i] > 1)
					checkPercent[i] = 1;
			}
			else{
				checkPercent[i] -= dt * 2;
				if(checkPercent[i] < 0)
					checkPercent[i] = 0;
			}
			if(inCheckObject[i].isActive())
				inCheckObject[i].update(gc, dt);
		}
		
		
		
	}

	@Override
	public void render(GameContainer gc, Renderer r) {
		for (GameObject obj : objects)
			if(obj.isActive())
				obj.render(gc, r);

		for (int i = 0; i < inCheckObject.length; i++) {
			PhysicsObject obj = inCheckObject[i];
			if(obj.isActive()){
				obj.render(gc, r);
				
				r.setzDepth(Renderer.FORGROUND_LAYER);
				r.drawFillRect((int)obj.getPosX(), (int)obj.getPosY(), obj.getWidth(), obj.getHeight(), ((int)(255 * checkPercent[i]) << 24 | DRAK_COLOR));
				r.setzDepth(Renderer.BACKGROUND_LAYER);
			}
		}
		
		
		
		
	}

	@Override
	public void collision(GameObject other) {
		// TODO Auto-generated method stub
		
	}
	
}

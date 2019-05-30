package com.ladun.game;

import java.util.ArrayList;

import com.ladun.engine.GameContainer;
import com.ladun.engine.Renderer;
import com.ladun.engine.gfx.Image;
import com.ladun.game.objects.GameObject;
import com.ladun.game.objects.creatures.Enemy;
import com.ladun.game.objects.environments.Building;
import com.ladun.game.objects.environments.Door;
import com.ladun.game.objects.environments.Ladder;
import com.ladun.game.objects.environments.Portal;
import com.ladun.game.objects.environments.Slope;

public class Map {
	private static final int LEFT_COLOR 	= 0xff0060ff;
	private static final int RIGHT_COLOR 	= 0xffff0000;
	private static final int SLOPE_COLOR 	= 0xff28b178;

	protected ArrayList<GameObject> objects = new ArrayList<GameObject>();
	protected ArrayList<Enemy> enemys = new ArrayList<Enemy>();

	private Image 		backgroundImage;
	private Image 		levelImage;
	private Image 		collisionImage;
	
	private String 		name;
	private String 		nextMap;
	private String 		preMap;

	private boolean  	load;
	private boolean[] 	collision;
	private int 		levelW, levelH;
	private int 		lX,lY;
	private int 		rX,rY;
	private int 		ambientColor;
	
	public Map( String name,String preMap,String nextMap, String sky, String level, String collision, int mapLightBlock,int ambientColor)
	{
		this.name 				= name;
		this.nextMap			= nextMap;
		this.preMap				= preMap;
		this.backgroundImage 	= new Image("/Resources/Map/" + sky);
		this.levelImage 		= new Image("/Resources/Map/" + level);
		this.collisionImage 	= new Image("/Resources/Map/" + collision);
		this.ambientColor		= ambientColor;
		levelImage.setLightBlock(mapLightBlock,this.getClass().hashCode());
	}
	public void MakeLadder(Ladder[] ladders)
	{
		/*new Ladder(new ImageTile("/Map/Ladders.png",32,32),
		17,12,
		0,
		8)
		*/
		for(int i = 0; i < ladders.length;i++)
			objects.add(ladders[i]);
	}

	public void MakeBuilding(int layer,int startX,int startY,int width,int height,Door[] doors)
	{
		objects.add(new Building( layer, startX, startY, width, height, doors));
	}
	
	public void MakeBuilding(int layer,int startX,int startY,int width,int height,Door[] doors,Portal[] portals)
	{
		objects.add(new Building( layer, startX, startY, width, height, doors,portals));
	}
	
		
	
	public void update(GameContainer gc, float dt)
	{

		for (int i = 0; i < objects.size(); i++) {
			if(objects.get(i).isActive())
				objects.get(i).update(gc, dt);
			if (objects.get(i).isDestroy()) {
				objects.remove(i);
				i--;
			}
		}
		for (int i = 0; i < enemys.size(); i++) {
			if(enemys.get(i).isActive())
				enemys.get(i).update(gc, dt);
			if (enemys.get(i).isDestroy()) {
				enemys.remove(i);
				i--;
			}
		}
	}
	public void render(GameContainer gc, Renderer r) {

		for (GameObject obj : objects)
			if(obj.isActive())
				obj.render(gc, r);
		for (GameObject obj : enemys)
			if(obj.isActive())
				obj.render(gc, r);
		
	}


	public void loadLevel() {
		load  =true;
		Image levelImage 	= collisionImage;
		levelW 				= levelImage.getW();
		levelH 				= levelImage.getH();
		collision 			= new boolean[levelW * levelH];
		
		boolean[] flag		= new boolean[levelW * levelH];

		for (int y = 0; y < levelH; y++) {
			for (int x = 0; x < levelW; x++) {
				if(!flag[x + y * levelW]){
					flag[x + y * levelW] = true;
					if (levelImage.getP()[x + y * levelImage.getW()] == LEFT_COLOR){
						lX = x;
						lY = y;
					}
					else if (levelImage.getP()[x + y * levelImage.getW()] == RIGHT_COLOR){
						rX = x;
						rY = y;
					}
					else if(levelImage.getP()[x + y * levelImage.getW()] == SLOPE_COLOR)
					{
						boolean dir = true;
						if(levelImage.getP()[(x + 1) + y * levelImage.getW()] ==  0xff000000)
							dir = false;
						else if (levelImage.getP()[(x - 1) + y * levelImage.getW()] ==  0xff6188ca)
							dir = false;
						int i = 1;
						while(i + x < levelW)
						{
							if(levelImage.getP()[(x + i) + y * levelImage.getW()] ==  SLOPE_COLOR)
							{
								flag[(x + i) + y * levelW] = true;
								i++;
							}
							else break;
						}
						objects.add(new Slope(x * GameManager.TS, y * GameManager.TS,i * 32,dir));
					
					}
					else if (levelImage.getP()[x + y * levelImage.getW()] == 0xff000000) {
						collision[x + y * levelImage.getW()] = true;
					} else {
						collision[x + y * levelImage.getW()] = false;
					}
				}
			}
		}
	}
	public boolean isLoad() {
		return load;
	}
	public void addObject(GameObject go)
	{
		if(go instanceof Enemy){
			enemys.add((Enemy)go);
		}else{
			objects.add(go);
		}
	}

	//Getter Setter------------------------------------------------------

	public Image getBackgroundImage() {
		return backgroundImage;
	}
	public String getNextMap() {
		return nextMap;
	}
	public String getPreMap() {
		return preMap;
	}
	public String getName() {
		return name;
	}
	public Image getLevelImage() {
		return levelImage;
	}
	public boolean getCollision(int x, int y) {
		if (x < 0 || x >= levelW || y < 0 || y >= levelH)
			return true;
		return collision[x + y * levelW];

	}

	public int getAmbientColor() {
		return ambientColor;
	}
	public int getlX() {
		return lX;
	}
	public int getlY() {
		return lY;
	}
	public int getrX() {
		return rX;
	}
	public int getrY() {
		return rY;
	}
	public int getLevelW() {
		return levelW;
	}

	public int getLevelH() {
		return levelH;
	}
	public ArrayList<GameObject> getObjects() {
		return objects;
	}
	public ArrayList<Enemy> getEnemys() {
		return enemys;
	}

}

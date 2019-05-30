package com.ladun.game;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;

import com.ladun.engine.AbstractGame;
import com.ladun.engine.GameContainer;
import com.ladun.engine.Renderer;
import com.ladun.engine.audio.SoundClip;
import com.ladun.engine.gfx.ImageTile;
import com.ladun.game.Save.UserData;
import com.ladun.game.Scene.AbstractScene;
import com.ladun.game.Scene.BeginningScene;
import com.ladun.game.Scene.InGameScene;
import com.ladun.game.Scene.MainMenuScene;
import com.ladun.game.Thread.MapLoader;
import com.ladun.game.objects.GameObject;

public class GameManager extends AbstractGame {
	private static class Singleton{
		static final GameManager single = new GameManager();
	}
	
	public static GameManager getSingleton()
	{
		return Singleton.single;
	}
	
	

	//Static Variable-----------------------------------------------
	public 	static final 	int 			TS 				= 64;
	public 	static 			boolean 		isDebugMode 	= true;
	private static 			GameContainer 	gc;
	

	//----------------------------------------------------
	private HashMap<String,SoundClip> soundClips		= new HashMap<String,SoundClip>();
	public static int 		KEYUP 				= KeyEvent.VK_UP;
	public static int 		KEYDOWN				= KeyEvent.VK_DOWN;
	public static int 		KEYRIGHT 			= KeyEvent.VK_RIGHT;
	public static int 		KEYLEFT 			= KeyEvent.VK_LEFT;
	public static int 		KEYATTACK 			= KeyEvent.VK_A;
	public static int 		KEYJUMP 			= KeyEvent.VK_SPACE;
	public static int	 	KEYINTERACT			= KeyEvent.VK_E;
	public static int 		KEYRUN				= KeyEvent.VK_SHIFT;
	public static int 		KEYUSE				= KeyEvent.VK_Q;
	public static int 		KEYWEAPONSWITCH		= KeyEvent.VK_S;
	public static int 		KEYITEMSWITCH		= KeyEvent.VK_W;
	public static int 		KEYRELOAD			= KeyEvent.VK_R;
	public static int 		KEYDROP				= KeyEvent.VK_F;
	
	public static float 	SOUND				= 1;
	
	//About Scene---------------------------------------------------
	private AbstractScene 		currentScene;
	private InGameScene 		inGame;
	private MainMenuScene		mainMenu;
	private BeginningScene 		beginning;
	
	private boolean 			loading;
	private boolean 			fading;
	private boolean 			fadingBlack;
	private AbstractScene 		changeScene;
	private float 				fadingPercent	= 0;
	private float				fadingTime		= 1;
	private boolean 			failded;
	private boolean 			clear;
	
	private ImageTile 			loadingImage;
	private float 				loadingAnim = 0;
	//--------------------------------------------------------------
	private UserData			ud;
	private float 				time;
	
	
	public GameManager() {
		//In Unity, Like a "Awake" Method
			
		this.inGame			= new InGameScene();
		this.mainMenu 		= new MainMenuScene();
		this.beginning 		= new BeginningScene();
		
		loadingImage 		= new ImageTile("/Resources/Loading.png",16,16);

		
		soundClips.put("Pistol_Reload"	, new SoundClip("/Resources/audio/Pistol_Reload.wav"));
		soundClips.put("AR_Reload"		, new SoundClip("/Resources/audio/AR_Reload.wav"));
		soundClips.put("Shoot"			, new SoundClip("/Resources/audio/shot.wav"));
		soundClips.put("ShotgunShoot"	, new SoundClip("/Resources/audio/Shotgun.wav"));
		soundClips.put("Shotgun_Reload"	, new SoundClip("/Resources/audio/Shotgun_Reload.wav"));
		//soundClips.put("Punch", new SoundClip("/Resources/audio/punch.wav"));
	}

	@Override
	public void init(GameContainer gc) {
		//In Unity, Like a "Start" Method
		
		currentScene 	= beginning;

		currentScene.init();
		gc.getRenderer().setAmbientColor(currentScene.getAmbientColor());
	}

	@Override
	public void update(GameContainer gc, float dt) {
		
		if(isDebugMode)
			if(gc.getInput().isKeyDown(KeyEvent.VK_Y))
				clear =true;
		
		
		if(!clear){
			if((currentScene instanceof InGameScene))
			{
				if(!((InGameScene)currentScene).isPause())
					time+= dt;
			}
		}
		//Scene Fading---------------------------------------------------------------------
		if(fading )
		{
			if(!loading)
				fadingPercent += dt * ( 1 /fadingTime);
			else{
				if(!fadingBlack)
					fadingPercent += dt * ( 1 /fadingTime);
			}
			//System.out.println(fadingPercent);
			
		}
		//End of Scene Fading----------------------------------------------------------------
		
		if(fadingBlack)
		{
			if(currentScene != changeScene){
				if(currentScene instanceof InGameScene)
					((InGameScene)currentScene).SetPause();
				currentScene = changeScene;
				if(!currentScene.isInitialized())
					currentScene.init();
				gc.getRenderer().setAmbientColor(currentScene.getAmbientColor());
				
				System.out.println("Changing Scene ( GameManager )");
			}
		}
		
		if(loading){
			loadingAnim += dt * 10;
			if(loadingAnim >= 4)
				loadingAnim = 0;
		}
	
		//GameObject Update---------------------------------------------------------------------
		currentScene.update(gc, dt);
		//End of GameObject Update----------------------------------------------------------------
	
	}

	@Override
	public void render(GameContainer gc, Renderer r) {

		//GameObject Render---------------------------------------------------------------------
		currentScene.render(gc, r);
		//End of GameObject Render----------------------------------------------------------------
	

		
		
		//Scene Fading---------------------------------------------------------------------
		if(fading)
		{
			float alpha = 0;
			if(fadingPercent < 1){
				alpha = 255 * fadingPercent;
				fadingBlack = false;
			}
			else if (fadingPercent < 1 + fadingTime)
			{
				alpha = 255;
				fadingBlack = true;

			}
			else if( fadingPercent < 2 + fadingTime)
			{
				fadingBlack = false;
				alpha = 255 * (2 + fadingTime- fadingPercent);
			}
			else if( fadingPercent >= 2 + fadingTime)
			{
				fadingBlack = false;
				fading 			= false;
			}
			
			int color = ((int)alpha << 24 | 0x00000000);
			
			r.drawUIFillRect(0, 0, gc.getWidth(), gc.getHeight(), color);
		}
		//End of Scene Fading----------------------------------------------------------------
		if(loading)
		{
			r.drawUIImageTile(loadingImage, 10, 10, (int)loadingAnim, 0);
		}
		
	}

	public void SceneChange(AbstractScene scene,float time)
	{
		StartFading(time);
		changeScene		= scene;
	}
	public void StartFading(float time)
	{
		fading 			= true;
		fadingPercent 	= 0;
		fadingTime		= time;
	}
	

	public void addObject(GameObject object)
	{
		currentScene.getObjects().add(object);
	}
	
	//Getter and Setter--------------------------------------------------------------------
	public GameObject getObject(String tag)
	{
		for(int i = 0; i < currentScene.getObjects().size();i++)
		{
			if(currentScene.getObjects().get(i).getTag().equalsIgnoreCase(tag))
				return currentScene.getObjects().get(i);
		}
		
		
		return null;
	}
	public GameObject getEnemy(String tag)
	{
		for(int i = 0; i < ((InGameScene)currentScene).getMap().getEnemys().size();i++)
		{
			if(((InGameScene)currentScene).getMap().getEnemys().get(i).getTag().equalsIgnoreCase(tag))
				return ((InGameScene)currentScene).getMap().getEnemys().get(i);
		}
		
		
		return null;
	}


	public Map getMap() {
		if(currentScene instanceof InGameScene)
			return ((InGameScene)currentScene).getMap();
		return null;
	}
	
	public boolean isFailded() {
		return failded;
	}

	public void setFailded(boolean failded) {
		this.failded = failded;
	}

	public float getTime() {
		return time;
	}

	public boolean isClear() {
		return clear;
	}

	public void setClear(boolean clear) {
		this.clear = clear;
	}

	public AbstractScene getCurrentScene()
	{
		return currentScene;
	}

	public InGameScene getInGame()
	{
		return inGame;
	}
	public MainMenuScene getMainMenu()
	{
		return mainMenu;
	}
	public BeginningScene getBeginning() {
		return beginning;
	}

	public boolean isFading()
	{
		return fading;
	}

	public ArrayList<GameObject> getObjects() {
		return currentScene.getObjects();
	}
	public static GameContainer getGc() {
		return gc;
	}

	
	public boolean isFadingBlack() {
		return fadingBlack;
	}

	public UserData getUd() {
		return ud;
	}

	public HashMap<String,SoundClip> getSoundClips() {
		return soundClips;
	}

	public boolean isLoading() {
		return loading;
	}

	public void setLoading(boolean loading) {
		this.loading = loading;
	}

	//---------------------------------------------------------------------------------
	public static void main(String[] args) {
		
		gc = new GameContainer(GameManager.getSingleton());
		gc.setWidth(640);
		gc.setHeight(360);
		gc.setScale(2f);
		gc.setTitle("Avaritia");
		gc.start();
		
		/*
		if(isDebugMode){
			GameContainer debug = new GameContainer(new DebugWindow());
			debug.setWidth(200);
			debug.setHeight(450);
			debug.setScale(2f);
			debug.setTitle("Debug");
			debug.start();
		}*/
		
		
	}
	
	

}

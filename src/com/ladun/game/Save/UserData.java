package com.ladun.game.Save;

import java.io.Serializable;
import java.util.ArrayList;

import com.ladun.game.GameManager;
import com.ladun.game.Items.Gun;
import com.ladun.game.Items.Inventory;
import com.ladun.game.Items.Item;
import com.ladun.game.Scene.InGameScene;
import com.ladun.game.UI.UserInterface;
import com.ladun.game.objects.creatures.Enemy;
import com.ladun.game.objects.creatures.Player;

public class UserData implements Serializable{
	
	//현재 사용 안함
	//Player--------------------------------------
	private int 	playerX;
	private int 	playerY;
	private int 	playerHealth;
	//Map-----------------------------------------
	private String 	currentMap;
	//Preference----------------------------------
	private int[]	keyCode 		= new int[13];
	private float	sound;
	//Inventory-----------------------------------
	private Item[] 	items;
	private Gun[] 	guns;
	private int[]	bulletsCount ;
	//Monster-------------------------------------
	private ArrayList<Integer> enemysX 		= new ArrayList<Integer>();
	private ArrayList<Integer> enemysY 		= new ArrayList<Integer>();
	private ArrayList<Integer> enemysHealth = new ArrayList<Integer>();
	//--------------------------------------------
	
	public UserData()
	{
		Player p 			= (Player)GameManager.getSingleton().getObject("Player");
		playerX 			= (int)p.getPosX();
		playerY 			= (int)p.getPosY();
		playerHealth		= (int)p.getHealth();
		
		InGameScene	igs 	= ((InGameScene)(GameManager.getSingleton().getCurrentScene()));
		
		currentMap			= igs.getMap().getName();
	
		keyCode[0] 			= GameManager.KEYUP;
		keyCode[1] 			= GameManager.KEYDOWN;
		keyCode[2] 			= GameManager.KEYRIGHT;
		keyCode[3] 			= GameManager.KEYLEFT;
		keyCode[4] 			= GameManager.KEYATTACK;
		keyCode[5] 			= GameManager.KEYJUMP;
		keyCode[6] 			= GameManager.KEYINTERACT;
		keyCode[7] 			= GameManager.KEYRUN;
		keyCode[8] 			= GameManager.KEYUSE;
		keyCode[9] 			= GameManager.KEYWEAPONSWITCH;
		keyCode[10] 		= GameManager.KEYITEMSWITCH;
		keyCode[11] 		= GameManager.KEYRELOAD;
		keyCode[12] 		= GameManager.KEYDROP;
		
		sound				= GameManager.SOUND;
		
		items 			= new Item[UserInterface.getSingleton().getInv().getSize()];
		guns			= new Gun[2];
		bulletsCount	= new int[Inventory.getGunCount()];
		
		for(int i = 0 ; i < items.length;i++)
		{
			items[i] = UserInterface.getSingleton().getInv().getItem(i);
		}
		for(int i = 0 ; i < guns.length;i++)
		{
			guns[i] = UserInterface.getSingleton().getInv().getGun(i);
		}
		for(int i = 0 ; i < bulletsCount.length;i++)
		{
			bulletsCount[i] = UserInterface.getSingleton().getInv().getBulletsCount(i);
		}
		
		for(int i = 0; i < igs.getMap().getEnemys().size();i++)
		{
			Enemy e 	= (Enemy)igs.getMap().getEnemys().get(i);
			enemysX.add((int)e.getPosX());
			enemysY.add((int)e.getPosY());
			enemysHealth.add((int)e.getHealth());
		}
	
	}

	public int getPlayerX() {
		return playerX;
	}

	public int getPlayerY() {
		return playerY;
	}

	public int getPlayerHealth() {
		return playerHealth;
	}

	public String getCurrentMap() {
		return currentMap;
	}

	public int[] getKeyCode() {
		return keyCode;
	}

	public float getSound() {
		return sound;
	}

	public Item[] getItems() {
		return items;
	}

	public Gun[] getGuns() {
		return guns;
	}

	public int[] getBulletsCount() {
		return bulletsCount;
	}

	public ArrayList<Integer> getEnemysX() {
		return enemysX;
	}

	public ArrayList<Integer> getEnemysY() {
		return enemysY;
	}

	public ArrayList<Integer> getEnemysHealth() {
		return enemysHealth;
	}
	
	
	
}

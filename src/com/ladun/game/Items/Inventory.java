package com.ladun.game.Items;

import com.ladun.game.GameManager;
import com.ladun.game.Scene.InGameScene;
import com.ladun.game.objects.creatures.AliveObject;
import com.ladun.game.objects.creatures.Player;

public class Inventory {
	
	private static final int GUN_COUNT = 4;
	
	private Item[] 		items;
	private Gun[] 		guns;
	private int[]		bulletsCount 	= new int[GUN_COUNT];
	private int[]		gettingBullets  = new int[]{ 12, 30,7,1};
	private int			size 			= 0;
	private int			currentSlot		= 0;
	private int			currentGun		= 0;
	
	
	public Inventory(int size)
	{
		this.items		= new Item[size];
		this.guns		= new Gun[2];
		this.size 		= size;
	}
	
	
	public boolean addItem(Item it)
	{
		for(int i = 0; i < size;i++)
		{
			if(items[i] == null){
				items[i] = it;
				return true;
			}
		}
		return false;
	}
	
	public boolean addGun(Gun g)
	{
		for(int i = 0; i < 2;i++)
		{
			if(guns[i] == null){
				guns[i] = g;
				if(((Player)GameManager.getSingleton().getObject("Player")).getCurrentGun() == null)
					((Player)GameManager.getSingleton().getObject("Player")).setCurrentGun(g);
				
				return true;
			}
		}
		if(currentGun < 2){
			DropGun();
			guns[currentGun] = g;
			((Player)GameManager.getSingleton().getObject("Player")).setCurrentGun(g);
			return true;
		}
		
		
		
		return false;
	}
	
	public void addBullets(int i)
	{
		bulletsCount[i] += gettingBullets[i];
	}

	public void UseItem(AliveObject target)
	{
		items[currentSlot].Use(target);
		items[currentSlot] = null;
	}
	
	public void DropItem()
	{
		if(items[currentSlot] != null){
			InGameScene igs = (InGameScene)(GameManager.getSingleton().getCurrentScene());
			Player p = (Player)GameManager.getSingleton().getObject("Player");
			igs.getMap().addObject(new DropItem(items[currentSlot],p.getPosX(),p.getPosY()));
			items[currentSlot] = null;
		}
	}
	public void DropGun()
	{
		if(guns[currentGun] != null)
		{

			InGameScene igs = (InGameScene)(GameManager.getSingleton().getCurrentScene());
			Player p = (Player)GameManager.getSingleton().getObject("Player");
			igs.getMap().addObject(new DropItem(guns[currentGun],p.getPosX(),p.getPosY()));
			guns[currentGun] = null;
		}
	}
	
	public int Reload(int i, int curCount,int maxCount)
	{
		int d = maxCount - curCount;
		if(d <= bulletsCount[i]){
			bulletsCount[i] -= d;
			return d;
		}
		else 
		{
			int less = bulletsCount[i];
			bulletsCount[i] = 0;
			return less;
		}
	}
	
	//-----------------------------------------------------------------
	
	public Item	getItem(int i)
	{
		if(i <0 || i >= size)
			return null;
		return items[i];
	}
	public static int getGunCount() {
		return GUN_COUNT;
	}


	public Gun	getGun(int i)
	{
		if(i <0 || i >= 2)
			return null;
		return guns[i];
	}
	
	public int getSize() {
		return size;
	}

	public int getBulletsCount(int i) {
		if(i >= GUN_COUNT)
			return 0;
		return bulletsCount[i];
	}


	public int getCurrentSlot() {
		return currentSlot;
	}


	public int getCurrentGun() {
		return currentGun;
	}


	public void setCurrentSlot() {

		currentSlot = (currentSlot + 1) % size;
	}


	public void setCurrentGun() {
		currentGun = (currentGun + 1) % 3;
	}
	
	
	
	
}

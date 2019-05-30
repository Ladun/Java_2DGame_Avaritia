package com.ladun.game.Items;

import com.ladun.game.UI.UserInterface;
import com.ladun.game.objects.creatures.AliveObject;

public abstract class Gun extends Item{
	
	public enum ShootType { Auto, Single};
	
	//------------------------------------------------------------------------
	private ShootType	shootType;
	private float		betweenShoot;
	private float 		nextShootTime;
	private int 		curBulletCount;
	private int 		maxBulletCount;
	protected int 		ID;
	//------------------------------------------------------------------------
	

	public Gun(ShootType shootType, String name,int maxBulletCount,float reloadingTime,float betweenShoot) {
		super( name, reloadingTime );
		this.shootType 		= shootType;
		this.curBulletCount	= maxBulletCount;
		this.maxBulletCount	= maxBulletCount;
		this.betweenShoot 	= betweenShoot;
	}

	@Override
	public void Use(AliveObject target) {
		// TODO Auto-generated method stub
		Inventory inv = UserInterface.getSingleton().getInv();
		curBulletCount += inv.Reload(ID, curBulletCount, maxBulletCount);

	}
	public boolean Trigger()
	{
		if(curBulletCount > 0){
			if(nextShootTime >= betweenShoot)
			{
				nextShootTime = 0;
				curBulletCount--;
				return true;
			}
		}
		return false;
	}
	public abstract void Shoot(int tileX,int tileY,float offX,float offY,boolean right);
	
	public void TimeAdd(float dt)
	{
		nextShootTime += dt;
	}

	public boolean isCanReload()
	{
		return curBulletCount < maxBulletCount;
	}
	
	public ShootType getShootType() {
		return shootType;
	}

	public int getCurBulletCount() {
		return curBulletCount;
	}

	public int getMaxBulletCount() {
		return maxBulletCount;
	}
	public int getID()
	{
		return ID;
	}

}

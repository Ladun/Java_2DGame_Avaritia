package com.ladun.game.Items;

import com.ladun.engine.gfx.ImageTile;
import com.ladun.game.GameManager;
import com.ladun.game.objects.projectiles.Bullet;

public class Shotgun extends Gun{

	public Shotgun() {
		super(ShootType.Single,"Shotgun", 7,1.5f, 1f);
		// TODO Auto-generated constructor stub
		this.ID = 2;
		this.image 	= new ImageTile("/Resources/Object/Item/Shotgun.png",32,32);
		this.dropPaddingTop = 20;
		this.dropPadding = 2;
		
	}

	@Override
	public void Shoot(int tileX, int tileY, float offX, float offY, boolean right) {
		// TODO Auto-generated method stub
		GameManager.getSingleton().addObject(new Bullet(tileX,tileY,offX,offY,-1,700f,right,3));
		GameManager.getSingleton().addObject(new Bullet(tileX,tileY,offX,offY,0,700f,right,3));
		GameManager.getSingleton().addObject(new Bullet(tileX,tileY,offX,offY,1,700f,right,3));
	}

}

package com.ladun.game.Items;

import com.ladun.engine.gfx.ImageTile;
import com.ladun.game.GameManager;
import com.ladun.game.objects.projectiles.Bullet;

public class Pistol extends Gun{

	public Pistol() {
		super(ShootType.Single, "Pistol", 12,.5f,.2f);
		// TODO Auto-generated constructor stub
		this.ID = 0;
		this.image 	= new ImageTile("/Resources/Object/Item/Pistol.png",32,32);
		this.dropPaddingTop = 20;
		this.dropPadding = 6;
		
	}

	@Override
	public void Shoot(int tileX, int tileY, float offX, float offY, boolean right) {
		// TODO Auto-generated method stub
		GameManager.getSingleton().addObject(new Bullet(tileX,tileY,offX,offY,0,500,right,1));
	}

}

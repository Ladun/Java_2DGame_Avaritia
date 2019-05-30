package com.ladun.game.Items;

import com.ladun.engine.gfx.ImageTile;
import com.ladun.game.GameManager;
import com.ladun.game.objects.projectiles.Bullet;

public class AssaultRifle extends Gun{

	public AssaultRifle() {
		super(ShootType.Auto, "AR",  30,1,0);
		// TODO Auto-generated constructor stub
		this.ID 	= 1;
		this.image 	= new ImageTile("/Resources/Object/Item/AK-47.png",32,32);
		this.dropPaddingTop = 20;
		this.dropPadding = 2;
	}

	@Override
	public void Shoot(int tileX, int tileY, float offX, float offY, boolean right) {
		// TODO Auto-generated method stub
		GameManager.getSingleton().addObject(new Bullet(tileX, tileY, offX, offY, 0, 600f, right, 1));
	}

}

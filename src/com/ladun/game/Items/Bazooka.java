package com.ladun.game.Items;

import com.ladun.engine.gfx.ImageTile;
import com.ladun.game.GameManager;
import com.ladun.game.objects.projectiles.Rocket;

public class Bazooka extends Gun{

	public Bazooka() {
		super(ShootType.Single,"Bazooka", 10,3f, 1f);
		// TODO Auto-generated constructor stub
		this.ID = 3;
		this.image 	= new ImageTile("/Resources/Object/Item/Bazooka.png",32,32);
		this.dropPaddingTop = 20;
		this.dropPadding = 2;
	}
	

	@Override
	public void Shoot(int tileX, int tileY, float offX, float offY, boolean right) {
		// TODO Auto-generated method stub
		GameManager.getSingleton().addObject(new Rocket(tileX, tileY, offX, offY, 800f, right, 100));
	}

}

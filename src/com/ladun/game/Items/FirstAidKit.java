package com.ladun.game.Items;

import com.ladun.engine.gfx.ImageTile;
import com.ladun.game.objects.creatures.AliveObject;

public class FirstAidKit extends Item{

	
	public FirstAidKit() {
		super( "FirstAidKit", 1);
		this.image = new ImageTile("/Resources/Object/Item/FirstAidKit.png",32,32); 
		this.dropPaddingTop = 21;
		this.dropPadding = 9;
	}

	@Override
	public void Use(AliveObject target) {
		// TODO Auto-generated method stub
		int recoveryHealth = (int)(target.getMaxHealth() * 0.5f);
		target.recoveryHealth(recoveryHealth);
	}

}

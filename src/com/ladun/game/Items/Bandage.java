package com.ladun.game.Items;

import com.ladun.engine.gfx.ImageTile;
import com.ladun.game.objects.creatures.AliveObject;

public class Bandage extends Item{

	
	public Bandage() {
		super( "Bandage", .6f);
		this.image = new ImageTile("/Resources/Object/Item/Bandage.png",32,32); 
		this.dropPaddingTop = 28;
		this.dropPadding = 12;
	}

	@Override
	public void Use(AliveObject target) {
		// TODO Auto-generated method stub
		int recoveryHealth = (int)(target.getMaxHealth() * 0.25f);
		target.recoveryHealth(recoveryHealth);
	}

}

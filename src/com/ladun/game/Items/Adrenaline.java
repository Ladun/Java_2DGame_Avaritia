package com.ladun.game.Items;

import com.ladun.engine.gfx.ImageTile;
import com.ladun.game.objects.creatures.AliveObject;
import com.ladun.game.objects.creatures.Player;

public class Adrenaline extends Item{

	
	public Adrenaline() {
		super( "Adrenaline", .3f);
		this.image = new ImageTile("/Resources/Object/Item/Adrenaline.png",32,32); 
		this.dropPaddingTop = 29;
		this.dropPadding = 12;
	}

	@Override
	public void Use(AliveObject target) {
		// TODO Auto-generated method stub
		if(target instanceof Player)
		{
			int recoveryHealth = (int)(target.getMaxHealth() * 0.1f);
			target.recoveryHealth(recoveryHealth);
			((Player)target).setAdrenaline(true);
		}
	}

}

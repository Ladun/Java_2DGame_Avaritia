package com.ladun.game.Items;

import com.ladun.engine.gfx.Image;
import com.ladun.engine.gfx.ImageTile;
import com.ladun.game.objects.creatures.AliveObject;

public abstract class Item {
	protected String			name;
	protected ImageTile 		image;	
	
	protected float 			usingTime;//아이템 사용 시간
	

	protected int dropPaddingTop;
	protected int dropPadding;
	
	public Item( String name,float usingTime){
		this.name		= name;
		this.usingTime	= usingTime;
	}
	
	public abstract void Use(AliveObject target);
	//public abstract boolean canUse(AliveObject target);

	public String getName() {
		return name;
	}

	public Image getDropImage()
	{
		return image.getTileImage(0, 0);
	}
	
	public Image getInvImage()
	{

		return image.getTileImage(1, 0);
	}
	
	public float getUsingTime() {
		return usingTime;
	}

	public int getDropPaddingTop() {
		return dropPaddingTop;
	}

	public int getDropPadding() {
		return dropPadding;
	}
	
}

package com.ladun.game.Items;

import com.ladun.engine.GameContainer;
import com.ladun.engine.Renderer;
import com.ladun.engine.gfx.Image;
import com.ladun.game.GameManager;
import com.ladun.game.UI.UserInterface;
import com.ladun.game.components.AABBComponent;
import com.ladun.game.objects.GameObject;
import com.ladun.game.objects.Interactable;

public class DropItem extends GameObject implements Interactable{

	
	private boolean 	magazine;
	private int			id;
	private Item		item;
	private Image		image;
	private boolean		glowing;
	//padding 27, 12 pistol
	//padding 21 , 9 aidkit
	//padding 24 , 5 ak
	//padding 28, 15 pistol m
	//padding 24, 13 ak m
	
	public DropItem(Item item,float posX, float posY){
		
		this.tag		= "DropItem";
		this.image		= item.getDropImage();
		this.item		= item;
		this.posX		= posX;
		this.posY 		= posY;
		this.width		= 32;//GameManager.TS;
		this.height		= 32;//GameManager.TS;
		this.padding 	= item.getDropPadding();
		this.paddingTop	= item.getDropPaddingTop();
		
		this.addComponent(new AABBComponent(this));
		
	}
	public DropItem(Image image,float posX, float posY,int id){
		
		this.tag		= "DropItem";
		this.id			= id;
		this.magazine 	= true;
		this.image		= image;
		this.posX		= posX;
		this.posY 		= posY;
		this.width		= 32;//GameManager.TS;
		this.height		= 32;//GameManager.TS;
		this.padding 	= 10;
		this.paddingTop	= 26;
		
		this.addComponent(new AABBComponent(this));
		
	}
	
	
	@Override
	public void update(GameContainer gc, float dt) {
		// TODO Auto-generated method stub
		glowing = false;
		this.updateComponents(gc, dt);
	}

	@Override
	public void render(GameContainer gc, Renderer r) {
		// TODO Auto-generated method stub
		
		r.setzDepth(Renderer.DROPITEM_LAYER);
		r.drawImage(image	, (int)posX, (int)posY);
		if(glowing)
			r.drawImageGlow(image, (int)posX, (int)posY, 0xfffff3b1);
		this.renderComponents(gc, r);
	}

	@Override
	public void collision(GameObject other) {
		// TODO Auto-generated method stub
		if(other.getTag().equalsIgnoreCase("Player"))
			glowing = true;
		
	}

	@Override
	public boolean Interact(GameObject go, boolean bool) {
		if(magazine){
			UserInterface.getSingleton().getInv().addBullets(id);
			this.destroy = true;
			return true;
		}
		else{
			if(item instanceof Gun){
				if(UserInterface.getSingleton().getInv().addGun((Gun)item))
				{
					this.destroy = true;
					return true;
				}
			}
			else{
				if(UserInterface.getSingleton().getInv().addItem(item))
				{
					this.destroy = true;
					return true;
				}
			}
		}
		return false;
	}

	
	
}

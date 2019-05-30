package com.ladun.game.objects.environments;

import com.ladun.engine.GameContainer;
import com.ladun.engine.Renderer;
import com.ladun.engine.gfx.Image;
import com.ladun.engine.gfx.ImageTile;
import com.ladun.engine.gfx.Light;
import com.ladun.game.GameManager;
import com.ladun.game.components.AABBComponent;
import com.ladun.game.objects.GameObject;
import com.ladun.game.objects.Interactable;

public class Door extends GameObject implements Interactable{
	
	public enum Type { Wood, Glass, Iron}
	
	private Type 		type;
	
	private int			centerModify;

	private boolean 	glowing;
	private boolean 	opened;
	private boolean 	right;
	
	private int 		health;
	
	
	ImageTile image ;
	
	
	public Door(ImageTile image,int tileX,int tileY,boolean right,Type type)
	{
		this.type			= type;
		this.tag 			= "door";
		this.health			= 5;
		this.posX 			= tileX * GameManager.TS;
		this.posY 			= tileY * GameManager.TS;
		this.image			= image;
		this.right			= right;
		if((right)){
			this.centerModify	= -7;
			this.image.setMirrorX(true);
		}
		else 
		{
			this.centerModify 	= 7;
			this.image.setMirrorX(false);
		}

		this.width 			= GameManager.TS;
		this.height 		= GameManager.TS;

		this.image.setLightBlock(Light.NONE,this.getClass().hashCode());
		this.addComponent(new AABBComponent(this));
	}

	@Override
	public boolean Interact(GameObject go, boolean bool) {
		
		if(!opened){
			return opened = true;
		}
		else
		{
			return opened = false;
		}
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
		
		if(opened){
			this.image.setLightBlock(Light.NONE,this.getClass().hashCode());
		}
		else
		{
			if(type != Type.Glass)
				this.image.setLightBlock(Light.FULL,this.getClass().hashCode());
		}

		r.setzDepth(Renderer.BETWEEN_LAYER);
		r.drawImageTile(image, (int)posX, (int)posY,( (opened)? 1 : 0 )+ ((health <= 0)? 2: 0) , 0);
		if(glowing)
			r.drawImageGlow(getTileImage(), (int)posX, (int)posY, 0xffffffff);
		this.renderComponents(gc, r);
	}

	@Override
	public void collision(GameObject other) {
		if(other.getTag().equalsIgnoreCase("Player")){
			glowing = true;
		}
	}
	

	public Image getTileImage() {
		if(opened)
			return image.getTileImage(1+ ((health <= 0)? 2: 0), 0);
		else
			return image.getTileImage(0+ ((health <= 0)? 2: 0), 0);
	}

	public boolean isOpened() {
		return opened;
	}
	public int getCenterModify() {
		return centerModify;
	}

	public int getHealth() {
		return health;
	}

	public void hit() {
		if(health >0)
			health --;
	}

	public boolean isRight() {
		return right;
	}

}

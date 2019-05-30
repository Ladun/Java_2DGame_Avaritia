package com.ladun.game.components;

import com.ladun.engine.GameContainer;
import com.ladun.engine.Renderer;
import com.ladun.game.Physics;
import com.ladun.game.UI.OptionMenu;
import com.ladun.game.objects.GameObject;

public class AABBComponent extends Component{
	
	
	private GameObject parent;
	private int centerX,centerY;
	private int halfWidth, halfHeight;
	private int lastCenterX, lastCenterY;
	
	//collision direction
	private boolean right, left;
	private boolean above, below;

	public AABBComponent(GameObject parent)
	{
		this.parent = parent;
		this.tag = "aabb";
		
	}
	
	@Override
	public void update(GameContainer gc, float dt) {
		lastCenterX = centerX;
		lastCenterY = centerY;
		
		centerX = (int)(parent.getPosX() + (parent.getWidth() / 2));
		centerY = (int)(parent.getPosY() + (parent.getHeight() / 2) + (parent.getPaddingTop()/2));
		halfWidth = (parent.getWidth()/ 2) - parent.getPadding();
		halfHeight = (parent.getHeight() /2) - (parent.getPaddingTop()/2);
		

		Physics.addAABBComponent(this);   
	}
	
	@Override
	public void render(GameContainer gc,  Renderer r) {
		// TODO Auto-generated method stub
		
		if(OptionMenu.getSingleton().getDrawAABBCheckBox().isChecked())
			collisionRangeDraw(r);
		
		
	}
	
	private void collisionRangeDraw(Renderer r)
	{

		
		r.drawRect(centerX - halfWidth, centerY - halfHeight, halfWidth * 2, halfHeight * 2, 0xff000000);
		r.drawRect(centerX, centerY ,1,1, 0xffff0000);
		

		r.drawRect(centerX, centerY - halfHeight ,1,1, 0xff00ff00);

		r.drawRect(centerX, centerY + halfHeight ,1,1, 0xff0000ff);
	}
	
	//Getter and Setter ---------------------------------------------------------------------
	public int getLastCenterX() {
		return lastCenterX;
	}
	public void setLastCenterX(int lastCenterX) {
		this.lastCenterX = lastCenterX;
	}
	public int getLastCenterY() {
		return lastCenterY;
	}
	public void setLastCenterY(int lastCenterY) {
		this.lastCenterY = lastCenterY;
	}
	public GameObject getParent() {
		return parent;
	}
	public void setParent(GameObject parent) {
		this.parent = parent;
	}
	public int getCenterX() {
		return centerX;
	}
	public void setCenterX(int centerX) {
		this.centerX = centerX;
	}
	public int getCenterY() {
		return centerY;
	}
	public void setCenterY(int centerY) {
		this.centerY = centerY;
	}
	public int getHalfWidth() {
		return halfWidth;
	}
	public void setHalfWidth(int halfWidth) {
		this.halfWidth = halfWidth;
	}
	public int getHalfHeight() {
		return halfHeight;
	}
	public void setHalfHeight(int halfHeight) {
		this.halfHeight = halfHeight;
	}

	public boolean isRight() {
		return right;
	}

	public void setRight(boolean right) {
		this.right = right;
	}

	public boolean isLeft() {
		return left;
	}

	public void setLeft(boolean left) {
		this.left = left;
	}

	public boolean isAbove() {
		return above;
	}

	public void setAbove(boolean above) {
		this.above = above;
	}

	public boolean isBelow() {
		return below;
	}

	public void setBelow(boolean below) {
		this.below = below;
	}


}

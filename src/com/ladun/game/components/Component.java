package com.ladun.game.components;

import com.ladun.engine.GameContainer;
import com.ladun.engine.Renderer;
import com.ladun.game.GameManager;

public abstract class Component {

	protected String 		tag;
	protected GameManager 	gm 		= GameManager.getSingleton();
	public abstract void update(GameContainer gc,float dt);
	public abstract void render(GameContainer gc,Renderer r);
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}


}

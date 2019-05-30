package com.ladun.engine.gfx;

import com.ladun.engine.Renderer;

public abstract class Request {

	public int zDepth;
	public int offX ,offY;
	public int color;
	
	public abstract void Draw(Renderer r);
	
	
	public abstract String GetString();
}

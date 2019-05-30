package com.ladun.engine.gfx;

import com.ladun.engine.Renderer;

public class ImageRequest extends Request{
	public enum Type {Image,Glow}
	
	public Type 	t;
	public Image 	image;

	
	public ImageRequest(Image image,int zDepth, int offX,int offY)
	{
		this.t		= Type.Image;
		this.image 	= image;
		this.zDepth = zDepth;
		this.offX 	= offX;
		this.offY	= offY;
	}
	
	public ImageRequest(Image image,int zDepth, int offX,int offY,int color)
	{
		this.t		= Type.Glow;
		this.image 	= image;
		this.zDepth = zDepth;
		this.offX 	= offX;
		this.offY	= offY;
		this.color 	= color;
	}
	
	public void Draw(Renderer r)
	{
		switch(t){
		case Image:
			r.drawImage(image, offX, offY);
			break;
		case Glow:
			r.drawImageGlow(image, offX, offY, color);
			break;
			
		}
	}
	public String GetString()
	{
		return "(" + image.getW() + ", " + image.getH() + ")";
		}
}

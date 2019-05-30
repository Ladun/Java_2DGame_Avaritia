package com.ladun.engine.gfx;

import com.ladun.engine.Renderer;

public class UIRequest {

	public enum Type{ Image,ImageTile, Text, FillRect};
	
	public Type t;
	
	public Image image;
	public String text;
	public Font font;
	
	
	public int offX ,offY;
	public int tileX,tileY;
	public int width, height;
	public int color;
	
	public UIRequest(Image image,int offX,int offY){
		SetPos(offX, offY);
		this.image = image;
		t = Type.Image;
	}
	public UIRequest(ImageTile image,int offX,int offY,int tileX,int tileY)
	{
		SetPos(offX,offY);
		this.image = image;
		this.tileX = tileX;
		this.tileY = tileY;
		t = Type.ImageTile;
	}
	
	public UIRequest(String text,int offX,int offY,int color,Font font){
		SetPos(offX, offY);
		this.text = text;
		this.color = color;
		this.font = font;
		t = Type.Text;
	}
	public UIRequest(int offX,int offY,int width, int height, int color){
		SetPos(offX, offY);
		this.width = width;
		this.height = height;
		this.color = color;
		t = Type.FillRect;
	}
	
	
	private void SetPos(int offX,int offY)
	{
		this.offX = offX;
		this.offY = offY;
	}
	
	public void Draw(Renderer r)
	{
		switch(t)
		{
		case Image: 
			r.drawImage(image, offX, offY);
			break;
		case ImageTile:
			r.drawImageTile((ImageTile)image, offX, offY, tileX, tileY);
			break;
		case Text: 
			r.drawText(text, offX, offY, color,font);
			break;
		case FillRect : 
			r.drawFillRect(offX, offY, width, height, color);
			break;
		}
	}
}

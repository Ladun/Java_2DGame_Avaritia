package com.ladun.engine.gfx;

import com.ladun.engine.Renderer;

// 외부 이미지를 이용하는게 아니라 코드로 그리는 이미지를 처리할 때
public class OtherRequest extends Request{
	public enum Type{ Rect,FilledRect,Line,Text,Water};
	
	public Type t;
	
	
	public int offX ,offY;
	public int[] otherData;
	
	
	//TextData
	public String text;
	public Font font;
	
	public OtherRequest(Type t,int zDepth,int offX,int offY,int width, int height,int color)
	{
		this.t				= t;
		this.zDepth 		= zDepth;
		this.otherData 		= new int[2];
		this.otherData[0] 	= width;
		this.otherData[1] 	= height;
		this.color 			= color;
		this.offX 			= offX;
		this.offY 			= offY;			
	}
	public OtherRequest(String text,int zDepth, int offX,int offY,int color,Font font)
	{
		this.t				= Type.Text;
		this.text  			= text;
		this.offX 			= offX;
		this.offY			= offY;
		this.color			= color;
		this.font 			= font;
	}
	
	public OtherRequest(int zDepth,int offX,int offY, int width ,int height,int fx,int color)
	{
		this.t				= Type.Water;
		this.zDepth  		= zDepth;
		this.offX 			= offX;
		this.offY			= offY;
		this.otherData 		= new int[3];
		this.otherData[0] 	= width;
		this.otherData[1] 	= height;
		this.otherData[2] 	= fx;
		this.color 			= color;
	}
	
	public void Draw(Renderer r)
	{
		switch(t)
		{
		case Rect:
			r.drawRect(offX, offY, otherData[0], otherData[1], color);
			break;
		case FilledRect:
			r.drawFillRect(offX, offY, otherData[0], otherData[1], color);
			break;
		case Line:
			r.drawLine(offX, offY, otherData[0], otherData[1], color);
			break;
		case Text:
			r.drawText(text, offX, offY, color, font);
			break;
		case Water:
			r.drawWater(offX, offY, otherData[0], otherData[1], otherData[2],color);
			break;
		}
	}
	
	public String GetString()
	{
		return "(" + otherData[0] + ", " + otherData[1] + ")";
	}
}

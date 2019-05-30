package com.ladun.engine;

import java.awt.image.DataBufferInt;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.omg.PortableServer.POA;

import com.ladun.engine.gfx.Font;
import com.ladun.engine.gfx.Image;
import com.ladun.engine.gfx.ImageRequest;
import com.ladun.engine.gfx.ImageTile;
import com.ladun.engine.gfx.Light;
import com.ladun.engine.gfx.LightRequest;
import com.ladun.engine.gfx.OtherRequest;
import com.ladun.engine.gfx.Request;
import com.ladun.engine.gfx.UIRequest;
import com.ladun.game.GameManager;

public class Renderer {
	
	public final static int BACKGROUND_LAYER 	= 0;
	public final static int DROPITEM_LAYER 		= 3;
	public final static int OBJECT_LAYER 		= 5;
	public final static int BETWEEN_LAYER 		= 7;
	public final static int FORGROUND_LAYER 	= 10;
	public final static int UI_LAYER 			= 15;
	
	private ArrayList<Request> imageRequests 	= new ArrayList<Request>();
	private ArrayList<UIRequest> uiImageRequests = new ArrayList<UIRequest>();
	private ArrayList<LightRequest> lightRequests	= new ArrayList<LightRequest>();
	
	
	private int pW,pH;
	private int[] p;
	private int[] zb;
	private int[] lm;
	private int[] lb;
	
	private int ambientColor = 0xff232323;
	private int zDepth = 0;//그려지는 순서(layer 개념)
	private boolean processing = false;
	private boolean uiProcessing = false;
	private int camX, camY;
	
	public Renderer(GameContainer gc){
		
		pW = gc.getWidth();
		pH = gc.getHeight();
		p = ((DataBufferInt)gc.getWindow().getImage().getRaster().getDataBuffer()).getData(); 
		zb = new int[p.length];
		lm = new int[p.length];
		lb = new int[p.length];
	}
	
	public void clear(){
		for(int i =0; i < p.length;i++)
		{
			p[i] = 0;
			zb[i] = 0;
			lm[i] = ambientColor;
			lb[i] = 0;
		}
	}
	
	public void process(){
		processing = true;
		
		Collections.sort(imageRequests,new Comparator<Request>(){

			@Override
			public int compare(Request i0, Request i1) {
				// TODO Auto-generated method stub
				if(i0.zDepth < i1.zDepth)
					return -1;
				if(i0.zDepth > i1.zDepth)
					return 1;
				return 0;
			}
		});
		
		//System.out.println("Start");
		for(int i = 0; i < imageRequests.size();i++)
		{
			Request ir = imageRequests.get(i);
			setzDepth(ir.zDepth);
			//System.out.println(ir.zDepth + " : "+ir.GetString() + " : " + (ir instanceof ImageRequest));
			ir.Draw(this);

		}
		//System.out.println("End");
		
		//Draw lighting
		for(int i = 0; i < lightRequests.size();i++)
		{
			LightRequest l = lightRequests.get(i);
			this.drawLightRequest(l.light, l.locX, l.locY);
		}
		
		for(int i = 0; i< p.length;i++)
		{
			
			float r = ((lm[i] >> 16) & 0xff) /255f;
			float g = ((lm[i] >> 8) & 0xff) /255f;
			float b = (lm[i] & 0xff) /255f;
			
			
			p[i] = ((int)(((p[i] >> 16) & 0xff) * r) << 16 | (int)(((p[i] >> 8) & 0xff) * g) << 8 | (int)((p[i] & 0xff) * b));
		}
		
		
		//UI Draw--------------------------------------------------------------		
		camX = 0;
		camY = 0;
		setzDepth(UI_LAYER);
		uiProcessing = true;
		for(int i = 0;i < uiImageRequests.size();i++){

			uiImageRequests.get(i).Draw(this);
		}
		uiProcessing = false;
		setzDepth(BACKGROUND_LAYER);
		//---------------------------------------------------------------------		
		

		drawText(imageRequests.size() + "", 10, 10, 0xffffffff, Font.STANDARD);
		uiImageRequests.clear();
		imageRequests.clear();
		lightRequests.clear();
		processing = false;
	}
	
	public void setPixel(int x,int y, int value){

		int alpha = ((value >> 24) & 0xff);
		
		if(x < 0 || x>= pW || y < 0 || y >= pH || alpha == 0)// 맵 밖이거나 Alpha 값이 0일 경우 그리지 않음
			return;
		
		int index = x + y * pW;//화면 픽셀의 인덱스를 구하는 바법
		   

		
		
		
		if(alpha == 255)	
		{
			p[index] = value;
			if(zb[index] > zDepth)
				return;
			zb[index] = zDepth;
		}
		else{
			int pixelColor = p[index];
			
			int newRed = ((pixelColor >> 16) & 0xff) - (int)((((pixelColor >> 16) & 0xff) - ((value >> 16) & 0xff)) * alpha /255f);
			int newGreen = ((pixelColor >> 8) & 0xff) - (int)((((pixelColor >> 8) & 0xff) - ((value >> 8) & 0xff)) * alpha /255f);
			int newBlue = ((pixelColor) & 0xff) - (int)((((pixelColor) & 0xff) - ((value) & 0xff)) * alpha /255f);
			
			p[index] = (newRed << 16 | newGreen << 8 | newBlue);
			
 		}
	}
	
	public void setLightMap(int x,int y,int value)
	{
		
		if(x < 0 || x>= pW || y < 0 || y >= pH )
			return;
		
		int baseColor = lm[x+ y * pW];
		
		int maxRed = Math.max((baseColor >> 16) & 0xff, (value >> 16) & 0xff); 
		int maxGreen = Math.max((baseColor >> 8) & 0xff, (value >> 8) & 0xff); 
		int maxBlue = Math.max(baseColor & 0xff, value & 0xff); 
		
		
		
		lm[x + y * pW] = ( maxRed << 16 | maxGreen << 8 | maxBlue);
		
		
	}

	public void setLightBlock(int x,int y,int value)
	{
		if(x < 0 || x>= pW || y < 0 || y >= pH )
			return;
		
		
		if(zb[x+y*pW] > zDepth)
			return;
		
		lb[x + y * pW] = value;
		
		
	}
	
	public void drawText(String text,int offX,int offY, int color,Font font)
	{

		if(!processing && !uiProcessing)
		{
			imageRequests.add(new OtherRequest(text,zDepth,offX,offY,color,font));
			return;
		}
		offX -= camX;
		offY -= camY;
		int offset = 0;
		
		for(int i = 0; i < text.length();i++)
		{
			int unicode = text.codePointAt(i);
			
			
			for(int y = 0; y < font.getFontImage().getH();y++)
			{
				for(int x = 0 ; x < font.getWidths()[unicode];x++)
				{
					if(font.getFontImage().getP()[(x + font.getOffsets()[unicode]) + y * font.getFontImage().getW()] == 0xffffffff){
						setPixel(x + offX + offset,y + offY,color);
					}
				}
			}
			
			offset += font.getWidths()[unicode];
		}
	}
	
	public void drawUIText(String text,int offX,int offY, int color,Font font)
	{
		uiImageRequests.add(new UIRequest(text, offX, offY, color,font));
	}
	
	public void drawImage(Image image, int offX, int offY){

		if( !processing && !uiProcessing)
		{
			imageRequests.add(new ImageRequest(image,zDepth,offX,offY));
			return;
		}
		offX -= camX;
		offY -= camY;
		
		//Don't Render code
		if(offX < -image.getW()) return;
		if(offY < -image.getH()) return;
		if(offX >= pW) return;
		if(offY >= pH) return;
		
		int newX = 0;
		int newY = 0;
		int newWidth = image.getW();
		int newHeight = image.getH();
		

		
		//Clipping code
		if(offX < 0){newX -= offX;}
		if(offY < 0){newY -= offY;}
		if(newWidth + offX >= pW){newWidth -= (newWidth + offX - pW);}
		if(newHeight + offY >= pH){newHeight -= (newHeight + offY - pH);}
		
		for(int y = newY; y < newHeight;y++)
		{
			for(int x = newX; x < newWidth;x++)
			{
				int px = image.getP()[((image.isMirrorX())?image.getW()-1 - x:x)
						               + ((image.isMirrorY())?image.getH()-1 - y:y) 
						               *image.getW()];
			setPixel(x + offX, y + offY, px);
				if(((px >> 24) & 0xff)  != 0)
					setLightBlock(x + offX, y + offY, image.getLightBlock());
			}
		}
	}
	
	public void drawImageTile(ImageTile image,int offX,int offY,int tileX,int tileY)
	{
		if(!processing && !uiProcessing)
		{
			imageRequests.add(new ImageRequest(image.getTileImage(tileX, tileY),zDepth,offX,offY));
			return;
		}
		offX -= camX;
		offY -= camY;

		
		//Don't Render code
		if(offX < -image.getTileW()) return;
		if(offY < -image.getTileH()) return;
		if(offX >= pW) return;
		if(offY >= pH) return;
		
		int newX = 0;
		int newY = 0;
		int newWidth = image.getTileW();
		int newHeight = image.getTileH();
		
				
		//Clipping code
		if(offX < 0){newX -= offX;}
		if(offY < 0){newY -= offY;}
		if(newWidth + offX >= pW){newWidth -= (newWidth + offX - pW);}
		if(newHeight + offY >= pH){newHeight -= (newHeight + offY - pH);}
		
		for(int y = newY; y < newHeight;y++)
		{
			for(int x = newX; x < newWidth;x++)
			{
				int px = image.getP()[((image.isMirrorX())?image.getTileW()-1 - x:x) +tileX * image.getTileW()
	             + (((image.isMirrorY())?image.getTileH()-1 - y:y)  + tileY * image.getTileH()) 
	             *image.getW()];
				
				setPixel(x + offX, y + offY,px);
				if(((px >> 24) & 0xff)  != 0)
					setLightBlock(x + offX, y + offY, image.getLightBlock());
			}
		}
	}

	public void drawImageGlow(Image image, int offX, int offY,int color)
	{
		if(!processing && !uiProcessing)
		{
			imageRequests.add(new ImageRequest(image,zDepth,offX,offY,color));
			return;
		}
		
		offX -= camX;
		offY -= camY;
		
		//Don't Render code
		if(offX < -image.getW()) return;
		if(offY < -image.getH()) return;
		if(offX >= pW) return;
		if(offY >= pH) return;
		
		int newX = 0;
		int newY = 0;
		int newWidth = image.getW();
		int newHeight = image.getH();
				
		//Clipping code
		if(offX < 0){newX -= offX;}
		if(offY < 0){newY -= offY;}
		if(newWidth + offX >= pW){newWidth -= (newWidth + offX - pW);}
		if(newHeight + offY >= pH){newHeight -= (newHeight + offY - pH);}

		int flagW = newWidth - newX;
		int flagH = newHeight -	newY;
		boolean[] flag = new boolean[flagW * flagH];
		for(int y = newY; y < newHeight;y++)
		{
			for(int x = newX; x < newWidth;x++)
			{
				int tx1 = ((image.isMirrorX())?image.getW()-1 - x:x);
				int ty1 = ((image.isMirrorY())?image.getH()-1 - y:y);
				int px = image.getP()[tx1 + ty1 * image.getW()];
				if(((px >> 24) & 0xff) !=0x00){

					
					if(y == newY)
					{
						setPixel(x + offX, y - 1 + offY, color);
					}
					else if (y == newHeight-1)
					{
						setPixel(x + offX, y + 1 + offY, color);
					}
					
					for(int i = -1; i <= 1; i++)
					{
						for(int j = -1; j<=1; j++)
						{
							if( i== 0 && j ==0 )
								continue;
							if(x + j < 0  || x + j >= flagW || y + i <0 || y + i>= flagH)
								continue;
							if(!flag[(y + i) * flagW + (x + j)]){
								int tx2 = ((image.isMirrorX())?image.getW()-1 - (x + j):(x + j));
								int ty2 = ((image.isMirrorY())?image.getH()-1 - (y + i):(y + i));
								
								int apx = image.getP()[tx2 + ty2 * image.getW()];
								if(((apx >> 24) & 0xff) == 0x00)
								{
									setPixel(x + j + offX, y + i + offY, color);
									flag[(y + i) * (newWidth - newX) + (x + j)] = true;
								}
							}
							
						}
					}
				}
			}
		}
	}

	public void drawUIImage(Image image,int offX,int offY ){

		uiImageRequests.add(new UIRequest(image,offX,offY));
	}
	
	public void drawUIImageTile(ImageTile image,int offX,int offY,int tileX,int tileY)
	{
		uiImageRequests.add(new UIRequest(image,offX,offY,tileX, tileY));
	}
	
	public void drawRect(int offX,int offY,int width, int height,int color)
	{
		
		if(!processing && !uiProcessing)
		{
			imageRequests.add(new OtherRequest(OtherRequest.Type.Rect,zDepth,offX,offY,width,height,color));
			return;
		}
		offX -= camX;
		offY -= camY;
		
	
		for(int y = 0; y <= height;y++)
		{
			setPixel(offX			,y + offY,color);
			setPixel(offX + width	,y + offY,color);
		}
		for(int x = 0; x <= width;x++)
		{
			setPixel(x + offX,offY			,color);
			setPixel(x + offX,offY + height	,color);
		}
	}
	
	public void drawFillRect(int offX,int offY,int width, int height,int color)
	{
		
		if(!processing && !uiProcessing)
		{
			imageRequests.add(new OtherRequest(OtherRequest.Type.FilledRect,zDepth,offX,offY,width,height,color));
			return;
		}
		
		
		offX -= camX;
		offY -= camY;

		
		//Don't Render code
		if(offX < -width) return;
		if(offY < -height) return;
		if(offX >= pW) return;
		if(offY >= pH) return;
		
		for(int y = 0; y < height;y++)
		{
			for(int x = 0; x < width;x++)
			{
				setPixel(x + offX,y +offY,color);
			}
		}
		
	}
	
	public void drawUIFillRect(int offX,int offY,int width, int height,int color){

		uiImageRequests.add(new UIRequest(offX,offY,width,height,color));
	}
	
	public void drawLine(int sx,int sy, int ex,int ey,int color)
	{
		if(!processing && !uiProcessing)
		{
			imageRequests.add(new OtherRequest(OtherRequest.Type.Line,zDepth,sx,sy,ex,ey,color));
			return;
		}
		
		sx -= camX;
		sy -= camY;
		ex -= camX;
		ey -= camY;

		int x = sx;
		int y =  sy;
		
		int dx = ex - sx;
		int dy = ey - sy;
		
		boolean inverted = false;
		int step = (int) Math.signum(dx);
		int gradientStep = (int) Math.signum(dy);
		
		int longest = Math.abs(dx);
		int shortest = Math.abs(dy);
		if(longest < shortest)
		{
			inverted = true;
			longest = Math.abs(dy);
			shortest = Math.abs(dx);

			step = (int) Math.signum(dy);
			gradientStep = (int) Math.signum(dx);
		}
		int gradientAccumulation = longest / 2;
		
		for(int i = 0; i < longest; i++)
		{
			setPixel(x, y, color);
			if(inverted)
			{
				y += step;
			}
			else {
				x += step;
			}
			
			gradientAccumulation += shortest;
			if(gradientAccumulation >= longest)
			{
				if(inverted)
				{
					x += gradientStep;
				}
				else
				{
					y += gradientStep;
				}
				gradientAccumulation -= longest;
			}
		}
	}
	
	public void drawLight(Light l, int offX,int offY)
	{
		lightRequests.add(new LightRequest(l,offX,offY));
	}
	
	private void drawLightRequest(Light l,int offX,int offY)
	{

		offX -= camX;
		offY -= camY;

		for(int i = 0; i <= l.getDiameter();i++)
		{
			//east
			if(l.getDir(0))
				drawLightLine(l, l.getRadius(),l.getRadius(), l.getDiameter(),i, offX, offY);
			//west
			if(l.getDir(1))
				drawLightLine(l, l.getRadius(),l.getRadius(), 0	, i 				, offX, offY);
			//South
			if(l.getDir(2))
				drawLightLine(l, l.getRadius(),l.getRadius(), i	, l.getDiameter()	, offX, offY);
			//North
			if(l.getDir(3))
				drawLightLine(l, l.getRadius(),l.getRadius(), i	, 0					, offX, offY);
		}

	}
	
	private void drawLightLine(Light l, int x0,int y0, int x1 ,int y1,int offX,int offY)
	{
		int x = x0 + offX;
		int y=  y0 + offY;
		
		int dx = x1 - x0;
		int dy = y1 - y0;
		
		boolean inverted = false;
		int step = (int) Math.signum(dx);
		int gradientStep = (int) Math.signum(dy);
		
		int longest = Math.abs(dx);
		int shortest = Math.abs(dy);
		if(longest < shortest)
		{
			inverted = true;
			longest = Math.abs(dy);
			shortest = Math.abs(dx);

			step = (int) Math.signum(dy);
			gradientStep = (int) Math.signum(dx);
		}
		int gradientAccumulation = longest / 2;
		
		boolean isBlocked = false;
		
		for(int i = 0; i < longest; i++)
		{

			int screenX = x - l.getRadius();
			int screenY = y - l.getRadius();

			if(screenX < 0 || screenX >= pW || screenY < 0 || screenY >= pH){
				return;
			}
			
			int lightColor = l.getLightValue(x - offX, y - offY);
			if(lightColor == 0){
				return;
			}
			
			
			if(lb[screenX + screenY * pW] == Light.FULL){
				isBlocked = true;
				
				float power = 1 - (float)(Math.sqrt((x0 - (x - offX)) * (x0 - (x - offX)) + (y0 - (y- offY)) * (y0 - (y- offY)))) / l.getRadius();

				lightColor = ((int)(((lightColor  >> 16) & 0xff) * power) << 16 | (int)(((lightColor >> 8) & 0xff) * power) << 8 | (int)(((lightColor) & 0xff) * power));
				
				//	return;
			}else if(isBlocked){
			}

			if((lb[screenX + screenY * pW] == Light.NONE && !isBlocked) || (lb[screenX + screenY * pW] == Light.FULL))
				setLightMap(screenX ,screenY,lightColor);
			
			if(inverted)
			{
				y += step;
			}
			else {
				x += step;
			}
			
			gradientAccumulation += shortest;
			if(gradientAccumulation >= longest)
			{
				if(inverted)
				{
					x += gradientStep;
				}
				else
				{
					y += gradientStep;
				}
				gradientAccumulation -= longest;
			}
		}
	}
	
	public void drawWater(int offX,int offY, int width ,int height,int fx,int color)
	{
		if(!processing)
		{
			imageRequests.add(new OtherRequest(zDepth,offX,offY,width,height,fx,color));
			return;
		}
		
		offX -= camX;
		offY -= camY;
		
		float fy;
		
		int h = GameManager.TS;
		
		for(int y = 0; y < h;y++)
		{
			for(int x=  0; x < width ; x++)
			{
				fy = (float) Math.sin((fx + x) * Math.PI / 180 * 8)  * h / 8 + h /8;
				if(fy < y)
					setPixel(x + offX,y +offY,color);
					
			}
		}
		
		for(int y = 0; y < height - h;y++)
		{
			for(int x = 0; x < width;x++)
			{
				setPixel(x + offX,y +offY,color);
			}
		}
	}
	
	/*
	private void drawLightLine(Light l, int x0,int y0, int x1 ,int y1,int offX,int offY)
	{
		int x = x0;
		int y = y0;
		
		int dx = Math.abs(x1 - x0);
		int dy = Math.abs(y1 - y0);
		
		int sx = x0 < x1 ? 1 : -1;
		int sy = y0 < y1 ? 1 : -1;
		
		int err = dx - dy;
		int e2;
		
		boolean isBlocked = false;
		
		while(true){
			int screenX = x0 - l.getRadius() + offX;
			int screenY = y0 - l.getRadius() + offY;

			if(screenX < 0 || screenX >= pW || screenY < 0 || screenY >= pH){
				return;
			}
			
			int lightColor = l.getLightValue(x0, y0);
			if(lightColor == 0){
				return;
			}
			
			
			if(lb[screenX + screenY * pW] == Light.FULL){
				isBlocked = true;
				
				float power = 1 - (float)(Math.sqrt((x - (x0 -offX)) * (x - (x0-offX)) + (y - (y0-offY)) * (y - (y0-offY)))) / l.getRadius();

				lightColor = ((int)(((lightColor  >> 16) & 0xff) * power) << 16 | (int)(((lightColor >> 8) & 0xff) * power) << 8 | (int)(((lightColor) & 0xff) * power));
				
				//	return;
			}else if(isBlocked){
					return;
			}

			setLightMap(screenX ,screenY,lightColor);
			
			if(x0 == x1 && y0 == y1)
				break;
			
			e2 = 2 * err;
			
			if(e2 > -1 * dy)
			{
				err -= dy;
				x0 += sx;
				
			}
			
			if(e2 < dx)
			{
				err += dx;
				y0 += sy;
			}
		}
	}
    */
	
	public int getAmbientColor() {
		return ambientColor;
	}

	public void setAmbientColor(int ambientColor) {
		this.ambientColor = ambientColor;
	}

	public int getCamX() {
		return camX;
	}

	public void setCamX(int camX) {
		this.camX = camX;
	}

	public int getCamY() {
		return camY;
	}

	public void setCamY(int camY) {
		this.camY = camY;
	}

	public int getzDepth() {
		return zDepth;
	}

	public void setzDepth(int zDepth) {
		this.zDepth = zDepth;
	}
}

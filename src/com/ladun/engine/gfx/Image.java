package com.ladun.engine.gfx;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Image {
	private int w,h;
	private int[] p;
	private boolean mirrorX = false;
	private boolean mirrorY = false;
	private boolean alpha = false;
	private int lightBlock = Light.NONE;
	private int inherenceID = 0;
	
	public Image(String path)
	{
		BufferedImage image = null;
		try {
			image = ImageIO.read(Image.class.getResourceAsStream(path));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		w = image.getWidth();
		h = image.getHeight();
		p = image.getRGB(0, 0, w, h, null, 0, w);
		
		image.flush();
	}
	
	public Image(int p[],int w,int h)
	{
		this.p = p;
		this.w = w;
		this.h = h;
	}
	
	public boolean isMirrorX() {
		return mirrorX;
	}

	public void setMirrorX(boolean mirrorX) {
		this.mirrorX = mirrorX;
	}

	public boolean isMirrorY() {
		return mirrorY;
	}

	public void setMirrorY(boolean mirrorY) {
		this.mirrorY = mirrorY;
	}

	public int getLightBlock() {
		return lightBlock;
	}

	public void setLightBlock(int lightBlock,int inherenceID) {
		this.lightBlock 	= lightBlock;
		this.inherenceID 	= inherenceID;
	}

	public boolean isAlpha() {
		return alpha;
	}

	public void setAlpha(boolean alpha) {
		this.alpha = alpha;
	}

	public int getW() {
		return w;
	}

	public void setW(int w) {
		this.w = w;
	}

	public int getH() {
		return h;
	}

	public void setH(int h) {
		this.h = h;
	}

	public int[] getP() {
		return p;
	}

	public void setP(int[] p) {
		this.p = p;
	}

	public int getInherenceID() {
		return inherenceID;
	}
}

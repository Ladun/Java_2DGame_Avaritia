package com.ladun.engine;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

public class Window {
	
	private JFrame frame;
	private BufferedImage image;
	private Canvas canvas;
	private BufferStrategy bs;
	private Graphics g;
	
	public Window(GameContainer gc)
	{
		
		image = new BufferedImage(gc.getWidth(),gc.getHeight(),BufferedImage.TYPE_INT_RGB);
		canvas = new Canvas();
		Dimension s = new Dimension((int)(gc.getWidth() * gc.getScale()),(int)(gc.getHeight()* gc.getScale()));
		canvas.setPreferredSize(s);
		canvas.setMaximumSize(s);
		canvas.setMinimumSize(s);
				
		frame = new JFrame(gc.getTitle());	

		//frame.setUndecorated(true);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.add(canvas,BorderLayout.CENTER);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setVisible(true);
		
		canvas.createBufferStrategy(2);
		bs= canvas.getBufferStrategy();
		g = bs.getDrawGraphics();
		
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Resources/Icon.png")));

	}
	public void update(){
		g.drawImage(image,0, 0, canvas.getWidth(), canvas.getHeight(), null);
		bs.show();
	}
	
	public void ResizeWindow(int width, int height, float scale,boolean fullscrean)
	{
		frame.dispose();
		
		Dimension s = new Dimension((int)(width *scale),(int)(height* scale));
		canvas.setPreferredSize(s);
		canvas.setMaximumSize(s);
		canvas.setMinimumSize(s);		
		
		frame.setUndecorated(fullscrean);
		frame.pack();
		
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation((dim.width/2)-(frame.getWidth()/2), (dim.height/2)-(frame.getHeight()/2));
		frame.setVisible(true);
		
	}
	
	public BufferedImage getImage() {
		return image;
	}

	public Canvas getCanvas() {
		return canvas;
	}
	public JFrame getFrame() {
		return frame;
	}


}

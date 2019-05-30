package com.ladun.engine;

import java.awt.Dimension;
import java.awt.Toolkit;

import com.ladun.game.objects.particles.ParticleSystem;

public class GameContainer implements Runnable{
	
	
	private Thread thread;
	private Window window;
	private Renderer renderer;
	private Input input;
	private AbstractGame game;
	
	private boolean running = false;
	private static final double UPDATE_CAP = 1.0/60.0;
	private int width = 320 ,height = 240;
	private float scale = 1f;
	private String title = "JavaGame";
	

	public GameContainer(AbstractGame game)
	{
		this.game = game;
	}
	public synchronized void start(){
		
		window = new Window(this);
		renderer = new Renderer(this);
		input = new Input(this);
		
		thread = new Thread(this);
		thread.start();
	}
	
	public synchronized void stop(){
		try {
			thread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void run(){
		
		running = true;
		
		boolean render = false;
		double firstTime = 0;
		double lastTime = System.nanoTime() / 1000000000.0;
		double passedTime = 0;
		double unprocessedTime = 0;
		
		double frameTime = 0;
		int frames = 0;
		int fps = 0;
		

		game.init(this);
		
		while(running){
			//render = false;
			
			firstTime = System.nanoTime() / 1000000000.0;
			passedTime = firstTime - lastTime;
			lastTime = firstTime;
			
			unprocessedTime += passedTime;
			frameTime += passedTime;
			
			//render = false;
			
			while(unprocessedTime >= UPDATE_CAP)
			{
				unprocessedTime -= UPDATE_CAP;
				render = true;
				
				game.update(this, (float)UPDATE_CAP);
				input.update();
				
				
				if(frameTime >= 1.0){
					
					frameTime =0;
					fps = frames;
					frames = 0;
					
					System.out.println("FPS : " + fps );
					
				}
			}
			
			if(render)
			{
				
				renderer.clear();
				game.render(this, renderer);
				renderer.process();
				window.update();
				frames++;
			}
			else{
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
	
					e.printStackTrace();
				}
				
			}
		}
		
		dispose();
	}
	
	private void dispose(){
		
		
	}
	
	public static float getUpdateCap()
	{
		return (float)UPDATE_CAP;
	}
	
	public Renderer getRenderer() {
		return renderer;
	}
	public Input getInput() {
		return input;
	}
	public Window getWindow() {
		return window;
	}
	public void FullScreen()
	{
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		
		this.width = (int)screenSize.getWidth();
		this.height = (int)screenSize.getHeight();
		
		System.out.println(width + " : " + height);
		
		window.ResizeWindow(width,height,scale,true);	
	}
	
	public void ResizeWindow(int width, int height)
	{
		this.width = width;
		this.height = height;
		window.ResizeWindow(width,height,scale,false);	
	}
	public void ResizeWindow(int width, int height,float scale)
	{
		this.scale = scale;
		this.ResizeWindow(width,height);
	}	
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public float getScale() {
		return scale;
	}
	public void setScale(float scale) {
		this.scale = scale;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
}

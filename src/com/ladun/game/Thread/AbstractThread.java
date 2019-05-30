package com.ladun.game.Thread;

public abstract class AbstractThread  implements Runnable{
	
	
	protected Thread thread;
	protected boolean running;
	protected int id;
	

	public int getID() {
		return id;
	}

	public void setID(int id) {
		this.id = id;
	}

	public Thread getThread() {
		return thread;
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	
	
}

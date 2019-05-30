package com.ladun.game.Thread;

import java.util.ArrayList;

public class ThreadManager {

	
	private ArrayList<AbstractThread> threads;
	private String tag;
	
	public ThreadManager(){
		threads = new ArrayList<AbstractThread>();
	}
	
	public ThreadManager(String tag)
	{
		this();
		this.tag = tag;
	}
	
	/*
	 * Thread의 갯수가 많아졌을 때 관리하는 함수
	 * */
	
	public void start()
	{
		//System.out.println(tag+" Thread Count : " + threads.size());
		for(int i = 0; i < threads.size();i++)
		{
			threads.get(i).setRunning(true);
			threads.get(i).setID(i);
		}
	}
	
	public void AddTheard(AbstractThread thread)
	{
		threads.add(thread);
	}
	
	public void RemoveThread(int count)
	{
		for(int i = 0; i < count;i++)
		{
			threads.remove(threads.size() - 1);
		}
	}
	
	public int GetThreadSize()
	{
		return threads.size();
	}
	
	public boolean IsAllStopped()
	{
		for(int i = 0; i < threads.size();i++)
		{
			if(threads.get(i).isRunning()){
				return false;
			}
		}
		
		return true;
	}
}

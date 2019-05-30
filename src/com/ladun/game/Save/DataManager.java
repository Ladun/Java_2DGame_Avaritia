package com.ladun.game.Save;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class DataManager {

	//사용 안함
	private static class Singleton{
		static final DataManager single = new DataManager();
	}
	
	public static DataManager getSingleton()
	{
		return Singleton.single;
	}
	
	
	
	public void Save(){
		try
		{
			FileOutputStream fo = new FileOutputStream("/data.ser");
			ObjectOutputStream oo = new ObjectOutputStream(fo);
			
			oo.writeObject(new UserData());
			
			oo.close();
		}
		catch(IOException e)
		{
			
		}
	}
	
	public UserData Load(){
		try
		{
			FileInputStream fi = new FileInputStream("/data.ser");
			ObjectInputStream oi = new ObjectInputStream(fi);
			UserData ud = (UserData) oi.readObject();
			oi.close();
			
			return ud;
		}
		catch(IOException e)
		{
			
		}
		catch(ClassNotFoundException e)
		{
			
		}
		return null;
	}
}

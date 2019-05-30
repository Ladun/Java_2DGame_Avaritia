package com.ladun.game.objects.UI;

import com.ladun.engine.GameContainer;
import com.ladun.engine.Renderer;

public class CheckBoxGroup {

	private CheckBox[] checkBoxs;
	
	private int lastCheckBoxIndex;
	
	public CheckBoxGroup(CheckBox[] checkBoxs)
	{
		this.checkBoxs = checkBoxs;
		
		boolean isChecked = false;
		for(int i = 0 ; i < checkBoxs.length;i++)
		{
			if(isChecked)
				checkBoxs[i].setChecked(false);
			else
			{
				if(checkBoxs[i].isChecked()){
					isChecked = true;
					lastCheckBoxIndex = i;
				}
			}
		}
		
		if(!isChecked){
			checkBoxs[0].setChecked(true);
			lastCheckBoxIndex = 0;
		}
	}
	
	public void update(GameContainer gc, float dt)
	{
		for(int i = 0; i < checkBoxs.length;i++)
		{
			if(checkBoxs[i].isChecked())
			{
				if(i != lastCheckBoxIndex)
				{
					checkBoxs[lastCheckBoxIndex].setChecked(false);
					lastCheckBoxIndex = i;
				}
				
			}		
			
			
			
			if(checkBoxs[i].isActive())
				checkBoxs[i].update(gc, dt);
		}
	}
	
	public void render(GameContainer gc, Renderer r)
	{
		for(int i = 0; i < checkBoxs.length;i++)
		{
			if(checkBoxs[i].isActive())
				checkBoxs[i].render(gc, r);
			}
	}
	
	public boolean isLastChecked(int index)
	{
		if(index>= checkBoxs.length || index < 0)
			return false;
		
		return checkBoxs[index].isLastChecked();
	}
}

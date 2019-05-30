package com.ladun.game.UI;

import java.awt.event.KeyEvent;

import com.ladun.engine.GameContainer;
import com.ladun.engine.Renderer;
import com.ladun.engine.gfx.Font;
import com.ladun.engine.gfx.Image;
import com.ladun.engine.gfx.ImageTile;
import com.ladun.game.GameManager;
import com.ladun.game.Items.Inventory;
import com.ladun.game.Items.Item;
import com.ladun.game.Scene.InGameScene;
import com.ladun.game.objects.GameObject;
import com.ladun.game.objects.UI.Button;
import com.ladun.game.objects.creatures.AliveObject;
import com.ladun.game.objects.creatures.Player;

public class UserInterface{

	private static class Singleton{
		static final UserInterface single = new UserInterface();
	}
	
	public static UserInterface getSingleton()
	{
		return Singleton.single;
	}

	

	private PauseMenu		pauseMenu;
	private ClearFailMenu	clearfailMenu;
	//------------------------------------------------------------------------------
	private GameObject 		target;
	//Inventory---------------------------------------------------------------------
	private Inventory		inv				= new Inventory(4);
	private Image 			invSlotImage 	= new Image("/Resources/Object/UI/InvenSlot.png");
	private Image 			slotSelect	 	= new Image("/Resources/Object/UI/SlotSelect.png");
	private float 			slotOffX;
	private float 			slotBetween		= 0;
	private boolean			finish;
	//------------------------------------------------------------------------------
	private Button			observerButton;
	private boolean 		observerBoolean = false;

	
	
	public UserInterface()
	{
		this.slotOffX 		= ((invSlotImage.getW() + slotBetween) * (inv.getSize() / 2)) - slotBetween/2;
		this.pauseMenu 		= new PauseMenu(GameManager.getGc());
		this.clearfailMenu 	= new ClearFailMenu();
		
		this.observerButton 	= new Button(32,328
				,new ImageTile("/Resources/Object/UI/Button/defaultButton8.png",8,8));
	}
	
	public void update(GameContainer gc , float dt) {
		// TODO Auto-generated method stub
		if(GameManager.getSingleton().getCurrentScene() instanceof InGameScene){
			InGameScene inGameScene = (InGameScene)(GameManager.getSingleton().getCurrentScene());
			
			if(GameManager.getSingleton().isFailded() || GameManager.getSingleton().isClear()){
				if(!finish){
					inGameScene.SetPause();
					finish = true;
				}
			}
	
			this.target 	= inGameScene.getCamera().getTarget();
			if(gc.getInput().isKeyDown(KeyEvent.VK_ESCAPE))
			{
				inGameScene.SetPause();
			}
			
			if(inGameScene.isPause())
			{
				if(!GameManager.getSingleton().isFailded() && !GameManager.getSingleton().isClear())
					pauseMenu.update(gc, dt);
				else{
					clearfailMenu.update(gc, dt);
				}
			}
			
			
			if(observerButton.isReleased()){
				if(!observerBoolean){
					GameManager.getSingleton().getObject("observer").setActive(true);
					GameManager.getSingleton().getObject("player").setActive(false);
					inGameScene.getCamera().setTargetTag("observer");
					observerBoolean = true;
				}
				else{
					GameManager.getSingleton().getObject("player").setActive(true);
					GameManager.getSingleton().getObject("observer").setActive(false);
					inGameScene.getCamera().setTargetTag("player");
					observerBoolean = false;
				}
			}
			
			observerButton.update(gc, dt);
		}
	}

	public void render(GameContainer gc, Renderer r) {
		//magazine View -----------------------------------------------------------------------------------------
		if(target instanceof Player){
			if(target != null){
				if(((Player)target).getCurrentGun() != null){
					r.drawUIText(((Player)target).getCurrentGun().getName()
							, (int)(gc.getWidth() - 96)
							, (int)(gc.getHeight() - 58)
							, 0xffa3a3a3, Font.STANDARD);
					r.drawUIText(String.valueOf(((Player)target).getCurrentGun().getCurBulletCount())
							, (int)(gc.getWidth() - 96)
							, (int)(gc.getHeight() - 48), 0xffa3a3a3
							,Font.STANDARD_16);
					r.drawUIFillRect((int)(gc.getWidth() - 64), (int)(gc.getHeight() - 48), 2, 20, 0xffa3a3a3);
					r.drawUIText(String.valueOf(inv.getBulletsCount(((Player)target).getCurrentGun().getID()))
							, (int)(gc.getWidth() - 56)
							, (int)(gc.getHeight() - 48), 0xffa3a3a3
							,Font.STANDARD_16);
				}
				//------------------------------------------------------------------------------------------------------
				int HPBarWidth =  (int)(64 * (((Player)target).getHealth() / ((Player)target).getMaxHealth()));
				r.drawUIFillRect((int)(gc.getWidth() - 96) + 64 -  HPBarWidth, (int)(gc.getHeight() - 24), HPBarWidth, 2, 0xffdc6666);
				int STBarWidth =  (int)(64 * (((Player)target).getCurStamina() / ((Player)target).getMaxStamina()));
				r.drawUIFillRect((int)(gc.getWidth() - 96) + 64 - (STBarWidth), (int)(gc.getHeight() - 20), STBarWidth, 2, 0xff6bdc66);
			}
		}
		
		//Slot Render-----------------------------------------------------------------------------------------
		for(int i = 0 ; i < inv.getSize();i++)
		{
			r.drawUIImage(invSlotImage, (int)((gc.getWidth() / 2 - slotOffX) + (invSlotImage.getW() + slotBetween) * i)
					, (int)(gc.getHeight() - 48));
		}
		for(int i = 0 ; i < inv.getSize();i++)
		{
			if(inv.getItem(i) != null){
				r.drawUIImage(inv.getItem(i).getInvImage()
						, (int)((gc.getWidth() / 2 - slotOffX) + (invSlotImage.getW() + slotBetween) * i)
						, (int)(gc.getHeight() - 48));
			}
		}
		r.drawUIImage(slotSelect
				, (int)((gc.getWidth() / 2 - slotOffX) + (invSlotImage.getW() + slotBetween) * inv.getCurrentSlot())
				, (int)(gc.getHeight() - 48));


		//------------------------------------------------------------------------------------------------------
		if(((InGameScene)(GameManager.getSingleton().getCurrentScene())).isPause())
		{
			if(!GameManager.getSingleton().isFailded() && !GameManager.getSingleton().isClear())
				pauseMenu.render(gc, r);
			else{
				clearfailMenu.render(gc, r);
			}
		}
		
		
		observerButton.render(gc, r);
	
	}

	public void SwitchItem()
	{
		inv.setCurrentSlot();
	}
	
	public void SwitchGun()
	{
		inv.setCurrentGun();
		if(target instanceof Player)
			((Player)target).setCurrentGun(inv.getGun(inv.getCurrentGun()));
	}
	
	public void ItemUse(AliveObject target){
		inv.UseItem(target);
		
	}
	
	public Inventory getInv() {
		return inv;
	}
	public Item	getCurrentItem()
	{
		return inv.getItem(inv.getCurrentSlot());
	}
	
	
}

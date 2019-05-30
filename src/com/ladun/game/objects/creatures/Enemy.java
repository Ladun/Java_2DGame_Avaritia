package com.ladun.game.objects.creatures;

import com.ladun.game.objects.GameObject;

public abstract class Enemy extends AliveObject{

	protected int damage;
	
	protected boolean 	readyToAttack;
	public Enemy(int tileX,int tileY, float health,int damage) {
		super(tileX,tileY, health);
		this.damage 	= damage;
		// TODO Auto-generated constructor stub
	}

	public int getDamage() {
		return damage;
	}
	public abstract void Hit(int damage);

	
	@Override
	public void collision(GameObject other)
	{
		super.collision(other);
		
		if(collisionDetected)
			return;
		
		if(other instanceof AttackBox)
		{
			if(((AttackBox)other).getParent() != this)
			{
				if(((AttackBox)other).getParent() instanceof Player)
				{
					Player 	parent	= (Player)((AttackBox)other).getParent();
					
					if(parent.isAttacking())
					{
						Hit(1);
					}
				}
			}
		}
		else collisionDetected = false;
	}
	
	

}

package com.jmc.jisuucc.entity.api;

import com.jmc.jisuucc.event.api.CollisionEvent;
import com.jmc.jisuucc.event.api.Direction;

public interface Mobile {
	
	public void move(Direction direction);
	public void moveTo(int x, int y);
	public void onCollision(CollisionEvent event);

}

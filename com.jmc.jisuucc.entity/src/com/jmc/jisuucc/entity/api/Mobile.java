package com.jmc.jisuucc.entity.api;

import java.awt.Point;

import com.jmc.jisuucc.event.api.CollisionEvent;
import com.jmc.jisuucc.event.api.Direction;

public interface Mobile {
	
	public void move(Direction direction);
	public void moveTo(Point p);
	public void onCollision(CollisionEvent event);
	
}

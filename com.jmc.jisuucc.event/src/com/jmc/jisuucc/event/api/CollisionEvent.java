package com.jmc.jisuucc.event.api;

import java.awt.Rectangle;

public class CollisionEvent {
	
	public enum CollisionEntity {
		MAP,
		ISUUCC,
		ENEMY
	}
	
	public final CollisionEntity what;
	public final Object who;
	public final Rectangle box;
	public final Direction direction;
	
	public CollisionEvent(CollisionEntity what, Object who, Rectangle box, Direction direction) {
		this.what = what;
		this.who = who;
		this.box = box;
		this.direction = direction;
	}
}

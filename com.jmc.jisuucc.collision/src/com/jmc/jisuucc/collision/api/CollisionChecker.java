package com.jmc.jisuucc.collision.api;

import com.jmc.jisuucc.entity.api.Entity;
import com.jmc.jisuucc.event.api.Direction;
import com.jmc.jisuucc.map.api.Map;

public interface CollisionChecker {
	
	public boolean checkCollision(Entity entity, Map map, Direction direction);

}

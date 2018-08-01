package com.jmc.jisuucc.collision.api;

import com.jmc.jisuucc.entity.api.Entity;
import com.jmc.jisuucc.entity.api.Mobile.Direction;
import com.jmc.jisuucc.map.api.Map;

public interface CollisionChecker {
	
	public boolean checkCollisionMap(Entity entity, Map map, Direction direction);

}

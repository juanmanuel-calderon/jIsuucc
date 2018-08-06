package com.jmc.jisuucc.entity.api;

import com.jmc.jisuucc.event.api.Direction;

public interface Tear extends Entity, Mobile {
	public double dmg();
	public double speed();
	public Direction direction();
}

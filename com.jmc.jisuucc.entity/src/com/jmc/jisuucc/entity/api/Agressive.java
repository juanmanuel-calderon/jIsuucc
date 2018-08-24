package com.jmc.jisuucc.entity.api;

import java.awt.Point;
import java.util.Optional;

import com.jmc.jisuucc.event.api.Direction;

public interface Agressive {
	public Optional<Tear> attack(Direction direction);
	public Optional<Tear> attack(Point point); 
}

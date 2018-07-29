package com.jmc.jisuucc.entity.api;

public interface Mobile {
	
	public enum Direction {
		UP,
		DOWN,
		LEFT,
		RIGHT
	}
	
	public void move(Direction direction);
	public void moveTo(int x, int y);

}

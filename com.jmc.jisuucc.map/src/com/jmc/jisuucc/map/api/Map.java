package com.jmc.jisuucc.map.api;

public interface Map {
	
	public int width();
	public int height();
	public Tile[][] tiles();
	public Tile tileAtCoord(int x, int y);
}

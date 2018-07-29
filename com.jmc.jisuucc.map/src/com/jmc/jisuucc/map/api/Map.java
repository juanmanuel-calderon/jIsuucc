package com.jmc.jisuucc.map.api;

import com.jmc.jisuucc.render.api.Renderable;

public interface Map extends Renderable {
	
	public String name();
	public int width();
	public int height();
	public Tile[][] tiles();
	public Tile tileAtCoord(int x, int y);
}

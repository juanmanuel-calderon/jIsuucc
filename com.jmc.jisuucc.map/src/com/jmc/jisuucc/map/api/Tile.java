package com.jmc.jisuucc.map.api;

import java.io.File;

import com.jmc.jisuucc.render.api.Texture;

public interface Tile {
	
	public static int TILE_SIZE = 32;
	public static String TILE_DEFINITION_FILENAME = "assets" + File.separator + "tiles.txt";
	
	public int id();
	public String description();
	public boolean hasCollision();
	public Texture texture();
	public void setTexture(Texture texture);
}

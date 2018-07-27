package com.jmc.jisuucc.map.impl;

import java.io.File;

import com.jmc.jisuucc.map.api.Tile;
import com.jmc.jisuucc.render.api.Texture;

public class TileImpl implements Tile {
	
	static String TILES_PNG = "assets" + File.separator + "tiles.png";
	
	private int id;
	private String description;
	private boolean hasCollision;
	private Texture texture;
	
	TileImpl(int x, int y, String description, boolean hasCollision) {
		this.id = x * MapImpl.TILE_COLUMNS + y;
		this.description = description;
		this.hasCollision = hasCollision;
	}

	@Override
	public int id() {
		return id;
	}

	@Override
	public String description() {
		return description;
	}

	@Override
	public boolean hasCollision() {
		return hasCollision;
	}

	@Override
	public Texture texture() {
		return texture;
	}
	
	@Override
	public void setTexture(Texture texture) {
		this.texture = texture;
	}

	@Override
	public String toString() {
		return "Tile [description=" + description + "]";
	}
	
}

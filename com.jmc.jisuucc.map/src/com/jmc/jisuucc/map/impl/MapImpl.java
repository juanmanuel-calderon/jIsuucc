package com.jmc.jisuucc.map.impl;

import com.jmc.jisuucc.map.api.Map;
import com.jmc.jisuucc.map.api.Tile;

public class MapImpl implements Map {
	
	static int TILE_COLUMNS = 3;
	
	private int width;
	private int height;
	private Tile[][] tiles;
	
	public MapImpl(int width, int height, Tile[][] tiles) {
		this.width = width;
		this.height = height;
		this.tiles = tiles;
	}

	@Override
	public int width() {
		return width;
	}

	@Override
	public int height() {
		return height;
	}

	@Override
	public Tile[][] tiles() {
		return tiles;
	}
	
	@Override
	public Tile tileAtCoord(int x, int y) {
		x = x / Tile.TILE_SIZE;
		y = y / Tile.TILE_SIZE;
		assert(x > 0 && x < tiles.length);
		assert(y > 0 && y < tiles[0].length);
		return tiles[x][y];
	}
	
	public void render() {
		System.out.println("Render map");
	}

}
